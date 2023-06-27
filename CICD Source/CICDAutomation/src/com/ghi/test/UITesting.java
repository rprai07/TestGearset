package com.ghi.test;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import com.ghi.ui.stepdefs.ClassicOrderPDFStepDef;
import com.ghi.ui.stepdefs.OrderEntryGuideStepDef;
import com.ghi.ui.stepdefs.ConsoleOrderPDFStepDef;


/**
 * This class is used to create UI automation testing
 * @author maaggarwal
 *
 */
public class UITesting {
	
	private UITesting() {
		
	}
	

	//private static String testPkg = "com.ghi.test";
	/*public static void execute(String logFilePath, String featureFolder) {		
		try {

			SFUtility.log("**************************************************  START  ****************************************************************************", logFilePath); 
			SFUtility.printTime(logFilePath);				
			SFUtility.log("Start..", logFilePath); 

			executeMethodWithGivenAnnotation("Run Data Entry Guide");
			executeMethodWithGivenAnnotation("Click On Order PDF Button");

			SFUtility.log("**************************************************  END  ****************************************************************************", logFilePath);
			SFUtility.printTime(logFilePath);

		} catch (Throwable e) { 
			System.out.println("Exception: "+e.getMessage());
			System.exit(1);
		}
		SFUtility.printTime(logFilePath);	
	}*/


	@Step("Run Data Entry Guide From Classic")
	public static void runDataEntryGuide() throws Throwable {
		try {
			System.out.println("runDataEntryGuide  1 ");
			OrderEntryGuideStepDef.open_the_Chrome_and_launch_salesforce();
			System.out.println("runDataEntryGuide  2 ");
			OrderEntryGuideStepDef.login_and_open_contact();
			System.out.println("runDataEntryGuide  3 ");
			OrderEntryGuideStepDef.click_on_Order_Data_Entry_Guide();
			
		} catch (Throwable e) { 
			System.out.println("Exception:==============================> "+e.getMessage());	
			throw e;
		}

	}


	@Step("Click On Order PDF Button From Console")
	public static void clickOnOrderPDFButton() throws Throwable {		
		try {
			ConsoleOrderPDFStepDef.open_the_Chrome_and_launch_salesforce();
			ConsoleOrderPDFStepDef.login_and_open_service_console_and_search_order();
		}  catch (Throwable e) { 
			System.out.println("Exception: "+e.getMessage());
			throw e;
		}	
	}
	
	@Step("Click On Order PDF Button From Classic")
	public static void clickOnOrderPDFButtonFromClassic() throws Throwable {		
		try {
			ClassicOrderPDFStepDef.open_the_Chrome_and_launch_salesforce();
			ClassicOrderPDFStepDef.login_and_open_classic_view_search_order_and_generate_pdf();
			
		}  catch (Throwable e) { 
			System.out.println("Exception: "+e.getMessage());
			throw e;
		}	
	}
	
	

	public static boolean isAnnotation( String value1) throws Exception {

		boolean isAnnotation =  false;

		Method methods[] = UITesting.class.getMethods();		
		for(Method method : methods) {

			if(method.isAnnotationPresent(Step.class)) {

				Annotation ann[] = method.getAnnotations();
				if(ann.length > 0) {					
					if(ann[0].annotationType().isAssignableFrom(Step.class)) {

						String value = ((Step)ann[0]).value();						
						if(value.equals(value1)) {
							isAnnotation = true;
							break;
						}
					}
				}
			}			
		}

		return isAnnotation;
	}

	public static void executeMethodWithGivenAnnotation( String value1) throws Exception {

		Method methods[] = UITesting.class.getMethods();		
		for(Method method : methods) {
           System.out.println("in executewith annotation");
			if(method.isAnnotationPresent(Step.class)) {	

				Annotation ann[] = method.getAnnotations();
				if(ann.length>0) {
					//System.out.println(ann[0].toString());
					if(ann[0].annotationType().isAssignableFrom(Step.class)) {

						String value = ((Step)ann[0]).value();
						//System.out.println("value: "+value);

						if(value.equals(value1)) {
							//System.out.println("method11: "+ method.getName());
							method.invoke(null, null);
							//isExecuted = true;
							break;
						}
					}
				}
			}
		}
	}
}