package com.fiap.msnotificacao.application.usecase;

import com.fiap.msnotificacao.domain.model.RelatorioSemanalMessage;
import com.fiap.msnotificacao.domain.service.RelatorioSemanalEmailTemplateService;
import com.fiap.msnotificacao.ports.outbound.EmailPort;

import java.util.logging.Logger;

public class NotificarRelatorioSemanalUseCase {

    private final EmailPort emailPort;
    private final RelatorioSemanalEmailTemplateService templateService;
    private final Logger logger = Logger.getLogger(NotificarRelatorioSemanalUseCase.class.getName());

    public NotificarRelatorioSemanalUseCase(
            EmailPort emailPort,
            RelatorioSemanalEmailTemplateService templateService
    ) {
        this.emailPort = emailPort;
        this.templateService = templateService;
    }

    public void executar(RelatorioSemanalMessage message) {

        logger.info("[INFO] Processando relatório semanal");

        String html = templateService.gerarHtml(message);

        try {
            emailPort.enviarEmailRelatorioSemanal(html);
            logger.info("[SUCESSO] Relatório semanal enviado por e-mail");
        } catch (Exception e) {
            logger.severe("[ERRO] Falha ao enviar relatório semanal");
            throw new RuntimeException(e);
        }
    }
}

