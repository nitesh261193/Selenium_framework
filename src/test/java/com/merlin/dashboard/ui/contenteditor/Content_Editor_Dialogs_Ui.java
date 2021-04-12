package com.merlin.dashboard.ui.contenteditor;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.melin.apps.api.Content_Editor_Api;
import com.merlin.common.Annotation;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;

import static com.melin.apps.locators.Content_Editor_Locators.*;


/**
 * @author Pooja Chopra
 *
 */
public class Content_Editor_Dialogs_Ui extends Annotation implements Init
{

	@BeforeMethod(alwaysRun = true)
	@Parameters({"style_Type"})
	public void Before_Method(String style_Type ) {
		String frame_Xpath = "" ; 
		switch(style_Type)
		{
		case "Text/Character Styles" : frame_Xpath = text_Styles_Frame ; break;
		case "Paragraph Styles" :  frame_Xpath =paragraph_Styles_Frame ;break;
		}
		navigate.Switch_To_Frame_By_Xpath(By.xpath(frame_Xpath));
		
	}
	
	@AfterMethod(alwaysRun = true)
	public void After_Method() {
		navigate.Switch_To_Parent_Frame();
		
	}
		
	@Test( groups = { "Validating_Styles_In_Editor"})
	@Parameters({"style_List"})
	public static void Validating_Styles_In_Editor(@Optional ArrayList<String> style_List)
	{
		style_List = ( style_List == null ) ? Content_Editor_Api.style_List :style_List  ;
		Util.Report_Log(Status.INFO, "Validating styles(s) present in content editor ");
		ArrayList<String> style_List_From_Ui = select.Webdriver_Get_Option_List(style_List_Xpath);
		style_List_From_Ui.remove("");
		Util.Report_Log(Status.INFO, "Styles fetched from dashboard - " + style_List_From_Ui);
		Assert.assertEquals(style_List_From_Ui, style_List, "Styles not same as expected.");
		Util.Report_Log(Status.PASS, "Style list is same as expected");
	}
	
	
}
