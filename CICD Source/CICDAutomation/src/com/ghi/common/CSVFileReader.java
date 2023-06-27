package com.ghi.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.ghi.data.SOAPUtil;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.sobject.OSM_Billing_Diagnosis_Code__c;
import com.sforce.soap.enterprise.sobject.RecordType;
import com.sforce.soap.enterprise.sobject.SObject;

public class CSVFileReader {


	/**
	 * Method to create apex script from given CSV file data. 
	 * @param filePath
	 * @param objectName
	 * @param logFilePath
	 * @return
	 */
	public static String getScript(String filePath, String objectName, String logFilePath, EnterpriseConnection enterConn) {	

		String script = "";
		CSVParser csvFileParser = null;
		try {
			Path path = Paths.get(filePath); 
			if (Files.exists(path)) {
				//SFUtility.log("Using default file:" + defaultFilePath, logFilePath);
				SFUtility.log("File exists:" + filePath, logFilePath);

				//path = Paths.get(defaultFilePath);  
				//}

				BufferedReader reader = Files.newBufferedReader(path);
				csvFileParser = new CSVParser(reader, CSVFormat.DEFAULT); 

				List<CSVRecord> csvRecords = csvFileParser.getRecords();
				List<String> fieldList = new ArrayList<String>();


				if(csvRecords != null && csvRecords.size() > 1) {
					CSVRecord header = (CSVRecord) csvRecords.get(0);
					for(Iterator<String> itr = header.iterator(); itr.hasNext(); ) {
						fieldList.add(itr.next().trim());
					}

					for(int i = 1; i < csvRecords.size(); i++) {
						String myScript = objectName + " record"+i+" = new "+objectName+"(); ";
						CSVRecord csvRecord = (CSVRecord) csvRecords.get(i); 

						for(int j = 0; j<csvRecord.size(); j++) {

							String fieldValue = csvRecord.get(j).trim();

							if(fieldList.get(j).equalsIgnoreCase("RecordTypeId")) {

								String soqlQry = "SELECT Id FROM RecordType WHERE SobjectType = '"+objectName+"' and Name="+fieldValue+" LIMIT 1"; 
								String recordTypeId = SFDataFactory.getRecordTypeIdFromName(enterConn, soqlQry, logFilePath);



								myScript = myScript + " record"+i+"."+fieldList.get(j) + " = '"+ recordTypeId + "';";
							} else if(fieldList.get(j).equalsIgnoreCase("OSM_Billing_Diagnosis_Code__c")) {

								String soqlQry = "SELECT Id FROM OSM_Billing_Diagnosis_Code__c WHERE Name="+fieldValue+" LIMIT 1"; 
								String billingDiagnosisCodeId = SFDataFactory.getOSMBillingDiagnosisCodeIdFromName(enterConn, soqlQry, logFilePath);

								myScript = myScript + " record"+i+"."+fieldList.get(j) + " = '"+ billingDiagnosisCodeId + "';";
							} else {
								myScript = myScript + " record"+i+"."+fieldList.get(j) + " = "+ fieldValue + ";";	
							}


						}

						myScript = myScript + " insert record"+i+"; Id recordId = record"+i+".id;";
						script = script + myScript;
					}
				}
				//System.out.println("script: "+script);
			}

		} catch (FileNotFoundException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
			//e.printStackTrace();
		} catch (IOException e) {

			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
			//e.printStackTrace();
		} catch (Exception e) {

			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
			//e.printStackTrace();
		} finally {
			try {

				if(csvFileParser!=null && !csvFileParser.isClosed()) {
					csvFileParser.close();
				}

			} catch (IOException e) {
				SFUtility.log("Exception: "+e.getMessage(), logFilePath);
				SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
				//e.printStackTrace();
			}
		}


		return script;
	}




