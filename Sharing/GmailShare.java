package Sharing;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;


public class GmailShare implements ShareInterface{
	
	static String EMAIL_SUBJECT = "Check out my DotVinci Drawing!";
	static String ATTACHMENT_DESC = "DotVinci";
	static String ATTACHMENT_IMG_TITLE = "DotVinci-Drawing";
	static String GMAIL_SMTP_URL = "smtp.googlemail.com";
	static int GMAIL_SMTP_PORT = 465;
	
	String to,from, message,imagePath;
	// from gmail account password
	String fromPassword;
	
	public GmailShare(String to,String from,String fromPassword, String message, String imagePath)
	{
		this.to=to;
	    this.imagePath = imagePath;
		this.message = message;
		this.from = from;
		this.fromPassword=fromPassword;
	}
	
	public void share()
	{
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName(GMAIL_SMTP_URL);
		email.setSmtpPort(GMAIL_SMTP_PORT);
		// needs gmail pass for from email
		email.setAuthenticator(new DefaultAuthenticator(from, fromPassword));
		email.setSSLOnConnect(true);
		if(imagePath!=null && !imagePath.isEmpty()){
		 EmailAttachment attachment = new EmailAttachment();
		  attachment.setPath(imagePath);
		  attachment.setDisposition(EmailAttachment.ATTACHMENT);
		  attachment.setDescription(ATTACHMENT_DESC);
		  attachment.setName(ATTACHMENT_IMG_TITLE);
		  try {
			email.attach(attachment);
		  } catch (Exception e1) {
			
			e1.printStackTrace();
		  }
		
		}
		
		try{
		email.setFrom(from);
		email.setSubject(EMAIL_SUBJECT);
		email.setMsg(message);
		email.addTo(to);
		email.send();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	
	
	
	
	
}
