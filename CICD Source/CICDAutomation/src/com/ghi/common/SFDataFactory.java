package com.ghi.common;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ghi.apex.ApexUtil;
import com.ghi.data.SOAPUtil;
import com.ghi.ut.UnitTestFixtures;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.soap.enterprise.sobject.OSM_Billing_Diagnosis_Code__c;
import com.sforce.soap.enterprise.sobject.Order;
import com.sforce.soap.enterprise.sobject.OrderItem;
import com.sforce.soap.enterprise.sobject.RecordType;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.sobject.Unit_Test_Result__c;
import com.sforce.soap.enterprise.sobject.User;
import com.sforce.soap.partner.PartnerConnection;

public class SFDataFactory {

	/**
	 * Method to create default test data.
	 * @param partnerConn
	 * @param enterConn
	 * @param logFilePath
	 * @param apexFolder
	 * @return
	 */
	public static ArrayList createDefaultTestData(PartnerConnection partnerConn, EnterpriseConnection enterConn, String logFilePath, String apexFolder) {


		SFUtility.log("Creating default data .........................",  logFilePath);	
		StringBuilder availableCsvList = new StringBuilder("");
		Map<String, String> defaultDataMap = new HashMap<String, String>();
		ArrayList defaultDataList = new ArrayList();
		StringBuilder defaultDataIdList = new StringBuilder("");
		//String recordId ="";		
		String payorAccountId ="";
		String hcoAccountId = "";
		String hcpContactId = "";

		String malePatientId = "";
		String femalePatientId = "";

		String agreementId = "";
		String userName = "";
		String userId = "";		
		String addressId = "";	


		List<String> filesAlreadyProcessed = new ArrayList<String>();
		List<String> filesmissing = new ArrayList<String>();
		//getCSVName(enterConn,logFilePath,"Default_PayorAccount_Create.csv","Account");

		//if(csvFile.getName().contains("Default_PayorAccount")) {
		String defaultPayorFilePath = apexFolder + "\\" +"Default_PayorAccount_Create.csv";
		String payorScript = CSVFileReader.getScript(defaultPayorFilePath, "Account", logFilePath, enterConn);		
		if(payorScript != null && !payorScript.trim().equals("")) {
			availableCsvList.append("Default_PayorAccount_Create.csv").append(",");
			String csvName = "Default_PayorAccount_Create.csv";

			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+csvName+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
			SFUtility.log("list size......................."+objList.size(),  logFilePath);


			if(objList.size()>0)
			{
				String executionid = checkRecordExist(enterConn,logFilePath,"Default_PayorAccount_Create.csv","Account");
				if(executionid != "Record Doesn't exist")
				{
					payorAccountId = getId(enterConn, executionid, logFilePath);
				}
				if(executionid == "Record Doesn't exist") {

					String recordFixture = UnitTestFixtures.getCreateRecordFixture(payorScript, timeStr, "",csvName);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					payorAccountId = getId(enterConn, timeStr, logFilePath);
				}

			}
			else
			{
				String recordFixture = UnitTestFixtures.getCreateRecordFixture(payorScript, timeStr, "",csvName);
				ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
				payorAccountId = getId(enterConn, timeStr, logFilePath);
			}
			//System.out.println("payor account id ========" +payorAccountId );
			defaultDataIdList.append(payorAccountId).append(",");
			SFUtility.log("Payor Account record id:::::::::::::::::::::::::::::::::::::::"+payorAccountId,  logFilePath);
			defaultDataMap.put("payorAccountId", payorAccountId);
			filesAlreadyProcessed.add(defaultPayorFilePath);

		}

		else {
			System.out.println("Missing default data file : Default_PayorAccount_Create.csv" );
			filesmissing.add("Default_PayorAccount_Create.csv");

		}
		//} else if(csvFile.getName().contains("Default_HCOAccount")) {


		String defaultAddressFilePath = apexFolder + "\\" +"Default_OSM_Address__c_Create.csv";			
		String addressScript = CSVFileReader.getScript(defaultAddressFilePath, "OSM_Address__c", logFilePath, enterConn);					

		if(addressScript !=null && !addressScript.trim().equals("")) {
			String csvName = "Default_OSM_Address__c_Create.csv";
			availableCsvList.append("Default_OSM_Address__c_Create.csv").append(",");
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+csvName+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
			SFUtility.log("list size......................."+objList.size(),  logFilePath);

			if(objList.size() >0)
			{
				//System.out.println("true got");
				String executionid = checkRecordExist(enterConn,logFilePath,"Default_OSM_Address__c_Create.csv","OSM_Address__c");

				if(executionid != "Record Doesn't exist")
				{
					addressId = getId(enterConn, executionid, logFilePath);
				}
				if(executionid == "Record Doesn't exist") {

					String recordFixture = UnitTestFixtures.getCreateRecordFixture(addressScript, timeStr, "",csvName);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					addressId = getId(enterConn, timeStr, logFilePath);	
				}
			}
			else {
				String recordFixture = UnitTestFixtures.getCreateRecordFixture(addressScript, timeStr, "",csvName);
				ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
				addressId = getId(enterConn, timeStr, logFilePath);	
			}

			defaultDataIdList.append(addressId).append(",");
			SFUtility.log("Address record id:::::::::::::::::::::::::::::::::::::::"+addressId,  logFilePath);

			defaultDataMap.put("addressId", addressId);
			filesAlreadyProcessed.add(defaultAddressFilePath);
		}
		else {
			System.out.println("Missing default data file : Default_OSM_Address__c_Create.csv" );
			filesmissing.add("Default_OSM_Address__c_Create.csv");
		}


		String defaultHCOFilePath = apexFolder + "\\" +"Default_HCOAccount_Create.csv";			
		String hcoScript = CSVFileReader.getScript(defaultHCOFilePath, "Account", logFilePath, enterConn);


		if(hcoScript != null && !hcoScript.trim().equals("")) {
			availableCsvList.append("Default_HCOAccount_Create.csv").append(",");
			String csvName = "Default_HCOAccount_Create.csv";
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+csvName+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
			SFUtility.log("list size......................."+objList.size(),  logFilePath);

			if(objList.size()>0) {
				String executionid = checkRecordExist(enterConn,logFilePath,"Default_HCOAccount_Create.csv","Account");

				if(executionid != "Record Doesn't exist")
				{
					hcoAccountId = getId(enterConn, executionid, logFilePath);
				}
				if(executionid == "Record Doesn't exist") {

					String recordFixture = UnitTestFixtures.getCreateRecordFixture(hcoScript, timeStr, "",csvName);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					hcoAccountId = getId(enterConn, timeStr, logFilePath);		
				}
			}
			else {
				String recordFixture = UnitTestFixtures.getCreateRecordFixture(hcoScript, timeStr, "",csvName);
				ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
				hcoAccountId = getId(enterConn, timeStr, logFilePath);	
			}

			defaultDataIdList.append(hcoAccountId).append(",");
			SFUtility.log("HCO Account id:::::::::::::::::::::::::::::::::::::::"+hcoAccountId,  logFilePath);

			defaultDataMap.put("hcoAccountId", hcoAccountId);

			SFUtility.log("addressId: "+addressId, logFilePath);

			//Create address affiliation
			String addressAffiliationFixture = UnitTestFixtures.getCreateAddressAffiliationFixture(hcoAccountId, addressId);
			ApexUtil.execute(partnerConn, addressAffiliationFixture,  logFilePath);	
			filesAlreadyProcessed.add(defaultHCOFilePath);
		}
		//} else if(csvFile.getName().contains("Default_HCPContact")) {
		else {
			System.out.println("Missing default data file : Default_HCOAccount_Create.csv" );
			filesmissing.add("Default_HCOAccount_Create.csv");

		}

		//String hcpfilePath = APEX_FOLDER + "\\" + sequence+"_HCPContact.csv";
		String defaultHCPFilePath = apexFolder + "\\" +"Default_HCPContact_Create.csv";			
		String hcpScript = CSVFileReader.getScript(defaultHCPFilePath, "Contact", logFilePath, enterConn);		

		if(hcpScript !=null && !hcpScript.trim().equals("")) {
			String csvName = "Default_HCPContact_Create.csv";
			availableCsvList.append("Default_HCPContact_Create.csv").append(",");
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+csvName+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
			SFUtility.log("list size......................."+objList.size(),  logFilePath);

			if(objList.size()>0) {
				String executionid = checkRecordExist(enterConn,logFilePath,"Default_HCPContact_Create.csv","Contact");

				if(executionid != "Record Doesn't exist")
				{
					hcpContactId = getId(enterConn, executionid, logFilePath);
				}
				if(executionid == "Record Doesn't exist") {

					String recordFixture = UnitTestFixtures.getCreateRecordFixture(hcpScript, timeStr, "",csvName);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					hcpContactId = getId(enterConn, timeStr, logFilePath);		
				}
			}
			else {
				String recordFixture = UnitTestFixtures.getCreateRecordFixture(hcpScript, timeStr, "",csvName);
				recordFixture = recordFixture.replace("insert record1", "record1.accountid = '"+payorAccountId+"';insert record1");
				ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
				hcpContactId = getId(enterConn, timeStr, logFilePath);

			}
			defaultDataIdList.append(hcpContactId).append(",");
			SFUtility.log("HCP Contact id:::::::::::::::::::::::::::::::::::::::"+hcpContactId,  logFilePath);

			defaultDataMap.put("hcpContactId", hcpContactId);
			filesAlreadyProcessed.add(defaultHCPFilePath);

		}
		else {
			System.out.println("Missing default data file : Default_HCPContact_Create.csv" );
			filesmissing.add("Default_HCPContact_Create.csv");
		}
		//} else if(csvFile.getName().contains("Default_PatientContactFemale")) {

		//String patientfilePath = APEX_FOLDER + "\\" + sequence+"_PatientContact.csv";
		String defaultPatientFemaleFilePath = apexFolder + "\\" +"Default_PatientContactFemale_Create.csv";			
		String femalePatientScript = CSVFileReader.getScript(defaultPatientFemaleFilePath, "Contact", logFilePath, enterConn);					

		if(femalePatientScript !=null && !femalePatientScript.trim().equals("")) {
			String csvName = "Default_PatientContactFemale_Create.csv";
			availableCsvList.append("Default_PatientContactFemale_Create.csv").append(",");
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+csvName+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
			SFUtility.log("list size......................."+objList.size(),  logFilePath);

			if(objList.size()>0) {
				String executionid = checkRecordExist(enterConn,logFilePath,"Default_PatientContactFemale_Create.csv","Contact");

				if(executionid != "Record Doesn't exist")
				{
					femalePatientId = getId(enterConn, executionid, logFilePath);
				}
				if(executionid == "Record Doesn't exist") {

					String recordFixture = UnitTestFixtures.getCreateRecordFixture(femalePatientScript, timeStr, "",csvName);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					femalePatientId = getId(enterConn, timeStr, logFilePath);		
				}
			}
			else {
				String recordFixture = UnitTestFixtures.getCreateRecordFixture(femalePatientScript, timeStr, "",csvName);
				recordFixture = recordFixture.replace("insert record1", "record1.accountid = '"+payorAccountId+"';insert record1");
				ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
				femalePatientId = getId(enterConn, timeStr, logFilePath);
			}

			defaultDataIdList.append(femalePatientId).append(",");
			SFUtility.log("Female patient id:::::::::::::::::::::::::::::::::::::::"+femalePatientId,  logFilePath);

			defaultDataMap.put("femalePatientId", femalePatientId);
			filesAlreadyProcessed.add(defaultPatientFemaleFilePath);

		}
		else {
			System.out.println("Missing default data file : Default_PatientContactFemale_Create.csv" );
			filesmissing.add("Default_PatientContactFemale_Create.csv");
		}
		//} else if(csvFile.getName().contains("Default_PatientContactMale")) {




		//String patientfilePath = APEX_FOLDER + "\\" + sequence+"_PatientContact.csv";
		String defaultPatientMaleFilePath = apexFolder + "\\" +"Default_PatientContactMale_Create.csv";			
		String malePatientScript = CSVFileReader.getScript(defaultPatientMaleFilePath, "Contact", logFilePath, enterConn);					

		if(malePatientScript !=null && !malePatientScript.trim().equals("")) {
			availableCsvList.append("Default_PatientContactMale_Create.csv").append(",");
			String csvName = "Default_PatientContactMale_Create.csv";

			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+csvName+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
			SFUtility.log("list size......................."+objList.size(),  logFilePath);

			if(objList.size()>0) {

				String executionid = checkRecordExist(enterConn,logFilePath,"Default_PatientContactMale_Create.csv","Contact");

				if(executionid != "Record Doesn't exist")
				{
					malePatientId = getId(enterConn, executionid, logFilePath);
				}
				if(executionid == "Record Doesn't exist") {

					String recordFixture = UnitTestFixtures.getCreateRecordFixture(malePatientScript, timeStr, "",csvName);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					malePatientId = getId(enterConn, timeStr, logFilePath);		
				}
			}
			else {
				String recordFixture = UnitTestFixtures.getCreateRecordFixture(malePatientScript, timeStr, "",csvName);
				recordFixture = recordFixture.replace("insert record1", "record1.accountid = '"+payorAccountId+"';insert record1");
				ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
				malePatientId = getId(enterConn, timeStr, logFilePath);
			}

			defaultDataIdList.append(malePatientId).append(",");
			SFUtility.log("Male patient id:::::::::::::::::::::::::::::::::::::::"+malePatientId,  logFilePath);


			defaultDataMap.put("malePatientId", malePatientId);
			filesAlreadyProcessed.add(defaultPatientMaleFilePath);

		}
		else {
			System.out.println("Missing default data file : Default_PatientContactMale_Create.csv" );
			filesmissing.add("Default_PatientContactMale_Create.csv");
		}

		//} else if(csvFile.getName().contains("Default_Agreement")) {
		//String patientfilePath = APEX_FOLDER + "\\" + sequence+"_PatientContact.csv";
		String defaultAgreementFilePath = apexFolder + "\\" +"Default_Apttus__APTS_Agreement__c_Create.csv";			
		String agreementScript = CSVFileReader.getScript(defaultAgreementFilePath, "Apttus__APTS_Agreement__c", logFilePath, enterConn);					

		if(agreementScript !=null && !agreementScript.trim().equals("")) {
			String csvName = "Default_Apttus__APTS_Agreement__c_Create.csv";

			availableCsvList.append("Default_Apttus__APTS_Agreement__c_Create.csv").append(",");
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+csvName+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
			SFUtility.log("list size......................."+objList.size(),  logFilePath);

			if(objList.size()>0) {

				String executionid = checkRecordExist(enterConn,logFilePath,"Default_Apttus__APTS_Agreement__c_Create.csv","Apttus__APTS_Agreement__c");

				if(executionid != "Record Doesn't exist")
				{
					agreementId = getId(enterConn, executionid, logFilePath);
				}
				if(executionid == "Record Doesn't exist") {

					String recordFixture = UnitTestFixtures.getCreateRecordFixture(agreementScript, timeStr, "",csvName);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					agreementId = getId(enterConn, timeStr, logFilePath);		
				}
			}
			else {
				String recordFixture = UnitTestFixtures.getCreateRecordFixture(agreementScript,timeStr, "",csvName);
				//recordFixture = recordFixture + "record1.accountid = '"+payorAccountId+"';update record1;";

				ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
				agreementId = getId(enterConn, timeStr, logFilePath);
			}

			defaultDataIdList.append(agreementId).append(",");
			SFUtility.log("Agreement id:::::::::::::::::::::::::::::::::::::::"+agreementId,  logFilePath);


			defaultDataMap.put("agreementId", agreementId);
			filesAlreadyProcessed.add(defaultAgreementFilePath);
		}
		else {
			System.out.println("Missing default data file : Default_Apttus__APTS_Agreement__c_Create.csv" );
			filesmissing.add("Default_Apttus__APTS_Agreement__c_Create.csv");
		}

		//} else if(csvFile.getName().contains("Default_User")) {

		//String patientfilePath = APEX_FOLDER + "\\" + sequence+"_PatientContact.csv";
		String defaultUserFilePath = apexFolder + "\\" + "Default_User_Create.csv";			
		String userScript = CSVFileReader.getScript(defaultUserFilePath, "User", logFilePath, enterConn);					

		if(userScript !=null && !userScript.trim().equals("")) {
			String csvName = "Default_User_Create.csv";

			availableCsvList.append("Default_User_Create.csv").append(",");

			//Date date = new Date();
			//long time = date.getTime();
			//String timeStr = Long.toString(time);
			//String recordFixture = UnitTestFixtures.getCreateRecordFixture(userScript, timeStr);
			//recordFixture = recordFixture + "record1.accountid = '"+payorAccountId+"';update record1;";

			String soqlcheck = "Select Data_CSV_File_Name__c,Record_Id__c,Time_Stamp__c from Unit_Test_Result__c where Data_CSV_File_Name__c = '"+csvName+"' and Record_Id__c != null LIMIT 1";
			List<SObject> objList = SOAPUtil.executeQuery(enterConn, soqlcheck, logFilePath);		
			SFUtility.log("list size......................."+objList.size(),  logFilePath);
			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			if(objList.size()>0) {

				String executionid = checkRecordExist(enterConn,logFilePath,"Default_User_Create.csv","User");

				if(executionid != "Record Doesn't exist")
				{
					userId = getId(enterConn, executionid, logFilePath);
				}
				if(executionid == "Record Doesn't exist") {

					String recordFixture = UnitTestFixtures.getCreateRecordFixture(agreementScript, timeStr, "",csvName);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);
					userId = getUserIdFromUserName(enterConn, userName, logFilePath);		
				}
			}
			else {
				ApexUtil.execute(partnerConn, userScript,  logFilePath);							
				userName = CSVFileReader.getUserNameFromUserScript(defaultUserFilePath, "User", logFilePath);
				userId = getUserIdFromUserName(enterConn, userName, logFilePath);
			}
			defaultDataIdList.append(userId);
			defaultDataMap.put("userId", userId);
			filesAlreadyProcessed.add(defaultUserFilePath);
		}
		else {
			//System.out.println("Missing default data file : Default_User_Create.csv" );
			//filesmissing.add("Default_User_Create.csv");
		}

