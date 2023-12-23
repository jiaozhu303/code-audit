package com.dj.tool.publisher;

import com.dj.tool.listener.DateRefreshListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;

public class DateRefreshMessagePublisher {

    public static final Topic<DateRefreshListener> TOPIC = new Topic<>("code audit - date refresh events", DateRefreshListener.class);

    public static DateRefreshMessagePublisher getInstance(Project project) {
        return ApplicationManager.getApplication().getService(DateRefreshMessagePublisher.class);
    }

    /**
     * 推送刷新事件
     */
    public void fireDateRefreshExecute(String date, Project project) {
        getPublisher(project).refresh(date, project);
    }

    @NotNull
    private static DateRefreshListener getPublisher(Project project) {
        return project.getMessageBus().syncPublisher(TOPIC);
    }
}

