package com.tongchuang.perimetrypro.perimetry.pattern;

import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;

import java.util.List;

/**
 * Created by qingdi on 8/6/16.
 */
public interface PatternGenerator {
    public static enum PatternType {P24_2, P30_2, PDEMO};

    public String[] getStimulusPositionCodes(ExamSettings.EXAM_FIELD_OPTION fieldOption);

    public String   getBlindSpot(ExamSettings.EXAM_FIELD_OPTION fieldOption);
}
