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
package io.mishmash.common.data;

/**
 * A base interface for {@link DataPoint}s.
 *
 * @param <K> - the key type
 * @param <V> - the value type
 */
public interface BaseDataPoint<K, V> {

    /**
     * Get the keys.
     *
     * @return an Iterable over the keys
     */
    Iterable<K> keys();

    /**
     * Get the value.
     *
     * @return - the value
     */
    V value();

}
