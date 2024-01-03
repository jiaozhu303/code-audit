package com.dj.tool.ui

import com.dj.tool.common.ApplicationCache
import com.dj.tool.model.CodeAuditSettingModel
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*

class CodeAuditSettingDialog : JDialog() {
    private var contentPane: JPanel? = null
    private var buttonOK: JButton? = null
    private var buttonCancel: JButton? = null
    private var urlTextField: JTextField? = null
    private var userNameTextField: JTextField? = null
    private var passwordTextField: JTextField? = null
    private var spaceKeyTextField: JTextField? = null
    private var parentIdTextField: JTextField? = null


    init {
        isModal = true
        setContentPane(contentPane)
        title = "Code Audit Setting"
        minimumSize = Dimension(600, 200)
        getRootPane().defaultButton = buttonOK

        buttonOK!!.addActionListener { onOK() }

        buttonCancel!!.addActionListener { onCancel() }

        // call onCancel() when cross is clicked
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
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

    companion object {
        private const val WIDTH = 600
        private const val HEIGHT = 600

        fun showDialog(model: CodeAuditSettingModel) {
            val dialog = CodeAuditSettingDialog()
            dialog.urlTextField!!.text = model.url
            dialog.userNameTextField!!.text = model.userName
            dialog.passwordTextField!!.text = model.password
            dialog.spaceKeyTextField!!.text = model.spaceKey
            dialog.parentIdTextField!!.text = model.parentId

            val screenSize = Toolkit.getDefaultToolkit().screenSize
            val w = (screenSize.width - WIDTH) / 2
            val h = (screenSize.height * 95 / 100 - HEIGHT) / 2
            dialog.setLocation(w, h)
            dialog.pack()
            dialog.isVisible = true
        }
    }
}
