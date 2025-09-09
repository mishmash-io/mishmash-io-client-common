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

import io.mishmash.common.rpc.MishmashRpc.Debug;
import io.mishmash.common.rpc.MishmashRpc.DebugAck;

/**
 * Helper methods to build Debug and DebugAck GRPC messages.
 *
 */
public final class Debugs {

    /**
     * The Debugs class should not be instantiated.
     */
    private Debugs() {
        // Hiding the constructor.
    }

    /**
     * Build a Debug for some Strings.
     *
     * @param info - the debug info
     * @return Builder - a Debug builder
     */
    public static Debug.Builder debug(final Iterable<String> info) {
        return Debug.newBuilder().addAllDebugInfo(info);
    }

    /**
     * Build a Debug for some Strings.
     *
     * @param info - the debug info
     * @return Builder - a Debug builder
     */
    public static Debug.Builder debug(final String...info) {
        return debug(Arrays.asList(info));
    }

    /**
     * Build a DebugAck for a previous Debug.
     *
     * @param debugSeqNo - the Debug sequence number
     * @return Builder - a DebugAck builder
     */
    public static DebugAck.Builder debugAck(final int debugSeqNo) {
        return DebugAck.newBuilder().setAckSeqNo(debugSeqNo);
    }
}
