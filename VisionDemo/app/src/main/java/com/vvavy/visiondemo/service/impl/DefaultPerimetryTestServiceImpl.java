package com.vvavy.visiondemo.service.impl;

import android.graphics.Point;

import com.vvavy.visiondemo.service.IntensityService;
import com.vvavy.visiondemo.service.PerimetryTestService;
import com.vvavy.visiondemo.object.Config;
import com.vvavy.visiondemo.object.Intensity;
import com.vvavy.visiondemo.object.PerimetryStimulus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by qingdi on 3/8/16.
 */
public class DefaultPerimetryTestServiceImpl implements PerimetryTestService {

    public static final int     RESULT_DISPLAY_SIZE = 40;
    private int                 promptTime;
    private int                 centerLeftX;
    private int                 centerRightX;
    private int                 centerY;
    private int                 radius;

    private List<Point>         fixations;
    private ExamType            examType;

    private List<PerimetryStimulus>    stimuli;
    private List<PerimetryStimulus>    resultPoints;
    private int                 currPointIndex;

    private IntensityService intensityHandler = new DefaultIntensityServiceImpl();

    public DefaultPerimetryTestServiceImpl(Config config) {
        this(config, ExamType.RIGHT);
    }

    public DefaultPerimetryTestServiceImpl(Config config, ExamType examType) {

        this.stimuli = new ArrayList<PerimetryStimulus>();
        this.stimuli.addAll(generatePointsByQuadrant(1, config.getInitDb(), config.getGap()));
        this.stimuli.addAll(generatePointsByQuadrant(2, config.getInitDb(), config.getGap()));
        this.stimuli.addAll(generatePointsByQuadrant(3, config.getInitDb(), config.getGap()));
        this.stimuli.addAll(generatePointsByQuadrant(4, config.getInitDb(), config.getGap()));

        Collections.sort(stimuli, new Comparator<PerimetryStimulus>() {
            @Override
            public int compare(PerimetryStimulus p1, PerimetryStimulus p2) {
                return ((p1.getPoint().x*p1.getPoint().x+p1.getPoint().y*p1.getPoint().y)-
                        (p2.getPoint().x*p2.getPoint().x+p2.getPoint().y*p2.getPoint().y));
            }
        });

        if (config.getNumPoints()<=stimuli.size()) {
            stimuli = stimuli.subList(0, config.getNumPoints());
        }

        this.resultPoints = new ArrayList<PerimetryStimulus>();
        this.centerLeftX = config.getCenterLeftX();
        this.centerRightX = config.getCenterRightX();
        this.centerY = config.getCenterY();
        this.radius = config.getRadius();
        this.promptTime = config.getPromptTime();
        this.examType = examType;

        this.fixations = new ArrayList<Point>();

        if (config.getNumFixations() == 1) {
            if (examType == ExamType.LEFT) {
                this.fixations.add(new Point(centerLeftX, centerY));
            } else {
                this.fixations.add(new Point(centerRightX, centerY));
            }
        } else {
            this.fixations.add(new Point(centerLeftX, centerY));
            this.fixations.add(new Point(centerRightX, centerY));
        }
    }

    private Collection<? extends PerimetryStimulus> generatePointsByQuadrant(int qIndex, int initDb, int gap) {
        List<PerimetryStimulus> stimuli = new ArrayList<PerimetryStimulus>();

        int x = gap/2, y = gap/2;

        if (qIndex == 2) {
            x = -x;
        } else if (qIndex == 3) {
            x = -x; y = -y;
        } else if (qIndex == 4) {
            y = -y;
        }

        Intensity defaultIndensity = DefaultIntensityServiceImpl.ALL_INTENSITIES[initDb];
        stimuli.add(new PerimetryStimulus(new Point(1*x, 1*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(1*x, 3*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(1*x, 5*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(1*x, 7*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(1*x, 9*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(3*x, 1*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(3*x, 3*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(3*x, 5*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(3*x, 7*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(3*x, 9*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(5*x, 1*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(5*x, 3*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(5*x, 5*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(5*x, 7*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(7*x, 1*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(7*x, 3*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(7*x, 5*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(9*x, 1*y), defaultIndensity));
        stimuli.add(new PerimetryStimulus(new Point(9*x, 3*y), defaultIndensity));

        return stimuli;
    }

    public int getCenterLeftX() {
        return centerLeftX;
    }

    public int getCenterRightX() {
        return centerRightX;
    }

    public int getCenterY() {
        return centerY;
    }


    public int getRadius() {
        return radius;
    }


    public int getPromptTime() {
        return promptTime;
    }


    public List<PerimetryStimulus> getStimuli() {
        return stimuli;
    }


    public PerimetryStimulus getCurrentStimulus() {
        if (!stimuli.isEmpty()) {
            // find the point
            currPointIndex = getRandomIndex();
            PerimetryStimulus point = stimuli.get(currPointIndex);
            return point;
        }
        return null;
    }

    private int getRandomIndex() {
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(stimuli.size());
    }

    public void processStimulus(PerimetryStimulus stimulus) {
        intensityHandler.adjustIntensity(stimulus);
        if (stimulus.isDone()) {
            stimuli.remove(currPointIndex);
            resultPoints.add(stimulus);
        }
        System.out.println("stimuli.size=" + stimuli.size());
    }

    public List<PerimetryStimulus> getExamResult() {
        return resultPoints;
    }

    @Override
    public int getStimulusX(PerimetryStimulus s) {
        return (examType == ExamType.LEFT?centerLeftX:centerRightX)+s.getPoint().x;
    }

    @Override
    public int getStimulusY(PerimetryStimulus s) {
        return centerY + s.getPoint().y;
    }

    @Override
    public List<Point> getFixations() {
        return this.fixations;
    }

    public boolean isDone() {
        return stimuli.isEmpty();
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }
}
