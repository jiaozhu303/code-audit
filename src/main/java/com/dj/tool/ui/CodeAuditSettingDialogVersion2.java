package com.dj.tool.ui;

import com.dj.tool.model.CodeAuditSettingModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class CodeAuditSettingDialogVersion2 extends DialogWrapper {

    private CodeAuditSettingModel model;




    public CodeAuditSettingDialogVersion2(@Nullable Project project, CodeAuditSettingModel model) {
        super(project, true);
        this.model = model;
        setTitle("Code Audit Setting");
        init();
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {

//        this.urlTextField.setText(this.model.getUrl());
//        this.userNameTextField.setText(this.model.getUserName());
//        this.passwordTextField.setText(this.model.getPassword());
//        this.spaceKeyTextField.setText(this.model.getSpaceKey());
//        this.parentIdTextField.setText(this.model.getParentId());

        JPanel dialogPanel = new JPanel(new BorderLayout(600, 200));

        JLabel label = new JLabel("testing");
        label.setPreferredSize(new Dimension(600, 200));
        dialogPanel.add(label, BorderLayout.CENTER);
//        dialogPanel.add(this.urlTextField);
//        dialogPanel.add(this.userNameTextField);
//        dialogPanel.add(this.spaceKeyTextField);
//        dialogPanel.add(this.passwordTextField);
        return dialogPanel;
    }
}
