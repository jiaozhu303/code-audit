package com.dj.tool.action;

import com.dj.tool.listener.DateRefreshNotifyListener;
import com.dj.tool.publisher.DateRefreshMessagePublisher;
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

        ManageReviewCommentUI manageReviewCommentUI = new ManageReviewCommentUI(project);
        manageReviewCommentUI.initUI();
        project.getMessageBus()
                .connect()
                .subscribe(DateRefreshMessagePublisher.TOPIC, new DateRefreshNotifyListener(manageReviewCommentUI));

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(manageReviewCommentUI.fullPanel, null, false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.setIcon(MyIcons.ToolWindow);

    }
}
