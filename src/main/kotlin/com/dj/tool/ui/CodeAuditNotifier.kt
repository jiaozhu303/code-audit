package com.dj.tool.ui;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class CodeAuditNotifier {
    public static void notifyError(Project project, String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Code Audit Notification")
                .createNotification(content, NotificationType.ERROR)
                .notify(project);
    }

    public static void notifyWarning(Project project, String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Code Audit Notification")
                .createNotification(content, NotificationType.WARNING)
                .notify(project);
    }

    public static void notifyInfo(Project project, String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Code Audit Notification")
                .createNotification(content, NotificationType.INFORMATION)
                .notify(project);
    }
}
