package com.dj.tool.action;

import com.dj.tool.common.ApplicationCache;
import com.dj.tool.model.CodeAuditSettingModel;
import com.dj.tool.ui.CodeAuditSettingDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class CodeAuditSetting extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        CodeAuditSettingModel model = ApplicationCache.getCodeAuditSetting();
        CodeAuditSettingDialog.showDialog(model);
    }
}
