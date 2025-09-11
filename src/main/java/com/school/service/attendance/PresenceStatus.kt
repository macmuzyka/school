package com.school.service.attendance

enum class PresenceStatus(present: Boolean) {
    PRESENT(true),
    ABSENT(false),
    LATE(true),
    LEFT_EARLY(true),
    UNSPECIFIED(false)
}