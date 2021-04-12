package com.merlin.dashboard.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.merlin.confluence.Confluence_Page;

/**
 * @author Krishna Saini
 *
 */

public class Settings_Tests extends Annotation implements Init {
	private static final Logger log = Logger.getLogger(Settings_Tests.class);
	static WebDriver driver = null;
	static Properties prop = null;

	@BeforeTest(alwaysRun = true)
	public void Before_Test_Class(ITestContext test_Context) throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard.properties"));

	}

	public static String version_Value_For_Given_Url(String url, String action, String version) {
		String fetched_Version = "";
		navigate.Webdriver_Navigate(url);
		By xpath = By.xpath(prop.getProperty("Settings_Qa_Input_Box"));

		if (action.equalsIgnoreCase("update")) {
			sendkeys.Webdriver_Sendkeys(xpath, version);
			click.Webdriver_Click(By.xpath(prop.getProperty("Settings_Update_Button")), true);
			fetched_Version = version;
		} else if (action.equalsIgnoreCase("verify")) {
			boolean flag = false;
			fetched_Version = text.Javascript_Get_Value(xpath);
			log.info("fetched version value : " + fetched_Version);
            flag = fetched_Version.equalsIgnoreCase(version);
			Assert.assertTrue(flag, "Unable to verify version, expected version : " + version + " fetched version : "
					+ fetched_Version);
		} else if (action.equalsIgnoreCase("fetch")) {
			fetched_Version = text.Javascript_Get_Value(xpath);
			fetched_Version = "v" + fetched_Version.replaceAll("[^0-9]", "");
		}

		return fetched_Version;

	}

	@Test(groups = { "Set_Content_Editor_Version" })
	public static void Set_Content_Editor_Version() throws Exception {
		String version = "";
		Constants.app_Name = "Content Editor";

		String release_Env = confluence_Page.get_Release_Env(Constants.release_Number);
		Util.Report_Log(Status.INFO, "current release is on - " + release_Env);

		if (release_Env.equals("UAT")) {
			Util.Report_Log(Status.INFO, "Fetching the version as current release is in UAT");
			Constants.app_Version = version_Value_For_Given_Url(Constants.dashboard_Setting_Content_Editor_Url, "fetch",
					"");
			Assert.assertFalse(Constants.app_Version.isEmpty(),
					"Content Editor version is empty, Unable to fetch version from dashboard setting page");
			Util.Report_Log(Status.INFO, "Content Editor version fetched as -" + Constants.app_Version);
		} else {
			if (Constants.app_Version.isEmpty()) {
				Util.Report_Log(Status.INFO, "Fetching version from Merlin Release page as it is on dev");
				Constants.app_Version = Confluence_Page
						.Fetch_Versions_Confluence_Page(Constants.merlin_Release_Confluence_Page_URL, "Content-Editor");
				Assert.assertFalse(Constants.app_Version.isEmpty(),
						"Content Editor version is empty, either the column name or the column is removed from confluence.");
				Util.Report_Log(Status.INFO, "Content Editor version fetched as -" + Constants.app_Version);
			}
			version = "/ceditor/" + Constants.app_Version + "/#/editor";
            version_Value_For_Given_Url(Constants.dashboard_Setting_Content_Editor_Url, "update", version);
			Util.Report_Log(Status.INFO, "Content Editor version Set : " + version);
			version_Value_For_Given_Url(Constants.dashboard_Setting_Content_Editor_Url, "verify", version);
			Util.Report_Log(Status.INFO, "Content Editor version Verified : " + version);

		}
	}

	@Test(groups = { "Set_Asset_Manager_Version" })
	public static void Set_Asset_Manager_Version() throws Exception {
		String version = "";
		Constants.app_Name = "Asset Manager";

		String release_Env = confluence_Page.get_Release_Env(Constants.release_Number);
		Util.Report_Log(Status.INFO, "current release is on - " + release_Env);

		if (release_Env.equals("UAT")) {
			Util.Report_Log(Status.INFO, "Fetching the version as current release is in UAT");
			Constants.app_Version = version_Value_For_Given_Url(Constants.dashboard_Setting_Asset_Manager_Url, "fetch",
					"");
			Assert.assertFalse(Constants.app_Version.isEmpty(),
					"Asset Manager version is empty, Unable to fetch version from dashboard setting page");
			Util.Report_Log(Status.INFO, "Asset Manager version fetched as -" + Constants.app_Version);
		} else {
			if (Constants.app_Version.isEmpty()) {
				Util.Report_Log(Status.INFO, "Fetching version from Merlin Release page as it is on dev");
				Constants.app_Version = Confluence_Page
						.Fetch_Versions_Confluence_Page(Constants.merlin_Release_Confluence_Page_URL, "Asset-Manager");
				Assert.assertFalse(Constants.app_Version.isEmpty(),
						"Asset Manager version is empty, either the column name or the column is removed from confluence.");
				Util.Report_Log(Status.INFO, "Asset Manager version fetched as -" + Constants.app_Version);
			}
			version = "/fsapp/" + Constants.app_Version + "/#/fsapp";
			version_Value_For_Given_Url(Constants.dashboard_Setting_Asset_Manager_Url, "update", version);
			Util.Report_Log(Status.INFO, "Asset Manager version Set : " + version);
			version_Value_For_Given_Url(Constants.dashboard_Setting_Asset_Manager_Url, "verify", version);
			Util.Report_Log(Status.INFO, "Asset Manager version Verified : " + version);

		}

	}

	@Test(groups = { "Set_Indd_Editor_Version" })
	public static void Set_Indd_Editor_Version() throws Exception {
		String editor_Version = "";
		Constants.app_Name = "InDesign Editor";

		String release_Env = confluence_Page.get_Release_Env(Constants.release_Number);
		Util.Report_Log(Status.INFO, "current release is on - " + release_Env);

		if (release_Env.equals("UAT")) {
			Util.Report_Log(Status.INFO, "Fetching the version as current release is in UAT");
			Constants.app_Version = version_Value_For_Given_Url(Constants.dashboard_Setting_INDD_EDITOR_URL, "fetch",
					"");
			Constants.app_Version_Script = version_Value_For_Given_Url(
					Constants.dashboard_Setting_INDD_EDITOR_Script_URL, "fetch", "");
			Assert.assertFalse(Constants.app_Version.isEmpty(),
					"Indd Editor version is empty, Unable to fetch version from dashboard setting page");
			Assert.assertFalse(Constants.app_Version_Script.isEmpty(),
					"Indd Script version is empty, Unable to fetch version from dashboard setting page");
			Util.Report_Log(Status.INFO, "Indd Editor version fetched as -" + Constants.app_Version);
			Util.Report_Log(Status.INFO, "Indd Script version fetched as -" + Constants.app_Version_Script);

		} else {
			if (Constants.app_Version.isEmpty()) {
				Util.Report_Log(Status.INFO, "Fetching version from Merlin Release page as it is on dev");
				Constants.app_Version = Confluence_Page
						.Fetch_Versions_Confluence_Page(Constants.merlin_Release_Confluence_Page_URL, "InDesignEditor");
				Constants.app_Version_Script = Confluence_Page.Fetch_Versions_Confluence_Page(
						Constants.merlin_Release_Confluence_Page_URL, "InDesign-Editor-Scripts");

				Assert.assertFalse(Constants.app_Version.isEmpty(),
						"Indd editor version is empty, either the column name or the column is removed from confluence.");
				Assert.assertFalse(Constants.app_Version_Script.isEmpty(),
						"Indd editor script version is empty, either the column name or the column is removed from confluence.");
				Util.Report_Log(Status.INFO, "Indd editor version fetched as -" + Constants.app_Version);
				Util.Report_Log(Status.INFO, "Indd editor script version fetched as -" + Constants.app_Version_Script);

			}
			editor_Version = "/inddeditor/" + Constants.app_Version + "/#/editor";

			version_Value_For_Given_Url(Constants.dashboard_Setting_INDD_EDITOR_Script_URL, "update",
					Constants.app_Version_Script);
			Util.Report_Log(Status.INFO, "Indd script version Set : " + Constants.app_Version_Script);
			version_Value_For_Given_Url(Constants.dashboard_Setting_INDD_EDITOR_Script_URL, "verify",
					Constants.app_Version_Script);
			Util.Report_Log(Status.INFO, "Indd script version Verified : " + Constants.app_Version_Script);

			version_Value_For_Given_Url(Constants.dashboard_Setting_INDD_EDITOR_URL, "update", editor_Version);
			Util.Report_Log(Status.INFO, "Indd version Set : " + editor_Version);
			version_Value_For_Given_Url(Constants.dashboard_Setting_INDD_EDITOR_URL, "verify", editor_Version);
			Util.Report_Log(Status.INFO, "Indd version Verified : " + editor_Version);

		}
	}

}
