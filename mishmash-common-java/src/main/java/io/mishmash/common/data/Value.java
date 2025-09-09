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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;

/**
 * A value in a {@link Mishmash} {@link DataPoint}.
 */
public final class Value extends Instance {

    /**
     * The value type.
     */
    private ValueType type;

    /**
     * The boolean value (if boolean).
     */
    private Boolean booleanValue;

    /**
     * The String value (if string).
     */
    private String stringValue;

    /**
     * The date/time value (if date/time).
     */
    private ZonedDateTime dateValue;

    /**
     * The 32bit value if decimal.
     */
    private Integer intValue;

    /**
     * The 64bit value if decimal.
     */
    private Long longValue;

    /**
     * The 64bit fractional value if decimal.
     */
    private Double doubleValue;

    /**
     * The value if decimal, fractional and big.
     */
    private BigDecimal bigDecimalValue;

    /**
     * The value if decimal and big.
     */
    private BigInteger bigIntegerValue;

    /**
     * Create a new Value of the given type and instanceId.
     *
     * @param t - the {@link ValueType}
     * @param instanceId - the instance ID
     */
    private Value(final ValueType t, final String instanceId) {
        super(instanceId);
        this.type = t;
    }

    /**
     * Create a new boolean Value.
     *
     * @param b - the boolean
     * @param instanceId - the instance ID
     */
    private Value(final boolean b, final String instanceId) {
        this(ValueType.booleanValue, instanceId);
        this.booleanValue = b;
    }

    /**
     * Create a new String Value.
     *
     * @param str - the String
     * @param instanceId - the instance ID
     */
    private Value(final String str, final String instanceId) {
        this(ValueType.stringValue, instanceId);
        this.stringValue = str;
    }

    /**
     * Create a new Date/Time Value.
     *
     * @param dt - the date/time
     * @param instanceId - the instance ID
     */
    private Value(final ZonedDateTime dt, final String instanceId) {
        this(ValueType.dateValue, instanceId);
        this.dateValue = dt;
    }

    /**
     * Create a new Decimal Value.
     *
     * @param d - the decimal
     * @param instanceId - the instance ID
     */
    private Value(final BigDecimal d, final String instanceId) {
        this(ValueType.decimalValue, instanceId);
        this.bigDecimalValue = d;
    }

    /**
     * Create a new Decimal Value.
     *
     * @param d - the decimal
     * @param instanceId - the instance ID
     */
    private Value(final BigInteger d, final String instanceId) {
        this(ValueType.decimalValue, instanceId);
        this.bigIntegerValue = d;
    }

    /**
     * Create a new Decimal Value.
     *
     * @param d - the decimal
     * @param instanceId - the instance ID
     */
    private Value(final int d, final String instanceId) {
        this(ValueType.decimalValue, instanceId);
        this.intValue = d;
    }

    /**
     * Create a new Decimal Value.
     *
     * @param d - the decimal
     * @param instanceId - the instance ID
     */
    private Value(final long d, final String instanceId) {
        this(ValueType.decimalValue, instanceId);
        this.longValue = d;
    }

    /**
     * Create a new Decimal Value.
     *
     * @param d - the decimal
     * @param instanceId - the instance ID
     */
    private Value(final double d, final String instanceId) {
        this(ValueType.decimalValue, instanceId);
        this.doubleValue = d;
    }

    /**
     * Check if value is of null type.
     *
     * @return - true if value is null
     */
    public boolean isNull() {
        return ValueType.nullValue == type;
    }

    /**
     * Check if value is of boolean type.
     *
     * @return - true if value is a boolean
     */
    public boolean isBoolean() {
        return ValueType.booleanValue == type;
    }

    /**
     * Return the boolean value or null if this value is not a boolean.
     *
     * @return - the boolean value
     */
    public boolean getBoolean() {
        return booleanValue;
    }

    /**
     * Check if value is of decimal type.
     *
     * @return - true if value is a decimal
     */
    public boolean isDecimal() {
        return ValueType.decimalValue == type;
    }

    /**
     * Check if this decimal is Not-A-Number value.
     *
     * @return - true if NaN
     */
    public boolean isNaN() {
        return doubleValue != null && doubleValue.isNaN();
    }

    /**
     * Check if this decimal is +Infinity.
     *
     * @return - true if +Infinity
     */
    public boolean isPlusInf() {
        return doubleValue != null
                && doubleValue.equals(Double.POSITIVE_INFINITY);
    }

    /**
     * Check if this decimal is -Infinity.
     *
     * @return - true if -Infinity
     */
    public boolean isMinusInf() {
        return doubleValue != null
                && doubleValue.equals(Double.NEGATIVE_INFINITY);
    }

    /**
     * Check if this decimal is a signed one.
     *
     * @return - true if decimal is signed
     */
    public boolean hasSign() {
        return (intValue != null && Integer.signum(intValue) < 0)
                || (longValue != null && Long.signum(longValue) < 0)
                || isDecimal();
    }

    /**
     * Check if this decimal has a fractional part.
     *
     * @return - true if decimal is fractional
     */
    public boolean hasFraction() {
        return doubleValue != null || bigDecimalValue != null;
    }

