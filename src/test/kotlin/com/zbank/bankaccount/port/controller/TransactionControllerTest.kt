package com.zbank.bankaccount.port.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.zbank.bankaccount.AbstractControllerTest
import com.zbank.bankaccount.LONG_MOCK_USER
import com.zbank.bankaccount.MOCK_USER
import com.zbank.bankaccount.application.TransactionApplicationService
import com.zbank.bankaccount.application.data.AccountStatementData
import com.zbank.bankaccount.domain.model.account.AccountNotFoundException
import com.zbank.bankaccount.domain.model.account.NegativeAmountException
import com.zbank.bankaccount.domain.model.account.NoBalanceAvailableException
import com.zbank.bankaccount.domain.model.transaction.Transaction
import com.zbank.bankaccount.port.model.TransactionOperation
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TransactionController::class)
class TransactionControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) : AbstractControllerTest() {

    @MockBean
    private lateinit var transactionApplicationServiceMock: TransactionApplicationService

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `post a withdraw transaction must return not found when the account id does not exists`() {
        val invalidAccountId = LONG_MOCK_USER
        val operation = buildFixture<TransactionOperation>("withdraw")
        val exception = AccountNotFoundException(invalidAccountId)

        `when`(transactionApplicationServiceMock.withdraw(invalidAccountId, operation.amount))
            .thenThrow(exception)

        val operationContent = objectMapper.writeValueAsString(operation)
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/$invalidAccountId/transactions")
            .content(operationContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
            .andExpect(jsonPath("$.message", equalTo(exception.message)))

        verify(transactionApplicationServiceMock).withdraw(invalidAccountId, operation.amount)
        verify(transactionApplicationServiceMock, never()).deposit(invalidAccountId, operation.amount)
        verify(transactionApplicationServiceMock, never())
            .transfer(invalidAccountId, operation.targetAccountId!!, operation.amount)
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `post a deposit transaction must return not found when the account id does not exists`() {
        val invalidAccountId = LONG_MOCK_USER
        val operation = buildFixture<TransactionOperation>("deposit")
        val exception = AccountNotFoundException(invalidAccountId)

        `when`(transactionApplicationServiceMock.deposit(invalidAccountId, operation.amount))
            .thenThrow(exception)

        val operationContent = objectMapper.writeValueAsString(operation)
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/$invalidAccountId/transactions")
            .content(operationContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
            .andExpect(jsonPath("$.message", equalTo(exception.message)))

        verify(transactionApplicationServiceMock, never()).withdraw(invalidAccountId, operation.amount)
        verify(transactionApplicationServiceMock).deposit(invalidAccountId, operation.amount)
        verify(transactionApplicationServiceMock, never())
            .transfer(invalidAccountId, operation.targetAccountId!!, operation.amount)
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `post a transfer transaction must return not found when some account id does not exists`() {
        val invalidAccountId = LONG_MOCK_USER
        val operation = buildFixture<TransactionOperation>("transfer")
        val exception = AccountNotFoundException(invalidAccountId)

        `when`(
            transactionApplicationServiceMock.transfer(invalidAccountId, operation.targetAccountId!!, operation.amount)
        ).thenThrow(exception)

        val operationContent = objectMapper.writeValueAsString(operation)
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/$invalidAccountId/transactions")
            .content(operationContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
            .andExpect(jsonPath("$.message", equalTo(exception.message)))

        verify(transactionApplicationServiceMock, never()).withdraw(invalidAccountId, operation.amount)
        verify(transactionApplicationServiceMock, never()).deposit(invalidAccountId, operation.amount)
        verify(transactionApplicationServiceMock)
            .transfer(invalidAccountId, operation.targetAccountId!!, operation.amount)
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `post a withdraw transaction must return unprocessable entity when the amount is negative`() {
        val accountId = LONG_MOCK_USER
        val operation = buildFixture<TransactionOperation>("withdraw")
        val exception = NegativeAmountException()

        `when`(transactionApplicationServiceMock.withdraw(accountId, operation.amount))
            .thenThrow(exception)

        val operationContent = objectMapper.writeValueAsString(operation)
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/$accountId/transactions")
            .content(operationContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.status", equalTo(HttpStatus.UNPROCESSABLE_ENTITY.value())))
            .andExpect(jsonPath("$.message", equalTo(exception.message)))

        verify(transactionApplicationServiceMock).withdraw(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never()).deposit(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never())
            .transfer(accountId, operation.targetAccountId!!, operation.amount)
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `post a deposit transaction must return unprocessable entity when the amount is negative`() {
        val accountId = LONG_MOCK_USER
        val operation = buildFixture<TransactionOperation>("deposit")
        val exception = NegativeAmountException()

        `when`(transactionApplicationServiceMock.deposit(accountId, operation.amount))
            .thenThrow(exception)

        val operationContent = objectMapper.writeValueAsString(operation)
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/$accountId/transactions")
            .content(operationContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.status", equalTo(HttpStatus.UNPROCESSABLE_ENTITY.value())))
            .andExpect(jsonPath("$.message", equalTo(exception.message)))

        verify(transactionApplicationServiceMock, never()).withdraw(accountId, operation.amount)
        verify(transactionApplicationServiceMock).deposit(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never())
            .transfer(accountId, operation.targetAccountId!!, operation.amount)
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `post a transfer transaction must return unprocessable entity when the amount is negative`() {
        val accountId = LONG_MOCK_USER
        val operation = buildFixture<TransactionOperation>("transfer")
        val exception = NegativeAmountException()

        `when`(transactionApplicationServiceMock.transfer(accountId, operation.targetAccountId!!, operation.amount))
            .thenThrow(exception)

        val operationContent = objectMapper.writeValueAsString(operation)
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/$accountId/transactions")
            .content(operationContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.status", equalTo(HttpStatus.UNPROCESSABLE_ENTITY.value())))
            .andExpect(jsonPath("$.message", equalTo(exception.message)))

        verify(transactionApplicationServiceMock, never()).withdraw(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never()).deposit(accountId, operation.amount)
        verify(transactionApplicationServiceMock)
            .transfer(accountId, operation.targetAccountId!!, operation.amount)
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `post a withdraw transaction must return unprocessable entity when the amount is is not available`() {
        val accountId = LONG_MOCK_USER
        val operation = buildFixture<TransactionOperation>("withdraw")
        val exception = NoBalanceAvailableException()

        `when`(transactionApplicationServiceMock.withdraw(accountId, operation.amount))
            .thenThrow(exception)

        val operationContent = objectMapper.writeValueAsString(operation)
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/$accountId/transactions")
            .content(operationContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.status", equalTo(HttpStatus.UNPROCESSABLE_ENTITY.value())))
            .andExpect(jsonPath("$.message", equalTo(exception.message)))

        verify(transactionApplicationServiceMock).withdraw(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never()).deposit(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never())
            .transfer(accountId, operation.targetAccountId!!, operation.amount)
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `post a transfer transaction must return unprocessable entity when the amount is is not available`() {
        val accountId = LONG_MOCK_USER
        val operation = buildFixture<TransactionOperation>("transfer")
        val exception = NoBalanceAvailableException()

        `when`(transactionApplicationServiceMock.transfer(accountId, operation.targetAccountId!!, operation.amount))
            .thenThrow(exception)

        val operationContent = objectMapper.writeValueAsString(operation)
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/$accountId/transactions")
            .content(operationContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.status", equalTo(HttpStatus.UNPROCESSABLE_ENTITY.value())))
            .andExpect(jsonPath("$.message", equalTo(exception.message)))

        verify(transactionApplicationServiceMock, never()).withdraw(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never()).deposit(accountId, operation.amount)
        verify(transactionApplicationServiceMock)
            .transfer(accountId, operation.targetAccountId!!, operation.amount)
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `post a withdraw transaction must return created if no error occurs`() {
        val accountId = LONG_MOCK_USER
        val operation = buildFixture<TransactionOperation>("withdraw")
        val transaction = buildFixture<Transaction>("withdraw")

        `when`(transactionApplicationServiceMock.withdraw(accountId, operation.amount))
            .thenReturn(transaction)

        val operationContent = objectMapper.writeValueAsString(operation)
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/$accountId/transactions")
            .content(operationContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id", equalTo(transaction.id?.toInt())))
            .andExpect(jsonPath("$.origin_account_id", equalTo(transaction.originAccountId.toInt())))
            .andExpect(jsonPath("$.destiny_account_id", equalTo(transaction.destinyAccountId?.toInt())))
            .andExpect(jsonPath("$.kind", equalTo(transaction.kind.toString())))
            .andExpect(jsonPath("$.amount", equalTo(transaction.amount.toDouble())))
            .andExpect(jsonPath("$.extra_amount", equalTo(transaction.extraAmount.toDouble())))

        verify(transactionApplicationServiceMock).withdraw(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never()).deposit(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never())
            .transfer(accountId, operation.targetAccountId!!, operation.amount)
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `post a deposit transaction must return created if no error occurs`() {
        val accountId = LONG_MOCK_USER
        val operation = buildFixture<TransactionOperation>("deposit")
        val transaction = buildFixture<Transaction>("deposit")

        `when`(transactionApplicationServiceMock.deposit(accountId, operation.amount))
            .thenReturn(transaction)

        val operationContent = objectMapper.writeValueAsString(operation)
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/$accountId/transactions")
            .content(operationContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id", equalTo(transaction.id?.toInt())))
            .andExpect(jsonPath("$.origin_account_id", equalTo(transaction.originAccountId.toInt())))
            .andExpect(jsonPath("$.destiny_account_id", equalTo(transaction.destinyAccountId?.toInt())))
            .andExpect(jsonPath("$.kind", equalTo(transaction.kind.toString())))
            .andExpect(jsonPath("$.amount", equalTo(transaction.amount.toDouble())))
            .andExpect(jsonPath("$.extra_amount", equalTo(transaction.extraAmount.toDouble())))

        verify(transactionApplicationServiceMock, never()).withdraw(accountId, operation.amount)
        verify(transactionApplicationServiceMock).deposit(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never())
            .transfer(accountId, operation.targetAccountId!!, operation.amount)
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `post a transfer transaction must return created if no error occurs`() {
        val accountId = LONG_MOCK_USER
        val operation = buildFixture<TransactionOperation>("transfer")
        val transaction = buildFixture<Transaction>("transfer")

        `when`(transactionApplicationServiceMock.transfer(accountId, operation.targetAccountId!!, operation.amount))
            .thenReturn(transaction)

        val operationContent = objectMapper.writeValueAsString(operation)
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/$accountId/transactions")
            .content(operationContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id", equalTo(transaction.id?.toInt())))
            .andExpect(jsonPath("$.origin_account_id", equalTo(transaction.originAccountId.toInt())))
            .andExpect(jsonPath("$.destiny_account_id", equalTo(transaction.destinyAccountId?.toInt())))
            .andExpect(jsonPath("$.kind", equalTo(transaction.kind.toString())))
            .andExpect(jsonPath("$.amount", equalTo(transaction.amount.toDouble())))
            .andExpect(jsonPath("$.extra_amount", equalTo(transaction.extraAmount.toDouble())))

        verify(transactionApplicationServiceMock, never()).withdraw(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never()).deposit(accountId, operation.amount)
        verify(transactionApplicationServiceMock)
            .transfer(accountId, operation.targetAccountId!!, operation.amount)
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `post a transaction must return forbidden if provide other id than the authenticated`() {
        val accountId = LONG_MOCK_USER + 1
        val operation = buildFixture<TransactionOperation>("default")
        val operationContent = objectMapper.writeValueAsString(operation)

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/$accountId/transactions")
            .content(operationContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden)

        verify(transactionApplicationServiceMock, never()).withdraw(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never()).deposit(accountId, operation.amount)
        verify(transactionApplicationServiceMock, never())
            .transfer(accountId, operation.targetAccountId!!, operation.amount)
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `get transactions must return not found when the accountId does not exists`() {
        val accountId = LONG_MOCK_USER
        val exception = AccountNotFoundException(accountId)

        `when`(transactionApplicationServiceMock.getAccountStatement(anyLong(), any(Pageable::class.java)))
            .thenThrow(exception)

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/$accountId/transactions"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
            .andExpect(jsonPath("$.message", equalTo(exception.message)))
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `get transactions must return not found when the account has no transactions`() {
        val accountId = LONG_MOCK_USER
        val accountStatementPage = Page.empty<AccountStatementData>()

        `when`(transactionApplicationServiceMock.getAccountStatement(anyLong(), any(Pageable::class.java)))
            .thenReturn(accountStatementPage)

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/$accountId/transactions"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.content", empty<AccountStatementData>()))
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `get transactions must return ok when the account has transactions`() {
        val accountId = LONG_MOCK_USER
        val accountStatement = buildFixture<AccountStatementData>(10, "default")
        val accountStatementPage = PageImpl(accountStatement)

        `when`(transactionApplicationServiceMock.getAccountStatement(anyLong(), any(Pageable::class.java)))
            .thenReturn(accountStatementPage)

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/$accountId/transactions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()", equalTo(accountStatement.size)))
    }

    @Test
    @WithMockUser(username = MOCK_USER)
    fun `get transactions must return forbidden if provide other id than the authenticated`() {
        val accountId = LONG_MOCK_USER + 1

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/$accountId/transactions"))
            .andExpect(status().isForbidden)
    }
}
