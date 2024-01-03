package com.dj.tool.common;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.annotate.AnnotationProvider;
import com.intellij.openapi.vcs.annotate.FileAnnotation;
import com.intellij.openapi.vcs.annotate.LineAnnotationAspect;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;

import java.util.Arrays;
import java.util.Optional;

public class GitOperationUtil {

    public static String getAnnotateAuthor(Project project, VirtualFile file, int currentLineNumber, boolean outsiderFile) {
        try {
            if (outsiderFile) {
                VirtualFile fileByPath = LocalFileSystem.getInstance().findFileByPath(getFilePath(file.getUserDataString()));
                return getAnnotateAuthorByProvider(project, fileByPath, currentLineNumber);
            }
            return getAnnotateAuthorByProvider(project, file, currentLineNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getAnnotateAuthorByProvider(Project project, VirtualFile virtualFile, int currentLineNumber) throws VcsException {

        AbstractVcs abstractVcs = VcsUtil.getVcsFor(project, virtualFile);
        if (abstractVcs == null) {
            return "";
        }

        AnnotationProvider annotationProvider = abstractVcs.getAnnotationProvider();
        if (annotationProvider == null) {
            return "";
        }

        try {
            FileAnnotation annotate = annotationProvider.annotate(virtualFile);
            LineAnnotationAspect aspect = annotate.getAspects()[2];
            return aspect.getValue(currentLineNumber);

        } catch (Exception e) {

        }
        return "";

    }

    public static String getFilePath(String userData) {
        userData = userData.substring(1, userData.length() - 1);
        Optional<String> first = Arrays.stream(userData.split(",")).filter((String item) -> {
            String[] strings = item.split("->");
            return strings[0].trim().equals("OutsidersPsiFileSupport.FilePath");
        }).findFirst();
        return first.map(s -> s.split("->")[1].trim()).orElse("");
    }
}
