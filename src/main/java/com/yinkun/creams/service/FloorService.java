package com.yinkun.creams.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Record;
import com.yinkun.creams.bean.BuildingModel;
import com.yinkun.creams.bean.FloorModel;
import com.yinkun.creams.synch.FloorSynch;
import com.yinkun.creams.utils.DbHelper;

public class FloorService {
	static Logger logger = Logger.getLogger(FloorSynch.class);
	
	private static String timeFormat = "yyyy-MM-dd HH:mm:ss";

	public static Date getLastUpdateDate() {
		String sql = "SELECT * from floor ORDER BY utime DESC LIMIT 1";
		Record rcd = DbHelper.getDb().findFirst(sql);
		if(rcd!=null) {
			return rcd.getDate("utime");
//			rcd.getTimestamp("utime");
		}else {
			return null;
		}
	}
	
	/**
	 * 插入数据
	 * @param parks
	 * @return
	 */
	public static boolean insertDataS(List<FloorModel> floors) {
		int[] count = null;
		boolean isInsertSuccess = false;
		List<String> sqlList = new ArrayList<String>();


		try {
			for(Iterator<FloorModel> it = floors.iterator(); it.hasNext();) {
				FloorModel floor = it.next();
				String sql = "INSERT INTO floor "
						+ "(floor_id,floor,building_id,building_name,park_id,park_name,remark,is_del,ctime,utime) VALUES "
						+ "(" +
						floor.getId() + ", '"+ floor.getFloor() +"', '" + floor.getBuildingId() + "', '" + floor.getBuildingName() + "', '" +  
						floor.getParkId() + "', '" + floor.getParkName() + "', '" + floor.getRemark() +"', " + 
						floor.getIsDel() + ",'" + floor.getCtime() + "','"+ floor.getUtime() +"')";
				sqlList.add(sql);
			}
			count = DbHelper.getDb().batch(sqlList, sqlList.size());
		} catch (Exception e) {
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + " :building 批量插入失败！");
			e.printStackTrace();
		}
		
		if(count != null && count.length > 0) {
			isInsertSuccess = true;
		}
		return isInsertSuccess;
	}
	
	/**
	 * 修改数据
	 * @param parks
	 * @return
	 */
	public static boolean updateDataS(List<FloorModel> floors) {
		int[] count = null;
		boolean isUptSuccess = false;
		List<String> sqlList = new ArrayList<String>();
		try {
			for(Iterator<FloorModel> it = floors.iterator(); it.hasNext();) {
				FloorModel floor = it.next();
				
				String sql = "update floor set floor = '" + floor.getFloor() + "' ,building_id='" + floor.getBuildingId() + 
						"', building_name='" + floor.getBuildingName() +  "', park_id='" + floor.getParkId() + "', park_name='" + 
						floor.getParkName() + "', remark='" + 
						floor.getRemark() +"',is_del = '"+ floor.getIsDel()
				+"' ,ctime = '"+ floor.getCtime() +"', utime= '"+ floor.getUtime() +"' where floor_id = " + floor.getId() + ";";
				sqlList.add(sql);
			}
			
			
			count = DbHelper.getDb().batch(sqlList, sqlList.size());
		} catch (Exception e) {
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + " : floor 批量更新失败！");
			e.printStackTrace();
		}
		if(count != null && count.length > 0) {
			isUptSuccess = true;
		}
		return isUptSuccess;
	}
}
