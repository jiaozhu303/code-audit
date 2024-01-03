package com.dj.tool.ui

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object CodeAuditNotifier {
    @JvmStatic
    fun notifyError(project: Project?, content: String?) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Code Audit Notification")
            .createNotification(content!!, NotificationType.ERROR)
            .notify(project)
    }

    @JvmStatic
    fun notifyWarning(project: Project?, content: String?) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Code Audit Notification")
            .createNotification(content!!, NotificationType.WARNING)
            .notify(project)
    }

    @JvmStatic
    fun notifyInfo(project: Project?, content: String?) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Code Audit Notification")
            .createNotification(content!!, NotificationType.INFORMATION)
            .notify(project)
    }
}
