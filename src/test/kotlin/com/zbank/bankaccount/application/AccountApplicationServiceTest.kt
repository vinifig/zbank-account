package com.zbank.bankaccount.application

import com.zbank.bankaccount.AbstractBaseTest
import com.zbank.bankaccount.application.command.CreateAccountCommand
import com.zbank.bankaccount.domain.model.account.Account
import com.zbank.bankaccount.domain.model.account.AccountAlreadyExistsException
import com.zbank.bankaccount.domain.model.account.AccountRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

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

        assertThat(returnedAccount).isEqualTo(expectedAccount)
    }
}
