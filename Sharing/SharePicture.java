package Sharing;

import java.io.File;



public class SharePicture {
	
	public static void main(String[] args)
	{
		
		GmailShare email = new GmailShare("jethva@purdue.edu", "m.jethva01@gmail.com", "Ganpati123456", "email message", "../sample.jpg");
		email.share();
		System.out.println("done");
	}
	
	
	
}
