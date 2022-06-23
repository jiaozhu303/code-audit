package com.dj.tool.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ProjectInstanceManager {
    private static ProjectInstanceManager instance;

    private final Map<String, InnerProjectCache> projectCacheMap;

    private ProjectInstanceManager() {
        projectCacheMap = new ConcurrentHashMap<>();
    }

    public synchronized static ProjectInstanceManager getInstance() {
        if (instance == null) {
            instance = new ProjectInstanceManager();
        }
        return instance;
    }

    public InnerProjectCache getProjectCache(String projectHash) {
        return projectCacheMap.get(projectHash);
    }

    public void addProjectCache(String projectHash, InnerProjectCache cache) {
        this.projectCacheMap.put(projectHash, cache);
    }
}
