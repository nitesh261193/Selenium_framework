package com.merlin.confluence;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;
import com.merlin.webdriverutilities.Visible;
import com.merlin.webdriverutilities.Webelement;
import com.aventstack.extentreports.Status;

/**
 *
 * @author Krishna Saini
 * 
 */

public class Confluence_Page extends Annotation implements Init {

	private static final Logger log = Logger.getLogger(Confluence_Page.class);

	public static void Login_Confluence_Page(String url) {
		navigate.Webdriver_Get(url);
		if (webelement.Is_Element_Displayed(By.className("aui-avatar-inner"), 2)) {
			Util.Report_Log(Status.INFO, "User already logged into Merlin Sign Off Page");
		} else {
			Util.Report_Log(Status.INFO,
					"User not logged in. Hence, login with " + Constants.confulence_Page_Username + " user");
			sendkeys.Webdriver_Sendkeys(By.id("os_username"), Constants.confulence_Page_Username);
			sendkeys.Webdriver_Sendkeys(By.id("os_password"), Constants.confulence_Page_Password);
			click.Webdriver_Click(By.id("loginButton"), true);
			Util.Report_Log(Status.INFO, "Logged in Merlin Release page with user : "
					+ Constants.confulence_Page_Username + " and password " + Constants.confulence_Page_Password);
		}

	}

	public void Update_Trun_Result(String column_Name, String version, String script_Version, String trun_Link,
			String trun_Status) throws Exception {
		WebElement ele = webelement.Get_Webelement(By
				.xpath("//table[contains(@class,'relative-table wrapped confluenceTable')]//tr[td[1]//a[contains(text(),'"
						+ column_Name + "')]]/td[2]"));
		if (version != null && !version.isEmpty())
			((JavascriptExecutor) driver)
					.executeScript("var ele=arguments[0]; ele.innerHTML = '<p>" + version + "</p>';", ele);
		if (script_Version != null && !script_Version.isEmpty())
			((JavascriptExecutor) driver)
					.executeScript("var ele=arguments[0]; ele.innerHTML += '<p>" + script_Version + "</p>';", ele);

		ele = webelement.Get_Webelement(By
				.xpath("//table[contains(@class,'relative-table wrapped confluenceTable')]//tr[td[1]//a[contains(text(),'"
						+ column_Name + "')]]/td[3]"));
		if (trun_Link.isEmpty())
			((JavascriptExecutor) driver).executeScript("var ele=arguments[0]; ele.innerHTML = '" + trun_Status + "';",
					ele);
		else
			((JavascriptExecutor) driver).executeScript(
					"var ele=arguments[0]; ele.innerHTML = '<a href=\"" + trun_Link + "\">" + trun_Status + "</a>';",
					ele);

		log.info("Result updated for : " + column_Name);

	}

	public void Click_Publish_Button() {
		navigate.Switch_To_Default_Frame();
		visible.Wait_Until_Visible(5, By.id("rte-button-publish"));
		click.Webdriver_Click(By.id("rte-button-publish"), true);
		if (visible.Is_Displayed(By.id("qed-publish-button"))) {
			String other_User = text.Webdriver_Get_Text(By.className("contributor-name"));
			log.info("Other user changes also got published - " + other_User);
			click.Webdriver_Click(By.id("qed-publish-button"), true);
			log.info("Clicked on update button to save sign off page");
		}
	}

	public static void Click_Edit_Button() throws InterruptedException {
		visible.Wait_For_Page_To_Load();
		WebElement ele = webelement
				.Get_Webelement(By.xpath("//table[contains(@class,'relative-table wrapped confluenceTable')]//tr"));
		if (ele.isDisplayed()) {
			log.info("Clicking on edit button");

			click.Webdriver_Javascipt_Click(By.id("editPageLink"));
			Thread.sleep(7000);
			visible.Wait_Until_Visible(5, By.id("rte-button-publish"));
			navigate.Switch_To_Frame("wysiwygTextarea_ifr");
		}
	}

	public void Update_Integration_Status(String build_Tag, String integration_Status) throws Exception {
		WebElement ele = webelement.Get_Webelement(By.xpath(
				"//table[contains(@class,'wrapped confluenceTable')]//tr[td[1]/p[contains(text(),'Integra')]]/td[2]"));
		((JavascriptExecutor) driver).executeScript(
				"var ele=arguments[0]; ele.innerHTML += '<p><a href=\"https://jira.naehas.com/issues/?jql=project%20%3D%20TRUN%20AND%20%22Tested%20On%20Dashboard%22%20!~%20%22IntegraPerfOffersDashboard%22%20AND%20%22Target%20Build%22%20%3D%20"
						+ build_Tag + "%20%20AND%20reporter%20%3D%20tagger%20ORDER%20BY%20summary%20ASC\">"
						+ integration_Status + "</a>';",
				ele);
		log.info("Updated release sign off page with Intgration Result as : " + integration_Status);

	}

