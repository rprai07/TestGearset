package com.ghi.coverage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import com.ghi.apex.ApexUtil;
import com.ghi.cicdlogs.CICDLogService;
import com.ghi.common.GitDiffReader;
import com.ghi.common.SFUtility;
import com.sforce.soap.partner.PartnerConnection;

public class CodeCoverage {  

	public static String codeCoverageValidationFile = "CodeCoverageValidation.apex";
	public static String tesClassExecutionScriptFile ="TestClassExecution.apex";
	public static String testClassExecutionStatusFile ="TestClassExecutionStatusCheck.apex";
	public static int POLLING_INTERVAL_IN_MILLISEC = 5000; //Default value
	private static boolean isAnyCoverageValidationFailure = false;
	private static int POLLING_RETRY_LIMIT = 60; //Default value
	private static final String SETTINGS_FILE = "\\settings.properties";	
	private static boolean failBuildForLowCoverage = false;
	private static boolean failBuildIfMissingTestClassMapping = false;	
	private static boolean arePropertiesLoaded = false;
	private static Map<String, String> testClassMap = new HashMap<String, String>();

	public static void executeCodeCoverage(PartnerConnection partnerConn, String codeCoverageScriptsDirPath, String repoPath, String workingDir, String logFilePath) {

		String coverageStatusSummary = "";
		String coverageFailureSummary = "";
		Set<String> pendingTestClassNames = new HashSet<String>();
		Map<String, Integer> testClassRetryCountMap = new HashMap<String, Integer>();
		loadProperties(workingDir, logFilePath);

		//Git diff logic
		Set<String> latestCheckedInClassSet = GitDiffReader.getListOfLatestCheckedInClassFiles(workingDir, repoPath, logFilePath);
		SFUtility.log("Total updated class files: "+ latestCheckedInClassSet.size(), logFilePath);
		for (String clsName : latestCheckedInClassSet) {
			SFUtility.log("\nLatest checked-in Class Name: "+ clsName, logFilePath);	
			String testClass = "";
			if(clsName != null && (clsName.trim().toUpperCase().startsWith("TEST") ||clsName.trim().toUpperCase().endsWith("TEST")  )) {
				testClass = clsName;
				clsName = GitDiffReader.getMainClassNameForGivenTestClass(clsName, workingDir, logFilePath);	
			} else {
				testClass = GitDiffReader.getTestClassNameForGivenMainClass(clsName, workingDir, logFilePath);					
			}

			SFUtility.log("Test Class: "+ testClass, logFilePath);
			if(testClass == null || testClass.trim().equals("") ) {
				if(isCoverageError(workingDir, logFilePath) && failBuildIfMissingTestClassMapping) {
					SFUtility.log("ERROR ::: Test Class Missing in mapping file, please update mapping file for main class : " + clsName, logFilePath);
					
				} else {
					SFUtility.log("WARNING ::: Test Class Missing in mapping file, please update mapping file for main class : " + clsName, logFilePath);
									
				}
				
				
				
				
				coverageStatusSummary = coverageStatusSummary + "\n"  + clsName + ": Test Class Missing in mapping file"; 
				if(failBuildIfMissingTestClassMapping) {
					coverageFailureSummary = coverageFailureSummary + "\n" + clsName + ": Test Class Missing in mapping file"; 
					isAnyCoverageValidationFailure = true;
				}

			} else if(clsName == null || clsName.trim().equals("") ) {
				if(isCoverageError(workingDir, logFilePath) && failBuildIfMissingTestClassMapping) {
					SFUtility.log("ERROR ::: Main Class Missing in mapping file, please update mapping file for test class : " + testClass, logFilePath);
					
				} else {
					SFUtility.log("WARNING ::: Main Class Missing in mapping file, please update mapping file for test class : " + testClass, logFilePath);
									
				}				
				
				coverageStatusSummary = coverageStatusSummary + "\n"  + clsName + ": Test Class Missing in mapping file"; 
				if(failBuildIfMissingTestClassMapping) {
					coverageFailureSummary = coverageFailureSummary + "\n" + clsName + ": Test Class Missing in mapping file"; 
					isAnyCoverageValidationFailure = true;
				}

			} else {
				testClassMap.put(clsName, testClass);
				pendingTestClassNames.add(testClass);
			}
		}


		for (String clsName : testClassMap.keySet()) {

			String testClass = testClassMap.get(clsName);
			//Insert test class into queue to get it executed

			SFUtility.log("Going to execute test class: " + testClass, logFilePath);
			String message = insertTestClassInQueue(codeCoverageScriptsDirPath, partnerConn, testClass, logFilePath);
			SFUtility.log("message::::: "+message, logFilePath);
			
			if(message != null && message.contains("not available")) {
				SFUtility.log("Test class " + testClass + " not available, please verify test class name !!", logFilePath);
				coverageStatusSummary = coverageStatusSummary + "\n" + testClass + ": Failed"; 
				if(failBuildIfMissingTestClassMapping) {
					coverageFailureSummary = coverageFailureSummary + "\n" + testClass + ": Failed"; 
					isAnyCoverageValidationFailure = true;					
				}
				pendingTestClassNames.remove(testClass);				
			}
		}

		wait(5000, logFilePath);
		
		File queueStatusScriptFile = new File(codeCoverageScriptsDirPath + "/" + testClassExecutionStatusFile);


		while(pendingTestClassNames.size() > 0) {
			String queueScript = ApexUtil.getApexScriptFromFile(queueStatusScriptFile, logFilePath);

			String testClass = getRandomTestClassFromSet(pendingTestClassNames);
			String clsName = getMainClassForGivenTestClass(testClassMap, testClass);
			if(testClass != null && !testClass.trim().equals("") ) {

				SFUtility.log("Going to check execution status of test class: " + testClass, logFilePath);
				queueScript = queueScript.replace("<TestClassName>", testClass);

				String queueMessage = ApexUtil.executeAndReturnMessage(partnerConn, queueScript, logFilePath);
				SFUtility.log("queueMessage:::: " + queueMessage, logFilePath);
				
				
				if(queueMessage != null && queueMessage.trim().toUpperCase().contains("COMPLETED")) {
					pendingTestClassNames.remove(testClass);
				}

				Integer retryCountForClass = testClassRetryCountMap.get(testClass);

				if(retryCountForClass == null || retryCountForClass == 0) {
					retryCountForClass = 1;
					testClassRetryCountMap.put(testClass, retryCountForClass);
				} else {
					retryCountForClass++;
					testClassRetryCountMap.put(testClass, retryCountForClass);
				}

			
				if(queueMessage == null || ( !queueMessage.trim().toUpperCase().contains("COMPLETED") && retryCountForClass < POLLING_RETRY_LIMIT)) {

					SFUtility.log("Apex test class '" +testClass + "' under processing, waiting for latest coverage results...", logFilePath);
					wait(POLLING_INTERVAL_IN_MILLISEC, logFilePath);					
					
					if(pendingTestClassNames.size() > 1) {
						SFUtility.log("Going to check status of other running test classes...", logFilePath);
					} else {
						SFUtility.log("No other pending test class, so waiting for its latest coverage results....", logFilePath);
					}

					continue;
				}

				
				if(queueMessage != null && queueMessage.trim().toUpperCase().contains("FAIL")) {
					//SFUtility.log("queueMessage: " + queueMessage, logFilePath);
					
					SFUtility.log("Test class: '" +testClass + "' Failed, continuing for next test class.. ", logFilePath);
					coverageStatusSummary = coverageStatusSummary + "\n" + testClass + ": Failed"; 
					coverageFailureSummary = coverageFailureSummary + "\n" + testClass + ": Failed"; 
					isAnyCoverageValidationFailure = true;
					pendingTestClassNames.remove(testClass);
					continue;				
				}
				
				
				if(retryCountForClass == POLLING_RETRY_LIMIT) {
					SFUtility.log("Polling retry limit exceeded for apex test class: '" +testClass + "' so, skipping and continuing for next test class.. ", logFilePath);
					coverageStatusSummary = coverageStatusSummary + "\n" + testClass + ": Polling retry limit exceeded"; 
					coverageFailureSummary = coverageFailureSummary + "\n" + testClass + ": Polling retry limit exceeded"; 
					isAnyCoverageValidationFailure = true;
					pendingTestClassNames.remove(testClass);
					continue;
				}

				SFUtility.log("Execution completed successfully, going to fetch latest coverage of class: " + clsName, logFilePath);

				File coverageValidationFile = new File(codeCoverageScriptsDirPath + "/" + codeCoverageValidationFile);
				String coverageValidationScript = ApexUtil.getApexScriptFromFile(coverageValidationFile, logFilePath);
				coverageValidationScript = coverageValidationScript.replace("<MainClassName>", clsName);

				String coverageValidationMessage = ApexUtil.executeAndReturnMessage(partnerConn, coverageValidationScript, logFilePath);	
				//coverageStatusSummary = coverageStatusSummary + "" + coverageValidationMessage;
				if(coverageValidationMessage != null && !coverageValidationMessage.contains("WARNING ::")) {
					//SFUtility.log(clsName + " : Coverage OK...", logFilePath);
					SFUtility.log(coverageValidationMessage, logFilePath);
					//coverageStatusSummary = coverageStatusSummary + "\n" + clsName + " : Coverage OK...";	
					coverageStatusSummary = coverageStatusSummary + "\n" + coverageValidationMessage;
					
				} else {
					SFUtility.log(coverageValidationMessage, logFilePath);
					coverageStatusSummary = coverageStatusSummary + "\n" + coverageValidationMessage;
					coverageFailureSummary = coverageFailureSummary + "\n" + coverageValidationMessage;
					isAnyCoverageValidationFailure = true;

				}
			}
		}

		printSummary(workingDir, logFilePath, coverageStatusSummary, coverageFailureSummary, partnerConn);
	}


