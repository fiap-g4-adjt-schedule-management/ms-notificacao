package com.fiap.msnotificacao.domain.service;

import com.fiap.msnotificacao.domain.model.Contagem;
import com.fiap.msnotificacao.domain.model.RelatorioSemanalMessage;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatorioSemanalEmailTemplateService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    .withZone(ZoneId.systemDefault());

    public String gerarHtml(RelatorioSemanalMessage msg) {

        StringBuilder html = new StringBuilder();

        String dataEmissao = formatarData(msg.getDateTimeEmission());

        html.append("""
            <div style="font-family: Arial, sans-serif; max-width: 700px; color:#333; line-height:1.5">

              <div style="
                margin-bottom:20px;
                padding:10px 14px;
                border-left:4px solid #1976d2;
                background:#fafafa;
              ">
                <span style="font-size:14px; color:#555;">
                  <strong>Data de emissão:</strong> %s
                </span>
             </div>
            
              <p>
                Resumo semanal dos feedbacks recebidos na plataforma, com volume diário e classificação por nível de urgência.
              </p>

              <hr style="border:none; border-top:1px solid #f0f0f0; margin:20px 0;" />

              <h3>⚠️ Urgência</h3>
              <p>
                Classificação de feedback por pontuação:
              </p>
              <ul>
                <li><strong> Critical:</strong> Feedbacks com pontuação <strong>igual ou inferior a 6</strong>,
                 que indicam problemas e exigem atenção imediata.
              </li>
                <li><strong> Normal:</strong> Feedbacks com pontuação <strong>superior a 6</strong>,
                relacionadas a sugestões ou pontos de melhoria.
              </li>
              </ul>

              <h3>📈 Quantidade de feedbacks por dia</h3>

              <table style="width:100%%; border-collapse: collapse; margin-bottom: 24px;">
                <thead>
                  <tr style="background:#f0f0f0;">
                    <th style="padding:8px; border:1px solid #ddd; text-align:left;">Data</th>
                    <th style="padding:8px; border:1px solid #ddd; text-align:right;">Quantidade</th>
                  </tr>
                </thead>
                <tbody>
            """.formatted(dataEmissao));

        adicionarLinhasTabela(html, msg.getRatingCountByDate());

        html.append("""
                </tbody>
              </table>

              <h3>🚨 Quantidade de feedbacks por urgência</h3>

              <table style="width:100%%; border-collapse: collapse;">
                <thead>
                  <tr style="background:#f0f0f0;">
                    <th style="padding:8px; border:1px solid #ddd; text-align:left;">Urgência</th>
                    <th style="padding:8px; border:1px solid #ddd; text-align:right;">Quantidade</th>
                  </tr>
                </thead>
                <tbody>
            """);

        adicionarLinhasTabela(html, msg.getRatingCountByUrgency());

        html.append("""
                </tbody>
              </table>

              <br />

              <p style="font-size:12px; color:#777;">
                Este é um relatório automático gerado pelo sistema Tech Challenge.
              </p>

            </div>
            """);

        return html.toString();
    }

    private String formatarData(String dateTimeEmission) {
        if (dateTimeEmission == null || dateTimeEmission.isBlank()) {
            return "Data não informada";
        }

        try {
            Instant instant = Instant.parse(dateTimeEmission);
            return FORMATTER.format(instant);
        } catch (Exception e) {
            return "Data inválida";
        }
    }


    private void adicionarLinhasTabela(StringBuilder html, List<Contagem> contagens) {
        if (contagens == null || contagens.isEmpty()) {
            html.append("""
                <tr>
                  <td colspan="2" style="padding:8px; border:1px solid #ddd; text-align:center;">
                    Nenhum dado disponível
                  </td>
                </tr>
            """);
            return;
        }

        for (Contagem c : contagens) {
            html.append("""
                <tr>
                  <td style="padding:8px; border:1px solid #ddd;">%s</td>
                  <td style="padding:8px; border:1px solid #ddd; text-align:right;">%d</td>
                </tr>
            """.formatted(c.getLabel(), c.getValue()));
        }
    }
}


