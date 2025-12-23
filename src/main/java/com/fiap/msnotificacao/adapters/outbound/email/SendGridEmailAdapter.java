package com.fiap.msnotificacao.adapters.outbound.email;

import com.fiap.msnotificacao.domain.model.FeedbackMessage;
import com.fiap.msnotificacao.ports.outbound.EmailPort;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

public class SendGridEmailAdapter implements EmailPort {

    @Override
    public void enviarEmail(FeedbackMessage msg, String html) throws Exception {

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));

        Mail mail = new Mail(
                new Email(System.getenv("SENDGRID_FROM_EMAIL"), "Notificações Tech Challenge"),
                "Novo feedback recebido na plataforma",
                new Email(System.getenv("ADMIN_NOTIFICATION_EMAIL")),
                new Content("text/html", html)
        );

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        sg.api(request);
    }
}
