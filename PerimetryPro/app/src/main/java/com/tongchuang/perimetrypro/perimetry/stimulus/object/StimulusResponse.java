package com.tongchuang.perimetrypro.perimetry.stimulus.object;

/**
 * Created by qingdi on 8/5/16.
 */
public class StimulusResponse {
    private int         stimulusDB;
    private boolean     detected;

    public StimulusResponse(int stimulusDB, boolean stimulusDetected) {
        this.stimulusDB = stimulusDB;
        this.detected = stimulusDetected;
    }

    public boolean isDetected() {
        return detected;
    }

    public void setDetected(boolean detected) {
        this.detected = detected;
    }

    public int getStimulusDB() {
        return stimulusDB;
    }

    public void setStimulusDB(int stimulusDB) {
        this.stimulusDB = stimulusDB;
    }
}
