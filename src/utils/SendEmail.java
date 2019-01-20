package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail extends Thread{
    private String to;
    private String mesaj;
    private String subject;
    public SendEmail(String to,String message,String subject)
    {
        this.to=to;
        this.mesaj=message;
        this.subject=subject;
    }
    @Override
    public void run()
    {
        // Recipient's email ID needs to be mentioned.

        // Sender's email ID needs to be mentioned
        String from = "web@gmail.com";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(mesaj);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
//    public static void main(String [] args) {
//        SendEmail sendEmail=new SendEmail("fffae@gmail.com","messsss","bla");
//        sendEmail.start();
//
//    }
}
