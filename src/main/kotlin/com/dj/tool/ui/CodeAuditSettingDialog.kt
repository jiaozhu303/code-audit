package com.dj.tool.ui

import com.dj.tool.common.ApplicationCache
import com.dj.tool.model.CodeAuditSettingModel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener
import java.awt.BorderLayout
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.KeyStroke

class CodeAuditSettingDialog(project: Project?, private val model: CodeAuditSettingModel) :
    DialogWrapper(project, true) {

    private var contentPane: JPanel? = null
    private var urlTextField: JTextField? = null
    private var userNameTextField: JTextField? = null
    private var passwordTextField: JTextField? = null
    private var spaceKeyTextField: JTextField? = null
    private var parentIdTextField: JTextField? = null

    init {
        init()
        title = "Code Audit Setting"
        urlTextField!!.text = model.url
        userNameTextField!!.text = model.userName
        passwordTextField!!.text = model.password
        spaceKeyTextField!!.text = model.spaceKey
        parentIdTextField!!.text = model.parentId

        // call onCancel() when cross is clicked
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                onCancel()
            }
        })

        // call onCancel() on ESCAPE
        contentPane!!.registerKeyboardAction(
            { onCancel() },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        )
        setSize(600, 300)
    }

    private fun onCancel() {
        // add your code here if necessary
        dispose()
    }

    override fun createCenterPanel(): JComponent? {
        var dialogPanel = JPanel(BorderLayout())
        dialogPanel.add(contentPane, BorderLayout.CENTER)
        return dialogPanel;
    }

    fun save() {
        ApplicationCache.saveCodeAuditSetting(
            urlTextField!!.text,
            userNameTextField!!.text,
            passwordTextField!!.text,
            spaceKeyTextField!!.text,
            parentIdTextField!!.text
        )
        dispose()
    }

}
