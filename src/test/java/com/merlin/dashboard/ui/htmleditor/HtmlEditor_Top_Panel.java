package com.merlin.dashboard.ui.htmleditor;

import com.merlin.common.Annotation;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static com.melin.apps.locators.Html_Editor_Locators.*;


/**
 * @author Nitesh Gupta
 */
public class HtmlEditor_Top_Panel extends Annotation implements Init
{

	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		navigate.Switch_To_Default_Frame();
		navigate.Switch_To_Frame("proofViewerFrame");
}
	
	@Test( groups = { "Click_On_Save_Button"})
	public static void Click_On_Save_Button()
	{
		Util.Report_Log(Status.INFO, "Clicking on save icon of Indd" );
		click.Webdriver_Click(save_Button,true);
		visible.Wait_For_Page_To_Load();
		Util.Report_Log(Status.PASS, "Save button is clicked successfully " );
	}
	
	@Test( groups = { "Click_On_Close_Button"})
	@Parameters({"close_After_Save","Choice"})
	public static void Click_On_Close_Button( String close_After_Save,@Optional String choice)
	{
		Util.Report_Log(Status.INFO,  "Clicking on close button...");
		click.Webdriver_Click_By_Action(Close_Button);
		if(!close_After_Save.equalsIgnoreCase("Yes"))
		{	visible.Wait_Until_Visible(2,By.xpath(pop_up_choice_Button.replace("choice",choice)));
			if(choice.equalsIgnoreCase("No"))
			       click.Webdriver_Click_And_Accept_Popup(By.xpath(pop_up_choice_Button.replace("choice",choice)), false);
			else
		       click.Webdriver_Click(By.xpath(pop_up_choice_Button.replace("choice",choice)),false);
			Util.Report_Log(Status.INFO,"Pop Up is displayed , clicked - "+choice);
			int ctr=0;
			while((visible.Is_Displayed(loading_Locator)) && (ctr < 60))
			{
				ctr++;
				//waiting for loader to disappear
			}
		}
		navigate.Switch_To_Tab(0);
		Assert.assertEquals(!Util.Get_Current_Frame().equalsIgnoreCase("proofViewerFrame"),true,"HTML Editor is closed");
	}
	

}
