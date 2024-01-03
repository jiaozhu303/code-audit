package com.dj.tool.common

import com.dj.tool.model.CodeAuditSettingModel
import com.dj.tool.model.ReviewCommentInfoModel
import com.dj.tool.service.CodeAuditSettingApplicationService.Companion.instance
import com.google.common.collect.Lists
import com.intellij.ide.util.PropertiesComponent
import org.apache.commons.collections4.CollectionUtils
import java.util.*

object ApplicationCache {
    val codeAuditSetting: CodeAuditSettingModel
        get() {
            val appComponent = PropertiesComponent.getInstance()
            val url = appComponent.getValue(Constants.SETTING_URL_KEY, "")
            val user = appComponent.getValue(Constants.SETTING_USER_KEY, "")
            val password = appComponent.getValue(Constants.SETTING_PASSWORD_KEY, "")
            val spaceKey = appComponent.getValue(Constants.SETTING_CONF_SPACE_KEY, "")
            val parentId = appComponent.getValue(Constants.SETTING_CONF_PARENT_DIR_ID, "")
            val model = CodeAuditSettingModel(url, user, password, spaceKey, parentId)
            return model
        }

    fun saveCodeAuditSetting(url: String?, userName: String?, password: String?, spaceKey: String?, parentId: String?) {
        val appComponent = PropertiesComponent.getInstance()
        appComponent.setValue(Constants.SETTING_URL_KEY, url)
        appComponent.setValue(Constants.SETTING_USER_KEY, userName)
        appComponent.setValue(Constants.SETTING_PASSWORD_KEY, password)
        appComponent.setValue(Constants.SETTING_CONF_SPACE_KEY, spaceKey)
        appComponent.setValue(Constants.SETTING_CONF_PARENT_DIR_ID, parentId)
    }

    fun cleanAllCache(projectName: String?) {
        val instance = instance
        instance.cleanAllCacheData(projectName)
    }

    fun deleteOneFromCache(id: Long?) {
        if (id == null) {
            return
        }
        val instance = instance
        instance.deleteOneCacheData(id)
    }

    fun deleteCacheList(idList: List<Long?>?) {
        if (CollectionUtils.isEmpty(idList)) {
            return
        }
        val instance = instance
        instance.deleteCacheList(idList!!)
    }

    fun addOneToCache(model: ReviewCommentInfoModel?) {
        if (Objects.isNull(model)) {
            return
        }
        val instance = instance
        instance.addOneCacheData(model!!)
    }

    fun getAllDataList(projectName: String?): List<ReviewCommentInfoModel?> {
        val instance = instance
        return instance.getAllDataList(projectName)
    }

    fun getProjectAllData(projectName: String?): List<ReviewCommentInfoModel?> {
        return Optional.ofNullable(getAllDataList(projectName)).orElseGet { Lists.newArrayList() }
    }

    fun updateProjectData(model: ReviewCommentInfoModel?) {
        val instance = instance
        instance.updateItem(model!!)
    }
}
