package com.zbank.bankaccount.template

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.zbank.bankaccount.domain.model.transaction.TransactionKind
import com.zbank.bankaccount.port.model.TransactionOperation

class TransactionOperationTemplate : TemplateLoader {

    override fun load() {
        Fixture.of(TransactionOperation::class.java)
            .addTemplate("default", Rule().apply {
                add("amount", uniqueRandom(*(1..999).map { it.toFloat() }.toTypedArray()))
                add("kind", TransactionKind.WITHDRAW)
                add("targetAccountId", uniqueRandom(*(1L..999L).toList().toTypedArray()))
            })
            .addTemplate("withdraw").inherits("default", Rule())
            .addTemplate("deposit").inherits("default", Rule().apply {
                add("kind", TransactionKind.DEPOSIT)
            })
            .addTemplate("transfer").inherits("default", Rule().apply {
                add("kind", TransactionKind.TRANSFER)
            })
    }

}
