package com.ghi.ut;

import com.ghi.apex.ApexUtil;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.partner.PartnerConnection;

public class CaseProcessing {
	
	
	public static String executeOrderProcessingCloseSpecArrivalCase(PartnerConnection partnerConn,
			EnterpriseConnection enterConn, String apexScript, String logFilePath, String orderableClass)
					throws InterruptedException {
		String closeSpecArrivalCase = UnitTestFixtures.getCloseSpecArrivalCaseFixture(orderableClass , UnitTestingUtils.orderId);
		ApexUtil.execute(partnerConn, closeSpecArrivalCase, logFilePath);
		Thread.sleep(UnitTestingUtils.WAIT_TIME_IN_MILLISEC);	
		UnitTestingUtils.waitTillUnlocked(enterConn, UnitTestingUtils.orderId, logFilePath);
		apexScript = apexScript.replace(orderableClass + "OrderProcessing.closeSpecArrivalCase($orderId);", "");
		return apexScript;
	}
	
	public static String executeOrderProcessingCloseBillingCase(PartnerConnection partnerConn,EnterpriseConnection enterConn, String apexScript, String logFilePath, String orderableClass)throws InterruptedException {
		String closeBillingCase = UnitTestFixtures.getCloseBillingCaseFixture(orderableClass , UnitTestingUtils.orderId);
		
		Thread.sleep(UnitTestingUtils.WAIT_TIME_IN_MILLISEC);
		Thread.sleep(UnitTestingUtils.WAIT_TIME_IN_MILLISEC);
		Thread.sleep(UnitTestingUtils.WAIT_TIME_IN_MILLISEC);
		ApexUtil.execute(partnerConn, closeBillingCase, logFilePath);
		Thread.sleep(UnitTestingUtils.WAIT_TIME_IN_MILLISEC);
		Thread.sleep(UnitTestingUtils.WAIT_TIME_IN_MILLISEC);
		Thread.sleep(UnitTestingUtils.WAIT_TIME_IN_MILLISEC);
		UnitTestingUtils.waitTillUnlocked(enterConn, UnitTestingUtils.orderId, logFilePath);
		apexScript = apexScript.replace(orderableClass + "OrderProcessing.closeBillingCase($orderId);", "");
		System.out.println("apex script" + apexScript);

		return apexScript;
	}

	
	public static String executeOrderProcessingCloseClinicalExperienceCase(PartnerConnection partnerConn,
			EnterpriseConnection enterConn, String apexScript, String logFilePath, String orderableClass)
					throws InterruptedException {
		String closeClinicalExperienceCaseFixture = UnitTestFixtures.getCloseClinicalExperienceCaseFixture(orderableClass , UnitTestingUtils.orderId);
		ApexUtil.execute(partnerConn, closeClinicalExperienceCaseFixture, logFilePath);

		UnitTestingUtils.waitTillUnlocked(enterConn, UnitTestingUtils.orderId, logFilePath);

		//Order not locked while pre-billing is getting closed, so need to add static wait time
		Thread.sleep(UnitTestingUtils.WAIT_TIME_IN_MILLISEC);
		apexScript = apexScript.replace(orderableClass + "OrderProcessing.closeClinicalExperienceCase($orderId);", "");
		return apexScript;
	}




	public static String executeOrderProcessingCloseSOMNCase(PartnerConnection partnerConn,
			EnterpriseConnection enterConn, String apexScript, String logFilePath, String orderableClass)
					throws InterruptedException {
		String closeSomnCaseFixture = UnitTestFixtures.getCloseSomnCaseFixture(orderableClass , UnitTestingUtils.orderId);
		ApexUtil.execute(partnerConn, closeSomnCaseFixture, logFilePath);

		UnitTestingUtils.waitTillUnlocked(enterConn, UnitTestingUtils.orderId, logFilePath);

		//Order not locked while pre-billing is getting closed, so need to add static wait time
		Thread.sleep(UnitTestingUtils.WAIT_TIME_IN_MILLISEC);
		apexScript = apexScript.replace(orderableClass + "OrderProcessing.closeSomnCase($orderId);", "");
		return apexScript;
	}




	public static String executeOrderProcessingClosePreBillingCase(PartnerConnection partnerConn,
			EnterpriseConnection enterConn, String apexScript, String logFilePath, String orderableClass)
					throws InterruptedException {
		String closePreBillingCaseFixture = UnitTestFixtures.getClosePreBillingCaseFixture(orderableClass , UnitTestingUtils.orderId);
		ApexUtil.execute(partnerConn, closePreBillingCaseFixture, logFilePath);

		UnitTestingUtils.waitTillUnlocked(enterConn, UnitTestingUtils.orderId, logFilePath);

		//Order not locked while pre-billing is getting closed, so need to add static wait time
		Thread.sleep(UnitTestingUtils.WAIT_TIME_IN_MILLISEC);
		apexScript = apexScript.replace(orderableClass + "OrderProcessing.closePreBillingCase($orderId);", "");
		return apexScript;
	}
}