package com.yinkun.creams.bean;

import java.sql.Timestamp;

public class ContractLeaseTermModel {
	private String id;
	
	private Timestamp termBeginDate;
	
	private Timestamp termEndDate;
	
	private String contractPayEnum;
	
	private String dayNumberForYear;
	
	private String calculateEnum;
	
	private String monthPriceConvertRoleEnum;
	
	private String leaseDivideRoleEnum;
	
	private String intervalMonth;
	
	private String payInAdvanceDay;
	
	private String paymentDateEnum;
	
	private String price;
	
	private String priceUnitEnum;
	
	private String monetaryUnit;
	
	private String contractId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Timestamp getTermBeginDate() {
		return termBeginDate;
	}

	public void setTermBeginDate(Timestamp termBeginDate) {
		this.termBeginDate = termBeginDate;
	}

	public Timestamp getTermEndDate() {
		return termEndDate;
	}

	public void setTermEndDate(Timestamp termEndDate) {
		this.termEndDate = termEndDate;
	}

	public String getContractPayEnum() {
		return contractPayEnum;
	}

	public void setContractPayEnum(String contractPayEnum) {
		this.contractPayEnum = contractPayEnum;
	}

	public String getDayNumberForYear() {
		return dayNumberForYear;
	}

	public void setDayNumberForYear(String dayNumberForYear) {
		this.dayNumberForYear = dayNumberForYear;
	}

	public String getCalculateEnum() {
		return calculateEnum;
	}

	public void setCalculateEnum(String calculateEnum) {
		this.calculateEnum = calculateEnum;
	}

	public String getMonthPriceConvertRoleEnum() {
		return monthPriceConvertRoleEnum;
	}

	public void setMonthPriceConvertRoleEnum(String monthPriceConvertRoleEnum) {
		this.monthPriceConvertRoleEnum = monthPriceConvertRoleEnum;
	}

	public String getLeaseDivideRoleEnum() {
		return leaseDivideRoleEnum;
	}

	public void setLeaseDivideRoleEnum(String leaseDivideRoleEnum) {
		this.leaseDivideRoleEnum = leaseDivideRoleEnum;
	}

	public String getIntervalMonth() {
		return intervalMonth;
	}

	public void setIntervalMonth(String intervalMonth) {
		this.intervalMonth = intervalMonth;
	}

	public String getPayInAdvanceDay() {
		return payInAdvanceDay;
	}

	public void setPayInAdvanceDay(String payInAdvanceDay) {
		this.payInAdvanceDay = payInAdvanceDay;
	}

	public String getPaymentDateEnum() {
		return paymentDateEnum;
	}

	public void setPaymentDateEnum(String paymentDateEnum) {
		this.paymentDateEnum = paymentDateEnum;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPriceUnitEnum() {
		return priceUnitEnum;
	}

	public void setPriceUnitEnum(String priceUnitEnum) {
		this.priceUnitEnum = priceUnitEnum;
	}

	public String getMonetaryUnit() {
		return monetaryUnit;
	}

	public void setMonetaryUnit(String monetaryUnit) {
		this.monetaryUnit = monetaryUnit;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
}
