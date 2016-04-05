package com.vvavy.visiondemo.app;

import com.vvavy.visiondemo.app.object.Intensity;

/**
 * Created by qingdi on 3/14/16.
 */
public class IntensityHandler {

    public static int MAX_GREY = 256;
    public static int MAX_INTENSITY = 10000;
    public static int MAX_DB = 40;
    public static int MIN_DB = 0;

    public static int INIT_DB = 4;
    public static Intensity[] ALL_INTENSITIES = generateAllIntensities();

    public static double calcIntensityDB(int alpha, int brightness) {
        double db = MAX_DB-Math.log10((brightness+1d)/MAX_GREY*(alpha+1d)/256*MAX_INTENSITY)*10;

        return db;
    }

    public static Intensity[] generateAllIntensities() {
        Intensity[] results = new Intensity[MAX_DB+1];
        for (int b = MAX_GREY-1; b >= 0; b--) {
            for (int a = MAX_GREY-1; a >=0; a--) {
                double db = calcIntensityDB(a, b);
                int dbIntLow = (int)Math.floor(db);
                int dbIntHigh = (int)Math.ceil(db);
                if (dbIntHigh>MAX_DB) {
                    System.out.println("am here:"+(new Intensity(dbIntLow, db, a, b)).toString());
                    continue;
                }
                if (results[dbIntLow] == null || results[dbIntLow].getDbDouble()>db) {
                    results[dbIntLow] = new Intensity(dbIntLow, db, a, b);
                }
                if (results[dbIntHigh] == null || results[dbIntHigh].getDbDouble()<db) {
                    results[dbIntHigh] = new Intensity(dbIntHigh, db, a, b);
                }
            }
        }

        return results;
    }


    private Intensity   intensity;
    private int         dbStep  = 4;
    private boolean     done = false;

    public IntensityHandler() {
        this.intensity = ALL_INTENSITIES[INIT_DB];
    }

    public void adjustIntensity(boolean visible) {
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
        if (dbStep == 4) {
            if (visible) {
                if (intensity.getDbInt()+dbStep>MAX_DB) {
                    intensity = ALL_INTENSITIES[MAX_DB];
                    done= true;
                } else {
                    intensity = ALL_INTENSITIES[intensity.getDbInt()+dbStep];
                }
            } else {
                dbStep = dbStep/2;
                if ((intensity.getDbInt()-dbStep)<MIN_DB) {
                    intensity = ALL_INTENSITIES[MIN_DB];
                    done = true;
                } else {
                    intensity = ALL_INTENSITIES[intensity.getDbInt()-dbStep];
                }
            }
        } else if (dbStep == 2) {
            dbStep = 1;
            if (visible) {
                if (intensity.getDbInt()+dbStep>MAX_DB) {
                    intensity = ALL_INTENSITIES[MAX_DB];
                    done= true;
                } else {
                    intensity = ALL_INTENSITIES[intensity.getDbInt()+dbStep];
                }
            } else {
                if ((intensity.getDbInt()-dbStep)<MIN_DB) {
                    intensity = ALL_INTENSITIES[MIN_DB];
                    done = true;
                } else {
                    intensity = ALL_INTENSITIES[intensity.getDbInt()-dbStep];
                }
            }
        } else if (dbStep == 1) {
            if (visible) {
                if (intensity.getDbInt()+dbStep>MAX_DB) {
                    intensity = ALL_INTENSITIES[MAX_DB];
                } else {
                    intensity = ALL_INTENSITIES[intensity.getDbInt()+dbStep];
                }
            }
            done= true;
        }
    }

    public boolean isDone() {
        return done;
    }

    public Intensity getIntensity() {
        return intensity;
    }

    public void setIntensity(Intensity intensity) {
        this.intensity = intensity;
    }
}
