package com.zbank.bankaccount.port.controller.handler

data class ApiError(val status: Int, val message: String, val type: String, val errors: List<FieldError> = emptyList())
