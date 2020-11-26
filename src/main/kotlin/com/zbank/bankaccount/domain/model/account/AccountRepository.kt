package com.zbank.bankaccount.domain.model.account

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : CrudRepository<Account, Long> {

    fun existsByCpf(cpf: String): Boolean
}
