package com.merlin.dashboard.ui.assetmanager;

import static com.merlin.apps.locators.Asset_Manager_Locators.active_Bread_Crumb;
import static com.merlin.apps.locators.Asset_Manager_Locators.center_Panel;
import static com.merlin.apps.locators.Asset_Manager_Locators.child_Folder_For_Move;
import static com.merlin.apps.locators.Asset_Manager_Locators.details_Pane_Field;
import static com.merlin.apps.locators.Asset_Manager_Locators.get_File_Or_Folder_Using_Name;
import static com.merlin.apps.locators.Asset_Manager_Locators.html_Editor_Close_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.image_File_Preview_Image;
import static com.merlin.apps.locators.Asset_Manager_Locators.indd_Editor_Document_Title_Text;
import static com.merlin.apps.locators.Asset_Manager_Locators.indd_Editor_Exit_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.indd_Editor_Loader;
import static com.merlin.apps.locators.Asset_Manager_Locators.indd_Editor_Warning_Popup;
import static com.merlin.apps.locators.Asset_Manager_Locators.indd_Editor_Warning_Popup_Close_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.indd_Editor_Warning_Popup_Continue_Button;
import static com.merlin.apps.locators.Asset_Manager_Locators.loading_Locators;
import static com.merlin.apps.locators.Asset_Manager_Locators.location_Table_Header;
import static com.merlin.apps.locators.Asset_Manager_Locators.nDrive_Div;
import static com.merlin.apps.locators.Asset_Manager_Locators.ndrive_Tree_View_Element;
import static com.merlin.apps.locators.Asset_Manager_Locators.pdf_File_Preview_Frame;
import static com.merlin.apps.locators.Asset_Manager_Locators.right_Click_Menu_Items;
import static com.merlin.apps.locators.Asset_Manager_Locators.root_Folder_For_Move;
import static com.merlin.apps.locators.Asset_Manager_Locators.tree_View_Childeren_Under_Ndrive;
import static com.merlin.apps.locators.Asset_Manager_Locators.indd_Editor_closing_Message;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.File_Utils;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.merlin.dashboard.ui.proofcollaboration.Proof_Collaboration_Center_Panel;
import com.merlin.dashboard.ui.proofcollaboration.Proof_Collaboration_Top_Panel;

/**
 * @author Krishna saini
 *
 */
public class Asset_Manager_Util extends Annotation implements Init {

	public static boolean Select_Given_Item(String item_Name) {
		String xpath = get_File_Or_Folder_Using_Name.replace("$File/Folder_Name", item_Name);
		By name_Ele = By.xpath(xpath);
		if (Verify_Existence_Of_File_Folder(name_Ele)) {
			visible.Scroll_Views(name_Ele);
			click.Webdriver_Click_By_Action(name_Ele);
			return true;
		} else
			return false;

	}

	public static void Select_Given_Right_Click_Option(String option_Name) {
		visible.Wait_For_Page_To_Load();
		By right_Click_Option = By.xpath(right_Click_Menu_Items.replace("$option_Name", option_Name));
		click.Webdriver_Click(right_Click_Option, true);

	}

	public static boolean Verify_Existence_Of_File_Folder(By name_Ele) {
		boolean flag = visible.Is_Displayed(name_Ele);

		return flag;
	}

	public static void Select_File_And_Perform_Action(String name, String action) {

		String action_Name = "";

		switch (action.toLowerCase()) {
		case "new folder":
			action_Name = "New Folder";
			break;
		case "upload":
			action_Name = "Upload";
			break;
		case "copy":
			action_Name = "Copy";
			break;
		case "move":
			action_Name = "Move";
			break;
		case "rename":
			action_Name = "Rename";
			break;
		case "remove":
			action_Name = "Remove";
			break;
		case "download":
			action_Name = "Download";
			break;
		case "send":
			action_Name = "Send";
			break;
		case "create collection":
			action_Name = "Create Collection";
			break;
		case "add to collection":
			action_Name = "Add To Collection";
			break;
		case "open":
			action_Name = "Open";
			break;
		case "edit":
			action_Name = "Edit";
			break;
		case "preview":
			action_Name = "Preview";
			break;
		case "share":
			action_Name = "Share";
			break;
		case "unzip here":
			action_Name = "Unzip Here";
			break;
		case "unzip in new folder":
			action_Name = "Unzip In New Folder";
			break;

		}
		if (name.isEmpty()) {
			if (webelement.Is_Present(location_Table_Header, 15))
				click.Webdriver_Context_Click(location_Table_Header);
			else
				click.Webdriver_Context_Click(center_Panel);
		} else {

			if (Select_Given_Item(name)) {
				String xpath = get_File_Or_Folder_Using_Name.replace("$File/Folder_Name", name);
				By name_Ele = By.xpath(xpath);
				click.Webdriver_Context_Click(name_Ele);
				Select_Given_Right_Click_Option(action_Name);

			}
		}

	}

