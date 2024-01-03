package com.dj.tool.ui

import com.dj.tool.common.ApplicationCache
import com.dj.tool.model.ReviewCommentInfoModel
import com.dj.tool.publisher.DateRefreshMessagePublisher
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import javax.swing.*

class AddReviewCommentDialog(project: Project?, model: ReviewCommentInfoModel) :
    DialogWrapper(project, true) {
    private var project: Project? = project
        get() = field
        set(value) {
            field = value
        }
    private var model: ReviewCommentInfoModel = model
        get() = field
        set(value) {
            field = value
        }
    private var reviewerTextField: JTextField? = null
    private var commentsTextArea: JTextArea? = null
    private var questionTypeComboBox: JComboBox<*>? = null
    private var severityComboBox: JComboBox<*>? = null
    private var triggerFactorComboBox: JComboBox<*>? = null
    private var addReviewCommentPanel: JPanel? = null
    private var filePathTextField: JTextField? = null
    private var codeContentsTextArea: JTextArea? = null
    private var lineTextField: JTextField? = null
    private var authorTextField: JTextField? = null

    init {
        init()
        title = "Add Code Audit Record"
        reviewerTextField!!.text = model.reviewer
        commentsTextArea!!.text = model.comments
        codeContentsTextArea!!.text = model.content
        filePathTextField!!.text = model.filePath
        lineTextField!!.text = model.getLineRange()
        authorTextField!!.text = model.author
        questionTypeComboBox!!.selectedItem = model.type
        severityComboBox!!.selectedItem = model.severity
        triggerFactorComboBox!!.selectedItem = model.factor

        setSize(600, 800)
    }


    override fun createCenterPanel(): JComponent? {
        var dialogPanel = JPanel(BorderLayout())
        dialogPanel.add(addReviewCommentPanel, BorderLayout.CENTER)
        return dialogPanel;
    }

    fun save() {
        model.content = codeContentsTextArea!!.text
        model.comments = commentsTextArea!!.text
        model.reviewer = reviewerTextField!!.text
        model.author = authorTextField!!.text
        model.type = questionTypeComboBox!!.selectedItem.toString()
        model.severity = severityComboBox!!.selectedItem.toString()
        model.factor = triggerFactorComboBox!!.selectedItem.toString()
        ApplicationCache.addOneToCache(model)
        DateRefreshMessagePublisher.getInstance(project).fireDateRefreshExecute("add code record", project)
        dispose()
    }

}
