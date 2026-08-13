package com.hardwaredeals.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String from;
    private final String publicUrl;

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.mail.from:no-reply@hardwaredeals.local}") String from,
                        @Value("${app.public-url:http://localhost:8080}") String publicUrl) {
        this.mailSender = mailSender;
        this.from = from;
        this.publicUrl = publicUrl;
    }

    public void sendVerification(String email, String token) {
        send(email, "Verifique seu e-mail", "Use este link para verificar seu e-mail: " + publicUrl + "/verify-email?token=" + token);
    }

    public void sendPasswordReset(String email, String token) {
        send(email, "Redefinição de senha", "Use este link para redefinir sua senha: " + publicUrl + "/reset-password?token=" + token);
    }

    private void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
