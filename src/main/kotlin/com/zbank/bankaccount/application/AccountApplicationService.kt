package com.zbank.bankaccount.application

import com.zbank.bankaccount.application.command.CreateAccountCommand
import com.zbank.bankaccount.application.data.AccountData
import com.zbank.bankaccount.domain.model.account.Account
import com.zbank.bankaccount.domain.model.account.AccountAlreadyExistsException
import com.zbank.bankaccount.domain.model.account.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AccountApplicationService(
    private val accountRepository: AccountRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun createAccount(account: CreateAccountCommand): AccountData {
        logger.info("creating account for ${account.name} with ${account.cpf}")

        if (accountRepository.existsByCpf(account.cpf)) {
            throw AccountAlreadyExistsException(account.cpf)
        }

        return accountRepository.save(Account(null, account.name, account.cpf))
            .let { AccountData.from(it) }
    }

}
