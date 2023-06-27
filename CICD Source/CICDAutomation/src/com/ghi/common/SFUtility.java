package com.ghi.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
 
public class SFUtility {

	public static List<File> sortFiles(File[] files, String logFilePath) {		
		List<File> sortFiles = Arrays.asList(files);
		try {			 
			sortFiles.sort(new Comparator<File>() {

				@Override
				public int compare(File f1, File f2) {
					int returnVal = 0;

					String name1 = f1.getName();
					String name2 = f2.getName();

					if(name1.contains("_") && name2.contains("_")) {					

						//Removing extension from file name
						//name1 = name1.replaceFirst("[.][^.]+$", "");
						//name2 = name2.replaceFirst("[.][^.]+$", "");					

						String seq1 = name1.substring(0, name1.indexOf("_"));
						String seq2 = name2.substring(0, name2.indexOf("_"));

						int sequence1 = Integer.parseInt(seq1);
						int sequence2 = Integer.parseInt(seq2);

						if(sequence1 == sequence2) {
							returnVal= 0;
						} else if (sequence1 > sequence2){
							returnVal= 1;            			
						} else {
							returnVal= -1;            			
						}
					}    
					return returnVal;
				}
			});			

			return sortFiles;
		} catch(Exception ex) {
			SFUtility.log("Exception: "+ex.getMessage(), logFilePath);
			SFUtility.log("Please verify your apex file names and execute again..", logFilePath);			
			System.exit(1);
		}		
		return null;
	}


	public static void log(String message, String logFilePath) {
		System.out.println(message);
		BufferedWriter bw = null;
		FileWriter fw = null;
		PrintWriter pw = null;
		try {

			File file = new File(logFilePath);
			// if file does not exist, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
			Calendar cal = Calendar.getInstance(); 
			Date now = cal.getTime();			
			pw.println(now + " : " + message);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (bw != null) {
					bw.close();
				}

				if (fw != null) {
					fw.close();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static List<Integer> getIncrementalSteps(String sequenceStr, String logFilePath) {
		List<Integer> incrementalStepsList = new ArrayList<Integer>();		
		try {			
			String steps[] = sequenceStr.split(",");
			for (String sequence : steps) {
				SFUtility.log("step(s): " + sequence, logFilePath); 
				if(sequence != null && !sequence.trim().equals("")) {
					sequence = sequence.trim();
					if(sequence.contains("-")) {				
						String series[] = sequence.split("-");
						if(series.length == 2) {
							String startStr = series[0];
							String endStr = series[1];
							int start = Integer.parseInt(startStr);
							int end = Integer.parseInt(endStr);
							for(int i = start; i<= end; i++) {
								incrementalStepsList.add(i);
							}
						}

					} else {
						incrementalStepsList.add(Integer.parseInt(sequence));
					}
				} else {
					SFUtility.log("No step(s) provided to be executed.", logFilePath); 
					System.exit(0);
				}
			}			
		} catch(Exception ex) {
			SFUtility.log("Please verify Incremental sequence string and try again..", logFilePath);
			System.exit(1);
		}
		return incrementalStepsList;
	}
	
	/**
	 * Method to print time in log file.
	 * @param logFilePath
	 */
	public static void printTime(String logFilePath) {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
		String strDate = sdf.format(cal.getTime());
		System.out.println("Current Time: "+strDate);
		SFUtility.log("Current Time: "+strDate,  logFilePath);
	}
}