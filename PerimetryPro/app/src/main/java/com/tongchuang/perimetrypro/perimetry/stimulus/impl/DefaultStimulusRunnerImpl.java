package com.tongchuang.perimetrypro.perimetry.stimulus.impl;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import com.tongchuang.perimetrypro.R;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusResponse;
import com.tongchuang.perimetrypro.util.ExamUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingdi on 8/8/16.
 * Updated by Ming on 11/8/2016  step size, false negative...
 */
public class DefaultStimulusRunnerImpl extends AbstractStimulusRunner  {
    @Override
    public boolean isFinished() {
        return state == STATE.FINISHED;
    }

    public static int INIT_DB_STEP = 4;
    private String      positionCode;
    private int         stimulusDB;
    private boolean     stimulusDetected;
    private ExamTask    examTask;
    private STATE       state;
    private int         dbStep;
    private String      finalResult;
    private StimulusRunner falseNegativeRunner;
  //  private FalseNegativePostProcessor postProcessor;
    private List<StimulusResponse>    allResponses;

    private SoundPool soundPool;
    private int         sourceId;
    Context context;

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
/*            state = STATE.READY;
            stimulusDetected = false;
            inTrainingMode = false;

  */
            falseNegativeRunner = examTask.getFalseNegativeRunner();
            falseNegativeRunner.getAllResponses().add(new StimulusResponse(stimulusDB, stimulusDetected));
            state = STATE.READY;
            inTrainingMode = false;
 //            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 5);
   //         sourceId = soundPool.load(context, R.raw.warning, 0);
     //       if (!stimulusDetected) {
       //         int playId = soundPool.play(sourceId, 2, 2, 0, 0, 1);
         //       System.out.println("aimu_log: soundPool playId="+playId);
        //    }

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
                dbStep = 2;  // 1;
            }
            if (nextStimulusDB < examTask.getMinStimulusDB()) {
                nextStimulusDB = examTask.getMinStimulusDB();
                dbStep = 2;  // 1;
            }
        } else {
            if (dbStep==2) {
                if (stimulusDetected) {
                    if (stimulusDB == examTask.getMaxStimulusDB()) {
                        finalResult = Integer.toString(stimulusDB) + "+";
                    } else {
                        finalResult = Integer.toString(stimulusDB + 1);  // +1 because step is changed to 4 / 2
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
                        dbStep = 2;  // 1;

                    }
                    nextStimulusDB = stimulusDB+dbStep;
                    if (nextStimulusDB > examTask.getMaxStimulusDB()) {
                        nextStimulusDB = examTask.getMaxStimulusDB();
                        dbStep = 2;  // 1;
                    }
                } else {
                    if (lastResponse.isDetected()) {
                        dbStep = 2;  // 1;

                    }
                    nextStimulusDB = stimulusDB-dbStep;
                    if (nextStimulusDB < examTask.getMinStimulusDB()) {
                        nextStimulusDB = examTask.getMinStimulusDB();
                        dbStep = 2;  // 1;
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
