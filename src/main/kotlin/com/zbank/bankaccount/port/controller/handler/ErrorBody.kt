package com.zbank.bankaccount.port.controller.handler

data class ErrorBody(val message: String, val fieldErrors: List<FieldError> = emptyList())
