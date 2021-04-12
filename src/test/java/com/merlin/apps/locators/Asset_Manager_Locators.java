package com.merlin.apps.locators;

import org.openqa.selenium.By;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.Static;

/**
 * @author Krishna saini
 *
 */

public class Asset_Manager_Locators {
	
	public static final By loading_Locators = By.xpath("//div[@class='sc-ifAKCX hsLvvT']");
	public static final By search_Input_Box = By.xpath("//input[@name='searchTerm']");
	public static final By search_Button = By.xpath("//a[@id='btn-search']");

	
		
    // ---------------- Popup Locators ----------------//
    public static final By popup_Header_For_Send_Share  = By.xpath("//div[@id='modal-root']//h4");
	public static final By cancel_Button = By.xpath("//button[@name= 'cancel']");
	public static final By copy_Button   = By.xpath("//button[@name= 'copy']");
	public static final By rename_Button   = By.xpath("//button[@name= 'rename']");
	public static final By move_Button     = By.xpath("//button[@name= 'move']");
	public static final By add_Button     = By.xpath("//button[@name= 'add']");
	public static final By ok_Button = By.xpath("//button[text() = 'Ok']");
	public static final By send_Button     = By.xpath("//button[@name= 'send']");


    public static final By warning_Message_For_Delete = By.xpath("//div[@id='modal-root']//p");
    public static final By delete_Button = By.xpath("//button[@name= 'delete']");
    public static final By root_Folder_For_Move = By.xpath("//div[@id='modal-root']//div[@class='tree-view__item_system ']");
    public static final By child_Folder_For_Move = By.xpath("//div[@id='modal-root']//ul[@class='tree-view__children--list']/li/div/span");
    public static final String collection_Name_To_Add_Item = "//li[@title = '$collection_Name']";
   
    public static final String preview_Window_For_File = "//div[@id = 'modal-root']//p[text() = '$File_Name']";
    public static final String preview_Window_Close_Button_For_File = "//div[@id = 'modal-root']//p[text() = '$File_Name' ]/following-sibling::span//button[contains(@class,'feature-btn')]";
    
    
    public static final By pdf_File_Preview_Frame = By.xpath("//iframe[@title='previewModalIFrame']");
    public static final By image_File_Preview_Image = By.xpath("//img[@alt='warning Image']");
    public static final By no_Preview_Available_Popup = By.xpath("//p[text()='No preview is available']");
    public static final By already_Existing_File_Error_Message = By.xpath("//span[contains(text(),'with same name')]");
    public static final By share_Email_Id_Input_Box = By.xpath("//div[@id='share-emailid']");
    public static final String select_Suppliers_To_Send_File = "//table[@class='send-modal-table']//tr//td[1][text() ='$supplier_Name']//following-sibling::td[3]";
    public static final By notify_Check_Box_For_Email = By.xpath("//input[@name = 'notify checkbox']");
    public static final By notify_Input_Box_For_Email = By.xpath("//input[@name = 'notify text']");
    public static final By warning_Message_For_Send = By.xpath("//p[contains(text(),'Compress and send')]");
    //---------------- Application Locators --------------//
    public static final String indd_Editor_Document_Title_Text = "//div[@class='title textEllipsis']/a[text()='$title_Text']";
    public static final By indd_Editor_Exit_Button = By.xpath("//div[@class='actbox pull-right']/ul/li/a[@uib-tooltip = 'Close']");
    public static final By html_Editor_Close_Button = By.xpath("//app-button[@ngbtooltip='Close']/span/button/a");
    public static final By indd_Editor_Loader = By.xpath("//div[@ng-show='isWaitingForResponse']");	
    public static final String indd_Editor_Warning_Popup = ("//h5[text() = '$Heading']/parent::div");
    public static final String indd_Editor_Warning_Popup_Close_Button=("//h5[text()='$Heading']/parent::div/button");
    public static final By indd_Editor_Warning_Popup_Continue_Button=By.xpath("//button[text() = 'Continue']");
    public static final By indd_Editor_closing_Message = By.xpath("//div[@ng-show='isWaitingForResponse']/div/span");	


   
    
   
   
   //-----------------------------------------    TOP PANNEL LOCATORS    -----------------------------------------
   
   public static final By header_Row = By.xpath("//div[contains(@class , 'header__feature-btns')]");
   public static final By message_Close_Button = By.xpath("//button[contains(@class,'Toastify__close-button')]");
   

   public static final By upload_Button_In_Top_Pannel = By.xpath("//*[name()='title'][text()='Upload']/ancestor::button");

   public static final By new_Folder_Button = By.xpath("//*[name()='title'][text()='New Folder']/ancestor::button");
 //  public static final By details_Pane_Button = By.xpath("//div[contains(@class , 'header__feature-btns ')]/button/img/parent::button/following-sibling::button");
   public static final By details_Pane_Button = By.xpath("//*[name()='title'][text()='Details']/ancestor::button");

