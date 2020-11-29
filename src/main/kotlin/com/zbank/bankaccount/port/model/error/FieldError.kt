package com.zbank.bankaccount.port.model.error

data class FieldError(val field: String, val error: String, val key: String? = null)
