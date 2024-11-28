package com.school.model

data class OptionalRequestParams(
        val fileType: String? = null,
        val id: Long? = null,
        val name: String? = null,
        val surname: String? = null,
        val identifier: String? = null,
        val subject: String? = null
)