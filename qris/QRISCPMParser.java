package qris;

// compile group: 'commons-codec', name: 'commons-codec', version: '1.15'
import org.apache.commons.codec.binary.Hex;
// compile group: 'org.apache.tomcat.embed', name: 'tomcat-embed-core', version: '9.0.43'
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRISCPMParser extends QRISMPMParser {

    /*
    TODO: this parser utility still have weak point
     which is to parse root content of id 61 and id 63
     */

    String[] idList = new String[] { "4F", "50", "57", "5A", "5F", "9F", "63" };
    String[] id5FList = new String[] { "5F20", "5F2D", "5F50" };
    String[] id9FList = new String[] { "9F08", "9F19", "9F24", "9F25" };
    String[] id63List = new String[] { "9F26", "9F27", "9F10", "9F36", "82", "9F37", "9F74" };
    String[] keysOfNumberData = new String[] {
            "61", "4F", "57", "90F8", "9F19", "9F25",
            "63", "9F26", "9F27", "9F10", "9F36", "82", "9F37"
    };

    public QRISCPMParser(String raw) {
        this.parse(raw);
    }

    // main process
    private void parse(String base64Raw) {

        // decode base64 to hex
        byte[] decodedBytesFromBase64 = Base64.decodeBase64(base64Raw);
        char[] decodedBytesFromHex = Hex.encodeHex(decodedBytesFromBase64);
        String rawHex = new String(decodedBytesFromHex).toUpperCase();
//        String rawHex = DatatypeConverter.printHexBinary(Base64.decodeBase64(base64Raw));

        // root id 85
        String id85Content = this.getContent(rawHex, false);
        this.putContent("85", id85Content);
        rawHex = this.stripContent(rawHex, id85Content.length());

        // root id 61
        String id61Content = this.getContentCustom(rawHex, "4F");
        this.putContent("61", id61Content);
        // sub id 61
        this.getLoopContent(idList, id61Content, false);
    }

    protected String getContent(String rawHex, boolean is4Digit) {
        // convert hex to int
        int dataLength;
        // starting point of 4 digit id is index 6, else is 4
        int start;
        if (is4Digit) {
            start = 6;
            dataLength = Integer.parseInt(rawHex.substring(4, 6), 16);
        } else {
            start = 4;
            dataLength = Integer.parseInt(rawHex.substring(2, 4), 16);
        }
        // since its represented as Hex, each character would take 2 bit. So multiply length by 2.
        return rawHex.substring(start, start + (dataLength * 2));
    }

    protected String getContentCustom(String rawHex, String patternToFind) {
        Pattern pattern = Pattern.compile(patternToFind + "(.*)");
        Matcher matcher = pattern.matcher(rawHex);
        String result = "";
        while (matcher.find()) {
            result = matcher.group();
        }
        return result;
    }

    @Override
    protected void putContent(String key, String hexData) {
        boolean number = isNumberData(key);
        boolean numericAcc = isNumericAccount(key);
        String data = hexData;
        if (!number && !numericAcc) {
            // decode hex to string
            try {
                byte[] decodedHex = Hex.decodeHex(hexData);
                data = new String(decodedHex);
            } catch (Exception ignored) { }
        } else if (numericAcc) {
            data = data.replace("F", "");
        }
        contents.put(key, data);
    }

    private boolean isNumberData(String key) {
        return Arrays.asList(keysOfNumberData).contains(key);
    }

    private boolean isNumericAccount(String key) {
        return key.equals("5A");
    }

    private String getLoopContent(String[] idList, String raw, boolean isSub) {
        for (String currentId : idList) {
            if (raw.length() == 0) {
                break;
            }
            String id = raw.substring(0, currentId.length());
            if (id.equals(currentId)) {
                if (id.equals("5F") && !isSub) {
                    raw = this.getLoopContent(id5FList, raw, true);
                } else if (id.equals("9F") && !isSub) {
                    raw = this.getLoopContent(id9FList, raw, true);
                } else if (id.equals("63") && !isSub) {
                    raw = this.getContentCustom(raw, "9F");
                    raw = this.getLoopContent(id63List, raw, true);
                } else if (id.equals("82") || !isSub) {
                    // id 82 is a special case which is come from sub id 63
                    String content = this.getContent(raw, false);
                    this.putContent(currentId, content);
                    raw = this.stripContent(raw, content.length());
                } else {
                    String content = this.getContent(raw, true);
                    this.putContent(currentId, content);
                    // add 2 for 4 digit id
                    raw = this.stripContent(raw, content.length() + 2);
                }
            } else {
                this.putContent(currentId, null);
            }
        }
        return raw;
    }
}
