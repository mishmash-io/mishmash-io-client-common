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
import java.time.format.DateTimeFormatter;

import com.google.protobuf.ByteString;

import io.mishmash.common.rpc.MishmashRpc.BigEndianDecimal;
import io.mishmash.common.rpc.MishmashRpc.BooleanValue;
import io.mishmash.common.rpc.MishmashRpc.BufferValue;
import io.mishmash.common.rpc.MishmashRpc.DateValue;
import io.mishmash.common.rpc.MishmashRpc.DecimalValue;
import io.mishmash.common.rpc.MishmashRpc.Id;
import io.mishmash.common.rpc.MishmashRpc.NullValue;
import io.mishmash.common.rpc.MishmashRpc.StringValue;
import io.mishmash.common.rpc.MishmashRpc.Value;
import io.mishmash.common.rpc.MishmashRpc.YieldValue;

/**
 * Helper methods to build Value GRPC messages.
 *
 */
public final class Values {

    /**
     * Ensures unsinged int to long transformation.
     */
    private static final long U_INT_MASK = 0x0FFFFFFFFL;

    /**
     * Gets all bits except sign bit.
     */
    private static final long U_LONG_NO_SIGN = 0x7FFFFFFFFFFFFFFFL;

    /**
     * The Yields class should not be instantiated.
     */
    private Values() {
        // Hide the constructor
    }

    /**
     * Create a builder for a null value.
     *
     * @return value builder
     */
    public static Value.Builder nullValue() {
        return Value.newBuilder().setNull(NullValue.newBuilder());
    }

    /**
     * Create a builder for a boolean value.
     *
     * @param b - the boolean
     * @return value builder
     */
    public static Value.Builder booleanValue(final boolean b) {
        return Value.newBuilder()
                .setBoolean(BooleanValue.newBuilder().setBoolean(b));
    }

    /**
     * Create a decimal builder for an unisnged int.
     *
     * @param v - the int
     * @return decimal value builder
     */
    private static DecimalValue.Builder decimalUnsigned(final int v) {
        return DecimalValue.newBuilder().setUInt32(v);
    }

    /**
     * Create a decimal builder for a signed int.
     *
     * @param v - the int
     * @return decimal value builder
     */
    private static DecimalValue.Builder decimalSigned(final int v) {
        return DecimalValue.newBuilder().setSInt32(v);
    }

    /**
     * Create a decimal builder for an unsigned long.
     *
     * @param v - the long
     * @return decimal value builder
     */
    private static DecimalValue.Builder decimalUnsigned(final long v) {
        return DecimalValue.newBuilder().setUInt64(v);
    }

    /**
     * Create a decimal builder for a signed long.
     *
     * @param v - the long
     * @return decimal value builder
     */
    private static DecimalValue.Builder decimalSigned(final long v) {
        return DecimalValue.newBuilder().setSInt64(v);
    }

    /**
     * Create a decimal builder for a double.
     *
     * @param d - the double
     * @return decimal value builder
     */
    private static DecimalValue.Builder decimal(final double d) {
        return DecimalValue.newBuilder().setFloating(d);
    }

    /**
     * Create a decimal builder for a String.
     *
     * @param s - the String
     * @return decimal value builder
     */
    private static DecimalValue.Builder decimal(final String s) {
        return DecimalValue.newBuilder().setStringSequence(s);
    }

    /**
     * Create a decimal builder for a complex decimal.
     *
     * @param sign - the sign bit
     * @param integer - the integer part
     * @param fractional - the fractional part
     * @return decimal value builder
     */
    private static DecimalValue.Builder decimal(final boolean sign,
            final byte[] integer, final byte[] fractional) {
        return DecimalValue.newBuilder().setBigDecimal(
                BigEndianDecimal.newBuilder()
                    .setSign(sign)
                    .setIntegerPart(ByteString.copyFrom(integer))
                    .setFractionalPart(ByteString.copyFrom(fractional))
                );
    }

    /**
     * Create a builder for an unsigned int.
     *
     * @param value - the int
     * @return value builder
     */
    public static Value.Builder decimalValueUnsigned(final int value) {
        return Value.newBuilder()
                .setDecimal(decimalUnsigned(value));
    }

