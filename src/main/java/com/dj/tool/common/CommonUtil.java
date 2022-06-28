package com.dj.tool.common;

import com.dj.tool.model.ReviewCommentInfoModel;
import com.intellij.openapi.diagnostic.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public class CommonUtil {

    public static void closeQuitely(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String copyToString(List<ReviewCommentInfoModel> cachedComments) {
        return Optional.ofNullable(cachedComments).orElseGet(ArrayList::new)
            .stream()
            .map(ReviewCommentInfoModel::toCopyString)
            .reduce("", (a, b) -> a + b);
    }

    private static final ThreadLocal<SimpleDateFormat> SDF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static String time2String(long millis) {
        return SDF.get().format(new Date(millis));
    }

    public static String getFormattedTimeForFileName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }

}
