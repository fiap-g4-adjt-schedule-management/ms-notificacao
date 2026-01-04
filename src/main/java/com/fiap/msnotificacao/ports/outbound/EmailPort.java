package com.fiap.msnotificacao.ports.outbound;

import com.fiap.msnotificacao.domain.model.FeedbackMessage;

public interface EmailPort {

    void enviarEmailFeedbackCritico(FeedbackMessage msg, String html) throws Exception;

    void enviarEmailRelatorioSemanal(String html) throws Exception;
}