   public static final String folder_Link_In_Bread_Crumb = "//li[@class='breadcrumb-item']/span[text()='$folder_Name']";
   public static final By active_Bread_Crumb = By.xpath("//li[@class='active breadcrumb-item']");
   public static final By search_Close_Button = By.xpath("//div[contains(@class ,  'header__feature-btns')]/button[1]");


   
  //------------------------------------------    COMMON POP UP LOCATORS ------------------------------------------
   public static final By popup_Header  = By.xpath("//div[@id='modal-root']//h5");
   public static final By save_Button     = By.xpath("//button[@name= 'save']");

   
   
  //------------------------------------------    NEW FOLDER POP UP LOCATORS ------------------------------------------
   public static final By input_Box_For_Folder_Rename = By.xpath("//input[@name = 'rename file']");
   public static final By create_Button = By.xpath("//button[@name= 'create']");
   public static final By input_Box_For_Folder_Name = By.xpath("//input[@name = 'folder name']");


   
  //------------------------------------------   UPLOAD POP UP LOCATORS ------------------------------------------
	public static final By upload_Button   = By.xpath("//button[@name= 'upload']");
	public static final String uploaded_File_Name = "//form[@id='my-awesome-dropzone']//div[@class = 'dz-filename']/span[text() = '$file_Name']";

	
	
  //------------------------------------------  DETAILS PANNEL LOCATORS ------------------------------------------
	public static final By details_Title_Tab = By.xpath("//div[@class='info-section__tabs']/div[text()='Details']");
	public static final By details_Item_Name_Title_Text = By.xpath("//div[@class='info-section__title']/span");
    public static final String details_Pane_Field = "//div[@class='info-section-group']//table//td[text()='$detail_Field']/following-sibling::td";
    public static final By metadata_Edit_button = By.xpath("//td[text() = 'Metadata']/following-sibling::td[@class= 'edit-buttons']/button");
    public static final String applied_Metadata = "//td[text() = '$Name']/following-sibling::td";

    
    
  //------------------------------------------  TREE VIEW LOCATORS ------------------------------------------
    
	public static final String open_Given_Root_Item_Under_Ndrive = "//div[@class = 'treeview_collection_folder_heading'][text()='$item_Name']";
	public static final String active_Given_Root_Item_Under_Ndrive = "//div[@class = 'currentlyVisibleSection'][text()='$item_Name']";
	public static final By ndrive_Tree_View_Element = By.xpath("//div[@class='tree-view__item_system active-node']");
	public static final By tree_View_Childeren_Under_Ndrive = By.xpath("//div[@class='tree-view__item_system active-node']//following-sibling::div[@class='tree-view__children']");
	public static final By nDrive_Div =By.xpath("//div[@title='nDrive']");


//------------------------------------------  CENTRAL PANNEL LOCATORS ------------------------------------------
	public static final String get_File_Or_Folder_Using_Name = "//table[@class='explorer-section__table']//td[@title='$File/Folder_Name']";
	public static final String get_File_Or_Folder_Location_Using_Name = "//table[@class='explorer-section__table']//td[@title='$File/Folder_Name']/following-sibling::td[5]";

	public static final String get_Name_Of_Folder_Or_File = "//table[@class='explorer-section__table']//td[@title='$File/Folder_Name']//span[2]";
	public static final By center_Panel = By.xpath("//section[@class='react-contextmenu-wrapper explorer-section']");
	public static final By location_Table_Header = By.xpath("//table[@class='explorer-section__table']//th[@name='location']");
	public static final By last_Modified_Date_Table_Header = By.xpath("//table[@class='explorer-section__table']//th[@name='lastModifiedDate']");
	public static final By last_Modified_By_Table_Header = By.xpath("//table[@class='explorer-section__table']//th[@name='lastModifiedByUser']");
	public static final By file_Type_Table_Header = By.xpath("//table[@class='explorer-section__table']//th[@name='type']");
	public static final By file_Size_Table_Header = By.xpath("//table[@class='explorer-section__table']//th[@name='size']");
	public static final By total_Files_Or_Folder=By.xpath( "//table[@class='explorer-section__table']//td[@class='name-cell disp-flex']");
    public static final String right_Click_Menu_Items = "//div[@role='menuitem'][text()='$option_Name']";
	public static final By input_Field_For_Upload_File = By.xpath("//input[@id = 'file-upload']");
	public static final String get_Type_Value_Of_File_Folder = "//table[@class='explorer-section__table']//td[@title='$File/Folder_Name']/following::td[3]";
	
//------------------------------------------  META DATA POPUP LOCATORS ------------------------------------------
	public static final By metadata_Header = By.xpath("//div[@class='modal-header']/h5");
    public static final String metadata_React_Drop_Down = "//div[contains(@id ,  'react-select')][text() = '$Option_Name']";
    public static final By metadata_Tag_Input_Box = By.xpath("//div[text() = 'Tags']/parent::div//input[contains(@id  , 'react-select')]");
    public static final By all_Selected_Tag_Values = By.xpath("//div[text() = 'Tags']/parent::div//div[@class= 'css-1ge5oe9-multiValue']/div[@class='css-1wpx8bi']");
    public static final String metadata_Custom_Field_Input_Box = "//label[text()  = '$Custom_Field']/parent::div//input" ;
    public static final String all_Selected_Custom_Fields_Values ="//label[text() = '$Custom_Field']/parent::div//div[@class= 'css-1ge5oe9-multiValue']/div[@class='css-1wpx8bi']";


}


