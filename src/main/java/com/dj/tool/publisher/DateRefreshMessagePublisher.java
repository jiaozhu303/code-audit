package com.dj.tool.publisher;

import com.dj.tool.listener.DateRefreshListener;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;

public class DateRefreshMessagePublisher {

    public static final Topic<DateRefreshListener> TOPIC = new Topic<>("code audit - date refresh events", DateRefreshListener.class);

    public static DateRefreshMessagePublisher getInstance(Project project) {
//        MyApplicationService applicationService = ServiceManager.getService(MyApplicationService.class);
//        MyProjectService projectService = ServiceManager.getService(project, MyProjectService.class);
//        MyModuleService moduleService = ModuleServiceManager.getService(module, MyModuleService.class);
//        return ApplicationManager.getApplication().getService(DateRefreshMessagePublisher.class);
        return ServiceManager.getService(project, DateRefreshMessagePublisher.class);
//        return project.getActualComponentManager().getService(DateRefreshMessagePublisher.class);
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

