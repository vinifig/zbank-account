package com.zbank.bankaccount.domain.model.account

import org.hibernate.validator.constraints.br.CPF
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("account")
data class Account(

    @Id
    val id: Long?,

    val name: String,

    @CPF
    val cpf: String,

    val balance: Float = 0f,

    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @LastModifiedDate
    val updatedAt: OffsetDateTime = OffsetDateTime.now()

) {

    fun withdraw(amount: Float): Account {
        if (amount < 0) {
            throw NegativeAmountException()
        }
        if (amount > balance) {
            throw NoBalanceAvailableException()
        }

        return this.copy(balance = balance - amount)
    }

    fun deposit(amount: Float): Account {
        if (amount < 0) {
            throw NegativeAmountException()
        }
        return this.copy(balance = balance + amount)
    }

}
