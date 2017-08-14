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
        byte[] headerBytes = Utils.HEX.decode("040000007de9e810cd73e50094e2b0994932f2030827496c057f51" +
                "ec2a7acd4bfa0a000000b983350ee0dc32ae24e1ff3cf66461cc73044559b0b76c3c7fc8e79ba917b779617759" +
                "ffff0f1eb0080300");

        System.out.println(Sha256Hash.wrapReversed(Sha256Hash.keccakHash(headerBytes)).toString());
        System.out.println(new Block(TestNet3Params.get(), headerBytes).getHashAsString());
    }
}
