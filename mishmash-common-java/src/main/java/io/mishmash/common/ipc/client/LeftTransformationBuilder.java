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

/**
 * A builder for GRPC left hand side of a transformation.
 *
 * @param <I> - the GRPC input message type
 * @param <O> - the GRPC output message type
 * @param <CT> - the GRPC {@link BaseClient} type
 * @param <MT> - the {@link ClientMishmash} type
 */
public class LeftTransformationBuilder<
            I, O,
            CT extends BaseClient<I, O>,
            MT extends ClientMishmash<I, O, CT>>
        extends DescriptorListBuilder<I, O, CT, MT> {

    /**
     * The method to invoke on leave.
     */
    private BuilderLeave<I, O, CT, MT,
            LeftTransformationBuilder<I, O, CT, MT>> leaveMethod;

    /**
     * Create a GRPC Left hand side transformation builder.
     *
     * @param parent - the parent builder
     * @param leave - the method to invoke on leave
     */
    public LeftTransformationBuilder(
            final BaseClientMishmashBuilder<I, O, CT, MT> parent,
            final BuilderLeave<I, O, CT, MT,
                LeftTransformationBuilder<I, O, CT, MT>> leave) {
        super(parent);
        this.leaveMethod = leave;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leaveTransformationLeft()
                    throws MishmashException {
        return leaveMethod.leave(this);
    }

}
