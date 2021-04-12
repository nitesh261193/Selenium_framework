package com.merlin.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.merlin.webdriverutilities.Set_Webdriver;

/**
 * @author Pooja Chopra
 *
 */
public class Annotation {

	private final Logger log = Logger.getLogger(Annotation.class);
	static ExtentTest test;
	public static ExtentReports extent;
	static ITestContext testContext;
	static public SoftAssert soft_assert = new SoftAssert();
	
	public WebDriver driver = Set_Webdriver.DRIVER.Get_Driver();

	@BeforeSuite(alwaysRun = true)

	public void Before_Suites(ITestContext test_Context) {

		Constants.reg_Name = test_Context.getCurrentXmlTest().getSuite().getName().split("-")[0].replace("_", "-");
		log.info("\n\n========================================================================================================================================================\n                                                            <<<<< TEST SUITE STARTED : " + Constants.reg_Name +" >>>>>\n========================================================================================================================================================\n");
		Constants.random_Number = Util.Get_Random_String(5) ;
		Constants.artifact_Path = "OutputData/" + Constants.reg_Name+ "_"+ Constants.random_Number;
		Constants.random_Number = Util.Get_Random_String(5);
		if((new File(Constants.outputdata_Path)).exists())
		{
			try {
				log.info("Cleaning the OutputData folder.");
				FileUtils.deleteDirectory(new File(Constants.outputdata_Path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.info("Creating Output Folder");
		File dir = new File(Constants.outputdata_Path);
		dir.mkdirs();	
		log.info("Output folder created");
	}

	@AfterTest(alwaysRun = true)
	public void After_Test(ITestContext test_Context) {
		 
		log.info(
				"\n\n-------------------------------------------------------------------------------------------------------------------------- \n TEST CASE ENDED : \""
						+ test_Context.getName()
						+ "\" \n-------------------------------------------------------------------------------------------------------------------------- \n");
	}

	@BeforeMethod(alwaysRun = true)
	public void Before_Method(Method m) {
		Report.Create_Group(m.getName());
	}

	@BeforeTest(alwaysRun = true)
	public void Before_Test_1(ITestContext test_Context) {
		log.info(
				"\n\n-------------------------------------------------------------------------------------------------------------------------- \n TEST CASE STARTED : \""
						+ test_Context.getName()
						+ "\" \n-------------------------------------------------------------------------------------------------------------------------- \n");
	
		Report.Create_Test(test_Context.getCurrentXmlTest().getName());
	}

	@AfterMethod(alwaysRun = true)
	public void After_Method(ITestResult test_Result, Method method) throws Exception {
		if (!test_Result.isSuccess()) {
			Report.Take_Screenshot(driver, Report.Get_Logger(), method.getName());
			Throwable th = test_Result.getThrowable();
			if (th != null) {
				Util.Report_Log(Status.FAIL,
						th.getMessage() + "\n" + printStacktrace(th.getStackTrace(), method.getName()));
				test_Result.setThrowable(null);
			}
		}
	}

	@AfterSuite(alwaysRun = true)
	public void After_Suites() {
		Report.Get_Instance().flush();
		log.info(
				"\n\n========================================================================================================================================================\n<<<<< TEST SUITE ENDED : "
						+ Constants.reg_Name
						+ " >>>>>\n========================================================================================================================================================\n");
		log.info("Saving data into Environment Propertie File");
		String reg_Num = Constants.reg_Name;
		Util.Save_Results_As_ENV("REG_NUM", reg_Num);
		Util.Save_Results_As_ENV("SUITE_NAME", Constants.suite_Name);
		Util.Save_Results_As_ENV("APP_NAME", Constants.app_Name);
		Util.Save_Results_As_ENV("TARGET_BUILD", Constants.build_Tag);
		Util.Save_Results_As_ENV("APP_VERSION", Constants.app_Version);
		Util.Save_Results_As_ENV("APP_SCRIPT_VERSION", Constants.app_Version_Script);
		Util.Save_Results_As_ENV("DASHBOARD_NAME", Constants.dashboard_Name);
	}

	private static String printStacktrace(StackTraceElement[] stack, String methodName) {
		String stackLog = "";
		for (StackTraceElement stk : stack) {
			stackLog = stackLog + stk.toString() + "<br />";
		}
		return stackLog;
	}


}
