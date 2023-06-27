package com.ghi.ui.stepdefs;

import com.ghi.common.SFDataFactory;
import com.ghi.main.Main;
import com.ghi.ut.UnitTestingUtils;
import com.ghi.ui.pages.ContactPage;
import com.ghi.ui.pages.HomePage;
import com.ghi.ui.pages.LoginPage;
import com.sforce.soap.enterprise.EnterpriseConnection; 

/**
 * This class is used to automate test cases related to running Order Entry Guide on contact in Classic mode
 */
public class OrderEntryGuideStepDef extends ParentTest {

	private static LoginPage objLogin;
	private static HomePage objHome;
	private static ContactPage objContact;	


	/**
	 * This method is created to open the Chrome and launch Salesforce
	 */
	public static void open_the_Chrome_and_launch_salesforce() throws Throwable {

		/*		try {
			System.setProperty("webdriver.chrome.driver", Main.getDriverPath());

		} catch (Exception e) {
			System.out.println("Exception:================================================================= "+e.getMessage());
		}

		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);*/
		ParentTest test=new ParentTest();
		driver.get(BASE_URL);
	}

	/**
	 *  This method is created to login into Salesforce and open Contact.
	 */
	public static void login_and_open_contact() throws Throwable {

		//Create login page object
		objLogin = new LoginPage(driver);
		Thread.sleep(10000);
		objLogin.login(Main.getSFUserName(), Main.getSFPassword());
		Thread.sleep(10000);

		String currentURL = driver.getCurrentUrl(); 
		System.out.println(currentURL); 

		Thread.sleep(20000);
		objHome = new HomePage(driver);
		if(currentURL.contains("lightning")) {
            
			 
			 objHome.clicklightningAvatarButton();
			 objHome.goToSalesforceClassic();

		} else {
			System.out.println("Not in Lightning.................");
		}
		
		
		//Create Home Page object
		objHome = new HomePage(driver);
		String label=objHome.getLabel();
		if(!label.equals("GenomicHealth Sales")) 
		{
			objHome.clickMenuButton();
			objHome.gotoClassicHomeGenomicHealthSales();
		}
		objHome.setSearch("Contacts");
		String patientId = UnitTestingUtils.getPatientId();
		String logFilePath = Main.getLogFilePath();
		EnterpriseConnection enterConn = Main.getEnterpriseConnction();
		String osmPatientId = SFDataFactory.getOSMPatientIdFromId(enterConn, patientId, logFilePath);
		objHome.setSearchBox(osmPatientId);
		objHome.clickGo();

	}

	/**
	 *The method is created to Click on Order entry guide in Patient.
	 */
	public static void click_on_Order_Data_Entry_Guide() throws InterruptedException {
		//Create Contact Page object
		objContact = new ContactPage(driver);	
		objContact.clickOrderEntryGuideLink();
		objContact.setChannel("Fax");
		objContact.setOrderable("IBC");
		objContact.setOutcome("New");
		objContact.setBillType("Medicare");
		objContact.setDescription("Test Description");
		objContact.clickContinueBtn();

	}

}