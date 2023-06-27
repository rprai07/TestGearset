package com.ghi.ui.pages;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CongaComposerPage {

	/**
	 * All WebElements are identified by @FindBy annotation
	 */

	private WebDriver driver;

	//@FindBy(xpath="//span[text()='Domestic - Prostate (GHI007)']")    
	//@FindBy(xpath ="//tr[@data-recordindex='0']")  

	@FindBy(xpath="//div[@id='apxt-c8-select-template-grd-body']//table[@role='presentation']/tbody/tr[1]/td[1]/div")
	private WebElement template;

	@FindBy(id="apxt-c8-multiselect-next")    		
	private WebElement nextBtn;		


	@FindBy(xpath ="//*[@data-qtip='Continue with the Composer Operation.']")    		
	private WebElement continueBtn;	


	public CongaComposerPage(WebDriver driver) {

		this.driver = driver;

		//This initElements method will create all WebElements
		PageFactory.initElements(driver, this);
	}


	//Select template
	public void selectTemplate() {
		
		Actions actions = new Actions(driver);    	
		actions.doubleClick(template).perform();
	}


	//Click Next button
	public void clickNextBtn() {
		nextBtn.click();
	}


	//Click Continue button
	public void clickContinueBtn() {
		continueBtn.click();
	}   

	public void generateCongaPDF() throws Exception {

		String parentWinHandle = driver.getWindowHandle();
		Set<String> winHandles = driver.getWindowHandles();
		
		// Loop through all handles
		for(String handle: winHandles) {	        	
			try {
				if(!handle.equals(parentWinHandle)) {
					Thread.sleep(15000);
					driver.switchTo().window(handle);
					Thread.sleep(15000);
					driver.manage().timeouts().implicitlyWait(25,TimeUnit.SECONDS) ;
					String windlowTitle = driver.getTitle();

					if(windlowTitle!=null && windlowTitle.contains("Conga Composer")) {
						Thread.sleep(15000);
						//driver.manage().timeouts().implicitlyWait(50,TimeUnit.SECONDS);

						selectTemplate();
						clickNextBtn();
						Thread.sleep(15000);
						//driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
						clickContinueBtn();
						Thread.sleep(10000);
						// driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
						// driver.close();
					}
				}	
			}
			catch(Exception ex) {
				System.out.println("exception "+ ex);
				throw ex;
				
			}
		}
		driver.switchTo().window(parentWinHandle);

	}
}