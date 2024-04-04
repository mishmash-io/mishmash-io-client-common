/*
 *    Copyright 2024 Mishmash IO UK Ltd.
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

import io.mishmash.common.exception.MishmashException;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetDescriptor;

/**
 * A builder for a GRPC predefined function argument.
 *
 * @param <I> - the GRPC input message type
 * @param <O> - the GRPC output message type
 * @param <CT> - the GRPC {@link BaseClient} type
 * @param <MT> - the {@link ClientMishmash} type
 */
public class PredefinedFunctionArgumentBuilder<
            I, O,
            CT extends BaseClient<I, O>,
            MT extends ClientMishmash<I, O, CT>>
        extends DescriptorBuilder<I, O, CT, MT> {

    /**
     * The method to invoke on leave.
     */
    private BuilderLeave<I, O, CT, MT,
            PredefinedFunctionArgumentBuilder<I, O, CT, MT>> leaveMethod;

    /**
     * The argument number.
     */
    private int argNo;

    /**
     * Create a builder for a GRPC predefined function argument.
     *
     * @param parent - the parent builder
     * @param argumentNumber - the argument number
     * @param leave - the leave method
     */
    public PredefinedFunctionArgumentBuilder(
            final BaseClientMishmashBuilder<I, O, CT, MT> parent,
            final int argumentNumber,
            final BuilderLeave<I, O, CT, MT,
                PredefinedFunctionArgumentBuilder<I, O, CT, MT>> leave) {
        super(parent);
        this.leaveMethod = leave;
        this.argNo = argumentNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leavePredefinedFunctionArgument(final int argumentNo)
                    throws MishmashException {
        // FIXME: check argNo
        return leaveMethod.leave(this);
    }

    /**
     * Get the builder for this GRPC predefined function argument.
     *
     * @return - the GRPC builder
     * @throws MishmashException - thrown on failure
     */
    public MishmashSetDescriptor.Builder getArgument()
            throws MishmashException {
        return getDescriptor();
    }
}
