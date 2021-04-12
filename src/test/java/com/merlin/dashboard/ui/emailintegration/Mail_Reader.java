package com.merlin.dashboard.ui.emailintegration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.search.SubjectTerm;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.merlin.common.Annotation;
import com.merlin.common.Constants;
import com.merlin.common.Init;
import com.merlin.common.Util;

public class Mail_Reader extends Annotation implements Init {
	private static final Logger log = Logger.getLogger(Mail_Reader.class);
	private static final String special_Char = "® , ™ , ℠ , © , & , • , — , – , ’ , ‘ , “ , ” , é , ó , á , í , ú , Ú , ñ , ¿ , ¡ , † , ‡ , § , ✓";
	private static final String Emojis = "🎯💰😀😃😄🐶🐱🐭🍏🍎🍐⚽🏀🏈🚗🚕🚙⌚📱💻💚💙💜🏳🏁❤️😂👍💥";
	private static String Email_text = "";
	private static final ArrayList<String> Email_Images = new ArrayList<>();
	private static final List<String> ignore_Text_List = Arrays.asList("https://cl.S7.exct.net/", "Exclusively for",
			"httpgetwrap|https://www.google.com");
	private static final String Email_html = "";

	public static String get_Email_Text() {
		return Email_text;
	}

	// Created By: Pooja (Last Modified By: Pooja )
	@Test(groups = { "Check_Mail" })
	@Parameters({ "email_Address", "email_Password", "email_Folder", "email_Subject", "email_Message", "email_Validate",
			"email_Subject_Validate" })
	public static void Check_Mail(String Username, String Password, String Email_Folder, String Email_Subject,
			String Email_Message, String Email_Validate, @Optional String Email_Subject_Validate) throws Exception {
		Email_Subject = (Email_Subject.contains("label")) ? Email_Subject.replace("label", Constants.label)
				: Email_Subject;
		Email_Subject = Email_Subject.replaceAll("copyProject", Constants.copy_Project_name);
		String[] subject = Email_Subject.split(",");
		// for validating multiple lines in the email body

		boolean isMailFound = false;
		Message lastUnreadMail = null;
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imap");

		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", Username, Password);
		Folder folder = store.getFolder(Email_Folder);
		try {
			folder.open(Folder.READ_WRITE);
			for (String email_Subject : subject) {
				int num_Of_Mail = 1;
				if (email_Subject.contains("count")) {
					num_Of_Mail = Integer.parseInt(email_Subject.split("count")[1]);
					email_Subject = email_Subject.split("count")[0];
				}

				Util.Report_Log(Status.INFO,
						"For Subject : " + email_Subject + "Number of mails expected - " + num_Of_Mail);

				Util.Report_Log(Status.INFO, "Total Message:" + folder.getMessageCount());
				Util.Report_Log(Status.INFO, "Unread Message:" + folder.getUnreadMessageCount());
				Message[] messages = null;

				Flags seen = new Flags(Flags.Flag.SEEN);
				FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
				messages = folder.search(new SubjectTerm(email_Subject), folder.search(unseenFlagTerm));
				if (Email_Validate.toLowerCase().equals("false")) {
					Assert.assertFalse(isMailFound, "Email with subject  - " + email_Subject + " recieved!");
					Util.Report_Log(Status.INFO, "Email with subject - " + email_Subject + " not recieved!");
				} else {

					Assert.assertEquals(messages.length, num_Of_Mail,
							"Number of mails not received as expected for subject - " + email_Subject);
					Util.Report_Log(Status.PASS, "Number of mails received as expected for subject - " + email_Subject);

					System.out.println(messages.length);
				}
				for (Message mail : messages) {
					if (!mail.isSet(Flags.Flag.SEEN)) {
						lastUnreadMail = mail;
						Util.Report_Log(Status.INFO, "Message Number is: " + lastUnreadMail.getMessageNumber());
						isMailFound = true;
						mail.setFlag(Flags.Flag.SEEN, true);
					}
				}
			}
			if (isMailFound && (!Email_Message.isEmpty())) {
				if (!Email_Message.startsWith("img")) {
					Email_text = get_Mail_Body(lastUnreadMail);
					validate_Text_In_Mail_Body(Email_text, Email_Message);
				} else
					validate_Images_Mail_Body(lastUnreadMail, Email_Message);
			}
			if (isMailFound && (Email_Subject_Validate != null)) {
				String email_subject = lastUnreadMail.getSubject();
				Util.Report_Log(Status.INFO, "Email Subject Obtained-" + email_subject);
				Assert.assertTrue(email_subject.contains(Email_Subject_Validate),
						"Email does not contains data in subject - " + Email_Subject_Validate);
				Util.Report_Log(Status.PASS, "Email does contains data in subject - " + Email_Subject_Validate);
			}
		} finally {

			folder.close(false);
			store.close();
			Email_text = "";
		}
	}

