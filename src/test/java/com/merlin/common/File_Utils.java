package com.merlin.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.lingala.zip4j.model.FileHeader;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.aventstack.extentreports.Status;
import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * @author Pooja Chopra
 * 
 */

public class File_Utils implements Init {

	public static boolean isValidFile(File file) {
		return (file != null && file.exists() && file.isFile());
	}

	public static void Move_File(String BasePath, String TargetPath, @Optional String file_Name) {
		Folder_Creation(TargetPath);
		File afile = new File(BasePath);
		String fileName = (file_Name == null) ? afile.getName() : file_Name;
		Assert.assertTrue(afile.renameTo(new File(TargetPath + File.separator + fileName)), "Unable to move file");
		Util.Report_Log(Status.INFO, "File is moved successful!! " + TargetPath + fileName);
	}

	public static String Folder_Creation(String Path) {
		File dir = new File(Path);
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				Util.Report_Log(Status.INFO, "Directory is created!  :" + Path);
			} else {
				Util.Report_Log(Status.INFO, "Failed to create directory!");
			}
		} else {
			Util.Report_Log(Status.INFO, "Directory Exists!");
		}
		return dir.getAbsolutePath();
	}

	public static String Search_Filename(String folder_Path, String filepattern, String extentsion) {
		String fileName = null;
		String file_Name = null;
		File dir = new File(folder_Path);
		File[] files = dir.listFiles();
		for (File f : files) {
			fileName = f.getName();
			if ((fileName.toLowerCase().contains(filepattern.toLowerCase()))
					&& (fileName.toLowerCase().contains(extentsion.toLowerCase()))) {
				file_Name = f.getName();
				break;
			}
		}
		Assert.assertTrue(file_Name != null, "Unable to find file with pattern - " + filepattern);
		return file_Name;
	}

	public static void Wait_To_Download() throws InterruptedException {
		Boolean Download_Complete = false;
		File a = new File(Constants.browser_Download_Folder);
		while (!Download_Complete) {
			String[] ab = a.list();

			for (String ac : ab) {
				Download_Complete = true;
				if (ac.contains("crdownload")) {
					visible.Pause(5);
					Download_Complete = false;
					Wait_To_Download();
				}
			}
		}
	}

	public static String Download_File_From_URL(String url, String target_File_Path, String file_Pattern,
			String file_Extension, @Optional String rename_Target_File) throws InterruptedException {
		navigate.Open_New_Tab();
		navigate.Webdriver_Get(url);
		Wait_To_Download();
		visible.Pause(5);
		String file_Name = File_Utils.Search_Filename(Constants.browser_Download_Folder, file_Pattern, file_Extension);
		System.out.println(file_Name);
		if (rename_Target_File != null && rename_Target_File.equals("version"))
			rename_Target_File = file_Name.replace(file_Extension, "") + "_V"
					+ Dashboard_Constants.get_Template_Version() + file_Extension;
		Move_File(Constants.browser_Download_Folder + File.separator + file_Name, target_File_Path, rename_Target_File);
		navigate.Webdriver_Close_Current_Tab();
		return rename_Target_File;
	}

	public static String Download_File_From_URL(String url, String target_File_Path, String file_Pattern,
			String file_Extension, @Optional String rename_Target_File, String fileName) throws InterruptedException {
		try{
			navigate.Open_New_Tab();
			navigate.Webdriver_Get(url);
			Wait_To_Download();
			visible.Pause(5);
			String file_Name = File_Utils.Search_Filename(Constants.browser_Download_Folder, file_Pattern, file_Extension);
			System.out.println(file_Name);
			if (rename_Target_File != null && rename_Target_File.equals("version"))
				rename_Target_File = fileName + "_V" + Dashboard_Constants.get_Template_Version() + file_Extension;
			Move_File(Constants.browser_Download_Folder + File.separator + file_Name, target_File_Path, rename_Target_File);
		}
		finally{
			navigate.Webdriver_Close_Current_Tab();
		}
		return rename_Target_File;
	}

	// Created by : Krishna Saini

	public static void Delete_file(String path) {
		try {

			File file = new File(path);

			if (file.delete()) {
				Util.Report_Log(Status.INFO, file.getName() + " is deleted!");
			} else {
				Util.Report_Log(Status.INFO, "Delete operation is failed.");
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
	// Created by : Krishna Saini

	public static String Get_File_Type_Extension(String file_Name) {

		if (file_Name.isEmpty()) {
			Util.Report_Log(Status.INFO, "File_Name is Null");
			return null;
		} else {
			return file_Name.substring(file_Name.lastIndexOf("."));
		}
	}
	// Created by : Krishna Saini

	public static boolean Wait_File_To_Download(String downloadDirPath, String name) throws Exception {
		Util.Report_Log(Status.INFO, "Downloading file in dir : " + downloadDirPath);
		int count = 0;
		while (true) {
			if (count <= 5) {
				visible.Pause(3);
				count++;
			} else {
				throw new Exception("Timeout for file to download");
			}
			File file = new File(Constants.browser_Download_Folder + "/" + name);
			if (file.exists() == true)
				return true;
		}
	}
	// Created by : Sagar

	public static String Wait_For_File_To_Download(String downloadDirPath, final String ext, String name)
			throws Exception {
		Util.Report_Log(Status.INFO, "downloading file in dir : " + downloadDirPath);
		boolean flag = false;
		File dir = new File(downloadDirPath);
		File[] files = null;
		String nameFromFile = null;
		int count = 0;
		while (true) {
			if (count <= 30) {
				Thread.sleep(5000);
				count++;
			} else {
				throw new Exception("timeout for file to download");
			}
			files = dir.listFiles(new FilenameFilter() {
				// @Override
				@Override
				public boolean accept(File dir, String name) {
					if (name.lastIndexOf('.') > 0) {
						// get last index for '.' char
						int lastIndex = name.lastIndexOf('.');
						// get extension
						String str = name.substring(lastIndex);
						// match path name extension
						return str.equals(ext);
					}
					return false;
				}
			});
			if ((files != null) && (files.length > 0)) {
				for (int i = 0; i < files.length; i++) {
					String strWithPath = files[i].toString();

					Util.Report_Log(Status.INFO, "Full Name with path is:" + strWithPath);
					 nameFromFile = files[i].getName();
						//int lastIndex = strWithPath.lastIndexOf('/');
						//nameFromFile = strWithPath.substring(lastIndex).substring(1).replaceAll("\\s+", "");

						Util.Report_Log(Status.INFO, "Name is from File:" + nameFromFile);
						Util.Report_Log(Status.INFO, "Expected Name is:" + name);
						// String NAme = "\\" + name;

						if (name.substring(0, 9).equalsIgnoreCase("Annotated")) {
							if (name.equalsIgnoreCase(nameFromFile) || name.contains(nameFromFile)
									|| name.contains(nameFromFile)
									|| name.substring(9).trim().equalsIgnoreCase(nameFromFile)) {
								Util.Report_Log(Status.INFO, "Found Downloaded File");
								flag = true;
								break;
							}
						} else {
							if (nameFromFile.contains(name)) {
								Util.Report_Log(Status.INFO, "Found Downloaded File");
								flag = true;
								break;
							} else {
								continue;
							}
						}
				
				}

			}
			if (flag)
				break;
		}
		return nameFromFile;
	}
	// Created by : Sagar

	public static boolean Unzip_File(String zipped_File_Path, String unZipped_Folder_Path) {
		File dir = new File(unZipped_Folder_Path);
		// create output directory if it doesn't exist
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis;
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipped_File_Path);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				System.out.println(fileName);
				File newFile = new File(unZipped_Folder_Path + File.separator + fileName);
				System.out.println("Unzipping to " + newFile.getAbsolutePath());
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				// close this ZipEntry
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
			// close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	// Created by : Sagar

	public static boolean Compare_Pdf_File(String target_Filepath, String target_Filepath2) throws IOException {
		final CompareResult result = new PdfComparator(target_Filepath, target_Filepath2).compare();
		boolean flag = false;
		if (result.isNotEqual()) {
			System.out.println("Differences found!");
			flag = false;
		}
		if (result.isEqual()) {
			System.out.println("No Differences found!");
			flag = true;
		}
		return flag;
	}

	public static void write_To_Xml_File(String xmlSource, String file_path)
			throws SAXException, ParserConfigurationException, IOException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));
		// Use a Transformer for output
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(file_path));
		transformer.transform(source, result);
	}

	public static void FileWriter(String filepath, String response, String filename) throws Exception {
		File_Utils.Folder_Creation(filepath);
		FileWriter fw = new FileWriter(new File(filepath + "/" + filename));
		fw.write(response);
		fw.close();
	}

	public static void Validate_Content_In_File(String[] text_To_Search, String file_Path) throws IOException {
		File file = new File(file_Path);
		Util.Report_Log(Status.INFO, "Validating text precence in file - " + file_Path);
		boolean flag = false;
		String text_Not_Found = "";
		for (String str : text_To_Search) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(str)) {
					flag = true;
					break;
				}
			}
			br.close();
			if (!flag)
				text_Not_Found += str;
		}
		Assert.assertTrue(text_Not_Found.isEmpty(), "The following text not found - " + text_Not_Found);
		Util.Report_Log(Status.PASS, "Text List is present in file ");

	}

	public static ArrayList<String> Column_list_From_Csv(String filepath) throws Exception {
		String line = "";
		ArrayList<String> col_list = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		while ((line = br.readLine()) != null) // returns a Boolean value
		{
			for (int i = 0; i < line.split(",").length; i++) {
				col_list.add(line.split(",")[i]);
			}
			break;
		}
		return col_list;
	}

	public static boolean Create_Unzip(String ZipPath, String Extract_Location) throws IOException, ZipException {
		Folder_Creation(Extract_Location);
		ZipFile zipFile = new ZipFile(ZipPath);
		zipFile.extractAll(Extract_Location);
		return true;
	}

	public static boolean Create_Unzip_WithFilename(String filename, String ZipPath, String Extract_Location)
			throws IOException, ZipException {
		Folder_Creation(Extract_Location);
		ZipFile zipFile = new ZipFile(ZipPath);
		zipFile.extractFile(filename, Extract_Location);
		return true;
	}

	public static void extractFileFromZip(String inPath, String filename, String unzipped_Folder_Path) {
		boolean unzipped=false;
		try {
			ZipFile zipFile = new ZipFile(inPath);
			List fileHeaderList = zipFile.getFileHeaders();
			FileHeader fileHeader = null;
			for (int i = 0; i < fileHeaderList.size(); i++) {
				fileHeader = (FileHeader) fileHeaderList.get(i);
				if (fileHeader.getFileName().contains(filename)) {
					zipFile.extractFile(fileHeader, unzipped_Folder_Path);
					zipFile.removeFile(fileHeader.getFileName());
					break;
				}
			}
			Dashboard_Constants.filename = fileHeader.getFileName();
			unzipped= true;
		} catch (ZipException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(unzipped, "Unable to unzipped folder " + unzipped_Folder_Path);
	}

	public static ArrayList<String> Record_list_From_Csv(String filepath) throws Exception {
		ArrayList<String> csv_Record = new ArrayList<String>();
		Path pathToFile = Paths.get(filepath);
		try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
			String line = br.readLine();
			while (line != null) {
				line = br.readLine();
				csv_Record.add(line);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return csv_Record;
	}
}
