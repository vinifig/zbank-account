package com.zbank.bankaccount.application.data

import com.zbank.bankaccount.domain.model.account.Account

data class AccountBalanceData(val id: Long, val balance: Double) {
    companion object {

        fun from(account: Account) = AccountBalanceData(
            account.id!!,
            account.balance
        )

    }
}
