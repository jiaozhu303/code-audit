package com.dj.tool.ui

import com.intellij.openapi.ui.DialogWrapper
import groovyjarjarantlr4.v4.runtime.misc.Nullable
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class DeleteConfirmDialog : DialogWrapper(true) {
    init {
        title = "Delete Confirm"
        init()
    }

    @Nullable
    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(BorderLayout(0, 0))

        val label = JLabel("Do you want Delete？", JLabel.CENTER)
        label.preferredSize = Dimension(200, 60)
        dialogPanel.add(label, BorderLayout.CENTER)
        return dialogPanel
    }
}
