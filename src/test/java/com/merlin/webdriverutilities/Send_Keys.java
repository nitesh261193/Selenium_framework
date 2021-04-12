package com.merlin.webdriverutilities;

import com.merlin.common.Init;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.merlin.common.Annotation;
import com.merlin.common.Constants;

/**
 * @author Pooja Chopra
 *
 */

public class Send_Keys extends Annotation implements Init {
	
	public  void Webdriver_Sendkeys(By by, String value){
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.presenceOfElementLocated(by));
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.visibilityOfElementLocated(by));
		driver.findElement(by).clear();
		driver.findElement(by).sendKeys(value);
	}
	
	public  void Webdriver_Sendkeys_Javascript(By by, String value){
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.presenceOfElementLocated(by));
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.visibilityOfElementLocated(by));
		WebElement we = driver.findElement(by);
		JavascriptExecutor executor = ((JavascriptExecutor) driver);
		executor.executeScript("arguments[0].value='"+value+"';", we);
	}

	public  void Webdriver_Sendkeys_Javascript_InnerHTML(By by, String value){
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.presenceOfElementLocated(by));
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.visibilityOfElementLocated(by));
		WebElement we = driver.findElement(by);
		JavascriptExecutor executor = ((JavascriptExecutor) driver);
		executor.executeScript("arguments[0].innerHTML='"+value+"';", we);
		driver.switchTo().activeElement().sendKeys(" ");
	}

	public  void Webdriver_Sendkeys_Active_Element(By by, String value){
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.presenceOfElementLocated(by));
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.visibilityOfElementLocated(by));
		driver.switchTo().activeElement().clear();
		driver.switchTo().activeElement().sendKeys(value);
	}

	
	public  void Webdriver_Sendkeys_Space(By by, String value){
		String[]  val = value.split("\\\\n");
		driver.findElement(by).clear();
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.presenceOfElementLocated(by));
		for(String a : val){
			driver.findElement(by).sendKeys(a);
			driver.findElement(by).sendKeys(Keys.ENTER);
		}
	}
	public  void Webdriver_Sendkeys_Enter(By by, String value){
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.presenceOfElementLocated(by));
		driver.findElement(by).clear();
		driver.findElement(by).sendKeys(value);
		driver.findElement(by).sendKeys(Keys.ENTER);
	}
	public  void Webdriver_Sendkeys_Enter_Without_Clear(By by, String value){
		driver.findElement(by).sendKeys(value);
		driver.findElement(by).sendKeys(Keys.ENTER);
	}
	
	public  void Webdriver_Sendkeys_Without_Clear( String value){
		driver.switchTo().activeElement().sendKeys(value);
	}
	public  void Webdriver_Sendkeys_Change(By by, String value) {
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.presenceOfElementLocated(by));
		driver.findElement(by).clear();
		driver.findElement(by).sendKeys(value);
		WebElement text_Field = driver.findElement(by);
        ((JavascriptExecutor) driver).executeScript("$(arguments[0]).change(); return true;", text_Field);
        driver.findElement(by).sendKeys(Keys.ENTER);
		visible.Wait_For_Page_To_Load();
	}

	public  void Webdriver_Sendkeys_Action(By by , String value ){
		WebElement send =  driver.findElement(by);
		Actions action = new Actions(driver);
		
		action.sendKeys(send, value).build().perform();
	}
	public  void Webdriver_Clear_sendkeys_Action(By by , String value ){
		Actions action = new Actions(driver);
		action.keyDown(Keys.CONTROL).sendKeys("a").build().perform();
		action.keyUp(Keys.CONTROL).build().perform();
		action.sendKeys(Keys.DELETE).build().perform();
		action.sendKeys(value).build().perform();
	}
	
	public  void Webdriver_Sendkeys_Clear(By by){
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.presenceOfElementLocated(by));
		driver.findElement(by).clear();
	}
	
	public  void Webdriver_Sendkeys_Tab(By by, String value) {
		visible.Wait_For_Page_To_Load();
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.presenceOfElementLocated(by));
		driver.findElement(by).sendKeys(value);
		driver.findElement(by).sendKeys(Keys.TAB);
		visible.Wait_For_Page_To_Load();
	}
}
