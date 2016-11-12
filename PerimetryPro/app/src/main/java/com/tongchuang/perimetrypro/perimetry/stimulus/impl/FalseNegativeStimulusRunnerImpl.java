package com.tongchuang.perimetrypro.perimetry.stimulus.impl;

import android.content.Context;

import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusResponse;
import com.tongchuang.perimetrypro.util.ExamUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingdi on 8/8/16.
 */
public class FalseNegativeStimulusRunnerImpl extends AbstractStimulusRunner  {

    private String      positionCode;
    private int         stimulusDB;
    private boolean     stimulusDetected;
    private ExamTask    examTask;
    private STATE       state;
    private String      finalResult;

    private List<StimulusResponse>    allResponses;
    private FalseNegativePostProcessor postProcessor;
    private StimulusRunner falseNegativeRunner;

    public FalseNegativeStimulusRunnerImpl(FalseNegativePostProcessor postProcessor, String positionCode, ExamTask examTask) {
        this.postProcessor = postProcessor;
        this.positionCode = positionCode;
        this.stimulusDB = examTask.getMinStimulusDB();
        this.examTask = examTask;
        this.state = STATE.READY;
        this.allResponses = new ArrayList<StimulusResponse>();
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

    public boolean isStimulusDetected() {
        return stimulusDetected;
    }

    @Override
    public void processs() {

        if (inTrainingMode) {
            falseNegativeRunner = examTask.getFalseNegativeRunner();
            falseNegativeRunner.getAllResponses().add(new StimulusResponse(stimulusDB, stimulusDetected));
            postProcessor.postProcess(stimulusDetected);
//            state = STATE.READY;
//            stimulusDetected = false;
//            inTrainingMode = false;
//            return;
        }


        // System.out.println("aimu_log: posCode="+positionCode+"; stimulusDB="+stimulusDB+"; nextStimulusDB="+nextStimulusDB+"; detected="+stimulusDetected);
      //  allResponses.add(new StimulusResponse(stimulusDB, stimulusDetected));



        if (state == STATE.STOPPED) {
            state = STATE.READY;
 //           stimulusDetected = false;
        }

    }

    @Override
    public void finish() {
        state = STATE.FINISHED;
    }

    @Override
    public String getFinalResult() {
        return Integer.toString(stimulusDB);
    }


    public List<StimulusResponse> getAllResponses() {
        return allResponses;
    }

    @Override
    public boolean isStarted() {
        return state == STATE.STARTED;
    }

    public boolean isStopped() { return state == STATE.STOPPED; }

    public STATE getState() {
        return state;
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
