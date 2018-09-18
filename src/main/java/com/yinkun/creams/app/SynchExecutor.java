package com.yinkun.creams.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.jfinal.log.Log;
import com.yinkun.creams.synch.BillsSynch;
import com.yinkun.creams.synch.BuildingSynch;
import com.yinkun.creams.synch.FloorSynch;
import com.yinkun.creams.synch.ParkSynch;
import com.yinkun.creams.synch.RoomSynch;
import com.yinkun.creams.synch.TenantSynch;
import com.yinkun.creams.utils.AccessToken;

public class SynchExecutor {
	
	public static Log logger = Log.getLog(SynchExecutor.class);

	private static String token;
	
	public static void main(String[] args){
		executeSysch();
	}
	/**
	 * 寮�濮嬪悓姝�
	 */
	public static void executeSysch() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
		getToken();
		ParkSynch ps = new ParkSynch(token);
		BillsSynch bs = new BillsSynch();
		BuildingSynch buildingSynch = new BuildingSynch(token);
		FloorSynch floorSynch = new FloorSynch(token);
		RoomSynch roomSynch = new RoomSynch(token);
		TenantSynch tenantSynch = new TenantSynch(token);
//		fixedThreadPool.execute(ps);
		fixedThreadPool.execute(buildingSynch);
		fixedThreadPool.execute(floorSynch);
		fixedThreadPool.execute(roomSynch);
		fixedThreadPool.execute(tenantSynch);
	}

	public static void getToken() { 
		token = AccessToken.getToken();
		System.out.println("token is : " + token);
	}
}
