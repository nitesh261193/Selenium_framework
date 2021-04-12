package com.merlin.dashboard.ui.inddeditor;


import static com.melin.apps.locators.Indesign_Editor_Locators.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Dashboard_Constants;
import com.merlin.common.File_Utils;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.merlin.utils.Json_Parsor;
import com.merlin.utils.Xml_Parsor;
import com.aventstack.extentreports.Status;


/**
 * @author Pooja Chopra
 *
 */
public class Indd_Util extends Annotation implements Init
{
	Integer version;
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod()
	{
		version = Dashboard_Constants.get_Template_Version();
	}
	
	public static void Wait_For_Loader_To_Disappear() {
		visible.Pause(1);
		visible.Wait_Until_Attribute_Visible(120, loading_Locator,"class", "ng-hide");
		Util.Report_Log(Status.INFO,"loader gets disappear");
	}
	
	public static void Warning_Pop_Up_Handle() {
		Util.Report_Log(Status.INFO, "Clicking on close icon");
		visible.Wait_Until_Visible(20,pop_Close_Button);
		click.Webdriver_Click(pop_Close_Button,true);
	}

	public static List<String> get_Page_Item_List() {
		return text.Get_Elements_Text_By_Attribute(page_Item, "id");
	}
	
	public static String get_Created_Page_Item_Id(List<String> initial_Pageitem_List) {
		List<String> current_Pageitem_List = get_Page_Item_List();
		System.out.println("Num of items created before - " + initial_Pageitem_List.size() + " Now is " + current_Pageitem_List.size());
		current_Pageitem_List.removeAll(initial_Pageitem_List);
		Assert.assertTrue(current_Pageitem_List.size() == 1, "More than one item created");
		return current_Pageitem_List.get(0);
	}
	
	public static String Get_Style_For_Api_Content(String style_Type) {
		switch(style_Type) {
		case "C_Styles":
			return "getCharStylesWithAttribs";
		case "P_Styles" :
			return "getParaStylesWithAttribs";
		default:
			return "";
		}
	}
	
	@Test( groups = { "Fetch_Asset_Id_For_Indd_Editor"})
	public void Fetch_Asset_Id_For_Indd_Editor() throws UnsupportedEncodingException
	{
		navigate.Switch_To_Default_Frame();
		String frame_Source = text.Webdriver_getText_ByAttribute(By.id("editorViewerFrame"), "src");
		String decode_URL=URLDecoder.decode( frame_Source, "UTF-8" );
		String url_Json=decode_URL.substring(decode_URL.indexOf("{"),decode_URL.lastIndexOf("}")+1);
		String asset_Id = Json_Parsor.Get_Value_From_Json(url_Json, "jobId");
		Dashboard_Constants.asset_Id = asset_Id;
		Util.Report_Log(Status.INFO, asset_Id);
	}
	
	@Test(groups = { "Download_Template_Frame_Image", "Get_Indd_Template_Xml" })
	@Parameters({"rename_File"})
	public void Get_Indd_Template_Xml(@Optional String rename_File) throws InterruptedException {
		rename_File = (rename_File == null) ? "version" : rename_File ;
		Util.Report_Log(Status.INFO, "Downloading template xml for version - " +version);
		String url = Constants.indd_Template_Url.replaceAll("ASSET_ID", Dashboard_Constants.asset_Id).replace("VERSION_ID", version+"");
		String file_Name = File_Utils.Download_File_From_URL(url , Constants.artifact_Path, Dashboard_Constants.asset_Id	, ".xml" , rename_File);
		Util.Report_Log(Status.INFO, "Template xml save as - " + Constants.artifact_Path + file_Name);
	}

	@Test(dependsOnMethods = {"Get_Indd_Template_Xml"} , groups = {  "Download_Template_Frame_Image", "Fetch_Image_Url_From_Xml" })
	@Parameters({ "textframe_Id"})
	public void Fetch_Image_Url_From_Xml(@Optional String textframe_Id) throws Exception {
		textframe_Id = (textframe_Id == null )? Dashboard_Constants.page_Item_Id : textframe_Id;
		String pageitem_Id = textframe_Id.split("_")[1]; 
		String image_URL =  Xml_Parsor.Get_Value_Of_Attribute_In_Xml( Constants.artifact_Path+File.separator+Dashboard_Constants.asset_Id+"_V"+version+".xml", "/document/spreads/spread/page/pageitem[@id='"+pageitem_Id+"']", "source");
		Dashboard_Constants.indd_Frame_Image_Url = image_URL ;
		Util.Report_Log(Status.INFO, "Image URL received as - " + image_URL);
	}
	
	@Test(dependsOnMethods = {"Fetch_Image_Url_From_Xml"} ,groups = { "Download_Template_Frame_Image", "Download_Textframe_Image" })
	@Parameters({"Frame","FileName"})
	public void Download_Textframe_Image(String frame,@Optional String filename) throws InterruptedException {
		Util.Report_Log(Status.INFO, "Downloading image for version - " +version);
		String url = Constants.indd_Template_Image_Url.replaceAll("ASSET_ID", Dashboard_Constants.asset_Id).replaceAll("IMAGE_URL", Dashboard_Constants.indd_Frame_Image_Url);
		String file_Name = File_Utils.Download_File_From_URL(url , Constants.artifact_Path, frame, ".png" , "version",filename);
		Util.Report_Log(Status.INFO, "Image frame saved as  - " + Constants.artifact_Path + File.separator + file_Name);
	}

