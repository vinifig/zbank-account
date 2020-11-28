package com.zbank.bankaccount.domain.model.transaction

import com.zbank.bankaccount.domain.model.transaction.TransactionKind.*
import org.springframework.data.annotation.Id
import org.springframework.data.domain.AbstractAggregateRoot
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("transaction")
data class Transaction(

    @Id
    val id: Long?,

    val originAccountId: Long,

    val destinyAccountId: Long?,

    val kind: TransactionKind,

    val amount: Float,

    val extraAmount: Float = 0f,

    val createdAt: OffsetDateTime = OffsetDateTime.now()

) : AbstractAggregateRoot<Transaction>() {
    companion object {

        fun deposit(accountId: Long, amount: Float, extraAmount: Float) = Transaction(
            null,
            accountId,
            null,
            DEPOSIT,
            amount,
            extraAmount
        )

        fun withdraw(accountId: Long, amount: Float, extraAmount: Float) = Transaction(
            null,
            accountId,
            null,
            WITHDRAW,
            -amount,
            -extraAmount
        )

        fun transfer(originAccountId: Long, destinyAccountId: Long, amount: Float) = Transaction(
            null,
            originAccountId,
            destinyAccountId,
            TRANSFER,
            amount
        )

    }
}
