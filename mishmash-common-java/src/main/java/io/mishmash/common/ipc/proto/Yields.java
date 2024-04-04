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
package io.mishmash.common.ipc.proto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import io.mishmash.common.data.BaseDataPoint;
import io.mishmash.common.data.Key;
import io.mishmash.common.data.Value;
import io.mishmash.common.exception.MishmashInvalidArgumentException;
import io.mishmash.common.rpc.MishmashRpc.YieldData;
import io.mishmash.common.rpc.MishmashRpc.YieldDataAck;
import io.mishmash.common.rpc.MishmashRpc.YieldValue;

/**
 * Helper methods to build Yield and YieldAck GRPC messages.
 *
 */
public final class Yields {

    /**
     * The Yields class should not be instantiated.
     */
    private Yields() {
        // Hiding the constructor.
    }

    /**
     * Build a YieldData for a {@link BaseDataPoint}.
     *
     * @param dataPoint - the data point
     * @return Builder - a YieldData builder
     * @throws MishmashInvalidArgumentException - on unknown value types
     */
    public static YieldData.Builder
            yield(final BaseDataPoint<Key, Value> dataPoint)
                throws MishmashInvalidArgumentException {
        Value val = dataPoint.value();

        if (val.isNull()) {
            return yieldNull(dataPoint.keys());
        } else if (val.isBoolean()) {
            return yieldBoolean(dataPoint.keys(),
                    val.getBoolean(),
                    val.getInstance());
        } else if (val.isDecimal()) {
            if (val.isNaN()) {
                return yieldDecimal(dataPoint.keys(),
                        Double.NaN,
                        val.getInstance());
            } else if (val.isMinusInf()) {
                return yieldDecimal(dataPoint.keys(),
                        Double.NEGATIVE_INFINITY,
                        val.getInstance());
            } else if (val.isPlusInf()) {
                return yieldDecimal(dataPoint.keys(),
                        Double.POSITIVE_INFINITY,
                        val.getInstance());
            } else if (val.hasFraction()) {
                if (val.fits64Bits()) {
                    return yieldDecimal(dataPoint.keys(),
                            val.getDouble(),
                            val.getInstance());
                } else {
                    return yieldDecimal(dataPoint.keys(),
                            val.getBigDecimal(),
                            val.getInstance());
                }
            } else {
                if (val.hasSign()) {
                    if (val.fits32Bits()) {
                        return yieldDecimalSigned(dataPoint.keys(),
                                val.getInt(),
                                val.getInstance());
                    } else if (val.fits64Bits()) {
                        return yieldDecimalSigned(dataPoint.keys(),
                                val.getLong(),
                                val.getInstance());
                    } else {
                        return yieldDecimal(dataPoint.keys(),
                                val.getBigInteger(),
                                val.getInstance());
                    }
                } else {
                    if (val.fits32Bits()) {
                        return yieldDecimalUnsigned(dataPoint.keys(),
                                val.getInt(),
                                val.getInstance());
                    } else if (val.fits64Bits()) {
                        return yieldDecimalUnsigned(dataPoint.keys(),
                                val.getInt(),
                                val.getInstance());
                    } else {
                        return yieldDecimal(dataPoint.keys(),
                                val.getBigInteger(),
                                val.getInstance());
                    }
                }
            }
        } else if (val.isString()) {
            return yieldString(dataPoint.keys(),
                    val.getString(),
                    val.getInstance());
        } else if (val.isDate()) {
            return yieldDate(dataPoint.keys(),
                    val.getDate(),
                    val.getInstance());
        } else if (val.isBuffer()) {
            return yieldBuffer(dataPoint.keys(),
                    val.getBufferBytes(),
                    val.getInstance());
        } else {
            throw new MishmashInvalidArgumentException();
        }
    }

    /**
     * Return a {@link BaseDataPoint} of {@link Key} and {@link Value} from a
     * GRPC YieldData.
     *
     * @param yield - the GRPC YieldData
     * @return - a {@link BaseDataPoint}
     */
    public static BaseDataPoint<Key, Value>
            fromYieldData(final YieldData yield) {
        return new BaseDataPoint<Key, Value>() {

            @Override
            public Iterable<Key> keys() {
                return () -> {
                    return Members.fromMembers(yield);
                };
            }

            @Override
            public Value value() {
                return Values.fromValue(yield.getValue());
            }

        };
    }

