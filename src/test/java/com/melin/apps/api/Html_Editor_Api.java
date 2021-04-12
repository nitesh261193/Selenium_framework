package com.melin.apps.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Dashboard_Constants;
import com.merlin.common.File_Utils;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.merlin.utils.Json_Parsor;
import com.merlin.utils.Rest_Api;
import com.merlin.utils.Yaml_Reader;


/**
 * @author Pooja Chopra
 *
 */

public class Html_Editor_Api extends Annotation implements Init {

	Rest_Api rest = new Rest_Api();

	static public ArrayList<String> file_List = new ArrayList<>();

	@Parameters({"api_Name" })
	public void Before_Test(String api_Name) {
		Constants.header_Map.clear();
		Constants.form_Map.clear();
		rest.api_Name = api_Name;
		Rest_Api.config_Name = "HtmlEditor";
		rest.end_Point = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".endpoint").replace("PROJECT_ID", Dashboard_Constants.project_Id)
				.replace("TEMPLATE_ID", Dashboard_Constants.template_Id).replace("DOC_ID", Dashboard_Constants.html_Editor_doc_Id)
				.replace("FOLDER_ID", Dashboard_Constants.shared_Asset_Folder_Id);
		rest.request_Body = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".requestBody");
		rest.status_Code = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".statusCode");
		Constants.header_Map.put("Accept", "application/json, text/plain, */*");
	}


	@Test(groups = { "Get_Html_through_API" })
	@Parameters({"filename"})
	public void Get_Html_through_API(String filename) throws Exception {
		Before_Test( "Get_HTML_API");
		String response = rest.Get_Api();
		File_Utils.FileWriter(Constants.artifact_Path,response,filename);
		Util.Report_Log(Status.INFO,"Response is written in text file");
	}

	public String Get_Doc_Id() {
		Before_Test( "DocID_API");
		Util.Report_Log(Status.INFO, "Fetching html document id from api");
		String response = rest.Get_Api();
		String id = Json_Parsor.Get_Id_From_Response(response, "docList", "docId");
		Util.Report_Log(Status.INFO, "Doc id fetched from API - " + id);
		return id;
	}

	@Test(groups = {"Save_Html_Editor_Through_API"})
	@Parameters({"Api_Body_filepath"})
	public void Save_Html_Editor_Through_API( String Api_Body_filepath) throws IOException  {
		if(Dashboard_Constants.html_Editor_doc_Id != null)
			Dashboard_Constants.html_Editor_doc_Id = Get_Doc_Id();
		Before_Test( "Save_API");
		Util.Report_Log(Status.INFO,"Calling save call to update html template by posting file - " + Api_Body_filepath);
		File base_File = new File(Api_Body_filepath);
		FileUtils.copyFile(base_File, new File(Constants.artifact_Path+File.separator + base_File.getName()));
		Util.Replace_File_Data(Constants.artifact_Path+File.separator + base_File.getName(), "random", Constants.random_Number);
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.header_Map.put("Content-Type", "multipart/form-data");
		Constants.form_Map.put("docId", Dashboard_Constants.html_Editor_doc_Id);
		Constants.form_Map.put("filename", "abc");
		Rest_Api.file_Path = Constants.artifact_Path +File.separator  + base_File.getName();
		rest.Post_Api("upload_File");
		Util.Report_Log(Status.INFO,"Save call triggered from api, to update html template");
	}

	@Test(groups = {"Get_Id_For_Asset_Folder"})
	@Parameters({"folder_Name"})
	public void Get_Id_For_Asset_Folder( String folder_Name)  {
		Before_Test( "Asset_folder_API");
		String response = rest.Get_Api();
		Dashboard_Constants.shared_Asset_Folder_Id=Json_Parsor.Get_Id_From_Asset_folder(response,"File","childFiles","location",folder_Name);
		Util.Report_Log(Status.INFO, "Shared Asset Folder id - " + Dashboard_Constants.shared_Asset_Folder_Id);

	}

	@Test(groups = {"Shared_Asset_Data_Through_API"})
	public void Shared_Asset_Data_Through_API( )  {
		Before_Test( "Asset_API");
		String response = rest.Get_Api();
		JSONObject jsonObject = new JSONObject(response);
		JSONArray array=jsonObject.getJSONObject("File").getJSONArray("childFiles");
		Iterator<Object> iterator = array.iterator();
		while (iterator.hasNext()) {
			JSONObject jsonObject1 = (JSONObject) iterator.next();
			if(jsonObject1.get("type").toString().equals("IMAGE_FILE"))
				file_List.add(jsonObject1.get("location").toString());
		}
		Assert.assertTrue(!file_List.isEmpty(), "No image exists in folder");
		Util.Report_Log(Status.INFO, "Images links list coming from dashboard - " + file_List);

	}

}

