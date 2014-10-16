echo "building..."
javac -cp  commons-email-1.3.3.jar:javax.mail.jar:. SharePicture.java GmailShare.java ShareInterface.java 
echo "running..."
java -cp  commons-email-1.3.3.jar:javax.mail.jar:. SharePicture
