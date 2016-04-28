package com.vvavy.visiondemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.vvavy.visiondemo.service.PerimetryTestService;
import com.vvavy.visiondemo.service.impl.DefaultPerimetryTestServiceImpl;
import com.vvavy.visiondemo.object.Config;
import com.vvavy.visiondemo.object.PerimetryStimulus;

/**
 * Created by qingdi on 3/4/16.
 */
public class ExamView extends View{


    private Paint paint = new Paint();


    private PerimetryStimulus stimulus;

    private PerimetryTestService exam;



    public ExamView(Context context, PerimetryTestService exam) {
        super(context);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        this.exam = exam;
    }
    public void setCurrentStimulus(PerimetryStimulus stimulus) {
        this.stimulus = stimulus;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // smooths
        // opacity
        //p.setAlpha(0x80); //
        if (exam.isDone()) {
            //show result;
            for (PerimetryStimulus p : exam.getExamResult()) {
                paint.setColor(Color.WHITE);
                paint.setTextSize(DefaultPerimetryTestServiceImpl.RESULT_DISPLAY_SIZE);
                canvas.drawText(Integer.toString(p.getIntensity().getDb())+", ",
                        exam.getStimulusX(p), exam.getStimulusY(p), paint);
            }
            return;
        }
        if (stimulus != null) {
            setBackgroundColor(stimulus.getBgColor());
            paint.setColor(stimulus.getStimulusColor());
            Point p = stimulus.getPoint();
            canvas.drawCircle(exam.getStimulusX(stimulus),
                    exam.getStimulusY(stimulus)+p.y, exam.getRadius(), paint);
        }
        paint.setColor(Color.RED);
        for (Point p : exam.getFixations()) {
            canvas.drawCircle(p.x, p.y, Config.getCenterRadius(), paint);
        }

    }


}
