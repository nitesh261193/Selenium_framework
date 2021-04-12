package com.merlin.dashboard.ui.assetmanager;

import static com.merlin.apps.locators.Asset_Manager_Locators.header_Row;
import static com.merlin.apps.locators.Asset_Manager_Locators.create_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.details_Pane_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.details_Title_Tab;
import static com.merlin.apps.locators.Asset_Manager_Locators.folder_Link_In_Bread_Crumb;
import static com.merlin.apps.locators.Asset_Manager_Locators.input_Box_For_Folder_Name;
import static com.merlin.apps.locators.Asset_Manager_Locators.message_Close_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.new_Folder_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.popup_Header;
import static com.merlin.apps.locators.Asset_Manager_Locators.save_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.search_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.search_Input_Box;
import static com.merlin.apps.locators.Asset_Manager_Locators.upload_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.upload_Button_In_Top_Pannel;
import static com.merlin.apps.locators.Asset_Manager_Locators.uploaded_File_Name;
import static com.merlin.apps.locators.Asset_Manager_Locators.search_Close_Button;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
public class Asset_Manager_Top_Pannel extends Annotation implements Init {

	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		navigate.Switch_To_Frame("fileSystemIFrame");
	}

	@Test(groups = { "Create_New_Folder" })
	@Parameters({ "create_Name" })
	public static void Create_New_Folder(String create_Name) {
		create_Name = create_Name.replace("random", Constants.random_Number);

		Util.Report_Log(Status.INFO, "Creating folder : '" + create_Name + "'");
		visible.Wait_Until_Visible(60 , header_Row);
		if (visible.Is_Displayed(message_Close_Button))
			click.Webdriver_Click(message_Close_Button, true);
		click.Webdriver_Click(header_Row, false);
		click.Webdriver_Click(new_Folder_Button, true);
		Util.Report_Log(Status.INFO, "Successfully clicked New Folder button from top pannel");
		String header = text.Webdriver_Get_Text(popup_Header);
		Assert.assertEquals(header.toLowerCase(), "Create New Folder".toLowerCase(),
				"Create Folder popup does not opened.");

		Util.Report_Log(Status.INFO, "Create folder popup is opened.");

		sendkeys.Webdriver_Sendkeys(input_Box_For_Folder_Name, create_Name);
		click.Webdriver_Click(create_Button, true);
		Constants.time_Stamp = Util.Get_Current_Date_In_String_Format();

		Util.Report_Log(Status.INFO, "Folder '" + create_Name + "' created");
	}

	@Test(groups = { "Upload_File" })
	@Parameters({ "upload_Name" })
	public static void Upload_File(String upload_Name) throws Exception {
		int i = 0;
		if (visible.Is_Displayed(message_Close_Button))
			click.Webdriver_Click(message_Close_Button, true);
		click.Webdriver_Click(upload_Button_In_Top_Pannel, true);
		Util.Report_Log(Status.INFO, "Successfully clicked upload button from top pannel");

		Assert.assertEquals(text.Webdriver_Get_Text(popup_Header), "Upload File", "Upload popup does not opened.");
		Util.Report_Log(Status.INFO, "Verified that upload popup is opened.");

		for (String file_Name_To_Upload : upload_Name.split(",")) {

			File file = new File(Constants.input_Data_Folder_For_Asset_Manager + File.separator + file_Name_To_Upload);
			byte[] array = Files.readAllBytes(file.toPath());
			String pdfBase64 = new String(Base64.encodeBase64(array));

			Util.Report_Log(Status.INFO, "Uploading given file : " + file_Name_To_Upload);

			drop.uploadFileViaDropzone_V2(pdfBase64, file_Name_To_Upload, "my-awesome-dropzone");
			String xpath = uploaded_File_Name.replace("$file_Name", file_Name_To_Upload);

			if (webelement.Is_Present(By.xpath(xpath), 10))
				Util.Report_Log(Status.PASS,
						"Verified that file : " + file_Name_To_Upload + " is present in uploaded queue");
			else
				Util.Report_Log(Status.FAIL, file_Name_To_Upload + " File is not present in uploaded queue");
			i++;
		}
		click.Webdriver_Click(upload_Button, true);

		try {
			Asset_Manager_Util.Wait_Till_Loading_Locators_Disappear();
			List<WebElement> headers = webelement.Webelement_List(popup_Header);
			for (WebElement header : headers) {
				if (header.getText().equalsIgnoreCase("Image Tags")) {

					Asset_Manager_Util.Wait_Till_Loading_Locators_Disappear();
					click.Webdriver_Click(save_Button, true);

					Asset_Manager_Util.Wait_Till_Loading_Locators_Disappear();

				}
			}
		} catch (Exception e) {
			// if (Visible.Is_Displayed(alert_Message_For_Image_Tags))
			navigate.Webdriver_Refresh();

			if (i <= 1) {
				throw e;
			} else
				visible.Wait_For_Page_To_Load();

		}

	}

	@Test(groups = { "Navigate_To_Folder_Using_Bread_Crumb" })
	@Parameters({ "navigate_Folder_Name" })
	public static void Navigate_To_Root_Folder_Using_Bread_Crumb(String navigate_Folder_Name) {
		navigate_Folder_Name = navigate_Folder_Name.replace("random", Constants.random_Number);
		click.Webdriver_Click(By.xpath(folder_Link_In_Bread_Crumb.replace("$folder_Name", navigate_Folder_Name)), true);
		Asset_Manager_Util.Wait_Till_Loading_Locators_Disappear();
		Util.Report_Log(Status.PASS, "Navigated to  : " + navigate_Folder_Name + " Using bread crum link");
	}

	@Test(groups = { "Open_Details_Pane" })
	public static void Open_Details_Pane() {
		if (visible.Is_Displayed(message_Close_Button))
			click.Webdriver_Click(message_Close_Button, true);
		click.Webdriver_Click_By_Action(details_Pane_Button);
        visible.Wait_Until_Visible(60, details_Title_Tab);

		boolean flag = visible.Is_Displayed(details_Title_Tab);
		Assert.assertTrue(flag, "Details Tab not opened");
		Util.Report_Log(Status.PASS, "Verified that details Pane is opened");
	}

	@Test(groups = { "Close_Details_Pane" })
	public static void Close_Details_Pane() {
		if (visible.Is_Displayed(message_Close_Button))
			click.Webdriver_Click(message_Close_Button, true);
		visible.Wait_Until_Visible(10, details_Title_Tab);
		Assert.assertTrue(visible.Is_Displayed(details_Title_Tab), "Details Pane is already Opened");
		click.Webdriver_Javascipt_Click(details_Pane_Button);
		Assert.assertFalse(visible.Is_Displayed(details_Title_Tab), "Unable to close Details Pane");
		Util.Report_Log(Status.PASS, "Verified that details Pane is closed successfully");
	}

	@Test(groups = { "Search_Item" })
	@Parameters({ "item_Name" })
	public static void Search_Item(String item_Name) {
		item_Name = item_Name.replace("random", Constants.random_Number);
		navigate.Switch_To_Default_Frame();
		sendkeys.Webdriver_Sendkeys(search_Input_Box, item_Name);
		Asset_Manager_Util.Wait_Till_Loading_Locators_Disappear();
		click.Webdriver_Click(search_Button, true);
		Util.Report_Log(Status.INFO, "Searching given Item :: " + item_Name);
		navigate.Switch_To_Frame("fileSystemIFrame");
	}
	@Test(groups = { "Close_Search_Window" })
	public static void Close_Search_Window() {
		Util.Report_Log(Status.INFO, "Closing searchh result window.");

		click.Webdriver_Click(search_Close_Button, true);
		Util.Report_Log(Status.PASS, "Clicked on Search Close Button");
	}

}
