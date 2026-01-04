package com.fiap.msnotificacao.adapters.inbound.servicebus;

import com.fiap.msnotificacao.application.usecase.NotificarFeedbackCriticoUseCase;
import com.fiap.msnotificacao.domain.model.FeedbackMessage;
import com.fiap.msnotificacao.domain.service.EmailTemplateService;
import com.fiap.msnotificacao.ports.outbound.EmailPort;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.mockito.Mockito.*;

class NotificarCriticasFunctionTest {

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
        message.setCreatedAt(Instant.parse("2025-12-23T15:00:00Z"));

        when(templateService.gerarHtml(any()))
                .thenReturn("<html>email</html>");

        useCase.executar(message);

        verify(emailPort).enviarEmailFeedbackCritico(eq(message), any());
    }

}

