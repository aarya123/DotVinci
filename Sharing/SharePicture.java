package Sharing;
import java.io.File;



public class SharePicture {
	
	public static void main(String[] args)
	{
		
		GmailShare email = new GmailShare("to_email", "from_email@gmail.com", "gmail_account_pass", "email message", "image_path");
		email.share();
		System.out.println("done");
	}
	
	
	
}
