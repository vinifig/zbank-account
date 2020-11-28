package com.zbank.bankaccount.template

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.zbank.bankaccount.domain.model.account.Account

class AccountTemplate : TemplateLoader {
    override fun load() {
        Fixture.of(Account::class.java)
            .addTemplate("default", Rule().apply {
                add("id", uniqueRandom(*(1L..999L).toList().toTypedArray()))
                add("name", regex("\\w{10}"))
                add("cpf", "012.345.678-90")
                add("balance", uniqueRandom(*(1..999).map { it.toFloat() }.toTypedArray()))
            })
            .addTemplate("zeroBalance").inherits("default", Rule().apply {
                add("balance", 0f)
            })
    }
}
