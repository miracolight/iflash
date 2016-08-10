package com.tongchuang.perimetrypro.util;

import android.graphics.Color;

import com.tongchuang.perimetrypro.perimetry.common.Intensity;

/**
 * Created by qingdi on 8/5/16.
 */
public class IntensityUtil {
    public static int getBackgroundColor(Intensity intensity) {
        int color = Color.argb(intensity.getBackgroundAlpha(),
               intensity.getBackgroundGreyscale(), intensity.getBackgroundGreyscale(), intensity.getBackgroundGreyscale());
        return color;
    }

    public static int getStimulusColor(Intensity intensity) {
        int color = Color.argb(intensity.getStimulusAlpha(),
                intensity.getStimulusGreyscale(), intensity.getStimulusGreyscale(), intensity.getStimulusGreyscale());
        return color;
    }
}
