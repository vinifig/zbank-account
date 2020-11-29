package com.zbank.bankaccount.port.authentication

import org.springframework.security.core.AuthenticationException

class AccountAuthenticationException(
    val accountId: Long
) : AuthenticationException("Could not authenticate to $accountId")
