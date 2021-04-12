package com.melin.apps.locators;

import org.openqa.selenium.By;

/**
 * @author Krishna saini
 *
 */

public class Content_Editor_Locators {

	public static final By source_Code_Button= By.xpath("//button[@id='source-code-btn-button']");
	public static final By source_Code_Ok_Button=By.xpath("//button[text() ='Ok']");
	public static final By save_Button=By.id("saveButton");
	public static final By cancel_Button=By.id("cancelButton");
	public static final String text_Styles_Frame=  "//iframe[@src='scripts/vendor/tinymce/plugins/spanstyles/dialog.html']";
	public static final String paragraph_Styles_Frame ="//iframe[@src='scripts/vendor/tinymce/plugins/divstyles/dialog.html']";
	public static final By color_List_Xpath= By.xpath("//div[contains(@class,'mce-colorbutton mce-first')]//button[@class='mce-open']");
	public static final By color_List_Elements=By.xpath("//div[contains(@class,'mce-start')]//div[@id='fillcolor']//div[@class='colorPickButton']");
	public static final By color_Box_Close_Button=By.xpath("//div[contains(@class,'mce-start')]//div[@id='fillcolor']//div[@class='colorPickButton'][normalize-space(text())='X']");
	public static final By font_Button=By.xpath("//button[@id='fontsbutton-open']");
	public static final By parent_Font_List=By.xpath("(//div[contains(@class,'mce-in')][not(contains(@style,'display: none;'))])[1]//span[contains(@id,'text')]");
	public static final By font_Family_List=By.xpath("(//div[contains(@class,'mce-in')][not(contains(@style,'display: none;'))])[last()]//span[contains(@id,'text')]");
	public static final By get_Data_From_Editor=By.xpath("//div[@id='private']");
	public static final By preview_Data_Xpath= By.xpath("//div[@id='previewPanel']//div[@class='modal-body']");
	public static final By preview_Data_Close_Button=By.xpath("//div[@id='previewPanel']//button[@aria-label='Close']");
	public static final By style_List_Xpath=By.xpath("//select[@id='divNameSelect']|//select[@id='nameSelect']");
}
