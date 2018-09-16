package com.yinkun.creams.app;

import java.util.Date;

import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;
import com.yinkun.creams.model._MappingKit;
import com.yinkun.creams.service.ParkService;
import com.yinkun.creams.utils.AccessToken;
import com.yinkun.workgo.test.ApiTestController;
import com.yinkun.workgo.test.kit.HttpHelper;
import com.yinkun.workgo.test.kit.HttpUtils;

public class AppConfig extends JFinalConfig {
	
	static Log log = Log.getLog(AppConfig.class);

	public void configConstant(Constants constants) {
    	PropKit.use("config.properties");
    	constants.setDevMode(false);
    }

    public static void main(String[] argv) {
        JFinal.start("src/main/webapp", 9999, "/");
    }
    
    
    public void configRoute(Routes routes) {
        routes.add("/api", ApiTestController.class);
    }

    public void configEngine(Engine engine) {

    }

    public void configPlugin(Plugins me) {
//		EhCachePlugin ehCachePlugin = createEhCachePlugin();
//		me.add(ehCachePlugin);
		
		// 配置 druid 数据库连接池插件
		DruidPlugin druidPlugin = new DruidPlugin(PropKit.use("config.properties").get("jdbcUrl"), PropKit.use("config.properties").get("user"), PropKit.use("config.properties").get("password").trim());
		me.add(druidPlugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		// 所有映射在 MappingKit 中自动化搞定
		_MappingKit.mapping(arp);
		me.add(arp);
    }

    public void configInterceptor(Interceptors interceptors) {

    }

    public void configHandler(Handlers handlers) {
    	
    }
    
	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.use("config.properties").get("jdbcUrl"), PropKit.use("config.properties").get("user"), PropKit.use("config.properties").get("password").trim());
	}
	
//	savePark
  @Override
  public void afterJFinalStart() {
  
	  ParkService.getLastUpdateDate();
  }
    
//    @Override
//    public void afterJFinalStart() {
//    	log.info("sychapp is started!");
//    	try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//    	
//    	String accessToken = AccessToken.getAccessToken(PropKit.use("config.properties").get("creamsUser"),
//    			PropKit.use("config.properties").get("creamsPass"), PropKit.use("config.properties").get("tokenUrl"));
//    	
//    	if(StrKit.isBlank(accessToken)) {
//        	log.error("-------获取TOKEN失败--任务未执行---"+ new Date());
//        	return;
//    	}
//    	Token = Bearer + accessToken;
//    }
//
//	public static String getToken() {
//		return Token;
//	}
}
