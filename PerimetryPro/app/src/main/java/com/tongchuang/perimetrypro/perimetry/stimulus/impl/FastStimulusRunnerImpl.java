package com.tongchuang.perimetrypro.perimetry.stimulus.impl;

import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingdi on 8/8/16.
 */
public class FastStimulusRunnerImpl implements com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner {
    @Override
    public boolean isFinished() {
        return state == STATE.FINISHED;
    }

    public static int INIT_DB_STEP = 2;
    private String      positionCode;
    private int         stimulusDB;
    private boolean     stimulusDetected;
    private ExamTask    examTask;
    private STATE       state;
    private int         dbStep;
    private String      finalResult;

    private List<StimulusResponse>    allResponses;

    public FastStimulusRunnerImpl(String positionCode, int initStimulusDB, ExamTask examTask) {
        this.positionCode = positionCode;
        this.stimulusDB = initStimulusDB+1;
        if (stimulusDB>examTask.getMaxStimulusDB()) {
            stimulusDB = examTask.getMaxStimulusDB();
        }
        this.examTask = examTask;
        this.state = STATE.READY;
        this.allResponses = new ArrayList<StimulusResponse>();
        this.dbStep = INIT_DB_STEP;
    }

    @Override
    public String getPositionCode() {
        return positionCode;
    }

    @Override
    public int getCurrentStimulusDB() {
        return stimulusDB;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    @Override
    public void setup() {
        return;
    }

    @Override
    public void start() {
        state = STATE.STARTED;
    }

    @Override
    public void stop() {
        state = STATE.STOPPED;
    }

    @Override
    public void setStimulusDetected(boolean stimulusDetected) {
        this.stimulusDetected = stimulusDetected;
    }

    @Override
    public void processs() {

        allResponses.add(new StimulusResponse(stimulusDB, stimulusDetected));

        if (stimulusDetected) {
            finalResult = Integer.toString(stimulusDB-1);
            finish();
        } else {
            stimulusDB = stimulusDB-1;
            if (stimulusDB < examTask.getMinStimulusDB()) {
                finalResult = Integer.toString(examTask.getMinStimulusDB());
                finish();
            }
        }

        if (state == STATE.STOPPED) {
            state = STATE.READY;
            stimulusDetected = false;
        }
    }

    @Override
    public void finish() {
        state = STATE.FINISHED;
    }

    @Override
    public String getFinalResult() {
        return finalResult==null?(stimulusDB+"?"):finalResult;
    }


    public List<StimulusResponse> getAllResponses() {
        return allResponses;
    }

    @Override
    public boolean isStarted() {
        return state == STATE.STARTED;
    }
}
