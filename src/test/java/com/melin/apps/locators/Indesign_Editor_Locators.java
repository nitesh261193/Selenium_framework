package com.melin.apps.locators;

import org.openqa.selenium.By;

/**
 * @author Nitesh Gupta
 *
 */

public class Indesign_Editor_Locators {

	// ------------------- Top Panel Locators ----------------------//
	public static final By indd_Save_Button = By.xpath("//a[@uib-tooltip='Save']");

	// ------------------- Left Panel Locators ----------------------//
	public static final By create_Textframe_Icon = By.xpath("//a[@title='Add TextFrame']//img");
	public final static By width_Input_Box = By
			.xpath("//label[contains(text() ,'Width')]/parent::div/following-sibling::div/input");
	public final static By height_Input_Box = By
			.xpath("//label[contains(text() ,'Height')]/parent::div/following-sibling::div/input");
	public final static By x_Position_Input_Box = By
			.xpath("//label[contains(text() ,'X')]/parent::div/following-sibling::div/input");
	public final static By y_Position_Input_Box = By
			.xpath("//label[contains(text() ,'Y')]/parent::div/following-sibling::div/input");
	public final static By create_Item_Button = By.xpath("//button[text()='Create Item']");
	public final static By place_With_Cursor_Button = By.xpath("//button[text()='Place with cursor']");
	public static final By create_Image_frame_Icon = By.xpath("//a[@title='Add Image']//img");
	public static final By image_Selection_Dropdown = By.id("imageFileNameElement");
	public static final By document_Links_Images = By.xpath("//div[@class='doc-list-items']//span");
	public static final String image_Source_Type = "//a[text()='SOURCE_TYPE']";
	public static final String image_document_links = "//a[@title='image']";
	public static final String image_Shared_Asset_links = "//div[@title='image']";
	public static final By select_Button = By.xpath("(//button[text()='Select'])[1]");
	public static final By cancel_Button = By.xpath("(//button[text()='Cancel'])[1]");

	// ------------------- Central Panel Locators ----------------------//
	public static final By pop_Close_Button = By.xpath("//button[@class='close']//span");
	public static final By pop_Up = By.xpath("//h5[text()='Logs']");
	public static final By loading_Locator = By.xpath("//div[@ng-show='isWaitingForResponse']");
	public final static By page_Item = By.xpath("//div[@class='page_item']");

	// ------------------- Right Panel Locators ----------------------//
	public static final By edit_Button = By.xpath("//img[contains(@src , 'edit_icon.png')]");
	public static final By content_Editor_Save_Button = By.id("ceditorSaveElement");
	public static final By content_Editor_Cancel_Button = By.id("ceditorCancelElement");
	public static final By associate_Content_Source_Button = By
			.xpath("//div[@aria-label='Associate Content Source']//button");
	public static final By associate_Content_Source_Dialog = By.id("contentSource");
	public static final By associate_Content_Source_Type_Dropdown = By.id("contentSource_type");
	public static final By associate_Content_Source_Type_Cancel_Button = By.xpath("//span[text()='Cancel']");
	public static final By pick_Button = By.id("pickElement");
	public final static By x_Position_Frame = By.id("xPosElement");
	public final static By y_Position_Frame = By.id("yPosElement");
	public final static By apply_Button = By.id("applyChangesElement");
	public static final By associate_Image_Content_Source_Type_Dropdown = By.id("vLinkElement");
	
	// ------------------- TopPanel Locators ----------------------//
	public static final By save_Button = By.xpath("//a[contains(@uib-tooltip , 'Save')]");
	public static final By Close_Button = By.xpath("//a[contains(@uib-tooltip , 'Close')]");
	public static final String pop_up_choice_Button = "//div[contains(@class,'modal-body')]//button[text()='Choice']";

	// ------------------- proof Viewer Locators ----------------------//
	public static final By preview_Button = By.xpath("//a[contains(@uib-tooltip , 'Preview')]");
	public static final String random_String = "//div[contains(text() , 'string')]";

	// ------------------- Content Editor Locators ---------------------//
	public static final String TinyMCE_Button = "//div[@aria-label='BUTTON_NAME']//button";
	public static final By TinyMCE_Apply_Button = By.xpath("//span[text()='Apply']/parent::button");
	public static final By TinyMCE_Cancel_Button = By.xpath("//span[text()='Cancel']/parent::button");
	public static final String P_C_Style_Property_Option = "//label[contains(text(),'$property_Name')]/parent::div/$element_Type";
	public static final String Style_Operation_Radio_Input_Button = "//label[text()='$operation']/preceding-sibling::input";
	public static final By Create_Style_Name_Input_Box= By.xpath("//input[@id='createStyleNameInput']");
	public static final By Operation_Ok_Button = By.xpath("//button[@id='okButton']");
	public static final By Operation_Cancel_Button = By.xpath("//button[text()='Cancel']/parent::div[@class='buttons']");
	public static final By Style_Dialog_Loading_Locator=By.xpath("//div[@id='loaderDiv']");
	// ------------------- Content Editor C Style Locators ---------------------//			
	public static final By C_Style_Dialog = By.id("cStyleListBox-head");
	public static final By C_Style_Options_Dialog = By.id("cStyleElement-title");
	public static final String Style_List_Edit_Button = "//span[@title='$style_Name']/parent::li//i[@title='Edit']";
	public static final String Style_List_Apply_Button= "//span[@title='$style_Name']/parent::li//i[@title='Apply']";
	
	// ------------------- Content Editor P Style Locators ---------------------//
	public static final By P_Style_Dialog = By.id("pStyleListBox-head");
}
