package com.example.demo.servis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.util.StringUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class DefaultEmailService {


    @Value("${email.sender}")
    private String senderEmail;

    @Autowired
    private JavaMailSenderImpl mailSender;

    public void send(String recipientEmail, String subject, String message) {
        if (StringUtils.isEmpty(recipientEmail) || StringUtils.isEmpty(subject) || StringUtils.isEmpty(message)) {
            throw new IllegalArgumentException("recipientEmail, subject, and message cannot be null or empty");
        }
        if (StringUtils.isEmpty(senderEmail)) {
            throw new IllegalArgumentException("senderEmail cannot be null or empty");
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(message, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}