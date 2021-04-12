package com.melin.apps.locators;

import org.openqa.selenium.By;

/**
 * @author Nitesh Gupta *
 */

public class Merge_Server_Locators {

	//rename the locators appropriately
	public static final By MergeServer_Username_Id=By.name("username");
	public static final By MergeServer_Password_Id=By.name("password");
	public static final By MergeServer_Login=By.xpath("//button[contains(text(),'Login')]");
	public static final By MergeServer_Profile_Button=By.xpath("//a[text()='Hi Naehas ']");
	public static final By MergeServer_Logout=By.xpath("//a[contains(text(),'Log Out')]");
	public static final By Job_Tab=By.xpath("//a[text()='Jobs']");
	public static final By Master_Task_Tab=By.xpath("//a[text()='Master Tasks']");
	public static final By Code_input=By.xpath("//input[contains(@ng-model,'code')]");
	public static final String Status_Asset_Id="//td[text()='asset_Id']//following::td[4]";
	public static final String Job_Type="//td[text()='asset_Id']//preceding-sibling::td[1]";
	public static final By Loader=By.xpath("//div[contains(@ng-show,'isLoading')]");
	public static final By Master_Task_Heading=By.xpath("//a[text()='Master Tasks']");
	public static final By Loader_Locator=By.xpath("//div[contains(@ng-show,'isLoading')]");






}
