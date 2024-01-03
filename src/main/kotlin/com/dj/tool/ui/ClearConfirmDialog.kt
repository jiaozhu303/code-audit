package com.dj.tool.ui;

import com.intellij.openapi.ui.DialogWrapper;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;

import javax.swing.*;
import java.awt.*;

public class ClearConfirmDialog extends DialogWrapper {


    public ClearConfirmDialog() {
        super(true); // use current window as parent
        setTitle("Clear Confirm");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout(0, 0));
        JLabel label = new JLabel("Do you want clear all project record ？");
        label.setPreferredSize(new Dimension(200, 60));
        dialogPanel.add(label, BorderLayout.CENTER);
        return dialogPanel;
    }
}
