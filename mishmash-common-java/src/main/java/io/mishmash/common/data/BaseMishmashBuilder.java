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
package io.mishmash.common.data;

import java.util.Map;

import io.mishmash.common.exception.MishmashException;

/**
 * Contains the basic operations available for building mishmashes -
 * instances of {@link Mishmash}.
 *
 * @param <T> - The type of result that will be built.
 */
public interface BaseMishmashBuilder<T> extends AutoCloseable {

    /**
     * Get the current client options.
     *
     * @return - a Map&lt;String, String&gt; of all settings.
     */
    Map<String, String> getClientOptions();

    /**
     * Set the client options.
     *
     * @param opts - a Map&lt;String, String$gt; of new client settings.
     * @return - A MishmashBuilder instance
     */
    BaseMishmashBuilder<T> setClientOptions(Map<String, String> opts);

    /**
     * Returns the type of mutation configured (if any).
     *
     * @return - the mutation type
     */
    int getMutationType(); // FIXME: not integer!

    /**
     * Configure the type of mutation - OVERWRITE or APPEND.
     *
     * @param mt - The mutation type
     * @return - A MishmashBuilder instance
     */
    BaseMishmashBuilder<T> setMutationType(int mt); // FIXME: not integer!

    /**
     * Enter a build for an UNION.
     *
     * @return - a SetupTargetBuilder
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> enterUnion() throws MishmashException;

    /**
     * Leave a UNION build.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> leaveUnion() throws MishmashException;

    /**
     * Enter a build for an INTERSECTION.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> enterIntersection() throws MishmashException;

    /**
     * Leave an INTERSECTION build.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> leaveIntersection() throws MishmashException;

    /**
     * Add a predefined set as a member.
     *
     * @param setType - the predefined set
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> addPredefinedSet(PredefinedSetType setType)
            throws MishmashException;

    /**
     * Add the 'PARENT' predefined set as a member.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    default BaseMishmashBuilder<T> addPredefinedSetParent()
            throws MishmashException {
        return addPredefinedSet(PredefinedSetType.parent);
    }

    /**
     * Add the 'CHILD' predefined set as a member.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    default BaseMishmashBuilder<T> addPredefinedSetChild()
            throws MishmashException {
        return addPredefinedSet(PredefinedSetType.child);
    }

    /**
     * Add the 'SIBLINGS' predefined set as a member.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    default BaseMishmashBuilder<T> addPredefinedSetSiblings()
            throws MishmashException {
        return addPredefinedSet(PredefinedSetType.siblings);
    }

    /**
     * Add the 'ANCESTOR' predefined set as a member.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    default BaseMishmashBuilder<T> addPredefinedSetAncestors()
            throws MishmashException {
        return addPredefinedSet(PredefinedSetType.ancestors);
    }

    /**
     * Add the 'DESCENDANTS' predefined set as a member.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    default BaseMishmashBuilder<T> addPredefinedSetDescendants()
            throws MishmashException {
        return addPredefinedSet(PredefinedSetType.descendants);
    }

    /**
     * Start building for a transform member.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> enterTransformation() throws MishmashException;

    /**
     * Complete the current transform member build.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> leaveTransformation() throws MishmashException;

    /**
     * Start building the left-hand side of a transform.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> enterTransformationLeft() throws MishmashException;

    /**
     * Complete the left-hand side of a transform.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> leaveTransformationLeft() throws MishmashException;

    /**
     * Start building the right-hand side of a transform.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> enterTransformationRight() throws MishmashException;

    /**
     * Complete the right-hand side of a transform.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> leaveTransformationRight() throws MishmashException;

    /**
     * Start building for a predefined function member.
     *
     * @param name - the function name
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> enterPredefinedFunction(String name)
            throws MishmashException;

    /**
     * Complete a predefined function build.
     *
     * @param name - the function name
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> leavePredefinedFunction(String name)
            throws MishmashException;

    /**
     * Start building for a predefined function argument.
     *
     * @param argumentNo - the argument number
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> enterPredefinedFunctionArgument(int argumentNo)
            throws MishmashException;

    /**
     * Complete a predefined function argument build.
     *
     * @param argumentNo - the argument number
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> leavePredefinedFunctionArgument(int argumentNo)
            throws MishmashException;

    /**
     * Start building a new lambda scope.
     *
     * @param runtime - the client runtime environment
     * @param lambdaName - the name of the lambda
     * @param scopeId - the lambda scope id
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T>
        enterNewLambdaScope(String runtime, String lambdaName, String scopeId)
            throws MishmashException;

    /**
     * Complete a new lambda scope build.
     *
     * @param runtime - the client runtime environment
     * @param lambdaName - the name of the lambda
     * @param scopeId - the lambda scope id
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T>
        leaveNewLambdaScope(String runtime, String lambdaName, String scopeId)
            throws MishmashException;

    /**
     * Enter an existing lambda scope.
     *
     * @param scopeId - the lambda scope id
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> enterExistingLambdaScope(String scopeId)
            throws MishmashException;

    /**
     * Leave an existing lambda scope.
     *
     * @param scopeId - the lambda scope id
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    BaseMishmashBuilder<T> leaveExistingLambdaScope(String scopeId)
            throws MishmashException;

//    public SetupTargetBuilder addLambda(Lambda lam) throws MishmashException;

    /**
     * Build the target object.
     *
     * All resources held by this builder should be released, close() will NOT
     * be called after build().
     *
     * @return - the object built
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    T build() throws MishmashException;

}
