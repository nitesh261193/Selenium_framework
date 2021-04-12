package com.merlin.offer_management;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.merlin.webdriverutilities.Visible;
import com.merlin.webdriverutilities.Webelement;
/***
 * 
 * 
 * @author harshsharma
 *
 */

public class Offer_Types extends Annotation implements Init {

	static Properties prop = null;
	static String global_Group_Name = null;
	static String offer_Type = "";
	public static HashMap<String, ArrayList<String>> offers_Details = new HashMap<>();

	@BeforeTest(alwaysRun = true)
	public void before_test() throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Offer_Types.properties"));

	}

	@Test(groups = {"Open_Offer_Type_Page_And_Validate"})
	@Parameters({"is_Displayed"})
	public static void Open_Offer_Type_Page_And_Validate(boolean is_Displayed)
	{
		Util.Report_Log(Status.INFO,"Navigating to Job admin page to validate Offers Tab is visible"); 
		navigate.Webdriver_Navigate(Constants.dashboard_Home);
		click.Webdriver_Click(By.xpath(prop.getProperty("Gear_Icon_Xpath")), true);

		if(is_Displayed)
		{
			Util.Report_Log(Status.INFO,"Asserting the User has successfully landed on the Offer Types Pages");
			//SoftAssertion.Soft_Assert("Offer Tab visible", "displayed", By.id("Offer_Types_link"), null, "The Offers Tab on Job admin page is not visible.", null);
			click.Webdriver_Click(By.id("Offer_Types_link"), true);
			Assert.assertTrue(webelement.Is_Displayed(By.id("Offer_Types_link"), 4));
			Assert.assertTrue(webelement.Is_Displayed(By.id("createtype"), 4));
			Assert.assertTrue(webelement.Is_Displayed(By.id("offerTypeTable"), 4));


			//SoftAssertion.Soft_Assert("New Offer Button", "displayed", By.id("createtype"), null, "Create Offer Type Button is not visible", null);

			//SoftAssertion.Soft_Assert("Offer Table", "displayed", By.id("offerTypeTable"), null, "Offer Type Table is not visible", null);
		}
		else
		{
			Assert.assertTrue(!webelement.Is_Displayed(By.id("Offer_Types_link"), 4));
		}
	}

	@Test(groups = {"Create_Offer_Type"})
	@Parameters({"offer_Type_Name","campaign_Name","project_Name"})
	public static void Create_Offer_Type(String offer_Type_Name, @Optional String campaign_Name, @Optional String project_Name)
	{
		if (Constants.label!=null && offer_Type_Name.contains("label"))
			offer_Type=offer_Type_Name.replace("label", Constants.label);
		else
			offer_Type=offer_Type_Name.replace("label", "Master"+Util.Get_Random_String(5));

		Util.Report_Log(Status.INFO,"From Offer Types Page Clicking on the New Offer Type Button to create New Offer Type");
		click.Webdriver_Click(By.id("createtype"), false);
		Assert.assertTrue(webelement.Is_Element_Displayed(By.xpath("//*[@id='myModalLabel'][text()='New Offer Type ']"), 4), "Offer Type Create Modal is Not Present");

		Util.Report_Log(Status.INFO,"Selecting the Campaign and Project and Entering the New Offer Type Name");
		visible.Pause(2);

		if(campaign_Name!=null)
			select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[text()='Campaign Name ']//following-sibling::div//div[@id='campaignDropdown_chosen']"), By.xpath("//div[@id='campaignDropdown_chosen']//div[@class='chosen-search']//input"), campaign_Name);

		visible.Pause(2);

		if(project_Name !=null)
			select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[text()='Project Name ']//following-sibling::div//div[@id='projectDropdown_chosen']"), By.xpath("//div[@id='projectDropdown_chosen']//div[@class='chosen-search']//input"), project_Name);

		sendkeys.Webdriver_Sendkeys(By.xpath("//div[@id='newOfferTypeModal']//label[text()='Offer Type ']//following-sibling::div//input[@name='offerTypeName']"), offer_Type);

		Util.Report_Log(Status.INFO,"Saving the New Offer Type");
		click.Webdriver_Click(By.id("newOfferTypeSubmit"), true);

		if(campaign_Name!=null && project_Name !=null)
		{
			Util.Report_Log(Status.INFO,"Validating Offer Type is Saved and Offer Type Details Page is Loaded.");
			String offer_Name_Saved = webelement.Get_Webelement(By.id("offerTypeName")).getAttribute("value");
			Assert.assertEquals(offer_Name_Saved, offer_Type.trim());

			Assert.assertTrue(webelement.Is_Displayed(By.xpath("//h4[text()='Offer Type']"), 2), "Offer Type Header not visible");
			Assert.assertTrue(webelement.Is_Displayed(By.xpath("//h4[text()='Offer Type Groups']"), 2), "Offer Type Groups Header not visible");
			Assert.assertTrue(webelement.Is_Displayed(By.xpath("//h4[text()='Offer Type Statuses']"), 2), "Offer Type Statuses Header not visible");
		}
		else 
		{
			Util.Report_Log(Status.INFO,"Validating Offer Type is not Saved.");
			Assert.assertTrue(!webelement.Is_Displayed(By.xpath("//h4[text()='Offer Type']"), 2), "Offer Type Header not visible");
			click.Webdriver_Click(By.xpath("//button[text()='Cancel']"), false);
		}
	}

	@Test(groups= {"Open_Offer_Type_Details_Page"})
	@Parameters({"offer_Type_Name"})
	public static void Open_Offer_Type_Details_Page(@Optional String offer_Type_Name)
	{
		Util.Report_Log(Status.INFO,"Opening the Offer Type Group Details Page");
		if(offer_Type_Name!=null)
			offer_Type_Name= offer_Type_Name;
		else
			offer_Type_Name = offer_Type.trim();

		Util.Report_Log(Status.INFO,"Filtering the Offer Type from table and Opening it.");
		String col_Index = Util.Get_Col_Index(By.xpath("//table[@id='offerTypeTable']//th"), "Offer Type");

		click.Webdriver_Click(By.xpath("//table[@id='offerTypeTable']//th["+col_Index+"]//i"), false);
		sendkeys.Webdriver_Sendkeys(By.id("offerTypeTableOfferType"), offer_Type_Name);
		visible.Pause(3);
		click.Webdriver_Click(By.id("applyofferTypeTableOfferType1"), false);
		String filtered_Value= text.Webdriver_Get_Text(By.xpath("//table[@id='offerTypeTable']//tbody//tr//td["+col_Index+"]/a"));
		if(filtered_Value.equalsIgnoreCase(offer_Type_Name))
			click.Webdriver_Click(By.xpath("//table[@id='offerTypeTable']//tbody//tr//td["+col_Index+"]/a"), true);

		Util.Report_Log(Status.INFO,"Asserting user landed on the Offer type Details page");
		Assert.assertTrue(webelement.Is_Displayed(By.xpath("//h4[text()='Offer Type']"), 2), "Offer Type Header not visible");
		Assert.assertTrue(webelement.Is_Displayed(By.xpath("//h4[text()='Offer Type Groups']"), 2), "Offer Type Groups Header not visible");
		Assert.assertTrue(webelement.Is_Displayed(By.xpath("//h4[text()='Offer Type Statuses']"), 2), "Offer Type Statuses Header not visible");

	}

	@Test(groups = {"Enable_Disable_Offer_Types"})
	@Parameters({"enabled_Offer_Type"})
	public static void Enable_Disable_Offer_Types(boolean enabled_Offer_Type)
	{
		Util.Report_Log(Status.INFO,"Clicking on the Checkbox under Offer Types Modal to enable or disabled curretly the value is" + enabled_Offer_Type);
		click.Webdriver_Select_Checkbox(By.id("offerTypeEnabled"), enabled_Offer_Type);
		click.Webdriver_Click(By.id("saveButton"), false);
	}

	@Test(groups= {"Create_New_Status"})
	@Parameters({"status","security","usern_Name","enabled","editable","color","task_Name","job_Name"})
	public static void Create_New_Status(String status, @Optional String security, @Optional String usern_Name,  @Optional Boolean enabled, @Optional Boolean editable , String color, @Optional String task_Name, @Optional String job_Name)
	{
		Util.Report_Log(Status.INFO,"Clicking on the New Status button to create new status");
		click.Webdriver_Click(By.id("newStatusButton"), false);

		Util.Report_Log(Status.INFO,"Creating the Offer Type Status for the values" + status);
		Assert.assertTrue(webelement.Is_Displayed(By.xpath("//div[@id='newStatusModal']//div[@class='modal-content']"), 4));

		Util.Report_Log(Status.INFO,"Enterting the status values for creation");
		sendkeys.Webdriver_Sendkeys(By.xpath("//label[text()='Status Name ']"
				+ "//following-sibling::div//input[@name='newStatusNameInputBox']"), status);

		Util.Report_Log(Status.INFO,"Clicking on the radio button to select the visibility of the status");
		if(security!=null && security.equalsIgnoreCase("users")) {
			click.Webdriver_Click(By.id("newLimitedRadio"), false);
			sendkeys.Webdriver_Sendkeys_Enter(By.xpath("//div[@id='newSecurityDropdown_chosen']//li//input"), usern_Name);
		}
		else {
			click.Webdriver_Click(By.id("newGlobalRadio"), false);
		}

		if(enabled!=null) {
			Util.Report_Log(Status.INFO,"Selecting the Enabled or Disable state  of the status");
			click.Webdriver_Select_Checkbox(By.xpath("//div[@id='newEnabledCheckbox_div']"
					+ "//label[text()[normalize-space() = 'Enabled']]//following-sibling::div//input[@id='newEnabledCheckbox']"), enabled);
		}

		if(editable!=null) {
			Util.Report_Log(Status.INFO,"Selecting the checkbox for Offer Edit Mode");
			click.Webdriver_Select_Checkbox(By.xpath("//div[@id='newOfferEditableCheckbox_div']"
					+ "//label[text()[normalize-space() = 'Offer is Editable']]//following-sibling::div//input[@id='newOfferEditableCheckbox']"), editable);
		}


		Util.Report_Log(Status.INFO,"Selecting the color status");
		click.Webdriver_Click(By.xpath("//div[@class='palette-color-picker-button']"), false);
		click.Webdriver_Click(By.xpath("//div[@class='palette-color-picker-button']//span[@data-color='"+color+"']"), false);

		if(task_Name!=null) {
			Util.Report_Log(Status.INFO,"Selecting Action Task");
			select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//div[@id='newTaskTypeDropdown_chosen']"), 
					By.xpath("//div[@id='newTaskTypeDropdown_chosen']//div[@class='chosen-drop']//input"), task_Name);
		}

		if(job_Name!=null){
			Util.Report_Log(Status.INFO,"Selecting Scheduled Job");
			select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//div[@id='newJobBatchDropdown_chosen']"), 
					By.xpath("//div[@id='newJobBatchDropdown_chosen']//div[@class='chosen-drop']//input"), job_Name);
		}

		Util.Report_Log(Status.INFO,"Saving the new status");
		click.Webdriver_Click(By.xpath("//button[@id='newStatusSubmit']"), false);
	}

	@Test(groups= {"Create_New_Group"})
	@Parameters({"group_Name", "group_Description", "group_color","group_Enable"})
	public static void Create_New_Group(String group_Name, @Optional String group_Description, String group_color, @Optional Boolean group_Enable)
	{
		if(group_Name.contains("random"))
			global_Group_Name=group_Name.replace("random", Constants.random_Number);
		else
			global_Group_Name = group_Name;

		Util.Report_Log(Status.INFO,"Creating the new Offer Group");
		click.Webdriver_Click(By.id("newGroup"), false);
		Assert.assertTrue(webelement.Is_Displayed(By.xpath("//div[@id='newGroupModal']//div[@class='modal-content']"), 4), "The New Group Modal creation pop-up iz not visible.");

		Util.Report_Log(Status.INFO,"Entering the New Offer Group Name and Details");
		sendkeys.Webdriver_Sendkeys(By.xpath("//div[@id='newGroupModal']//label[normalize-space(text())='Group Name']//following-sibling::div//input[@name='newGroupName']"), global_Group_Name);

		if(group_Description!=null)
			sendkeys.Webdriver_Sendkeys(By.xpath("//div[@id='newGroupModal']//label[normalize-space(text())='Group Description']//following-sibling::div//textarea[@name='newGroupDescription']"), group_Description);

		Util.Report_Log(Status.INFO,"Selecting the New Offer Group Name Color");
		click.Webdriver_Click(By.xpath("//div[@id='newGroupModal']//label[normalize-space(text())='Color']//following-sibling::div//div[@class='palette-color-picker-button']"), false);
		visible.Pause(2);
		click.Webdriver_Click(By.xpath("//div[@class='palette-color-picker-button']//div//span[@data-color='"+group_color+"']"), false);

		if(group_Enable!=null) {
			Util.Report_Log(Status.INFO,"Selecting Enable/Disbale for the Group Name");
			click.Webdriver_Select_Checkbox(By.xpath("//div[@id='newGroupModal']//label[normalize-space(text())='Enabled']//following-sibling::div//input"), group_Enable);
		}

		Util.Report_Log(Status.INFO,"Saving the group Details");
		click.Webdriver_Click(By.xpath("//div[@class='modal-footer']//button[text()='Save']"), true);
		String saved_Group_Name= webelement.Get_Webelement(By.id("groupName")).getAttribute("value");
		Assert.assertEquals(saved_Group_Name, global_Group_Name.trim());
	}

	@Test(groups= {"Add_Group_Data_Field"})
	@Parameters({"db_Name","db_Name_Check","friendly_Name","enabled","data_Type", "source" ,"data_Feed_Name", "source_Values" ,"lookup_Column_Name","lookup_Link_Column_Name", "content_Block_Value","rule_Set","rule_Name"})
	public static void Add_Group_Data_Field(String db_Name, @Optional String db_Name_Check , @Optional String friendly_Name, @Optional Boolean enabled, 
			@Optional String data_Type, @Optional String source,
			@Optional String data_Feed_Name, @Optional String source_Values, @Optional String lookup_Column_Name, 
			@Optional String lookup_Link_Column_Name ,@Optional String content_Block_Value, 
			@Optional String rule_Set, @Optional String rule_Name)
	{
		Util.Report_Log(Status.INFO,"Clicking on the Add New Data Field button to add data field for Offer Group");
		click.Webdriver_Click(By.xpath("//div[not(contains(@class,'repeat'))][contains(@class,'panel-default')]//a[@id='newDataField']"), true);

		Util.Report_Log(Status.INFO,"Enterting the Data Field Data Base Name as " + db_Name);
		sendkeys.Webdriver_Sendkeys(By.xpath("//label[normalize-space(text())='Database Name']//following-sibling::div//input"), db_Name);
		
		if(friendly_Name!=null) {
			Util.Report_Log(Status.INFO,"Enterting the Data Field Friendly Name");
			sendkeys.Webdriver_Sendkeys(By.xpath("//label[normalize-space(text())='Friendly Name']//following-sibling::div//input"), friendly_Name);
		}

		if(enabled!=null) {
			Util.Report_Log(Status.INFO,"Selecting the Enable checkbox for Enable or Disabling the Data Filed");
			click.Webdriver_Select_Checkbox(By.xpath("//label[normalize-space(text())='Enabled']//following-sibling::div//input"), enabled);
		}

		if(data_Type!=null)
		{
			switch (data_Type) {
			case "Single Select Dropdown":
				Util.Report_Log(Status.INFO,"Selecting the Data Type as " + data_Type);
				select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//div[@id='dataTypeId_chosen']//a"),
						By.xpath("//div[@id='dataTypeId_chosen']//input"), data_Type);
				Assert.assertTrue(webelement.Is_Displayed(By.xpath("//div[@id='dataTypeId_chosen']//a//span[normalize-space(text())='"+data_Type+"']"), 1));

				Util.Report_Log(Status.INFO,"Selecting the Drop Down Source Type as " + source);
				select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[normalize-space(text())='Dropdown Source Type']//following-sibling::div/*[@id='sourceTypeId_chosen']"), 
						By.xpath("//div[@id='sourceTypeId_chosen']//input"), source);
				Assert.assertTrue(webelement.Is_Displayed(By.xpath("//div[@id='sourceTypeId_chosen']//a//span[normalize-space(text())='"+source+"']"), 1));

				if(source.equalsIgnoreCase("Write In Values")) {
					Util.Report_Log(Status.INFO,"Entering the write in values in text field as " + source_Values);
					sendkeys.Webdriver_Sendkeys(By.xpath("//label[text()='Values (comma separated)']//following-sibling::div//input"), source_Values);
					
					Util.Report_Log(Status.INFO,"Saving the Data Field");
					click.Webdriver_Click(By.xpath("//button[text()='Save']"), true);
				}
				else if(source.equalsIgnoreCase("Name/Value Column")) {
					Util.Report_Log(Status.INFO,"Selecting the Lookup Data Feed as " + data_Feed_Name);
					select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[normalize-space(text())='Lookup Data Feeds']//following-sibling::div//*[@id='lookupFeedId_chosen']"), 
							By.xpath("//div[@id='lookupFeedId_chosen']//input"), data_Feed_Name);
					Assert.assertTrue(webelement.Is_Displayed(By.xpath("//div[@id='lookupFeedId_chosen']//a//span[normalize-space(text())='"+data_Feed_Name+"']"), 1));
					
					
					Util.Report_Log(Status.INFO,"Selecting the Value: Link Columns  as " + lookup_Column_Name);
					select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[normalize-space(text())='Value: Link Columns']//following-sibling::div//*[@id='linkColumnValueId_chosen']"), 
							By.xpath("//div[@id='linkColumnValueId_chosen']//input"), lookup_Column_Name);
					Assert.assertTrue(webelement.Is_Displayed(By.xpath("//div[@id='linkColumnValueId_chosen']//a//span[normalize-space(text())='"+lookup_Column_Name+"']"), 1));
					
					visible.Pause(3);
					Util.Report_Log(Status.INFO,"Selecting the Display Name: Link Columns as " + lookup_Link_Column_Name);
					select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[normalize-space(text())='Display Name: Link Columns']//following-sibling::div//*[@id='linkColumnDisplayId_chosen']"), 
							By.xpath("//div[@id='linkColumnDisplayId_chosen']//input"), lookup_Link_Column_Name);
					Assert.assertTrue(webelement.Is_Displayed(By.xpath("//div[@id='linkColumnDisplayId_chosen']//a//span[normalize-space(text())='"+lookup_Link_Column_Name+"']"), 1));
					
					Util.Report_Log(Status.INFO,"Saving the Data Field");
					click.Webdriver_Click(By.xpath("//button[text()='Save']"), true);
					
					Assert.assertEquals(text.Webdriver_Get_Text(By.xpath("//div[text()='Value Data Feed']//following-sibling::div[1]")), data_Feed_Name+"."+lookup_Column_Name, "Expected Values are not getting saved on Data Field Page");
					Assert.assertEquals(text.Webdriver_Get_Text(By.xpath("//div[text()='Display Data Feed']//following-sibling::div[1]")), data_Feed_Name+"."+lookup_Link_Column_Name, "Expected Values are not getting saved on Data Field Page");
				}
				else {
					Util.Report_Log(Status.INFO,"Selecting the Lookup Data Feed as " + data_Feed_Name);
					select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[normalize-space(text())='Lookup Data Feeds']//following-sibling::div//*[@id='lookupFeedId_chosen']"), 
							By.xpath("//div[@id='lookupFeedId_chosen']//input"), data_Feed_Name);

					Util.Report_Log(Status.INFO,"Selecting the Link Columns for the Data feed as " + lookup_Column_Name);
					select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[normalize-space(text()='Link Columns')]//following-sibling::div//*[@id='linkcolumnId_chosen']"), 
							By.xpath("//div[@id='linkcolumnId_chosen']//input"), lookup_Column_Name);
					
					Util.Report_Log(Status.INFO,"Saving the Data Field");
					click.Webdriver_Click(By.xpath("//button[text()='Save']"), true);
					Assert.assertEquals(text.Webdriver_Get_Text(By.xpath("//div[text()='External Column']//following-sibling::div[3]")), data_Feed_Name+"."+lookup_Column_Name, "Expected Values are not getting saved on Data Field Page");
				}

				break;

			case "Date Selector":
				Util.Report_Log(Status.INFO,"Selecting the Date Selector Field");
				select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//div[@id='dataTypeId_chosen']//a"),
						By.xpath("//div[@id='dataTypeId_chosen']//div[@class='chosen-search']//input"), data_Type);

				
				Util.Report_Log(Status.INFO,"Saving the Data Field");
				click.Webdriver_Click(By.xpath("//button[text()='Save']"), true);
				break;

			case "Text Small (100 char)":
				Util.Report_Log(Status.INFO,"Selecting the Small Text Field");
				select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//div[@id='dataTypeId_chosen']//a"),
						By.xpath("//div[@id='dataTypeId_chosen']//div[@class='chosen-search']//input"), data_Type);
				
				Util.Report_Log(Status.INFO,"Saving the Data Field");
				click.Webdriver_Click(By.xpath("//button[text()='Save']"), true);
				break;

			case "Text Large (Unlimited)":
				Util.Report_Log(Status.INFO,"Selecting the Large Text Field");
				select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//div[@id='dataTypeId_chosen']//a"),
						By.xpath("//div[@id='dataTypeId_chosen']//div[@class='chosen-search']//input"), data_Type);
				
				Util.Report_Log(Status.INFO,"Saving the Data Field");
				click.Webdriver_Click(By.xpath("//button[text()='Save']"), true);
				break;

			case "Content Block Selector":
				Util.Report_Log(Status.INFO,"Selecting the Content Block Values as " + content_Block_Value);
				select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//div[@id='dataTypeId_chosen']//a"),
						By.xpath("//div[@id='dataTypeId_chosen']//div[@class='chosen-search']//input"), data_Type);
				visible.Pause(14);
				click.Webdriver_Click(By.xpath("//label[normalize-space(text())='Associated Content Sets']//following-sibling::div//input"), false);
				sendkeys.Webdriver_Sendkeys_Enter(By.xpath("//label[normalize-space(text())='Associated Content Sets']//following-sibling::div//input"), content_Block_Value);

				Util.Report_Log(Status.INFO,"Saving the Data Field");
				click.Webdriver_Click(By.xpath("//button[text()='Save']"), true);
				visible.Pause(14);
				Assert.assertTrue(webelement.Is_Displayed(By.xpath("//div[@id='sharedContentSet_chosen']//span[text()='"+content_Block_Value+"']"), 9));
				
				break;	

			case "Rule Execution":
				select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//div[@id='dataTypeId_chosen']//a"),
						By.xpath("//div[@id='dataTypeId_chosen']//div[@class='chosen-search']//input"), data_Type);
				select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[normalize-space(text())='Rule Set']//following-sibling::div//*[@id='ruleSetID_chosen']"), 
						By.xpath("//div[@id='ruleSetID_chosen']//input"), rule_Set);
				select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[normalize-space(text())='Rule']//following-sibling::div//*[@id='ruleID_chosen']"), 
						By.xpath("//div[@id='ruleID_chosen']//input"), rule_Name);
				
				Util.Report_Log(Status.INFO,"Saving the Data Field");
				click.Webdriver_Click(By.xpath("//button[text()='Save']"), true);
				Assert.assertTrue(!webelement.Is_Displayed(By.xpath("//div[@class='errorMesg']"), 3) , "The Rules selector is not saved");
				break;	
			}
		}

		else
		{
			Util.Report_Log(Status.INFO,"Saving the Data Field");
			click.Webdriver_Click(By.xpath("//button[text()='Save']"), true);
			if(db_Name_Check!=null)
			{
				Util.Report_Log(Status.INFO,"Asserting that invalid DB name is not getting saved " + db_Name);
				Assert.assertTrue(webelement.Is_Displayed(By.xpath("//div[@class='errorMesg'][text()='//div[@class='errorMesg'][text()='The Database Name supports only alphanumeric characters and - or _']']"), 2), "The error meaasge should be displayed as: The Database Name supports only alphanumeric characters and - or _");
			}
			else {
				Assert.assertTrue(webelement.Is_Displayed(By.xpath("//div[@class='errorMesg'][text()='Please select a data type']"), 2), "The error meaasge should be displayed as Please select a data type but the Data Field got saved without Data Type");
			}
		}

	}

	@Test(groups= {"Validate_Offers_Tables_Headers"})
	@Parameters({"table_Name","columnslist"})
	public static void Validate_Offers_Tables_Headers(String table_Name, String columnslist)
	{
		table_Name = table_Name.equalsIgnoreCase("Offer_Type")? "offerTypeTable": table_Name.equalsIgnoreCase("Offer_Group")? "groupTable": "dataFieldTable";

		Util.Report_Log(Status.INFO,"Validating the Table Headers for the Offer Type");
		Util.validate_Tables_Headers(table_Name, columnslist, By.xpath("//table[@id[contains(.,'" + table_Name +"')]]//thead//th"));
	}

	@Test(groups= {"Navigate_Using_BreadCrumbs"})
	@Parameters({"breadCrumbs_Name"})
	public static void Navigate_Using_BreadCrumbs(String breadCrumbs_Name)
	{
		if(Constants.build_Tag !=null)
		{
			breadCrumbs_Name = breadCrumbs_Name.contains("random") ? breadCrumbs_Name.replace("random", Constants.random_Number) : breadCrumbs_Name.contains("label") ? breadCrumbs_Name.replace("label", Constants.label) : breadCrumbs_Name;
			Util.Report_Log(Status.INFO,"Clicking on the value " + breadCrumbs_Name + " from the breadcrumbs section.");
			click.Webdriver_Click(By.xpath("//div[@id='row-breadcrumbs']//li//a[contains(text(), '"+breadCrumbs_Name+"')]"), true);
		}
		else
		{
			breadCrumbs_Name = breadCrumbs_Name.contains("label") ? breadCrumbs_Name.replace("Offer_Type_label", "Master"+Constants.label) : breadCrumbs_Name.contains("random") ? breadCrumbs_Name.replace("random", Constants.random_Number) : breadCrumbs_Name;
			click.Webdriver_Click(By.xpath("//div[@id='row-breadcrumbs']//li//a[contains(text(), '"+breadCrumbs_Name+"')]"), true);
		}
	}

	@Test(groups= {"Filter_And_Validate_Table_For_Offer_Type_Details_Page"})
	@Parameters({"table_Name","column_Name","filter_Value"})
	public static void Filter_Table(String table_Name , String column_Name, String filter_Value)
	{
		filter_Value = filter_Value.contains("random") ? filter_Value.replace("random", Constants.random_Number) : filter_Value.contains("label") ? filter_Value.replace("label", Constants.label) :  filter_Value;

		Util.Report_Log(Status.INFO,"Filterng the Table present for Offer types with the value as " + column_Name);
		String get_Col_Index = Util.Get_Col_Index(By.xpath("//table[@id='groupTable']//th"), column_Name);

		Util.Report_Log(Status.INFO,"Column index for " + filter_Value + " is found on " + get_Col_Index +" position");
		column_Name = column_Name.equalsIgnoreCase("Group Name")?"GroupName" : column_Name;
		click.Webdriver_Click(By.xpath("//table[@id='groupTable']//th["+get_Col_Index+"]//i"), false);
		sendkeys.Webdriver_Sendkeys(By.xpath("//input[@id='groupTableGroupName']"), filter_Value);
		click.Webdriver_Click(By.id("applygroupTableGroupName2"), false);

		Util.Report_Log(Status.INFO,"Validating the filtered results.");
		int row_Count = webelement.Webelement_Size(By.xpath("//table[@id='groupTable']//tbody//tr"));
		Assert.assertTrue(row_Count>0);
		Assert.assertTrue(row_Count<=1);

		String col_Data = text.Webdriver_Get_Text(By.xpath("//table[@id='groupTable']//tbody//tr//td["+get_Col_Index+"]"));
		Assert.assertEquals(col_Data, filter_Value);

	}

	@Test(groups= {"Validate_Data_Field_Count_On_Group_Table"})
	@Parameters({"table_Name","column_Name","filter_Value","validate_Count"})
	public static void Validate_Data_Field_Count_On_Group_Table(String table_Name , String column_Name, String filter_Value, String validate_Count)
	{
		filter_Value = filter_Value.contains("random") ? filter_Value.replace("random", Constants.random_Number) : filter_Value.contains("label") ? filter_Value.replace("label", Constants.label) :  filter_Value;

		//navigate.Webdriver_Refresh();
		Util.Report_Log(Status.INFO,"Validating the data fields count on the Offer Type Details page under Offer Group Table.");
		Filter_Table(table_Name, column_Name, filter_Value);

		String count= text.Webdriver_Get_Text(By.xpath("//table[@id='groupTable']//tbody//tr//td[5]"));
		Assert.assertEquals(count, validate_Count);
	}

	@Test(groups = {"Open_Offer_Group_Details_Page"})
	@Parameters({"group_Name","table_Name","column_Name"})
	public static void Open_Offer_Group_Details_Page(@Optional String group_Name, String table_Name, String column_Name)
	{
		navigate.Webdriver_Refresh();

		Util.Report_Log(Status.INFO,"Opening the Offer Group Deatils Page from Offer Type Details Page.");
		if(group_Name!=null)
			group_Name = group_Name;
		else
			group_Name=global_Group_Name.trim();

		Util.Report_Log(Status.INFO,"Filtering the Offer Group from table and Opening it.");
		Filter_Table(table_Name, column_Name, group_Name);
		String name = text.Webdriver_Get_Text(By.xpath("//table[@id='groupTable']//tbody//tr//td[2]"));
		if(name.equalsIgnoreCase(group_Name))
			click.Webdriver_Click(By.xpath("//table[@id='groupTable']//tbody//tr//td[2]/a"), true);

		Assert.assertTrue(webelement.Is_Displayed(By.xpath("//h4[text()='Offer Type Group']"), 2));
		Assert.assertTrue(webelement.Is_Displayed(By.xpath("//h4[text()='Group Data Fields']"), 2));
	}

	@Test(groups= {"Copy_Group"})
	@Parameters({"new_Name","copy_Type","copy_Value","copy_To"})
	public static void Copy_Group(String new_Name, String copy_Type, @Optional String copy_Value, @Optional String copy_To)
	{
		Util.Report_Log(Status.INFO,"Copying the Rquired Group or Data Field to " + copy_Type + " Offer Type");
		new_Name = new_Name.contains("random") ? new_Name.replace("random", Constants.random_Number) : new_Name;

		if(copy_Value!=null)
			copy_Value=copy_Value;
		else
			copy_Value= global_Group_Name.trim();

		Util.Report_Log(Status.INFO,"Counting the Data Field Before the Copy");
		String data_count = text.Webdriver_Get_Text(By.xpath("//table[contains(@id,'Table')]//tr//td//*[normalize-space(text())='"+copy_Value+"']//parent::td//following-sibling::td[3]"));

		Util.Report_Log(Status.INFO,"Clicking on the Copy Button");
		click.Webdriver_Click(By.xpath("//table[contains(@id,'Table')]//tr//td//*[normalize-space(text())='"+copy_Value+"']//parent::td//following-sibling::td[6]"), false);


		switch (copy_Type) 
		{
		case "Same Offer Type":
			Util.Report_Log(Status.INFO,"Asserting that the Copy Group Modal is visible and Eentering the values");
			Assert.assertTrue(webelement.Is_Displayed(By.xpath("//div[@id='copyGroupModal']//h4[text()='Copy Group']"), 2));
			sendkeys.Webdriver_Sendkeys(By.xpath("//*[@id='copyGroupModal']//div//label[normalize-space(text())='Group Name']//following-sibling::div//input"), new_Name);
			select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[normalize-space(text())='Create Under']//following-sibling::div//div[@id='copySelector_chosen']")
					, By.xpath("//div[@id='copySelector_chosen']//input"), copy_Type);

			Util.Report_Log(Status.INFO,"Saving Group");
			click.Webdriver_Click(By.id("copyGroupSubmit"), true);
			break;

		case "Different Offer Type":	
			Util.Report_Log(Status.INFO,"Asserting that the Copy Data Feild Modal is visible and Entering the Values");
			Util.Report_Log(Status.INFO,"Asserting that the Copy Group Modal is visible");
			Assert.assertTrue(webelement.Is_Displayed(By.xpath("//div[@id='copyGroupModal']//h4[text()='Copy Group']"), 2));
			sendkeys.Webdriver_Sendkeys(By.xpath("//*[@id='copyGroupModal']//div//label[normalize-space(text())='Group Name']//following-sibling::div//input"), new_Name);
			select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[normalize-space(text())='Create Under']//following-sibling::div//div[@id='copySelector_chosen']")
					, By.xpath("//div[@id='copySelector_chosen']//input"), copy_Type);
			select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath("//label[normalize-space(text())='Offer Type']//following-sibling::div//div[@id='offerTypeSelect_chosen']"), 
					By.xpath("//div[@id='offerTypeSelect_chosen']//input"), copy_To);

			Util.Report_Log(Status.INFO,"Saving Group");
			click.Webdriver_Click(By.id("copyGroupSubmit"), true);
			break;
		}

		Util.Report_Log(Status.INFO,"Validating that the Group is copied successfully under the required Offer Type and Contains the same number of Data Fields");
		Assert.assertTrue(webelement.Is_Displayed(By.xpath("//h4[text()='Offer Type Group']"),2));
		Assert.assertEquals(text.Webdriver_Get_Text_Of_Attribute(By.id("groupName"), "value"), new_Name,
				"The Copied Group name is not same as expected is " + new_Name + " but found " + text.Webdriver_Get_Text_Of_Attribute(By.id("groupName"), "value"));
		Assert.assertEquals(Util.get_Row_Count(By.xpath("//table[@id='dataFieldTable']//tbody//tr")), Integer.parseInt(data_count));
	}

	@Test(groups= {"Validate_Copied_Offer_Group_Saved"})
	@Parameters({"offer_Type_Name","group_Name","table_Name","column_Name"})
	public static void Validate_Copied_Offer_Group_Saved(@Optional String offer_Type_Name, @Optional String group_Name, String table_Name, String column_Name)
	{
		if (offer_Type_Name!=null)
			offer_Type_Name = offer_Type_Name;
		else
			offer_Type_Name = offer_Type.trim();

		if (group_Name!=null && group_Name.contains("random"))
			group_Name = group_Name.replace("random", Constants.random_Number);

		Util.Report_Log(Status.INFO,"Validating that the copied Group is getting listed on the Offer Type Deatils Page");
		Navigate_Using_BreadCrumbs(offer_Type_Name);

		Util.Report_Log(Status.INFO,"Filtering the Offer Group Table for Validation");
		Filter_Table(table_Name, column_Name, group_Name);

		String get_Col_Index=Util.Get_Col_Index(By.xpath("//table[@id='groupTable']//thead//th"), column_Name);
		String col_Text=text.Webdriver_Get_Text(By.xpath("//table[@id='groupTable']//tbody//tr//td["+get_Col_Index+"]//a"));
		Assert.assertEquals(col_Text, group_Name, "The group name is not present on the screen");
		navigate.Webdriver_Refresh();
	}

	@Test(groups="Get_Offer_Type_Data")
	@Parameters({"offer_Type_Name"})
	public static void Get_Offer_Type_Data(String offer_Type_Name)
	{
		Util.Report_Log(Status.INFO,"Fetching the Values of the Offer Groups");

		Util.Report_Log(Status.INFO,"Counting the total Number of Groups Present");
		int row_Count=Util.get_Row_Count(By.xpath("//table[@id='groupTable']//tbody//tr"));
		Util.Report_Log(Status.INFO,"The total Number of Groups Present are " + row_Count);
		for(int i=1;i<row_Count+1;i++)
		{
			String group_Name = text.Webdriver_Get_Text(By.xpath("//table[@id='groupTable']//tbody//tr["+i+"]//td[2]//a"));
			Util.Report_Log(Status.INFO,"Opening the group " + group_Name);
			click.Webdriver_Click(By.xpath("//table[@id='groupTable']//tbody//tr["+i+"]//td[2]//a[normalize-space(text())='"+group_Name+"']"), true);

			Util.Report_Log(Status.INFO,"Counting the total number of Data Fields in the group " + group_Name  + " and storing them");


			int data_Row_Count=Util.get_Row_Count(By.xpath("//table[@id='dataFieldTable']//tbody//tr"));

			Util.Report_Log(Status.INFO,"The total Number of Data Fields Present are " + data_Row_Count);
			ArrayList<String> data_Field = new ArrayList<String>();
			for(int j=1;j<data_Row_Count+1;j++)
			{
				String data_Field_Name = text.Webdriver_Get_Text(By.xpath("//table[@id='dataFieldTable']//tbody//tr["+j+"]//td[2]//a"));
				data_Field.add(data_Field_Name);
			}

			offers_Details.put(group_Name, data_Field);
			System.out.println(offers_Details);
			Navigate_Using_BreadCrumbs(offer_Type_Name);
		}
		System.out.println(offers_Details);
	}
}
