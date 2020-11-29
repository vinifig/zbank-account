package com.zbank.bankaccount

import com.zbank.bankaccount.port.config.TestSecurityConfig
import org.springframework.boot.autoconfigure.ImportAutoConfiguration

const val MOCK_USER = "1"
const val LONG_MOCK_USER = 1L

@ImportAutoConfiguration(classes = [TestSecurityConfig::class])
abstract class AbstractControllerTest : AbstractBaseTest()
