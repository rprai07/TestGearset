package com.ghi.buildautomation;

import java.util.List;

import com.ghi.common.SFUtility;
import com.ghi.data.SOAPUtil;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.partner.PartnerConnection;

public class RunOnceAndReRun {

	/*public static boolean isAlreadyExecutedSucessfully(PartnerConnection connection, String scriptFileName, String logFilePath) {
		String query =  "select id from CICD_Logs__c where Run_Once__c = true and Script_Execution_Result__c = 'Passed' and Automation_Type__c = 'Build Automation' and Apex_File_Name__c  = '"+scriptFileName + "'";

		List<com.sforce.soap.partner.sobject.SObject> objList = SOAPUtil.executeQuery(connection, query, logFilePath); 
		if(objList != null && objList.size() > 0) {
			return true;
		}

		return false;
	}*/
	
	
	public static String getIdOfSkippedCICDLog(PartnerConnection connection, String scriptFileName, String logFilePath, String preOrPost) {
	String query =  "select id from CICD_Logs__c where Script_Execution_Result__c = 'Skipped' and Automation_Type__c = 'Build Automation' and Script_Number__c = '' and Apex_File_Name__c  = '"+scriptFileName + "'" +  " and Pre_Post_Ant_Script__c  = '"+preOrPost + "' order by createddate desc limit 1";

	List<com.sforce.soap.partner.sobject.SObject> objList = SOAPUtil.executeQuery(connection, query, logFilePath); 
	if(objList != null && objList.size() > 0) {
		SFUtility.log("Skipped CICD Log ID:: "+objList.get(0).getId(), logFilePath);		

		return objList.get(0).getId();
	}

	return null;
}

	
}
