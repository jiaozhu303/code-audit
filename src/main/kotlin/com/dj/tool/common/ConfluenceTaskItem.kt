package com.dj.tool.common

class ConfluenceTaskItem(// <ac:task-id>5</ac:task-id>
    var taskId: Int, // <ac:task-body> <span>Task 1</span></ac:task-body>
    var taskBody: String
) {
    var taskStatus: String? = null // <ac:task-status>incomplete</ac:task-status>  complete/incomplete

    override fun toString(): String {
        return "<ac:task>" + this.taskIdTag + this.taskStatusTag + this.taskBodyWithSpanTag + "</ac:task>"
    }

    val taskIdTag: String
        get() = "<ac:task-id>" + this.taskId + "</ac:task-id>"

    val taskStatusTag: String
        get() = "<ac:task-status>incomplete</ac:task-status>"

    val taskBodyWithSpanTag: String
        get() = "<ac:task-body><span>" + this.taskBody + "</span></ac:task-body>"
}
