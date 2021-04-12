package com.merlin.dashboard.ui.htmleditor;

import java.io.IOException;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.File_Utils;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;


/**
 * @author Nitesh Gupta
 */
public class HtmlEditor_Util extends Annotation implements Init
{

	
	@Test( groups = { "Validate_Content_In_Html_Src"})
	@Parameters({"text_To_Search","file_Path"})
	public static void Validate_Content_In_Html_Src( String text_To_Search,String file_Path ) throws IOException {
		Util.Report_Log(Status.INFO,"Validating text precence "+text+" in file - " + file_Path);
		file_Path = file_Path.replaceAll("artifact_Folder", Constants.artifact_Path);
		File_Utils.Validate_Content_In_File(text_To_Search.replaceAll("random", Constants.random_Number).split(","), file_Path);
	}
}



	

	


