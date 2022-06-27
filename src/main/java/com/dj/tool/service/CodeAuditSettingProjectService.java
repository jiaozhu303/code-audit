package com.dj.tool.service;

import com.dj.tool.model.CodeReviewCommentCache;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@State(name = "CodeAuditSettingProjectService",
    storages = {
        @Storage(value = "CodeAuditSettingProjectService.xml")
    }
)
public final class CodeAuditSettingProjectService implements PersistentStateComponent<Map<String, CodeReviewCommentCache>> {


    private Map<String, CodeReviewCommentCache> projectData = new HashMap<>();

    /**
     * @return a component state. All properties, public and annotated fields are serialized. Only values, which differ
     * from the default (i.e., the value of newly instantiated class) are serialized. {@code null} value indicates
     * that the returned state won't be stored, as a result previously stored state will be used.
     * @see XmlSerializer
     */
    @Nullable
    @Override
    public Map<String, CodeReviewCommentCache> getState() {
        return projectData;
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
    public void loadState(@NotNull Map<String, CodeReviewCommentCache> state) {
        projectData = state;
    }

    public static CodeAuditSettingProjectService getInstance(Project project) {
        // implementation according to Application/Project level service
        return project.getService(CodeAuditSettingProjectService.class);
    }

    public CodeReviewCommentCache getProjectData(String cacheId) {
        return this.projectData.getOrDefault(cacheId, new CodeReviewCommentCache());
    }

    public void setProjectData(String cacheId, CodeReviewCommentCache data) {
        if (this.projectData == null) {
            this.projectData = new HashMap<>();
        }
        this.projectData.put(cacheId, data);
    }
}
