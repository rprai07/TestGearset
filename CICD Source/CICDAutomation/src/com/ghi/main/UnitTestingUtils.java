package com.ghi.main;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ghi.apex.ApexUtil;
import com.ghi.common.CSVFileReader;
import com.ghi.common.SFDataFactory;
import com.ghi.common.SFUtility;
import com.ghi.data.SOAPUtil;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.sobject.Order;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.sobject.Unit_Test_Result__c;
import com.sforce.soap.partner.PartnerConnection; 

/**
 * Main class to Start Unit Test Automation Execution.
 * @author manoj aggarwal
 *
 */
public final class UnitTestingUtils {

	public static boolean isFailure = false;
	public static String defaultPayorAccountId = "";
	public static String defaultHcoAccountId = "";
	public static String defaultHcpContactId = "";	
	public static String defaultMalePatientId = "";
	public static String defaultFemalePatientId = "";
	public static String defaultAgreementId = "";
	public static String defaultUserId = "";
	public static String defaultAddressId = "";
	public static String payorAccountId = "";
	public static String hcoAccountId = "";
	public static String hcpContactId = "";	
	public static String patientId = "";
	public static String orderId = ""; 
	public static String agreementId = "";
	public static String userId = "";
	public static String addressId = "";	
	public static final int WAIT_TIME_IN_MILLISEC = 20000;

	private UnitTestingUtils() {

	}


	/**
	 * Start of Unit Tests Execution 
	 * @param apexFolder
	 * @param workingDir
	 * @param logFolder
	 * @param buildNumber
	 * @param executionSeries
	 * @param sfUserName
	 * @param sfPassword
	 * @param environment 
	 * @param logFilePath
	 * @param icrtUserName
	 * @param icrtPassword
	 */
	static void execute(final String apexFolder, final String workingDir, final String logFolder, final String buildNumber, final String executionSeries, final String sfUserName, final String sfPassword, final String environment, final String logFilePath, final String icrtUserName, final String icrtPassword) {
		SFUtility.log("**************************************************  UNIT TESTING START  ****************************************************************************", logFilePath); 


		SFUtility.printTime(logFilePath);

		SFUtility.log("Scripts folder path: " + apexFolder, logFilePath);
		SFUtility.log("Working directory: " + workingDir, logFilePath);						
		SFUtility.log("Log folder path: " + logFolder, logFilePath); 		
		SFUtility.log("Build Number: " + buildNumber, logFilePath); 		
		SFUtility.log("Execution Series: " + executionSeries, logFilePath); 
		if(!executionSeries.equalsIgnoreCase("FULL")) {
			Main.validateScriptsAvailability(executionSeries, logFilePath); 
		}
		SFUtility.log("User Name: " + sfUserName, logFilePath); 		
		SFUtility.log("Environment: " + environment, logFilePath); 
		SFUtility.log("ICRT User Name: " + icrtUserName, logFilePath); 

		if(icrtUserName == null || icrtPassword == null || icrtUserName.trim().equals("") || icrtPassword.trim().equals("")) {
			SFUtility.log("Please verify ICRT username/password and try again..", logFilePath); 
			System.exit(1);

		}
		SFUtility.printTime(logFilePath);				
		SFUtility.log("Going to login..", logFilePath);			


		PartnerConnection partnerConn = ApexUtil.login(workingDir, sfUserName, sfPassword, logFilePath, environment);  
		if(partnerConn == null) {
			SFUtility.log("Please verify your URL and credentials and try again..", logFilePath); 
			System.exit(1);
		}

		SFUtility.log("Start..", logFilePath); 

		EnterpriseConnection enterConn = SOAPUtil.login(workingDir, sfUserName, sfPassword, logFilePath, environment);				
		if(enterConn == null) {
			SFUtility.log("Please verify your URL and credentials and try again..", logFilePath); 
			System.exit(1);
		}


		SFUtility.log("**************************************************  APEX EXECUTION  **************************************************************", logFilePath);
		List<String> scriptsExecutionSummary = executeApexScripts(partnerConn, enterConn, apexFolder, logFilePath, executionSeries, icrtUserName, icrtPassword, apexFolder);

		String executionStatusSummary = scriptsExecutionSummary.get(0);
		String executionFailureSummary = scriptsExecutionSummary.get(1);


		if(executionFailureSummary.length() > 0) {
			isFailure = true;
			SFUtility.log("Execution Completed with Error(s)!! ", logFilePath);					
		} else {
			SFUtility.log("Execution Completed Successfully!! ", logFilePath);
		}

		SFUtility.log("\n******************************************************** UNIT TEST CASE EXECUTION SUMMARY *******************************************" + executionStatusSummary, logFilePath);	

		if(isFailure) {
			SFUtility.log("\n******************************************************** UNIT TEST CASE FAILURE SUMMARY ***********************************************" + executionFailureSummary, logFilePath);	
		}

		SFUtility.log("**************************************************  END  ****************************************************************************", logFilePath);
		SFUtility.printTime(logFilePath);

		try {
			partnerConn.logout();

		} catch(Exception e) {
			//Session already expired.	 			
		}

		if(isFailure) {
			System.exit(1);					
		}
	}




