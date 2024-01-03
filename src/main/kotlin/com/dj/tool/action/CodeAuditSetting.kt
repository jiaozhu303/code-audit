package com.dj.tool.action;

import com.dj.tool.common.ApplicationCache;
import com.dj.tool.model.CodeAuditSettingModel;
import com.dj.tool.ui.CodeAuditSettingDialog;
import com.dj.tool.ui.CodeAuditSettingDialogVersion2;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class CodeAuditSetting extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        CodeAuditSettingModel model = ApplicationCache.getCodeAuditSetting();
        CodeAuditSettingDialog.showDialog(model);
//        if(new CodeAuditSettingDialogVersion2(e.getProject(), model).showAndGet()) {
            // user pressed OK
//        }
    }
}
