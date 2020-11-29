package com.zbank.bankaccount.application

import com.zbank.bankaccount.application.command.CreateAccountCommand
import com.zbank.bankaccount.application.data.AccountBalanceData
import com.zbank.bankaccount.application.data.AccountData
import com.zbank.bankaccount.domain.model.account.Account
import com.zbank.bankaccount.domain.model.account.AccountAlreadyExistsException
import com.zbank.bankaccount.domain.model.account.AccountNotFoundException
import com.zbank.bankaccount.domain.model.account.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.log

@Service
class AccountApplicationService(
    private val accountRepository: AccountRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun createAccount(account: CreateAccountCommand): AccountData {
        logger.info("creating account for ${account.name} with ${account.cpf}")

        if (accountRepository.existsByCpf(account.cpf)) {
            throw AccountAlreadyExistsException(account.cpf)
        }

        return accountRepository.save(Account(null, account.name, account.cpf))
            .let { AccountData.from(it) }
    }

    @Transactional(readOnly = true)
    fun getBalance(accountId: Long): AccountBalanceData {
        logger.info("getting account balance for $accountId")

        return accountRepository.findById(accountId)
            .map { AccountBalanceData.from(it) }
            .orElseThrow { AccountNotFoundException(accountId) }
    }

    @Transactional(readOnly = true)
    fun getAccount(accountId: Long): Account {
        logger.info("getting account for $accountId")

        return accountRepository.findById(accountId)
            .orElseThrow { AccountNotFoundException(accountId) }
    }

}
