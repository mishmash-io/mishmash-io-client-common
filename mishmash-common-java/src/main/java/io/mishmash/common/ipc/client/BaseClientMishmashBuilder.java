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

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import io.mishmash.common.data.MishmashBuilder;
import io.mishmash.common.data.PredefinedSetType;
import io.mishmash.common.exception.MishmashException;
import io.mishmash.common.exception.MishmashInvalidArgumentException;
import io.mishmash.common.exception.MishmashInvalidStateException;
import io.mishmash.common.exception.MishmashUnimplementedException;
import io.mishmash.common.ipc.proto.Members;
import io.mishmash.common.ipc.proto.Values;
import io.mishmash.common.rpc.MishmashRpc.Intersection;
import io.mishmash.common.rpc.MishmashRpc.LambdaFunction;
import io.mishmash.common.rpc.MishmashRpc.Literal;
import io.mishmash.common.rpc.MishmashRpc.PredefinedFunction;
import io.mishmash.common.rpc.MishmashRpc.PredefinedSet;
import io.mishmash.common.rpc.MishmashRpc.Transformation;
import io.mishmash.common.rpc.MishmashRpc.Union;

/**
 * A base builder for client mishmashes.
 *
 * @param <I> - the GRPC input message type
 * @param <O> - the GRPC output message type
 * @param <CT> - the GRPC {@link BaseClient} type
 * @param <MT> - the {@link ClientMishmash} type
 */
