package com.merlin.apps.locators;

import org.openqa.selenium.By;

/**
 * @author Krishna saini
 *
 */

public class Proof_Collaboration_Locators {
	
	
	//-------------------- Header Panel Locators -----------------------//

	public static final By proof_Download_Button = By.xpath("//div[@class='rightIconsPanel']/a[@data-original-title='Download Annotated PDF']");
	public static final By proof_Collaboration_Exit_Button = By.xpath("//a[contains(@data-original-title,'Exit Proof')]");
	public static final String proof_Collaboration_Document_Title ="//div[@class='title textEllipsis']//li[contains(text(),'$title')]";
	

	
	//-------------------- Left Panel Locators -----------------------//
	public static final By compare_Switch_Button = By.xpath("//div[@class='header-ico']/a[@class='btnIcon copy1 compare_qc']");
	public static final By run_New_Compare_Button = By.xpath("//div[@class='sideBarContent h-100 ng-scope']//a[text()=' Run New Compare']");
	public static final By proof_Comparison_Dialog_Header= By.xpath("//div[@class='modal-header']/h5[text()='Proof Comparison']");
	public static final By select_Template_Dropdown = By.xpath("//div[@id='requiredFieldError']/ancestor::div[@class='modal-content']//select[@id='templateId']");
	public static final By select_Proof_Type_Dropdown = By.xpath("//div[@id='requiredFieldError']/ancestor::div[@class='modal-content']//select[@id='projectProofTypeId']");
	public static final By compare_Candidate_File_Input = By.xpath("//div[@id='requiredFieldError']/ancestor::div[@class='modal-content']//input[@id='candidateFileField']");
	public static final By compare_Submit_Button = By.xpath("//div[@id='requiredFieldError']/ancestor::div[@class='modal-content']//button[@type='submit']");
	public static final By compare_Download_Popover_Button = By.xpath("//a[@id='popoverTrigger_0']/i[@class='fa fa-download']");
	public static final String compare_Download_With_Option_Button= ("//div[@class='popover-content']//li/i[@title='$download_Option']");
	public static final By Compare_Download_Button_Spinner = By.xpath("//i[@class='fa-spin fa fa-refresh']");
	
	//-------------------  Center Panel Locators ----------------------//
	public static final By page_Text_Div= By.xpath("//div[@id= 'text-layer']/div");
	public static final By loader_Message = By.xpath("//span[@ng-bind='loadingMessage']/ancestor::div");
    // ---------------- Right Panel Locators ----------------//
	public static final String markup_Tool_Panel_Header =("//div[@id='markupTools']/h2[contains(@expanded,'$expanded')]");
	public static final By markup_Tool_Right_Toggle = By.xpath("//div[@id='markupTools']/h2[contains(@class,'fa fa-caret-right')]");
	public static final String markup_Tool_To_Select= "//a[@class='right-btnIcon' and @data-original-title='$Type']";
	public static final By active_Comment_Box = By.xpath("//div[@class='notesTextarea isActive']/div[@id='commentTextBox']");
	public static final By annotation_List_Panel_Header = By.xpath("//div[@class='currentPageAnnotation' and contains(text(),'Page')]");
	public static final By available_Comment_Elements = By.xpath("//div[@annotation-id and @source=\"'comments'\"]");
	public static final String saved_Comment = "//div[@annotation-id and text()='$text']";
	public static final By comment_Delete_Button = By.xpath("//i[@class='fa fa-trash-o delIcon']");
	// ---------------- Popup Locators ----------------//
	
	public static final By popup_Message = By.xpath("//div[@class='modal-body']/p");
	public static final By popup_Cancel_Button = By.xpath("//button[text()='Cancel']");
	public static final By popup_No_Button = By.xpath("//button[text()='No']");
	public static final By popup_Yes_Button = By.xpath("//button[text()='Yes']");
	public static final By popup_Ok_Button  = By.xpath("//button[@class='btn btn-sm btn-primary-naehas ng-binding' and text()='OK']"); 
	//------------------Proofs Pages Page Locator---//

}


