package com.merlin.webdriverutilities;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;


/**
 * @author Pooja Chopra
 *
 */

public class Navigate extends Annotation implements Init {
	private final Logger log = Logger.getLogger(Navigate.class);

	@Test(groups = { "Navigate_To_URL" })
	@Parameters({ "url" })
	public void Webdriver_Get(String url) {
		url = url.replace("DASHBOARD_NAME", Constants.dashboard_Name);
		(new WebDriverWait(driver, Constants.expicit_Wait_Time))
				.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//body"))));
		driver.get(url);
		visible.Wait_For_Page_To_Load();
	}

	public void Webdriver_Navigate(String url) {
		driver.navigate().to(url);
		visible.Wait_For_Page_To_Load();
	}

	public void Webdriver_Navigate_Breadcrumb(String breadcrumb) {
		click.Webdriver_Click(By.xpath("//div[@id='row-breadcrumbs']//a[contains(text(),'" + breadcrumb + "')]"), true);
		visible.Wait_For_Page_To_Load();
	}

	public String Webdriver_Get_Current_Url() {
		String url = driver.getCurrentUrl();
		return url;
	}

	@Test(groups = { "Reload_Page" })
	public void Webdriver_Refresh() {
		driver.navigate().refresh();
		(new WebDriverWait(driver, Constants.expicit_Wait_Time))
				.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//body"))));
	}

	@Test(groups = { "navigate_Back" })
	public void Navigate_Back() {
		driver.navigate().back();
		(new WebDriverWait(driver, Constants.expicit_Wait_Time))
				.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//body"))));
	}

	@Test(groups = { "Switch_To_Tab" })
	@Parameters({ "tab_Index", "stop_On_Fail", "validate" })
	public void Switch_To_Tab(@Optional int tab_Index, @Optional String stop_On_Fail, @Optional String validate) {
		validate = validate == null ? "true" : validate;
		log.info("Switching browser tab:-  " + tab_Index);
		String current_Tab_Url = Webdriver_Get_Current_Url();
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(tab_Index));
		visible.Wait_For_Page_To_Load();
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//body"))));
		if (current_Tab_Url.equals(Webdriver_Get_Current_Url()) && validate.equals(true)) {
			if (stop_On_Fail.equals("true"))
				Assert.fail("Not able to switch tab Hence stoping execution");
		}
		log.info(driver.getCurrentUrl());
		visible.Wait_For_Page_To_Load();

	}

	@Test(groups = { "Switch_To_Tab_Zero" })
	public void Switch_To_Tab_Zero() {
		log.info("Switching to 1st browser tab");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(0));
	}

	public void Switch_To_Frame(String frame_Name) {
		String current_Frame = Util.Get_Current_Frame();
		System.out.println("Currently on frame - " + current_Frame);
		if (current_Frame.equals(frame_Name)) {
			log.info("No to switch frame as driver is already on exxpected frame " + current_Frame);
		} else {
			log.info("Switching to frame - " + frame_Name);
			driver.switchTo().frame(frame_Name);
			String current_Frame2 = Util.Get_Current_Frame();
			log.info("Switched frame "+current_Frame2);
		}
	}

	public void Switch_To_Parent_Frame() {
		driver.switchTo().parentFrame();
		log.info("Switching to parent frame - " + Util.Get_Current_Frame());
	}

	public void Switch_To_Frame_By_Xpath(By frame_Xpath) {
		log.info("Switching to inner frame.");
		driver.switchTo().frame(driver.findElement(frame_Xpath));
		log.info("Switching to frame - " + Util.Get_Current_Frame());
	}

	public void Switch_To_Default_Frame() {
		log.info("Switching to default frame");
		driver.switchTo().defaultContent();
	}

	@Test(groups = { "close_Current_Tab" })
	public void Webdriver_Close_Current_Tab() {
		driver.close();
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		int tabSize = tabs2.size();
		driver.switchTo().window(tabs2.get(tabSize - 1));
	}

	@Test(groups = { "open_New_Tab" })
	public void Open_New_Tab() {
		((JavascriptExecutor) driver).executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(tabs.size()-1));
	}
	// created by Krishna saini
	@Test(groups = { "switch_to_tab" })
	public void Switch_To_Tab(int tab_Index) {
		log.info("Switching to "+tab_Index+" browser tab");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(tab_Index));
	}
}
