package com.merlin.common;


import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


public class Report {

//    static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
    private static final ExtentReports extent_Report = Get_Instance();
    private static ExtentTest parent_Test;
    private static ExtentTest child_Test;
    private static ExtentHtmlReporter html_Report;
    private static ExtentReports report;
    public static synchronized ExtentReports Get_Instance(){
        if(report == null){
        	html_Report = new ExtentHtmlReporter(System.getProperty("user.dir")+File.separator + Constants.extent_Report_Path);
        	report = new ExtentReports();
            report.attachReporter(html_Report);
            html_Report.config().setReportName(Constants.reg_Name);
            html_Report.config().setDocumentTitle("Automatoion Report");
        }
        
        return report;
    }
    
    public static ExtentTest Get_Logger(){
        return child_Test;
    }

    public static synchronized void Create_Test(String name){
    	parent_Test = extent_Report.createTest(name);
    }

    public static synchronized void Create_Group(String name){
    	System.out.println("Creating child node");
    	child_Test = parent_Test.createNode(name);
   }
    
    public static void Take_Screenshot(WebDriver driver,ExtentTest test,String comment) throws Exception{
        String screenshot_Path = Constants.extent_Report_Screenshot_Path+File.separator+comment+Util.Get_Random_String(3)+".png";
        TakesScreenshot takesScreenshot = ((TakesScreenshot)driver);
        File image = takesScreenshot.getScreenshotAs(OutputType.FILE);
        File destination_File= new File (screenshot_Path);
        FileUtils.copyFile(image,destination_File);
        String path=Constants.user_Dir_Path +screenshot_Path;
        System.out.println("Screenshot path ----- "+ path);
        Get_Logger().log(Status.FAIL,"Test method failed");
        Get_Logger().addScreenCaptureFromPath(path);
    }
    
}
