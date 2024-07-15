package org.pmedv.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * The <code>SMTPMailer</code> is a class capable of sending EMail messages via
 * the SMTP protocol.
 * </p> 
 * <p>
 * <b>Usage</b> : Create a new Instance of Mailer, set the corresponding attributes like from,
 * to and the text. At least one recipient is needed. If everything is prepared well, 
 * the according send() method can be called and the message is being sent.
 * </p>
 * <p>
 * <b>Example:</b>
 * <p>
 * <code>
 * 		<p>
 * 		SMTPMailer mailer = new SMTPMailer("your.mail.host","yourMailUser","yourPass");
 *		</p>
 *		<p>
 *		mailer.setFrom("your@emailaddress.com");<br>
 *		mailer.setSubject("Test");<br>
 *		mailer.setText("<b>I am an HTML message with an attachment.</b>");<br>
 *		mailer.setTo("your@recipient");<
 *		</p>
 *		<p>		
 *		File f1 = new File("/path/to/file1");<br>
 *		File f2 = new File("/path/to/file2");
 *		</p>
 *		<p>
 *		mailer.addAttachment(f1);<br>
 *		mailer.addAttachment(f2);
 *		</p>
 *		<p>
 *		mailer.createMessage(MessageType.TEXT_HTML_MIME);<br>	
 *		mailer.addCcRecipient("yourCC@recipient");<br>	
 *		mailer.sendMessage();
 *		</p>
 *</code>
 *</p>
 *
 * @author Matthias Pueski
 * 
 */
public class SMTPMailer {

	private static final Log log = LogFactory.getLog(SMTPMailer.class);
	
	/**
	 * The <code>MessageType</code> determines which type of message should be created.
	 */
	public static enum MessageType {
		
		/**
		 * Plain text message without attachments
		 */
		TEXT_PLAIN,
		
		/**
		 * HTML message without attachments
		 */
		TEXT_HTML,
		
		/**
		 * Plain text message with attachments
		 */
		TEXT_PLAIN_MIME,
		
		/**
		 * HTML message with attachments
		 */
		TEXT_HTML_MIME
		
	}
	
	private String host;
	private String from;
	private String to;
	private String username;
	private String password;
	private String text;
	private String subject;
	private ArrayList<File> attachments;
	private ArrayList<String> contentIds;
	private Session session;
	private MimeMultipart multipart;
	private BodyPart messageBodyPart;
	private Properties props;
	private MimeMessage message;
	
	/**
	 * Creates a new SMTP mailer in order to send emails.
	 * 
	 * @param host     the SMTP host used for sending mails
	 * @param username the username on the SMTP server
	 * @param password the password of the user on the SMTP server
	 * 
	 */
	public SMTPMailer(String host, String username, String password) {

		this.host = host;
		this.username = username;
		this.password = password;
		
		props = System.getProperties();
		props.put("mail.smtp.host", host);
		props.setProperty("mail.smtp.auth", "true");

	}

	/**
	 * <p>
	 * Creates a new HTML message with mime body part enabled
	 * </p>
	 * <p>
	 * Creates a HTML message with attachments enabled, the filenames need to be added as well 
	 * as the corresponding contentIDs which will be used inside the message body
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void createHTMLMimeMessage() throws Exception {
		
		session = Session.getDefaultInstance(props, null);
		// session.setDebug(true);

		message = new MimeMessage(session);

		// Fill its headers
		message.setSubject(subject);
		message.setFrom(new InternetAddress(from));
		// message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		// Create your new message part
		messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(text, "text/html; charset=\"iso-8859-15\"");

		// Create a related multi-part to combine the parts
		multipart = new MimeMultipart("related");
		multipart.addBodyPart(messageBodyPart);

		DataSource DataSources[] = new DataSource[attachments.size()];
		
		for (int i = 0; i < attachments.size(); i++) {
			
			// Create part for the file
			messageBodyPart = new MimeBodyPart();

			// Fetch the file and associate to part
			DataSources[i] = new FileDataSource(attachments.get(i));
			messageBodyPart.setDataHandler(new DataHandler(DataSources[i]));
			messageBodyPart.setFileName(attachments.get(i).getName());
			
			if (contentIds != null)			
				messageBodyPart.setHeader("Content-ID", contentIds.get(i));
			
			// Add part to multi-part
			multipart.addBodyPart(messageBodyPart);

		}

		message.setContent(multipart);
		
	}

	/**
	 * <p>
	 * Creates a new text message with mime body part enabled
	 * </p>
	 * <p>
	 * Creates a text message with attachments enabled, the filenames need to be added as well 
	 * as the corresponding contentIDs which will be used inside the message body
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void createTextMimeMessage() throws Exception {
		
		session = Session.getDefaultInstance(props, null);

		message = new MimeMessage(session);

		// Fill its headers
		message.setSubject(MimeUtility.encodeText(subject, "iso-8859-15", null));
		message.setFrom(new InternetAddress(from));

		// Create your new message part
		messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(text, "text/plain; charset=\"iso-8859-15\"");

		// Create a related multi-part to combine the parts
		multipart = new MimeMultipart("related");
		multipart.addBodyPart(messageBodyPart);

		DataSource DataSources[] = new DataSource[attachments.size()];
		
		for (int i = 0; i < attachments.size(); i++) {
			// Create part for the file
			messageBodyPart = new MimeBodyPart();

			// Fetch the file and associate to part
			DataSources[i] = new FileDataSource(attachments.get(i));
			messageBodyPart.setDataHandler(new DataHandler(DataSources[i]));
			messageBodyPart.setFileName(attachments.get(i).getName());
			
			if (contentIds != null)			
				messageBodyPart.setHeader("Content-ID", contentIds.get(i));
			
			// Add part to multi-part
			multipart.addBodyPart(messageBodyPart);

		}

		message.setContent(multipart);
		
	}	
	
	/**
	 * <p>
	 * Creates a new HTML message 
	 * </p>
	 * @throws Exception
	 */
	private void createHTMLMessage() throws MessagingException {

		session = Session.getDefaultInstance(props, null);

		try {
			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));

