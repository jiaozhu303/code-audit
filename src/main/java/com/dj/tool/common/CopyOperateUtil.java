package com.dj.tool.common;


import com.dj.tool.model.ReviewCommentInfoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CopyOperateUtil {
    public static String copyToString(List<ReviewCommentInfoModel> cachedComments) {
        return Optional.ofNullable(cachedComments).orElseGet(ArrayList::new)
            .stream()
            .map(ReviewCommentInfoModel::toCopyString)
            .reduce("", (a, b) -> a + b);
    }
}
