package com.merlin.dashboard.ui.inddeditor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
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

/**
 * @author Nitesh Gupta
 *
 */
public class Indd_Center_Panel extends Annotation implements Init
{
	static Properties prop = null;

	@BeforeTest(alwaysRun = true)
	public void Before_Test_Class(ITestContext testContext) throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard.properties"));

	}
	
	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		navigate.Switch_To_Frame("editorViewerFrame");
	}

	@Test(groups = { "Select_TextFrame"})
	@Parameters({"textframe_Id"})
	public static void Select_TextFrame(@Optional String textframe_Id) {
		textframe_Id = (textframe_Id == null )? Dashboard_Constants.page_Item_Id : textframe_Id;
		click.Webdriver_Click_By_Action(By.xpath(prop.getProperty("Select_Frame").replace("pageItem_", textframe_Id)));
		Util.Report_Log(Status.PASS, "Selected text frame : "+textframe_Id+" successfully" );
	}

}
