/*
 * Created on Sep 26, 2017, 5:17:43 PM
 *
 * Copyright(c) 2017 Kobiton Inc.  All Rights Reserved.
 * This software is the proprietary information of Kobiton Company.
 *
 */
package com.kobiton.usecases;

import io.appium.java_client.android.AndroidDriver;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTestAndroid extends BaseTest {

    private AndroidDriver<WebElement> driver = null;

    @BeforeTest
    @Override
    public void Setup() throws Exception {
        super.Setup();

        String deviceUdid = System.getenv("KOBITON_DEVICE_UDID");
		String deviceName = System.getenv("KOBITON_DEVICE_NAME");
		String deviceOrientation = System.getenv("KOBITON_SESSION_DEVICE_ORIENTATION");
		String captureScreenshots = System.getenv("KOBITON_SESSION_CAPTURE_SCREENSHOTS");
		String deviceGroup = System.getenv("KOBITON_SESSION_DEVICE_GROUP");
		String app = System.getenv("KOBITON_SESSION_APPLICATION_URL");
		String platformVersion = System.getenv("KOBITON_SESSION_PLATFORM_VERSION");
		String groupId = System.getenv("KOBITON_SESSION_GROUP_ID");

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

        driver = new AndroidDriver<>(getAutomationUrl(), capabilities);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }

    @AfterTest
    public void Teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(description = "should verify Acura categories")
    public void testSearchQuestionsOnAcuraSupportCommunity() {
        /*
      * Steps:
      * 1. Click on "Car and Truck" Categories on Homepage
      * 2. Click on "Acura" Categories
      *
      * Expected:
      * General Information is "Acura".
         */
        driver.findElementByXPath("//*[@resource-id='android:id/home']").click();
        sleep(3);
        driver.findElementByXPath("//*[@text='Car and Truck']").click();
        driver.findElementByXPath("//*[@text='Acura']").click();
        sleep(3);
        String acuraText = driver.findElementByXPath("//*[@resource-id='com.dozuki.ifixit:id/topic_title' and @index=1]")
                .getText();

        Assert.assertEquals(acuraText, "Acura");

    }

    public void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            //Do nothing if sleep failed
        }
    }
}
