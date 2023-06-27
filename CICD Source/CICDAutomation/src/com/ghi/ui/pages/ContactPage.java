package com.ghi.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ContactPage {
	/**
	 * All WebElements are identified by @FindBy annotation
	 */

	private WebDriver driver;


	//@FindBy(id="ae-pb-guide-0-0-l", className ="ae-pb-guide-link")

	//@FindBy(xpath="span[@id='ae-pb-guide-0-0-l']") 
	@FindBy(id="ae-pb-guide-0-0-l") 
	private WebElement orderEntryGuideLink;


	@FindBy(xpath ="//*[@data-aesf-data-name='screen.OSM_Channel__c']")
	private WebElement channel;		 

	@FindBy(xpath ="//*[@data-aesf-data-name='screen.OSM_Product__c']")
	private WebElement orderable;


	@FindBy(xpath ="//*[@data-aesf-data-name='screen.OSM_Triage_Outcome__c']")
	private WebElement outcome;


	@FindBy(xpath ="//*[@data-aesf-data-name='screen.OSM_Bill_Type__c']")
	private WebElement billType;


	@FindBy(xpath ="//*[@data-aesf-data-name='screen.Description']")
	private WebElement description;


	@FindBy(xpath ="//*[@class='aesf-theme-answer-btn aesf-theme-btn ae-sf-viewer-screen-button aesf-jqui-btn ui-state-default ui-button-text-only ui-button ui-corner-all ui-widget']")
	private WebElement continueBtn;


	@FindBy(name="sbstr")
	private WebElement searchBox;

	@FindBy(name="search")
	private WebElement goBtn;

	@FindBy(name="pw")
	private WebElement password;    

	//@FindBy(className="barone")
	// WebElement titleText;

	@FindBy(name="Login")
	private WebElement login;



	public ContactPage(WebDriver driver) {

		this.driver = driver;

		//This initElements method will create all WebElements

		PageFactory.initElements(driver, this);

	}

	//Set user name in textbox

	/* public void setSearch(String searchObject){

 	search.sendKeys(searchObject);     
 }*/


	public void setSearchBox(String searchObject) {

		searchBox.sendKeys(searchObject);     
	}

	//Set password in password textbox

	public void setPassword(String strPassword){

		password.sendKeys(strPassword);

	}

	//Click on Order Entry Guide Link

	public void clickOrderEntryGuideLink() throws InterruptedException {

		Thread.sleep(15000);
		driver.switchTo().frame(2);
		driver.switchTo().frame(0);	
		orderEntryGuideLink.click();

	}  


	//Click on Go button

	public void clickGo() {

		goBtn.click();

	}


	//Click on Continue button on 'Order Data Entry' Guide

	public void clickContinueBtn() throws InterruptedException {

		continueBtn.click();
		Thread.sleep(5000);
		System.out.println("test case passed");
		driver.close();

	}



	public void setChannel(String channel) throws InterruptedException {

		Thread.sleep(20000);
		//wait.until(ExpectedConditions.visibilityOfElementLocated("urelement"));
		driver.switchTo().frame(0);
		Thread.sleep(20000);

		//printAll();
		driver.switchTo().frame(0);
		this.channel.sendKeys(channel);     
	}




	public void setOrderable(String orderable) {

		this.orderable.sendKeys(orderable);     
	}




	public void setOutcome(String outcome) {

		this.outcome.sendKeys(outcome);     
	}



	public void setBillType(String billType) {

		this.billType.sendKeys(billType);     
	}





	public void setDescription(String description) {

		this.description.sendKeys(description);     
	}



}
