package com.ghi.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

	/**
	 * All WebElements are identified by @FindBy annotation
	 */
	private WebDriver driver;

	@FindBy(id="username")
	private WebElement userName;

	@FindBy(name="pw")
	private WebElement password;    

	@FindBy(name="Login")
	private WebElement login;

	public LoginPage(WebDriver driver) {

		this.driver = driver;

		//This initElements method will create all WebElements
		PageFactory.initElements(driver, this);

	}

	//Set user name in textbox
	public void setUserName(String strUserName) {
		System.out.println("Driver:=========================================== "+ driver);


		/*if(driver ==null) {
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			//wait = new WebDriverWait(driver, 60);		
			driver.get("http://test.salesforce.com");
    	}
		 */




		/*		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    	waitForVisibility(userName, 30, driver);

    	WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOf(userName));*/

		userName.sendKeys(strUserName);     
	}

	//Set password in password textbox
	public void setPassword(String strPassword) {

		password.sendKeys(strPassword);

	}

	//Click on login button
	public void clickLogin() {
		login.click();

	}  


	/**
	 * This POM method will be exposed in test case to login in the application
	 * @param strUserName
	 * @param strPasword
	 * @return
	 */
	public void login(String strUserName,String strPasword) {

		//Fill user name
		//Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(10)).pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class);

		this.setUserName(strUserName);

		//Fill password
		this.setPassword(strPasword);

		//Click Login button
		this.clickLogin();           
	}


	/*public static WebElement waitForVisibility(WebElement element, int timeToWaitInSec, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, timeToWaitInSec);
		return wait.until(ExpectedConditions.visibilityOf(element));
	}*/
}
