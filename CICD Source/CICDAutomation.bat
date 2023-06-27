ECHO CICD Automation going to start...
::Please provide 12 arguments in double quotes(jar location,apex script folder location, working directory, log folder location, build number, execution series, username, password and environment(sandbox/production), ICRT username , ICRT password , Chrome driver location)

:: 1.) <Jar Location> is the location of CICDAutomation jar on your machine in double quotes.                                                                                                                  
:: 2.) <apex script folder location> is the location of apex scripts folder on your machine in double quotes.
:: 3.) <Current working directory> is the location of current working directory (having settings.properties) on your machine in double quotes.
:: 4.) <Log folder location> is the location of log folder on your machine in double quotes.
:: 5.) <Build Number> is the number of current build being deployed in double quotes.
:: 6.) <Execution Series> is used to distinguish FULL/Selective scripts run. Use "FULL" for full run. Use series for selective script run for given steps e.g. "2-4,6".Use hyphen("-") for sequences separated by comma (",") to make execution series for selective runs. 
:: 7.) <Username> is username for target salesforce environment e.g. abc@genomichealth.com.devXYZ.
:: 8.) <Password> is password for target salesforce environment.
:: 9.) <Environment> environment (sandbox or production)
:: 10.) <ICRTUserName> is ICRT Username - This is required for Unit Testing(UT/UI) only
:: 11.) <ICRTPassword> is ICRT Password - This is required for Unit Testing(UT/UI) only
:: 12.) <Chrome Driver Location> is the location of driver installed on your machine - This is required for Unit Testing(UT/UI) only


java -jar "E:\jar\CICDAutomation.jar" "E:\Gabilan\march2019release\Automation\resources\apexFolder" "D:\CICD Automation" "D:\logs" "cicdtest" "22" "maggarwal@genomichealth.com.dev35" "yourpassword" "sandbox" "icrtsfdcintegration@dev35.ghi" "icrtpassword" "E:\softwares\jars\chromedriver_win32\chromedriver.exe"

ECHO CICD Automation completed !!
EXIT /B %ERRORLEVEL%