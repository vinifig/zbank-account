package com.zbank.bankaccount.domain.model.common

abstract class EntityNotFoundException(
    entity: String,
    id: Long?,
) : BusinessException(
    "$entity($id) not found"
)
