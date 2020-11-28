package com.zbank.bankaccount.template

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.zbank.bankaccount.application.data.AccountStatementData
import com.zbank.bankaccount.application.data.StatementKindData
import java.time.OffsetDateTime

class AccountStatementDataTemplate : TemplateLoader {

    override fun load() {
        Fixture.of(AccountStatementData::class.java)
            .addTemplate("default", Rule().apply {
                add("amount", uniqueRandom(*(1..999).map { it.toFloat() }.toTypedArray()))
                add("extraAmount", uniqueRandom(*(1..999).map { it.toFloat() }.toTypedArray()))
                add("kind", uniqueRandom(*StatementKindData.values().toList().toTypedArray()))
                add("relatedTo", uniqueRandom(*(1L..999L).toList().toTypedArray()))
                add("createdAt", OffsetDateTime.now())
            })
    }

}
