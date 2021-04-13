package com.mlpt.merchant.transactionservice.cpm.util;

import com.mlpt.merchant.transactionservice.common.util.CommonUtil;

import java.util.Map;

public class QRISCPMResult {

    // extend this model to your desired model or use BeanUtils.copyProperties

    String payloadFormatIndicator;
    String applicationTemplate;
    String applicationDefinitionFileName;
    String applicationLabel;
    String track2EquivalentData;
    String applicationPAN;
    String cardHolderName;
    String languagePreference;
    String issuerURL;
    String applicationVersionNumber;
    String tokenRequesterID;
    String paymentAccountReference;
    String last4DigitPAN;
    String applicationSpecificTransparentTemplate;
    String applicationCryptogram;
    String cryptogramInformationData;
    String issuerApplicationData;
    String applicationTransactionCounter;
    String applicationInterchangeProfile;
    String unpredictableNumber;
    String issuerQRISData;

    Map<String, String> qrisParsed;

    public QRISCPMResult(String qrisRaw) {
        this.qrisParsed = new QRISCPMParser(qrisRaw).getResult();
        this.setContents();
    }

    public QRISCPMResult(Map<String, String> qrisParsed) {
        this.qrisParsed = qrisParsed;
        this.setContents();
    }

    private void setContents() {
        this.payloadFormatIndicator = qrisParsed.get("85");
        this.applicationTemplate = qrisParsed.get("61");
        this.applicationDefinitionFileName = qrisParsed.get("4F");
        this.applicationLabel = qrisParsed.get("50");
        this.track2EquivalentData = qrisParsed.get("57");
        this.applicationPAN = qrisParsed.get("5A");
        this.cardHolderName = qrisParsed.get("5F20");
        this.languagePreference = qrisParsed.get("5F2D");
        this.issuerURL = qrisParsed.get("5F50");
        this.applicationVersionNumber = qrisParsed.get("9F08");
        this.tokenRequesterID = qrisParsed.get("9F19");
        this.paymentAccountReference = qrisParsed.get("9F24");
        this.last4DigitPAN = qrisParsed.get("9F25");
//        this.applicationSpecificTransparentTemplate = qrisParsed.get("63");
        this.applicationCryptogram = qrisParsed.get("9F26");
        this.cryptogramInformationData = qrisParsed.get("9F27");
        this.issuerApplicationData = qrisParsed.get("9F10");
        this.applicationTransactionCounter = qrisParsed.get("9F36");
        this.applicationInterchangeProfile = qrisParsed.get("82");
        this.unpredictableNumber = qrisParsed.get("9F37");
        this.issuerQRISData = qrisParsed.get("9F74");
    }

    public String getPayloadFormatIndicator() {
        return payloadFormatIndicator;
    }

    public void setPayloadFormatIndicator(String payloadFormatIndicator) {
        this.payloadFormatIndicator = payloadFormatIndicator;
    }

    public String getApplicationTemplate() {
        return applicationTemplate;
    }

    public void setApplicationTemplate(String applicationTemplate) {
        this.applicationTemplate = applicationTemplate;
    }

    public String getApplicationDefinitionFileName() {
        return applicationDefinitionFileName;
    }

    public void setApplicationDefinitionFileName(String applicationDefinitionFileName) {
        this.applicationDefinitionFileName = applicationDefinitionFileName;
    }

    public String getApplicationLabel() {
        return applicationLabel;
    }

    public void setApplicationLabel(String applicationLabel) {
        this.applicationLabel = applicationLabel;
    }

    public String getTrack2EquivalentData() {
        return track2EquivalentData;
    }

    public void setTrack2EquivalentData(String track2EquivalentData) {
        this.track2EquivalentData = track2EquivalentData;
    }

    public String getApplicationPAN() {
        return applicationPAN;
    }

    public void setApplicationPAN(String applicationPAN) {
        this.applicationPAN = applicationPAN;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public String getIssuerURL() {
        return issuerURL;
    }

    public void setIssuerURL(String issuerURL) {
        this.issuerURL = issuerURL;
    }

    public String getApplicationVersionNumber() {
        return applicationVersionNumber;
    }

    public void setApplicationVersionNumber(String applicationVersionNumber) {
        this.applicationVersionNumber = applicationVersionNumber;
    }

    public String getTokenRequesterID() {
        return tokenRequesterID;
    }

    public void setTokenRequesterID(String tokenRequesterID) {
        this.tokenRequesterID = tokenRequesterID;
    }

    public String getPaymentAccountReference() {
        return paymentAccountReference;
    }

    public void setPaymentAccountReference(String paymentAccountReference) {
        this.paymentAccountReference = paymentAccountReference;
    }

    public String getLast4DigitPAN() {
        return last4DigitPAN;
    }

    public void setLast4DigitPAN(String last4DigitPAN) {
        this.last4DigitPAN = last4DigitPAN;
    }

    public String getApplicationSpecificTransparentTemplate() {
        return applicationSpecificTransparentTemplate;
    }

    public void setApplicationSpecificTransparentTemplate(String applicationSpecificTransparentTemplate) {
        this.applicationSpecificTransparentTemplate = applicationSpecificTransparentTemplate;
    }

    public String getApplicationCryptogram() {
        return applicationCryptogram;
    }

    public void setApplicationCryptogram(String applicationCryptogram) {
        this.applicationCryptogram = applicationCryptogram;
    }

    public String getCryptogramInformationData() {
        return cryptogramInformationData;
    }

    public void setCryptogramInformationData(String cryptogramInformationData) {
        this.cryptogramInformationData = cryptogramInformationData;
    }

    public String getIssuerApplicationData() {
        return issuerApplicationData;
    }

    public void setIssuerApplicationData(String issuerApplicationData) {
        this.issuerApplicationData = issuerApplicationData;
    }

    public String getApplicationTransactionCounter() {
        return applicationTransactionCounter;
    }

    public void setApplicationTransactionCounter(String applicationTransactionCounter) {
        this.applicationTransactionCounter = applicationTransactionCounter;
    }

    public String getApplicationInterchangeProfile() {
        return applicationInterchangeProfile;
    }

    public void setApplicationInterchangeProfile(String applicationInterchangeProfile) {
        this.applicationInterchangeProfile = applicationInterchangeProfile;
    }

    public String getUnpredictableNumber() {
        return unpredictableNumber;
    }

    public void setUnpredictableNumber(String unpredictableNumber) {
        this.unpredictableNumber = unpredictableNumber;
    }

    public String getIssuerQRISData() {
        return issuerQRISData;
    }

    public void setIssuerQRISData(String issuerQRISData) {
        this.issuerQRISData = issuerQRISData;
    }

    @Override
    public String toString() {
        return CommonUtil.printPrettyJSON(this);
    }
}
