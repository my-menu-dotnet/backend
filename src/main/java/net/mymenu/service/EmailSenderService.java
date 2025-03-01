package net.mymenu.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailSenderService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${aws.ses.from-email}")
    private String fromEmail;

    @Autowired
    public EmailSenderService(AmazonSimpleEmailService amazonSimpleEmailService) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
    }

    @Async
    public void sendEmail(String to, String subject, String template, Context body) {
        try {
            String htmlBody = templateEngine.process(template, body);

            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(to))
                    .withMessage(new Message()
                            .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBody)))
                            .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                    .withSource(fromEmail);

            amazonSimpleEmailService.sendEmail(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
