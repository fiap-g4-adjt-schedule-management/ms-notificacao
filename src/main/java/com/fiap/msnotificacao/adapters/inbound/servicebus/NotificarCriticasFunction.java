package com.fiap.msnotificacao.adapters.inbound.servicebus;

import com.fiap.msnotificacao.application.usecase.NotificarFeedbackCriticoUseCase;
import com.fiap.msnotificacao.adapters.outbound.email.SendGridEmailAdapter;
import com.fiap.msnotificacao.domain.model.FeedbackMessage;
import com.fiap.msnotificacao.domain.service.EmailTemplateService;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

public class NotificarCriticasFunction {

    private final NotificarFeedbackCriticoUseCase useCase;

    public NotificarCriticasFunction() {
        this.useCase = new NotificarFeedbackCriticoUseCase(
                new SendGridEmailAdapter(),
                new EmailTemplateService()
        );
    }

    public NotificarCriticasFunction(NotificarFeedbackCriticoUseCase useCase) {
        this.useCase = useCase;
    }

    @FunctionName("NotificarCriticas")
    public void run(
            @ServiceBusQueueTrigger(
                    name = "message",
                    queueName = "%QUEUE_CRITICAL_NOTIFICATION%",
                    connection = "SERVICE_BUS_CONNECTION"
            ) FeedbackMessage message,
            ExecutionContext context
    ) {
        context.getLogger().info("[INFO] Mensagem recebida da fila Service Bus");
        context.getLogger().info("[INFO] Payload recebido: " + message);

        useCase.executar(message);
    }
}


