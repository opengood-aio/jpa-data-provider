package app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.sql.Date as SqlDate

@Configuration
class TestAppConfig {

    @Bean
    fun clock(): Clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())

    @Bean
    fun dateString(clock: Clock, isoDateFormatter: DateTimeFormatter): String =
        LocalDate.now(clock).format(isoDateFormatter)

    @Bean
    fun isoDateFormatter(): DateTimeFormatter = DateTimeFormatter.ISO_DATE

    @Bean
    fun isoDateTimeFormatter(): DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

    @Bean
    fun sqlDate(clock: Clock): SqlDate = SqlDate(clock.millis())

    @Bean
    fun timestampString(clock: Clock, isoDateTimeFormatter: DateTimeFormatter): String =
        LocalDateTime.now(clock).format(isoDateTimeFormatter)

    @Bean
    fun uuid(): UUID = UUID.randomUUID()
}
