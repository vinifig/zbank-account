package com.zbank.bankaccount.application

import com.zbank.bankaccount.AbstractBaseTest
import com.zbank.bankaccount.domain.model.account.Account
import com.zbank.bankaccount.domain.model.account.AccountNotFoundException
import com.zbank.bankaccount.domain.model.account.AccountRepository
import com.zbank.bankaccount.domain.model.transaction.Transaction
import com.zbank.bankaccount.domain.model.transaction.TransactionKind
import com.zbank.bankaccount.domain.model.transaction.TransactionKind.*
import com.zbank.bankaccount.domain.model.transaction.TransactionRepository
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
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

    @Test
    fun `#deposit must throw a AccountNotFoundException if the account_id does not exists` () {
        val invalidAccountId = -1L

        `when`(accountRepositoryMock.findById(invalidAccountId)).thenReturn(Optional.empty())

        assertThrows<AccountNotFoundException> {
            transactionApplicationService.deposit(invalidAccountId, 0.0)
        }

        verify(accountRepositoryMock).findById(invalidAccountId)
        verify(accountRepositoryMock, never()).save(any(Account::class.java))
        verify(transactionRepositoryMock, never()).save(any(Transaction::class.java))
    }

    @Test
    fun `#deposit must return a Transaction with valid values`() {
        val account = buildFixture<Account>("default")
        val accountId = account.id ?: fail("must have a id")
        val amount = 5.0

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
            transactionApplicationService.withdraw(invalidAccountId, 0.0)
        }

        verify(accountRepositoryMock).findById(invalidAccountId)
        verify(accountRepositoryMock, never()).save(any(Account::class.java))
        verify(transactionRepositoryMock, never()).save(any(Transaction::class.java))
    }

    @Test
    fun `#withdraw must return a Transaction with valid values`() {
        val account = buildFixture<Account>("default")
        val accountId = account.id ?: fail("must have a id")
        val amount = 5.0

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
}
