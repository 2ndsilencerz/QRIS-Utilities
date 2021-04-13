package qris;

import java.util.Locale;
import java.util.Map;

public class QRISMPMBuilder extends QRISMPMParser {

    public QRISMPMBuilder(QRISMPMResult qrismpmResult) {
        this.setContents(qrismpmResult);
    }
    
    private void setContents(QRISMPMResult qrismpmResult) {
        contents.put("00", qrismpmResult.payloadFormatIndicator);
        contents.put("01", qrismpmResult.pointOfInitiationMethod);

        // replace primaryTag with default number (its 26 or above, max 45)
        // tag 02 only for VISA
        // tag 04 only for MasterCard
        String primaryTag = "26";
        contents.put(primaryTag,
                "00".concat(String.format("%02d", qrismpmResult.globalUniqueIdentifier.length()))
                        .concat(qrismpmResult.globalUniqueIdentifier)
                .concat("01").concat(String.format("%02d", qrismpmResult.merchantPAN.length()))
                        .concat(qrismpmResult.merchantPAN)
                .concat("02").concat(String.format("%02d", qrismpmResult.merchantID.length()))
                        .concat(qrismpmResult.merchantID)
                .concat("03").concat(String.format("%02d", qrismpmResult.merchantCriteria.length()))
                        .concat(qrismpmResult.merchantCriteria));

        // tag 51 is optional if the tag 02 until 45 isn't filled yet
//        String guidAlt = "ID.CO.QRIS.WWW";
//        String merchantAccountInformation = "00".concat(String.format("%02d", guidAlt.length()))
//                .concat(guidAlt) +
//                "02".concat(String.format("%02d", qrismpmResult.merchantPAN.length()))
//                        .concat(qrismpmResult.merchantPAN) +
//                "03".concat(String.format("%02d", qrismpmResult.merchantCriteria.length()))
//                        .concat(qrismpmResult.merchantCriteria);
//        contents.put("51", merchantAccountInformation);

        contents.put("52", qrismpmResult.merchantCategoryCode);
        contents.put("53", qrismpmResult.transactionCurrency);
        contents.put("54", qrismpmResult.transactionAmount);
        if (qrismpmResult.tipIndicator != null) {
            contents.put("55", qrismpmResult.tipIndicator);
            if (qrismpmResult.tipIndicator.equals("02")) {
                contents.put("56", qrismpmResult.tipFixedValue);
            } else if (qrismpmResult.tipIndicator.equals("03")) {
                contents.put("57", qrismpmResult.tipPercentageValue);
            }
        }

        contents.put("58", qrismpmResult.countryCode);
        contents.put("59", qrismpmResult.merchantName);
        contents.put("60", qrismpmResult.merchantCity);
        contents.put("61", qrismpmResult.postalCode);

        // set tag 62 contents
        StringBuilder additionalDataField = new StringBuilder();
        if (qrismpmResult.billNumber != null && !qrismpmResult.billNumber.isEmpty()) {
            additionalDataField.append("01");
            additionalDataField.append(String.format("%02d", qrismpmResult.billNumber.length()));
            additionalDataField.append(qrismpmResult.billNumber);
        }
        if (qrismpmResult.mobileNumber != null && !qrismpmResult.mobileNumber.isEmpty()) {
            additionalDataField.append("02");
            additionalDataField.append(String.format("%02d", qrismpmResult.mobileNumber.length()));
            additionalDataField.append(qrismpmResult.mobileNumber);
        }
        if (qrismpmResult.storeLabel != null && !qrismpmResult.storeLabel.isEmpty()) {
            additionalDataField.append("03");
            additionalDataField.append(String.format("%02d", qrismpmResult.storeLabel.length()));
            additionalDataField.append(qrismpmResult.storeLabel);
        }
        if (qrismpmResult.loyaltyNumber != null && !qrismpmResult.loyaltyNumber.isEmpty()) {
            additionalDataField.append("04");
            additionalDataField.append(String.format("%02d", qrismpmResult.loyaltyNumber.length()));
            additionalDataField.append(qrismpmResult.loyaltyNumber);
        }
        if (qrismpmResult.referenceLabel != null && !qrismpmResult.referenceLabel.isEmpty()) {
            additionalDataField.append("05");
            additionalDataField.append(String.format("%02d", qrismpmResult.referenceLabel.length()));
            additionalDataField.append(qrismpmResult.referenceLabel);
        }
        if (qrismpmResult.customerLabel != null && !qrismpmResult.customerLabel.isEmpty()) {
            additionalDataField.append("06");
            additionalDataField.append(String.format("%02d", qrismpmResult.customerLabel.length()));
            additionalDataField.append(qrismpmResult.customerLabel);
        }
        if (qrismpmResult.terminalLabel != null && !qrismpmResult.terminalLabel.isEmpty()) {
            additionalDataField.append("07");
            additionalDataField.append(String.format("%02d", qrismpmResult.terminalLabel.length()));
            additionalDataField.append(qrismpmResult.terminalLabel);
        }
        if (qrismpmResult.purposeOfTransaction != null && !qrismpmResult.purposeOfTransaction.isEmpty()) {
            additionalDataField.append("08");
            additionalDataField.append(String.format("%02d", qrismpmResult.purposeOfTransaction.length()));
            additionalDataField.append(qrismpmResult.purposeOfTransaction);
        }
        if (qrismpmResult.additionalConsumerDataRequest != null &&
                !qrismpmResult.additionalConsumerDataRequest.isEmpty()) {
            additionalDataField.append("09");
            additionalDataField.append(String.format("%02d", qrismpmResult.additionalConsumerDataRequest.length()));
            additionalDataField.append(qrismpmResult.additionalConsumerDataRequest);
        }
        if (additionalDataField.toString().length() > 0) {
            contents.put("62", additionalDataField.toString());
        }

        // tag 64 is optional (for alternative)
//        contents.put("64".concat("00"), qrismpmResult.languagePreference);
//        contents.put("64".concat("01"), qrismpmResult.merchantNameAlt);
//        contents.put("64".concat("02"), qrismpmResult.merchantCityAlt);
        if (qrismpmResult.languagePreference != null) {
            contents.put("64",
                    "00".concat(String.format("%02d", qrismpmResult.languagePreference.length()))
                    .concat(qrismpmResult.languagePreference)
                    .concat("01").concat(String.format("%02d", qrismpmResult.merchantNameAlt.length()))
                    .concat(qrismpmResult.merchantNameAlt)
                    .concat("02").concat(String.format("%02d", qrismpmResult.merchantCityAlt.length()))
                    .concat(qrismpmResult.merchantCityAlt));
        }
    }

    public String getQRISResult() {

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : contents.entrySet()) {
//            System.out.println(entry.getKey() + "-" + entry.getValue());
            if (entry.getValue() != null) {
//                System.out.println(entry.getKey() + "-" + entry.getValue().length() + "-" + entry.getValue());
                stringBuilder.append(entry.getKey());
                stringBuilder.append(String.format("%02d", entry.getValue().length()));
                stringBuilder.append(entry.getValue());
            }
        }
        String crc = Utils.Checksum(stringBuilder.toString().concat("6304")).toUpperCase(Locale.ROOT);
        stringBuilder.append("6304".concat(crc));
        return stringBuilder.toString();
    }
}
