package com.zbank.bankaccount.port.model.error

data class ErrorBody(val message: String, val fieldErrors: List<FieldError> = emptyList())
