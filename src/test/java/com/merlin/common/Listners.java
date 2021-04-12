package com.merlin.common;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class Listners  implements ITestListener{

	@Override
	public void onFinish(ITestContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		
		//This screenshot copy is for reporting use.=======================================
		System.out.println(arg0.getClass().getMethods().toString());
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
		String formatedDateAndTime = now.format(format);
		String path1 =  "target/surefire-reports/Screenshots/screenshotID"
				+ formatedDateAndTime + ".png";
		String path =  "Screenshots/"
				+ formatedDateAndTime + ".png";
		try {
			Util.Take_Screenshot(path1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String pathToSend = "<a href='"+path+"' target='_blank'>Click here to open evidence</a>";
		String pathToSend = "http://devbuild02.ad.naehas.com/view/Merlin/job/Merlin/job/Dashboard/view/Release_Sign_Off_Jobs/job/"+System.getProperty("JOB_NAME").replace("Merlin/Dashboard/", "")+"/"+System.getProperty("JOB_ID") +"/HTML_20Report/Screenshots/screenshotID"+ formatedDateAndTime +".png";
		System.out.print("*******************************************************************************");
		System.out.println("\n");
		System.err.println(pathToSend);
		System.out.println("\n");
		System.out.println("*******************************************************************************");
		//Reporter.setEscapeHtml(false);
		Reporter.log(pathToSend);
		//==================================================================================
		
		
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestStart(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

}
