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
import com.yinkun.creams.bean.ParkModel;
import com.yinkun.creams.service.BuildingService;
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
			System.out.println(result);
			
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
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + " :building 数据为空！");
			return false;
		}
		
		try {
			for(Iterator<BuildingModel> it = buildingS.iterator();it.hasNext();) {
				BuildingModel buildingModel = it.next();
				if(buildingModel.getCtime().getTime() == buildingModel.getUtime().getTime() || buildingModel.getCtime().getTime() > lastUptDate.getTime()) {
					this.insertDatas.add(buildingModel);
				}else {
					this.updateDatas.add(buildingModel);
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
			logger.info(new SimpleDateFormat(timeFormat).format(new Date()) + " :building 批量插入失败！");
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
	private boolean updateDataS(List<ParkModel> parks) {
		int[] count = null;
		boolean isUptSuccess = false;
		List<String> sqlList = new ArrayList<String>();
		try {
			for(Iterator<ParkModel> it = parks.iterator(); it.hasNext();) {
				ParkModel park = it.next();
				String sql = "update park set park_name = '"+ park.getParkName() +"',is_del = '"+ park.getIsDel()
				+"' ,ctime = '"+ park.getCtime() +"', utime= '"+ park.getUtime() +"' where park_id = " + park.getId() + ";";
				sqlList.add(sql);
			}
			
			
			count = DbHelper.getDb().batch(sqlList, sqlList.size());
		} catch (Exception e) {
			logger.info(new SimpleDateFormat(timeFormat).format(new Date()) + " : park 批量更新失败！");
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
			insertDataS(insertDatas);
		}
		if(updateDatas != null && updateDatas.size() > 0) {
//			updateDataS(updateDatas);
		}
	}
	
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("BuildingSynch thread is running ...");
		Date lastDate = BuildingService.getLastUpdateDate();
		String result = fetchFromWebApi(token,lastDate);
		if(StrKit.isBlank(result)) {
			System.out.println(new SimpleDateFormat(timeFormat).format(new Date()) + " :building 网络接口调用异常！");
			return;
		}
		Boolean isSuccess = ProcessDataS(result);
		if(isSuccess) {
			SynchDatas();
		}
		
	}

}
