package com.fiap.msnotificacao.application.usecase;

import com.fiap.msnotificacao.domain.model.FeedbackMessage;
import com.fiap.msnotificacao.domain.service.EmailTemplateService;
import com.fiap.msnotificacao.ports.outbound.EmailPort;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NotificarFeedbackCriticoUseCaseTest {

    @Test
    void naoDeveEnviarEmailQuandoNaoForCritico() throws Exception {

        EmailPort emailPort = mock(EmailPort.class);
        EmailTemplateService templateService = new EmailTemplateService();

        NotificarFeedbackCriticoUseCase useCase =
                new NotificarFeedbackCriticoUseCase(emailPort, templateService);

        FeedbackMessage msg = new FeedbackMessage();
        msg.setCritical(false);

        useCase.executar(msg);

        verify(emailPort, never()).enviarEmail(any(), any());
    }

    @Test
    void deveEnviarEmailQuandoForCritico() throws Exception {

        EmailPort emailPort = mock(EmailPort.class);
        EmailTemplateService templateService = mock(EmailTemplateService.class);

        NotificarFeedbackCriticoUseCase useCase =
                new NotificarFeedbackCriticoUseCase(emailPort, templateService);

        FeedbackMessage message = new FeedbackMessage();
        message.setId("fb-123");
        message.setRating(1);
        message.setDescription("Problema grave");
        message.setEmail("teste@email.com");
        message.setCritical(true);
        message.setCreatedAt(Instant.parse("2025-12-23T15:00:00Z")); //

        when(templateService.gerarHtml(any()))
                .thenReturn("<html>email</html>");

        useCase.executar(message);

        verify(emailPort).enviarEmail(eq(message), any());
    }


    @Test
    void deveLancarExceptionQuandoFalharEnvioEmail() throws Exception {

        EmailPort emailPort = mock(EmailPort.class);
        EmailTemplateService templateService = mock(EmailTemplateService.class);

        NotificarFeedbackCriticoUseCase useCase =
                new NotificarFeedbackCriticoUseCase(emailPort, templateService);

        FeedbackMessage message = new FeedbackMessage();
        message.setId("fb-erro");
        message.setRating(1);
        message.setDescription("Erro no envio");
        message.setCritical(true);
        message.setCreatedAt(Instant.parse("2025-12-23T15:00:00Z"));

        when(templateService.gerarHtml(any()))
                .thenReturn("<html>email</html>");

        doThrow(new RuntimeException("Falha SendGrid"))
                .when(emailPort)
                .enviarEmail(any(), any());

        assertThrows(RuntimeException.class, () -> useCase.executar(message));
    }

}

