package com.dj.tool.provider;

import com.dj.tool.action.LeftMarkIconProvider;
import com.dj.tool.publisher.DateRefreshMessagePublisher;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.project.Project;

public class MarkIconProviderFactory {

    public static LeftMarkIconProvider getInstance(Project project) {
//        MyApplicationService applicationService = ServiceManager.getService(MyApplicationService.class);
//        MyProjectService projectService = ServiceManager.getService(project, MyProjectService.class);
//        MyModuleService moduleService = ModuleServiceManager.getService(module, MyModuleService.class);
//        return ApplicationManager.getApplication().getService(DateRefreshMessagePublisher.class);
//        return project.getActualComponentManager().getService(DateRefreshMessagePublisher.class);

        LeftMarkIconProvider provider = ServiceManager.getService(project, LeftMarkIconProvider.class);
        return provider;
    }

}