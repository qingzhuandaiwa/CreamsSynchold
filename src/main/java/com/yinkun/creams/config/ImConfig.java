package com.yinkun.creams.config;


public class ImConfig {
	private static final Constants constants = new Constants();//常量信息
	private static final Config config = new Config();//配置信息
	
	private ImConfig() {
	}
	
	public static final Config getConfig() {
		return config;
	}
	
	/**
	 * 获取常量对象
	 * @return
	 */
	public static final Constants getConstants() {
		return constants;
	}
	
	
}
