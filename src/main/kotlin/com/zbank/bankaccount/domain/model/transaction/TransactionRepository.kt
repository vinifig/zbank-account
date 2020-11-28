package com.zbank.bankaccount.domain.model.transaction

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface TransactionRepository : PagingAndSortingRepository<Transaction, Long> {

    // TODO: migrate from spring data jdbc to fix the paginating bug https://jira.spring.io/browse/DATAJDBC-554
    fun findAllByOriginAccountIdOrDestinyAccountId(
        originAccountId: Long,
        destinyAccountId: Long,
        pageable: Pageable
    ): List<Transaction>
}
