package com.zbank.account.domain.model.account

import com.zbank.account.domain.model.common.BusinessException

class NegativeAmountException : BusinessException("The amount must be a positive one")
