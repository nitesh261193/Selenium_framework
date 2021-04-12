package com.merlin.dashboard.ui.proofcollaboration;

import static com.merlin.apps.locators.Proof_Collaboration_Locators.proof_Collaboration_Exit_Button;
import static com.merlin.common.Constants.*;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
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
public class Proof_Collaboration_Top_Panel extends Annotation implements Init {
	static Properties prop = null;

	@BeforeTest(alwaysRun = true)
	public void Before_Test_Class(ITestContext testContext) throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(locator_Path + "Dashboard.properties"));

	}

	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		if(reg_Name.contains("406"))
			navigate.Switch_To_Frame("proofViewerframe");
			else
				navigate.Switch_To_Frame("proofViewerFrame");


	}

	@Test(groups = { "Click_On_Proof_Collaboration_Exit_Button" })
	public static void Click_On_Proof_Collaboration_Exit_Button() {
		click.Webdriver_Javascipt_Click_Without_Wait(proof_Collaboration_Exit_Button);
		Util.Report_Log(Status.INFO, "Proof Collaboration is closed successfully");
		navigate.Switch_To_Default_Frame();
	}

	@Test(groups = { "Download_Proof_And_Verify_Comments" })
	@Parameters({ "comment_Type", "added_Comment_Text", "downloaded_Proof_Name" })
	public static void Download_Proof_And_Verify_Comments(String comment_Type, String added_Comment_Text,
			String downloaded_Proof_Name) throws Exception {
		String download_Path = System.getProperty("user.dir") + File.separator + browser_Download_Folder;
		Proof_Collaboration_Utility.Download_Proof(downloaded_Proof_Name, download_Path);
		boolean comment_Found = Proof_Collaboration_Utility.Verify_Comment_In_Downloaded_Proof(downloaded_Proof_Name,
				added_Comment_Text, download_Path);
		assertTrue(comment_Found, "Comment not found as expected in downloaded Pdf");
		Util.Report_Log(Status.PASS, "Verified that Comments Present in downloaded proof");
	}

}
