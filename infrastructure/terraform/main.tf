provider "aws" {
    region                      = "sa-east-1"
    access_key                  = "test"
    secret_key                  = "test"
    skip_credentials_validation = true
    skip_requesting_account_id  = true
    skip_metadata_api_check     = true

    endpoints {
        sqs         = "http://localhost:4566"
        apigateway  = "http://localhost:4566"
        dynamodb    = "http://localhost:4566"
    }
}

resource "aws_sqs_queue" "debit_cancellation_queue" {
    name                        = "cancellation-queue"
    max_message_size            = "2048"
    message_retention_seconds   = "43200"

    tags = {
        Enviroment = "dev"
    }
}


resource "aws_api_gateway_rest_api" "itau_case_gateway_rest_api" {
    name = "itau-case-gateway-rest-api"
    body = jsonencode({
        openapi = "3.0.1"
        info =  {
            title   = "debit-cancellation-case"
            version = "1.0"
        }
        paths = {
            "/debit/cancel" = {
                post = {
                    x-amazon-apigateway-integration = {
                        httpMethod              = "POST"
                        type                    = "HTTP_PROXY"
                        uri                     = "http://host.docker.internal:8080/banking-cancel-debit/debit/cancel"
                    }
                }
            }
            "/debit" = {
                post = {
                    x-amazon-apigateway-integration = {
                        httpMethod              = "POST"
                        type                    = "HTTP_PROXY"
                        uri                     = "http://host.docker.internal:8080/banking-cancel-debit/debit"
                    }
                }
            }
        }
    })

    endpoint_configuration {
        types = ["REGIONAL"]
    }

    tags = {
        Environment = "dev"
    }
}

resource "aws_api_gateway_deployment" "itau_case_gateway_deployment" {
    rest_api_id = aws_api_gateway_rest_api.itau_case_gateway_rest_api.id

    triggers = {
        redeployment = sha1(aws_api_gateway_rest_api.itau_case_gateway_rest_api.body)
    }

    lifecycle {
        create_before_destroy = true
    }
}

resource "aws_api_gateway_stage" "itau_case_gateway_stage" {
    deployment_id   = aws_api_gateway_deployment.itau_case_gateway_deployment.id
    rest_api_id     = aws_api_gateway_rest_api.itau_case_gateway_rest_api.id
    stage_name      = "itau-case-gateway-stage"

    tags = {
        Environment = "dev"
    }
}

resource "aws_dynamodb_table" "itau_case_dynamo_table" {
    name            = "Debits"
    billing_mode    = "PAY_PER_REQUEST"
    hash_key        = "id"

    attribute  {
        name = "id"
        type = "S"
    }

    tags = {
        Environment = "dev"
    }
}