package com.ghi.main;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ghi.apex.ApexUtil;
import com.ghi.cicdlogs.CICDLogService;
import com.ghi.common.SFUtility;
import com.ghi.coverage.CodeCoverage;
import com.ghi.data.SOAPUtil;
import com.ghi.ut.UnitTestingUtils;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.partner.PartnerConnection;
 
public class Main {

	private static String APEX_FOLDER = "";
	private static String LOG_FOLDER = "";	
	private static String BUILD_NUMBER = "";		
	private static final String DOC_FOLDER = "\\docs"; 
	private static String LOG_FILE_PATH = "";
	private static final String LOG_FILE_NAME = "_log.txt";	
	private static String EXECUTION_SERIES = "";
	private static String WORKING_DIR = ""; 
	private static boolean isFailure = false;
	private static String SF_USERNAME = "";
	private static String SF_PASSWORD = "";
	private static String ENVIRONMENT = "";
	private static String AUTOMATION_TYPE = "";
	private static String ICRT_USERNAME = "";
	private static String ICRT_PASSWORD = "";	
	private static String DRIVER_PATH = "";
	private static EnterpriseConnection enterConn = null;
	private static String VALIDATE_COVERAGE = "";
	private static String REPO_FOLDER_PATH = "";
	private static String CODE_COVERAGE_SCRIPT_FOLDER = "";	
	private static boolean isCoverageTest = false;
	//private static boolean isBuildAutomationWithRunOnceReRunFeature = false;	
	//private static String EXECUTION_SERIES_RERUN_OF_RUNONCE_SCRIPTS = "";
	

