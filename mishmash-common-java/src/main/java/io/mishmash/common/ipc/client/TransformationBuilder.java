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
import io.mishmash.common.exception.MishmashInvalidStateException;
import io.mishmash.common.rpc.MishmashRpc.Intersection;
import io.mishmash.common.rpc.MishmashRpc.LambdaFunction;
import io.mishmash.common.rpc.MishmashRpc.Literal;
import io.mishmash.common.rpc.MishmashRpc.PredefinedFunction;
import io.mishmash.common.rpc.MishmashRpc.PredefinedSet;
import io.mishmash.common.rpc.MishmashRpc.Transformation;
import io.mishmash.common.rpc.MishmashRpc.Transformation.Axis;
import io.mishmash.common.rpc.MishmashRpc.Union;

/**
 * A builder for a GRPC transformation.
 *
 * @param <I> - the GRPC input message type
 * @param <O> - the GRPC output message type
 * @param <CT> - the GRPC {@link BaseClient} type
 * @param <MT> - the {@link ClientMishmash} type
 */
public class TransformationBuilder<
            I, O,
            CT extends BaseClient<I, O>,
            MT extends ClientMishmash<I, O, CT>>
        extends BaseClientMishmashBuilder<I, O, CT, MT> {

    /**
     * The method to invoke on leave.
     */
    private BuilderLeave<I, O, CT, MT,
            TransformationBuilder<I, O, CT, MT>> leaveMethod;

    /**
     * The transformation Axis builder.
     */
    private Axis.Builder builder;

    /**
     * Create a GRPC transformation builder.
     *
     * @param parent - the parent builder
     * @param leave - the method to invoke on leave
     */
    public TransformationBuilder(
            final BaseClientMishmashBuilder<I, O, CT, MT> parent,
            final BuilderLeave<I, O, CT, MT,
                TransformationBuilder<I, O, CT, MT>> leave) {
        super(parent);
        this.leaveMethod = leave;
        this.builder = Axis.newBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT> leaveTransformation()
            throws MishmashException {
        if (builder.getLeft() == null || builder.getRight() == null) {
            throw new MishmashInvalidStateException();
        }

        return leaveMethod.leave(this);
    }

    /**
     * Leave method for the {@link LeftTransformationBuilder}.
     *
     * @param left - the {@link LeftTransformationBuilder}
     * @return - this builder
     * @throws MishmashException - on merge failure
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leave(final LeftTransformationBuilder<I, O, CT, MT> left)
                    throws MishmashException {
        builder = builder.setLeft(left.getDescriptorList());
        return this;
    }

    /**
     * Leave method for the {@link RightTransformationBuilder}.
     *
     * @param right - the {@link RightTransformationBuilder}
     * @return - this builder
     * @throws MishmashException - on merge failure
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leave(final RightTransformationBuilder<I, O, CT, MT> right)
                    throws MishmashException {
        builder = builder.setRight(right.getDescriptorList());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT>
            enterTransformationLeft()
                    throws MishmashException {
        if (builder.getLeft() != null) {
            throw new MishmashInvalidStateException();
        }

        return new LeftTransformationBuilder<I, O, CT, MT>(
                this, this::leave);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT>
            enterTransformationRight()
                    throws MishmashException {
        if (builder.getRight() != null) {
            throw new MishmashInvalidStateException();
        }

        return new RightTransformationBuilder<I, O, CT, MT>(
                this, this::leave);
    }

    /**
     * Get the GRPC transformation builder.
     *
     * @return - the GRPC transformation builder
     */
    public Transformation.Builder getTransformation() {
        return Transformation.newBuilder().setAxis(builder);
    }

    /**
     * Merging of literals is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public void merge(final Literal.Builder literal)
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * Merging of predefined sets is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public void merge(final PredefinedSet.Builder predefinedSet)
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * Merging of intersections is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public void merge(final Intersection.Builder intersection)
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * Merging of unions is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public void merge(final Union.Builder union)
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * Merging of lambdas is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public void merge(final LambdaFunction.Builder lambda)
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * Merging of predefined functions is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public void merge(final PredefinedFunction.Builder funcion)
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * Merging of other transformations is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public void merge(final Transformation.Builder transform)
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * Entering unions is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT> enterUnion()
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * Entering intersections is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT> enterIntersection()
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * Entering transformations is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT> enterTransformation()
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * Entering predefined functions is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT>
            enterPredefinedFunction(final String name)
                    throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * Entering lambdas is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT>
            enterNewLambdaScope(
                    final String runtime,
                    final String lambdaName,
                    final String scopeId)
                    throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * Entering lambdas is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT>
            enterExistingLambdaScope(final String scopeId)
                    throws MishmashException {
        throw new MishmashInvalidStateException();
    }

}
