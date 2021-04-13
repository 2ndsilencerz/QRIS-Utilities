package qris;

import java.nio.charset.StandardCharsets;

public class Utils {

    public static String Checksum(String param) {
        // TODO Auto-generated method stub
        String output = "";
        try {
            int crc = 0xFFFF; // initial value
            int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)

            // byte[] testBytes = "123456789".getBytes("ASCII");

            byte[] bytes = param.getBytes(StandardCharsets.US_ASCII);

            for (byte b : bytes) {
                for (int i = 0; i < 8; i++) {
                    boolean bit = ((b >> (7 - i) & 1) == 1);
                    boolean c15 = ((crc >> 15 & 1) == 1);
                    crc <<= 1;
                    if (c15 ^ bit)
                        crc ^= polynomial;
                }
            }

            crc &= 0xffff;
            output = String.format("%04x", crc).toUpperCase();
//            output = Integer.toHexString(crc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
