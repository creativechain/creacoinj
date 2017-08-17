package wallettemplate;

import org.creativecoinj.core.Block;
import org.creativecoinj.core.Sha256Hash;
import org.creativecoinj.core.Utils;
import org.creativecoinj.params.TestNet3Params;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Created by ander on 20/06/17.
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {

        long[] bits = new long[]{503365631, 503377646};

        for (long b : bits) {
            BigInteger target = Utils.decodeCompactBits(b);
            System.out.println(Long.toHexString(b) + ": " + target.toString(16));
        }

        BigInteger t = Utils.decodeMPI(Utils.HEX.decode("000000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"), false);
        long d = Utils.encodeCompactBits(t);
        System.out.println(Long.toHexString(d) + ": " + t.toString(16));

    }
}
