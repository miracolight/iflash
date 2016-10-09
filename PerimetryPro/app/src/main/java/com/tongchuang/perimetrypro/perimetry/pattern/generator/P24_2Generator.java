package com.tongchuang.perimetrypro.perimetry.pattern.generator;

import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.pattern.PatternGenerator;

/**
 * Created by qingdi on 8/6/16.
 */
public class P24_2Generator implements PatternGenerator {
    String[] positionCodesLeft = {"q1r1c1","q1r1c2","q1r1c3","q1r1c4","q1r1c5",
            "q1r2c1","q1r2c2","q1r2c3","q1r2c4",
            "q1r3c1","q1r3c2","q1r3c3",
            "q1r4c1","q1r4c2",
            "q2r1c1","q2r1c2","q2r1c3","q2r1c4",
            "q2r2c1","q2r2c2","q2r2c3","q2r2c4",
            "q2r3c1","q2r3c2","q2r3c3",
            "q2r4c1","q2r4c2",
            "q3r1c1","q3r1c2","q3r1c3","q3r1c4",
            "q3r2c1","q3r2c2","q3r2c3","q3r2c4",
            "q3r3c1","q3r3c2","q3r3c3",
            "q3r4c1","q3r4c2",
            "q4r1c1","q4r1c2","q4r1c3","q4r1c4","q4r1c5",
            "q4r2c1","q4r2c2","q4r2c3","q4r2c4",
            "q4r3c1","q4r3c2","q4r3c3",
            "q4r4c1","q4r4c2"};

    String[] positionCodesRight = {"q1r1c1","q1r1c2","q1r1c3","q1r1c4",
            "q1r2c1","q1r2c2","q1r2c3","q1r2c4",
            "q1r3c1","q1r3c2","q1r3c3",
            "q1r4c1","q1r4c2",
            "q2r1c1","q2r1c2","q2r1c3","q2r1c4","q2r1c5",
            "q2r2c1","q2r2c2","q2r2c3","q2r2c4",
            "q2r3c1","q2r3c2","q2r3c3",
            "q2r4c1","q2r4c2",
            "q3r1c1","q3r1c2","q3r1c3","q3r1c4","q3r1c5",
            "q3r2c1","q3r2c2","q3r2c3","q3r2c4",
            "q3r3c1","q3r3c2","q3r3c3",
            "q3r4c1","q3r4c2",
            "q4r1c1","q4r1c2","q4r1c3","q4r1c4",
            "q4r2c1","q4r2c2","q4r2c3","q4r2c4",
            "q4r3c1","q4r3c2","q4r3c3",
            "q4r4c1","q4r4c2"};

    String blindSpotLeft = "q3r1c3";
    String blindSpotRight = "q4r1c3";

    @Override
    public String[] getStimulusPositionCodes(ExamSettings.EXAM_FIELD_OPTION fieldOption) {
        return (fieldOption== ExamSettings.EXAM_FIELD_OPTION.LEFT)? positionCodesLeft : positionCodesRight;
    }

    @Override
    public String getBlindSpot(ExamSettings.EXAM_FIELD_OPTION fieldOption) {
        return (fieldOption== ExamSettings.EXAM_FIELD_OPTION.LEFT)? blindSpotLeft : blindSpotRight;
    }


}
