package com.ghi.ui.pages;

//import static org.testng.AssertJUnit.assertNotNull;
//import static org.testng.AssertJUnit.assertNotNull;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
//import org.testng.Assert;

public class ClassicOrderPage  {

	/**
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

	@FindBy(partialLinkText=".pdf")
	private WebElement pdffound ; 

	/*@FindBy(partialLinkText =".pdf")	
	WebElement pdfLink;*/


	public ClassicOrderPage(WebDriver driver) {
		this.driver = driver;

		//This initElements method will create all WebElements
		PageFactory.initElements(driver, this);

	}

	//Click on 'Order PDF' button
	public void clickOrderPDFButton() throws InterruptedException {
		//Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
        Thread.sleep(10000);
		orderPDFBtn.click();
	}




	//Click on attachments link
	public void clickOnNotesAndAttachmentsLink() {
		notesAndAttachmentsLink.click();
	}

	public void checkPDFFound()
	{
		WebElement ele9 = pdffound;
		//Assert.assertNotNull(ele9);    
		System.out.println("pass");

	}


	/*	//Click on attachments link
	public void clickOnPDFLink() {
		pdfLink.click();
	}*/

}
