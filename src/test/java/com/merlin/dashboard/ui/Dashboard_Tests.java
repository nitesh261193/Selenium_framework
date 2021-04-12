package com.merlin.dashboard.ui;

import static com.melin.apps.locators.Content_Editor_Locators.preview_Data_Close_Button;
import static com.melin.apps.locators.Content_Editor_Locators.preview_Data_Xpath;
import static com.merlin.common.Dashboard_Constants.proof_Name;
import static com.merlin.common.Dashboard_Constants.url;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Dashboard_Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.merlin.dashboard.ui.inddeditor.Indd_Util;

/**
 * @author Pooja Chopra
 *
 */
public class Dashboard_Tests extends Annotation implements Init {

	static Properties prop = null;

	@BeforeTest(alwaysRun = true)
	public void Before_Test_Class(ITestContext testContext) throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard.properties"));

	}

	@Test(groups = { "Login", "Open_Dashboard" })
	public static void Open_Dashboard() {
		navigate.Webdriver_Get(Constants.dashboard_Login_Url);
		Util.Report_Log(Status.INFO, "Dashboard Url : " + navigate.Webdriver_Get_Current_Url());
	}

	@Test(groups = { "Get_Remote_Machine_Address"})
	public static void Get_Remote_Machine_Address() {
		String machine_Info=text.Webdriver_Get_Text(By.xpath(prop.getProperty("Page_Footer_Machine_Info")));
		machine_Info=machine_Info.substring(machine_Info.lastIndexOf(" ")+1);
		Dashboard_Constants.remote_Machine_Host=machine_Info.trim();
		Util.Report_Log(Status.INFO, "Remote machine address fetched is: " + Dashboard_Constants.remote_Machine_Host);
	}



	@Test(dependsOnMethods = { "Open_Dashboard" }, groups = { "Login" })
	public static void Login() throws Exception {
		sendkeys.Webdriver_Sendkeys(By.id(prop.getProperty("Login_Username_Id")), Constants.login_Username);
		sendkeys.Webdriver_Sendkeys(By.id(prop.getProperty("Login_Password_Id")), Constants.login_Password);
		click.Webdriver_Click(By.xpath(prop.getProperty("Login_Button_Xpath")), true);

		Util.Report_Log(Status.PASS, "Login Successfully with Username : " + Constants.login_Username + " & Password : "
				+ Constants.login_Password);

	}


	public static String build_Tag;

	@Test(groups = { "Get_Build_Info" })
	public static void Get_Build_Info() {
		StringBuilder string_Builder = new StringBuilder();
		String current_Url = navigate.Webdriver_Get_Current_Url();
		navigate.Webdriver_Get(Constants.dashboard_About);
		build_Tag = text.Webdriver_Get_Text(By.xpath(prop.getProperty("Build_Info_Xpath"))).split("Tag: ")[1];
		String[] tag = build_Tag.split("\\.");
		string_Builder.append(tag[0] + tag[1] + "_S" + tag[2] + "_B" + tag[3] + "_");
		for (String word : build_Tag.split((build_Tag.contains(".")) ? "\\." : "-")) {

			string_Builder.append(word.charAt(0) + word.replaceAll("\\D+", "")
			+ ((word.split("\\d").length > 1) ? word.split("\\d")[1].trim() : "").toUpperCase().trim() + "_");
		}
		Constants.build_Revision = text.Webdriver_Get_Text(By.xpath("//p[3]")).split(":")[1].trim();
		Constants.build_Tag = build_Tag;
		Constants.release_Number = tag[0] + "." + tag[1];
		Constants.random_Number = Util.Get_Random_String(5);
		Constants.label = string_Builder.toString() + Constants.random_Number;
		Util.Report_Log(Status.INFO, "Release Name       : " + Constants.release_Name);
		Util.Report_Log(Status.INFO, "Build Tag          : " + Constants.build_Tag);
		Util.Report_Log(Status.INFO, "Label              : " + Constants.label);

		navigate.Webdriver_Get(current_Url);
	}



	@Test(groups = { "Open_Project_Page_By_Id" })
	@Parameters({ "project_Id" })
	public static void Open_Project_Page_By_Id(String project_Id) {
		Util.Report_Log(Status.INFO, "Navigating to Project Home Page of Project Id : " + project_Id);
		navigate.Webdriver_Get(Constants.dashboard_Project_Page + project_Id);
		Util.Report_Log(Status.PASS,
				"Navigated to Project Home Page by using Project Id. URL : " + navigate.Webdriver_Get_Current_Url());
		Constants.project_Id = navigate.Webdriver_Get_Current_Url().split("lpId=")[1].trim();
	}

	@Test(groups = { "Open_Project_Template_Page_By_Id" })
	@Parameters({ "project_Id" })
	public static void Open_Project_Template_Page_By_Id(String project_Id) {
		Util.Report_Log(Status.INFO, "Navigating to Project Template Page of Project Id : " + project_Id);
		navigate.Webdriver_Get(Constants.dashboard_Project_Template_Page + project_Id);
		Util.Report_Log(Status.PASS,
				"Navigated to Project Home Page by using Project Id. URL : " + navigate.Webdriver_Get_Current_Url());
		Dashboard_Constants.url=text.Webdriver_getText_ByAttribute(By.xpath("//div[contains(text(),'Content Directory')]//a"), "href");
		Util.Report_Log(Status.INFO, "Content Directory "+url+" is fetched ");


	}

	@Test(groups = { "Open_Project_Publish_Page_By_Id" })
	@Parameters({ "project_Id" })
	public static void Open_Project_Publish_Page_By_Id(String project_Id) {
		Util.Report_Log(Status.INFO, "Navigating to Project Publish Page of Project Id : " + project_Id);
		navigate.Webdriver_Get(Constants.dashboard_Project_Publish_Page + project_Id);
		Util.Report_Log(Status.PASS,
				"Navigated to Project Publish Page by using Project Id. URL : " + navigate.Webdriver_Get_Current_Url());
	}

	@Test(groups = { "Open_Project_Template_Indd_Editor" })
	@Parameters({ "template_Name" })
	public static void Open_Project_Template_Indd_Editor(@Optional String template_Name) {
		template_Name = (template_Name == null) ? Dashboard_Constants.template_Name : template_Name;
		sendkeys.Webdriver_Sendkeys_Enter(By.xpath("//input[@aria-label='search terms']"), template_Name);
		Util.Report_Log(Status.INFO, "Opening template in INDD Editor : " + template_Name);
		String edit_Indd_Col = Util.Get_Col_Index(By.xpath("//table[@id='all-settings']/thead/tr/th"),
				"Edit INDD Template");
		click.Webdriver_Click(By.xpath(
				"//table[@id='all-settings']//tbody//tr[td[1][a='" + template_Name + "']]//td[" + edit_Indd_Col + "]"),
				true);
		navigate.Switch_To_Tab(1, "true", "true");
		//		Util.Wait_Until_Requests_Complete();
		visible.Wait_Until_Frame_Is_Visible(15, By.id("editorViewerFrame"));
		//		navigate.Switch_To_Frame("editorViewerFrame");
		Indd_Util.Wait_For_Loader_To_Disappear();
		visible.Pause(1);
		Indd_Util.Wait_For_Loader_To_Disappear();
		// issue with indd latest build loader getting dissappear in between temp wait
		// added for v138
		visible.Pause(1);
		Indd_Util.Wait_For_Loader_To_Disappear();
		Indd_Util.Warning_Pop_Up_Handle();
		Dashboard_Constants.template_Version = 0;
		Util.Report_Log(Status.PASS, "INDD editor opened successfully");
	}

	@Test(groups = { "Open_Project_Template_Html_Editor" })
	@Parameters({ "template_Name" })
	public static void Open_Project_Template_Html_Editor(String template_Name) {
		Util.Report_Log(Status.INFO, "Opening template in INDD Editor : " + template_Name);
		String edit_Indd_Col = Util.Get_Col_Index(By.xpath("//table[@id='all-settings']/thead/tr/th"),
				"Edit HTML Template");
		click.Webdriver_Click(By.xpath(
				"//table[@id='all-settings']//tbody//tr[td[1][a='" + template_Name + "']]//td[" + edit_Indd_Col + "]"),
				true);
		navigate.Switch_To_Tab(1, "true", "true");
		navigate.Switch_To_Frame("proofViewerFrame");
		Util.Report_Log(Status.PASS, "HTML editor opened successfully");
	}

	@Test(groups = { "Open_Project_Template_Content_Page" })
	@Parameters({ "project_Id", "template_Id" })
	public static void Open_Project_Template_Content_Page(String project_Id, @Optional String template_Id) {
		template_Id = (template_Id == null) ? Dashboard_Constants.template_Id : template_Id;
		Util.Report_Log(Status.INFO, "Navigating to Project Template Content Page of Project Id : " + project_Id);
		navigate.Webdriver_Get(Constants.dashboard_Project_Template_Content_Page.replaceAll("TEMPID", template_Id)
				.replaceAll("PROJECTID", project_Id));
		Util.Report_Log(Status.PASS, "Navigated to Project Template Page by using Project Id. URL : "
				+ navigate.Webdriver_Get_Current_Url());
	}

	@Test(groups = { "Validate_Template_Content_Preview_Dialog" })
	@Parameters({ "content_Name", "content_Data" })
	public static void Validate_Template_Content_Preview_Dialog(String content_Name, String content_Data) {
		content_Data = content_Data.replace("random", Constants.random_Number);
		Util.Report_Log(Status.INFO, "Clicking on preview button for content - " + content_Name);
		click.Webdriver_Click(By
				.xpath(prop.getProperty("Project_Template_Content_Table_Row_Xpath").replace("CONTENTNAME", content_Name)
						+ "//td[contains(@class,'column_preview')]//a"),
				false);
		String preview_Data = text.Webdriver_Get_Text(preview_Data_Xpath);
		assertEquals(preview_Data, content_Data, "Data in preview content not found as expected");
		Util.Report_Log(Status.PASS, "Data in content data found as expected - " + content_Data);
		click.Webdriver_Click(preview_Data_Close_Button, true);
	}

	@Test(groups = { "Click_Template_Content_Edit_Content_Button" })
	@Parameters({ "content_Name" })
	public static void Click_Template_Content_Edit_Content_Button(String content_Name) {
		Util.Report_Log(Status.INFO, "Clicking on edit content button for content - " + content_Name);
		click.Webdriver_Click(By
				.xpath(prop.getProperty("Project_Template_Content_Table_Row_Xpath").replace("CONTENTNAME", content_Name)
						+ "//td[contains(@class,'column_edit')]//a"),
				true);
		Util.Report_Log(Status.PASS, "Edit content button clicked.");
	}

	// added by Krishna Saini

	@Test(groups = { "Open_Asset_Management" })
	public static void Open_Asset_Management() {
		boolean flag = false;

		Util.Report_Log(Status.INFO, "Opening Asset Management");
		click.Webdriver_Click(By.xpath(prop.getProperty("Menu_Icon")), true);
		Util.Report_Log(Status.PASS, "Menu icon clicked.");
		click.Webdriver_Click(By.xpath(prop.getProperty("Asset_Menu_Option")), true);
		Util.Report_Log(Status.PASS, "Clicked on Assets Management option from menu item");
		click.Webdriver_Click(By.xpath(prop.getProperty("Shared_Files_Link")), true);
		Util.Report_Log(Status.PASS, "Clicked on DAM link.");
		navigate.Switch_To_Tab(1, "true", "true");
		String headerTile = text.Webdriver_Get_Text(By.xpath(prop.getProperty("Header_Title")));
		if (headerTile.contains("Asset"))
			flag = true;
		assertTrue(flag, "Verified that Asset Management Shared Files tab is opened.");
		Util.Report_Log(Status.PASS, "Verified that Asset tab is opened.");
	}

	@Test(groups = { "Delete_Template_From_Content_Directory" })
	@Parameters({ "content_Directory_Name", "delete_Template_Name" })
	public static void Delete_Template_From_Content_Directory(String content_Directory_Name,
			@Optional String delete_Template_Name) {
		delete_Template_Name = (delete_Template_Name == null) ? Dashboard_Constants.template_Name
				: delete_Template_Name;
		Util.Report_Log(Status.INFO, "Opening content directory from template page");
		click.Webdriver_Click(By.xpath(prop.getProperty("Project_Template_Content_Directory_Xpath")
				.replace("CONTENT_DIRECTORY", content_Directory_Name)), true);
		Util.Report_Log(Status.INFO, "Clicking on delete icon for template " + delete_Template_Name);
		click.Click_And_Accept_Swirl_Popup(By.xpath(prop.getProperty("Template_Content_Delete_Template_Xpath")
				.replace("TEMPLATENAME", Dashboard_Constants.template_Name)));
		Assert.assertTrue(!visible.Is_Displayed(By.linkText(delete_Template_Name)), "Template not deleted");
		Util.Report_Log(Status.PASS, "Template deleted");
	}

	// added by Krishna Saini
	@Test(groups = { "Search_For_Proof_And_Select" })
	@Parameters({ "proof_Name" })
	public static void Search_For_Proof_And_Select(String proof_Name) {
		visible.Wait_For_Page_To_Load();
		Util.Report_Log(Status.INFO, "Opening proof filter to search proof : " + proof_Name);
		click.Webdriver_Click_By_Action(By.xpath(prop.getProperty("proof_Name_Open_Filter_Button")));
		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("proof_Name_Filter_Input_Box")), proof_Name);
		Util.Report_Log(Status.INFO, "Sucessfully enter given proof name : " + proof_Name + " in filter input box.");
		click.Webdriver_Click_By_Action(By.xpath(prop.getProperty("proof_Name_Filter_Apply_Button")));
		Util.Report_Log(Status.INFO, "Sucessfully clicked apply button.");
		visible.Wait_For_Page_To_Load();
		visible.Wait_Until_Visible(3, By.xpath(prop.getProperty("proof_To_Select").replace("$name", proof_Name)));
		click.Webdriver_Click_By_Action(By.xpath(prop.getProperty("proof_To_Select").replace("$name", proof_Name)));
		Util.Report_Log(Status.INFO, "Successfully select row for given proof : " + proof_Name);
	}

	// added by Krishna Saini
	@Test(groups = { "Open_Selected_Proof" })
	@Parameters({ "proof_Name" })
	public static void Open_Selected_Proof(String proof_Name) {
		Util.Report_Log(Status.INFO, "Opening given proof : " + proof_Name);
		click.Webdriver_Javascipt_Click(By.xpath(prop.getProperty("tab_Select_Link").replace("$tabName", "Details")));
		String slected_Proof_Name = text
				.Javascript_Get_Text(By.xpath(prop.getProperty("selected_Proof_Name_In_Details_Tab")));
		Assert.assertEquals(slected_Proof_Name.trim().toLowerCase(), proof_Name.trim().toLowerCase(),
				"Proof you want to open is not selected.Expected proof name : " + proof_Name + " and found proof : "
						+ slected_Proof_Name + "as selected");
		Util.Report_Log(Status.INFO, "Proof : " + proof_Name + " is selected");
		click.Webdriver_Click(By.xpath(prop.getProperty("proof_Review_Button")), true);
		Util.Report_Log(Status.PASS, "Successfully clicked on proof review button");
		visible.Wait_For_Page_To_Load();

	}

	@Test(groups = { "Open_Tasks_List_Page" })
	public static void Open_Tasks_List_Page() {
		boolean flag = false;

		Util.Report_Log(Status.INFO, "Opening Tasks List Page");
		click.Webdriver_Click(By.xpath(prop.getProperty("Menu_Icon")), true);
		Util.Report_Log(Status.PASS, "Menu icon clicked.");
		click.Webdriver_Click(By.xpath(prop.getProperty("tasks_Link")), true);
		Util.Report_Log(Status.PASS, "Clicked on Tasks Link");
		String headerTile = text.Webdriver_Get_Text(By.xpath(prop.getProperty("Header_Title")));
		if (headerTile.contains("Tasks"))
			flag = true;
		Assert.assertTrue(flag, "Verified that Tasks List Page is opened.");
		Util.Report_Log(Status.PASS, "Verified that Tasks List Page is opened.");
	}

	@Test(groups = { "Search_Task_And_Open" })
	@Parameters({ "task_Name" })
	public static void Search_Task_And_Open(String task_Name) {
		visible.Wait_For_Page_To_Load();
		Util.Report_Log(Status.INFO, "Opening task filter to search task : " + task_Name);
		click.Webdriver_Click_By_Action(By.xpath(prop.getProperty("task_Filter_Button")));
		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("task_Filter_Input_Box")), task_Name);
		Util.Report_Log(Status.INFO, "Sucessfully enter given task name : " + task_Name + " in filter input box.");
		click.Webdriver_Click_By_Action(By.xpath(prop.getProperty("task_Filter_Apply_Button")));
		Util.Report_Log(Status.INFO, "Sucessfully clicked apply button.");
		visible.Wait_For_Page_To_Load();
		visible.Wait_Until_Visible(3, By.xpath(prop.getProperty("task_To_Open").replace("$name", task_Name)));
		click.Webdriver_Click_By_Action(By.xpath(prop.getProperty("task_To_Open").replace("$name", task_Name)));
		visible.Wait_For_Page_To_Load();
		Util.Report_Log(Status.INFO, "Successfully open given task : " + task_Name);
		Dashboard_Constants.task_Id = navigate.Webdriver_Get_Current_Url().split("smartTaskId=")[1].trim();

	}

	@Test(groups = { "Copy_Task_And_Save_Id" })
	@Parameters({ "task_Name" })
	public static void Copy_Task_And_Save_Id(String task_Name) {
		Util.Report_Log(Status.INFO, "Copying given task : " + task_Name);
		visible.Wait_Until_Visible(10, By.xpath(prop.getProperty("copy_Task_Link")));
		click.Webdriver_Click_By_Action(By.xpath(prop.getProperty("copy_Task_Link")));
		visible.Wait_For_Page_To_Load();
		Util.Report_Log(Status.INFO, "Successfully copied given task : " + task_Name);
		Dashboard_Constants.task_Id = navigate.Webdriver_Get_Current_Url().split("smartTaskId=")[1].trim();
	}

	@Test(groups = { "Open_Task_Page_By_Id" })
	@Parameters({ "task_Id" })
	public static void Open_Task_Page_By_Id(String task_Id) {
		if (task_Id.isEmpty())
			task_Id = Dashboard_Constants.task_Id;
		Util.Report_Log(Status.INFO, "Navigating to Task using Id : " + task_Id);
		navigate.Webdriver_Get(Constants.dashboard_Task_Page + task_Id);
		Util.Report_Log(Status.PASS,
				"Navigated to Task by using task Id. URL : " + navigate.Webdriver_Get_Current_Url());
		Constants.task_Id = navigate.Webdriver_Get_Current_Url().split("smartTaskId=")[1].trim();
	}

	@Test(groups = { "Open_Proof_From_Task" })
	@Parameters({ "proof_Name" })
	public static void Open_Proof_From_Task(String proof_Name) {
		navigate.Webdriver_Refresh();
		visible.Wait_For_Page_To_Load();
		Util.Report_Log(Status.INFO, "Opening given proof : " + proof_Name);
		click.Webdriver_Javascipt_Click(
				By.xpath(prop.getProperty("task_Proof_Name_Link").replace("$proofName", proof_Name)));
		Util.Report_Log(Status.PASS, "Successfully clicked on proof name link to  preview it");
		visible.Wait_For_Page_To_Load();

	}

	@Test(groups = { "Generate_proof" })
	@Parameters({ "proof_Type", "segment" })
	public void Generate_proof(String proof_Type, String segment) {
		visible.Scroll_To_Element(By.xpath(prop.getProperty("Generate_tab")));
		click.Webdriver_Click(By.xpath(prop.getProperty("Generate_tab")), true);
		Util.Report_Log(Status.INFO, "Click on Generate Tab");
		assertEquals(webelement.Get_Webelement(By.xpath(prop.getProperty("Proof_Type_text"))).isDisplayed(), true,
				"Generate Tab is not clicked ");
		click.Webdriver_Select_Checkbox(
				By.xpath(prop.getProperty("Segments_Check").replace("Segments", "All Segments")), true);
		click.Webdriver_Select_Checkbox(
				By.xpath(prop.getProperty("Segments_Check").replace("Segments", "All Segments")), false);
		click.Webdriver_Select_Checkbox(By.xpath(prop.getProperty("Segments_Check").replace("SEGMENT", segment)), true);
		Util.Report_Log(Status.INFO, "Checked segment box " + segment);
		click.Webdriver_Click(By.xpath(prop.getProperty("Dropdown_Box")), true);
		click.Webdriver_Click_By_Action(
				By.xpath(prop.getProperty("Dropdown_Value_Select").replace("dropdown_value", proof_Type)));
		Util.Report_Log(Status.INFO, proof_Type + " - Proof type is selected");
		assertEquals(webelement
				.Get_Webelement(By
						.xpath(prop.getProperty("Dropdown_Box_After_Selection").replace("dropdown_value", proof_Type)))
				.isDisplayed(), true, "Proof type dropdown is not selected");
		click.Webdriver_Click(By.xpath(prop.getProperty("Generate_Proof_Button")), true);
		if (visible.Is_Displayed(By.xpath(prop.getProperty("Asset_Text")))) {
			click.Webdriver_Click(By.xpath(prop.getProperty("Continue_Button")), true);
		}
	}

	@Test(groups = { "Validate_Proof_Present_On_Home_Page_With_Job_Id" })
	public void Validate_Proof_Present_On_Home_Page_With_Job_Id() {
		Util.Report_Log(Status.INFO,
				"Verifying that proof is present in dashbaord table for job_Id " + Dashboard_Constants.Job_Id);
		String proof_Table_Td_Xpath = prop.getProperty("Verify_Job_Id").replace("Job_Id", Dashboard_Constants.Job_Id);
		visible.Wait_Until_Visible(60, By.xpath(proof_Table_Td_Xpath));
		Boolean flag = visible.Is_Displayed(By.xpath(proof_Table_Td_Xpath));
		assertTrue(flag, "proof is not present in dashboard table");
		Dashboard_Constants.proof_Id = webelement
				.Get_Webelement(By.xpath(
						proof_Table_Td_Xpath + "/preceding-sibling::td[contains(@class,'column_proofname row_')]"))
				.getAttribute("innerHTML");
		Util.Report_Log(Status.INFO, "Proof Id for the created proof is - " + Dashboard_Constants.proof_Id);
		proof_Name = text.Webdriver_Get_Text(
				By.xpath(proof_Table_Td_Xpath + "/preceding-sibling::td[contains(@class,'proofName')]"));
		Util.Report_Log(Status.INFO, "Proof Name for the created proof is - " + proof_Name);
	}

	@Test(groups = { "Compare_PDF" })
	@Parameters({ "candidate_Proof", "reference_Proof" })
	public void Compare_PDF(String candidate_Proof, String reference_Proof) {
		candidate_Proof = (candidate_Proof.equals("created")) ? proof_Name : candidate_Proof;
		Dashboard_Tests.select_Proof_In_Proof_Table(candidate_Proof);
		click.Webdriver_Click(By.xpath(prop.getProperty("Project_Page_Click_Proof_From_ProofTable_Xpath")
				.replace("PROOF_NAME", candidate_Proof)), true);
		Util.Report_Log(Status.INFO, "Candidate Proof is selected - " + candidate_Proof);
		visible.Scroll_To_Element(By.xpath(prop.getProperty("Compare_Tab")));
		click.Webdriver_Click(By.xpath(prop.getProperty("Compare_Tab")), true);
		Util.Report_Log(Status.INFO, "Compare tab is opened");
		click.Webdriver_Click(By.xpath(prop.getProperty("Swap_Proof_Button")), true);
		visible.Scroll_To_Element(By.xpath(prop.getProperty("Project_Page_Click_Filter_button_Xpath")));
		Dashboard_Tests.select_Proof_In_Proof_Table(reference_Proof);
		click.Webdriver_Click(By.xpath(prop.getProperty("Project_Page_Click_Proof_From_ProofTable_Xpath")
				.replace("PROOF_NAME", reference_Proof)), true);
		Util.Report_Log(Status.INFO, "Reference Proof is selected - " + reference_Proof);
		visible.Scroll_To_Element(By.xpath(prop.getProperty("Pdf_Compare_Button")));
		click.Webdriver_Click(By.xpath(prop.getProperty("Pdf_Compare_Button")), true);
		Util.Report_Log(Status.INFO, "Compare Button is clicked");
	}


	@Test(groups = {"Validate_Compare_Pdf_Job_In_Dashboard"})
	@Parameters({"compare_For"})
	public void Validate_Compare_Pdf_Job_In_Dashboard(String compare_For) {

		boolean flag  = false;
		String proof_Table_Td_Xpath = prop.getProperty("Verify_Job_Id").replace("Job_Id", Dashboard_Constants.proof_Id);
		proof_Name = text.Webdriver_Get_Text(
				By.xpath(proof_Table_Td_Xpath + "/following-sibling::td[contains(@class,'proofName')]"));
		Util.Report_Log(Status.INFO, "Proof Name for the created proof is - " + proof_Name);
		Dashboard_Tests.select_Proof_In_Proof_Table(proof_Name);
		By compare_Icon_Xpath = By.xpath(proof_Table_Td_Xpath+prop.getProperty("Proof_Comparision_Icon"));
		visible.Wait_Until_Visible(120, compare_Icon_Xpath);
		String title = text.Webdriver_Get_Text_Of_Attribute(compare_Icon_Xpath, "title");

		if(title.equalsIgnoreCase("COMPLETE") || title.equalsIgnoreCase(compare_For))
			flag = true;

		Assert.assertTrue(flag, "Compare PDF Job isn't completed yet");
		Util.Report_Log(Status.PASS,"On hovering Compare PDF icon, Complete label is displayed");

	}

	public static void select_Proof_In_Proof_Table(String proof_Name) {
		click.Webdriver_Click(By.xpath(prop.getProperty("Project_Page_Click_Filter_button_Xpath")), true);
		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("Project_Page_Filter_Input_Field_Xpath")), proof_Name);
		click.Webdriver_Click(By.xpath(prop.getProperty("Project_Page_Filter_Apply_Button_Xpath")), true);
		Util.Report_Log(Status.INFO, "Filter is applied in proof Name col of proof table ");
	}

	@Test(groups = { "Publish_Email_Through_Ui" })
	@Parameters({ "publish_Type", "publish_Option", "email_Address" })
	public void Publish_Email_Through_Ui(String publish_Type, String publish_Option, @Optional String email_Address) {
		select.Webdriver_Select_DropdownList_By_Visible_Text_IgnoreCase(
				By.xpath(prop.getProperty("publish_Type_Select_Box")), publish_Type);
		Util.Report_Log(Status.INFO, "Selected Publish Type-" + publish_Type);
		Select_Email_Publish_Options(publish_Option, email_Address);
		Util.Report_Log(Status.INFO, "Publish Option selected - " + publish_Option);
		click.Webdriver_Click(By.xpath(prop.getProperty("publish_Page_Publish_Button")), true);
		Util.Report_Log(Status.PASS, " Clicked on Publish Page Publish Button");
	}

	public static void Select_Email_Publish_Options(String Publish_Options, String email_Address) {

		click.Webdriver_Click(By.xpath(prop.getProperty("publish_Page_Publish_Option_RadioButton")
				.replace("$Option", "Test Send:").replace("$Set", "false")), true);

		switch (Publish_Options) {
		case "Throttle Send":
			click.Webdriver_Click(By.xpath(prop.getProperty("publish_Page_Publish_Option_RadioButton")
					.replace("$Option", Publish_Options + ":").replace("$Set", "true")), true);
			break;
		case "Deduplicate":
			click.Webdriver_Click(By.xpath(prop.getProperty("publish_Page_Publish_Option_RadioButton")
					.replace("$Option", Publish_Options + ":").replace("$Set", "true")), true);
			break;
		case "Send Only Text":
			click.Webdriver_Click(By.xpath(prop.getProperty("publish_Page_Publish_Option_RadioButton")
					.replace("$Option", Publish_Options + ":").replace("$Set", "YES")), true);
			break;

		case "Test Send":
			click.Webdriver_Click(By.xpath(prop.getProperty("publish_Page_Publish_Option_RadioButton")
					.replace("$Option", Publish_Options + ":").replace("$Set", "true")), true);
			sendkeys.Webdriver_Sendkeys(
					By.xpath(prop.getProperty("publish_Page_Publish_Option_Test_Send_Target_Address_Input")),
					email_Address);

		case "Scheduled Send:":
			click.Webdriver_Click(By.xpath(prop.getProperty("publish_Page_Publish_Option_RadioButton")
					.replace("$Option", Publish_Options + ":").replace("$Set", "true")), true);
			Util.Report_Log(Status.INFO, "Scheduling mail to be send at 3 min from now");
			String time = null;
			time = Util.Get_Time_In_CST("MM/dd/yyyy hh:mm a", 3);
			Constants.current_Date_In_CST = time;
			Util.Report_Log(Status.INFO, "Mail will be sent at - " + time);
			sendkeys.Webdriver_Sendkeys(
					By.xpath(prop.getProperty("publish_page_Publish_Option_ScheduleTime_Input_Xpath")), time);
			break;

		default:
			Util.Report_Log(Status.INFO, "Default Values Selected");
		}

		visible.Wait_For_Page_To_Load();
	}

}
