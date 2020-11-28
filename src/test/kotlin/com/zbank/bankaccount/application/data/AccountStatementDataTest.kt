package com.zbank.bankaccount.application.data

import com.zbank.bankaccount.AbstractBaseTest
import com.zbank.bankaccount.domain.model.transaction.Transaction
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

class AccountStatementDataTest : AbstractBaseTest() {

    @Test
    fun `AccountStatementData#from must convert a DEPOSIT transaction`() {
        val accountId = 1L
        val transaction = buildFixture<Transaction>("deposit").copy(originAccountId = accountId)

        val accountStatement = AccountStatementData.from(accountId, transaction)

        assertThat(accountStatement.amount).isEqualTo(transaction.amount)
        assertThat(accountStatement.extraAmount).isEqualTo(transaction.extraAmount)
        assertThat(accountStatement.relatedTo).isNull()
        assertThat(accountStatement.createdAt).isEqualTo(transaction.createdAt)
        assertThat(accountStatement.kind).isEqualTo(StatementKindData.INCOME)
    }

    @Test
    fun `AccountStatementData#from must convert a WITHDRAW transaction`() {
        val accountId = 1L
        val transaction = buildFixture<Transaction>("withdraw").copy(originAccountId = accountId)

        val accountStatement = AccountStatementData.from(accountId, transaction)

        assertThat(accountStatement.amount).isEqualTo(transaction.amount)
        assertThat(accountStatement.extraAmount).isEqualTo(transaction.extraAmount)
        assertThat(accountStatement.relatedTo).isNull()
        assertThat(accountStatement.createdAt).isEqualTo(transaction.createdAt)
        assertThat(accountStatement.kind).isEqualTo(StatementKindData.OUTCOME)
    }

    @Test
    fun `AccountStatementData#from must convert a outcome TRANSFER transaction`() {
        val originAccountId = 1L
        val destinyAccountId = 2L
        val transaction = buildFixture<Transaction>("transfer").copy(
            originAccountId = originAccountId,
            destinyAccountId = destinyAccountId
        )

        val accountStatement = AccountStatementData.from(originAccountId, transaction)

        assertThat(accountStatement.amount).isEqualTo(transaction.amount)
        assertThat(accountStatement.extraAmount).isEqualTo(transaction.extraAmount)
        assertThat(accountStatement.relatedTo).isEqualTo(destinyAccountId)
        assertThat(accountStatement.createdAt).isEqualTo(transaction.createdAt)
        assertThat(accountStatement.kind).isEqualTo(StatementKindData.OUTCOME)
    }

    @Test
    fun `AccountStatementData#from must convert a income TRANSFER transaction`() {
        val originAccountId = 1L
        val destinyAccountId = 2L
        val transaction = buildFixture<Transaction>("transfer").copy(
            originAccountId = originAccountId,
            destinyAccountId = destinyAccountId
        )

        val accountStatement = AccountStatementData.from(destinyAccountId, transaction)

        assertThat(accountStatement.amount).isEqualTo(transaction.amount)
        assertThat(accountStatement.extraAmount).isEqualTo(transaction.extraAmount)
        assertThat(accountStatement.relatedTo).isEqualTo(originAccountId)
        assertThat(accountStatement.createdAt).isEqualTo(transaction.createdAt)
        assertThat(accountStatement.kind).isEqualTo(StatementKindData.INCOME)
    }
}
