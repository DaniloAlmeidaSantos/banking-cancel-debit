output "api_gateway_id" {
  value       = aws_api_gateway_rest_api.itau_case_gateway_rest_api.id
  description = "ID do API Gateway criado"
}

output "api_invoke_url" {
  value       = "http://localhost:4566/restapis/${aws_api_gateway_rest_api.itau_case_gateway_rest_api.id}/${aws_api_gateway_stage.itau_case_gateway_stage.stage_name}/_user_request_/debit/cancel"
  description = "URL base para invocar o endpoint via LocalStack"
}