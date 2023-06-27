package com.ghi.apex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ghi.buildautomation.RunOnceAndReRun;
import com.ghi.cicdlogs.CICDLogService;
import com.ghi.common.SFUtility;
import com.ghi.ut.UnitTestingUtils;
import com.sforce.soap.apex.Connector;
import com.sforce.soap.apex.ExecuteAnonymousResult;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class ApexUtil {
	private static String authEndPoint = "";
	private static final String SETTINGS_FILE = "\\settings.properties";

	public static Map<String, String> execute(PartnerConnection partnerConn, String code, String logFilePath) { 
		Map<String, String> stepExecutionStatusMap = new HashMap<String, String>();

		try {			
			if(partnerConn != null) {
				SFUtility.log("Going to execute apex code ! ", logFilePath);	
				SFUtility.log("Apex Code:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: "+code, logFilePath);	


				// Get an Apex stub
				ConnectorConfig apexCfg = new ConnectorConfig();
				apexCfg.setSessionId(partnerConn.getSessionHeader().getSessionId());

				apexCfg.setServiceEndpoint(partnerConn.getConfig().getServiceEndpoint().replace("/u/", "/s/"));
				com.sforce.soap.apex.SoapConnection apexSoapConn = Connector.newConnection(apexCfg); 
				ExecuteAnonymousResult result = apexSoapConn.executeAnonymous(code);

				if (result.isSuccess()) {  
					SFUtility.log("Successfully executed apex script for this step !!", logFilePath); 
					stepExecutionStatusMap.put("Passed", "Passed");
				} else {
					SFUtility.log("Exception: Please verify apex script and run again ...", logFilePath);
					SFUtility.log("Exception Message: " + result.getExceptionMessage(), logFilePath);
					SFUtility.log("Exception Stack Trace: " + result.getExceptionStackTrace(), logFilePath);

					if(result.isCompiled()) {
						stepExecutionStatusMap.put("Failed", result.getExceptionMessage());	
						UnitTestingUtils.addEnvProvMessage(result.getExceptionMessage());

					} else {

						String compileProblem = result.getCompileProblem();
						SFUtility.log("Compile Problem: " + compileProblem, logFilePath);						
						stepExecutionStatusMap.put("Failed", compileProblem);
						UnitTestingUtils.addEnvProvMessage(compileProblem);

					}					
				} 
			}			
		} catch (ConnectionException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
			UnitTestingUtils.addEnvProvMessage(e.getMessage());
			UnitTestingUtils.addEnvProvMessage(e.getCause().toString());			

			e.printStackTrace();
			System.exit(1);
		}		
		return stepExecutionStatusMap;
	}


	public static PartnerConnection login(String workingDir, String username, String password, String logFilePath, String environment) {
		PartnerConnection partnerConn = null;
		FileReader reader = null;
		try {
			reader = new FileReader(workingDir + SETTINGS_FILE);
			Properties p=new Properties();  
			p.load(reader);

			if(environment!=null && environment.trim().equalsIgnoreCase("sandbox")) {
				authEndPoint = p.getProperty("sandbox_partnerUrl");
			} else if (environment!=null && environment.trim().equalsIgnoreCase("production")) {
				authEndPoint = p.getProperty("production_partnerUrl");
			}

			SFUtility.log("User Name: "+ username, logFilePath);			 
			SFUtility.log("AuthEndPoint: "+authEndPoint, logFilePath);
			boolean success = false;
			try {
				ConnectorConfig config = new ConnectorConfig();
				config.setUsername(username);
				config.setPassword(password);				
				config.setAuthEndpoint(authEndPoint);
				partnerConn = new PartnerConnection(config);
				success = true;
				SFUtility.log("login successfully to SFDC... !!", logFilePath);
			} catch (ConnectionException ce) {
				SFUtility.log("Exception: "+ce.getMessage(), logFilePath);
			} 

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
		return partnerConn;
	}


	public static List<File> getApexScripts(String folderPath, String logFilePath) {
		List<File> sortedFiles = new ArrayList<File>();
		File apexFolder = new File(folderPath);
		SFUtility.log("apexFolder: "+apexFolder, logFilePath);			

		if(apexFolder != null && apexFolder.isDirectory()) {
			SFUtility.log("Found apex folder !!", logFilePath);
			File apexFiles[] = apexFolder.listFiles(new FileFilter() {

				@Override
				public boolean accept(File file) {
					String name = file.getName().toLowerCase();
					return name.endsWith(".apex") && file.isFile();
				}

			});
			sortedFiles = SFUtility.sortFiles(apexFiles, logFilePath);
		}	
		return sortedFiles;
	}	

	public static String getApexScriptFromFile(File apexFile, String logFilePath) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		SFUtility.log("file name: "+ apexFile.getName(), logFilePath);
		try {
			br = new BufferedReader(new FileReader(apexFile));
			String str;
			while ((str = br.readLine()) != null) {
				//SFUtility.log(str, logFilePath);
				sb.append(str.trim());
				sb.append("\n");
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
		return sb.toString(); 
	}

	public static String getTextFileContents(File file, String logFilePath) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		SFUtility.log("file name: "+ file.getName(), logFilePath);
		try {
			br = new BufferedReader(new FileReader(file));
			String str;
			while ((str = br.readLine()) != null) {
				//SFUtility.log(str, logFilePath);
				sb.append(str.trim());
				sb.append("\n");
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
		return sb.toString(); 
	}



	/*	public static void executeApexScripts(PartnerConnection partnerConn, String folderPath, String logFilePath) {
				List<File> apexScripts = getApexScripts(folderPath, logFilePath);
			for(File apexFile : apexScripts) {

				String apexScript = getApexScriptFromFile(apexFile, logFilePath);
				execute(partnerConn, apexScript, logFilePath);
			}

	}*/



	//public static List<String> executeApexScripts(EnterpriseConnection enterConn, PartnerConnection partnerConn, String folderPath, String logFilePath, String sequence, String executionSeriesReRunOfRunOnceScripts) {
	public static List<String> executeApexScripts(EnterpriseConnection enterConn, PartnerConnection partnerConn, String folderPath, String logFilePath, String sequence, String buildNumber) {
		String executionStatusSummary = "";
		String executionFailureSummary = "";
		List<String> executionSummary = new ArrayList<String>(); 
		String preOrPost = "";
		if(folderPath != null) {
			String folderPathUpper = folderPath.toUpperCase();
			if(folderPathUpper.contains("/PRE") || folderPathUpper.contains("\\PRE") ) {
				preOrPost = "PRE";
			} else if(folderPathUpper.contains("/POST") || folderPathUpper.contains("\\POST") ) {
				preOrPost = "POST";
			}			
		}

		if(sequence != null ) {
			sequence = sequence.trim();
			if(sequence.equalsIgnoreCase("FULL")) {

				//Full Build
				List<File> apexScripts = getApexScripts(folderPath, logFilePath);
				for(File apexFile : apexScripts) {
					String apexFileName = apexFile.getName();
					String seqStr = apexFileName.substring(0, apexFileName.indexOf("_"));
					int seq = Integer.parseInt(seqStr); 
					boolean isRunOnceScript = false;


					/*if(apexFileName.toUpperCase().endsWith("_RUNONCE.APEX")) {
						boolean isReadyToReRun = false;
						isRunOnceScript = true;
						boolean isAlreadyExecutedSuccessfully = RunOnceAndReRun.isAlreadyExecutedSucessfully(partnerConn, apexFileName, logFilePath);
						if(isAlreadyExecutedSuccessfully) {
							
							if(executionSeriesReRunOfRunOnceScripts != null && !executionSeriesReRunOfRunOnceScripts.trim().equals("")) {
								List<Integer> incrementalStepsReRunList = SFUtility.getIncrementalSteps(executionSeriesReRunOfRunOnceScripts, logFilePath);
								if(incrementalStepsReRunList.contains(seq)) {
									//Rerun of run once script
									isReadyToReRun = true;									
								}
							}
							

							if(!isReadyToReRun) {
								SFUtility.log("Already executed successfully, so skipped......", logFilePath);
								executionStatusSummary = executionStatusSummary  + "\n" + apexFileName + ": Already executed successfully, so skipped.";
								CICDLogService.createBuildAutomationCICDLog(partnerConn, logFilePath, apexFileName, "", "Skipped", isRunOnceScript, seq);
								continue;
							} else {
								SFUtility.log("Re-running of run-once script......", logFilePath);
							}
						}
					}*/
					String apexScript = getApexScriptFromFile(apexFile, logFilePath);
					Map<String, String> stepExecutionStatusMap = execute(partnerConn, apexScript, logFilePath);	
					for(String key : stepExecutionStatusMap.keySet()) {
						if(key.equals("Failed")) {
							executionStatusSummary = executionStatusSummary  + "\n" + apexFileName + ": Failed";
							String exceptionMsg = stepExecutionStatusMap.get(key);
							executionFailureSummary = executionFailureSummary + "\n" + apexFileName + ": Failed\nReason: " + exceptionMsg;  
							CICDLogService.createBuildAutomationCICDLog(partnerConn, logFilePath, apexFileName, exceptionMsg, "Failed", isRunOnceScript, seq, preOrPost, buildNumber);
						} else {							
							String id = RunOnceAndReRun.getIdOfSkippedCICDLog(partnerConn, apexFileName, logFilePath, preOrPost);
							if(id != null && !id.trim().equals("")) 
							{
								executionStatusSummary = executionStatusSummary  + "\n" + apexFileName + ": Already executed successfully, so Skipped...";
							}
							else
							{
								executionStatusSummary = executionStatusSummary  + "\n" + apexFileName + ": Passed";
							}
							CICDLogService.createBuildAutomationCICDLog(partnerConn, logFilePath, apexFileName, "", "Passed", isRunOnceScript, seq, preOrPost, buildNumber);
						}
					}
				}
			} else {

				SFUtility.log("Incremental build..", logFilePath);
				//Incremental Build
				List<File> apexScripts = getApexScripts(folderPath, logFilePath);
				List<Integer> incrementalStepsList = SFUtility.getIncrementalSteps(sequence, logFilePath);

				for(File apexFile : apexScripts) {
					String fileName = apexFile.getName();

					//Removing extension from file name
					fileName = fileName.replaceFirst("[.][^.]+$", "");

					String seqStr = fileName.substring(0, fileName.indexOf("_"));
					int seq = Integer.parseInt(seqStr); 
					
					
					if(incrementalStepsList.contains(seq)) {
						boolean isRunOnceScript = false;
						/*if(apexFile.getName().toUpperCase().endsWith("_RUNONCE.APEX")) {
							isRunOnceScript = true;
							boolean isReadyToReRun = false;
							boolean isAlreadyExecutedSuccessfully = RunOnceAndReRun.isAlreadyExecutedSucessfully(partnerConn, apexFile.getName(), logFilePath);
							if(isAlreadyExecutedSuccessfully) {	
								
								if(executionSeriesReRunOfRunOnceScripts != null && !executionSeriesReRunOfRunOnceScripts.trim().equals("")) {
									List<Integer> incrementalStepsReRunList = SFUtility.getIncrementalSteps(executionSeriesReRunOfRunOnceScripts, logFilePath);
									if(incrementalStepsReRunList.contains(seq)) {
										//Rerun of run once script
										isReadyToReRun = true;									
									}
								}

								if(!isReadyToReRun) {
									SFUtility.log("Already executed successfully, so skipped......", logFilePath);
									executionStatusSummary = executionStatusSummary  + "\n" + apexFile.getName() + ": Already executed successfully, so skipped.";
									CICDLogService.createBuildAutomationCICDLog(partnerConn, logFilePath, apexFile.getName(), "", "Skipped", isRunOnceScript, seq);								
									continue;
								} else {
									SFUtility.log("Re-running of run-once script......", logFilePath);
								}

							}
						}*/

						String apexScript = getApexScriptFromFile(apexFile, logFilePath);
						Map<String, String> stepExecutionStatusMap = execute(partnerConn, apexScript, logFilePath);	
						for(String key : stepExecutionStatusMap.keySet()) {
							if(key.equals("Failed")) {
								executionStatusSummary = executionStatusSummary  + "\n" + apexFile.getName() + ": Failed";
								String exceptionMsg = stepExecutionStatusMap.get(key);
								executionFailureSummary = executionFailureSummary + "\n" + apexFile.getName() + ": Failed\nReason: " + exceptionMsg;
								CICDLogService.createBuildAutomationCICDLog(partnerConn, logFilePath, apexFile.getName(), exceptionMsg, "Failed", isRunOnceScript, seq, preOrPost, buildNumber);
							} else {								
								String id = RunOnceAndReRun.getIdOfSkippedCICDLog(partnerConn, apexFile.getName(), logFilePath, preOrPost);
								if(id != null && !id.trim().equals("")) 
								{
									executionStatusSummary = executionStatusSummary  + "\n" + apexFile.getName() + ": Already executed successfully, so Skipped...";
								}
								else
								{
									executionStatusSummary = executionStatusSummary  + "\n" + apexFile.getName() + ": Passed";
								}
								CICDLogService.createBuildAutomationCICDLog(partnerConn, logFilePath, apexFile.getName(), "", "Passed", isRunOnceScript, seq, preOrPost, buildNumber);
							}
						}
					} else {
						//SFUtility.log("Not part of Incremental build.."); 
					}
				}			
			}
		}

		executionSummary.add(executionStatusSummary);
		executionSummary.add(executionFailureSummary);		
		return executionSummary;
	}

	public static String executeAndReturnMessage(PartnerConnection partnerConn, String code, String logFilePath) { 
		String message = "";

		try {			
			if(partnerConn != null) {
				//SFUtility.log("Going to execute apex code ! ", logFilePath);	
				//SFUtility.log("Code: "+code, logFilePath);	


				// Get an Apex stub
				ConnectorConfig apexCfg = new ConnectorConfig();
				apexCfg.setSessionId(partnerConn.getSessionHeader().getSessionId());

				apexCfg.setServiceEndpoint(partnerConn.getConfig().getServiceEndpoint().replace("/u/", "/s/"));
				com.sforce.soap.apex.SoapConnection apexSoapConn = Connector.newConnection(apexCfg); 
				ExecuteAnonymousResult result = apexSoapConn.executeAnonymous(code);

				if (result.isSuccess()) {  
					//SFUtility.log("Successfully executed apex script for this step !!", logFilePath); 
					//stepExecutionStatusMap.put("Passed", "Passed");
				} else {
					//SFUtility.log("Exception: Please verify apex script and run again ...", logFilePath);
					//SFUtility.log("Message: " + result.getExceptionMessage(), logFilePath);
					//SFUtility.log("Stack Trace: " + result.getExceptionStackTrace(), logFilePath);
					message = result.getExceptionMessage();
					//SFUtility.log("............................Message ...................................... !! "+message , logFilePath); 

					if(result.isCompiled()) {
						//stepExecutionStatusMap.put("Failed", result.getExceptionMessage());	
					} else {

						String compileProblem = result.getCompileProblem();
						//SFUtility.log("Compile Problem: " + compileProblem, logFilePath);						
						//stepExecutionStatusMap.put("Failed", compileProblem);
					}					
				} 
			}			
		} catch (ConnectionException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
			e.printStackTrace();
			System.exit(1);
		}		
		if(message!=null && message.contains("OSM_Exception: ")) {
			message = message.substring(15);
		}
		return message;
	}

	
	
	public static String executeAndReturnMessageAndCompileError(PartnerConnection partnerConn, String code, String logFilePath) { 
		String message = "";

		try {			
			if(partnerConn != null) {
				//SFUtility.log("Going to execute apex code ! ", logFilePath);	
				//SFUtility.log("Code: "+code, logFilePath);	


				// Get an Apex stub
				ConnectorConfig apexCfg = new ConnectorConfig();
				apexCfg.setSessionId(partnerConn.getSessionHeader().getSessionId());

				apexCfg.setServiceEndpoint(partnerConn.getConfig().getServiceEndpoint().replace("/u/", "/s/"));
				com.sforce.soap.apex.SoapConnection apexSoapConn = Connector.newConnection(apexCfg); 
				ExecuteAnonymousResult result = apexSoapConn.executeAnonymous(code);

				if (result.isSuccess()) {  
					//SFUtility.log("Successfully executed apex script for this step !!", logFilePath); 
					//stepExecutionStatusMap.put("Passed", "Passed");
				} else {
					//SFUtility.log("Exception: Please verify apex script and run again ...", logFilePath);
					//SFUtility.log("Message: " + result.getExceptionMessage(), logFilePath);
					//SFUtility.log("Stack Trace: " + result.getExceptionStackTrace(), logFilePath);
					message = result.getExceptionMessage();
					//SFUtility.log("............................Message ...................................... !! "+message , logFilePath); 

					if(result.isCompiled()) {
						//stepExecutionStatusMap.put("Failed", result.getExceptionMessage());	
					} else {

						message = result.getCompileProblem();
						//SFUtility.log("Compile Problem: " + message, logFilePath);						
						//stepExecutionStatusMap.put("Failed", compileProblem);
					}					
				} 
			}			
		} catch (ConnectionException e) {
			SFUtility.log("Exception: "+e.getMessage(), logFilePath);
			SFUtility.log("Exception cause: "+e.getCause(), logFilePath);
			e.printStackTrace();
			System.exit(1);
		}		
		if(message!=null && message.contains("OSM_Exception: ")) {
			message = message.substring(15);
		}
		return message;
	}

	
}