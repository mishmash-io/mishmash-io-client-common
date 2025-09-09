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
package io.mishmash.common.ipc.proto;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import io.mishmash.common.data.MishmashBuilder;
import io.mishmash.common.exception.MishmashException;
import io.mishmash.common.exception.MishmashInternalErrorException;
import io.mishmash.common.rpc.MishmashRpc.DateValue;
import io.mishmash.common.rpc.MishmashRpc.DecimalValue;
import io.mishmash.common.rpc.MishmashRpc.Intersection;
import io.mishmash.common.rpc.MishmashRpc.LambdaFunction;
import io.mishmash.common.rpc.MishmashRpc.Literal;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetDescriptor;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetDescriptorList;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetup;
import io.mishmash.common.rpc.MishmashRpc.PredefinedSet;
import io.mishmash.common.rpc.MishmashRpc.Transformation;
import io.mishmash.common.rpc.MishmashRpc.Transformation.Axis;
import io.mishmash.common.rpc.MishmashRpc.Union;
import io.mishmash.common.rpc.MishmashRpc.Value;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetup.MutationType;
import io.mishmash.common.rpc.MishmashRpc.PredefinedFunction;

/**
 * A helper class for SETUP-related work.
 */
public final class ProtoSetup {

    /**
     * Instances of ProtoSetup are not needed.
     */
    private ProtoSetup() {

    }

