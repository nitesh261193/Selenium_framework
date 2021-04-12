package com.merlin.dashboard.ui.inddeditor;

import com.merlin.common.Annotation;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.testng.annotations.*;

import static com.melin.apps.locators.Indesign_Editor_Locators.*;


/**
 * @author Nitesh Gupta
 */
public class Indd_Top_Panel extends Annotation implements Init
{

	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		navigate.Switch_To_Frame("editorViewerFrame");
}
	
	@Test( groups = { "Click_On_Save_Button"})
	public static void Click_On_Save_Button()
	{
		Util.Report_Log(Status.INFO, "Clicking on save icon of Indd" );
		click.Webdriver_Click(save_Button,true);
		Util.Report_Log(Status.INFO, "Waiting for loader to dissappear" );
		Indd_Util.Wait_For_Loader_To_Disappear();
		Util.Report_Log(Status.PASS, "Save button is clicked successfully " );
	}
	
	@Test( groups = { "Click_On_Close_Button"})
	@Parameters({"close_After_Save","Choice"})
	public static void Click_On_Close_Button( String close_After_Save,@Optional String choice)
	{
		Util.Report_Log(Status.INFO,  "Clicking on close button...");
		click.Webdriver_Click_By_Action(Close_Button);
		if(close_After_Save.equalsIgnoreCase("No"))
		{
			click.Webdriver_Click_By_Action(By.xpath(pop_up_choice_Button.replace("Choice",choice)));
		}
		int ctr=0;
		while((visible.Is_Displayed(loading_Locator)) && (ctr < 60))
		{
			ctr++;
			//waiting for loader to disappear
		}
		navigate.Switch_To_Tab(0,"true","true");
	}
	
	@Test(groups = { "Click_On_Preview_Button"})
	public static void Click_On_Preview_Button() {
		click.Webdriver_Click(preview_Button,true);
		Util.Report_Log(Status.PASS, "Preview Button is clicked successfully" );
		visible.Wait_For_Page_To_Load();
//		if(visible.Is_Displayed(By.xpath("//button[@class='close']//span")))
//			Indd_Util.Warning_Pop_Up_Handle();
	}
}
