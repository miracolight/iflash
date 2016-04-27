package com.vvavy.visiondemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.WindowManager;

import com.vvavy.visiondemo.app.exam.PerimetryExam;
import com.vvavy.visiondemo.app.object.Config;
import com.vvavy.visiondemo.app.object.PerimetryStimulus;

/**
 * Created by qingdi on 3/4/16.
 */
public class ConfigView extends View{


    private Paint paint = new Paint();

    private PerimetryExam   exam;

    public ConfigView(Context context, PerimetryExam exam) {
        super(context);
        this.exam = exam;
    }

    public void setExam(PerimetryExam exam) {
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

}
