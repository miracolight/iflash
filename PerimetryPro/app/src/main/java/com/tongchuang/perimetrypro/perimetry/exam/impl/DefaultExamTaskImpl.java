package com.tongchuang.perimetrypro.perimetry.exam.impl;

import android.graphics.Point;

import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTaskListener;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner;
import com.tongchuang.perimetrypro.perimetry.stimulus.impl.DefaultStimulusRunnerImpl;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusSelector;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings.EXAM_FIELD_OPTION;
import com.tongchuang.perimetrypro.perimetry.common.Intensity;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusInstance;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qingdi on 8/6/16.
 */
public class DefaultExamTaskImpl implements ExamTask {

    private ExamSettings                examSettings;
    private EXAM_FIELD_OPTION           currFieldOption;
    private List<Point>                 fixations;
    private int                         centerX;
    private int                         centerY;
    private int                         screenWidth;
    private Map<String, Point>          positionPoints;
    private List<StimulusRunner>        stimulusRunners;
    private StimulusRunner              blindSpotRunner;
    private int                         maxStimulusDB;
    private int                         minStimulusDB;
    private StimulusRunner              currentStimulus;
    private StimulusSelector            stimulusSelector;
    private boolean                     showStimulus = false;
    private boolean                     examDone = false;
    private ExamTask.STATE              taskState;


    private List<ExamTaskListener>      examTaskListeners;

    public DefaultExamTaskImpl(ExamSettings examSettings, ExamSettings.EXAM_FIELD_OPTION currFieldOption) {
        this.minStimulusDB = examSettings.getMinStimulusDB();
        this.maxStimulusDB = examSettings.getMaxStimulusDB();

        this.centerX = currFieldOption == ExamSettings.EXAM_FIELD_OPTION.LEFT?
                        examSettings.getLeftFixation().x:(examSettings.getRightFixation().x-examSettings.getLeftFixation().x)/2;
        this.centerY = currFieldOption == ExamSettings.EXAM_FIELD_OPTION.LEFT?
                        examSettings.getLeftFixation().y:examSettings.getRightFixation().y;

        this.fixations = Collections.singletonList(new Point(centerX, centerY));

        this.currFieldOption = currFieldOption;

        this.screenWidth = examSettings.getLeftFixation().x+examSettings.getRightFixation().x;
    }

    public StimulusSelector getStimulusSelector() {
        return stimulusSelector;
    }

    public void setStimulusSelector(StimulusSelector stimulusSelector) {
        this.stimulusSelector = stimulusSelector;
    }

    public Map<String, Point> getPositionPoints() {
        return positionPoints;
    }

    public void setPositionPoints(Map<String, Point> positionPoints) {
        this.positionPoints = positionPoints;
    }

    @Override
    public List<Point> getFixations() {
        return fixations;
    }

    @Override
    public int getStimulusRadius() {
        return examSettings.getStimulusRadius();
    }

    @Override
    public int getFixationRadius() {
        return examSettings.getFixationRadius();
    }

    @Override
    public int getTextDisplaySize() {
        return examSettings.getTextDisplaySize();
    }

    @Override
    public int getCenterX() {
        return centerX;
    }

    @Override
    public int getCenterY() {
        return centerY;
    }


    @Override
    public boolean isRunning() {
        return taskState==STATE.RUNNING;
    }

    @Override
    public boolean isDone() {
        return taskState==STATE.FINISHED;
    }

    @Override
    public void onResponse() {
        if (currentStimulus != null && (currentStimulus.isStarted()||currentStimulus.isStopped())) {
            currentStimulus.setStimulusDetected(true);
        }
    }

    public void setStimulusRunners(List<StimulusRunner> stimulusRunners) {
        this.stimulusRunners = stimulusRunners;
    }

    @Override
    public List<StimulusRunner> getStimulusRunners() {
        return stimulusRunners;
    }

    @Override
    public StimulusRunner getBlindSpotRunner() {
        return blindSpotRunner;
    }

    public void setBlindSpotRunner(StimulusRunner blindSpotRunner) {
        this.blindSpotRunner = blindSpotRunner;
    }

    public StimulusRunner getCurrentStimulusRunner() {
        return currentStimulus;
    }

    @Override
    public StimulusInstance getCurrentStimulusInstance() {
        int stimulusDB = currentStimulus.getCurrentStimulusDB();
        String positionCode = currentStimulus.getPositionCode();
        StimulusInstance instance = new StimulusInstance(positionCode, positionPoints.get(positionCode),
                examSettings.getIntensities().get(stimulusDB));

        if (instance.getIntensity() == null) {
            System.out.println("aimuLog-error: stimulusDB="+stimulusDB+" is invalid");
        }
        return instance;
    }

    @Override
    public ExamSettings getExamSettings() {
        return examSettings;
    }

    @Override
    public int getMaxStimulusDB() {
        return maxStimulusDB;
    }

    public void setMaxStimulusDB(int maxStimulusDB) {
        this.maxStimulusDB = maxStimulusDB;
    }

    @Override
    public int getMinStimulusDB() {
        return minStimulusDB;
    }

    public void setMinStimulusDB(int minStimulusDB) {
        this.minStimulusDB = minStimulusDB;
    }

    @Override
    public void run() {
        taskState = STATE.RUNNING;
        while ((currentStimulus = stimulusSelector.getNextStimulus()) != null) {
            currentStimulus.setup();
            currentStimulus.start();
            notifyStimulusChange();

            try {
                Thread.sleep(examSettings.getStimulateDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentStimulus.stop();
            notifyStimulusChange();

            try {
                Thread.sleep(examSettings.getStimulateInterval());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentStimulus.processs();
        }

        examDone = true;
        notifyExamTaskDone();


    }

    private void notifyStimulusChange() {
        if (examTaskListeners == null) {
            return;
        }
        for (ExamTaskListener l : examTaskListeners) {
            l.onStimulusChange();
        }
    }

    private void notifyExamTaskDone() {
        taskState = STATE.FINISHED;
        if (examTaskListeners == null) {
            return;
        }
        for (ExamTaskListener l : examTaskListeners) {
            l.onExamTaskDone();
        }
    }

    public void setExamSettings(ExamSettings examSettings) {
        this.examSettings = examSettings;
    }

    public void setExamTaskListeners(List<ExamTaskListener> examTaskListeners) {
        this.examTaskListeners = examTaskListeners;
    }

    public EXAM_FIELD_OPTION getCurrFieldOption() {
        return currFieldOption;
    }

    public int getScreenWidth() {
        return screenWidth;
    }
}
