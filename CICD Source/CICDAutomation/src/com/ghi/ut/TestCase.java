package com.ghi.ut;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ghi.apex.ApexUtil;
import com.ghi.common.SFDataFactory;
import com.ghi.common.SFUtility;
import com.ghi.test.UITesting;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.sobject.Calendar;
import com.sforce.soap.partner.PartnerConnection;

public class TestCase {
	public static long tcStartTime;
	public static long tcEndTime;		
	public static long tcExecTime;	
	//public static int stmtExecTime;
	public static String draftOrderURL = "https://use6-cai.dm-us.informaticacloud.com/active-bpel/rt/INT-MP-External-SYNC-CreateDraftOrder";
	public static String updateDraftOrderURL = "https://use6-cai.dm-us.informaticacloud.com/active-bpel/rt/INT-MP-EU-Portal-OrderDataMessageV2";


	
	public static boolean execute(PartnerConnection partnerConn, EnterpriseConnection enterConn, String apexScript, String apexFileName, String logFilePath, int sequence, String icrtUser, String icrtPassword, String apexFolder) {

		try {
			Map<String, String> stepExecutionStatusMap = new HashMap<String, String>();
			boolean isSpecificOrderDetailsCSVAvailable = false;
			//boolean isPassed = true; 
			//Set<String> unitTestResultId =  new HashSet<String>(); 
 
			String orderableClass = UnitTestingUtils.getOrderableClassFromScript(apexScript);

			//isSpecificOrderDetailsCSVAvailable = createTestCaseSpecificData(apexFolder, orderableClass, partnerConn, enterConn, apexScript, logFilePath, sequence);
			//It will create all custom/specific data including order record as well(files using given sequence number).
			List<Object> testDataAndSpecificOrderDetailsFlag = UnitTestingUtils.createTestCaseSpecificData(apexFolder, orderableClass, partnerConn, enterConn, apexScript,apexFileName, logFilePath, sequence, draftOrderURL, icrtUser, icrtPassword);
			
			Set<String>  orderIdsList = new HashSet<String>();
			String availableFinalCsvList = (String)testDataAndSpecificOrderDetailsFlag.get(2);
			
			try { 

				isSpecificOrderDetailsCSVAvailable = (boolean)testDataAndSpecificOrderDetailsFlag.get(1);
				

				String[] statements =  apexScript.split("\\n");
				String apexWithoutFixtures = "";

				if(UnitTestingUtils.patientId == null || UnitTestingUtils.patientId.trim().equals("")) {

					if(orderableClass.contains("Prostate")) {
						//SFDataFactory.updatePatient(partnerConn, patientId, "Male",logFilePath);

						UnitTestingUtils.patientId = UnitTestingUtils.defaultMalePatientId;
					} else {
						UnitTestingUtils.patientId = UnitTestingUtils.defaultFemalePatientId;
					}
				}

				if(UnitTestingUtils.addressId == null || UnitTestingUtils.addressId.trim().equals("") ) {
					UnitTestingUtils.addressId = UnitTestingUtils.defaultAddressId;
				}


				//Create contacts, accounts are other required data here and store Ids					

				
				//checking default OLI exist or not.
				String updateOLIFilePath1 = apexFolder + "\\" + "Default_OrderItem_GHI_UTF_Colon_Update.csv";
				String updateOLIFilePath2 = apexFolder + "\\" + "Default_OrderItem_GHI_UTF_DCIS_Update.csv";
				String updateOLIFilePath3 = apexFolder + "\\" + "Default_OrderItem_GHI_UTF_IBC_Update.csv";
				String updateOLIFilePath4 = apexFolder + "\\" + "Default_OrderItem_GHI_UTF_MMR_Update.csv";
				String updateOLIFilePath5 = apexFolder + "\\" + "Default_OrderItem_GHI_UTF_Prostate_Update.csv";
				String updateOLIFilePath6 = apexFolder + "\\" + "Default_OrderItem_GHI_UTF_ProstateARV7_Update.csv";
				String updateOLIFilePath7 = apexFolder + "\\" + "Default_OrderItem_GHI_UTF_PCDxIHC_Update.csv";
				String updateOLIFilePath8 = apexFolder + "\\" + "Default_OrderItem_GHI_UTF_PCDxMolecular_Update.csv";
				
			    Path path = Paths.get(updateOLIFilePath1); 
			    Path path2 = Paths.get(updateOLIFilePath2); 
			    Path path3 = Paths.get(updateOLIFilePath3); 
			    Path path4 = Paths.get(updateOLIFilePath4); 
			    Path path5 = Paths.get(updateOLIFilePath5); 
			    Path path6 = Paths.get(updateOLIFilePath6);
			    Path path7 = Paths.get(updateOLIFilePath7);
			    Path path8 = Paths.get(updateOLIFilePath8);
			    
			    
			    List<String> missingcsv = new ArrayList<String>();
			    if (!Files.exists(path)) 
			   {
				   String csvname= "Default_OrderItem_GHI_UTF_Colon_Update.csv";
				   missingcsv.add(csvname);
					
			   }
			    if(!Files.exists(path2)){
			    	String csvname= "Default_OrderItem_GHI_UTF_DCIS_Update.csv";
			    	missingcsv.add(csvname);
					
			    	}
			    if(!Files.exists(path3)){
			    	String csvname= "Default_OrderItem_GHI_UTF_IBC_Update.csv";
			    	missingcsv.add(csvname);
					
			    	}
			    if(!Files.exists(path4)) {
			    	String csvname= "Default_OrderItem_GHI_UTF_MMR_Update.csv";
			    	missingcsv.add(csvname);
					
			    	}
			    if(!Files.exists(path5)) {
			    	String csvname= "Default_OrderItem_GHI_UTF_Prostate_Update.csv";
			    	missingcsv.add(csvname);
			    	}
			    if(!Files.exists(path6)) {
			    	String csvname= "Default_OrderItem_GHI_UTF_ProstateARV7_Update.csv";
			    	missingcsv.add(csvname);
				  }
			    if(!Files.exists(path7)) {
			    	String csvname= "Default_OrderItem_GHI_UTF_PCDxIHC_Update.csv";
			    	missingcsv.add(csvname);
				  }
			    if(!Files.exists(path8)) {
			    	String csvname= "Default_OrderItem_GHI_UTF_PCDxMolecular_Update.csv";
			    	missingcsv.add(csvname);
				  }
			    
			    
			    
			    
			    if(missingcsv.size()>0)	{
			    SFUtility.log("Default CSV Missing :  "+ missingcsv, logFilePath);
			    System.exit(1);
			    }

			    tcStartTime = getTime(logFilePath);
			    //SFUtility.log("Unit test case (apex script) start time: " + startTime, logFilePath);
				for(String statement : statements) {

					statement = statement.trim();	

					if(UITesting.isAnnotation(statement)) {
						SFUtility.log("statement: " + "annotation found !!",  logFilePath);
						
						
						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);		
						apexWithoutFixtures = "";
						UITesting.executeMethodWithGivenAnnotation(statement);
						
						apexScript = apexScript.replace(statement, "");

					} else if(statement != null && statement.equals(orderableClass + "OrderProcessing.createOrder();")) {

						if(!isSpecificOrderDetailsCSVAvailable) {

							String orderable = UnitTestingUtils.getOrderableFromOrderableClass(orderableClass);
							System.out.println("orderableClass::::::::::::::::::::::::::::: "+orderableClass);
							System.out.println("orderable::::::::::::::::::::::::::::::::::::: "+orderable);
							

							UnitTestingUtils.orderId = SFDataFactory.createDefaultOrderRecord(orderable, partnerConn, enterConn, logFilePath, apexFolder, UnitTestingUtils.payorAccountId, UnitTestingUtils.hcoAccountId, UnitTestingUtils.hcpContactId, UnitTestingUtils.patientId);
							orderIdsList.add(UnitTestingUtils.orderId);
						}
						
						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);		
						apexWithoutFixtures = "";	
						apexScript = UnitTestingUtils.executeOrderProcessingCreate(partnerConn, enterConn, apexScript, logFilePath, apexFolder, orderableClass, sequence);

					} else if(statement != null && orderableClass.toUpperCase().contains("PCDX") && statement.equals(orderableClass + "OrderProcessing.createDraftOrder();")) {

						if(!isSpecificOrderDetailsCSVAvailable) {

							String orderable = UnitTestingUtils.getOrderableFromOrderableClass(orderableClass);
							System.out.println("orderable::::::::::::::::::::::::::::::::::::: "+orderable);
							

							//UnitTestingUtils.orderId = SFDataFactory.createDefaultPCDxDraftOrderRecord(orderable, partnerConn, enterConn, logFilePath, apexFolder, UnitTestingUtils.payorAccountId, UnitTestingUtils.hcoAccountId, UnitTestingUtils.hcpContactId, UnitTestingUtils.patientId);
							UnitTestingUtils.orderId = SFDataFactory.createDefaultPCDxDraftOrderRecord(partnerConn, logFilePath, apexFolder, draftOrderURL, icrtUser, icrtPassword, enterConn);
							
							orderIdsList.add(UnitTestingUtils.orderId);
						}
						
						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						
						apexWithoutFixtures = "";	
						apexScript = UnitTestingUtils.executePCDXDraftOrderProcessingCreate(partnerConn, enterConn, apexScript, logFilePath, apexFolder, orderableClass, sequence);

					} 



					else if(statement != null && statement.contains(orderableClass + "OrderProcessing.createOrderRole")) {	

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";
						apexScript = UnitTestingUtils.executeOrderProcessingCreateOrderRole(partnerConn, enterConn, apexScript, logFilePath, orderableClass, statement);

					} 

					
					else if(statement != null && statement.contains(orderableClass + "OrderProcessing.createOrderItem")) {	

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";
						apexScript = UnitTestingUtils.executeOrderProcessingCreateOrderItem(partnerConn, enterConn, apexScript, logFilePath, orderableClass, statement);

					} 


					else if(statement != null && statement.equals(orderableClass + "OrderProcessing.processOrder($orderId);")) {

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";
						apexScript = UnitTestingUtils.executeOrderProcessingProcessOrder(partnerConn, enterConn, apexScript, logFilePath, orderableClass);



					} 

					//SFUtility.log("apexScript: " + apexScript,  LOG_FILE_PATH);

					else if(statement != null && statement.equals(orderableClass + "OrderProcessing.closePreBillingCase($orderId);")) {

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";				
						apexScript = CaseProcessing.executeOrderProcessingClosePreBillingCase(partnerConn, enterConn, apexScript, logFilePath, orderableClass);

					} 


					else if(statement != null && statement.equals(orderableClass + "OrderProcessing.closeSomnCase($orderId);")) {

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";
						apexScript = CaseProcessing.executeOrderProcessingCloseSOMNCase(partnerConn, enterConn, apexScript, logFilePath, orderableClass);

					}


					else if(statement != null && statement.equals(orderableClass + "OrderProcessing.closeClinicalExperienceCase($orderId);")) {

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";					
						apexScript = CaseProcessing.executeOrderProcessingCloseClinicalExperienceCase(partnerConn, enterConn, apexScript, logFilePath, orderableClass);
					}



					else if(statement != null && statement.equals(orderableClass + "OrderProcessing.createPackage($orderId);")) {

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";					
						apexScript = UnitTestingUtils.executeOrderProcessingCreatePackage(partnerConn, enterConn, apexScript, logFilePath, orderableClass);

					} 


					else if(statement != null && statement.equals(orderableClass + "OrderProcessing.closeSpecArrivalCase($orderId);")) {

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";
						apexScript = CaseProcessing.executeOrderProcessingCloseSpecArrivalCase(partnerConn, enterConn, apexScript,	logFilePath, orderableClass);

					} 


					else if(statement != null && statement.equals(orderableClass + "OrderProcessing.createSpecimen($orderId);")) {

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";
						apexScript = UnitTestingUtils.executeOrderProcessingCreateSpecimen(partnerConn, enterConn, apexScript, logFilePath, icrtUser, icrtPassword, orderableClass);

					} 


					else if(statement != null && statement.equals(orderableClass + "OrderProcessing.createResult($orderId);")) {

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";
						apexScript = UnitTestingUtils.executeOrderProcessingCreateResult(partnerConn, enterConn, apexScript, logFilePath, icrtUser, icrtPassword, orderableClass);

					} 


					else if(statement != null && statement.equals(orderableClass + "OrderProcessing.updateOLIToDVC($orderId);")) {

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";					
						apexScript = UnitTestingUtils.executeOrderProcessingUpdateOLIToDVC(partnerConn, enterConn, apexScript, logFilePath, orderableClass);					

					}


					else if(statement != null && statement.equals(orderableClass + "OrderProcessing.createDistributionEvent($orderId);")) {

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";
						apexScript = UnitTestingUtils.executeOrderProcessingCreateDistributionEvent(partnerConn, enterConn, apexScript, logFilePath, icrtUser, icrtPassword, orderableClass);

					}	

					else if(statement != null && statement.equals(orderableClass + "OrderProcessing.createAgreement();")) {
						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";
						apexScript = UnitTestingUtils.executeOrderProcessingCreateAgreement(apexScript, orderableClass);

					} 

					else if(statement != null && statement.equals(orderableClass + "OrderProcessing.createUser();")) {
						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";		

						apexScript = UnitTestingUtils.executeOrderProcessingCreateUser(apexScript, orderableClass);
					} 

					//else if(statement != null && statement.equals(orderableClass + "OrderProcessing.updateOLI($orderId);")) {	
					else if(statement != null && statement.contains(orderableClass + "OrderProcessing.updateOLI($orderId")) {	
								
						int oliIndex = getOliIndexFromStatement(statement, logFilePath);
						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";				

						apexScript = UnitTestingUtils.executeOrderProcessingUpdateOLI(partnerConn, enterConn, apexScript, logFilePath, sequence, apexFolder, orderableClass, oliIndex, statement);


					} else if(statement != null && statement.equals(orderableClass + "OrderProcessing.updateOrder($orderId);")) {					

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";
						apexScript = UnitTestingUtils.executeOrderProcessingUpdateOrder(partnerConn, enterConn, apexScript, logFilePath, sequence, apexFolder, orderableClass);


					} else if(statement != null && orderableClass.toUpperCase().contains("PCDX") && statement.equals(orderableClass + "OrderProcessing.updateDraftOrder($orderId);")) {					

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";
						apexScript = UnitTestingUtils.executeOrderProcessingUpdatePCDxDraftOrder(partnerConn, enterConn, apexScript, logFilePath, sequence, apexFolder, orderableClass, updateDraftOrderURL, icrtUser, icrtPassword);


					}  else if(statement != null && statement.equals(orderableClass + "OrderProcessing.closeBillingCase($orderId);")) {

						apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
						apexWithoutFixtures = "";
						apexScript = CaseProcessing.executeOrderProcessingCloseBillingCase(partnerConn, enterConn, apexScript,	logFilePath, orderableClass);


					} else if(statement != null) {
						apexWithoutFixtures = apexWithoutFixtures + statement;
					}
				}
			    tcEndTime = getTime(logFilePath);
			    printTimeSpent(logFilePath);

				apexScript = UnitTestingUtils.executeApexWithoutFixtures(partnerConn, apexScript, logFilePath, apexWithoutFixtures);
				apexWithoutFixtures = "";

				apexScript = apexScript.trim();
				SFUtility.log("apexScript|" + apexScript + "|",  logFilePath);
				SFUtility.log("orderId: "+UnitTestingUtils.orderId, logFilePath);
				if(apexScript != null && !apexScript.trim().equals("") && UnitTestingUtils.orderId != null) {
					apexScript = apexScript.trim();
					apexScript = UnitTestingUtils.populateGlobalVariableValues(apexScript);

					SFUtility.log("statement: " + apexScript,  logFilePath);
					stepExecutionStatusMap = ApexUtil.execute(partnerConn, apexScript.trim(), logFilePath);
					for(String key : stepExecutionStatusMap.keySet()) {
						if(key.equals("Failed")) {
							UnitTestingUtils.updateUnitTestResults(partnerConn, orderIdsList, "Failed", logFilePath);
							return false;
						} else {
							UnitTestingUtils.updateUnitTestResults(partnerConn, orderIdsList, "Passed", logFilePath);
							//System.out.println("availableFinalCsvList ===================" +availableFinalCsvList);
							return true;
						}
					}
					apexScript = "";
				}

			} catch (Exception ex) {
				//System.out.println("Exception: "+ex.getMessage());
				SFUtility.log("Exception: "+ex.getMessage(), logFilePath);
				UnitTestingUtils.addEnvProvMessage(ex.getMessage());
				UnitTestingUtils.updateUnitTestResults(partnerConn, orderIdsList, "Failed", logFilePath);
				return false;
			}
			
