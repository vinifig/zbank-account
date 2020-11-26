package com.zbank.bankaccount.port.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.zbank.bankaccount.AbstractBaseTest
import com.zbank.bankaccount.application.AccountApplicationService
import com.zbank.bankaccount.application.command.CreateAccountCommand
import com.zbank.bankaccount.application.data.AccountData
import com.zbank.bankaccount.domain.model.account.AccountAlreadyExistsException
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AccountController::class)
class AccountControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) : AbstractBaseTest() {

    @MockBean
    private lateinit var accountApplicationServiceMock: AccountApplicationService

    @Test
    fun `post account must return created when the cpf is available`() {
        val accountCommand = buildFixture<CreateAccountCommand>("default")
        val expectedAccount = buildFixture<AccountData>("default").copy(
            cpf = accountCommand.cpf
        )

        `when`(accountApplicationServiceMock.createAccount(accountCommand)).thenReturn(expectedAccount)

        val accountCommandContent = objectMapper.writeValueAsString(accountCommand)
        mockMvc.perform(post("/accounts")
            .content(accountCommandContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id", equalTo(expectedAccount.id?.toInt())))
            .andExpect(jsonPath("$.cpf", equalTo(expectedAccount.cpf)))
    }

    @Test
    fun `post account must return unprocessable entity when the cpf is unavailable`() {
        val accountCommand = buildFixture<CreateAccountCommand>("default")
        val exception = AccountAlreadyExistsException(accountCommand.cpf)

        `when`(accountApplicationServiceMock.createAccount(accountCommand)).thenThrow(exception)

        val accountCommandContent = objectMapper.writeValueAsString(accountCommand)
        mockMvc.perform(post("/accounts")
            .content(accountCommandContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.status", equalTo(422)))
            .andExpect(jsonPath("$.message", equalTo(exception.message)))
    }
}
