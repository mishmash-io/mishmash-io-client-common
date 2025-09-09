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
import io.mishmash.common.rpc.MishmashRpc.LambdaFunctionBody;
import io.mishmash.common.rpc.MishmashRpc.Literal;
import io.mishmash.common.rpc.MishmashRpc.PredefinedFunction;
import io.mishmash.common.rpc.MishmashRpc.PredefinedSet;
import io.mishmash.common.rpc.MishmashRpc.Transformation;
import io.mishmash.common.rpc.MishmashRpc.Union;

/**
 * A builder for GRPC lambdas.
 *
 * @param <I> - the GRPC input message type
 * @param <O> - the GRPC output message type
 * @param <CT> - the GRPC {@link BaseClient} type
 * @param <MT> - the {@link ClientMishmash} type
 */
public class NewLambdaBuilder<
            I, O,
            CT extends BaseClient<I, O>,
            MT extends ClientMishmash<I, O, CT>>
        extends BaseClientMishmashBuilder<I, O, CT, MT> {

    /**
     * The method to invoke on leave.
     */
    private BuilderLeave<I, O, CT, MT,
        NewLambdaBuilder<I, O, CT, MT>> leaveMethod;

    /**
     * The client runtime environment.
     */
    private String runtime;

    /**
     * The lambda name, if given.
     */
    private String name;

    /**
     * The lambda scope id.
     */
    private String scope;

    /**
     * The GRPC lambda builder.
     */
    private LambdaFunction.Builder builder;

    /**
     * Create a new GRPC lambda builder.
     *
     * @param parent - the parent builder
     * @param runtimeEnv - the client runtime
     * @param functionName - the lambda name (if given)
     * @param scopeId - the scope id
     * @param leave - the method to invoke on leave
     */
    public NewLambdaBuilder(
            final BaseClientMishmashBuilder<I, O, CT, MT> parent,
            final String runtimeEnv,
            final String functionName,
            final String scopeId,
            final BuilderLeave<I, O, CT, MT,
                NewLambdaBuilder<I, O, CT, MT>> leave) {
        super(parent);
        this.runtime = runtimeEnv;
        this.name = functionName;
        this.scope = scopeId;
        this.leaveMethod = leave;
        this.builder = LambdaFunction.newBuilder()
                .setClientRuntime(runtime)
                .setName(functionName)
                .setScopeId(scopeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT>
            leaveNewLambdaScope(
                    final String runtimeEnv,
                    final String lambdaName,
                    final String scopeId)
                            throws MishmashException {
        // FIXME: check name and scope

        if (builder.getBody() == null) {
            throw new MishmashInvalidStateException();
        }

        return leaveMethod.leave(this);
    }

    /**
     * Get the GRPC lambda builder.
     *
     * @return - the GRPC lambda builder
     */
    public LambdaFunction.Builder getLambda() {
        return builder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseClientMishmashBuilder<I, O, CT, MT>
            addSource(
                    final String clientEnv,
                    final String lambdaName,
                    final String source)
                            throws MishmashException {
        if (builder.getBody() != null) {
            throw new MishmashInvalidStateException();
        }

        builder = builder.setBody(
                LambdaFunctionBody
                    .newBuilder()
                    .setSource(source));

        return this;
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
    public void merge(final PredefinedFunction.Builder function)
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
            enterPredefinedFunction(final String functionName)
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
                    final String runtimeEnv,
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
