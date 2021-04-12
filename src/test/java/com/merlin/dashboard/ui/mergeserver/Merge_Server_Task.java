package com.merlin.dashboard.ui.mergeserver;

import static com.melin.apps.locators.Merge_Server_Locators.Code_input;
import static com.melin.apps.locators.Merge_Server_Locators.Job_Tab;
import static com.melin.apps.locators.Merge_Server_Locators.Job_Type;
import static com.melin.apps.locators.Merge_Server_Locators.Loader;
import static com.melin.apps.locators.Merge_Server_Locators.Loader_Locator;
import static com.melin.apps.locators.Merge_Server_Locators.Master_Task_Tab;
import static com.melin.apps.locators.Merge_Server_Locators.Status_Asset_Id;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
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
public class Merge_Server_Task extends Annotation implements Init
{

	static Properties prop = null;
	
	
	@BeforeTest(alwaysRun = true)
	public void Before_Test_Class(ITestContext testContext) throws IOException{
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard.properties"));	
		
	}
	
	@Test(groups = { "Open_Master_Task"})
	public static void Open_Master_Task() {
		click.Webdriver_Click(Job_Tab,true);
		Util.Report_Log(Status.INFO,"Job tab is clicked ");
		click.Webdriver_Javascipt_Click(Master_Task_Tab);
		visible.Wait_Until_Attribute_Visible(20,Loader_Locator,"class","ng-hide");
		Assert.assertTrue(navigate.Webdriver_Get_Current_Url().contains("masterTasks"),"master task is opened");
		Util.Report_Log(Status.INFO,"Master task tab is clicked ");
	}



	@Test(groups = { "Monitor_Master_task_Job_Status"})
	@Parameters({"status","job_Type"})
	public static void Monitor_Master_task_Job_Status(String status,String job_Type) {
		String job_Id = (job_Type.equals("EDITOR"))?Dashboard_Constants.asset_Id : Dashboard_Constants.merge_Server_Job_Id;
		Util.Report_Log(Status.INFO,"Monitoring job status for code" + job_Id);
		sendkeys.Webdriver_Sendkeys_Enter(Code_input,job_Id);
		visible.Wait_Until_Attribute_Visible(30,Loader,"class", "ng-hide");
		String type=text.Webdriver_Get_Text(By.xpath(Job_Type.replace("asset_Id",job_Id)));
		Assert.assertEquals(type.equalsIgnoreCase(job_Type),true,"Job type is not " +job_Type);
		Util.Report_Log(Status.PASS,"Job type received as " + type);
		int count=0;
		while(!text.Webdriver_Get_Text(By.xpath(Status_Asset_Id.replace("asset_Id",job_Id))).equalsIgnoreCase(status) && count<10){
			navigate.Webdriver_Refresh();
			visible.Pause(10);
			count++;
		}
		Assert.assertEquals(text.Webdriver_Get_Text(By.xpath(Status_Asset_Id.replace("asset_Id",job_Id))),status,"Status is not as expected - "+ status);
		Util.Report_Log(Status.PASS,"Status is received as -" + status);
	}

}
