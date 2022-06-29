package com.dj.tool.common;

import com.dj.tool.ui.ManageReviewCommentUI;
import com.intellij.openapi.project.Project;

public class ReviewManagerFactory {

    private static ManageReviewCommentUI instance;

    public synchronized static ManageReviewCommentUI getInstance(Project project) {
        if (instance == null) {
            instance = new ManageReviewCommentUI(project);
        }
        return instance;
    }

}
