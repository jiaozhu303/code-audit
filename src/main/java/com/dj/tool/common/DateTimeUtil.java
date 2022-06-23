package com.dj.tool.common;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateTimeUtil {
    private static final ThreadLocal<SimpleDateFormat> SDF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static String time2String(long millis) {
        return SDF.get().format(new Date(millis));
    }

    public static String getFormattedTimeForFileName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }
}
