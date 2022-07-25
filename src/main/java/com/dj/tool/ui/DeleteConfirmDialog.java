package com.dj.tool.ui;

import com.intellij.openapi.ui.DialogWrapper;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;

import javax.swing.*;
import java.awt.*;


public class DeleteConfirmDialog extends DialogWrapper {


    public DeleteConfirmDialog() {
        super(true); // use current window as parent
        setTitle("Delete Confirm");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout(0, 0));

        JLabel label = new JLabel("Do you want Delete？");
        label.setPreferredSize(new Dimension(200, 60));
        dialogPanel.add(label, BorderLayout.CENTER);
        return dialogPanel;
    }
}
