package com.dj.tool.common

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.lang3.ArrayUtils
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

object JsonUtils {
    private val GSON = Gson()

    fun <T> fromJson(json: String?, classOfT: Class<T>?): T {
        return GSON.fromJson(json, classOfT)
    }

    fun <T> fromJson(json: String?, typeOfT: Type?): T {
        return GSON.fromJson(json, typeOfT)
    }

    fun toJson(src: Any?): String {
        return GSON.toJson(src)
    }

    @Throws(IOException::class)
    fun <T> fromFile(filePath: String?, classOfT: Class<T>?): T {
        InputStreamReader(FileInputStream(filePath), StandardCharsets.UTF_8).use { json ->
            return GSON.fromJson(json, classOfT)
        }
    }

    fun toJsonWithNull(`object`: Any?): String {
        val gson = GsonBuilder().serializeNulls().create()
        return gson.toJson(`object`)
    }

    /**
     * 在转成json时，可以忽略某些字段的自定义策略。
     */
    private class CustomExclusionStrategy(private val ignoredFields: Array<String>) : ExclusionStrategy {
        override fun shouldSkipClass(clazz: Class<*>?): Boolean {
            return false
        }

        override fun shouldSkipField(f: FieldAttributes): Boolean {
            return ArrayUtils.contains(ignoredFields, f.name)
        }
    }
}
