package com.zbank.bankaccount.port.controller

import com.zbank.bankaccount.application.TransactionApplicationService
import com.zbank.bankaccount.domain.model.transaction.Transaction
import com.zbank.bankaccount.domain.model.transaction.TransactionKind.*
import com.zbank.bankaccount.port.controller.model.TransactionOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/accounts/{accountId}/transactions")
class TransactionController(
    private val transactionApplicationService: TransactionApplicationService
) {

    @PostMapping
    fun executeTransaction(
        @PathVariable accountId: Long,
        @RequestBody transaction: TransactionOperation
    ) : ResponseEntity<Transaction> {
        val transaction = when(transaction.kind) {
            WITHDRAW -> transactionApplicationService.withdraw(accountId, transaction.amount)
            DEPOSIT -> transactionApplicationService.deposit(accountId, transaction.amount)
            TRANSFER -> throw NotImplementedError()
        }

        return ResponseEntity(transaction, HttpStatus.CREATED)
    }
}
