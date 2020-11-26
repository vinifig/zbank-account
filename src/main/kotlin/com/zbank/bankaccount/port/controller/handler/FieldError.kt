package com.zbank.bankaccount.port.controller.handler

data class FieldError(val field: String, val error: String, val key: String? = null)
