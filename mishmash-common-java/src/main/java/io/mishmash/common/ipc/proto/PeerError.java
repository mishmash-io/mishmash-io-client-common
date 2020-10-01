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

import java.util.Arrays;
import java.util.Collections;

import io.mishmash.common.exception.MishmashException;

/**
 * Represents an error that occurred at the peer.
 *
 */
public class PeerError {

    /**
     * The error code.
     */
    private final int code;

    /**
     * The error message.
     */
    private final String message;

    /**
     * Additional details.
     */
    private final Iterable<String> info;

    /**
     * Create a new instance of a PeerError from an optional String array.
     *
     * @param errorCode - the error code
     * @param errorMessage - the error message
     * @param additionalInfo - additional details, if any
     */
    public PeerError(final int errorCode, final String errorMessage,
            final String...additionalInfo) {
        this(errorCode, errorMessage,
                additionalInfo == null || additionalInfo.length == 0
                    ? Collections::emptyIterator
                    : Arrays.asList(additionalInfo));
    }

    /**
     * Create a new PeerError from an Iterable with additional details.
     *
     * @param errorCode - the error code
     * @param errorMessage - the error message
     * @param additionalInfo - additional details, if any
     */
    public PeerError(final int errorCode, final String errorMessage,
            final Iterable<String> additionalInfo) {
        this.code = errorCode;
        this.message = errorMessage;
        this.info = additionalInfo == null
                ? Collections::emptyIterator
                : additionalInfo;
    }

    /**
     * Create a new PeerError, without additional details.
     *
     * @param errorCode - the error code
     * @param errorMessage - the error message
     */
    public PeerError(final int errorCode, final String errorMessage) {
        this(errorCode, errorMessage, Collections::emptyIterator);
    }

    /**
     * Create a new PeerError from a {@link MishmashException}.
     *
     * @param mex - the mishmash exception
     */
    public PeerError(final MishmashException mex) {
        this(mex.getErrorCode(), mex.getMessage());
    }

    /**
     * Create a new PeerError from a {@link MishmashException}.
     *
     * @param mex - the mishmash exception
     * @param additionalInfo - extra info
     */
    public PeerError(final MishmashException mex,
            final Iterable<String> additionalInfo) {
        this(mex.getErrorCode(), mex.getMessage(), additionalInfo);
    }

    /**
     * Create a new PeerError from a {@link MishmashException}.
     *
     * @param mex - the mishmash exception
     * @param additionalInfo - extra info
     */
    public PeerError(final MishmashException mex,
            final String...additionalInfo) {
        this(mex.getErrorCode(), mex.getMessage(), additionalInfo);
    }

    /**
     * Get the error code.
     *
     * @return int - the error code
     */
    public int getErrorCode() {
        return code;
    }

    /**
     * Get the error message.
     *
     * @return String - the error message
     */
    public String getErrorMessage() {
        return message;
    }

    /**
     * Get the additional details, if any.
     *
     * @return Iterable - additional details or, returns empty Iterator if none
     */
    public Iterable<String> getAdditionalInfo() {
        return info;
    }

}
