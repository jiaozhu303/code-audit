package com.dj.tool.common

import com.dj.tool.model.ReviewCommentInfoModel
import org.apache.commons.compress.utils.Lists
import org.apache.commons.lang3.StringUtils
import java.io.Closeable
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors

object CommonUtil {
    @JvmStatic
    fun closeQuitely(closeable: Closeable?) {
        if (closeable == null) {
            return
        }

        try {
            closeable.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun copyToString(cachedComments: List<ReviewCommentInfoModel?>): String {
        return Optional.ofNullable(cachedComments).orElseGet { ArrayList() }
            .stream()
            .map { obj: ReviewCommentInfoModel? -> obj?.toCopyString() ?: "" }
            .reduce("") { a: String, b: String -> a + b }
    }

    private val SDF: ThreadLocal<SimpleDateFormat> = ThreadLocal.withInitial { SimpleDateFormat("yyyy-MM-dd HH:mm:ss") }

    fun time2String(millis: Long): String {
        return SDF.get().format(Date(millis))
    }

    val formattedTimeForFileName: String
        get() {
            val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss")
            return simpleDateFormat.format(Date())
        }

    fun getFormattedTimeForTitle(projectName: String): String {
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd-HH.mm.ss")
        return simpleDateFormat.format(Date()) + "【" + projectName + "】"
    }

    fun buildConfluenceFormatString(dataList: List<ReviewCommentInfoModel?>): String {
        val stringDataList = Optional.ofNullable(dataList)
            .orElseGet { Lists.newArrayList() }
            .stream()
            .filter { obj: ReviewCommentInfoModel? -> Objects.nonNull(obj) }
            .map { obj: ReviewCommentInfoModel? -> obj?.toSyncString() }
            .filter { cs: String? -> StringUtils.isNotBlank(cs) }
            .collect(Collectors.toList())

        val taskList = stringDataList.stream()
            .map { task: String? -> task?.let { ConfluenceTaskItem(stringDataList.indexOf(task), it) } }
            .collect(Collectors.toList())
        return ConfluenceTaskListFactory(taskList).toString()
    }
}
