package com.merlin.dashboard.ui.htmleditor;

import static com.melin.apps.locators.Html_Editor_Locators.cancel_Button_Asset;
import static com.melin.apps.locators.Html_Editor_Locators.edit_Button;
import static com.melin.apps.locators.Html_Editor_Locators.file_links;
import static com.melin.apps.locators.Html_Editor_Locators.select_Button_Asset;
import static com.melin.apps.locators.Indesign_Editor_Locators.image_Shared_Asset_links;

import java.util.ArrayList;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.melin.apps.api.Html_Editor_Api;
import com.merlin.common.Annotation;
import com.merlin.common.Init;
import com.merlin.common.Util;


/**
 * @author Nitesh Gupta
 *
 */
public class HtmlEditor_Right_Panel extends Annotation implements Init
{
	static Properties prop = null;

	
	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		navigate.Switch_To_Frame("proofViewerFrame");
	}

	@Test( groups = {"Click_On_Edit_Button"})
	public static void Click_On_Edit_Button()
	{
		Util.Report_Log(Status.INFO, "Clicking on edit icon to open CE" );
		click.Webdriver_Click(edit_Button,true);
		Util.Report_Log(Status.PASS, "Edit button is click successfully " );
		visible.Wait_For_Page_To_Load();
	}

	@Test(groups = {"Select_Image_Source"})
	@Parameters({"image","folder"})
	public void Select_Image_Source(String img, String folder)
	{
		navigate.Switch_To_Frame("sharedPanelFrame");
		visible.Wait_For_Page_To_Load();
		click.Webdriver_Double_Click_By_Action(By.xpath(image_Shared_Asset_links.replace("image",folder)));
		Util.Report_Log(Status.INFO,"Shared Asset folder is opened- " + folder);
		click.Webdriver_Click(By.xpath(image_Shared_Asset_links.replace("image",img)),true);
		click.Webdriver_Click(select_Button_Asset,true);
		navigate.Switch_To_Default_Frame();
		Util.Report_Log(Status.INFO, "New Image is added successfully" );
	}

	@Test(groups = {"Click_On_Cancel_Button"})
	public void Click_On_Cancel_Button( )
	{
		Util.Report_Log(Status.INFO, "Clicking on cancel Button" );
		click.Webdriver_Click(cancel_Button_Asset,true);
		navigate.Switch_To_Default_Frame();
		Util.Report_Log(Status.INFO, "Cancel Button is clicked successfully" );

	}

	@Test(groups = {"Validating_FileType_Coming_In_Shared_Asset"})
	@Parameters({"folder_Name" , "file_List"})
	public static void Validating_FileType_Coming_In_Shared_Asset(String folder_Name , @Optional ArrayList<String> file_List)
	{
		file_List = ( file_List == null ) ? Html_Editor_Api.file_List :file_List  ;
		Util.Report_Log(Status.INFO, "Validating files coming from shared assets - " + file_List);
		navigate.Switch_To_Frame("sharedPanelFrame");
		ArrayList<String> file_Links_From_UI = text.Get_Elements_Text_By_Attribute(file_links,"title");
		Util.Report_Log(Status.INFO, "files fetched from dashboard UI - " + file_Links_From_UI);
		Assert.assertEquals(file_Links_From_UI, file_List, "Shared Asset File list  is not same as expected.");
		Util.Report_Log(Status.PASS,  "Shared Asset contains only Image File in html Editor");
	}
}
