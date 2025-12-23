package com.fiap.msnotificacao.ports.outbound;

import com.fiap.msnotificacao.domain.model.FeedbackMessage;

public interface EmailPort {
    void enviarEmail(FeedbackMessage message, String html) throws Exception;
}