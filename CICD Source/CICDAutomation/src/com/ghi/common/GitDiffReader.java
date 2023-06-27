package com.ghi.common;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class GitDiffReader { 

	private static final String SETTINGS_FILE = "\\settings.properties";	
	private static final String CMD = "cmd /c ";//git diff head~19 --name-only"; //1 to 6 for testing
	private static String GIT_COVERAGE_CMD = ""; //"cmd /c git diff head~19 --name-only"; //1 to 6 for testing



	public static Set<String> getListOfLatestCheckedInClassFiles(String workingDir, String repoPath, String logFilePath) {
		Set<String> checkedInClassSet = new HashSet<String>();
		try {
			GIT_COVERAGE_CMD = getGitCoverageCommand(workingDir, logFilePath);
			//Run GIT command
			Process process = Runtime.getRuntime().exec(GIT_COVERAGE_CMD, null, new File(repoPath)); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.endsWith(".cls")) {
					if(line.contains("/")) {
						int index = line.lastIndexOf("/")+1;
						line = line.substring(index);
					}

					//Removing .cls extension
					line = line.substring(0, line.length()-4);					
					checkedInClassSet.add(line);
				}
			}
			int exitVal = process.waitFor();
			if (exitVal == 0) {
				SFUtility.log("Success!!", logFilePath);
			} else {
				SFUtility.log("abnormal exit of process", logFilePath);
			}
		} catch (IOException ex) {
			SFUtility.log("Exception: "+ex.getMessage(), logFilePath);
		} catch (InterruptedException ex) {
			SFUtility.log("Exception: "+ex.getMessage(), logFilePath);
		}
		return checkedInClassSet;
	}

	public static String getTestClassNameForGivenMainClass(String mainClassName, String workingDir, String logFilePath) {
		PropertyFileReader.getTestClassPropertiesMap(workingDir, logFilePath);

		HashMap <String,String> map = PropertyFileReader.getTestClassPropertiesMap(workingDir, logFilePath);
		String testClass = map.get(mainClassName);
		if(testClass == null) {
			testClass = "";
		}
		return testClass;
	}


	public static String getMainClassNameForGivenTestClass(String testClassName, String workingDir, String logFilePath) {
		PropertyFileReader.getTestClassPropertiesMap(workingDir, logFilePath);

		HashMap <String,String> map = PropertyFileReader.getTestClassPropertiesMap(workingDir, logFilePath);
		for(String mainClass : map.keySet()) {
			String testClass = map.get(mainClass);
			if(testClass != null && testClass.trim().equalsIgnoreCase(testClassName)) {
				return mainClass;
			}			
		}
		return "";
	}

	
	
	public static String getGitCoverageCommand(String workingDir, String logFilePath) {
		FileReader reader = null;
		String gitCoverageCommand = "";
		try {
			reader = new FileReader(workingDir + SETTINGS_FILE); 
			Properties p = new Properties();  
			p.load(reader);  

			gitCoverageCommand = p.getProperty("git_coverage_command");

			SFUtility.log("GIT coverage command: "+ gitCoverageCommand, logFilePath);   
			if(gitCoverageCommand == null || gitCoverageCommand.trim().equals("")) {
				SFUtility.log("GIT coverage command is missing in settings file. So exiting....", logFilePath);   
				System.exit(1);							
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
		return CMD + gitCoverageCommand;
	}
}