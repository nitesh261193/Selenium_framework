package com.merlin.comparison;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.merlin.common.Constants;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;

/**
 * @author Pooja Chopra
 *
 */

public class Image_Comparison extends Comparison_Base{

	@Test(groups = {"Compare_Images"})
	@Parameters({"base_Filepath" , "target_Filepath"})
	public void Compare_Images(String base_Filepath, String target_Filepath) throws IOException 
	{ 
		Util.Report_Log(Status.INFO, "Comparing two images");
		is_Valid_Comparison_Files(base_Filepath, target_Filepath.replace("artifact_Folder", Constants.artifact_Path));

		BufferedImage base_Image = ImageIO.read(base_File); 
		BufferedImage target_Image = ImageIO.read(target_File); 


		int width1 = base_Image.getWidth(); 
		int width2 = target_Image.getWidth(); 
		int height1 = base_Image.getHeight(); 
		int height2 = target_Image.getHeight(); 

		if ((width1 != width2) || (height1 != height2)) 
		{
			Assert.fail("Error: Images dimensions mismatch");
		}
			else
			{ 
				long difference = 0; 
				for (int y = 0; y < height1; y++) 
				{ 
					for (int x = 0; x < width1; x++) 
					{ 
						int rgbA = base_Image.getRGB(x, y); 
						int rgbB = target_Image.getRGB(x, y); 
						int redA = (rgbA >> 16) & 0xff; 
						int greenA = (rgbA >> 8) & 0xff; 
						int blueA = (rgbA) & 0xff; 
						int redB = (rgbB >> 16) & 0xff; 
						int greenB = (rgbB >> 8) & 0xff; 
						int blueB = (rgbB) & 0xff; 
						difference += Math.abs(redA - redB); 
						difference += Math.abs(greenA - greenB); 
						difference += Math.abs(blueA - blueB); 
					} 
				} 
				double total_pixels = width1 * height1 * 3; 
				double avg_different_pixels = difference / total_pixels; 

				double percentage = (avg_different_pixels / 255) * 100; 
				Util.Report_Log(Status.INFO,"Difference percentage found - " + percentage);
				Assert.assertTrue(percentage == 0d , "Differce found while image comparison");
				Util.Report_Log(Status.PASS, "Two images sent for compare appears to be identical");
			} 
	} 
	
}
