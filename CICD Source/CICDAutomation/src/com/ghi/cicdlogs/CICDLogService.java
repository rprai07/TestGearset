package com.ghi.cicdlogs;

import java.util.Map;

import com.ghi.apex.ApexUtil;
import com.ghi.buildautomation.RunOnceAndReRun;
import com.ghi.common.SFUtility;
import com.sforce.soap.partner.PartnerConnection;

public class CICDLogService {

	public static void createCoverageCICDLog(String message, PartnerConnection partnerConn, String logFilePath, String buildStatus) { 
		SFUtility.log("Going to insert CICD Log record...", logFilePath);
		String apexScript = getInsertCoverageCICDLogScript(message, buildStatus, logFilePath);
		Map<String, String> executionStatusMap = ApexUtil.execute(partnerConn, apexScript.trim(), logFilePath);
		boolean isFailed = false;
		for(String key : executionStatusMap.keySet()) {
			if(key.equals("Failed")) {
				String exceptionMsg = executionStatusMap.get(key);
				SFUtility.log("Exception:: " + exceptionMsg, logFilePath);
				isFailed = true;
			}
		}
		if(!isFailed) {
			SFUtility.log("CICD Log record created...", logFilePath);			
		}
	}



	public static void createEnvProvCICDLog(String message, PartnerConnection partnerConn, String buildStatus, String logFilePath) { 
		SFUtility.log("Going to insert CICD Log record...", logFilePath);
		String apexScript = getInsertEnvProvCICDLogScript(message, buildStatus, logFilePath);
		Map<String, String> executionStatusMap = ApexUtil.execute(partnerConn, apexScript.trim(), logFilePath);
		boolean isFailed = false;
		for(String key : executionStatusMap.keySet()) {
			if(key.equals("Failed")) {
				String exceptionMsg = executionStatusMap.get(key);
				SFUtility.log("Exception:: " + exceptionMsg, logFilePath);
				isFailed = true;
			}
		}
		if(!isFailed) {
			SFUtility.log("CICD Log record created...", logFilePath);			
		}
	}

	
	public static void createBuildAutomationCICDLog(PartnerConnection partnerConn, String logFilePath, String apexFileName, String errorMessage, String scriptExecutionResult, boolean runOnce, int scriptNumber, String preOrPost, String buildNumber ) { 
		SFUtility.log("Going to insert CICD Log record...", logFilePath);
		String apexScript = getInsertBuildAutomationCICDLogScript(partnerConn, apexFileName, errorMessage, scriptExecutionResult, runOnce, scriptNumber, logFilePath, preOrPost, buildNumber);
		Map<String, String> executionStatusMap = ApexUtil.execute(partnerConn, apexScript.trim(), logFilePath);
		boolean isFailed = false;
		for(String key : executionStatusMap.keySet()) {
			if(key.equals("Failed")) {
				String exceptionMsg = executionStatusMap.get(key);
				SFUtility.log("Exception: " + exceptionMsg, logFilePath);
				isFailed = true;
			}
		}
		if(!isFailed) {
			SFUtility.log("CICD Log record created...", logFilePath);			
		}
	}

	
	
	public static String getInsertCoverageCICDLogScript(String message, String buildStatus, String logFilePath) {

		String apexScript = "CICD_Logs__c log = new CICD_Logs__c();"
				+ "log.Message__c = '<message>';"
				+ "log.Automation_Type__c = 'Code Coverage';"
				+ "log.Build_Status__c = '" + buildStatus + "';"
				+ "insert log;";

		message = message.replace("'", "");		
		message = message.replace("%", "");
		message = message.replace("\n", "   ");	 
		message = message.replace("\"\"", "double quotes");	 
		message = message.replace("\"", "double quotes");	  
		apexScript = apexScript.replace("<message>", message);
		SFUtility.log("apexScript:: "+apexScript, logFilePath);		

		return apexScript;


	}
	
