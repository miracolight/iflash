package com.tongchuang.perimetrypro.perimetry.stimulus;

import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusResponse;

import java.util.List;

/**
 * Created by qingdi on 8/9/16.
 */
public interface StimulusRunner {
    String getFinalResult();
    public List<StimulusResponse> getAllResponses();

    boolean isStarted();

    public static enum STATE {READY, STARTED, STOPPED, FINISHED};

    boolean isFinished();

    String getPositionCode();

    public int getCurrentStimulusDB();
    void setup();

    void start();

    void stop();

    void setStimulusDetected(boolean stimulusDetected);

    void processs();

    void finish();
}
