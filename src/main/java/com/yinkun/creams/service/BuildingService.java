package com.yinkun.creams.service;

import java.util.Date;

import com.jfinal.plugin.activerecord.Record;
import com.yinkun.creams.utils.DbHelper;

public class BuildingService {

	
	public static Date getLastUpdateDate() {
		String sql = "SELECT * from building ORDER BY utime DESC LIMIT 1";
		Record rcd = DbHelper.getDb().findFirst(sql);
		if(rcd!=null) {
			return rcd.getDate("utime");
		}else {
			return null;
		}
	}
}