		if(filesmissing.size()>0)
		{
			SFUtility.log("Default CSV files missing : " + filesmissing,logFilePath);
			SFUtility.log("Please verify the CSV files and run again..",logFilePath);
			System.exit(1);

		}
		//} 


		/*		File csvFiles[] = ApexUtil.getDefaultCSVFiles(apexFolder, logFilePath);

		SFUtility.log("Default CSV files list size::::::"+csvFiles.length,  logFilePath);	


		for(File csvFile : csvFiles) {

			String fileName = csvFile.getName();
			if(!filesAlreadyProcessed.contains(csvFile.getAbsolutePath()) &&  !fileName.contains("Default_Order_Create.csv")  &&  !fileName.contains("OrderItem")) {


				//else {

				String fileNameWithoutExt = fileName.substring(0, fileName.length() -4);
				String fileNameWithoutExtWithoutOperation = fileNameWithoutExt;


				if(fileNameWithoutExt.contains("_Create")) {
					fileNameWithoutExtWithoutOperation = fileNameWithoutExt.substring(0, fileNameWithoutExt.length() - "_Create".length() );

				} else if(fileNameWithoutExt.contains("_Update")) {
					fileNameWithoutExtWithoutOperation = fileNameWithoutExt.substring(0, fileNameWithoutExt.length() - "_Update".length() );

				}  
				System.out.println("fileNameWithoutExtWithoutOperation: "+fileNameWithoutExtWithoutOperation);


				if(fileNameWithoutExtWithoutOperation.startsWith("Default_")) {
					fileNameWithoutExtWithoutOperation = fileNameWithoutExtWithoutOperation.substring("Default_".length(), fileNameWithoutExtWithoutOperation.length());
				}

				System.out.println("fileNameWithoutExtWithoutOperation: "+fileNameWithoutExtWithoutOperation);


				String objectAPIName = fileNameWithoutExtWithoutOperation;
				System.out.println("objectAPIName: "+objectAPIName);

				//String defaultAddressFilePath = apexFolder + "\\" +"Default_OSM_Address__c_create.csv";
				String defaultFilePath = csvFile.getAbsolutePath();


				//String defaultAddressScript = CSVFileReader.getScript(defaultAddressFilePath, "OSM_Address__c", logFilePath);		
				String defaultRecordScript = CSVFileReader.getScript(defaultFilePath, objectAPIName, logFilePath);		


				if(defaultRecordScript != null && !defaultRecordScript.trim().equals("")) {
					Date date = new Date();
					long time = date.getTime();
					String timeStr = Long.toString(time);
					String recordFixture = UnitTestFixtures.getCreateRecordFixture(defaultRecordScript, timeStr);
					ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
					recordId = getId(enterConn, timeStr, logFilePath);

					if(objectAPIName.equalsIgnoreCase("OSM_Address__c")) {
						defaultDataMap.put("addressId", recordId);
					}
				}
				//}

			}
		}*/


