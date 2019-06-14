package com.example.delivery_service.services;

import com.example.delivery_service.model.Entity.Order;
import com.example.delivery_service.model.Entity.Partner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mail", context);
    }

    /**type: 0-sender, 1-recipient, 2-user*/
    public String build(Order order, Partner user, int type, String message) {
        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("order", order);
        context.setVariable("user", user);
        return templateEngine.process("mail", context);
    }
}
