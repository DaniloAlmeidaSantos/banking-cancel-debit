# banking-cancel-debit

Projeto focado no cancelamento de débitos bancários, também permitindo a criação de débitos para facilitar testes e controle de regras de negócio.

## Funcionalidade Principal

- **Cancelar Débito:** Endpoint para cancelar débitos registrados.
- **Criar Débito:** (funcionalidade extra) Endpoint que permite criar um débito no sistema, mesmo não sendo o foco principal do projeto, para facilitar o controle e testes das regras de negócio.

## Arquitetura e Tecnologias

- **Java** – Lógica principal da aplicação
- **HCL/Terraform** – Provisionamento de infraestrutura
- **DynamoDB, SQS, API Gateway** – Serviços AWS simulados via Localstack
- **Localstack via Docker Compose** – Provisiona os serviços AWS localmente para desenvolvimento e testes

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

3. **Configure suas variáveis de ambiente**
    - Certifique-se de que sua aplicação está configurada para apontar para o endpoint local do Localstack (geralmente http://localhost:4566).

4. **Provisione os recursos via Terraform**
    - Vá até o diretório ./infrastructure/terraform como comando:
    ```bash
    cd /infrastructure/terraform
    ```
   - Em seguida rode os seguintes comandos:
   ```bash
    terraform init
    terraform apply -auto-approve
    ```

5. **Compile e execute a aplicação**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
   Ou conforme o padrão do projeto.

## Endpoints Principais

- **Criar débito**
    - `POST /debits`
    - Body exemplo:
      ```json
      {
        "accountId": "12345",
        "amount": 100.0,
        "description": "Compra teste"
      }
      ```

- **Cancelar débito**
    - `POST /debit/cancel`
    - Body exemplo:
      ```json
      {
        "debitId": "12345",
        "reason": "Desistência",
        "requestedBy": "id do cliente"
      }
      ```

## Estrutura do Projeto

- `src/` – Código-fonte Java
- `docker-compose.yml` – Sobe o Localstack com os serviços necessários simulando AWS
- `terraform/` ou arquivos `.tf` – Infraestrutura como código

## Testes

Para rodar os testes:
```bash
mvn test
