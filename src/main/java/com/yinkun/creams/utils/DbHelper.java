package com.yinkun.creams.utils;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.SqlServerDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.yinkun.creams.config.ImConfig;
import com.yinkun.creams.config.ImDbConfig;
/**
 * 指标数据库操作类
 * @author gj
 * @date 2018-04-10
 * @version 0.1
 * 
 */
public class DbHelper {
	public final static String CONFIG_NAME = "INDICATOR_DB_DATASOURCE";//数据库配置别名
	private static boolean status = false;//是否加载数据库配置, 默认没有加载
	/**
	 * 按配置文件启动数据库
	 */
	public static void start(){
		start(new ImDbConfig());
	}
	
	/**
	 * 可自定义配置启动数据库
	 * @param imDbConfig 自定义配置bean
	 */
	public static void start(ImDbConfig imDbConfig){
		
		try{//检测该数据源是否已经存在
			Db.use(CONFIG_NAME);
			status = true;
		}catch(Exception e){
			status = false;
		}
		
		if(!status){
			System.out.println("加载数据库配置开始...");
//			if(!imDbConfig.getEnable())
//				imDbConfig.setEnable(ImConfig.getConfig().getDbEnable());
			
			if(StrKit.isBlank(imDbConfig.getType()))
				imDbConfig.setType(ImConfig.getConfig().getDbType());
			
			if(!imDbConfig.getShowSql())
				imDbConfig.setShowSql(ImConfig.getConfig().getDbShowSql());
			
			String url = "";
			String username = "";
			String password = "";
			url = ImConfig.getConfig().getDbUrl();
			username = ImConfig.getConfig().getDbUserame();
			password = ImConfig.getConfig().getDbPassword();
			
			DruidPlugin dp = new DruidPlugin(url,username, password);
			if(imDbConfig.getType().equals("mysql")){
				dp.setDriverClass("com.mysql.jdbc.Driver");
				dp.setValidationQuery("select 1 from dual");
			}
			dp.addFilter(new StatFilter());
			WallFilter wall = new WallFilter();
			wall.setDbType("mysql");
			dp.addFilter(wall);
			dp.start();
			
			ActiveRecordPlugin arp = new ActiveRecordPlugin(CONFIG_NAME, dp);
			if(imDbConfig.getShowSql()){//显示打印sql语句
				arp.setShowSql(true);
			}
			if(imDbConfig.getType().equals("mysql")){
				arp.setDialect(new MysqlDialect());
			}
			arp.setContainerFactory(new CaseInsensitiveContainerFactory());
			
			arp.start();
			
			status = true;//启动成功
			System.out.println("加载数据库配置结束！");
		}else{
			System.out.println("数据库配置已经启动！");
		}
	}
	

	/**
	 * 加载数据库配置
	 * @param configName
	 * @param jdbcUrl
	 * @param userName
	 * @param password
	 * @param dbType 数据库类型 mysql oracle sqlserver
	 * @throws Exception 
	 */
	public static void loadDBConfig(String configName, String jdbcUrl, String userName, String password, String dbType) throws Exception{
		try{
			try{//检测该数据源是否已经存在
				Db.use(configName);
				return;
			}catch(Exception e){
			}
			
			DruidPlugin dp = new DruidPlugin(jdbcUrl,userName, password);
			if(StrKit.notBlank(dbType) && dbType.equals("oracle")){
				dp.setDriverClass(JdbcConstants.ORACLE_DRIVER);
			}else if(StrKit.notBlank(dbType) && dbType.equals("sqlserver")){
				dp.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			}else{
				dp.setDriverClass(JdbcConstants.MYSQL_DRIVER);
			}
			dp.addFilter(new StatFilter());
			WallFilter wall = new WallFilter();
			if(StrKit.notBlank(dbType) && dbType.equals("oracle")){
				wall.setDbType(JdbcConstants.ORACLE);
			}else if(StrKit.notBlank(dbType) && dbType.equals("sqlserver")){
				wall.setDbType(JdbcConstants.SQL_SERVER);
			}else{
				wall.setDbType(JdbcConstants.MYSQL);
			}
			dp.addFilter(wall);
			dp.start();
			
			ActiveRecordPlugin arp = new ActiveRecordPlugin(configName, dp);
			arp.setShowSql(true);
			if(StrKit.notBlank(dbType) && dbType.equals("oracle")){
				arp.setDialect(new OracleDialect());
			}else if(StrKit.notBlank(dbType) && dbType.equals("sqlserver")){
				arp.setDialect(new SqlServerDialect());
			}else{
				arp.setDialect(new MysqlDialect());
			}
			arp.setContainerFactory(new CaseInsensitiveContainerFactory());
			arp.start();
		}catch(IllegalArgumentException e){
			System.out.println(e.getMessage());
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 指标操作数据库接口(统一使用该方法操作指标)
	 * @return
	 */
	public static DbPro getDb(){
		DbPro dp = null;
		try{
			dp = Db.use(CONFIG_NAME);
		}catch(Exception e){
			start();
			dp = Db.use(CONFIG_NAME);
		}
		
		return dp;
	}
	
	
//
//	public static void main(String[] args) {
//		ImDbConfig imDbConfig = new ImDbConfig();
//		imDbConfig.setType("mysql");
//		imDbConfig.setUrl("jdbc:mysql://127.0.0.1:3306/order?characterEncoding=utf-8&amp;autoReconnect=true");
//		imDbConfig.setUsername("root");
//		imDbConfig.setPassword("root");
//		DbHelper.start(imDbConfig);
//		DbHelper.getDb().find("select * from sec_user");
//		DbHelper.start(imDbConfig);
//		
//	}
//	
	
	
}

