package com.merlin.dashboard.ui.inddeditor;

import static com.melin.apps.locators.Indesign_Editor_Locators.*;

import java.util.ArrayList;
import java.util.Collections;

import com.merlin.webdriverutilities.Webelement;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.merlin.common.Annotation;
import com.merlin.common.Dashboard_Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;
import com.melin.apps.api.Indd_Editor_Api;

import org.testng.asserts.Assertion;


/**
 * @author Nitesh Gupta
 */
public class Indd_Right_Panel extends Annotation implements Init
{

	@BeforeClass(alwaysRun = true)
	public static void Before_Class() {
		navigate.Switch_To_Default_Frame();
		navigate.Switch_To_Frame("editorViewerFrame");
}
	
	@Test( groups = {"Click_On_Edit_Button"})
	public static void Click_On_Edit_Button()
	{
		Util.Report_Log(Status.INFO, "Clicking on edit icon to open CE" );
		click.Webdriver_Click(edit_Button,true);
		Util.Report_Log(Status.INFO, "Waiting for loader to dissappear" );
		Indd_Util.Wait_For_Loader_To_Disappear();
		Util.Report_Log(Status.PASS, "Edit button is click successfully " );
	}
	
	@Test( groups = { "Save_Indd_Content_Editor"})
	public static void Save_Indd_Content_Editor()
	{	Before_Class();
		Util.Report_Log(Status.INFO, Util.Get_Current_Frame());
		Util.Report_Log(Status.INFO,  "Clicking on save button...");
		click.Webdriver_Click_By_Action(content_Editor_Save_Button);
		Indd_Util.Wait_For_Loader_To_Disappear();
		Dashboard_Constants.update_Template_Version();
		Assert.assertTrue(!webelement.Is_Element_Displayed(content_Editor_Save_Button,15) , "Content Editor closed");

	}
	
	@Test( groups = { "Cancel_Content_Editing_In_Content_Editor"})
	public static void Cancel_Content_Editing_In_Content_Editor()
	{	Before_Class();
		Util.Report_Log(Status.INFO, Util.Get_Current_Frame());
		Util.Report_Log(Status.INFO,  "Clicking on cancel button...");
		click.Webdriver_Click_By_Action(content_Editor_Cancel_Button);
		Indd_Util.Wait_For_Loader_To_Disappear();
		Assert.assertTrue(!webelement.Is_Element_Displayed(content_Editor_Cancel_Button,15) , "Content Editor closed");
	}
	
	

	@Test( groups = { "Add_Image_From_Asset_Manager"})
	public static void Add_Image_From_Asset_Manager()
	{
		Util.Report_Log(Status.INFO,  "Clicking on pick button...");
		click.Webdriver_Click_By_Action(pick_Button);
	}

	@Test(groups = {"Edit_Text_Frame_Position"})
    @Parameters({"pos_X","pos_Y"})
	public static void Edit_Text_Frame_Position( String pos_X, String pos_Y)
    {
		String before_Attribute = webelement.Get_Webelement(By.id(Dashboard_Constants.page_Item_Id)).getAttribute("ng-style");
        sendkeys.Webdriver_Sendkeys(x_Position_Frame, pos_X);
        sendkeys.Webdriver_Sendkeys(y_Position_Frame, pos_Y);
        click.Webdriver_Click(apply_Button,true);
        Indd_Util.Wait_For_Loader_To_Disappear();
        Dashboard_Constants.update_Template_Version();
		String after_Attribute = webelement.Get_Webelement(By.id(Dashboard_Constants.page_Item_Id)).getAttribute("ng-style");
		Assert.assertTrue(!before_Attribute.equalsIgnoreCase(after_Attribute) , "Frame position is changed");
		Util.Report_Log(Status.PASS,"Frame position is changed successfully");
    }
	
	@Test(groups = { "Validating_Image_Content_Source_Coming_In_Editor"})
	@Parameters({"content_Source_List"})
	public void Validating_Image_Content_Source_Coming_In_Editor(@Optional ArrayList<String> image_Content_Source_List)
	{
		image_Content_Source_List = ( image_Content_Source_List == null ) ? Indd_Editor_Api.image_var_List :image_Content_Source_List ;

		Util.Report_Log(Status.INFO, "Validating content source list is present in content editor - " + image_Content_Source_List);
		click.Webdriver_Click(associate_Image_Content_Source_Type_Dropdown,true);

		ArrayList<String> image_Content_List_From_UI = select.Webdriver_Get_Option_List(associate_Image_Content_Source_Type_Dropdown);
		image_Content_List_From_UI.remove("NO CONTENT ASSOCIATION");
		click.Webdriver_Click(associate_Image_Content_Source_Type_Dropdown,true);
		Collections.sort(image_Content_List_From_UI);
		Util.Report_Log(Status.INFO, "Contents fetched from dashboard - " + image_Content_List_From_UI);

		Assert.assertTrue(image_Content_List_From_UI.equals(image_Content_Source_List) , "Content source list is not same as expected.");
		Util.Report_Log(Status.PASS, "Content source list is same as expected");
	}	

}
