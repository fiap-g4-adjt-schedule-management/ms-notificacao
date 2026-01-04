package com.fiap.msnotificacao.adapters.outbound.email;

import com.fiap.msnotificacao.domain.model.FeedbackMessage;
import com.fiap.msnotificacao.ports.outbound.EmailPort;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

public class SendGridEmailAdapter implements EmailPort {

    private SendGrid client() {
        return new SendGrid(System.getenv("SENDGRID_API_KEY"));
    }

    private Email from() {
        return new Email(
                System.getenv("SENDGRID_FROM_EMAIL"),
                "Notificações Tech Challenge"
        );
    }

    private Email toAdmin() {
        return new Email(System.getenv("ADMIN_NOTIFICATION_EMAIL"));
    }

    @Override
    public void enviarEmailFeedbackCritico(FeedbackMessage msg, String html) throws Exception {

        Mail mail = new Mail(
                from(),
                "Novo feedback recebido na plataforma",
                toAdmin(),
                new Content("text/html", html)
        );

        enviar(mail);
    }

    @Override
    public void enviarEmailRelatorioSemanal(String html) throws Exception {

        Mail mail = new Mail(
                from(),
                "Relatório semanal de feedbacks",
                toAdmin(),
                new Content("text/html", html)
        );

        enviar(mail);
    }

    private void enviar(Mail mail) throws Exception {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        client().api(request);
    }
}

