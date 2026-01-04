package com.fiap.msnotificacao.application.usecase;

import com.fiap.msnotificacao.domain.model.FeedbackMessage;
import com.fiap.msnotificacao.domain.service.EmailTemplateService;
import com.fiap.msnotificacao.ports.outbound.EmailPort;

import java.util.logging.Logger;

public class NotificarFeedbackCriticoUseCase {

    private final EmailPort emailPort;
    private final EmailTemplateService templateService;
    private final Logger logger = Logger.getLogger(NotificarFeedbackCriticoUseCase.class.getName());

    public NotificarFeedbackCriticoUseCase(
            EmailPort emailPort,
            EmailTemplateService templateService
    ) {
        this.emailPort = emailPort;
        this.templateService = templateService;
    }

    public void executar(FeedbackMessage message) {

        logger.info("[INFO] Processando feedback ID" + message.getId());

        if (!mensagemValida(message)) {
            logger.severe("[ERRO] Payload inválido recebido" + message);
            return;
        }

        if (!message.isCritical()) {
            logger.info("[INFO] Feedback " + message.getId() + " não é crítico. Nenhuma ação necessária.");
            return;
        }

        logger.info("[INFO] Feedback crítico detectado. Gerando e-mail.");

        String html = templateService.gerarHtml(message);

        try {
            logger.info("[INFO] Enviando e-mail de notificação para adminstrador.");
            emailPort.enviarEmailFeedbackCritico(message, html);
            logger.info("SUCESSO] E-mail de notificação enviado ao administrador.");
        } catch (Exception e) {
            logger.severe("[ERRO] Falha ao enviar e-mail para o feedback ID" + message.getId() + "-" + e.getMessage());
            throw new RuntimeException(e);
        }

        }

    private boolean mensagemValida(FeedbackMessage msg) {
        return msg.getId() != null
                && msg.getRating() != null
                && msg.getDescription() != null
                && msg.getCreatedAt() != null;
    }
}

