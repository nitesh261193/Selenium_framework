package com.merlin.dashboard.ui.contenteditor;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.merlin.common.Annotation;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;

/**
 * @author Pooja Chopra
 *
 */
public class Content_Editor_Tiny_Mce extends Annotation implements Init{

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod()
	{
		visible.Wait_For_Page_To_Load();
		navigate.Switch_To_Frame("htmlEditor_ifr");
	}
	
	@AfterMethod(alwaysRun = true)
	public void afterMethod()
	{
		navigate.Switch_To_Parent_Frame();
    }
	
	@Test( groups = {"Select_Data_In_Editor"})
	public void Select_Data_In_Editor()
	{
		Util.Report_Log(Status.INFO	, "Selecting data in html editor frame");
		Actions action = new Actions(driver);
		action.click(driver.findElement(By.id("tinymce"))).perform();
		action.keyDown(Keys.SHIFT).sendKeys(Keys.HOME).keyUp(Keys.SHIFT).build().perform();
		Util.Report_Log(Status.INFO	, "All the data selected in editor");
	}
	
}