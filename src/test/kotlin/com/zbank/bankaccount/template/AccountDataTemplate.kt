package com.zbank.bankaccount.template

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.zbank.bankaccount.application.data.AccountData

class AccountDataTemplate : TemplateLoader {

    override fun load() {
        Fixture.of(AccountData::class.java)
            .addTemplate("default", Rule().apply {
                add("id", uniqueRandom(*(1L..999L).toList().toTypedArray()))
                add("cpf", "012.345.678-90")
            })
    }

}
