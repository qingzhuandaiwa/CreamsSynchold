package com.yinkun.creams.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Record;
import com.yinkun.creams.bean.FloorModel;
import com.yinkun.creams.bean.TenantModel;
import com.yinkun.creams.utils.DbHelper;

public class TenantService {
	
	private static Logger logger = Logger.getLogger(TenantService.class);
	
	private static String timeFormat = "yyyy-MM-dd HH:mm:ss";
	
	public static Date getLastUpdateDate() {
//		Park lastData = Park.dao.findFirst("SELECT * from park ORDER BY utime DESC LIMIT 1");
//		Date date = lastData.getUtime();
//		return date;
		String sql = "SELECT * from tenant ORDER BY utime DESC LIMIT 1";
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
	public static boolean insertDataS(List<TenantModel> tenants) {
		int[] count = null;
		boolean isInsertSuccess = false;
		List<String> sqlList = new ArrayList<String>();


		try {
			for(Iterator<TenantModel> it = tenants.iterator(); it.hasNext();) {
				TenantModel tenant = it.next();
				
//				String approvalTime = "'0000-00-00 00:00:00'";
				String approvalTime = null;
				if(tenant.getApprovalTime() != null) {
					approvalTime = "'" + tenant.getApprovalTime()+ "'";
				}
				String foundingTime =null;
				if( tenant.getFoundingTime() !=null) {
					foundingTime =  "'" +tenant.getFoundingTime() + "'";
				}
				
				String sql = "INSERT INTO tenant "
						+ "(enterprise_id,enterprise_name,contacts,certificate_number,legal_person,"
						+ "has_current_contract,tel,email,address,remark,approval_time,birth_country,"
						+ "business_address,business_industry,business_scope,business_term,company_type,district,"
						+ "english_name,founding_time,operating_state,organization_code,registered_capital,"
						+ "registration_authority,registration_number,staff_size,taxpayerId_number,unifiedSocialCreditCode,"
						+ "is_del,ctime,utime) VALUES "
						+ "(" +
						tenant.getId() + ", '"+ tenant.getEnterpriseName() +"', '" + tenant.getContacts() + "', '" + tenant.getCertificateNumber() + "', '" +  
						tenant.getLegalPerson() + "', '" + tenant.getHasCurrentContract()+ "', '" + tenant.getTel()+"', '" + tenant.getEmail() + "', '" + 
						tenant.getAddress() + "', '" + tenant.getRemark() + "', " + approvalTime + ", '" + tenant.getBirthCountry() + "', '" + tenant.getBusinessAddress() + "', '" + 
						tenant.getBusinessIndustry() + "', '" + tenant.getBusinessScope() + "', '" + tenant.getBusinessTerm() + "', '" + tenant.getCompanyType() + "', '" + tenant.getDistrict() + "', '" + 
						tenant.getEnglishName() + "', " + foundingTime + ", '" + tenant.getOperatingState() + "', '" + tenant.getOrganizationCode() + "', '" + tenant.getRegisteredCapital() + "', '" +
						tenant.getRegistrationAuthority() + "', '" + tenant.getRegistrationNumber() + "', '" + tenant.getStaffSize() + "', '" + tenant.getTaxpayerIdNumber() + "', '" + tenant.getUnifiedSocialCreditCode() + "', " +
						tenant.getIsDel() + ",'" + tenant.getCtime() + "','"+ tenant.getUtime() +"')";
				sqlList.add(sql);
			}
			count = DbHelper.getDb().batch(sqlList, sqlList.size());
		} catch (Exception e) {
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + " :tenant 批量插入失败！");
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
	public static boolean updateDataS(List<TenantModel> tenants) {
		int[] count = null;
		boolean isUptSuccess = false;
		List<String> sqlList = new ArrayList<String>();
		try {
			for(Iterator<TenantModel> it = tenants.iterator(); it.hasNext();) {
				TenantModel tenant = it.next();
				String approvalTime = null;
				if(tenant.getApprovalTime() != null) {
					approvalTime = "'" + tenant.getApprovalTime()+ "'";
				}
				String foundingTime =null;
				if( tenant.getFoundingTime() !=null) {
					foundingTime =  "'" +tenant.getFoundingTime() + "'";
				}
				
				String sql = "update tenant set enterprise_name = '" + tenant.getEnterpriseName() + "' ,contacts='" + tenant.getContacts() + 
						"', certificate_number='" + tenant.getCertificateNumber() +  "', legal_person='" + tenant.getLegalPerson() + "', has_current_contract='" + 
						tenant.getHasCurrentContract() + "', tel='" + tenant.getTel() + "', email='" + tenant.getEmail() + "', address='" + tenant.getAddress() + "', remark='" + tenant.getRemark() + 
						"', approval_time=" + approvalTime + ", birth_country='" + tenant.getBirthCountry() + "',business_address='" + tenant.getBusinessAddress() + 
						"', business_industry='" + tenant.getBusinessIndustry() + "', business_scope='" + tenant.getBusinessScope() + "', business_term='" + tenant.getBusinessTerm() + 
						"', company_type='" + tenant.getCompanyType() + "', district='" + tenant.getDistrict() + "', english_name='" + tenant.getEnglishName() + "', founding_time=" + foundingTime + 
						", operating_state='" + tenant.getOperatingState() + "', organization_code='" +tenant.getOrganizationCode() + "', registered_capital='" + tenant.getRegisteredCapital() + 
						"', registration_authority='" + tenant.getRegistrationAuthority() + "', registration_number='" + tenant.getRegistrationNumber() + "', staff_size='" + tenant.getStaffSize() + 
						"', taxpayerId_number='" + tenant.getTaxpayerIdNumber() + "', unifiedSocialCreditCode='" + tenant.getUnifiedSocialCreditCode() +
						"',is_del = '"+ tenant.getIsDel() +"' ,ctime = '"+ tenant.getCtime() +"', utime= '"+ tenant.getUtime() +"' where enterprise_id = " + tenant.getId() + ";";
				sqlList.add(sql);
			}
			
			
			count = DbHelper.getDb().batch(sqlList, sqlList.size());
		} catch (Exception e) {
			logger.error(new SimpleDateFormat(timeFormat).format(new Date()) + " : tenant 批量更新失败！");
			e.printStackTrace();
		}
		if(count != null && count.length > 0) {
			isUptSuccess = true;
		}
		return isUptSuccess;
	}
}
