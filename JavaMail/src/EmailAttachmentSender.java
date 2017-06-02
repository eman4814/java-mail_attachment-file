
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Computer
 */
public class EmailAttachmentSender {

    public static void sendEmailWithAttachments(String host, String port,
            final String userName, final String password, String toAddress,
            String subject, String message, String[] attachFiles) {
        try {
            //set SMTP server Properties
            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.user", userName);
            properties.put("mail.password", password);

            //create a new session with an authenticator
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password); //To change body of generated methods, choose Tools | Templates.
                }
            };

            Session session = Session.getInstance(properties, auth);
            //create a new email message
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(userName));
            InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
            msg.setRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            //creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(message, "text/html");

            //creates multi part
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            //add attachments
            if (attachFiles != null && attachFiles.length > 0) {
                for (String filePath : attachFiles) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    attachPart.attachFile(filePath);
                    multipart.addBodyPart(attachPart);
                }
            }

            //set the multi parts as  email's contents
            msg.setContent(multipart);

            //sends the email
            Transport.send(msg);

        } catch (AddressException ex) {
            System.out.println("Error AddressException :" + ex);
        } catch (MessagingException ex) {
            System.out.println("Error Messaging exception @method sendEmail : " + ex);
        } catch (IOException ex) {
            System.out.println("Error IOException " + ex);
        }

    }

    public static void main(String[] args) {
        //SMTP INFO
        //SMTP yahoo = "smtp.mail.yahoo.com"
        //PORT yaho ="465";
        //live / hotmail SMPT = "smtp.live.com"
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "developer.fides@gmail.com"; // your email address
        String password = "fidesdeveloper"; // your email password

        //message info
        String mailTo = "yvandertorren@gmail.com"; // your friend email
        String subject = "Report Auto email Test";
        String message = "Report Auto Email test with Attachment File";

        //attachments
        String[] attachFiles = new String[3];
        attachFiles[0] = "src/mail/attachfiles/fsslogo.png";
        attachFiles[1] = "src/mail/attachfiles/PDFPresentation.pdf";
        attachFiles[2] = "src/mail/attachfiles/WindOfChange.mp3";

        try {
            sendEmailWithAttachments(host, port, mailFrom, password, mailTo, subject, message, attachFiles);
            System.out.println("Email Sent");
        } catch (Exception e) {
            System.out.println("Could not send email :"+e);
        }
    }

}
