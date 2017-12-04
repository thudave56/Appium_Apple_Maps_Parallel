import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import io.appium.java_client.*;
import io.appium.java_client.ios.*;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

/**
 * This template is for users that use DigitalZoom Reporting (ReportiumClient).
 * For any other use cases please see the basic template at https://github.com/PerfectoCode/Templates.
 * For more programming samples and updated templates refer to the Perfecto Documentation at: http://developers.perfectomobile.com/
 */
public class Maps_Wilmington {
	protected IOSDriver<WebElement> driver;
	protected ReportiumClient reportiumClient;
	@Test
	public void f() {
			PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
      	        .withProject(new Project("Open  Maps", "1.0"))
      	        .withJob(new Job("Maps_Parallel", 45))
      	        .withContextTags("parallel_appium")
      	        .withWebDriver(driver)
      	        .build();
      	   	  
			ReportiumClient reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
				
			   // Reporting client. For more details, see http://developers.perfectomobile.com/display/PD/Reporting
	        	   try {
	            
	        		reportiumClient.testStart("Open Maps - Wilmington, DE", new TestContext("davidde", "maps"));
	            // write your code here
	            switchToContext(driver, "NATIVE_APP");
	            reportiumClient.testStep("step1: Open Application");
	            Map<String, Object> openMaps = new HashMap<>();
	            openMaps.put("name", "maps");
	            driver.executeScript("mobile:application:open", openMaps);
	            
	            checkAllowLocation(driver,15);
	            
	            driver.findElementsByXPath("//*[@label=\"Search for a place or an address\"]");
	            
	            reportiumClient.testStep("step2: Enter Wilmington, DE");
	            driver.findElementByXPath("//*[@label=\"Search for a place or address\"]").sendKeys("Wilmington, DE");
	            
	            reportiumClient.testStep("step3: Validate Wilmington, DE in Results & Click");
	            driver.findElementsByXPath("//*[@label=\"Wilmington, DE, United States\"]");
	            driver.findElementByXPath("//*[@label=\"Wilmington, DE, United States\"]").click();
	            
	            reportiumClient.testStep("step4: Validate Elsmere is visible -town close to Wilm, DE");
	            driver.findElementsByXPath("//*[@label=\"Elsmere\"]");
	   
	            reportiumClient.testStep("step5: Close Maps app");
	            Map<String, Object> closeMaps = new HashMap<>();
	            closeMaps.put("name", "maps");
	            driver.executeScript("mobile:application:close", closeMaps);
	            
	            reportiumClient.testStep("step5: Click Home button");
	            driver.executeScript("mobile:handset:ready");
	
	            reportiumClient.testStop(TestResultFactory.createSuccess());
	        } catch (Exception e) {
	            reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
	            e.printStackTrace();
	            
	        }
	}
	
	@Parameters({"host", "user", "password", "deviceModel","devicePlatform","deviceManufacturer"})
	@BeforeClass
    public void beforeClass(String host, String user, String password, String deviceModel, String devicePlatform, String deviceManufacturer) throws MalformedURLException, IOException {
        System.out.println("Run started");
        
        String browserName = "mobileOS";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("host", host);
        capabilities.setCapability("user", user);
        capabilities.setCapability("password", password);
   //     capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("model", deviceModel);
        capabilities.setCapability("platformName", devicePlatform);
        capabilities.setCapability("manufacturer", deviceManufacturer);

          this.driver = new IOSDriver<WebElement>(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
          driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
          
          
	}
     


	@AfterClass
	public void afterClass() {
        try {
            driver.close();
            
            // Retrieve the URL to the DigitalZoom Report (= Reportium Application) for an aggregated view over the execution
            String reportURL = reportiumClient.getReportUrl();
            
            driver.getCapabilities().getCapability("reportPdfUrl");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
   
        driver.quit();
    System.out.println("Run ended");

	}
	
    
    private static void switchToContext(RemoteWebDriver driver, String context) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        Map<String,String> params = new HashMap<String,String>();
        params.put("name", context);
        executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
    }
    
    private static String getCurrentContextHandle(RemoteWebDriver driver) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
        return context;
    }
    
    private static List<String> getContextHandles(RemoteWebDriver driver) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
        return contexts;
    }
    protected static boolean checkAllowLocation(RemoteWebDriver driver2, int timeout) {
        By adTree = By.xpath("//*[@label=\\\"Allow\\\"]");
        Map<String, Object> adParams = new HashMap<>();
        //trying to wait for the ad to come up and then click the Expense button
        driver2.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        ((AppiumDriver<WebElement>) driver2).context("NATIVE_APP");          
        FluentWait<WebDriver> await = new FluentWait<WebDriver> (driver2)
                .withTimeout(timeout, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);
        try {
            await.until (ExpectedConditions.visibilityOf(driver2.findElement(adTree)));
            // go BACK to eliminate the popup
            adParams.clear();
            adParams.put("name", "Allow");
            driver2.executeScript("mobile:presskey", adParams);
            System.out.println("Press the OK button to close location popup");
            return true;
        } catch (Exception t) {
            System.out.println("no popup showed up");
            return false;
        }
    }
}
