package com.zbank.account.application

import com.zbank.account.AbstractBaseTest
import com.zbank.account.domain.model.account.Account
import com.zbank.account.domain.model.account.AccountAlreadyExistsException
import com.zbank.account.domain.model.account.AccountRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`

class AccountApplicationServiceTest(
    @Mock private val accountRepositoryMock: AccountRepository
) : AbstractBaseTest() {

    private val accountApplicationService = AccountApplicationService(accountRepositoryMock)

    @Test
    fun `#createAccount must throw a AccountAlreadyExistsException if already have a account with the provided cpf`() {
        val account = buildFixture<Account>("default")

        `when`(accountRepositoryMock.existsByCpf(account.cpf)).thenReturn(true)

        assertThrows<AccountAlreadyExistsException> {
            accountApplicationService.createAccount(account.name, account.cpf)
        }
    }

    @Test
    fun `#createAccount must return a new Account if have no account registered with the provided cpf`() {
        val account = buildFixture<Account>("zeroBalance")

        `when`(accountRepositoryMock.existsByCpf(account.cpf)).thenReturn(false)
        `when`(accountRepositoryMock.save(any(Account::class.java))).thenReturn(account)

        val returnedAccount = accountApplicationService.createAccount(account.name, account.cpf)

        assertThat(returnedAccount).isEqualTo(account)
    }
}
