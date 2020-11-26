package com.zbank.bankaccount.port.controller

import com.zbank.bankaccount.application.AccountApplicationService
import com.zbank.bankaccount.application.command.CreateAccountCommand
import com.zbank.bankaccount.application.data.AccountBalanceData
import com.zbank.bankaccount.application.data.AccountData
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountApplicationService: AccountApplicationService
) {

    @PostMapping
    fun createAccount(@RequestBody accountCommand: CreateAccountCommand): ResponseEntity<AccountData> {
        val account = accountApplicationService.createAccount(accountCommand)

        return ResponseEntity(account, HttpStatus.CREATED)
    }

    @GetMapping("/{accountId}/balance")
    fun getAccountBalance(@PathVariable accountId: Long): AccountBalanceData {
        return accountApplicationService.getBalance(accountId)
    }

}
