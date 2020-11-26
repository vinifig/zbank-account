package com.zbank.bankaccount.template

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.zbank.bankaccount.application.data.AccountBalanceData
import com.zbank.bankaccount.application.data.AccountData

class AccountBalanceDataTemplate : TemplateLoader {

    override fun load() {
        Fixture.of(AccountBalanceData::class.java)
            .addTemplate("default", Rule().apply {
                add("id", uniqueRandom(*(1L..999L).toList().toTypedArray()))
                add("balance", uniqueRandom(*(1..999).map { it.toDouble() }.toTypedArray()))
            })
    }

}
