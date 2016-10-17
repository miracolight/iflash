package com.tongchuang.perimetrypro.perimetry.exam;

import android.content.Context;
import android.graphics.Point;

import com.tongchuang.perimetrypro.activity.PatientMainActivity;
import com.tongchuang.perimetrypro.perimetry.exam.impl.DefaultExamTaskImpl;
import com.tongchuang.perimetrypro.perimetry.pattern.PatternGenerator;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.pattern.PatternGeneratorFactory;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusSelector;
import com.tongchuang.perimetrypro.perimetry.stimulus.impl.BlindSpotPostProcessor;
import com.tongchuang.perimetrypro.perimetry.stimulus.impl.BlindSpotStimulusRunnerImpl;
import com.tongchuang.perimetrypro.util.ExamUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qingdi on 8/5/16.
 */
public class ExamTaskBuilder {

    public static ExamTask build(Context context, ExamSettings examSettings, ExamSettings.EXAM_FIELD_OPTION currFieldOption)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        DefaultExamTaskImpl exam = new DefaultExamTaskImpl(examSettings, currFieldOption);

        int minStimulusDB = exam.getMinStimulusDB();

        PatternGenerator patternGenerator = PatternGeneratorFactory.getPatternGenerator(examSettings.getPatternType());
        String[] stimulusPositionCodes = patternGenerator.getStimulusPositionCodes(currFieldOption);
        String blindSpot = patternGenerator.getBlindSpot(currFieldOption);

        Constructor stimulusRunnerConstructor = Class.forName(examSettings.getStimulusRunnerClass())
                                        .getConstructor(String.class, int.class, ExamTask.class);

        Map<String, Integer> initStimulusDB = examSettings.getInitStimulusDB(currFieldOption);
        BlindSpotPostProcessor blindSpotPostProcessor = new BlindSpotPostProcessor(context);
        List<StimulusRunner> stimulusRunners = new ArrayList<StimulusRunner>();
        Map<String, Point> positionPoints = new HashMap<String, Point>();
        for (String code : stimulusPositionCodes) {
            Integer db = initStimulusDB.get(code);
            positionPoints.put(code, ExamUtil.getPoint(code, examSettings, currFieldOption));

            if (code.equals(blindSpot)) {
                exam.setBlindSpotRunner(new BlindSpotStimulusRunnerImpl(blindSpotPostProcessor, code, exam));
            } else {
                StimulusRunner r = (StimulusRunner)stimulusRunnerConstructor.newInstance(code, db==null?minStimulusDB:db.intValue(), exam);
                stimulusRunners.add(r);
            }
        }

        exam.setStimulusRunners(stimulusRunners);

        exam.setPositionPoints(positionPoints);
        exam.setExamSettings(examSettings);

        Constructor stimulusSelectorConstructor = Class.forName(examSettings.getStimulusSelectorClass())
                .getConstructor(ExamTask.class);
        StimulusSelector s = (StimulusSelector)stimulusSelectorConstructor.newInstance(exam);
        exam.setStimulusSelector(s);

        return exam;
    }

}
