package com.zbank.bankaccount.domain.model.common

abstract class BusinessException(override val message: String, cause: Throwable? = null)
    : RuntimeException(message, cause)
