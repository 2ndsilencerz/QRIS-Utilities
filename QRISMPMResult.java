package com.mlpt.merchant.transactionservice.mpm.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mlpt.merchant.transactionservice.common.util.CommonUtil;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;
import java.util.Map;

@MappedSuperclass
public class QRISMPMResult {

	// extend this model to your desired model or use BeanUtils.copyProperties
	String payloadFormatIndicator;
	String pointOfInitiationMethod;
	String globalUniqueIdentifier;
	String merchantPAN;
	String merchantID;
	String merchantCriteria;
	String merchantAccountInformation;
	String merchantCategoryCode;
	String transactionCurrency;
	String transactionAmount;
	String tipIndicator;
	String tipFixedValue;
	String tipPercentageValue;
	String countryCode;
	String merchantName;
	String merchantCity;
	String postalCode;
	String additionalDataField;
	String billNumber;
	String mobileNumber;
	String storeLabel;
	String loyaltyNumber;
	String referenceLabel;
	String customerLabel;
	String terminalLabel;
	String purposeOfTransaction;
	String additionalConsumerDataRequest;
	String crc;
	String languagePreference;
	String merchantNameAlt;
	String merchantCityAlt;

	// database value
	Date createdDate;
	Date validUntil;
	String qrisStatus = QRISStatus.ACTIVE.toString();
	Date inactiveDate;

	public enum QRISStatus {
		ACTIVE, INACTIVE
	}

	@Transient
	Map<String, String> qrisParsed;

	public QRISMPMResult() {
	}

	@JsonIgnore
	public QRISMPMResult(String qrisRaw) {
		this.qrisParsed = new QRISMPMParser(qrisRaw).getResult();
		this.setContents();
	}

	@JsonIgnore
	public QRISMPMResult(Map<String, String> qrisParsed) {
		this.qrisParsed = qrisParsed;
		this.setContents();
	}

	@JsonIgnore
	private void setContents() {
		this.payloadFormatIndicator = qrisParsed.get("00");
		this.pointOfInitiationMethod = qrisParsed.get("01");
		for (int i = 2; i <= 45; i++) {
			String index = String.format("%02d", i);
			if (null != qrisParsed.get(index)) {
				String guid = qrisParsed.get(index.concat(QRISMPMParser.SEPARATOR).concat("00"));
				String mpan = qrisParsed.get(index.concat(QRISMPMParser.SEPARATOR).concat("01"));
				String mid = qrisParsed.get(index.concat(QRISMPMParser.SEPARATOR).concat("02"));
				String criteria = qrisParsed.get(index.concat(QRISMPMParser.SEPARATOR).concat("03"));
				this.globalUniqueIdentifier = guid;
				this.merchantPAN = mpan;
				this.merchantID = mid;
				this.merchantCriteria = criteria;
				break;
			}
		}

		this.merchantAccountInformation = qrisParsed.get("51");
		if (this.merchantID == null && null != qrisParsed.get("51")) {
			this.globalUniqueIdentifier = qrisParsed.get("51" + QRISMPMParser.SEPARATOR + "00");
			this.merchantID = qrisParsed.get("51" + QRISMPMParser.SEPARATOR + "02");
			this.merchantCriteria = qrisParsed.get("51" + QRISMPMParser.SEPARATOR + "03");
		}
		
		//for cash out purpose
		if(this.merchantCriteria == null && null != qrisParsed.get("51")) {
			this.merchantCriteria = qrisParsed.get("51" + QRISMPMParser.SEPARATOR + "03");
		}

		this.merchantCategoryCode = qrisParsed.get("52");
		this.transactionCurrency = qrisParsed.get("53");
		this.transactionAmount = qrisParsed.get("54");
		if (null != qrisParsed.get("55")) {
			this.tipIndicator = qrisParsed.get("55");
			if (this.tipIndicator.equals("02")) {
				this.tipFixedValue = qrisParsed.get("56");
			} else if (this.tipIndicator.equals("03")) {
				this.tipPercentageValue = qrisParsed.get("57");
			}
		}

		this.countryCode = qrisParsed.get("58");
		this.merchantName = qrisParsed.get("59");
		this.merchantCity = qrisParsed.get("60");
		this.postalCode = qrisParsed.get("61");
		this.additionalDataField = qrisParsed.get("62");

		if (null != qrisParsed.get("62")) {
			this.getBit62Contents(qrisParsed);
			if (this.terminalLabel == null || this.terminalLabel.isEmpty()) {
				this.terminalLabel = String.format("%-16s",
						qrisParsed.get("62" + QRISMPMParser.SEPARATOR + "07") != null
								? qrisParsed.get("62" + QRISMPMParser.SEPARATOR + "07")
								: qrisParsed.get("62"));
			}
		}
		this.crc = qrisParsed.get("63");
		if (null != qrisParsed.get("64")) {
			this.getBit64Contents(qrisParsed);
		}
	}

	@JsonIgnore
	private void getBit62Contents(Map<String, String> qrisParsed) {
		String[] contents = new String[9];
		for (int i = 1; i < 10; i++) {
//			try {
				String key = "62" + QRISMPMParser.SEPARATOR + String.format("%02d", i);
				contents[i - 1] = qrisParsed.get(key);
//			} catch (Exception ignored) {
//			}
		}
		this.billNumber = contents[0];
		this.mobileNumber = contents[1];
		this.storeLabel = contents[2];
		this.loyaltyNumber = contents[3];
		this.referenceLabel = contents[4];
		this.customerLabel = contents[5];
		this.terminalLabel = contents[6];
//		System.out.println(this.terminalLabel);
		this.purposeOfTransaction = contents[7];
		this.additionalConsumerDataRequest = contents[8];
	}

