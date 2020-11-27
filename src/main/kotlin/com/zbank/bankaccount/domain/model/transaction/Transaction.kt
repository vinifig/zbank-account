package com.zbank.bankaccount.domain.model.transaction

import com.zbank.bankaccount.domain.model.transaction.TransactionKind.*
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("transaction")
data class Transaction(

    @Id
    val id: Long?,

    val originAccountId: Long,

    val destinyAccountId: Long?,

    val kind: TransactionKind,

    val amount: Double,

    val extraAmount: Double = 0.0,

    val createdAt: OffsetDateTime = OffsetDateTime.now()

) {
    companion object {

        fun deposit(accountId: Long, amount: Double, extraAmount: Double) = Transaction(
            null,
            accountId,
            null,
            DEPOSIT,
            amount,
            extraAmount
        )

        fun withdraw(accountId: Long, amount: Double, extraAmount: Double) = Transaction(
            null,
            accountId,
            null,
            WITHDRAW,
            -amount,
            -extraAmount
        )

    }
}
