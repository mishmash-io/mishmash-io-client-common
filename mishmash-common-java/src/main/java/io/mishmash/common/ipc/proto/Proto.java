/*
 * Copyright 2020 MISHMASH I O OOD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mishmash.common.ipc.proto;

import io.mishmash.common.exception.MishmashException;
import io.mishmash.common.exception.MishmashInvalidArgumentException;
import io.mishmash.common.exception.MishmashInvalidStateException;
import io.mishmash.common.exception.MishmashProtocolException;
import io.mishmash.common.exception.MishmashUnimplementedException;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetup;
import io.mishmash.common.rpc.MishmashRpc.MutationClientMessage;
import io.mishmash.common.rpc.MishmashRpc.MutationServerMessage;
import io.mishmash.common.rpc.MishmashRpc.StreamClientMessage;
import io.mishmash.common.rpc.MishmashRpc.StreamServerMessage;

/**
 * A helper class for protocol-related work.
 *
 */
public final class Proto {

    /**
     * Instances of the Proto class are not needed.
     */
    private Proto() {

    }

    /**
     * Ensure string is not null or emtpy.
     *
     * @param s - the string
     * @param errorMessage - the message when check fails
     * @return - the string
     * @throws MishmashProtocolException - when the check fails
     */
    public static String
            ensureNonEmpty(final String s, final String errorMessage)
            throws MishmashProtocolException {
        if (s == null || s.isBlank()) {
            throw new MishmashProtocolException();
        }

        return s;
    }

    /**
     * Ensure a non-null object.
     *
     * @param <T> - an object of some class.
     * @param o - the object to check
     * @param errorMessage - the message if check fails
     * @return - the object
     * @throws MishmashProtocolException - when the check fails
     */
    public static <T> T
            ensureNonNull(final T o, final String errorMessage)
            throws MishmashProtocolException {
        if (o == null) {
            throw new MishmashProtocolException();
        }

        return o;
    }

    /**
     * Ensure an GRPC AltCase is not equal to a given value.
     *
     * @param <T> - the class of the AltCase
     * @param altCase - the AltCase
     * @param notSetCase - the undesired value
     * @param errorMessage - the message if check fails
     * @return - the AltCase
     * @throws MishmashProtocolException - when the check fails
     */
    public static <T> T
            ensureAltCase(final T altCase, final T notSetCase,
                    final String errorMessage)
                    throws MishmashProtocolException {
        if (altCase == null || notSetCase.equals(altCase)) {
            throw new MishmashProtocolException();
        }

        return altCase;
    }

    /**
     * Throw an error because of an unexpected (not allowed) type.
     *
     * @param <T> - the type
     * @param altCase - the unexpected AltCase
     * @throws MishmashInvalidArgumentException - always
     */
    public static <T extends Enum<?>> void
            unexpectedAltCase(final T altCase)
            throws MishmashInvalidArgumentException {
        String errorMessage = "Unknown Type " + altCase.name();
        throw new MishmashInvalidArgumentException();
    }

    /**
     * Throw an error because of an unimplemented functionality.
     *
     * @param <T> - the AltCase of the functionality
     * @param altCase - the unimplemented functionality
     * @throws MishmashUnimplementedException - always
     */
    public static <T extends Enum<?>>
            void unimplementedAltCase(final T altCase)
            throws MishmashUnimplementedException {
        String errorMessage = "Unimplemented Type " + altCase.name();
        throw new MishmashUnimplementedException();
    }

    /**
     * Throw an error because a message field is badly formatted.
     *
     * @param fieldName - the field name
     * @param fieldValue - it's value
     * @param t - the error thrown by the format check
     * @throws MishmashProtocolException - always
     */
    public static void
            fieldFormatError(final String fieldName, final String fieldValue,
                    final Throwable t)
                    throws MishmashProtocolException {
        String errorMessage = "Bad format for field " + fieldName
                + ": '" + fieldValue + "', error: " + t.getMessage();
        throw new MishmashProtocolException();
    }

    /**
     * Ensure message type is set.
     *
     * @param msg - the message
     * @return - the message type
     * @throws MishmashProtocolException - when AltCase is not set
     */
    public static StreamClientMessage.AltCase
            ensureCase(final StreamClientMessage msg)
                    throws MishmashProtocolException {
        return ensureAltCase(msg.getAltCase(),
                StreamClientMessage.AltCase.ALT_NOT_SET,
                "Stream Message Type not set");
    }

