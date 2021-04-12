package com.merlin.comparison;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;


/**
 * @author Nitesh Gupta
 *
 */

public class TextFile_Comparison extends Comparison_Base implements Init {

	@Test(groups = {"Compare_Text"})
	@Parameters({"base_Filepath" , "target_Filepath"})
	public void Compare_Text(String base_Filepath, String target_Filepath) throws IOException
	{
		Util.Report_Log(Status.INFO, "Comparing two text Files");
		base_Filepath=base_Filepath.replaceAll("artifact_Folder", Constants.artifact_Path);
		target_Filepath = target_Filepath.replaceAll("artifact_Folder", Constants.artifact_Path);
		Util.Report_Log(Status.INFO, "Base file path - " + base_Filepath);
		Util.Report_Log(Status.INFO, "Target file path - " + target_Filepath);
		is_Valid_Comparison_Files(base_Filepath, target_Filepath);
		File f1 = new File(base_Filepath);
		File f2 = new File(target_Filepath);
		boolean result = FileUtils.contentEqualsIgnoreEOL(f1, f2, "utf-8");
		Assert.assertEquals(result,true, "Content is Not same in both files");
		Util.Report_Log(Status.PASS,"Both text files are same ");
	}
} 
	