	/**
	 * Method to check if the order is locked or not using 'ICRT_Lock_Processing__c' flag field value.
	 * @param enterConn
	 * @param orderId
	 * @return
	 */

	private static boolean isOrderLocked(EnterpriseConnection enterConn, String orderId, String logFilePath) {

		String soql = "Select ICRT_Lock_Processing__c from order where id = '"+orderId+"'"; 
		List<SObject> objList = SOAPUtil.executeQuery(enterConn, soql, logFilePath);
		SFUtility.log("list size......................."+objList.size(),  logFilePath);
		if(objList !=null && objList.size()==1) {
			SObject obj = objList.get(0);
			boolean isLocked = ((Order)obj).getICRT_Lock_Processing__c();
			SFUtility.log("isLocked:::::::::::::::::::::::::::::::::::::::" + isLocked,  logFilePath);
			return isLocked;			
		}

		return true;
	}




	/**
	 * Method to execute apex scripts using salesforce web services.
	 * @param partnerConn
	 * @param enterConn
	 * @param folderPath
	 * @param logFilePath
	 * @param sequence
	 * @param icrtUser
	 * @param icrtPassword
	 * @return
	 */
	private static List<String> executeApexScripts(PartnerConnection partnerConn, EnterpriseConnection enterConn, String folderPath, String logFilePath, String sequence, String icrtUser, String icrtPassword, String apexFolder) {
		String executionStatusSummary = "";
		String executionFailureSummary = "";
		List<String> executionSummary = new ArrayList<String>();
		System.out.println("apex folder " +apexFolder);
		if(sequence != null ) {
			sequence = sequence.trim();

			ArrayList defaultDataList = SFDataFactory.createDefaultTestData(partnerConn, enterConn, logFilePath, apexFolder);	
			Map<String, String> defaultDataMap =(Map<String, String>)defaultDataList.get(0);

			/* commented by priyanka
			String defaultDataCSV =(String)defaultDataList.get(1);
			String[] values = defaultDataCSV.split(",");
			ArrayList defaultCSVList = new ArrayList(Arrays.asList(values));

			String defaultDataIdList = (String)defaultDataList.get(2);
			String[] valuesId = defaultDataIdList.split(",");
			ArrayList defaultCSVIdList = new ArrayList(Arrays.asList(valuesId));*/
			//int seq1 = Integer.parseInt(sequence);

			//	createDefaultUnitTestResults(partnerConn,logFilePath,0,defaultCSVList,defaultCSVIdList);
			//		else {
			for(String key: defaultDataMap.keySet()) {

				if(key.equals("addressId")) {
					defaultAddressId = defaultDataMap.get(key);
				} else if(key.equals("payorAccountId")) {
					defaultPayorAccountId = defaultDataMap.get(key);
				} else if(key.equals("hcoAccountId")) {
					defaultHcoAccountId = defaultDataMap.get(key);
				} else if(key.equals("hcpContactId")) {
					defaultHcpContactId = defaultDataMap.get(key);
				} else if(key.equals("malePatientId")) {
					defaultMalePatientId = defaultDataMap.get(key); 
				} else if(key.equals("femalePatientId")) {
					defaultFemalePatientId = defaultDataMap.get(key); 
				} else if(key.equals("agreementId")) {
					defaultAgreementId = defaultDataMap.get(key);
				} else if(key.equals("userId")) {
					defaultUserId = defaultDataMap.get(key);
				}
			}


			//Full run to execute all unit test scripts
			if(sequence.equalsIgnoreCase("FULL")) {

				//Full Build
				List<File> apexScripts = ApexUtil.getApexScripts(folderPath, logFilePath);
				for(File apexFile : apexScripts) {

					String fileName = apexFile.getName();

					//Removing extension from file name
					fileName = fileName.replaceFirst("[.][^.]+$", "");

					String seqStr = fileName.substring(0, fileName.indexOf("_"));
					int seq = Integer.parseInt(seqStr); 
					String apexScript = ApexUtil.getApexScriptFromFile(apexFile, logFilePath);
					boolean result = executeTestCase(partnerConn, enterConn, apexScript,fileName, logFilePath, seq, icrtUser, icrtPassword, apexFolder);

					if(result == false) {
						executionStatusSummary = executionStatusSummary  + "\n" + apexFile.getName() + ": Failed";						
						executionFailureSummary = executionFailureSummary + "\n" + apexFile.getName() + ": Failed";
						SFUtility.log(apexFile.getName() + ": Failed", logFilePath); 
						//updateUnitTestResults(partnerConn, demoSet, "Failed", logFilePath, seq,defaultDataCSV);

					} else {
						executionStatusSummary = executionStatusSummary  + "\n" + apexFile.getName() + ": Passed";
						SFUtility.log(apexFile.getName() + ": Passed", logFilePath);
						//updateUnitTestResults(partnerConn, demoSet, "Passed", logFilePath, seq,defaultDataCSV);

					}
				}
			} else {

				SFUtility.log("Selective unit test run...", logFilePath);
				//Incremental Build
				List<File> apexScripts = ApexUtil.getApexScripts(folderPath, logFilePath);
				List<Integer> incrementalStepsList = SFUtility.getIncrementalSteps(sequence, logFilePath);

				for(File apexFile : apexScripts) {
					String fileName = apexFile.getName();

					//Removing extension from file name
					fileName = fileName.replaceFirst("[.][^.]+$", "");

					String seqStr = fileName.substring(0, fileName.indexOf("_"));
					int seq = Integer.parseInt(seqStr); 
					if(incrementalStepsList.contains(seq)) {
						String apexScript = ApexUtil.getApexScriptFromFile(apexFile, logFilePath);
						//Map<String, String> stepExecutionStatusMap = ApexUtil.execute(partnerConn, apexScript, logFilePath);

						boolean result = executeTestCase(partnerConn, enterConn, apexScript,fileName, logFilePath, seq,icrtUser, icrtPassword, apexFolder);
						if(result == false) {
							executionStatusSummary = executionStatusSummary  + "\n" + apexFile.getName() + ": Failed";						
							executionFailureSummary = executionFailureSummary + "\n" + apexFile.getName() + ": Failed";
							SFUtility.log(apexFile.getName() + ": Failed", logFilePath); 
							//updateUnitTestResults(partnerConn, demoSet, "Failed", logFilePath, seq,defaultDataCSV);

						} else {
							executionStatusSummary = executionStatusSummary  + "\n" + apexFile.getName() + ": Passed";
							SFUtility.log(apexFile.getName() + ": Passed", logFilePath);
							//updateUnitTestResults(partnerConn, demoSet, "Passed", logFilePath, seq,defaultDataCSV);

						}

					} else {
						//SFUtility.log("Not part of Incremental build.."); 
					}
				}			
			}
		}
		//	}
		executionSummary.add(executionStatusSummary);
		executionSummary.add(executionFailureSummary);		
		return executionSummary;
	}