			if (subject != null) {
				message.setSubject(subject);
			}
			else {
				message.setSubject("(unknown subject)");
			}

			message.setContent(text, "text/plain; charset=\"iso-8859-15\"");

		}
		catch (MessagingException m) {
			throw m;
		}

	}

	/**
	 * <p>
	 * Creates a new plain text message 
	 * </p>
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	private void createTextMessage() throws MessagingException, UnsupportedEncodingException {

		session = Session.getDefaultInstance(props, null);

		try {
			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			// message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			if (subject != null) {
				message.setSubject(MimeUtility.encodeText(subject, "iso-8859-15", null));
			}
			else {
				message.setSubject("(unknown subject)");
			}

			message.setContent(text, "text/plain; charset=\"iso-8859-15\"");

		}
		catch (MessagingException m) {
			throw m;
		}

	}
	
	/**
	 * Adds a new recipient to the message
	 * 
	 * @param recipient
	 */
	public void addToRecipient(String recipient) {	
		
		if (message == null)
			throw new IllegalArgumentException("You must create a message first.");

		try {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
		}
		catch (MessagingException m) {
			log.error("Could not add recipient.");
			log.error(m.getMessage());
		}

	}
	
	/**
	 * Adds a new copy recipient to the message
	 * 
	 * @param recipient
	 */
	public void addCcRecipient(String recipient) {
		
		if (message == null)
			throw new IllegalArgumentException("You must create a message first.");
		
		try {
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(recipient));
		}
		catch (MessagingException m) {
			log.error("Could not add recipient.");			
			log.error(m.getMessage());
		}

	}

	/**
	 * Adds a new copy recipient to the message
	 * 
	 * @param recipient
	 */
	public void addBccRecipient(String recipient) {
		
		if (message == null)
			throw new IllegalArgumentException("You must create a message first.");
		
		try {
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress(recipient));
		}
		catch (MessagingException m) {
			log.error("Could not add recipient.");
			log.error(m.getMessage());
		}

	}
	
	/**
	 * Creates the message depending on the type and prepares
	 * it for send.
	 * 
	 * @param type the  type of message to create
	 * @throws Exception
	 */
	public void createMessage(MessageType type) throws Exception {
		
		if (type.equals(MessageType.TEXT_HTML)) {
			createHTMLMessage();
		}
		else if(type.equals(MessageType.TEXT_HTML_MIME)) {
			createHTMLMimeMessage();
		}
		else if(type.equals(MessageType.TEXT_PLAIN)) {
			createTextMessage();
		}
		else if(type.equals(MessageType.TEXT_PLAIN_MIME)) {
			createTextMimeMessage();
		}
		else
			throw new IllegalArgumentException("unknown message type");
		
	}

	/**
	 * Sends an eMail message via SMTP
	 * 
	 * @throws MessagingException
	 * @throws Exception
	 */
	public void sendMessage() throws Exception {

		if (message == null)
			throw new IllegalArgumentException("You must create a message first.");
		
		try {
			log.info("Initiating smtp transport");			
			Transport transport = session.getTransport("smtp");
			log.info("Connecting to smtp server.");
			transport.connect(host, username, password);
			log.info("Sending message.");
			transport.sendMessage(message, message.getAllRecipients());
			log.info("Closing smtp transport.");
			transport.close();			
		}
		catch (Exception e) {
			throw(e);
		}
		
		log.info("Success.");

	}
	
	/**
	 * Adds a new attachment to the message.
	 * 
	 * @param attachment the file to attach to this message
	 * 
	 */
	public void addAttachment(File attachment) {
		
		if (attachment == null)
			throw new IllegalArgumentException("Attachment file must not be null.");
		
		if (attachments == null)
			attachments = new ArrayList<File>();
		
		attachments.add(attachment);
		
	}

	/**
	 * @return Returns the from.
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from The from to set.
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return Returns the host.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host The host to set.
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text The text to set.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Returns the subject.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return Returns the to.
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to The to to set.
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param contentIds the contentIds to set
	 */
	public void setContentIds(ArrayList<String> contentIds) {
		this.contentIds = contentIds;
	}
	
	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		
		if (session != null)
			return session.getDebug();
		
		else
			return false;
		
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		
		if (session != null)
			session.setDebug(debug);
		
		else
			log.error("no existing session found.");
		
	}

}
