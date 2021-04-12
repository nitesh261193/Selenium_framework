package com.merlin.dashboard.ui.contenteditor;

import com.merlin.webdriverutilities.Set_Webdriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.merlin.common.Annotation;
import com.merlin.common.Init;
import com.merlin.webdriverutilities.Visible;

import static com.melin.apps.locators.Content_Editor_Locators.get_Data_From_Editor;

/**
 * @author Pooja Chopra
 *
 */
public class Content_Editor_Util extends Annotation implements Init{

	static WebDriver driver = Set_Webdriver.DRIVER.Get_Driver() ;
	
	public static void Type_Text_Wysiwyg(String frame_Id , By input_Xpath, String text)
	{
		visible.Wait_For_Page_To_Load();
		navigate.Switch_To_Frame("htmlEditor_ifr");
		sendkeys.Webdriver_Sendkeys(input_Xpath, text);
		navigate.Switch_To_Parent_Frame();
    }
	
	public static String Get_Data_Wysiwyg()
	{
		visible.Wait_For_Page_To_Load();
		navigate.Switch_To_Frame("htmlEditor_ifr");
		String data = text.Webdriver_Get_Text(get_Data_From_Editor);
		navigate.Switch_To_Parent_Frame();
        return data;
	}
}