	/**
	 * Method to get orderable class from apex script
	 * @param apexScript
	 * @return
	 */
	public static String getOrderableClassFromScript(String apexScript) {
		String orderableClass = "";

		if(apexScript!=null && apexScript.contains("IBCOrderProcessing")) {		
			orderableClass = "GHI_UTF_IBC";

		} else if(apexScript!=null && apexScript.contains("MMROrderProcessing")) {		
			orderableClass = "GHI_UTF_MMR";

		} else if(apexScript!=null && apexScript.contains("ColonOrderProcessing")) {		
			orderableClass = "GHI_UTF_Colon";

		} else if(apexScript!=null && apexScript.contains("DCISOrderProcessing")) {		
			orderableClass = "GHI_UTF_DCIS";

		} else if(apexScript!=null && apexScript.contains("ProstateOrderProcessing")) {		
			orderableClass = "GHI_UTF_Prostate";

		} else if(apexScript!=null && apexScript.contains("ProstateARV7OrderProcessing")) {		
			orderableClass = "GHI_UTF_ProstateARV7";

		} else if(apexScript!=null && apexScript.contains("MMRAndColonOrderProcessing")) {		
			orderableClass = "GHI_UTF_MMRAndColon";

		} else if(apexScript!=null && apexScript.contains("MMRReflexToColonOrderProcessing")) {		
			orderableClass = "GHI_UTF_MMRReflexToColon";

		}

		return orderableClass;
	}



