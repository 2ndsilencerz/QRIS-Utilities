package com.mlpt.merchant.transactionservice.mpm.util;

import com.mlpt.merchant.transactionservice.common.service.CommonServiceImpl;

import java.util.LinkedHashMap;
import java.util.Map;

public class QRISMPMParser {

    public static final String SEPARATOR = "";
    private static final int MAX_INDEX = 65;

    public Map<String, String> contents = new LinkedHashMap<>();

    // constructor automatically parse data
    public QRISMPMParser(String raw) {
        this.parseAsMap(raw);
    }

    public QRISMPMParser() {
    }

    // to get result
    public Map<String, String> getResult() { return contents; }

    // init process
    private void parseAsMap(String raw) {
        this.parse(raw, "");
    }

    // parse for root data and sub data
    private void parse(String rawData, String rootId) {
        int indexId = 0;
        while (true) {
            if (rawData.length() == 0) {
                break;
            }

            String currentId = rawData.substring(0, 2);
            String expectedId = String.format("%02d", indexId);

            if (currentId.equals(expectedId)) {
                String data = this.getContent(rawData);
                putContent(rootId.concat(expectedId), data);

                // if root id have sub id
                if (rootId.isEmpty() && this.isRootIdHaveSubId(currentId)) {
                    this.getSubContent(data, currentId);
                }
                rawData = this.stripContent(rawData, data.length());
            } else {
                putContent(rootId.concat(expectedId), null);
            }

            indexId++;
            if (indexId == MAX_INDEX) {
                break;
            }
        }
    }

    // check whether have sub, configure here (all of them will scan through MAX_INDEX)
    private boolean isRootIdHaveSubId(String rootId) {
        if (Integer.parseInt(rootId) >= 2 && Integer.parseInt(rootId) <= 51)
            return true;
        else if (Integer.parseInt(rootId) == 62)
            return true;
        else return Integer.parseInt(rootId) == 64;
    }

    public static String getQRISDataWithoutCRC(Map<String, String> map) {
        StringBuilder QRISData = new StringBuilder();
        for (int i = 0; i < 63; i++) {
//            String key = (i < 10 ? "0" + i : String.valueOf(i));
            String key = String.format("%02d", i);
            if (map.get(key) != null) {
                QRISData.append(key);
                String contentLen = String.format("%02d", map.get(key).length()); //map.get(key).length() < 10 ?
//                        "0" + map.get(key).length() : String.valueOf(map.get(key).length());
                QRISData.append(contentLen);
                QRISData.append(map.get(key));
            }
        }
        QRISData.append("6304");
        return QRISData.toString();
    }

    // get content by getting data length
    protected String getContent(String rawData) {
        int dataLength = Integer.parseInt(rawData.substring(2, 4));
        return rawData.substring(4, 4 + dataLength);
    }

    // strip the content which already gotten
    protected String stripContent(String remainingData, Integer length) {
        return remainingData.substring(4 + length);
    }

    // main process to get sub content
    private void getSubContent(String rawData, String rootId) {
        // if you wanna add separator for sub add .concat() after rootId
        parse(rawData, rootId.concat(SEPARATOR));
    }

    // set to Map
    protected void putContent(String key, String data) {
//        System.out.println(key + "-" + data);
        contents.put(key, data);
    }

    public static boolean compareCRC(Map<String, String> source, String crc) {
        String calculated = new CommonServiceImpl().Checksum(getQRISDataWithoutCRC(source));
        return crc.equals(calculated);
    }
}