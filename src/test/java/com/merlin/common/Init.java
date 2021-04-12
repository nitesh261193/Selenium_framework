package com.merlin.common;

import com.melin.apps.api.Dashboard_Api_Util;
import com.merlin.confluence.Confluence_Page;
import com.merlin.dashboard.ui.emailintegration.Email_Integration_Util;
import com.merlin.webdriverutilities.Click;
import com.merlin.webdriverutilities.Drag_And_Drop;
import com.merlin.webdriverutilities.Navigate;
import com.merlin.webdriverutilities.Select_Dropdown;
import com.merlin.webdriverutilities.Send_Keys;
import com.merlin.webdriverutilities.Text;
import com.merlin.webdriverutilities.Visible;
import com.merlin.webdriverutilities.Webelement;



/**
 * @author Pooja Chopra
 *
 */
public interface Init {

	 Click click = new Click();
	 Drag_And_Drop drag_drop= new Drag_And_Drop();
	 Navigate navigate = new Navigate();
	 Send_Keys sendkeys = new Send_Keys();
	 Text text = new Text();
	 Visible visible= new Visible();
	 Select_Dropdown select = new Select_Dropdown();
     Drag_And_Drop drop = new Drag_And_Drop();
     Webelement webelement= new Webelement();
	 Confluence_Page confluence_Page= new Confluence_Page();
	 Dashboard_Api_Util dashboard_Api_Util = new Dashboard_Api_Util();
	Email_Integration_Util email_Integration_Util= new Email_Integration_Util();


	
	
}
