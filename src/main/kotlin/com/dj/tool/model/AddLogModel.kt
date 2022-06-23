package com.dj.tool.model

data class AddLogModel(
    var reviewer: String? = null,
    var comments: String? = null,
    var filePath: String? = null,

    var startLine: Int = 0,
    var endLine: Int = 0,
    var content: String? = null,
    var author: String? = null,
    var type: String? = null,
    var severity: String? = null,
    var factor: String? = null,
    var dateTime: String? = null
)
