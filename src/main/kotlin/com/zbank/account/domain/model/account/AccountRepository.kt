package com.zbank.account.domain.model.account

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : CrudRepository<Account, Int> {

    fun existsByCpf(cpf: String): Boolean
}