    /**
     * Check if this decimal fits into 32 bits.
     *
     * @return - true if decimal fits into 32 bits.
     */
    public boolean fits32Bits() {
        return intValue != null;
    }

    /**
     * Check if this decimal fits into 64 bits.
     *
     * @return - true if decimal fits into 64 bits.
     */
    public boolean fits64Bits() {
        return longValue != null || doubleValue != null;
    }

    /**
     * Get the 32 bit decimal value.
     *
     * @return - the value
     */
    public int getInt() {
        return intValue;
    }

    /**
     * Get the 64 bit decimal value.
     *
     * @return - the value
     */
    public long getLong() {
        return longValue;
    }

    /**
     * Get the 64 bit fractional decimal.
     *
     * @return - the value
     */
    public Double getDouble() {
        return doubleValue;
    }

    /**
     * Get the big fractional decimal.
     *
     * @return - the value
     */
    public BigDecimal getBigDecimal() {
        return bigDecimalValue;
    }

    /**
     * Get the big whole decimal.
     *
     * @return - the value
     */
    public BigInteger getBigInteger() {
        return bigIntegerValue;
    }

    /**
     * Check if this value is a String value.
     *
     * @return - true if a String value
     */
    public boolean isString() {
        return ValueType.stringValue == type;
    }

    /**
     * Get the String value (if this Value is a string).
     *
     * @return - the String value
     */
    public String getString() {
        return stringValue;
    }

    /**
     * Check if this is a date/time Value.
     *
     * @return - true if value is a date/time
     */
    public boolean isDate() {
        return ValueType.dateValue == type;
    }

    /**
     * Get the date/time value.
     *
     * @return - the date/time
     */
    public ZonedDateTime getDate() {
        return dateValue;
    }

    /**
     * Check if this Value is a buffer.
     *
     * @return - true if value is a buffer
     */
    public boolean isBuffer() {
        return ValueType.bufferValue == type;
    }

    /**
     * Get the buffer bytes as a byte array.
     *
     * @return - the buffer bytes
     */
    public byte[] getBufferBytes() {
        throw new UnsupportedOperationException();
    }

    /**
     * Compose a new NULL Value with the given instance ID.
     *
     * @param instanceId - the value instance ID
     * @return - a new Value
     */
    public static Value ofNull(final String instanceId) {
        return new Value(ValueType.nullValue, instanceId);
    }

    /**
     * Compose a new boolean Value with the given instance ID.
     *
     * @param bool - the value
     * @param instanceId - the instance id
     * @return - a new Value
     */
    public static Value ofBoolean(final boolean bool,
            final String instanceId) {
        return new Value(bool, instanceId);
    }

    /**
     * Compose a new date/time Value with the given instance ID.
     *
     * @param date - the value
     * @param instanceId - the instance id
     * @return - a new Value
     */
    public static Value ofDate(final ZonedDateTime date,
            final String instanceId) {
        return new Value(date, instanceId);
    }

    /**
     * Compose a new Decimal Value with the given instance ID.
     *
     * @param d - the value
     * @param instanceId - the instance id
     * @return - a new Value
     */
    public static Value ofDouble(final double d,
            final String instanceId) {
        return new Value(d, instanceId);
    }

    /**
     * Compose a new String Value with the given instance ID.
     *
     * @param str - the value
     * @param instanceId - the instance id
     * @return - a new Value
     */
    public static Value ofString(final String str,
            final String instanceId) {
        return new Value(str, instanceId);
    }

    /**
     * Compose a new Decimal Value with the given instance ID.
     *
     * @param i - the value
     * @param instanceId - the instance id
     * @return - a new Value
     */
    public static Value ofInt(final int i,
            final String instanceId) {
        return new Value(i, instanceId);
    }

    /**
     * Compose a new Decimal Value with the given instance ID.
     *
     * @param l - the value
     * @param instanceId - the instance id
     * @return - a new Value
     */
    public static Value ofLong(final long l,
            final String instanceId) {
        return new Value(l, instanceId);
    }

    /**
     * Compose a new Decimal Value with the given instance ID.
     *
     * @param i - the value
     * @param instanceId - the instance id
     * @return - a new Value
     */
    public static Value ofBigInteger(final BigInteger i,
            final String instanceId) {
        return new Value(i, instanceId);
    }

    /**
     * Compose a new Decimal Value with the given instance ID.
     *
     * @param d - the value
     * @param instanceId - the instance id
     * @return - a new Value
     */
    public static Value ofBigDecimal(final BigDecimal d,
            final String instanceId) {
        return new Value(d, instanceId);
    }

    /**
     * Represents the type of a Value.
     */
    public enum ValueType {
        /**
         * NULL Value Type.
         */
        nullValue,

        /**
         * BOOLAN Value Type.
         */
        booleanValue,

        /**
         * DECIMAL Value Type.
         */
        decimalValue,

        /**
         * STRING Value Type.
         */
        stringValue,

        /**
         * DATE/TIME Value Type.
         */
        dateValue,

        /**
         * BUFFER Value Type.
         */
        bufferValue
    }
}
