package com.merlin.comparison;

import java.io.File;

import org.testng.Assert;

import com.merlin.common.Annotation;
import com.merlin.common.File_Utils;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;

/**
 * @author Pooja Chopra
 *
 */
public class Comparison_Base  extends Annotation{
	
	File base_File, target_File;

	public void is_Valid_Comparison_Files(String base, String target)
	{
		Boolean base_Exists  = File_Utils.isValidFile(new File(base));
		Boolean target_Exists  = File_Utils.isValidFile(new File(target));
		String msg= "";
		if(!base_Exists)
		{
			msg = "Invalid baseline folder path - " + base ;
			Util.Report_Log(Status.ERROR, "Invalid baseline folder path - " + base);
		}
		if(!target_Exists)
		{
			msg += "Invalid target folder path - " + target;
			Util.Report_Log(Status.ERROR, "Invalid target folder path - " + target);
		}
		Assert.assertTrue(base_Exists && target_Exists , msg );
		base_File = new File(base);
		target_File = new File(target);
		Util.Report_Log(Status.INFO, "Base file found --- " + base +" Target file found --- " + target);
	}
}
