package com.ghi.ui.stepdefs;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.ghi.main.Main;

public class ParentTest {

	public static WebDriver driver;
	public static final String BASE_URL = "http://test.salesforce.com";
	//LoginPage objLogin;
	//WebDriverWait wait = null;
	//HomePage objHome;
	//ContactPage objContact;

	public ParentTest() {
		try {
			System.setProperty("webdriver.chrome.driver", Main.getDriverPath());

		} catch (Exception e) {
			System.out.println("Exception:================================================================= "+e.getMessage());
		}

		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		System.out.println("Driver !! :::::::: ============================================"+ driver);
	}


	/*	//@BeforeTest
	public void setup() {
		System.out.println("INSIDE SETUP !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		try {

			Properties p=new Properties();  
			String chromeDriverPath = p.getProperty("driver_path");
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);


		} catch (Exception e) {
			System.out.println("Exception:================================================================= "+e.getMessage());
		}
	}*/



	/*	//@AfterClass
	public void tearDown()
	{
		//testRunner.finish();
	}*/
}