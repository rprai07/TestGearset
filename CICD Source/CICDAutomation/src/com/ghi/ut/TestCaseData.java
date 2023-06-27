package com.ghi.ut;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ghi.apex.ApexUtil;
import com.ghi.common.CSVFileReader;
import com.ghi.common.XMLAndJSONRequestHandler;
import com.ghi.common.SFDataFactory;
import com.ghi.common.SFUtility;
import com.ghi.data.SOAPUtil;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.partner.PartnerConnection;

public class TestCaseData {
	
	
	
	

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
	public static List<Object>  create(String apexFolder, String orderableClass, PartnerConnection partnerConn, EnterpriseConnection enterConn, String apexScript,String apexFileName, String logFilePath, int sequence, String url, String username, String password) throws Exception{


		boolean isSpecificOrderDetailsCSVOrJSONAvailable = false;
		StringBuilder availableCsvAndJsonList = new StringBuilder("");
		String payorFilePath = apexFolder + "\\" + sequence+"_UTF_PayorAccount_Create.csv";
		//String defaultPayorFilePath = APEX_FOLDER + "\\" +"Default_PayorAccount.csv";
		String payorScript = CSVFileReader.getScript(payorFilePath, "Account", logFilePath, enterConn);
		Set<String>  executionIdList = new HashSet<String>();
		List<Object> testDataAndSpecificOrderDetailsFlag = new ArrayList<Object>();
		

		if(payorScript != null && !payorScript.trim().equals("")) {
			String specificCSV = sequence+"_UTF_PayorAccount_Create.csv" ;
			availableCsvAndJsonList.append(sequence+"_UTF_PayorAccount_Create.csv").append(",");
			System.out.println("availableFinalCsvList payor ===================" +availableCsvAndJsonList);
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+specificCSV+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
	     	SFUtility.log("list size......................."+objList.size(),  logFilePath);
	     	
			
			if(objList.size()>0)
			{
				String executionid = UnitTestingUtils.checkRecordExistance(enterConn,logFilePath,specificCSV,"Account");
				if(executionid != "Record Doesn't exist")
				{
					UnitTestingUtils.payorAccountId = SFDataFactory.getId(enterConn, executionid, logFilePath);
				SFUtility.log("Payor Record id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.payorAccountId,  logFilePath);
				executionIdList.add(executionid);
				}
		    	if(executionid == "Record Doesn't exist") {
			    	
			    	String recordFixture = UnitTestFixtures.getCreateRecordFixture(payorScript, timeStr, apexFileName,specificCSV);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					UnitTestingUtils.payorAccountId = SFDataFactory.getId(enterConn, timeStr, logFilePath);
					SFUtility.log("Payor Record id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.payorAccountId,  logFilePath);
					executionIdList.add(timeStr);
			    }
		    	
			}
			else
			{
		    String recordFixture = UnitTestFixtures.getCreateRecordFixture(payorScript, timeStr,apexFileName, specificCSV);
			ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
			UnitTestingUtils.payorAccountId = SFDataFactory.getId(enterConn, timeStr, logFilePath);
			SFUtility.log("Payor Record id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.payorAccountId,  logFilePath);
			executionIdList.add(timeStr);
		} }
		else {
			UnitTestingUtils.payorAccountId = UnitTestingUtils.defaultPayorAccountId;
		}




		String addressFilePath = apexFolder + "\\" + sequence+"_UTF_OSM_Address__c_Create.csv";			
		String addressScript = CSVFileReader.getScript(addressFilePath, "OSM_Address__c", logFilePath, enterConn);					

		if(addressScript !=null && !addressScript.trim().equals("")) {
			String specificCSV = sequence+"_UTF_OSM_Address__c_Create.csv" ;
			availableCsvAndJsonList.append(sequence+"_UTF_OSM_Address__c_Create.csv").append(",");
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+specificCSV+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
	     	SFUtility.log("list size......................."+objList.size(),  logFilePath);
	     	
			
			if(objList.size()>0)
			{
				String executionid = UnitTestingUtils.checkRecordExistance(enterConn,logFilePath,specificCSV,"OSM_Address__c");
				if(executionid != "Record Doesn't exist")
				{
					UnitTestingUtils.addressId = SFDataFactory.getId(enterConn, executionid, logFilePath);
				SFUtility.log("Address record id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.addressId,  logFilePath);
				executionIdList.add(executionid);
				}
		    	if(executionid == "Record Doesn't exist") {
			    	
			    	String recordFixture = UnitTestFixtures.getCreateRecordFixture(addressScript, timeStr, apexFileName,specificCSV);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					UnitTestingUtils.addressId = SFDataFactory.getId(enterConn, timeStr, logFilePath);
					SFUtility.log("Address record id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.addressId,  logFilePath);
					executionIdList.add(timeStr);
			    }
		    	
			}
			else {
			String recordFixture = UnitTestFixtures.getCreateRecordFixture(addressScript, timeStr, apexFileName, specificCSV);
			//recordFixture = recordFixture + "record1.accountid = '"+payorAccountId+"';update record1;";

			ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
			UnitTestingUtils.addressId = SFDataFactory.getId(enterConn, timeStr, logFilePath);	
			SFUtility.log("Address record id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.addressId,  logFilePath);
			executionIdList.add(timeStr);
			} 
		} else {
			UnitTestingUtils.addressId = UnitTestingUtils.defaultAddressId;
		}


		String hcofilePath = apexFolder + "\\" + sequence+"_UTF_HCOAccount_Create.csv";
		//String defaultHCOFilePath = APEX_FOLDER + "\\" +"Default_HCOAccount.csv";			
		String hcoScript = CSVFileReader.getScript(hcofilePath, "Account", logFilePath, enterConn);


		if(hcoScript != null && !hcoScript.trim().equals("")) {
			String specificCSV = sequence+"_UTF_HCOAccount_Create.csv" ;
			availableCsvAndJsonList.append(sequence+"_UTF_HCOAccount_Create.csv").append(",");
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+specificCSV+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
	     	SFUtility.log("list size......................."+objList.size(),  logFilePath);
	     	
			
			if(objList.size()>0)
			{
				String executionid = UnitTestingUtils.checkRecordExistance(enterConn,logFilePath,specificCSV,"Account");
				if(executionid != "Record Doesn't exist")
				{
					UnitTestingUtils.hcoAccountId = SFDataFactory.getId(enterConn, executionid, logFilePath);
				SFUtility.log("HCO Account id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.hcoAccountId,  logFilePath);

				//Create address affiliation
				String addressAffiliationFixture = UnitTestFixtures.getCreateAddressAffiliationFixture(UnitTestingUtils.hcoAccountId, UnitTestingUtils.addressId);
				ApexUtil.execute(partnerConn, addressAffiliationFixture,  logFilePath);	
				executionIdList.add(executionid);
				}
		    	if(executionid == "Record Doesn't exist") {
			    	
			    	String recordFixture = UnitTestFixtures.getCreateRecordFixture(hcoScript, timeStr, apexFileName,specificCSV);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					UnitTestingUtils.hcoAccountId = SFDataFactory.getId(enterConn, timeStr, logFilePath);
					SFUtility.log("HCO Account id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.hcoAccountId,  logFilePath);

					//Create address affiliation
					String addressAffiliationFixture = UnitTestFixtures.getCreateAddressAffiliationFixture(UnitTestingUtils.hcoAccountId, UnitTestingUtils.addressId);
					ApexUtil.execute(partnerConn, addressAffiliationFixture,  logFilePath);	
					executionIdList.add(timeStr);
		    	
		    	}
		    	
			}
			else
			{
			String recordFixture = UnitTestFixtures.getCreateRecordFixture(hcoScript, timeStr,apexFileName, specificCSV);
			ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
			UnitTestingUtils.hcoAccountId = SFDataFactory.getId(enterConn, timeStr, logFilePath);
			SFUtility.log("HCO Account id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.hcoAccountId,  logFilePath);

			//Create address affiliation
			String addressAffiliationFixture = UnitTestFixtures.getCreateAddressAffiliationFixture(UnitTestingUtils.hcoAccountId, UnitTestingUtils.addressId);
			ApexUtil.execute(partnerConn, addressAffiliationFixture,  logFilePath);	
			executionIdList.add(timeStr);
			}

		} else {
			UnitTestingUtils.hcoAccountId = UnitTestingUtils.defaultHcoAccountId;
		}








		String hcpfilePath = apexFolder + "\\" + sequence+"_UTF_HCPContact_Create.csv";
		//String defaultHCPFilePath = APEX_FOLDER + "\\" +"Default_HCPContact.csv";			
		String hcpScript = CSVFileReader.getScript(hcpfilePath, "Contact", logFilePath, enterConn);


		if(hcpScript !=null && !hcpScript.trim().equals("")) {
			String specificCSV = sequence+"_UTF_HCPContact_Create.csv" ;
			availableCsvAndJsonList.append(sequence+"_UTF_HCPContact_Create.csv").append(",");
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			
			
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+specificCSV+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
	     	SFUtility.log("list size......................."+objList.size(),  logFilePath);
	     	
			
			if(objList.size()>0)
			{
				String executionid = UnitTestingUtils.checkRecordExistance(enterConn,logFilePath,specificCSV,"Contact");
				if(executionid != "Record Doesn't exist")
				{
					UnitTestingUtils.hcpContactId = SFDataFactory.getId(enterConn, executionid, logFilePath);
				SFUtility.log("HCP Contact id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.hcpContactId,  logFilePath);
				executionIdList.add(executionid);
				}
		    	if(executionid == "Record Doesn't exist") {
			    	
			    	String recordFixture = UnitTestFixtures.getCreateRecordFixture(hcpScript, timeStr, apexFileName,specificCSV);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					UnitTestingUtils.hcpContactId = SFDataFactory.getId(enterConn, timeStr, logFilePath);
					SFUtility.log("HCP Contact id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.hcpContactId,  logFilePath);
					executionIdList.add(timeStr);
		    	}
		    	
			}
			else
			{
			String recordFixture = UnitTestFixtures.getCreateRecordFixture(hcpScript, timeStr, apexFileName, specificCSV);

			recordFixture = recordFixture.replace("insert record1", "record1.accountid = '"+UnitTestingUtils.payorAccountId+"';insert record1");

			//recordFixture = recordFixture + "record1.accountid = '"+payorAccountId+"';update record1;";
			//recordFixture = recordFixture + "record1.accountid = '"+payorAccountId+"';";

			ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
			UnitTestingUtils.hcpContactId = SFDataFactory.getId(enterConn, timeStr, logFilePath);	
			SFUtility.log("HCP Contact id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.hcpContactId,  logFilePath);
			executionIdList.add(timeStr);
		} }else {
			UnitTestingUtils.hcpContactId = UnitTestingUtils.defaultHcpContactId;
		}







		String patientfilePath = apexFolder + "\\" + sequence+"_UTF_PatientContact_Create.csv";
		//String defaultPatientFilePath = APEX_FOLDER + "\\" +"Default_PatientContact.csv";			
		String patientScript = CSVFileReader.getScript(patientfilePath, "Contact", logFilePath, enterConn);


		if(patientScript !=null && !patientScript.trim().equals("")) {
			String specificCSV = sequence+"_UTF_PatientContact_Create.csv" ;
			availableCsvAndJsonList.append(sequence+"_UTF_PatientContact_Create.csv").append(",");
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+specificCSV+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
	     	SFUtility.log("list size......................."+objList.size(),  logFilePath);
	     	
			if(objList.size()>0)
			{
				String executionid = UnitTestingUtils.checkRecordExistance(enterConn,logFilePath,specificCSV,"Contact");
				if(executionid != "Record Doesn't exist")
				{
					UnitTestingUtils.patientId = SFDataFactory.getId(enterConn, executionid, logFilePath);
				SFUtility.log("Patient id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.patientId,  logFilePath);
				executionIdList.add(executionid);
				}
		    	if(executionid == "Record Doesn't exist") {
			    	
			    	String recordFixture = UnitTestFixtures.getCreateRecordFixture(patientScript, timeStr, apexFileName,specificCSV);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					UnitTestingUtils.patientId = SFDataFactory.getId(enterConn, timeStr, logFilePath);
					SFUtility.log("Patient id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.patientId,  logFilePath);
					executionIdList.add(timeStr);
		    	}
		    	
			}
			else
			{
			String recordFixture = UnitTestFixtures.getCreateRecordFixture(patientScript, timeStr,apexFileName, specificCSV);
			//recordFixture = recordFixture + "record1.accountid = '"+payorAccountId+"';update record1;";
			recordFixture = recordFixture.replace("insert record1", "record1.accountid = '"+UnitTestingUtils.payorAccountId+"';insert record1");
			ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
			UnitTestingUtils.patientId = SFDataFactory.getId(enterConn, timeStr, logFilePath);
			SFUtility.log("Patient id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.patientId,  logFilePath);
			executionIdList.add(timeStr);
		} }else {
			if(orderableClass.contains("Prostate")) {
				//SFDataFactory.updatePatient(partnerConn, patientId, "Male",logFilePath);

				UnitTestingUtils.patientId = UnitTestingUtils.defaultMalePatientId;
			}else {
				UnitTestingUtils.patientId = UnitTestingUtils.defaultFemalePatientId;
			}
		}



		//SFUtility.log("orderableClass:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: "+orderableClass, logFilePath);
		//SFUtility.log("patientId:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: "+patientId, logFilePath);



		String orderfilePath = apexFolder + "\\" + sequence+"_UTF_Order_Create.csv";
		//String defaultPatientFilePath = APEX_FOLDER + "\\" +"Default_PatientContact.csv";			
		String orderScript = CSVFileReader.getScript(orderfilePath, "Order", logFilePath, enterConn);


		if(orderScript !=null && !orderScript.trim().equals("")) {
			String specificCSV = sequence+"_UTF_Order_Create.csv" ;
			availableCsvAndJsonList.append(sequence+"_UTF_Order_Create.csv").append(",");
			isSpecificOrderDetailsCSVOrJSONAvailable = true;
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			String recordFixture = UnitTestFixtures.getCreateRecordFixture(orderScript, timeStr,apexFileName, specificCSV);
			//recordFixture = recordFixture + "record1.accountid = '"+payorAccountId+"';update record1;";				
			recordFixture = recordFixture.replace("insert record1", "record1.accountid = '"+UnitTestingUtils.payorAccountId+"';record1.OSM_Patient__c='"+UnitTestingUtils.patientId+"';insert record1");				
			ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
			UnitTestingUtils.orderId = SFDataFactory.getId(enterConn, timeStr, logFilePath);	
			SFUtility.log("Order id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.orderId,  logFilePath);

			executionIdList.add(timeStr);
			//String script1 = UnitTestFixtures.getUpdateOLIICDCodeFixture(orderable, orderId);
			//ApexUtil.execute(partnerConn, script1,  logFilePath);
			//String script2 = UnitTestFixtures.getUpdateOLINodeStatusFixture(orderable, orderId);
			//ApexUtil.execute(partnerConn, script2,  logFilePath);


			//String ssOrderRoleFixture = UnitTestFixtures.getCreateSSOrderRoleFixture(orderId, hcoAccountId, hcpContactId);
			//ApexUtil.execute(partnerConn, ssOrderRoleFixture,  LOG_FILE_PATH);	
		}


		
		
		String pcdxDraftOrderfilePath = apexFolder + "\\" + sequence+"_UTF_PCDxDraftOrder_Create.json";
		String pcdxDraftOrderScript = XMLAndJSONRequestHandler.readFile(pcdxDraftOrderfilePath, logFilePath);

		if(pcdxDraftOrderScript !=null && !pcdxDraftOrderScript.trim().equals("")) {
			String specificJSON = sequence+"_UTF_PCDxDraftOrder_Create.json" ;
			availableCsvAndJsonList.append(sequence+"_UTF_PCDxDraftOrder_Create.json").append(",");
			isSpecificOrderDetailsCSVOrJSONAvailable = true;
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			//String recordFixture = UnitTestFixtures.getCreateRecordFixture("", timeStr,apexFileName, specificJSON);
			//recordFixture = recordFixture.replace("insert record1", "record1.accountid = '"+UnitTestingUtils.payorAccountId+"';record1.OSM_Patient__c='"+UnitTestingUtils.patientId+"';insert record1");				
			String orderId = XMLAndJSONRequestHandler.postJsonRequest(url, pcdxDraftOrderfilePath, username, password, logFilePath, enterConn);
			//ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
			UnitTestingUtils.orderId = orderId;
			SFUtility.log("Order id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.orderId,  logFilePath);

			executionIdList.add(timeStr);
		}

		
		
		
		
		
		

		//String patientfilePath = APEX_FOLDER + "\\" + sequence+"_PatientContact.csv";
		String agreementFilePath = apexFolder + "\\" + sequence+"_UTF_Apttus__APTS_Agreement__c_Create.csv";			
		String agreementScript = CSVFileReader.getScript(agreementFilePath, "Apttus__APTS_Agreement__c", logFilePath, enterConn);					

		if(agreementScript !=null && !agreementScript.trim().equals("")) {
			String specificCSV = sequence+"_UTF_Apttus__APTS_Agreement__c_Create.csv" ;
			availableCsvAndJsonList.append(sequence+"_UTF_Apttus__APTS_Agreement__c_Create.csv").append(",");
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+specificCSV+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
	     	SFUtility.log("list size......................."+objList.size(),  logFilePath);
	     	
			
			if(objList.size()>0)
			{
				String executionid = UnitTestingUtils.checkRecordExistance(enterConn,logFilePath,specificCSV,"Apttus__APTS_Agreement__c");
				if(executionid != "Record Doesn't exist")
				{
					UnitTestingUtils.agreementId = SFDataFactory.getId(enterConn, executionid, logFilePath);
					SFUtility.log("Agreement id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.agreementId,  logFilePath);
					executionIdList.add(executionid);
				}
		    	if(executionid == "Record Doesn't exist") {
			    	
			    	String recordFixture = UnitTestFixtures.getCreateRecordFixture(agreementScript, timeStr, apexFileName,specificCSV);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					UnitTestingUtils.agreementId = SFDataFactory.getId(enterConn, timeStr, logFilePath);
					SFUtility.log("Agreement id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.agreementId,  logFilePath);
					executionIdList.add(timeStr);
		    	}
		    	
			}
			else
			{
			String recordFixture = UnitTestFixtures.getCreateRecordFixture(agreementScript, timeStr,apexFileName, specificCSV);
			//recordFixture = recordFixture + "record1.accountid = '"+payorAccountId+"';update record1;";

			ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
			UnitTestingUtils.agreementId = SFDataFactory.getId(enterConn, timeStr, logFilePath);		
			SFUtility.log("Agreement id:::::::::::::::::::::::::::::::::::::::"+UnitTestingUtils.agreementId,  logFilePath);
			executionIdList.add(timeStr);
		}} else {
			UnitTestingUtils.agreementId = UnitTestingUtils.defaultAgreementId;
		}









		//String patientfilePath = APEX_FOLDER + "\\" + sequence+"_PatientContact.csv";
		String userFilePath = apexFolder + "\\" + sequence+"_UTF_User_Create.csv";			
		String userScript = CSVFileReader.getScript(userFilePath, "User", logFilePath, enterConn);					

		if(userScript !=null && !userScript.trim().equals("")) {
			//Date date = new Date();
			//long time = date.getTime();
			//String timeStr = Long.toString(time);
			//String recordFixture = UnitTestFixtures.getCreateRecordFixture(userScript, timeStr);
			//recordFixture = recordFixture + "record1.accountid = '"+payorAccountId+"';update record1;";
			//String specificCSV = sequence+"_UTF_User_Create.csv" ;
			availableCsvAndJsonList.append(sequence+"_UTF_User_Create.csv").append(",");
			ApexUtil.execute(partnerConn, userScript,  logFilePath);							
			String userName = CSVFileReader.getUserNameFromUserScript(userFilePath, "User", logFilePath);
			try {
				Thread.sleep(UnitTestingUtils.WAIT_TIME_IN_MILLISEC - 10000);
			} catch (InterruptedException ex) {
				System.out.println("Exception: "+ex.getMessage());
				SFUtility.log("Exception: "+ex.getMessage(), logFilePath);
			}
			UnitTestingUtils.userId = SFDataFactory.getUserIdFromUserName(enterConn, userName,logFilePath);
		} else {
			UnitTestingUtils.userId = UnitTestingUtils.defaultUserId;
		}


		testDataAndSpecificOrderDetailsFlag.add(executionIdList);
		testDataAndSpecificOrderDetailsFlag.add(isSpecificOrderDetailsCSVOrJSONAvailable);
		testDataAndSpecificOrderDetailsFlag.add(availableCsvAndJsonList.toString());
		

		//return isSpecificOrderDetailsCSVAvailable;
		return testDataAndSpecificOrderDetailsFlag;
	
	}
}