	/**
	 * Method to get orderable name from orderable class 
	 * @param orderableClass
	 * @return
	 */
	public static String getOrderableFromOrderableClass(String orderableClass) {
		String orderable = "";

		if(orderableClass!=null && orderableClass.contains("IBC")) {		
			orderable = "IBC";

		} else if(orderableClass!=null && orderableClass.contains("MMR")) {		
			orderable = "MMR";

		} else if(orderableClass!=null && orderableClass.contains("Colon")) {		
			orderable = "Colon";

		} else if(orderableClass!=null && orderableClass.contains("DCIS")) {		
			orderable = "DCIS";

		} else if(orderableClass!=null && orderableClass.contains("ProstateARV7")) {		
			orderable = "Prostate-AR-V7";

		} else if(orderableClass!=null && orderableClass.contains("Prostate")) {		
			orderable = "Prostate";

		}  else if(orderableClass!=null && orderableClass.contains("MMRAndColon")) {		
			orderable = "MMR and Colon";

		} else if(orderableClass!=null && orderableClass.contains("MMRReflexToColon")) {		
			orderable = "MMR Reflex to Colon";
		}

		return orderable;
	}




	/**
	 * Method to execute unit test case script.
	 * @param partnerConn
	 * @param enterConn
	 * @param apexScript
	 * @param logFilePath
	 * @param sequence
	 * @param icrtUser
	 * @param icrtPassword
	 * @return
	 */
	private static boolean executeTestCase(PartnerConnection partnerConn, EnterpriseConnection enterConn, String apexScript, String apexFileName, String logFilePath, int sequence, String icrtUser, String icrtPassword, String apexFolder) {

		return TestCase.execute(partnerConn, enterConn, apexScript, apexFileName, logFilePath, sequence, icrtUser, icrtPassword, apexFolder);
	}



	public static void  updateUnitTestResults(PartnerConnection partnerConn, Set<String> orderIdsList, String result, String logFilePath) {

		String apexScript = "";
		for (String orderId : orderIdsList) {
			apexScript = "List<Unit_Test_Result__c> results = [select id, Result__c from Unit_Test_Result__c where Record_Id__c  = : '"+orderId+"']; system.debug('results: ' +results.size());for(Unit_Test_Result__c result : results){ result.Result__c = '"+result+"';} update results;";
			ApexUtil.execute(partnerConn, apexScript.trim(), logFilePath);
		}
		ApexUtil.execute(partnerConn, apexScript.trim(), logFilePath);
	}


	/*private static void createDefaultUnitTestResults(PartnerConnection partnerConn,  String logFilePath, int sequence ,  ArrayList<String> defaultCSVList ,ArrayList<String> defaultCSVIdList) {
		//System.out.println("availableFinalCsvList ===================" +availableFinalCsvList);
		for (String defaultCSV : defaultCSVList)
		{
		for (String defaultCSVId : defaultCSVIdList){
		String apexScript = "Unit_Test_Result__c insertDefaultRecord =  new Unit_Test_Result__c(); insertDefaultRecord.Apex_Script_File_Name__c = "+sequence+" ; insertDefaultRecord.Data_CSV_File_Name__c ='"+defaultCSV+"';insertDefaultRecord.Record_Id__c = '"+defaultCSVId+"';insert insertDefaultRecord;";
		ApexUtil.execute(partnerConn, apexScript.trim(), logFilePath);
	  }
	}
}*/




