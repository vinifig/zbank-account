package com.zbank.bankaccount.port.controller

import com.zbank.bankaccount.application.TransactionApplicationService
import com.zbank.bankaccount.application.data.AccountStatementData
import com.zbank.bankaccount.domain.model.transaction.Transaction
import com.zbank.bankaccount.domain.model.transaction.TransactionKind.*
import com.zbank.bankaccount.port.controller.model.TransactionOperation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Page.empty
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/accounts/{accountId}/transactions")
class TransactionController(
    private val transactionApplicationService: TransactionApplicationService
) {

    @PostMapping
    fun executeTransaction(
        @PathVariable accountId: Long,
        @RequestBody transaction: TransactionOperation
    ): ResponseEntity<Transaction> {
        val transaction = when(transaction.kind) {
            WITHDRAW ->
                transactionApplicationService.withdraw(accountId, transaction.amount)
            DEPOSIT ->
                transactionApplicationService.deposit(accountId, transaction.amount)
            TRANSFER ->
                transactionApplicationService.transfer(accountId, transaction.targetAccountId!!, transaction.amount)
        }

        return ResponseEntity(transaction, HttpStatus.CREATED)
    }

    // TODO: investigate why spring controller is returning route not found 404 for pages when not encapsulated by ResponseEntity
    @GetMapping
    fun getAccountStatement(
        @PathVariable accountId: Long,
        pageable: Pageable
    ): ResponseEntity<Page<AccountStatementData>> {
        val accountStatements = transactionApplicationService.getAccountStatement(accountId, pageable)
        val httpStatus = when(accountStatements) {
            empty<AccountStatementData>() -> HttpStatus.NOT_FOUND
            else -> HttpStatus.OK
        }

        return ResponseEntity(accountStatements, httpStatus)
    }
}
