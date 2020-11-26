package com.zbank.bankaccount.domain.model.account

import com.zbank.bankaccount.domain.model.common.BusinessException

class AccountAlreadyExistsException(val cpf: String) : BusinessException("The cpf $cpf is already in use")
