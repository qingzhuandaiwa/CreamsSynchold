package com.yinkun.creams.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.yinkun.creams.synch.ParkSynch;
import com.yinkun.creams.utils.AccessToken;

public class SynchExecutor {

	private static String token;
	
	public static void main(String[] args){
		executeSysch();
	}
	/**
	 * 开始同步
	 */
	public static void executeSysch() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
		getToken();
		ParkSynch ps = new ParkSynch(token);
		fixedThreadPool.execute(ps);
	}

	public static void getToken() {
		token = AccessToken.getToken();
	}
}
