package com.dj.tool.action

import com.dj.tool.common.ApplicationCache
import com.dj.tool.ui.CodeAuditSettingDialog
import com.dj.tool.ui.CodeAuditSettingDialogVersion2
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class CodeAuditSetting : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val model = ApplicationCache.codeAuditSetting
        CodeAuditSettingDialog.showDialog(model)
//        if (CodeAuditSettingDialogVersion2(e.getData(CommonDataKeys.PROJECT), model).showAndGet()) {
//        }
    }
}