	public static void Validate_Properties_From_Api(String key, String property_Value) {
		String property_Tag=Get_Style_Attribute_For_Api(key);
		String tag_Value_From_Api=Constants.prop_Map_From_Api.get(property_Tag);
		Assert.assertTrue(tag_Value_From_Api.contains(property_Value),"Property value not as expected -" +property_Value);
	}
	
	public static String Get_Style_Attribute_For_Api(String property_Name) {
		switch(property_Name) {
		case "Font Family":
			return "font-family";
		case "Font Style":
			return"font-style";
		case "Font Kerning":
			return"font-kerning";
		case "Position":
			return"vertical-align";
		case "Color":
			return"color";
		case "List Type":
			return"list-style-type";
		case "Alignment_Indents":
			return"text-align";
		case "Alignment_Bullet":
			return"bullets-alignment";
		case "Font Size":
			return"font-size";
		case "Line Height":
			return"line-height";
		case "Letter Spacing":
			return"letter-spacing";
		case "Horizontal Scale":
			return"horizontal-scale";
		case "Vertical Scale":
			return"vertical-scale";
		case "Last Line Indent":
			return"last-line-indent";
		case "Space Before":
			return"margin-top";
		case "Space After":
			return"margin-bottom";
		case "Baseline Shift":
			return"baseline-shift";
		case "Tab Position":
			return"tab-stops";
		case "Right Indent":	
			return"margin-right";
		case "Strikethrough":
		case "Underline":
			return"text-decoration";
		case "Left Indent_Bullet":
		case "Left Indent_Indents":
			return"margin-left";
		case "First Line Indent_Indents":
		case "First Line Indent_Bullet":
			return"text-indent";
		default :
			System.out.println("Invalid attribute");
			return "";
		}
	}
	
	public static void Select_Properties(String option_Name, String option_Value) {
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
				sendkeys.Webdriver_Sendkeys(By.xpath(property_Xpath), option_Value);
			    break;
			case "Strikethrough":
			case "Underline":
				property_Xpath = property_Xpath.replace("$element_Type", "input");
				click.Webdriver_Javascipt_Click_Without_Wait(By.xpath(property_Xpath));
			    break;
			case "Left Indent_Indents":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='indentLeftIndent']");
				sendkeys.Webdriver_Sendkeys(By.xpath(property_Xpath), option_Value);
				break;
			case "First Line Indent_Indents":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='indentFirstLineIndent']");
				sendkeys.Webdriver_Sendkeys(By.xpath(property_Xpath), option_Value);
				break;
			case "Left Indent_Bullet":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='bulletLeftIndent']");
				sendkeys.Webdriver_Sendkeys(By.xpath(property_Xpath), option_Value);
				break;
			case "First Line Indent_Bullet":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='bulletFirstLineIndent']");
				sendkeys.Webdriver_Sendkeys(By.xpath(property_Xpath), option_Value);
				break;
			default :
				System.out.println("Invalid option");
		}
	}

	public static void Validate_Properties_On_Ui(String option_Name, String option_Value) {
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
				value_Fetched= text.Javascript_Get_Value(By.xpath(property_Xpath));
			    break;
			case "Alignment_Indents":
				property_Xpath = property_Xpath.replace("$element_Type", "select[@id='textAlignSelect']");
				value_Fetched= text.Javascript_Get_Value(By.xpath(property_Xpath));
				break;
			case "Alignment_Bullet":
				property_Xpath = property_Xpath.replace("$element_Type", "select[@id='bulletAlignmentSelect']");
				value_Fetched= text.Javascript_Get_Value(By.xpath(property_Xpath));
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
				value_Fetched= text.Javascript_Get_Value(By.xpath(property_Xpath));
			    break;
			case "Strikethrough":
			case "Underline":
				property_Xpath = property_Xpath.replace("$element_Type", "input");
				value_Fetched= text.Javascript_Get_Value_Checkbox(By.xpath(property_Xpath));
				break;
			case "Left Indent_Indents":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='indentLeftIndent']");
				value_Fetched= text.Javascript_Get_Value(By.xpath(property_Xpath));
				break;
			case "First Line Indent_Indents":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='indentFirstLineIndent']");
				value_Fetched= text.Javascript_Get_Value(By.xpath(property_Xpath));
				break;
			case "Left Indent_Bullet":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='bulletLeftIndent']");
				value_Fetched= text.Javascript_Get_Value(By.xpath(property_Xpath));
				break;
			case "First Line Indent_Bullet":
				property_Xpath = property_Xpath.replace("$element_Type", "input[@id='bulletFirstLineIndent']");
				value_Fetched= text.Javascript_Get_Value(By.xpath(property_Xpath));
				break;
			default :
				System.out.println("Invalid option");
			break;
		}
		if(option_Name.equalsIgnoreCase("Position")) {
			option_Value=option_Value.split(" ")[0];
			option_Value=option_Value.toLowerCase();
		}
		System.out.println("Value fetched is -"+ value_Fetched);
		Assert.assertTrue(value_Fetched.contains(option_Value),"Property value not as expected -" +option_Value);
	}
	
	
	
	public static void select_Style_Option(String option_Name) {
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
