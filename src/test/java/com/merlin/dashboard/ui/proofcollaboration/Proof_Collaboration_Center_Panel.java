package com.merlin.dashboard.ui.proofcollaboration;

import static com.melin.apps.locators.Indesign_Editor_Locators.random_String;
import static com.merlin.apps.locators.Proof_Collaboration_Locators.proof_Collaboration_Document_Title;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.Assert;
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
public class Proof_Collaboration_Center_Panel extends Annotation implements Init {
	static Properties prop = null;

	@BeforeTest(alwaysRun = true)
	public void Before_Test_Class(ITestContext testContext) throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard.properties"));

	}

	@BeforeClass(alwaysRun = true)
	public void Before_Class() {

		if(Constants.reg_Name.contains("406"))
			navigate.Switch_To_Frame("proofViewerframe");
			else
				navigate.Switch_To_Frame("proofViewerFrame");


	}

	@Test(groups = { "Verify_Proof_Opened" })
	@Parameters({ "proof_Name" })
	public static void Verify_Proof_Opened(String proof_Name) {
		String xpath = proof_Collaboration_Document_Title.replace("$title", proof_Name);
		visible.Wait_Until_Visible(Constants.expicit_Wait_Time, By.xpath(xpath));
		Assert.assertTrue(visible.Is_Displayed(By.xpath(xpath)), "Unable to open Proof");
		Util.Report_Log(Status.PASS, "Proof opened successfully");
	}

	@Test(groups = { "Verify_Text_On_PDF" })
	@Parameters({ "content_Value" })
	public static void Verify_Text_On_PDF(String content_Value) {
		Util.Report_Log(Status.INFO, "Verifying text in pdf viewer - " + content_Value);
		visible.Wait_Until_Visible(Constants.expicit_Wait_Time,
				By.xpath(random_String.replace("string", content_Value)));
		String viewer_text = text.Webdriver_Get_Text(By.xpath(random_String.replace("string", content_Value)));
		Assert.assertTrue(viewer_text.equalsIgnoreCase(content_Value), "Viewer does not contain expected text");
		Util.Report_Log(Status.PASS, "Correct PDF is displayed on proof Viewer");
	}

}
