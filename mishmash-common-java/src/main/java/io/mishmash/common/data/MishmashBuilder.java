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
package io.mishmash.common.data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

import io.mishmash.common.exception.MishmashException;

/**
 * Represents the interface that helps build mishmashes -
 * instances of {@link Mishmash} or Protocol messages.
 *
 * @param <T> - The type of result that will be built.
 */
public interface MishmashBuilder<T> extends BaseMishmashBuilder<T> {

    /**
     * Add an instance ID.
     *
     * @param instance - the instance ID
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    MishmashBuilder<T> addInstance(String instance) throws MishmashException;

    /**
     * Add a boolean member.
     *
     * @param bool - the boolean member.
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    MishmashBuilder<T> addBoolean(boolean bool) throws MishmashException;

    /**
     * Add a DateTime member.
     *
     * @param dt - the DateTime as {@link ZonedDateTime}
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    MishmashBuilder<T> addDate(ZonedDateTime dt) throws MishmashException;

    /**
     * Add a NULL member.
     *
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    MishmashBuilder<T> addNull() throws MishmashException;

    /**
     * Add a string member.
     *
     * @param str - the string member
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    MishmashBuilder<T> addString(String str) throws MishmashException;

    /**
     * Add a decimal member.
     *
     * @param decimal - the decimal
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    MishmashBuilder<T> addBigDecimal(BigDecimal decimal)
            throws MishmashException;

    /**
     * Add a decimal member.
     *
     * @param decimal - the decimal
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    MishmashBuilder<T> addDouble(double decimal) throws MishmashException;

    /**
     * Add a decimal member.
     *
     * @param decimal - the decimal
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    MishmashBuilder<T> addDecimalString(String decimal)
            throws MishmashException;

    /**
     * Add a decimal member.
     *
     * @param decimal - the decimal
     * @param isSigned - if signed or unsigned
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    MishmashBuilder<T> addInteger(int decimal, boolean isSigned)
            throws MishmashException;

    /**
     * Add a decimal member.
     *
     * @param decimal - the decimal
     * @param isSigned - if signed or unsigned
     * @return - a MishmashBuilder instance
     * @throws MishmashException - if an error occurs
     *          (refer to a specific implementation).
     */
    MishmashBuilder<T> addLong(long decimal, boolean isSigned)
            throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> setClientOptions(Map<String, String> opts);

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> setMutationType(int mt); // FIXME: not integer!

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> enterUnion() throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> leaveUnion() throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> enterIntersection() throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> leaveIntersection() throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> addPredefinedSet(PredefinedSetType setType)
            throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    default MishmashBuilder<T> addPredefinedSetParent()
            throws MishmashException {
        return addPredefinedSet(PredefinedSetType.parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default MishmashBuilder<T> addPredefinedSetChild()
            throws MishmashException {
        return addPredefinedSet(PredefinedSetType.child);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default MishmashBuilder<T> addPredefinedSetSiblings()
            throws MishmashException {
        return addPredefinedSet(PredefinedSetType.siblings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default MishmashBuilder<T> addPredefinedSetAncestors()
            throws MishmashException {
        return addPredefinedSet(PredefinedSetType.ancestors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default MishmashBuilder<T> addPredefinedSetDescendants()
            throws MishmashException {
        return addPredefinedSet(PredefinedSetType.descendants);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> enterTransformation() throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> leaveTransformation() throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> enterTransformationLeft() throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> leaveTransformationLeft() throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> enterTransformationRight() throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> leaveTransformationRight() throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> enterPredefinedFunction(String name)
            throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> leavePredefinedFunction(String name)
            throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> enterPredefinedFunctionArgument(int argumentNo)
            throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> leavePredefinedFunctionArgument(int argumentNo)
            throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> enterNewLambdaScope(String runtime, String lambdaName,
            String scopeId)
            throws MishmashException;

    /**
     * Add source code to the mishmash.
     *
     * @param clientEnv - the client run-time environment (language)
     * @param lambdaName - the name of the lambda, if given
     * @param source - the source code
     * @return - a MishmashBuilder
     * @throws MishmashException - if checks fail
     */
    MishmashBuilder<T> addSource(String clientEnv,
            String lambdaName, String source)
                    throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> leaveNewLambdaScope(String runtime,
            String lambdaName, String scopeId)
            throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> enterExistingLambdaScope(String scopeId)
            throws MishmashException;

    /**
     * {@inheritDoc}
     */
    @Override
    MishmashBuilder<T> leaveExistingLambdaScope(String scopeId)
            throws MishmashException;

}
