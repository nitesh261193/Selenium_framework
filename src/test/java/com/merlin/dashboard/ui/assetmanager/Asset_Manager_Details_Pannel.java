package com.merlin.dashboard.ui.assetmanager;

import static com.merlin.apps.locators.Asset_Manager_Locators.all_Selected_Custom_Fields_Values;
import static com.merlin.apps.locators.Asset_Manager_Locators.all_Selected_Tag_Values;
import static com.merlin.apps.locators.Asset_Manager_Locators.applied_Metadata;
import static com.merlin.apps.locators.Asset_Manager_Locators.details_Item_Name_Title_Text;
import static com.merlin.apps.locators.Asset_Manager_Locators.details_Pane_Field;
import static com.merlin.apps.locators.Asset_Manager_Locators.metadata_Custom_Field_Input_Box;
import static com.merlin.apps.locators.Asset_Manager_Locators.metadata_Edit_button;
import static com.merlin.apps.locators.Asset_Manager_Locators.metadata_Header;
import static com.merlin.apps.locators.Asset_Manager_Locators.metadata_React_Drop_Down;
import static com.merlin.apps.locators.Asset_Manager_Locators.metadata_Tag_Input_Box;
import static com.merlin.apps.locators.Asset_Manager_Locators.save_Button;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;

/**
 * @author Krishna Saini
 *
 */
