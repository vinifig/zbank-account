package com.zbank.bankaccount.application.data

import com.zbank.bankaccount.domain.model.account.Account

data class AccountData(val id: Long, val cpf: String) {
    companion object {

        fun from(account: Account) = AccountData(
            account.id!!,
            account.cpf
        )

    }
}
