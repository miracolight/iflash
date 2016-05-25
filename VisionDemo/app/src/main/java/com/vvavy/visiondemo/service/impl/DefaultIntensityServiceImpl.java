package com.vvavy.visiondemo.service.impl;

import com.vvavy.visiondemo.service.IntensityService;
import com.vvavy.visiondemo.object.Intensity;
import com.vvavy.visiondemo.object.PerimetryStimulus;

/**
 * Created by qingdi on 3/14/16.
 */
public class DefaultIntensityServiceImpl implements IntensityService {

    private static final int[][] rawData = {{10, 1000, 237, 255, 255, 55},
            {11, 1000, 254, 221, 255, 55},
            {12, 1000, 252, 202, 255, 55},
            {13, 1000, 251, 186, 255, 55},
            {14, 1000, 251, 170, 255, 55},
            {15, 1000, 251, 154, 255, 55},
            {16, 1000, 250, 143, 255, 55},
            {17, 1000, 246, 134, 255, 55},
            {18, 1000, 247, 123, 255, 55},
            {19, 1000, 247, 114, 255, 55},
            {20, 1000, 246, 105, 255, 55},
            {21, 1000, 245, 98, 255, 55},
            {22, 1000, 245, 91, 255, 55},
            {23, 1000, 244, 85, 255, 55},
            {24, 1000, 244, 81, 255, 55},
            {25, 1000, 244, 76, 255, 55},
            {26, 1000, 247, 72, 255, 55},
            {27, 1000, 247, 69, 255, 55},
            {28, 1000, 209, 70, 255, 55},
            {29, 1000, 197, 67, 255, 55},
            {30, 1000, 197, 65, 255, 55},
            {31, 1000, 191, 63, 255, 55},
            {32, 1000, 191, 61, 255, 55},
            {33, 1000, 191, 59, 255, 55},
                                           /* {34,43,255,255,28,255},
                                            {35,43,215,255,28,255},
                                            {36,43,175,255,28,255},
                                            {37,43,150,255,28,255},
                                            {38,43,127,255,28,255},
                                            {39,43,107,255,28,255},
                                            {40,43,90,255,28,255}*/
                                            {34,900,245,54,249,54},
                                            {35,600,220,45,224,45},
                                            {36,900,246,54,249,54},
                                            {37,600,221,45,224,45},
                                            {38,700,236,54,238,54},
                                            {39,900,247,54,249,54},
                                            {40,600,220,43,224,45}
                                };
/*
900	245	54	249	54
600	220	45	224	45
900	246	54	249	54
600	221	45	224	45
700	236	54	238	54
900	247	54	249	54
600	220	43	224	45
 */

    public static int MAX_DB = 40;
    public static int MIN_DB = 10;

    public static int INIT_DB = 10;

    //public static Intensity[] ALL_INTENSITIES = loadIntensities();

    private Intensity[] allIntensities;

    public static Intensity DUMMY_INTENSITY = new Intensity(10, 1f, 255, 255, 255, 0);

    private String calibrationCode;

    public DefaultIntensityServiceImpl(String calibrationCode, String rawCalibration) {
        this.calibrationCode = calibrationCode;
        this.allIntensities = parseIntensities(rawCalibration);
    }

    public static Intensity[] loadIntensities() {
        Intensity[] intensities = new Intensity[MAX_DB+1];

        for (int i = 0; i < rawData.length; i++) {
            int db = rawData[i][0];
            intensities[db] = new Intensity(rawData[i][0], rawData[i][1]/1000f, rawData[i][2], rawData[i][3], rawData[i][4], rawData[i][5]);
        }
        return intensities;
    }

    public static String getDefaultCalibrationCode() {
        return "000-000";
    }

    public static String getDefaultCalibrationResult() {
        StringBuilder strBlder = new StringBuilder();
        for (int i = 0; i < rawData.length; i++) {
            strBlder.append(rawData[i][0]).append(",")
                    .append(rawData[i][1]).append(",")
                    .append(rawData[i][2]).append(",")
                    .append(rawData[i][3]).append(",")
                    .append(rawData[i][4]).append(",")
                    .append(rawData[i][5]).append(";");
        }
        return strBlder.toString();
    }

    private Intensity[] parseIntensities(String rawValues) {
        String[] rawIntensities = rawValues.split(";");
        Intensity[] intensities = new Intensity[MAX_DB+1];
        for (int i = 0; i < rawIntensities.length; i++) {
            String[] data = rawIntensities[i].split(",");
            int db = Integer.parseInt(data[0]);
            intensities[db] = new Intensity(db, Integer.parseInt(data[1])/1000f,
                    Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
        }
        return intensities;
    }

    public void adjustIntensity(PerimetryStimulus stimulus) {
        /*
         1. start with dbstep=4
            if visible, brightness+=4, till dbInt==40;
            if not visible && dbInt>=2, dbstep=2, brightness-=2; else dbInt=0, done
         2. dbstep=2
            set dbstep=1
            if visible, brightness+=1, till dbInt==40;
            if not visible && dbInt >=1, brightness-=1; else dbInt=0, done
         3. dbstep=1
            if visible, brightness+=1, till dbInt=40;
            done.
          */
        int dbStep = stimulus.getDbStep();
        Intensity intensity = stimulus.getIntensity();
        if (dbStep == 4) {
            if (stimulus.isDetected()) {
                if (intensity.getDb()+dbStep>MAX_DB) {
                    stimulus.setIntensity(allIntensities[MAX_DB]);
                    stimulus.setDone(true);
                } else {
                    intensity = allIntensities[intensity.getDb()+dbStep];
                    stimulus.setIntensity(intensity);
                }
            } else {
                dbStep = dbStep/2;
                if ((intensity.getDb()-dbStep)<MIN_DB) {
                    stimulus.setIntensity(allIntensities[MIN_DB]);
                    stimulus.setDone(true);
                } else {
                    stimulus.setIntensity(allIntensities[intensity.getDb()-dbStep]);
                }
            }
        } else if (dbStep == 2) {
            dbStep = 1;
            if (stimulus.isDetected()) {
                if (intensity.getDb()+dbStep>MAX_DB) {
                    stimulus.setIntensity(allIntensities[MAX_DB]);
                    stimulus.setDone(true);
                } else {
                    stimulus.setIntensity(allIntensities[intensity.getDb()+dbStep]);
                }
            } else {
                if ((intensity.getDb()-dbStep)<MIN_DB) {
                    stimulus.setIntensity(allIntensities[MIN_DB]);
                    stimulus.setDone(true);
                } else {
                    stimulus.setIntensity(allIntensities[intensity.getDb()-dbStep]);
                }
            }
        } else if (dbStep == 1) {
            if (stimulus.isDetected()) {
                if (intensity.getDb()+dbStep>MAX_DB) {
                    stimulus.setIntensity(allIntensities[MAX_DB]);
                } else {
                    stimulus.setIntensity(allIntensities[intensity.getDb()+dbStep]);
                }
            }
            stimulus.setDone(true);
        }
        stimulus.setDbStep(dbStep);
    }

    @Override
    public Intensity getIntensity(int db) {
        return allIntensities[db];
    }

}
