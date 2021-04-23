package saneforce.sanclm.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PaintDraw extends View {
    Paint paint;
    Path path;
    ArrayList<Path> mPath=new ArrayList<>();
    ArrayList<Paint> mPaint=new ArrayList<>();
    public static int paintColor=Color.BLACK;
    boolean isRect=false;
    float startX,startY,endX,endY;
    public PaintDraw(Context context) {
        super(context);
        init();
    }

    public PaintDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public PaintDraw(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        path = new Path();
        paint=new Paint();
        paint.setColor(paintColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeWidth(5);

        //this.setBackgroundColor(Color.TRANSPARENT);

    }

    public void drawpath(Canvas canvas){
        for(int i=0;i<mPath.size();i++){
            canvas.drawPath(mPath.get(i),mPaint.get(i));
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
       canvas.drawColor(Color.WHITE);
        drawpath(canvas);
        if(isRect){
            paintRect(canvas);
        }
        else
        canvas.drawPath(path,paint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        
        switch (event.getAction() & MotionEvent.ACTION_MASK) {



            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(),event.getY());
                startX=event.getX();
                startY=event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(),event.getY());
                endX=event.getX();
                endY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if(!isRect){
                mPath.add(path);
                mPaint.add(paint);
                }
                init();
                break;


        }
        invalidate();
        return true;
    }

    public void changePaintColor(){
        paintColor=Color.RED;
        init();
        invalidate();
    }



    public void paintRect(Canvas _canvas) {
        try {
            _canvas.drawRect(startX, startY, endX, endY, paint);
            _canvas.drawRect(endX, startY, startX, endY, paint);
            _canvas.drawRect(endX, endY, startX, startY, paint);
            _canvas.drawRect(startX, endY, endX, startY, paint);
        } catch (Exception e) {
        }
        invalidate();
    }

    public void changeRect(){
        isRect=true;
    }

}