		defaultDataList.add(defaultDataMap);
		defaultDataList.add(availableCsvList.toString());
		defaultDataList.add(defaultDataIdList.toString());
		defaultDataList.add(filesmissing);
		return defaultDataList;
	}





	/**
	 * Method to create default order record by reading its data from CSV file.
	 * @param orderable
	 * @param partnerConn
	 * @param enterConn
	 * @param logFilePath
	 * @param apexFolder
	 * @param payorAccountId
	 * @param hcoAccountId
	 * @param hcpContactId
	 * @param patientId
	 * @return
	 */
	public static String createDefaultOrderRecord(String orderable, PartnerConnection partnerConn, EnterpriseConnection enterConn, String logFilePath, String apexFolder, String payorAccountId, String hcoAccountId, String hcpContactId, String patientId) {



		String orderId = "";
		//Create default order data
		//String patientfilePath = APEX_FOLDER + "\\" + sequence+"_PatientContact.csv";
		String defaultOrderFilePath = apexFolder + "\\" +"Default_Order_Create.csv";			
		String orderScript = CSVFileReader.getScript(defaultOrderFilePath, "Order", logFilePath, enterConn);					

		if(orderScript !=null && !orderScript.trim().equals("")) {
			String csvName = "Default_Order_Create.csv";

			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			String recordFixture = UnitTestFixtures.getCreateRecordFixture(orderScript, timeStr, "",csvName);
			//recordFixture = recordFixture + "record1.accountid = '"+payorAccountId+"';update record1;";
			recordFixture = recordFixture.replace("insert record1", "record1.OSM_Product__c = '"+orderable+"';"+"record1.accountid = '"+payorAccountId+"';record1.OSM_Patient__c='"+patientId+"';insert record1");
			ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
			orderId = getId(enterConn, timeStr, logFilePath);		
			SFUtility.log("Order id:::::::::::::::::::::::::::::::::::::::"+orderId,  logFilePath);

			//String script1 = UnitTestFixtures.getUpdateOLIICDCodeFixture(orderable, orderId);
			//ApexUtil.execute(partnerConn, script1,  logFilePath);
			//String script2 = UnitTestFixtures.getUpdateOLINodeStatusFixture(orderable, orderId);
			//ApexUtil.execute(partnerConn, script2,  logFilePath);


			//Thread.sleep(10000);
			//String orderingOrderRoleFixture = UnitTestFixtures.getCreateOrderRoleFixture(orderable, "Ordering", orderId, hcoAccountId, hcpContactId);
			//ApexUtil.execute(partnerConn, orderingOrderRoleFixture,  logFilePath);
			//String ssOrderRoleFixture = UnitTestFixtures.getCreateOrderRoleFixture(orderable, "Specimen Submitting", orderId, hcoAccountId, hcpContactId);
			//ApexUtil.execute(partnerConn, ssOrderRoleFixture,  logFilePath);



		}
		else {
			//System.out.println("Missing default data file : Default_Order_Create.csv" );
			SFUtility.log("Default CSV files missing : Default_Order_Create.csv", logFilePath);
			SFUtility.log("Please verify the CSV files and run again..",logFilePath);
			System.exit(1);
		}

		return orderId;

	}


	/**
	 * Method to create default order record by reading its data from CSV file.
	 * @param orderable
	 * @param partnerConn
	 * @param enterConn
	 * @param logFilePath
	 * @param apexFolder
	 * @param payorAccountId
	 * @param hcoAccountId
	 * @param hcpContactId
	 * @param patientId
	 * @return
	 */
	public static String createDefaultPCDxDraftOrderRecord(PartnerConnection partnerConn, String logFilePath, String apexFolder, String url, String username, String password, EnterpriseConnection enterConn) throws Exception {

		String orderId = "";
		//Create default order data
		String defaultPCDxDraftOrderFilePath = apexFolder + "\\" +"Default_PCDxDraftOrder_Create.json";			
		//String draftOrderScript = JSONFileReader.getScript(defaultPCDxDraftOrderFilePath, "Order", logFilePath, enterConn);	
		String draftOrderScript = XMLAndJSONRequestHandler.readFile(defaultPCDxDraftOrderFilePath, logFilePath);	
		

		if(draftOrderScript !=null && !draftOrderScript.trim().equals("")) {
			String jsonFileName = "Default_PCDxDraftOrder_Create.json";

			Date date = new Date();
			long time = date.getTime();
			String timeStr = Long.toString(time);
			//String recordFixture = UnitTestFixtures.getCreateRecordFixture("", timeStr, "",jsonFileName);
			//recordFixture = recordFixture.replace("insert record1", "record1.OSM_Product__c = '"+orderable+"';"+"record1.accountid = '"+payorAccountId+"';record1.OSM_Patient__c='"+patientId+"';insert record1");
			orderId = XMLAndJSONRequestHandler.postJsonRequest(url, defaultPCDxDraftOrderFilePath, username, password, logFilePath, enterConn);
			//ApexUtil.execute(partnerConn, recordFixture,  logFilePath);	
			//orderId = getId(enterConn, timeStr, logFilePath);		
			SFUtility.log("Order id:::::::::::::::::::::::::::::::::::::::"+orderId,  logFilePath);

		}
		else {
			//System.out.println("Missing default data file : Default_Order_Create.csv" );
			SFUtility.log("Default JSON file missing : Default_PCDxDraftOrder_Create.json", logFilePath);
			SFUtility.log("Please verify JSON file and run again..",logFilePath);
			System.exit(1);
		}

		return orderId;

	}



	/**
	 * Method to get Id using unique time value.
	 * @param enterConn
	 * @param timeStr
	 * @param logFilePath
	 * @return
	 */  
	public static String getId(EnterpriseConnection enterConn, String timeStr, String logFilePath) { 

		//String soql = "select id from order limit 1";
		String recordId = "";
		String soql = "Select Record_Id__c from Unit_Test_Result__c where Time_Stamp__c = '"+timeStr+"'";  
		List<SObject> objList = SOAPUtil.executeQuery(enterConn, soql, logFilePath);
		SFUtility.log("list size......................."+objList.size(),  logFilePath);
		if(objList !=null && objList.size()==1) {
			SObject obj = objList.get(0);
			//SFUtility.log("object:::::::::::::::::::::::::::::::::::::::"+obj,  LOG_FILE_PATH);			
			recordId = ((Unit_Test_Result__c)obj).getRecord_Id__c();
			//SFUtility.log("record id:::::::::::::::::::::::::::::::::::::::"+recordId,  logFilePath);
		}

		return recordId;
	}



	/**
	 * Method to get user id from user name.
	 * @param enterConn
	 * @param username
	 * @param logFilePath
	 * @return
	 */
	public static String getUserIdFromUserName(EnterpriseConnection enterConn, String username, String logFilePath) {

		//String soql = "select id from order limit 1";
		String recordId = "";
		System.out.println("getting user id");
		String soql = "Select id from User where username = "+username; 
		List<SObject> objList = SOAPUtil.executeQuery(enterConn, soql, logFilePath);
		SFUtility.log("list size......................."+objList.size(),  logFilePath);
		if(objList !=null && objList.size()==1) {

			//System.out.println("got user id.....................");
			SObject obj = objList.get(0);
			//SFUtility.log("object:::::::::::::::::::::::::::::::::::::::"+obj,  LOG_FILE_PATH);
			recordId = ((User)obj).getId();
			SFUtility.log("User record id:::::::::::::::::::::::::::::::::::::::"+recordId,  logFilePath);
		}

		System.out.println("user id:"+recordId);
		return recordId;
	}





	/*	public static String getOLIIdFromOrderOld(EnterpriseConnection enterConn, String orderId, String logFilePath) {

		String recordId = "";
		System.out.println("getting OLI id");
		String soql = "Select id from OrderItem where orderId = '"+orderId+"'"; 
		List<SObject> objList = SOAPUtil.executeQuery(enterConn, soql, logFilePath);
		SFUtility.log("list size......................."+objList.size(),  logFilePath);
		if(objList !=null && objList.size()==1) {

			System.out.println("got OLI id.....................");
			SObject obj = objList.get(0);			
			recordId = ((OrderItem)obj).getId();
			SFUtility.log("OLI record id:::::::::::::::::::::::::::::::::::::::"+recordId,  logFilePath);
		}

		System.out.println("OLI id:            "+recordId);
		return recordId;
	}*/



	public static String getOSMPatientIdFromId(EnterpriseConnection enterConn, String id, String logFilePath) {

		String osmPatientId = "";
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("getting patient id::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		String soql = "Select OSM_Patient_ID__c from Contact where id = '"+id+"'"; 

		//SFUtility.log("enterConn......................"+enterConn,  logFilePath);

		List<SObject> objList = SOAPUtil.executeQuery(enterConn, soql, logFilePath);
		//SFUtility.log("list size......................."+objList.size(),  logFilePath);
		if(objList !=null && objList.size()==1) {

			//System.out.println("got patient id.....................");
			SObject obj = objList.get(0);			
			osmPatientId = ((Contact)obj).getOSM_Patient_ID__c();
			//SFUtility.log("patient id:::::::::::::::::::::::::::::::::::::::"+recordId,  logFilePath);
		}

		//System.out.println("osmPatientId:            "+osmPatientId);
		return osmPatientId;
	}




	/**
	 * Method to get OLI Id from order
	 * @param enterConn
	 * @param orderId
	 * @param logFilePath
	 * @return
	 */
	public static String getOLIIdListFromOrder(EnterpriseConnection enterConn, String orderId, String logFilePath) {

		String recordId = "";
		SFUtility.log("getting OLI id", logFilePath);
		String soql = "Select id from OrderItem where orderId = '"+orderId+"'"; 
		List<SObject> objList = SOAPUtil.executeQuery(enterConn, soql, logFilePath);
		SFUtility.log("list size......................."+objList.size(),  logFilePath);
		if(objList !=null && objList.size()>0) {

			SFUtility.log("got OLI id.....................", logFilePath);

			for(SObject obj : objList) {
				//SObject obj = objList.get(0);	
				if(!recordId.equals("")) {
					recordId = recordId + "," + ((OrderItem)obj).getId();
				} else {
					recordId = ((OrderItem)obj).getId();
				}


				SFUtility.log("OLI record id list:::::::::::::::::::::::::::::::::::::::"+recordId,  logFilePath);	
			}

		}

		SFUtility.log("OLI id list:"+recordId, logFilePath);
		return recordId;
	}


	/*	public static void updatePatientOld(PartnerConnection partnerConn, String patientId, String gender, String logFilePath) {

		String script = "Contact patient = [Select OSM_Gender__c from contact where id  = '"+patientId+"'];" + 
				"patient.OSM_Gender__c = '"+gender+"'; update patient;";
		ApexUtil.execute(partnerConn, script,  logFilePath);

	}*/





	/**
	 * Method to get Order number from order id
	 * @param enterConn
	 * @param orderId
	 * @param logFilePath
	 * @return
	 */
	public static String getOrderNumberFromOrderId(EnterpriseConnection enterConn, String orderId, String logFilePath) {

		String orderNumber = "";
		System.out.println("getting order number");
		String soql = "Select orderNumber from Order where Id = '"+orderId+"'"; 
		List<SObject> objList = SOAPUtil.executeQuery(enterConn, soql, logFilePath);		
		SFUtility.log("list size......................."+objList.size(),  logFilePath);
		if(objList !=null && objList.size()==1) {

			System.out.println("got order number.....................");
			SObject obj = objList.get(0);			
			orderNumber = ((Order)obj).getOrderNumber();
			//SFUtility.log("patient id:::::::::::::::::::::::::::::::::::::::"+recordId,  logFilePath);
		}

		System.out.println("order number:"+orderNumber);
		return orderNumber;
	}
	
	
	/**
	 * Method to get OLI Id from order number
	 * @param enterConn
	 * @param orderNumber
	 * @param logFilePath
	 * @return
	 */
	public static String getOrderIdFromOrderNumber(EnterpriseConnection enterConn, String orderNumber, String logFilePath) {

		String orderId = "";
		System.out.println("getting order Id...");
		String soql = "Select id from Order where orderNumber = '"+orderNumber+"'"; 
		List<SObject> objList = SOAPUtil.executeQuery(enterConn, soql, logFilePath);		
		SFUtility.log("list size......................."+objList.size(),  logFilePath);
		if(objList !=null && objList.size()==1) {

			System.out.println("got order id.....................");
			SObject obj = objList.get(0);			
			orderId = ((Order)obj).getId();
			//SFUtility.log("patient id:::::::::::::::::::::::::::::::::::::::"+recordId,  logFilePath);
		}

		System.out.println("order Id:::::"+orderId);
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
			if(objList1.size()!=0)
			{
				//System.out.println("record existsss...............");	
				return executionid;
			}
			else {
				System.out.println("Record Doesn't exist...............");	
				return "Record Doesn't exist";
			}
		}
		else {
			System.out.println("Record Doesn't exist...............");
		}

		return null;
	}
	
	/**
	 * Method to get Id using unique time value.
	 * @param enterConn
	 * @param timeStr
	 * @param logFilePath
	 * @return
	 */  
	public static String getRecordTypeIdFromName(EnterpriseConnection enterConn, String soql, String logFilePath) { 

		String recordId = "";		  
		List<SObject> objList = SOAPUtil.executeQuery(enterConn, soql, logFilePath);
		SFUtility.log("list size......................."+objList.size(),  logFilePath);
		if(objList !=null && objList.size()==1) {
			SObject obj = objList.get(0);
			//SFUtility.log("object:::::::::::::::::::::::::::::::::::::::"+obj,  LOG_FILE_PATH);			
			recordId = ((RecordType)obj).getId();
			//SFUtility.log("record id:::::::::::::::::::::::::::::::::::::::"+recordId,  logFilePath);
		}

		return recordId;
	}



	/**
	 * Method to get Id using unique time value.
	 * @param enterConn
	 * @param timeStr
	 * @param logFilePath
	 * @return
	 */  
	public static String getOSMBillingDiagnosisCodeIdFromName(EnterpriseConnection enterConn, String soql, String logFilePath) { 

		String osmBillingDiagnosisCodeId = "";		  
		List<SObject> objList = SOAPUtil.executeQuery(enterConn, soql, logFilePath);
		SFUtility.log("list size......................."+objList.size(),  logFilePath);
		if(objList !=null && objList.size()==1) {
			SObject obj = objList.get(0);
			//SFUtility.log("object:::::::::::::::::::::::::::::::::::::::"+obj,  LOG_FILE_PATH);			
			osmBillingDiagnosisCodeId = ((OSM_Billing_Diagnosis_Code__c)obj).getId();
			//SFUtility.log("record id:::::::::::::::::::::::::::::::::::::::"+recordId,  logFilePath);
		}

		return osmBillingDiagnosisCodeId;
	}

}