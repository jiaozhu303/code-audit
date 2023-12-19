package com.dj.tool.common;

import com.dj.tool.ui.ManageReviewCommentUI;
import com.intellij.openapi.project.Project;
import org.apache.commons.collections.MapUtils;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ReviewManagerFactory {

    private static ConcurrentHashMap<String, ManageReviewCommentUI> instanceMap;

    public synchronized static ManageReviewCommentUI getInstance(Project project) {
        if (instanceMap == null) {
            instanceMap = new ConcurrentHashMap();
        }
        ManageReviewCommentUI manageReviewCommentUI = instanceMap.get(project.getName());
        if (Objects.isNull(manageReviewCommentUI)) {
//        ManageReviewCommentUI manageReviewCommentUI = new ManageReviewCommentUI(project);
            updateInstanceMap(manageReviewCommentUI, project.getName());
        }
        return manageReviewCommentUI;
    }

    public static void reloadAllProjectData() {
        if (MapUtils.isEmpty(instanceMap)) {
            return;
        }
        instanceMap
            .values()
            .stream()
            .forEach(item -> item.reloadTableData());
    }

    private static void updateInstanceMap(ManageReviewCommentUI manageReviewCommentUI, String projectName) {
        instanceMap.put(projectName, manageReviewCommentUI);
    }


}
