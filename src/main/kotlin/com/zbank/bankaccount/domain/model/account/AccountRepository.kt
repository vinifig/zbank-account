package com.zbank.bankaccount.domain.model.account

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : CrudRepository<Account, Int> {

    fun existsByCpf(cpf: String): Boolean
}
