package com.merlin.dashboard.ui.inddeditor;

import static com.melin.apps.locators.Indesign_Editor_Locators.*;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.melin.apps.api.Indd_Editor_Api;
import com.merlin.common.Annotation;
import com.merlin.common.Dashboard_Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;
/**
 * 
 * @author Pooja Chopra
 */

public class Indd_Left_Panel extends Annotation implements Init
{

	List<String> current_Page_Item = new ArrayList<String>();
	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		navigate.Switch_To_Frame("editorViewerFrame");
	}
	
	@Test( groups = {"Add_Text_Frame"})
	@Parameters({"height", "width" , "place_With_Cursor", "pos_X", "pos_Y"})
	public void Add_Text_Frame(String height, String width, Boolean place_With_Cursor, @Optional String pos_X, @Optional String pos_Y )
	{
		List<String> current_Page_Item = Indd_Util.get_Page_Item_List();
		Util.Report_Log(Status.INFO, "Clicking on create text frame" );
		click.Webdriver_Click(create_Textframe_Icon,true);
		Util.Report_Log(Status.INFO, "Entering details of textframe height and width - "+ height + " , " + width );
		sendkeys.Webdriver_Sendkeys(width_Input_Box, width);
		sendkeys.Webdriver_Sendkeys(height_Input_Box, height);
		if(!place_With_Cursor)
		{
			Util.Report_Log(Status.INFO, "Entering position of text frame "+ pos_X + " , " + pos_Y );
			sendkeys.Webdriver_Sendkeys(x_Position_Input_Box, pos_X);
			sendkeys.Webdriver_Sendkeys(y_Position_Input_Box, pos_Y);
			Util.Report_Log(Status.INFO, "Clicking on create item button");
			click.Webdriver_Click(create_Item_Button, true);
		}
		else
		{
			Util.Report_Log(Status.INFO, "Clicking on place with cursosr button");
			click.Webdriver_Click(place_With_Cursor_Button, true);
		}
		Indd_Util.Wait_For_Loader_To_Disappear();
		Dashboard_Constants.update_Template_Version();
		Dashboard_Constants.page_Item_Id = Indd_Util.get_Created_Page_Item_Id(current_Page_Item);
		Util.Report_Log(Status.INFO, "New page item created with id - " + Dashboard_Constants.page_Item_Id );
	}
	
	@Test( groups = {"Click_Add_Image_Button"})
	public void Click_Add_Image_Button()
	{
		current_Page_Item = Indd_Util.get_Page_Item_List();
		Util.Report_Log(Status.INFO, "Clicking on create image frame icon" );
		click.Webdriver_Click(create_Image_frame_Icon,true);
	}
	
	@Test(groups = {"Click_Image_Selection_Dropdown"})
	public void Click_Image_Selection_Dropdown()
	{
		Util.Report_Log(Status.INFO, "Clicking on image selection dropdown" );
		click.Webdriver_Click(image_Selection_Dropdown,true);
	}

	@Test(groups = {"Select_Image_Source"})
	@Parameters({"image_Source","image","folder"})
	public void Select_Image_Source(@Optional  String image_Source, @Optional  String img, @Optional String folder)
	{
		Util.Report_Log(Status.INFO, "Clicking on selecting image source - " + image_Source );
		visible.Pause(5);
		//visible.Wait_Until_Visible(5,By.xpath(image_Source_Type.replace("SOURCE_TYPE", image_Source)));
		click.Webdriver_Click(By.xpath(image_Source_Type.replace("SOURCE_TYPE", image_Source)),true);
		if(image_Source.equalsIgnoreCase("Shared Assets")){
			visible.Wait_For_Page_To_Load();
			navigate.Switch_To_Frame("sharedAssetFrame");
			visible.Pause(3);
			visible.Scroll_View(By.xpath(image_Shared_Asset_links.replace("image",folder)));
			click.Webdriver_Double_Click_By_Action(By.xpath(image_Shared_Asset_links.replace("image",folder)));
			visible.Pause(2);
			click.Webdriver_Click_By_Action(By.xpath(image_Shared_Asset_links.replace("image",img)));
		}else click.Webdriver_Click_By_Action(By.xpath(image_document_links.replace("image",img)));

	}

	@Test(groups = {"Click_On_Select_Button"})
	@Parameters({"Option"})
	public void Click_On_Select_Button(@Optional String Option )
	{
		click.Webdriver_Click_By_Action(select_Button);
		navigate.Switch_To_Default_Frame();
		navigate.Switch_To_Frame("editorViewerFrame");
		visible.Wait_For_Page_To_Load();
		if(!Option.equals(null) && Option.equalsIgnoreCase("Edit_Image") )
		{
		Dashboard_Constants.update_Template_Version();
//		Dashboard_Constants.page_Item_Id = Indd_Util.get_Created_Page_Item_Id(current_Page_Item);
//		Util.Report_Log(Status.INFO, "New page item created with id - " + Dashboard_Constants.page_Item_Id );
		}
	}

	@Test(groups = {"Click_On_Cancel_Button_Image_Frame"})
	public void Click_On_Cancel_Button_Image_Frame()
	{
		click.Webdriver_Click_By_Action(cancel_Button);
		navigate.Switch_To_Default_Frame();
		navigate.Switch_To_Frame("editorViewerFrame");
		click.Webdriver_Click_By_Action(cancel_Button);
	}

	@Test(groups = {"Add_Image_Frame"})
	@Parameters({"height", "width" , "place_With_Cursor", "pos_X", "pos_Y"})
	public void Add_Image_Frame(String height, String width, Boolean place_With_Cursor, @Optional String pos_X, @Optional String pos_Y )
	{
		sendkeys.Webdriver_Sendkeys(width_Input_Box, width);
		sendkeys.Webdriver_Sendkeys(height_Input_Box, height);
		if(!place_With_Cursor)
		{
			Util.Report_Log(Status.INFO, "Entering position of text frame "+ pos_X + " , " + pos_Y );
			sendkeys.Webdriver_Sendkeys(x_Position_Input_Box, pos_X);
			sendkeys.Webdriver_Sendkeys(y_Position_Input_Box, pos_Y);
			Util.Report_Log(Status.INFO, "Clicking on create item button");
			visible.Pause(3);
			visible.Wait_Until_Visible(5,create_Item_Button);
			click.Webdriver_Click(create_Item_Button, true);
		}
		else
		{
			Util.Report_Log(Status.INFO, "Clicking on place with cursosr button");
			click.Webdriver_Click(place_With_Cursor_Button, true);
		}
		Indd_Util.Wait_For_Loader_To_Disappear();
		Dashboard_Constants.update_Template_Version();
		Dashboard_Constants.page_Item_Id = Indd_Util.get_Created_Page_Item_Id(current_Page_Item);
		Util.Report_Log(Status.INFO, "New page item created with id - " + Dashboard_Constants.page_Item_Id );
	}

	
	@Test(groups = {"Validating_Document_Links_Coming_In_Editor"})
	@Parameters({"image_Links_List"})
	public static void Validating_Document_Links_Coming_In_Editor(@Optional ArrayList<String> image_Links_List)
	{
		image_Links_List = ( image_Links_List == null ) ? Indd_Editor_Api.image_Links_List :image_Links_List  ;
		Util.Report_Log(Status.INFO, "Validating image links list is present in content editor - " + image_Links_List);
		ArrayList<String> document_Links_From_UI = text.Get_Elements_Text(document_Links_Images);
		
		Util.Report_Log(Status.INFO, "Document links fetched from dashboard - " + document_Links_From_UI);
		Assert.assertEquals(document_Links_From_UI.get(0), image_Links_List.get(0), "Documents links list is not same as expected.");
		
		Util.Report_Log(Status.PASS,  "Documents links list is same as expected");
	}
}