	public static String Get_Col_Index(By table_Xpath, String col_Name) {

		int index = 0;
		boolean column_Found = false;

		List<WebElement> column_Headers = webelement.Webelement_List(table_Xpath);

		for (WebElement Column_Header : column_Headers) {
			String header_Name = Column_Header.getText().trim().toLowerCase();

			if (!col_Name.equalsIgnoreCase("icon") && header_Name.contains(col_Name.toLowerCase())) {
				index = column_Headers.indexOf(Column_Header) + 1;
				log.info("Column : '" + col_Name + "' Found present on the " + index + " Position of the Table.");
				column_Found = true;
				break;
			} else if (col_Name.equalsIgnoreCase("icon")) {
				index = column_Headers.indexOf(Column_Header) + 1;
				try {
					if (webelement.Get_Webelement(By.xpath(table_Xpath.toString().replace("By.xpath: ", "") + "["
							+ index + "]//i[contains(@class,'fa-download')]/parent::th")).isDisplayed()) {
						column_Found = true;
						break;
					}
				} catch (Exception e) {
				}
			}

		}

		if (!column_Found) {
			log.info("Column Header : '" + col_Name + "' NOT found Present in the Table.");
			Assert.fail("Column Header : '" + col_Name + "' NOT found Present in the Table.");

		}

		return Integer.toString(index);
	}

	public static String Fetch_Versions_Confluence_Page(String url, String app_Name) {
		String version = "";
		String current_URL = navigate.Webdriver_Get_Current_Url();
		Util.Report_Log(Status.INFO,
				"Navigate to Merlin Release Sign Off Page : " + url + " to Fetch Latest Version of: " + app_Name);
		Login_Confluence_Page(url);

		Assert.assertEquals(navigate.Webdriver_Get_Current_Url(), url,
				"User not navigated to Merlin Release Signoff Page");

		String column_Number = Get_Col_Index(By.xpath("//table[contains(@class,'confluenceTable')]//tbody//th"),
				app_Name);
		WebElement version_Cell = webelement.Get_Webelement(
				By.xpath("//table[contains(@class,'confluenceTable')]//tbody/tr[.//th[.='Latest-Versions']]//th["
						+ column_Number + "]"));
		version = version_Cell.getText();

		Util.Report_Log(Status.INFO, "User logged in and fetched the latest Version as : " + version);
		navigate.Webdriver_Navigate(current_URL);
		Util.Report_Log(Status.INFO, "Navigating Back to URL : " + current_URL);
		Assert.assertFalse(version.isEmpty(),
				app_Name + " version is empty, either the column name or the column is removed from confluence.");
		return version;

	}
	
	public String get_Release_Env(String release) 
	{
		String current_URL = navigate.Webdriver_Get_Current_Url();
		Util.Report_Log(Status.INFO , "Navigate to  Release Sign Off Page  to Fetch environment for release " + release);
		Login_Confluence_Page(Constants.release_Sign_Off_Url);

		Assert.assertEquals(navigate.Webdriver_Get_Current_Url(), Constants.release_Sign_Off_Url, "User not navigated to Release Signoff Page");

		String uat_Column_Number = Get_Col_Index(By.xpath("//table[contains(@class,'confluenceTable')]//tbody//th"), "UAT");
		String dev_Column_Number = Get_Col_Index(By.xpath("//table[contains(@class,'confluenceTable')]//tbody//th"), "DEV");
		String uat_Release = text.Webdriver_Get_Text(By.xpath("//table[contains(@class,'confluenceTable')]//tbody/tr/td["+uat_Column_Number+"]"));
		String dev_Release = text.Webdriver_Get_Text(By.xpath("//table[contains(@class,'confluenceTable')]//tbody/tr/td["+dev_Column_Number+"]"));
		navigate.Webdriver_Navigate(current_URL);
		Util.Report_Log(Status.INFO ,"Navigating Back to URL : " + current_URL);
		if(release.equals(dev_Release))
		{
			return "DEV";
		}
		return "UAT";
	}


}