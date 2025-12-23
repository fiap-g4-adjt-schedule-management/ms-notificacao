package com.fiap.msnotificacao.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackMessageTest {

    @Test
    void deveRetornarFalseQuandoCriticalForNull() {
        FeedbackMessage msg = new FeedbackMessage();
        msg.setCritical(null);

        assertFalse(msg.isCritical());
    }

    @Test
    void deveRetornarFalseQuandoCriticalForFalse() {
        FeedbackMessage msg = new FeedbackMessage();
        msg.setCritical(false);

        assertFalse(msg.isCritical());
    }

    @Test
    void deveRetornarTrueQuandoCriticalForTrue() {
        FeedbackMessage msg = new FeedbackMessage();
        msg.setCritical(true);

        assertTrue(msg.isCritical());
    }
}

