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
package io.mishmash.common.ipc.proto;

import io.mishmash.common.rpc.MishmashRpc.ClientInvokeRequest;
import io.mishmash.common.rpc.MishmashRpc.ClientInvokeResult;
import io.mishmash.common.rpc.MishmashRpc.Value;

/**
 * Helper methods to build ClientInvokeRequest and Result GRPC messages.
 *
 */
public final class ClientInvokes {

    /**
     * The ClientInvokes class should not be instantiated.
     */
    private ClientInvokes() {
        // Hiding the constructor.
    }

    /**
     * Build a ClientInvokeRequest.
     *
     * @param callableId - the id of the callable at the client
     * @return Builder - a ClientInvokeRequest Builder
     */
    public static ClientInvokeRequest.Builder request(final String callableId) {
        return ClientInvokeRequest.newBuilder().setCallableId(callableId);
    }

    /**
     * Build a ClientInvokeResult.
     *
     * @param requestSeqNo - the sequence number of the ClientInvokeRequest
     * @param value - the result value (as a builder)
     * @return Builder - a ClientInvokeResponse Builder
     */
    public static ClientInvokeResult.Builder result(final int requestSeqNo,
            final Value.Builder value) {
        return ClientInvokeResult.newBuilder()
                .setAckSeqNo(requestSeqNo)
                .setResult(value);
    }
}
