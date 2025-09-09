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

import io.grpc.ManagedChannel;
import io.mishmash.common.exception.MishmashException;
import io.mishmash.common.rpc.MishmashRpc.StreamClientMessage;
import io.mishmash.common.rpc.MishmashRpc.StreamServerMessage;
import io.mishmash.common.rpc.MishmashServiceGrpc;

/**
 * A {@link ClientMishmashBuilder} for a stream.
 */
public class StreamBuilder extends ClientMishmashBuilder<
        StreamServerMessage,
        StreamClientMessage,
        StreamClient,
        StreamMishmash> {

    /**
     * The GRPC communications channel.
     */
    private ManagedChannel grpcChannel;

    /**
     * Create a new StreamBuilder.
     *
     * @param channel - the GRPC network channel
     */
    public StreamBuilder(final ManagedChannel channel) {
        this.grpcChannel = channel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StreamMishmash build() throws MishmashException {
        return new StreamMishmash(MishmashServiceGrpc.newStub(
                grpcChannel),
                getBuilder(),
                new StreamClient());
    }

}
