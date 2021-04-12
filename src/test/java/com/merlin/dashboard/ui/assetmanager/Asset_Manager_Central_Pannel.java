package com.merlin.dashboard.ui.assetmanager;

import static com.merlin.apps.locators.Asset_Manager_Locators.add_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.already_Existing_File_Error_Message;
import static com.merlin.apps.locators.Asset_Manager_Locators.cancel_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.collection_Name_To_Add_Item;
import static com.merlin.apps.locators.Asset_Manager_Locators.create_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.delete_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.get_File_Or_Folder_Using_Name;
import static com.merlin.apps.locators.Asset_Manager_Locators.get_Name_Of_Folder_Or_File;
import static com.merlin.apps.locators.Asset_Manager_Locators.get_Type_Value_Of_File_Folder;
import static com.merlin.apps.locators.Asset_Manager_Locators.input_Box_For_Folder_Name;
import static com.merlin.apps.locators.Asset_Manager_Locators.input_Box_For_Folder_Rename;
import static com.merlin.apps.locators.Asset_Manager_Locators.move_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.notify_Check_Box_For_Email;
import static com.merlin.apps.locators.Asset_Manager_Locators.notify_Input_Box_For_Email;
import static com.merlin.apps.locators.Asset_Manager_Locators.ok_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.popup_Header;
import static com.merlin.apps.locators.Asset_Manager_Locators.popup_Header_For_Send_Share;
import static com.merlin.apps.locators.Asset_Manager_Locators.preview_Window_Close_Button_For_File;
import static com.merlin.apps.locators.Asset_Manager_Locators.preview_Window_For_File;
import static com.merlin.apps.locators.Asset_Manager_Locators.rename_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.select_Suppliers_To_Send_File;
import static com.merlin.apps.locators.Asset_Manager_Locators.send_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.share_Email_Id_Input_Box;
import static com.merlin.apps.locators.Asset_Manager_Locators.warning_Message_For_Delete;
import static com.merlin.apps.locators.Asset_Manager_Locators.warning_Message_For_Send;
import static com.merlin.apps.locators.Asset_Manager_Locators.get_File_Or_Folder_Location_Using_Name;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.File_Utils;
import com.merlin.common.Init;
import com.merlin.common.Mail_Check;
import com.merlin.common.Util;
import com.merlin.webdriverutilities.Webelement;

/**
 * @author Krishna Saini
 *
 */