	/**
	 * Method to create apex script from given CSV file data for update operations. 
	 * @param filePath
	 * @param objectName
	 * @param id
	 * @param logFilePath
	 * @return
	 */
	public static String getScriptForUpdate(String filePath, String objectName, String id, String logFilePath, EnterpriseConnection enterConn, String orderableClass) {	

		String script = "";
		CSVParser csvFileParser = null;
		try {
			Path path = Paths.get(filePath); 
			if (Files.exists(path)) {
				//SFUtility.log("Using default file:" + defaultFilePath, logFilePath);
				SFUtility.log("File exists:" + filePath, logFilePath);

				//path = Paths.get(defaultFilePath);  
				//}

				BufferedReader reader = Files.newBufferedReader(path);
				csvFileParser = new CSVParser(reader, CSVFormat.DEFAULT);

				List<CSVRecord> csvRecords = csvFileParser.getRecords();
				List<String> fieldList = new ArrayList<String>();


				String myScript = objectName +" " + "record1" + " = [SELECT ";

				if(csvRecords != null && csvRecords.size() > 1) {
					CSVRecord header = (CSVRecord) csvRecords.get(0);
					for(Iterator<String> itr = header.iterator(); itr.hasNext(); ) {
						String fieldName = itr.next().trim();
						fieldList.add(fieldName);

						if(fieldList.size()< header.size() ) {
							myScript = myScript + fieldName + ", ";	
						} else {
							myScript = myScript + fieldName + " from "+ objectName+" where id ='"+id+"'];";
						}

					}

					for(int i = 1; i < csvRecords.size(); i++) {

						CSVRecord csvRecord = (CSVRecord) csvRecords.get(i);

						for(int j = 0; j<csvRecord.size(); j++) {

							String fieldValue = csvRecord.get(j).trim();


							if(fieldList.get(j).equalsIgnoreCase("RecordTypeId")) {

								String soqlQry = "SELECT Id FROM RecordType WHERE SobjectType = '"+objectName+"' and Name="+fieldValue+" LIMIT 1"; 
								String recordTypeId = SFDataFactory.getRecordTypeIdFromName(enterConn, soqlQry, logFilePath);



								myScript = myScript + " record"+i+"."+fieldList.get(j) + " = '"+ recordTypeId + "';";
							} else if(fieldList.get(j).equalsIgnoreCase("OSM_Billing_Diagnosis_Code__c")) {
								String soqlQry = "";
								if(orderableClass != null && !orderableClass.trim().equals("")) {
									orderableClass = orderableClass.trim();
									if(orderableClass.toUpperCase().contains("IHC")) {
										soqlQry = "SELECT Id FROM OSM_Billing_Diagnosis_Code__c WHERE Name="+fieldValue+" AND OSM_Orderable__c ='PCDx IHC Only' LIMIT 1"; 

									} else if(orderableClass.toUpperCase().contains("MOLECULAR")) {
										soqlQry = "SELECT Id FROM OSM_Billing_Diagnosis_Code__c WHERE Name="+fieldValue+" AND OSM_Orderable__c ='PCDx Molecular Only' LIMIT 1"; 

									} else if(orderableClass.toUpperCase().contains("FULL")) {
										soqlQry = "SELECT Id FROM OSM_Billing_Diagnosis_Code__c WHERE Name="+fieldValue+" AND OSM_Orderable__c ='PCDx Full' LIMIT 1"; 

									}

								}

								if(soqlQry.equals("")) {
									soqlQry = "SELECT Id FROM OSM_Billing_Diagnosis_Code__c WHERE Name="+fieldValue+" LIMIT 1"; 									
								}
								String billingDiagnosisCodeId = SFDataFactory.getOSMBillingDiagnosisCodeIdFromName(enterConn, soqlQry, logFilePath);

								myScript = myScript + " record"+i+"."+fieldList.get(j) + " = '"+ billingDiagnosisCodeId + "';";
							} else {
								myScript = myScript + " record"+i+"."+fieldList.get(j) + " = "+ fieldValue + ";";
							}

						}

						myScript = myScript + " update record"+i+";";
						script = script + myScript;
					}
				}
				//System.out.println("script: "+script);
			}

		} catch (FileNotFoundException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
			//e.printStackTrace();
		} catch (IOException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
			//e.printStackTrace();
		} catch (Exception e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
			//e.printStackTrace();
		} finally {
			try {
				if(csvFileParser!=null && !csvFileParser.isClosed()) {
					csvFileParser.close();
				}

			} catch (IOException e) {
				SFUtility.log("Exception: "+e.getMessage(), logFilePath);
				SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
				//e.printStackTrace();
			}
		}
		SFUtility.log("script: "+script, logFilePath);

		return script;
	}




	/**
	 * Method to get user name from user CSV file.
	 * @param filePath
	 * @param objectName
	 * @param logFilePath
	 * @return
	 */
	public static String getUserNameFromUserScript(String filePath, String objectName, String logFilePath) {		
		CSVParser csvFileParser = null;
		try {
			Path path = Paths.get(filePath); 
			if (Files.exists(path)) {
				SFUtility.log("User File exists:" + filePath, logFilePath);
				BufferedReader reader = Files.newBufferedReader(path);
				csvFileParser = new CSVParser(reader, CSVFormat.DEFAULT);

				List<CSVRecord> csvRecords = csvFileParser.getRecords();
				List<String> fieldList = new ArrayList<String>();


				if(csvRecords != null && csvRecords.size() > 1) {
					CSVRecord header = (CSVRecord) csvRecords.get(0);
					for(Iterator<String> itr = header.iterator(); itr.hasNext(); ) {
						fieldList.add(itr.next().trim());
					}

					for(int i = 1; i < csvRecords.size(); i++) {						
						CSVRecord csvRecord = (CSVRecord) csvRecords.get(i);
						for(int j = 0; j<csvRecord.size(); j++) {
							String fieldValue = csvRecord.get(j).trim();					

							if(fieldList.get(j)!=null && fieldList.get(j).equalsIgnoreCase("username")) {
								System.out.println("username found !! : "+fieldValue);
								return fieldValue;
							}
						}
					}
				}
			}

		} catch (FileNotFoundException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
			//e.printStackTrace();
		} catch (IOException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
			//e.printStackTrace();
		} catch (Exception e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
			//e.printStackTrace();
		} finally {
			try {
				if(csvFileParser!=null && !csvFileParser.isClosed()) {
					csvFileParser.close();
				}

			} catch (IOException e) {
				SFUtility.log("Exception: "+e.getMessage(), logFilePath);
				SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
				//e.printStackTrace();
			}
		}
		return "";
	}


}