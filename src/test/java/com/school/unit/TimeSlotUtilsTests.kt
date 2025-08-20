package com.school.unit

import com.school.service.utils.TimeSlotUtils.Companion.isNotLaterThan
import java.time.LocalTime

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TimeSlotUtilsTests {
    @Test
    fun shouldBeEqualOrEarlier() {
        val now = LocalTime.now()
        val alsoNow = LocalTime.now()
        assertTrue { now.isNotLaterThan(alsoNow) }
        assertTrue { alsoNow.isNotLaterThan(now) }

        val minuteEarlier = now.minusMinutes(1)
        assertTrue { minuteEarlier.isNotLaterThan(now) }
    }

    @Test
    fun shouldNotExceedGivenTime() {
        var notExceeding = LocalTime.of(9, 40)
        val comparedTime = LocalTime.of(9, 40)
        assertTrue { notExceeding.isNotLaterThan(comparedTime) }
        notExceeding = LocalTime.of(9, 39)
        assertTrue { notExceeding.isNotLaterThan(comparedTime) }
    }

    @Test
    fun exceedingGivenTime() {
        val exceeding = LocalTime.of(9, 41)
        val comparedTime = LocalTime.of(9, 40)
        assertFalse { exceeding.isNotLaterThan(comparedTime) }
    }
}