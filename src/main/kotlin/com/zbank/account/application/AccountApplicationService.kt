package com.zbank.account.application

import com.zbank.account.domain.model.account.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountApplicationService(
    private val accountRepository: AccountRepository
) {
    
}
