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
 */
public class DefaultStimulusSelectorImpl implements StimulusSelector{

    private static final int MIN_RUNNER_POOL_SIZE = 5;
    private static final int TRAINING_STIMULUS_COUNT=5;
    private int currTrainingShown = 0;

    private Map<Integer, List<StimulusRunner>> stimulusRunnersByPriorities;

    private int currLevel = Integer.MAX_VALUE;
    private int maxLevel = Integer.MIN_VALUE;

    private int stimulateCount = 0;
    private int stimulateCountMax = Integer.MAX_VALUE;




    public DefaultStimulusSelectorImpl(ExamTask examTask) {
        stimulusRunnersByPriorities = new HashMap<Integer, List<StimulusRunner>>();
        ExamSettings examSettings = examTask.getExamSettings();
        Map<String, Integer> priorities = examSettings.getStimulusPriorities(examTask.getCurrFieldOption());
        List<StimulusRunner> runners = examTask.getStimulusRunners();

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
        }

        if (examSettings.getStimulateCountMax() != null) {
            stimulateCountMax = examSettings.getStimulateCountMax();
        }
    }


    @Override
    public StimulusRunner getNextStimulus() {
        StimulusRunner runner = null;

        if (currTrainingShown < TRAINING_STIMULUS_COUNT) {
            List<StimulusRunner> runners = stimulusRunnersByPriorities.get(currLevel);
            if (runners != null && !runners.isEmpty()) {
                runner = runners.get(getRandomIndex(runners.size()));
            }
            runner.setForTraining(true);
            currTrainingShown++;
            System.out.println("aimu_log: currTrainingShown="+currTrainingShown);
            return runner;
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
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(range);
    }
}
