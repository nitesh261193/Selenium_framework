package com.merlin.webdriverutilities;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.merlin.common.Constants;

import io.github.bonigarcia.wdm.WebDriverManager;


/**
 * @author Pooja Chopra
 *
 */

public class Set_Webdriver {
	public final static Set_Webdriver DRIVER = new Set_Webdriver();
	WebDriver driver;

	private static final Logger log = Logger.getLogger(Set_Webdriver.class);

	public WebDriver Get_Driver() {
		if(driver == null) {
			Setup_Webdriver();
		}
		return driver;
	}
	
	public void Setup_Webdriver() {


		HashMap<String, Object> chrome_Prefs = new HashMap<String, Object>();
		chrome_Prefs.put("profile.default_content_settings.popups", 0);
		chrome_Prefs.put("download.default_directory", Get_Download_Path().getAbsolutePath());
		chrome_Prefs.put("safebrowsing.enabled", "false"); 
		chrome_Prefs.put("download.prompt_for_download", "false");
		ChromeOptions chrome_Options = new ChromeOptions();
		chrome_Options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
		chrome_Options.setExperimentalOption("prefs", chrome_Prefs);
		chrome_Options.addArguments("--disable-extensions");
		chrome_Options.addArguments("--test-type");
		chrome_Options.addArguments("disable-infobars");
		chrome_Options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		chrome_Options.setPageLoadStrategy(PageLoadStrategy.NONE);
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(chrome_Options);
		log.info("Launching chrome driver +++ ");
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.MINUTES);
		driver.manage().window().maximize();
	}

	public static File Get_Download_Path() {
		File download_Path = new File(Constants.browser_Download_Folder);
		return download_Path;
	}

}
