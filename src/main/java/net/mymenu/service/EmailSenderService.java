package net.mymenu.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

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
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
