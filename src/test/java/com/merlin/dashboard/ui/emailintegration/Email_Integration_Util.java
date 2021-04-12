package com.merlin.dashboard.ui.emailintegration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.aventstack.extentreports.Status;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelSftp.LsEntrySelector;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.merlin.common.Constants;
import com.merlin.common.Dashboard_Constants;
import com.merlin.common.File_Utils;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.merlin.utils.Xml_Parsor;

public class Email_Integration_Util implements Init {
	public static final String remote_Machine_Folder_Name=Constants.dashboard_Name.substring(0, Constants.dashboard_Name.lastIndexOf("D"));
	public static final String remote_Machine_Xml_Path="/data1/"+remote_Machine_Folder_Name+"-dashboard/work/dashboard/downloads";
	private static final String xml_Remote_Path=remote_Machine_Xml_Path.toLowerCase();

	public static String Get_Value_For_Tag_Name(String tag_Name, String file_Path) throws Exception {
		String tag_For_Xml = Get_Tag_Name_For_Xml(tag_Name);
		String xpath_Expression = "/SoftproofRequest/Merge/Requests/Request/" + tag_For_Xml;
		String tag_Value = Xml_Parsor.Get_Value_Of_Tag_From_Xml(file_Path, xpath_Expression);
		Util.Report_Log(Status.INFO, "Name of the tag: " + tag_For_Xml + " and its value is: " + tag_Value);
		return tag_Value;
	}


	private static String Get_Tag_Name_For_Xml(String tag_Name) {
		String tag_Name_For_Xml = null;
		switch (tag_Name) {
			case "Send_Only_Text":
				tag_Name_For_Xml = "EmailSendTxtOnly";
				break;
			case "Deduplicate":
				tag_Name_For_Xml = "EmailDedupOn";
				break;
			case "Delivery_Profile":
				tag_Name_For_Xml = "EmailDeliveryProfile";
				break;
			case "Sender_Profile":
				tag_Name_For_Xml = "EmailSendProfile";
				break;
			case "Test_Send":
				tag_Name_For_Xml = "testEmailValue";
				break;
			case "Schedule_Send":
				tag_Name_For_Xml = "EmailScheduleTime";
				break;
				
			case "Throttle_Start_Time":
				tag_Name_For_Xml = "EmailSendStartTime";
				break;
			case "Throttle_End_Time":
				tag_Name_For_Xml = "EmailSendEndTime";
				break;
			case "Throttle_Limit":
				tag_Name_For_Xml = "EmailSendLimit";
				break;
			default:
				tag_Name_For_Xml = "Invalid tag name: " + tag_Name;
				break;
		}
		return tag_Name_For_Xml;
	}


	public static String Get_Xml_File_From_Remote_Machine(String job_Id)
			throws JSchException, SftpException, IOException {
		final List<String> xml_File_Name = new ArrayList<String>();
		JSch jsch = new JSch();
		System.out.println("folder path" + xml_Remote_Path);
		Session jschSession = jsch.getSession(Constants.remote_Machine_Username, Dashboard_Constants.remote_Machine_Host);
		jschSession.setPassword(Constants.remote_Machine_Password);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		jschSession.setConfig(config);
		jschSession.connect();
		Util.Report_Log(Status.INFO, "Session connected...");
		ChannelSftp channel = (ChannelSftp) jschSession.openChannel("sftp");
		channel.connect();
		channel.cd(xml_Remote_Path);
		LsEntrySelector selector = new LsEntrySelector() {

			@Override
			public int select(LsEntry entry) {
				String file_name = entry.getFilename();
				file_name = file_name.split(".xml")[0];
				if (file_name.endsWith("_" + job_Id)) {
					xml_File_Name.add(entry.getFilename());
					return BREAK;
				}
				return CONTINUE;
			}
		};
		channel.ls(xml_Remote_Path, selector);
		File_Utils.Folder_Creation(Constants.artifact_Path);
		File xml_File = new File(Constants.artifact_Path + File.separator + xml_File_Name.get(0));
		xml_File.createNewFile();
		channel.get(xml_Remote_Path + "/" + xml_File_Name.get(0), xml_File.getPath());
		channel.exit();
		return xml_File.getPath();
	}

	public static String Get_zip_File_From_Remote_Machine(String job_Id)
			throws JSchException, SftpException, IOException {
		final List<String> zip_File_Name = new ArrayList<String>();
		JSch jsch = new JSch();
		Session jschSession = jsch.getSession(Constants.remote_Machine_Username, Dashboard_Constants.remote_Machine_Host);
		jschSession.setPassword(Constants.remote_Machine_Password);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		jschSession.setConfig(config);
		jschSession.connect();
		Util.Report_Log(Status.INFO, "Session connected...");
		ChannelSftp channel = (ChannelSftp) jschSession.openChannel("sftp");
		channel.connect();
		channel.cd(xml_Remote_Path);
		LsEntrySelector selector = new LsEntrySelector() {

			@Override
			public int select(LsEntry entry) {
				String file_name = entry.getFilename();
				file_name = file_name.split(".zip")[0];
				if (file_name.endsWith("_" + job_Id)) {
					zip_File_Name.add(entry.getFilename());
					return BREAK;
				}
				return CONTINUE;
			}
		};
		channel.ls(xml_Remote_Path, selector);
		File_Utils.Folder_Creation(Constants.artifact_Path);
		File zip_File = new File(Constants.artifact_Path + File.separator + zip_File_Name.get(0));
		zip_File.createNewFile();
		channel.get(xml_Remote_Path + "/" + zip_File_Name.get(0), zip_File.getPath());
		channel.exit();
		return zip_File.getPath();
	}

}
