package com.merlin.webdriverutilities;

import java.util.concurrent.TimeUnit;

import com.merlin.common.Annotation;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.merlin.common.Constants;
import com.merlin.common.Util;
import static com.melin.apps.locators.Indesign_Editor_Locators.loading_Locator;

/**
 * @author Pooja Chopra
 *
 */
public class Visible extends Annotation {

//	static WebDriver driver = Set_Webdriver.DRIVER.Get_Driver();
	private static final Logger log = Logger.getLogger(Visible.class);
	public static int pause_Time = 0;

	public  void Wait_For_Page_To_Load() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.info("ztest");
			e.printStackTrace();
		}

		ExpectedCondition<Boolean> page_Load_Condition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, Constants.expicit_Wait_Time);
		wait.until(page_Load_Condition);
	}

	public  Boolean Is_Selected(By by) {
		return driver.findElement(by).isSelected();
	}

	public void Pause(int seconds) {

		log.info("Pausing the Execution for " + seconds + " Seconds.");
		pause_Time = pause_Time + seconds;

		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public  void Scroll_View(By by) {
		WebElement scroll = driver.findElement(by);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView()", scroll);
	}

	public  void Scroll_Views(By by) {
		WebElement scroll = driver.findElement(by);
		Actions actions = new Actions(driver);
		actions.moveToElement(scroll);
		actions.perform();
	}

	public  void Scroll_To_Element(By by) {
		WebElement element = driver.findElement(by);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(
				"window.scrollBy(" + element.getLocation().getX() + "," + (element.getLocation().getY() + 100) + ")",
				"");
	}
	public  void Scroll_To_Element(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(
				"window.scrollBy(" + element.getLocation().getX() + "," + (element.getLocation().getY() + 100) + ")",
				"");
	}

	public  void Wait_Until_Visible(int time_To_Wait, By locator) {
		new WebDriverWait(driver, time_To_Wait).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	public  void Wait_Until_Invisible(int time_To_Wait, By locator) {
		new WebDriverWait(driver, time_To_Wait).until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}
	
	public  void Wait_Until_Frame_Is_Visible(int time_To_Wait, By locator) {
		new WebDriverWait(driver, time_To_Wait).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}
	
	public  Boolean Is_Displayed(By by) {
		Boolean is_displayed = false;
		try {
			is_displayed = driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			log.info("Element not found");
		}
		return is_displayed;
	}

	public void Wait_Until_Attribute_Visible(int time_To_Wait, By locator, String attribute, String attribute_Value) {
		WebElement we = driver.findElement(locator);
		new WebDriverWait(driver, time_To_Wait).until(e -> we.getAttribute(attribute).contains(attribute_Value));

	}
}
