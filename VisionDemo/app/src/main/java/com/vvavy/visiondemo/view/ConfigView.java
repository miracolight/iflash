package com.vvavy.visiondemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.vvavy.visiondemo.app.PerimetryExam;
import com.vvavy.visiondemo.app.object.Config;
import com.vvavy.visiondemo.app.object.PerimetryPoint;

/**
 * Created by qingdi on 3/4/16.
 */
public class ConfigView extends View{


    private Paint paint = new Paint();

    private PerimetryExam exam;

    public ConfigView(Context context, PerimetryExam exam) {
        super(context);
        this.exam = exam;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // smooths
        // opacity
        //p.setAlpha(0x80); //

        for (PerimetryPoint p : exam.getPoints()) {
            paint.setColor(p.getColor());
            canvas.drawCircle(exam.getCenterRightX()+p.getPoint().x*exam.getGap(),
                        exam.getCenterY()+p.getPoint().y*exam.getGap(),
                        exam.getRadius(), paint);
        }

        paint.setColor(Color.RED);
        canvas.drawCircle(exam.getCenterLeftX(), exam.getCenterY(), Config.CENTER_RADIUS, paint);
        canvas.drawCircle(exam.getCenterRightX(), exam.getCenterY(), Config.CENTER_RADIUS, paint);
    }

}
