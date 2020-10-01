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
import io.mishmash.common.data.BaseDataPoint;
import io.mishmash.common.data.Key;
import io.mishmash.common.data.Value;
import io.mishmash.common.exception.MishmashInvalidStateException;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetup;
import io.mishmash.common.rpc.MishmashRpc.MutationClientMessage;
import io.mishmash.common.rpc.MishmashRpc.MutationServerMessage;
import io.mishmash.common.rpc.MishmashServiceGrpc.MishmashServiceStub;

/**
 * A {@link ClientMishmash} for a mutation.
 *
 */
public class MutationMishmash extends
        ClientMishmash<
            MutationServerMessage,
            MutationClientMessage,
            MutationClient> {

    /**
     * Create a new {@link ClientMishmash} for a mutation.
     *
     * @param stub - the GRPC service stub to use
     * @param setup - the GRPC MishmashSetup message
     * @param client - the {@link MutationClient}
     */
    public MutationMishmash(
            final MishmashServiceStub stub,
            final MishmashSetup.Builder setup,
            final MutationClient client) {
        super(client.getSetupMessage(setup), client, stub::mutate);
    }

    /**
     * Returns a future that was completed with an error.
     * A MutationMishmash does not implement this method.
     *
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<? extends BaseDataPoint<Key, Value>> get() {
        return CompletableFuture
                .failedFuture(new MishmashInvalidStateException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> put(
            final BaseDataPoint<Key, Value> dataPoint) {
        return getClient().accept(dataPoint);
    }

}
