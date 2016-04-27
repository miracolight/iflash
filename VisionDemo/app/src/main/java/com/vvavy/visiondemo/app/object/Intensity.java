package com.vvavy.visiondemo.app.object;

/**
 * Created by qingdi on 3/14/16.
 */
public class Intensity {
    public static final Intensity MAX_INTENSITY = new Intensity(0, 1f, 0, 255, 0, 0);
    private int     db;
    private float   screenBrightness;
    private int     stimulusAlpha;
    private int     stimulusGreyscale;
    private int     bgAlpha;
    private int     bgGreyscale;

    public Intensity(int db, float screenBrightness, int stimulusAlpha, int stimulusGreyscale, int bgAlpha, int bgGreyscale) {
        this.db = db;
        this.screenBrightness = screenBrightness;
        this.stimulusAlpha = stimulusAlpha;
        this.stimulusGreyscale = stimulusGreyscale;
        this.bgAlpha = bgAlpha;
        this.bgGreyscale = bgGreyscale;
    }

    public int getDb() {
        return db;
    }

    public float getScreenBrightness() {
        return screenBrightness;
    }

    public int getStimulusAlpha() {
        return stimulusAlpha;
    }

    public int getStimulusGreyscale() {
        return stimulusGreyscale;
    }

    public int getBgAlpha() {
        return bgAlpha;
    }

    public int getBgGreyscale() {
        return bgGreyscale;
    }
}
