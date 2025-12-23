package com.fiap.msnotificacao.domain.service;

import com.fiap.msnotificacao.domain.model.FeedbackMessage;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class EmailTemplateService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    .withZone(ZoneId.systemDefault());

    public String gerarHtml(FeedbackMessage msg) {

        String estrelas = "⭐".repeat(Math.max(1, msg.getRating()));
        String dataFormatada = formatarData(msg.getCreatedAt());

        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px;">
        
              <h2 style="color: #d32f2f;">🚨 Feedback Crítico Detectado</h2>
              <p>Um feedback crítico foi registrado no sistema e requer atenção.</p>

              <hr />

              <p><strong>ID do Feedback:</strong> %s</p>
              <p><strong>Data:</strong> %s</p>
              <p><strong>Avaliação:</strong> %s (%d/6)</p>

              <p><strong>E-mail informado pelo usuário:</strong>
                %s
              </p>

              <p><strong>Descrição:</strong></p>
              <p style="background:#f5f5f5; padding:10px; border-left:4px solid #d32f2f;">
                %s
              </p>

              <br />
              <p style="font-size: 12px; color: #777;">
                Esta é uma notificação automática do sistema Tech Challenge.
              </p>

            </div>
        """.formatted(
                msg.getId(),
                dataFormatada,
                estrelas,
                msg.getRating(),
                msg.getEmail() != null ? msg.getEmail() : "<em>Não informado</em>",
                msg.getDescription()
        );
    }

    private String formatarData(String createdAt) {
        try {
            Instant instant = Instant.parse(createdAt);
            return FORMATTER.format(instant);
        } catch (Exception e) {
            return "Data inválida";
        }
    }
}


