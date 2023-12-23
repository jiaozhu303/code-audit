package com.dj.tool.listener;

import com.intellij.openapi.project.Project;

public interface DateRefreshListener {
    void refresh(String date, Project fireProject);
}
