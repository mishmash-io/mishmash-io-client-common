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

import io.mishmash.common.exception.MishmashException;

/**
 * A leave method for a client builder.
 *
 * @param <I> - the GRPC input message type
 * @param <O> - the GRPC output message type
 * @param <CT> - the GRPC {@link BaseClient} type
 * @param <MT> - the {@link ClientMishmash} type
 * @param <BT> - the builder that will be left
 */
@FunctionalInterface
public interface BuilderLeave<
            I, O,
            CT extends BaseClient<I, O>,
            MT extends ClientMishmash<I, O, CT>,
            BT extends BaseClientMishmashBuilder<I, O, CT, MT>> {

    /**
     * Leave the builder.
     *
     * @param left - the builder that is being left
     * @return - the new builder
     * @throws MishmashException - thrown on failure
     */
    BaseClientMishmashBuilder<I, O, CT, MT> leave(BT left)
            throws MishmashException;

}
