package com.dj.tool.common;

public class ConfluenceTaskItem {

    private Integer taskId;                  // <ac:task-id>5</ac:task-id>
    private String taskStatus;              // <ac:task-status>incomplete</ac:task-status>  complete/incomplete
    private String taskBody;                // <ac:task-body> <span>Task 1</span></ac:task-body>

    public ConfluenceTaskItem(Integer taskId, String taskBody) {
        this.taskId = taskId;
        this.taskBody = taskBody;
    }

    @Override
    public String toString() {
        return "<ac:task>" + this.getTaskIdTag() + this.getTaskStatusTag() + this.getTaskBodyWithSpanTag() + "</ac:task>";
    }

    public String getTaskIdTag() {
        return "<ac:task-id>" + this.taskId + "</ac:task-id>";
    }

    public String getTaskStatusTag() {
        return "<ac:task-status>incomplete</ac:task-status>";
    }

    public String getTaskBodyWithSpanTag() {
        return "<ac:task-body><span>" + this.taskBody + "</span></ac:task-body>";
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskBody() {
        return taskBody;
    }

    public void setTaskBody(String taskBody) {
        this.taskBody = taskBody;
    }

}
