package com.ghi.common;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

public class PropertyFileReader {
	private static final String TEST_CLASS_MAPPINGS_FILE = "test_class_mapping.properties";
	public static void main(String[] args) {
		HashMap <String,String> map = getTestClassPropertiesMap("E:\\", null);
		for(String key : map.keySet()) {
			System.out.println("class: "+ key);
			String testClass = map.get(key);
			System.out.println("test class: "+testClass);
			 
			
			 
		}
	}
	
	public static HashMap <String,String> getTestClassPropertiesMap(String workingDir, String logFilePath) {
		return getPropertiesMap(workingDir, logFilePath, TEST_CLASS_MAPPINGS_FILE);
	}
		
		
	public static HashMap <String,String> getPropertiesMap(String workingDir, String logFilePath, String fileName) {
		FileReader reader = null;
		HashMap <String,String> map = new HashMap<String,String>();
		try {
			//reader = new FileReader(workingDir + TEST_CLASS_MAPPINGS_FILE);
			reader = new FileReader(workingDir + "\\"+  fileName);
			
			Properties properties = new Properties();  
			properties.load(reader);
			
			for (Entry<Object, Object> entry : properties.entrySet()) {
			    map.put((String) entry.getKey(), (String) entry.getValue());
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
		return map;
	}
}