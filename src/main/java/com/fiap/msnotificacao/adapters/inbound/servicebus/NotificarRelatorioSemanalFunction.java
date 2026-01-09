package com.fiap.msnotificacao.adapters.inbound.servicebus;

import com.fiap.msnotificacao.application.usecase.NotificarRelatorioSemanalUseCase;
import com.fiap.msnotificacao.adapters.outbound.email.SendGridEmailAdapter;
import com.fiap.msnotificacao.domain.model.RelatorioSemanalMessage;
import com.fiap.msnotificacao.domain.service.RelatorioSemanalEmailTemplateService;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

public class NotificarRelatorioSemanalFunction {

    private final NotificarRelatorioSemanalUseCase useCase;

    public NotificarRelatorioSemanalFunction() {
        this.useCase = new NotificarRelatorioSemanalUseCase(
                new SendGridEmailAdapter(),
                new RelatorioSemanalEmailTemplateService()
        );
    }

    public NotificarRelatorioSemanalFunction(NotificarRelatorioSemanalUseCase useCase) {
        this.useCase = useCase;
    }

    @FunctionName("NotificarRelatorioSemanal")
    public void run(
            @ServiceBusQueueTrigger(
                    name = "message",
                    queueName = "QUEUE_WEEKLY_REPORTt",
                    connection = "SERVICE_BUS_CONNECTION"
            ) RelatorioSemanalMessage message,
            ExecutionContext context
    ) {
        context.getLogger().info("Relatório semanal recebido");
        useCase.executar(message);
    }
}


