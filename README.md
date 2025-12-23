## 📌 MS-NOTIFICACAO – Serviço de Notificação (Serverless | Java)

Este repositório contém o microsserviço **ms-notificacao**, responsável por **consumir feedbacks críticos publicados em uma fila** e **notificar administradores via e-mail**.

O serviço foi desenvolvido em **Java**, utilizando **Azure Functions**, seguindo os princípios de **Serverless**, **Responsabilidade Única** e **Arquitetura Hexagonal (Ports & Adapters)**.

---

## 🧩 Responsabilidade do Serviço

Este microsserviço possui **uma única responsabilidade**:

> Consumir mensagens de feedback publicadas em uma fila e enviar notificações por e-mail quando o feedback for crítico.

O serviço **não**:

* cria feedbacks
* calcula médias
* gerencia usuários
* persiste dados

Essa separação é **intencional** e faz parte da proposta arquitetural do projeto.

---

## 🏗️ Arquitetura da Solução

* **Plataforma:** Microsoft Azure
* **Modelo:** Serverless
* **Runtime:** Java 17
* **Trigger:** Azure Service Bus Queue
* **Notificação:** SendGrid (E-mail)
* **Monitoramento:** Azure Functions Logs / Application Insights

### Arquitetura Hexagonal (Ports & Adapters)

O projeto foi estruturado seguindo **Arquitetura Hexagonal**, separando claramente responsabilidades:

* **Domain:** modelos e regras de negócio
* **Application (Use Cases):** orquestração da lógica
* **Ports:** contratos de integração
* **Adapters:** integrações externas (Azure, SendGrid)

Fluxo simplificado:

```
[Outro Microsserviço]
        |
        v
[Azure Service Bus Queue]
        |
        v
[Azure Function - Adapter Inbound]
        |
        v
[Use Case - NotificarFeedbackCritico]
        |
        v
[EmailPort]
        |
        v
[SendGrid Adapter]
```

---

## 📥 Contrato da Mensagem Consumida

A função consome mensagens da fila no seguinte formato JSON:

```json
{
  "id": "string",
  "rating": "number",
  "description": "string",
  "email": "string",
  "critical": "boolean",
  "createdAt": "ISO-8601"
}
```

### Regras de Processamento

* Se `critical = false` → nenhuma ação é tomada
* Se `critical = true` → um e-mail é enviado ao administrador
* Em caso de falha no envio do e-mail → a função lança exceção para permitir **retry automático** pelo Service Bus

---

## ✉️ Conteúdo do E-mail

O e-mail enviado contém:

* ID do feedback
* Data do feedback
* Descrição do problema
* Avaliação recebida (rating)
* Indicador visual de criticidade (⭐)
* E-mail informado pelo usuário (quando disponível)

---

## ⚙️ Configurações (Variáveis de Ambiente)

As configurações do serviço são feitas **exclusivamente por variáveis de ambiente**, sem hardcode no código.

Exemplo de configuração local (`local.settings.json`):

```json
{
  "IsEncrypted": false,
  "Values": {
    "FUNCTIONS_WORKER_RUNTIME": "java",
    "ServiceBusConnection": "<service-bus-connection-string>",
    "SENDGRID_API_KEY": "<sendgrid-api-key>",
    "SENDGRID_FROM_EMAIL": "alertas@techchallenge.com",
    "ADMIN_NOTIFICATION_EMAIL": "admin@techchallenge.com"
  }
}
```

⚠️ **Importante:**
O arquivo `local.settings.json` **não é versionado** e está listado no `.gitignore`.

---

## 🚀 Execução Local

### Pré-requisitos

* Java 17+
* Maven
* Azure Functions Core Tools
* Conta SendGrid configurada
* Namespace e fila no Azure Service Bus

### Passos para executar localmente

1. Compilar o projeto e gerar o staging da Function:

```bash
mvn clean package
```

2. Subir a Azure Function localmente:

```bash
mvn azure-functions:run
```

3. Enviar uma mensagem para a fila (`q-ms-notificacao`) via Azure Portal ou Service Bus Explorer.

---

## 🔐 Segurança

* Nenhuma credencial é versionada no código
* Secrets armazenados em **Application Settings** da Azure
* Acesso ao Service Bus via **Shared Access Policy**
* Comunicação assíncrona via fila

---

## 📊 Monitoramento

* Logs disponíveis via Azure Functions
* Falhas de envio de e-mail registradas nos logs
* Integração com **Application Insights** para observabilidade

---

## 🧪 Testes

O projeto possui **testes unitários** para:

* Domínio (`FeedbackMessage`)
* Serviços de domínio (template de e-mail)
* Use Cases
* Azure Function (adapter inbound)

Todos os testes são executados **sem dependência de Azure ou SendGrid reais**, utilizando mocks.

---

## ✅ Requisitos do Tech Challenge Atendidos

☑ Serverless
☑ Execução em Cloud
☑ Responsabilidade Única
☑ Notificação automática para problemas críticos
☑ Monitoramento
☑ Arquitetura bem definida e testável
☑ Separação clara de responsabilidades

---

## 📌 Observação Final

Este repositório contempla **exclusivamente** o microsserviço de notificação.
A criação das mensagens na fila e a infraestrutura Azure são responsabilidades de outros componentes da solução.
