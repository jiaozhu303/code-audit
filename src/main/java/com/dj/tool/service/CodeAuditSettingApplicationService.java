package com.dj.tool.service;

import com.dj.tool.model.ReviewCommentInfoModel;
import com.google.common.collect.Maps;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@State(name = "CodeAuditSettingService",
    storages = {
        @Storage(value = "CodeAuditSettingApplicationService.xml")
    }
)
public final class CodeAuditSettingApplicationService implements PersistentStateComponent<Map<Long, ReviewCommentInfoModel>> {

    public Map<Long, ReviewCommentInfoModel> getData() {
        return data;
    }

    public void setData(Map<Long, ReviewCommentInfoModel> data) {
        this.data = data;
    }

    private Map<Long, ReviewCommentInfoModel> data = new HashMap<>();

    /**
     * @return a component state. All properties, public and annotated fields are serialized. Only values, which differ
     * from the default (i.e., the value of newly instantiated class) are serialized. {@code null} value indicates
     * that the returned state won't be stored, as a result previously stored state will be used.
     * @see XmlSerializer
     */
    @Nullable
    @Override
    public Map<Long, ReviewCommentInfoModel> getState() {
        return data;
    }

    /**
     * This method is called when new component state is loaded. The method can and will be called several times, if
     * config files were externally changed while IDE was running.
     * <p>
     * State object should be used directly, defensive copying is not required.
     *
     * @param state loaded component state
     * @see XmlSerializerUtil#copyBean(Object, Object)
     */
    @Override
    public void loadState(@NotNull Map<Long, ReviewCommentInfoModel> state) {
        data = state;
    }

    public static CodeAuditSettingApplicationService getInstance() {
        // implementation according to Application/Project level service
        return ApplicationManager.getApplication().getService(CodeAuditSettingApplicationService.class);
    }

    public void cleanAllCacheData() {
        this.data = new HashMap<>();
    }

    public void addOneCacheData(ReviewCommentInfoModel model) {
        this.data.put(model.getIdentifier(), model);
    }

    public void deleteOneCacheData(Long id) {
        if (this.data.containsKey(id)) {
            this.data.remove(id);
        }
    }

    public void deleteCacheList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        idList.forEach(id -> {
            if (this.data.containsKey(id)) {
                this.data.remove(id);
            }
        });
    }

    public Collection<ReviewCommentInfoModel> getAllDataList() {
        return Optional.ofNullable(this.data).orElseGet(Maps::newHashMap).values();
    }

    public void updateItem(ReviewCommentInfoModel model) {
        if (Objects.isNull(model) && MapUtils.isNotEmpty(this.data)) {
            return;
        }
        this.data.put(model.getIdentifier(), model);
    }
}
