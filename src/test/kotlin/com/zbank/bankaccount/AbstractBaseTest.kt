package com.zbank.bankaccount

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import org.mockito.Mockito
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
abstract class AbstractBaseTest {

    init {
        FixtureFactoryLoader.loadTemplates("com.zbank.bankaccount.template");
    }

    inline fun <reified T : Any> buildFixture(label: String): T {
        return Fixture.from(T::class.java).gimme(label)
    }

    inline fun <reified T : Any> buildFixture(quantity: Int, label: String): List<T> {
        return Fixture.from(T::class.java).gimme(quantity, label)
    }

    fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

}
