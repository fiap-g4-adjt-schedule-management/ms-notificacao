package com.fiap.msnotificacao.adapters.inbound.servicebus;

import com.fiap.msnotificacao.application.usecase.NotificarRelatorioSemanalUseCase;
import com.fiap.msnotificacao.domain.model.RelatorioSemanalMessage;
import com.microsoft.azure.functions.ExecutionContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.logging.Logger;

import static org.mockito.Mockito.*;

class NotificarRelatorioSemanalFunctionTest {

    @Test
    void deveExecutarUseCaseQuandoMensagemRecebida() {

        NotificarRelatorioSemanalUseCase useCaseMock =
                Mockito.mock(NotificarRelatorioSemanalUseCase.class);

        NotificarRelatorioSemanalFunction function =
                new NotificarRelatorioSemanalFunction(useCaseMock);

        RelatorioSemanalMessage message =
                Mockito.mock(RelatorioSemanalMessage.class);

        ExecutionContext context = mock(ExecutionContext.class);
        Logger logger = Logger.getLogger("test");
        when(context.getLogger()).thenReturn(logger);

        function.run(message, context);

        verify(useCaseMock, times(1)).executar(message);
    }
}