    /**
     * Create a builder for a signed int.
     *
     * @param value - the int
     * @return value builder
     */
    public static Value.Builder decimalValueSigned(final int value) {
        return Value.newBuilder()
                .setDecimal(decimalSigned(value));
    }

    /**
     * Create a builder for an unsigned long.
     *
     * @param value - the long
     * @return value builder
     */
    public static Value.Builder decimalValueUnsigned(final long value) {
        return Value.newBuilder()
                .setDecimal(decimalUnsigned(value));
    }

    /**
     * Create a builder for a signed long.
     *
     * @param value - the long
     * @return value builder
     */
    public static Value.Builder decimalValueSigned(final long value) {
        return Value.newBuilder()
                .setDecimal(decimalSigned(value));
    }

    /**
     * Create a builder for a double.
     *
     * @param value - the double
     * @return value builder
     */
    public static Value.Builder decimalValue(final double value) {
        return Value.newBuilder()
                .setDecimal(decimal(value));
    }

    /**
     * Create a builder for a String Decimal.
     *
     * @param value - the Decimal String
     * @return value builder
     */
    public static Value.Builder decimalValue(final String value) {
        return Value.newBuilder()
                .setDecimal(decimal(value));
    }

    /**
     * Create a builder for a BigDecimal.
     *
     * @param value - the Decimal
     * @return value builder
     */
    public static Value.Builder decimalValue(final BigDecimal value) {
        // for now encode large numbers as string, so that
        // client libraries for languages other than java
        // can work on them too
        return Value.newBuilder()
                .setDecimal(decimal(value.toPlainString()));
    }

    /**
     * Create a builder for a BigInteger.
     *
     * @param value - the Decimal
     * @return value builder
     */
    public static Value.Builder decimalValue(final BigInteger value) {
        // for now encode large numbers as string, so that
        // client libraries for languages other than java
        // can work on them too
        return Value.newBuilder()
                .setDecimal(decimal(value.toString()));
    }

    /**
     * Create a value for a String.
     *
     * @param s - the String
     * @return value builder
     */
    public static Value.Builder stringValue(final String s) {
        return Value.newBuilder()
                .setString(StringValue.newBuilder().setSequence(s));
    }

    /**
     * Create a value for a datetime.
     *
     * @param iso8601 - the date, as an ISO8601 String
     * @return value builder
     */
    public static Value.Builder dateValue(final String iso8601) {
        return Value.newBuilder()
                .setDate(DateValue.newBuilder().setIso8601(iso8601));
    }

    /**
     * Create a value for a datetime.
     *
     * @param dt - the datetime
     * @return value builder
     */
    public static Value.Builder dateValue(final ZonedDateTime dt) {
        return Value.newBuilder()
                .setDate(DateValue.newBuilder()
                        .setIso8601(
                                dt.format(DateTimeFormatter.ISO_DATE_TIME)
                        )
                );
    }

