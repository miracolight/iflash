package com.tongchuang.perimetrypro.perimetry.settings;

import android.graphics.Point;

import com.tongchuang.perimetrypro.perimetry.common.Intensity;
import com.tongchuang.perimetrypro.perimetry.pattern.PatternGenerator;

import java.util.Map;

/**
 * Created by qingdi on 8/6/16.
 */
public class ExamSettings {


    public static enum EXAM_FIELD_OPTION {LEFT, RIGHT, BOTH};

    private DeviceSettings  deviceSettings;
    private PatientSettings patientSettings;

    private Integer         minStimulusDB;
    private Integer         maxStimulusDB;


    public ExamSettings(DeviceSettings deviceSettings, PatientSettings patientSettings) {
        this.deviceSettings = deviceSettings;
        this.patientSettings = patientSettings;

        minStimulusDB = Integer.MAX_VALUE;
        maxStimulusDB = Integer.MIN_VALUE;
        for (Integer db : deviceSettings.getIntensities().keySet()) {
            if (db < minStimulusDB) {
                minStimulusDB = db;
            }
            if (db > maxStimulusDB) {
                maxStimulusDB = db;
            }
        }
    }

    public Intensity getDefaultIntensity() {
        return deviceSettings.getIntensities().get(maxStimulusDB);
    }

    public String getDeviceSettingsVersion() {
        return deviceSettings.getVersion();
    }

    public String getPatientSettingsVersion() {
        return patientSettings.getVersion();
    }

    public Integer getFixationRadius() {
        return patientSettings.getFixationRadius()!=null ?
                patientSettings.getFixationRadius():deviceSettings.getFixationRadius();
    }

    public Map<Integer, Intensity> getIntensities() {
        return deviceSettings.getIntensities();
    }

    public Point getLeftFixation() {
        return deviceSettings.getLeftFixation();
    }

    public Point getRightFixation() {
        return deviceSettings.getRightFixation();
    }

    public Integer getStimulateDuration() {
        return deviceSettings.getStimulateDuration();
    }

    public Integer getStimulateInterval() {
        return deviceSettings.getStimulateInterval();
    }

    public Integer getStimulusRadius() {
        return deviceSettings.getStimulusRadius();
    }

    public Integer getStimulusSpacing() {
        return deviceSettings.getStimulusSpacing();
    }

    public Map<String, Integer> getInitStimulusDB(EXAM_FIELD_OPTION examFieldOption) {
        return examFieldOption==EXAM_FIELD_OPTION.LEFT?
                patientSettings.getInitStimulusDBLeft():patientSettings.getInitStimulusDBRight();
    }

    public String getStimulusRunnerClass() {
        return patientSettings.getStimulusRunnerClass();
    }

    public String getStimulusSelectorClass() {
        return patientSettings.getStimulusSelectorClass();
    }

    public PatternGenerator.PatternType getPatternType() {
        return patientSettings.getPatternType();
    }

    public Map<String, Integer> getStimulusPriorities(EXAM_FIELD_OPTION examFieldOption) {
        return examFieldOption==EXAM_FIELD_OPTION.LEFT?
                patientSettings.getStimulusPrioritiesLeft():patientSettings.getStimulusPrioritiesRight();
    }

    public int getTextDisplaySize() {
        return deviceSettings.getTextDisplaySize();
    }


    public EXAM_FIELD_OPTION getExamFieldOption() {
        return patientSettings.getExamFieldOption();
    }

    public Integer getMaxStimulusDB() {
        return maxStimulusDB;
    }

    public Integer getMinStimulusDB() {
        return minStimulusDB;
    }

    public Integer getStimulateCountMax() {
        return patientSettings.getStimulateCountMax();
    }
}
