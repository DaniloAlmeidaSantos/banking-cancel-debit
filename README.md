# banking-cancel-debit

Projeto focado no cancelamento de débitos bancários, também permitindo a criação de débitos para facilitar testes e controle de regras de negócio.

## Instruções

> ⚠️ Observação: Por questões de consistência e integridade do domínio, o cancelamento só pode ser realizado para débitos previamente criados. O endpoint de criação (`POST /debits`) foi incluído para suportar esse fluxo de forma segura.

1. Crie um débito via o endpoint `POST /debits`. A resposta trará um UUID no campo `debitId`.
2. Com esse `debitId`, envie uma requisição para o endpoint `POST /debit/cancel`, informando-o no corpo da requisição.


## Funcionalidade Principal

- **Cancelar Débito:** Endpoint para cancelar débitos registrados.
- **Criar Débito:** (funcionalidade extra) Endpoint que permite criar um débito no sistema, mesmo não sendo o foco principal do projeto, para facilitar o controle e testes das regras de negócio.

## Arquitetura e Tecnologias

- **Java** – Lógica principal da aplicação
- **HCL/Terraform** – Provisionamento de infraestrutura
- **DynamoDB, SQS, API Gateway** – Serviços AWS simulados via Localstack
- **Localstack via Docker Compose** – Provisiona os serviços AWS localmente para desenvolvimento e testes

> _Obs.: Nenhum recurso real da AWS é utilizado — toda a infraestrutura é simulada localmente com Localstack._

## Como rodar o projeto localmente

### Pré-requisitos

- Java 21
- Docker e Docker Compose (para o Localstack)
- Terraform
- Maven

### Passos

1. **Clone o repositório**
    ```bash
    git clone https://github.com/DaniloAlmeidaSantos/banking-cancel-debit.git
    cd banking-cancel-debit
    ```

2. **Suba o ambiente Localstack**
    ```bash
    docker-compose up -d
    ```
   Isso vai iniciar containers simulando DynamoDB, SQS e API Gateway.


3. **Provisione os recursos via Terraform**
    - Vá até o diretório ./infrastructure/terraform como comando:
    ```bash
    cd /infrastructure/terraform
    ```
   - Em seguida rode os seguintes comandos:
   ```bash
    terraform init
    terraform apply -auto-approve
    ```
   

4. **Compile e execute a aplicação**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
   Ou conforme o padrão do projeto.

## Endpoints Principais

- **Criar débito**
    - `POST /debits`
    - Body exemplo **(Obrigatório)**: 
      ```json
      {
        "accountId": "12345",
        "amount": 100.0,
        "description": "Compra teste"
      }
      ```
    - Status de sucesso exemplo: `201`
    - Response body exemplo:
        ```json
        {
            "debitId": "64c8b988-8667-4092-9250-693978c81146",
            "status": "ACTIVE"
        }
        ```
  

- **Cancelar débito**
    - `POST /debit/cancel`
    - Header **(Opcional)**:
       - `X-Correlation-Id: <UUID>`
       - Recomendado para tracking da mensagem no SQS.
    - Body exemplo **(Obrigatório)**:
      ```json
      {
        "debitId": "64c8b988-8667-4092-9250-693978c81146",
        "reason": "Desistência",
        "requestedBy": "id do cliente"
      }
      ```
      
> ⚠️ Observação: O header X-Correlation-Id é opcional, mas altamente recomendado para rastreamento da requisição durante o fluxo da mensagem. Caso não seja fornecido, a aplicação irá gerar um novo UUID automaticamente e retorná-lo no header X-Correlation-Id da resposta. 


## Estrutura do Projeto

- `src/` – Código-fonte Java
- `docker-compose.yml` – Sobe o Localstack com os serviços necessários simulando AWS
- `terraform/` ou arquivos `.tf` – Infraestrutura como código

## Testes

Para rodar os testes:
```bash
mvn test
```