	public static String executeOrderProcessingUpdateOrder(PartnerConnection partnerConn,EnterpriseConnection enterConn, String apexScript, String logFilePath, int sequence, String apexFolder,String orderableClass) throws InterruptedException {
		updateOrder(apexFolder, partnerConn, enterConn, orderId, sequence, logFilePath);
		Thread.sleep(WAIT_TIME_IN_MILLISEC + 10000);					
		apexScript = apexScript.replace(orderableClass+"OrderProcessing.updateOrder($orderId);", "");
		return apexScript;
	}




	public static String executeOrderProcessingUpdateOLI(PartnerConnection partnerConn, EnterpriseConnection enterConn,
			String apexScript, String logFilePath, int sequence, String apexFolder, String orderableClass)
					throws InterruptedException {
		String oliId = SFDataFactory.getOLIIdFromOrder( enterConn,  orderId,  logFilePath);
		updateOLI(apexFolder, orderableClass, partnerConn,  enterConn,  oliId,  sequence,  logFilePath) ;
		Thread.sleep(WAIT_TIME_IN_MILLISEC + 10000);					
		apexScript = apexScript.replace(orderableClass+"OrderProcessing.updateOLI($orderId);", "");
		return apexScript;
	}




	public static String executeOrderProcessingCreateUser(String apexScript, String orderableClass)
			throws InterruptedException {
		Thread.sleep(WAIT_TIME_IN_MILLISEC + 10000);					
		apexScript = apexScript.replace(orderableClass+"OrderProcessing.createUser();", "");
		return apexScript;
	}




	public static String executeOrderProcessingCreateAgreement(String apexScript, String orderableClass)
			throws InterruptedException {
		Thread.sleep(WAIT_TIME_IN_MILLISEC + 10000);					
		apexScript = apexScript.replace(orderableClass+"OrderProcessing.createAgreement();", "");
		return apexScript;
	}




	public static String executeOrderProcessingCreateDistributionEvent(PartnerConnection partnerConn,
			EnterpriseConnection enterConn, String apexScript, String logFilePath, String icrtUser, String icrtPassword,
			String orderableClass) throws InterruptedException {
		String createDistributionEvent = UnitTestFixtures.getCreateDistributionEventFixture(orderableClass , orderId, icrtUser, icrtPassword);
		ApexUtil.execute(partnerConn, createDistributionEvent, logFilePath);					
		waitTillUnlocked(enterConn, orderId, logFilePath);
		//Order not locked while getting closed, so need to wait
		Thread.sleep(WAIT_TIME_IN_MILLISEC);
		apexScript = apexScript.replace(orderableClass + "OrderProcessing.createDistributionEvent($orderId);", "");
		return apexScript;
	}




	public static String executeOrderProcessingUpdateOLIToDVC(PartnerConnection partnerConn,
			EnterpriseConnection enterConn, String apexScript, String logFilePath, String orderableClass)
					throws InterruptedException {
		String updateOLIToDVC = UnitTestFixtures.getUpdateOLIToDVCFixture(orderableClass , orderId);
		ApexUtil.execute(partnerConn, updateOLIToDVC, logFilePath);						
		waitTillUnlocked(enterConn, orderId, logFilePath);
		Thread.sleep(WAIT_TIME_IN_MILLISEC + 15000);
		Thread.sleep(WAIT_TIME_IN_MILLISEC + 15000);
		apexScript = apexScript.replace(orderableClass + "OrderProcessing.updateOLIToDVC($orderId);", "");
		Thread.sleep(WAIT_TIME_IN_MILLISEC + 15000);
		return apexScript;

	}




	public static String executeOrderProcessingCreateResult(PartnerConnection partnerConn,
			EnterpriseConnection enterConn, String apexScript, String logFilePath, String icrtUser, String icrtPassword,
			String orderableClass) throws InterruptedException {
		String createResult = UnitTestFixtures.getCreateResultFixture(orderableClass , orderId, icrtUser, icrtPassword);
		ApexUtil.execute(partnerConn, createResult, logFilePath);
		//Thread.sleep(30000);	
		waitTillUnlocked(enterConn, orderId, logFilePath);
		//Order not locked while result is getting created
		Thread.sleep(WAIT_TIME_IN_MILLISEC);
		apexScript = apexScript.replace(orderableClass + "OrderProcessing.createResult($orderId);", "");
		return apexScript;
	}




