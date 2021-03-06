package com.zbank.bankaccount.domain.model.account

import com.zbank.bankaccount.AbstractBaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AccountTest : AbstractBaseTest() {

    @Test
    fun `#withdraw must throw a NoBalanceAvailableException if has no balance available`() {
        val account = buildFixture<Account>("zeroBalance")
        val amountToWithdraw = 1.0f

        assertThrows<NoBalanceAvailableException> {
            account.withdraw(amountToWithdraw)
        }
    }

    @Test
    fun `#withdraw must throw a NegativeAmountException if the amount is negative`() {
        val account = buildFixture<Account>("default")
        val amountToWithdraw = -1.0f

        assertThrows<NegativeAmountException> {
            account.withdraw(amountToWithdraw)
        }
    }

    @Test
    fun `#withdraw must remove the amount if has balance available`() {
        val account = buildFixture<Account>("default")
        val amountToWithdraw = account.balance

        val updatedAccount = account.withdraw(amountToWithdraw)

        assertThat(updatedAccount.balance).isEqualTo(account.balance - amountToWithdraw)
    }

    @Test
    fun `#deposit must add the amount to account balance`() {
        val account = buildFixture<Account>("default")
        val amountToDeposit = 10.0f

        val updatedAccount = account.deposit(amountToDeposit)

        assertThat(updatedAccount.balance).isEqualTo(account.balance + amountToDeposit)
    }

    @Test
    fun `#deposit must throw a NegativeAmountException if the amount is negative`() {
        val account = buildFixture<Account>("default")
        val amountToDeposit = -1.0f

        assertThrows<NegativeAmountException> {
            account.deposit(amountToDeposit)
        }
    }
}
