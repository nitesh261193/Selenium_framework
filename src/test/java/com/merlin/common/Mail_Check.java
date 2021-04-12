package com.merlin.common;

import java.io.IOException;
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

import com.aventstack.extentreports.Status;

/**
 * @author Krishna Saini
 *
 */

public class Mail_Check  extends Annotation implements Init{

	 public static boolean Check_Mail_Recieved(String email_Id, String email_Password, String email_Subject , String email_Message,String item_Name) {
			Folder folder = null;
			Store store = null;
			Message lastUnreadMail = null;
			System.out.println("***READING MAILBOX...");
			try {
				Properties props = System.getProperties();
				props.setProperty("mail.store.protocol", "imaps");
				Session session = Session.getInstance(props, null);
				store = session.getStore("imaps");
				store.connect("imap.gmail.com", email_Id, email_Password);
				folder = store.getFolder("INBOX");
				folder.open(Folder.READ_ONLY);
				Message[] messages = folder.getMessages();
				System.out.println("No of Messages : " + folder.getMessageCount());
				System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());
				Flags seen = new Flags(Flags.Flag.SEEN);
				FlagTerm  unseenFlagTerm = new FlagTerm(seen, false);
				messages = folder.search(new SubjectTerm(email_Subject),folder.search(unseenFlagTerm));
				for (Message mail : messages) {
					if (!mail.isSet(Flags.Flag.SEEN)) {
						lastUnreadMail = mail;
						break;
					}
				}
				Message msg =lastUnreadMail;
				String strMailSubject = "", strMailBody = "";
					// Getting mail subject
				Object subject = msg.getSubject();
				System.out.println(subject);
					// Getting mail body
					// Casting objects of mail subject and body into String
				strMailSubject = (String) subject;
					// ---- This is what you want to do------
				strMailBody = toString(msg);
					if (strMailSubject.contains(email_Subject) && (strMailBody.contains(email_Message) || strMailBody.contains(item_Name))) {
						System.out.println("Email found ");
						return true;
					}
			} catch (MessagingException messagingException) {
				messagingException.printStackTrace();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			} finally {
				if (folder != null) {
					try {
						folder.close(true);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (store != null) {
					try {
						store.close();
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return false;
		}
	    public static String toString(Message message) throws MessagingException, IOException {
			Object content = message.getContent();
			if (content instanceof MimeMultipart) {
				MimeMultipart multipart = (MimeMultipart) content;
				if (multipart.getCount() > 0) {
					BodyPart part = multipart.getBodyPart(0);
					content = part.getContent();
				}
			}
			if (content != null) {
				return content.toString();
			}
			return null;
		}
	    public static void Mark_All_Mail_Read(String Username, String Password) throws MessagingException {
			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", Username, Password);
			Folder folder = store.getFolder("INBOX");
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
			Util.Report_Log(Status.INFO, "Unread Message:" + folder.getUnreadMessageCount());
		}
	    public static void Wait_For_Mail_To_Recieve(String Username , String Password, String Email_Subject, int wait_Time) throws MessagingException
		{
			int i = 0;
			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "imap");
			Session session ;
			Store store = null;
			Folder folder = null;
			try
			{
				do{
					session = Session.getDefaultInstance(props, null);
					store = session.getStore("imaps");
					store.connect("imap.gmail.com", Username, Password);
					folder = store.getFolder("INBOX");
					folder.open(Folder.READ_ONLY);
					folder.hasNewMessages();

					Message[] messages = null;

					Flags seen = new Flags(Flags.Flag.SEEN);
					FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
					messages = folder.search(new SubjectTerm(Email_Subject),folder.search(unseenFlagTerm));
					if(messages.length >= 1)
					{
						Util.Report_Log(Status.INFO,"Mail recieved after " + i + "minutes, wait over ...");
						break;
					}
					i++;
					visible.Pause(12);
				}while(i < wait_Time);

			} 
			finally{

				folder.close(false);
				store.close();
			}
		}
}