package com.tongchuang.perimetrypro.perimetry.exam;

import android.graphics.Point;

import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.common.Intensity;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner;
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

    public static enum STATE {READY, RUNNING, FINISHED};

    public Intensity getDefaultIntensity();

    public boolean isRunning();
    public boolean isDone();

    public void onResponse();

    public ExamSettings getExamSettings();


    public int getMaxStimulusDB();

    public int getMinStimulusDB();
    
    public List<StimulusRunner> getStimulusRunners();

    public StimulusRunner getCurrentStimulusRunner();
    public StimulusInstance getCurrentStimulusInstance();
}
