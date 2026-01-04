package com.fiap.msnotificacao.application.usecase;

import com.fiap.msnotificacao.domain.model.RelatorioSemanalMessage;
import com.fiap.msnotificacao.domain.service.RelatorioSemanalEmailTemplateService;
import com.fiap.msnotificacao.ports.outbound.EmailPort;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificarRelatorioSemanalUseCaseTest {

    @Test
    void deveEnviarEmailQuandoRelatorioForValido() throws Exception {

        EmailPort emailPort = mock(EmailPort.class);
        RelatorioSemanalEmailTemplateService templateService =
                mock(RelatorioSemanalEmailTemplateService.class);

        when(templateService.gerarHtml(any())).thenReturn("<html>relatorio</html>");

        NotificarRelatorioSemanalUseCase useCase =
                new NotificarRelatorioSemanalUseCase(emailPort, templateService);

        RelatorioSemanalMessage msg = new RelatorioSemanalMessage();
        msg.setDateTimeEmission(String.valueOf(Instant.now()));
        msg.setRatingCountByDate(List.of());
        msg.setRatingCountByUrgency(List.of());

        useCase.executar(msg);

        verify(emailPort).enviarEmailRelatorioSemanal(any());
    }

    @Test
    void deveLancarExcecaoQuandoFalharEnvioEmail() throws Exception {

        EmailPort emailPort = mock(EmailPort.class);
        RelatorioSemanalEmailTemplateService templateService =
                mock(RelatorioSemanalEmailTemplateService.class);

        when(templateService.gerarHtml(any())).thenReturn("<html>relatorio</html>");
        doThrow(new RuntimeException("Erro SendGrid"))
                .when(emailPort).enviarEmailRelatorioSemanal(any());

        NotificarRelatorioSemanalUseCase useCase =
                new NotificarRelatorioSemanalUseCase(emailPort, templateService);

        RelatorioSemanalMessage msg = new RelatorioSemanalMessage();
        msg.setDateTimeEmission(String.valueOf(Instant.now()));
        msg.setRatingCountByDate(List.of());
        msg.setRatingCountByUrgency(List.of());

        assertThrows(RuntimeException.class, () -> useCase.executar(msg));
    }
}
