package com.dj.tool.model;

import java.io.Serializable;
import java.util.Map;


public class CodeReviewCommentCache implements Serializable {
    private static final long serialVersionUID = -1770117176436016029L;
    /**
     * 记录最后一次填写的review信息，用于添加评审意见的时候，记住上一次填写的author、选择的类别等信息
     */
    private ReviewCommentInfoModel lastCommentData;
    private Map<Long, ReviewCommentInfoModel> comments;

    public ReviewCommentInfoModel getLastCommentData() {
        return lastCommentData;
    }

    public void setLastCommentData(ReviewCommentInfoModel lastCommentData) {
        this.lastCommentData = lastCommentData;
    }

    public Map<Long, ReviewCommentInfoModel> getComments() {
        return comments;
    }

    public void setComments(Map<Long, ReviewCommentInfoModel> comments) {
        this.comments = comments;
    }

}
