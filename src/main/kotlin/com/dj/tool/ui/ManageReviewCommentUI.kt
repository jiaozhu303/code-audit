package com.dj.tool.ui

import com.dj.tool.common.*
import com.dj.tool.model.CommentTableModel
import com.dj.tool.model.ReviewCommentInfoModel
import com.dj.tool.publisher.DateRefreshMessagePublisher
import com.dj.tool.render.CommentTableCellRender
import com.dj.tool.ui.CodeAuditNotifier.notifyError
import com.dj.tool.ui.CodeAuditNotifier.notifyInfo
import com.dj.tool.ui.CodeAuditNotifier.notifyWarning
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.util.ui.TextTransferable
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.StringUtils
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.util.*
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.table.TableModel

class ManageReviewCommentUI(private val project: Project) {
    private var clearButton: JButton? = null
    private var deleteButton: JButton? = null
    private var exportButton: JButton? = null
    private var commentTable: JTable? = null
    var fullPanel: JPanel? = null
    private var copyButton: JButton? = null
    private var syncConfluenceButton: JButton? = null

    private var tableData: List<ReviewCommentInfoModel?>

    init {
        this.tableData = ApplicationCache.getProjectAllData(project.name)
    }


    fun initUI() {
        bindButtons()
        reloadTableData()
        bindTableListeners()
    }

    fun reloadTableData() {
        this.tableData = ApplicationCache.getProjectAllData(project.name)
        val rowDataList: MutableList<Array<Any?>> = ArrayList()
        for (model in this.tableData) {
            val row = arrayOf<Any?>(
                model!!.identifier, model.reviewer, model.comments, model.author, model.type,
                model.severity, model.factor, model.projectName, model.filePath, model.getLineRange(), model.content,
                model.dateTime
            )
            rowDataList.add(row)
        }
        val rowData = rowDataList.toTypedArray();
        val dataModel: TableModel = CommentTableModel(rowData, COLUMN_NAMES)
        commentTable!!.model = dataModel
        commentTable!!.isEnabled = true

        // 设置指定列只能通过下拉框选择数据
        val typeComboBox: JComboBox<String> = ComboBox()
        typeComboBox.addItem(Constants.TYPE_QUESTION)
        typeComboBox.addItem(Constants.TYPE_ADVICE)
        typeComboBox.addItem(Constants.TYPE_ALLEGATIONS)
        commentTable!!.columnModel.getColumn(4).cellEditor = DefaultCellEditor(typeComboBox)

        val severityComboBox: JComboBox<String> = ComboBox()
        severityComboBox.addItem(Constants.SEVERITY_WARNING)
        severityComboBox.addItem(Constants.SEVERITY_GENERAL)
        severityComboBox.addItem(Constants.SEVERITY_SERIOUS)
        commentTable!!.columnModel.getColumn(5).cellEditor = DefaultCellEditor(severityComboBox)

        val factorComboBox: JComboBox<String> = ComboBox()
        factorComboBox.addItem(Constants.FACTOR_BASIC)
        factorComboBox.addItem(Constants.FACTOR_BUSINESS)
        factorComboBox.addItem(Constants.FACTOR_SECURITY)
        val column = commentTable!!.columnModel.getColumn(6)
        column.cellEditor = DefaultCellEditor(factorComboBox)


        commentTable!!.model.addTableModelListener { e ->
            log.info("table changed...")
            val row = e.firstRow
            val identifier = commentTable!!.getValueAt(row, 0) as Long
            val reviewer = commentTable!!.getValueAt(row, 1) as String
            val comments = commentTable!!.getValueAt(row, 2) as String
            val author = commentTable!!.getValueAt(row, 3) as String
            val type = commentTable!!.getValueAt(row, 4) as String
            val severity = commentTable!!.getValueAt(row, 5) as String
            val factor = commentTable!!.getValueAt(row, 6) as String
            val model = ReviewCommentInfoModel()
            model.identifier = identifier
            model.reviewer = reviewer
            model.comments = comments
            model.author = author
            model.type = type
            model.severity = severity
            model.factor = factor
            ApplicationCache.updateProjectData(model)
        }
    }

