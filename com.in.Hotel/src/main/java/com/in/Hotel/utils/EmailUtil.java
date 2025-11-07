package com.in.Hotel.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class EmailUtil {

    @Autowired
    private JavaMailSender emailSender;

    private static final Logger log = LoggerFactory.getLogger(EmailUtil.class);

    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("arockiajerin2003@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            if (list != null && !list.isEmpty()) {
                message.setCc(getCcArray(list));
            }

            emailSender.send(message);
            log.info("Email sent successfully to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", to, e.getMessage());
            // Don't throw exception - just log it
            // This allows the main user update functionality to continue working
        }
    }

    private String[] getCcArray(List<String> ccList) {
        return ccList.toArray(new String[0]);
    }

    public void forgotMail(String to,String subject,String password) throws MessagingException {
        log.info("📧 Attempting to send password email to: {}", to);
        MimeMessage message =emailSender.createMimeMessage();
        MimeMessageHelper helper =new MimeMessageHelper(message,true);
        helper.setFrom("arockiajerin2003@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg = "<p><b>Your Login details for Hotel Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
        message.setContent(htmlMsg,"text/html");
        emailSender.send(message);
        log.info("✅ Email sent successfully to: {}", to);
        log.info("✅ Simple email sent successfully to: {}", to);
    }
}