	public static void main(String[] args) {
		try {  
			int length = args.length;		
			System.out.println("arguments length: " + length);	

			if(length == 9) {

				String arg0 = args[0];
				
				// Code coverage validation				
				if(arg0 != null && arg0.trim().equalsIgnoreCase("false")) {
					System.out.println("Code Coverage Validation is disabled, exiting from coverage check...");
					System.exit(0);
				}
				
				if(arg0 != null && arg0.trim().equalsIgnoreCase("true")) {
					isCoverageTest = true;
					VALIDATE_COVERAGE = args[0];
					CODE_COVERAGE_SCRIPT_FOLDER = args[1]; 
					REPO_FOLDER_PATH = args[2];	
					WORKING_DIR = args[3]; 
					LOG_FOLDER = args[4];
					BUILD_NUMBER = args[5];
					SF_USERNAME = args[6];
					SF_PASSWORD = args[7];	
					ENVIRONMENT = args[8];	
					validateFolderExistance(CODE_COVERAGE_SCRIPT_FOLDER);
					validateFolderExistance(REPO_FOLDER_PATH);
					validateFolderExistance(WORKING_DIR);
					validateFolderExistance(LOG_FOLDER);				
					
					LOG_FILE_PATH = LOG_FOLDER + "\\"+ BUILD_NUMBER + LOG_FILE_NAME; 
					SFUtility.log("Validate Coverage(true/false): " + VALIDATE_COVERAGE, LOG_FILE_PATH);
					SFUtility.log("Code Coverage Scripts Folder Path: " + CODE_COVERAGE_SCRIPT_FOLDER, LOG_FILE_PATH);	
					SFUtility.log("Repo Folder Path: " + REPO_FOLDER_PATH, LOG_FILE_PATH);						
					SFUtility.log("Working Directory: " + WORKING_DIR, LOG_FILE_PATH);						
					SFUtility.log("Log folder path: " + LOG_FOLDER, LOG_FILE_PATH); 		
					SFUtility.log("Build Number: " + BUILD_NUMBER, LOG_FILE_PATH); 		
					SFUtility.log("SFDC User Name: " + SF_USERNAME, LOG_FILE_PATH); 		
					SFUtility.log("Environment: " + ENVIRONMENT, LOG_FILE_PATH);
				}
			} 

			if( !isCoverageTest && length >= 8) {

				APEX_FOLDER = args[0];	
				WORKING_DIR = args[1];
				LOG_FOLDER = args[2];
				BUILD_NUMBER = args[3];
				EXECUTION_SERIES = args[4];
				SF_USERNAME = args[5];
				SF_PASSWORD = args[6];	
				ENVIRONMENT = args[7];
				
				//if( length == 9) {
					//EXECUTION_SERIES_RERUN_OF_RUNONCE_SCRIPTS = args[8];
					//isBuildAutomationWithRunOnceReRunFeature = true;
				//}

				validateFolderExistance(APEX_FOLDER);
				validateFolderExistance(WORKING_DIR);
				validateFolderExistance(LOG_FOLDER);
				
				
				LOG_FILE_PATH = LOG_FOLDER + "\\"+ BUILD_NUMBER + LOG_FILE_NAME; 

				SFUtility.log("Scripts folder path: " + APEX_FOLDER, LOG_FILE_PATH);
				SFUtility.log("Working directory: " + WORKING_DIR, LOG_FILE_PATH);						
				SFUtility.log("Log folder path: " + LOG_FOLDER, LOG_FILE_PATH); 		
				SFUtility.log("Build Number: " + BUILD_NUMBER, LOG_FILE_PATH); 		
				SFUtility.log("Execution Series: " + EXECUTION_SERIES, LOG_FILE_PATH); 	
				SFUtility.log("SFDC User Name: " + SF_USERNAME, LOG_FILE_PATH); 		
				SFUtility.log("Environment: " + ENVIRONMENT, LOG_FILE_PATH);

				AUTOMATION_TYPE = getAutomationType();
				System.out.println("AUTOMATION_TYPE: " + AUTOMATION_TYPE);

				if(AUTOMATION_TYPE.equals("") && EXECUTION_SERIES != null && EXECUTION_SERIES.equalsIgnoreCase("FULL") ) {
					System.out.println("No apex script file available, exiting...");
					System.exit(0);

				}


				SFUtility.log("**************************************************  START  ****************************************************************************", LOG_FILE_PATH);

				enterConn = SOAPUtil.login(WORKING_DIR, SF_USERNAME, SF_PASSWORD, LOG_FILE_PATH, ENVIRONMENT);				
				if(enterConn == null) {
					SFUtility.log("Please verify your URL and credentials and try again..", LOG_FILE_PATH); 
					System.exit(1);
				}
			}


			if(length == 9 && isCoverageTest) {

				//Code Coverage Validation
				testCoverage();


			} else if(length == 8) {

				if(AUTOMATION_TYPE.equals("build")) {
					runBuildAutomation();

				} else {
					System.out.println("Error: Could not identify automation type...");
					System.exit(1);
				}
			} else if (length == 11) {

				//Custom unit test automation using apex scripts

				ICRT_USERNAME  = args[8];
				ICRT_PASSWORD  = args[9];
				DRIVER_PATH  = args[10];

				SFUtility.log("ICRT User Name: " + ICRT_USERNAME, LOG_FILE_PATH);
				SFUtility.log("Driver Path: " + DRIVER_PATH, LOG_FILE_PATH);
				PartnerConnection partnerConn = getConnection(); 
				validateIICSLogin(ICRT_USERNAME, ICRT_PASSWORD, partnerConn, LOG_FILE_PATH);
				if(AUTOMATION_TYPE.equals("test")) {					
					runUnitTestAutomation();

				} else {
					System.out.println("Error: Could not identify automation type...");
					System.exit(1);
				}				


			} else {
				System.out.println("Please verify path of test case scripts folder, working folder, log folder, build number, execution series, salesforce username, password, environment(sandbox/production), ICRT username, password, driver path and run again..");						
				System.exit(1);

			}	
		} catch (Exception e) { 
			SFUtility.log("Exception: "+e.getMessage(), LOG_FILE_PATH);
			System.exit(1);	
		}
		SFUtility.printTime(LOG_FILE_PATH);
	}


