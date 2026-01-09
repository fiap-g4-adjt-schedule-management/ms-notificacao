## 📌 MS-NOTIFICACAO – Serviço de Notificação (Serverless | Java)

Este repositório contém o microsserviço **ms-notificacao**, responsável por **consumir mensagens publicadas em filas** e **notificar administradores via e-mail**, de acordo com o tipo de evento recebido.

O serviço foi desenvolvido em **Java**, utilizando **Azure Functions**, seguindo os princípios de **Serverless**, **Responsabilidade Única** e **Arquitetura Hexagonal (Ports & Adapters)**.

---

## 🧩 Responsabilidade do Serviço

Este microsserviço possui **uma única responsabilidade**:

> Consumir mensagens de notificação publicadas em filas e enviar e-mails aos administradores.

Atualmente, o serviço trata **dois tipos de notificações**:

1. **Notificação de feedback crítico**
2. **Notificação de relatório semanal de avaliações**

O serviço **não**:

* cria feedbacks
* calcula métricas
* gera relatórios
* gerencia usuários
* persiste dados

Toda a lógica de geração das informações ocorre em outros microsserviços.
Este serviço atua **exclusivamente como consumidor e notificador**.

---

## 🏗️ Arquitetura da Solução

* **Plataforma:** Microsoft Azure
* **Modelo:** Serverless
* **Runtime:** Java 17
* **Triggers:** Azure Service Bus Queue
* **Notificação:** SendGrid (E-mail)
* **Monitoramento:** Azure Functions Logs / Application Insights

### Arquitetura Hexagonal (Ports & Adapters)

O projeto segue **Arquitetura Hexagonal**, separando claramente responsabilidades:

* **Domain:** modelos e regras de negócio
* **Application (Use Cases):** orquestração da lógica
* **Ports:** contratos de integração
* **Adapters:** integrações externas (Azure, SendGrid)

Fluxo simplificado:

```
[Outros Microsserviços]
          |
          v
[Azure Service Bus Queues]
          |
          v
[Azure Functions - Adapters Inbound]
          |
          v
[Use Cases]
          |
          v
[EmailPort]
          |
          v
[SendGrid Adapter]
```

---

## 🔔 Tipos de Notificação

### 1️⃣ Notificação de Feedback Crítico

#### Fila consumida

```
q-ms-critical-ratings
```

#### Contrato da Mensagem

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

#### Regras de Processamento

* Se `critical = false` → nenhuma ação é tomada
* Se `critical = true` → um e-mail é enviado ao administrador
* Em caso de falha no envio → exceção é lançada para permitir **retry automático** pelo Service Bus

#### Conteúdo do E-mail

* ID do feedback
* Data do feedback
* Descrição do problema
* Avaliação recebida (rating)
* Indicador visual de criticidade (⭐)
* E-mail informado pelo usuário (quando disponível)

---

### 2️⃣ Notificação de Relatório Semanal de Avaliações

#### Fila consumida

```
q-ms-weekly-report
```

#### Contrato da Mensagem

```json
{
  "ratingCountByDate": [
    { "label": "YYYY-MM-DD", "value": "number" }
  ],
  "ratingCountByUrgency": [
    { "label": "CRITICAL", "value": "number" },
    { "label": "NORMAL", "value": "number" }
  ],
  "dateTimeEmission": "ISO-8601"
}
```

#### Regras de Processamento

* A mensagem representa um **relatório semanal já consolidado**
* O serviço **não realiza cálculos**
* Um e-mail é enviado ao administrador contendo o resumo semanal
* Em caso de falha no envio → exceção é lançada para permitir retry automático

#### Conteúdo do E-mail

* Descrição do relatório semanal
* Explicação dos níveis de urgência
* Data de emissão do relatório
* Quantidade de avaliações por dia (tabela)
* Quantidade de avaliações por urgência (tabela)

---

## ⚙️ Configurações (Variáveis de Ambiente)

As configurações do serviço são feitas **exclusivamente por variáveis de ambiente**.

Exemplo (`local.settings.json`):

```json
{
  "IsEncrypted": false,
  "Values": {
    "FUNCTIONS_WORKER_RUNTIME": "java",
    "SERVICE_BUS_CONNECTION": "<service-bus-connection-string>",
    "SENDGRID_FROM_EMAIL": "xxxx@xxx.xxx",
    "ADMIN_NOTIFICATION_EMAIL": "xxxx@xxx.xxx",
    "QUEUE_CRITICAL_NOTIFICATION": "q-ms-critical-ratings",
    "QUEUE_WEEKLY_REPORT": "q-ms-weekly-report"
  }
}
```

⚠️ **Importante:**
O arquivo `local.settings.json` **não é versionado** e está listado no `.gitignore`.

| Nome da variável           | Para que serve            |
| -------------------------- | ------------------------- |
| `SENDGRID_API_KEY`         | Chave da API do SendGrid  |
| `SENDGRID_FROM_EMAIL`      | E-mail remetente          |
| `ADMIN_NOTIFICATION_EMAIL` | Destinatário              |
| `SERVICE_BUS_CONNECTION`     | Conexão com o Service Bus |

---

## 🚀 Execução Local

### Pré-requisitos

* Java 17+
* Maven
* Azure Functions Core Tools
* Conta SendGrid configurada
* Azure Service Bus com as filas configuradas

### Passos para executar localmente

1. Compilar o projeto:

```bash
mvn clean package
```

2. Executar a Azure Function:

```bash
mvn azure-functions:run
```

3. Publicar mensagens nas filas:

* `q-ms-critical-ratings`
* `q-ms-weekly-report`

---

## 🧪 Testes

O projeto possui **testes unitários** para:

* Modelos de domínio
* Templates de e-mail
* Use cases
* Azure Functions (adapters inbound)

Todos os testes são executados **sem dependência de Azure ou SendGrid reais**, utilizando mocks.

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
* Integração com **Application Insights**

---

## ✅ Requisitos do Tech Challenge Atendidos

☑ Serverless
☑ Execução em Cloud
☑ Responsabilidade Única
☑ Notificação automática de eventos
☑ Relatório semanal via mensageria
☑ Monitoramento
☑ Arquitetura bem definida e testável

---

## 📌 Observação Final

Este repositório contempla **exclusivamente o microsserviço de notificação**.
A geração dos feedbacks, métricas e relatórios, assim como a infraestrutura Azure, são responsabilidades de outros componentes da solução.
