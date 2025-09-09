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

import io.mishmash.common.exception.MishmashException;
import io.mishmash.common.exception.MishmashInvalidStateException;
import io.mishmash.common.rpc.MishmashRpc.Intersection;
import io.mishmash.common.rpc.MishmashRpc.LambdaFunction;
import io.mishmash.common.rpc.MishmashRpc.Literal;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetDescriptor;
import io.mishmash.common.rpc.MishmashRpc.PredefinedFunction;
import io.mishmash.common.rpc.MishmashRpc.PredefinedSet;
import io.mishmash.common.rpc.MishmashRpc.Transformation;
import io.mishmash.common.rpc.MishmashRpc.Union;

/**
 * An abstract builder for GRPC MishmashSetDescriptor message.
 *
 * @param <I> - the GRPC input message type
 * @param <O> - the GRPC output message type
 * @param <CT> - the GRPC {@link BaseClient} type
 * @param <MT> - the {@link ClientMishmash} type
 */
public abstract class DescriptorBuilder<
            I, O,
            CT extends BaseClient<I, O>,
            MT extends ClientMishmash<I, O, CT>>
        extends BaseClientMishmashBuilder<I, O, CT, MT> {

    /**
     * The GRPC MishmashSetDescriptor builder.
     */
    private MishmashSetDescriptor.Builder builder;

    /**
     * True if it has been built already.
     */
    private boolean isSet = false;

    /**
     * Create a DescriptorBuilder.
     *
     * @param parent - the parent builder
     */
    public DescriptorBuilder(
            final BaseClientMishmashBuilder<I, O, CT, MT> parent) {
        super(parent);
        builder = MishmashSetDescriptor.newBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final Literal.Builder literal) throws MishmashException {
        if (isSet) {
            throw new MishmashInvalidStateException();
        }

        isSet = true;
        builder = builder.setLiteral(literal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final PredefinedSet.Builder predefinedSet)
            throws MishmashException {
        if (isSet) {
            throw new MishmashInvalidStateException();
        }

        isSet = true;
        builder = builder.setPredefinedSet(predefinedSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final Intersection.Builder intersection)
            throws MishmashException {
        if (isSet) {
            throw new MishmashInvalidStateException();
        }

        isSet = true;
        builder = builder.setIntersection(intersection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final Union.Builder union) throws MishmashException {
        if (isSet) {
            throw new MishmashInvalidStateException();
        }

        isSet = true;
        builder = builder.setUnion(union);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final LambdaFunction.Builder lambda)
            throws MishmashException {
        if (isSet) {
            throw new MishmashInvalidStateException();
        }

        isSet = true;
        builder = builder.setLambdaFunction(lambda);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final PredefinedFunction.Builder function)
            throws MishmashException {
        if (isSet) {
            throw new MishmashInvalidStateException();
        }

        isSet = true;
        builder = builder.setPredefinedFunction(function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final Transformation.Builder transform)
            throws MishmashException {
        if (isSet) {
            throw new MishmashInvalidStateException();
        }

        isSet = true;
        builder = builder.setTransformation(transform);
    }

    /**
     * Get the GRPC MishamshSetDescriptor builder.
     *
     * @return - the GRPC MishmashSetDescriptor builder
     * @throws MishmashException - if not built yet
     */
    public MishmashSetDescriptor.Builder getDescriptor()
            throws MishmashException {
        if (!isSet) {
            throw new MishmashInvalidStateException();
        }

        return builder;
    }
}
