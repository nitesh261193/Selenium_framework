package com.melin.apps.api;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.merlin.common.*;
import com.merlin.dashboard.ui.inddeditor.Indd_Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.aventstack.extentreports.Status;
import com.merlin.utils.Json_Parsor;
import com.merlin.utils.Rest_Api;
import com.merlin.utils.Yaml_Reader;

import static com.merlin.common.Dashboard_Constants.template_Version;

/**
 * @author Pooja Chopra
 *
 */

public class Indd_Editor_Api extends Annotation implements Init {

	Rest_Api rest = new Rest_Api();

	static public ArrayList<String> content_List = new ArrayList<>();
	static public ArrayList<String> image_Links_List = new ArrayList<>();
	static public ArrayList<String> image_var_List = new ArrayList<>();
	static public ArrayList<String> system_var_list = new ArrayList<>();
	static public ArrayList<String> default_var_list = new ArrayList<>(Arrays.asList("picklistRowid", "test_version","audit_reason_codes"));

	@BeforeMethod(alwaysRun = true)
	@Parameters({ "config_Name", "api_Name" })
	public void BeforeMethod(String config_Name, String api_Name) {
		Constants.header_Map.clear();
		Constants.form_Map.clear();
		rest.api_Name = api_Name;
		Rest_Api.config_Name = config_Name;
		rest.end_Point = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".endpoint").replace("ASSET_ID", Dashboard_Constants.asset_Id).replace("PROJECT_ID", Dashboard_Constants.project_Id).replace("TEMPLATE_ID", Dashboard_Constants.template_Id)
				.replace("TEMPLATEFORMATID", Dashboard_Constants.templateFormat_Id).replace("DASHBOARD",Constants.dashboard_Name).replace("VERSION",String.valueOf(template_Version));
		rest.request_Body = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".requestBody");
		rest.status_Code = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".statusCode");
		Constants.header_Map.put("Accept", "application/json, text/plain, */*");
	}


	@Test(groups = { "Fetch_Asset_Id_From_Api" })
	public void Fetch_Asset_Id_From_Api() throws Exception {
		visible.Pause(5);
		String response = rest.Get_Api();
		String iframe_Url = Json_Parsor.Get_Value_From_Json(response, "embedURL");
		Document document = Jsoup.parse(iframe_Url);
		iframe_Url = document.select("iframe").attr("src");
		String decode_URL=URLDecoder.decode(iframe_Url, "UTF-8" );
		String url_Json=decode_URL.substring(decode_URL.indexOf("{"),decode_URL.lastIndexOf("}")+1);
		String asset_Id = Json_Parsor.Get_Value_From_Json(url_Json, "jobId");
		Dashboard_Constants.asset_Id = asset_Id;
		Util.Report_Log(Status.INFO, asset_Id);
	}

	@Test(groups = { "Trigger_Command_API" })
	public void Trigger_Command_API( ) throws Exception {
		Util.Report_Log(Status.INFO, "1st Command API is triggering");
		BeforeMethod("InddEditor","Command_API");
		visible.Pause(30);
		Constants.form_Map.put("assetId", Dashboard_Constants.asset_Id);
		Constants.form_Map.put("requiredContent","{\"functionName\":\"csvInfo\",\"content\":{\"variableLinkExists\":\"true\",\"userId\":\"3\"}}" );
		Constants.form_Map.put("userId", "3");
		String response = rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "1st Command API is completed ");
	//	System.out.println(response);
	}

	@Test(groups = { "Trigger_Command_API_1" })
	public void Trigger_Command_API_1( ) throws Exception {
		Util.Report_Log(Status.INFO, "2nd Command API is triggering");
		BeforeMethod("InddEditor","Command_API");
		visible.Pause(10);
		Constants.form_Map.put("assetId", Dashboard_Constants.asset_Id);
		Constants.form_Map.put("requiredContent","{\"command\":\"\",\"functionName\":\"extractDocLabelInfo\",\"version\":\"\",\"content\":\"\"}" );
		Constants.form_Map.put("userId", "3");
		String response = rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "2nd Command API is completed ");
		//System.out.println(response);
	}

	@Test(groups = { "Trigger_Command_API_2" })
	public void Trigger_Command_API_2( ) throws Exception {
		Util.Report_Log(Status.INFO, "3rd Command API is triggering");
		BeforeMethod("InddEditor","Command_API");
		visible.Pause(10);
		Constants.form_Map.put("assetId", Dashboard_Constants.asset_Id);
		Constants.form_Map.put("requiredContent","{\"functionName\":\"openProject\",\"content\":{\"continueExists\":\"false\",\"userId\":\"3\"}}" );
		Constants.form_Map.put("userId", "3");
		String response = rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "3rd Command API is completed ");
	//	System.out.println(response);
	}

	@Test(groups = { "Get_Style_Info_Through_Api" })
	@Parameters("style_Type")
	public void Get_Style_Info_Through_Api(String style_Type) throws Exception {
		Util.Report_Log(Status.INFO, "Fetching style info");
		Constants.prop_Map_From_Api.clear();
		String function_Name=Indd_Util.Get_Style_For_Api_Content(style_Type);
		Constants.header_Map.put("Origin","https://perf.naehas.com");
		visible.Pause(10);
		Constants.form_Map.put("assetId", Dashboard_Constants.asset_Id);
		Constants.form_Map.put("requiredContent","{\"command\":\"VERSIONED\",\"functionName\":\""+function_Name+"\",\"version\":\"\",\"content\":\"\"}");
		String response = rest.Post_Api("form_Url");
		JSONObject jsonObject = new JSONObject(response);
		System.out.println(jsonObject.length());
		String arrayString = jsonObject.get("content").toString();
		arrayString=arrayString.substring(arrayString.indexOf("[")+1);
		String[] arr = arrayString.split("},");
		for(String style_Details:arr) {
			if(style_Details.contains(Dashboard_Constants.style_Name)) {
				System.out.println(style_Details);
				style_Details=style_Details.replace("\"","");
				style_Details=style_Details.substring(style_Details.indexOf("{")+1,style_Details.lastIndexOf("}"));
				Constants.style_Fetched=style_Details;
				break;
			}
		}
		System.out.println("style fetched from api - "+Constants.style_Fetched);
		String[] style_Details =Constants.style_Fetched.substring(Constants.style_Fetched.indexOf(":{")+2).split(",");
		for(int i=0;i<style_Details.length;i++) {
			System.out.println(style_Details[i].split(":")[0]+":"+style_Details[i].split(":")[1]);
			Constants.prop_Map_From_Api.put(style_Details[i].split(":")[0],style_Details[i].split(":")[1]);
		}
		Util.Report_Log(Status.INFO, "Style Property Details fetched from Api : "+Constants.prop_Map_From_Api);
	}
	
	
	@Test(groups = { "Fetch_XML_Through_API" })
	@Parameters({"filename"})
	public void Fetch_XML_Through_API( String filename) throws Exception {
		Util.Report_Log(Status.INFO, "fetching xml through API ");
		BeforeMethod("InddEditor","fetch_Xml_API");
		visible.Pause(10);
		Constants.form_Map.put("assetId", Dashboard_Constants.asset_Id);
		Constants.form_Map.put("commmand", "ORIGINAL");
		Constants.form_Map.put("version", String.valueOf(template_Version));
		Constants.form_Map.put("userId", "3");
		String response = rest.Get_Api_withparams();
		Util.Report_Log(Status.INFO, "fetching response through API ");
		//System.out.println(response);
		File_Utils.FileWriter(Constants.artifact_Path,response,filename);
		Util.Report_Log(Status.INFO,"Response is written in text file");

	}

	@Test(groups = { "Save_Data_In_Indd_Content_Block_Using_Post_Api" })
	@Parameters({ "data" })
	public void Save_Data_In_Indd_Content_Block_Using_Post_Api(String data) throws SAXException, ParserConfigurationException, IOException, TransformerException {
		BeforeMethod("InddEditor", "indd_content_editor_save_API");
		Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.header_Map.put("Referer", "https://perf.naehas.com/inddeditor/v135/");
		Constants.form_Map.put("version", Dashboard_Constants.get_Template_Version().toString());
		Constants.form_Map.put("pageItemId", "1037");
		Constants.form_Map.put("taggedTextContent", "<ASCII-MAC><pstyle:NormalParagraphStyle><0x00A0>postman-api-pooja123");
		Constants.form_Map.put("variableLinkLabel", "");
		Constants.form_Map.put("forceSave", "false");
		System.out.println(Constants.form_Map);
		String xml_Response = rest.Post_Api("form_Url");
		File_Utils.write_To_Xml_File(xml_Response, Constants.artifact_Path +"/abc.xml");
		Util.Report_Log(Status.INFO, "call post api");
	}
	
	@Test(groups = { "Click_Save_Button_Post_Api" })
	public void Click_Save_Button_Post_Api(String data) throws SAXException, ParserConfigurationException, IOException, TransformerException {
		Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.header_Map.put("Referer", "https://perf.naehas.com/inddeditor/v133/");
		Constants.form_Map.put("assetId", Dashboard_Constants.get_Template_Version().toString());
		Constants.form_Map.put("userId", Dashboard_Constants.page_Item_Id);
		Constants.form_Map.put("xml", data);
		System.out.println(Constants.form_Map);
		 rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "call post api");
	}
	
	@Test(groups = { "Get_Content_List_Coming_Through_Api" })
	public void Get_Content_List_Coming_Through_Api() {
		
		Constants.header_Map.clear();
		Constants.header_Map.put("origin", "https://perf.naehas.com");
		Constants.form_Map.put("userId", "3");
		Constants.form_Map.put("requiredContent", "{\"functionName\":\"csvInfo\",\"content\":{\"variableLinkExists\":\"true\",\"userId\":\"3\"}}");
		Constants.form_Map.put("assetId", Dashboard_Constants.asset_Id);
		Constants.form_Map.put("Content-Type", "application/x-www-form-urlencoded");
		System.out.println(Constants.form_Map);
		String response = rest.Post_Api("form_Url");
		System.out.println(response);
		
		JSONObject jsonObject = new JSONObject(response).getJSONObject("csvInfo");
		String array = jsonObject.get("variableLinks").toString();
		String[] arr = array.split("\\},");
		for(String json : arr)
		{
			String vlink_Type = Json_Parsor.Get_Value_From_Json(json + "}", "DefaultVLinkTypeIndex");
			if(vlink_Type.equals("1")|| vlink_Type.equals("0"))
				content_List.add(Json_Parsor.Get_Value_From_Json(json + "}", "mName"));
			else if(vlink_Type.equals("-1"))
				system_var_list.add(Json_Parsor.Get_Value_From_Json(json + "}", "mName"));
			else if(vlink_Type.equals("2"))
				image_var_List.add(Json_Parsor.Get_Value_From_Json(json + "}", "mName"));
		}
		
		Util.Report_Log(Status.INFO, "Creating the Vlinks list for Content List");
		content_List.addAll(system_var_list);
		content_List.addAll(default_var_list);
		Collections.sort(content_List);
		Util.Report_Log(Status.INFO, "Content list coming from dashboard - " + content_List);
		
		Util.Report_Log(Status.INFO, "Creating the Vlinks list for Image List");
		image_var_List.addAll(system_var_list);
		image_var_List.addAll(default_var_list);
		Collections.sort(image_var_List);
		Util.Report_Log(Status.INFO, "Image list coming from dashboard - " + content_List);
	}
	
	@Test(groups = { "Get_Image_Links_List_Coming_Through_Api" })
	public void Get_Image_Links_List_Coming_Through_Api() {
		String response = rest.Get_Api();
		JSONObject jsonObject = new JSONObject(response);
		JSONArray array = jsonObject.getJSONArray("links");
		Iterator<Object> iterator = array.iterator();
		while (iterator.hasNext()) {
			JSONObject jsonObject1 = (JSONObject) iterator.next();
			image_Links_List.add(jsonObject1.get("linkName").toString());
		}
		Assert.assertTrue(!image_Links_List.isEmpty(), "No list fetched from api");
		Util.Report_Log(Status.INFO, "Images links list coming from dashboard - " + image_Links_List);
	}

	@Test(groups = {"UnlockTemplate_Through_Api"})
	public void UnlockTemplate_Through_Api(){
		Util.Report_Log(Status.INFO, "Unlocking template throughg post api for template - "+ Dashboard_Constants.template_Id);
		Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		//Constants.header_Map.put("Content-Type", "application/x-www-form-urlencoded");
		Constants.form_Map.put("lpId", Dashboard_Constants.project_Id);
		Constants.form_Map.put("templateId", Dashboard_Constants.template_Id);
		System.out.println(Constants.form_Map);
		rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, " Unlock template post api is triggered successfully ");
	}
}
