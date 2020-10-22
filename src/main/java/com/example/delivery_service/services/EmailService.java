package com.example.delivery_service.services;

import com.example.delivery_service.model.Entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    private final JavaMailSender emailSender;
    private final MailContentBuilder mailContentBuilder;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    public EmailService(JavaMailSender emailSender, MailContentBuilder mailContentBuilder) {
        this.emailSender = emailSender;
        this.mailContentBuilder = mailContentBuilder;
    }

    public void sendNewOrderMail(String subject, Order order){
        String message = messageSource.getMessage("mail.new.order.message", null, LocaleContextHolder.getLocale());

        //type: 0-sender, 1-recipient, 2-user
        String sendContent = mailContentBuilder.build(order, order.getCustomer(), 0, message);
        //String recContent = mailContentBuilder.build(order, order.getRecipient(), 1, message);
        //String userContent = mailContentBuilder.build(order, order.getUser(), 2, message);

        sendHtmlEmail(order.getCustomer().getEmail(), subject, sendContent);
        //sendHtmlEmail(order.getRecipient().getEmail(), subject, recContent);
        //sendHtmlEmail(order.getUser().getEmail(), subject, userContent);
    }

    public void sendHtmlEmail(String recipient, String subject, String content) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("info@delivery.service.com");
            //massageHelper.setTo(recipient);
            messageHelper.setTo("neky.d@seznam.cz");
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
        };

        emailSender.send(messagePreparator);
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
