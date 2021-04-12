package com.merlin.confluence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.http.auth.AuthenticationException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

/**
 *
 * @author Krishna Saini
 * 
 */
public class Confluence_Page_Script extends Annotation implements Init {

	static String app_List = "Mergeserver,InDesign Editor,ProofCollab App,Content Editor,Html Editor,HTML Compare,Asset Manager";
	static String app_Name_For_Text_Updation = "Email-Integration";
	static String app_Version = "";
	static String app_Script_Version = "";
	static String trun_Name = "";
	static String trun_Status = "";
	static boolean integration_Status_Flag = true;
	static String integration_Status = "";
	static String static_Text = "No Test Case As Of Now";

	private static final Logger log = Logger.getLogger(Confluence_Page_Script.class);

	@Test
	public void update_Build_Page_With_Result() throws Exception {
		Confluence_Page confluncePage = new Confluence_Page();

		Confluence_Page.Login_Confluence_Page(Constants.tag_Confluence_Page_URL);
		String exact_Trget_Version = Confluence_Page.Fetch_Versions_Confluence_Page(
				Constants.merlin_Release_Confluence_Page_URL, app_Name_For_Text_Updation);
		Confluence_Page.Click_Edit_Button();
		String[] app = app_List.split(",");
		log.info(
				"\n\n==================================================================================================================================================================\n                                        Updating Product Integration Result for Build : "
						+ Constants.build_Tag_For_Confulence
						+ "\n==================================================================================================================================================================\n");

		for (String app_Name : app) {
			trun_Name = "";
			trun_Status = "";
			app_Version = "";
			app_Script_Version = "";
			app_Name = app_Name.trim();
			Get_Trun_Details(app_Name, Constants.build_Tag_For_Confulence);
			confluence_Page.Update_Trun_Result(app_Name, app_Version, app_Script_Version,
					"https://jira.naehas.com/browse/" + trun_Name, trun_Status);
			if (!trun_Status.equalsIgnoreCase("PASS"))
				integration_Status_Flag = false;
		}
		confluence_Page.Update_Trun_Result(app_Name_For_Text_Updation, exact_Trget_Version, "", "",
				"No Test Case As Of Now");
		if (integration_Status_Flag == true)
			integration_Status = "Integration-PASS";
		else
			integration_Status = "Integration-In Analysis";
		confluence_Page.Update_Integration_Status(Constants.build_Tag_For_Confulence, integration_Status);
		confluence_Page.Click_Publish_Button();

	}

	public String Get_JIRA_API_Response(String uri)
			throws AuthenticationException, ClientHandlerException, IOException {
		Client client = Client.create();
		WebResource webResource = client.resource(uri);

		String auth = new String(Base64.encode("qaautomation" + ":" + "2TaskShot1"));
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").get(ClientResponse.class);
		int status_Code = response.getStatus();

		if (status_Code == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		} else if (status_Code == 403) {
			throw new AuthenticationException("Forbidden");
		} else if (status_Code == 200 || status_Code == 201) {
		} else {
			System.out.print("Http Error : " + status_Code);
		}
		// ******************************Getting Responce
		// body*********************************************
		BufferedReader input_Stream = new BufferedReader(new InputStreamReader(response.getEntityInputStream()));
		String line = null, json = "";
		while ((line = input_Stream.readLine()) != null) {
			json += line;
		}
		client.destroy();
		return json;
	}

	public void Get_Trun_Details(String app_Name, String build_Tag)
			throws AuthenticationException, ClientHandlerException, IOException {
		app_Name = app_Name.replace(" ", "%20");
		String get_REG_List_URI = "https://jira.naehas.com/rest/api/2/search?jql=project%20%3D%20TRUN%20AND%20%22Target%20Build%22%20%3D%20%22"
				+ build_Tag + "%22%20%20AND%20%22App%20Name%22%20%3D%22" + app_Name
				+ "%22%20AND%20reporter%20%3Dtagger%20";

		String json_Response = Get_JIRA_API_Response(get_REG_List_URI);
		JSONObject jsonObject = new JSONObject(json_Response);
		JSONArray array = jsonObject.getJSONArray("issues");
		Iterator<Object> iterator = array.iterator();
		log.info(
				"\n\n==================================================================================================================================================================\n                                        Printing Trun Details \n==================================================================================================================================================================\n");
		while (iterator.hasNext()) {
			JSONObject jsonObject1 = (JSONObject) iterator.next();
			trun_Name = JsonPath.parse(jsonObject1.toString()).read("$.key").toString();
			trun_Status = JsonPath.parse(jsonObject1.toString()).read("$.fields.status.name").toString();
			app_Version = JsonPath.parse(jsonObject1.toString()).read("$.fields.customfield_13195").toString();
			app_Script_Version = (JsonPath.parse(jsonObject1.toString()).read("$.fields.customfield_13196") != null)
					? JsonPath.parse(jsonObject1.toString()).read("$.fields.customfield_13196").toString() : "";
			log.info("TRUN : " + trun_Name);
			log.info("TRUN Status : " + trun_Status);
			log.info("App Name : " + app_Name);
			log.info("App Version : " + app_Version);
			log.info("App Script Version : " + app_Script_Version);
			log.info(
					"\n\n===================================================================================================================================================================================================================================================================================================================================\n");
		}
	}

}