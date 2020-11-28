package com.zbank.bankaccount.port.config

import com.zbank.bankaccount.AccountApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import java.sql.Timestamp
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Configuration
@EnableJdbcRepositories(basePackageClasses = [AccountApplication::class])
@EnableJdbcAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
class JdbcConfig : AbstractJdbcConfiguration() {

    override fun jdbcCustomConversions(): JdbcCustomConversions {
        return JdbcCustomConversions(listOf(
            TimestampTzToOffsetDateTimeConverter.INSTANCE,
            OffsetDateTimeToTimestampTzConverter.INSTANCE
        ))
    }

    @ReadingConverter
    enum class TimestampTzToOffsetDateTimeConverter : Converter<Timestamp, OffsetDateTime> {
        INSTANCE;

        override fun convert(source: Timestamp): OffsetDateTime {
            return source.toInstant().atOffset(ZoneOffset.UTC);
        }
    }

    @WritingConverter
    enum class OffsetDateTimeToTimestampTzConverter : Converter<OffsetDateTime, Timestamp> {
        INSTANCE;

        override fun convert(source: OffsetDateTime): Timestamp {
            return Timestamp.from(source.toInstant())
        }
    }

    @Bean
    fun auditingDateTimeProvider(): DateTimeProvider? {
        return DateTimeProvider { Optional.of(OffsetDateTime.now()) }
    }

}

