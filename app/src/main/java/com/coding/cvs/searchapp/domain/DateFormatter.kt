package com.coding.cvs.searchapp.domain

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object DateFormatter {
    private val formatter: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .withZone(ZoneId.systemDefault())

    /**
     * The API returns ISO-8601 timestamps like "2017-11-04T18:48:46.250Z".
     */
    fun formatCreatedDate(apiCreated: String): String {
        return try {
            val instant = Instant.parse(apiCreated)
            formatter.format(instant)
        } catch (_: Exception) {
            apiCreated
        }
    }
}

