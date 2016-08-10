package com.tongchuang.perimetrypro.perimetry.exam.impl;

import android.graphics.Point;

import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTaskListener;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner;
import com.tongchuang.perimetrypro.perimetry.stimulus.impl.DefaultStimulusRunnerImpl;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusSelector;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
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
    private List<Point>                 fixations;
    private int                         centerX;
    private int                         centerY;
    private Map<String, Point>          positionPoints;
    private List<StimulusRunner>        stimulusRunners;
    private int                         maxStimulusDB;
    private int                         minStimulusDB;
    private StimulusRunner              currentStimulus;
    private StimulusSelector            stimulusSelector;
    private boolean                     showStimulus = false;
    private boolean                     examDone = false;
    private ExamTask.STATE              taskState;


    private List<ExamTaskListener>      examTaskListeners;

    public DefaultExamTaskImpl(ExamSettings examSettings) {
        minStimulusDB = Integer.MAX_VALUE;
        maxStimulusDB = Integer.MIN_VALUE;
        for (Integer db : examSettings.getIntensities().keySet()) {
            if (db < minStimulusDB) {
                minStimulusDB = db;
            }
            if (db > maxStimulusDB) {
                maxStimulusDB = db;
            }
        }

        fixations = Collections.singletonList(examSettings.getExamFieldOption()== ExamSettings.EXAM_FIELD_OPTION.LEFT?
                                examSettings.getLeftFixation():examSettings.getRightFixation());
        centerX = (examSettings.getLeftFixation().x+examSettings.getRightFixation().x)/2;
        centerY = (examSettings.getLeftFixation().y+examSettings.getRightFixation().y)/2;
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
    public Intensity getDefaultIntensity() {
        return null;
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
        if (currentStimulus != null && currentStimulus.isStarted()) {
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
    public StimulusInstance getCurrentStimulusInstance() {
        int stimulusDB = currentStimulus.getCurrentStimulusDB();
        String positionCode = currentStimulus.getPositionCode();
        StimulusInstance instance = new StimulusInstance(positionCode, positionPoints.get(positionCode),
                examSettings.getIntensities().get(stimulusDB));
        return instance;
    }

    @Override
    public ExamSettings getExamSettings() {
        return null;
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
}
