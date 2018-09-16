package com.yinkun.creams.synch;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.yinkun.creams.app.AppConfig;
import com.yinkun.creams.app.SynchExecutor;
import com.yinkun.creams.utils.AccessToken;

public class ParkSynchTest {

	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testFetchFromWebApi() {
//		fail("Not yet implemented");
//		parkSynch.fetchFromWebApi(AccessToken.getToken());
		SynchExecutor.executeSysch();
	}

}
