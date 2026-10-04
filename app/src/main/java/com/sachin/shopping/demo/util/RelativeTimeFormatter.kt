package com.sachin.shopping.demo.util
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object RelativeTimeFormatter {

    fun format(
        timestamp: String,
        now: Instant = Instant.now()
    ): String {
        val instant = try {
            Instant.parse(timestamp)
        } catch (e: Exception) {
            return ""
        }

        val duration = Duration.between(instant, now)

        // Handle future timestamps
        if (duration.isNegative) {
            return "Just now"
        }

        val seconds = duration.seconds

        return when {
            seconds < 60 -> "Just now"

            seconds < 3600 -> {
                val minutes = seconds / 60
                "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
            }

            seconds < 86400 -> {
                val hours = seconds / 3600
                "$hours ${if (hours == 1L) "hour" else "hours"} ago"
            }

            else -> {
                val localDate = instant
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                val today = now
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                when {
                    localDate == today.minusDays(1) -> "Yesterday"
                    localDate.year == today.year ->
                        localDate.format(
                            DateTimeFormatter.ofPattern("dd MMM")
                        )
                    else -> localDate.format(
                        DateTimeFormatter.ofPattern("dd MMM yyyy")
                    )
                }
            }
        }
    }
}
