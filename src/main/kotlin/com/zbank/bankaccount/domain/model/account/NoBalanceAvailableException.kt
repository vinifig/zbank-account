package com.zbank.bankaccount.domain.model.account

import com.zbank.bankaccount.domain.model.common.BusinessException

class NoBalanceAvailableException : BusinessException("No balance available to perform operation.")
