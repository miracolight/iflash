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
    public DefaultStimulusSelectorImpl(ExamTask examTask) {
        stimulusRunnersByPriorities = new HashMap<Integer, List<StimulusRunner>>();
        ExamSettings examSettings = examTask.getExamSettings();
        Map<String, Integer> priorities = examSettings.getStimulusPriorities();
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
    }


    @Override
    public StimulusRunner getNextStimulus() {
        StimulusRunner runner = null;
        while (currLevel <= maxLevel) {
            List<StimulusRunner> runners = stimulusRunnersByPriorities.get(currLevel);
            removeFinishedRunners(runners);
            System.out.println("aimu_log: level="+currLevel+"; runners.size()="+runners.size());
            if (runners != null && !runners.isEmpty()) {
                runner = runners.get(getRandomIndex(runners.size()));
                break;
            }
            currLevel++;
        }

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