	@JsonIgnore
	private void getBit64Contents(Map<String, String> qrisParsed) {
		String[] contents = new String[3];
		for (int i = 0; i < 3; i++) {
			try {
				String key = "64" + QRISMPMParser.SEPARATOR + String.format("%02d", i);
				contents[i] = qrisParsed.get(key);
			} catch (Exception ignored) {
			}
		}
		this.languagePreference = contents[0];
		this.merchantNameAlt = contents[1];
		this.merchantCity = contents[2];
	}

	public String getPayloadFormatIndicator() {
		return payloadFormatIndicator;
	}

	public void setPayloadFormatIndicator(String payloadFormatIndicator) {
		this.payloadFormatIndicator = payloadFormatIndicator;
	}

	public String getPointOfInitiationMethod() {
		return pointOfInitiationMethod;
	}

	public void setPointOfInitiationMethod(String pointOfInitiationMethod) {
		this.pointOfInitiationMethod = pointOfInitiationMethod;
	}

	public String getGlobalUniqueIdentifier() {
		return globalUniqueIdentifier;
	}

	public void setGlobalUniqueIdentifier(String globalUniqueIdentifier) {
		this.globalUniqueIdentifier = globalUniqueIdentifier;
	}

	public String getMerchantPAN() {
		return merchantPAN;
	}

	public void setMerchantPAN(String merchantPAN) {
		this.merchantPAN = merchantPAN;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getMerchantCriteria() {
		return merchantCriteria;
	}

	public void setMerchantCriteria(String merchantCriteria) {
		this.merchantCriteria = merchantCriteria;
	}

	public String getMerchantAccountInformation() {
		return merchantAccountInformation;
	}

	public void setMerchantAccountInformation(String merchantAccountInformation) {
		this.merchantAccountInformation = merchantAccountInformation;
	}

	public String getMerchantCategoryCode() {
		return merchantCategoryCode;
	}

	public void setMerchantCategoryCode(String merchantCategoryCode) {
		this.merchantCategoryCode = merchantCategoryCode;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTipIndicator() {
		return tipIndicator;
	}

	public void setTipIndicator(String tipIndicator) {
		this.tipIndicator = tipIndicator;
	}

	public String getTipFixedValue() {
		return tipFixedValue;
	}

	public void setTipFixedValue(String tipFixedValue) {
		this.tipFixedValue = tipFixedValue;
	}

	public String getTipPercentageValue() {
		return tipPercentageValue;
	}

	public void setTipPercentageValue(String tipPercentageValue) {
		this.tipPercentageValue = tipPercentageValue;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantCity() {
		return merchantCity;
	}

	public void setMerchantCity(String merchantCity) {
		this.merchantCity = merchantCity;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getAdditionalDataField() {
		return additionalDataField;
	}

	public void setAdditionalDataField(String additionalDataField) {
		this.additionalDataField = additionalDataField;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getStoreLabel() {
		return storeLabel;
	}

	public void setStoreLabel(String storeLabel) {
		this.storeLabel = storeLabel;
	}

	public String getLoyaltyNumber() {
		return loyaltyNumber;
	}

	public void setLoyaltyNumber(String loyaltyNumber) {
		this.loyaltyNumber = loyaltyNumber;
	}

	public String getReferenceLabel() {
		return referenceLabel;
	}

	public void setReferenceLabel(String referenceLabel) {
		this.referenceLabel = referenceLabel;
	}

	public String getCustomerLabel() {
		return customerLabel;
	}

	public void setCustomerLabel(String customerLabel) {
		this.customerLabel = customerLabel;
	}

	public String getTerminalLabel() {
		return terminalLabel;
	}

	public void setTerminalLabel(String terminalLabel) {
		this.terminalLabel = terminalLabel;
	}

	public String getPurposeOfTransaction() {
		return purposeOfTransaction;
	}

	public void setPurposeOfTransaction(String purposeOfTransaction) {
		this.purposeOfTransaction = purposeOfTransaction;
	}

	public String getAdditionalConsumerDataRequest() {
		return additionalConsumerDataRequest;
	}

	public void setAdditionalConsumerDataRequest(String additionalConsumerDataRequest) {
		this.additionalConsumerDataRequest = additionalConsumerDataRequest;
	}

	public String getCrc() {
		return crc;
	}

	public void setCrc(String crc) {
		this.crc = crc;
	}

	public String getLanguagePreference() {
		return languagePreference;
	}

	public void setLanguagePreference(String languagePreference) {
		this.languagePreference = languagePreference;
	}

	public String getMerchantNameAlt() {
		return merchantNameAlt;
	}

	public void setMerchantNameAlt(String merchantNameAlt) {
		this.merchantNameAlt = merchantNameAlt;
	}

	public String getMerchantCityAlt() {
		return merchantCityAlt;
	}

	public void setMerchantCityAlt(String merchantCityAlt) {
		this.merchantCityAlt = merchantCityAlt;
	}

	public Map<String, String> getQrisParsed() {
		return qrisParsed;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	public String getQrisStatus() {
		return qrisStatus;
	}

	public void setQrisStatus(String qrisStatus) {
		this.qrisStatus = qrisStatus;
	}

	public Date getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(Date inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	@Override
	public String toString() {
		return CommonUtil.printPrettyJSON(this);
	}
}
