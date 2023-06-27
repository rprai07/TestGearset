package com.ghi.ui.pages;


import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class ServiceConsoleHomePage {
	
	//private static final String WebElement = null;

	/**
	 * All WebElements are identified by @FindBy annotation
	 */

	private WebDriver driver;       


	@FindBy(id="phSearchInput")
	private WebElement consoleSearch;
	//Select appMenuSelect = new Select(appMenu);         

	@FindBy(xpath="//li[contains(@id,'navigatortab__scc-pt')]")
	private List<WebElement> tabs;

	@FindBy(xpath="//li[contains(@class,'x-tab-strip-closable')]/a[contains(@id,'ext-gen') and contains(@class,'x-tab-strip-close')]")
	private WebElement closetabs;

	@FindBy(css="#Order_body tr.dataRow th a")
	private WebElement OrderClick;

	public ServiceConsoleHomePage(WebDriver driver) {
		this.driver = driver;

		//This initElements method will create all WebElements
		PageFactory.initElements(driver, this);

	}

	//Set user name in textbox
	public void setConsoleSearch(String searchObject) {

		consoleSearch.sendKeys(searchObject);     
		consoleSearch.sendKeys(Keys.ENTER);
	}


	public void openOrder(String orderNumber) {
		WebElement we = driver.findElement(By.partialLinkText(orderNumber)); 
		we.click();

	}
	public int findTabs() throws InterruptedException {
		//WebElement e = waitWithXPaths(tabs,driver,30,5);
		//driver.findElement(By.)
		Thread.sleep(10000);
		return tabs.size();
	}

	public void closeTabs(WebDriver driver) {
		WebElement ele = waitWithXPath(closetabs,driver,60,2);
		//System.out.println("tabss xpath is" + closetabs);
		Hover(driver,ele);

	}
	public static void Hover(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).click(element).build().perform();
	}

	public void clickOnOrder() {
		driver.switchTo().frame(1);
		WebElement e1 = waitWithCSS(OrderClick, driver,30,5);
		e1.click();
		//driver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS) ;

	}
	/*public static WebElsement waitWithId(String id) {

		WebElement element = wait.until(new Function() {    
		    public WebElement apply(WebDriver driver) {    
		        return driver.findElement(By.id(id));    
		    }
		});

		return element;
		}*/

	public static WebElement waitWithXPath(WebElement xpath,WebDriver driver,int timeout,int polling) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofSeconds(polling)).ignoring(NoSuchElementException.class);
		System.out.println("xapth is" +xpath);
		WebElement aboutMe=wait.until(new Function<WebDriver,WebElement>() {
			public WebElement apply(WebDriver driver) {
				System.out.println("xpathhh is -------- " +xpath);
				return xpath;
			}

		});	
		return aboutMe;
	}

	/*public static WebElement waitWithXPaths(List<WebElement> xpath,WebDriver driver,int timeout,int polling) {

			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofSeconds(polling)).ignoring(NoSuchElementException.class);
			System.out.println("xapth is" +xpath);
			WebElement aboutMe=wait.until(new Function<WebDriver,WebElement>() {
				public WebElement apply(WebDriver driver)			
				{
					System.out.println("xpathhh is -------- " +xpath);
					return xpath;
				}

			});	
			return aboutMe;
			}
	 */
	/*public static List<WebElement> waitWithXPathforListofWebElement(String xpath,WebDriver driver,int timeout,int polling) {

			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofSeconds(polling)).ignoring(NoSuchElementException.class);
			WebElement aboutMe=wait.until(new Function<WebDriver,WebElement>() {
				public WebElement apply(WebDriver driver)			
				{
					return driver.findElements(By.xpath(xpath));
				}

			});	
			return aboutMe;
			}*/

	public static WebElement waitWithCSS(WebElement css,WebDriver driver,int timeout,int polling) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofSeconds(polling)).ignoring(NoSuchElementException.class);
		WebElement aboutme = wait.until(new Function<WebDriver,WebElement>() {    
			public WebElement apply(WebDriver driver) {    
				return css;    
			}
		});

		return aboutme;
	}
}