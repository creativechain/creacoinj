/*
 * Copyright 2013 Google Inc.
 * Copyright 2015 Andreas Schildbach
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

import org.creativecoinj.core.*;

import static com.google.common.base.Preconditions.*;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class MainNetParams extends AbstractBitcoinNetParams {
    public static final int MAINNET_MAJORITY_WINDOW = 1000;
    public static final int MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED = 950;
    public static final int MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE = 750;

    public MainNetParams() {
        super();
        targetSpacing = 2 * 60;
        targetTimespan = 24 * 60 * 60;
        newPowTargetTimespan = 2 * 60;
        changePowHeight = 46368;

        maxTarget = Utils.decodeMPI(Utils.HEX.decode("00000fffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"), false);
        dumpedPrivateKeyHeader = 128;
        addressHeader = 28;
        p2shHeader = 5;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        port = 10946;
        packetMagic = 0xccccccccL /* 0xf9beb4d9L*/;
        bip32HeaderPub = 0x0488B21E; //The 4 byte header that serializes in base58 to "xpub".
        bip32HeaderPriv = 0x0488ADE4; //The 4 byte header that serializes in base58 to "xprv"

        majorityEnforceBlockUpgrade = MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE;
        majorityRejectBlockOutdated = MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED;
        majorityWindow = MAINNET_MAJORITY_WINDOW;

        genesisBlock.setTime(1493601901L);
        genesisBlock.setNonce(741491);
        genesisBlock.setDifficultyTarget(0x1e0ffff0L);

        id = ID_MAINNET;
        subsidyHalvingInterval = 840000;
        spendableCoinbaseDepth = 100;
        String genesisHash = genesisBlock.getHashAsString();
        checkState(genesisHash.equals("1f4d98cb9ae1b5ac2da8c2c83e25245766e8db4ecc21e557e8cb28c1f193f89e"),
                genesisHash);

        // This contains (at a minimum) the blocks which are not BIP30 compliant. BIP30 changed how duplicate
        // transactions are handled. Duplicated transactions could occur in the case where a coinbase had the same
        // extraNonce and the same outputs but appeared at different heights, and greatly complicated re-org handling.
        // Having these here simplifies block connection logic considerably.
/*        checkpoints.put(91722, Sha256Hash.wrap("00000000000271a2dc26e7667f8419f2e15416dc6955e5a6c6cdf3f2574dd08e"));
        checkpoints.put(91812, Sha256Hash.wrap("00000000000af0aed4792b1acee3d966af36cf5def14935db8de83d6f9306f2f"));
        checkpoints.put(91842, Sha256Hash.wrap("00000000000a4d0a398161ffc163c503763b1f4360639393e0e4c8e300e0caec"));
        checkpoints.put(91880, Sha256Hash.wrap("00000000000743f190a18c5577a3c2d2a1f610ae9601ac046a38084ccb7cd721"));
        checkpoints.put(200000, Sha256Hash.wrap("000000000000034a7dedef4a161fa058a2d67a173a90155f3a2fe6fc132e0ebf"));

        dnsSeeds = new String[] {
                "seed.creativecoin.sipa.be",         // Pieter Wuille
                "dnsseed.bluematt.me",          // Matt Corallo
                "dnsseed.creativecoin.dashjr.org",   // Luke Dashjr
                "seed.bitcoinstats.com",        // Chris Decker
                "seed.bitnodes.io",             // Addy Yeow
                "bitseed.xf2.org",              // Jeff Garzik
                "seed.creativecoin.jonasschnelli.ch" // Jonas Schnelli
        };
        httpSeeds = new HttpDiscovery.Details[] {
                // Andreas Schildbach
                new HttpDiscovery.Details(
                        ECKey.fromPublicOnly(Utils.HEX.decode("0238746c59d46d5408bf8b1d0af5740fe1a6e1703fcb56b2953f0b965c740d256f")),
                        URI.create("http://httpseed.bitcoin.schildbach.de/peers")
                )
        }; */

        addrSeeds = new int[] {
                0x1681B6D9, 0x25915097, 0x22915097, 0xB2D4F150, 0x3B0BD0A2, 0x4398BD05, 0x7CB5BD05
                //0xD9B68116, 0x97509125, 0x97509122, 0x50F1D4B2, 0xA2D00B3B, 0x05BD9843, 0x05BDB57C
        };
    }

    private static MainNetParams instance;
    public static synchronized MainNetParams get() {
        if (instance == null) {
            instance = new MainNetParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }
}
