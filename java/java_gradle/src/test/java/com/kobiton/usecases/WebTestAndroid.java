/*
 * Created on Sep 26, 2017, 5:17:43 PM
 *
 * Copyright(c) 2017 Kobiton Inc.  All Rights Reserved.
 * This software is the proprietary information of Kobiton Company.
 *
 */
package com.kobiton.usecases;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class WebTestAndroid extends BaseTest {

    private RemoteWebDriver driver = null;

    @BeforeTest
    @Override
    public void Setup() throws Exception {
        super.Setup();

        String deviceUdid = System.getenv("KOBITON_DEVICE_UDID");
		String deviceName = System.getenv("KOBITON_DEVICE_NAME");
		
		String deviceOrientation = System.getenv("KOBITON_SESSION_DEVICE_ORIENTATION");
		String captureScreenshots = System.getenv("KOBITON_SESSION_CAPTURE_SCREENSHOTS");
		String deviceGroup = System.getenv("KOBITON_SESSION_DEVICE_GROUP");
		String browserName = System.getenv("KOBITON_SESSION_BROWSER_NAME");
		String platformVersion = System.getenv("KOBITON_SESSION_PLATFORM_VERSION");
		String groupId = System.getenv("KOBITON_SESSION_GROUP_ID");

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("sessionName", "Automation test android app session");
		capabilities.setCapability("sessionDescription", "Automation test android app session"); 
		capabilities.setCapability("deviceOrientation", ((deviceOrientation == null) ? "portrait" : deviceOrientation));  
		capabilities.setCapability("captureScreenshots", Boolean.parseBoolean((captureScreenshots == null) ? "true" : captureScreenshots)); 
		capabilities.setCapability("browserName", ((browserName == null) ? "chrome" : browserName)); 
		capabilities.setCapability("deviceGroup", ((deviceGroup == null) ? "KOBITON" : deviceGroup));
		capabilities.setCapability("platformName", "Android");

		if (deviceUdid != null) {
			capabilities.setCapability("deviceUdid", deviceUdid);
		}
		else {
			capabilities.setCapability("deviceName", ((deviceName == null) ? "Galaxy S6" : deviceName));
			capabilities.setCapability("platformVersion", platformVersion);
		}
		
		if (groupId != null) {
			capabilities.setCapability("groupId", groupId);
		}

        driver = new RemoteWebDriver(getAutomationUrl(), capabilities);
    }

    @AfterTest
    public void Teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(description = "should run test successfully with correct username and password")
    public void testLoginSuccessfully() {
        driver.get("http://google.com");
        WebElement searchFieldE = driver.findElementByXPath("//input[@name='q']");
        searchFieldE.sendKeys("Kobiton");
        searchFieldE.sendKeys(Keys.ENTER);
        driver.findElementByXPath("//a[@href='https://kobiton.com/']").click();
        String message = driver.findElementByXPath("//div[@class='intro-message']/h2").getText();

        Assert.assertTrue(message.contains("Real Devices in the Cloud, Better Testing for You"));
    }
}
