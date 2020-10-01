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
package io.mishmash.common.ipc.client;

import io.mishmash.common.exception.MishmashException;
import io.mishmash.common.rpc.MishmashRpc.Union;

/**
 * A builder for GRPC Union.
 *
 * @param <I> - the GRPC input message type
 * @param <O> - the GRPC output message type
 * @param <CT> - the GRPC {@link BaseClient} type
 * @param <MT> - the {@link ClientMishmash} type
 */
public class UnionBuilder<
            I, O,
            CT extends BaseClient<I, O>,
            MT extends ClientMishmash<I, O, CT>>
        extends DescriptorListBuilder<I, O, CT, MT> {

    /**
     * The method to invoke on leave.
     */
    private BuilderLeave<I, O, CT, MT,
        UnionBuilder<I, O, CT, MT>> leaveMethod;

    /**
     * The GRPC union builder.
     */
    private Union.Builder builder;

    /**
     * Create a GRPC union builder.
     *
     * @param parent - the parent builder
     * @param leave - the leave method
     */
    public UnionBuilder(final BaseClientMishmashBuilder<I, O, CT, MT> parent,
            final BuilderLeave<I, O, CT, MT,
                UnionBuilder<I, O, CT, MT>> leave) {
        super(parent);
        this.leaveMethod = leave;
        this.builder = Union.newBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT> leaveUnion()
            throws MishmashException {
        builder = builder.setSets(getDescriptorList());
        return leaveMethod.leave(this);
    }

    /**
     * Get the GRPC union builder.
     *
     * @return - the GRPC union builder
     */
    public Union.Builder getUnion() {
        return builder;
    }
}