	public static void runBuildAutomation() {
		/*if(isBuildAutomationWithRunOnceReRunFeature) {
			SFUtility.log("Executing build automation with Run-once and Re-run feature.....", LOG_FILE_PATH);
		} else {
			SFUtility.log("Executing build automation without Run-once and Re-run feature.....", LOG_FILE_PATH);
		}*/

		SFUtility.log("**************************************************  APEX EXECUTION  **************************************************************", LOG_FILE_PATH);
		PartnerConnection partnerConn = getConnection(); 		
		
		//List<String> scriptsExecutionSummary = ApexUtil.executeApexScripts(enterConn, partnerConn, APEX_FOLDER, LOG_FILE_PATH, EXECUTION_SERIES, EXECUTION_SERIES_RERUN_OF_RUNONCE_SCRIPTS);
		List<String> scriptsExecutionSummary = ApexUtil.executeApexScripts(enterConn, partnerConn, APEX_FOLDER, LOG_FILE_PATH, EXECUTION_SERIES, BUILD_NUMBER);

		/*enterConn = SOAPUtil.login(WORKING_DIR, SF_USERNAME, SF_PASSWORD, LOG_FILE_PATH, ENVIRONMENT);				
		if(enterConn == null) {
			SFUtility.log("Please verify your URL and credentials and try again..", LOG_FILE_PATH); 
			System.exit(1);
		}*/

		List<String> congaAttachmentSummary = SOAPUtil.loadCongaAttachments(enterConn, APEX_FOLDER + DOC_FOLDER, WORKING_DIR, LOG_FILE_PATH, EXECUTION_SERIES);	

		String executionStatusSummary = scriptsExecutionSummary.get(0);
		String executionFailureSummary = scriptsExecutionSummary.get(1);

		String congaAttachStatusSummary = congaAttachmentSummary.get(0);
		String congaAttachFailureSummary = congaAttachmentSummary.get(1);				

		if(executionFailureSummary.length() > 0 || congaAttachFailureSummary.length() > 0) {
			isFailure = true;
			SFUtility.log("Execution Completed with Error(s)!! ", LOG_FILE_PATH);					
		} else {
			SFUtility.log("Execution Completed Successfully!! ", LOG_FILE_PATH);
		}

		SFUtility.log("\n******************************************************** SCRIPT STATUS SUMMARY ********************************************************" + executionStatusSummary + congaAttachStatusSummary, LOG_FILE_PATH);	

		if(isFailure) {
			SFUtility.log("\n******************************************************** SCRIPT FAILURE SUMMARY ********************************************************" + executionFailureSummary + congaAttachFailureSummary, LOG_FILE_PATH);	
		}

		SFUtility.log("**************************************************  END  ****************************************************************************", LOG_FILE_PATH);

		try {
			partnerConn.logout();
			enterConn.logout();	
		} catch(Exception e) {
			//Session already expired.	 			
		}

		if(isFailure) {
			System.exit(1);					
		}	
	}	


	private static void runUnitTestAutomation() {
		UnitTestingUtils.execute(APEX_FOLDER, WORKING_DIR, LOG_FOLDER, BUILD_NUMBER, EXECUTION_SERIES, SF_USERNAME, SF_PASSWORD, ENVIRONMENT, LOG_FILE_PATH, ICRT_USERNAME, ICRT_PASSWORD);
	}


	public static String getWorkingDir() {
		return WORKING_DIR;
	}	

	public static String getSFUserName() {
		return SF_USERNAME;
	}

	public static String getSFPassword() {
		return SF_PASSWORD;
	}

	public static String getDriverPath() {
		return DRIVER_PATH;
	}


