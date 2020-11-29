package com.zbank.bankaccount.port.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import com.zbank.bankaccount.port.model.error.ApiError
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AccountAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : BasicAuthenticationEntryPoint() {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val error = ApiError(HttpStatus.UNAUTHORIZED.value(), authException.message!!, "Unauthorized")

        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"$realmName\"")
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        response.status = error.status
        response.writer.println(objectMapper.writeValueAsString(error))
    }

    override fun afterPropertiesSet() {
        realmName = "Account"
        super.afterPropertiesSet()
    }
}
