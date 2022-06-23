package com.dj.tool.action;

import com.dj.tool.common.InnerProjectCache;
import com.dj.tool.common.ProjectInstanceManager;
import com.dj.tool.ui.ManageReviewCommentUI;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import icons.MyIcons;
import org.jetbrains.annotations.NotNull;


public class ManageReviewComment implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 打开不同的window窗口的时候，会进来一次
        // 由于不同窗口，插件是同一个进程，因此UI示例必须要分开
        String locationHash = project.getLocationHash();


        InnerProjectCache projectCache = ProjectInstanceManager.getInstance().getProjectCache(locationHash);
        if (projectCache == null) {
            projectCache = new InnerProjectCache(project);
            ProjectInstanceManager.getInstance().addProjectCache(project.getLocationHash(), projectCache);
        }
        ManageReviewCommentUI manageReviewCommentUI = projectCache.getManageReviewCommentUI();
        if (manageReviewCommentUI == null) {
            manageReviewCommentUI = new ManageReviewCommentUI(project);
            projectCache.setManageReviewCommentUI(manageReviewCommentUI);
        }
        manageReviewCommentUI.initUI();

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(manageReviewCommentUI.fullPanel, "", false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.setIcon(MyIcons.ToolWindow);

    }
}
