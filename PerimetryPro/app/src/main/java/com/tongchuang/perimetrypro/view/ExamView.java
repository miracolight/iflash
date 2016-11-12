package com.tongchuang.perimetrypro.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.tongchuang.perimetrypro.activity.ExamActivity;
import com.tongchuang.perimetrypro.perimetry.exam.ExamTask;
import com.tongchuang.perimetrypro.perimetry.settings.ExamSettings;
import com.tongchuang.perimetrypro.perimetry.stimulus.StimulusRunner;
import com.tongchuang.perimetrypro.perimetry.stimulus.object.StimulusInstance;

import java.util.Iterator;
import java.util.List;

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
        int notCompleted=0;
        //int iprogress;
        int progress=0;

        ExamTask exam = examActivity.getExam();
        List<StimulusRunner> runners;

        int xPos = 0; int yPos = 70;
        if (exam.isRunning()) {
            StimulusRunner stimulusRunner = exam.getCurrentStimulusRunner();

            if (stimulusRunner.isStarted()) {
                StimulusInstance stimulus = exam.getCurrentStimulusInstance();
                setBackgroundColor(stimulus.getBackgroundColor());
                paint.setColor(stimulus.getStimulusColor());
                canvas.drawCircle(stimulus.getPoint().x,
                        stimulus.getPoint().y, exam.getStimulusRadius(), paint);
            }

            if (stimulusRunner.isStimulusDetected()) {
 //               paint.setTextSize(exam.getTextDisplaySize()*2);
 //               paint.setColor(Color.RED);
 //               canvas.drawText(stimulusRunner.getPositionCode(), xPos, yPos, paint);
               // canvas.drawText(Integer.toString(stimulusRunner.getCurrentStimulusDB()), xPos, yPos*2, paint);
 /*
                runners = exam.getStimulusRunners();
                //fullSize = exam.getExamSettings().get   //getStimulusRunners().size();

                if (runners == null || runners.isEmpty()) {
                    notCompleted = 0;
                    progress = 100;
                }
                else {
                    Iterator<StimulusRunner> it = runners.iterator();
                    while (it.hasNext()) {
                        StimulusRunner r = it.next();
                        if (!r.isFinished()) {
                            notCompleted += 1;
                        }
                    }
                   // notCompleted = runners.size();
                }

                progress = 100 - (int)((double)notCompleted/53.0 *100.0);
               // canvas.drawText(Integer.toString(notCompleted)  ,  xPos, yPos, paint);   //Integer.toString(fullSize)
              //  canvas.drawText(Integer.toString(progress) + "%" ,  xPos, yPos*2 + 10, paint);   //Integer.toString(fullSize)
                exam.setProgress(progress);
                */
            }

           // paint.setColor(Color.RED);
            paint.setColor(Color.rgb(255, 165,0));
            for (Point p : exam.getFixations()) {
                canvas.drawCircle(p.x, p.y, exam.getFixationRadius(), paint);
            }

            // draw test progress
            paint.setTextSize(exam.getTextDisplaySize()*3);
            paint.setColor(Color.RED);
            canvas.drawText("进程：" + Integer.toString(exam.getProgress()) + "%" , xPos, yPos, paint);
            canvas.drawText("视标：" + Integer.toString(exam.getRCounter()/2) + "/" + exam.getExamSettings().getStimulateCountMax() , xPos, yPos*2, paint);

        } else if (exam.isDone()) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(exam.getTextDisplaySize());
            canvas.drawText("测试结束", exam.getCenterX(), exam.getCenterY(), paint);
        }
        else {
            //paint.setColor(Color.RED);
            paint.setColor(Color.rgb(255, 165,0));
            for (Point p : exam.getFixations()) {
                canvas.drawCircle(p.x, p.y, exam.getFixationRadius(), paint);
            }
            int secondsToStart = examActivity.getSecondsToStart();
            if (secondsToStart > 0) {
                paint.setColor(Color.WHITE);
                paint.setTextSize(exam.getTextDisplaySize()*4);
                canvas.drawText(Integer.toString(secondsToStart), exam.getCenterX()-(int)(exam.getTextDisplaySize()), exam.getCenterY()-(int)(exam.getTextDisplaySize()*1.5), paint);
            }
        }
    }


}
