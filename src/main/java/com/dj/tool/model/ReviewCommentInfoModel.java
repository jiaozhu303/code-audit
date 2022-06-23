package com.dj.tool.model;


import com.intellij.openapi.util.text.StringUtil;

import java.io.Serializable;

public class ReviewCommentInfoModel implements Serializable {
    private static final long serialVersionUID = -5134323185285399922L;
    private long identifier;
    private String reviewer;

    private String comments;

    private String filePath;

    /**
     * start ~ end的格式，用于显示
     * 运算的时候，行号是从0计算的，因此显示的时候，start和end在实际下标上+1
     */
    private String lineRange;

    private int startLine;
    private int endLine;

    private String content;

    private String author;

    private String type;

    private String severity;

    private String factor;

    private String dateTime;

    private String projectName;

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ReviewCommentInfoModel() {
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLineRange() {
        if (lineRange == null) {
            int start = startLine + 1;
            int end = endLine + 1;
            lineRange = start + " ~ " + end;
        }
        return lineRange;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public int getStartLine() {
        return this.startLine;
    }

    public int getEndLine() {
        return this.endLine;
    }

    public boolean lineMatched(int currentLine) {

        if (startLine > currentLine || endLine < currentLine) {
            // 范围没有交集
            return false;
        }

//        if (startLine > startIndex && endIndex < endLine) {
//            // 完全在范围内的情况，忽略
//            // 比如选择了一大段内容，里面会有很多的空格或者换行的情况，直接忽略掉
//            return false;
//        }

        return true;
    }

    @Override
    public String toString() {
        return "ReviewCommentInfoModel{" +
            "identifier=" + identifier +
            ", reviewer='" + reviewer + '\'' +
            ", comments='" + comments + '\'' +
            ", filePath='" + filePath + '\'' +
            ", lineRange='" + lineRange + '\'' +
            ", content='" + content + '\'' +
            ", type='" + type + '\'' +
            ", severity='" + severity + '\'' +
            ", factor='" + factor + '\'' +
            ", dateTime='" + dateTime + '\'' +
            ", author='" + author + '\'' +
            ", projectName='" + projectName + '\'' +
            '}';
    }

    public String toCopyString() {
        String copyLine = "[" + projectName + "] - " + filePath + " line: " + lineRange;
        if (!StringUtil.isEmpty(comments)) {
            copyLine = copyLine + " (" + comments + ") ";
        }
        if (!StringUtil.isEmpty(author)) {
            copyLine = copyLine + " author: @" + author;
        }
        return copyLine + "\n\r";
    }
}
