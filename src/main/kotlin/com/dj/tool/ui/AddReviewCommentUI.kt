package com.dj.tool.ui

import com.dj.tool.common.ApplicationCache
import com.dj.tool.model.ReviewCommentInfoModel
import com.dj.tool.publisher.DateRefreshMessagePublisher
import com.intellij.openapi.project.Project
import java.awt.Toolkit
import java.awt.event.ActionEvent
import javax.swing.*


class AddReviewCommentUI {
    private var reviewerTextField: JTextField? = null
    private var commentsTextArea: JTextArea? = null
    private var questionTypeComboBox: JComboBox<*>? = null
    private var severityComboBox: JComboBox<*>? = null
    private var triggerFactorComboBox: JComboBox<*>? = null
    private var saveButton: JButton? = null
    private var cancelButton: JButton? = null
    private var addReviewCommentPanel: JPanel? = null
    private var filePathTextField: JTextField? = null
    private var codeContentsTextArea: JTextArea? = null
    private var lineTextField: JTextField? = null
    private var authorTextField: JTextField? = null

    companion object {
        private const val WIDTH = 600
        private const val HEIGHT = 600

        fun showDialog(model: ReviewCommentInfoModel, project: Project?) {
            val dialog = JDialog(JFrame())
            dialog.title = "Add Code Audit Record"
            val addComment = AddReviewCommentUI()
            addComment.reviewerTextField!!.text = model.reviewer
            addComment.commentsTextArea!!.text = model.comments
            addComment.codeContentsTextArea!!.text = model.content
            addComment.filePathTextField!!.text = model.filePath
            addComment.lineTextField!!.text = model.getLineRange()
            addComment.authorTextField!!.text = model.author
            addComment.questionTypeComboBox!!.selectedItem = model.type
            addComment.severityComboBox!!.selectedItem = model.severity
            addComment.triggerFactorComboBox!!.selectedItem = model.factor
            addComment.saveButton!!.addActionListener { e: ActionEvent? ->
                model.content = addComment.codeContentsTextArea!!.text
                model.comments = addComment.commentsTextArea!!.text
                model.reviewer = addComment.reviewerTextField!!.text
                model.author = addComment.authorTextField!!.text
                model.type = addComment.questionTypeComboBox!!.selectedItem.toString()
                model.severity = addComment.severityComboBox!!.selectedItem.toString()
                model.factor = addComment.triggerFactorComboBox!!.selectedItem.toString()
                ApplicationCache.addOneToCache(model)
                DateRefreshMessagePublisher.getInstance(project).fireDateRefreshExecute("add code record", project)
                dialog.dispose()
            }

            addComment.cancelButton!!.addActionListener { e: ActionEvent? ->
                dialog.dispose()
            }

            val screenSize = Toolkit.getDefaultToolkit().screenSize
            val w = (screenSize.width - WIDTH) / 2
            val h = (screenSize.height * 95 / 100 - HEIGHT) / 2
            dialog.setLocation(w, h)

            dialog.contentPane = addComment.addReviewCommentPanel
            dialog.defaultCloseOperation = JDialog.DISPOSE_ON_CLOSE
            dialog.pack()
            dialog.isVisible = true
        }
    }
}
