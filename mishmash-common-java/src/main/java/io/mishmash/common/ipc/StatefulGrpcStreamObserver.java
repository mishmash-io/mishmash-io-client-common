/*
 * Copyright 2020 MISHMASH I O OOD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mishmash.common.ipc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import io.grpc.stub.StreamObserver;

/**
 * A GRPC Stream Observer with states.
 *
 * @param <I> - The input message type
 * @param <O> - The output message type
 */
public class StatefulGrpcStreamObserver<I, O> implements StreamObserver<I> {

    /**
     * The GRPC connection peer.
     */
    private StreamObserver<O> peer;

    /**
     * The current StreamState.
     */
    private GrpcStreamState<I, O> currentState;

    /**
     * A future waiting on the next output message.
     */
    private CompletableFuture<Void> outputFuture;

    /**
     * The local message sequence number.
     */
    private AtomicInteger localSeqNo = new AtomicInteger(0);

    /**
     * Create a StatefulStreamObserver.
     *
     * @param remotePeer - the GRPC connection peer
     */
    public StatefulGrpcStreamObserver(final StreamObserver<O> remotePeer) {
        this.peer = remotePeer;
    }

    /**
     * Create a StatefulStreamObserver without a remote peer.
     * It must be set later.
     */
    public StatefulGrpcStreamObserver() {
        // empty
    }

    /**
     * Get the remote GRPC peer.
     *
     * @return - the remote peer
     */
    public StreamObserver<O> getRemotePeer() {
        return peer;
    }

    /**
     * Set the remote GRPC peer.
     *
     * @param remotePeer - the remote peer
     */
    public void setRemotePeer(final StreamObserver<O> remotePeer) {
        this.peer = remotePeer;
    }

    /**
     * Set the given state as current state and begin work with it.
     *
     * @param state - the new state
     */
    protected void initState(final GrpcStreamState<I, O> state) {
        if (outputFuture != null) {
            if (!outputFuture.isDone()) {
                outputFuture.cancel(true);
            }

            outputFuture = null;
        }

        currentState = state;
        currentState.enter()
            .thenAcceptAsync(this::asyncOutput)
            .exceptionally(this::closeWithError);
    }

    /**
     * Handle the next input message.
     *
     * @param value - the message
     */
    @Override
    public void onNext(final I value) {
        try {
            currentState.validateInput(value);

            currentState.input(value);
        } catch (Exception e) {
            closeWithError(e);
        }
    }

    /**
     * Handle an error from the network.
     *
     * @param t - the error thrown
     */
    @Override
    public void onError(final Throwable t) {
        if (currentState != null) {
            currentState.error(t);
        }

        onCompleted();
    }

    /**
     * Handle GRPC stream close.
     */
    @Override
    public void onCompleted() {
        peer.onCompleted();

        handleClose();
    }

    /**
     * Notify remote peer of an error and close connection.
     *
     * @param t - the error thrown
     * @return - null
     */
    protected Void closeWithError(final Throwable t) {
        peer.onError(t);

        currentState.error(t);

        handleClose();

        return null;
    }

    /**
     * Installs code waiting to process the next output message.
     *
     * @param v - null
     */
    protected void asyncOutput(final Void v) {
        outputFuture = currentState.output(localSeqNo)
                .thenAccept(this::output)
                .exceptionally(this::closeWithError);
    }

    /**
     * Sends the next output message (called when available).
     *
     * @param output - next output message
     */
    protected void output(final O output) {
        if (output == null) {
            // current state is exhausted
            GrpcStreamState<I, O> nextState = currentState.leave();
            if (nextState == null) {
                currentState = null;
                // this was the last state
                peer.onCompleted();
                handleClose();
            } else {
                initState(nextState);
            }
        } else {
            peer.onNext(output);
            // wait for next output
            asyncOutput(null);
        }
    }

    /**
     * Releases any resources held.
     */
    protected void handleClose() {
        if (currentState != null) {
            try {
                currentState.close();
            } catch (Exception e) {
                // ignore
            }

            currentState = null;
        }
    }
}