    /**
     * Parse a DateValue and build it into a {@link MishmashBuilder}.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param date - the date value
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromDate(final DateValue date, final MishmashBuilder<MT> builder)
                    throws MishmashException {
        String iso8601 = Proto.ensureNonEmpty(
                date.getIso8601(),
                "DATE VALUE cannot be empty");

        try {
            ZonedDateTime dt = ZonedDateTime.parse(
                    iso8601,
                    DateTimeFormatter.ISO_DATE_TIME);
            return builder.addDate(dt);
        } catch (DateTimeParseException e) {
            Proto.fieldFormatError("DATE VALUE", iso8601, e);
        }

        // should not be reached
        throw new MishmashInternalErrorException();
    }

    /**
     * Build a member from a decimal value.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param decimal - the value
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromDecimal(final DecimalValue decimal,
                    final MishmashBuilder<MT> builder)
                            throws MishmashException {
        DecimalValue.AltCase altCase = Proto.ensureAltCase(
                decimal.getAltCase(),
                DecimalValue.AltCase.ALT_NOT_SET,
                "DECIMAL VALUE Type not set");

        switch (altCase) {
        case BIG_DECIMAL:
            // FIXME:
            Proto.unimplementedAltCase(altCase);
            break;
        case FLOATING:
            return builder.addDouble(decimal.getFloating());
        case STRING_SEQUENCE:
            return builder.addDecimalString(
                    Proto.ensureNonEmpty(
                            decimal.getStringSequence(),
                            "DECIMAL STRING cannot be empty"));
        case S_INT_32:
            return builder.addInteger(decimal.getSInt32(), true);
        case S_INT_64:
            return builder.addLong(decimal.getSInt64(), true);
        case U_INT_32:
            return builder.addInteger(decimal.getUInt32(), false);
        case U_INT_64:
            return builder.addLong(decimal.getUInt64(), false);
        default:
            Proto.unexpectedAltCase(altCase);
        }

        // should not be reached
        throw new MishmashInternalErrorException();
    }

    /**
     * Build a member from a protocol value.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param value - the value
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromValue(final Value value, final MishmashBuilder<MT> builder)
                    throws MishmashException {
        Value.AltCase altCase = Proto.ensureAltCase(
                value.getAltCase(),
                Value.AltCase.ALT_NOT_SET,
                "LITERAL VALUE Type not set");

        switch (altCase) {
        case BOOLEAN:
            return builder.addBoolean(
                    Proto.ensureNonNull(
                            value.getBoolean(),
                            "BOOLEAN VALUE cannot be null")
                        .getBoolean());
        case BUFFER:
            Proto.unimplementedAltCase(altCase);
            break;
        case DATE:
            return fromDate(
                    Proto.ensureNonNull(
                            value.getDate(),
                            "DATE VALUE cannot be null"),
                    builder);
        case DECIMAL:
            return fromDecimal(
                    Proto.ensureNonNull(
                            value.getDecimal(),
                            "DECIMAL VALUE cannot be null"),
                    builder);
        case NULL:
            return builder.addNull();
        case STRING:
            return builder.addString(
                    Proto.ensureNonEmpty(
                            Proto.ensureNonNull(
                                    value.getString(),
                                    "STRING VALUE cannot be null")
                                .getSequence(),
                    "STRING VALUE cannot be empty"));
        default:
            Proto.unexpectedAltCase(altCase);
        }

        // should not be reached
        throw new MishmashInternalErrorException();
    }

    /**
     * Build a literal member.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param literal - the literal
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromLiteral(final Literal literal,
                    final MishmashBuilder<MT> builder)
                            throws MishmashException {
        Literal.AltCase altCase = Proto.ensureAltCase(literal.getAltCase(),
                Literal.AltCase.ALT_NOT_SET, "LITERAL Type not set");

        if (altCase == Literal.AltCase.ID) {
            return builder.addInstance(
                    Proto.ensureNonEmpty(
                            Proto.ensureNonNull(
                                    literal.getId(),
                                    "LITERAL ID cannot be null")
                                .getId(),
                            "LITERAL ID cannot be empty"));
        } else if (altCase == Literal.AltCase.VALUE) {
            return fromValue(
                    Proto.ensureNonNull(
                            literal.getValue(),
                            "LITERAL VALUE cannot be null"),
                    builder);
        } else {
            Proto.unexpectedAltCase(altCase);
        }

        // should not be reached
        throw new MishmashInternalErrorException();
    }

    /**
     * Build from a protocol union message.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param u - the union
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromUnion(final Union u, final MishmashBuilder<MT> builder)
                    throws MishmashException {
        MishmashBuilder<MT> res = builder;
        MishmashSetDescriptorList descriptors = Proto.ensureNonNull(
                u.getSets(),
                "UNION members cannot be null");

        if (descriptors.getEntriesCount() > 0) {
            res = res.enterUnion();
            res = fromDescriptors(descriptors, res);
            res = res.leaveUnion();
        }

        return res;
    }

    /**
     * Build from a protocol intersection message.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param i - the intersection
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromIntersection(final Intersection i,
                    final MishmashBuilder<MT> builder)
                            throws MishmashException {
        MishmashBuilder<MT> res = builder;
        MishmashSetDescriptorList descriptors = Proto.ensureNonNull(
                i.getSets(),
                "INTERSECTION members cannot be null");

        if (descriptors.getEntriesCount() > 0) {
            res = res.enterIntersection();
            res = fromDescriptors(descriptors, res);
            res = res.leaveIntersection();
        }

        return res;
    }

    /**
     * Build from a protocol predefined set.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param set - the predefined set
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromPredefinedSet(final PredefinedSet set,
                    final MishmashBuilder<MT> builder)
                            throws MishmashException {
        MishmashBuilder<MT> res = builder;
        PredefinedSet.identifier identifier = Proto.ensureNonNull(
                set.getIdent(),
                "PREDEFINED SET ID cannot be null");

        switch (identifier) {
        case ANCESTORS:
            res = res.addPredefinedSetAncestors();
            break;
        case CHILD:
            res = res.addPredefinedSetChild();
            break;
        case DESCENDANTS:
            res = res.addPredefinedSetDescendants();
            break;
        case PARENT:
            res = res.addPredefinedSetParent();
            break;
        case SIBLINGS:
            res = res.addPredefinedSetSiblings();
            break;
        case UNRECOGNIZED:
            Proto.unimplementedAltCase(identifier);
            break;
        default:
            Proto.unexpectedAltCase(identifier);
            break;
        }

        return res;
    }

    /**
     * Build from a protocol predefined function.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param function - the function
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromPredefinedFunction(final PredefinedFunction function,
                    final MishmashBuilder<MT> builder)
                            throws MishmashException {
        MishmashBuilder<MT> res = builder;
        String functionName = Proto.ensureNonEmpty(
                function.getName(),
                "PREDEFINED FUNC Name cannot be empty");
        int argsCnt = function.getArgumentsCount();

        res = res.enterPredefinedFunction(functionName);
        for (int i = 0; i < argsCnt; i++) {
            res = res.enterPredefinedFunctionArgument(i);
            res = fromDescriptor(
                    Proto.ensureNonNull(
                            function.getArguments(i),
                            "PREDEFINED FUNC Arg cannot be null"),
                    res);
            res = res.leavePredefinedFunctionArgument(i);
        }

        return res.leavePredefinedFunction(functionName);
    }

    /**
     * Build from a protocol transform.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param transform - the transform
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromTransformation(final Transformation transform,
                    final MishmashBuilder<MT> builder)
                            throws MishmashException {
        MishmashBuilder<MT> res = builder;
        Axis axes = Proto.ensureNonNull(
                transform.getAxis(),
                "TRANSFORM AXES cannot be null");
        MishmashSetDescriptorList left = Proto.ensureNonNull(
                axes.getLeft(),
                "TRANSFORM LEFT AXIS cannot be null");
        MishmashSetDescriptorList right = Proto.ensureNonNull(
                axes.getRight(),
                "TRANSFORM RIGHT AXIS cannot be null");

        res = res
                .enterIntersection()
                .enterTransformationLeft();
        res = fromDescriptors(left, res);

        res = res
                .leaveTransformationLeft()
                .enterTransformationRight();
        res = fromDescriptors(right, res);

        return res.leaveTransformationRight()
                .leaveTransformation();
    }

    /**
     * Build from a lambda function.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param function - the lambda function
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if build or protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromLambda(final LambdaFunction function,
                    final MishmashBuilder<MT> builder)
                            throws MishmashException {
        String clientRuntime = Proto.ensureNonEmpty(
                function.getClientRuntime(),
                "Client Run-time environment cannot be empty");
        String functionName = function.getName(); // can be missing
        String scopeId = Proto.ensureNonEmpty(
                function.getScopeId(),
                "SCOPE ID cannot be empty");

        MishmashBuilder<MT> res = builder
                .enterNewLambdaScope(clientRuntime, functionName, scopeId);

        res = res.addSource(clientRuntime, functionName,
                Proto.ensureNonEmpty(
                        Proto.ensureNonNull(function.getBody(),
                                "FUNCTION BODY cannot be null")
                            .getSource(),
                        "FUNCTION CODE cannot be empty"));

        return res.leaveNewLambdaScope(clientRuntime, functionName, scopeId);
    }

    /**
     * Build from a protocol descriptor.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param descriptor - the descriptor
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromDescriptor(final MishmashSetDescriptor descriptor,
                    final MishmashBuilder<MT> builder)
                            throws MishmashException {
        MishmashSetDescriptor.AltCase altCase = Proto.ensureAltCase(
                descriptor.getAltCase(),
                MishmashSetDescriptor.AltCase.ALT_NOT_SET,
                "SET DESCRIPTOR Type not set");

        switch (altCase) {
        case INTERSECTION:
            return fromIntersection(
                    Proto.ensureNonNull(
                            descriptor.getIntersection(),
                            "INTERSECTION cannot be null"),
                    builder);
        case LAMBDA_FUNCTION:
            return fromLambda(
                    Proto.ensureNonNull(
                            descriptor.getLambdaFunction(),
                            "LAMBDA FUNCTION cannot be null"),
                    builder);
        case LITERAL:
            return fromLiteral(
                    Proto.ensureNonNull(
                            descriptor.getLiteral(),
                            "LITERAL cannot be null"),
                    builder);
        case PREDEFINED_FUNCTION:
            return fromPredefinedFunction(
                    Proto.ensureNonNull(
                            descriptor.getPredefinedFunction(),
                            "SET PREDEFINED FUNC cannot be null"),
                    builder);
        case PREDEFINED_SET:
            return fromPredefinedSet(
                    Proto.ensureNonNull(
                            descriptor.getPredefinedSet(),
                            "PREDEFINED SET cannot be null"),
                    builder);
        case TRANSFORMATION:
            return fromTransformation(
                    Proto.ensureNonNull(
                            descriptor.getTransformation(),
                            "TRANSFORM cannot be null"),
                    builder);
        case UNION:
            return fromUnion(
                    Proto.ensureNonNull(
                            descriptor.getUnion(),
                            "UNION cannot be null"),
                    builder);
        default:
            Proto.unexpectedAltCase(altCase);
            break;
        }

        // should not be reached
        throw new MishmashInternalErrorException();
    }

    /**
     * Build from a list of protocol descriptors.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param descriptors - the list of descriptors
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromDescriptors(final MishmashSetDescriptorList descriptors,
                    final MishmashBuilder<MT> builder)
                            throws MishmashException {
        MishmashBuilder<MT> res = builder;

        for (int i = 0; i < descriptors.getEntriesCount(); i++) {
            res = fromDescriptor(
                    Proto.ensureNonNull(
                            descriptors.getEntries(i),
                            "SET DESCRIPTOR cannot be null"),
                    res);
        }

        return res;
    }

    /**
     * Build from a protocol SETUP message.
     *
     * @param <MT> the Class of objects built by the {@link MishmashBuilder}
     * @param setup - the setup message
     * @param builder - the builder
     * @return - a builder instance
     * @throws MishmashException - if protocol checks fail
     */
    public static <MT> MishmashBuilder<MT>
            fromSetup(final MishmashSetup setup,
                    final MishmashBuilder<MT> builder)
                            throws MishmashException {
        MishmashBuilder<MT> res = builder;
        Map<String, String> opts = setup.getClientOptionsMap();
        if (opts != null) {
            res = res.setClientOptions(opts);
        }

        MutationType mutationType = setup.getMutationType();
        if (mutationType != null) {
            res = res.setMutationType(mutationType.getNumber());
        }

        return fromDescriptors(
                Proto.ensureNonNull(
                        setup.getTargetSet(),
                        "SETUP has no TARGET"),
                res);
    }
}
