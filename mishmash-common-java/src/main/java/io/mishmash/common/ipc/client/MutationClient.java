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
import io.mishmash.common.ipc.proto.MutationMessages;
import io.mishmash.common.ipc.proto.Yields;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetup;
import io.mishmash.common.rpc.MishmashRpc.MutationClientMessage;
import io.mishmash.common.rpc.MishmashRpc.MutationServerMessage;

/**
 * A {@link BaseClient} for a GRPC mutation.
 */
public class MutationClient extends BaseClient<
            MutationServerMessage,
            MutationClientMessage> {

    /**
     * A future that completes on SetupAck or failure
     * to open the mutation.
     */
    private CompletableFuture<Void> openFuture;

    /**
     * A future that completes when next {@link BaseDataPoint} is available.
     */
    private CompletableFuture<BaseDataPoint<Key, Value>> yieldFuture;

    /**
     * A future that completes on YieldAck from the server.
     */
    private CompletableFuture<Void> yieldAckFuture;

    /**
     * {@inheritDoc}
     */
    @Override
    public MutationClientMessage
            getSetupMessage(final MishmashSetup.Builder setup) {
        return MutationMessages.clientSetup(0, setup).build();
    }

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
        initState(new MutationSetup());

        return openFuture;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void>
            accept(final BaseDataPoint<Key, Value> dataPoint) {
        if (yieldFuture.isDone()) {
            throw new IllegalStateException();
        }

        yieldAckFuture = new CompletableFuture<>();
        yieldFuture.complete(dataPoint);
        return yieldAckFuture;
    }

    /**
     * Sends the GRPC MishmashSetup to the server.
     */
    public class MutationSetup extends SendMutateSetupState<MutationMishmash> {

        /**
         * {@inheritDoc}
         */
        @Override
        public MutationClientMessage getSetupMessage() {
            return MutationMessages
                    .clientSetup(0, MishmashSetup.newBuilder())
                    .build();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void error(final Throwable t) {
            super.error(t);
            openFuture.completeExceptionally(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public GrpcStreamState<MutationServerMessage, MutationClientMessage>
                prepareNext() {
            return new WaitSetupAck();
        }
    }

    /**
     * Waits for the SetupAck and resolves the open() future, so that
     * users of this mutation can know when it's okay to start yielding
     * data.
     */
    public class WaitSetupAck
            extends WaitMutateSetupAckState<MutationMishmash> {

        /**
         * {@inheritDoc}
         */
        @Override
        public void input(final MutationServerMessage input)
                throws Exception {
            super.input(input);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void error(final Throwable t) {
            super.error(t);
            openFuture.completeExceptionally(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public GrpcStreamState<MutationServerMessage, MutationClientMessage>
                prepareNext() {
            return new AcceptState();
        }
    }

    /**
     * A {@link GrpcStreamState} that yields {@link BaseDataPoint}s
     * to the server.
     */
    public class AcceptState implements GrpcStreamState<
                MutationServerMessage,
                MutationClientMessage> {

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
        public void validateInput(final MutationServerMessage input)
                throws Exception {
            // TODO Auto-generated method stub

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void input(final MutationServerMessage input)
                throws Exception {
            // TODO Auto-generated method stub
            // must be acks
            yieldAckFuture.complete(null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public GrpcStreamState<MutationServerMessage, MutationClientMessage>
                leave() {
            // TODO Auto-generated method stub
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public CompletableFuture<MutationClientMessage>
                output(final AtomicInteger currentSeqNo) {
            CompletableFuture<MutationClientMessage> res =
                    new CompletableFuture<>();

            yieldFuture.whenComplete((dp, t) -> {
                if (t != null) {
                    res.completeExceptionally(t);
                } else {
                    try {
                        res.complete(
                                MutationMessages
                                    .clientYield(
                                            currentSeqNo.getAndIncrement(),
                                            Yields.yield(dp))
                                    .build());
                        yieldFuture = new CompletableFuture<>();
                    } catch (Exception e) {
                        res.completeExceptionally(e);
                    }
                }
            });

            return res;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void error(final Throwable t) {
            yieldAckFuture.completeExceptionally(t);
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
