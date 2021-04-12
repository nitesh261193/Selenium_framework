package com.merlin.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pooja Chopra
 *
 */
public class Constants {

	public static final String local_Browser_Name = "chrome";
	public static final String local_Release_Name = "204";


	public static final String local_Dashboard_Name = "IntegraPerf4Dashboard";

	public static final String local_DB_Storage = "false";
	public static String project_Id;
	public static String task_Id;
	public static final String local_Screen_Resolution = "maximize";
	public static String random_Number = "d";
	public static String label;
	public static String test_Case_Name;
	public static String build_Tag;
	public static String release_Number;
	public static String copy_Project_name;
	public static final String browser_Download_Folder = "OutputData/downloadFiles";
	public static final String input_Data_Folder_For_Asset_Manager = "InputData/AssetManager";
	public static final String release_Name = (System.getProperty("RELEASE_NAME") != null)
			? System.getProperty("RELEASE_NAME").toLowerCase().trim()
			: local_Release_Name;
	public static final String browser_Name = (System.getProperty("BROWSER_TO_USE") != null)
			? System.getProperty("BROWSER_TO_USE").toLowerCase().trim()
			: local_Browser_Name;
	public static final int expicit_Wait_Time = 80;
	public static final int explicit_Wait_For_Short_Time = 10;
	public static String growl_Message_Text = "Growl_Message_Not_Recieved";
	public static String screen_Resolution = (System.getProperty("SCREEN_RESOLUTION") != null)
			? System.getProperty("SCREEN_RESOLUTION").toLowerCase().trim()
			: local_Screen_Resolution;
	public static final String dashboard_Name = (System.getProperty("DASHBOARD_NAME") != null)
			? System.getProperty("DASHBOARD_NAME")
			: local_Dashboard_Name;
	public static final String dashboard_Domain = (System.getProperty("TARGET_DASHBOARD_DOMAIN") != null)
			? System.getProperty("TARGET_DASHBOARD_DOMAIN")
			: "https://perf.naehas.com";
	public static final String dashboard_Server_Name = dashboard_Name.split("Dashboard")[0].toLowerCase();
	public static final String dashboard_Url = dashboard_Domain + "/" + dashboard_Name;
	public static final String dashboard_Login_Url = dashboard_Url + "/Login.jsp";
	public static final String dashboard_Home = dashboard_Url + "/home";
	public static final String dashboard_About = dashboard_Url + "/about/About";
	public static final String dashboard_Project_Page = dashboard_Url + "/lp/project?lpId=";
	public static final String dashboard_Task_Page = dashboard_Url + "/smarttasks/details?smartTaskId=";
	public static final String Dashboard_Setting_job_page = dashboard_Url + "/lp/job/jobs";
	public static final String Dashboard_Setting_Metadata_Admin_page = dashboard_Url + "/content/metadata";

	public static final String dashboard_Project_Template_Page = dashboard_Url + "/lp/project/listTemplates?lpId=";
	public static final String dashboard_Project_Template_Content_Page = dashboard_Url
			+ "/lp/contentblocks?templateId=TEMPID&lpId=PROJECTID";
	public static final String dashboard_Project_Publish_Page = dashboard_Url + "lp/project/listSuppliers?lpId=";
	public static final String locator_Path = "Locators" + File.separator + release_Name + File.separator;
	public static final String outputdata_Path = "OutputData" + File.separator;
	public final static String user_Dir_Path = (System.getenv("BUILD_URL") != null)
			? System.getenv("BUILD_URL") + "artifact" + File.separator + "Merlin_App_Automation" + File.separator
			: System.getProperty("user.dir") + File.separator;
	public static final String extent_Report_Path = outputdata_Path + "ExtentReport.html";
	public static final String extent_Report_Screenshot_Path = outputdata_Path + "Screenshots";
	public static String reg_Name;
	public static String artifact_Path;
	public static String login_Username = "qaadmin@naehas.com";
	public static String login_Password = "naehas2016";
	public static String build_Revision;
	public static String zip_Folder;
	public static String app_Name;

	public static String suite_Name= (System.getenv("SUITE_NAME") != null)? System.getenv("SUITE_NAME").toLowerCase().trim() : "";
	public static String app_Version= (System.getenv("APP_VERSION") != null)? System.getenv("APP_VERSION").toLowerCase().trim() : "";
	public static String app_Version_Script = (System.getenv("APP_SCRIPT_VERSION") != null)? System.getenv("APP_SCRIPT_VERSION").toLowerCase().trim() : "";

	public static HashMap<String, String> header_Map = new HashMap<String, String>();
	public static HashMap<String, String> form_Map = new HashMap<String, String>();
	public static HashMap<String,String> propertyMap= new HashMap<String,String>();
	public static HashMap<String,String> prop_Map_From_Api= new HashMap<String,String>();
	public static Map<String, Object> api_Response;

