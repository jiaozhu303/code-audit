package com.dj.tool.action

import com.dj.tool.common.CommonUtil
import com.dj.tool.common.Constants
import com.dj.tool.model.ReviewCommentInfoModel
import com.dj.tool.ui.AddReviewCommentUI
import com.intellij.codeInsight.daemon.OutsidersPsiFileSupport
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import org.jetbrains.annotations.NonNls

class AddComment : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        //获取当前类文件的路径
        val project = e.project
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val isOutsiderFile = OutsidersPsiFileSupport.isOutsiderFile(virtualFile)
        val fullPath: @NonNls String = virtualFile.path ?: return
        val basePath = project!!.basePath
        val fileName = fullPath.substring(basePath!!.length + 1, fullPath.length)
        val data = e.getData(CommonDataKeys.EDITOR) ?: return
        val selectionModel = data.selectionModel
        // 获取当前选择的内容
        val selectedText = selectionModel.selectedText
        if (selectedText == null || "" == selectedText) {
            return
        }

        val document = data.document
        val startLine = document.getLineNumber(selectionModel.selectionStart)
        val endLine = document.getLineNumber(selectionModel.selectionEnd)

        val model = ReviewCommentInfoModel()
        model.comments = ""
        model.startLine = startLine
        model.endLine = endLine
        model.content = selectedText
        //        model.setAuthor(GitOperationUtil.getAnnotateAuthor(project, virtualFile, startLine, isOutsiderFile));
        model.author = ""
        model.projectName = project.name
        model.filePath = fileName
        val currentTimeMillis = System.currentTimeMillis()
        model.identifier = currentTimeMillis
        model.dateTime = CommonUtil.time2String(currentTimeMillis)
        model.reviewer = Constants.REVIEWER
        model.type = Constants.TYPE_QUESTION
        model.severity = Constants.SEVERITY_GENERAL
        model.factor = Constants.FACTOR_BASIC

        AddReviewCommentUI.showDialog(model, project)
    }
}
