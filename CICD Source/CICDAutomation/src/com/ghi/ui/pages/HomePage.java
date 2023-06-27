package com.ghi.ui.pages;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

	/**
	 * All WebElements are identified by @FindBy annotation
	 */

	private WebDriver driver;

	@FindBy(name="sen")
	private WebElement search;

	@FindBy(name="sbstr")
	private WebElement searchBox;

	@FindBy(name="search")
	private WebElement goBtn;

	@FindBy(xpath="/html//span[@id='userNavLabel']")
	private WebElement dropdown;

	@FindBy(xpath="//div[@id='userNav-menuItems']/a[@title='Logout']")
	private WebElement logoutbtn; 

	@FindBy(id="tsidLabel")
	private WebElement label;

	@FindBy(id="tsidButton")
	private WebElement menubutton;

	@FindBy(xpath="//*[contains(@id,'tsid-menuItems')]/a[contains(@href,'/home/home.jsp?tsid')]")
	private List<WebElement> genomichomeList;

	@FindBy(xpath="(//table[contains(@class,'list')]//tbody//tr[contains(@class,'dataRow even last first')]//th//a)[1]")
	private WebElement clickOrder;

	@FindBy(xpath ="//*[contains(@id,'tsid-menuItems')]/a[contains(@href,'/console?tsid')]")
	private List<WebElement> consoleList;

	@FindBy(xpath="//*[contains(@class , 'uiImage')]")
	private WebElement lightningavatarbutton;
	
	@FindBy(xpath="//a[contains(.,'Switch to Salesforce Classic')]")
	private WebElement clickOnSwitchToClassicLink;

	public HomePage(WebDriver driver) {

		this.driver = driver;

		//This initElements method will create all WebElements
		PageFactory.initElements(driver, this);

	}

	//Set user name in textbox
	public void setSearch(String searchObject) throws InterruptedException {
		//Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(15)).pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
		Thread.sleep(10000);
		search.sendKeys(searchObject);     
	}


	public void setSearchBox(String searchObject) {
		searchBox.sendKeys(searchObject);     
	}

	/*	//Set password in password textbox
	public void setPassword(String strPassword) {
		password.sendKeys(strPassword);
	}*/

	/*	//Click on login button
	public void clickLogin() {
		login.click();
	}  */


	//Click on Go button
	public void clickGo() {
		goBtn.click();
	}

	public void logout() {
		// Waiting 30 seconds for an element to be present on the page, checking
		// for its presence once every 5 seconds.
		//Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(30)).pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
		dropdown.click();
		logoutbtn.click();
		driver.close();
	}
	public String getLabel() throws InterruptedException {
		//Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(20, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		Thread.sleep(10000);
		WebElement eie= driver.findElement(By.xpath("/html//span[@id='tsidLabel']"));
		return eie.getText();
	}

	public void clickMenuButton() { 
		System.out.println("clicking menu button");
		//Thread.sleep(5000);
		menubutton.click();
	}
	
	
	public void clicklightningAvatarButton() throws InterruptedException { 
		System.out.println("clicking avatar button");
		Thread.sleep(10000);
		lightningavatarbutton.click();
	}
	
	public void goToSalesforceClassic() { 
		System.out.println("clicking on switch to classic link ");
		//Thread.sleep(5000);
		clickOnSwitchToClassicLink.click();
	}
	

	public void gotoClassicHomeGenomicHealthSales() throws InterruptedException {
		List<WebElement> drop= genomichomeList;
		//System.out.println("trying to click classic home");
		java.util.Iterator<WebElement> i = drop.iterator();
		Thread.sleep(10000);
		while(i.hasNext()) {
			WebElement row = i.next();
			try {
				
				if(row.getText().contains("GenomicHealth Sales")) {
					System.out.println("row is"+ row.getText());
					row.sendKeys(Keys.ENTER);
				//	row.click();
					
					break;
				}
			}
			catch(Exception e) {
				System.out.println("exception " +e);
				throw e;
			}
		} 
	}
	public String getCurrentUrl() throws InterruptedException {
		//Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(20)).pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
		Thread.sleep(5000);
		return driver.getCurrentUrl();
	}


	public void searchAndClickOrder() {
		if(driver.getCurrentUrl().contains("search")) {
			clickOrder.click();		    
		}
	}

	public void switchToConsole() throws InterruptedException {
		Thread.sleep(10000);
		List<WebElement> drop = consoleList;
		//System.out.println("drops is "+drop);

		java.util.Iterator<WebElement> i = drop.iterator();
		while(i.hasNext()) {
			WebElement row = i.next();
			//String values = row.getText();
			System.out.println(row.getText());

			if(row.getText().contains("OSM Service Console")) {
				//System.out.println("going to click");
				row.click();
				break;
			}
		} 
	}

	/*//Set password in password textbox
	public void setAppMenu(String strAppName) {
		Select appMenuSelect = new Select(appMenu);
		appMenuSelect.selectByVisibleText(strAppName);
		//appMenu.sendKeys(strAppName);

	}*/
}