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

import io.mishmash.common.data.BaseDataPoint;
import io.mishmash.common.data.Key;
import io.mishmash.common.data.Value;
import io.mishmash.common.ipc.StatefulGrpcStreamObserver;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetup;

/**
 * A base implementation for a mishmash io client.
 *
 * @param <I> - the input GRPC message type
 * @param <O> - the output GRPC message type
 */
public abstract class BaseClient<I, O>
        extends StatefulGrpcStreamObserver<I, O>
        implements AutoCloseable {

    /**
     * Open the client.
     *
     * @return - A completable future that will complete when open or on error
     */
    public abstract CompletableFuture<Void> open();

    /**
     * Build the Setup GRPC message.
     *
     * @param setup - a MishmashSetup that has been built
     * @return - the proper GRPC Setup message
     */
    public abstract O getSetupMessage(MishmashSetup.Builder setup);

    /**
     * Accepts and stores a {@link BaseDataPoint}.
     *
     * @param output - the {@link BaseDataPoint} to be stored
     * @return - a completable future that will complete on success or on error
     */
    public abstract CompletableFuture<Void>
            accept(BaseDataPoint<Key, Value> output);

    /**
     * Close the client.
     *
     * @throws Exception - on close failure
     */
    public void close() throws Exception {
        // FIXME:
        this.getRemotePeer().onCompleted();
    }
}
