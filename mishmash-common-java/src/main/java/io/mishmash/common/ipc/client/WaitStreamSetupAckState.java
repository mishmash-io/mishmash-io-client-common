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

import io.mishmash.common.data.Key;
import io.mishmash.common.data.Mishmash;
import io.mishmash.common.data.Value;
import io.mishmash.common.ipc.proto.Proto;
import io.mishmash.common.rpc.MishmashRpc.StreamClientMessage;
import io.mishmash.common.rpc.MishmashRpc.StreamServerMessage;

/**
 * A GRPC Client state that waits for a SetupAck from the server.
 *
 * @param <MT> - the type of {@link Mishmash} that's being built
 */
public abstract class WaitStreamSetupAckState<MT extends Mishmash<Key, Value>>
        extends WaitSetupAckState<
            StreamServerMessage,
            StreamClientMessage,
            MT> {

    /**
     * Ensure an incoming GRPC server message is a SetupAck.
     *
     * @param input - the incoming message
     * @throws Exception - if message verification fails
     */
    public void validateInput(final StreamServerMessage input)
            throws Exception {
        Proto.ensureSetupAck(input);
    }

}
