package com.school.model

data class ProjectVersion(
    val id: String,
    val groupId: String,
    val artifactId: String,
    val version: String,
    val description: String
)