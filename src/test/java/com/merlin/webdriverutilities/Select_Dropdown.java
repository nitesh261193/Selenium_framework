package com.merlin.webdriverutilities;

import java.util.ArrayList;
import java.util.List;

import com.merlin.common.Util;
import com.aventstack.extentreports.Status;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;

/**
 * @author Pooja Chopra
 *
 */
public class Select_Dropdown extends Annotation implements Init {
	private final Logger log = Logger.getLogger(Select_Dropdown.class);

	public void Webdriver_Select_Dropdown_List_By_Value(By by, String value) {
		Select select = new Select(driver.findElement(by));
		select.selectByValue(value);
	}

	public String Webdriver_Select_Dropdown_Get_Selected_Value(By by) {
		Select select = new Select(driver.findElement(by));
		return select.getFirstSelectedOption().getText();
	}

	public void Webdriver_Select_Dropdown_List_By_Visible_Text(By by, String value) {
		(new WebDriverWait(driver, Constants.expicit_Wait_Time)).until(ExpectedConditions.presenceOfElementLocated(by));
		(new WebDriverWait(driver, Constants.expicit_Wait_Time))
				.until(ExpectedConditions.visibilityOfElementLocated(by));
		Select select = new Select(driver.findElement(by));
		select.selectByVisibleText(value);
	}

	public void Webdriver_Select_Click(By clicks, By dropdown_List, String value) {
		click.Webdriver_Click(clicks, true);
		List<WebElement> list = webelement.Webelement_List(dropdown_List);
		for (WebElement drop : list) {
			if (drop.getText().toLowerCase().equals(value.toLowerCase())) {
				drop.click();
				break;
			}
		}
	}

	public void Webdriver_Select_Dropdown_By_Click(By select_Box, By select_Input, String value,
			String notclick_SelectBox) {
		(new WebDriverWait(driver, Constants.expicit_Wait_Time))
				.until(ExpectedConditions.presenceOfElementLocated(select_Box));
		(new WebDriverWait(driver, Constants.expicit_Wait_Time))
				.until(ExpectedConditions.visibilityOfElementLocated(select_Box));

		if (notclick_SelectBox != null) {
			log.info("Not Clicking Dropdown");
		} else {
			click.Webdriver_Click(select_Box, true);
		}

		if (select_Input != null) {
			(new WebDriverWait(driver, Constants.expicit_Wait_Time))
					.until(ExpectedConditions.visibilityOfElementLocated(select_Input));
			sendkeys.Webdriver_Sendkeys(select_Input, value);
		}
		List<WebElement> no_Of_Values = driver.findElement(select_Box)
				.findElements(By.xpath("./following::div//ul/li"));
		for (WebElement element : no_Of_Values) {
			String name = element.getText().trim();
			if (name != null && !name.isEmpty()) {
				if (name.equals(value)) {
					element.click();
					log.info("Found and Selected the value : " + value);
					break;
				}
			}
		}

	}

	public boolean Webdriver_Select_Dropdown_Validate_Values_Present(By select_Box, By select_Input, String[] value,
			String not_Click) {
		boolean flag = false, value_Present;
		ArrayList<String> values_Not_Presents = new ArrayList<String>();

		(new WebDriverWait(driver, Constants.expicit_Wait_Time))
				.until(ExpectedConditions.presenceOfElementLocated(select_Box));
		(new WebDriverWait(driver, Constants.expicit_Wait_Time))
				.until(ExpectedConditions.visibilityOfElementLocated(select_Box));

		if (not_Click == null) {
			click.Webdriver_Click(select_Box, true);
		}

		(new WebDriverWait(driver, Constants.expicit_Wait_Time))
				.until(ExpectedConditions.visibilityOfElementLocated(select_Input));

		if (select_Input != null) {

			for (String val : value) {
				flag = false;
				sendkeys.Webdriver_Sendkeys(select_Input, val);

				List<WebElement> no_Of_Values = driver.findElement(select_Box)
						.findElements(By.xpath(".//following-sibling::div//ul/li"));

				for (WebElement element : no_Of_Values) {
					String name = element.getText().trim();
					if (name.equals(val)) {
						log.info("Value Present in the Dropdown : " + val);
						flag = true;
						break;
					}
				}
				if (!flag) {
					log.info(val + " not found in the dropdown");
					values_Not_Presents.add(val);
				}
			}
		}
		click.Webdriver_Click(select_Box, true); // additional click to close
													// dropdown
		value_Present = values_Not_Presents.isEmpty();
		return value_Present;
	}

	public void Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By select_Box, By select_Input, String value) {
		click.Webdriver_Click_By_Action(select_Box);
		visible.Pause(4);
		sendkeys.Webdriver_Sendkeys_Enter(select_Input, value);
	}

	public void Webdriver_Select_DropdownList_By_Visible_Text_IgnoreCase(By select_Box, String value) {
		Select drop_Down = new Select(driver.findElement(select_Box));
		int index = 0;
		for (WebElement option : drop_Down.getOptions()) {
			if (option.getText().trim().equalsIgnoreCase(value))
				break;
			index++;
		}
		drop_Down.selectByIndex(index);
	}

	public void Webdriver_Select_Dropdown_Multi_Select(By input_Xpath, String value) {
		String[] dropdown_Values = value.split(",");

		for (String values : dropdown_Values) {
			log.info("Clicking on the input box for the drop down to appear");
			click.Webdriver_Click(input_Xpath, false);

			log.info("Enter the values under the text box and pressing enter");
			sendkeys.Webdriver_Sendkeys_Enter_Without_Clear(input_Xpath, value);
		}
	}

	public ArrayList<String> Webdriver_Get_Option_List(By select_Box) {
		ArrayList<String> options = new ArrayList<String>();
		Select drop_Down = new Select(driver.findElement(select_Box));
		List<WebElement> no_Of_Values = drop_Down.getOptions();
		for (WebElement opt : no_Of_Values) {
			options.add(opt.getText());
		}
		return options;
	}

	public static void Webdriver_selectDropdown_By_ActionClick(By selectBox , By Select_Input , String Value) {
		click.Webdriver_Click_By_Action(selectBox);
		if(!webelement.Is_Element_Displayed(Select_Input, 4)){
			Util.Report_Log(Status.INFO,"RE Click for First Time as Dropdown not opened");
			Util.Report_Log(Status.INFO,"Clicking on Generate Tab to remove focus from drop down");
			click.Webdriver_Click(By.id("generateTabId"), false);
			//Visible.pause(2);
			click.Webdriver_Click_By_Action(selectBox);
			if(!webelement.Is_Element_Displayed(Select_Input, 4)){
				Util.Report_Log(Status.INFO,"Unable to open Dropdown, RE Click for Second Time as Dropdown not opened");
				Util.Report_Log(Status.INFO,"Clicking on Generate Tab to remove focus from drop down");
				//Visible.pause(3);
				click.Webdriver_Click(By.id("generateTabId"), false);
				click.Webdriver_Click_By_Action(selectBox);
			}
		}
		sendkeys.Webdriver_Sendkeys(Select_Input, Value);
		click.Webdriver_Click(By.xpath("//li[text()='" + Value + "']"), true);
	}
}
