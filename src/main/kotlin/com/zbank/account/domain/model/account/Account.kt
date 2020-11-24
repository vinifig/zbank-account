package com.zbank.account.domain.model.account

import org.hibernate.validator.constraints.br.CPF
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("account")
data class Account(

    @Id
    val id: Long?,

    val name: String,

    @CPF
    val cpf: String,

    val balance: Double = 0.0

) {

    fun withdraw(amount: Double): Account {
        if (amount < 0) {
            throw NegativeAmountException()
        }
        if (amount > balance) {
            throw NoBalanceAvailableException()
        }

        return this.copy(balance = balance - amount)
    }

    fun deposit(amount: Double): Account {
        if (amount < 0) {
            throw NegativeAmountException()
        }
        return this.copy(balance = balance + amount)
    }

}
