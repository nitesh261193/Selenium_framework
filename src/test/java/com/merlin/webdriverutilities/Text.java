package com.merlin.webdriverutilities;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.merlin.common.Constants;

/**
 * @author Pooja Chopra
 *
 */

public class Text {

	WebDriver driver = Set_Webdriver.DRIVER.Get_Driver();

	public String Webdriver_Get_Text(By by) {
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.presenceOfElementLocated(by));
		String text = driver.findElement(by).getText().trim();
		return text;
	}

	public String Javascript_Get_Text(By by) {
		WebElement e = driver.findElement(by);
		String elementText = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML", e);
		return elementText.trim();
	}

	public String Javascript_Get_Outer_Html(By by) {
		WebElement e = driver.findElement(by);
		String elementText = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].outerHTML", e);
		return elementText.trim();
	}

	public String Javascript_Get_Outer_Html(WebElement ele) {
		String elementText = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].outerHTML", ele);
		return elementText.trim();
	}

	public String Javascript_Get_Inner_Text(By by) {
		WebElement e = driver.findElement(by);
		String elementText = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerText", e);
		return elementText.trim();
	}

	public String Javascript_Get_Inner_Html(WebElement element) {
		String elementText = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML",
				element);
		return elementText.trim();
	}

	public String Javascript_Get_Value(By by) {
		WebElement e = driver.findElement(by);
		String elementText = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].value", e);
		return elementText.trim();
	}

	public String Javascript_Get_Value_Checkbox(By by) {
		WebElement e = driver.findElement(by);
		Boolean value = (Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].checked", e);
		return value.toString();
	}
	
	public String Javascript_Get_Text_Of_Css(By by) {
		WebElement e = driver.findElement(by);
		String elementText = ((String) ((JavascriptExecutor) driver)
				.executeScript("return window.getComputedStyle(arguments[0],':after').getPropertyValue('content')", e));
		return elementText.trim();
	}

	public String Javascript_Get_Computed_Style(By by) {
		WebElement e = driver.findElement(by);
		String elementText = ((String) ((JavascriptExecutor) driver)
				.executeScript("return getComputedStyle(arguments[0]).backgroundColor", e));
		return elementText.trim();
	}

	public String Webdriver_Action_Get_Text(By by) throws UnsupportedFlavorException, IOException {
		Actions a = new Actions(driver);
		a.click(driver.findElement(by)).build().perform();
		a.keyDown(Keys.CONTROL).sendKeys("a").perform();
		a.keyUp(Keys.CONTROL).perform();
		a.keyDown(Keys.CONTROL).sendKeys("c").perform();
		a.keyUp(Keys.CONTROL).perform();
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		return (String) clipboard.getData(DataFlavor.stringFlavor);
	}

	public String Webdriver_getText_ByAttribute(By by, String attribute) {
		String text = driver.findElement(by).getAttribute(attribute).trim();
		return text;
	}

	public String Webdriver_Get_Text_By_Attribute(WebElement ele, String attribute) {
		String text = ele.getAttribute(attribute).trim();
		return text;
	}

	public String Webdriver_Get_Text_Of_Select_Box(By by) {
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.presenceOfElementLocated(by));
		(new WebDriverWait(driver, Constants.expicit_Wait_Time))
				.until(ExpectedConditions.visibilityOfElementLocated(by));
		String text = driver.findElement(by).findElement(By.xpath(".//option[@selected]")).getText().trim();
		return text;
	}

	public String Webdriver_Get_Text_Of_Attribute(By by, String attribute) {
		String text = driver.findElement(by).getAttribute(attribute);
		return text;
	}

	public String Webdriver_Get_Page_Title() {
		return driver.getTitle().replace("Naehas", "").replace("-", "").trim();
	}

	public ArrayList<String> Get_Elements_Text(By locator) {

		ArrayList<String> data = new ArrayList<String>();
		List<WebElement> list = driver.findElements(locator);
		for (WebElement a : list) {
			data.add(a.getText().trim());
		}
		return data;
	}

	public ArrayList<String> Get_Elements_Text_By_Attribute(By locator, String attribute) {

		ArrayList<String> data = new ArrayList<String>();
		List<WebElement> list = driver.findElements(locator);
		for (WebElement a : list) {
			data.add(a.getAttribute(attribute).trim());
		}
		return data;
	}
	
	/*
	 * This fnction was made for soft-assertions 
	 * it handles the soft assertion failure so that everything passes the soft assertion
	 */
	public  String Webdriver_getText_softAssert(By by){
		String text = "No text found on UI";
		try
		{
			text = Webdriver_Get_Text( by);
		}
		catch(Exception e)
		{
		}
		return text.trim();
	}
	
	public  String Webdriver_getPage_Title(){
		return driver.getTitle().replace("Naehas","").replace("-", "").trim();
	}
}
