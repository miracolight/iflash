package com.tongchuang.perimetrypro.perimetry.exam;

import android.graphics.Point;

import com.tongchuang.perimetrypro.perimetry.exam.impl.DefaultExamTaskImpl;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.pattern.PatternGeneratorFactory;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner;
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

    public static ExamTask build(ExamSettings examSettings, List<ExamTaskListener> examTaskListeners)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        DefaultExamTaskImpl exam = new DefaultExamTaskImpl(examSettings);

        int minStimulusDB = exam.getMinStimulusDB();
        String[] stimulusPositionCodes = PatternGeneratorFactory.getPatternGenerator(examSettings.getPatternType())
                                            .getStimulusPositionCodes(examSettings.getExamFieldOption());

        Constructor stimulusRunnerConstructor = Class.forName(examSettings.getStimulusRunnerClass())
                                        .getConstructor(String.class, int.class, ExamTask.class);

        Map<String, Integer> initStimulusDB = examSettings.getInitStimulusDB();
        List<StimulusRunner> stimulusRunners = new ArrayList<StimulusRunner>();
        Map<String, Point> positionPoints = new HashMap<String, Point>();
        for (String code : stimulusPositionCodes) {
            Integer db = initStimulusDB.get(code);
            StimulusRunner r = (StimulusRunner)stimulusRunnerConstructor.newInstance(code, db==null?minStimulusDB:db.intValue(), exam);
            stimulusRunners.add(r);
            positionPoints.put(code, ExamUtil.getPoint(code, examSettings));
        }

        exam.setStimulusRunners(stimulusRunners);
        exam.setPositionPoints(positionPoints);

        exam.setExamTaskListeners(examTaskListeners);
        exam.setExamSettings(examSettings);

        return exam;
    }
}
