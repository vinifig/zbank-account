package com.zbank.account.domain.model.common

abstract class BusinessException(override val message: String, cause: Throwable? = null)
    : RuntimeException(message, cause)
