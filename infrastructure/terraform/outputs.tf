output "api_gateway_id" {
  value       = aws_api_gateway_rest_api.itau_case_gateway_rest_api.id
  description = "Api Gateway ID created"
}

output "api_debit_cancel_invoke_url" {
  value       = "http://localhost:4566/restapis/${aws_api_gateway_rest_api.itau_case_gateway_rest_api.id}/${aws_api_gateway_stage.itau_case_gateway_stage.stage_name}/_user_request_/debit/cancel"
  description = "Base URL to invoke debit cancel API via LocalStack"
}

output "api_debit_create_invoke_url" {
  value       = "http://localhost:4566/restapis/${aws_api_gateway_rest_api.itau_case_gateway_rest_api.id}/${aws_api_gateway_stage.itau_case_gateway_stage.stage_name}/_user_request_/debit"
  description = "Base URL to invoke create debit API via LocalStack"
}