package com.melin.apps.api;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.merlin.dashboard.ui.Dashboard_Settings;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Dashboard_Constants;
import com.merlin.common.Util;
import com.merlin.utils.Json_Parsor;
import com.merlin.utils.Rest_Api;
import com.merlin.utils.Yaml_Reader;

/**
 * @author Pooja Chopra
 *
 */

public class Dashboard_Api_Util extends Annotation {

	Rest_Api rest = new Rest_Api();

	static public ArrayList<String> color_List = new ArrayList<>();
	static public ArrayList<String> style_List = new ArrayList<>();
	static public HashMap<String, ArrayList<String>> font_Map = new HashMap<String, ArrayList<String>>();

	@BeforeMethod(alwaysRun = true)
	@Parameters({  "api_Name" , "project_Id"})
	public void BeforeMethod( String api_Name,@Optional String project_Id) {
	   Constants.header_Map.clear();
	   Constants.form_Map.clear();
	   if(project_Id != null)
	   {
	      Dashboard_Constants.project_Id = project_Id;
	      Util.Report_Log(Status.INFO, "Project id - " + project_Id);
	   }
	   rest.api_Name = api_Name;
	   Rest_Api.config_Name = "DashboardApiUtil";
	   rest.end_Point = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".endpoint").replace("PROJECT_ID", Dashboard_Constants.project_Id)
	         .replace("TEMPLATE_ID", Dashboard_Constants.template_Id).replace("CB_ID", Dashboard_Constants.content_Block_Id).replace("TASK_ID", Dashboard_Constants.task_Id);
	   rest.request_Body = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".requestBody");
	   rest.status_Code = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".statusCode");
	   Constants.header_Map.put("Accept", "application/json, text/plain, */*");
	}


	@Test(groups = { "Get_Ids_For_Content_Editor" })
	@Parameters({ "project_Id", "template_Name", "content_Name" })
	public void Get_Ids_For_Content_Editor(String project_Id, String template_Name, String content_Name) {
		Dashboard_Constants.content_Name = content_Name;
		Dashboard_Constants.template_Id = Get_Template_Id(template_Name);
		Dashboard_Constants.content_Block_Id = Get_CB_Id(content_Name);
	}

	@Test(groups = { "Get_Template_Ids_For_HtmlEditor" })
	@Parameters({"project_Id","template_Name"})
	public void Get_Template_Ids_For_HtmlEditor(String project_Id, String template_Name) {
		Dashboard_Constants.project_Id = project_Id;
		Dashboard_Constants.template_Id = Get_Template_Id(template_Name);
		Util.Report_Log(Status.INFO, "Template id - " + Dashboard_Constants.template_Id);
	}


	@Test(groups = { "Get_Template_Ids_For_Merge_Server" })
	@Parameters({"project_Id","template_Name"})
	public void Get_Template_Ids_For_Merge_Server(String project_Id, String template_Name) {
		Dashboard_Constants.project_Id = project_Id;
		Dashboard_Constants.template_Id = Get_Template_Id(template_Name);
		Util.Report_Log(Status.INFO, "Template id - " + Dashboard_Constants.template_Id);
	}

	@Test(groups = { "Get_Template_Ids_For_Exact_target" })
	@Parameters({"project_Id","template_Name"})
	public void Get_Template_Ids_For_Exact_target(String project_Id, String template_Name) {
		Dashboard_Constants.project_Id = project_Id;
		Dashboard_Constants.template_Id = Get_Template_Id(template_Name);
		Util.Report_Log(Status.INFO, "Template id - " + Dashboard_Constants.template_Id);
	}


	public String Get_Template_Id(String name) {
		BeforeMethod("temp_info_API",null);
		String response = rest.Get_Api();
		String id = Json_Parsor.Get_Id_From_Json_Based_On_Value(response, "projectTemplates", "name", name);
		Util.Report_Log(Status.INFO, "Template id fetched from API - " + id);
		return id;
	}

	public String Get_TemplateFormat_Id(String name ) {
		BeforeMethod("temp_info_API",null);
		String response = rest.Get_Api();
		String id = Json_Parsor.Get_Value_Of_Key_From_Json_Based_On_Value(response, "projectTemplates", "name", name,"inddTemplateFormatId");
		Util.Report_Log(Status.INFO, "Template id fetched from API - " + id);
		return id;
	}

	public String Get_CB_Id(String content_Name) {
		BeforeMethod( "cb_info_API",null);
		String response = rest.Get_Api();
		JSONObject jsonObject = new JSONObject(response);
		JSONArray array = jsonObject.getJSONArray("aaData");
		Iterator<Object> iterator = array.iterator();
		String cb_Id = "", vl_Id = "";

		while (iterator.hasNext()) {
			JSONArray jsonObject1 = (JSONArray) iterator.next();
			String name = (jsonObject1.get(1)).toString();
			if (name.equals(content_Name)) {
				vl_Id = (jsonObject1.get(3)).toString();
				Dashboard_Constants.variable_Link_Id = vl_Id;
				cb_Id = (jsonObject1.get(5)).toString();
				break;
			}
		}
		Util.Report_Log(Status.INFO, "Content block id fetched from API - " + cb_Id);
		return cb_Id;
	}

	public String Get_Segment_Id(String segment_Name) throws UnsupportedEncodingException {
		BeforeMethod("segment_info_API",null);
		Constants.header_Map.put("Accept", "text/html,application/xhtml+xml,application/xml");
		String response = rest.Get_Api();
		Document document = Jsoup.parse(response);
		String segment_Url = document.getElementsByAttributeValue("id", "segment_name_"+segment_Name).attr("href");
//		String decode_URL=URLDecoder.decode( segment_Url.toString(), "UTF-8" );
//		String url_Json=decode_URL.substring(decode_URL.indexOf("{"),decode_URL.lastIndexOf("}")+1);
		String segment_Id = segment_Url.split("segmentId=")[1].split("&")[0];
		Dashboard_Constants.segment_Id = segment_Id;
		Util.Report_Log(Status.INFO, "Segment id fetched from API - " + segment_Id);
		return segment_Id;
	}

	@Test(groups = { "Copy_Template" })
	@Parameters({ "template_Name" , "copy_Template_Name", "segment_Name"})
	public void Copy_Template(String template_Name, String copy_Template_Name, String segment_Name) {
		copy_Template_Name = copy_Template_Name.replace("random", Constants.random_Number);
		String temp_Id = Get_Template_Id(template_Name);
		BeforeMethod("copy_template_API",null);
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("assignTemplate", "true");
		Constants.form_Map.put("lpId", Dashboard_Constants.project_Id);
		Constants.form_Map.put("templateId", temp_Id);
		Constants.form_Map.put("name", copy_Template_Name);
		rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "Copying template by post request");
		String copied_Template_Id = Get_Template_Id(copy_Template_Name);
		String templateFormat_Id=Get_TemplateFormat_Id(copy_Template_Name);
		Assert.assertTrue((copied_Template_Id != null), "Unable to fetch copied template Id");
		Dashboard_Constants.template_Id = copied_Template_Id;
		Dashboard_Constants.template_Name = copy_Template_Name;
		Dashboard_Constants.templateFormat_Id = templateFormat_Id;
		Util.Report_Log(Status.INFO, "Copied template name - "+ Dashboard_Constants.template_Name + " ID -- " + Dashboard_Constants.template_Id);
	}

	@Test(groups = { "Assign_Template_To_Segment" })
	@Parameters({ "segment_Name" , "template_Id","assign"})
	public void Assign_Template_To_Segment(String segment_Name,@Optional String template_Id, String assign) throws UnsupportedEncodingException {
		String segment_Id = Dashboard_Constants.segment_Id.isEmpty() ? Get_Segment_Id(segment_Name): Dashboard_Constants.segment_Id;
		BeforeMethod("assign_template_to_segment_API",null);
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("segmentId", segment_Id);
		Constants.form_Map.put("templateId", Dashboard_Constants.template_Id);
		Constants.form_Map.put("assign", assign);
		Constants.form_Map.put("csrf", "");
		rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "Copied template name - "+ Dashboard_Constants.template_Name + " ID -- " + Dashboard_Constants.template_Id);
	}

	@Test(groups = { "Remove_Template_Association_With_Project" })
	@Parameters({  "template_Id"})
	public void Remove_Template_Association_With_Project(@Optional String template_Id)  {
		template_Id = (template_Id == null) ? Dashboard_Constants.template_Id : template_Id;
		BeforeMethod("remove_template_association_with_project_API",null);
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("templateId", Dashboard_Constants.template_Id);
		Constants.form_Map.put("lpId", Dashboard_Constants.project_Id);
		rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "Removed template association with the project - " + Dashboard_Constants.template_Id);
	}
	
	@Test(groups = { "Get_Ids_For_Proof_Collaboration" })
	@Parameters({ "template_Name", "proof_Id" })
	public void Get_Ids_For_Proof_Collaboration(String template_Name, String proof_Id) {
		Dashboard_Constants.proof_Id = proof_Id;
		Dashboard_Constants.template_Id = Get_Template_Id(template_Name);
	}
	
	@Test(groups = {"Upload_Proof_Through_API"})
	@Parameters({"file_Path_To_Upload"})
	public void Upload_Proof_Through_API(String file_Path_To_Upload)  {
    	Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("proofsId", Dashboard_Constants.proof_Id);
		file_Path_To_Upload=System.getProperty("user.dir") + File.separator+file_Path_To_Upload;
		Rest_Api.file_Path = file_Path_To_Upload;
		rest.Post_Api("upload_File");
		Util.Report_Log(Status.INFO,"Successfully uploaded given file : "+file_Path_To_Upload+" using API");
	}
	@Test(groups = { "Get_Uploaded_Proof_ID" })
	@Parameters({ "proof_Name" })
	public void Get_Uploaded_Proof_ID(String proof_Name) {
		String response = rest.Get_Api();
		String id = Json_Parsor.Get_Id_From_Json_Based_On_Value(response, "aaData", "Name", proof_Name);
		Util.Report_Log(Status.INFO, "Proof id fetched from API - " + id);
		Dashboard_Constants.proof_Id = id;
	}
	@Test(groups = {"Upload_Task_Attachment_Through_API"})
	@Parameters({"file_Path_To_Upload"})
	public void Upload_Task_Attachment_Through_API(String file_Path_To_Upload)  {
    	Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("csrf", Dashboard_Constants.proof_Id);
		file_Path_To_Upload=System.getProperty("user.dir") + File.separator+file_Path_To_Upload;
		Rest_Api.file_Path = file_Path_To_Upload;
		rest.Post_Api("upload_File");
		Util.Report_Log(Status.INFO,"Successfully uploaded given attachment : "+file_Path_To_Upload+" as proof in task using API");
	}
	@Test(groups = { "Get_Uploaded_Task_Proof_ID" })
	@Parameters({ "proof_Name" })
	public void Get_Uploaded_Task_Proof_ID(String proof_Name) {
		Constants.header_Map.clear();
		Constants.header_Map.put("Referer", "https://perf.naehas.com");
		Constants.header_Map.put("Host", "<calculated when request is sent>");
		String response = rest.Get_Api();
		JSONObject jsonObject = new JSONObject(response);
		JSONArray array = jsonObject.getJSONArray("aaData");
		Iterator<Object> iterator = array.iterator();
		String proof_Id = "";

		while (iterator.hasNext()) {
			System.out.println("testing");
			JSONArray jsonObject1 = (JSONArray) iterator.next();
			String name = (jsonObject1.get(2)).toString();
			if (name.contains(proof_Name)) {
				proof_Id = (name.substring(name.indexOf("proofId"), name.indexOf("&"))).replaceAll("[^0-9]", "");
				String smartTaskAttachmentId = (jsonObject1.get(0)).toString();
				smartTaskAttachmentId = smartTaskAttachmentId.substring(smartTaskAttachmentId.indexOf("editRoundNumber("), smartTaskAttachmentId.indexOf(",")).replaceAll("[^0-9]", "");
				Dashboard_Constants.smart_Task_Attachment_Id = smartTaskAttachmentId;
				break;
			}
		}
		Util.Report_Log(Status.INFO, "Proof id fetched from API - " + proof_Id);
		Dashboard_Constants.proof_Id = proof_Id;
	}
	@Test(groups = { "Remove_Proof_From_Task" })
	public void Remove_Proof_From_Task() {
		Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("smartTaskAttachmentId", Dashboard_Constants.smart_Task_Attachment_Id);
		Constants.form_Map.put("csrf", "");
		rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "Successfully remove proof from task Using API ");
	}	
	@Test(groups = { "Delete_Task" })
	public void Delete_Task() {
		Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("smartTaskId", Dashboard_Constants.task_Id);
		Constants.form_Map.put("csrf", "");
		rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "Successfully remove proof from task Using API ");
	}	

	@Test(groups = {"Unlock_Template_Through_Api"})
	public void Unlock_Template_Through_Api(){
		Util.Report_Log(Status.INFO, "Unlocking template throughg post api for template - "+ Dashboard_Constants.template_Id);
		Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("lpId", Dashboard_Constants.project_Id);
		Constants.form_Map.put("templateId", Dashboard_Constants.template_Id);
		System.out.println(Constants.form_Map);
		rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, " Unlock template post api is triggered successfully ");
	}

	@Test(groups = {"Lock_Template_Through_Api"})
	public void Lock_Template_Through_Api(){
		Util.Report_Log(Status.INFO, "Locking template through post api for template - "+ Dashboard_Constants.template_Id);
		Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("lpId", Dashboard_Constants.project_Id);
		Constants.form_Map.put("templateId", Dashboard_Constants.template_Id);
		System.out.println(Constants.form_Map);
		rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "Lock template post api is triggered successfully ");

	}

	@Test(groups = { "Retire_Proof" })
	public void Retire_Proof() {
		Util.Report_Log(Status.INFO, "Retiring proof for proof id - " + Dashboard_Constants.proof_Id);
		Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("proofsId", Dashboard_Constants.proof_Id);
		Constants.form_Map.put("csrf", "");
		rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "Successfully retire proof Using API ");
	}

	@Test(groups = { "Delete_Template_From_Content_Directory"})
	public void Delete_Template_From_Content_Directory() {
		BeforeMethod("delete_template_API","project_Id");
		Util.Report_Log(Status.INFO, "Retiring proof for proof id - " + Dashboard_Constants.proof_Id);
		Dashboard_Constants.assetDirId=Dashboard_Settings.get_Id_From_Url(Dashboard_Constants.url,"assetDirId");
		Dashboard_Constants.campaignId=Dashboard_Settings.get_Id_From_Url(Dashboard_Constants.url,"campaignId");
		Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("assetDirId", Dashboard_Constants.assetDirId);
		Constants.form_Map.put("templateId", Dashboard_Constants.template_Id);
		Constants.form_Map.put("campaignId", Dashboard_Constants.campaignId);
		rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "Successfully retire proof Using API ");
	}

}
