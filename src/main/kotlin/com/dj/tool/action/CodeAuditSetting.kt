package com.dj.tool.action

import com.dj.tool.common.ApplicationCache
import com.dj.tool.ui.CodeAuditSettingDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class CodeAuditSetting : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val model = ApplicationCache.codeAuditSetting
//        CodeAuditSettingDialog.showDialog(model)
        val codeAuditSettingDialog = CodeAuditSettingDialog(e.getData(CommonDataKeys.PROJECT), model)
        if (codeAuditSettingDialog.showAndGet()) {
            codeAuditSettingDialog.save()
        }
    }
}
