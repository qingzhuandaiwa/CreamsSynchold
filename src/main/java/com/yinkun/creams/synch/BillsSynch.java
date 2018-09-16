package com.yinkun.creams.synch;

import java.sql.SQLException;
import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yinkun.creams.service.ParkService;
import com.yinkun.creams.utils.DbHelper;

public class BillsSynch implements Runnable{

	public void run() {
		System.out.println("parkSynch thread is running ...");
		Date lastDate = ParkService.getLastUpdateDate();
		
		saveTest();
	}

//	@Before(Tx.class)
	private void saveTest() {
		
		Db.tx(new IAtom() {
			   public boolean run() throws SQLException {
				   String sql0 = 
							"INSERT INTO bill (park_id,park_name,is_del,ctime,utime) VALUES(12,'nametest','0',NOW(),NOW())";
					
					DbHelper.getDb().update(sql0);
					
					String sql1 = 
							"INSERT INTO park (park_id,park_name,is_del,ctime,utime) VALUES(12,'nametest','0',NOW(),'')";
					
					DbHelper.getDb().update(sql1);
					return true;
			   }
			});
		
	}  
	
	

}
