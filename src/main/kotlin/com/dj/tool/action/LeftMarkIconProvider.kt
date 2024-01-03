package com.dj.tool.action

import com.dj.tool.common.ApplicationCache
import com.dj.tool.model.ReviewCommentInfoModel
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import icons.MyIcons

class LeftMarkIconProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>?>
    ) {
        if (element !is PsiWhiteSpace) {
            super.collectNavigationMarkers(element, result)
            return
        }

        val textOffset = element.getTextOffset()
        val textLength = element.getTextLength()
        val textEndOffset = textOffset + textLength

        if (textOffset < 0) {
            super.collectNavigationMarkers(element, result)
            return
        }


        val containingFile = element.getContainingFile()
        val project = element.getProject()
        val document = PsiDocumentManager.getInstance(project).getDocument(containingFile)
        if (document != null) {
            val startLineNumber = document.getLineNumber(textOffset)
            val endLineNumber = document.getLineNumber(textEndOffset)

            // 同一行内的空格重复匹配，不处理，直接忽略
            if (startLineNumber == endLineNumber) {
                super.collectNavigationMarkers(element, result)
                return
            }

            // currentLine统一用endLine来处理，标准化所有处理场景，避免换行的场景，上下都被匹配上了
            val currentLine = endLineNumber - 1

            val projectAllData = ApplicationCache.getProjectAllData(project.name)
            if (projectAllData != null) {
                val path = element.getContainingFile().virtualFile.name

                val comment = getCommentInfo(path, currentLine, projectAllData)
                if (comment != null) {
                    val builder = NavigationGutterIconBuilder.create(MyIcons.EditorQuestion)
                    builder.setTarget(element)
                    builder.setTooltipText(comment)
                    result.add(builder.createLineMarkerInfo(element))
                    return
                }
            }
        }

        super.collectNavigationMarkers(element, result)
    }

    fun getCommentInfo(filePath: String?, currentLine: Int, dataList: List<ReviewCommentInfoModel?>): String? {
        var result: String? = null
        for (entry in dataList) {
            if (entry!!.filePath!!.contains(filePath!!) && (currentLine >= entry.startLine && currentLine <= entry.endLine)) {
                result = entry.comments
                break
            }
        }
        return result
    }
}
