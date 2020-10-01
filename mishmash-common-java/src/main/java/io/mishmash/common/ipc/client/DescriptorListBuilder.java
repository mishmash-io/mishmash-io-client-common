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

import io.mishmash.common.rpc.MishmashRpc.Intersection;
import io.mishmash.common.rpc.MishmashRpc.LambdaFunction;
import io.mishmash.common.rpc.MishmashRpc.Literal;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetDescriptor;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetDescriptorList;
import io.mishmash.common.rpc.MishmashRpc.PredefinedFunction;
import io.mishmash.common.rpc.MishmashRpc.PredefinedSet;
import io.mishmash.common.rpc.MishmashRpc.Transformation;
import io.mishmash.common.rpc.MishmashRpc.Union;

/**
 * Build a GRPC MishmashSetDecriptorList.
 *
 * @param <I> - the GRPC input message type
 * @param <O> - the GRPC output message type
 * @param <CT> - the GRPC {@link BaseClient} type
 * @param <MT> - the {@link ClientMishmash} type
 */
public abstract class DescriptorListBuilder<
            I, O,
            CT extends BaseClient<I, O>,
            MT extends ClientMishmash<I, O, CT>>
        extends BaseClientMishmashBuilder<I, O, CT, MT> {

    /**
     * The GRPC MishmashSetDescriptorList builder.
     */
    private MishmashSetDescriptorList.Builder builder;

    /**
     * Create a DescriptorListBuilder.
     *
     * @param parent - the parent builder
     */
    public DescriptorListBuilder(
            final BaseClientMishmashBuilder<I, O, CT, MT> parent) {
        super(parent);
        builder = MishmashSetDescriptorList.newBuilder();
    }

    /**
     * Get the MishmashSetDescriptorList that has been built.
     *
     * @return - the GRPC MishmashSetDescriptorList builder
     */
    public MishmashSetDescriptorList.Builder getDescriptorList() {
        return builder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final Literal.Builder literal) {
        builder.addEntries(
                MishmashSetDescriptor
                    .newBuilder()
                    .setLiteral(literal));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final PredefinedSet.Builder predefinedSet) {
        builder.addEntries(
                MishmashSetDescriptor
                    .newBuilder()
                    .setPredefinedSet(predefinedSet));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final Intersection.Builder intersection) {
        builder.addEntries(
                MishmashSetDescriptor
                    .newBuilder()
                    .setIntersection(intersection));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final Union.Builder union) {
        builder.addEntries(
                MishmashSetDescriptor
                    .newBuilder()
                    .setUnion(union));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final LambdaFunction.Builder lambda) {
        builder.addEntries(
                MishmashSetDescriptor
                    .newBuilder()
                    .setLambdaFunction(lambda));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final PredefinedFunction.Builder predefinedFunction) {
        builder.addEntries(
                MishmashSetDescriptor
                    .newBuilder()
                    .setPredefinedFunction(predefinedFunction));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final Transformation.Builder transform) {
        builder.addEntries(
                MishmashSetDescriptor
                    .newBuilder()
                    .setTransformation(transform));
    }

}
