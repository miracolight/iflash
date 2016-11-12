package com.tongchuang.perimetrypro.perimetry.stimulus.impl;

import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by qingdi on 8/8/16.
 * Updated by Ming on Nov, 2016  //adding false negative and positive stimulus
 */
public class DefaultStimulusSelectorImplold implements StimulusSelector {

    private static final int MIN_RUNNER_POOL_SIZE = 5;
    private static final int TRAINING_STIMULUS_COUNT = 5; //5;
    private int currTrainingShown = 0;

    private Map<Integer, List<StimulusRunner>> stimulusRunnersByPriorities;
    private Map<Integer, List<StimulusRunner>> stimulusRunnersAll;

    private StimulusRunner blindSpotRunner;
    private StimulusRunner falseNegativeRunner;
    private int iprogress;
    //private boolean     showBlindSpot;
    private double      blindSpotWeight = 0.05d;  //blind spot is testing false positive at the same stimulus (i.e. blind spot)

    //private boolean    showFalsePositive;
    private double     falseNegativeWeight = 0.95d; /// 0.95d; testing, acutal value should be 0.95d (for 5%)

    private int currLevel = Integer.MAX_VALUE;
    private int maxLevel = Integer.MIN_VALUE;

    private int stimulateCount = 0;
    private int stimulateCountMax = Integer.MAX_VALUE;

    private Random blindSpotCheckRandom = new Random();
    private Random runnerRandom = new Random();



    public DefaultStimulusSelectorImplold(ExamTask examTask) {
        stimulusRunnersByPriorities = new HashMap<Integer, List<StimulusRunner>>();
        stimulusRunnersAll = new HashMap<Integer, List<StimulusRunner>>();
        ExamSettings examSettings = examTask.getExamSettings();
        Map<String, Integer> priorities = examSettings.getStimulusPriorities(examTask.getCurrFieldOption());
        List<StimulusRunner> runners = examTask.getStimulusRunners();

        blindSpotRunner = examTask.getBlindSpotRunner();
        falseNegativeRunner = examTask.getFalseNegativeRunner();
        //  iprogress = examTask.getProgress();
        //int r, a;

//        stimulusRunnersAll.put(1, examTask.getStimulusRunners());

        for (StimulusRunner r : runners) {
            Integer priority = priorities.get(r.getPositionCode());
            if (priority == null) {
                priority = 1;
            }
            if (priority < currLevel) {
                currLevel = priority;
            } else if (priority > maxLevel) {
                maxLevel = priority;
            }
            List<StimulusRunner> rList = stimulusRunnersByPriorities.get(priority);

            if (rList == null) {
                rList = new ArrayList<StimulusRunner>();
                stimulusRunnersByPriorities.put(priority, rList);
            }
            rList.add(r);

            List<StimulusRunner> aList = stimulusRunnersAll.get(1);
            if(aList == null) {
                aList = new ArrayList<StimulusRunner>();
                stimulusRunnersAll.put(1, aList);
            }
            aList.add(r);
        }
        if (examSettings.getStimulateCountMax() != null) {
            stimulateCountMax = examSettings.getStimulateCountMax();
        }
    }

    @Override
    public void updateCurrentProgress (ExamTask examTask) {
        int iProgress = examTask.getProgress();
        List<StimulusRunner> runners = stimulusRunnersByPriorities.get(currLevel);
        removeFinishedRunners(runners);
        List<StimulusRunner> runnersAll = stimulusRunnersAll.get(1);
        if (runners != null && !runners.isEmpty()) {
            iProgress = (int) ((double) runners.size() / (double) (runnersAll.size()) * 100);
        }
        examTask.setProgress (iProgress);
    }

    @Override
    public StimulusRunner getNextStimulus() {

        StimulusRunner runner = null;
        if (currTrainingShown < TRAINING_STIMULUS_COUNT) {
            List<StimulusRunner> runners = stimulusRunnersByPriorities.get(currLevel);
            if (runners != null && !runners.isEmpty()) {
                falseNegativeRunner = runners.get(getRandomIndex(runners.size()));
            }
            falseNegativeRunner.setForTraining(true);
            currTrainingShown++;
            System.out.println("aimu_log: currTrainingShown="+currTrainingShown);

            return falseNegativeRunner;
        }

        double r = blindSpotCheckRandom.nextDouble();   // didn't change name when adding false positive
        System.out.println("aimu_log: showSpot random = "+r);
        if (r < blindSpotWeight) {
            System.out.println("aimu_log: showBlindSpot = true");
            return blindSpotRunner;
        }
        if (r> falseNegativeWeight){
            System.out.println("aimu_log: showFalseSpot = true");
            // return;
            //List<StimulusRunner> temp =
            List<StimulusRunner> runnersAll = stimulusRunnersAll.get(1);  //get(currLevel);
            //removeFinishedRunners(runners);
            if (runnersAll != null && !runnersAll.isEmpty()) {
                falseNegativeRunner = runnersAll.get(getRandomIndex(runnersAll.size()));
                System.out.println("aimu_log:  false Negative runners.size()="+runnersAll.size()+"; poscode="+falseNegativeRunner.getPositionCode());
            }
            falseNegativeRunner.setForTraining(true);

            //System.out.println("aimu_log: currTrainingShown="+currTrainingShown);
            return falseNegativeRunner;
        }

        while (currLevel <= maxLevel && stimulateCount < stimulateCountMax) {
            List<StimulusRunner> runners = stimulusRunnersByPriorities.get(currLevel);
            removeFinishedRunners(runners);

            if (runners != null && !runners.isEmpty()) {

                if (runners.size() < MIN_RUNNER_POOL_SIZE) {
                    List<StimulusRunner> restAvailableRunners = getRestAvailableRunners(currLevel+1);
                    int index = getRandomIndex(Math.min(MIN_RUNNER_POOL_SIZE, runners.size()+restAvailableRunners.size()));
                    if (index < runners.size()) {
                        runner = runners.get(index);
                    } else {
                        runner = restAvailableRunners.get(getRandomIndex(restAvailableRunners.size()));
                    }
                } else {
                    runner = runners.get(getRandomIndex(runners.size()));
                }
                System.out.println("aimu_log: level="+currLevel+"; runners.size()="+runners.size()+"; poscode="+runner.getPositionCode());


                break;
            }
            currLevel++;
        }
        stimulateCount++;
        return runner;
    }

    private List<StimulusRunner> getRestAvailableRunners(int currLevel) {
        List<StimulusRunner> runners = new ArrayList<StimulusRunner>();
        while (currLevel <= maxLevel) {
            runners.addAll(stimulusRunnersByPriorities.get(currLevel));
            currLevel++;
        }
        removeFinishedRunners(runners);
        return runners;
    }

    private void removeFinishedRunners(List<StimulusRunner> runners) {
        if (runners == null || runners.isEmpty()) {
            return;
        }
        Iterator<StimulusRunner> it = runners.iterator();
        while (it.hasNext()) {
            StimulusRunner r = it.next();
            if (r.isFinished()) {
                it.remove();
            }
        }
    }

    private int getRandomIndex(int range) {
        return runnerRandom.nextInt(range);
    }
}
