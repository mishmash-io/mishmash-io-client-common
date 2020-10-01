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
package io.mishmash.common.ipc.server;

import java.util.function.Supplier;

import io.mishmash.common.data.Key;
import io.mishmash.common.data.Mishmash;
import io.mishmash.common.data.MishmashBuilder;
import io.mishmash.common.data.Value;
import io.mishmash.common.ipc.proto.Proto;
import io.mishmash.common.rpc.MishmashRpc.StreamClientMessage;
import io.mishmash.common.rpc.MishmashRpc.StreamServerMessage;

/**
 * A {@link WaitSetupState} that waits for the SETUP on a GRPC stream().
 *
 * A {@link MishmashBuilder} is obtained through a {@link Supplier} mainly
 * to avoid expensive operations on stores before a SETUP message is actually
 * received (it can time out too).
 *
 * @param <MT> - the type of {@link Mishmash} built by the
 *                      {@link MishmashBuilder}
 */
public abstract class WaitStreamSetupState<MT extends Mishmash<Key, Value>>
        extends WaitSetupState<StreamClientMessage, StreamServerMessage, MT> {

    /**
     * Create a new WaitStreamSetupState.
     *
     * @param targetBuilderSupplier - the {@link MishmashBuilder} Supplier
     */
    public WaitStreamSetupState(
            final Supplier<MishmashBuilder<MT>> targetBuilderSupplier) {
        super(targetBuilderSupplier);
    }

    /**
     * Validate the incoming message is a SETUP.
     *
     * @param input - the incoming message
     */
    @Override
    public void
            validateInput(final StreamClientMessage input)
                    throws Exception {
        Proto.ensureSetup(input);
    }

    /**
     * Process an incoming SETUP message.
     *
     * @param input - the incoming message
     */
    @Override
    public void input(final StreamClientMessage input) throws Exception {
        input(input.getSetup());
    }

}
