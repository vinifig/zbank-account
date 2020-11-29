package com.zbank.bankaccount.port.config

import com.zbank.bankaccount.port.authentication.AccountAuthenticationEntryPoint
import com.zbank.bankaccount.port.authentication.AccountAuthenticationProvider
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestSecurityConfig {

    @Bean
    fun getTestAccountAuthenticationProvider()
        = mock(AccountAuthenticationProvider::class.java)

    @Bean
    fun getAccountAuthenticationEntryPoint()
        = mock(AccountAuthenticationEntryPoint::class.java)
}
