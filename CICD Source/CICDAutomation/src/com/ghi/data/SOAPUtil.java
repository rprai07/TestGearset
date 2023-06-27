package com.ghi.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.ghi.common.SFUtility;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.APXTConga4__Conga_Template__c;
import com.sforce.soap.enterprise.sobject.Attachment;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig; 

public class SOAPUtil {
	private static String authEndPoint = "";
	private static EnterpriseConnection connection;
	private static final String CONGA_ATTACHMENTS_FILE = "\\conga_attachments.properties";
	private static final String SETTINGS_FILE = "\\settings.properties";	

	public static EnterpriseConnection login(String workingDir, String username, String password, String logFilePath, String environment) {
		FileReader reader = null;
		try {
			reader = new FileReader(workingDir + SETTINGS_FILE); 
			Properties p = new Properties();  
			p.load(reader);  
			
			if(environment!=null && environment.trim().equalsIgnoreCase("sandbox")) {
				authEndPoint = p.getProperty("sandbox_enterpriseUrl");
			} else if (environment!=null && environment.trim().equalsIgnoreCase("production")) {
				authEndPoint = p.getProperty("production_enterpriseUrl");
			}			
			
			SFUtility.log("User Name: "+ username, logFilePath);   
			SFUtility.log("AuthEndPoint: "+authEndPoint, logFilePath);

		} catch (FileNotFoundException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			System.exit(1);
		} catch (IOException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			System.exit(1);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			}
		}

		boolean success = false;
		try {
			ConnectorConfig config = new ConnectorConfig();
			config.setUsername(username);
			config.setPassword(password);

			//SFUtility.log("AuthEndPoint: " + authEndPoint);
			config.setAuthEndpoint(authEndPoint);
			connection = new EnterpriseConnection(config);
			success = true;
			SFUtility.log("login successfully to SFDC... !!", logFilePath);
		} catch (ConnectionException ce) {
			//SFUtility.log("Exception: "+ce.getMessage(), logFilePath);
			SFUtility.log("Please verify your URL and credentials and try again..", logFilePath); 
			System.exit(1);
		}		
		return connection;
	}


	
	public static List<SObject> executeQuery(EnterpriseConnection connection, String query, String logFilePath) {
		SFUtility.log("executing query..:::: " + query, logFilePath);
		List<SObject> sobjectList = new ArrayList<SObject>();
		try {			
			QueryResult qr = connection.query(query);
			//SFUtility.log("2.......", logFilePath);
			boolean done = false;
			if (qr.getSize() > 0) {
				//SFUtility.log("\nLogged-in user can see " + qr.getRecords().length + " records.", logFilePath);
		
				while (!done) {						
					SObject[] records = qr.getRecords();
					for (int i = 0; i < records.length; ++i) {						
						SObject record = records[i];
						sobjectList.add(record);						
					}
					
					if (qr.isDone()) {
						done = true;
					} else {
						qr = connection.queryMore(qr.getQueryLocator());
					}
				}
			} else {
				SFUtility.log("No record found.", logFilePath);
			}
		} catch (Exception ex) {
			SFUtility.log("Exception ::::::::::::::: "+ex.getMessage(), logFilePath);
			System.exit(1);
		}
		
		return sobjectList;
	}
	
	public static List<com.sforce.soap.partner.sobject.SObject> executeQuery(PartnerConnection connection, String query, String logFilePath) {
		SFUtility.log("executing query..:::: " + query, logFilePath);
		List<com.sforce.soap.partner.sobject.SObject> sobjectList = new ArrayList<com.sforce.soap.partner.sobject.SObject>();
		try {			
			com.sforce.soap.partner.QueryResult qr = connection.query(query);
			//SFUtility.log("2.......", logFilePath);
			boolean done = false;
			if (qr.getSize() > 0) {
				//SFUtility.log("\nLogged-in user can see " + qr.getRecords().length + " records.", logFilePath);
		
				while (!done) {						
					com.sforce.soap.partner.sobject.SObject[] records = qr.getRecords();
					for (int i = 0; i < records.length; ++i) {						
						com.sforce.soap.partner.sobject.SObject record = records[i];
						sobjectList.add(record);						
					}
					
					if (qr.isDone()) {
						done = true;
					} else {
						qr = connection.queryMore(qr.getQueryLocator());
					}
				}
			} else {
				SFUtility.log("No record found.", logFilePath);
			}
		} catch (Exception ex) {
			SFUtility.log("Exception ::::::::::::::: "+ex.getMessage(), logFilePath);
			System.exit(1);
		}
		
		return sobjectList;
	}

