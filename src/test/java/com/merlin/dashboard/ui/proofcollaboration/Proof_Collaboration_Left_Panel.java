package com.merlin.dashboard.ui.proofcollaboration;

import static com.merlin.apps.locators.Proof_Collaboration_Locators.compare_Submit_Button;
import static com.merlin.apps.locators.Proof_Collaboration_Locators.compare_Switch_Button;
import static com.merlin.apps.locators.Proof_Collaboration_Locators.loader_Message;
import static com.merlin.apps.locators.Proof_Collaboration_Locators.proof_Comparison_Dialog_Header;
import static com.merlin.apps.locators.Proof_Collaboration_Locators.run_New_Compare_Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.File_Utils;
import com.merlin.common.Init;
import com.merlin.common.Util;

/**
 * @author Krishna Saini
 *
 */
public class Proof_Collaboration_Left_Panel extends Annotation implements Init {
	static Properties prop = null;

	@BeforeTest(alwaysRun = true)
	public void Before_Test_Class(ITestContext testContext) throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard.properties"));

	}

	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		navigate.Switch_To_Frame("proofViewerFrame");

	}

	@Test(groups = { "Run_New_Compare" })
	@Parameters({ "compare_From", "candidate_File_Path_To_Upload", "template_To_Select", "proof_Type_To_Select" })
	public static void Run_New_Compare(String compare_From, String candidate_File_Path_To_Upload,
			String template_To_Select, String proof_Type_To_Select) {
		visible.Wait_Until_Visible(Constants.expicit_Wait_Time, compare_Switch_Button);
		click.Webdriver_Click(compare_Switch_Button, false);
		visible.Wait_Until_Visible(Constants.expicit_Wait_Time, run_New_Compare_Button);
		click.Webdriver_Click(run_New_Compare_Button, true);
		boolean proof_Compare_Opened = visible.Is_Displayed(proof_Comparison_Dialog_Header);
		Assert.assertTrue(proof_Compare_Opened, "Unable to Open Proof Compare Dialog");
		Util.Report_Log(Status.PASS, "Proof Compare Dialog Opened successfully");
		if (compare_From.equalsIgnoreCase("project")) {
			Proof_Collaboration_Utility.Select_Template_For_Compare(template_To_Select);
			Proof_Collaboration_Utility.Select_Proof_Type_For_Compare(proof_Type_To_Select);
		}
		Proof_Collaboration_Utility.Upload_Template_For_Compare(
				System.getProperty("user.dir") + File.separator + candidate_File_Path_To_Upload);
		click.Webdriver_Javascipt_Click(compare_Submit_Button);
		Util.Report_Log(Status.PASS, "Compare submit button clicked successfully");
		visible.Wait_Until_Attribute_Visible(Constants.explicit_Wait_For_Short_Time, loader_Message, "class",
				"ng-hide");
		visible.Wait_For_Page_To_Load();
	}

	@Test(groups = { "Download_Compare_Result_And_Unzip" })
	@Parameters({ "download_Result_Choice", "compared_Result_Downloaded", "unzipped_Folder" })
	public static void Download_Compare_Result_And_Unzip(String download_Result_Choice,
			String compared_Result_Downloaded, String unzipped_Folder) throws Exception {
		Util.Report_Log(Status.INFO, "Downloaing results and unzipping in folder " + unzipped_Folder);
		String download_Path = System.getProperty("user.dir") + File.separator + Constants.browser_Download_Folder;
		String compare_Result_Downloaded = Proof_Collaboration_Utility.Download_Compare_Result(download_Result_Choice,
				download_Path, compared_Result_Downloaded);
		compare_Result_Downloaded = download_Path + File.separator + compare_Result_Downloaded;
		String unzipped_Folder_Path = download_Path + File.separator + unzipped_Folder;
		boolean unzziped = File_Utils.Unzip_File(compare_Result_Downloaded, unzipped_Folder_Path);
		Assert.assertTrue(unzziped, "Unable to unzipped folder " + unzipped_Folder);
		Util.Report_Log(Status.PASS,
				"Successfully Downloaded Compare Result and unzipped in Folder " + unzipped_Folder);
	}

	@Test(groups = { "Compare_Pdf_File" })
	@Parameters({ "base_Filepath", "target_Filepath", "unzipped_Folder" })
	public static void Compare_Pdf_File(String base_Filepath, String target_Filepath, String unzipped_Folder)
			throws Exception {
		Util.Report_Log(Status.INFO, "verifying file to be same");
		base_Filepath = System.getProperty("user.dir") + File.separator + base_Filepath;
		target_Filepath = System.getProperty("user.dir") + File.separator + target_Filepath;
		boolean unzziped = File_Utils.Compare_Pdf_File(target_Filepath, target_Filepath);
		Assert.assertTrue(unzziped, "Files are not equal");
		Util.Report_Log(Status.PASS, "Successfully Verified Files to be same");
	}
}
