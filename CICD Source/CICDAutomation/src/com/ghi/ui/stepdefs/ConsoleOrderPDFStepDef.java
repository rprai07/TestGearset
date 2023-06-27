package com.ghi.ui.stepdefs;
import com.ghi.common.SFDataFactory;
import com.ghi.main.Main;
import com.ghi.ut.UnitTestingUtils;
import com.ghi.ui.pages.CongaComposerPage;
import com.ghi.ui.pages.HomePage;
import com.ghi.ui.pages.LoginPage;
import com.ghi.ui.pages.ServiceConsoleHomePage;
import com.ghi.ui.pages.ServiceConsoleOrderPage;
import com.sforce.soap.enterprise.EnterpriseConnection;

/**
 * This class is used to automate test cases related to PDF generation from orders in Salesforce Console view
 */
public class ConsoleOrderPDFStepDef extends ParentTest {   


	private static LoginPage objLogin;
	private static HomePage objHome;
	//private static ContactPage objContact;	
	private static ServiceConsoleHomePage objConsolePage;


	/**
	 * 	This method is created to open the Chrome and launch Salesforce
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
	 * This method is used to Login and open service console and search order.
	 * After Order is successfully found, we will click on orderPDF button on order.
	 * After conga composer window pops up, we switch to that frame and generate pdf. 
	 * In the end we check if the pdf is generated in Notes and Attachments or not.
	 * @throws Exception 
	 */
	public static void login_and_open_service_console_and_search_order() throws Exception {

		//Create object of login page
		objLogin = new LoginPage(driver);

		Thread.sleep(10000);
		objLogin.login(Main.getSFUserName(), Main.getSFPassword());

		String currentURL = driver.getCurrentUrl(); 
		System.out.println(currentURL); 

		Thread.sleep(20000);
		objHome = new HomePage(driver);

		if(currentURL.contains("lightning")) {
             objHome.clicklightningAvatarButton();
	        objHome.goToSalesforceClassic();

		} else {
			System.out.println("already in classic..................");
		}
		
		if(!currentURL.contains("console")) {

			//Create object of Home page
			objHome.clickMenuButton();
			objHome.switchToConsole();

		} else {
			System.out.println("already in console..................");
		}

		//Create Service console home page object
		objConsolePage = new ServiceConsoleHomePage(driver);

		int tabs=objConsolePage.findTabs();
		System.out.println("no if tabs are====================" +tabs);
		if(tabs > 0) {
			int i;
			for(i = 0; i < tabs; i++) {
				objConsolePage.closeTabs(driver);		  
			}
		}

		String orderId = UnitTestingUtils.getOrderId();
		EnterpriseConnection enterConn = Main.getEnterpriseConnction();
		String logFilePath = Main.getLogFilePath();
		String orderNumber = SFDataFactory.getOrderNumberFromOrderId(enterConn, orderId, logFilePath);

		objConsolePage.setConsoleSearch(orderNumber);
		objConsolePage.clickOnOrder();
		System.out.println("opening order...........");
		ServiceConsoleOrderPage consoleOrder = new ServiceConsoleOrderPage(driver);
		consoleOrder.clickOrderPDFButton();

		//Create Conga composer page object
		System.out.println("clicked order pdf button order...........");
		CongaComposerPage congaPage = new CongaComposerPage(driver);
		congaPage.generateCongaPDF();
		consoleOrder.clickOnNotesAndAttachmentsLink();
		consoleOrder.vertifyPDFexist();
		consoleOrder.logout();

	}
}