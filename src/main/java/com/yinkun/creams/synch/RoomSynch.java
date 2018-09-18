package com.yinkun.creams.synch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.yinkun.creams.bean.BuildingModel;
import com.yinkun.creams.bean.FloorModel;
import com.yinkun.creams.bean.ParkModel;
import com.yinkun.creams.bean.RoomModel;
import com.yinkun.creams.service.ParkService;
import com.yinkun.creams.service.RoomService;
import com.yinkun.creams.utils.DbHelper;
import com.yinkun.workgo.test.kit.HttpHelper;

public class RoomSynch implements Runnable{

	private Date lastUptDate = null;
	
	private static String timeFormat = "yyyy-MM-dd HH:mm:ss";

	Logger logger = Logger.getLogger(ParkSynch.class);
	
    public List<RoomModel> insertDatas = new ArrayList<RoomModel>(); 
    public List<RoomModel> updateDatas = new ArrayList<RoomModel>(); 
	
	private String token;
	
	public RoomSynch(String token) {
		this.token = token;
	}
	
	
	/**
	 * 从webapi中获取数据
	 * @param token
	 * @param lastUptDate 最近一次更新时间
	 */
	@SuppressWarnings("deprecation")
	public String fetchFromWebApi(String token,Date lastUptDate) {
		Date lastDate = lastUptDate;
		//日期转字符串
		if(lastDate == null) {
			Calendar now = Calendar.getInstance();
			now.add(Calendar.MONTH, -3);// 月份减1 
			lastDate = now.getTime();
		}
		
		this.lastUptDate = lastDate;
		
		Map<String, Object> params = new HashMap<String, Object>();
		int[] buildingIds = new int[0];
        params.put("queryDateFrom", new SimpleDateFormat(timeFormat).format(lastDate));
//        String paras = JSONObject.toJSON(params).toString();
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Content-Type", "application/json; charset=utf-8");
		headers.put("Authorization", token);
		String result = null;
		try {
//			result = HttpHelper.post(PropKit.use("config.properties").get("parkUrl"),paras,headers);
			result = HttpHelper.get(PropKit.use("config.properties").get("roomUrl"),params,headers);
//			System.out.println(result);
			logger.info(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 处理返回的数据
	 * @param result
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Boolean ProcessDataS(String result) {
		JSONObject jsonResult = null;
		
//		List<Student> studentList1 = JSON.parseArray(JSON.parseObject(json).getString("studentList"), Student.class);
		
		try {
			jsonResult = JSONObject.parseObject(result);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + " :park 解析数据内容失败！");
			return false;
		}
		
		if(jsonResult.getIntValue("code") != 200) {
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + 
					" :park 网络接口调用出现问题,code: "+ jsonResult.getIntValue("code") +", message=" + jsonResult.getString("message") +" ！");
			return false;
		}
		
		JSONArray arr=jsonResult.getJSONArray("data");//获取的结果集合转换成数组
		String js=JSONObject.toJSONString(arr);//将array数组转换成字符串
		List<RoomModel>  rooms = JSONObject.parseArray(js, RoomModel.class);//把字符串转换成集合
//		List<ParkModel> parkS = JSON.parseArray(jsonResult.getString("data"),ParkModel.class);
//		List<ParkModel> parkS = jsonResult.getData();
		
		if( rooms == null || rooms.size() <= 0) {
			logger.warn(new SimpleDateFormat(timeFormat).format(new Date()) + " :room 数据为空！");
			return false;
		}
		
		StringBuilder sql = new StringBuilder("select * from ( ");
		
		try {
			for(Iterator<RoomModel> it = rooms.iterator();it.hasNext();) {
				RoomModel room = it.next();
//				if(room.getCtime().getTime() == room.getUtime().getTime() || room.getCtime().getTime() > lastUptDate.getTime()) {
//					this.insertDatas.add(room);
//				}else {
//					this.updateDatas.add(room);
//				}
				sql.append("select '" + room.getId() + "' id UNION ");
			}
			
			int lastUnIndx = sql.lastIndexOf("UNION");
			int length = sql.length();
			if(sql.lastIndexOf("UNION") > 0) {
				sql.delete(lastUnIndx, length-1);
			}
			
			sql.append("from dual) temp where EXISTS (SELECT 1 from room where temp.id = room_id)");
			List<String> rcdS = DbHelper.getDb().query(sql.toString());
			
			for(Iterator<RoomModel> it = rooms.iterator();it.hasNext();) {
				RoomModel roomModel = it.next();
				if(rcdS.contains(roomModel.getId())) {
					this.updateDatas.add(roomModel);
				}else {
					this.insertDatas.add(roomModel);
				}
			}
		} catch (Exception e) {
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + "遍历rooms失败");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 同步数据
	 */
	public void SynchDatas() {
		if(insertDatas != null && insertDatas.size() > 0) {
			boolean isSuccess = RoomService.insertDataS(insertDatas);
			if(isSuccess) {
				logger.info("新增成功");
			}else {
				logger.info("新增失败");
			}
		}
		if(updateDatas != null && updateDatas.size() > 0) {
			boolean isSuccess = RoomService.updateDataS(updateDatas);
			if(isSuccess) {
				logger.info("更新成功");
			}else {
				logger.info("更新失败");
			}
		}
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("RoomSynch thread is running ...");
		Date lastDate = RoomService.getLastUpdateDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastDate);
		calendar.add(Calendar.MINUTE, -1);//
		String result = fetchFromWebApi(token,calendar.getTime());
		if(StrKit.isBlank(result)) {
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + " :park 网络接口调用异常！");
			return;
		}
		Boolean isSuccess = ProcessDataS(result);
		if(isSuccess) {
			SynchDatas();
		}
	}

}
