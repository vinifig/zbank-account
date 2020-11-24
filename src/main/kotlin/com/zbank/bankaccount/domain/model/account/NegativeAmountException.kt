package com.zbank.bankaccount.domain.model.account

import com.zbank.bankaccount.domain.model.common.BusinessException

class NegativeAmountException : BusinessException("The amount must be a positive one")
