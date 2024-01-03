package com.dj.tool.model

import com.google.common.base.Objects
import java.io.Serializable

class CodeReviewCommentCache : Serializable {
    /**
     * 记录最后一次填写的review信息，用于添加评审意见的时候，记住上一次填写的author、选择的类别等信息
     */
    var lastCommentData: ReviewCommentInfoModel? = null
    var comments: Map<Long, ReviewCommentInfoModel>? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is CodeReviewCommentCache) return false
        val that = o
        return Objects.equal(lastCommentData, that.lastCommentData) && Objects.equal(
            comments, that.comments
        )
    }

    override fun hashCode(): Int {
        return Objects.hashCode(lastCommentData, comments)
    }

    companion object {
        private const val serialVersionUID = -1770117176436016029L
    }
}
