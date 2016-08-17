package com.tongchuang.perimetrypro.perimetry.pattern.generator;

import com.tongchuang.perimetrypro.perimetry.pattern.PatternGenerator;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;

/**
 * Created by qingdi on 8/6/16.
 */
public class PDemoGenerator implements PatternGenerator {
    String[] positionCodesLeft = {"q1r1c1", "q2r1c1", "q3r1c1", "q4r1c1"};

    String[] positionCodesRight = {"q1r1c1", "q2r1c1", "q3r1c1", "q4r1c1"};

    @Override
    public String[] getStimulusPositionCodes(ExamSettings.EXAM_FIELD_OPTION fieldOption) {
        return (fieldOption== ExamSettings.EXAM_FIELD_OPTION.LEFT)? positionCodesLeft : positionCodesRight;
    }
}
