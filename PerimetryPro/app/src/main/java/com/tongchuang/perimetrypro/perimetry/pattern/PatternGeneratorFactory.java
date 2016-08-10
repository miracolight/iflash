package com.tongchuang.perimetrypro.perimetry.pattern;

import com.tongchuang.perimetrypro.perimetry.pattern.generator.P24_2Generator;

/**
 * Created by qingdi on 8/6/16.
 */
public class PatternGeneratorFactory {

    public static PatternGenerator getPatternGenerator(PatternGenerator.PatternType patternType) {
        if (patternType == PatternGenerator.PatternType.P24_2) {
            return new P24_2Generator();
        }

        return null;
    }

}
