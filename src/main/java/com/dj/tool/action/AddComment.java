package com.dj.tool.action;

import com.dj.tool.common.CommonUtil;
import com.dj.tool.model.ReviewCommentInfoModel;
import com.dj.tool.ui.AddReviewCommentUI;
import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import com.intellij.codeInsight.daemon.OutsidersPsiFileSupport;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;

import static com.dj.tool.common.Constants.FACTOR_BASIC;
import static com.dj.tool.common.Constants.REVIEWER;
import static com.dj.tool.common.Constants.SEVERITY_GENERAL;
import static com.dj.tool.common.Constants.TYPE_QUESTION;


public class AddComment extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //获取当前类文件的路径
        Project project = e.getProject();
        VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virtualFile == null) {
            return;
        }
        boolean isOutsiderFile = OutsidersPsiFileSupport.isOutsiderFile(virtualFile);
        String fullPath = virtualFile.getPath();
        if (fullPath == null) {
            return;
        }
        String basePath = project.getBasePath();
        String fileName = fullPath.substring(basePath.length()+1, fullPath.length());
        Editor data = e.getData(CommonDataKeys.EDITOR);
        if (data == null) {
            return;
        }
        SelectionModel selectionModel = data.getSelectionModel();
        // 获取当前选择的内容
        String selectedText = selectionModel.getSelectedText();
        if (selectedText == null || "".equals(selectedText)) {
            return;
        }

        Document document = data.getDocument();
        int startLine = document.getLineNumber(selectionModel.getSelectionStart());
        int endLine = document.getLineNumber(selectionModel.getSelectionEnd());

        ReviewCommentInfoModel model = new ReviewCommentInfoModel();
        model.setComments("");
        model.setStartLine(startLine);
        model.setEndLine(endLine);
        model.setContent(selectedText);
//        model.setAuthor(GitOperationUtil.getAnnotateAuthor(project, virtualFile, startLine, isOutsiderFile));
        model.setAuthor("");
        model.setProjectName(project.getName());
        model.setFilePath(fileName);
        long currentTimeMillis = System.currentTimeMillis();
        model.setIdentifier(currentTimeMillis);
        model.setDateTime(CommonUtil.time2String(currentTimeMillis));
        model.setReviewer(REVIEWER);
        model.setType(TYPE_QUESTION);
        model.setSeverity(SEVERITY_GENERAL);
        model.setFactor(FACTOR_BASIC);

        AddReviewCommentUI.showDialog(model, project);
    }

}
