package com.iskarman

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

data class Countdown(val months: Long, val days: Long, val hours: Long, val minutes: Long, val seconds: Long) {
    fun formatted(): String = "$months:$days:$hours:$minutes:$seconds"
}

object CountdownCalculator {
    val zone: ZoneId = ZoneId.of("Europe/Sofia")
    val target: ZonedDateTime = ZonedDateTime.of(2026, 9, 12, 10, 0, 0, 0, zone)

    fun calculate(now: ZonedDateTime = ZonedDateTime.now(zone)): Countdown {
        if (!now.isBefore(target)) {
            return Countdown(0, 0, 0, 0, 0)
        }

        var cursor = now
        var months = 0L
        while (true) {
            val next = cursor.plusMonths(1)
            if (next.isAfter(target)) break
            months++
            cursor = next
        }

        var days = 0L
        while (true) {
            val next = cursor.plusDays(1)
            if (next.isAfter(target)) break
            days++
            cursor = next
        }

        val hours = ChronoUnit.HOURS.between(cursor, target)
        cursor = cursor.plusHours(hours)
        val minutes = ChronoUnit.MINUTES.between(cursor, target)
        cursor = cursor.plusMinutes(minutes)
        val seconds = ChronoUnit.SECONDS.between(cursor, target)

        return Countdown(months, days, hours, minutes, seconds)
    }
}
