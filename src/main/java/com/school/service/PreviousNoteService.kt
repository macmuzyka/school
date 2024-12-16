package com.school.service

import com.school.configuration.ApplicationConfig
import com.school.model.dto.PreviousNoteDTO
import com.school.model.entity.PreviousNote
import com.school.repository.PreviousNoteRepository
import com.school.repository.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PreviousNoteService(private val subjectRepository: SubjectRepository,
                          private val previousNoteRepository: PreviousNoteRepository,
                          private val applicationConfig: ApplicationConfig
) {
    private val log = LoggerFactory.getLogger(PreviousNoteService::class.java)
    fun saveSubjectNote(note: PreviousNoteDTO): PreviousNote {
        val (content, subjectId) = note.also { log.info("Note: $it") }
        val subjectName = findCorrespondingSubjectName(subjectId)
        val previousNotes = previousNoteRepository.findPreviousNotesBySubject(subjectName)

        if (notesExceededMax(previousNotes)) {
            log.info("Need to make space for new note")
            deleteOldestNote(previousNotes)
        }

        return previousNoteRepository.save(PreviousNote(content, subjectName))
    }

    private fun findCorrespondingSubjectName(subjectId: Long): String = subjectRepository.findById(subjectId).takeIf { it.isPresent }?.get()?.name
            ?: throw IllegalArgumentException("Could not find subject based on provided subject ID [$subjectId]")

    private fun deleteOldestNote(previousNotes: List<PreviousNote>) =
            previousNotes
                    .sortedBy { it.createdAt }
                    .first()
                    .let { toDelete ->
                        previousNoteRepository.delete(toDelete)
                        log.info("Deleting note $toDelete as oldest")
                    }

    private fun notesExceededMax(previousNotes: List<PreviousNote>) = previousNotes.size >= applicationConfig.maxPreviousNotes

    fun getSubjectNotes(subjectId: Long?): List<PreviousNoteDTO> {
        return subjectId?.let {
            val subjectName = findCorrespondingSubjectName(subjectId)
            previousNoteRepository.findPreviousNotesBySubject(subjectName)
                    .sortedByDescending { it.createdAt }
                    .map { PreviousNoteDTO(it.content, subjectId, subjectName) }
                    .also { log.info("Returning notes based on passed ID [$subjectId]") }
        } ?: previousNoteRepository.findAll()
                .sortedByDescending { it.createdAt }
                .map { PreviousNoteDTO(it.content, 0L, it.subject) }
                .also { log.info("Returning notes without any filter") }
    }
}