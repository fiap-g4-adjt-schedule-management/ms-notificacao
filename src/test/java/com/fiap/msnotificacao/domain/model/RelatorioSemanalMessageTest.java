package com.fiap.msnotificacao.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelatorioSemanalMessageTest {

    @Test
    void deveSerValidoQuandoCamposObrigatoriosPreenchidos() {

        RelatorioSemanalMessage msg = new RelatorioSemanalMessage();
        msg.setDateTimeEmission(String.valueOf(Instant.now()));
        msg.setRatingCountByDate(List.of(criarContagem("2025-12-22", 10)));
        msg.setRatingCountByUrgency(List.of(criarContagem("CRITICAL", 5)));

        assertTrue(msg.isValido());
    }

    @Test
    void deveSerInvalidoQuandoCamposObrigatoriosNulos() {

        RelatorioSemanalMessage msg = new RelatorioSemanalMessage();

        assertFalse(msg.isValido());
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

