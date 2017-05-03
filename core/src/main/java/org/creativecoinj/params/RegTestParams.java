/*
 * Copyright 2013 Google Inc.
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

package org.creativecoinj.params;

import org.creativecoinj.core.Block;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkState;

/**
 * Network parameters for the regression test mode of bitcoind in which all blocks are trivially solvable.
 */
public class RegTestParams extends TestNet2Params {
    private static final BigInteger MAX_TARGET = new BigInteger("7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", 16);

    public RegTestParams() {
        super();
        // Difficulty adjustments are disabled for regtest. 
        // By setting the block difficultyAdjustmentInterval for difficulty adjustments to Integer.MAX_VALUE we make sure difficulty never changes.
        difficultyAdjustmentInterval = Integer.MAX_VALUE;
        maxTarget = MAX_TARGET;
        subsidyHalvingInterval = 150;
        port = 18444;
        id = ID_REGTEST;
        packetMagic = 0xfabfb5da;

        majorityEnforceBlockUpgrade = 750;
        majorityRejectBlockOutdated = 950;
        majorityWindow = 100;
        targetTimespan = (int) 3.5 * 24 * 60 * 60;

    }

    @Override
    public boolean allowEmptyPeerChain() {
        return true;
    }

    private static Block genesis;

    @Override
    public Block getGenesisBlock() {
        synchronized (RegTestParams.class) {
            if (genesis == null) {
                genesis = super.getGenesisBlock();
                genesis.setNonce(0);
                genesis.setDifficultyTarget(0x207fffff);
                genesis.setTime(1296688602);
                checkState(genesis.getHashAsString().toLowerCase().equals("069928a84afce6486bebb795b47a6ea5109ad2645f8135bbc46bb6220ab04857"));
            }
            return genesis;
        }
    }

    private static RegTestParams instance;
    public static synchronized RegTestParams get() {
        if (instance == null) {
            instance = new RegTestParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_REGTEST;
    }
}