public class Asset_Manager_Central_Pannel extends Annotation implements Init {

	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		navigate.Switch_To_Frame("fileSystemIFrame");
	}

	@Test(groups = { "Verify_File_Or_Folder_Existence" })
	@Parameters({ "verify_Name" })
	public static void Verify_File_Or_Folder_Existence(String verify_Name) {
		Asset_Manager_Util.Wait_Till_Loading_Locators_Disappear();
		verify_Name = verify_Name.replace("random", Constants.random_Number);
		for (String name1 : verify_Name.split(",")) {
			String xpath = get_File_Or_Folder_Using_Name.replace("$File/Folder_Name", name1);
			By folder_Ele = By.xpath(xpath);
			visible.Is_Displayed(folder_Ele);
			visible.Scroll_To_Element(folder_Ele);

			if (visible.Is_Displayed(folder_Ele)) {
				String fetched_file = text
						.Webdriver_Get_Text(By.xpath(get_Name_Of_Folder_Or_File.replace("$File/Folder_Name", name1)));

				Assert.assertEquals(fetched_file, name1, "'" + name1 + "' folder/file does not exists.");
				Util.Report_Log(Status.PASS, "Verified that folder/file : '" + name1 + "' exists.");

			} else
				Util.Report_Log(Status.FAIL, "'" + name1 + "' folder/file does not exists.");
		}
	}

	@Test(groups = { "Open_Folder" })
	@Parameters({ "open_Name" })
	public static void Open_Folder(String open_Name) {
		open_Name = open_Name.replace("random", Constants.random_Number);
		String xpath = get_File_Or_Folder_Using_Name.replace("$File/Folder_Name", open_Name);
		Util.Report_Log(Status.INFO, "Opening folder : " + open_Name);
		By folder_Ele = By.xpath(xpath);
		visible.Scroll_To_Element(folder_Ele);
		click.Webdriver_Double_Click_By_Action(folder_Ele);
		boolean flag = Asset_Manager_Util.Verify_Active_Folder_Name_In_Bread_Crumb(open_Name);
		Assert.assertTrue(flag, "Unable to open folder : " + open_Name);
		Util.Report_Log(Status.PASS, "Verified that given folder : " + open_Name + " opned successfully.");

	}

	@Test(groups = { "Preview_File" })
	@Parameters({ "preview_Name" })
	public static void Preview_File(String preview_Name) {

		for (String file_Name_To_Preview : preview_Name.split(",")) {
			Asset_Manager_Util.Select_File_And_Perform_Action(file_Name_To_Preview, "preview");
			Asset_Manager_Util.Wait_Till_Loading_Locators_Disappear();

			Assert.assertTrue(
					visible.Is_Displayed(By.xpath(preview_Window_For_File.replace("$File_Name", file_Name_To_Preview))),
					"Preview window does not open for given file : " + file_Name_To_Preview);
			Util.Report_Log(Status.INFO, "Preview window opened for file : " + file_Name_To_Preview);
			boolean flag = Asset_Manager_Util.Verify_Selected_File_Preview(file_Name_To_Preview);
			Assert.assertTrue(flag, "Preview of the file : " + file_Name_To_Preview + " does not visible");
			Util.Report_Log(Status.PASS,
					preview_Name + " verified that Preview of the file : " + file_Name_To_Preview + " is visible");
			click.Webdriver_Click(
					By.xpath(preview_Window_Close_Button_For_File.replace("$File_Name", file_Name_To_Preview)), true);
		}

	}

	@Test(groups = { "Open_File" })
	@Parameters({ "open_File_Name" })
	public static void Open_File(String open_File_Name) {
		open_File_Name = open_File_Name.replace("random", Constants.random_Number);
			for (String file_Name_To_Open : open_File_Name.split(",")) {
				Asset_Manager_Util.Select_File_And_Perform_Action(file_Name_To_Open, "open");
				
				boolean flag =
				 Asset_Manager_Util.Validate_File_Opened_In_ProofCollaboration(file_Name_To_Open);
				 Assert.assertTrue(flag, file_Name_To_Open + " file does not open in Proof Collaboration tool");
				 Util.Report_Log(Status.PASS,
				 "Verified that : " + file_Name_To_Open + " file is opened in Proof Collaboartion tool");
			}
		
	}

	@Test(groups = { "Edit_File" })
	@Parameters({ "edit_File_Name" })
	public static void Edit_File(String edit_File_Name) throws Exception {
		edit_File_Name = edit_File_Name.replace("random", Constants.random_Number);
		for (String file_Name_To_Edit : edit_File_Name.split(",")) {
			try {
				Asset_Manager_Util.Select_File_And_Perform_Action(file_Name_To_Edit, "edit");

				boolean flag = Asset_Manager_Util.Verify_Selected_File_Editing(file_Name_To_Edit);
				Assert.assertTrue(flag, file_Name_To_Edit + " file does not open in respective edit mode");
				Util.Report_Log(Status.PASS,

						"Verified that : " + file_Name_To_Edit + " file is opened in respective edit mode");
			} catch (Exception e) {
				navigate.Switch_To_Tab(1);
				navigate.Switch_To_Frame("fileSystemIFrame");
				throw e;
			}
		}

	}

	@Test(groups = { "Copy_Folder_Or_File" })
	@Parameters({ "copy_Name" })
	public static void Copy_Folder_Or_File(String copy_Name) {
		copy_Name = copy_Name.replace("random", Constants.random_Number);

		Asset_Manager_Util.Select_File_And_Perform_Action(copy_Name, "copy");
		Util.Report_Log(Status.PASS, "Copy given file/folder : " + copy_Name);
	}

	@Test(groups = { "Rename_Folder_Or_File" })
	@Parameters({ "existing_Name", "name_To_Change" })
	public static void Rename_Folder_Or_File(String existing_Name, String name_To_Change) {
		existing_Name = existing_Name.replace("random", Constants.random_Number);
		name_To_Change = name_To_Change.replace("random", Constants.random_Number);
		visible.Pause(2);
		Asset_Manager_Util.Select_File_And_Perform_Action(existing_Name, "rename");
		Assert.assertEquals(text.Webdriver_Get_Text(popup_Header), "Rename", "Rename popup dos not opened");
		Util.Report_Log(Status.INFO, "Verified that rename popup is opened");

		sendkeys.Webdriver_Sendkeys(input_Box_For_Folder_Rename, name_To_Change);
		click.Webdriver_Click(rename_Button, true);
		Util.Report_Log(Status.PASS, "Renamed given file/folder : " + existing_Name + "to : " + name_To_Change);

	}

	@Test(groups = { "Download_Folder_Or_File" })
	@Parameters({ "download_Name" })
	public static void Download_Folder_Or_File(String download_Name) throws Exception {
		download_Name = download_Name.replace("random", Constants.random_Number);

		Asset_Manager_Util.Select_File_And_Perform_Action(download_Name, "download");

		boolean download_Status = File_Utils.Wait_File_To_Download(Constants.browser_Download_Folder, download_Name);

		Assert.assertTrue(download_Status, "Unable to verify that file/folder get downloaded");
		Util.Report_Log(Status.PASS, "Verified that file/folder downloaded sucessfully");
	}

	@Test(groups = { "Move_Folder_Or_File" })
	@Parameters({ "move_File_Name", "folder_Name_To_Move" })
	public static void Move_Folder_Or_File(String move_File_Name, String folder_Name_To_Move) {
		move_File_Name = move_File_Name.replace("random", Constants.random_Number);
		folder_Name_To_Move = folder_Name_To_Move.replace("random", Constants.random_Number);

		Asset_Manager_Util.Select_File_And_Perform_Action(move_File_Name, "move");
		Assert.assertEquals(text.Webdriver_Get_Text(popup_Header), "Move File", "Move popup does not opned");
		Util.Report_Log(Status.INFO, "Verified that move popup is opened");
		try {
			Asset_Manager_Util.Select_Folder_To_Move(folder_Name_To_Move);

			click.Webdriver_Click(move_Button, true);
			if (visible.Is_Displayed(already_Existing_File_Error_Message))
				click.Webdriver_Click(ok_Button, true);
			Util.Report_Log(Status.PASS, move_File_Name + " folder/file is moved to folder : " + folder_Name_To_Move);
		} catch (Exception e) {
			click.Webdriver_Click(cancel_Button, true);
		}

	}

	@Test(groups = { "Delete_File_Or_Folder" })
	@Parameters({ "delete_Name" })
	public static void Delete_File_Or_Folder(String delete_Name) {
		delete_Name = delete_Name.replace("random", Constants.random_Number);
		String expected_Warning_message = "Are you sure you want to permanently delete this item?";
		Asset_Manager_Util.Select_File_And_Perform_Action(delete_Name, "remove");
		if (visible.Is_Displayed(warning_Message_For_Delete)) {
			String warning_Message_Text = text.Webdriver_Get_Text(warning_Message_For_Delete);

			if (warning_Message_Text.equalsIgnoreCase(expected_Warning_message)
					|| warning_Message_Text.contains(expected_Warning_message)) {
				Util.Report_Log(Status.INFO, "Verified delete warning messgae : " + warning_Message_Text);
			} else
				Util.Report_Log(Status.FAIL, "Unable to verify delete warning messgae, expected message : "
						+ expected_Warning_message + " and received message : " + warning_Message_Text);

		}
		visible.Wait_Until_Visible(3, delete_Button);
		click.Webdriver_Click(delete_Button, true);
		Asset_Manager_Util.Wait_Till_Loading_Locators_Disappear();
		boolean flag_delete = visible
				.Is_Displayed(By.xpath(get_File_Or_Folder_Using_Name.replace("$File/Folder_Name", delete_Name)));
		Assert.assertFalse(flag_delete, "Unable to delete file/folder : " + delete_Name);
		Util.Report_Log(Status.PASS, delete_Name + " file/folder deleted.");

	}

	@Test(groups = { "Create_Collection" })
	@Parameters({ "file_Name_To_Add_In_Collection", "collection_Name" })
	public static void Create_Collection(String file_Name, String collection_Name) {
		file_Name = file_Name.replace("random", Constants.random_Number);
		collection_Name = collection_Name.replace("random", Constants.random_Number);

		Util.Report_Log(Status.INFO, "Creating Collection : " + collection_Name + " with file : " + file_Name);

		Asset_Manager_Util.Select_File_And_Perform_Action(file_Name, "create collection");
		String header = text.Webdriver_Get_Text(popup_Header);
		Assert.assertEquals(header.toLowerCase(), "Create Collection".toLowerCase(),
				"Create Collection popup does not opened.");

		Util.Report_Log(Status.INFO, "Create Collection popup is opened.");

		sendkeys.Webdriver_Sendkeys(input_Box_For_Folder_Name, collection_Name);
		click.Webdriver_Click(create_Button, true);
		Util.Report_Log(Status.INFO, "Collection " + collection_Name + " created");
	}

	@Test(groups = { "Add_Folder_Or_File_To_Collection" })
	@Parameters({ "file_Name_To_Add_In_Collection", "collection_Name_To_ADD" })
	public static void Add_Folder_Or_File_To_Collection(String name, String collection_Name_To_ADD) {
		name = name.replace("random", Constants.random_Number);
		collection_Name_To_ADD = collection_Name_To_ADD.replace("random", Constants.random_Number);
		Asset_Manager_Util.Select_File_And_Perform_Action(name, "add to collection");
		Assert.assertEquals(text.Webdriver_Get_Text(popup_Header), "Add To Collection",
				"Add To Collection popup does not opned");
		Util.Report_Log(Status.INFO, "Verified that Add To Collection popup is opened");

		click.Webdriver_Click(By.xpath(collection_Name_To_Add_Item.replace("$collection_Name", collection_Name_To_ADD)),
				true);

		click.Webdriver_Click(add_Button, true);
		Util.Report_Log(Status.PASS, name + " folder/file is added to collection : " + collection_Name_To_ADD);
	}

	@Test(groups = { "Send_Folder_Or_File" })
	@Parameters({ "file_Name_To_Send", "supplier_Name", "email_Id" })
	public static void Send_Folder_Or_File(String file_Name_To_Send, String supplier_Name, String email_Id) {
		file_Name_To_Send = file_Name_To_Send.replace("random", Constants.random_Number);
		String warning_Message = "Compress and send " + file_Name_To_Send
				+ " and all enclosed files to the following suppliers?";
		Asset_Manager_Util.Select_File_And_Perform_Action(file_Name_To_Send, "send");
		Assert.assertEquals(text.Webdriver_Get_Text(popup_Header_For_Send_Share), "Send File",
				"Send File popup does not opned");
		Util.Report_Log(Status.INFO, "Verified that Send File popup is opened");
		click.Webdriver_Click_By_Action(
				By.xpath(select_Suppliers_To_Send_File.replace("$supplier_Name", supplier_Name)));
		click.Webdriver_Click_By_Action(notify_Check_Box_For_Email);
		sendkeys.Webdriver_Sendkeys(notify_Input_Box_For_Email, email_Id);
		click.Webdriver_Click(send_Button, true);
		if (visible.Is_Displayed(warning_Message_For_Send)) {
			if (text.Webdriver_Get_Text(warning_Message_For_Send).equalsIgnoreCase(warning_Message))
				Util.Report_Log(Status.INFO, "Verified message : " + warning_Message);
			else
				Util.Report_Log(Status.FAIL, "Message : " + warning_Message + "does not verified");
			click.Webdriver_Click(send_Button, true);
		}
	}

	@Test(groups = { "Share_Folder_Or_File" })
	@Parameters({ "file_Name_To_Share", "email_Id" })
	public static void Share_Folder_Or_File(String name, String email_Id) {
		name = name.replace("random", Constants.random_Number);
		Asset_Manager_Util.Select_File_And_Perform_Action(name, "share");
		Assert.assertEquals(text.Webdriver_Get_Text(popup_Header), "Share Link", "Share popup does not opned");
		Util.Report_Log(Status.INFO, "Verified that Share popup is opened");
		sendkeys.Webdriver_Sendkeys(share_Email_Id_Input_Box, email_Id);
		click.Webdriver_Click(send_Button, true);
	}

	@Test(groups = { "Check_Mail" })
	@Parameters({ "email_Id", "email_Password", "email_Subject", "email_Message", "item_Name" })
	public static void Check_Mail(String email_Id, String email_Password, String email_Subject, String email_Message,
			@Optional String item_Name) throws MessagingException, IOException {
		Mail_Check.Wait_For_Mail_To_Recieve(email_Id, email_Password, email_Subject, 20);
		boolean message_Recieved = Mail_Check.Check_Mail_Recieved(email_Id, email_Password, email_Subject,
				email_Message, item_Name);
		Assert.assertTrue(message_Recieved, "The mail not recieved on email address-" + email_Id);
		Util.Report_Log(Status.PASS, "Verified that mail has been recieved on email address with subject  " + email_Id);
		Mail_Check.Mark_All_Mail_Read(email_Id, email_Password);
	}

	@Test(groups = { "Unzip_Here" })
	@Parameters({ "file_Name_For_Unzip" })
	public static void Unzip_Here(String file_Name) {
		Asset_Manager_Util.Select_File_And_Perform_Action(file_Name, "unzip here");
		Util.Report_Log(Status.PASS, "Unziping given File : " + file_Name + " using unzip here");
	}

	@Test(groups = { "Unzip_In_New_Folder" })
	@Parameters({ "file_Name_For_Unzip" })
	public static void Unzip_In_New_Folder(String file_Name) {
		Asset_Manager_Util.Select_File_And_Perform_Action(file_Name, "unzip in new folder");
		Util.Report_Log(Status.PASS, "Unziping given File : " + file_Name + " using unzip in new folder");
	}

	@Test(groups = { "Unzip_File" })
	@Parameters({ "file_Name_For_Unzip", "unziping_File_Action", "unzipped_Folder_Created" })
	public static void Unzip_File(String file_Name, String unziping_File_Action, String unzipped_Folder_Created) {
		Asset_Manager_Util.Select_File_And_Perform_Action(file_Name, unziping_File_Action);
		boolean unzipped_Folder = Asset_Manager_Util.Get_Unzipped_Folder(unzipped_Folder_Created);
		Assert.assertTrue(unzipped_Folder, "unexpected Unzipped folder found-" + unzipped_Folder_Created);
		visible.Pause(4);
		Open_Folder(unzipped_Folder_Created);
		Util.Report_Log(Status.PASS, "File Unzipped successfully with folder name as -" + unzipped_Folder_Created);
		if (unziping_File_Action.equalsIgnoreCase("unzip in new folder")) {
			Util.Report_Log(Status.INFO, "File Unzipped successfully as" + unziping_File_Action
					+ " so opening new Folder " + unzipped_Folder);
			Open_Folder(unzipped_Folder_Created.substring(0, unzipped_Folder_Created.lastIndexOf(" Copy")));
			Util.Report_Log(Status.PASS, "File Unzipped successfully in new folder name as -" + unzipped_Folder);
		}
	}

	@Test(groups = { "Validate_Folder_Content" })
	@Parameters({ "folder_Name", "folder_Content_To_Validate", "sub_Folder_Content_To_Validate",
			"unzipped_Folder_Created" })
	public static void Validate_Folder_Content(String folder_Name, String folder_Content_To_Validate,
			String sub_Folder_Content_To_Validate, String unzipped_Folder_Created) {
		boolean folder_content_Found = false;
		boolean sub_Folder_Content_Found = false;
		String sub_Folder_Name = null;
		String file_Type = null;
		String xpath;
		for (String fille_Folder : folder_Content_To_Validate.split(",")) {
			xpath = get_File_Or_Folder_Using_Name.replace("$File/Folder_Name", fille_Folder);
			folder_content_Found = Asset_Manager_Util.Verify_Existence_Of_File_Folder(By.xpath(xpath));
			Assert.assertTrue(folder_content_Found, fille_Folder + " not found in the folder content");
			file_Type = text.Javascript_Get_Inner_Text(
					By.xpath(get_Type_Value_Of_File_Folder.replace("$File/Folder_Name", fille_Folder)));
			if (file_Type.contains("STANDARD_FOLDER")) {
				sub_Folder_Name = fille_Folder;
			}
		}
		Open_Folder(sub_Folder_Name);
		xpath = get_File_Or_Folder_Using_Name.replace("$File/Folder_Name", sub_Folder_Content_To_Validate);
		sub_Folder_Content_Found = Asset_Manager_Util.Verify_Existence_Of_File_Folder(By.xpath(xpath));
		Assert.assertTrue(sub_Folder_Content_Found, sub_Folder_Name + " not found in the sub folder content");
		Util.Report_Log(Status.PASS, "Successfully verified Folder and Sub Folder Content Present");
		Util.Report_Log(Status.INFO, "Deleting the unzipped File");
		Asset_Manager_Tree_View_Pannel.Navigate_To_Collections_Or_Folders("Folders");
		Open_Folder(folder_Name);
	}
	
	@Test(groups = { "Verify_File_Or_Folder_Location" })
	@Parameters({ "verify_Name" , "verify_Location" })
	public static void Verify_File_Or_Folder_Location(String verify_Name , String verify_Location) {
		Asset_Manager_Util.Wait_Till_Loading_Locators_Disappear();
		verify_Name = verify_Name.replace("random", Constants.random_Number);
		verify_Location = verify_Location.replace("random", Constants.random_Number);
		String xpath = get_File_Or_Folder_Location_Using_Name.replace("$File/Folder_Name", verify_Name) ;
		
		ArrayList<String> location_Names = text.Get_Elements_Text(By.xpath(xpath));

		boolean flag = location_Names.contains(verify_Location);
			

				Assert.assertTrue(flag , "Unable to verify given location : " + verify_Location+ " for file : " + verify_Name);
				Util.Report_Log(Status.PASS, "Verified given location : " + verify_Location+ " for file : " + verify_Name);

			
	}
	

}
