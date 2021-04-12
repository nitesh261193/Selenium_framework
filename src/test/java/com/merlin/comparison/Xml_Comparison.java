package com.merlin.comparison;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.merlin.common.Constants;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;

/**
 * @author Pooja Chopra
 *
 */

public class Xml_Comparison extends Comparison_Base{

	@Test(groups = {"compare_Xmls"})
	@Parameters({"base_Filepath" , "target_Filepath"})
	public void compare_Xmls(String base_Filepath, String target_Filepath) throws IOException, SAXException 
	{    
		Util.Report_Log(Status.INFO	, "Starting with xml comparison");
		base_Filepath = base_Filepath.replaceAll("artifact_Folder", Constants.artifact_Path);
		Util.Replace_File_Data(base_Filepath, "%20", " ");
		target_Filepath = target_Filepath.replaceAll("artifact_Folder", Constants.artifact_Path);
		Util.Report_Log(Status.INFO	, "Base file path - "+ base_Filepath);
		Util.Report_Log(Status.INFO	, "target file path - "+ target_Filepath);
		FileInputStream fis1 = new FileInputStream(base_Filepath);
		FileInputStream fis2 = new FileInputStream(target_Filepath);

		// using BufferedReader for improved performance
		BufferedReader  source = new BufferedReader(new InputStreamReader(fis1));
		BufferedReader  target = new BufferedReader(new InputStreamReader(fis2));

		//configuring XMLUnit to ignore white spaces
		XMLUnit.setIgnoreWhitespace(true);
		//comparing two XML using XMLUnit in Java
		List<Difference> differences = compareXML(source, target);

		//showing differences found in two xml files
		printDifferences(differences);
		Assert.assertTrue(differences.isEmpty(), "Differences found in xml comparison - " +differences);
		Util.Report_Log(Status.PASS	, "2 files compared and no differences found");
	}    

	public static List<Difference> compareXML(Reader source, Reader target) throws SAXException, IOException {

		//creating Diff instance to compare two XML files
		Diff xmlDiff = new Diff(source, target);

		//for getting detailed differences between two xml files
		DetailedDiff detailXmlDiff = new DetailedDiff(xmlDiff);

		return detailXmlDiff.getAllDifferences();
	}

	public static void printDifferences(List<Difference> differences){
		int totalDifferences = differences.size();
		System.out.println("===============================");
		System.out.println("Total differences : " + totalDifferences);
		System.out.println("================================");

		for(Difference difference : differences){
			System.out.println(difference);
		}
		
	}
} 
	
