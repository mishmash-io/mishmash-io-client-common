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
package io.mishmash.common.ipc.proto;

import io.mishmash.common.rpc.MishmashRpc;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetup;
import io.mishmash.common.rpc.MishmashRpc.MutationClientMessage;
import io.mishmash.common.rpc.MishmashRpc.MutationServerMessage;
import io.mishmash.common.rpc.MishmashRpc.SetupAck;
import io.mishmash.common.rpc.MishmashRpc.YieldData;

/**
 * Helper methods to build Mutation GRPC messages.
 *
 */
public final class MutationMessages {

    /**
     * The MutationMessages class should not be instantiated.
     */
    private MutationMessages() {
        // Hide the constructor
    }

    /**
     * Build a MutationClientMessage.
     *
     * @param clientSeqNo - the client sequence number
     * @return Builder - the Builder
     */
    public static MutationClientMessage.Builder client(final int clientSeqNo) {
        return MutationClientMessage.newBuilder()
                .setClientSeqNo(clientSeqNo);
    }

    /**
     * Build a client Setup message.
     *
     * @param clientSeqNo - the client sequence number
     * @param setup - the target
     * @return Builder - the Builder
     */
    public static MutationClientMessage.Builder
            clientSetup(final int clientSeqNo,
                    final MishmashSetup.Builder setup) {
        return client(clientSeqNo)
                .setSetup(setup);
    }

    /**
     * Build a YieldData client message.
     *
     * @param clientSeqNo - the client sequence number
     * @param yield - the data
     * @return Builder - the Builder
     */
    public static MutationClientMessage.Builder
            clientYield(final int clientSeqNo, final YieldData.Builder yield) {
        return client(clientSeqNo)
                .setYieldData(yield);
    }

    /**
     * Build an Error client message.
     *
     * @param clientSeqNo - the client sequence number
     * @param error - the error
     * @return Builder - the Builder
     */
    public static MutationClientMessage.Builder
            clientError(final int clientSeqNo,
                    final MishmashRpc.Error.Builder error) {
        return client(clientSeqNo)
                .setError(error);
    }

    /**
     * Build an Error client message from a {@link PeerError}.
     *
     * @param clientSeqNo - the client sequence number
     * @param error - the error
     * @return Builder - the Builder
     */
    public static MutationClientMessage.Builder
            clientError(final int clientSeqNo, final PeerError error) {
        return clientError(clientSeqNo, Errors.error(error));
    }

    /**
     * Build a MutationServerMessage.
     *
     * @param serverSeqNo - the server sequence number
     * @return Builder - the Builder
     */
    public static MutationServerMessage.Builder server(final int serverSeqNo) {
        return MutationServerMessage.newBuilder()
                .setServerSeqNo(serverSeqNo);
    }

    /**
     * Build a SetupAck.
     *
     * @param serverSeqNo - the server sequence number
     * @param clientSeqNo - the client sequence number being acknowledged
     * @return Builder - the Builder
     */
    public static MutationServerMessage.Builder
            serverSetupAck(final int serverSeqNo, final int clientSeqNo) {
        return server(serverSeqNo)
                .setSetupAck(SetupAck.newBuilder());
    }

    /**
     * Build a YieldDataAck.
     *
     * @param serverSeqNo - the server sequence number
     * @param clientSeqNo - the client sequence number being acknowledged
     * @return Builder - the Builder
     */
    public static MutationServerMessage.Builder
            serverYieldAck(final int serverSeqNo, final int clientSeqNo) {
        return server(serverSeqNo)
                .setAck(Yields.yieldDataAck());
    }

    /**
     * Build an Error server message.
     *
     * @param serverSeqNo - the server sequence number
     * @param error - the error
     * @return Builder - the Builder
     */
    public static MutationServerMessage.Builder
            serverError(final int serverSeqNo,
                    final MishmashRpc.Error.Builder error) {
        return server(serverSeqNo)
                .setError(error);
    }

    /**
     * Build an Error server message from a {@link PeerError}.
     *
     * @param serverSeqNo - the server sequence number
     * @param error - the peer error
     * @return Builder - the Builder
     */
    public static MutationServerMessage.Builder
            serverError(final int serverSeqNo, final PeerError error) {
        return serverError(serverSeqNo, Errors.error(error));
    }
}
