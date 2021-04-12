package com.merlin.common;

import com.aventstack.extentreports.Status;

/**
 * @author Pooja Chopra
 *
 */
public class Dashboard_Constants {

	public static String project_Id = "";
	public static String proof_Id = "";
	public static String task_Id = "";
	public static String smart_Task_Attachment_Id = "";
	public static String template_Id = "14937";
	public static String html_Editor_doc_Id = "";
	public static String shared_Asset_Folder_Id = "";
	public static String template_Name = "TEMP_IN_USE_INDD_EDITOR_FOR_INTEGRATION";
	public static String style_Name = "";
	public static String proof_Name = "";
	public static String segment_Id = "";
	public static String segment_Name = "";
	public static String content_Name = "";
	public static String content_Block_Id = "";
	public static String variable_Link_Id = "";
	public static String Last_Job_Id="";
	public static String Job_Id="";
	public static int merge_Server_Login_count=0;
	public static String asset_Id="";
	public static String assetDirId="";
	public static String campaignId="";
	public static String url="";
	public static String templateFormat_Id="";
	public static String merge_Server_Job_Id = "";
	public static String indd_Frame_Image_Url = "";
	public static String page_Item_Id = "pageItem_1037";
	public static Integer template_Version = 0;
	public static String Job_Status = "";
	public static String current_Date_In_CST= "";
	public static String filename= "";
	public static String remote_Machine_Host ="" ;

	public static void update_Template_Version()
	{
		Util.Report_Log(Status.INFO	, "Updating indd template version");
		template_Version++;
	}
	public static Integer get_Template_Version()
	{
		Util.Report_Log(Status.INFO	, "Current template version is -  " + template_Version);
		return template_Version;
	}
}
