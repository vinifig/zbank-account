package com.zbank.bankaccount.domain.model.account

import com.zbank.bankaccount.AbstractRepositoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AccountRepositoryTest(
    @Autowired private val accountRepository: AccountRepository
) : AbstractRepositoryTest() {

    @BeforeEach
    fun setUp() {
        accountRepository.deleteAll()
    }

    @Test
    fun `#existsByCpf must returns true if the account exists`() {
        val account = accountRepository.save(buildFixture<Account>("default").copy(id = null))

        assertThat(accountRepository.existsByCpf(account.cpf)).isTrue()
    }

    @Test
    fun `#existsByCpf must returns false if the account does not exists`() {
        assertThat(accountRepository.existsByCpf("")).isFalse()
    }
}
