package com.school.service

import com.school.model.ProjectVersion
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.FileReader

@Service
class ProjectVersionService {
    private val log = LoggerFactory.getLogger(ProjectVersionService::class.java)
    fun getCurrentProjectVersion(): ProjectVersion {
        try {
            val mavenXpp3Reader = MavenXpp3Reader()
            val model = mavenXpp3Reader.read(FileReader("pom.xml"))
            return ProjectVersion(model.id, model.groupId, model.artifactId, model.version, "Have a nice day :)")
                .also { log.info("Fetched project version: $it") }
        } catch (e: IllegalStateException) {
            log.warn("Could not extract project version information!")
            log.warn(e.message)
            return ProjectVersion(
                "",
                "",
                "",
                "",
                "Error fetching project version!"
            )
        }
    }
}