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

public class OrderPDFStepDef extends ParentTest {   


	private static LoginPage objLogin;
	private static HomePage objHome;
	//private static ContactPage objContact;	
	private static ServiceConsoleHomePage objConsolePage;

	//@Given("^Open the Chrome browser window and launch salesforce app$")
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
		//wait = new WebDriverWait(driver, 60);		
		ParentTest test=new ParentTest();
		driver.get(BASE_URL);

		System.out.println("Driver !! :::::::: ============================================"+ driver);


	}

	//@When("^Login and open service console and search order$")
	public static void login_and_open_service_console_and_search_order() throws Exception {

		//try {


		//login to application
		objLogin = new LoginPage(driver);

		Thread.sleep(10000);
		objLogin.login(Main.getSFUserName(), Main.getSFPassword());

		String currentURL = driver.getCurrentUrl(); 
		System.out.println(currentURL); 

		Thread.sleep(20000);
		if(!currentURL.contains("console")) {

			System.out.println("Opening Console....................");
			objHome = new HomePage(driver);
			objHome.clickMenuButton();
			objHome.switchToConsole();


		} else {
			System.out.println("already in console..................");
		}


		objConsolePage = new ServiceConsoleHomePage(driver);

		int tabs=objConsolePage.findTabs();
		System.out.println("no if tabs are====================" +tabs);
		if(tabs>0)
		{
			int i;
			for(i=0;i<tabs;i++)
			{
				objConsolePage.closeTabs(driver);		  
			}
		}

		String orderId = UnitTestingUtils.getOrderId();
		EnterpriseConnection enterConn = Main.getEnterpriseConnction();
		String logFilePath = Main.getLogFilePath();
		String orderNumber = SFDataFactory.getOrderNumberFromOrderId(enterConn, orderId, logFilePath);

		//objConsolePage.setConsoleSearch("OR001203503");
		objConsolePage.setConsoleSearch(orderNumber);
		objConsolePage.clickOnOrder();
		System.out.println("opening order...........");
		ServiceConsoleOrderPage consoleOrder = new ServiceConsoleOrderPage(driver);
		consoleOrder.clickOrderPDFButton();
		CongaComposerPage congaPage = new CongaComposerPage(driver);
		congaPage.generateCongaPDF();
		consoleOrder.clickOnNotesAndAttachmentsLink();
		consoleOrder.vertifyPDFexist();
		consoleOrder.logout();


		//commented by p

		/*for (String windows : driver.getWindowHandles()) {

			System.out.println("window: "+ windows);
			//driver.switchTo().window(windows);	    	
		}

		//printAll(); 

		//driver.switchTo().frame(1);	
		String title = driver.switchTo().frame(1).getTitle();
		System.out.println("title: "+title);

		//objConsolePage.openOrder("OR001203503");

		objConsolePage.openOrder("OR");

		System.out.println("order opened...........");
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		driver.switchTo().parentFrame();
		driver.switchTo().frame(2);
		ServiceConsoleOrderPage orderPage = new ServiceConsoleOrderPage(driver);
		orderPage.clickOrderPDFButton();

		Thread.sleep(30000);

		// Get current window handle
		String parentWinHandle = driver.getWindowHandle();

		Set<String> winHandles = driver.getWindowHandles();
		// Loop through all handles
		for(String handle: winHandles){

			System.out.println("handle:===================================================== " + handle);

			if(!handle.equals(parentWinHandle)) {
				driver.switchTo().window(handle);
				Thread.sleep(1000);
				String windlowTitle = driver.getTitle();
				System.out.println("Title of the new window: " +windlowTitle);

				if(windlowTitle!=null && windlowTitle.contains("Conga Composer")) { 

					CongaComposerPage congaPage = new CongaComposerPage(driver);

					Thread.sleep(20000);
					congaPage.selectTemplate();
					System.out.println("template selected......................");
					System.out.println("Clicking next button ......................");

					congaPage.clickNextBtn();					
					congaPage.clickContinueBtn();

				}			
			}
		}	

		driver.switchTo().window(parentWinHandle);		 
		printAll();

		driver.navigate().refresh();
		driver.switchTo().frame(1);		
		Thread.sleep(5000);

		orderPage.clickOnNotesAndAttachmentsLink();

		System.out.println("found attachments link...............");

			WebElement pdfLink = driver.findElement(By.partialLinkText(".pdf"));
			System.out.println("element found, test case passed...");


			driver.switchTo().parentFrame();
			Thread.sleep(3000);
		 	WebElement eles= driver.findElement(By.xpath("//li[contains(@class,'x-tab-strip-closable x-tab-strip-active')]/a[contains(@id,'ext-gen') and contains(@class,'x-tab-strip-close')]"));
			Hover(driver,eles);

			Thread.sleep(3000);
	        driver.switchTo().parentFrame();
	        driver.findElement(By.xpath("//*[contains(@id,'navigatortab__scc')]")).click();
	        System.out.println("tab switched to 1 ");
	        driver.navigate().refresh();
	        driver.switchTo().parentFrame();

	     //	Thread.sleep(15000);
	        WebElement eles3= driver.findElement(By.xpath("(//span[contains(@class,'x-tab-strip-text')]//span[contains(@class,'tabText')])[1]"));
	        WebElement eles2= driver.findElement(By.xpath("//li[contains(@class,'x-tab-strip-closable')]/a[contains(@id,'ext-gen') and contains(@class,'x-tab-strip-close')]"));
	        HoverAndClick(driver,eles3,eles2);
	     	//Thread.sleep(10000);
	        System.out.println("crossed");
			 //Thread.sleep(10000);

			//System.out.println("tab found");

			driver.switchTo().parentFrame();
			WebElement ele9 = driver.findElement(By.xpath("/html//span[@id='userNavLabel']"));
			 ele9.click();

			 WebElement ele10 = driver.findElement(By.xpath("/html//a[@id='app_logout']"));
			 ele10.click();
			// Thread.sleep(5000);
			driver.close();
		 */


	}




	//@Then("^click on Order PDF button$")
	public static void click_on_Order_PDF_button() throws Throwable {




	}

	/*@And("^you are in And annotation$")
  public void and() throws Throwable {
  }*/

	/*@But("^you are in But annotation$")
  public void but() throws Throwable {
  }*/
	/*

	public static void printAll() {


		System.out.println("========================================= Inside printAll() method =====================================================");

		driver.findElements(By.tagName("a"));
		List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@type='checkbox']")); 
		System.out.println(checkboxes.size());

		for(int i =0;i<checkboxes.size();i++) {
			String elementText = checkboxes.get(i).getText(); 
			System.out.println(elementText); 
		}




		List<WebElement> dropdown = driver.findElements(By.tagName("select"));  
		System.out.println("selects: "+ dropdown.size());
		for(int i =0;i<dropdown.size();i++) {
			String elementText = dropdown.get(i).getText(); 
			System.out.println(elementText); 
		}

		List<WebElement> frames  = driver.findElements(By.tagName("iframe"));
		for(int i =0;i<frames.size();i++) {
			String elementText = frames.get(i).getText(); 
			System.out.println(elementText); 
		}




		List<WebElement> listOfElements = driver.findElements(By.xpath("//div"));


		for(int i = 0; i < listOfElements.size(); i++) {
			String elementText = listOfElements.get(i).getText(); 
			System.out.println(elementText); 
		}


		List<WebElement> textboxes = driver.findElements(By.xpath("//input[@type='text'[@class='inputtext']")); 

	    	 System.out.println(textboxes.size());


	    	List<WebElement> listElement = driver.findElements(By.cssSelector("*"));
	    	for(int i =0;i<listElement.size();i++) {
	    	 String elementText = listElement.get(i).getText(); 
	    	 System.out.println(elementText); 
	    	}
	}
	 */
	/*	public static void Hover(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).click(element).build().perform();
	}
	public static void HoverAndClick(WebDriver driver,WebElement elementToHover,WebElement elementToClick) {
		Actions action = new Actions(driver);
		action.moveToElement(elementToHover).click(elementToClick).build().perform();
	}*/
}