	private static String getAutomationType() {

		List<File> apexScripts = ApexUtil.getApexScripts(APEX_FOLDER, LOG_FILE_PATH);

		if(apexScripts != null && apexScripts.size()>0) {
			File apexFile = apexScripts.get(0);

			String fileName = apexFile.getName();
			System.out.println("fileName: "+fileName);
			if (fileName.contains("_UTF_CODE_") ||  fileName.contains("_UTF_UI_")) {

				return "test";

			} else {
				return "build";
			}
		}	
		return "";
	}
	public static void validateScriptsAvailability(String executionSeries,String logFilePath) {
		List<File> apexScripts = ApexUtil.getApexScripts(APEX_FOLDER, LOG_FILE_PATH);
		List<Integer> incrementalStepsList = SFUtility.getIncrementalSteps(executionSeries, logFilePath);
		for(Integer scriptNo : incrementalStepsList)
		{	
			List<String> allFileNames = new ArrayList<String>();
			for(File apexFile : apexScripts) {	

				String fileName = apexFile.getName();
				allFileNames.add(fileName);
			}
			Iterator<String> fileIterator = allFileNames.iterator();

			boolean isFileAvailable = false;
			while (fileIterator.hasNext()) {
				if(fileIterator.next().startsWith(scriptNo+"_")) {
					SFUtility.log("Unit test apex script of execution series " +scriptNo + " exist in folder.", LOG_FILE_PATH);
					isFileAvailable = true;
					break;
				}
			}

			if(!isFileAvailable) {
				SFUtility.log("Unit test case of execution series " +scriptNo+ " does not exist in folder...", LOG_FILE_PATH);
				SFUtility.log("Please check and try again...", LOG_FILE_PATH);
				System.exit(1);		
			}			
		}
	}

	public static String getLogFilePath() {
		return LOG_FILE_PATH;
	}

	public static EnterpriseConnection getEnterpriseConnction() {
		return enterConn;
	}	 

	public static void testCoverage() { 

		SFUtility.log("**************************************************  COVERAGE VALIDATION  **************************************************************", LOG_FILE_PATH);
		PartnerConnection partnerConn = getConnection();
		CodeCoverage.executeCodeCoverage(partnerConn, CODE_COVERAGE_SCRIPT_FOLDER, REPO_FOLDER_PATH, WORKING_DIR, LOG_FILE_PATH);

		SFUtility.log("**************************************************  END  ****************************************************************************", LOG_FILE_PATH);

		try {
			partnerConn.logout();
		} catch(Exception e) {
			//Session already expired.	 			
		}
	}

	public static PartnerConnection getConnection() {
		SFUtility.log("Going to login..", LOG_FILE_PATH); 
		PartnerConnection partnerConn = ApexUtil.login(WORKING_DIR, SF_USERNAME, SF_PASSWORD, LOG_FILE_PATH, ENVIRONMENT); 
		if(partnerConn == null) {
			SFUtility.log("Please verify your URL and credentials and try again..", LOG_FILE_PATH); 
			System.exit(1);
		}
		return partnerConn;


	}
	
	public static void validateFolderExistance(String folderPath) {
		File folder = new File(folderPath);
		if(folder != null && folder.exists() && !folder.isFile()) {
			return;		
		} else {
			System.out.println("Please verify folder path and execute again:: " + folderPath);
			System.exit(1);
		}
	}
	
	
	public static void validateIICSLogin(String iicsUser, String iicsPassword, PartnerConnection partnerConn, String logFilePath) {
			
		SFUtility.log("Going to validate IICS user credentials with dummy request !!", logFilePath);
		String apexScript  = "GHI_UTF_OrderProcessing.validateIICSUserCredentials('"+iicsUser+"','"+iicsPassword+"');";		

			String message = ApexUtil.executeAndReturnMessage(partnerConn, apexScript, logFilePath);
			SFUtility.log("message:: " + message, logFilePath);
			if(message != null && (message.contains("Unauthorized") || message.contains("not authorized"))) {
				SFUtility.log("IICS login failed, please verify IICS credentials and execute again:: " + iicsUser, logFilePath);
				CICDLogService.createEnvProvCICDLog("IICS login failed, please verify IICS credentials.", partnerConn, "Failed",logFilePath);
				System.exit(1);
			} else if(message != null && message.contains("incorrect signature")) {
				CICDLogService.createEnvProvCICDLog(message, partnerConn, "Failed", logFilePath);
				System.exit(1);
			} else {
				SFUtility.log("IICS user authorized successfully!!", logFilePath);
			}
	}
}