	public static String executeOrderProcessingCreateSpecimen(PartnerConnection partnerConn,
			EnterpriseConnection enterConn, String apexScript, String logFilePath, String icrtUser, String icrtPassword,
			String orderableClass) throws InterruptedException {
		String createSpecimen = UnitTestFixtures.getCreateSpecimenFixture(orderableClass , orderId, icrtUser, icrtPassword);
		ApexUtil.execute(partnerConn, createSpecimen, logFilePath);
		//Thread.sleep(30000);	
		waitTillUnlocked(enterConn, orderId, logFilePath);
		//Order not locked while specimen is getting created
		Thread.sleep(WAIT_TIME_IN_MILLISEC + 25000);
		apexScript = apexScript.replace(orderableClass + "OrderProcessing.createSpecimen($orderId);", "");
		return apexScript;
	}








	public static String executeOrderProcessingCreatePackage(PartnerConnection partnerConn,
			EnterpriseConnection enterConn, String apexScript, String logFilePath, String orderableClass)
					throws InterruptedException {
		String createPackageFixture = UnitTestFixtures.getCreatePackageFixture(orderableClass , orderId);
		ApexUtil.execute(partnerConn, createPackageFixture, logFilePath);					
		waitTillUnlocked(enterConn, orderId, logFilePath);

		//Order not locked for specimen arrival case, so need to add static wait time
		Thread.sleep(WAIT_TIME_IN_MILLISEC);
		apexScript = apexScript.replace(orderableClass + "OrderProcessing.createPackage($orderId);", "");
		return apexScript;
	}







	public static String executeOrderProcessingProcessOrder(PartnerConnection partnerConn,
			EnterpriseConnection enterConn, String apexScript, String logFilePath, String orderableClass) {
		String processOrderFixture = UnitTestFixtures.getProcessOrderFixture(orderableClass , orderId);
		ApexUtil.execute(partnerConn, processOrderFixture, logFilePath);					
		waitTillUnlocked(enterConn, orderId, logFilePath);
		apexScript = apexScript.replace(orderableClass + "OrderProcessing.processOrder($orderId);", "");
		return apexScript;
	}




	public static String executeOrderProcessingCreateOrderRole(PartnerConnection partnerConn, EnterpriseConnection enterConn,
			String apexScript, String logFilePath, String orderableClass, String statement) {
		String orderRoleName = getOrderRoleName(statement);
		String createOrderRoleFixture = UnitTestFixtures.getCreateOrderRoleFixture(orderableClass, orderRoleName, orderId, hcoAccountId, hcpContactId);

		//SFUtility.log(":::::::::::::::::::::::::::::::::::::::: "+createOrderRoleFixture, logFilePath);
		ApexUtil.execute(partnerConn, createOrderRoleFixture, logFilePath);					
		waitTillUnlocked(enterConn, orderId, logFilePath);
		apexScript = apexScript.replace(statement, "////"+statement);
		return apexScript;
	}




	public static String executeOrderProcessingCreate(PartnerConnection partnerConn, EnterpriseConnection enterConn,
			String apexScript, String logFilePath, String apexFolder, String orderableClass)
					throws InterruptedException {
		Thread.sleep(WAIT_TIME_IN_MILLISEC + 10000);					
		//orderId = getId(enterConn, timeStr);
		isOrderLocked(enterConn, orderId, logFilePath);
		apexScript = apexScript.replace(orderableClass+"OrderProcessing.createOrder();", "");

		String oliId = SFDataFactory.getOLIIdFromOrder( enterConn,  orderId,  logFilePath);

		updateDefaultOLI(apexFolder, orderableClass, partnerConn, enterConn, oliId, logFilePath);
		return apexScript;
	}




