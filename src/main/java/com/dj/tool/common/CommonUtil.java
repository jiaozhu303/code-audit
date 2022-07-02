package com.dj.tool.common;

import com.dj.tool.model.ReviewCommentInfoModel;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


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

    public static String getFormattedTimeForTitle() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");
        return simpleDateFormat.format(new Date());
    }

    public static String buildConfluenceFormatString(Collection<ReviewCommentInfoModel> dataList) {
        String data = Optional.ofNullable(dataList)
            .orElseGet(Lists::newArrayList)
            .stream()
            .filter(Objects::nonNull)
            .map(ReviewCommentInfoModel::toSyncString)
            .filter(StringUtils::isNotBlank)
            .map(item -> "<li>" + item + "        fixed: no" + "</li>")
//            .map(CommonUtil::buildItem)
            .reduce("", (x, y) -> x + y);
        return data;
    }

    private static String buildItem(String itemData) {
        String id = UUID.randomUUID().toString();
        return "<div>" +
            "  <input type=\"checkbox\" id=\" " + id + "\" value=\"second_checkbox\" checked=\"checked\">" +
            "  <label for=\"" + id + " \">" + itemData +
            "</div>";
    }

}
