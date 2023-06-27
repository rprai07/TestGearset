package com.ghi.ui.stepdefs;
import com.ghi.common.SFDataFactory;
import com.ghi.main.Main;
import com.ghi.ut.UnitTestingUtils;
import com.ghi.ui.pages.ClassicOrderPage;
import com.ghi.ui.pages.CongaComposerPage;
import com.ghi.ui.pages.HomePage;
import com.ghi.ui.pages.LoginPage;
import com.sforce.soap.enterprise.EnterpriseConnection; 

/**
 * This class is used to automate test cases related to PDF generation from orders in Classic mode
 */
public class ClassicOrderPDFStepDef extends ParentTest {
	private static LoginPage objLogin;
	private static HomePage objHome;
	//private static ContactPage objContact;
	private static ClassicOrderPage objClassicOrderPage;

	/**
	 * 
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
	 * This method is created to open the classic view of salesforce . Here we are searching order in search box
	 * and then we click on orderPDF button on order. After conga composer window pops up, we switch to that frame and
	 * generate pdf. In the end we check if the pdf is generated in Notes and Attachments or not.
	 * @throws Exception 
	 */
	public static void login_and_open_classic_view_search_order_and_generate_pdf() throws Exception {

		//Create Login Page object
		objLogin = new LoginPage(driver);
		Thread.sleep(10000);
		objLogin.login(Main.getSFUserName(), Main.getSFPassword());

		String currentURL = driver.getCurrentUrl(); 
		System.out.println(currentURL); 
		objHome = new HomePage(driver);
		if(currentURL.contains("lightning")) {
            
			 
			 objHome.clicklightningAvatarButton();
			 objHome.goToSalesforceClassic();

		} else {
			System.out.println("Not in genomichealth lightning..................");
		}
		
		Thread.sleep(20000);

		//Create Home Page object
		HomePage home= new HomePage(driver);
		String label=home.getLabel();
		if(!label.equals("GenomicHealth Sales")) 
		{
			home.clickMenuButton();
			home.gotoClassicHomeGenomicHealthSales();	   
		}
		String currentURL1 = driver.getCurrentUrl(); 
		System.out.println(currentURL1); 
		if(currentURL1.contains("lightning")) {
            
			 
			 objHome.clicklightningAvatarButton();
			 objHome.goToSalesforceClassic();

		} else {
			System.out.println("Not in genomichealth lightning..................");
		}
		home.setSearch("Search All");
		String orderId = UnitTestingUtils.getOrderId();
		EnterpriseConnection enterConn = Main.getEnterpriseConnction();
		String logFilePath = Main.getLogFilePath();
		String orderNumber = SFDataFactory.getOrderNumberFromOrderId(enterConn, orderId, logFilePath);
		home.setSearchBox(orderNumber);

		home.clickGo();
		home.searchAndClickOrder();
		//Create ClassicOrderPage object
		objClassicOrderPage = new ClassicOrderPage(driver);
		objClassicOrderPage.clickOrderPDFButton();

		//Create CongaComposerPage object
		CongaComposerPage conga = new CongaComposerPage(driver);
		conga.generateCongaPDF();
		objClassicOrderPage.clickOnNotesAndAttachmentsLink();
		objClassicOrderPage.checkPDFFound();
		home.logout();
	}
}