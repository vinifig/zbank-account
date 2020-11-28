package com.zbank.bankaccount.port.controller.model

import com.zbank.bankaccount.domain.model.transaction.TransactionKind

data class TransactionOperation(
    val amount: Float,
    val kind: TransactionKind,
    val targetAccountId: Long?
)
