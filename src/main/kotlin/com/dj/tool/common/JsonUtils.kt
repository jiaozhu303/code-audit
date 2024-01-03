package com.dj.tool.common;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.ArrayUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public final class JsonUtils {

    private static final Gson GSON = new Gson();

    private JsonUtils() {
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    public static String toJson(Object src) {
        return GSON.toJson(src);
    }

    public static <T> T fromFile(String filePath, Class<T> classOfT) throws IOException {
        try (Reader json = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)) {
            return GSON.fromJson(json, classOfT);
        }
    }

    /**
     * 在转成json时，可以忽略某些字段的自定义策略。
     */
    private static class CustomExclusionStrategy implements ExclusionStrategy {
        private String[] ignoredFields;

        public CustomExclusionStrategy(String[] ignoredFields) {
            this.ignoredFields = ignoredFields;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return ArrayUtils.contains(ignoredFields, f.getName());
        }
    }

    public static String toJsonWithNull(Object object) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(object);
    }
}
