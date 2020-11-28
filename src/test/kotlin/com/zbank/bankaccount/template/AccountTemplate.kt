package com.zbank.bankaccount.template

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.zbank.bankaccount.domain.model.account.Account
import java.time.OffsetDateTime

class AccountTemplate : TemplateLoader {
    override fun load() {
        Fixture.of(Account::class.java)
            .addTemplate("default", Rule().apply {
                add("id", uniqueRandom(*(1L..999L).toList().toTypedArray()))
                add("name", regex("\\w{10}"))
                add("cpf", regex("\\w{3}.\\w{3}.\\w{3}-\\w{2}"))
                add("balance", uniqueRandom(*(1..999).map { it.toFloat() }.toTypedArray()))
                add("createdAt", OffsetDateTime.now())
                add("updatedAt", OffsetDateTime.now())
            })
            .addTemplate("zeroBalance").inherits("default", Rule().apply {
                add("balance", 0f)
            })
    }
}
