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

import java.util.Arrays;

import io.mishmash.common.rpc.MishmashRpc;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetup;
import io.mishmash.common.rpc.MishmashRpc.SetupAck;
import io.mishmash.common.rpc.MishmashRpc.StreamClientMessage;
import io.mishmash.common.rpc.MishmashRpc.StreamServerMessage;
import io.mishmash.common.rpc.MishmashRpc.Value;
import io.mishmash.common.rpc.MishmashRpc.YieldData;

/**
 * Helper methods to build Stream GRPC messages.
 *
 */
public final class StreamMessages {

    /**
     * The StreamMessages class should not be instantiated.
     */
    private StreamMessages() {
        // Hide constructor
    }

    /**
     * Return a StreamClientMessage Builder.
     *
     * @param clientSeqNo - the client sequence number
     * @return Builder - the Builder
     */
    public static StreamClientMessage.Builder client(final int clientSeqNo) {
        return StreamClientMessage.newBuilder()
                .setClientSeqNo(clientSeqNo);
    }

    /**
     * Build a client Setup message.
     *
     * @param clientSeqNo - the client sequence number
     * @param setup - the target
     * @return Builder - the Builder
     */
    public static StreamClientMessage.Builder
            clientSetup(final int clientSeqNo,
                    final MishmashSetup.Builder setup) {
        return client(clientSeqNo)
                .setSetup(setup);
    }

    /**
     * Build a client YieldAck.
     *
     * @param clientSeqNo - the client sequence number
     * @param serverSeqNo - the server sequence number being acknowledged
     * @return Builder - the Builder
     */
    public static StreamClientMessage.Builder
            clientYieldAck(final int clientSeqNo, final int serverSeqNo) {
        return client(clientSeqNo)
                .setAck(Yields.yieldDataAck());
    }

    /**
     * Build a client Error message.
     *
     * @param clientSeqNo - the client sequence number
     * @param error - the error
     * @return Builder - the Builder
     */
    public static StreamClientMessage.Builder
            clientError(final int clientSeqNo,
                    final MishmashRpc.Error.Builder error) {
        return client(clientSeqNo)
                .setError(error);
    }

    /**
     * Build a client Error from a {@link PeerError}.
     *
     * @param clientSeqNo - the client sequence number
     * @param error - the error
     * @return Builder - the Builder
     */
    public static StreamClientMessage.Builder
            clientError(final int clientSeqNo, final PeerError error) {
        return clientError(clientSeqNo, Errors.error(error));
    }

    /**
     * Build a ClientInvokeResult message.
     *
     * @param clientSeqNo - the client sequence number
     * @param serverSeqNo - the server sequence number of ClientInvokeRequest
     * @param value - the invoke result value (as a builder)
     * @return Builder - the Builder
     */
    public static StreamClientMessage.Builder
            clientInvokeResult(final int clientSeqNo, final int serverSeqNo,
                    final Value.Builder value) {
        return client(clientSeqNo)
                .setInvokeResult(ClientInvokes.result(serverSeqNo, value));
    }

    /**
     * Build ConsoleOutputAck message.
     *
     * @param clientSeqNo - the client sequence number
     * @param serverSeqNo - the server sequence number being acknowledged
     * @return Builder - the Builder
     */
    public static StreamClientMessage.Builder
            clientConsoleOutputAck(final int clientSeqNo,
                    final int serverSeqNo) {
        return client(clientSeqNo)
                .setOutputAck(ConsoleOutputs.consoleOutputAck(serverSeqNo));
    }

    /**
     * Build DebugAck message.
     *
     * @param clientSeqNo - the client sequence number
     * @param serverSeqNo - the server sequence number being acknowledged
     * @return Builder - the Builder
     */
    public static StreamClientMessage.Builder
            clientDebugAck(final int clientSeqNo, final int serverSeqNo) {
        return client(clientSeqNo)
                .setDebugAck(Debugs.debugAck(serverSeqNo));
    }

    /**
     * Build a server stream message.
     *
     * @param serverSeqNo - the server sequence number
     * @return Builder - the Builder
     */
    public static StreamServerMessage.Builder server(final int serverSeqNo) {
        return StreamServerMessage.newBuilder()
                .setServerSeqNo(serverSeqNo);
    }

    /**
     * Build a SetupAck.
     *
     * @param serverSeqNo - the server sequence number
     * @param clientSeqNo - the client sequence number being acknowledged
     * @return Builder - the Builder
     */
    public static StreamServerMessage.Builder
            serverSetupAck(final int serverSeqNo, final int clientSeqNo) {
        return server(serverSeqNo)
                .setSetupAck(SetupAck.newBuilder());
    }

    /**
     * Build a Yield server message.
     *
     * @param serverSeqNo - the server sequence number
     * @param yield - the data
     * @return Builder - the Builder
     */
    public static StreamServerMessage.Builder
            serverYield(final int serverSeqNo, final YieldData.Builder yield) {
        return server(serverSeqNo)
                .setYieldData(yield);
    }

    /**
     * Build a server Error message.
     *
     * @param serverSeqNo - the server sequence number
     * @param error - the error
     * @return Builder - the Builder
     */
    public static StreamServerMessage.Builder
            serverError(final int serverSeqNo,
                    final MishmashRpc.Error.Builder error) {
        return server(serverSeqNo)
                .setError(error);
    }

    /**
     * Build a server Error message from a {@link PeerError}.
     *
     * @param serverSeqNo - the server sequence number
     * @param error - the error
     * @return Builder - the Builder
     */
    public static StreamServerMessage.Builder
            serverError(final int serverSeqNo, final PeerError error) {
        return serverError(serverSeqNo, Errors.error(error));
    }

    /**
     * Build a ClientInvokeRequest message.
     *
     * @param serverSeqNo - the server sequence number
     * @param callableId - the id of the client callable
     * @return Builder - the Builder
     */
    public static StreamServerMessage.Builder
            serverClientInvoke(final int serverSeqNo,
                    final String callableId) {
        return server(serverSeqNo)
                .setInvoke(ClientInvokes.request(callableId));
    }

    /**
     * Build a ConsoleOutput server message.
     *
     * @param serverSeqNo - the server sequence number
     * @param output - the output string
     * @return Builder - the Builder
     */
    public static StreamServerMessage.Builder
            serverCounsoleOutput(final int serverSeqNo, final String output) {
        return server(serverSeqNo)
                .setOutput(ConsoleOutputs.consoleOutput(output));
    }

    /**
     * Build a Debug server message.
     *
     * @param serverSeqNo - the server sequence number
     * @param info - the debug info
     * @return Builder - the Builder
     */
    public static StreamServerMessage.Builder
            serverDebug(final int serverSeqNo, final Iterable<String> info) {
        return server(serverSeqNo)
                .setDebug(Debugs.debug(info));
    }

    /**
     * Build a Debug server message.
     *
     * @param serverSeqNo - the server sequence number
     * @param info - the debug info
     * @return Builder - the Builder
     */
    public static StreamServerMessage.Builder
            serverDebug(final int serverSeqNo, final String...info) {
        return serverDebug(serverSeqNo, Arrays.asList(info));
    }
}
