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

import io.mishmash.common.rpc.MishmashRpc.ConsoleOutput;
import io.mishmash.common.rpc.MishmashRpc.ConsoleOutputAck;

/**
 * Helper methods to build ConsoleOutput and ConsoleOutputAck GRPC messages.
 *
 */
public final class ConsoleOutputs {

    /**
     * The ConsoleOutputs class should not be instantiated.
     */
    private ConsoleOutputs() {
        // Hiding the constructor.
    }

    /**
     * Build a ConsoleOutput for a String.
     *
     * @param output - the output string
     * @return Builder - a ConsoleOutput builder
     */
    public static ConsoleOutput.Builder consoleOutput(final String output) {
        return ConsoleOutput.newBuilder().setConsoleOutputLine(output);
    }

    /**
     * Build a ConsoleOutputAck.
     *
     * @param outputSeqNo - the sequence number of the ConsoleOutput
     * @return Builder - a ConsoleOutputAck builder
     */
    public static ConsoleOutputAck.Builder
            consoleOutputAck(final int outputSeqNo) {
        return ConsoleOutputAck.newBuilder().setAckSeqNo(outputSeqNo);
    }

}
