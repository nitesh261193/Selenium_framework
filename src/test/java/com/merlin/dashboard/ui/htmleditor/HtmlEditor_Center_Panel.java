package com.merlin.dashboard.ui.htmleditor;

import java.util.Properties;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;

import static com.melin.apps.locators.Html_Editor_Locators.side_Bar_Icon;

/**
 * @author Nitesh Gupta
 *
 */
public class HtmlEditor_Center_Panel extends Annotation implements Init
{
	static Properties prop = null;
	
	@BeforeMethod(alwaysRun = true)
	public void Before_Class() {
		navigate.Switch_To_Default_Frame();
		navigate.Switch_To_Frame("proofViewerFrame");
	}

	@Test(groups = { "Select_ImageFrame"})
	public static void Select_ImageFrame() {
		Util.Report_Log(Status.INFO, "Selecting image frame" );
		WebElement ele=webelement.shadowRoot_Element("editor-main","img");
		ele.click();
		Assert.assertEquals(ele.getAttribute("class").contains("selected"),true,"Image Frame is not selected");
		Util.Report_Log(Status.PASS, " Image frame Selected :  successfully" );
	}

	@Test(groups = { "Select_TextFrame"})
	public static void Select_TextFrame() {
		Util.Report_Log(Status.INFO, "Selecting text frame" );
		WebElement ele=webelement.shadowRoot_Element("editor-main","h2");
		visible.Scroll_To_Element(ele);
		ele.click();
		Assert.assertEquals(ele.getAttribute("class").contains("selected"),true,"Text Frame is not selected");
		Util.Report_Log(Status.PASS, " Text frame selected :  successfully" );
	}

	@Test(groups = { "Validate_ImageFrame"})
	@Parameters({"image"})
	public static void Validate_ImageFrame(String image) {
		Util.Report_Log(Status.INFO, "Validating image in html template - " + image );
		WebElement ele=webelement.shadowRoot_Element("editor-main","img");
		Util.Report_Log(Status.INFO, "Image source found as - " + ele.getAttribute("src") );
		Assert.assertTrue(ele.getAttribute("src").contains(image),"Image not found as expected.");
		Util.Report_Log(Status.PASS, "Image source found as expected.");
	}

	@Test(groups = { "Validate_TextFrame"})
	@Parameters({"content"})
	public static void Validate_TextFrame(String text) {
		text = text.replace("random", Constants.random_Number);
		Util.Report_Log(Status.INFO, "Validating text in html template - " + text );
		WebElement ele=webelement.shadowRoot_Element("editor-main","h2");
		Util.Report_Log(Status.INFO, "Text data found as - " + ele.getText() );
		Assert.assertTrue(ele.getText().contains(text),"Text not found as expected.");
		Util.Report_Log(Status.PASS, "Text found as expected.");
	}
	@Test(groups = { "Open_Right_Pannel_By_clicking_Sidebar_Icon"})
	public static void open_Right_Pannel_By_clicking_Sidebar_Icon()
	{
		if(Integer.parseInt(Constants.release_Name) > 204) {
		Util.Report_Log(Status.INFO, "Checking Right panel is opened or not");
		String class_Attribute = text.Webdriver_getText_ByAttribute(side_Bar_Icon, "class");
		if(class_Attribute.contains("RotateIcon")) {
			Util.Report_Log(Status.INFO, "Right Pannel is already opned");
		}
		else
		{
			click.Webdriver_Click(side_Bar_Icon, true);
			Util.Report_Log(Status.INFO, "Clicked on side bar icon to open Right Pannel");
		}
		}
		else
		{
			Util.Report_Log(Status.INFO, "Side Bar functionality is implementead in 205 tag");
		}
	}
}
