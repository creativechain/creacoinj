package wallettemplate;

import org.creativecoinj.core.Utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Created by ander on 20/06/17.
 */
public class Test {

    // CREA      FIAT
    // 1 ------- PRICE
    // A ------- ?

    public static void main(String[] args) throws InterruptedException {
        final long COIN = 100000000L;
        final long MAX_ERAS = 50000000;
        long supply = 0;
        int halvingInterval = 50;
        long startReward = 100 * COIN;
        int eras = 0;
        long height = 0;
        int countHalving = 0;

        while (true) {
            boolean changeEra = countHalving == halvingInterval;
            if (changeEra) {
                countHalving = 0;
                eras += 1;
                //System.out.println("Changing to ERA " + eras + " on block " + height);
            }

            long subsidy = Math.round(startReward * (100d / eras));
            if (subsidy == 0) {
                break;
            }
            //subsidy <<= eras;

            supply += subsidy;
            if (changeEra) {
                System.out.println("Era: " + eras + ", Block " + height + ", Reward:" + BigDecimal.valueOf(subsidy, 8).toPlainString() + " EXC, Circulating: " + BigDecimal.valueOf(supply, 8).toPlainString() + " EXC, " +
                        BigDecimal.valueOf(height * 60d / (60*60*24*365)).toPlainString() + " years");
            }
            height++;
            countHalving++;
            //Thread.sleep(50);
        }

        //System.out.println("Coin live: " + (BigDecimal.valueOf(height * 60d / (60*60*24*365)).toPlainString() + " years"));

    }
}
