package com.merlin.dashboard.ui.emailintegration;

import static com.merlin.common.Dashboard_Constants.Job_Id;
import static com.merlin.common.Dashboard_Constants.current_Date_In_CST;
import java.io.File;
import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.melin.apps.api.Email_Integration_Api;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Dashboard_Constants;
import com.merlin.common.File_Utils;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.merlin.utils.Xml_Parsor;

public class Email_Integration_Publish extends Annotation implements Init {

	public static ArrayList<String> Col_list = new ArrayList<>();
	public static ArrayList<String> Record_List = new ArrayList<>();
	public static ArrayList<String> tag_Values_From_Xml = new ArrayList<>();

	@Test(groups = { "Validate_Data_In_Xml" })
	@Parameters({ "job_Id", "tag_Name", "tag_Value" })
	public static void Validate_Data_In_Xml(@Optional String job_Id,String tag_Name, @Optional String tag_Value) throws Exception {
		job_Id=(!Job_Id.equalsIgnoreCase("")?Job_Id:job_Id);
		Util.Report_Log(Status.INFO, "Validating data in xml file...");
		String file_Path = Email_Integration_Util.Get_Xml_File_From_Remote_Machine(job_Id);
		String tag_Value_From_Xml = Email_Integration_Util.Get_Value_For_Tag_Name(tag_Name, file_Path);
		if(tag_Name.equalsIgnoreCase("Schedule_Send")){
			tag_Value=Dashboard_Constants.current_Date_In_CST;
			tag_Value=tag_Value.replace("/", "-");
			String[] date_Components=tag_Value.split(" ");
			date_Components[0]=Util.Convert_String_Date_Format(date_Components[0],"MM-dd-yyyy","yyyy-MM-dd");
			System.out.println(date_Components[0]+" --- "+date_Components[1]);
			Assert.assertEquals(tag_Value_From_Xml.contains(date_Components[0]) && tag_Value_From_Xml.contains(date_Components[1]), true,"Tag value not found same as expected.");
		}else if(tag_Name.contains("Time")){
			tag_Value_From_Xml=tag_Value_From_Xml.substring(0, tag_Value_From_Xml.indexOf(".")-2);
			tag_Value_From_Xml=tag_Value_From_Xml.substring(0, tag_Value_From_Xml.lastIndexOf(":"));
			tag_Value_From_Xml=Util.Convert_Given_Time_In_Given_Format(tag_Value_From_Xml,"hh:mm a");
			Assert.assertTrue(tag_Value_From_Xml.contains(tag_Value), "Tag value not found same as expected.");
		}else {
			Assert.assertTrue(tag_Value_From_Xml.contains(tag_Value), "Tag value not found same as expected.");
		}
		Util.Report_Log(Status.PASS, "Successfully validate data in xml file!");
	}

	@Test(groups = { "Validate_Data_In_Xml_For_Throttle_Publish_Tags"})
	@Parameters({ "job_Id", "tag_Name"})
	public static void Validate_Data_In_Xml_For_Throttle_Publish_Tags(@Optional String job_Id,String tag_Name) throws Exception {
		String []tags=tag_Name.split(",");
		String []tag_Values=Constants.throttle_tags.split(",");
		for(int i=0;i<tags.length;i++) {
			Validate_Data_In_Xml(job_Id,tags[i],tag_Values[i]);
		}
	}



	@Test(groups = { "Fetch_Varliable_Links_List_From_XML" })
	@Parameters({ "job_Id", "tag_Name", "tag_Value" })
	public static void Fetch_Varliable_Links_List_From_XML(@Optional String job_Id, String tag_Name, String tag_Value) throws Exception {
		job_Id=(!Job_Id.equalsIgnoreCase("")?Job_Id:job_Id);
		Util.Report_Log(Status.INFO, "Validating data in xml file...");
		String file_Path = Email_Integration_Util.Get_Xml_File_From_Remote_Machine(job_Id);
		tag_Values_From_Xml = Xml_Parsor.Get_list_Of_Attribute_tag_In_Xml(file_Path,tag_Name,tag_Value);
		Assert.assertTrue(!tag_Values_From_Xml.isEmpty(), "No list fetched from Xml");
		Util.Report_Log(Status.INFO, "Variable links list coming from dashboard - " + tag_Values_From_Xml);
	}

