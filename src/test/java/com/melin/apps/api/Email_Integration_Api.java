package com.melin.apps.api;

import com.merlin.common.*;
import com.merlin.utils.Json_Parsor;
import com.merlin.utils.Rest_Api;
import com.merlin.utils.Yaml_Reader;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static com.merlin.common.Dashboard_Constants.*;

/**
 * @author Nitesh Gupta
 *
 */

public class Email_Integration_Api extends Annotation implements Init {

	Rest_Api rest = new Rest_Api();
	public static ExtentTest test;
	static Properties prop = null;

	public static ArrayList<String>Var_links= new ArrayList<>(Arrays.asList("segmentname","recordnumber","naehasid","naehaslistid","auditflag","audit_seq"));

	@BeforeMethod(alwaysRun = true)
	@Parameters({ "api_Name" })
	public void BeforeMethod(String api_Name) {
		Constants.header_Map.clear();
		Constants.form_Map.clear();
		rest.api_Name = api_Name;
		Rest_Api.config_Name = "EmailIntegration";
		rest.end_Point = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".endpoint")
				.replace("PROJECT_ID", Dashboard_Constants.project_Id).replace("TEMPLATE_ID", template_Id);
		rest.request_Body = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".requestBody");
		rest.status_Code = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".statusCode");
		Constants.header_Map.put("Accept", "application/json, text/plain, */*");
	}

	@BeforeClass(alwaysRun = true)
	public void BeforeClass(ITestContext testContext) throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard.properties"));

	}

	@Test(groups = { "Publish_Email" })
	@Parameters({ "project_Id", "option", "api_Name", "email_Address" })
	public void Publish_Email(String project_Id, String option, String api_Name, @Optional String email_Address) {
		Dashboard_Constants.project_Id = project_Id;
		BeforeMethod(api_Name);
		Constants.header_Map.clear();
		Util.Report_Log(Status.INFO, "Publishing Email with Publish Option as- " + option);
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("lpId", Dashboard_Constants.project_Id);
		Constants.form_Map.put("sendClassification", "Default Format");
		Constants.form_Map.put("senderProfile", "9");
		Constants.form_Map.put("deliveryProfile", "Default");
		Constants.form_Map.put("addToJob", "");
		Constants.form_Map.put("pub_publishTypeId", "1459");
		Constants.form_Map.put("ovride_vendorId", "430");
		if (!option.equalsIgnoreCase("deduplicateOption"))
			Constants.form_Map.put("deduplicateOption", "false");
		switch (option) {
		case "ExactTargetTestAddress":
			Util.Report_Log(Status.INFO, "Sending ExactTargetTestAddress Param as - " + email_Address);
			Constants.form_Map.put("ExactTargetTestAddress", email_Address);
			break;
		case "scheduledTime":
			String time = null;
			time = Util.Get_Time_In_CST("MM/dd/yyyy hh:mm a", 3);
			Constants.form_Map.put("scheduledTime", time);
			Dashboard_Constants.current_Date_In_CST = time;
			Util.Report_Log(Status.INFO, "Mail will be sent at - " + time);
			break;
		case "sendOnlyText":
			Constants.form_Map.put("sendOnlyText", "true");
			break;
		case "throttleOption":
			Constants.form_Map.put("throttleOption", "true");
			Util.Report_Log(Status.INFO, "Sending throttleStartTime Param as - "+Constants.throttle_tags.split(",")[0]);
			Constants.form_Map.put("throttleStartTime", Constants.throttle_tags.split(",")[0]);
			Util.Report_Log(Status.INFO, "Sending throttleEndTime Param as - "+Constants.throttle_tags.split(",")[1]);
			Constants.form_Map.put("throttleEndTime", Constants.throttle_tags.split(",")[1]);
			Util.Report_Log(Status.INFO, "Sending throttleLimit Param as - "+Constants.throttle_tags.split(",")[2]);
			Constants.form_Map.put("throttleLimit", Constants.throttle_tags.split(",")[2]);
			break;
		case "deduplicateOption":
			Constants.form_Map.put("deduplicateOption", "true");
			break;
		}
		rest.Post_Api("form_Url");
		visible.Pause(10);
		Util.Report_Log(Status.PASS, "Successfully Publish email Using API with publish Option -" + option);
	}


	@Test(groups = { "Monitor_Publish_Job_Status_Through_API" })
	@Parameters({ "project_Id"})
	public void Monitor_Publish_Job_Status_Through_API(String project_Id) throws Exception {
		Dashboard_Constants.project_Id = project_Id;
		Util.Report_Log(Status.INFO, "Wait Until Job status is Done through ApI");
		String response = "";
		BeforeMethod("listSuppliers_Api");
		response = rest.Get_Api();
		Job_Id = Json_Parsor.Get_Value_From_Response_For_Given_Field(response, "publishJobExecutions",
				"RUNNING or PENDING", "ID", "status");
		Assert.assertEquals(Integer.parseInt(Job_Id) > Integer.parseInt(Last_Job_Id), true,
				"New Publish Job id not created ");
		Util.Report_Log(Status.PASS, "Publish Job Id created as "+Job_Id);
		int count =0;
		while (true) {
			BeforeMethod("listSuppliers_Api");
			response = rest.Get_Api();
			Job_Status = Json_Parsor.Get_Value_From_Response_For_Given_Field(response, "publishJobExecutions",
					Job_Id, "status", "ID");
			if ((Job_Status.equalsIgnoreCase("Done") || Job_Status.equalsIgnoreCase("ERROR"))&& count<15) {
				break;
			}
			visible.Pause(10);
			count ++;
			continue;
		}
		Assert.assertTrue(!(Job_Status.equalsIgnoreCase("ERROR")), "Publish Job Status is error");
		Util.Report_Log(Status.PASS, "For Job Id - " + Job_Id + " and Job Status is -" + Job_Status +"after "+count*10 +"seconds");
	}

	@Test(groups = { "Get_Last_Job_ID_From_Response_through_API" })
	public void Get_Last_Job_ID_From_Response_through_API() throws Exception {
		BeforeMethod("listSuppliers_Api");
		String response = rest.Get_Api();
		Last_Job_Id = Json_Parsor.Get_Value_From_Response_For_Given_Field(response, "publishJobExecutions", "Publish",
				"ID", "type");
		Util.Report_Log(Status.PASS, "Last Job Id - " + Last_Job_Id + " is fetched from response");
	}

	@Test(groups = { "Get_Variable_link_List_Through_API" })
	public void Get_Variable_link_List_Through_API() throws Exception {
		BeforeMethod("variable_link_Api");
		String response = rest.Get_Api();
		ArrayList list_FromResponse=Json_Parsor.Fetch_All_Json_Array_For_Given_Field_From_Response_in_a_List(response,"variableLinks","name");
		Var_links.addAll(Var_links.size()-1,list_FromResponse);
		System.out.println(Var_links);
		Assert.assertTrue(!Var_links.isEmpty(), "No list fetched from api");
		Util.Report_Log(Status.PASS, "Variable links list coming from dashboard - <br />" + Var_links);
	}

}