	public static boolean Verify_Active_Folder_Name_In_Bread_Crumb(String folder_Name) {
		visible.Wait_For_Page_To_Load();
		visible.Pause(2);
		String activeFolder = text.Webdriver_Get_Text(active_Bread_Crumb);

        return activeFolder.equalsIgnoreCase(folder_Name);
	}

	public static void Wait_Till_Loading_Locators_Disappear() {
		visible.Wait_Until_Invisible(Constants.expicit_Wait_Time, loading_Locators);
	}

	public static void Select_Folder_To_Move(String folder_Name) {
		Wait_Till_Loading_Locators_Disappear();
		if (folder_Name.isEmpty() || folder_Name.equalsIgnoreCase("nDrive")) {
			click.Webdriver_Click(root_Folder_For_Move, true);
		} else {
			List<WebElement> child_Nodes = webelement.Webelement_List(child_Folder_For_Move);
			for (WebElement ele : child_Nodes) {
				System.out.println(ele.getText());
				System.out.print(folder_Name);
				if (ele.getText().trim().equalsIgnoreCase(folder_Name)) {
					ele.click();
					break;
				}

			}

		}

	}

	public static boolean Validate_File_Opened_In_ProofCollaboration(String file_Name) {
		navigate.Switch_To_Default_Frame();
		navigate.Switch_To_Tab(2, "true", "true");
		visible.Wait_For_Page_To_Load();
		navigate.Switch_To_Frame("proofViewerFrame");
		try {
			Proof_Collaboration_Center_Panel.Verify_Proof_Opened(file_Name);
			Proof_Collaboration_Top_Panel.Click_On_Proof_Collaboration_Exit_Button();
			navigate.Switch_To_Tab(1);

			navigate.Switch_To_Frame("fileSystemIFrame");
			return true;

		} catch (Exception e) {
			navigate.Switch_To_Tab(1);
			navigate.Switch_To_Frame("fileSystemIFrame");

			return false;
		}
	}

	public static boolean Verify_Selected_File_Preview(String file_Name) {
		String file_Type = File_Utils.Get_File_Type_Extension(file_Name);
		boolean file_Previewed = false;
		switch (file_Type) {
		case ".png":
		case ".jpg":
			file_Previewed = visible.Is_Displayed(image_File_Preview_Image);
			break;

		case ".pdf":
			file_Previewed = visible.Is_Displayed((pdf_File_Preview_Frame));
			break;
		default:

			break;
		}
		return file_Previewed;
	}

	public static boolean Verify_Selected_File_Editing(String file_Name) throws Exception {
		try {
			String file_Type = File_Utils.Get_File_Type_Extension(file_Name);
			boolean flag = false;
			if (file_Type.equalsIgnoreCase(".html")) {
				flag = Validate_File_Opened_In_Html_Editor(file_Name);
				click.Webdriver_Click_By_Action(html_Editor_Close_Button);
			} else if (file_Type.equalsIgnoreCase(".indd")) {
				flag = Validate_File_Opened_In_Indesign_Editor(file_Name);
				
				if(!flag)
					 throw new Exception("Unable to verify that indesign file opened");
				/*
				 * visible.Wait_Until_Attribute_visible(Constants.explicit_Wait_For_Short_Time,
				 * indd_Editor_Loader);
				 */ 
				click.Webdriver_Click(indd_Editor_Exit_Button ,false);
				String closing_Message =  text.Webdriver_Get_Text(indd_Editor_closing_Message);
				Assert.assertTrue(closing_Message.contains("closing"), "Unable to verify closing message for indd file");
				Util.Report_Log(Status.PASS, "Successfully verified closing message : "+closing_Message);
	
			}
			navigate.Switch_To_Tab(1);
			navigate.Switch_To_Frame("fileSystemIFrame");
			return flag;
		} catch (Exception e) {
			throw e;
		}
	}

