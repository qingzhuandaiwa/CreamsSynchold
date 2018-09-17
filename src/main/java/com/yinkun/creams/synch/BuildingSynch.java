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
import com.jfinal.plugin.activerecord.Record;
import com.yinkun.creams.bean.BuildingModel;
import com.yinkun.creams.bean.ParkModel;
import com.yinkun.creams.service.BuildingService;
import com.yinkun.creams.service.FloorService;
import com.yinkun.creams.service.ParkService;
import com.yinkun.creams.utils.DbHelper;
import com.yinkun.workgo.test.kit.HttpHelper;

public class BuildingSynch implements Runnable{
	
	Logger logger = Logger.getLogger(BuildingSynch.class);
	
    public List<BuildingModel> insertDatas = new ArrayList<BuildingModel>(); 
    public List<BuildingModel> updateDatas = new ArrayList<BuildingModel>(); 

	private static String timeFormat = "yyyy-MM-dd HH:mm:ss";
	
	private Date lastUptDate = null;
	
	private String token;
	
	public BuildingSynch(String token) {
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
			result = HttpHelper.get(PropKit.use("config.properties").get("buildingUrl"),params,headers);
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
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + " :building 解析数据内容失败！");
			return false;
		}
		
		if(jsonResult.getIntValue("code") != 200) {
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + 
					" :building 网络接口调用出现问题,code: "+ jsonResult.getIntValue("code") +", message=" + jsonResult.getString("message") +" ！");
			return false;
		}
		
		JSONArray arr=jsonResult.getJSONArray("data");//获取的结果集合转换成数组
		String js=JSONObject.toJSONString(arr);//将array数组转换成字符串
		List<BuildingModel>  buildingS = JSONObject.parseArray(js, BuildingModel.class);//把字符串转换成集合
//		List<ParkModel> parkS = JSON.parseArray(jsonResult.getString("data"),ParkModel.class);
//		List<ParkModel> parkS = jsonResult.getData();
		
		if( buildingS == null || buildingS.size() <= 0) {
			logger.warn(new SimpleDateFormat(timeFormat).format(new Date()) + " :building 数据为空！");
			return false;
		}
		
		List<String> ids = new ArrayList<String>();
		StringBuilder sql = new StringBuilder("select * from ( ");
		
		try {
			for(Iterator<BuildingModel> it = buildingS.iterator();it.hasNext();) {
				BuildingModel buildingModel = it.next();
				sql.append("select '" + buildingModel.getId() + "' id UNION ");
//				ids.add();
//				if(buildingModel.getCtime().getTime() == buildingModel.getUtime().getTime() || buildingModel.getCtime().getTime() > lastUptDate.getTime()) {
//					this.insertDatas.add(buildingModel);
//				}else {
//					this.updateDatas.add(buildingModel);
//				}
			}
			int lastUnIndx = sql.lastIndexOf("UNION");
			int lastIndx = sql.length();
			if(sql.lastIndexOf("UNION") > 0) {
				sql.delete(sql.lastIndexOf("UNION"), lastIndx-1);
			}
			
			sql.append("from dual) temp where EXISTS (SELECT 1 from building where temp.id = building_id)");
			

			List<String> rcdS = DbHelper.getDb().query(sql.toString());
			
			for(Iterator<BuildingModel> it = buildingS.iterator();it.hasNext();) {
				BuildingModel buildModel = it.next();
				if(rcdS.contains(buildModel.getId())) {
					this.updateDatas.add(buildModel);
				}else {
					this.insertDatas.add(buildModel);
				}
			}
			
			
		} catch (Exception e) {
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + "遍历building失败");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	/**
	 * 插入数据
	 * @param parks
	 * @return
	 */
	private boolean insertDataS(List<BuildingModel> buildings) {
		int[] count = null;
		boolean isInsertSuccess = false;
		List<String> sqlList = new ArrayList<String>();


		try {
			for(Iterator<BuildingModel> it = buildings.iterator(); it.hasNext();) {
				BuildingModel building = it.next();
				String sql = "INSERT INTO building "
						+ "(building_id,building_name,park_id,park_name,address,image_url,remark,is_del,ctime,utime) VALUES "
						+ "(" +
						building.getId() + ", '"+ building.getBuildingName() +"', '" + 
						building.getParkId() + "', '" + building.getParkName() + "', '" + 
						building.getAddress() + "', '" + building.getImageUrl() + "', '" + building.getRemark() +"', " + 
						building.getIsDel() + ",'" + building.getCtime() + "','"+ building.getUtime() +"');";
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
	private boolean updateDataS(List<BuildingModel> buildings) {
		int[] count = null;
		boolean isUptSuccess = false;
		List<String> sqlList = new ArrayList<String>();
		try {
			for(Iterator<BuildingModel> it = buildings.iterator(); it.hasNext();) {
				BuildingModel building = it.next();
				
				String sql = "update building set building_name = '" + building.getBuildingName() + "', park_id='" + building.getParkId() + "', park_name='" + 
				building.getParkName() + "' , address='" + building.getAddress() + "', image_url='" + building.getImageUrl() + "', remark='" + 
				building.getRemark() +"',is_del = '"+ building.getIsDel()
				+"' ,ctime = '"+ building.getCtime() +"', utime= '"+ building.getUtime() +"' where building_id = " + building.getId() + ";";
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

	/**
	 * 同步数据
	 */
	public void SynchDatas() {
		if(insertDatas != null && insertDatas.size() > 0) {
			boolean isSuccess = insertDataS(insertDatas);
			if(isSuccess) {
				logger.info("新增成功");
			}else {
				logger.info("新增失败");
			}
		}
		if(updateDatas != null && updateDatas.size() > 0) {
			boolean isSuccess = updateDataS(updateDatas);
			if(isSuccess) {
				logger.info("更新成功");
			}else {
				logger.info("更新失败");
			}
		}
	}
	
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("BuildingSynch thread is running ...");
		Date lastDate = BuildingService.getLastUpdateDate();
		String result = fetchFromWebApi(token,lastDate);
		if(StrKit.isBlank(result)) {
//			System.out.println();
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + " :building 网络接口调用异常！");
			return;
		}
		Boolean isSuccess = ProcessDataS(result);
		if(isSuccess) {
			SynchDatas();
		}
		
	}

}
