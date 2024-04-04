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
 * A single Key in a {@link Mishmash} {@link DataPoint}.
 */
public class Key extends Instance {

    /**
     * The member name, if this Key represents a member.
     */
    private String member;

    /**
     * The index, if this Key represents an index.
     */
    private Long index;

    /**
     * True when this is a member Key.
     */
    private boolean isMember;

    /**
     * Create a member Key.
     *
     * @param memberName - the member name
     * @param instanceId - the member name instance id
     */
    public Key(final String memberName, final String instanceId) {
        super(instanceId);
        this.member = memberName;
        isMember = true;
    }

    /**
     * Create an index Key.
     *
     * @param indexNo - the index value
     * @param instanceId - the index value instance id
     */
    public Key(final long indexNo, final String instanceId) {
        super(instanceId);
        this.index = indexNo;
        isMember = false;
    }

    /**
     * Test if this Key is a member Key.
     *
     * @return - true when this is a member Key
     */
    public boolean isMember() {
        return isMember;
    }

    /**
     * Test if this Key is an index Key.
     *
     * @return - true when this is an index Key
     */
    public boolean isIndex() {
        return !isMember;
    }

    /**
     * Get the member name.
     *
     * @return - member name or null if not a member Key
     */
    public String getMember() {
        return member;
    }

    /**
     * Get the index.
     *
     * @return - the index value or null if not an index Key
     */
    public Long getIndex() {
        return index;
    }

    /**
     * Compose a new Key for a member.
     *
     * @param member - the member name
     * @param instanceId - the member instance ID
     * @return - a new Key
     */
    public static Key of(final String member, final String instanceId) {
        return new Key(member, instanceId);
    }

    /**
     * Compose a new Key for an index.
     *
     * @param index - the index number
     * @param instanceId - the index number instance ID
     * @return - a new Key
     */
    public static Key of(final long index, final String instanceId) {
        return new Key(index, instanceId);
    }
}