    /**
     * Build a YieldData for a null value.
     *
     * @param members - the member hierarchy
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder yieldNull(
            final Iterable<Key> members) {
        return Members.toMembers(members).setValue(nullValue());
    }

    /**
     * Build a YieldData for a boolean value.
     *
     * @param members - the member hierarchy
     * @param value - the value
     * @param valueId - the id of the value
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder yieldBoolean(
            final Iterable<Key> members,
            final boolean value, final String valueId) {
        return Members.toMembers(members)
                .setValue(booleanValue(value, valueId));
    }

    /**
     * Build a YieldData for a decimal unsigned int.
     *
     * @param members - the member hierarchy
     * @param value - the value
     * @param valueId - the id of the value
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder yieldDecimalUnsigned(
            final Iterable<Key> members,
            final int value, final String valueId) {
        return Members.toMembers(members)
                .setValue(decimalUnsignedValue(value, valueId));
    }

    /**
     * Build a YieldData for a decimal signed int.
     *
     * @param members - the member hierarchy
     * @param value - the value
     * @param valueId - the id of the value
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder yieldDecimalSigned(
            final Iterable<Key> members,
            final int value, final String valueId) {
        return Members.toMembers(members)
                .setValue(decimalSignedValue(value, valueId));
    }

    /**
     * Build a YieldData for a decimal unsigned long.
     *
     * @param members - the member hierarchy
     * @param value - the value
     * @param valueId - the id of the value
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder yieldDecimalUnsigned(
            final Iterable<Key> members,
            final long value, final String valueId) {
        return Members.toMembers(members)
                .setValue(decimalUnsignedValue(value, valueId));
    }

    /**
     * Build a YieldData for a decimal signed long.
     *
     * @param members - the member hierarchy
     * @param value - the value
     * @param valueId - the id of the value
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder yieldDecimalSigned(
            final Iterable<Key> members,
            final long value, final String valueId) {
        return Members.toMembers(members)
                .setValue(decimalSignedValue(value, valueId));
    }

    /**
     * Build a YieldData for a double.
     *
     * @param members - the member hierarchy
     * @param value - the value
     * @param valueId - the id of the value
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder yieldDecimal(
            final Iterable<Key> members,
            final double value, final String valueId) {
        return Members.toMembers(members)
                .setValue(decimalValue(value, valueId));
    }

    /**
     * Build a YieldData for a String Decimal.
     *
     * @param members - the member hierarchy
     * @param value - the value
     * @param valueId - the id of the value
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder yieldDecimal(
            final Iterable<Key> members,
            final String value, final String valueId) {
        return Members.toMembers(members)
                .setValue(decimalValue(value, valueId));
    }

    /**
     * Build a YieldData for a BigDecimal.
     *
     * @param members - the member hierarchy
     * @param value - the value
     * @param valueId - the id of the value
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder yieldDecimal(
            final Iterable<Key> members,
            final BigDecimal value, final String valueId) {
        return Members.toMembers(members)
                .setValue(decimalValue(value, valueId));
    }

    /**
     * Build a YieldData for a BigInteger.
     *
     * @param members - the member hierarchy
     * @param value - the value
     * @param valueId - the id of the value
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder yieldDecimal(
            final Iterable<Key> members,
            final BigInteger value, final String valueId) {
        return Members.toMembers(members)
                .setValue(decimalValue(value, valueId));
    }

    /**
     * Build a YieldData for a String.
     *
     * @param members - the member hierarchy
     * @param value - the value
     * @param valueId - the id of the value
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder yieldString(
            final Iterable<Key> members,
            final String value, final String valueId) {
        return Members.toMembers(members)
                .setValue(stringValue(value, valueId));
    }

    /**
     * Build a YieldData for a DateTime.
     *
     * @param members - the member hierarchy
     * @param value - the value
     * @param valueId - the id of the value
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder yieldDate(
            final Iterable<Key> members,
            final ZonedDateTime value, final String valueId) {
        return Members.toMembers(members)
                .setValue(dateValue(value, valueId));
    }

    /**
     * Build a YieldData for a Buffer.
     *
     * @param members - the member hierarchy
     * @param value - the value
     * @param valueId - the id of the value
     * @return Builder - a YeidData builder
     */
    public static YieldData.Builder yieldBuffer(
            final Iterable<Key> members,
            final byte[] value, final String valueId) {
        return Members.toMembers(members)
                .setValue(bufferValue(value, valueId));
    }

