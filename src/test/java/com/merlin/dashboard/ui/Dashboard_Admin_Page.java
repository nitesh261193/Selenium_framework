package com.merlin.dashboard.ui;

import static com.melin.apps.locators.Merge_Server_Locators.Code_input;
import static com.melin.apps.locators.Merge_Server_Locators.Job_Type;
import static com.melin.apps.locators.Merge_Server_Locators.Loader;
import static com.melin.apps.locators.Merge_Server_Locators.Status_Asset_Id;
import static com.merlin.common.Dashboard_Constants.Job_Id;
import static com.merlin.common.Dashboard_Constants.Last_Job_Id;
import static com.merlin.common.Dashboard_Constants.merge_Server_Job_Id;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.merlin.common.*;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

/**
 * @author Nitesh Gupta
 *
 */

public class Dashboard_Admin_Page extends Annotation implements Init {
	static Properties prop = null;

	@BeforeTest(alwaysRun = true)
	public void Before_Test_Class(ITestContext test_Context) throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard.properties"));

	}

	@Test(groups = { "Get_Last_JobId_Present_On_Admin_Page" })
	@Parameters({ "project_Name", "job_Type" })
	public static void Get_Last_JobId_Present_On_Admin_Page(String Project_Name, String job_Type) {
		String currenturl = navigate.Webdriver_Get_Current_Url();
		Util.Report_Log(Status.INFO, "GOING TO ADMIN PAGE TO FETCH THE LAST JOB ID PRESENT");
		navigate.Webdriver_Get(Constants.Dashboard_Setting_job_page);
		Last_Job_Id = text.Webdriver_Get_Text(By.xpath(prop.getProperty("Settings_JobAdminPage_JobId_Xpath")));
		Util.Report_Log(Status.PASS, "Last job id present on admin page is - " + Last_Job_Id);
		navigate.Webdriver_Get(currenturl);
	}

	@Test(groups = { "Get_JobId_Admin_Page" })
	@Parameters({ "project_Name", "job_Type" })
	public static void Get_JobId_Admin_Page(String Project_Name, String job_Type) {
		Util.Report_Log(Status.INFO, "GOING TO ADMIN PAGE TO FETCH THE  JOB ID PRESENT");
		navigate.Webdriver_Get(Constants.Dashboard_Setting_job_page);
		select.Webdriver_Select_Dropdown_List_By_Value(By.xpath(prop.getProperty("Job_Type")), job_Type);
		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("Project_Name")), Project_Name);
		click.Webdriver_Click(By.xpath(prop.getProperty("Filter_Button")), true);
		Job_Id = text.Webdriver_Get_Text(By.xpath(prop.getProperty("Settings_JobAdminPage_JobId_Xpath")));
		Util.Report_Log(Status.INFO, "Latest job id present on admin page is - " + Job_Id);
		Assert.assertEquals(Integer.parseInt(Job_Id) > Integer.parseInt(Last_Job_Id), true,
				"latest job is not greater than last job id ");
		Util.Report_Log(Status.PASS, "Latest job id present on admin page is - " + Job_Id + "and is greater than Last Job Id Fetched Before -"+ Last_Job_Id);
	}

	@Test(groups = { "Get_Merge_Server_Job_Id" })
	public static void Get_Merge_Server_Job_Id() {
		int count = 0;
		while (text.Webdriver_Get_Text(By.xpath(prop.getProperty("Job_Status").replace("Job_Id", Job_Id)))
				.equalsIgnoreCase("RUNNING") && count < 10) {
			navigate.Webdriver_Refresh();
			count++;
		}
		click.Webdriver_Click(By.xpath(prop.getProperty("View_Steps").replace("Job_Id", Job_Id)), true);
		Util.Report_Log(Status.INFO, "View steps is clicked");
		click.Webdriver_Click(By.xpath(prop.getProperty("View_All_Params")), true);
		Util.Report_Log(Status.INFO, "View_All_Params is clicked");
		sendkeys.Webdriver_Sendkeys_Enter(By.xpath(prop.getProperty("Search_jobID")), "jobId");
		visible.Wait_Until_Visible(90, By.xpath(prop.getProperty("job_ID")));
		merge_Server_Job_Id = text.Webdriver_Get_Text(By.xpath(prop.getProperty("job_ID")));
		Assert.assertEquals(merge_Server_Job_Id.contains("IntegraPerf"), true, "Asset ID is not fetched correctly ");
		Util.Report_Log(Status.PASS, "Asset ID is fetched : - " + merge_Server_Job_Id);

	}

	@Test(groups = { "Monitor_Publish_Job_Status_On_Ui" })
	@Parameters({ "job_Type", "job_Id" })
	public static void Monitor_Publish_Job_Status_On_Ui(String job_Type, @Optional String job_Id) {
		job_Id = (!Job_Id.equalsIgnoreCase("") ? Job_Id : job_Id);
		Util.Report_Log(Status.INFO, "Monitoring job status for Id-" + job_Id);
		Util.Report_Log(Status.INFO, "Applying Filter for job Id-" + job_Id);
		sendkeys.Webdriver_Sendkeys(By.xpath(prop.getProperty("Settings_JobadminPage_JobId_SearchBox")), job_Id);
		click.Webdriver_Click(By.xpath(prop.getProperty("Filter_Button")), true);
		Util.Report_Log(Status.INFO, "Wait for Job status received to be DONE");
		int count = 0;
		while (!text.Webdriver_Get_Text(By.xpath(prop.getProperty("Job_Status").replace("$Job_Id", job_Id)))
				.equalsIgnoreCase("DONE") && count < 12) {
			navigate.Webdriver_Refresh();
			Util.Wait_Until_Requests_Complete();
			visible.Pause(10);
			count++;
		}
		String job_Status = text
				.Webdriver_Get_Text(By.xpath(prop.getProperty("Job_Status").replace("$Job_Id", job_Id)));
		Assert.assertEquals(job_Status, "DONE", "Status is not as expected - " + "DONE");
		Util.Report_Log(Status.PASS, "Status is received after wait as "+job_Status);
	}
}
