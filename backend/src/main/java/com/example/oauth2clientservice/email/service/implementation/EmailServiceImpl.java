package com.example.oauth2clientservice.email.service.implementation;

import com.example.oauth2clientservice.email.model.Email;
import com.example.oauth2clientservice.email.model.EmailSender;
import com.example.oauth2clientservice.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailSender emailSender;

    @Override
    public void sendEmail(Email email) {
        try {
            Session session = createSession();
            MimeMessage msg = createMimeMessage(session, email);
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", emailSender.getHost());
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        emailSender.getUsername(),
                        emailSender.getPassword()
                );
            }
        };
        return Session.getInstance(props, auth);
    }

    private MimeMessage createMimeMessage(Session session, Email email) {
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(emailSender.getUsername(), "NoReply-JD"));
            msg.setReplyTo(InternetAddress.parse(emailSender.getUsername(), false));
            msg.setSubject(email.getSubject(), "UTF-8");
            msg.setContent(email.getBody(), "text/html; charset=utf-8");
            msg.setSentDate(new Date());
            msg.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email.getRecipient(), false)
            );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return msg;
    }
}
