package com.merlin.dashboard.ui.proofcollaboration;

import static com.merlin.apps.locators.Proof_Collaboration_Locators.markup_Tool_Panel_Header;
import static com.merlin.apps.locators.Proof_Collaboration_Locators.markup_Tool_Right_Toggle;
import static com.merlin.apps.locators.Proof_Collaboration_Locators.markup_Tool_To_Select;
import static com.merlin.apps.locators.Proof_Collaboration_Locators.saved_Comment;
import static com.merlin.apps.locators.Proof_Collaboration_Locators.available_Comment_Elements;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;


/**
 * @author Krishna Saini
 *
 */
public class Proof_Collaboration_Right_Panel extends Annotation implements Init {
	static Properties prop = null;

	@BeforeTest(alwaysRun = true)
	public void Before_Test_Class(ITestContext testContext) throws IOException {
		prop = new Properties();
		prop.load(new FileInputStream(Constants.locator_Path + "Dashboard.properties"));

	}

	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		navigate.Switch_To_Frame("proofViewerFrame");

	}

	@Test(groups = { "Expand_Markuptool_Drop_Down" })
	public static void Expand_Markuptool_Drop_Down() {
		String un_Expanded = markup_Tool_Panel_Header.replace("$expanded", "false");
		
		if (visible.Is_Displayed(By.xpath(un_Expanded))) {
			Util.Report_Log(Status.INFO, "Markup Tool Panel not expanded ,expanding markuptool tools ...");
			click.Webdriver_Javascipt_Click(markup_Tool_Right_Toggle);
		} else {
			Util.Report_Log(Status.INFO, "Markup Tool Panel expanded already");

		}
		String expanded = markup_Tool_Panel_Header.replace("$expanded", "true");
		Assert.assertTrue(visible.Is_Displayed(By.xpath(expanded)), "unable to expand the Markuptools Drop down");
		Util.Report_Log(Status.PASS, "Markup Tool Panel opend successfully");
	}

	@Test(groups = { "Select_Markuptool_And_Add_Comment" })
	@Parameters({ "comment_Type", "comment_Text" })
	public static void Select_Markuptool_And_Add_Comment(String comment_Type, String comment_Text) {
		String markup_Tool_Type = markup_Tool_To_Select.replace("$Type", comment_Type);
		Util.Report_Log(Status.INFO, "Selecting markup tool - " + comment_Type);
		click.Webdriver_Javascipt_Click(By.xpath(markup_Tool_Type));
		Proof_Collaboration_Utility.Select_Random_Div();
		Proof_Collaboration_Utility.Add_Text_In_Comment_Box_And_Save(comment_Text);
		Util.Report_Log(Status.PASS, "Comment is added and saved");
	}

	@Test(groups = { "Verify_Created_Comment_Exist" })
	@Parameters({ "comment_Text" })
	public static void Verify_Created_Comment_Exists(String comment_Text) {
		comment_Text = comment_Text.replace("random", Constants.random_Number);
		visible.Wait_For_Page_To_Load();
		Util.Report_Log(Status.INFO, "Comment : " + comment_Text);
		visible.Wait_Until_Visible(Constants.expicit_Wait_Time, By.xpath(saved_Comment.replace("$text", comment_Text)));
		String comment_Found = text.Javascript_Get_Inner_Text(By.xpath(saved_Comment.replace("$text", comment_Text)));
		System.out.println(comment_Found);
		Assert.assertTrue(comment_Found.equals(comment_Text), "Unable to find given comment : " + comment_Text);
		Util.Report_Log(Status.PASS, "Successfully verified that given comment : " + comment_Text + " Exists.");
	}
	
	@Test(groups = { "Verify_Only_One_Comment_Present_In_Proof" })
	public static void Verify_Only_One_Comment_Present_In_Proof() {
		visible.Wait_For_Page_To_Load();
		Util.Report_Log(Status.INFO, "verifying that only one comment is present in proof.");
        try{
		visible.Wait_Until_Visible(30, available_Comment_Elements);
        }
        catch(Exception e)
        {
        	// to Do
        }
		List<WebElement> eles = webelement.Webelement_List(available_Comment_Elements);
        Assert.assertTrue(eles.size()==1, "Total Number of comments present are : " + eles.size());
		Util.Report_Log(Status.PASS, "Successfully verified that only one comment is present in proof.");
	}

	@Test(groups = { "Delete_Comments" })
	@Parameters()
	public static void Delete_Comments() {
		boolean deleted = Proof_Collaboration_Utility.Delete_Comment();
		Assert.assertTrue(deleted, "Unable to delete all comments");
		Util.Report_Log(Status.PASS, "Successfully deleted all Comments present.");
	}

}
