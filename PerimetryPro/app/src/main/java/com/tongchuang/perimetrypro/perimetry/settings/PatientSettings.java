package com.tongchuang.perimetrypro.perimetry.settings;

import android.graphics.Point;

import com.tongchuang.perimetrypro.perimetry.common.Intensity;
import com.tongchuang.perimetrypro.perimetry.pattern.PatternGenerator.PatternType;

import java.util.Map;

/**
 * Created by qingdi on 8/7/16.
 */
public class PatientSettings {
    private Map<String, Integer> initStimulusDB;
    private Map<String, Integer> stimulusPriorities;

    private Integer     stimulateDuration;
    private Integer     stimulateInterval;

    private Integer     stimulusSpacing;
    private Integer     stimulusRadius;
    private Integer     fixationRadius;
    private Point       leftFixation;
    private Point       rightFixation;

    private String      stimulusSelectorClass;
    private String      stimulusHandlerClass;

    private PatternType patternType;

    public Integer getFixationRadius() {
        return fixationRadius;
    }

    public void setFixationRadius(Integer fixationRadius) {
        this.fixationRadius = fixationRadius;
    }

    public Map<String, Integer> getInitStimulusDB() {
        return initStimulusDB;
    }

    public void setInitStimulusDB(Map<String, Integer> initStimulusDB) {
        this.initStimulusDB = initStimulusDB;
    }

    public Point getLeftFixation() {
        return leftFixation;
    }

    public void setLeftFixation(Point leftFixation) {
        this.leftFixation = leftFixation;
    }

    public Point getRightFixation() {
        return rightFixation;
    }

    public void setRightFixation(Point rightFixation) {
        this.rightFixation = rightFixation;
    }

    public Integer getStimulateDuration() {
        return stimulateDuration;
    }

    public void setStimulateDuration(Integer stimulateDuration) {
        this.stimulateDuration = stimulateDuration;
    }

    public Integer getStimulateInterval() {
        return stimulateInterval;
    }

    public void setStimulateInterval(Integer stimulateInterval) {
        this.stimulateInterval = stimulateInterval;
    }

    public Integer getStimulusRadius() {
        return stimulusRadius;
    }

    public void setStimulusRadius(Integer stimulusRadius) {
        this.stimulusRadius = stimulusRadius;
    }

    public Integer getStimulusSpacing() {
        return stimulusSpacing;
    }

    public void setStimulusSpacing(Integer stimulusSpacing) {
        this.stimulusSpacing = stimulusSpacing;
    }

    public String getStimulusHandlerClass() {
        return stimulusHandlerClass;
    }

    public void setStimulusHandlerClass(String stimulusHandlerClass) {
        this.stimulusHandlerClass = stimulusHandlerClass;
    }

    public String getStimulusSelectorClass() {
        return stimulusSelectorClass;
    }

    public void setStimulusSelectorClass(String stimulusSelectorClass) {
        this.stimulusSelectorClass = stimulusSelectorClass;
    }

    public PatternType getPatternType() {
        return patternType;
    }

    public void setPatternType(PatternType patternType) {
        this.patternType = patternType;
    }

    public Map<String, Integer> getStimulusPriorities() {
        return stimulusPriorities;
    }

    public void setStimulusPriorities(Map<String, Integer> stimulusPriorities) {
        this.stimulusPriorities = stimulusPriorities;
    }
}
