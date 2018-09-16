package com.yinkun.creams.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.yinkun.creams.app.AppConfig;
import com.yinkun.workgo.test.kit.HttpHelper;

public class AccessToken {
	static Log log = Log.getLog(AccessToken.class);
	
	public static String Bearer = "Bearer ";
	public static String Token = null;
	
	public static String getToken() {
		
		log.info("get Token...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String accessToken = AccessToken.getAccessToken(PropKit.use("config.properties").get("creamsUser"),
				PropKit.use("config.properties").get("creamsPass"), PropKit.use("config.properties").get("tokenUrl"));
		
		if(StrKit.isBlank(accessToken)) {
	    	log.error("-------获取TOKEN失败--任务未执行---"+ new Date());
	    	return null;
		}
		Token = Bearer + accessToken;
		return Token;
	}
	
	/**
	 * 获取TOKEN
	 * @param username
	 * @param password
	 * @param tokenUrl
	 * @return
	 */
	@SuppressWarnings("finally")
	public static String getAccessToken(String username, String password, String tokenUrl){
		System.out.println("-------获取TOKEN--START----");
		Map<String, String> params = new HashMap<String, String>();
        params.put("clientId", "web_app");
        params.put("password", password);
        params.put("username", username);
        String paras = JSONObject.toJSON(params).toString();
        Map<String,String> headers = new HashMap<String,String>();
		headers.put("Content-Type", "application/json; charset=utf-8");
		String accessToken = "";
        try {
        	String result = HttpHelper.post(tokenUrl,paras,headers);
			Map maps = (Map)JSONObject.parse(result);
	        accessToken = (String)maps.get("accessToken");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("-------获取TOKEN---ERROR---" + e.getMessage());
		}finally{
			System.out.println("-------获取TOKEN---END---");
			return accessToken;
		}
	}
	
	/**
	 * 替换参数
	 * @param list
	 * @param accessToken
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public static void replaceAccessToken(List<Source> list, String accessToken){
//        //extractor.run();
//        for(Source s:list){
//        	//String header = s.getStr("header").replaceAll("XXXXXXXX", accessToken);
//        	((Map)s.getObj("header")).put("Authorization", "Bearer "+accessToken);
//        }
//	}
}