    /**
     * Create a value for a buffer.
     *
     * @param bytes - the buffer
     * @return value builder
     */
    public static Value.Builder bufferValue(final byte[] bytes) {
        return Value.newBuilder()
                .setBuffer(BufferValue.newBuilder()
                        .setBuf(ByteString.copyFrom(bytes)));
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from a GRPC BooleanValue.
     *
     * @param value - the BooleanValue
     * @param id - its Id
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromBoolean(final BooleanValue value, final Id id) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        return io.mishmash.common.data.Value.ofBoolean(
                value.getBoolean(),
                Members.fromId(id));
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from a GRPC NullValue.
     *
     * @param value - the NullValue
     * @param id - its Id
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromNull(final NullValue value, final Id id) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        return io.mishmash.common.data.Value.ofNull(
                Members.fromId(id));
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from a GRPC StringValue.
     *
     * @param value - the StringValue
     * @param id - its Id
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromString(final StringValue value, final Id id) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        return io.mishmash.common.data.Value.ofString(
                value.getSequence(),
                Members.fromId(id));
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from a GRPC DateValue.
     *
     * @param value - the DateValue
     * @param id - its Id
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromDate(final DateValue value, final Id id) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        ZonedDateTime date;
        try {
            date = ZonedDateTime.parse(value.getIso8601(),
                    DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse ISO Date", e);
        }

        return io.mishmash.common.data.Value.ofDate(date,
                Members.fromId(id));
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from a GRPC BufferValue.
     *
     * @param value - the BufferValue
     * @param id - its Id
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromBuffer(final BufferValue value, final Id id) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        throw new UnsupportedOperationException("Buffer values not supported");
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from
     * a GRPC BigEndianDecimal.
     *
     * @param value - the BigEndianDecimal
     * @param id - its Id
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromDecimal(final BigEndianDecimal value, final Id id) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        BigInteger wh = new BigInteger(value.getIntegerPart().toByteArray());
        BigInteger fr = new BigInteger(
                value.getFractionalPart().toByteArray());

        throw new UnsupportedOperationException(
                "BigEndianDecimal values not supported");
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from a double and Id.
     *
     * @param value - the double
     * @param id - its Id
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromDecimal(final double value, final Id id) {
        return io.mishmash.common.data.Value.ofDouble(value,
                Members.fromId(id));
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from a
     * decimal String and Id.
     *
     * @param value - the BooleanValue
     * @param id - its Id
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromDecimal(final String value, final Id id) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        return io.mishmash.common.data.Value.ofBigDecimal(new BigDecimal(value),
                Members.fromId(id));
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from a
     * decimal int and Id.
     *
     * @param value - the value
     * @param id - its Id
     * @param isSigned - if the int should be treated as signed
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromDecimal(final int value, final Id id, final boolean isSigned) {
        if (isSigned) {
            return io.mishmash.common.data.Value.ofInt(value,
                    Members.fromId(id));
        } else {
            return io.mishmash.common.data.Value.ofLong(value & U_INT_MASK,
                    Members.fromId(id));
        }
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from a
     * decimal long and Id.
     *
     * @param value - the value
     * @param id - its Id
     * @param isSigned - if the long should be treated as signed
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromDecimal(final long value, final Id id, final boolean isSigned) {
        if (isSigned) {
            return io.mishmash.common.data.Value.ofLong(value,
                Members.fromId(id));
        } else {
            BigInteger d = BigInteger.valueOf(value & U_LONG_NO_SIGN);
            if (value < 0) {
                d = d.flipBit(Long.SIZE - 1);
            }

            return io.mishmash.common.data.Value.ofBigDecimal(new BigDecimal(d),
                    Members.fromId(id));
        }
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from a GRPC DecimalValue.
     *
     * @param value - the DecimalValue
     * @param id - its Id
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromDecimal(final DecimalValue value, final Id id) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        switch (value.getAltCase()) {
        case ALT_NOT_SET:
            throw new IllegalArgumentException("Value type not set");
        case BIG_DECIMAL:
            return fromDecimal(value.getBigDecimal(), id);
        case FLOATING:
            return fromDecimal(value.getFloating(), id);
        case STRING_SEQUENCE:
            return fromDecimal(value.getStringSequence(), id);
        case S_INT_32:
            return fromDecimal(value.getSInt32(), id, true);
        case S_INT_64:
            return fromDecimal(value.getUInt64(), id, true);
        case U_INT_32:
            return fromDecimal(value.getUInt32(), id, false);
        case U_INT_64:
            return fromDecimal(value.getUInt64(), id, false);
        default:
            throw new IllegalArgumentException("Value of unknown type: "
                    + value.getAltCase().name());
        }
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from a GRPC Value.
     *
     * @param value - the Value
     * @param id - its Id
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromValue(final Value value, final Id id) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        switch (value.getAltCase()) {
        case ALT_NOT_SET:
            throw new IllegalArgumentException("Value type not set");
        case BOOLEAN:
            return fromBoolean(value.getBoolean(), id);
        case BUFFER:
            return fromBuffer(value.getBuffer(), id);
        case DATE:
            return fromDate(value.getDate(), id);
        case DECIMAL:
            return fromDecimal(value.getDecimal(), id);
        case NULL:
            return fromNull(value.getNull(), id);
        case STRING:
            return fromString(value.getString(), id);
        default:
            throw new IllegalArgumentException("Value of unknown type: "
                    + value.getAltCase().name());
        }
    }

    /**
     * Create a {@link io.mishmash.common.data.Value} from a GRPC YieldValue.
     *
     * @param value - the YieldValue
     * @return - a {@link io.mishmash.common.data.Value}
     */
    public static io.mishmash.common.data.Value
            fromValue(final YieldValue value) {
        return fromValue(value.getValue(), value.getInstanceId());
    }
}
