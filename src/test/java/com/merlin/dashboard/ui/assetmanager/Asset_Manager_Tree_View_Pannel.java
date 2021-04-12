package com.merlin.dashboard.ui.assetmanager;

import static com.merlin.apps.locators.Asset_Manager_Locators.active_Given_Root_Item_Under_Ndrive;
import static com.merlin.apps.locators.Asset_Manager_Locators.open_Given_Root_Item_Under_Ndrive;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Init;
import com.merlin.common.Util;

/**
 * @author Krishna Saini
 *
 */
public class Asset_Manager_Tree_View_Pannel extends Annotation implements Init {

	@BeforeClass(alwaysRun = true)
	public void Before_Class() {
		navigate.Switch_To_Frame("fileSystemIFrame");
	}

	@Test(groups = { "Navigate_To_Collections_Or_Folders" })
	@Parameters({ "item_Name" })
	public static void Navigate_To_Collections_Or_Folders(String name) {

		boolean tree_View_Xpanded = Asset_Manager_Util.Expand_And_Tree_View();
		if (tree_View_Xpanded
				&& visible.Is_Displayed(By.xpath(open_Given_Root_Item_Under_Ndrive.replace("$item_Name", name)))) {
			click.Webdriver_Click(By.xpath(open_Given_Root_Item_Under_Ndrive.replace("$item_Name", name)), true);
			Util.Report_Log(Status.PASS, "Successfully open the given item under nDrive : " + name);
		} else if (tree_View_Xpanded
				&& visible.Is_Displayed(By.xpath(active_Given_Root_Item_Under_Ndrive.replace("$item_Name", name)))) {
			Util.Report_Log(Status.INFO, "Given item is already opened : " + name);
		} else {
			Util.Report_Log(Status.FAIL, "Given item does not exists under ndrive : " + name);
		}
	}

}
