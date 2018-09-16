package com.yinkun.creams.config;

public class ImDbConfig {

	private boolean enable = false;
	private String type = null;
	private String url = null;
	private String username = null;
	private String password = null;
	private boolean showSql = false;
	
	public boolean getEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean getShowSql() {
		return showSql;
	}
	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}
	
	
	
	
}
