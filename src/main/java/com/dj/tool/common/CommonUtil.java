package com.dj.tool.common;

import com.dj.tool.ui.ManageReviewCommentUI;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

import java.io.Closeable;
import java.io.IOException;


public class CommonUtil {

    private static final Logger log = Logger.getInstance(CommonUtil.class);

    public static void closeQuitely(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void reloadCommentListShow(Project project) {
        try {
            InnerProjectCache projectCache = ProjectInstanceManager.getInstance().getProjectCache(project.getLocationHash());

            ManageReviewCommentUI manageReviewCommentUI = projectCache.getManageReviewCommentUI();
            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("CodeRecord");

            if (manageReviewCommentUI != null && toolWindow != null) {
                manageReviewCommentUI.refreshTableDataShow();
            } else {
                log.info("manageReviewCommentUI = " + manageReviewCommentUI);
                log.info("toolWindow = " + toolWindow);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
