package com.yinkun.creams.bean;

import java.sql.Timestamp;

public class ParkModel {
	
	private String id;
	
	private String parkName;
	
	private Timestamp ctime;
	
	private Timestamp utime;

	private String isDel;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
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
	
	
	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
}
