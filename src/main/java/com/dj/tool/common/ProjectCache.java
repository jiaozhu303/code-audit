//package com.dj.tool.common;
//
//import com.dj.tool.service.CodeAuditSettingProjectService;
//import com.intellij.openapi.project.Project;
//
//
//public class ProjectCache {
//    private static CodeAuditSettingProjectService instance;
//
//
//    public synchronized static CodeAuditSettingProjectService getInstance(Project project) {
//        if (instance == null) {
//            instance = CodeAuditSettingProjectService.getInstance(project);
//        }
//        return instance;
//    }
//
//}
