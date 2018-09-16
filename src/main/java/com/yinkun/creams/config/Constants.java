package com.yinkun.creams.config;

final class Constants {
	
	private String dbConfigFilePath ="config.properties";//数据库配置文件路径
	
	public String dbConfigFilePath() {
		return dbConfigFilePath;
	}

	public void setDevMode(String dbConfigFilePath) {
		this.dbConfigFilePath = dbConfigFilePath;
	}
	
	
	
}
