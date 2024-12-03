package com.school.service

import org.springframework.stereotype.Service

@Service
class EnvironmentService(
        private val environment: org.springframework.core.env.Environment
) {

    fun profileOtherThanDefaultIsActive() = environment.activeProfiles.isNotEmpty()
    fun currentProfileOtherThanDevel() = !environment.activeProfiles.contains("devel")
    fun currentProfileIsDevel() = environment.activeProfiles.contains("devel")
    fun resolveCurrentDataBase(): String {
        return if (environment.activeProfiles.contains("devel")) {
            "h2"
        } else {
            "PostgresSQL"
        }
    }
}