	public static String executeApexWithoutFixtures(PartnerConnection partnerConn, String apexScript,String logFilePath, String apexWithoutFixtures) {
		if(apexWithoutFixtures!=null && !apexWithoutFixtures.trim().equals("")) {
			System.out.println("apexWithoutFixtures"+apexWithoutFixtures);
			String apexWithoutFixturesNew = populateGlobalVariableValues(apexWithoutFixtures);
			ApexUtil.execute(partnerConn, apexWithoutFixturesNew, logFilePath);							
			apexScript = apexScript.replace(apexWithoutFixturesNew, "/* "+ apexWithoutFixturesNew+ "*/");						
		}
		return apexScript;
	}





	/**
	 * Method to wait till order is unlocked. It will check after every 5 seconds (max. for 2 minutes).
	 * @param enterConn
	 * @param orderId
	 * @param logFilePath
	 * @return
	 */
	public static boolean waitTillUnlocked( EnterpriseConnection enterConn, String orderId, String logFilePath) {

		final int tryMax = 24;

		//wait interval in millisec 
		final int waitInterval = 5000; 

		try {

			for(int i=0; i< tryMax; i++ ) {

				if(isOrderLocked(enterConn, orderId, logFilePath)) {
					Thread.sleep(waitInterval);
				} else {

					//Order is unlocked now 
					SFUtility.log("Order is unlocked now. ", logFilePath);
					return true;
				}
			}

		} catch (InterruptedException ex) {
			System.out.println("Exception: "+ex.getMessage());
			SFUtility.log("Exception: "+ex.getMessage(), logFilePath);
		}	

		//Order is still locked	
		SFUtility.log("Order is still locked. ", logFilePath);	
		return false;
	}



	/**
	 * Method to update default OLI by reading data from default CSV file.
	 * @param orderableClass
	 * @param partnerConn
	 * @param enterConn
	 * @param oliId
	 * @param logFilePath
	 */
	private static void updateDefaultOLI(String apexFolder, String orderableClass, PartnerConnection partnerConn, EnterpriseConnection enterConn, String oliId, String logFilePath) {

		String oliIds[] = oliId.split(",");
		List<String> sheetsAlreadyProcessed = new ArrayList<String>();
		for(String id : oliIds) {

			String updateOLIFilePath1 = apexFolder + "\\" + "Default_OrderItem_"+orderableClass+"_Update.csv";	
			if(!sheetsAlreadyProcessed.contains(updateOLIFilePath1)) {
				String updateOLIScript1 = CSVFileReader.getScriptForUpdate(updateOLIFilePath1, "OrderItem", id, logFilePath, enterConn);
				SFUtility.log("updateOLIScript1:----------------------------------------------------------- "+updateOLIScript1, logFilePath);
				if(updateOLIScript1 !=null && !updateOLIScript1.trim().equals("")) {
					ApexUtil.execute(partnerConn, updateOLIScript1,  logFilePath);	
					sheetsAlreadyProcessed.add(updateOLIFilePath1);

				}				
			}

		}
	}




	/**
	 * Method to update Order record by reading data from given CSV file.
	 * @param partnerConn
	 * @param enterConn
	 * @param orderId
	 * @param sequence
	 * @param logFilePath
	 */
	private static void updateOrder(String apexFolder, PartnerConnection partnerConn, EnterpriseConnection enterConn, String orderId, int sequence, String logFilePath) {

		String updateOrderFilePath = apexFolder + "\\" + sequence+"_Order_Update.csv";	

		String updateOLIScript = CSVFileReader.getScriptForUpdate(updateOrderFilePath, "Order", orderId, logFilePath, enterConn);
		if(updateOLIScript !=null && !updateOLIScript.trim().equals("")) {
			ApexUtil.execute(partnerConn, updateOLIScript,  logFilePath);	

		}				
	}



