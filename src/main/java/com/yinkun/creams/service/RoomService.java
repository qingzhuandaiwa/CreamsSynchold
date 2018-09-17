package com.yinkun.creams.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Record;
import com.yinkun.creams.bean.RoomModel;
import com.yinkun.creams.synch.ParkSynch;
import com.yinkun.creams.utils.DbHelper;

public class RoomService {
	
	private static Logger logger = Logger.getLogger(RoomService.class);
	
	private static String timeFormat = "yyyy-MM-dd HH:mm:ss";
	
	public static Date getLastUpdateDate() {
//		Park lastData = Park.dao.findFirst("SELECT * from park ORDER BY utime DESC LIMIT 1");
//		Date date = lastData.getUtime();
//		return date;
		String sql = "SELECT * from room ORDER BY utime DESC LIMIT 1";
		Record rcd = DbHelper.getDb().findFirst(sql);
		if(rcd!=null) {
			return rcd.getDate("utime");
		}else {
			return null;
		}
	}
	
	/**
	 * 插入数据
	 * @param parks
	 * @return
	 */
	public static boolean insertDataS(List<RoomModel> rooms) {
		int[] count = null;
		boolean isInsertSuccess = false;
		List<String> sqlList = new ArrayList<String>();


		try {
			for(Iterator<RoomModel> it = rooms.iterator(); it.hasNext();) {
				RoomModel room = it.next();
				String sql = "INSERT INTO room "
						+ "(room_id,room_number,floor_id,floor,building_id,building_name,park_id,park_name,fitment,image_url,remark,is_del,ctime,utime) VALUES "
						+ "(" +
						room.getId() + ", '"+ room.getRoomNumber() +"', '" + 
						room.getFloorId() + "', '" + room.getFloor() + "', '" + 
						room.getBuildingId() + "', '" + room.getBuildingName() +  "', '" + room.getParkId() + "', '" + 
						room.getParkName() + "', '"  + room.getFitment() + "', '"  + room.getImageUrl() + "', '" + room.getRemark() +"', " + 
						room.getIsDel() + ",'" + room.getCtime() + "','"+ room.getUtime() +"');";
				sqlList.add(sql);
			}
			count = DbHelper.getDb().batch(sqlList, sqlList.size());
		} catch (Exception e) {
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + " :room 批量插入失败！");
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
	public static boolean updateDataS(List<RoomModel> rooms) {
		int[] count = null;
		boolean isUptSuccess = false;
		List<String> sqlList = new ArrayList<String>();
		try {
			for(Iterator<RoomModel> it = rooms.iterator(); it.hasNext();) {
				RoomModel room = it.next();
				
				String sql = "update room set room_number = '" + room.getRoomNumber() + "', floor_id='" + room.getFloorId() + "', floor='" + 
				room.getFloor() + "' , building_id='" + room.getBuildingId() + "', building_name='" + room.getBuildingName() + "', park_id='" + 
				room.getParkId() +  "', park_name='" + room.getParkName() + "', fitment='" + room.getFitment() + "', image_url='" + room.getImageUrl() + "', remark='" + 
				room.getRemark() +"',is_del = '"+ room.getIsDel()+"' ,ctime = '"+ room.getCtime() +"', utime= '"+ 
				room.getUtime() +"' where room_id = " + room.getId() + ";";
				sqlList.add(sql);
			}
			
			count = DbHelper.getDb().batch(sqlList, sqlList.size());
		} catch (Exception e) {
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + " : building 批量更新失败！");
			e.printStackTrace();
		}
		if(count != null && count.length > 0) {
			isUptSuccess = true;
		}
		return isUptSuccess;
	}
}