	// Created By: Pooja (Last Modified By: )
	public static String get_Mail_Body(Message message) throws Exception {
		if (message.isMimeType("text/plain")) {
			Email_text = message.getContent().toString();
		} else {
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			int count = mimeMultipart.getCount();
			System.out.println("Multiparts Count -" + count);
			for (int i = 0; i < count; i++) {
				BodyPart bodyPart = mimeMultipart.getBodyPart(i);

				if (bodyPart.isMimeType("text/plain")) {
					Email_text = Email_text + "\n" + bodyPart.getContent();
				} else if (bodyPart.isMimeType("text/html")) {
					String html = (String) bodyPart.getContent();
					Email_text = Email_text + "\n" + html;
				}
			}
		}

		return Email_text;
	}

	public static void validate_Images_Mail_Body(Message message, String image_Url)
			throws MessagingException, IOException {
		MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/html")) {
				String html = (String) bodyPart.getContent();
				Document doc = org.jsoup.Jsoup.parse(html);
				Elements links = doc.select("img");
				for (Element link : links) {
					Email_Images.add(link.attr("src"));
				}
			}
		}
		Assert.assertTrue(Email_Images.contains(image_Url.replace("img", "")),
				"Picture url not present in mail. Please validate mail for images");
		Util.Report_Log(Status.INFO, "Validated image are present in the mail");
	}

	// Created By: Pooja (Last Modified By: )

	public static void validate_Text_In_Mail_Body(String Email_Text_Received, String Text_To_Validate)
			throws MessagingException, IOException {
		if (!Text_To_Validate.contains("FILE")) {
			String[] message = Text_To_Validate.split("\\|");
			for (String msg : message) {
				msg = msg.replace("SPECIAL_CHAR", special_Char).replace("EMOJIS", Emojis).replace("CURRENT_DATE",
						Util.Get_Time_In_CST("MMM dd,yyyy h:mm", 0));
				System.out.println("message is -" + msg);
				Assert.assertTrue(Email_text.contains(msg), "Email does not contains the Message added - " + msg);
				Util.Report_Log(Status.INFO, "Email does contains the Message added - " + msg);
			}
		} else
			validate_Mail_Body_With_File(Email_Text_Received, Text_To_Validate);

	}

	// Created By: Pooja (Last Modified By: )
	public static void validate_Mail_Body_With_File(String Email_Text_Received, String Text_To_Validate) {
		String file_Path = Text_To_Validate.split("-")[1];
		Util.Report_Log(Status.INFO, "Validating text in received mail with the base file " + file_Path);
		File file = new File(file_Path);
		String data_From_File = "";
		BufferedReader br;
		ArrayList<String> base_List = new ArrayList<>();
		ArrayList<String> test_List = new ArrayList<>();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
			String st;
			while ((st = br.readLine()) != null)
				data_From_File += st + "||";
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		data_From_File.replaceAll("CURRENT_DATE", Util.Get_Time_In_CST("MMM dd,yyyy h:mm", 0));
		base_List.addAll(Arrays.asList(data_From_File.split("\\|\\|")));
		test_List.addAll(Arrays.asList(Email_Text_Received.split("\\r?\\n")));
		ArrayList<String> common_Data = new ArrayList<String>(test_List);
		common_Data.retainAll(base_List);
		test_List.removeAll(common_Data);
		base_List.removeAll(common_Data);
		for (String ignore : ignore_Text_List) {
			test_List.removeIf(s -> s.contains(ignore));
			base_List.removeIf(s -> s.contains(ignore));
		}
		Assert.assertTrue((test_List.isEmpty() && base_List.isEmpty()),
				"Mail data received is not as expected . Base data missing - " + base_List + " Test data missing - "
						+ test_List);
		Util.Report_Log(Status.INFO, "Mail received is in the format as expected  (coming from base file)");
	}

	// Created By: Pooja (Last Modified By: )
	@Test(groups = { "Mark_All_Mail_Read" })
	@Parameters({ "email_Address", "email_Password", "email_Folder" })
	public static void Mark_All_Mail_Read(String Username, String Password, String Email_Folder)
			throws MessagingException {

		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");

		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", Username, Password);

		Folder folder = store.getFolder(Email_Folder);
		folder.open(Folder.READ_WRITE);

		Util.Report_Log(Status.INFO, "Total Message:" + folder.getMessageCount());
		Util.Report_Log(Status.INFO, "Unread Message:" + folder.getUnreadMessageCount());
		Message[] messages = null;

		Flags seen = new Flags(Flags.Flag.SEEN);
		FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
		messages = folder.search(unseenFlagTerm);
		Util.Report_Log(Status.INFO, "Marking all mails as read. ");
		for (Message mail : messages) {
			if (!mail.isSet(Flags.Flag.SEEN)) {
				mail.setFlag(Flags.Flag.SEEN, true);
			}
		}
		folder.close(false);
		store.close();

		Util.Report_Log(Status.PASS, "Unread Message after marking read:" + folder.getUnreadMessageCount());
	}

	// Created By : Pooja Chopra(Last Modified By: Pooja)
	@Test(groups = { "Wait_For_Mail_To_Recieve" })
	@Parameters({ "email_Address", "email_Password", "email_Folder", "email_Subject", "wait_Time" })
	public static void Wait_For_Mail_To_Recieve(String Username, String Password, String Email_Folder,
			String Email_Subject, int wait_Time) throws MessagingException {
		Email_Subject = (Email_Subject.contains("label")) ? Email_Subject.replace("label", Constants.label)
				: Email_Subject;

		Email_Subject = Email_Subject.replaceAll("copyProject", Constants.copy_Project_name);
		String[] subject = Email_Subject.split(",");
		// for validating multiple lines in the email body

		int i = 0;
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imap");
		boolean email_Recieved = false;
		Session session;
		Store store = null;

		Folder folder = null;
		try {
			do {
				session = Session.getDefaultInstance(props, null);
				store = session.getStore("imaps");
				store.connect("imap.gmail.com", Username, Password);
				folder = store.getFolder(Email_Folder);
				folder.open(Folder.READ_WRITE);
				for (String email_Subject : subject) {
					if (email_Subject.contains("count")) {
						email_Subject = email_Subject.split("count")[0];
					}
					folder.hasNewMessages();
					Util.Report_Log(Status.INFO, "Waiting for mail to be recieved for Subject : " + email_Subject);
					Message[] messages = null;
					Flags seen = new Flags(Flags.Flag.SEEN);
					FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
					messages = folder.search(new SubjectTerm(email_Subject), folder.search(unseenFlagTerm));
					if (messages.length >= 1) {
						email_Recieved = true;
						Util.Report_Log(Status.PASS, "Mail recieved after " + i + "minutes, wait over ...");
						break;
					}
				}
				if (email_Recieved) {
					break;
				}
				i++;
				visible.Pause(60);
			} while (i < wait_Time);
			
		} finally {
			folder.close(false);
			store.close();
		}
	}
}