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
package io.mishmash.common.ipc.client;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import io.grpc.stub.StreamObserver;
import io.mishmash.common.data.Key;
import io.mishmash.common.data.Mishmash;
import io.mishmash.common.data.Value;

/**
 * A base class for client mishmashes.
 *
 * @param <I> - the input GRPC message type
 * @param <O> - the output GRPC message type
 * @param <CT> - the {@link BaseClient} implementation class
 */
public abstract class ClientMishmash<I, O, CT extends BaseClient<I, O>>
        implements Mishmash<Key, Value> {

    /**
     * The GRPC client.
     */
    private CT grpcClient;

    /**
     * Returns the remote peer instance.
     */
    private Function<CT, StreamObserver<O>> remotePeerGetter;

    /**
     * Create a client mishmash.
     *
     * @param setup - the Setup GRPC message
     * @param client - the GRPC client
     * @param remotePeer - a function that will supply the remote peer
     */
    public ClientMishmash(final O setup, final CT client,
            final Function<CT, StreamObserver<O>> remotePeer) {
        this.grpcClient = client;
        this.remotePeerGetter = remotePeer;
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws Exception {
        grpcClient.close();
    }

    /**
     * {@inheritDoc}
     */
    public CompletableFuture<Void> open() {
        return openAsync();
    }

    /**
     * Get the GRPC client.
     *
     * @return - the GRPC client
     */
    public CT getClient() {
        return grpcClient;
    }

    /**
     * Begins the open procedure.
     *
     * @return - a future that completes on success or error
     */
    protected CompletableFuture<Void> openAsync() {
        grpcClient.setRemotePeer(remotePeerGetter.apply(grpcClient));
        return grpcClient.open();
    }
}
