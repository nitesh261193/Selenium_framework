package com.merlin.dashboard.ui.proofcollaboration;

import static com.merlin.apps.locators.Proof_Collaboration_Locators.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationMarkup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.File_Utils;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;

/**
 * @author Krishna saini
 *
 */
public class Proof_Collaboration_Utility extends Annotation implements Init {

	public static void Add_Text_In_Comment_Box_And_Save(String text_To_Send) {
		click.Webdriver_Click(active_Comment_Box, false);
		sendkeys.Webdriver_Sendkeys_Change(active_Comment_Box, text_To_Send);
		click.Webdriver_Click_By_Action(annotation_List_Panel_Header);
		Util.Report_Log(Status.INFO, "Successfully type given comment : "+text_To_Send+ " in active comment box." );
	}

	public static boolean Delete_Comment() {
		List<WebElement> comments ;
		boolean comments_Deleted=false;
		while (true) {
			try {
				comments=webelement.Webelement_List(comment_Delete_Button);
				Util.Report_Log(Status.INFO, "Total Comment Present : "+comments.size());
				comments.get(0).click();
				click.Webdriver_Click(popup_Ok_Button, false);
				Util.Report_Log(Status.INFO, "Successfully delete comment.");
				continue;
			}catch(Exception e) {
				comments_Deleted=true;
				Util.Report_Log(Status.INFO, "No comment exits now.");
				break;
			}
		}
		return comments_Deleted;
	}

	public static void Select_Random_Div() {
		List<WebElement> textDivs = webelement.Webelement_List(page_Text_Div);
		int randomProduct = new Random().nextInt(textDivs.size());
		click.Webdriver_Click_By_Action(textDivs.get(randomProduct));
		Util.Report_Log(Status.INFO, "Selected Random div number is :"+randomProduct );
	}

	public static void Download_Proof(String downloaded_Proof_Name ,String path_To_Download) throws Exception {
		click.Webdriver_Click(proof_Download_Button, true);
		click.Webdriver_Click(popup_Yes_Button, false);
		File_Utils.Wait_For_File_To_Download(path_To_Download,".pdf",downloaded_Proof_Name);
		Util.Report_Log(Status.INFO, "Proof download successfully" );
	}

	public synchronized static boolean Verify_Comment_In_Downloaded_Proof(String downloaded_Proof_Name,String added_Comment_Text , String path_To_Download) throws IOException
	{
		try{
		boolean foundComment = false;
		Util.Report_Log(Status.INFO, "Verifying Comments in download proof pdf");
		Util.Report_Log(Status.INFO, "Directory path is:" + path_To_Download);
		downloaded_Proof_Name = downloaded_Proof_Name.replaceAll("\\s", "");
		Util.Report_Log(Status.INFO, "Proof Name is:" + downloaded_Proof_Name);
		path_To_Download = path_To_Download + "/" + downloaded_Proof_Name;
		Util.Report_Log(Status.INFO, "Compelete path with file name for Reading file is:" + path_To_Download);
		
		PDDocument document = PDDocument.load(new File(path_To_Download));

		for (int i = 0; i < document.getNumberOfPages(); i++)
		{
			System.out.println("pageNumber:"+i);
			PDPage page = document.getPage(i);
			List<PDAnnotation> annotsArray = null;
			if (page.getAnnotations() == null)
				continue;
			annotsArray = page.getAnnotations();
			int j = 0;
			for (ListIterator<PDAnnotation> iter = annotsArray.listIterator(); iter.hasNext();)
			{
				PDAnnotation annotation = annotsArray.get(j);
				PDAnnotationMarkup annotationMarkup = (PDAnnotationMarkup) annotation;
				String content = annotationMarkup.getContents();
				Assert.assertNotNull(content);
				System.out.println("pdf String : " + content.trim());
				if (content.trim().contains(added_Comment_Text))
				{
					Util.Report_Log(Status.INFO, content);
					foundComment = true;
					break;
				}
				j++;
			}
			
		}
		return foundComment;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}

		
	}

	public static void Select_Template_For_Compare(String template_To_Select) {
		Util.Report_Log(Status.INFO, "selecting Template " + template_To_Select);
		select.Webdriver_Select_Dropdown_List_By_Visible_Text(select_Template_Dropdown, template_To_Select);
		String template_Selected = text.Javascript_Get_Text(select_Template_Dropdown);
		Assert.assertTrue(template_Selected.contains(template_To_Select),
				"unable to select template " + template_To_Select + "template selected is :" + template_Selected);
		Util.Report_Log(Status.PASS, "Template is selected succesfully-" + template_To_Select);
	    visible.Wait_For_Page_To_Load(); 
	}

	public static void Select_Proof_Type_For_Compare(String proof_Type_To_Select) {
		Util.Report_Log(Status.INFO, "selecting Proof Type  " + proof_Type_To_Select);
		select.Webdriver_Select_Dropdown_List_By_Visible_Text(select_Proof_Type_Dropdown, proof_Type_To_Select);
		String proof_Type_Selected = text.Javascript_Get_Text(select_Proof_Type_Dropdown);
		Assert.assertTrue(proof_Type_Selected.contains(proof_Type_To_Select), "unable to select Proof Type "
				+ proof_Type_To_Select + "Proof Type selected is :" + proof_Type_Selected);
		Util.Report_Log(Status.PASS, "Proof is selected succesfully -" + proof_Type_To_Select);
	    visible.Wait_For_Page_To_Load(); 

	}

	public static void Upload_Template_For_Compare(String file_Path_To_Upload) {
		Util.Report_Log(Status.INFO, "uploading candidate Proof " + file_Path_To_Upload);
		sendkeys.Webdriver_Sendkeys(compare_Candidate_File_Input, file_Path_To_Upload);
		visible.Wait_For_Page_To_Load();
		String proof_To_Upload = file_Path_To_Upload.substring(file_Path_To_Upload.lastIndexOf('\\'));
		String candidate_Proof_Uploaded = text.Javascript_Get_Value(compare_Candidate_File_Input);
		Assert.assertTrue(candidate_Proof_Uploaded.contains(proof_To_Upload), "unable to upload candidate Proof "
				+ proof_To_Upload + " Proof selected is :" + candidate_Proof_Uploaded);
		Util.Report_Log(Status.PASS, "Candidate Proof is uploaded succesfully-" + proof_To_Upload);
	    visible.Wait_For_Page_To_Load(); 

	}

	public static String Download_Compare_Result(String download_Result_Choice, String download_Path,
			String compared_Result_Downloaded) throws Exception {
		visible.Wait_For_Page_To_Load();
		visible.Wait_Until_Visible(Constants.expicit_Wait_Time, compare_Download_Popover_Button);
		visible.Wait_Until_Invisible(Constants.explicit_Wait_For_Short_Time, Compare_Download_Button_Spinner);
		click.Webdriver_Click(compare_Download_Popover_Button, false);
		String xpath = compare_Download_With_Option_Button.replace("$download_Option", download_Result_Choice);
		click.Webdriver_Click(By.xpath(xpath), false);
		return File_Utils.Wait_For_File_To_Download(download_Path, ".zip", compared_Result_Downloaded);
	}

}