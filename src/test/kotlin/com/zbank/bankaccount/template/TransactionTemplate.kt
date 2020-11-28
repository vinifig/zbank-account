package com.zbank.bankaccount.template

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.zbank.bankaccount.domain.model.transaction.Transaction
import com.zbank.bankaccount.domain.model.transaction.TransactionKind
import java.time.OffsetDateTime

class TransactionTemplate : TemplateLoader {

    override fun load() {
        Fixture.of(Transaction::class.java)
            .addTemplate("default", Rule().apply {
                add("id", uniqueRandom(*(1L..999L).toList().toTypedArray()))
                add("originAccountId", uniqueRandom(*(1L..999L).toList().toTypedArray()))
                add("destinyAccountId", uniqueRandom(*(1L..999L).toList().toTypedArray()))
                add("kind", TransactionKind.WITHDRAW)
                add("amount", uniqueRandom(*(1..999).map { it.toFloat() }.toTypedArray()))
                add("extraAmount", uniqueRandom(*(1..9).map { it.toFloat() }.toTypedArray()))
                add("createdAt", OffsetDateTime.now())
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
