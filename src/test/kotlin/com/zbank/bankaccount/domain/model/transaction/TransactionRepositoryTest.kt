package com.zbank.bankaccount.domain.model.transaction

import com.zbank.bankaccount.AbstractRepositoryTest
import com.zbank.bankaccount.domain.model.account.Account
import com.zbank.bankaccount.domain.model.account.AccountRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest

class TransactionRepositoryTest(
    @Autowired private val accountRepository: AccountRepository,
    @Autowired private val transactionRepository: TransactionRepository
) : AbstractRepositoryTest() {

    private val defaultPageable = PageRequest.of(0, 5)

    @BeforeEach
    fun setUp() {
        accountRepository.deleteAll()
        transactionRepository.deleteAll()
    }

    @Test
    fun `#findAllByOriginAccountIdOrDestinyAccountId must paginate the transactions with the provided originAccountId`() {
        val account = accountRepository.save(buildFixture<Account>("default").copy(id = null))
        val transactionIds = buildFixture<Transaction>(10, "default")
            .map {
                transactionRepository.save(
                    it.copy(id = null, originAccountId = account.id!!, destinyAccountId = null)
                ).id
            }
        val transactions = transactionRepository.findAllById(transactionIds)

        validateTransactions(account.id!!, transactions)
    }

    @Test
    fun `#findAllByOriginAccountIdOrDestinyAccountId must paginate the transactions with the provided destinyAccountId`() {
        val originAccount = accountRepository.save(buildFixture<Account>("default").copy(id = null))
        val destinyAccount = accountRepository.save(buildFixture<Account>("default").copy(id = null))
        val transactionIds = buildFixture<Transaction>(10, "default")
            .map {
                transactionRepository.save(
                    it.copy(id = null, originAccountId = originAccount.id!!, destinyAccountId = destinyAccount.id!!)
                ).id
            }
        val transactions = transactionRepository.findAllById(transactionIds)

        validateTransactions(destinyAccount.id!!, transactions)
    }

    private fun validateTransactions(accountId: Long, expectedTransactions: Iterable<Transaction>) {
        val returnedTransactions = transactionRepository.findAllByOriginAccountIdOrDestinyAccountId(
            accountId,
            accountId,
            defaultPageable
        )

        assertThat(returnedTransactions).hasSize(defaultPageable.pageSize)
        returnedTransactions.forEach {
            assertThat(it).isIn(expectedTransactions)
        }
    }
}