/*	public static void executeQueries(EnterpriseConnection enterConn, String folderPath, String logFilePath) {
		List<String> queries = SOAPUtil.getQueries(folderPath, logFilePath);
		for(String qry : queries) {
			SFUtility.log("qry: "+qry, logFilePath);
			SOAPUtil.executeQuery(enterConn, qry, logFilePath);
		}

	}*/

	public static String createRecord(SObject sobject, String logFilePath) {

		String status = "Passed";
		SFUtility.log("Going to create new record...", logFilePath);
		// Invoke the update call and save the results
		try {
			SaveResult[] saveResults = connection.create(new SObject[] {sobject});
			for (SaveResult saveResult : saveResults) {
				if (saveResult.isSuccess()) {
					SFUtility.log("Successfully created Record ID: "
							+ saveResult.getId(), logFilePath);
				} else {
					// Handle the errors.
					// We just print the first error out for sample purposes.
					com.sforce.soap.enterprise.Error[] errors = saveResult.getErrors();
					if (errors.length > 0) {
						SFUtility.log("\tThe error reported was: ("
								+ errors[0].getStatusCode() + ") "
								+ errors[0].getMessage() + ".", logFilePath);
						status = "Creation Failed\nReason: " + errors[0].getMessage();

					}
				}
			}
		} catch (ConnectionException ce) {
			SFUtility.log("Exception: "+ce.getMessage(), logFilePath);
			status = "Creation Failed\nReason: " + ce.getMessage();
		}		
		return status;
	}



	public static String updateRecord(SObject sobject, String logFilePath) {		
		String status = "Passed";		
		// Invoke the update call and save the results
		try {

			SaveResult[] saveResults = connection.update(new SObject[] {sobject});
			for (SaveResult saveResult : saveResults) {
				if (saveResult.isSuccess()) {
					SFUtility.log("Successfully updated Record ID: " + saveResult.getId(), logFilePath);
				} else {
					// Handle the errors.
					// We just print the first error out for sample purposes.
					com.sforce.soap.enterprise.Error[] errors = saveResult.getErrors();
					if (errors.length > 0) {
						SFUtility.log("\tThe error reported was: ("
								+ errors[0].getStatusCode() + ") "
								+ errors[0].getMessage() + ".", logFilePath);
						status = "Update Failed\nReason: " + errors[0].getMessage();
					}
				}
			}
		} catch (ConnectionException ce) {
			SFUtility.log("Exception: "+ce.getMessage(), logFilePath);
			status = "Update Failed\nReason: " + ce.getMessage();
		}
		return status;
	}

	/*	public static void createCongaTemplate(String logFilePath) {
		APXTConga4__Conga_Template__c template = new APXTConga4__Conga_Template__c();

		template.setAPXTConga4__Name__c("Test2");
		template.setAPXTConga4__Template_Group__c("TestG");
		template.setAPXTConga4__Template_Type__c("Document");
		//QueryResult qr = template.getAttachments();		
		//template.setAttachments(qr);
		createRecord(template, logFilePath);
	}*/


	public static String createOrUpdateAttachment(String name, byte[] contents, String parentId, String attachmentId, String logFilePath) {
		String status = "";
		Attachment template = new Attachment();
		template.setBody(contents);

		if(attachmentId != null && attachmentId.trim().length()>0) {
			template.setId(attachmentId);
			SFUtility.log("Already there so updating attachment...", logFilePath);
			status = updateRecord(template, logFilePath);
		} else {
			SFUtility.log("Creating new attachment...", logFilePath);
			template.setName(name);
			template.setParentId(parentId);
			if(name.toUpperCase().endsWith(".PDF")) {
				template.setContentType("application/pdf");	
				
			} else if(name.toUpperCase().endsWith(".DOCX")) {
				template.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");	
			}
			
			status = createRecord(template, logFilePath);
		}

		return status;
	}


	public static void readCongaTemplates(EnterpriseConnection connection, String logFilePath) {
		String soqlQuery = "SELECT Id, APXTConga4__Name__c FROM APXTConga4__Conga_Template__c";	

		try {
			QueryResult qr = connection.query(soqlQuery);
			boolean done = false;

			if (qr.getSize() > 0) {
				SFUtility.log("\nLogged-in user can see " + qr.getRecords().length + " Conga Template records.", logFilePath);

				while (!done) {					
					SObject[] records = qr.getRecords();
					for (int i = 0; i < records.length; ++i) {
						APXTConga4__Conga_Template__c record = (APXTConga4__Conga_Template__c) records[i];
						String fName = record.getAPXTConga4__Name__c();

						SFUtility.log("Conga Record::  " + (i + 1) + ": " + fName, logFilePath);							

					}

					if (qr.isDone()) {
						done = true;
					} else {
						qr = connection.queryMore(qr.getQueryLocator());
					}
				}
			} else {
				SFUtility.log("No record found.", logFilePath);
			}
		} catch (ConnectionException ce) {
			SFUtility.log("Exception: "+ce.getMessage(), logFilePath);
		}
	}


	public static void readCongaTemplate(EnterpriseConnection connection, String templateName, String logFilePath) {
		
		String soqlQuery = "SELECT Id, APXTConga4__Name__c FROM APXTConga4__Conga_Template__c where APXTConga4__Name__c = '"+templateName+"'";	

		/*		String soqlQuery = 	"select Name, Body, ContentType, parentid  from attachment where name like '%Prostate_SOMN_Order.pdf%' "
		+ "and parentid in (SELECT Id FROM APXTConga4__Conga_Template__c where APXTConga4__Name__c = '"+templateName+"'";	*/

		try {

			QueryResult qr = connection.query(soqlQuery);
			boolean done = false;

			if (qr.getSize() > 0) {
				while (!done) {					
					SObject[] records = qr.getRecords();
					for (int i = 0; i < records.length; ++i) {
						APXTConga4__Conga_Template__c record = (APXTConga4__Conga_Template__c) records[i];
						String fName = record.getAPXTConga4__Name__c();
						SFUtility.log("Conga Record::  " + (i + 1) + ": " + fName, logFilePath);

						QueryResult qr1 = record.getNotesAndAttachments();
						SFUtility.log("qr1: "+qr1, logFilePath);
						SObject so[] = qr1.getRecords();
						for( SObject obj : so) {								
							SFUtility.log("obj: "+obj, logFilePath);
						}							
					}

					if (qr.isDone()) {
						done = true;
					} else {
						qr = connection.queryMore(qr.getQueryLocator());
					}
				}
			} else {
				SFUtility.log("No record found.", logFilePath);
			}
		} catch (ConnectionException ce) {
			SFUtility.log("Exception: "+ce.getMessage(), logFilePath);
		}
	}




