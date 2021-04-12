package com.merlin.dashboard.ui.htmleditor;


import static com.melin.apps.locators.Html_Editor_Locators.CE_text_Area;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.aventstack.extentreports.Status;


/**
 * @author Nitesh Gupta
 *
 */
public class HtmlEditor_Content_Editor extends Annotation implements Init
{

	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		navigate.Switch_To_Default_Frame();
		navigate.Switch_To_Frame("proofViewerFrame");
		navigate.Switch_To_Frame("editoriframe");
}
	
	@Test( groups = {"Edit_Text_Of_Text_Frame"})
	@Parameters({"content"})
	public static void Edit_Text_Of_Text_Frame(String content)
	{
		content = content.replace("random", Constants.random_Number);
		navigate.Switch_To_Frame("htmlEditor_ifr");
		sendkeys.Webdriver_Sendkeys_Without_Clear(content);
		Util.Report_Log(Status.INFO,"String is entered in CE");
	}
}
