package com.merlin.dashboard.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;

public class Users_Settings extends Annotation implements Init {

	static Properties prop = null;
	
	@BeforeTest(alwaysRun = true)
	public void before_test() throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Users_Settings.properties"));

	}

	@Test(groups= {"Open_Users_Page"})
	public static void Open_Users_Page()
	{
		Util.Report_Log(Status.INFO,"Navigating to Users Page");
		navigate.Webdriver_Navigate(Constants.dashboard_Users_Page_URL);
	}

	@Test(groups= {"Filter_User_And_Open_User_Details_Page"})
	@Parameters({"user_Name"})
	public static void Filter_User_And_Open_User_Details_Page(String user_Name)
	{
		Util.Report_Log(Status.INFO,"Searching for the User Name " + user_Name + " and open user details page");
		sendkeys.Webdriver_Sendkeys_Enter(By.xpath(prop.getProperty("Global_Users_Page_Input_Text_Box_Xpath")),
				user_Name);

		int count = webelement.Webelement_Size(By.xpath(prop.getProperty("Global_Users_Page_Table_Row_Xpath")));
		if(count>1)
		{
			for(int i=1; i<count;i++)
			{
				String row_Text= text.Webdriver_Get_Text(By.xpath(prop.getProperty("Global_Users_Page_Table_Row_Xpath")+"["+i+"]//td[1]//a"));
				if(row_Text.equals(user_Name))
					click.Webdriver_Click(By.xpath(prop.getProperty("Global_Users_Page_Table_Row_Xpath")+"//td[1]//a[text()='"+user_Name+"']"), true);
				break;
			}
		}
		else
		{
			click.Webdriver_Click(By.xpath(prop.getProperty("Global_Users_Page_Table_Row_Xpath")+"//td//a[text()='"+user_Name+"']"), true);
		}
	}

	@Test(groups= {"Add_Remove_Roles"})
	@Parameters({"user_Roles","select"})
	public void add_Remove_Roles(String user_Roles, Boolean select)
	{
		Util.Report_Log(Status.INFO,"Adding the required User roles to perform further operations");
		
		String role_Header_Name;

		Util.Report_Log(Status.INFO,"Splitting the User Roles List with respect to the div and its roles name");
		String[] split_User_Roles_List = user_Roles.split("/");

		for (int i = 0; i < split_User_Roles_List.length; i++)
		{
			String split_Values = split_User_Roles_List[i];
			
			String[] split_Roles_Category = split_Values.split("-",5);

			role_Header_Name = split_Roles_Category[0];
			
			Util.Report_Log(Status.INFO,"Validating if the Roles Tab is Expanded or not, if not then opening it.");
			if(webelement.Is_Displayed(By.xpath(prop.getProperty("User_Page_Roles_Tab_Collapse_Icon_Xpath").replace("Header_Name", role_Header_Name)), 2))
			{
				click.Webdriver_Click(By.id(role_Header_Name+"toggle"), false);
			}
			
			String[] role_Name = split_Roles_Category[1].split(",");
			for(int j=0; j<role_Name.length;j++)
			{
				Util.Report_Log(Status.INFO,"Assigning the User Roles, clicking on the checkbox next to user role " + role_Name[j]);
				click.Webdriver_Select_Checkbox(By.xpath(prop.getProperty("User_Page_Roles_Checkbox_Xpath").replace("Role_Name", role_Name[j])), select);
			}
		}
		
		Util.Report_Log(Status.INFO,"Saving the user roles changes");
		click.Webdriver_Click(By.id("Save"), true);
	}
}