	public static String proofing_Server = "https://proofing.perf.naehas.com/master/services/";
	public static String indd_Template_Url = proofing_Server
			+ "action/openProject/xml?assetId=ASSET_ID&commmand=ORIGINAL&userId=3&version=VERSION_ID";
	public static String indd_Template_Image_Url = proofing_Server
			+ "action/asset/get?userId=3&assetId=ASSET_ID&filePath=IMAGE_URL";

	public static final String release_Sign_Off_Url = "https://confluence.naehas.com/pages/viewpage.action?spaceKey=quality&title=Release+Signoffs";
	public static final String dashboard_Setting_Merger_Script_Url = dashboard_Url
			+ "/settings/edit?name=mergeserver.merger.script.version";
	public static final String dashboard_Setting_Merger_Script_Delimited_Url = dashboard_Url
			+ "/settings/edit?name=mergeserver.merger.script.version.options.comma.delimited";
	public static final String dashboard_Setting_Proof_Collaboration_Url = dashboard_Url
			+ "/settings/edit?name=proof.viewer.host.endpoint";
	public static final String dashboard_Setting_Content_Editor_Url = dashboard_Url
			+ "/settings/edit?name=content.editor.host.endpoint";
	public static final String dashboard_Setting_HTML_Editor_Url = dashboard_Url
			+ "/settings/edit?name=html.template.editor.host.endpoint";
	public static final String dashboard_Setting_HTML_Compare_Url = dashboard_Url
			+ "/settings/edit?name=html.compare.host.endpoint";
	public static final String dashboard_Setting_Asset_Manager_Url = dashboard_Url
			+ "/settings/edit?name=file.system.host.endpoint";
	public static final String dashboard_Setting_INDD_EDITOR_URL = dashboard_Url
			+ "/settings/edit?name=indd.template.editor.host.endpoint";
	public static final String dashboard_Setting_INDD_EDITOR_Script_URL = dashboard_Url
			+ "/settings/edit?name=versioned.templateeditor.revision";
	public static final String dashboard_Setting_INDD_EDITOR_Master_URL = dashboard_Url
			+ "/settings/edit?name=versioned.templateeditor.master.url";
	public static final String dashboard_Setting_INDD_EDITOR_Other_URL = dashboard_Url
			+ "/settings/edit?name=templateeditor.url";
	public static final String dashboard_Setting_INDD_EDITOR_Version_Enabled_URL = dashboard_Url
			+ "/settings/edit?name=versioned.templateeditor.enabled";
	public static final String dashboard_Setting_INDD_EDITOR_Service_Enabled_URL = dashboard_Url
			+ "/settings/edit?name=indd.template.editor.service.enabled";
	public static final String dashboard_Setting_INDD_EDITOR_Domain_URL = dashboard_Url
			+ "/settings/edit?name=indd.template.editor.host.domain";
	public static final String dashboard_Setting_Exact_Target_Url = dashboard_Url
			+ "/settings/edit?name=emailsend.exacttarget.version";
	public static final String dashboard_Users_Page_URL = dashboard_Url + "/lp/list/User";

	public static final String dashboard_Offers_Page_URL = dashboard_Url + "/offers/summary";

	public static final String build_Tag_For_Confulence = (System.getProperty("TARGET_BUILD") != null)
			? System.getProperty("TARGET_BUILD")
			: "";
	public static final String merlin_Release_Confluence_Page_URL = "https://confluence.naehas.com/pages/viewpage.action?spaceKey=PROD&title=Merlin+Integration+Releases";
	public static final String release_Confluence_Page_URL = "https://confluence.naehas.com/display/quality/Release+Signoffs";
	public static final String tag_Confluence_Page_URL = "https://confluence.naehas.com/display/quality/"
			+ build_Tag_For_Confulence;
	public static final String confulence_Page_Username = "tagger";
	public static final String confulence_Page_Password = "j1r@n@#h@s123";

	public static final String Growl_Message_Text = null;

	public static String time_Stamp = "";
	public static String style_Fetched="";

	public static final String remote_Machine_Username = "naehas";
	public static final String remote_Machine_Password = "pr25#lite";

	public static String merge_Server_URL = "https://proofing.perf.naehas.com/mergeserver/#/login";
	public static String merge_Server_Dashbaord_URL = "https://proofing.perf.naehas.com/mergeserver/#/dashboard";
	public static String merge_Server_Username = "proofing-support@naehas.com";
	public static String merge_Server_Password = "1234rty7890";
	public static String current_Date_In_CST = "";
	public static String throttle_tags = "12:00 AM,12:30 AM,500";

}
