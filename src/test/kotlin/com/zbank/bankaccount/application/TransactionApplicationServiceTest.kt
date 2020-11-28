package com.zbank.bankaccount.application

import com.zbank.bankaccount.AbstractBaseTest
import com.zbank.bankaccount.application.data.AccountStatementData
import com.zbank.bankaccount.domain.model.account.Account
import com.zbank.bankaccount.domain.model.account.AccountNotFoundException
import com.zbank.bankaccount.domain.model.account.AccountRepository
import com.zbank.bankaccount.domain.model.transaction.Transaction
import com.zbank.bankaccount.domain.model.transaction.TransactionKind.*
import com.zbank.bankaccount.domain.model.transaction.TransactionRepository
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*

@ExtendWith(MockitoExtension::class)
class TransactionApplicationServiceTest(
    @Mock private val accountRepositoryMock: AccountRepository,
    @Mock private val transactionRepositoryMock: TransactionRepository
) : AbstractBaseTest() {

    private val transactionApplicationService = TransactionApplicationService(
        accountRepositoryMock,
        transactionRepositoryMock
    )

    private val defaultPageable = PageRequest.of(0, 20)

    @Test
    fun `#deposit must throw a AccountNotFoundException if the account_id does not exists` () {
        val invalidAccountId = -1L

        `when`(accountRepositoryMock.findById(invalidAccountId)).thenReturn(Optional.empty())

        assertThrows<AccountNotFoundException> {
            transactionApplicationService.deposit(invalidAccountId, 0.0f)
        }

        verify(accountRepositoryMock).findById(invalidAccountId)
        verify(accountRepositoryMock, never()).save(any(Account::class.java))
        verify(transactionRepositoryMock, never()).save(any(Transaction::class.java))
    }

    @Test
    fun `#deposit must return a Transaction with valid values`() {
        val account = buildFixture<Account>("default")
        val accountId = account.id ?: fail("must have a id")
        val amount = 5.0f

        `when`(accountRepositoryMock.findById(accountId)).thenReturn(Optional.of(account))
        `when`(accountRepositoryMock.save(any(Account::class.java))).then { it.arguments.first() }
        `when`(transactionRepositoryMock.save(any(Transaction::class.java))).then { it.arguments.first() }

        val transaction = transactionApplicationService.deposit(accountId, amount)

        assertThat(transaction.amount).isEqualTo(amount)
        assertThat(transaction.extraAmount).isEqualTo(amount * DEPOSIT_BONUS)
        assertThat(transaction.originAccountId).isEqualTo(accountId)
        assertThat(transaction.destinyAccountId).isNull()
        assertThat(transaction.kind).isEqualTo(DEPOSIT)

        verify(accountRepositoryMock).findById(accountId)
        verify(accountRepositoryMock).save(any(Account::class.java))
        verify(transactionRepositoryMock).save(any(Transaction::class.java))
    }

    @Test
    fun `#withdraw must throw a AccountNotFoundException if the account_id does not exists` () {
        val invalidAccountId = -1L

        `when`(accountRepositoryMock.findById(invalidAccountId)).thenReturn(Optional.empty())

        assertThrows<AccountNotFoundException> {
            transactionApplicationService.withdraw(invalidAccountId, 0f)
        }

        verify(accountRepositoryMock).findById(invalidAccountId)
        verify(accountRepositoryMock, never()).save(any(Account::class.java))
        verify(transactionRepositoryMock, never()).save(any(Transaction::class.java))
    }

    @Test
    fun `#withdraw must return a Transaction with valid values`() {
        val account = buildFixture<Account>("default")
        val accountId = account.id ?: fail("must have a id")
        val amount = 5.0f

        `when`(accountRepositoryMock.findById(accountId)).thenReturn(Optional.of(account))
        `when`(accountRepositoryMock.save(any(Account::class.java))).then { it.arguments.first() }
        `when`(transactionRepositoryMock.save(any(Transaction::class.java))).then { it.arguments.first() }

        val transaction = transactionApplicationService.withdraw(accountId, amount)

        assertThat(transaction.amount).isEqualTo(-amount)
        assertThat(transaction.extraAmount).isEqualTo(-amount * WITHDRAW_FEE)
        assertThat(transaction.originAccountId).isEqualTo(accountId)
        assertThat(transaction.destinyAccountId).isNull()
        assertThat(transaction.kind).isEqualTo(WITHDRAW)

        verify(accountRepositoryMock).findById(accountId)
        verify(accountRepositoryMock).save(any(Account::class.java))
        verify(transactionRepositoryMock).save(any(Transaction::class.java))
    }

    @Test
    fun `#transfer must throw a AccountNotFoundException if the account_id does not exists` () {
        val invalidAccountId = -1L
        val destinyAccount = buildFixture<Account>("default")
        val accountIds = listOf(invalidAccountId, destinyAccount.id!!)
        val accounts = listOf(destinyAccount)

        `when`(accountRepositoryMock.findAllById(accountIds)).thenReturn(accounts)

        assertThrows<AccountNotFoundException> {
            transactionApplicationService.transfer(invalidAccountId, destinyAccount.id!!, 0f)
        }

        verify(accountRepositoryMock).findAllById(accountIds)
        verify(accountRepositoryMock, never()).save(any(Account::class.java))
        verify(transactionRepositoryMock, never()).save(any(Transaction::class.java))
    }

    @Test
    fun `#transfer must throw a AccountNotFoundException if the destiny_account_id does not exists` () {
        val originAccount = buildFixture<Account>("default")
        val invalidAccountId = -1L
        val accountIds = listOf(originAccount.id!!, invalidAccountId)
        val accounts = listOf(originAccount)

        `when`(accountRepositoryMock.findAllById(accountIds)).thenReturn(accounts)

        assertThrows<AccountNotFoundException> {
            transactionApplicationService.transfer(originAccount.id!!, invalidAccountId, 0f)
        }

        verify(accountRepositoryMock).findAllById(accountIds)
        verify(accountRepositoryMock, never()).save(any(Account::class.java))
        verify(transactionRepositoryMock, never()).save(any(Transaction::class.java))
    }

    @Test
    fun `#transfer must return a Transaction with valid values`() {
        val originAccount = buildFixture<Account>("default")
        val destinyAccount = buildFixture<Account>("default")
        val accountIds = listOf(originAccount.id!!, destinyAccount.id!!)
        val accounts = listOf(originAccount, destinyAccount)
        val amount = 5f

        `when`(accountRepositoryMock.findAllById(accountIds)).thenReturn(accounts)
        `when`(accountRepositoryMock.save(any(Account::class.java))).then { it.arguments.first() }
        `when`(transactionRepositoryMock.save(any(Transaction::class.java))).then { it.arguments.first() }

        val transaction = transactionApplicationService.transfer(originAccount.id!!, destinyAccount.id!!, amount)

        assertThat(transaction.amount).isEqualTo(amount)
        assertThat(transaction.extraAmount).isEqualTo(0f)
        assertThat(transaction.originAccountId).isEqualTo(originAccount.id!!)
        assertThat(transaction.destinyAccountId).isEqualTo(destinyAccount.id!!)
        assertThat(transaction.kind).isEqualTo(TRANSFER)

        verify(accountRepositoryMock).findAllById(accountIds)
        verify(accountRepositoryMock, atLeast(2)).save(any(Account::class.java))
        verify(transactionRepositoryMock).save(any(Transaction::class.java))
    }

    @Test
    fun `#getAccountStatement must throw AccountNotFoundException when the account_id does not exists`() {
        val invalidAccountId = -1L

        `when`(accountRepositoryMock.existsById(invalidAccountId)).thenReturn(false)

        assertThrows<AccountNotFoundException> {
            transactionApplicationService.getAccountStatement(invalidAccountId, defaultPageable)
        }

        verify(accountRepositoryMock).existsById(invalidAccountId)
        verify(transactionRepositoryMock, never())
            .findAllByOriginAccountIdOrDestinyAccountId(anyLong(), anyLong(), any(Pageable::class.java))
    }

    @Test
    fun `#getAccountStatement must return a page with all results`() {
        val account = buildFixture<Account>("default")
        val transactions = buildFixture<Transaction>(2, "default")
        val expectedStatements = transactions.map { AccountStatementData.from(account.id!!, it) }

        `when`(accountRepositoryMock.existsById(account.id!!)).thenReturn(true)
        `when`(transactionRepositoryMock.findAllByOriginAccountIdOrDestinyAccountId(
                account.id!!,
                account.id!!,
                defaultPageable
            )).thenReturn(transactions)

        val statements = transactionApplicationService.getAccountStatement(account.id!!, defaultPageable)

        assertThat(statements.content.toList()).isEqualTo(expectedStatements)

        verify(accountRepositoryMock).existsById(account.id!!)
        verify(transactionRepositoryMock)
            .findAllByOriginAccountIdOrDestinyAccountId(account.id!!, account.id!!, defaultPageable)
    }

    @Test
    fun `#getAccountStatement must return a empty page if has no results`() {
        val account = buildFixture<Account>("default")
        val transactions = listOf<Transaction>()
        val expectedStatements = listOf<AccountStatementData>()

        `when`(accountRepositoryMock.existsById(account.id!!)).thenReturn(true)
        `when`(transactionRepositoryMock.findAllByOriginAccountIdOrDestinyAccountId(
            account.id!!,
            account.id!!,
            defaultPageable
        )).thenReturn(transactions)

        val statements = transactionApplicationService.getAccountStatement(account.id!!, defaultPageable)

        assertThat(statements.content.toList()).isEqualTo(expectedStatements)

        verify(accountRepositoryMock).existsById(account.id!!)
        verify(transactionRepositoryMock)
            .findAllByOriginAccountIdOrDestinyAccountId(account.id!!, account.id!!, defaultPageable)
    }
}
