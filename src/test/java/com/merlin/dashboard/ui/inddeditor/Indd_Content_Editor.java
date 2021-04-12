package com.merlin.dashboard.ui.inddeditor;

import static com.melin.apps.locators.Indesign_Editor_Locators.associate_Content_Source_Button;
import static com.melin.apps.locators.Indesign_Editor_Locators.associate_Content_Source_Dialog;
import static com.melin.apps.locators.Indesign_Editor_Locators.associate_Content_Source_Type_Cancel_Button;
import static com.melin.apps.locators.Indesign_Editor_Locators.associate_Content_Source_Type_Dropdown;
import static com.melin.apps.locators.Indesign_Editor_Locators.TinyMCE_Button;
import static com.melin.apps.locators.Indesign_Editor_Locators.P_Style_Dialog;
import static com.melin.apps.locators.Indesign_Editor_Locators.C_Style_Dialog;
import static com.melin.apps.locators.Indesign_Editor_Locators.Style_List_Edit_Button;
import static com.melin.apps.locators.Indesign_Editor_Locators.TinyMCE_Apply_Button;

import java.util.ArrayList;
import java.util.Collections;

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
 * @author Pooja Chopra
 */
public class Indd_Content_Editor extends Annotation implements Init
{

	@BeforeClass(alwaysRun = true)
	public static void Before_Class() {
		navigate.Switch_To_Default_Frame();
		navigate.Switch_To_Frame("editorViewerFrame");
		navigate.Switch_To_Frame("contentEditorframe");
	}
	
	@Test( groups = {"Click_On_Associate_Content_Source_Button", "Validating_Content_Source_Coming_In_Editor"})
	public static void Click_On_Associate_Content_Source_Button() {
		Util.Report_Log(Status.INFO, "Clicking on associate content source button" );
		click.Webdriver_Click(associate_Content_Source_Button,true);
		Assert.assertTrue(visible.Is_Displayed(associate_Content_Source_Dialog));
		Util.Report_Log(Status.PASS, "Content source dialog opened succesfully" );
	}
	
	@Test( dependsOnMethods= {"Click_On_Associate_Content_Source_Button"}, groups = { "Validating_Content_Source_Coming_In_Editor"})
	@Parameters({"content_Source_List"})
	public void Validating_Content_Source_Coming_In_Editor(@Optional ArrayList<String> content_Source_List) {
		content_Source_List = ( content_Source_List == null ) ? Indd_Editor_Api.content_List :content_Source_List  ;
		Util.Report_Log(Status.INFO, "Validating content source list is present in content editor - " + content_Source_List);
		navigate.Switch_To_Frame_By_Xpath(By.xpath("//iframe[@src='scripts/vendor/tinymce/plugins/contentSource/dialog.html']"));
		click.Webdriver_Click_By_Action(By.xpath("//select[@id='contentSource_type']"));
		ArrayList<String> content_List_From_UI = select.Webdriver_Get_Option_List(associate_Content_Source_Type_Dropdown);
		content_List_From_UI.remove("NO CONTENT ASSOCIATION");
		Before_Class();
		click.Webdriver_Click(associate_Content_Source_Type_Cancel_Button, true);
		
		Collections.sort(content_List_From_UI);
		Util.Report_Log(Status.INFO, "Contents fetched from dashboard - " + content_List_From_UI);
		Assert.assertTrue(content_List_From_UI.equals(content_Source_List) , "Content source list is not same as expected.");
		Util.Report_Log(Status.PASS,  "Content source list is same as expected");
	}
	
	@Test( groups = {"Click_On_P_Style_Button"})
	public static void Click_On_P_Style_Button() {
	   Util.Report_Log(Status.INFO, "Clicking on P Style Button" );
	   click.Webdriver_Click(By.xpath(TinyMCE_Button.replace("BUTTON_NAME","P Style")),true);
	   Assert.assertTrue(visible.Is_Displayed(P_Style_Dialog));
	   Util.Report_Log(Status.PASS, "Content source dialog opened succesfully" );
	}
	
	@Test( groups = {"Open_C_Styles_List_Dialog"})
	public static void Open_C_Styles_List_Dialog() {
	   Util.Report_Log(Status.INFO, "Clicking on C Style Button" );
	   click.Webdriver_Click(By.xpath(TinyMCE_Button.replace("BUTTON_NAME","C Style")),true);
	   Assert.assertTrue(visible.Is_Displayed(C_Style_Dialog));
	   Util.Report_Log(Status.PASS, "C Styles list dialog opened succesfully" );
	}
	
	@Test(groups = { "Open_C_Style_From_List_Dialog" })
	@Parameters({"C_Style_Name"})
	public static void Open_C_Style_From_List_Dialog(@Optional String C_Style_Name) {
		C_Style_Name=(C_Style_Name != null )?C_Style_Name:Dashboard_Constants.style_Name;
		Util.Report_Log(Status.INFO, "Selecting C Style for edit -"+ C_Style_Name);
		navigate.Switch_To_Frame_By_Xpath(By.xpath("//div[@id='cStyleListBox-body']/iframe"));
		visible.Scroll_To_Element(By.xpath(Style_List_Edit_Button.replace("$style_Name", C_Style_Name)));
		click.Webdriver_Click(By.xpath(Style_List_Edit_Button.replace("$style_Name", C_Style_Name)), false);
		Util.Report_Log(Status.PASS, "C Style edit selected ");
	}

	@Test( groups = {"Open_P_Styles_List_Dialog"})
	public static void Open_P_Styles_List_Dialog() {
		Util.Report_Log(Status.INFO, "Clicking on P Style Button" );
		click.Webdriver_Click(By.xpath(TinyMCE_Button.replace("BUTTON_NAME","P Style")),true);
		Assert.assertTrue(visible.Is_Displayed(P_Style_Dialog));
		Util.Report_Log(Status.PASS, "P Styles list dialog opened succesfully" );
	}

	@Test(groups = { "Open_P_Style_From_List_Dialog" })
	@Parameters({"P_Style_Name"})
	public static void Open_P_Style_From_List_Dialog(@Optional String P_Style_Name) {
		P_Style_Name=(P_Style_Name != null )?P_Style_Name:Dashboard_Constants.style_Name;
		Util.Report_Log(Status.INFO, "Selecting P Style for edit -"+ P_Style_Name);
		navigate.Switch_To_Frame_By_Xpath(By.xpath("//div[@id='pStyleListBox-body']/iframe"));
		visible.Scroll_To_Element(By.xpath(Style_List_Edit_Button.replace("$style_Name", P_Style_Name)));
		click.Webdriver_Click(By.xpath(Style_List_Edit_Button.replace("$style_Name", P_Style_Name)), false);
		Util.Report_Log(Status.PASS, "P Style edit selected ");
	}
	
	
	@Test(groups = { "Click_On_Apply_For_Style_Properties_Options" })
	public static void Click_On_Apply_For_Style_Properties_Options() {
		Before_Class();
		click.Webdriver_Click(TinyMCE_Apply_Button, false);
		Util.Report_Log(Status.PASS, "Clicked on Apply for Style Options");
	}
}
