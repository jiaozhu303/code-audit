package com.dj.tool.service

import com.dj.tool.model.ReviewCommentInfoModel
import com.google.common.collect.Maps
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.collections.MapUtils
import java.util.*
import java.util.function.BinaryOperator
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Collectors

@State(name = "CodeAuditSettingService", storages = [Storage(value = "CodeAuditSettingApplicationService.xml")])
class CodeAuditSettingApplicationService : PersistentStateComponent<MutableMap<Long?, ReviewCommentInfoModel?>?> {
    fun getData(): Map<Long?, ReviewCommentInfoModel?> {
        return data
    }

    fun setData(data: MutableMap<Long?, ReviewCommentInfoModel?>) {
        this.data = data
    }

    private var data: MutableMap<Long?, ReviewCommentInfoModel?> = HashMap()

    /**
     * @return a component state. All properties, public and annotated fields are serialized. Only values, which differ
     * from the default (i.e., the value of newly instantiated class) are serialized. `null` value indicates
     * that the returned state won't be stored, as a result previously stored state will be used.
     * @see XmlSerializer
     */
    override fun getState(): MutableMap<Long?, ReviewCommentInfoModel?>? {
        return data
    }

    /**
     * This method is called when new component state is loaded. The method can and will be called several times, if
     * config files were externally changed while IDE was running.
     *
     *
     * State object should be used directly, defensive copying is not required.
     *
     * @param state loaded component state
     * @see XmlSerializerUtil.copyBean
     */
    override fun loadState(state: MutableMap<Long?, ReviewCommentInfoModel?>) {
        data = state
    }

    fun cleanAllCacheData(projectName: String?) {
        this.data =
            Optional.ofNullable<Map<Long?, ReviewCommentInfoModel?>>(this.data).orElseGet { Maps.newHashMap() }.entries
                .stream()
                .filter { entry: Map.Entry<Long?, ReviewCommentInfoModel?> ->
                    entry.value != null && entry.value!!.projectName != null && !entry.value!!.projectName.equals(
                        projectName,
                        ignoreCase = true
                    )
                }
                .collect(
                    Collectors.toMap(
                        Function { en: Map.Entry<Long?, ReviewCommentInfoModel?> -> en.key },
                        Function { en: Map.Entry<Long?, ReviewCommentInfoModel?> -> en.value },
                        BinaryOperator { l: ReviewCommentInfoModel?, r: ReviewCommentInfoModel? -> l })
                )
    }

    fun addOneCacheData(model: ReviewCommentInfoModel) {
        data[model.identifier] = model
    }

    fun deleteOneCacheData(id: Long?) {
        if (data.containsKey(id)) {
            data.remove(id)
        }
    }

    fun deleteCacheList(idList: List<Long?>) {
        if (CollectionUtils.isEmpty(idList)) {
            return
        }
        idList.forEach(Consumer { id: Long? ->
            if (data.containsKey(id)) {
                data.remove(id)
            }
        })
    }

    fun getAllDataList(projectName: String?): List<ReviewCommentInfoModel?> {
        return Optional.ofNullable<Map<Long?, ReviewCommentInfoModel?>>(this.data)
            .orElseGet { Maps.newHashMap() }.entries
            .stream()
            .filter { entry: Map.Entry<Long?, ReviewCommentInfoModel?> ->
                entry.value != null && entry.value!!.projectName != null && entry.value!!.projectName.equals(
                    projectName,
                    ignoreCase = true
                )
            }
            .map { en: Map.Entry<Long?, ReviewCommentInfoModel?> -> en.value }
            .collect(Collectors.toList())
    }

    fun updateItem(model: ReviewCommentInfoModel) {
        if (Objects.isNull(model) && MapUtils.isNotEmpty(this.data)) {
            return
        }
        data[model.identifier] = model
    }

    companion object {
        @JvmStatic
        val instance: CodeAuditSettingApplicationService
            get() =// implementation according to Application/Project level service
                ApplicationManager.getApplication().getService(
                    CodeAuditSettingApplicationService::class.java
                )
    }
}
