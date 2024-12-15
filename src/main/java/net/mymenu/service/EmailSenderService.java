package net.mymenu.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private static final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.email.from}")
    private String fromEmail;

    @Async
    public void sendEmail(Personalization personalization, String templateId) {

        Content content = new Content("text/html", "<--! Email content -->");
        Mail mail = new Mail();

        mail.setFrom(new Email(fromEmail));
        mail.addContent(content);
        mail.setTemplateId(templateId);

        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(sendGridApiKey);

        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            logger.info("Email sent to: " + personalization.getTos().get(0).getEmail());
            logger.info("Email response: " + response.getStatusCode());
            logger.info("Email response: " + response.getBody());
            logger.info("Email response: " + response.getHeaders());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
