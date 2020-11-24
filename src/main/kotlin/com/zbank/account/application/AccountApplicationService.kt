package com.zbank.account.application

import com.zbank.account.domain.model.account.Account
import com.zbank.account.domain.model.account.AccountRepository
import com.zbank.account.domain.model.account.AccountAlreadyExistsException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AccountApplicationService(
    private val accountRepository: AccountRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun createAccount(name: String, cpf: String): Account {
        logger.info("creating account for $name with $cpf")

        if (accountRepository.existsByCpf(cpf)) {
            throw AccountAlreadyExistsException(cpf)
        }

        return accountRepository.save(Account(null, name, cpf))
    }

}
