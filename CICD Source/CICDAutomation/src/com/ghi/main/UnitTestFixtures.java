package com.ghi.main;

import java.util.ArrayList;
import java.util.List;

public class UnitTestFixtures {

	private static List<String> fixtureList = new ArrayList<String>();

	public static void getFixtures() {
		/*		fixtureList.add("IBCOrderProcessing.createOrder()");
		fixtureList.add("MMROrderProcessing.createOrder()");
		fixtureList.add("ColonOrderProcessing.createOrder()");
		fixtureList.add("DCISOrderProcessing.createOrder()");
		fixtureList.add("ProstateOrderProcessing.createOrder()");
		fixtureList.add("ProstateARV7OrderProcessing.createOrder()");


		fixtureList.add("IBCOrderProcessing.processOrder()");
		fixtureList.add("MMROrderProcessing.processOrder()");
		fixtureList.add("ColonOrderProcessing.processOrder()");
		fixtureList.add("DCISOrderProcessing.processOrder()");
		fixtureList.add("ProstateOrderProcessing.processOrder()");
		fixtureList.add("ProstateARV7OrderProcessing.processOrder()");


		fixtureList.add("IBCOrderProcessing.processOrder()");
		fixtureList.add("MMROrderProcessing.processOrder()");
		fixtureList.add("ColonOrderProcessing.processOrder()");
		fixtureList.add("DCISOrderProcessing.processOrder()");
		fixtureList.add("ProstateOrderProcessing.processOrder()");
		fixtureList.add("ProstateARV7OrderProcessing.processOrder()");*/


	}


	public static String getCreateUserFixtureOLD(String script, String timeStr) { 


		String apexScript  = script 		
				+ " Unit_Test_Result__c utr = new Unit_Test_Result__c();"
				+ "utr.Record_Id__c = recordId;"
				+ "utr.Time_Stamp__c = '" + timeStr + "';"
				+ "insert utr;";

		return apexScript;

	}



	/**
	 * Method to create record fixture by appending unit test result record.
	 * @param script
	 * @param timeStr
	 * @return
	 */
	public static String getCreateRecordFixture(String script, String timeStr, String apexScriptFile,String CSV) {


		String apexScript  = script 		
				+ " Unit_Test_Result__c utr = new Unit_Test_Result__c();"
				+ "utr.Record_Id__c = recordId;"
				+ "utr.Time_Stamp__c = '" + timeStr + "';"
				+ "utr.Apex_Script_File_Name__c = '" + apexScriptFile + "';"	
				+ "utr.Data_CSV_File_Name__c = '"+CSV+"';" 
				+ "insert utr;";

		return apexScript;

	}



	/**
	 * Method to create address affiliation fixture.	
	 * @param accountId
	 * @param addressId
	 * @return
	 */
	public static String getCreateAddressAffiliationFixture(String accountId, String addressId) { 		

		String apexScript  = "GHI_UTF_OrderProcessing.createAddressAffiliation('"+accountId+"','"+addressId+"');";

		return apexScript;

	}






	/*public static String getUpdateOLIICDCodeFixture(String orderable, String orderId) {


	String apexScript  = orderable+"OrderProcessing.updateOLIICDCode('"+orderId+"');";

	return apexScript;

}*/



	/*public static String getUpdateOLINodeStatusFixture(String orderable, String orderId) {


	String apexScript  = orderable+"OrderProcessing.updateOLINodeStatus('"+orderId+"');";

	return apexScript;

}*/





	/*public static String getCreateSSOrderRoleFixture(String orderId, String hcoAccountId, String hcpContactId) {	

	String apexScript  = "OrderProcessing.createSSOrderRole('"+orderId+"','"+hcoAccountId+"','"+hcpContactId+"');";

	return apexScript;

}*/

	//createOrder(patientId, payorAccountId, "GHI to Request", date, "Private Insurance", "New", hcpContactId, hcoAccountId);

	//public static String getCreateOrderFixture(String orderable, String timeStr) {		
	//public static String getCreateOrderFixture(String orderable, String timeStr, patientId, payorAccountId, "GHI to Request", date, "Private Insurance", "New", hcpContactId, hcoAccountId) {








	/**
	 * Method to create order fixture by appending unit test result record..
	 * @param orderableClass
	 * @param timeStr
	 * @param patientId
	 * @param payorAccountId
	 * @param specRetrievalOption
	 * @param signDate
	 * @param billType
	 * @param triageOutcome
	 * @param hcpContactId
	 * @param hcoAccountId
	 * @return
	 */
	public static String getCreateOrderFixture(String orderableClass, String timeStr, String patientId, String payorAccountId, String specRetrievalOption, String signDate, String billType, String triageOutcome, String hcpContactId, String hcoAccountId) {



		/*String apexScript  = "ID orderId = "+orderable+"OrderProcessing.createOrder(); "				
				+ "Unit_Test_Result__c utr = new Unit_Test_Result__c();"
				+ "utr.Record_Id__c = orderId;"
				+ "utr.Time_Stamp__c = '" + timeStr + "';"
				+ "insert utr;";
		 */

		String apexScript  = "ID orderId = "+orderableClass+"OrderProcessing.createOrder('"+patientId+"','"+payorAccountId+"','"+specRetrievalOption+"',"+signDate+",'"+billType+"','"+triageOutcome+"','"+hcpContactId+"','"+hcoAccountId+"'); "				
				+ "Unit_Test_Result__c utr = new Unit_Test_Result__c();"
				+ "utr.Record_Id__c = orderId;"
				+ "utr.Time_Stamp__c = '" + timeStr + "';"
				+ "insert utr;";









		return apexScript;


	}




