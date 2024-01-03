package com.dj.tool.common

import org.apache.commons.collections4.CollectionUtils


/**
 * <ac:task-list>
 *
 *
 *
 *
 * <ac:task>
 *
 *
 * <ac:task-id>5</ac:task-id>
 *
 *
 * <ac:task-status>incomplete</ac:task-status>
 *
 *
 * <ac:task-body>
 * <span>Task 1</span>
</ac:task-body> *
</ac:task> *
 *
 *
 * <ac:task>
 * <ac:task-id>6</ac:task-id>
 * <ac:task-status>incomplete</ac:task-status>
 * <ac:task-body>
 * <span>Task 2</span>
</ac:task-body> *
 *
 *
</ac:task> *
 * <ac:task>
 * <ac:task-id>7</ac:task-id>
 * <ac:task-status>complete</ac:task-status>
 * <ac:task-body>
 * <span>Task 3 (Done)</span>
</ac:task-body> *
 *
 *
</ac:task> *
 *
 *
 *
 *
</ac:task-list> *
 */
class ConfluenceTaskListFactory(taskList: List<ConfluenceTaskItem?>) {
    var taskListTag: String? = null // <ac:task-list> </ac:task-list>
    var taskList: List<ConfluenceTaskItem?> = ArrayList()

    init {
        this.taskList = taskList
    }

    override fun toString(): String {
        if (CollectionUtils.isEmpty(this.taskList)) {
            return "";
        }
        val taskTagList = taskList
            .stream()
            .map { obj: ConfluenceTaskItem? -> obj.toString() }
            .reduce("") { item1: String, item2: String -> item1 + item2 }
        return "<ac:task-list> $taskTagList</ac:task-list>"
    }
}