    /**
     * Build a YieldValue for a null.
     *
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder nullValue() {
        return YieldValue.newBuilder()
                .setValue(Values.nullValue());
    }

    /**
     * Build a YieldValue for a boolean.
     *
     * @param value - the mishmash value
     * @param instanceId - the value instance id
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder booleanValue(final boolean value,
            final String instanceId) {
        return YieldValue.newBuilder()
                .setInstanceId(Members.id(instanceId))
                .setValue(Values.booleanValue(value));
    }

    /**
     * Build a YieldValue for an unsigned int.
     *
     * @param value - the mishmash value
     * @param instanceId - the value instance id
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder decimalUnsignedValue(final int value,
            final String instanceId) {
        return YieldValue.newBuilder()
                .setInstanceId(Members.id(instanceId))
                .setValue(Values.decimalValueUnsigned(value));
    }

    /**
     * Build a YieldValue for a signed int.
     *
     * @param value - the mishmash value
     * @param instanceId - the value instance id
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder decimalSignedValue(final int value,
            final String instanceId) {
        return YieldValue.newBuilder()
                .setInstanceId(Members.id(instanceId))
                .setValue(Values.decimalValueSigned(value));
    }

    /**
     * Build a YieldValue for an unsigned long.
     *
     * @param value - the mishmash value
     * @param instanceId - the value instance id
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder decimalUnsignedValue(final long value,
            final String instanceId) {
        return YieldValue.newBuilder()
                .setInstanceId(Members.id(instanceId))
                .setValue(Values.decimalValueUnsigned(value));
    }

    /**
     * Build a YieldValue for a signed long.
     *
     * @param value - the mishmash value
     * @param instanceId - the value instance id
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder decimalSignedValue(final long value,
            final String instanceId) {
        return YieldValue.newBuilder()
                .setInstanceId(Members.id(instanceId))
                .setValue(Values.decimalValueUnsigned(value));
    }

    /**
     * Build a YieldValue for a double.
     *
     * @param value - the mishmash value
     * @param instanceId - the value instance id
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder decimalValue(final double value,
            final String instanceId) {
        return YieldValue.newBuilder()
                .setInstanceId(Members.id(instanceId))
                .setValue(Values.decimalValue(value));
    }

    /**
     * Build a YieldValue for a String Decimal.
     *
     * @param value - the mishmash value
     * @param instanceId - the value instance id
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder decimalValue(final String value,
            final String instanceId) {
        return YieldValue.newBuilder()
                .setInstanceId(Members.id(instanceId))
                .setValue(Values.decimalValue(value));
    }

    /**
     * Build a YieldValue for a BigDecimal.
     *
     * @param value - the mishmash value
     * @param instanceId - the value instance id
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder decimalValue(final BigDecimal value,
            final String instanceId) {
        return YieldValue.newBuilder()
                .setInstanceId(Members.id(instanceId))
                .setValue(Values.decimalValue(value));
    }

    /**
     * Build a YieldValue for a BigInteger.
     *
     * @param value - the mishmash value
     * @param instanceId - the value instance id
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder decimalValue(final BigInteger value,
            final String instanceId) {
        return YieldValue.newBuilder()
                .setInstanceId(Members.id(instanceId))
                .setValue(Values.decimalValue(value));
    }

    /**
     * Build a YieldValue for a String.
     *
     * @param value - the mishmash value
     * @param instanceId - the value instance id
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder stringValue(final String value,
            final String instanceId) {
        return YieldValue.newBuilder()
                .setInstanceId(Members.id(instanceId))
                .setValue(Values.stringValue(value));
    }

    /**
     * Build a YieldValue for a DateTime.
     *
     * @param value - the mishmash value
     * @param instanceId - the value instance id
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder dateValue(final ZonedDateTime value,
            final String instanceId) {
        return YieldValue.newBuilder()
                .setInstanceId(Members.id(instanceId))
                .setValue(Values.dateValue(value));
    }

    /**
     * Build a YieldValue for a Buffer.
     *
     * @param value - the mishmash value
     * @param instanceId - the value instance id
     * @return Builder - a YieldValue builder
     */
    public static YieldValue.Builder bufferValue(final byte[] value,
            final String instanceId) {
        return YieldValue.newBuilder()
                .setInstanceId(Members.id(instanceId))
                .setValue(Values.bufferValue(value));
    }

    /**
     * Build a YieldDataAck.
     *
     * @return Builder - a YieldAck builder
     */
    public static YieldDataAck.Builder yieldDataAck() {
        return YieldDataAck.newBuilder();
    }

}
