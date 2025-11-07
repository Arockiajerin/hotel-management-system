package com.in.Hotel.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockEmailUtil {

    private static final Logger log = LoggerFactory.getLogger(MockEmailUtil.class);

    public void forgotMail(String to, String subject, String password) {
        log.info("Sending forgot password email simulation:");
        log.info("To: {}", to);
        log.info("Subject: {}", subject);
        log.info("Password: {}", password);
        log.info("Email sent successfully (simulated)");
    }

    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
        // Simulate email sending without actual SMTP
        log.info("📧 MOCK EMAIL SENT:");
        log.info("To: {}", to);
        log.info("Subject: {}", subject);
        log.info("Message: {}", text);
        log.info("CC List: {}", list);
        log.info("----------------------------------------");
    }
}