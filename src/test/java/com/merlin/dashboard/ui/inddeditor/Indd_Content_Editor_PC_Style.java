package com.merlin.dashboard.ui.inddeditor;

import static com.melin.apps.locators.Indesign_Editor_Locators.Create_Style_Name_Input_Box;
import static com.melin.apps.locators.Indesign_Editor_Locators.Operation_Ok_Button;
import static com.melin.apps.locators.Indesign_Editor_Locators.P_C_Style_Property_Option;
import static com.melin.apps.locators.Indesign_Editor_Locators.Style_Dialog_Loading_Locator;
import static com.melin.apps.locators.Indesign_Editor_Locators.Style_Operation_Radio_Input_Button;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Dashboard_Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;

/**
 * @author Pooja Chopra
 */
public class Indd_Content_Editor_PC_Style extends Annotation implements Init {

	
	
	@BeforeClass(alwaysRun = true)
	public static void Before_Class() {
		navigate.Switch_To_Default_Frame();
		navigate.Switch_To_Frame("editorViewerFrame");
		navigate.Switch_To_Frame("contentEditorframe");
		if (Constants.reg_Name.contains("REG-550")) {
			navigate.Switch_To_Frame_By_Xpath(By.xpath("//div[@id='cStyleElement-body']/iframe"));
		} else if (Constants.reg_Name.contains("REG-523")) {
			navigate.Switch_To_Frame_By_Xpath(By.xpath("//div[@id='pStyleElement-body']/iframe"));
		}
	}
	
	
	@Test(groups = { "Select_C_Style_Edit_Option_From_List" })
	@Parameters({ "option_Name" })
	public static void Select_C_Style_Edit_Option_From_List(String option_Name) {
		Util.Report_Log(Status.INFO, "Selecting C Style for edit -" + option_Name);
		Indd_Util.select_Style_Option(option_Name);
		Util.Report_Log(Status.PASS, "C Styles Option selected successfully");
	}

	@Test(groups = { "Select_P_Style_Edit_Option_From_List" })
	@Parameters({ "option_Name" })
	public static void Select_P_Style_Edit_Option_From_List(String option_Name) {
		Util.Report_Log(Status.INFO, "Selecting P Style for edit -" + option_Name);
		select_Style_Option(option_Name);
		Util.Report_Log(Status.PASS, "P Styles list dialog opened succesfully");
	}

	@Test(groups = { "Edit_Style_Properties_Options" })
	@Parameters({ "properties"})
	public static void Edit_Style_Properties_Options(String option_Name) {
		Constants.propertyMap.clear();
		Util.Report_Log(Status.INFO, "Editing Properties-" +option_Name);
		String []options=option_Name.split(",");
		for(int i=0;i<options.length;i++) {
			Constants.propertyMap.put(options[i].split("-")[0],options[i].split("-")[1]);
		}
		 Iterator hmIterator = Constants.propertyMap.entrySet().iterator(); 
		 while (hmIterator.hasNext()) { 
	            Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
	            String property_Value = ((String)mapElement.getValue()); 
	            System.out.println(mapElement.getKey() + " : " + property_Value);
	            Indd_Util.Select_Properties((String)mapElement.getKey(), property_Value);
	        } 
		Util.Report_Log(Status.PASS, "Styles Property edit succesfully");
	}
	
