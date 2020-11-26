package com.zbank.bankaccount.domain.model.account

import com.zbank.bankaccount.domain.model.common.EntityNotFoundException

class AccountNotFoundException(id: Long) : EntityNotFoundException("Account", id)
