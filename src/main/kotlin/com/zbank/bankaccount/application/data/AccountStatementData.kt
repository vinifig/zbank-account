package com.zbank.bankaccount.application.data

import com.zbank.bankaccount.domain.model.transaction.Transaction
import com.zbank.bankaccount.domain.model.transaction.TransactionKind.*
import java.time.OffsetDateTime

data class AccountStatementData(
    val amount: Float,
    val extraAmount: Float,
    val kind: StatementKindData,
    val relatedTo: Long?,
    val createdAt: OffsetDateTime
) {
    companion object {
        fun from(accountId: Long, transaction: Transaction): AccountStatementData = when(transaction.kind) {
            DEPOSIT ->
                AccountStatementData(
                    transaction.amount,
                    transaction.extraAmount,
                    StatementKindData.INCOME,
                    null,
                    transaction.createdAt
                )
            WITHDRAW ->
                AccountStatementData(
                    transaction.amount,
                    transaction.extraAmount,
                    StatementKindData.OUTCOME,
                    null,
                    transaction.createdAt
                )
            TRANSFER -> transfer(accountId, transaction)
        }

        private fun transfer(accountId: Long, transaction: Transaction): AccountStatementData {
            val (kind, relatedTo) = if (accountId == transaction.originAccountId) {
                StatementKindData.OUTCOME to transaction.destinyAccountId
            }
            else {
                StatementKindData.INCOME to transaction.originAccountId
            }

            return AccountStatementData(
                transaction.amount,
                transaction.extraAmount,
                kind,
                relatedTo,
                transaction.createdAt
            )
        }
    }
}

enum class StatementKindData {
    INCOME,
    OUTCOME
}
