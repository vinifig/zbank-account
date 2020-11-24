package com.zbank.bankaccount.template

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import com.zbank.bankaccount.application.command.CreateAccountCommand

class CreateAccountCommandTemplate : TemplateLoader {
    override fun load() {
        Fixture.of(CreateAccountCommand::class.java)
            .addTemplate("default", Rule().apply {
                add("name", regex("\\w{10}"))
                add("cpf", "012.345.678-90")
            })
    }
}