	public static String getInsertEnvProvCICDLogScript(String message, String buildStatus, String logFilePath) {

		String apexScript = "CICD_Logs__c log = new CICD_Logs__c();"
				+ "log.Message__c = '<message>';"
				+ "log.Automation_Type__c = 'Environment Provisioning';"
				+ "log.Build_Status__c = '" + buildStatus + "';"
				+ "insert log;";

		message = message.replace("'", "");		
		message = message.replace("%", "");
		message = message.replace("\n", "   ");	 
		message = message.replace("\"\"", "");	 
		message = message.replace("\"", "");	  
		
		//message = message.replaceAll("[^a-zA-Z0-9\\s+]", "");
		//message = message.replaceAll("[^a-zA-Z0-9:,\n\\s+]", "");
		message = message.replaceAll("[^a-zA-Z0-9:,_\n\\s+]", "");



		apexScript = apexScript.replace("<message>", message);
		SFUtility.log("apexScript:: "+apexScript, logFilePath);		

		return apexScript;


	}


	public static String getInsertBuildAutomationCICDLogScript(PartnerConnection partnerConn, String apexFileName, String errorMessage, String scriptExecutionResult, boolean runOnce, int scriptNumber, String logFilePath, String preOrPost, String buildNumber) {

		String id = RunOnceAndReRun.getIdOfSkippedCICDLog(partnerConn, apexFileName, logFilePath, preOrPost);
		String apexScript = "";
		if(id != null && !id.trim().equals("")) {
			//Return update script
			
			apexScript = "List<CICD_Logs__c> logs = [SELECT Id,  Error_Message__c, Script_Execution_Result__c, Script_Number__c, Build_Number__c FROM CICD_Logs__c where id = '"+id+"'];"
					+ "if(logs != null && logs.size() ==1 ) {"
					+ "CICD_Logs__c log = logs.get(0);"
					+ "log.Error_Message__c = '<message>';"
					+ "log.Apex_File_Name__c = '" + apexFileName + "';"
					//+ "log.Script_Execution_Result__c = '" + scriptExecutionResult + "';"
					+ "log.Script_Number__c = '" + scriptNumber + "';"
					+ "log.Build_Number__c = '" + buildNumber + "';"
					+ "update log;"
					+ "}";
			
				
		} else {
			//Return insert script
			if(scriptExecutionResult.equals("Failed"))
			{
				apexScript = "List<CICD_Logs__c> logs = [SELECT Id,  Error_Message__c,Automation_Type__c, Script_Execution_Result__c, Script_Number__c, Apex_File_Name__c, Build_Number__c FROM CICD_Logs__c where Apex_File_Name__c = '"+apexFileName+"' and Script_Execution_Result__c ='Passed'];"
						+ "if(logs != null && logs.size() ==1 ) {"
						+ "CICD_Logs__c log = logs.get(0);"
						//+ "log.Error_Message__c = '<message>';"
						//+ "log.Apex_File_Name__c = '" + apexFileName + "';"
						//+ "log.Script_Execution_Result__c = '" + scriptExecutionResult + "';"
						+ "log.Script_Execution_Result__c = 'Rerun';"
						+ "update log;"
						+ "}"; 
			}
			apexScript += "CICD_Logs__c log = new CICD_Logs__c();"
					+ "log.Error_Message__c = '<message>';"
					+ "log.Automation_Type__c = 'Build Automation';"
					+ "log.Apex_File_Name__c = '" + apexFileName + "';"
					//+ "log.Run_Once__c = " + runOnce + ";"
					+ "log.Script_Execution_Result__c = '" + scriptExecutionResult + "';"
					+ "log.Script_Number__c = '" + scriptNumber + "';"	
					+ "log.Pre_Post_Ant_Script__c = '" + preOrPost + "';"	
					+ "log.Build_Number__c = '" + buildNumber + "';"
					+ "insert log;";
			
		}
		
		
		errorMessage = errorMessage.replace("'", "");		
		errorMessage = errorMessage.replace("%", "");
		errorMessage = errorMessage.replace("\n", "   ");	 
		errorMessage = errorMessage.replace("\"\"", "double quotes");	 
		errorMessage = errorMessage.replace("\"", "double quotes");	  
		apexScript = apexScript.replace("<message>", errorMessage);

		
		SFUtility.log("apexScript:: "+apexScript, logFilePath);		

		return apexScript;
	}	
}