package com.melin.apps.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.jayway.jsonpath.JsonPath;
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

public class Content_Editor_Api extends Annotation {

	Rest_Api rest = new Rest_Api();

	static public ArrayList<String> color_List = new ArrayList<>();
	static public ArrayList<String> style_List = new ArrayList<>();
	static public HashMap<String, ArrayList<String>> font_Map = new HashMap<String, ArrayList<String>>();

	@BeforeTest(alwaysRun = true)
	@Parameters({ "config_Name", "api_Name" })
	public void Before_Test(String config_Name, String api_Name) {
		Constants.header_Map.clear();
		Constants.form_Map.clear();
		rest.api_Name = api_Name;
		Rest_Api.config_Name = config_Name;
		rest.end_Point = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".endpoint").replace("PROJECT_ID", Dashboard_Constants.project_Id)
				.replace("TEMPLATE_ID", Dashboard_Constants.template_Id).replace("CB_ID", Dashboard_Constants.content_Block_Id);
		rest.request_Body = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".requestBody");
		rest.status_Code = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".statusCode");
		Constants.header_Map.put("Accept", "application/json, text/plain, */*");
	}
	

	@Test(groups = { "Get_Ids_For_Content_Editor" })
	@Parameters({ "project_Id", "template_Name", "content_Name" })
	public void Get_Ids_For_Content_Editor(String project_Id, String template_Name, String content_Name) {
		Dashboard_Constants.project_Id = project_Id;
		Util.Report_Log(Status.INFO, "Project id - " + project_Id);
		Dashboard_Constants.content_Name = content_Name;
		Dashboard_Constants.template_Id = Get_Template_Id(template_Name);
		Dashboard_Constants.content_Block_Id = Get_CB_Id(content_Name);
	}

	@Test(groups = { "Validate_Content_Block_Data_Through_Api" })
	@Parameters({ "content_Value" })
	public void Validate_Content_Block_Data_Through_Api(String content_Value) {
		content_Value = content_Value.replace("random", Constants.random_Number);
		String response = rest.Get_Api();
		String content_Encoded = Json_Parsor.Get_Value_From_Json(response, "content");
		Util.Report_Log(Status.INFO, "Encoded content received as - " + content_Encoded);
		String decoded_Content = Util.Get_Decoded_Content(content_Encoded);
		Util.Report_Log(Status.INFO, "Decoded content as - " + decoded_Content);
		Assert.assertTrue(decoded_Content.contains(content_Value), "Data not updated as expected");
		Util.Report_Log(Status.PASS, "Validated data through API response is as expected");
	}

	@Test(groups = { "Get_Color_List_Coming_Through_Api" })
	public void Get_Color_List_Coming_Through_Api() {
		String response = rest.Get_Api();
		JSONObject jsonObject = new JSONObject(response);
		JSONArray array = jsonObject.getJSONArray("colors");
		Iterator<Object> iterator = array.iterator();
		while (iterator.hasNext()) {
			JSONObject jsonObject1 = (JSONObject) iterator.next();
			String key = JsonPath.parse(jsonObject1.toString()).read("$.COLOR_NAME").toString();
			color_List.add(key);
		}

		Util.Report_Log(Status.INFO, "Colors coming from dashboard are - " + color_List);
	}

	@Test(groups = { "Get_Style_List_Coming_Through_Api" })
	public void Get_Style_List_Coming_Through_Api() {
		style_List.clear();
		String response = rest.Get_Api();
		JSONObject jsonObject = new JSONObject(response);
		JSONArray array = jsonObject.getJSONArray("name");
		Iterator<Object> iterator = array.iterator();
		while (iterator.hasNext()) {
			JSONObject jsonObject1 = (JSONObject) iterator.next();
			String key = JsonPath.parse(jsonObject1.toString()).read("$.value").toString();
			style_List.add(key);
		}
		Util.Report_Log(Status.INFO, "Styles coming from dashboard API are - " + style_List);
	}

	@Test(groups = { "Get_Font_List_Coming_Through_Api" })
	public void Get_Font_List_Coming_Through_Api() {
		String response = rest.Get_Api();
		JSONObject jsonObject = new JSONObject(response);
		JSONArray array = jsonObject.getJSONArray("fontNames");
		Iterator<Object> iterator = array.iterator();
		while (iterator.hasNext()) {

			JSONArray jsonObject1 = (JSONArray) iterator.next();
			String font_Family = (jsonObject1.get(2)).toString();
			ArrayList<String> font_List = (font_Map.get(font_Family) == null) ? new ArrayList<String>()
					: font_Map.get(font_Family);
			font_List.add((jsonObject1.get(1)).toString());
			font_Map.put(font_Family, font_List);
		}
		Util.Report_Log(Status.INFO, "Fonts coming from dashboard API are - " + font_Map);
	}

	@Test(groups = { "Save_Data_In_Content_Block_Using_Post_Api" })
	@Parameters({ "data" })
	public void Save_Data_In_Content_Block_Using_Post_Api(String data) {
		Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("pageURI", "/lp/contentblock/projectEdit");
		Constants.form_Map.put("cbContent", data.replace("random", Constants.random_Number));
		Constants.form_Map.put("ID", Dashboard_Constants.content_Block_Id);
		Constants.form_Map.put("contentType", "TEXT");
		Constants.form_Map.put("vlId", Dashboard_Constants.variable_Link_Id);
		Constants.form_Map.put("lpId", Dashboard_Constants.project_Id);
		Constants.form_Map.put("templateId", Dashboard_Constants.template_Id);
		Constants.form_Map.put("name", Dashboard_Constants.content_Name);
		System.out.println(Constants.form_Map);
		rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "Saving data in Content editor through api");
	}

	public String Get_Template_Id(String name) {
		Before_Test("ContentEditor", "temp_info_API");
		String response = rest.Get_Api();
		String id = Json_Parsor.Get_Id_From_Json_Based_On_Value(response, "projectTemplates", "name", name);
		Util.Report_Log(Status.INFO, "Template id fetched from API - " + id);
		return id;
	}

	public String Get_CB_Id(String content_Name) {
		Before_Test("ContentEditor", "cb_info_API");
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
		Util.Report_Log(Status.INFO, "Variable link id fetched from API - " + vl_Id);
		Util.Report_Log(Status.INFO, "Content block id fetched from API - " + cb_Id);
		return cb_Id;
	}
}