public abstract class BaseClientMishmashBuilder<
            I, O,
            CT extends BaseClient<I, O>,
            MT extends ClientMishmash<I, O, CT>>
        implements MishmashBuilder<MT> {

    /**
     * The client options.
     */
    private Map<String, String> clientOptions;

    /**
     * The mutation type, if a mutation.
     */
    private int mutationType;

    /**
     * If a mutation or a stream.
     */
    private boolean isMutation;

    /**
     * Create a BaseClientMishmashBuilder.
     *
     * @param parent - a parent builder or null if none
     */
    public BaseClientMishmashBuilder(
            final BaseClientMishmashBuilder<I, O, CT, MT> parent) {
        clientOptions = new HashMap<>();

        if (parent != null) {
            setClientOptions(parent.getClientOptions());
            mutationType = parent.mutationType;
            isMutation = parent.isMutation;
        }
    }

    /**
     * Test if building a mutation.
     *
     * @return - true if this represents a mutation
     */
    public boolean isMutation() {
        return isMutation;
    }

    /**
     * Configure a build for a mutation.
     *
     * @return - returns this builder
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> setMutation() {
        isMutation = true;
        return this;
    }

    /**
     * Configure a build for a stream.
     *
     * @return - returns this builder
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> setStream() {
        isMutation = false;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String> getClientOptions() {
        return clientOptions;
    }

    /**
     * {@inheritDoc}
     */
    public int getMutationType() {
        return mutationType;
    }

    /**
     * {@inheritDoc}
     */
    public MT build() throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws Exception {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            addInstance(final String instance)
                    throws MishmashException {
        merge(Literal.newBuilder().setId(Members.id(instance)));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            addBoolean(final boolean bool)
                    throws MishmashException {
        merge(Literal.newBuilder().setValue(Values.booleanValue(bool)));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            addDate(final ZonedDateTime dt)
                    throws MishmashException {
        merge(Literal.newBuilder().setValue(Values.dateValue(dt)));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> addNull()
            throws MishmashException {
        merge(Literal.newBuilder().setValue(Values.nullValue()));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> addString(final String str)
            throws MishmashException {
        merge(Literal.newBuilder().setValue(Values.stringValue(str)));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            addBigDecimal(final BigDecimal decimal)
                    throws MishmashException {
        merge(Literal.newBuilder().setValue(Values.decimalValue(decimal)));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            addDouble(final double decimal)
                    throws MishmashException {
        merge(Literal.newBuilder().setValue(Values.decimalValue(decimal)));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            addDecimalString(final String decimal)
                    throws MishmashException {
        merge(Literal.newBuilder().setValue(Values.decimalValue(decimal)));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            addInteger(final int decimal, final boolean isSigned)
                    throws MishmashException {
        if (isSigned) {
            merge(Literal
                    .newBuilder()
                    .setValue(
                            Values.decimalValueSigned(decimal)));
        } else {
            merge(Literal
                    .newBuilder()
                    .setValue(
                            Values.decimalValueUnsigned(decimal)));
        }

        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            addLong(final long decimal, final boolean isSigned)
                    throws MishmashException {
        if (isSigned) {
            merge(Literal
                    .newBuilder()
                    .setValue(
                            Values.decimalValueSigned(decimal)));
        } else {
            merge(Literal
                    .newBuilder()
                    .setValue(
                            Values.decimalValueUnsigned(decimal)));
        }

        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            setClientOptions(final Map<String, String> opts) {
        clientOptions.clear();
        clientOptions.putAll(opts);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            setMutationType(final int mt) {
        mutationType = mt;

        return this;
    }

    /**
     * Merge a Literal into this build.
     *
     * @param literal - the literal
     * @throws MishmashException - if merge fails
     */
    public abstract void merge(Literal.Builder literal)
            throws MishmashException;

    /**
     * Merge a PredefinedSet into this build.
     *
     * @param predefinedSet - the predefined set
     * @throws MishmashException - if merge fails
     */
    public abstract void merge(PredefinedSet.Builder predefinedSet)
            throws MishmashException;

    /**
     * Merge an intersection into this build.
     *
     * @param intersection - the intersection
     * @throws MishmashException - if merge fails
     */
    public abstract void merge(Intersection.Builder intersection)
            throws MishmashException;

    /**
     * Merge a Union into this build.
     *
     * @param union - the union
     * @throws MishmashException - if merge fails
     */
    public abstract void merge(Union.Builder union)
            throws MishmashException;

    /**
     * Merge a lambda function into this build.
     *
     * @param lambda - the lambda function
     * @throws MishmashException - if merge fails
     */
    public abstract void merge(LambdaFunction.Builder lambda)
            throws MishmashException;

    /**
     * Merge a predefined function into this build.
     *
     * @param function - the predefined function
     * @throws MishmashException - if merge fails
     */
    public abstract void merge(PredefinedFunction.Builder function)
            throws MishmashException;

    /**
     * Merge a tranform into this build.
     *
     * @param transform - the transform
     * @throws MishmashException - if merge fails
     */
    public abstract void merge(Transformation.Builder transform)
            throws MishmashException;

    /**
     * Leave and merge a {@link UnionBuilder}.
     *
     * @param builder - the {@link UnionBuilder}
     * @return - this builder
     * @throws MishmashException - on merge fialure
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leave(final UnionBuilder<I, O, CT, MT> builder)
                    throws MishmashException {
        merge(builder.getUnion());
        return this;
    }

    /**
     * Leave and merge an {@link IntersectionBuilder}.
     *
     * @param builder - the {@link IntersectionBuilder}
     * @return - this builder
     * @throws MishmashException - on merge failure
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leave(final IntersectionBuilder<I, O, CT, MT> builder)
                    throws MishmashException {
        merge(builder.getIntersection());
        return this;
    }

    /**
     * Leave and merge a {@link TransformationBuilder}.
     *
     * @param builder - the {@link TransformationBuilder}
     * @return - this builder
     * @throws MishmashException - on merge failure
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leave(final TransformationBuilder<I, O, CT, MT> builder)
                    throws MishmashException {
        merge(builder.getTransformation());
        return this;
    }

    /**
     * Leave and merge a {@link PredefinedFunctionBuilder}.
     *
     * @param builder - the {@link PredefinedFunctionBuilder}
     * @return - this bulder
     * @throws MishmashException - on merge failure
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leave(final PredefinedFunctionBuilder<I, O, CT, MT> builder)
                    throws MishmashException {
        merge(builder.getPredefinedFunction());
        return this;
    }

    /**
     * Leave and merge a {@link NewLambdaBuilder}.
     *
     * @param builder - the {@link NewLambdaBuilder}
     * @return - this builder
     * @throws MishmashException - on merge failure
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leave(final NewLambdaBuilder<I, O, CT, MT> builder)
                    throws MishmashException {
        merge(builder.getLambda());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> enterUnion()
            throws MishmashException {
        return new UnionBuilder<I, O, CT, MT>(this, this::leave);
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> leaveUnion()
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> enterIntersection()
            throws MishmashException {
        return new IntersectionBuilder<I, O, CT, MT>(this, this::leave);
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> leaveIntersection()
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            addPredefinedSet(final PredefinedSetType setType)
                    throws MishmashException {
        switch (setType) {
        case ancestors:
            merge(PredefinedSet
                    .newBuilder()
                    .setIdent(PredefinedSet.identifier.ANCESTORS));
            break;
        case child:
            merge(PredefinedSet
                    .newBuilder()
                    .setIdent(PredefinedSet.identifier.CHILD));
            break;
        case descendants:
            merge(PredefinedSet
                    .newBuilder()
                    .setIdent(PredefinedSet.identifier.DESCENDANTS));
            break;
        case parent:
            merge(PredefinedSet
                    .newBuilder()
                    .setIdent(PredefinedSet.identifier.PARENT));
            break;
        case siblings:
            merge(PredefinedSet
                    .newBuilder()
                    .setIdent(PredefinedSet.identifier.SIBLINGS));
            break;
        default:
            throw new MishmashInvalidArgumentException();
        }

        return this;
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> enterTransformation()
            throws MishmashException {
        return new TransformationBuilder<I, O, CT, MT>(this, this::leave);
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> leaveTransformation()
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> enterTransformationLeft()
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> leaveTransformationLeft()
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> enterTransformationRight()
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT> leaveTransformationRight()
            throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            enterPredefinedFunction(final String name)
                    throws MishmashException {
        return new PredefinedFunctionBuilder<I, O, CT, MT>(
                this, name, this::leave);
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leavePredefinedFunction(final String name)
                    throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            enterPredefinedFunctionArgument(final int argumentNo)
                    throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leavePredefinedFunctionArgument(final int argumentNo)
                    throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            enterNewLambdaScope(
                    final String runtime,
                    final String lambdaName,
                    final String scopeId)
                    throws MishmashException {
        return new NewLambdaBuilder<I, O, CT, MT>(
                this, runtime, lambdaName, scopeId, this::leave);
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            addSource(
                    final String clientEnv,
                    final String lambdaName,
                    final String source)
                    throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leaveNewLambdaScope(
                    final String runtime,
                    final String lambdaName,
                    final String scopeId)
                    throws MishmashException {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            enterExistingLambdaScope(final String scopeId)
                    throws MishmashException {
        throw new MishmashUnimplementedException();
    }

    /**
     * {@inheritDoc}
     */
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leaveExistingLambdaScope(final String scopeId)
                    throws MishmashException {
        throw new MishmashInvalidStateException();
    }

}
