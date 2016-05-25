package com.vvavy.visiondemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.vvavy.visiondemo.object.Intensity;
import com.vvavy.visiondemo.service.PerimetryTestService;
import com.vvavy.visiondemo.object.Config;
import com.vvavy.visiondemo.object.PerimetryStimulus;

/**
 * Created by qingdi on 3/4/16.
 */
public class ConfigView extends View{


    private Paint paint = new Paint();

    private PerimetryTestService exam;

    public ConfigView(Context context, PerimetryTestService exam) {
        super(context);
        this.exam = exam;
    }

    public void setExam(PerimetryTestService exam) {
        this.exam = exam;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // smooths
        // opacity
        //p.setAlpha(0x80); //
        setBackgroundColor(exam.getStimuli().get(0).getBgColor());
        for (PerimetryStimulus p : exam.getStimuli()) {
            paint.setColor(p.getStimulusColor());
            canvas.drawCircle(exam.getCenterRightX()+p.getPoint().x,
                        exam.getCenterY()+p.getPoint().y,
                        exam.getRadius(), paint);
        }

        paint.setColor(Color.RED);
        canvas.drawCircle(exam.getCenterLeftX(), exam.getCenterY(), Config.CENTER_RADIUS, paint);
        canvas.drawCircle(exam.getCenterRightX(), exam.getCenterY(), Config.CENTER_RADIUS, paint);
    }

    public String getCurrentDBValue() {
        StringBuilder strBlder = new StringBuilder();
        Intensity i = exam.getStimuli().get(0).getIntensity();
        strBlder.append("Screen:").append(i.getScreenBrightness()).append("; ")
                .append("Bg:").append(i.getBgAlpha()).append(",").append(i.getBgGreyscale()).append("; ")
                .append("Stimuli:").append(i.getStimulusAlpha()).append(",").append(i.getStimulusGreyscale()).append("; ");
        return strBlder.toString();
    }
}
