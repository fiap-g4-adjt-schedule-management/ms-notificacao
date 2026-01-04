package com.fiap.msnotificacao.domain.service;

import com.fiap.msnotificacao.domain.model.Contagem;
import com.fiap.msnotificacao.domain.model.RelatorioSemanalMessage;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelatorioSemanalEmailTemplateServiceTest {

    private final RelatorioSemanalEmailTemplateService service =
            new RelatorioSemanalEmailTemplateService();

    @Test
    void deveGerarHtmlCompletoDoRelatorioSemanal() {

        RelatorioSemanalMessage msg = new RelatorioSemanalMessage();
        msg.setDateTimeEmission(String.valueOf(Instant.parse("2025-12-29T19:56:31Z")));

        msg.setRatingCountByDate(List.of(
                criarContagem("2025-12-22", 2),
                criarContagem("2025-12-23", 117)
        ));

        msg.setRatingCountByUrgency(List.of(
                criarContagem("CRITICAL", 380),
                criarContagem("NORMAL", 489)
        ));

        String html = service.gerarHtml(msg);

        assertNotNull(html);
        assertTrue(html.contains("2025-12-22"));
        assertTrue(html.contains("Urgência"));
        assertTrue(html.contains("CRITICAL"));
        assertTrue(html.contains("NORMAL"));
        assertTrue(html.contains("117"));
        assertTrue(html.contains("Tech Challenge"));
    }

    private Contagem criarContagem(String label, Integer value) {
        Contagem c = new Contagem();
        try {
            var labelField = Contagem.class.getDeclaredField("label");
            labelField.setAccessible(true);
            labelField.set(c, label);

            var valueField = Contagem.class.getDeclaredField("value");
            valueField.setAccessible(true);
            valueField.set(c, value);
        } catch (Exception e) {
            fail("Erro ao criar contagem para teste");
        }
        return c;
    }
}

