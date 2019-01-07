package androidApp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.appium.java_client.android.AndroidDriver;

public class Android_app {
	public static AndroidDriver<WebElement> driver = null;

	@Given("^User starts a session on android device$")
	public void start_an_android_app_session() throws MalformedURLException {
		String username = System.getenv("KOBITON_USERNAME");
		String apiKey = System.getenv("KOBITON_API_KEY");

		String deviceUdid = System.getenv("KOBITON_DEVICE_UDID");
		String deviceName = System.getenv("KOBITON_DEVICE_NAME");
		
		String deviceOrientation = System.getenv("KOBITON_SESSION_DEVICE_ORIENTATION");
		String captureScreenshots = System.getenv("KOBITON_SESSION_CAPTURE_SCREENSHOTS");
		String deviceGroup = System.getenv("KOBITON_SESSION_DEVICE_GROUP");
		String app = System.getenv("KOBITON_SESSION_APPLICATION_URL");
		String platformVersion = System.getenv("KOBITON_SESSION_PLATFORM_VERSION");
		String groupId = System.getenv("KOBITON_SESSION_GROUP_ID");

		URL kobitonServerUrl = new URL("https://" + username + ":" + apiKey + "@api.kobiton.com/wd/hub");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("sessionName", "Automation test android app session");
		capabilities.setCapability("sessionDescription", "Automation test android app session"); 
		capabilities.setCapability("deviceOrientation", ((deviceOrientation == null) ? "portrait" : deviceOrientation));  
		capabilities.setCapability("captureScreenshots", Boolean.parseBoolean((captureScreenshots == null) ? "true" : captureScreenshots)); 
		capabilities.setCapability("app", ((app == null) ? "https://s3-ap-southeast-1.amazonaws.com/kobiton-devvn/apps-test/demo/iFixit.apk" : app)); 
		capabilities.setCapability("deviceGroup", ((deviceGroup == null) ? "KOBITON" : deviceGroup));
		capabilities.setCapability("platformName", "Android");
		
		if (deviceUdid != null) {
			capabilities.setCapability("deviceUdid", deviceUdid);
		}
		else {
			capabilities.setCapability("deviceName", ((deviceName == null) ? "Galaxy S7" : deviceName));
			capabilities.setCapability("platformVersion", platformVersion);
		}
		
		if (groupId != null) {
			capabilities.setCapability("groupId", groupId);
		}

		driver = new AndroidDriver<WebElement>(kobitonServerUrl, capabilities);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}

	@Given("^User goes to Home page$")
	public void go_to_Home_Page() {
		WebElement homepage = driver.findElementByXPath("//android.widget.TextView[@text='iFixit']");
		homepage.click();
		sleep(2);
	}

	@And("^User clicks on Car and Truck category$")
	public void go_to_Car_and_Truck_category() {
		driver.findElementByXPath("//android.widget.TextView[@text='Car and Truck']").click();
	}

	@And("^User clicks on Acura category$")
	public void go_to_Acura_cateogry() {
		driver.findElementByXPath("//android.widget.TextView[@text='Acura']").click();
		sleep(2);
	}

	@And("^User waits for General Information$")
	public void wait_for_general_infor() {
		(new WebDriverWait(driver, 30))
	      .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.TextView[@text='General Information']")));
	}

	@Then("^User see items display: Acura Integra, Acura MDX, Acura RL, Acura TL, Acura TSX$")
	public void verify_five_items() {
	    boolean hasAcuraIntegra = driver.findElementByXPath("//android.widget.TextView[@text='Acura Integra']")
	      .isDisplayed();
	    boolean hasAcuraMDX = driver.findElementByXPath("//android.widget.TextView[@text='Acura MDX']").isDisplayed();
	    boolean hasAcuraRL = driver.findElementByXPath("//android.widget.TextView[@text='Acura RL']").isDisplayed();
	    boolean hasAcuraTL = driver.findElementByXPath("//android.widget.TextView[@text='Acura TL']").isDisplayed();
	    boolean hasAcuraTSX = driver.findElementByXPath("//android.widget.TextView[@text='Acura TSX']").isDisplayed();
	    
	    Assert.assertEquals(hasAcuraIntegra, true);
	    Assert.assertEquals(hasAcuraMDX, true);
	    Assert.assertEquals(hasAcuraRL, true);
	    Assert.assertEquals(hasAcuraTL, true);
	    Assert.assertEquals(hasAcuraTSX, true);
	}
	
	@Given("^User ends session on Android device$")
	public void end_an_android_app_session() {
		try {
			if (driver != null)
				driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sleep(int seconds) {
	    try {
	      Thread.sleep(seconds * 1000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	  }
}