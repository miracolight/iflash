package com.tongchuang.perimetrypro.util;

import android.graphics.Point;

import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qingdi on 8/7/16.
 */
public class ExamUtil {

    private static Pattern POS_CODE_PATTERN = Pattern.compile("q([0-9]+)r([0-9]+)c([0-9]+)");
    public static Point getPoint(String posCode, ExamSettings examSettings) {


        Matcher m = POS_CODE_PATTERN.matcher(posCode);

        if (!m.find()) {
            throw new RuntimeException("matcher not found!");
        }
        int quad = Integer.parseInt(m.group(1));
        int row = Integer.parseInt(m.group(2));
        int col = Integer.parseInt(m.group(3));

        int x = (int)(((col-1)+0.5)*examSettings.getStimulusSpacing());
        int y = (int)(((row-1)+0.5)*examSettings.getStimulusSpacing());
        if (quad == 2) {
            x = -x;
        } else if (quad == 3) {
            x= -x; y = -y;
        } else if (quad == 4) {
            y = -y;
        }

        x += (examSettings.getExamFieldOption()==ExamSettings.EXAM_FIELD_OPTION.LEFT)?
                examSettings.getLeftFixation().x:examSettings.getRightFixation().x;
        y += examSettings.getLeftFixation().y;
        return new Point(x, y);
    }


}