	private static boolean Validate_File_Opened_In_Html_Editor(String file_Name) {
		try {
			navigate.Switch_To_Tab(2, "true", "true");
			visible.Wait_For_Page_To_Load();
			navigate.Switch_To_Frame("htmlEditorFrame");
			Util.Report_Log(Status.INFO, file_Name + " opened in html editor");

			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	private static boolean Validate_File_Opened_In_Indesign_Editor(String file_Name) {
		boolean flag = false;
		try {
			
			navigate.Switch_To_Tab(2, "true", "true");
			visible.Wait_For_Page_To_Load();
			navigate.Switch_To_Frame("editorViewerFrame");
			visible.Pause(2);
			visible.Wait_Until_Attribute_Visible(Constants.expicit_Wait_Time * 5, indd_Editor_Loader, "class",
					"ng-hide");
			Indesign_Handle_Warning_Popup("Warning");
			visible.Pause(2);
			visible.Wait_Until_Attribute_Visible(Constants.expicit_Wait_Time * 5, indd_Editor_Loader, "class",
					"ng-hide");

			Indesign_Handle_Warning_Popup("Logs");

			String xpath = indd_Editor_Document_Title_Text.replace("$title_Text",
					file_Name.substring(0, file_Name.lastIndexOf(".")));
		//	Assert.assertTrue(visible.Is_Displayed(By.xpath(xpath)),
		//			"Indesign file title is not as expected -" + file_Name);
			flag = visible.Is_Displayed(By.xpath(xpath));
            if(flag)
			Util.Report_Log(Status.INFO, file_Name + " opened in Indesign editor");
            else
            	Util.Report_Log(Status.FAIL, "Unable to verify file name : "+file_Name+" in indd editor");
		} catch (Exception e) {
			Util.Report_Log(Status.FAIL, "Unable to verify file Name : " + file_Name);
		}
		return flag;
	}

	private static void Indesign_Handle_Warning_Popup(String headerText) {
		String xpath = indd_Editor_Warning_Popup.replace("$Heading", headerText);
		boolean flag = visible.Is_Displayed(By.xpath(xpath));
		if (flag && headerText.equalsIgnoreCase("Warning")) {
			click.Webdriver_Click(indd_Editor_Warning_Popup_Continue_Button, true);
		}
		else if(flag) {
			click.Webdriver_Javascipt_Click(
					By.xpath(indd_Editor_Warning_Popup_Close_Button.replace("$Heading", headerText)));

			Util.Report_Log(Status.INFO, "Warning Popup pops out and handled");
		} else {
			Util.Report_Log(Status.INFO, "No Warning Popup pops out");
		}
	}

	public static boolean Expand_And_Tree_View() {
		click.Webdriver_Javascipt_Click(nDrive_Div);
		boolean xpanded = visible.Is_Displayed(tree_View_Childeren_Under_Ndrive);
		if (!xpanded) {
			click.Webdriver_Javascipt_Click(ndrive_Tree_View_Element);
			xpanded = true;
		}
		return xpanded;
	}

	public static boolean Get_Unzipped_Folder(String file_Name) {
		String unzipped_Folder_Name = file_Name.substring(0, file_Name.lastIndexOf("."));
		String xpath = get_File_Or_Folder_Using_Name.replace("$File/Folder_Name", unzipped_Folder_Name);
		By name_Ele = By.xpath(xpath);
		visible.Wait_Until_Attribute_Visible(Constants.expicit_Wait_Time, name_Ele, "class", "ng-hide");
		if (Verify_Existence_Of_File_Folder(name_Ele)) {
			visible.Scroll_Views(name_Ele);
			Util.Report_Log(Status.INFO, "extracted folder found with name as -" + file_Name);
			return true;
		} else {
			return false;
		}
	}

	public static boolean Validate_File_Folder_Details_In_Details_Pane(String verify_Location, String verify_Date)
			throws UnsupportedFlavorException, IOException {

		String xpath = details_Pane_Field.replace("$detail_Field", "Location");
		String location = text.Javascript_Get_Text(By.xpath(xpath));
		xpath = details_Pane_Field.replace("$detail_Field", "Created");
		String created_Date = text.Javascript_Get_Text(By.xpath(xpath));
		if (location.contains(verify_Location) && created_Date.contains(verify_Date)) {
			Util.Report_Log(Status.INFO, "Location and Date verified successfully to be as " + verify_Location + " and "
					+ verify_Date + " respectively");
			return true;
		} else {
			Util.Report_Log(Status.ERROR,
					"Location and Date are not as expected " + location + " and " + created_Date + " respectively");
			return false;
		}

	}
}
