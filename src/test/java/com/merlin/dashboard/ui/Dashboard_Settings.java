package com.merlin.dashboard.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.merlin.confluence.Confluence_Page;
import com.aventstack.extentreports.Status;

/**
 * @author Pooja Chopra
 *
 */

public class Dashboard_Settings extends Annotation implements Init {
	static Properties prop = null;

	@BeforeTest(alwaysRun = true)
	public void Before_Test_Class(ITestContext test_Context) throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard.properties"));

	}

	@Test(groups = { "Set_App_Version" })
	@Parameters({ "app_Name", "script" })
	public void Set_App_Version(String app_Name, @Optional Boolean script) {
		script = script != null && script;
		Constants.app_Name = app_Name;
		String release_Env = confluence_Page.get_Release_Env(Constants.release_Number);
		Util.Report_Log(Status.INFO, "current release is on - " + release_Env);
		Boolean set_Version  = get_Version(release_Env, app_Name, script);
		if(release_Env.equals("DEV") || set_Version)
		{
			set_Version_Value(app_Name);
			if(script)
			{
				set_Version_Value(app_Name+ " Script");
			}
		}
	}

	
	public Boolean get_Version(String release_Env, String app_Name, Boolean Script)  {

		Boolean set_Version = false;
		if (Constants.app_Version.isEmpty()) 
		{
			if(release_Env.equals("DEV"))
			{
				Util.Report_Log(Status.INFO, "Fetching version from Merlin Release page as it is on dev");
				Constants.app_Version = Confluence_Page.Fetch_Versions_Confluence_Page(Constants.merlin_Release_Confluence_Page_URL, get_Sign_Off_App_Name( app_Name));
				if(Script)
				{
					Util.Report_Log(Status.INFO, "Fetching script version from Merlin Release page as it is on dev");
					Constants.app_Version_Script = Confluence_Page.Fetch_Versions_Confluence_Page(Constants.merlin_Release_Confluence_Page_URL, get_Sign_Off_App_Name( app_Name + " Script"));
				}
			}
			else
			{
				Util.Report_Log(Status.INFO, "Fetching the version as current release is in UAT");
				Constants.app_Version = get_Version_Value(app_Name);
				if(Script)
				{
					Util.Report_Log(Status.INFO, "Fetching the script version as current release is in UAT");
					Constants.app_Version_Script = get_Version_Value(app_Name + " Script");
				}
			}
		}
		else 
		{
			Util.Report_Log(Status.INFO, "version given from jenkins - "+ Constants.app_Version);
			Util.Report_Log(Status.INFO, "script version given from jenkins - "+ Constants.app_Version_Script);
			set_Version = true;
		}
		return set_Version;
	}
	
	
	public void set_Version_Value(String app_Name) {
		String version_Value =  get_Version_Text(app_Name);
		Util.Report_Log(Status.INFO, "Version text that needs to be updated " + version_Value);
		String[] url_List = get_Dashboard_Setting_Url(app_Name).split(",");
		for(String url : url_List)
		{
			navigate.Webdriver_Navigate(url);
			Util.Report_Log(Status.INFO, "Navigating to dashboard settings page - " + url);
			By xpath = By.xpath(prop.getProperty("Settings_Qa_Input_Box"));
			sendkeys.Webdriver_Sendkeys(xpath,version_Value);
			click.Webdriver_Click(By.xpath(prop.getProperty("Settings_Update_Button")), true);
			String fetched_Version = get_Version_Value(app_Name);
			Assert.assertTrue(version_Value.contains(fetched_Version) , "Unable to verify version");
			Util.Report_Log(Status.INFO, "Version value updated successfullly" + fetched_Version);
		}
	}
	
	public String get_Version_Value(String app_Name) {
		String[] url_List = get_Dashboard_Setting_Url(app_Name).split(",");
		String fetched_Version = "";
		navigate.Webdriver_Navigate(url_List[0]);
		By xpath = By.xpath(prop.getProperty("Settings_Qa_Input_Box"));
		 fetched_Version = text.Javascript_Get_Value(xpath);
		fetched_Version = "v" + fetched_Version.replaceAll("[^0-9_]", "");

		Util.Report_Log(Status.INFO, "Fetched version value : " + fetched_Version);
		return fetched_Version;
	}
	
	public static String get_Sign_Off_App_Name(String App_Name) {
		String sign_Off_App_Name="";
		switch(App_Name)
		{
		case "Asset Manager" : sign_Off_App_Name = "Asset-Manager" ; break;
		case "Content Editor" : sign_Off_App_Name = "Content-Editor" ;break;
		case "InDesign Editor" : sign_Off_App_Name = "InDesignEditor" ;break;
		case "InDesign Editor Script" : sign_Off_App_Name = "InDesign-Editor-Scripts" ;break;
		case "ProofCollab App" : sign_Off_App_Name = "ProofCollab-App" ;break;
		case "Html Editor" : sign_Off_App_Name = "HTML-Editor" ;break;
		case "Mergeserver" : sign_Off_App_Name = "MergerScripts" ;break;
		case "Exact Target": sign_Off_App_Name = "Email-Integration (ExactTarget)"  ;break;
		}
		Assert.assertFalse(sign_Off_App_Name.isEmpty(), "Unable to find app name on sign off for - " + App_Name);
		return sign_Off_App_Name ;
	}

	public String get_Dashboard_Setting_Url(String App_Name) {
		String setting_Url="";
		switch(App_Name)
		{
		case "Asset Manager" : setting_Url = Constants.dashboard_Setting_Asset_Manager_Url ;break;
		case "Content Editor" : setting_Url = Constants.dashboard_Setting_Content_Editor_Url ;break;
		case "InDesign Editor" : setting_Url = Constants.dashboard_Setting_INDD_EDITOR_URL ;break;
		case "InDesign Editor Script" : setting_Url = Constants.dashboard_Setting_INDD_EDITOR_Script_URL;break;
		case "ProofCollab App" : setting_Url = Constants.dashboard_Setting_Proof_Collaboration_Url;break;
		case "Html Editor" : setting_Url = Constants.dashboard_Setting_HTML_Editor_Url;break;
		case "Mergeserver" : setting_Url = Constants.dashboard_Setting_Merger_Script_Url+","+Constants.dashboard_Setting_Merger_Script_Delimited_Url;break;
		case "Exact Target" : setting_Url = Constants.dashboard_Setting_Exact_Target_Url;break;
		}
		Assert.assertFalse(setting_Url.isEmpty(), "Unable to find settings url for - " + App_Name);
		return setting_Url ;
	}

	public static String get_Version_Text(String App_Name) {
		String version_Value="";
		switch(App_Name)
		{
		case "Asset Manager" : version_Value= "/fsapp/" + Constants.app_Version + "/#/fsapp" ;break;
		case "InDesign Editor" : version_Value= "/inddeditor/" + Constants.app_Version + "/#/editor" ;break;
		case "Content Editor" : version_Value= "/ceditor/" + Constants.app_Version + "/#/editor" ;break;
		case "InDesign Editor Script" : version_Value = Constants.app_Version_Script;break;
		case "ProofCollab App" : version_Value = "/viewer/"+Constants.app_Version+"/#/viewer";break;
		case "Html Editor" : version_Value =  "/editor/" + Constants.app_Version + "/#/Editor"; break;
		case "Mergeserver" : version_Value = Constants.app_Version; break;
		case "Exact Target": version_Value = Constants.app_Version; break;
		}
		Assert.assertFalse(version_Value.isEmpty(), "Unable to find version text for - " + App_Name);
		return version_Value;
	}

	public static String get_Id_From_Url(String url, String key )
	{
		String[] params = url.split("\\?")[1].split("&");
		HashMap<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			map.put(name, value);
		}
		Util.Report_Log(Status.PASS,key + " Id received as - " + map.get(key));
		return map.get(key);
	}
}
