package com.dj.tool.common

import com.dj.tool.common.CommonUtil.closeQuitely
import com.dj.tool.model.ReviewCommentInfoModel
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

object ExcelOperateUtil {
    @Throws(Exception::class)
    fun importExcel(path: String?): List<ReviewCommentInfoModel> {
        val models: MutableList<ReviewCommentInfoModel> = ArrayList()

        var xlsFile: InputStream? = null
        var workbook: XSSFWorkbook? = null
        try {
            xlsFile = FileInputStream(path)
            // 获得工作簿对象
            workbook = XSSFWorkbook(xlsFile)
            // 获得所有工作表,0指第一个表格
            val sheet = workbook.getSheet("Review Comments")

            val lastRowNum = sheet.lastRowNum
            if (lastRowNum < 10) {
                return models
            }


            for (i in 10..lastRowNum) {
                val row = sheet.getRow(i)
                try {
                    val model = ReviewCommentInfoModel()
                    val identifier = row.getCell(0).stringCellValue
                    model.identifier = identifier.toLong()
                    model.reviewer = row.getCell(1).stringCellValue
                    model.comments = row.getCell(2).stringCellValue
                    model.type = row.getCell(3).stringCellValue
                    model.severity = row.getCell(4).stringCellValue
                    model.factor = row.getCell(5).stringCellValue
                    model.filePath = row.getCell(7).stringCellValue
                    model.projectName = row.getCell(6).stringCellValue

                    val lineRange = row.getCell(8).stringCellValue
                    val split = lineRange.split("~".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    model.startLine = split[0].trim { it <= ' ' }.toInt()
                    model.endLine = split[1].trim { it <= ' ' }.toInt()
                    model.author = row.getCell(10).stringCellValue
                    model.content = row.getCell(9).stringCellValue
                    model.dateTime = row.getCell(11).stringCellValue
                    models.add(model)
                } catch (exx: Exception) {
                    exx.printStackTrace()
                }
            }
        } catch (ex: Exception) {
            throw Exception(ex)
        } finally {
            closeQuitely(xlsFile)
            closeQuitely(workbook)
        }

        return models
    }

    @Throws(Exception::class)
    fun exportExcel(path: String?, commentInfoModels: List<ReviewCommentInfoModel?>) {
        val destFile = File(path)

        var xlsFile: InputStream? = null
        var fileOutputStream: FileOutputStream? = null
        var workbook: XSSFWorkbook? = null
        try {
            xlsFile = ExcelOperateUtil::class.java.classLoader.getResourceAsStream("code-review-result.xlsx")
            // 获得工作簿对象
            workbook = XSSFWorkbook(xlsFile)
            // 获得所有工作表,0指第一个表格
            val sheet = workbook.getSheet("Review Comments")


            //设置边框
            val cellStyle = workbook.createCellStyle()
            cellStyle.borderBottom = BorderStyle.THIN // 底部边框
            cellStyle.borderLeft = BorderStyle.THIN // 左边边框
            cellStyle.borderRight = BorderStyle.THIN // 右边边框
            cellStyle.borderTop = BorderStyle.THIN // 上边边框


            // 从0计数，从第11行开始写（index为10）
            var rowIndex = 9
            for (value in commentInfoModels) {
                val sheetRow = sheet.createRow(rowIndex)
                buildCell(sheetRow, cellStyle, 0, value!!.identifier.toString())
                buildCell(sheetRow, cellStyle, 1, value.reviewer)
                buildCell(sheetRow, cellStyle, 2, value.comments)
                buildCell(sheetRow, cellStyle, 3, value.type)
                buildCell(sheetRow, cellStyle, 4, value.severity)
                buildCell(sheetRow, cellStyle, 5, value.factor)
                buildCell(sheetRow, cellStyle, 6, value.projectName)
                buildCell(sheetRow, cellStyle, 7, value.filePath)
                buildCell(sheetRow, cellStyle, 8, value.getLineRange())
                buildCell(sheetRow, cellStyle, 9, value.content)
                buildCell(sheetRow, cellStyle, 10, value.author)
                buildCell(sheetRow, cellStyle, 11, value.dateTime)
                buildCell(sheetRow, cellStyle, 12, "")
                buildCell(sheetRow, cellStyle, 13, "")
                buildCell(sheetRow, cellStyle, 14, "")
                buildCell(sheetRow, cellStyle, 15, "")

                rowIndex++
            }

            //将excel写入
            fileOutputStream = FileOutputStream(destFile)
            workbook.write(fileOutputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e)
        } finally {
            closeQuitely(xlsFile)
            closeQuitely(fileOutputStream)
            closeQuitely(workbook)
        }
    }

    private fun buildCell(sheetRow: XSSFRow, cellStyle: XSSFCellStyle, cellColumnIndex: Int, value: String?) {
        val cell = sheetRow.createCell(cellColumnIndex)
        cell.setCellValue(value)
        cell.cellStyle = cellStyle
    }

    @Throws(Exception::class)
    private fun copyFile(src: File, dest: File) {
        var `in`: FileInputStream? = null
        var out: FileOutputStream? = null
        try {
            `in` = FileInputStream(src)
            out = FileOutputStream(dest)
            val buffer = ByteArray(2048)
            while (true) {
                val ins = `in`.read(buffer)
                if (ins == -1) {
                    break
                }
                out.write(buffer, 0, ins)
            }
            out.flush()
        } catch (e: Exception) {
            throw Exception("copy failed", e)
        } finally {
            closeQuitely(`in`)
            closeQuitely(out)
        }
    }
}
