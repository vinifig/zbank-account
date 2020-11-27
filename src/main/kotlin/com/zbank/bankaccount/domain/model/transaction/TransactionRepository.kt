package com.zbank.bankaccount.domain.model.transaction

import org.springframework.data.repository.PagingAndSortingRepository

interface TransactionRepository : PagingAndSortingRepository<Transaction, Long>
