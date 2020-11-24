package com.zbank.account.domain.model.account

import com.zbank.account.domain.model.common.BusinessException

class NoBalanceAvailableException : BusinessException("No balance available to perform operation.")
