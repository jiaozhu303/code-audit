package com.dj.tool.ui

import com.dj.tool.common.ApplicationCache
import com.dj.tool.model.CodeAuditSettingModel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import java.awt.Toolkit
import java.awt.event.KeyEvent
import javax.swing.*

class CodeAuditSettingDialogVersion2(project: Project?, private val model: CodeAuditSettingModel) :
    DialogWrapper(project, true) {

    private var contentPane: JPanel? = null
    private var buttonOK: JButton? = null
    private var buttonCancel: JButton? = null
    private var urlTextField: JTextField? = null
    private var userNameTextField: JTextField? = null
    private var passwordTextField: JTextField? = null
    private var spaceKeyTextField: JTextField? = null
    private var parentIdTextField: JTextField? = null

    init {
        title = "Code Audit Setting"
        urlTextField!!.text = model!!.url
        userNameTextField!!.text = model!!.userName
        passwordTextField!!.text = model!!.password
        spaceKeyTextField!!.text = model!!.spaceKey
        parentIdTextField!!.text = model!!.parentId
        init()
        buttonOK!!.addActionListener { onOK() }
        buttonCancel!!.addActionListener { onCancel() }
        // call onCancel() on ESCAPE
        contentPane!!.registerKeyboardAction(
            { onCancel() },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        )
    }


    override fun createCenterPanel(): JComponent? {

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val w = (screenSize.width - 600) / 2
        val h = (screenSize.height * 95 / 100 - 600) / 2
        setLocation(w, h)
        pack()


        val dialogPanel = JPanel(BorderLayout(600, 600))

//        label.preferredSize = Dimension(600, 200)
//        dialog.add(label, BorderLayout.CENTER)
        //        dialogPanel.add(this.urlTextField);
//        dialogPanel.add(this.userNameTextField);
//        dialogPanel.add(this.spaceKeyTextField);
        dialogPanel.add(dialogPanel);
        return dialogPanel
    }

    private fun onOK() {
        ApplicationCache.saveCodeAuditSetting(
            urlTextField!!.text,
            userNameTextField!!.text,
            passwordTextField!!.text,
            spaceKeyTextField!!.text,
            parentIdTextField!!.text
        )
        dispose()
    }

    private fun onCancel() {
        // add your code here if necessary
        dispose()
    }
}
