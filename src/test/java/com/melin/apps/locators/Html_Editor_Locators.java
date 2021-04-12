package com.melin.apps.locators;

import org.openqa.selenium.By;

/**
 * @author Nitesh Gupta
 *
 */

public class Html_Editor_Locators {
	
	//-------------------  Top Panel Locators ----------------------//

	public static final By save_Button = By.xpath("//img[@src='assets/images/control_icon6.png']");
	public static final By Close_Button = By.xpath("//img[@src='assets/images/control_icon7.png']");
	public static final String pop_up_choice_Button  = "//button[normalize-space(text())='choice']";
	public static final By loading_Locator = By.xpath("//div[@class='spinner-border spinStyle']");

	//-------------------  Right Panel Locators ----------------------//
	public static final By side_Bar_Icon = By.xpath("//span[@id='sideBarIcon']");
	public static final By edit_Button = By.xpath("//img[contains(@src , 'edit_icon.png')]");
	public static final By file_links = By.xpath("//div[@class='grid__item--icon']");
	public static final By select_Button_Asset =  By.xpath("(//button[text()='Select'])[1]");
	public static final By  cancel_Button_Asset =  By.xpath("//div[@class='selection-button-section']//button[text()='Cancel']");


	//-------------------  Content Editor Locators ----------------------//
	public static final By CE_text_Area = By.xpath("//body[@id='tinymce']/div[@id='private']");







}