	@Test(groups = { "Validate_Style_Properties" })
	@Parameters({"validation_Type"})
	public static void Validate_Style_Properties(String validation_Type) {
		Util.Report_Log(Status.INFO, "Validating Properties by "+validation_Type);
		 Iterator hmIterator = Constants.propertyMap.entrySet().iterator(); 
		 while (hmIterator.hasNext()) { 
	            Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
	            String property_Value = ((String)mapElement.getValue()); 
	            System.out.println(mapElement.getKey() + " : " + property_Value);
	            if(validation_Type.equalsIgnoreCase("UI"))

	            	Indd_Util.Validate_Properties_On_Ui((String)mapElement.getKey(), property_Value);
	            else
	            	Indd_Util.Validate_Properties_From_Api((String)mapElement.getKey(), property_Value);
	        } 
		Util.Report_Log(Status.PASS, "Styles Property validated succesfully from -"+validation_Type);
	}

	
	@Test(groups = { "Perform_Style_Operation" })
	@Parameters({ "operation_Type","new_Style_Name" })
	public static void Perform_Style_Operation(String operation_Type,@Optional String style_Name) {
		Before_Class();
		String xpath=Style_Operation_Radio_Input_Button.replace("$operation",operation_Type);
		click.Webdriver_Click_By_Action(By.xpath(xpath));
		if(operation_Type.contains("Create")) {
			style_Name = style_Name.replace("random", Constants.random_Number);
			sendkeys.Webdriver_Sendkeys(Create_Style_Name_Input_Box, style_Name);
			Dashboard_Constants.style_Name=style_Name;
		}
		click.Webdriver_Click_By_Action(Operation_Ok_Button);
		visible.Wait_Until_Invisible(6,Style_Dialog_Loading_Locator);
		Util.Report_Log(Status.INFO,"Successfully performed operation -"+operation_Type);
	}
	

	
	private static void Select_Properties(String option_Name, String option_Value) {
		String property_Xpath = P_C_Style_Property_Option.replace("$property_Name", option_Name.split("_")[0]);
		switch (option_Name) {
			case "Font Family":
			case "Font Style":
			case "Font Kerning":
			case "Position":
			case "Color":
			case "List Type":
				property_Xpath = property_Xpath.replace("$element_Type", "select");
				select.Webdriver_Select_Dropdown_List_By_Visible_Text(By.xpath(property_Xpath), option_Value);
			    break;
			case "Alignment_Indents":
				property_Xpath = property_Xpath.replace("$element_Type", "select[@id='textAlignSelect']");
				select.Webdriver_Select_Dropdown_List_By_Visible_Text(By.xpath(property_Xpath), option_Value);
				break;
			case "Alignment_Bullet":
				property_Xpath = property_Xpath.replace("$element_Type", "select[@id='bulletAlignmentSelect']");
				select.Webdriver_Select_Dropdown_List_By_Visible_Text(By.xpath(property_Xpath), option_Value);
				break;
			case "Font Size":
			case "Line Height":
			case "Letter Spacing":
			case "Horizontal Scale":
			case "Vertical Scale":
			case "Last Line Indent":
			case "Space Before":
			case "Space After":
			case "Baseline Shift":
			case "Tab Position":
			case "Right Indent":	
				property_Xpath = property_Xpath.replace("$element_Type", "input");
				sendkeys.Webdriver_Sendkeys_Javascript(By.xpath(property_Xpath), option_Value);
			    break;
			case "Strikethrough":
			case "Underline":
				property_Xpath = property_Xpath.replace("$element_Type", "input");
				click.Webdriver_Javascipt_Click_Without_Wait(By.xpath(property_Xpath));
			    break;
			case "Left Indent_Indents":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='indentLeftIndent']");
				sendkeys.Webdriver_Sendkeys_Javascript(By.xpath(property_Xpath), option_Value);
				break;
			case "First Line Indent_Indents":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='indentFirstLineIndent']");
				sendkeys.Webdriver_Sendkeys_Javascript(By.xpath(property_Xpath), option_Value);
				break;
			case "Left Indent_Bullet":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='bulletLeftIndent']");
				sendkeys.Webdriver_Sendkeys_Javascript(By.xpath(property_Xpath), option_Value);
				break;
			case "First Line Indent_Bullet":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='bulletFirstLineIndent']");
				sendkeys.Webdriver_Sendkeys_Javascript(By.xpath(property_Xpath), option_Value);
				break;
			default :
				System.out.println("Invalid option");
		}
	}

	private static void Validate_Properties(String option_Name, String option_Value) {
		String value_Fetched="";
		String property_Xpath = P_C_Style_Property_Option.replace("$property_Name", option_Name.split("_")[0]);
		switch (option_Name) {
			case "Font Family":
			case "Font Style":
			case "Font Kerning":
			case "Position":
			case "Color":
			case "List Type":
				property_Xpath = property_Xpath.replace("$element_Type", "select");
			    break;
			case "Alignment_Indents":
				property_Xpath = property_Xpath.replace("$element_Type", "select[@id='textAlignSelect']");
				break;
			case "Alignment_Bullet":
				property_Xpath = property_Xpath.replace("$element_Type", "select[@id='bulletAlignmentSelect']");
				break;
			case "Font Size":
			case "Line Height":
			case "Letter Spacing":
			case "Horizontal Scale":
			case "Vertical Scale":
			case "Last Line Indent":
			case "Space Before":
			case "Space After":
			case "Baseline Shift":
			case "Tab Position":
			case "Right Indent":	
				property_Xpath = property_Xpath.replace("$element_Type", "input");
			    break;
			case "Strikethrough":
			case "Underline":
				property_Xpath = property_Xpath.replace("$element_Type", "input");
				break;
			case "Left Indent_Indents":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='indentLeftIndent']");
				break;
			case "First Line Indent_Indents":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='indentFirstLineIndent']");
				break;
			case "Left Indent_Bullet":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='bulletLeftIndent']");
				break;
			case "First Line Indent_Bullet":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='bulletFirstLineIndent']");
				break;
			default :
				System.out.println("Invalid option");
			break;
		}
		value_Fetched=text.Javascript_Get_Value(By.xpath(property_Xpath));
		Assert.assertTrue(value_Fetched.contains(option_Value),"Property value not as expected -" +option_Value);
	}
	
	
	
	private static void select_Style_Option(String option_Name) {
		option_Name = option_Name.replace("_", " ");
		click.Webdriver_Click_By_Action(By.xpath("//a[text()='" + option_Name + "']"));
		if(option_Name.equalsIgnoreCase("Bullet and Numbering")){
			visible.Wait_Until_Visible(3, By.xpath("//h2[text()='Bullets and Numbering']"));
			Assert.assertTrue(visible.Is_Displayed(By.xpath("//h2[text()='Bullets and Numbering']")));
		}else{
		visible.Wait_Until_Visible(3, By.xpath("//h2[text()='" + option_Name + "']"));
		Assert.assertTrue(visible.Is_Displayed(By.xpath("//h2[text()='" + option_Name + "']")));
		}
		Util.Report_Log(Status.PASS, "Successfully clicked on option -" + option_Name);
	}


}
