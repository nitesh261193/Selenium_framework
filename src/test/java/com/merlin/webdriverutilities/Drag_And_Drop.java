package com.merlin.webdriverutilities;

import com.merlin.common.Annotation;
import com.merlin.common.Init;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.merlin.common.Annotation;


/**
 * @author Pooja Chopra
 *
 */

public class Drag_And_Drop extends Annotation implements Init {

	private static final Logger log  = Logger.getLogger(Set_Webdriver.class);

	public void WebDriver_Drag_And_Drop(By source_Locator , By destination_Locator)
	{
		Actions action = new Actions(driver);
		action.clickAndHold(driver.findElement(source_Locator)).moveToElement(driver.findElement(destination_Locator)).release(driver.findElement(destination_Locator)).build().perform();
		visible.Pause(1);
	}
	
	public void Drag_Element_By_Offset(By text_To_Move, By position_To_Move_Text){

		WebElement text_To_Move_Ele = webelement.Get_Webelement(text_To_Move);
		WebElement position_To_Move_Text_Ele = webelement.Get_Webelement(position_To_Move_Text);

		int position = position_To_Move_Text_Ele.getLocation().getY();
		log.info("UI position on which Element to move = " + position);

		int position1 = text_To_Move_Ele.getLocation().getY();
		log.info("UI position of place where element to be moved = " + position1);

		int result = position - position1;
		result = (result > 0) ? result : result - 4 ; //this -4px is provided due to UI limitation of not dragging component in upward direction

		log.info("Resultant position of Element in corresponding to position(where it needs to move is), by " + result + "pixel");

		Actions action = new Actions(driver);
		action.clickAndHold(text_To_Move_Ele).moveByOffset(0, result).build().perform();  
		action.release().build().perform();
		log.info("Drag and Drop performed By Action"); // Assertion to be placed in the @Test die to different behaviour on different page
	}
	
	public void uploadFileViaDropzone_V2(String base64Data, String fileName , String dropzone_Name) throws Exception {
			String fileType = getFileType(fileName);
			JavascriptExecutor jse = (JavascriptExecutor) driver;

			String aa = "var byteCharacters = atob(\"" + base64Data + "\");" +

					"var byteArrays = [];" +

					"for (var offset = 0; offset < byteCharacters.length; offset += 512) "
					+ "{ var slice = byteCharacters.slice(offset, offset + 512); "
					+ "var byteNumbers = new Array(slice.length);" + " for (var i = 0; i < slice.length; i++) "
					+ "{ byteNumbers[i] = slice.charCodeAt(i);  " + "} "
					+ "var byteArray = new Uint8Array(byteNumbers);byteArrays.push(byteArray); }" +

					"var blob = new Blob(byteArrays, {type: '" + fileType + "'});" +

					" var parts = [blob, new ArrayBuffer()];" +

					" file = new File(parts, '" + fileName + "', {" + "    lastModified: new Date(0)," + // optional
																											// -
																											// default
																											// =
																											// now"+
					"      type: '" + fileType + "' " + " });" +

					// $('input[accept=\'image/jpg,image/gif,image/png,image/jpeg\']').files
					// = [file];
					" var myzone = Dropzone.forElement('#"+dropzone_Name+"');" + " myzone.addFile(file);";

			jse.executeScript(aa);
		
	}

	public String getFileType(String fileName) {
		String type = null;
		if (fileName.contains(".")) {
			type = fileName.split("\\.")[1];
			if (type.equals("jpg") || type.equals("jpeg") || type.equals("png")) {
				type = "image/jpeg";
			} else if (type.equals("pdf")) {
				type = "application/pdf";
			} else if (type.equals("html")) {
				type = "application/html";
			} else if (type.equals("csv")) {
				type = "application/csv";
			} else if (type.equals("indd")) {
				type = "application/x-indesign";
			} else if (type.equals("zip")) {
				type = "application/zip";
			}
			else if (type.equals("wfd")) {
				type = "";
			}
		}

		return type;
	}

}
