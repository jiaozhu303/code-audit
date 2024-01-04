package com.dj.tool.common

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.VcsException
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.vcsUtil.VcsUtil
import java.util.*

object GitOperationUtil {
    fun getAnnotateAuthor(project: Project, file: VirtualFile, currentLineNumber: Int, outsiderFile: Boolean): String {
        try {
            if (outsiderFile) {
                val fileByPath = LocalFileSystem.getInstance().findFileByPath(getFilePath(file.userDataString))
                return getAnnotateAuthorByProvider(project, fileByPath, currentLineNumber)
            }
            return getAnnotateAuthorByProvider(project, file, currentLineNumber)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @Throws(VcsException::class)
    private fun getAnnotateAuthorByProvider(
        project: Project,
        virtualFile: VirtualFile?,
        currentLineNumber: Int
    ): String {
        val abstractVcs = VcsUtil.getVcsFor(project, virtualFile!!) ?: return ""

        val annotationProvider = abstractVcs.annotationProvider ?: return ""

        try {
            val annotate = annotationProvider.annotate(virtualFile)
            val aspect = annotate.aspects[2]
            return aspect.getValue(currentLineNumber)
        } catch (e: Exception) {
        }
        return ""
    }

    fun getFilePath(userData: String): String {
        var userData = userData
        userData = userData.substring(1, userData.length - 1)
        val first = Arrays.stream(userData.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            .filter { item: String ->
                val strings = item.split("->".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                strings[0].trim { it <= ' ' } == "OutsidersPsiFileSupport.FilePath"
            }.findFirst()
        return first.map { s: String ->
            s.split("->".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1].trim { it <= ' ' }
        }.orElse("")
    }
}
