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

/**
 * Helper methods to build Error GRPC messages.
 *
 */
public final class Errors {

    /**
     * The Errors class should not be instantiated.
     */
    private Errors() {
        // Hiding the constructor.
    }

    /**
     * Build an Error message from a {@link PeerError}.
     *
     * @param peerError - the error
     * @return Builder - an Error builder
     */
    public static MishmashRpc.Error.Builder error(final PeerError peerError) {
        return MishmashRpc.Error.newBuilder()
                .setErrorCode(peerError.getErrorCode())
                .setMessage(peerError.getErrorMessage())
                .addAllAdditionalInfo(peerError.getAdditionalInfo());
    }

}
