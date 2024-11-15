package com.school.service

import com.school.configuration.ApplicationConfig
import com.school.model.statistics.ProgressRecord
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Service
class ProgressRecordExportService(
        private val environmentService: EnvironmentService,
        private val applicationConfig: ApplicationConfig
) {
    private val log = LoggerFactory.getLogger(ProgressRecordExportService::class.java)

    companion object {
        @JvmField
        var progressRecords: List<ProgressRecord> = mutableListOf()
        @JvmField
        var recordsReadyToExport: Boolean = false
    }

    private fun exportProgressRecordsToXlsFile(progressRecords: List<ProgressRecord>) {
        val currentDatabase = environmentService.resolveCurrentDataBase()
        val file = File(applicationConfig.progressRecordsDirectory + applicationConfig.progressRecordsFile)
        val workbook: Workbook
        val sheet: Sheet

        if (file.exists()) {
            FileInputStream(file).use { fis ->
                workbook = XSSFWorkbook(fis)
            }
            sheet = workbook.getSheetAt(0) ?: workbook.createSheet("Seed Data")
        } else {
            workbook = XSSFWorkbook()
            sheet = workbook.createSheet("Seed Data")

            val headerRow = sheet.createRow(0)
            headerRow.createCell(0).setCellValue("Total Records")
            headerRow.createCell(1).setCellValue("Data Source")
            headerRow.createCell(2).setCellValue("Progress %")
            headerRow.createCell(3).setCellValue("Duration (ms)")
            headerRow.createCell(4).setCellValue("Total Records")
            headerRow.createCell(5).setCellValue("Data Source")
            headerRow.createCell(6).setCellValue("Progress %")
            headerRow.createCell(7).setCellValue("Duration (ms)")
        }

        val startRow = sheet.lastRowNum + 1
        val recordRowCount = progressRecords.size

        if (recordRowCount > 0) {
            val initialRow = sheet.createRow(startRow)

            val startColumn = if (currentDatabase == "h2") 0 else 4

            val totalRecordsCell = initialRow.createCell(startColumn)
            totalRecordsCell.setCellValue(applicationConfig.gradesToAdd.toDouble())

            sheet.addMergedRegion(CellRangeAddress(startRow, startRow + recordRowCount - 1, startColumn, startColumn))

            val dataSourceCell = initialRow.createCell(startColumn + 1)
            dataSourceCell.setCellValue(currentDatabase)
            sheet.addMergedRegion(CellRangeAddress(startRow, startRow + recordRowCount - 1, startColumn + 1, startColumn + 1))

            progressRecords.forEachIndexed { index, progressRecord ->
                val row = sheet.getRow(startRow + index) ?: sheet.createRow(startRow + index)
                row.createCell(startColumn + 2).setCellValue(progressRecord.percentageProgress.toDouble())
                row.createCell(startColumn + 3).setCellValue(progressRecord.duration)
            }
        }

        FileOutputStream(file).use { fos ->
            workbook.write(fos)
        }
        workbook.close()
    }

    @Scheduled(cron = "*/15 * * * * *")
    fun scheduledExportTask() {
        if (progressRecords.isNotEmpty() && recordsReadyToExport) {
            log.info("[Executing scheduled export task]")
            exportProgressRecordsToXlsFile(progressRecords)
            progressRecords = mutableListOf()
            recordsReadyToExport = false
        }
    }
}