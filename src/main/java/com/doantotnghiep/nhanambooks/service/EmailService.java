package com.doantotnghiep.nhanambooks.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);

    void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException;

    void sendResetPasswordEmail(String email, String token);
}
