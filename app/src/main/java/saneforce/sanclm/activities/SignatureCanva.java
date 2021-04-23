package saneforce.sanclm.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SignatureCanva extends View {
    Paint paint;
    Path path;
    int count=0;
    public SignatureCanva(Context context) {
        super(context);
        init();
    }

    public SignatureCanva(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public SignatureCanva(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        path = new Path();
        paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5);

        //this.setBackgroundColor(Color.TRANSPARENT);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path,paint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                FeedbackActivity.clearLay();
                path.moveTo(event.getX(),event.getY());

                break;

            case MotionEvent.ACTION_MOVE:
                ++count;
                Log.v("final_count","incanva"+count);
                path.lineTo(event.getX(),event.getY());

                break;
                case MotionEvent.ACTION_UP:
                    break;


        }
        invalidate();
        return true;
    }

    public boolean checkingForSign(){
        if(count>0)
        return true;
        else
            return false;
    }

}