	public static String getMainClassForGivenTestClass(Map<String, String> testClassMap, String testClass) {
		Set<String> mainClassSet = testClassMap.keySet();

		for(String mainClass : mainClassSet) {
			String testCls = testClassMap.get(mainClass);
			if(testCls != null && testClass != null && testCls.equalsIgnoreCase(testClass)) {
				return mainClass;
			}		
		}
		return "";		
	}



	public static void printSummary(String workingDir, String logFilePath,String coverageStatusSummary,String coverageFailureSummary, PartnerConnection partnerConn) {
		boolean failBuild = isCoverageError(workingDir, logFilePath);
		String buildStatus = "Passed";
		if(failBuild && isAnyCoverageValidationFailure) {
			buildStatus = "Failed";
		}



		if(coverageStatusSummary != null && !coverageStatusSummary.trim().equals("")) {
			if(failBuild) {
				coverageStatusSummary = coverageStatusSummary.replace("WARNING ::", "ERROR ::");
			}

			SFUtility.log("\n******************************************************** COVERAGE STATUS SUMMARY ********************************************************\n" + coverageStatusSummary, logFilePath);	
			SFUtility.log("\n***************************************************************************************************************************************\n", logFilePath);	


			CICDLogService.createCoverageCICDLog("COVERAGE STATUS SUMMARY:: " + coverageStatusSummary, partnerConn, logFilePath, buildStatus);			
		} else {
			CICDLogService.createCoverageCICDLog("COVERAGE STATUS SUMMARY:: No apex class updated in current check-in..", partnerConn, logFilePath, buildStatus);
		}


		if(isAnyCoverageValidationFailure) {

			if(failBuild) {
				coverageFailureSummary = coverageFailureSummary.replace("WARNING ::", "ERROR ::");
				coverageFailureSummary = coverageFailureSummary + "\nERROR:: Low Coverage :: Build Failed !! ";
			}

			SFUtility.log("\n******************************************************** COVERAGE FAILURE SUMMARY ********************************************************\n" + coverageFailureSummary, logFilePath);
			SFUtility.log("\n***************************************************************************************************************************************\n", logFilePath);	

			CICDLogService.createCoverageCICDLog("COVERAGE FAILURE SUMMARY:: " + coverageFailureSummary , partnerConn, logFilePath, buildStatus);			

			if(failBuild) {
				SFUtility.log("\nERROR:: Low Coverage :: Build Failed !! ", logFilePath);			
				System.exit(1);				
			}			
		}
		
		if(coverageFailureSummary != null && coverageFailureSummary.contains("WARNING ::")) {
			
			SFUtility.log("\nBuild Passed with low coverage warning(s) !! ", logFilePath);	
		} else {
			SFUtility.log("\nBuild Passed coverage validation successfully !! ", logFilePath);
		}
		
	}



