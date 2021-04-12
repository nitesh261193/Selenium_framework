package com.merlin.dashboard.ui.contenteditor;

import static com.melin.apps.locators.Content_Editor_Locators.cancel_Button;
import static com.melin.apps.locators.Content_Editor_Locators.color_Box_Close_Button;
import static com.melin.apps.locators.Content_Editor_Locators.color_List_Elements;
import static com.melin.apps.locators.Content_Editor_Locators.color_List_Xpath;
import static com.melin.apps.locators.Content_Editor_Locators.font_Button;
import static com.melin.apps.locators.Content_Editor_Locators.font_Family_List;
import static com.melin.apps.locators.Content_Editor_Locators.parent_Font_List;
import static com.melin.apps.locators.Content_Editor_Locators.source_Code_Button;
import static com.melin.apps.locators.Content_Editor_Locators.source_Code_Ok_Button;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.melin.apps.api.Content_Editor_Api;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;


/**
 * @author Pooja Chopra
 *
 */
public class Content_Editor_Ui extends Annotation implements Init
{
	static Properties prop = null;
	@BeforeTest(alwaysRun = true)
	public void Before_Test() throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard.properties"));
		
	}

	
	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		String frameName= (Constants.reg_Name.contains("REG-450") || Constants.reg_Name.contains("REG-550"))? "contentEditorframe" : "contentEditorIFrame";
		navigate.Switch_To_Frame(frameName);
}
	
	@Test( groups = { "Add_Data_In_Content_Editor"})
	@Parameters({"content_Value"})
	public static void Add_Data_In_Content_Editor(String content_Value)
	{
		content_Value = content_Value.replace("random", Constants.random_Number);
		Util.Report_Log(Status.INFO,  "Adding data in content editor - " + content_Value);
		Content_Editor_Util.Type_Text_Wysiwyg("htmlEditor_ifr", By.id("tinymce"), content_Value);
		click.Webdriver_Click(By.xpath("//button[@id='source-code-btn-button']"),true);
		click.Webdriver_Click(By.xpath("//button[text() ='Ok']"),true);
	}
	
	@Test( groups = { "Validating_Data_In_Content_Editor"})
	@Parameters({"content_Data"})
	public static void Validating_Data_In_Content_Editor(String content_Data)
	{
		content_Data = content_Data.replace("random", Constants.random_Number);
		
		Util.Report_Log(Status.INFO,  "Validating data in content editor - " + content_Data);
		String data = Content_Editor_Util.Get_Data_Wysiwyg();
		Assert.assertEquals(data, content_Data, "Data not found same as expected");
	}
	
	@Test( groups = { "Save_Content_Editor"})
	public static void Save_Content_Editor()
	{
		Util.Report_Log(Status.INFO,  "Clicking on save button...");
		click.Webdriver_Click(source_Code_Button,true);
		click.Webdriver_Click(source_Code_Ok_Button,true);
		navigate.Switch_To_Default_Frame();
		if ((Constants.reg_Name.contains("REG_406"))) {
			navigate.Switch_To_Frame("editorViewerFrame");
		}
		String Button_Id= (Constants.reg_Name.contains("REG_406"))? "ceditorSaveElement" : "saveButton";
		click.Webdriver_Click(By.id(Button_Id),true);
	}

	@Test( groups = { "Click_Cancel_Button"})
	public static void Click_Cancel_Button()
	{
		Util.Report_Log(Status.INFO ,"Clicking on cancel button...");
		navigate.Switch_To_Default_Frame();
		click.Webdriver_Click(cancel_Button, true);
	}
	
	@Test( groups = { "Validating_Colors_In_Editor"})
	@Parameters({"color_List"})
	public static void Validating_Colors_In_Editor_Through_Dashboard(@Optional ArrayList<String> color_List)
	{
		color_List = ( color_List == null ) ? Content_Editor_Api.color_List :color_List  ;
		
		Util.Report_Log(Status.INFO, "Validating color(s) is present in content editor - " + color_List);
		click.Webdriver_Click(color_List_Xpath, true);
		ArrayList<String> color_List_From_UI = text.Get_Elements_Text_By_Attribute(color_List_Elements,"title");
		color_List_From_UI.remove("No Color");
		click.Webdriver_Click(color_Box_Close_Button, false);
		Util.Report_Log(Status.INFO, "Colors fetched from dashboard - " + color_List_From_UI);
		Assert.assertEquals(color_List_From_UI, color_List, "Colors not same as expected.");
		Util.Report_Log(Status.PASS,  "Color list is same as expected");
	}
	

	@Test( groups = { "Open_Style_Dialog_In_Editor"})
	@Parameters({"style_Type"})
	public static void Open_Style_Dialog_In_Editor(String style_Type )
	{
		Util.Report_Log(Status.INFO, "Opening styles(s) dialog in content editor -" + style_Type);
		click.Webdriver_Click(By.xpath("//div[@aria-label='"+style_Type+"']//button"), true);
	}
	
	@Test( groups = { "Validating_Fonts_In_Editor"})
	@Parameters({"parent_Font_Family_List"})
	public static void Validating_Fonts_In_Editor(@Optional ArrayList<String> parent_Font_Family_List)
	{
		if( parent_Font_Family_List == null ) 
		{
			parent_Font_Family_List = new ArrayList<>();
			parent_Font_Family_List.addAll(Content_Editor_Api.font_Map.keySet()) ;
		}
		Util.Report_Log(Status.INFO, "Validating font(s) is present in content editor. ");
		click.Webdriver_Click(font_Button, true);
		ArrayList<String> font_Parent_List_From_Ui = text.Get_Elements_Text(parent_Font_List);
	    HashMap<String,ArrayList<String>> font_Map_Ui = new HashMap<String,ArrayList<String>>();
		for(String font_Parent : font_Parent_List_From_Ui)
		{
			click.Webdriver_Click(By.xpath("(//div[contains(@class,'mce-in')][not(contains(@style,'display: none;'))])[1]//div[span='"+font_Parent+"']//div"), true);
			ArrayList<String> font_List_From_Ui = text.Get_Elements_Text(font_Family_List);
			font_Map_Ui.put(font_Parent, font_List_From_Ui);
		}
		Util.Report_Log(Status.INFO, "Fonts coming from UI - " + font_Map_Ui);
		Assert.assertEquals(Content_Editor_Api.font_Map, font_Map_Ui, "Fonts not same as expected.");
		Util.Report_Log(Status.PASS, "Fonts shown on UI is same as expected");
	}
	
	@Test( groups = { "Click_Apply_Cancel_Button_In_Dialog"})
	@Parameters({"button"})
	public static void Click_Apply_Cancel_Button_In_Dialog(String button )
	{
		Util.Report_Log(Status.INFO, "Clicking on " + button + " in dialog.");
		click.Webdriver_Click(By.xpath("//div[starts-with(@id,'mceu')]//button[span='"+button+"']"), true);
	}
	

}
