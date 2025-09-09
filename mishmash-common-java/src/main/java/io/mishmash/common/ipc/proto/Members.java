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
package io.mishmash.common.ipc.proto;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import io.mishmash.common.data.Key;
import io.mishmash.common.rpc.MishmashRpc.Id;
import io.mishmash.common.rpc.MishmashRpc.Member;
import io.mishmash.common.rpc.MishmashRpc.YieldData;
import io.mishmash.common.rpc.MishmashRpc.YieldMember;

/**
 * Helper methods to work with GRPC Members messages.
 */
public final class Members {

    /**
     * The Members class should not be instantiated.
     */
    private Members() {
        // Hide the constructor
    }

    /**
     * Build a YieldMember from a member.
     *
     * @param member - the mishmash member key
     * @param keyId - the mishmash member id
     * @return Builder - a YieldMember builder
     */
    public static YieldMember.Builder member(final String member,
            final String keyId) {
        return YieldMember.newBuilder()
                .setInstanceId(id(keyId))
                .setMember(
                        Member.newBuilder().setName(member));
    }

    /**
     * Build a YieldMember from an index.
     *
     * @param index - the mishmash index key
     * @param indexId - the mishmash index id
     * @return Builder - a YieldMember builder
     */
    public static YieldMember.Builder member(final long index,
            final String indexId) {
        return YieldMember.newBuilder()
                .setInstanceId(id(indexId))
                .setMember(
                        Member.newBuilder().setIndex(index));
    }

    /**
     * Build a YieldMember from a {@link Key}.
     *
     * @param key - the {@link Key}
     * @return Builder - a YieldMember builder
     */
    public static YieldMember.Builder member(
            final Key key) {
        if (key.isIndex()) {
            return member(key.getIndex(), key.getInstance());
        } else if (key.isMember()) {
            return member(key.getMember(), key.getInstance());
        } else {
            throw new IllegalArgumentException(
                    "Key is neither member nor index");
        }
    }

    /**
     * Build an Id from an instance id.
     *
     * @param instanceId - the mishmash instance id
     * @return Builder - an Id builder
     */
    public static Id.Builder id(final String instanceId) {
        return Id.newBuilder().setId(instanceId);
    }

    /**
     * Extract the {@link Key} id from a GRPC Id.
     *
     * @param id - the GRPC Id message
     * @return - the id
     */
    public static String fromId(final Id id) {
        if (id == null) {
            throw new IllegalArgumentException("Member Id cannot be null");
        }

        return id.getId();
    }

    /**
     * Build a {@link Key} from a GRPC Member and an id.
     *
     * @param member - the GRPC Member
     * @param id - the id
     * @return - a {@link Key}
     */
    public static Key fromMember(final Member member, final Id id) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        switch (member.getAltCase()) {
        case ALT_NOT_SET:
            throw new IllegalArgumentException("Member type not set");
        case INDEX:
            return Key.of(member.getIndex(), fromId(id));
        case NAME:
            return Key.of(member.getName(), fromId(id));
        default:
            throw new IllegalArgumentException("Unknown Member type: "
                    + member.getAltCase().name());
        }
    }

    /**
     * Build a {@link Key} from a GRPC YieldMember.
     *
     * @param member - the GRPC Member
     * @return - a {@link Key}
     */
    public static Key fromMember(final YieldMember member) {
        return fromMember(member.getMember(), member.getInstanceId());
    }

    /**
     * Build a YieldData for a given member hierarchy.
     *
     * @param members - the member hierarchy
     * @return Builder - a YieldData builder
     */
    public static YieldData.Builder toMembers(
            final Iterable<Key> members) {
        return YieldData.newBuilder()
                .addAllHierarchy(
                        () -> Iterators.transform(members.iterator(),
                                ent -> member(ent).build()));
    }

    /**
     * Get a {@link Key} iterator over the members of a GRPC YieldData.
     *
     * @param yield - the YieldData
     * @return - an Iterator over {@link Key}s
     */
    public static Iterator<Key> fromMembers(final YieldData yield) {
        return Iterators.transform(yield.getHierarchyList().iterator(),
                Members::fromMember);
    }
}
