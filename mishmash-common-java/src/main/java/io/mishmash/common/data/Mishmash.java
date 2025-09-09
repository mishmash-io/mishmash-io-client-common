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

import java.util.concurrent.CompletableFuture;

/**
 * A single mishmash that has been built and data in it can be accessed.
 *
 * @param <K> - the {@link DataPoint} key type
 * @param <V> - the {@link DataPoint} value type
 */
public interface Mishmash<K, V> extends AutoCloseable {

    /**
     * Open this Mishmash.
     *
     * @return - a Future that completes on opening or fails with error
     */
    CompletableFuture<Void> open();

    /**
     * Get the next {@link DataPoint} from this Mishmash.
     *
     * @return - a future that completes with the next
     *          {@link DataPoint} or error
     */
    CompletableFuture<? extends BaseDataPoint<K, V>> get();

    /**
     * Put a {@link DataPoint} into this Mishmash.
     *
     * @param dataPoint - the new {@link DataPoint}
     * @return - a Future that completes when the {@link DataPoint} is
     *          saved or on error
     */
    CompletableFuture<Void> put(BaseDataPoint<K, V> dataPoint);

}