	public static String insertTestClassInQueue(String codeCoverageScriptsDirPath, PartnerConnection partnerConn, String testClass, String logFilePath) {
		File coverageFile = new File(codeCoverageScriptsDirPath + "/" + tesClassExecutionScriptFile);
		String coverageScipt = ApexUtil.getApexScriptFromFile(coverageFile, logFilePath);
		coverageScipt = coverageScipt.replace("<TestClassName>", testClass);

		String message = ApexUtil.executeAndReturnMessage(partnerConn, coverageScipt, logFilePath);	
		if(message != null && message.contains("Apex test class already enqueued")) {
			SFUtility.log("Apex test class '" +testClass + "' already enqueued, waiting for latest coverage results.. ", logFilePath);
		}
		
		return message;
	}




	public static void wait(int delayInMilliSec, String logFilePath) {
		try {
			Thread.sleep(delayInMilliSec);
		} catch (InterruptedException ex) {
			SFUtility.log("Exception: "+ex.getMessage(), logFilePath);
		}

	}


	public static boolean isCoverageError(String workingDir, String logFilePath) {
		if(!arePropertiesLoaded) {
			loadProperties(workingDir, logFilePath);			
		}
		return failBuildForLowCoverage;
	}


	public static void loadProperties(String workingDir, String logFilePath) {
		FileReader reader = null;
		try {
			reader = new FileReader(workingDir + SETTINGS_FILE); 
			Properties p = new Properties();  
			p.load(reader);  


			String failBuildForLowCoverageStr = p.getProperty("fail_build_for_low_coverage");
			SFUtility.log("fail_build_for_low_coverage: "+ failBuildForLowCoverageStr, logFilePath);   
			if(failBuildForLowCoverageStr != null && failBuildForLowCoverageStr.trim().equalsIgnoreCase("true")) {
				failBuildForLowCoverage = true;
			}



			String failBuildIfMissingTestClassMappingStr = p.getProperty("fail_build_if_missing_test_class_mapping");
			SFUtility.log("fail_build_if_missing_test_class_mapping: "+ failBuildIfMissingTestClassMappingStr, logFilePath);   
			if(failBuildIfMissingTestClassMappingStr != null && failBuildIfMissingTestClassMappingStr.trim().equalsIgnoreCase("true")) {
				failBuildIfMissingTestClassMapping = true;
			}
			

			String pollingIntervalInMillisecStr = p.getProperty("polling_interval_in_millisec");
			SFUtility.log("polling_interval_in_millisec: "+ pollingIntervalInMillisecStr, logFilePath);   
			if(pollingIntervalInMillisecStr != null && !pollingIntervalInMillisecStr.trim().equals("")) {
				POLLING_INTERVAL_IN_MILLISEC = Integer.parseInt(pollingIntervalInMillisecStr.trim());
			}



			String pollingRetryLimitStr = p.getProperty("polling_retry_limit");
			SFUtility.log("polling_retry_limit: "+ pollingRetryLimitStr, logFilePath);   
			if(pollingRetryLimitStr != null && !pollingRetryLimitStr.trim().equals("")) {
				POLLING_RETRY_LIMIT = Integer.parseInt(pollingRetryLimitStr.trim());
			}

			arePropertiesLoaded = true;

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
	}




	public static String getRandomTestClassFromSet(Set<String> pendingTestClassSet) {		
		Random random = new Random();
		int randomNumber = random.nextInt(pendingTestClassSet.size());

		int currentIndex = 0;
		String randomElement = "";

		for(String element : pendingTestClassSet) {

			randomElement = element;
			if(currentIndex == randomNumber) {
				return randomElement;
			}
			currentIndex++;
		}
		return randomElement;
	}



	/*	public static void getCodeCoverage(String workingDir, String logFilePath) {
		ArrayList<String> classList = XMLFileReader.getMemebersForGivenName("ApexClass");
		for(String className : classList) {

			HashMap <String,String> testClassMappings = PropertyFileReader.getTestClassPropertiesMap(workingDir, logFilePath);

			for(String key : testClassMappings.keySet()) {
				//SFUtility.log("class: "+ key);
				String testClass = testClassMappings.get(key);
				SFUtility.log("test class: "+testClass, logFilePath);
			}

			String testClass = testClassMappings.get(className);
			SFUtility.log("test class for given main class from mapping file: "+testClass, logFilePath);

		}
	}
	 */

}