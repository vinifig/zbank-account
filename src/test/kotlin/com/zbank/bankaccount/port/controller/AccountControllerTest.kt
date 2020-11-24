package com.zbank.bankaccount.port.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.zbank.bankaccount.AbstractBaseTest
import com.zbank.bankaccount.application.AccountApplicationService
import com.zbank.bankaccount.application.command.CreateAccountCommand
import com.zbank.bankaccount.domain.model.account.Account
import org.hamcrest.Matcher

import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`

@WebMvcTest(AccountController::class)
class AccountControllerTest(
    @Mock private val accountApplicationServiceMock: AccountApplicationService,
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) : AbstractBaseTest() {

    @Test
    fun `post account must return created when the cpf is available`() {
        val accountCommand = buildFixture<CreateAccountCommand>("default")
        val expectedAccount = buildFixture<Account>("zeroBalance").copy(
            name = accountCommand.name,
            cpf = accountCommand.cpf
        )

        `when`(accountApplicationServiceMock.createAccount(accountCommand)).thenReturn(expectedAccount)

        val accountCommandContent = objectMapper.writeValueAsString(accountCommand)
        mockMvc.perform(post("/account")
            .content(accountCommandContent)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id", equalTo(expectedAccount.id)))
            .andExpect(jsonPath("$.name", equalTo(expectedAccount.name)))
            .andExpect(jsonPath("$.cpf", equalTo(expectedAccount.cpf)))
            .andExpect(jsonPath("$.balance", equalTo(expectedAccount.balance)))
    }
}
