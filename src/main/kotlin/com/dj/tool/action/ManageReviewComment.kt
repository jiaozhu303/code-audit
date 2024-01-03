package com.dj.tool.action

import com.dj.tool.listener.DateRefreshNotifyListener
import com.dj.tool.publisher.DateRefreshMessagePublisher
import com.dj.tool.ui.ManageReviewCommentUI
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import icons.MyIcons

class ManageReviewComment : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val manageReviewCommentUI = ManageReviewCommentUI(project)
        manageReviewCommentUI.initUI()
        project.messageBus
            .connect()
            .subscribe(DateRefreshMessagePublisher.TOPIC, DateRefreshNotifyListener(manageReviewCommentUI))

        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(manageReviewCommentUI.fullPanel, null, false)
        toolWindow.contentManager.addContent(content)
        toolWindow.setIcon(MyIcons.ToolWindow)
    }
}
