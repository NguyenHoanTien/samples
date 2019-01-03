package iosWeb;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.appium.java_client.ios.IOSDriver;

public class Ios_web {
	public static IOSDriver<WebElement> driver = null;

	@Given("^User starts a session on iOS device$")
	public void start_an_ios_web_session() throws MalformedURLException {
		String deviceUdid = System.getenv("KOBITON_DEVICE_UDID");
		String deviceName = System.getenv("KOBITON_DEVICE_NAME");
		
		String deviceOrientation = System.getenv("KOBITON_SESSION_DEVICE_ORIENTATION");
		String captureScreenshots = System.getenv("KOBITON_SESSION_CAPTURE_SCREENSHOTS");
		String deviceGroup = System.getenv("KOBITON_SESSION_DEVICE_GROUP");
		String browserName = System.getenv("KOBITON_SESSION_BROWSER_NAME");
		String platformVersion = System.getenv("KOBITON_SESSION_PLATFORM_VERSION");
		String groupId = System.getenv("KOBITON_SESSION_GROUP_ID");

		URL kobitonServerUrl = new URL("https://" + username + ":" + apiKey + "@api.kobiton.com/wd/hub");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("sessionName", "Automation test ios app session");
		capabilities.setCapability("sessionDescription", "Automation test ios app session"); 
		capabilities.setCapability("deviceOrientation", ((deviceUdid == null) ? "portrait" : deviceUdid));  
		capabilities.setCapability("captureScreenshots", Boolean.parseBoolean((captureScreenshots == null) ? "true" : captureScreenshots)); 
		capabilities.setCapability("browserName", ((browserName == null) ? "safari" : browserName)); 
		capabilities.setCapability("deviceGroup", ((deviceGroup == null) ? "KOBITON" : deviceGroup));
		capabilities.setCapability("platformName", "iOS");
		
		if (!isEmpty(deviceUdid)) {
			capabilities.setCapability("deviceUdid", deviceUdid);
		}
		else {
			capabilities.setCapability("deviceName", ((deviceName == null) ? "iPad mini 2G (Cellular)" : deviceName));
			capabilities.setCapability("platformVersion", platformVersion);
		}
		
		if (!isEmpty(groupId)) {
			capabilities.setCapability("groupId", groupId);
		}

		driver = new io.appium.java_client.ios.IOSDriver<WebElement>(kobitonServerUrl, capabilities);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}

	@Given("^User go to login page$")
	public void go_to_login_herokuapp_page() {
		driver.get("http://the-internet.herokuapp.com/login");
		sleep(2);
	}

	@And("^User inputs username ([^\"]*)$")
	public void user_input_username(String username) {
		driver.findElementById("username").sendKeys(username);
	}

	@And("^User inputs password ([^\"]*)$")
	public void user_input_password(String password) {
		driver.findElementById("password").sendKeys(password);
	}

	@And("^User clicks login button$")
	public void user_click_login() {
		driver.findElementByXPath("//form[@name='login']").submit();
	}

	@Then("^User will see message ([^\"]*)$")
	public void user_verify_message(String mesg) {
		sleep(2);
		Assert.assertTrue(getMessage().contains(mesg));
	}

	@Given("^User ends session on Android device$")
	public void end_an_ios_web_session() {
		try {
			if (driver != null)
				driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMessage() {
		return driver.findElementById("flash").getText();
	}

	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}