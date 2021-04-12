package com.merlin.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.merlin.webdriverutilities.Set_Webdriver;
import com.aventstack.extentreports.Status;

/**
 * @author Pooja Chopra
 *
 */

public class Util extends Annotation implements Init {

	public static Logger log = Logger.getLogger(Util.class);
	static WebDriver driver = Set_Webdriver.DRIVER.Get_Driver();
	private static final SecureRandom random = new SecureRandom();

	public static void Take_Screenshot(String file_Name) throws IOException {
		File scr_File = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scr_File, new File(Constants.zip_Folder + File.separator + file_Name + ".png"));
		File a = new File(Constants.zip_Folder + File.separator + file_Name);
		log.info("ScreenShot Path : " + a.getAbsolutePath());
		File b = new File(file_Name);
		FileUtils.copyFile(scr_File, b);
		log.info("ScreenShot Path attached in report: " + b.getAbsolutePath());

	}

	public static String Get_Random_String(int length) {

		String result = new BigInteger(Long.SIZE * length, random).toString(32);
		return result.substring(0, length);
	}

	public static String Get_Decoded_Content(String encoded_Content) {
		Base64.Decoder decoder = Base64.getMimeDecoder();
		String decoded_Content = new String(decoder.decode(encoded_Content));
		return decoded_Content;
	}

	public static String Get_Current_Frame() {
		JavascriptExecutor js_Executor = (JavascriptExecutor) driver;
		String currentFrame = (String) js_Executor.executeScript("return self.name");
		return currentFrame;
	}

	public static ArrayList<String> Print_Zip_File_List(String file_Path) {

		ArrayList<String> strings = new ArrayList<String>();
		FileInputStream fis = null;
		ZipInputStream zip_Is = null;
		ZipEntry z_Entry = null;
		try {
			fis = new FileInputStream(file_Path);
			zip_Is = new ZipInputStream(new BufferedInputStream(fis));
			while ((z_Entry = zip_Is.getNextEntry()) != null) {
				if (!z_Entry.isDirectory() && !z_Entry.getName().contains("__MACOSX")) {
					strings.add(z_Entry.getName().substring(12, 17));
				}
			}
			log.info("Files Present are " + strings);
			zip_Is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strings;
	}

	public static void Wait_Until_Requests_Complete() {
		log.info("WAITING FOR AJAX CALLS TO COMPLETE.");
		WebDriverWait wait = new WebDriverWait(driver, 150);
		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				boolean returnedValue = false;
				JavascriptExecutor je = (JavascriptExecutor) driver;
				if (Boolean.parseBoolean(String.valueOf(je.executeScript(" return jQuery.active === 0")))) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					returnedValue = Boolean
							.parseBoolean(String.valueOf(je.executeScript(" return jQuery.active === 0")));
				}
				return returnedValue;
			}
		});
		log.info("ALL CALLS COMPLETED");
	}

	public static String Get_Col_Index(By table_Xpath, String col_Name) {

		int index = 0;
		boolean column_Found = false;

		List<WebElement> column_Headers = driver.findElements(table_Xpath);

		for (WebElement Column_Header : column_Headers) {
			String header_Name = Column_Header.getText().trim().toLowerCase();

			if (!col_Name.equalsIgnoreCase("icon") && header_Name.equalsIgnoreCase(col_Name.toLowerCase())) {
				index = column_Headers.indexOf(Column_Header) + 1;
				log.info("Column : '" + col_Name + "' Found present on the " + index + " Position of the Table.");
				column_Found = true;
				break;
			} else if (col_Name.equalsIgnoreCase("icon")) {
				index = column_Headers.indexOf(Column_Header) + 1;
				try {
					if (driver.findElement(By.xpath(table_Xpath.toString().replace("By.xpath: ", "") + "[" + index
							+ "]//i[contains(@class,'fa-download')]/parent::th")).isDisplayed()) {
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

	public static boolean Remove_Directory_Withdays(int days_Back, File directory) {

		// System.out.println("removeDirectory " + directory);
		if (directory == null) {
			log.info("Directory is Null");
			return false;
		}

		if (!directory.exists()) {
			log.info("Directory Does not Exist");
			return true;
		}

		if (!directory.isDirectory()) {
			log.info("Directory is Not a Directory");
			return false;
		}

		String[] list = directory.list();
		long purgeTime = System.currentTimeMillis() - (days_Back * 24 * 60 * 60 * 1000);
		// directory is empty.
		if (list != null) {
			for (int i = 0; i < list.length; i++) {

				File entry = new File(directory, list[i]);

				if (entry.lastModified() < purgeTime) {
					if (!entry.delete()) {
						if (entry.isDirectory()) {
							if (entry.isDirectory()) {
								if (!Remove_Directory_Withdays(days_Back, entry))
									return false;
							} else {
								if (!entry.delete())
									return false;
							}

						}
						// System.err.println("Unable to delete file: " +
						// entry.getName());

					}
				}
				// System.out.println("\tremoving entry " + entry);

			}
		}

		return directory.delete();
	}

	public static void Save_Results_As_ENV(String variable_Name, String variable_Value) {
		try {
			String text = variable_Name + "=" + variable_Value;
			File env_Property = new File("OutputData/env.properties");
			FileWriter fw = new FileWriter(env_Property.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.newLine();
			bw.close();
			Util.Report_Log(Status.INFO, variable_Name + " = " + variable_Value + " saved in env.properties file.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("error saving " + variable_Name + " in property file");
			e.printStackTrace();
		}
	}

	public static void Replace_File_Data(String file_Path, String old_String, String new_String) {
		File file_To_Be_Modified = new File(file_Path);
		String old_Content = "";
		BufferedReader reader = null;
		FileWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(file_To_Be_Modified));
			// Reading all the lines of input text file into oldContent
			String line = reader.readLine();
			while (line != null) {
				old_Content = old_Content + line + System.lineSeparator();
				line = reader.readLine();
			}
			// Replacing oldString with newString in the oldContent
			String newContent = old_Content.replaceAll(old_String, new_String);
			// Rewriting the input text file with newContent
			writer = new FileWriter(file_To_Be_Modified);
			writer.write(newContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Closing the resources
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void Report_Log(Status status, String message) {
		String str = Thread.currentThread().getStackTrace()[2].getClassName();
		str = str.substring(str.lastIndexOf(".") + 1);
		String class_Info = "(" + str + ".Java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ") ";
		message = class_Info + message;
		Report.Get_Logger().log(status, message);
		log.info(message);
	}

	public static int get_Number_Of_Tabs() {
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		return tabs.size();
	}

	public static void validate_Tables_Headers(String table_Name, String columnslist, By by) {
		Util.Report_Log(Status.INFO, "Validating the Table Headers for the Offer Type");
		String[] columnlist = columnslist.split(",");
		for (String col : columnlist) {
			String col_Index = Get_Col_Index(by, col);
			Assert.assertEquals(col,
					text.Webdriver_Get_Text(
							By.xpath("//table[@*[contains(.,'" + table_Name + "')]]//thead//th[" + col_Index + "]")),
					"The Column " + col + "is not present in the table header");
		}
	}

	public static int get_Row_Count(By by) {
		Util.Report_Log(Status.INFO, "Counting the Number of Rows in a table");
		int count = driver.findElements(by).size();
		return count;
	}

	// Created by : Sagar
	public static String Get_Current_Date_In_String_Format() {
		Date date = new Date();
		SimpleDateFormat DateFor = new SimpleDateFormat("MM/dd/yyyy");
		DateFor = new SimpleDateFormat("MMM dd,yyyy h:mm");
		String currentDate = DateFor.format(date);
		System.out.println("Formatted Date: " + currentDate);
		return currentDate;
	}

	// created by : Krishna Saini
	public static long Get_Current_Date_In_Milli_Seconds() {
		Date date = new Date();
		long timeMilli = date.getTime();
		System.out.println("Time in milliseconds : " + timeMilli);
		return timeMilli;

	}

	public static String Get_Time_In_CST(String format, int min) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date(System.currentTimeMillis() + min * 60 * 1000);
		sdf.setTimeZone(TimeZone.getTimeZone("CST"));
		return sdf.format(date);
	}

	public static String Convert_String_Date_Format(String date_String, String string_Old_Format,
													String string_New_Format) throws ParseException {
		final String OLD_FORMAT = string_Old_Format;
		final String NEW_FORMAT = string_New_Format;
		String oldDateString = date_String;
		String newDateString;

		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		Date d = sdf.parse(oldDateString);
		sdf.applyPattern(NEW_FORMAT);
		newDateString = sdf.format(d);
		return newDateString;
	}

	public static String Convert_Given_Time_In_Given_Format(String tag_Value,String format) {
		LocalTime date = LocalTime.parse(tag_Value);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		String text = date.format(formatter);
		System.out.println(text);
		return text;
	}
}