public class Asset_Manager_Details_Pannel extends Annotation implements Init {

	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		// navigate.Webdriver_Refresh();
		navigate.Switch_To_Frame("fileSystemIFrame");
	}

	@Test(groups = { "Verify_Folder_Name_In_Details_Pannel" })
	@Parameters({ "verify_Name" })
	public static void Verify_Folder_Name_In_Details_Pannel(String verify_Name)
			throws UnsupportedFlavorException, IOException {
		verify_Name = verify_Name.replace("random", Constants.random_Number);
		Asset_Manager_Util.Select_Given_Item(verify_Name);
		visible.Wait_Until_Visible(60, details_Item_Name_Title_Text);
		String file_Folder_Name = text.Webdriver_Get_Text(details_Item_Name_Title_Text);
		if (file_Folder_Name.equalsIgnoreCase(verify_Name))
			Util.Report_Log(Status.PASS, "Details pane opened for selected Folder-" + verify_Name);

		else
			Util.Report_Log(Status.FAIL, "Details pane not opened for selected Folder-" + verify_Name);

	}

	@Test(groups = { "Verify_Folder_Location_In_Details_Pannel" })
	@Parameters({ "verify_Location" })
	public static void Verify_Folder_Location_In_Details_Pannel(String verify_Location)
			throws UnsupportedFlavorException, IOException {
		verify_Location = verify_Location.replace("random", Constants.random_Number);

		String xpath = details_Pane_Field.replace("$detail_Field", "Location");
		visible.Wait_Until_Visible(60, By.xpath(xpath));

		String location = text.Javascript_Get_Text(By.xpath(xpath));
		if (location.equalsIgnoreCase(verify_Location))
			Util.Report_Log(Status.PASS,
					"Successfully verified given location : " + verify_Location + " in details pannel");

		else
			Util.Report_Log(Status.FAIL, "Unable to verify given location : " + verify_Location + " in details pannel");

	}

	@Test(groups = { "Verify_Folder_Creation_Date_In_Details_Pannel" })
	public static void Verify_Folder_Creation_Date_In_Details_Pannel()
			throws UnsupportedFlavorException, IOException {
		String xpath = details_Pane_Field.replace("$detail_Field", "Created");
		visible.Wait_Until_Visible(60, By.xpath(xpath));

		String created_Date = text.Javascript_Get_Text(By.xpath(xpath));
		if (created_Date.equalsIgnoreCase(Constants.time_Stamp) || created_Date.contains(Constants.time_Stamp))
			Util.Report_Log(Status.PASS, "Successfully verified given folder creation Date and Time : "
					+ Constants.time_Stamp + " in details pannel");

		else
			Util.Report_Log(Status.FAIL, "Unable to verify given folder creation Date and Time : " + Constants.time_Stamp
					+ " in details pannel");

	}

	@Test(groups = { "Apply_Tag_On_Given_Item" })
	@Parameters({ "item_Name", "tag_Names" })
	public static void Apply_Tag_On_Given_Item(String item_Name, String tag_Names)
			throws UnsupportedFlavorException, IOException {
		item_Name = item_Name.replace("random", Constants.random_Number);
		tag_Names = tag_Names.replace("random", Constants.random_Number);
		Util.Report_Log(Status.INFO, "Selecting given item : " + item_Name + " to apply tag on it");
		Asset_Manager_Util.Select_Given_Item(item_Name);
		visible.Wait_Until_Visible(60, metadata_Edit_button);
		click.Webdriver_Click(metadata_Edit_button, true);
		Util.Report_Log(Status.INFO, "Clicked on meta data edit button");
		visible.Wait_Until_Visible(60, metadata_Header);

		String header_Text = text.Webdriver_Get_Text(metadata_Header);
		Assert.assertTrue((header_Text.contains("Manage Metadata") || header_Text.equalsIgnoreCase("Manage Metadata")),
				"Manage Metadata popup does not opened.");
		Util.Report_Log(Status.INFO, "Manage Metadata Popup Opened Successufully.");
		for (String tag_Name : tag_Names.split(",")) {
			Util.Report_Log(Status.INFO, "Typing tag name : " + tag_Name + " to select it from existing tags.");
			sendkeys.Webdriver_Sendkeys(metadata_Tag_Input_Box, tag_Name);
			click.Webdriver_Click(By.xpath(metadata_React_Drop_Down.replace("$Option_Name", tag_Name)), true);
			ArrayList<String> selected_Tags = text.Get_Elements_Text(all_Selected_Tag_Values);

			boolean flag = selected_Tags.contains(tag_Name);
			Assert.assertTrue(flag, "Unable to verify given tag : " + tag_Name + " in selected tag list");
			Util.Report_Log(Status.INFO, "successfully verified given tag : " + tag_Name + " in selected tag list");
		}
		click.Webdriver_Click(save_Button, true);
		Util.Report_Log(Status.PASS, "successfully clicked on save button");

	}

	@Test(groups = { "Verify_Applied_Tags" })
	@Parameters({ "item_Name", "tag_Names" })
	public static void Verify_Applied_Tags(String item_Name, String tag_Names)
			throws UnsupportedFlavorException, IOException {
		item_Name = item_Name.replace("random", Constants.random_Number);
		tag_Names = tag_Names.replace("random", Constants.random_Number);
		Util.Report_Log(Status.INFO, "Selecting given item : " + item_Name + " to verify tag applied on it");
		Asset_Manager_Util.Select_Given_Item(item_Name);
		visible.Wait_Until_Visible(60, metadata_Edit_button);
		String applied_Tags = text.Webdriver_Get_Text(By.xpath(applied_Metadata.replace("$Name", "Tags")));
		boolean flag = true;
		for (String tag_Name : tag_Names.split(",")) {
			if (applied_Tags.contains(tag_Name))
				Util.Report_Log(Status.PASS, "successfully verified given tag : " + tag_Name + " in applied tag list");
			else {
				Util.Report_Log(Status.FAIL, "Unable to verify given tag : " + tag_Name + " in applied tag list");
				flag = false;
			}
		}
		Assert.assertTrue(flag, "Unable to verify given tags : " + tag_Names + " in applied tag list");
		Util.Report_Log(Status.PASS, "successfully verified given tag : " + tag_Names + " in applied tag list");

	}

	@Test(groups = { "Apply_Multi_Value_Custom_Fields_On_Given_Item" })
	@Parameters({ "item_Name", "custom_Field_Name", "custom_Field_Values" })
	public static void Apply_Multi_Value_Custom_Fields_On_Given_Item(String item_Name, String custom_Field_Name,
			String custom_Field_Values) throws UnsupportedFlavorException, IOException {
		item_Name = item_Name.replace("random", Constants.random_Number);
		custom_Field_Name = custom_Field_Name.replace("random", Constants.random_Number);
		custom_Field_Values = custom_Field_Values.replace("random", Constants.random_Number);
		Util.Report_Log(Status.INFO, "Selecting given item : " + item_Name + " to apply custom field on it");
		Asset_Manager_Util.Select_Given_Item(item_Name);
		visible.Wait_Until_Visible(60, metadata_Edit_button);
		click.Webdriver_Click(metadata_Edit_button, true);
		Util.Report_Log(Status.INFO, "Clicked on meta data edit button: " + item_Name + " to apply custom field on it");
		visible.Wait_Until_Visible(60, metadata_Header);

		String header_Text = text.Webdriver_Get_Text(metadata_Header);
		Assert.assertTrue((header_Text.contains("Manage Metadata") || header_Text.equalsIgnoreCase("Manage Metadata")),
				"Manage Metadata popup does not opened.");
		Util.Report_Log(Status.INFO, "Manage Metadata Popup Opened Successufully.");
		for (String custom_Field_Value : custom_Field_Values.split(",")) {
			Util.Report_Log(Status.INFO,
					"Typing custom field value : " + custom_Field_Value + " to select it from existing values.");
			sendkeys.Webdriver_Sendkeys(
					By.xpath(metadata_Custom_Field_Input_Box.replace("$Custom_Field", custom_Field_Name)),
					custom_Field_Value);
			click.Webdriver_Click(By.xpath(metadata_React_Drop_Down.replace("$Option_Name", custom_Field_Value)), true);
			ArrayList<String> selected_Fields = text.Get_Elements_Text(
					By.xpath(all_Selected_Custom_Fields_Values.replace("$Custom_Field", custom_Field_Name)));

			boolean flag = selected_Fields.contains(custom_Field_Value);
			Assert.assertTrue(flag, "Unable to verify given custom field value : " + custom_Field_Value
					+ " in selected custom field values");
			Util.Report_Log(Status.INFO, "successfully verified given custom field value : " + custom_Field_Value
					+ " in custom field values");
		}
		click.Webdriver_Click(save_Button, true);
		Util.Report_Log(Status.PASS, "successfully clicked on save button");

	}

	@Test(groups = { "Verify_Applied_Custom_Fields" })
	@Parameters({ "item_Name", "custom_Field_Name", "custom_Field_Values" })
	public static void Verify_Applied_Custom_Fields(String item_Name, String custom_Field_Name,
			String custom_Field_Values) throws UnsupportedFlavorException, IOException {
		item_Name = item_Name.replace("random", Constants.random_Number);
		custom_Field_Name = custom_Field_Name.replace("random", Constants.random_Number);
		custom_Field_Values = custom_Field_Values.replace("random", Constants.random_Number);
		Util.Report_Log(Status.INFO, "Selecting given item : " + item_Name + " to verify applied custom field on it");
		Asset_Manager_Util.Select_Given_Item(item_Name);
		visible.Wait_Until_Visible(60, metadata_Edit_button);
		String applied_Custom_Field = text
				.Webdriver_Get_Text(By.xpath(applied_Metadata.replace("$Name", custom_Field_Name)));
		boolean flag = true;
		for (String custom_Field_Value : custom_Field_Values.split(",")) {
			if (applied_Custom_Field.contains(custom_Field_Value))
				Util.Report_Log(Status.PASS, "Successfully verified given custom field value : " + custom_Field_Value
						+ " in applied custom field value for : " + custom_Field_Name);
			else {
				Util.Report_Log(Status.FAIL, "Unable to verify given custom field value : " + custom_Field_Value
						+ " in applied custom field value for : " + custom_Field_Name);
				flag = false;
			}
		}
		Assert.assertTrue(flag, "Unable to verify given custom field values : " + custom_Field_Values
				+ " in applied values for : " + custom_Field_Name);
		Util.Report_Log(Status.PASS, "successfully verified given custom field values : " + custom_Field_Values
				+ " in applied values for : " + custom_Field_Name);

	}
}
