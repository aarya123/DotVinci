javac *.java
javac -cp Sharing/commons-email-1.3.3.jar:Sharing/javax.mail.jar:. Main.java  Sharing/GmailShare.java Sharing/ShareInterface.java
java -cp Sharing/commons-email-1.3.3.jar:Sharing/javax.mail.jar:. Main
