package com.dj.tool.common

object Constants {
    const val MIME_TYPE_APPLICATION_JSON: String = "application/json"
    const val HEADER_NAME_CONTENT_TYPE: String = "Content-Type"
    const val HEADER_NAME_AUTHORIZATION: String = "Authorization"
    const val BASIC_AUTH_PREFIX: String = "Basic "
    const val CONNECTION_REQUEST_TIMEOUT: Int = 10000
    const val SOCKET_TIMEOUT: Int = 5000
    const val CONNECT_TIMEOUT: Int = 2000
    const val CHARSET_UTF_8: String = "UTF-8"
    var TYPE_QUESTION: String = "Question"
    var TYPE_ADVICE: String = "Advice"
    var TYPE_ALLEGATIONS: String = "Allegations"

    var SEVERITY_WARNING: String = "Warning"
    var SEVERITY_GENERAL: String = "General"
    var SEVERITY_SERIOUS: String = "Serious"

    var FACTOR_BASIC: String = "Basic Coding"
    var FACTOR_BUSINESS: String = "Business"
    var FACTOR_SECURITY: String = "Security"


    const val REVIEWER: String = "reviewer"

    const val SETTING_URL_KEY: String = "code_audit_key_url"
    const val SETTING_USER_KEY: String = "code_audit_key_user_name"
    const val SETTING_PASSWORD_KEY: String = "code_audit_key_password"

    const val SETTING_CONF_SPACE_KEY: String = "code_audit_conf_space_key"

    const val SETTING_CONF_PARENT_DIR_ID: String = "code_audit_conf__parent_dir_id"

    const val CREATE_DOC_CONFLUENCE_API: String =
        "https://km.xpaas.lenovo.com/rest/api/content" // space_key: "PPC", dir_id: "172484093",
}
