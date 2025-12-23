package com.fiap.msnotificacao.domain.service;

import com.fiap.msnotificacao.domain.model.FeedbackMessage;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailTemplateServiceTest {

    @Test
    void deveGerarHtmlComDadosDoFeedback() {

        FeedbackMessage msg = new FeedbackMessage();
        msg.setId("123");
        msg.setRating(1);
        msg.setDescription("Erro grave");
        msg.setEmail("user@email.com");
        msg.setCreatedAt(Instant.now());

        EmailTemplateService service = new EmailTemplateService();

        String html = service.gerarHtml(msg);

        assertTrue(html.contains("Feedback Crítico"));
        assertTrue(html.contains("123"));
        assertTrue(html.contains("Erro grave"));
        assertTrue(html.contains("⭐"));
        assertTrue(html.contains("user@email.com"));
    }
}
