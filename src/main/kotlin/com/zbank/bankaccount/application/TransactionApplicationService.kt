package com.zbank.bankaccount.application

import com.zbank.bankaccount.domain.model.account.AccountNotFoundException
import com.zbank.bankaccount.domain.model.account.AccountRepository
import com.zbank.bankaccount.domain.model.transaction.Transaction
import com.zbank.bankaccount.domain.model.transaction.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

const val WITHDRAW_FEE = 0.01
const val DEPOSIT_BONUS = 0.005

@Service
class TransactionApplicationService(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun deposit(accountId: Long, amount: Double): Transaction {
        logger.info("depositing $amount in account $accountId")
        val extraAmount = amount * DEPOSIT_BONUS
        val account = accountRepository.findById(accountId)
            .orElseThrow { AccountNotFoundException(accountId) }
            .deposit(amount + extraAmount)

        val transaction = Transaction.deposit(accountId, amount, extraAmount)

        accountRepository.save(account)
        return transactionRepository.save(transaction)
    }

    @Transactional
    fun withdraw(accountId: Long, amount: Double): Transaction {
        logger.info("withdrawing $amount from account $accountId")
        val extraAmount = amount * WITHDRAW_FEE
        val account = accountRepository.findById(accountId)
            .orElseThrow { AccountNotFoundException(accountId) }
            .withdraw(amount + extraAmount)

        val transaction = Transaction.withdraw(accountId, amount, extraAmount)

        accountRepository.save(account)
        return transactionRepository.save(transaction)
    }

}
