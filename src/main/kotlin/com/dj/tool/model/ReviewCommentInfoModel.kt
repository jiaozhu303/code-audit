package com.dj.tool.model

import com.google.common.base.Objects
import com.intellij.openapi.util.text.StringUtil
import java.io.Serializable


class ReviewCommentInfoModel : Serializable {
    @JvmField
    var identifier: Long = 0
    @JvmField
    var reviewer: String? = null

    @JvmField
    var comments: String? = null

    @JvmField
    var filePath: String? = null

    /**
     * start ~ end的格式，用于显示
     * 运算的时候，行号是从0计算的，因此显示的时候，start和end在实际下标上+1
     */
    private var lineRange: String? = null

    @JvmField
    var startLine: Int = 0
    @JvmField
    var endLine: Int = 0

    @JvmField
    var content: String? = null

    @JvmField
    var author: String? = null

    @JvmField
    var type: String? = null

    @JvmField
    var severity: String? = null

    @JvmField
    var factor: String? = null

    @JvmField
    var dateTime: String? = null

    @JvmField
    var projectName: String? = null


    fun getLineRange(): String {
        if (lineRange == null) {
            val start = startLine + 1
            val end = endLine + 1
            lineRange = "$start ~ $end"
        }
        return lineRange as String
    }

    fun lineMatched(currentLine: Int): Boolean {
        if (startLine > currentLine || endLine < currentLine) {
            // 范围没有交集
            return false
        }

        //        if (startLine > startIndex && endIndex < endLine) {
//            // 完全在范围内的情况，忽略
//            // 比如选择了一大段内容，里面会有很多的空格或者换行的情况，直接忽略掉
//            return false;
//        }
        return true
    }

    override fun toString(): String {
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
                '}'
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is ReviewCommentInfoModel) return false
        val that = o
        return identifier == that.identifier && startLine == that.startLine && endLine == that.endLine && Objects.equal(
            reviewer, that.reviewer
        ) && Objects.equal(comments, that.comments) && Objects.equal(
            filePath, that.filePath
        ) && Objects.equal(getLineRange(), that.getLineRange()) && Objects.equal(
            content, that.content
        ) && Objects.equal(author, that.author) && Objects.equal(type, that.type) && Objects.equal(
            severity, that.severity
        ) && Objects.equal(factor, that.factor) && Objects.equal(dateTime, that.dateTime) && Objects.equal(
            projectName, that.projectName
        )
    }

    override fun hashCode(): Int {
        return Objects.hashCode(
            identifier,
            reviewer,
            comments,
            filePath,
            getLineRange(),
            startLine,
            endLine,
            content,
            author,
            type,
            severity,
            factor,
            dateTime,
            projectName
        )
    }

    fun toCopyString(): String {
        var copyLine = "[$projectName] - $filePath line: $lineRange"
        if (!StringUtil.isEmpty(comments)) {
            copyLine = "$copyLine ($comments) "
        }
        if (!StringUtil.isEmpty(author)) {
            copyLine = "$copyLine author: @$author"
        }
        return """
             $copyLine
             
             
             """.trimIndent()
    }

    fun toSyncString(): String {
        var copyLine = "[$projectName] - $filePath line: $lineRange"
        if (!StringUtil.isEmpty(comments)) {
            copyLine = "$copyLine ($comments) "
        }
        if (!StringUtil.isEmpty(author)) {
            copyLine = "$copyLine author: @$author"
        }
        return copyLine
    }

    companion object {
        private const val serialVersionUID = -5134323185285399922L
    }
}
