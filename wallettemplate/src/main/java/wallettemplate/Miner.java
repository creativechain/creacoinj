package wallettemplate;

import org.creativecoinj.core.*;
import org.json.JSONArray;
import org.json.JSONObject;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException;
import wf.bitcoin.javabitcoindrpcclient.BitcoinRpcException;
import wf.bitcoin.krotjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by ander on 20/07/17.
 */
public class Miner extends Thread {


    private class BitcoinRpcClient extends BitcoinJSONRPCClient {
        public BitcoinRpcClient(String rpcUrl) throws MalformedURLException {
            super(rpcUrl);
        }

        public JSONObject getBlockTemplate(){
            return (JSONObject) query("getblocktemplate");
        }

        public void submitBlock(String hex) {
            Object object = query("submitblock", hex);
            System.out.println("Submit response: " + object.toString());
        }

        @Override
        public Object loadResponse(InputStream in, Object expectedID, boolean close) throws IOException, BitcoinRpcException {
            Object obj;
            try {
                String r = new String(loadStream(in, close), QUERY_CHARSET);

                try {
                    JSONObject response = new JSONObject(r);
                    Map ex = (Map) JSON.parse(r);
                    if(!expectedID.equals(response.get("id"))) {
                        throw new BitcoinRPCException("Wrong response ID (expected: " + String.valueOf(expectedID) + ", response: " + ex.get("id") + ")");
                    }

                    if(!response.get("error").toString().equals("null")) {
                        throw new BitcoinRpcException(response.get("error").toString());
                    }

                    obj = response.get("result");
                } catch (ClassCastException var10) {
                    throw new BitcoinRPCException("Invalid server response format (data: \"" + r + "\")");
                }
            } finally {
                if(close) {
                    in.close();
                }

            }

            return obj;
        }
    }

    private ECKey coinbaseKey;
    private BitcoinRpcClient bitcoinClient;
    private boolean reset = false;
    private int blocksFound = 0;

    public Miner(ECKey coinbaseKey) {
        this.coinbaseKey = coinbaseKey;
    }

    @Override
    public void run() {
        try {
            String url = "http://crea:tivecoin@192.168.42.136:9600/";
            bitcoinClient = new BitcoinRpcClient(url);
            while (true) {

                JSONObject blockTemplate = bitcoinClient.getBlockTemplate();

                int height = blockTemplate.getInt("height");
                Block block = new Block(Main.params, blockTemplate.getLong("version"));
                block.setDifficultyTarget(Long.parseLong(blockTemplate.getString("bits"), 16));
                block.setPrevBlockHash(Sha256Hash.wrap(blockTemplate.getString("previousblockhash")));

                JSONArray txs = blockTemplate.getJSONArray("transactions");

                List<Transaction> transactions = new ArrayList<>();
                for (int x = 0; x < txs.length(); x++) {
                    byte[] rawTx = Utils.HEX.decode(txs.getJSONObject(x).getString("data"));
                    Transaction tx = new Transaction(Main.params, rawTx);
                    transactions.add(tx);
                }

                Coin coinbaseValue = Coin.valueOf(blockTemplate.getLong("coinbasevalue"));

                block.addCoinbaseTransaction(coinbaseKey.getPubKey(), coinbaseValue, height);
                for (Transaction t : transactions) {
                    block.addTransaction(t);
                }
                long start = System.currentTimeMillis();
                solve(block, height);
                if (!reset) {
                    System.out.println("Resolved " + block.toString() + " in " + (System.currentTimeMillis() - start) + "ms");
                } else {
                    reset = false;
                    System.out.println("Work restarted!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void solve(Block block, int height) {
        long hashes = 0;
        long time = System.currentTimeMillis();
        boolean found = false;
        while (!reset) {
            try {
                hashes++;
                // Is our proof of work valid yet?
                if (block.checkProofOfWork(false)) {
                    found = true;
                    break;
                }
                // No, so increment the nonce and try again.
                block.setNonce(block.getNonce() + 1);
                block.setTime(System.currentTimeMillis() / 1000);
                if ((System.currentTimeMillis() - time) >= 1000) {
                    System.out.println("Calculating block " + height + " with " + hashes + " h/s Found: " + blocksFound);
                    hashes = 0;
                    time = System.currentTimeMillis();
                }
            } catch (VerificationException e) {
                throw new RuntimeException(e); // Cannot happen.
            }
        }

        if (found) {
            bitcoinClient.submitBlock(Utils.HEX.encode(block.bitcoinSerialize()));
            blocksFound++;
        }
    }

    public void reset() {
        reset = true;
    }

    private byte[] loadStream(InputStream in, boolean close) throws IOException {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        while(true) {
            int nr = in.read(buffer);
            if(nr == -1) {
                return o.toByteArray();
            }

            if(nr == 0) {
                throw new IOException("Read timed out");
            }

            o.write(buffer, 0, nr);
        }
    }
}
