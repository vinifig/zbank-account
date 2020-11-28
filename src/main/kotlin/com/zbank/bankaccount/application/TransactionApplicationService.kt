package com.zbank.bankaccount.application

import com.zbank.bankaccount.application.data.AccountStatementData
import com.zbank.bankaccount.domain.model.account.AccountAlreadyExistsException
import com.zbank.bankaccount.domain.model.account.AccountNotFoundException
import com.zbank.bankaccount.domain.model.account.AccountRepository
import com.zbank.bankaccount.domain.model.transaction.Transaction
import com.zbank.bankaccount.domain.model.transaction.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.LongSupplier

const val WITHDRAW_FEE = 0.01f
const val DEPOSIT_BONUS = 0.005f

@Service
class TransactionApplicationService(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun deposit(accountId: Long, amount: Float): Transaction {
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
    fun withdraw(accountId: Long, amount: Float): Transaction {
        logger.info("withdrawing $amount from account $accountId")
        val extraAmount = amount * WITHDRAW_FEE
        val account = accountRepository.findById(accountId)
            .orElseThrow { AccountNotFoundException(accountId) }
            .withdraw(amount + extraAmount)

        val transaction = Transaction.withdraw(accountId, amount, extraAmount)

        accountRepository.save(account)
        return transactionRepository.save(transaction)
    }

    @Transactional
    fun transfer(originAccountId: Long, destinyAccountId: Long, amount: Float): Transaction {
        logger.info("transferring $amount from account $originAccountId into $destinyAccountId")
        val accounts = accountRepository.findAllById(listOf(originAccountId, destinyAccountId))

        val originAccount = accounts.find { it.id == originAccountId }
            ?: throw AccountNotFoundException(originAccountId)

        val destinyAccount = accounts.find { it.id == destinyAccountId }
            ?: throw AccountNotFoundException(destinyAccountId)

        accountRepository.save(originAccount.withdraw(amount))
        accountRepository.save(destinyAccount.deposit(amount))

        return transactionRepository.save(
            Transaction.transfer(originAccountId, destinyAccountId, amount)
        )
    }

    @Transactional(readOnly = true)
    fun getAccountStatement(accountId: Long, pageable: Pageable): Page<AccountStatementData> {
        logger.info("getting account statement for account $accountId")

        if (!accountRepository.existsById(accountId)) {
            throw AccountNotFoundException(accountId)
        }

        val transactions = transactionRepository
            .findAllByOriginAccountIdOrDestinyAccountId(accountId, accountId, pageable)
            .let {
                PageableExecutionUtils.getPage(it, pageable) { it.size.toLong() }
            }

        return transactions.map { AccountStatementData.from(accountId, it) }
    }

}
