package com.melin.apps.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Dashboard_Constants;
import com.merlin.common.Util;
import com.merlin.utils.Rest_Api;
import com.merlin.utils.Yaml_Reader;

/**
 * @author Krishna Saini
 *
 */

public class Proof_Collaboration_Api extends Annotation {

	Rest_Api rest = new Rest_Api();

	static public ArrayList<String> file_List = new ArrayList<>();

	@BeforeTest(alwaysRun = true)
	@Parameters({ "api_Name" })
	public void Before_Test(String api_Name) {
		Constants.header_Map.clear();
		Constants.form_Map.clear();
		rest.api_Name = api_Name;
		Rest_Api.config_Name = "ProofCollaboration";
		rest.end_Point = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".endpoint").replace("PROOF_ID",
				Dashboard_Constants.proof_Id);
		rest.request_Body = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".requestBody");
		rest.status_Code = Yaml_Reader.Get_Yaml_Value("api", api_Name + ".statusCode");
		Constants.header_Map.put("Accept", "application/json, text/plain, */*");
	}

	@Test(groups = { "Download_Proof" })
	public void Download_Proof() {
		Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.form_Map.put("proofHistoryId ", Dashboard_Constants.proof_Id);
		Constants.form_Map.put("csrf", "");
		Constants.form_Map.put("template", "");
		Constants.form_Map.put("includeComments ", "true");
		Constants.form_Map.put("lpId ", "13208");
		rest.Post_Api("form_Url");
		Util.Report_Log(Status.INFO, "Successfully downloded proof Using API ");
	}

	@Test(groups = { "Save_Comment" })
	@Parameters({ "comment" })
	public void Save_Comment(String comment) throws UnsupportedEncodingException {
		comment = comment.replace("random", Constants.random_Number);
		long time = Util.Get_Current_Date_In_Milli_Seconds();
		String comment_Json = "{\"" + Dashboard_Constants.proof_Id
				+ "\":[{\"pageNumber\":1,\"annotations\":[{\"position\":[{\"angle\":\"rotate(0rad)\",\"x\":\"416.5px\",\"y\":\"209px\",\"w\":\"1px\",\"h\":\"1px\",\"initialPageRotation\":0}],\"annotationId\":\"annotation_element_8fd4f928-8498-1852-a02c-49aa8b1ea108\",\"comments\":{\"user\":\"qaadmin\",\"comment\":\""
				+ comment
				+ "\",\"date\":"+time+",\"subcomments\":[],\"commentNumber\":1,\"edited\":\"false\"},\"type\":\"comment_box\",\"status\":\"active\",\"color\":\"#FF0000\",\"textColor\":\"#000000\"}]}]}";
		String comment_Json1 = URLEncoder.encode(comment_Json, StandardCharsets.UTF_8.toString());

		rest.end_Point = rest.end_Point.replace("PROOF_ID", Dashboard_Constants.proof_Id).replace("COMMENT_JSON",
				comment_Json1);
		Constants.header_Map.clear();
		Constants.header_Map.put("Origin", "https://perf.naehas.com");
		Constants.header_Map.put("Content-Type", "application/x-www-form-urlencoded");
		rest.Post_Api("post_Without_Url_Encoding");
		Util.Report_Log(Status.INFO, "Successfully Save Comment : "+comment+" Using API ");
	}

}
