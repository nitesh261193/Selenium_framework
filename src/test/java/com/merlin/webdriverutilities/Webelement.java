package com.merlin.webdriverutilities;

import java.util.List;

import com.merlin.common.Annotation;
import com.merlin.common.Init;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Krishna Saini
 *
 */
public class Webelement extends Annotation implements Init {


	private static final Logger log = Logger.getLogger(Webelement.class);

	/**
	 * Method can be used to get the Size of the element.
	 * 
	 * @param by
	 *            receives the element locator.
	 * @return Integer, If Element is not found still it'll return 0 (Zero)
	 */
	public  int Webelement_Size(By by) {

		visible.Wait_For_Page_To_Load();

		return driver.findElements(by).size();

	}

	public  List<WebElement> Webelement_List(By by) {

		visible.Wait_For_Page_To_Load();

		(new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(by));

		return driver.findElements(by);

	}

	public  boolean Is_Element_Displayed(By by, int time_To_Wait) {

		boolean elementDisplayed = true;

		try {
			log.info("Waiting for the page to load.");
			// Visible.waitfor_page_toload();

			(new WebDriverWait(driver, time_To_Wait)).until(ExpectedConditions.visibilityOfElementLocated(by));
			log.info("Page loaded.");
		} catch (Exception e) {
			log.info("Exception occurred. Hence, setting 'elementDisplayed' to FALSE.");
			elementDisplayed = false;
		}

		return elementDisplayed;
	}

	public  WebElement Get_Webelement(By by) {
		return driver.findElement(by);

	}

	public  boolean Is_Displayed(By locator, int time_To_Wait) {
		try {
			new WebDriverWait(driver, time_To_Wait).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
			return true;
		} catch (Exception e) {
			log.info("element not displayed hence returning false");
			return false;
		}
	}

	public  boolean Is_Present(By locator, int time_To_Wait) {
		try {
			new WebDriverWait(driver, time_To_Wait).until(ExpectedConditions.presenceOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			log.info("element not present hence returning false");
			return false;
		}
	}

	public  boolean Is_Checked(By locator) {

		return driver.findElement(locator).isSelected();
	}

	public  boolean Is_Enabled(By by) {
		return driver.findElement(by).isEnabled();
	}


	public WebElement expandRootElement(WebElement element) {
		WebElement ele = (WebElement) ((JavascriptExecutor)driver)
				.executeScript("return arguments[0].shadowRoot", element);
		return ele;
	}

	public WebElement shadowRoot_Element(String root_tag, String tagname) {
		WebElement root = Get_Webelement(By.tagName(root_tag));
		WebElement shadowRoot = expandRootElement(root);
		WebElement ele = shadowRoot.findElements(By.tagName(tagname)).get(0);
		return ele;
	}

}
