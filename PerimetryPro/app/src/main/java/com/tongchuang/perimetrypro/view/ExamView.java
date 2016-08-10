package com.tongchuang.perimetrypro.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.tongchuang.perimetrypro.activity.ExamActivity;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusInstance;

/**
 * Created by qingdi on 3/4/16.
 */
public class ExamView extends View{


    private Paint paint = new Paint();

    private ExamActivity    examActivity;


    public ExamView(ExamActivity examActivity) {
        super(examActivity);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        this.examActivity = examActivity;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        ExamTask exam = examActivity.getExam();
        if (exam.isRunning()) {
            StimulusInstance stimulus = exam.getCurrentStimulusInstance();
            setBackgroundColor(stimulus.getBackgroundColor());
            paint.setColor(stimulus.getStimulusColor());
            canvas.drawCircle(stimulus.getPoint().x,
                    stimulus.getPoint().y, exam.getStimulusRadius(), paint);

            paint.setColor(Color.RED);
            for (Point p : exam.getFixations()) {
                canvas.drawCircle(p.x, p.y, exam.getFixationRadius(), paint);
            }
        } else if (exam.isDone()) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(exam.getTextDisplaySize());
            canvas.drawText("测试结束", exam.getCenterX(), exam.getCenterY(), paint);
        }
        else {
            int secondsToStart = examActivity.getSecondsToStart();
            if (secondsToStart > 0) {
                paint.setColor(Color.WHITE);
                paint.setTextSize(exam.getTextDisplaySize());
                canvas.drawText(Integer.toString(secondsToStart), exam.getCenterX(), exam.getCenterY(), paint);
            }
        }
    }

}
