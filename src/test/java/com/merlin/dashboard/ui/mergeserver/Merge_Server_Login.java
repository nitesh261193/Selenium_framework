package com.merlin.dashboard.ui.mergeserver;

import static com.melin.apps.locators.Merge_Server_Locators.MergeServer_Login;
import static com.melin.apps.locators.Merge_Server_Locators.MergeServer_Logout;
import static com.melin.apps.locators.Merge_Server_Locators.MergeServer_Password_Id;
import static com.melin.apps.locators.Merge_Server_Locators.MergeServer_Profile_Button;
import static com.melin.apps.locators.Merge_Server_Locators.MergeServer_Username_Id;
import static com.merlin.common.Constants.merge_Server_Dashbaord_URL;
import static com.merlin.common.Constants.merge_Server_Password;
import static com.merlin.common.Constants.merge_Server_URL;
import static com.merlin.common.Constants.merge_Server_Username;
import static com.merlin.common.Dashboard_Constants.merge_Server_Login_count;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;


/**
 * @author Nitesh Gupta
 *
 */
public class Merge_Server_Login extends Annotation implements Init
{

	static Properties prop = null;
	
	
	@BeforeTest(alwaysRun = true)
	public void Before_Test_Class(ITestContext testContext) throws IOException{
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard.properties"));	
		
	}
	
	@Test(groups = {"Open_Merge_Server"})
	public static void Open_Merge_Server() {

		navigate.Webdriver_Navigate(merge_Server_URL);
		if ((merge_Server_Login_count == 0)) {
			Login();
		} else {
			navigate.Webdriver_Navigate(merge_Server_Dashbaord_URL);
		}
		Util.Report_Log(Status.INFO,"Dashboard Url : " + navigate.Webdriver_Get_Current_Url());
		Assert.assertEquals(navigate.Webdriver_Get_Current_Url(),merge_Server_Dashbaord_URL,"Merge Server is not logged in");
	}
	
	@Test(groups = { "Login" })
	public static void Login()
	{
		visible.Wait_Until_Visible(60,MergeServer_Username_Id);
		sendkeys.Webdriver_Sendkeys(MergeServer_Username_Id, merge_Server_Username);
		sendkeys.Webdriver_Sendkeys(MergeServer_Password_Id,merge_Server_Password);
		click.Webdriver_Click(MergeServer_Login, true);
		Assert.assertEquals(navigate.Webdriver_Get_Current_Url().contains("mergeserver/#/dashboard"),true,"Merge Server is not logged in");
		Util.Report_Log(Status.PASS, "Login Successfully with Username : " + Constants.login_Username + " & Password : " + Constants.login_Password);
		merge_Server_Login_count++;
	}

	@Test(groups = {"Logout_Merge_Server"})
	public static void Logout_Merge_Server() {
		click.Webdriver_Click(MergeServer_Profile_Button,true);
		Util.Report_Log(Status.INFO,"log Out tab is opened");
		click.Webdriver_Click_By_Action_WithMoveTo(MergeServer_Logout);
		Util.Report_Log(Status.INFO,"Merge Server logout successfully");
	}
	


}
