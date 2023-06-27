package com.ghi.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
public class ServiceConsoleOrderPage  {

	/*
	 * All WebElements are identified by @FindBy annotation
	 */
	private WebDriver driver;

	@FindBy(name="osm_order_pdf")
	private WebElement orderPDFBtn;


	//xpath=//a[contains(@href,'listDetails.do') 	
	//@FindBy(xpath ="//@href[contains(text('RelatedNoteList']")	
	//@FindBy(xpath ="//a[contains(@href, 'RelatedNoteList')]")


	@FindBy(partialLinkText ="Attachments")	
	private WebElement notesAndAttachmentsLink;

	@FindBy(xpath="/html//span[@id='userNavLabel']")
	private WebElement dropdown;

	@FindBy(xpath="//div[@id='userNav-menuItems']/a[@title='Logout']")
	private WebElement logoutbtn; 


	public ServiceConsoleOrderPage(WebDriver driver) {
		this.driver = driver;

		//This initElements method will create all WebElements
		PageFactory.initElements(driver, this);

	}

	//Click on 'Order PDF' button
	public void clickOrderPDFButton() throws InterruptedException {
		// driver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS) ;
		Thread.sleep(12000);
		driver.switchTo().parentFrame();
		//WebElement e2 =waitWithName(orderPDFBtn,driver,60,5);
		driver.switchTo().frame(2);
		/*WebDriverWait wait = new WebDriverWait(driver, 6000);
			WebElement element = driver.findElement(By.name("osm_order_pdf"));
			element.click();
		 */
		Thread.sleep(10000);
		//System.out.println("clicking order psdf order...........");
		orderPDFBtn.click();



	}

	//Click on attachments link
	public void clickOnNotesAndAttachmentsLink() {

		driver.navigate().refresh();
		driver.switchTo().frame(1); 

		notesAndAttachmentsLink.click();

	}

	public void logout() throws InterruptedException
	{
		// Waiting 30 seconds for an element to be present on the page, checking
		// for its presence once every 5 seconds.
		Thread.sleep(10000);
		driver.switchTo().parentFrame();
		dropdown.click();
		logoutbtn.click();
		driver.close();
	}

	public void vertifyPDFexist() throws InterruptedException 
	{
		Thread.sleep(10000);
		WebElement pdflink = driver.findElement(By.partialLinkText(".pdf"));
		System.out.println("element found, test case passed...");
	}

	/*public static WebElement waitWithName(WebElement name,WebDriver driver,int timeout,int polling) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofSeconds(polling)).ignoring(NoSuchElementException.class);
		System.out.println("xapth is" +name);
		WebElement aboutMe=wait.until(new Function<WebDriver,WebElement>() {
			public WebElement apply(WebDriver driver)			
			{
				System.out.println("xpathhh is -------- " +name);
				return name;
			}

		});	
		return aboutMe;
	}*/
}
