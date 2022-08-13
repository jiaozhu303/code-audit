package com.dj.tool.common;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <ac:task-list>
 * <p>
 * <p>
 * <ac:task>
 * <p>
 * <ac:task-id>5</ac:task-id>
 * <p>
 * <ac:task-status>incomplete</ac:task-status>
 * <p>
 * <ac:task-body>
 * <span>Task 1</span>
 * </ac:task-body>
 * </ac:task>
 * <p>
 * <ac:task>
 * <ac:task-id>6</ac:task-id>
 * <ac:task-status>incomplete</ac:task-status>
 * <ac:task-body>
 * <span>Task 2</span>
 * </ac:task-body>
 * <p>
 * </ac:task>
 * <ac:task>
 * <ac:task-id>7</ac:task-id>
 * <ac:task-status>complete</ac:task-status>
 * <ac:task-body>
 * <span>Task 3 (Done)</span>
 * </ac:task-body>
 * <p>
 * </ac:task>
 * <p>
 * <p>
 * </ac:task-list>
 */
public class ConfluenceTaskListFactory {


    private String taskListTag; // <ac:task-list> </ac:task-list>
    private List<ConfluenceTaskItem> taskList = new ArrayList();

    public ConfluenceTaskListFactory(List<ConfluenceTaskItem> taskList) {
        this.taskList = taskList;
    }

    public String getTaskListTag() {
        return taskListTag;
    }

    public void setTaskListTag(String taskListTag) {
        this.taskListTag = taskListTag;
    }

    public List<ConfluenceTaskItem> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<ConfluenceTaskItem> taskList) {
        this.taskList = taskList;
    }

    @Override
    public String toString() {
        String taskTagList = Optional.ofNullable(this.taskList).orElseGet(ArrayList::new)
            .stream().map(ConfluenceTaskItem::toString)
            .reduce("", (item1, item2) -> item1 + item2);
        return "<ac:task-list> " + taskTagList + "</ac:task-list>";
    }
}


