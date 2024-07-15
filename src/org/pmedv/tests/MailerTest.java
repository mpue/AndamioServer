package org.pmedv.tests;

import java.io.File;

import org.pmedv.mail.SMTPMailer;
import org.pmedv.mail.SMTPMailer.MessageType;


public class MailerTest {

	public static void main(String[] args) throws Exception {
		
		File f = new File("/home/pueski/devel/apache-tomcat-5.5.28/_groupware/attachments/active/1009/1009644.attachment");
		
		SMTPMailer mailer = new SMTPMailer("serverpool.org", "mpue", "cousteau08");
		
		mailer.setFrom("matthias@pueski.de");
		mailer.setSubject("Test123");
		mailer.setText("This is a test äöüß!");
		
		mailer.addAttachment(f);
		
		mailer.createMessage(MessageType.TEXT_PLAIN_MIME);
		
		mailer.addToRecipient("pueski@gmx.de");
		
		mailer.sendMessage();

	
		
		
	}
	
}
