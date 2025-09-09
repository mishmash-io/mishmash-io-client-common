/*
 *    Copyright 2025 Mishmash IO UK Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package io.mishmash.common.ipc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An interface representing a single state for a
 * {@link StatefulGrpcStreamObserver}.
 *
 * @param <I> - Input message type
 * @param <O> - Output message type
 */
public interface GrpcStreamState<I, O> extends AutoCloseable {

    /**
     * Enter the state (initialize). During enter() (that is until the returned
     * Future completes) the state will receive messages through its input()
     * method, but it will not be allowed to send messages to the remote
     * peer. This only happens after the Future resolves.
     *
     * @return - a Future that will be completed when the state is initialized
     */
    CompletableFuture<Void> enter();

    /**
     * Validate the next incoming input.
     *
     * @param input - the input message
     * @throws Exception - if checks fail
     */
    void validateInput(I input) throws Exception;

    /**
     * Process the next input message.
     *
     * @param input - the input message
     * @throws Exception - if processing fails
     */
    void input(I input) throws Exception;

    /**
     * LEAVE this state. Return a new state, or null
     * if this was the last state and the stream should be closed.
     *
     * @return - the new state or null
     */
    GrpcStreamState<I, O> leave();

    /**
     * Get a Future that completes when next output message is available. Or
     * when this state has no more data to send, in which case the
     * Future will be completed with a null message.
     *
     * currentSeqNo can be incremented if needed (when sending requests) -
     * it is the stream local-end sequence number.
     *
     * @param currentSeqNo - the current stream sequence number
     * @return - A Future that completes on next output message
     */
    CompletableFuture<O> output(AtomicInteger currentSeqNo);

    /**
     * Called when the GRPC stream is about to be closed because an
     * error in communication with the peer was encontered.
     *
     * @param t - the observed error
     */
    void error(Throwable t);

    /**
     * Called on GRPC stream is closed (for whatever reason). It is
     * NOT called when leaving this state, even if this is the final
     * state.
     *
     * @throws Exception - if an error occurs
     */
    void close() throws Exception;

}
