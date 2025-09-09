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
import io.mishmash.common.rpc.MishmashRpc.MishmashSetup;
import io.mishmash.common.rpc.MishmashRpc.PredefinedFunction;
import io.mishmash.common.rpc.MishmashRpc.PredefinedSet;
import io.mishmash.common.rpc.MishmashRpc.Transformation;
import io.mishmash.common.rpc.MishmashRpc.Union;

/**
 * A base 'top-level' builder for client mishmashes.
 *
 * Builds the GRPC MishmashSetup message.
 *
 * All mishmashes begin with an intersection, so, this builder
 * only allows an intersection element.
 *
 * @param <I> - the GRPC input message type
 * @param <O> - the GRPC output message type
 * @param <CT> - the GRPC {@link BaseClient} type
 * @param <MT> - the {@link ClientMishmash} type
 */
public abstract class ClientMishmashBuilder<
            I, O,
            CT extends BaseClient<I, O>,
            MT extends ClientMishmash<I, O, CT>>
        extends BaseClientMishmashBuilder<I, O, CT, MT> {

    /**
     * The GRPC MishmashSetup builder.
     */
    private MishmashSetup.Builder setupBuilder;

    /**
     * Create the 'top-level' base client mishmash builder.
     */
    public ClientMishmashBuilder() {
        super(null);
        setupBuilder = MishmashSetup.newBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final Intersection.Builder intersection)
            throws MishmashException {
        if (setupBuilder.getTargetSet() == null) {
            throw new MishmashInvalidStateException();
        }

        setupBuilder = setupBuilder.setTargetSet(intersection.getSets());
    }

    /**
     * Get the GRPC MishmashSetup builder.
     *
     * @return - the builder
     * @throws MishmashException - thrown on failure
     */
    public MishmashSetup.Builder getBuilder() throws MishmashException {
        setupBuilder = setupBuilder.putAllClientOptions(getClientOptions());

        if (isMutation()) {
            setupBuilder = setupBuilder.setMutationType(
                    getMutationType() == 0
                        ? MishmashSetup.MutationType.OVERWRITE
                        : MishmashSetup.MutationType.APPEND);
        }

        return setupBuilder;
    }

    /**
     * Merging of literals is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public void merge(final Literal.Builder literal) throws MishmashException {
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
     * Merging of unions is not allowed.
     *
     * {@inheritDoc}
     */
    @Override
    public void merge(final Union.Builder union) throws MishmashException {
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
     * Merging of transformations is not allowed.
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
