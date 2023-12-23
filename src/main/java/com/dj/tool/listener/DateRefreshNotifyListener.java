package com.dj.tool.listener;

import com.dj.tool.ui.ManageReviewCommentUI;
import com.intellij.openapi.project.Project;

public class DateRefreshNotifyListener implements DateRefreshListener {

    private ManageReviewCommentUI ui;

    public DateRefreshNotifyListener() {
        super();
    }

    public DateRefreshNotifyListener(ManageReviewCommentUI ui) {
        super();
        this.ui = ui;
    }

    @Override
    public void refresh(String date, Project fireProject) {
        this.ui.reloadTableData();
    }

}

