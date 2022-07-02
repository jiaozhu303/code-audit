package com.dj.tool.common;

import com.dj.tool.model.CodeAuditSettingModel;
import com.dj.tool.model.ReviewCommentInfoModel;
import com.dj.tool.service.CodeAuditSettingApplicationService;
import com.google.common.collect.Lists;
import com.intellij.ide.util.PropertiesComponent;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dj.tool.common.Constants.SETTING_CONF_PARENT_DIR_ID;
import static com.dj.tool.common.Constants.SETTING_CONF_SPACE_KEY;
import static com.dj.tool.common.Constants.SETTING_PASSWORD_KEY;
import static com.dj.tool.common.Constants.SETTING_URL_KEY;
import static com.dj.tool.common.Constants.SETTING_USER_KEY;

public class ApplicationCache {

    public static CodeAuditSettingModel getCodeAuditSetting() {
        PropertiesComponent appComponent = PropertiesComponent.getInstance();
        String url = appComponent.getValue(SETTING_URL_KEY);
        String user = appComponent.getValue(SETTING_USER_KEY);
        String password = appComponent.getValue(SETTING_PASSWORD_KEY);
        String spaceKey = appComponent.getValue(SETTING_CONF_SPACE_KEY);
        String parentId = appComponent.getValue(SETTING_CONF_PARENT_DIR_ID);
        CodeAuditSettingModel model = new CodeAuditSettingModel(url, user, password, spaceKey, parentId);
        return model;
    }

    public static void saveCodeAuditSetting(String url, String userName, String password, String spaceKey, String parentId) {
        PropertiesComponent appComponent = PropertiesComponent.getInstance();
        appComponent.setValue(SETTING_URL_KEY, url);
        appComponent.setValue(SETTING_USER_KEY, userName);
        appComponent.setValue(SETTING_PASSWORD_KEY, password);
        appComponent.setValue(SETTING_CONF_SPACE_KEY, spaceKey);
        appComponent.setValue(SETTING_CONF_PARENT_DIR_ID, parentId);
    }

    public static void cleanAllCache() {
        CodeAuditSettingApplicationService instance = CodeAuditSettingApplicationService.getInstance();
        instance.cleanAllCacheData();
    }

    public static void deleteOneFromCache(Long id) {
        if (id == null) {
            return;
        }
        CodeAuditSettingApplicationService instance = CodeAuditSettingApplicationService.getInstance();
        instance.deleteOneCacheData(id);
    }

    public static void deleteCacheList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        CodeAuditSettingApplicationService instance = CodeAuditSettingApplicationService.getInstance();
        instance.deleteCacheList(idList);
    }

    public static void addOneToCache(ReviewCommentInfoModel model) {
        if (Objects.isNull(model)) {
            return;
        }
        CodeAuditSettingApplicationService instance = CodeAuditSettingApplicationService.getInstance();
        instance.addOneCacheData(model);
    }

    public static Collection<ReviewCommentInfoModel> getAllDataList() {
        CodeAuditSettingApplicationService instance = CodeAuditSettingApplicationService.getInstance();
        return instance.getAllDataList();
    }

    public static List<ReviewCommentInfoModel> getProjectAllData(String projectName) {
        return Optional.ofNullable(getAllDataList()).orElseGet(Lists::newArrayList)
            .stream()
            .filter(item -> item.getProjectName().equalsIgnoreCase(projectName))
            .collect(Collectors.toList());
    }

    public static void updateProjectData(ReviewCommentInfoModel model) {
        CodeAuditSettingApplicationService instance = CodeAuditSettingApplicationService.getInstance();
        instance.updateItem(model);
    }

}
