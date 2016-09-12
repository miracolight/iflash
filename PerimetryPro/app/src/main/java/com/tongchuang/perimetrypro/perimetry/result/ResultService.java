package com.tongchuang.perimetrypro.perimetry.result;

import com.tongchuang.perimetrypro.database.VisionDBSQLiteHelper;
import com.tongchuang.perimetrypro.perimetry.exam.object.ExamResult;

/**
 * Created by qingdi on 8/22/16.
 */
public interface ResultService {
    void saveResult(ExamResult result, final ResultServiceResponseHandler responseHandler);
}
