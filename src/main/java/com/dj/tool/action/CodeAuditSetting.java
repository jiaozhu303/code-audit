package com.dj.tool.action;

import com.dj.tool.model.CodeAuditSettingModel;
import com.dj.tool.ui.CodeAuditSettingDialog;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class CodeAuditSetting extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        CodeAuditSettingModel model = getCodeAuditSettingModel();
        CodeAuditSettingDialog.showDialog(model);
    }

    @NotNull
    private CodeAuditSettingModel getCodeAuditSettingModel() {
        PropertiesComponent appComponent = PropertiesComponent.getInstance();
        String url = appComponent.getValue(CodeAuditSettingModel.URL_KEY);
        String user = appComponent.getValue(CodeAuditSettingModel.USER_KEY);
        String password = appComponent.getValue(CodeAuditSettingModel.PASSWORD_KEY);
        CodeAuditSettingModel model = new CodeAuditSettingModel(url, user, password);
        return model;
    }
}
