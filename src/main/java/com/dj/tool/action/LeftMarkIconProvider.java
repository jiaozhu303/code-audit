package com.dj.tool.action;

import com.dj.tool.common.ApplicationCache;
import com.dj.tool.model.ReviewCommentInfoModel;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import icons.MyIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;


public class LeftMarkIconProvider extends RelatedItemLineMarkerProvider {
    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {

        if (!(element instanceof PsiWhiteSpace)) {
            super.collectNavigationMarkers(element, result);
            return;
        }

        int textOffset = element.getTextOffset();
        int textLength = element.getTextLength();
        int textEndOffset = textOffset + textLength;

        if (textOffset < 0) {
            super.collectNavigationMarkers(element, result);
            return;
        }


        PsiFile containingFile = element.getContainingFile();
        Project project = element.getProject();
        Document document = PsiDocumentManager.getInstance(project).getDocument(containingFile);
        if (document != null) {
            int startLineNumber = document.getLineNumber(textOffset);
            int endLineNumber = document.getLineNumber(textEndOffset);

            // 同一行内的空格重复匹配，不处理，直接忽略
            if (startLineNumber == endLineNumber) {
                super.collectNavigationMarkers(element, result);
                return;
            }

            // currentLine统一用endLine来处理，标准化所有处理场景，避免换行的场景，上下都被匹配上了
            int currentLine = endLineNumber - 1;

            List<ReviewCommentInfoModel> projectAllData = ApplicationCache.getProjectAllData(project.getName());
            if (projectAllData != null) {
                String path = element.getContainingFile().getVirtualFile().getName();

                String comment = getCommentInfo(path, currentLine, projectAllData);
                if (comment != null) {
                    NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(MyIcons.EditorQuestion);
                    builder.setTarget(element);
                    builder.setTooltipText(comment);
                    result.add(builder.createLineMarkerInfo(element));
                    return;
                }
            }
        }

        super.collectNavigationMarkers(element, result);
    }

    public String getCommentInfo(String filePath, int currentLine, List<ReviewCommentInfoModel> dataList) {
        String result = null;
        for (ReviewCommentInfoModel entry : dataList) {
            if (entry.getFilePath().contains(filePath) && (currentLine >= entry.getStartLine() && currentLine <= entry.getEndLine())) {
                result = entry.getComments();
                break;
            }
        }
        return result;
    }
}