	/**
	 * Method to update OLI by reading data from given CSV file.
	 * @param orderableClass
	 * @param partnerConn
	 * @param enterConn
	 * @param oliId
	 * @param sequence
	 * @param logFilePath
	 */
	private static void updateOLI(String apexFolder, String orderableClass, PartnerConnection partnerConn, EnterpriseConnection enterConn, String oliId, int sequence, String logFilePath) {

		String oliIds[] = oliId.split(",");
		List<String> sheetsAlreadyProcessed = new ArrayList<String>();
		for(String id : oliIds) {

			String updateOLIFilePath = apexFolder + "\\" + sequence+"_OrderItem_Update.csv";	
			if(!sheetsAlreadyProcessed.contains(updateOLIFilePath)) {

				String updateOLIScript = CSVFileReader.getScriptForUpdate(updateOLIFilePath, "OrderItem", id, logFilePath, enterConn);

				if(updateOLIScript !=null && !updateOLIScript.trim().equals("")) {
					ApexUtil.execute(partnerConn, updateOLIScript,  logFilePath);	
					sheetsAlreadyProcessed.add(updateOLIFilePath);

				}				
			}

			String updateOLIFilePath1 = apexFolder + "\\" + sequence+"_OrderItem_"+orderableClass+"_Update.csv";	

			if(!sheetsAlreadyProcessed.contains(updateOLIFilePath1)) {

				String updateOLIScript1 = CSVFileReader.getScriptForUpdate(updateOLIFilePath1, "OrderItem", id, logFilePath, enterConn);

				if(updateOLIScript1 !=null && !updateOLIScript1.trim().equals("")) {
					ApexUtil.execute(partnerConn, updateOLIScript1,  logFilePath);	
					sheetsAlreadyProcessed.add(updateOLIFilePath1);

				}				
			}
		}
	}



	/**
	 * Method to get order role name from given apex statement.
	 * @param statement
	 * @return
	 */
	private static String getOrderRoleName(String statement) {
		int firstIndex = statement.indexOf("'")+1;

		int lastIndex = statement.lastIndexOf("'");

		String orderRoleName = statement.substring(firstIndex, lastIndex);

		System.out.println("Order role name: "+ orderRoleName);
		return orderRoleName;
	}




	/**
	 * Method to create test case specific data by reading data from given CSV files.
	 * @param orderableClass
	 * @param partnerConn
	 * @param enterConn
	 * @param apexScript
	 * @param logFilePath
	 * @param sequence
	 * @return
	 */
	public static List<Object> createTestCaseSpecificData(String apexFolder, String orderableClass, PartnerConnection partnerConn, EnterpriseConnection enterConn, String apexScript,String apexFileName, String logFilePath, int sequence) {

		return TestCaseData.create(apexFolder, orderableClass, partnerConn, enterConn, apexScript, apexFileName, logFilePath, sequence);
	}



	/**
	 * Method to populate global variable values.
	 * @param apexScript
	 * @return
	 */
	public static String populateGlobalVariableValues(String apexScript) {
		System.out.println("apexScript before: "+apexScript);
		if(apexScript != null && !apexScript.trim().equals("")) {
			apexScript = apexScript.trim();
			apexScript = apexScript.replace(":$orderId", "'"+orderId+"'");
			apexScript = apexScript.replace("$orderId", "'"+orderId+"'");
			apexScript = apexScript.replace(":$agreementId", "'"+agreementId+"'");
			apexScript = apexScript.replace("$agreementId", "'"+agreementId+"'");
			apexScript = apexScript.replace(":$userId", "'"+userId+"'");
			apexScript = apexScript.replace("$userId", "'"+userId+"'");
		}

		System.out.println("apexScript after: "+apexScript);
		return apexScript;
	}


	public static String getPatientId() {
		return patientId;
	}

	public static String getOrderId() {
		return orderId;
	}


	public static String checkRecordExist(EnterpriseConnection enterConn, String logFilePath, String csvName, String objName) {

		String soql = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+csvName+"' and Record_Id__c != null LIMIT 1";
		List<SObject> objList = SOAPUtil.executeQuery(enterConn, soql, logFilePath);		
		SFUtility.log("list size......................."+objList.size(),  logFilePath);
		if(objList !=null) {
			Unit_Test_Result__c utr = (Unit_Test_Result__c) objList.get(0);
			String executionid= utr.getTime_Stamp__c();
			String soql1 ="select Name from "+objName+" where id = '"+utr.getRecord_Id__c()+"' ";
			List<SObject> objList1 = SOAPUtil.executeQuery(enterConn, soql1, logFilePath);		
			SFUtility.log("list size......................."+objList1.size(),  logFilePath);
			if(objList1.size()!=0) {					
				return executionid;
			}
			else {
				SFUtility.log("Record Doesn't exist...............", logFilePath);	
				return "Record doesn't exist";
			}
		}
		else {
			SFUtility.log("Record doesn't exist...............", logFilePath);
		}

		return null;
	}
}