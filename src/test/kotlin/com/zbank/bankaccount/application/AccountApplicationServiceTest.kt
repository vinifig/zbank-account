package com.zbank.bankaccount.application

import com.zbank.bankaccount.AbstractBaseTest
import com.zbank.bankaccount.application.command.CreateAccountCommand
import com.zbank.bankaccount.application.data.AccountBalanceData
import com.zbank.bankaccount.application.data.AccountData
import com.zbank.bankaccount.domain.model.account.Account
import com.zbank.bankaccount.domain.model.account.AccountAlreadyExistsException
import com.zbank.bankaccount.domain.model.account.AccountNotFoundException
import com.zbank.bankaccount.domain.model.account.AccountRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class AccountApplicationServiceTest(
    @Mock private val accountRepositoryMock: AccountRepository
) : AbstractBaseTest() {

    private val accountApplicationService = AccountApplicationService(accountRepositoryMock)

    @Test
    fun `#createAccount must throw a AccountAlreadyExistsException if already have a account with the provided cpf`() {
        val accountCommand = buildFixture<CreateAccountCommand>("default")

        `when`(accountRepositoryMock.existsByCpf(accountCommand.cpf)).thenReturn(true)

        assertThrows<AccountAlreadyExistsException> {
            accountApplicationService.createAccount(accountCommand)
        }
    }

    @Test
    fun `#createAccount must return a new Account if have no account registered with the provided cpf`() {
        val accountCommand = buildFixture<CreateAccountCommand>("default")
        val expectedAccount = buildFixture<Account>("zeroBalance").copy(
            name = accountCommand.name,
            cpf = accountCommand.cpf
        )

        `when`(accountRepositoryMock.existsByCpf(accountCommand.cpf)).thenReturn(false)
        `when`(accountRepositoryMock.save(any(Account::class.java))).thenReturn(expectedAccount)

        val returnedAccount = accountApplicationService.createAccount(accountCommand)

        assertThat(returnedAccount).isEqualTo(AccountData.from(expectedAccount))
    }

    @Test
    fun `#getBalance must throw a AccountNotFoundException if the account id does not exists`() {
        val invalidAccountId = -1L

        `when`(accountRepositoryMock.findById(invalidAccountId)).thenReturn(Optional.empty())

        assertThrows<AccountNotFoundException> {
            accountApplicationService.getBalance(invalidAccountId)
        }
    }

    @Test
    fun `#getBalance must return the balance if the account id exists`() {
        val account = buildFixture<Account>("zeroBalance")
        val accountId = account.id ?: fail("must have a account id")

        `when`(accountRepositoryMock.findById(accountId)).thenReturn(Optional.of(account))

        val balance = accountApplicationService.getBalance(accountId)

        assertThat(balance).isEqualTo(AccountBalanceData.from(account))
    }
}