/*	public static void readAttachment(EnterpriseConnection connection, String templateName, String logFilePath) {
		

		//String soqlQuery = "SELECT Id, APXTConga4__Name__c FROM APXTConga4__Conga_Template__c where APXTConga4__Name__c = '"+templateName+"'";	

		String soqlQuery = 	"select Name, Body, ContentType, parentid  from attachment where name like '%Prostate_SOMN_Order.pdf%' "
				+ "and parentid in (SELECT Id FROM APXTConga4__Conga_Template__c where APXTConga4__Name__c = '"+templateName+"')";	
		
		SFUtility.log("executing query..::" + soqlQuery, logFilePath);

		try {

			//SFUtility.log("connection: "+connection, logFilePath);
			QueryResult qr = connection.query(soqlQuery);
			//SFUtility.log("qr: "+qr);
			boolean done = false;

			if (qr.getSize() > 0) {
				//SFUtility.log("\nLogged-in user can see this record.", logFilePath);
				while (!done) {
					//SFUtility.log("reading attachment...", logFilePath);
					SObject[] records = qr.getRecords();
					for (int i = 0; i < records.length; ++i) {
						Attachment record = (Attachment) records[i];
						SFUtility.log("attachment record: "+record, logFilePath);
						byte contents[] = record.getBody();
						SFUtility.log("Contents::  " + (i + 1) + ": " + contents, logFilePath);
						writeFile("Prostate_SOMN_Order.pdf", contents, logFilePath);

						//QueryResult qr1 = record.getNotesAndAttachments();
						//SFUtility.log("qr1: "+qr1);
						//SObject so[] = qr1.getRecords();
						for( SObject obj : so) {								
								SFUtility.log("obj: "+obj);
							}							
					}

					if (qr.isDone()) {
						done = true;
					} else {
						qr = connection.queryMore(qr.getQueryLocator());
					}
				}
			} else {
				SFUtility.log("No record found.", logFilePath);
			}
		} catch (ConnectionException ce) {
			SFUtility.log("Exception: "+ce.getMessage(), logFilePath);
		}
	}*/

	public static void writeFile(String path, byte[] contents, String logFilePath) {		
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(path);
			stream.write(contents);
		} catch (IOException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			}
		}		
	}

	public static byte[] readFile(String path, String logFilePath) {
		byte[] contents = null;
		FileInputStream fileInputStream = null;
		try {
			File file = new File(path);
			fileInputStream = new FileInputStream(file);

			contents = new byte[(int) file.length()];

			fileInputStream.read(contents);

		} catch (FileNotFoundException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
		} catch (IOException e1) {
			SFUtility.log("Exception: "+e1.getMessage(), logFilePath);
		} finally {
			try {
				if(fileInputStream != null) {
					fileInputStream.close();
				}				
			} catch (IOException e) {
				SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			}
		}
		return contents;
	}


	public static List<String> getQueries(String folderPath, String logFilePath) {
		List<String> qryStrList = new ArrayList<String>();
		BufferedReader br = null;
		try {
			File qryFolder = new File(folderPath);
			SFUtility.log("qryFolder: "+qryFolder, logFilePath);			

			if(qryFolder != null && qryFolder.isDirectory()) {
				SFUtility.log("Found query folder !!", logFilePath);
				File qryFiles[] = qryFolder.listFiles();

				List<File> sortedFiles = SFUtility.sortFiles(qryFiles, logFilePath);
				if(sortedFiles != null && sortedFiles.size() > 0) {

					for(File qryFile :  sortedFiles) {
						SFUtility.log("file name: "+ qryFile.getName(), logFilePath);
						br = new BufferedReader(new FileReader(qryFile));
						String str;
						while ((str = br.readLine()) != null) {							
							qryStrList.add(str.trim());
						}
					}
				}
			}			
		} catch (IOException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
		} finally {
			if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					SFUtility.log("Exception: "+e.getMessage(), logFilePath);
				}
			}			
		}		
		return qryStrList;
	}



	/*public static Map<String, List<String>> getCongaAttachments_old(String logFilePath) {
		Map<String, List<String>> templateAttachmentsMap = new HashMap<String, List<String>>();
		FileReader reader = null;
		try {
			reader = new FileReader(CONGA_ATTACHMENTS_FILE);
			Properties p = new Properties();  
			p.load(reader);   

			Set<Object> keySet  =  p.keySet();
			for (Object key : keySet) {				
				String attachments = (String) p.get(key);
				if(attachments != null && attachments.trim().length()>0) {

					String attachArr[] = attachments.split(",");
					List<String> docs = templateAttachmentsMap.get(key);
					if(docs == null) {
						SFUtility.log("creating new list..", logFilePath);
						docs = new ArrayList<String>();						
						docs.addAll(Arrays.asList(attachArr));
					} else {
						SFUtility.log("updating list..", logFilePath);
						docs.addAll(Arrays.asList(attachArr));
					}
					templateAttachmentsMap.put(key.toString(), docs);					
				}				
			}			
		} catch (FileNotFoundException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
		} catch (IOException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			}
		}
		return templateAttachmentsMap;
	}*/

	public static Map<String, List<String>> getCongaAttachments(String workingDir, String logFilePath, String sequence) {
		Map<String, List<String>> templateAttachmentsMap = new HashMap<String, List<String>>();
		FileReader reader = null;
		BufferedReader br = null;
		try {
			if(sequence != null) {
				sequence = sequence.trim();
				reader = new FileReader(workingDir + CONGA_ATTACHMENTS_FILE);
				br = new BufferedReader(reader);
				String str;

				if(sequence.equalsIgnoreCase("FULL")) {
					SFUtility.log("Conga attachment full load..", logFilePath);

					while ((str = br.readLine()) != null) {						
						
						//Getting mapping details excluding comments						
						if(!str.trim().startsWith("#")) {
							String templateAttachments[] = str.split(":");
							if(templateAttachments != null && templateAttachments.length == 3) {
								String nameOfTemplate = templateAttachments[1].trim();
								String attachments = templateAttachments[2].trim();
								SFUtility.log("Name Of Template: "+nameOfTemplate, logFilePath);					
								//Full Build							
								if(attachments.length() > 0) {
									String attachArr[] = attachments.split(",");
									List<String> docs = templateAttachmentsMap.get(nameOfTemplate);
									if(docs == null) {
										SFUtility.log("creating new list..", logFilePath);
										docs = new ArrayList<String>();						
										docs.addAll(Arrays.asList(attachArr));
									} else {
										SFUtility.log("updating list..", logFilePath);
										docs.addAll(Arrays.asList(attachArr));
									}
									templateAttachmentsMap.put(nameOfTemplate, docs);					
								}
							}
						}
					}
				} else {

					SFUtility.log("Conga attachment incremental load..", logFilePath);				
					List<Integer> incrementalStepsList = SFUtility.getIncrementalSteps(sequence, logFilePath);	

					while ((str = br.readLine()) != null) {

						//Getting mapping details excluding comments						
						if(!str.trim().startsWith("#")) {							
							String templateAttachments[] = str.split(":");
							if(templateAttachments != null && templateAttachments.length == 3) {
								String stepNumber = templateAttachments[0].trim();					
								String nameOfTemplate = templateAttachments[1].trim();
								String attachments = templateAttachments[2].trim();				

								int seq = Integer.parseInt(stepNumber); 						
								if(incrementalStepsList.contains(seq)) {								
									SFUtility.log("Name Of Template: "+nameOfTemplate, logFilePath);	
									if(attachments.length() > 0) {
										String attachArr[] = attachments.split(",");
										List<String> docs = templateAttachmentsMap.get(nameOfTemplate);
										if(docs == null) {
											SFUtility.log("creating new list..", logFilePath);
											docs = new ArrayList<String>();						
											docs.addAll(Arrays.asList(attachArr));
										} else {
											SFUtility.log("updating list..", logFilePath);
											docs.addAll(Arrays.asList(attachArr));
										}
										templateAttachmentsMap.put(nameOfTemplate, docs);					
									}

								} else {
									//SFUtility.log("Not part of Incremental build.."); 
								}

							} else {
								SFUtility.log("Please verify conga attachment properties file format !!", logFilePath);

							}
						}
					}
				}
			}

		} catch (FileNotFoundException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
		} catch (IOException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
		} finally {
			try {
				if(reader != null) {
					reader.close();
				}

			} catch (IOException e) {
				SFUtility.log("Exception::= "+e.getMessage(), logFilePath);
			}
		}
		return templateAttachmentsMap;
	}


	public static List<String> loadCongaAttachments(EnterpriseConnection connection, String docFolderPath, String workingDir, String logFilePath, String sequence) {
		SFUtility.log("Going to load conga attachments..", logFilePath);

		String failureSummary = "";
		String statusSummary = "";		
		List<String> executionSummary = new ArrayList<String>();

		File docsFolder  = new File(docFolderPath);
		if(docsFolder.exists()) {	
			Map<String, List<String>> templateAttachmentsMap = getCongaAttachments(workingDir, logFilePath, sequence);

			Set<String> keySet = templateAttachmentsMap.keySet();
			for(String nameOfTemplate : keySet) {
				List<String> docs =  templateAttachmentsMap.get(nameOfTemplate);
				String congaTemplateId = getIdOfCongaTemplate(connection, nameOfTemplate, logFilePath);
				if(congaTemplateId != null && congaTemplateId.trim().length() > 0) {
		
					for (String docName : docs) {
						byte[]  contents = SOAPUtil.readFile(docFolderPath + "/"+docName, logFilePath);

						if(contents != null) {
							String attachmentId = getAttachmentId(connection, nameOfTemplate, docName, logFilePath);
							String status = createOrUpdateAttachment(docName.trim(), contents, congaTemplateId,attachmentId, logFilePath);
							if(status.equals("Passed")) {
								statusSummary = statusSummary  + "\n" + "Conga Template: " + nameOfTemplate + ": " + docName +  ": Passed";								
							} else {
								failureSummary = failureSummary + "\n" + "Conga Template: " + nameOfTemplate + ": " + docName +  ": Failed\nReason: " + status;
							}
						} else {
							statusSummary = statusSummary  + "\n" + "Conga Template: " + nameOfTemplate + ": " + docName +  ": Failed";	
							failureSummary = failureSummary + "\n" + "Conga Template: " + nameOfTemplate + ": " + docName +  ": Failed\nReason: " + "File Not Found.";						
						}
					}
				} else {
					SFUtility.log("Could not find the template..", logFilePath);
					statusSummary = statusSummary  + "\n" + "Conga Template: " + nameOfTemplate + ": " + ": Failed";	
					failureSummary = failureSummary + "\n" + "Conga Template: " + nameOfTemplate + ": " + ": Failed\nReason: " + "File Not Found.";					
				
				}
			}
		} else {
			SFUtility.log("No docs folder found to upload pdfs..", logFilePath);
		}
		executionSummary.add(statusSummary);
		executionSummary.add(failureSummary);	
		return executionSummary;
	}


	public static String getAttachmentId(EnterpriseConnection connection, String templateName, String docName, String logFilePath) {
		String soqlQuery = 	"select Id  from attachment where name = '"+ docName
				+ "' and parentid in (SELECT Id FROM APXTConga4__Conga_Template__c where APXTConga4__Name__c = '"+templateName+"')";	

		SFUtility.log("soqlQuery::::::::::::"+soqlQuery, logFilePath);
		try {
			
			QueryResult qr = connection.query(soqlQuery); 

			if (qr.getSize() > 0) {
				SObject[] records = qr.getRecords();
				Attachment record = (Attachment) records[0];
				SFUtility.log("Attchment id:::::::::::::::::::::::::::::::::::::::"+record.getId(),  logFilePath);
				return record.getId();
			} else {
				SFUtility.log("No record found.", logFilePath);

			}
		} catch (ConnectionException ce) {
			SFUtility.log("Exception: "+ce.getMessage(), logFilePath); 
		}
		return null;
	}



	public static String getIdOfCongaTemplate(EnterpriseConnection connection, String nameOfTemplate, String logFilePath) {
		String soqlQuery = "SELECT Id FROM APXTConga4__Conga_Template__c where APXTConga4__Name__c = '" + nameOfTemplate + "'";	
		
		SFUtility.log("executing query..::" + soqlQuery, logFilePath);
		
		
		try {
			QueryResult qr = connection.query(soqlQuery);
			boolean done = false;
			if (qr.getSize() > 0) {
				while (!done) {
					SObject[] records = qr.getRecords();
					for (int i = 0; i < records.length; ++i) {
						APXTConga4__Conga_Template__c record = (APXTConga4__Conga_Template__c) records[i];
						String id = record.getId();
						SFUtility.log("Conga Template Record Id::  " + (i + 1) + ": " + id, logFilePath);
						return id;						
					}

					if (qr.isDone()) {
						done = true;
					} else {
						qr = connection.queryMore(qr.getQueryLocator());
					}
				}
			} else {
				SFUtility.log("No record found.", logFilePath);
			}
		} catch (ConnectionException ce) {
			SFUtility.log("Exception: "+ce.getMessage(), logFilePath);
		}
		return null;		
	}
}