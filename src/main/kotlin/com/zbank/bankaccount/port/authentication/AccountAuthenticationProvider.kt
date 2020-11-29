package com.zbank.bankaccount.port.authentication

import com.zbank.bankaccount.application.AccountApplicationService
import com.zbank.bankaccount.domain.model.account.Account
import com.zbank.bankaccount.domain.model.account.AccountNotFoundException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component


const val USER_ROLE = "USER_ROLE"
const val ACCOUNT_ID_AUTHORIZATION_EXPRESSION = "authentication.name.equals(#accountId.toString())"

@Component
class AccountAuthenticationProvider(
    private val accountApplicationService: AccountApplicationService
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val accountId = authentication.name.toLong()
        val cpf = authentication.credentials.toString()

        validateAuthentication(accountId, cpf)

        val authorities = arrayListOf(
            SimpleGrantedAuthority(USER_ROLE)
        )

        return UsernamePasswordAuthenticationToken(accountId.toString(), cpf, authorities)
    }

    override fun supports(authentication: Class<*>): Boolean =
        authentication == UsernamePasswordAuthenticationToken::class.java

    private fun validateAuthentication(accountId: Long, cpf: String) {
        val account = try {
            val account: Account = accountApplicationService.getAccount(accountId)

            account.takeIf { it.cpf == cpf }
        } catch (ex: AccountNotFoundException) {
            null
        }

        account ?: throw AccountAuthenticationException(accountId)
    }

}
