package com.dj.tool.service;

import com.dj.tool.model.ReviewCommentInfoModel;
import com.google.common.collect.Lists;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@State(name = "CodeAuditSettingProjectService",
    storages = {
        @Storage(value = "CodeAuditSettingProjectService.xml")
    }
)
public final class CodeAuditSettingProjectService implements PersistentStateComponent<List<ReviewCommentInfoModel>> {

    private List<ReviewCommentInfoModel> projectDataList = Lists.newArrayList();


    public List<ReviewCommentInfoModel> getProjectDataList() {
        return projectDataList;
    }

    public void setProjectDataList(List<ReviewCommentInfoModel> projectDataList) {
        this.projectDataList = projectDataList;
    }

    /**
     * @return a component state. All properties, public and annotated fields are serialized. Only values, which differ
     * from the default (i.e., the value of newly instantiated class) are serialized. {@code null} value indicates
     * that the returned state won't be stored, as a result previously stored state will be used.
     * @see XmlSerializer
     */
    @Nullable
    @Override
    public List<ReviewCommentInfoModel> getState() {
        return projectDataList;
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
    public void loadState(@NotNull List<ReviewCommentInfoModel> state) {
        projectDataList = state;
    }

    public static CodeAuditSettingProjectService getInstance(Project project) {
        // implementation according to Application/Project level service
        return project.getService(CodeAuditSettingProjectService.class);
    }

    public List<ReviewCommentInfoModel> getProjectAllData() {
        return this.projectDataList;
    }

    public void addProjectData(ReviewCommentInfoModel data) {
        if (this.projectDataList == null) {
            this.projectDataList = Lists.newArrayList();
        }
        this.projectDataList.add(data);
    }

    public void deleteComments(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        this.projectDataList = Optional.ofNullable(this.projectDataList).orElseGet(Lists::newArrayList)
            .stream()
            .filter(data -> !idList.contains(data.getIdentifier()))
            .collect(Collectors.toList());
    }

    public void cleanAllData() {
        this.projectDataList = Lists.newArrayList();
    }

    public void updateProjectData(ReviewCommentInfoModel model) {
        Optional.ofNullable(this.projectDataList).orElseGet(Lists::newArrayList)
            .stream()
            .forEach(data -> {
                if (data.getIdentifier() == model.getIdentifier()) {
                    data = model;
                }
            });
    }
}
