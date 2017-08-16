package org.creativecoinj.tools;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ander on 16/08/17.
 */
public class AddressSeedConverter {

    public static void main(String[] args) throws UnknownHostException {


        for (String s : args) {
            InetAddress iAddress = InetAddress.getByName(s);
            byte[] addressBytes = iAddress.getAddress();

            long value = 0;
            for (int i = 0; i < addressBytes.length; i++)
            {
                value += ((long) addressBytes[i] & 0xffL) << (8 * i);
            }

            System.out.println("0x" + Long.toHexString(value));
        }
    }
}