			UnitTestingUtils.updateUnitTestResults(partnerConn, orderIdsList, "Passed", logFilePath);
			UnitTestingUtils.patientId = "";	
			return true;
		} catch (Exception ex) {
			System.out.println(""+ex.getMessage());
			UnitTestingUtils.addEnvProvMessage(ex.getMessage());
			return false;
		}
	
	}
	
	public static int getOliIndexFromStatement(String statement, String logFilePath) {
		
		int oliIndex = 1;
		if(statement != null && !statement.trim().equals("")) {
			int startIndex = statement.indexOf(",");
			int endIndex = statement.indexOf(")");
			
			String oliIndexStr = statement.substring(startIndex+1, endIndex);
			SFUtility.log("oliIndexStr: "+oliIndexStr, logFilePath); 
			try {
				if(oliIndexStr != null) {
					oliIndex = Integer.parseInt(oliIndexStr.trim());				
				}
			} catch (Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}		
		}

		return oliIndex;
	}	
	
	
	
	public static long getTime(String logFilePath) {
	     Date now = java.util.Calendar.getInstance().getTime();
	     SFUtility.log("Current Time:: " + now, logFilePath);
	     return now.getTime();
	     
	}
	
	public static void printTimeSpent(String logFilePath) {
		long timeTakenInMilliSec = tcEndTime - tcStartTime;
		tcExecTime = timeTakenInMilliSec/1000;
		SFUtility.log("Execution time(in sec): " + tcExecTime, logFilePath);	     
	}
	
}