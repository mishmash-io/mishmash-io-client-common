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
import io.mishmash.common.exception.MishmashInvalidStateException;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetup;
import io.mishmash.common.rpc.MishmashRpc.StreamClientMessage;
import io.mishmash.common.rpc.MishmashRpc.StreamServerMessage;
import io.mishmash.common.rpc.MishmashServiceGrpc.MishmashServiceStub;

/**
 * A {@link ClientMishmash} for a stream.
 *
 */
public class StreamMishmash extends
        ClientMishmash<
            StreamServerMessage,
            StreamClientMessage,
            StreamClient> {

    /**
     * Create a new {@link ClientMishmash} for a stream.
     *
     * @param stub - the GRPC service stub to use
     * @param setup - the GRPC MishmashSetup message
     * @param client - the {@link StreamClient}
     */
    public StreamMishmash(
            final MishmashServiceStub stub,
            final MishmashSetup.Builder setup,
            final StreamClient client) {
        super(client.getSetupMessage(setup), client, stub::stream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<? extends BaseDataPoint<Key, Value>> get() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns a future that was completed with an error.
     * A StreamMishmash does not implement this method.
     *
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> put(
            final BaseDataPoint<Key, Value> dataPoint) {
        return CompletableFuture
                .failedFuture(new MishmashInvalidStateException());
    }

}
