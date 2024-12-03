package com.doantotnghiep.nhanambooks.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;


    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("booknhanam@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            javaMailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("booknhanam@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        message.setContent(htmlBody,"text/html;charset=utf-8");
        javaMailSender.send(message);
    }

    @Override
    public void sendResetPasswordEmail(String email, String token) {
        String subject = "Reset your password";
        String text = "To reset your password, click the link below:\n"
                + "http://localhost:8080/reset-password?token=" + token;
        sendSimpleEmail(email, subject, text);
    }
}
