package com.dj.tool.common;

import com.dj.tool.model.CodeAuditSettingModel;
import com.dj.tool.model.ReviewCommentInfoModel;
import com.dj.tool.service.CodeAuditSettingApplicationService;
import com.intellij.ide.util.PropertiesComponent;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.dj.tool.common.Constants.SETTING_PASSWORD_KEY;
import static com.dj.tool.common.Constants.SETTING_URL_KEY;
import static com.dj.tool.common.Constants.SETTING_USER_KEY;

public class ApplicationCache {

    public static CodeAuditSettingModel getCodeAuditSetting() {
        PropertiesComponent appComponent = PropertiesComponent.getInstance();
        String url = appComponent.getValue(SETTING_URL_KEY);
        String user = appComponent.getValue(SETTING_USER_KEY);
        String password = appComponent.getValue(SETTING_PASSWORD_KEY);
        CodeAuditSettingModel model = new CodeAuditSettingModel(url, user, password);
        return model;
    }

    public static void saveCodeAuditSetting(String url, String userName, String password) {
        PropertiesComponent appComponent = PropertiesComponent.getInstance();
        appComponent.setValue(SETTING_URL_KEY, url);
        appComponent.setValue(SETTING_USER_KEY, userName);
        appComponent.setValue(SETTING_PASSWORD_KEY, password);
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

}
