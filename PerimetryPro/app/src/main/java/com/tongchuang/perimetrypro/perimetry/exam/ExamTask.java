package com.tongchuang.perimetrypro.perimetry.exam;

import android.graphics.Point;

import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.common.Intensity;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusSelector;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusInstance;

import java.util.List;

/**
 * Created by qingdi on 8/5/16.
 */
public interface ExamTask extends Runnable{


    public List<Point> getFixations();

    public int getStimulusRadius();

    public int getFixationRadius();

    public int getTextDisplaySize();

    public int getCenterX();
    public int getCenterY();

    public void setExamTaskListeners(List<ExamTaskListener> examTaskListeners);

    public StimulusRunner getBlindSpotRunner();

    public StimulusRunner getFalseNegativeRunner();

    public static enum STATE {READY, RUNNING, FINISHED};

    public boolean isRunning();
    public boolean isDone();

    public void onResponse();

    public ExamSettings getExamSettings();


    public int getMaxStimulusDB();

    public int getMinStimulusDB();

    public int getProgress();

    public void setProgress(int progress);

    public int getRCounter();

    public void setRCounter(int rCounter);
    
    public List<StimulusRunner> getStimulusRunners();
    public StimulusSelector getStimulusSelector();

    public StimulusRunner getCurrentStimulusRunner();
    public StimulusInstance getCurrentStimulusInstance();

    public ExamSettings.EXAM_FIELD_OPTION getCurrFieldOption();

    public int getScreenWidth();
}
