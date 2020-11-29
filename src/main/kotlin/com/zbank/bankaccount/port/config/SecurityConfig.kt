package com.zbank.bankaccount.port.config

import com.zbank.bankaccount.port.authentication.AccountAuthenticationEntryPoint
import com.zbank.bankaccount.port.authentication.AccountAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val authProvider: AccountAuthenticationProvider,
    private val accountAuthenticationEntryPoint: AccountAuthenticationEntryPoint
) : WebSecurityConfigurerAdapter() {

    @Autowired
    fun configAuthentication(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authProvider)
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/accounts").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .csrf().disable()
            .formLogin().disable()
            .httpBasic()
            .authenticationEntryPoint(accountAuthenticationEntryPoint)
    }
}