    private fun bindTableListeners() {
        // 指定可编辑列颜色变更
        commentTable!!.setDefaultRenderer(Any::class.java, CommentTableCellRender())

        // 双击跳转到源码位置
        commentTable!!.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                super.mouseClicked(e)
            }
        })
    }

    private fun bindButtons() {
        copyButton!!.addActionListener { e: ActionEvent? ->
            val copyProject = this.project
            if (CollectionUtils.isEmpty(this.tableData)) {
                notifyWarning(copyProject, "Has no record to copy")
                return@addActionListener
            }
            try {
                val copyData = CommonUtil.copyToString(this.tableData)
                notifyInfo(copyProject, "Copy successfully!")
                CopyPasteManager.getInstance()
                    .setContents(TextTransferable(java.lang.String(copyData)))
            } catch (ex: Exception) {
                notifyWarning(copyProject, "Copy failed! Cause:" + System.lineSeparator() + ex.message)
            }
        }

        clearButton!!.addActionListener { e: ActionEvent? ->
            val cleanProject = this.project
            if (CollectionUtils.isEmpty(this.tableData)) {
                notifyWarning(cleanProject, "Has no record to clean")
                return@addActionListener
            }
            if (ClearConfirmDialog().showAndGet()) {
                ApplicationCache.cleanAllCache(cleanProject.name)
                DateRefreshMessagePublisher.getInstance(cleanProject)
                    .fireDateRefreshExecute("clean code record", cleanProject)
            }
        }

        syncConfluenceButton!!.addActionListener { e: ActionEvent? ->
            val syncProject = this.project
            if (CollectionUtils.isEmpty(this.tableData)) {
                notifyWarning(syncProject, "Has no record to sync")
                return@addActionListener
            }
            val codeAuditSetting = ApplicationCache.codeAuditSetting
            val valid = codeAuditSetting.isValid
            if (!valid) {
                notifyWarning(syncProject, "Please setting confluence info!")
                return@addActionListener
            }
            val projectName = syncProject.name
            try {
                val allDataList: List<ReviewCommentInfoModel?> = ApplicationCache.getAllDataList(projectName)
                val data = CommonUtil.buildConfluenceFormatString(allDataList)
                if (StringUtils.isBlank(data)) {
                    notifyWarning(syncProject, "There is no record need to sync!")
                    return@addActionListener
                }
                HttpRequestFactory.sendDataToConf(codeAuditSetting.url,
                    codeAuditSetting.userName,
                    codeAuditSetting.password,
                    CommonUtil.getFormattedTimeForTitle(projectName),
                    codeAuditSetting.spaceKey,
                    codeAuditSetting.parentId,
                    data,
                    { successMessage: String? ->
                        notifyInfo(syncProject, successMessage)
                    },
                    { failMessage: String? ->
                        notifyError(syncProject, failMessage)
                    })
            } catch (ex: Exception) {
                notifyError(syncProject, "Sync to confluence fail!")
                throw RuntimeException(ex)
            }
        }

        exportButton!!.addActionListener { e: ActionEvent? ->
            val exportProject = this.project
            if (CollectionUtils.isEmpty(this.tableData)) {
                notifyWarning(exportProject, "Has no record to export")
                return@addActionListener
            }
            val fileChooser = JFileChooser()
            fileChooser.selectedFile =
                File("[" + project.name + "]_code_review_report_" + CommonUtil.formattedTimeForFileName)
            fileChooser.fileFilter = FileNameExtensionFilter("Excel表格(*.xlsx)", ".xlsx")
            val saveDialog = fileChooser.showSaveDialog(fullPanel)
            if (saveDialog == JFileChooser.APPROVE_OPTION) {
                var path = fileChooser.selectedFile.path
                if (!path.lowercase(Locale.getDefault()).endsWith(".xlsx")) {
                    path += ".xlsx"
                }

                try {
                    ExcelOperateUtil.exportExcel(path, this.tableData)
                    notifyInfo(exportProject, "Export successfully!")
                } catch (ex: Exception) {
                    notifyError(exportProject, "Export failed! Cause:" + System.lineSeparator() + ex.message)
                }
            }
        }

        deleteButton!!.addActionListener { e: ActionEvent? ->
            val deleteButtonProject = this.project
            val selectedRows = commentTable!!.selectedRows
            if (selectedRows!!.size <= 0) {
                notifyWarning(deleteButtonProject, "Please select item first!")
                return@addActionListener
            }
            if (DeleteConfirmDialog().showAndGet()) {
                val deleteIndentifierList: MutableList<Long> = ArrayList()
                if (selectedRows != null && selectedRows.size > 0) {
                    for (rowId in selectedRows) {
                        val valueAt = commentTable!!.getValueAt(rowId, 0) as Long
                        deleteIndentifierList.add(valueAt)
                    }
                    ApplicationCache.deleteCacheList(deleteIndentifierList)
                }
                DateRefreshMessagePublisher.getInstance(deleteButtonProject)
                    .fireDateRefreshExecute("refresh data list", deleteButtonProject)
            }
        }
    }

    companion object {
        private val log = Logger.getInstance(
            ManageReviewCommentUI::class.java
        )

        private val COLUMN_NAMES = arrayOf<Any?>(
            "ID", "Reviewer", "Comments", "Author", "Type",
            "Severity", "TriggerFactor", "ProjectName", "File", "Line", "CodeFragment", "Time"
        )
    }
}
