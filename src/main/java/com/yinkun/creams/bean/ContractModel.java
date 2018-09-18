package com.yinkun.creams.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class ContractModel {
	
	List<ContractRoomModel> rooms;
	
	List<ContractContactModel> contacts;
	
	List<ContractLeaseTermModel> leaseTerms;
	
	private String id;
	
	private String contractNo;
	
	private String enterpriseId;
	
	private String enterpriseName;
	
	private String contractState;
	
	private String terminationState;
	
	private String nullificationState;

	private String contractStatus;
	
	private String buildingId;
	
	private String buildingName;
	
	private BigDecimal areaSize;
	
	private String buildingType;
	
	private String deposit;
	
	private String depositUnitEnum;
	
	private Timestamp leaseBeginDate;
	
	private Timestamp leaseEndDate;
	
	private Timestamp signDate;
	
	private String isDel;
	
	private String contractStateName;
	
	private String showStatus;
	
	private Timestamp ctime;

	private Timestamp utime;

	public List<ContractRoomModel> getRooms() {
		return rooms;
	}

	public void setRooms(List<ContractRoomModel> rooms) {
		this.rooms = rooms;
	}

	public List<ContractContactModel> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContractContactModel> contacts) {
		this.contacts = contacts;
	}

	public List<ContractLeaseTermModel> getLeaseTerms() {
		return leaseTerms;
	}

	public void setLeaseTerms(List<ContractLeaseTermModel> leaseTerms) {
		this.leaseTerms = leaseTerms;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getContractState() {
		return contractState;
	}

	public void setContractState(String contractState) {
		this.contractState = contractState;
	}

	public String getTerminationState() {
		return terminationState;
	}

	public void setTerminationState(String terminationState) {
		this.terminationState = terminationState;
	}

	public String getNullificationState() {
		return nullificationState;
	}

	public void setNullificationState(String nullificationState) {
		this.nullificationState = nullificationState;
	}

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public BigDecimal getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(BigDecimal areaSize) {
		this.areaSize = areaSize;
	}

	public String getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getDepositUnitEnum() {
		return depositUnitEnum;
	}

	public void setDepositUnitEnum(String depositUnitEnum) {
		this.depositUnitEnum = depositUnitEnum;
	}

	public Timestamp getLeaseBeginDate() {
		return leaseBeginDate;
	}

	public void setLeaseBeginDate(Timestamp leaseBeginDate) {
		this.leaseBeginDate = leaseBeginDate;
	}

	public Timestamp getLeaseEndDate() {
		return leaseEndDate;
	}

	public void setLeaseEndDate(Timestamp leaseEndDate) {
		this.leaseEndDate = leaseEndDate;
	}

	public Timestamp getSignDate() {
		return signDate;
	}

	public void setSignDate(Timestamp signDate) {
		this.signDate = signDate;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public String getContractStateName() {
		return contractStateName;
	}

	public void setContractStateName(String contractStateName) {
		this.contractStateName = contractStateName;
	}

	public String getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}

	public Timestamp getCtime() {
		return ctime;
	}

	public void setCtime(Timestamp ctime) {
		this.ctime = ctime;
	}

	public Timestamp getUtime() {
		return utime;
	}

	public void setUtime(Timestamp utime) {
		this.utime = utime;
	}
	
}
