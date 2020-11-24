package com.zbank.bankaccount.port.controller

import com.zbank.bankaccount.application.AccountApplicationService
import com.zbank.bankaccount.application.command.CreateAccountCommand
import com.zbank.bankaccount.domain.model.account.Account
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountApplicationService: AccountApplicationService
) {

    @PostMapping
    fun createAccount(@RequestBody accountCommand: CreateAccountCommand): ResponseEntity<Account> {
        val account = accountApplicationService.createAccount(accountCommand)

        return ResponseEntity(account, HttpStatus.CREATED)
    }


}
