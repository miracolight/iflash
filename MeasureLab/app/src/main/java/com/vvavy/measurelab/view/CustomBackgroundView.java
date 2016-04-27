package com.vvavy.measurelab.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.vvavy.measurelab.object.State;

/**
 * Created by qingdi on 3/4/16.
 */
public class CustomBackgroundView extends View{


    private Paint paint = new Paint();
    private State state;

    public CustomBackgroundView(Context context, State state) {
        super(context);
        this.state = state;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // smooths
        // opacity
        //p.setAlpha(0x80); //
        // set background intensity
        setBackgroundColor(Color.argb(state.getBackAlpha(), state.getBackGrey(), state.getBackGrey(), state.getBackGrey()));

        paint.setColor(Color.argb(state.getStimulusAlpha(), state.getStimulusGrey(), state.getStimulusGrey(), state.getStimulusGrey()));
        canvas.drawCircle(20, 20, 10, paint);


        canvas.drawRect(canvas.getWidth()/2, 0, canvas.getWidth(), canvas.getHeight(), paint);
    }

}
