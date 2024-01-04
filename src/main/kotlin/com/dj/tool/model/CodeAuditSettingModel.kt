package com.dj.tool.model

import org.apache.commons.lang3.StringUtils

class CodeAuditSettingModel(
    @JvmField var url: String,
    @JvmField var userName: String,
    @JvmField var password: String,
    @JvmField var spaceKey: String,
    @JvmField var parentId: String
) {
    val isValid: Boolean
        get() = StringUtils.isNoneBlank(this.url) &&
                StringUtils.isNoneBlank(this.userName) &&
                StringUtils.isNoneBlank(this.password) &&
                StringUtils.isNoneBlank(this.spaceKey) &&
                StringUtils.isNoneBlank(this.password)
}
