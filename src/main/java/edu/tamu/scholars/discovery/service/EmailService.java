package edu.tamu.scholars.discovery.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MailConfig;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    private final MailConfig mailConfig;

    EmailService(JavaMailSender emailSender, MailConfig mailConfig) {
        this.emailSender = emailSender;
        this.mailConfig = mailConfig;
    }

    public void send(String to, String subject, String message) {
        send(to, subject, message, mailConfig.getFrom(), mailConfig.getReplyTo());
    }

    private void send(String to, String subject, String message, String from, String replyTo) {
        emailSender.send(createMimeMessagePreparator(to, subject, message, from, replyTo));
    }

    MimeMessagePreparator createMimeMessagePreparator(
            String to,
            String subject,
            String message,
            String from,
            String replyTo) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setReplyTo(replyTo);
            messageHelper.setSubject(subject);
            messageHelper.setText(message, true);
        };
    }

}
