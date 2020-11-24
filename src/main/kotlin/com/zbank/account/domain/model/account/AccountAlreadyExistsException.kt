package com.zbank.account.domain.model.account

import com.zbank.account.domain.model.common.BusinessException

class AccountAlreadyExistsException(val cpf: String) : BusinessException("The cpf $cpf, is already in use")
