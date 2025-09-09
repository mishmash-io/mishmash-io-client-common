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
package io.mishmash.common.ipc.server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import io.mishmash.common.data.Key;
import io.mishmash.common.data.Mishmash;
import io.mishmash.common.data.MishmashBuilder;
import io.mishmash.common.data.Value;
import io.mishmash.common.exception.MishmashTimeoutException;
import io.mishmash.common.ipc.GrpcStreamState;
import io.mishmash.common.rpc.MishmashRpc.MishmashSetup;

/**
 * A base {@link GrpcStreamState} that waits for the SETUP on a GRPC stream.
 *
 * A {@link MishmashBuilder} is obtained through a {@link Supplier} mainly
 * to avoid expensive operations on stores before a SETUP message is actually
 * received (it can time out too).
 *
 * @param <I> - the GRPC input message type
 * @param <O> - the GRPC output message type
 * @param <MT> - the type of {@link Mishmash} built by the
 *                      {@link MishmashBuilder}
 */
public abstract class WaitSetupState<I, O, MT extends Mishmash<Key, Value>>
        implements GrpcStreamState<I, O> {

    /**
     * Default number of milliseconds to wait for the SETUP message.
     */
    public static final int SETUP_TIMEOUT_MSEC = 1500;

    /**
     * The {@link MishmashBuilder} supplier.
     */
    private Supplier<MishmashBuilder<MT>> builderSupplier;

    /**
     * The {@link MishmashBuilder} in use.
     */
    private MishmashBuilder<MT> targetBuilder;

    /**
     * A Future that is resolved by receiving the SETUP message or a timeout.
     */
    private CompletableFuture<Void> waitingFuture;

    /**
     * The next state that should be provided back to the GRPC Observer.
     */
    private GrpcStreamState<I, O> nextState;

    /**
     * Init the state.
     *
     * @param targetBuilderSupplier - a supplier of a {@link MishmashBuilder}
     */
    public WaitSetupState(
            final Supplier<MishmashBuilder<MT>> targetBuilderSupplier) {
        this.builderSupplier = targetBuilderSupplier;
    }

    /**
     * Return the obtained {@link MishmashBuilder}.
     *
     * @return - the {@link MishmashBuilder} in use
     */
    public MishmashBuilder<MT> getTargetBuilder() {
        return targetBuilder;
    }

    /**
     * Setup a timeout and wait for the SETUP message.
     *
     * The returned Future will either
     * complete when SETUP is received or complete exceptionally
     * with a {@link MishmashTimeoutException}.
     *
     * @return - a Future that will complete or throw
     *          {@link MishmashTimeoutException}
     */
    @Override
    public CompletableFuture<Void> enter() {
        waitingFuture = new CompletableFuture<>();

        CompletableFuture<Void> timeoutFuture =
                new CompletableFuture<Void>()
                    .orTimeout(SETUP_TIMEOUT_MSEC, TimeUnit.MILLISECONDS)
                    .exceptionally(t -> {
                        waitingFuture.completeExceptionally(
                                new MishmashTimeoutException());

                        return (Void) null;
                    });

        return waitingFuture;
    }

    /**
     * Prepare the next state that should be entered when a SETUP message
     * has been received.
     *
     * @param setup - the SETUP message
     * @return - the next state to enter
     */
    public abstract GrpcStreamState<I, O> prepareNext(MishmashSetup setup);

    /**
     * Handle the SETUP received.
     *
     * Stop the timeout and obtain a {@link MishmashBuilder} through
     * the Supplier given earlier.
     *
     * @param setup - the received SETUP GRPC message
     * @throws Exception - if an error is encountered
     */
    public void input(final MishmashSetup setup) throws Exception {
        // stop the timeout
        waitingFuture.complete(null);
        waitingFuture = null;

        // request the target builder
        targetBuilder = builderSupplier.get();
        // request the next state
        nextState = prepareNext(setup);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GrpcStreamState<I, O> leave() {
        return nextState;
    }

    /**
     * Return a completed Future with null message so that we can
     * immediately request a change of states.
     *
     * @param currentSeqNo - the local-end sequence number
     * @return - a Future completed with null value
     */
    @Override
    public CompletableFuture<O> output(final AtomicInteger currentSeqNo) {
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Close the underlying builder if it is open.
     *
     * Overwrite this method to add logging, as it ignores
     * exceptions thrown during close()
     */
    @Override
    public void close() throws Exception {
        if (targetBuilder != null) {
            targetBuilder.close();
        }
    }

}
