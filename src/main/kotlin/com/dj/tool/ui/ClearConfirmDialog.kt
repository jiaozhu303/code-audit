package com.dj.tool.ui

import com.intellij.openapi.ui.DialogWrapper
import groovyjarjarantlr4.v4.runtime.misc.Nullable
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class ClearConfirmDialog : DialogWrapper(true) {
    init {
        title = "Clear Confirm"
        init()
    }

    @Nullable
    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(BorderLayout())
        val label = JLabel("Do you want clear this project records ？", JLabel.LEFT)
        label.preferredSize = Dimension(200, 50)
        dialogPanel.add(label, BorderLayout.CENTER)
        return dialogPanel
    }
}