	@Test(groups = { "Fetch_Column_List_From_ProdCsv" })
	@Parameters({ "job_Id", "unzipped_Folder" })
	public static void Fetch_Column_List_From_ProdCsv(@Optional String job_Id,  String unzipped_Folder) throws Exception{
		job_Id=(job_Id == null)?Job_Id:job_Id;
		Util.Report_Log(Status.INFO, "Validating data in zip file...");
		String file_Path = Email_Integration_Util.Get_zip_File_From_Remote_Machine(job_Id);
		String unzipped_Folder_Path = Constants.artifact_Path + File.separator + unzipped_Folder;
		File_Utils.extractFileFromZip(file_Path,".csv",unzipped_Folder_Path );
		Col_list= File_Utils.Column_list_From_Csv(unzipped_Folder_Path+File.separator+Dashboard_Constants.filename);
		Record_List=File_Utils.Record_list_From_Csv(unzipped_Folder_Path+File.separator+Dashboard_Constants.filename);
		Assert.assertTrue(!Col_list.isEmpty() && !Record_List.isEmpty(), "No Data fetched from Prod.csv");
		Util.Report_Log(Status.INFO, "Column List Fetch from Prod.csv is : "+Col_list);
		Util.Report_Log(Status.INFO, "Record List Fetch from Prod.csv is : "+Record_List);
	}

	@Test(groups = { "Validate_Variable_Links_In_Prodcsv_And_Manifest_File" })
	public static void Validate_Variable_Links_In_Prodcsv_And_Manifest_File(){
		Util.Report_Log(Status.INFO, "Validating Variable Links data from api and data in Manifest File to be same as expected");
		Assert.assertEquals(tag_Values_From_Xml,Email_Integration_Api.Var_links,"Variable Links data from api and that from Manifest are not same as expected");
		Util.Report_Log(Status.INFO, "Validating Column List from Prod.csv and Variable Links from api to be same as expected");
		Assert.assertEquals(Col_list,Email_Integration_Api.Var_links,"Column List from Prod.csv and Variable Links data from api are not same as expected");
		Util.Report_Log(Status.PASS,"All three data Lists are same as Expected");
	}

	@Test(groups = { "Fetch_Column_List_From_NDEProdCsv" })
	@Parameters({ "job_Id", "unzipped_Folder" })
	public static void Fetch_Column_List_From_NDEProdCsv(@Optional String job_Id,  String unzipped_Folder) throws Exception {
		job_Id=(job_Id == null)?Job_Id:job_Id;
		Util.Report_Log(Status.INFO, "Validating data in zip file...");
		String file_Path = Email_Integration_Util.Get_zip_File_From_Remote_Machine(job_Id);
		String unzipped_Folder_Path = Constants.artifact_Path + File.separator  + unzipped_Folder;
		File_Utils.extractFileFromZip(file_Path,"NDE_Prod_Encrypt",unzipped_Folder_Path );
		Util.Report_Log(Status.PASS, "Successfully extracted Zip Folder NDE_Prod_Encrypt");
		File_Utils.extractFileFromZip(unzipped_Folder_Path+File.separator+Dashboard_Constants.filename+File.separator,".csv",unzipped_Folder_Path );
		Util.Report_Log(Status.PASS, "Successfully extracted NDE_PROd.Csv from NDE_Prod_Encrypt");
		Col_list= File_Utils.Column_list_From_Csv(unzipped_Folder_Path+File.separator+Dashboard_Constants.filename+File.separator);
		ArrayList<String> list_Fetched=File_Utils.Record_list_From_Csv(unzipped_Folder_Path+File.separator+Dashboard_Constants.filename+File.separator);
		Assert.assertTrue(!Col_list.isEmpty() && Record_List.size()==list_Fetched.size(), "Data fetched from NDE_Prod.csv is not same as data fetched from Prod.csv");
		Util.Report_Log(Status.INFO, "Column List Fetch from NDE_Prod.csv is : "+Col_list);
		Util.Report_Log(Status.INFO, "Record List Fetch from Prod.csv previously is : "+Record_List);
		Util.Report_Log(Status.INFO, "Record List Fetch from NDE_Prod.csv is : "+list_Fetched);
		Util.Report_Log(Status.PASS, "Successfully validated that Record List Fetch from NDE_Prod.csv is equal to Record List fetched from Prod.csv as expected");
	}

	@Test(groups = { "Fetch_TextFrame_List_From_XML" })
	public static void Fetch_TextFrame_List_From_XML() throws Exception {
		tag_Values_From_Xml = Xml_Parsor.Get_list_Of_Attribute_tag_In_Xml("F:\\Indd_Regression\\Merlin_App_Automation\\InputData\\1.xml","TextFrame_With_Anchored");
		Assert.assertTrue(!tag_Values_From_Xml.isEmpty(), "No list fetched from Xml");
		Util.Report_Log(Status.INFO, "TextFrame list coming from dashboard - " + tag_Values_From_Xml);
	}
}
