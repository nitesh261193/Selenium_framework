package com.merlin.dashboard.ui;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.merlin.common.*;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

/**
 * @author Krishna Saini
 *
 */

public class Dashboard_Metadata_Admin extends Annotation implements Init {
	static Properties prop = null;

	@BeforeTest(alwaysRun = true)
	public void Before_Test_Class(ITestContext test_Context) throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard_Metadata_Admin.properties"));
		

	}
	@BeforeMethod(alwaysRun = true)
	public void Before_Method() {
		navigate.Switch_To_Default_Frame();
		navigate.Switch_To_Tab(0);
		visible.Wait_For_Page_To_Load();
		Util.Report_Log(Status.INFO,"Navigating To tags section under Meta data admin Page");
		navigate.Webdriver_Get(Constants.Dashboard_Setting_Metadata_Admin_page);
		navigate.Switch_To_Frame("metadataAdminIFrame");
	}
	@AfterMethod(alwaysRun = true)
	public void After_Method(ITestContext test_Context) {
		navigate.Switch_To_Default_Frame();
		navigate.Switch_To_Tab(1);
		visible.Wait_For_Page_To_Load();

	}

	@Test(groups = { "Create_New_Tag" })
	@Parameters({"tag_Name" , "tag_Description" , "tag_Location"})
	public static void Create_New_Tag(String tag_Name , String tag_Description , String tag_Location ) {
		
			
		
		tag_Name = tag_Name.replace("random", Constants.random_Number);

		webelement.Is_Enabled(By.xpath(prop.getProperty("New_Tag_Button")));
		click.Webdriver_Javascipt_Click(By.xpath(prop.getProperty("New_Tag_Button")));
		Util.Report_Log(Status.INFO,"Clicked on create Tag button.");
		
		String header = text.Webdriver_Get_Text(By.xpath(prop.getProperty("Create_Tag_Header")));
		
		Assert.assertEquals((header.equalsIgnoreCase("Create Tags") || header.contains("Create Tags")),true , "Unable to verify that Create Tag section opened");
		Util.Report_Log(Status.INFO,"Create Tags section opened successfully.");

		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("Tag_Name_InputBox")),tag_Name);
		Util.Report_Log(Status.INFO,"Enter given : "+tag_Name + " in tag name input box");

		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("Tag_Description_InputBox")),tag_Description);
		Util.Report_Log(Status.INFO,"Enter given : "+tag_Description + " in tag decription input box");

		click.Webdriver_Click(By.xpath(prop.getProperty("Tag_Location").replace("$Location", tag_Location)),true);
		Util.Report_Log(Status.INFO,"Successfully select given location : " + tag_Location);

		click.Webdriver_Click(By.xpath(prop.getProperty("Save_Button")),true);
		Util.Report_Log(Status.INFO,"Successfully clicked save button");
		
		visible.Wait_Until_Visible(90,By.xpath(prop.getProperty("Active_Tag_By_Name").replace("$Tag_Name", tag_Name)));
		Assert.assertTrue(visible.Is_Displayed(By.xpath(prop.getProperty("Active_Tag_By_Name").replace("$Tag_Name", tag_Name))),"Unable to add new Tag : " + tag_Name);
		Util.Report_Log(Status.PASS,"Successfully verified that tag : " + tag_Name + " added");
		
		
	}
	@Test(groups = { "Retire_Tag" })
	@Parameters({"tag_Name"})
	public static void Retire_Tag(String tag_Name ) {
	
		
		tag_Name = tag_Name.replace("random", Constants.random_Number);

		visible.Wait_Until_Visible(90,By.xpath(prop.getProperty("Active_Tag_By_Name").replace("$Tag_Name", tag_Name)));
		click.Webdriver_Click(By.xpath(prop.getProperty("Active_Tag_By_Name").replace("$Tag_Name", tag_Name)), true);

		Util.Report_Log(Status.INFO,"clicked on tag : " + tag_Name+" to retire it.");

		
		visible.Wait_Until_Visible(90,By.xpath(prop.getProperty("Retire_Tag_Button")));
		click.Webdriver_Click(By.xpath(prop.getProperty("Retire_Tag_Button")), true);

		Util.Report_Log(Status.INFO,"clicked on retire tag button");
		
		visible.Wait_Until_Visible(90,By.xpath(prop.getProperty("Retire_Popup_Message")));
		String warning_Message = text.Webdriver_Get_Text(By.xpath(prop.getProperty("Retire_Popup_Message")));

		Util.Report_Log(Status.INFO," Warning Message : " + warning_Message);
		
		Assert.assertTrue(warning_Message.contains("Are you sure you want to retire this tag?"), "Unble to verify warning message");
		
		Util.Report_Log(Status.INFO,"Successfully verified warning message : " + warning_Message);
		
				
		click.Webdriver_Click(By.xpath(prop.getProperty("Retire_Button")),true);
				
		Assert.assertFalse(visible.Is_Displayed(By.xpath(prop.getProperty("Active_Tag_By_Name").replace("$Tag_Name", tag_Name))),"Unable to remove tag : " + tag_Name);
		Util.Report_Log(Status.PASS,"Successfully verified that tag : " + tag_Name + " removed");
		
	}
	
	
}




