package com.tongchuang.perimetrypro.util;

import android.graphics.Point;

import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qingdi on 8/7/16.
 */
public class ExamUtil {

    private static Pattern POS_CODE_PATTERN = Pattern.compile("q([0-9]+)r([0-9]+)c([0-9]+)");
    public static Point getPoint(String posCode, ExamSettings examSettings, ExamSettings.EXAM_FIELD_OPTION currFieldOption) {


        Matcher m = POS_CODE_PATTERN.matcher(posCode);

        if (!m.find()) {
            throw new RuntimeException("matcher not found!");
        }
        int quad = Integer.parseInt(m.group(1));
        int row = Integer.parseInt(m.group(2));
        int col = Integer.parseInt(m.group(3));

        int x = (int)(((col-1)+0.5)*examSettings.getStimulusSpacing());
        int y = (int)(((row-1)+0.5)*examSettings.getStimulusSpacing());

        if (quad == 1) {
            y = -y;
        } else if (quad == 2) {
            x = -x;
            y = -y;
        } else if (quad == 3) {
            x = -x;
        }

        x += (currFieldOption==ExamSettings.EXAM_FIELD_OPTION.LEFT)?
                examSettings.getLeftFixation().x:examSettings.getRightFixation().x;
        y += examSettings.getLeftFixation().y;
        return new Point(x, y);
    }


    public static String fromAllResponses(List<StimulusResponse> allResponses) {
        StringBuilder strBld = new StringBuilder();
        if (allResponses != null) {
            for (StimulusResponse r : allResponses) {
                strBld.append(r.getStimulusDB()).append(":").append(r.isDetected()?1:0).append(";");
            }
        }
        return strBld.toString();
    }

    public static List<StimulusResponse> toAllResponses(String allResponses) {
        List<StimulusResponse> result = new ArrayList<StimulusResponse>();
        String[] data = allResponses.split(";");
        for (String d : data) {
            String[] v = d.split(":");
            StimulusResponse r = new StimulusResponse(Integer.parseInt(v[0]), Integer.parseInt(v[1])==1?true:false);
            result.add(r);
        }

        return result;

    }
}
