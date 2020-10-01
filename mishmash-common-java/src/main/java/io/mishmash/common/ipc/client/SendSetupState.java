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
package io.mishmash.common.ipc.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import io.mishmash.common.data.Key;
import io.mishmash.common.data.Mishmash;
import io.mishmash.common.data.Value;
import io.mishmash.common.exception.MishmashInvalidStateException;
import io.mishmash.common.ipc.GrpcStreamState;

/**
 * A base {@link GrpcStreamState} that sends a SETUP on a GRPC stream.
 *
 * @param <I> - the GRPC input message type
 * @param <O> - the GRPC output message type
 * @param <MT> - the type of {@link Mishmash} built
 */
public abstract class SendSetupState<I, O, MT extends Mishmash<Key, Value>>
        implements GrpcStreamState<I, O> {

    /**
     * A flag to know if we already sent the setup.
     */
    private boolean isSent = false;

    /**
     * Prepare and return the Setup message.
     *
     * @return - the Setup GRPC message
     */
    public abstract O getSetupMessage();

    /**
     * Return the next State that should be entered.
     *
     * @return - the next state
     */
    public abstract GrpcStreamState<I, O> prepareNext();

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> enter() {
        return CompletableFuture.completedFuture(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateInput(final I input) throws Exception {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void input(final I input) throws Exception {
        throw new MishmashInvalidStateException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GrpcStreamState<I, O> leave() {
        return prepareNext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<O> output(final AtomicInteger currentSeqNo) {
        if (isSent) {
            return CompletableFuture.completedFuture(null);
        } else {
            isSent = true;
            return CompletableFuture.completedFuture(getSetupMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final Throwable t) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {

    }

}
