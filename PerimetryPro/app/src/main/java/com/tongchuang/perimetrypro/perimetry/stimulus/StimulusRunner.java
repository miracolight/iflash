package com.tongchuang.perimetrypro.perimetry.stimulus;

import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusResponse;

import java.util.List;

/**
 * Created by qingdi on 8/9/16.
 */
public interface StimulusRunner {
    public String getFinalResult();
    public List<StimulusResponse> getAllResponses();

    public boolean isStarted();
    public boolean isStopped();

    public static enum STATE {READY, STARTED, STOPPED, FINISHED};

    public boolean isFinished();

    public String getPositionCode();

    public int getCurrentStimulusDB();
    public void setup();

    public void start();

    public void stop();

    public void setStimulusDetected(boolean stimulusDetected);
    public boolean isStimulusDetected();

    public void processs();

    public void finish();

    public STATE getState() ;
}