    /**
     * Ensure message type is set.
     *
     * @param msg - the message
     * @return - the message type
     * @throws MishmashProtocolException - when AltCase is not set
     */
    public static StreamServerMessage.AltCase
            ensureCase(final StreamServerMessage msg)
                throws MishmashProtocolException {
        return ensureAltCase(msg.getAltCase(),
                StreamServerMessage.AltCase.ALT_NOT_SET,
                "Stream Message Type not set");
    }

    /**
     * Ensure message is of a given type.
     *
     * @param msg - the message
     * @param altCase - the required message type
     * @throws MishmashException - if a check fails
     */
    public static void
            ensureCase(final StreamClientMessage msg,
                    final StreamClientMessage.AltCase altCase)
                    throws MishmashException {
        StreamClientMessage.AltCase ac = ensureCase(msg);

        if (altCase != ac) {
            throw new MishmashInvalidStateException();
        }
    }

    /**
     * Ensure message is of a given type.
     *
     * @param msg - the message
     * @param altCase - the required message type
     * @throws MishmashException - if a check fails
     */
    public static void
            ensureCase(final StreamServerMessage msg,
                    final StreamServerMessage.AltCase altCase)
                    throws MishmashException {
        StreamServerMessage.AltCase ac = ensureCase(msg);

        if (altCase != ac) {
            throw new MishmashInvalidStateException();
        }
    }

    /**
     * Ensure message is a SETUP and has a non-null SETUP element.
     *
     * @param msg - the message
     * @return - the MishmashSetup element
     * @throws MishmashException - if a check fails
     */
    public static MishmashSetup ensureSetup(final StreamClientMessage msg)
            throws MishmashException {
        ensureCase(msg, StreamClientMessage.AltCase.SETUP);

        return ensureNonNull(msg.getSetup(), "Missing SETUP element");
    }

    /**
     * Ensure message is SETUP ACK.
     *
     * @param msg - the message
     * @throws MishmashException - if checks fail
     */
    public static void ensureSetupAck(final StreamServerMessage msg)
            throws MishmashException {
        ensureCase(msg, StreamServerMessage.AltCase.SETUP_ACK);
    }

    /**
     * Ensure message type is set.
     *
     * @param msg - the message
     * @return - the message type
     * @throws MishmashProtocolException - if the check fails
     */
    public static MutationClientMessage.AltCase
            ensureCase(final MutationClientMessage msg)
                    throws MishmashProtocolException {
        return ensureAltCase(msg.getAltCase(),
                MutationClientMessage.AltCase.ALT_NOT_SET,
                "Mutation Message Type not set");
    }

    /**
     * Ensure message type is set.
     *
     * @param msg - the message
     * @return - the message type
     * @throws MishmashProtocolException - if the check fails
     */
    public static MutationServerMessage.AltCase
            ensureCase(final MutationServerMessage msg)
                    throws MishmashProtocolException {
        return ensureAltCase(msg.getAltCase(),
                MutationServerMessage.AltCase.ALT_NOT_SET,
                "Mutation Message Type not set");
    }

    /**
     * Ensure message is of given type.
     *
     * @param msg - the message
     * @param altCase - the required message type
     * @throws MishmashException - if a check fails
     */
    public static void
            ensureCase(final MutationClientMessage msg,
                    final MutationClientMessage.AltCase altCase)
                    throws MishmashException {
        MutationClientMessage.AltCase ac = ensureCase(msg);

        if (altCase != ac) {
            throw new MishmashInvalidStateException();
        }
    }

    /**
     * Ensure message is of given type.
     *
     * @param msg - the message
     * @param altCase - the required message type
     * @throws MishmashException - if a check fails
     */
    public static void
            ensureCase(final MutationServerMessage msg,
                    final MutationServerMessage.AltCase altCase)
                    throws MishmashException {
        MutationServerMessage.AltCase ac = ensureCase(msg);

        if (altCase != ac) {
            throw new MishmashInvalidStateException();
        }
    }

    /**
     * Ensure message is a SETUP and the SETUP element is non-null.
     *
     * @param msg - the message
     * @return - the MishmashSetup element
     * @throws MishmashException - if a check fails
     */
    public static MishmashSetup ensureSetup(final MutationClientMessage msg)
            throws MishmashException {
        ensureCase(msg, MutationClientMessage.AltCase.SETUP);

        return ensureNonNull(msg.getSetup(), "Missing SETUP element");
    }

    /**
     * Ensure message is a SETUP ACK.
     *
     * @param msg - the message
     * @throws MishmashException - if checks fail
     */
    public static void ensureSetupAck(final MutationServerMessage msg)
            throws MishmashException {
        ensureCase(msg, MutationServerMessage.AltCase.SETUP_ACK);
    }
}