	/**
	 * Method to create order role fixture.
	 * @param orderableClass
	 * @param role
	 * @param orderId
	 * @param accountId
	 * @param contactId
	 * @return
	 */
	public static String getCreateOrderRoleFixture(String orderableClass, String role, String orderId, String accountId, String contactId) {		

		//String apexScript  = orderable+"OrderProcessing.createOrderingOrderRole('"+orderId+"');";		
		String apexScript  = orderableClass+"OrderProcessing.createOrderRole('"+role+"','"+orderId+"','"+accountId+"','"+contactId+"');";		

		return apexScript;

	}



	/*public static String getCreateOrderingOrderRoleFixture(String orderable, String orderId, String accountId, String contactId) {		

	//String apexScript  = orderable+"OrderProcessing.createOrderingOrderRole('"+orderId+"');";		
	String apexScript  = orderable+"OrderProcessing.createOrderingOrderRole('"+orderId+"','"+accountId+"','"+contactId+"');";		

		return apexScript;

	}*/


	/*public static String getCreateSSOrderRoleFixtureFixture(String orderable, String orderId, String accountId, String contactId) {		

	//String apexScript  = orderable+"OrderProcessing.createOrderingOrderRole('"+orderId+"');";		
	String apexScript  = orderable+"OrderProcessing.createSSOrderRole('"+orderId+"','"+accountId+"','"+contactId+"');";		

		return apexScript;

	}
	 */


	/**
	 * Method to create process order fixture.	
	 * @param orderableClass
	 * @param orderId
	 * @return
	 */
	public static String getProcessOrderFixture(String orderableClass, String orderId) {		

		String apexScript  = orderableClass+"OrderProcessing.processOrder('"+orderId+"');";		
		return apexScript;

	}




	/**
	 * Method to create close pre-billing case fixture.	
	 * @param orderableClass
	 * @param orderId
	 * @return
	 */
	public static String getClosePreBillingCaseFixture(String orderableClass, String orderId) {		
		String apexScript  = orderableClass+"OrderProcessing.closePreBillingCase('"+orderId+"');";		
		return apexScript;

	}


	
	/**
	 * Method to create close SOMN case fixture.	
	 * @param orderableClass
	 * @param orderId
	 * @return
	 */
	public static String getCloseSomnCaseFixture(String orderableClass, String orderId) {		
		String apexScript  = orderableClass+"OrderProcessing.closeSomnCase('"+orderId+"');";		
		return apexScript;

	}
	
	
	/**
	 * Method to create close Clinical Experience case fixture.	
	 * @param orderableClass
	 * @param orderId
	 * @return
	 */
	public static String getCloseClinicalExperienceCaseFixture(String orderableClass, String orderId) {		
		String apexScript  = orderableClass+"OrderProcessing.closeClinicalExperienceCase('"+orderId+"');";		
		return apexScript;

	}
	
	
	

	/**
	 * Method to get create package fixture. 
	 * @param orderableClass
	 * @param orderId
	 * @return
	 */
	public static String getCreatePackageFixture(String orderableClass, String orderId) {		
		String apexScript  = orderableClass+"OrderProcessing.createPackage('"+orderId+"');";		
		return apexScript;

	}




	/**
	 * Method to get close specimen arrival case fixture.
	 * @param orderableClass
	 * @param orderId
	 * @return
	 */
	public static String getCloseSpecArrivalCaseFixture(String orderableClass, String orderId) {		
		String apexScript  = orderableClass+"OrderProcessing.closeSpecArrivalCase('"+orderId+"');";		
		return apexScript;

	}
	
	/**
	 * Method to get close specimen arrival case fixture.
	 * @param orderableClass
	 * @param orderId
	 * @return
	 */
	public static String getCloseBillingCaseFixture(String orderableClass, String orderId) {		
		String apexScript  = orderableClass+"OrderProcessing.closeBillingCase('"+orderId+"');";		
		return apexScript;

	}
	



	/**
	 * Method to get create specimen fixture. 
	 * @param orderableClass 
	 * @param orderId
	 * @param icrtUser
	 * @param icrtPassword
	 * @return
	 */
	public static String getCreateSpecimenFixture(String orderableClass, String orderId, String icrtUser, String icrtPassword) {		
		String apexScript  = orderableClass+"OrderProcessing.createSpecimen('"+orderId+"','"+icrtUser+"','"+icrtPassword+"');";		

		return apexScript;

	}


	/**
	 * Method to get create result fixture. 
	 * @param orderableClass
	 * @param orderId
	 * @param icrtUser
	 * @param icrtPassword
	 * @return
	 */
	public static String getCreateResultFixture(String orderableClass, String orderId, String icrtUser, String icrtPassword) {
		String apexScript  = orderableClass+"OrderProcessing.createResult('"+orderId+"','"+icrtUser+"','"+icrtPassword+"');";		

		return apexScript;

	}


	/**
	 * Method to get update OLI to DVC fixture.
	 * @param orderableClass
	 * @param orderId
	 * @return
	 */
	public static String getUpdateOLIToDVCFixture(String orderableClass, String orderId) {		
		String apexScript  = orderableClass+"OrderProcessing.updateOLIToDVC('"+orderId+"');";		
		return apexScript;

	}



	/**
	 * Method to get create distribution event fixture.
	 * @param orderableClass
	 * @param orderId
	 * @param icrtUser
	 * @param icrtPassword
	 * @return
	 */
	public static String getCreateDistributionEventFixture(String orderableClass, String orderId, String icrtUser, String icrtPassword) {
		String apexScript  = orderableClass+"OrderProcessing.createDistributionEvent('"+orderId+"','"+icrtUser+"','"+icrtPassword+"');";		

		return apexScript;

	}

}
