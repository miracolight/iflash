package com.tongchuang.perimetrypro.perimetry.stimulus.impl;

import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusResponse;
import com.tongchuang.perimetrypro.util.ExamUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingdi on 8/8/16.
 */
public class DefaultStimulusRunnerImpl extends AbstractStimulusRunner  {
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

    public DefaultStimulusRunnerImpl(String positionCode, int initStimulusDB, ExamTask examTask) {
        this.positionCode = positionCode;
        this.stimulusDB = initStimulusDB;
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
        if (inTrainingMode) {
            return ExamUtil.getStimulusDBForTraining(examTask, stimulusDB);
        }
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
            state = STATE.READY;
            stimulusDetected = false;
            inTrainingMode = false;
            return;
        }

        StimulusResponse lastResponse = null;
        int nextStimulusDB = stimulusDB;

        boolean finish = false;

        if (!allResponses.isEmpty()) {
            lastResponse = allResponses.get(allResponses.size()-1);
        }


        if (lastResponse == null) {
            if (stimulusDetected) {
                nextStimulusDB = stimulusDB+dbStep;
            } else {
                nextStimulusDB = stimulusDB-dbStep;
            }
            if (nextStimulusDB > examTask.getMaxStimulusDB()) {
                nextStimulusDB = examTask.getMaxStimulusDB();
                dbStep = 1;
            }
            if (nextStimulusDB < examTask.getMinStimulusDB()) {
                nextStimulusDB = examTask.getMinStimulusDB();
                dbStep = 1;
            }
        } else {
            if (dbStep==1) {
                if (stimulusDetected) {
                    if (stimulusDB == examTask.getMaxStimulusDB()) {
                        finalResult = Integer.toString(stimulusDB) + "+";
                    } else {
                        finalResult = Integer.toString(stimulusDB);
                    }
                } else {
                    if (stimulusDB == examTask.getMinStimulusDB()) {
                        finalResult = Integer.toString(stimulusDB) + "-";
                    } else {
                        finalResult = Integer.toString(stimulusDB - 1);
                    }
                }
                finish();
            } else {
                if (stimulusDetected) {
                    if (!lastResponse.isDetected()) {
                        dbStep = 1;

                    }
                    nextStimulusDB = stimulusDB+dbStep;
                    if (nextStimulusDB > examTask.getMaxStimulusDB()) {
                        nextStimulusDB = examTask.getMaxStimulusDB();
                        dbStep = 1;
                    }
                } else {
                    if (lastResponse.isDetected()) {
                        dbStep = 1;

                    }
                    nextStimulusDB = stimulusDB-dbStep;
                    if (nextStimulusDB < examTask.getMinStimulusDB()) {
                        nextStimulusDB = examTask.getMinStimulusDB();
                        dbStep = 1;
                    }
                }
            }
        }

       // System.out.println("aimu_log: posCode="+positionCode+"; stimulusDB="+stimulusDB+"; nextStimulusDB="+nextStimulusDB+"; detected="+stimulusDetected);
        allResponses.add(new StimulusResponse(stimulusDB, stimulusDetected));
        stimulusDB = nextStimulusDB;


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

    public boolean isStopped() { return state == STATE.STOPPED; }

    public STATE getState() {
        return state;
    }
}
