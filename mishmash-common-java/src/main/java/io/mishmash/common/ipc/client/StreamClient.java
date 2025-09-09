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
package io.mishmash.common.ipc.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import io.mishmash.common.data.BaseDataPoint;
import io.mishmash.common.data.Key;
import io.mishmash.common.data.Value;
import io.mishmash.common.ipc.GrpcStreamState;
import io.mishmash.common.ipc.proto.StreamMessages;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetup;
import io.mishmash.common.rpc.MishmashRpc.StreamClientMessage;
import io.mishmash.common.rpc.MishmashRpc.StreamServerMessage;

/**
 * A {@link BaseClient} for a GRPC stream.
 */
public class StreamClient
    extends BaseClient<StreamServerMessage, StreamClientMessage> {

    /**
     * A future that completes on SetupAck or failure to open the stream.
     */
    private CompletableFuture<Void> openFuture;

    /**
     * A future that completes when next {@link BaseDataPoint} is available.
     */
    private CompletableFuture<BaseDataPoint<Key, Value>> yieldFuture;

    /*
     * A future that completes on YieldAck from the server.
     */
    // TODO
    //private CompletableFuture<Void> yieldAckFuture;

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> open() {

        if (openFuture != null) {
            throw new IllegalStateException("Mishmash client already open");
        }

        openFuture = new CompletableFuture<>();
        yieldFuture = new CompletableFuture<>();
        initState(new StreamSetup());

        return openFuture;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StreamClientMessage getSetupMessage(
            final MishmashSetup.Builder setup) {

        return StreamMessages.clientSetup(0, setup).build();
    }

    /**
     * Throws an error, streams do not support this method.
     *
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> accept(
            final BaseDataPoint<Key, Value> output) {

        throw new IllegalStateException("Stream client cannot accept data");
    }

    /**
     * Sends the GRPC MishmashSetup to the server.
     */
    public class StreamSetup extends SendStreamSetupState<StreamMishmash> {

        /**
         * {@inheritDoc}
         */
        @Override
        public StreamClientMessage getSetupMessage() {
            return StreamMessages.clientSetup(0, MishmashSetup.newBuilder())
                    .build();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public GrpcStreamState<StreamServerMessage, StreamClientMessage>
                prepareNext() {

            return new WaitSetupAck();
        }

    }

    /**
     * Waits for the SetupAck and resolves the open() future,
     * so that users of this stream can know when it's okay
     * to start yielding data.
     */
    public class WaitSetupAck extends WaitStreamSetupAckState<StreamMishmash> {

        /**
         * {@inheritDoc}
         */
        @Override
        public GrpcStreamState<StreamServerMessage, StreamClientMessage>
                prepareNext() {

            return new AcceptState();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void error(final Throwable t) {
            super.error(t);
            openFuture.completeExceptionally(t);
        }

    }

    /**
     * A {@link GrpcStreamState} that yields {@link BaseDataPoint}s
     * from the server.
     */
    public class AcceptState implements
            GrpcStreamState<StreamServerMessage, StreamClientMessage> {

        /**
         * {@inheritDoc}
         */
        @Override
        public CompletableFuture<Void> enter() {
            openFuture.complete(null);
            return CompletableFuture.completedFuture(null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void validateInput(final StreamServerMessage input)
                throws Exception {

            // TODO Auto-generated method stub

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void input(final StreamServerMessage input) throws Exception {
            // TODO Auto-generated method stub

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public
            GrpcStreamState<StreamServerMessage, StreamClientMessage> leave() {
            // TODO Auto-generated method stub
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public CompletableFuture<StreamClientMessage>
            output(final AtomicInteger currentSeqNo) {

            // TODO Auto-generated method stub
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void error(final Throwable t) {
            // TODO Auto-generated method stub

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws Exception {
            // TODO Auto-generated method stub

        }

    }

}
