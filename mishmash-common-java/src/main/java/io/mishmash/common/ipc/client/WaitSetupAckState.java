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
package io.mishmash.common.ipc.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.mishmash.common.data.Key;
import io.mishmash.common.data.Mishmash;
import io.mishmash.common.data.Value;
import io.mishmash.common.ipc.GrpcStreamState;

/**
 * A {@link GrpcStreamState} that waits for a GRPC SetupAck from the server.
 *
 * @param <I> - the type of GRPC input messages
 * @param <O> - the type of GRPC output messages
 * @param <MT> - the type of {@link Mishmash} that is used
 */
public abstract class WaitSetupAckState<
            I, O,
            MT extends Mishmash<Key, Value>>
        implements GrpcStreamState<I, O> {

    /**
     * Default number of milliseconds to wait for the SETUP ACK message.
     */
    public static final int SETUP_ACK_WAIT_MSEC = 1500;

    /**
     * A future that waits for the SetupAck.
     */
    private CompletableFuture<O> waitingFuture;

    /**
     * Supplies the next state when this completes successfully.
     *
     * @return - the next {@link GrpcStreamState}
     */
    public abstract GrpcStreamState<I, O> prepareNext();

    /**
     * {@inheritDoc}
     */
    public CompletableFuture<Void> enter() {
        return CompletableFuture.completedFuture(null);
    }

    /**
     * {@inheritDoc}
     */
    public void input(final I input) throws Exception {
        waitingFuture.complete(null);
        waitingFuture = null;
    }

    /**
     * {@inheritDoc}
     */
    public GrpcStreamState<I, O> leave() {
        return prepareNext();
    }

    /**
     * {@inheritDoc}
     */
    public CompletableFuture<O> output(final AtomicInteger currentSeqNo) {
        waitingFuture = (new CompletableFuture<O>())
                .orTimeout(SETUP_ACK_WAIT_MSEC, TimeUnit.MILLISECONDS);

        return waitingFuture;
    }

    /**
     * {@inheritDoc}
     */
    public void error(final Throwable t) {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws Exception {
        if (waitingFuture != null && !waitingFuture.isDone()) {
            waitingFuture.cancel(true);
        }
    }

}
