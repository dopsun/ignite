/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.processors.cache;

import java.util.HashSet;
import java.util.Set;
import org.apache.ignite.internal.processors.cache.distributed.dht.preloader.GridDhtPartitionsExchangeFuture;
import org.apache.ignite.internal.processors.cache.distributed.dht.preloader.GridDhtPartitionsFullMessage;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class ExchangeContext {
    /** */
    private Set<Integer> requestGrpsAffOnJoin;

    /** */
    private boolean fetchAffOnJoin;

    /** */
    private final boolean merge;

    /** */
    private final ExchangeDiscoveryEvents evts;

    /**
     * @param protocolVer Protocol version.
     * @param fut Exchange future.
     */
    public ExchangeContext(int protocolVer, GridDhtPartitionsExchangeFuture fut) {
        fetchAffOnJoin = protocolVer == 1;

        merge = protocolVer > 1;

        evts = new ExchangeDiscoveryEvents(fut);
    }

    public ExchangeDiscoveryEvents events() {
        return evts;
    }

    /**
     * @return {@code True} if on local join need fetch affinity per-group (old protocol),
     *      otherwise affinity is sent in {@link GridDhtPartitionsFullMessage}.
     */
    boolean fetchAffinityOnJoin() {
        return fetchAffOnJoin;
    }

    /**
     * @param grpId Cache group ID.
     */
    void addGroupAffinityRequestOnJoin(Integer grpId) {
        if (requestGrpsAffOnJoin == null)
            requestGrpsAffOnJoin = new HashSet<>();

        requestGrpsAffOnJoin.add(grpId);
    }

    /**
     * @return Groups to request affinity for.
     */
    @Nullable public Set<Integer> groupsAffinityRequestOnJoin() {
        return requestGrpsAffOnJoin;
    }

    public boolean canMergeExchanges() {
        return merge;
    }
}
