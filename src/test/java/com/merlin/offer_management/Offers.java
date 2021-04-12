package com.merlin.offer_management;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

public class Offers extends Annotation implements Init{

	static Properties prop = null;
	public static String global_Offer_Name=null;

	@BeforeTest(alwaysRun = true)
	public void before_test() throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Offers.properties"));

	}

	@Test(groups= {"Vaildated_Hamburger_Menu"})
	@Parameters({"menu_Option","is_Displayed","sub_Menu"})
	public static void Vaildated_Hamburger_Menu(String menu_Option, boolean is_Displayed, @Optional String sub_Menu)
	{
		Util.Report_Log(Status.INFO, "Opening the Hamburger Menu and Validating if the required Link is present or not");
		click.Webdriver_Click(By.id("sidenav-toggle-icon"), false);

		Util.Report_Log(Status.INFO, "The Hamburger menu is opend and now validating if the link is present");

		if(sub_Menu!=null){
			if(webelement.Is_Displayed(By.xpath(prop.getProperty("Hamburger_Icon_Menu_Options_And_Drop_Arrow_Xpath").replace("menu_Option", menu_Option)), 2)) {

				Util.Report_Log(Status.INFO, "The Menu Option have sub options, thus clicking on the arrow button to open sub menu");
				click.Webdriver_Click(By.xpath(prop.getProperty("Hamburger_Icon_Menu_Options_And_Drop_Arrow_Xpath").replace("menu_Option", menu_Option)), false);

				Util.Report_Log(Status.INFO, "Asserting the sub menu option is opened");
				Assert.assertTrue(webelement.Webelement_Size(By.xpath(prop.getProperty("Hamburger_Menu_Arrow_Down_Xpath")))<=1);
				Assert.assertEquals(webelement.Is_Displayed(By.xpath(prop.getProperty("Hamburger_Sub_Menu_Options_And_Drop_Arrow_Xpath").replace("sub_Menu", sub_Menu)), 3), is_Displayed);
			}
		}
		else
		{
			Assert.assertEquals(webelement.Is_Displayed(By.xpath(prop.getProperty("Hamburger_Menu_Options_And_Drop_Arrow_Xpath").replace("menu_Option", menu_Option)), 3), is_Displayed);
		}
	}

	@Test(groups= {"Open_Offers_Page"})
	public static void Open_Offers_Page()
	{
		navigate.Webdriver_Navigate(Constants.dashboard_Offers_Page_URL);
		Util.Report_Log(Status.INFO, "Asserting User landed on the Offers Page");
		Assert.assertTrue(webelement.Is_Displayed(By.xpath(prop.getProperty("Offers_Page_Header_Xpath")), 3)
				, "Offers Page didn't loaded properly");
		Assert.assertTrue(webelement.Is_Displayed(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Button")), 3)
				, "Offers Page didn't loaded properly, Create New Offer Button is missing");
		Assert.assertTrue(webelement.Is_Displayed(By.id("offerListTable"), 3)
				, "Offers Page didn't loaded properly, Create New Offer Button is missing");
	}

	@Test(groups= {"Create_New_Offer"})
	@Parameters({"offer_Type","offer_Name","offer_Code","start_Date","end_Date"})
	public static void Create_New_Offer(String offer_Type, String offer_Name, String offer_Code, @Optional String start_Date, @Optional String end_Date)
	{
		global_Offer_Name = offer_Name.contains("label") ? offer_Name.replace("label", Constants.label) : offer_Name;
		offer_Code = offer_Code.contains("random") ? offer_Code.replace("random", Constants.random_Number) : offer_Code;

		Util.Report_Log(Status.INFO, "Creating a New Offer Type");
		click.Webdriver_Click(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Button_Xpath")), false);
		click.Webdriver_Click(By.id(prop.getProperty("Offers_Page_Create_New_Offer_From_Scratch_Id")), false);
		Assert.assertTrue(webelement.Is_Displayed(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_PopUp_Header_Xapth")), 2), "Create New Offer Pop-Up Modal is not visible.");

		Util.Report_Log(Status.INFO, "Entering the Details for the new Offer");
		select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Select_Offer_Type_Drop_Down_Xpath")), 
				By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Select_Offer_Type_Drop_Down_Input_Field_Xpath")), offer_Type);
		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Offer_Name_Text_Field_Xpath")), global_Offer_Name);
		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Offer_Code_Text_Field_Xpath")), offer_Code);

		Util.Report_Log(Status.INFO, "Entering the Start Date");
		click.Webdriver_Click(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Start_Date_Text_Field_Xpath")), false);
		click.Webdriver_Click(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Current_Date_Picker_Xpath")), false);

		Util.Report_Log(Status.INFO, "Entering the End Date");
		click.Webdriver_Click(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_End_Date_Text_Field_Xpath")), false);
		String current_Date = text.Webdriver_Get_Text(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Current_Date_Picker_Xpath")));

		Util.Report_Log(Status.INFO, "Fetching the last day of the Month");
		int row_Count = Util.get_Row_Count(By.xpath("//table[@class='ui-datepicker-calendar']//tbody//tr"));
		int col_Count = Util.get_Row_Count(By.xpath("//table[@class='ui-datepicker-calendar']//tbody//tr["+row_Count+"]//td//a"));
		String last_Date = text.Webdriver_Get_Text(By.xpath("//table[@class='ui-datepicker-calendar']//tbody//tr["+row_Count+"]//td["+col_Count+"]//a"));

		Util.Report_Log(Status.INFO, "Checking if End date and Start date are equal");
		if(current_Date.equals(last_Date))
		{
			Util.Report_Log(Status.INFO, "End date and start date are same start date is " + current_Date + "month end date us "+ last_Date);
			click.Webdriver_Click(By.xpath("//div[@id='ui-datepicker-div']//span[text()='Next']"), false);
			click.Webdriver_Click(By.xpath("//table[@class='ui-datepicker-calendar']//tbody//tr["+row_Count+"]//td["+col_Count+"]//a"), false);
		}
		else
			click.Webdriver_Click(By.xpath("//table[@class='ui-datepicker-calendar']//tbody//tr["+row_Count+"]//td["+col_Count+"]//a"), false);

		Util.Report_Log(Status.INFO, "Saving the Offer Details");
		click.Webdriver_Click(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Save_Button_Xpath")), false);
	}

	@Test(groups= {"Validated_Mandatory_Filed_For_Offer_Creation"})
	@Parameters({"offer_Type","offer_Name","offer_Code","start_Date","end_Date"})
	public static void Validated_Mandatory_Filed_For_Offer_Creation(String offer_Type, String offer_Name, String offer_Code, @Optional String start_Date, @Optional String end_Date)
	{
		String msg;
		Util.Report_Log(Status.INFO, "Validating the Error Growl Message for the Every Required fields.");

		Util.Report_Log(Status.INFO, "Clicking on the button to create New Offer");
		click.Webdriver_Click(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Button_Xpath")), false);
		click.Webdriver_Click(By.id(prop.getProperty("Offers_Page_Create_New_Offer_From_Scratch_Id")), false);

		Util.Report_Log(Status.INFO, "Validating all the required fileds are diasbled as no Offer Type is Selected");
		Assert.assertTrue(webelement.Is_Present(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Name_Text_Field_Xpath")+"[@disabled]"), 2),
				"The Offer Name Text Field is enabled where as it should be disbaled.");

		Assert.assertTrue(webelement.Is_Present(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Code_Text_Field_Xpath")+"[@disabled]"), 2),
				"The Offer Code Text Field is enabled where as it should be disbaled.");

		Assert.assertTrue(webelement.Is_Present(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Start_Date_Field_Xpath")+"[@disabled]"), 2),
				"The Start Date Text Field is enabled where as it should be disbaled.");

		Assert.assertTrue(webelement.Is_Present(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_End_Date_Field_Xpath")+"[@disabled]"), 2),
				"The End Date Text Field is enabled where as it should be disbaled.");

		Util.Report_Log(Status.INFO, "Validating all the required fileds are diasbled as no Offer Type is Selected");

		Util.Report_Log(Status.INFO, "Selecting the Offer Type as " + offer_Type);
		select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Select_Offer_Type_Drop_Down_Xpath")), 
				By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Select_Offer_Type_Drop_Down_Input_Field_Xpath")), offer_Type);
		Util.Report_Log(Status.INFO, "Saving Offer Type and Validating the Error");
		msg = click.Webdriver_click_capture_GrowlMessage(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Save_Button_Xpath")));
		Assert.assertEquals(msg, "Please enter an Offer Name.");
		click.close_Growl_Message();

		Util.Report_Log(Status.INFO, "Entering the Offer Name as " + offer_Name);
		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Name_Text_Field_Xpath")), offer_Name);
		msg = click.Webdriver_click_capture_GrowlMessage(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Save_Button_Xpath")));
		Assert.assertEquals(msg, "Please enter an Offer Code.");
		click.close_Growl_Message();

		Util.Report_Log(Status.INFO, "Entering the Offer Code as " + offer_Code);
		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Code_Text_Field_Xpath")), offer_Code);
		msg = click.Webdriver_click_capture_GrowlMessage(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Save_Button_Xpath")));
		Assert.assertEquals(msg, "Please enter a Start Date.");
		click.close_Growl_Message();
		
		Util.Report_Log(Status.INFO, "Entering the Start Date as " + start_Date);
		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Start_Date_Field_Xpath")), start_Date);
		msg = click.Webdriver_click_capture_GrowlMessage(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Save_Button_Xpath")));
		Assert.assertEquals(msg, "Please enter a End Date.");
		click.close_Growl_Message();
		
		Util.Report_Log(Status.INFO, "Entering the End Date as " + start_Date);
		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_End_Date_Field_Xpath")), start_Date);
		click.Webdriver_Click(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Cancel_Button_Xpath")), false);
		
		Util.Report_Log(Status.INFO, "Click on cancel button and validation that the Pop is removed.");
		Assert.assertTrue(!webelement.Is_Displayed(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_PopUp_Xapth")), 1), "The Create Offer Modal is still Visisble whereas it should be removed.");
		Assert.assertTrue(webelement.Is_Displayed(By.id(prop.getProperty("Offer_Page_Offers_Table_Id")), 2), "Offers Table is not Visible");
	}

	@Test(groups= {"Validate_Offer_Type_Under_Offer_Creation"})
	@Parameters({"offer_Type","is_Present"})
	public static void Validate_Offer_Type_Under_Offer_Creation(String offer_Type, Boolean is_Present)
	{
		Util.Report_Log(Status.INFO, "Validating that the Offer Type is Visible or not on Offer creation Modal.");
		Util.Report_Log(Status.INFO, "Validating the Error Growl Message for the Every Required fields.");

		Util.Report_Log(Status.INFO, "Clicking on the button to create New Offer");
		click.Webdriver_Click(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Button_Xpath")), false);
		click.Webdriver_Click(By.id(prop.getProperty("Offers_Page_Create_New_Offer_From_Scratch_Id")), false);
		
		Util.Report_Log(Status.INFO, "Selecting the Offer Type as " + offer_Type);
		select.Webdriver_Select_Dropdown_By_Click_Sendkeys_Enter(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Select_Offer_Type_Drop_Down_Xpath")), 
				By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Select_Offer_Type_Drop_Down_Input_Field_Xpath")), offer_Type);
		
		if(is_Present)
			Assert.assertTrue(webelement.Is_Displayed(By.xpath(prop.getProperty("Offers_Page_Select_Drop_Down_Selected_Value").replace("offer_Type", offer_Type)), 1), "Offer Type should be present but not");
		else
			Assert.assertTrue(webelement.Is_Displayed(By.xpath(prop.getProperty("Offers_Page_Select_Drop_Down_Selected_Value").replace("offer_Type", "Select One")), 1), "Offer Type should be present but not");
		
		click.Webdriver_Click(By.xpath(prop.getProperty("Offers_Page_Create_New_Offer_Modal_Cancel_Button_Xpath")), false);
	}

}