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
        while (currLevel <= maxLevel && stimulateCount < stimulateCountMax) {
            List<StimulusRunner> runners = stimulusRunnersByPriorities.get(currLevel);
            removeFinishedRunners(runners);
            if (runners != null && !runners.isEmpty()) {
                runner = runners.get(getRandomIndex(runners.size()));
                System.out.println("aimu_log: level="+currLevel+"; runners.size()="+runners.size()+"; poscode="+runner.getPositionCode());
                break;
            }
            currLevel++;
        }
        stimulateCount++;
        return runner;
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
