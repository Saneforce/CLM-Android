
package saneforce.sanclm.activities;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class CustomShape extends LinearLayout {


    private Path hexagonPath;
    private Path hexagonBorderPath;
    private float radius;
    private float width, height;
    private int maskColor;
    private int transparent_color=getResources().getColor(android.R.color.transparent);

    static int shapeCount=0;
    ImageView img;
    float width1, height1;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;
    private ImageView view1, fin;
    private Bitmap bmap;

    Path newPath=new Path();
    RectF rectF;


    int currentAngle=0;
    boolean firstCircle=false;
    boolean secondCircle=false;
    boolean thridCircle=false;
    int startAngles=0;
    int callArc=0;
    int secondAngle,thirdAngle,firstAngle;
    int firstStartAngle=0,secondStartAngle,thirdStartAngle;

    int txtFx,txtFy,txtSx,txtSy,txtTx,txtTy;
    int txtCount;
    ArrayList<Integer> txtxPoint=new ArrayList<>();
    ArrayList<Integer> txtyPoint=new ArrayList<>();
    boolean drawtextCanva=false,callCanvatxt=true;


    public CustomShape(Context context) {
        super(context);
        init();
    }

    public CustomShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomShape(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        hexagonPath = new Path();
        hexagonBorderPath = new Path();
        maskColor = 0xFF01FF77;
        this.setBackgroundColor(Color.TRANSPARENT);

    }

    public void setRadius(float r) {
        this.radius = r;
        calculatePath();
    }

    public void setMaskColor(int color) {
        this.maskColor = color;
        invalidate();
    }

    private void calculatePath() {
        Log.v("calculatePath","inside"+width);
           /* float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
            float centerX = width / 2;
            float centerY = height / 2;
            hexagonPath.moveTo(centerX, centerY + radius);
            hexagonPath.lineTo(centerX - triangleHeight, centerY + radius / 2);
            hexagonPath.lineTo(centerX - triangleHeight, centerY - radius / 2);
            hexagonPath.lineTo(centerX, centerY - radius);
            hexagonPath.lineTo(centerX + triangleHeight, centerY - radius / 2);
            hexagonPath.lineTo(centerX + triangleHeight, centerY + radius / 2);
            hexagonPath.moveTo(centerX, centerY + radius);

            float radiusBorder = radius - 5;
            float triangleBorderHeight = (float) (Math.sqrt(3) * radiusBorder / 2);
            hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
            hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY
                    + radiusBorder / 2);
            hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY
                    - radiusBorder / 2);
            hexagonBorderPath.lineTo(centerX, centerY - radiusBorder);
            hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY
                    - radiusBorder / 2);
            hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY
                    + radiusBorder / 2);
            hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);*//*

          *//*  hexagonBorderPath.moveTo(centerX,0);
            hexagonBorderPath.lineTo(width-20,height-20);
            hexagonBorderPath.lineTo(20,height-20);
            hexagonBorderPath.moveTo(centerX,0);*//*

           *//* hexagonBorderPath.moveTo(160,20);
            hexagonBorderPath.cubicTo(140,8,50,10,160,190);
            hexagonBorderPath.cubicTo(160,190,270,10,165,8);
            hexagonBorderPath.moveTo(160,20);
            hexagonBorderPath.close();*//*

        hexagonBorderPath.moveTo(width / 2, height / 4);
        hexagonBorderPath.cubicTo(width / 2, 0, width, 0, width, height / 2);
        hexagonBorderPath.cubicTo(width, height / 2, width, (height / 2) + (height / 4), (width / 2 + width / 4), (height / 2) + (height / 4) + 30);
        hexagonBorderPath.cubicTo((width / 2 + width / 4), (height / 2) + (height / 4) + 30, width / 2 + 20, height - 20, width / 2, height);
        hexagonBorderPath.cubicTo(width / 2, height, width / 2 - 20, height - 20, width / 4, (height / 2) + (height / 4) + 30);
        hexagonBorderPath.cubicTo(width / 4, (height / 2) + (height / 4) + 30, 0, (height / 2) + (height / 4), 0, height / 2);
        hexagonBorderPath.moveTo(0, height / 2);
        hexagonBorderPath.cubicTo(0, 0, width / 2, 0, width / 2, height / 4);
        hexagonBorderPath.close();
        *//*hexagonBorderPath.cubicTo(width/2,height,0,(height/2)+(height/4),0,height/2);*//**//*



        /*    hexagonBorderPath.moveTo(width/2,height/2);
            hexagonBorderPath.cubicTo(width/2,0,width,0,width,height/2);
            hexagonBorderPath.cubicTo(width,height/2,width,(height/2)+(height/4),(width/2+width/4),(height/2)+(height/4)+30);
            hexagonBorderPath.cubicTo(width/2,height,0,(height/2)+(height/4),0,height/2);
            hexagonBorderPath.moveTo(0,height/2);
            hexagonBorderPath.cubicTo(0,0,width/2,0,width/2,height/2);

            //draw heart shape
*//*

          *//*  hexagonBorderPath.moveTo(width/4,height/4);
            hexagonBorderPath.cubicTo(width/4,0,(width/2)+(width/4),0,(width/2)+(width/4),height/4);
            hexagonBorderPath.cubicTo(width,(height/4)+20,width-20,(height/2)+20,(width/2)+(width/4),(height/2)+20);
            hexagonBorderPath.cubicTo(width,(height/2)+(height/4)+10,width-30,height,width/2,height-15);*//*

        *//*hexagonBorderPath.cubicTo(width/2,height,0,(height/2)+(height/4),0,height/2);*//**//*



/*
        if(shapeCount==0) {
            hexagonBorderPath.moveTo(width / 2, height / 4);
            hexagonBorderPath.cubicTo(width / 2, 0, width, 0, width, height / 2);
            hexagonBorderPath.cubicTo(width, height / 2, width, (height / 2) + (height / 4), (width / 2 + width / 4), (height / 2) + (height / 4) + 30);
            hexagonBorderPath.cubicTo((width / 2 + width / 4), (height / 2) + (height / 4) + 30, width / 2 + 20, height - 20, width / 2, height);
            hexagonBorderPath.cubicTo(width / 2, height, width / 2 - 20, height - 20, width / 4, (height / 2) + (height / 4) + 30);
            hexagonBorderPath.cubicTo(width / 4, (height / 2) + (height / 4) + 30, 0, (height / 2) + (height / 4), 0, height / 2);
            *//*
        *//*hexagonBorderPath.cubicTo(width/2,height,0,(height/2)+(height/4),0,height/2);*//**//*

            hexagonBorderPath.moveTo(0, height / 2);
            hexagonBorderPath.cubicTo(0, 0, width / 2, 0, width / 2, height / 4);
            hexagonBorderPath.close();
            shapeCount=1;
        }
        else if(shapeCount==1){
            hexagonBorderPath.moveTo(0,0);
            hexagonBorderPath.lineTo(width,height/4);
            hexagonBorderPath.lineTo(width,height);
            hexagonBorderPath.lineTo(0,height);
            hexagonBorderPath.moveTo(0,0);
            hexagonBorderPath.close();
            shapeCount=21;
        }
        else{
            float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
            float centerX = width / 2;
            float centerY = height / 2;
            float radiusBorder = radius - 5;
            float triangleBorderHeight = (float) (Math.sqrt(3) * radiusBorder / 2);
            hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
            hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY
                    + radiusBorder / 2);
            hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY
                    - radiusBorder / 2);
            hexagonBorderPath.lineTo(centerX, centerY - radiusBorder);
            hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY
                    - radiusBorder / 2);
            hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY
                    + radiusBorder / 2);
            hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
            hexagonBorderPath.close();
        }
*//*
*/
       /* Paint p = new Paint();
       rectF = new RectF(50, 20, 100, 80);
        p.setColor(Color.BLACK);
        hexagonBorderPath.arcTo (rectF, 90, 45, true);*/


        invalidate();
    }

/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        */
/*this= (ImageView) v;*//*

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = (newDist / oldDist);
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null && event.getPointerCount() == 2 || event.getPointerCount() == 3) {
                        newRot = rotation(event);
                        float r = newRot - d;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        float tx = values[2];
                        float ty = values[5];
                        float sx = values[0];
                        float xc = (this.getWidth() / 2) * sx;
                        float yc = (this.getHeight() / 2) * sx;
                        matrix.postRotate(r, tx + xc, ty + yc);
                    }
                }
                break;
        }

        // listener.dummyFun(matrix,bmap);
        //this.setImageMatrix(matrix);
        this.getMatrix().set(matrix);

        bmap= Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmap);
        this.draw(canvas);

        return true;
    }
*/


/*
    @Override
    protected void dispatchDraw(Canvas canvas) {
        int save = canvas.save();

        canvas.clipPath(hexagonBorderPath);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }
*/

    @Override
        public void onDraw(Canvas canvas) {

 Path clipPath = new Path();


        super.onDraw(canvas);



        Log.v("canvas_draw","inside"+width);

        //canvas.save();
// Move the origin to the right for the next rectangle.
        //
     /*   Paint p1=new Paint();
        p1.setStyle(Paint.Style.STROKE);
        p1.setColor(Color.RED);
        Paint p = new Paint();
        RectF rectF = new RectF(0, 0, width, height);
        canvas.drawRect(rectF,p1);
        p.setColor(Color.BLACK);
        canvas.drawArc (rectF, 90, 180, true, p);
        p.setColor(Color.BLUE);
        canvas.drawArc (rectF, 180, 90, true, p);
        p.setColor(Color.YELLOW);
        canvas.drawArc (rectF, 270, 180, true, p);
        Paint p2=new Paint();
        p2.setStyle(Paint.Style.FILL);
        p2.setColor(Color.WHITE);
        canvas.drawCircle(width/2, height/2, 100, p2);*/
        //canvas.drawLine(0,0,width/2,height/2,p);
       /* Paint p1=new Paint();
        p1.setStyle(Paint.Style.FILL);
        p1.setColor(Color.RED);
        RectF rectF = new RectF(0, 0, width, height);
        Log.v("Current_angle","values"+currentAngle);
        canvas.drawArc(rectF, 0, currentAngle, false, p1);
        */

        Paint p1=new Paint();
        RectF rectF = new RectF(0, 0, width, height);
        Path pathss=new Path();
        if(firstCircle==true){

            p1.setStyle(Paint.Style.FILL);
            p1.setColor(Color.RED);
            p1.setAntiAlias(true);

            Log.v("Current_angle","values"+currentAngle);
            canvas.drawArc(rectF, startAngles, currentAngle, true, p1);
            Paint xx=new Paint();
            xx.setColor(Color.GREEN);
            if(firstAngle<=45){
                int x= (int) ((width/2)+(width/4));
                int y= (int) ((height/2)+25);
                canvas.drawText("Hello",x,y,xx);

            }
            else if(firstAngle<=90){
                int x= (int) ((width/2)+(width/3));
                int y= (int) ((height/2)+12);
                canvas.drawText("Hello",x,y,xx);
            }
            else if(firstAngle<=135){
                int x= (int) ((width/2));
                int y= (int) ((height/2)+(height/3));
                canvas.drawText("Hello",x,y,xx);
            }

        }
        if(secondCircle==true){
            p1.setStyle(Paint.Style.FILL);
            p1.setColor(Color.RED);
            p1.setAntiAlias(true);
            canvas.drawArc(rectF, firstStartAngle, firstAngle, true, p1);
            p1.setStyle(Paint.Style.FILL);
            p1.setColor(Color.YELLOW);
            p1.setAntiAlias(true);
            Log.v("Current_angle","values"+currentAngle);
            canvas.drawArc(rectF, startAngles, currentAngle, true, p1);
        }
        if(thridCircle==true){
            Log.v("first_angle_start",firstStartAngle+"Last"+firstAngle+"second_angle"+secondStartAngle+"Last"+secondAngle+"Third_angle " +
                    thirdStartAngle+"Last "+thirdAngle);
            p1.setStyle(Paint.Style.FILL);
            p1.setColor(Color.RED);
            p1.setAntiAlias(true);
            canvas.drawArc(rectF, firstStartAngle, firstAngle, true, p1);
            p1.setStyle(Paint.Style.FILL);
            p1.setColor(Color.YELLOW);
            p1.setAntiAlias(true);
            canvas.drawArc(rectF, secondStartAngle, secondAngle, true, p1);

            p1.setStyle(Paint.Style.FILL);
            p1.setColor(Color.BLUE);
            p1.setAntiAlias(true);
            Log.v("Current_angle_blue","values"+currentAngle);
            canvas.drawArc(rectF, thirdStartAngle, currentAngle, true, p1);

        }
        if(callCanvatxt){
            alog(0,90,width,height);
            alog(90,180,width,height);
            alog(180,270,width,height);
        }
        Paint p2=new Paint();
        p2.setStyle(Paint.Style.FILL);
        p2.setColor(Color.WHITE);
        p2.setAntiAlias(true);
        canvas.drawCircle(width/2, height/2, 50, p2);

       /* Paint xx=new Paint();
        xx.setColor(Color.GREEN);
        xx.setStrokeWidth(25);
        pathss.addArc(rectF,0,45);
        canvas.rotate(-15);
        canvas.drawTextOnPath("hellodddddddddddddd",pathss,40,50,xx);*/
        Paint xx=new Paint();
        xx.setColor(Color.GREEN);
        xx.setStrokeWidth(45);
        xx.setStyle(Paint.Style.FILL);
        xx.setTextSize(35);
        if(drawtextCanva){
            Log.v("txtxPointVal","enter");
            for(int i=0;i<txtxPoint.size();i++){
                Log.v("txtxPointVal","enter"+txtxPoint.get(i)+" lllll "+txtyPoint.get(i));
                canvas.drawText("A",txtxPoint.get(i),txtyPoint.get(i),xx);
            }
        }
      /* int x = (int) (width/2);//Horizontal center of canvas view
       int y = (int) (height/2); //Vertical center of canvas view
        canvas.rotate(0, x ,y ); //Rotates canvas to a line in the middle
//of start and end of arc
        canvas.translate(-60f,0);//Moves the text a little out of the center of the circle (50f is arbitrary)
        xx.setStyle(Paint.Style.FILL);
        canvas.drawText("hellodddddddddddddd", x, y, xx);
//Undo the translations and rotations so that next arc can be drawn normally
        canvas.translate(60f,0);*/

    }

    // getting the view size and default radius
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        width1 = MeasureSpec.getSize(widthMeasureSpec);
        height1 = MeasureSpec.getSize(heightMeasureSpec);
        radius = height / 2 - 10;
        calculatePath();
        //startAnimatingArc(270);
    }

    public void changeShape(int i){
        shapeCount=i;
        invalidate();

    }


    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        float s=x * x + y * y;
        return (float)Math.sqrt(s);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public void viewpass(View v){
        img=(ImageView) v;
    }


   /* public static void callll(DummyActivity.Dummy listener1){
        listener=listener1;
    }*/

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // compute the path
        float halfWidth = w / 2f;
        float halfHeight = h / 2f;
        float centerX = halfWidth;
        float centerY = halfHeight;
        newPath.reset();
        // newPath.addCircle(centerX, centerY, Math.min(halfWidth, halfHeight), Path.Direction.CW);
        newPath.addCircle(centerX, centerY, 50, Path.Direction.CW);
        newPath.close();

    }

    public void startAnimatingArc(final float maxAngle) {

        new Thread(new Runnable() {
            public void run() {
                while (currentAngle < maxAngle) {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            Log.v("Thread_are_run","called_contionusly");
                            invalidate();
                        }
                    });

                    try {
                        currentAngle=currentAngle+10;
                        Thread.sleep(50);
                        Log.v("Thread_are","called_contionusly"+currentAngle);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(currentAngle<maxAngle){

                }
                else{
                    if(callArc==0){
                        secondCircle=true;
                        firstCircle=false;
                        thridCircle=false;
                        callArc=callArc+1;
                        Log.v("current_angle_value11","inside_third"+currentAngle);
                        secondStartAngle=firstAngle;
                        startAngles=currentAngle;
                        currentAngle=0;
                        startAnimatingArc(secondAngle);

                    }
                    if(callArc==1){
                        secondCircle=false;
                        firstCircle=false;
                        thridCircle=true;
                        callArc=callArc+1;
                        Log.v("current_angle_value","inside_third"+currentAngle);
                        thirdStartAngle=firstAngle+secondAngle;
                        Log.v("current_angle_value","inside_third"+thirdStartAngle);
                        startAngles=currentAngle;
                        currentAngle=0;
                        startAnimatingArc(thirdAngle);
                        callArc=-1;
                    }

                }

            }
        }).start();
    }

    public void firstArc(int x, int y,int secy,int thirdy){
        firstCircle=true;
        startAngles=0;
        firstAngle=y;
        secondAngle=secy;
        thirdAngle=thirdy;
        startAnimatingArc(y);
        Log.v("first_circle","are_drawing");


    }


    public void alog(int startangle,int endangle,float width,float height){
        Log.v("width_size", String.valueOf(width1));
        if(endangle<=90){

            if(endangle<=45){


                txtxPoint.add((int) ((width/2)+(width/4)));
                txtyPoint.add((int) ((height/2)+(25)));
            }
            else{
                txtxPoint.add((int) ((width/2)+(width/4)));
                txtyPoint.add((int) ((height/2)+(height/4)));
            }
            Log.v("first_circle22","are_drawing"+txtxPoint.get(txtxPoint.size()-1));
        }
        else if(endangle<=180){

            if(endangle<=135){
                txtxPoint.add((int) ((width/4)));
                txtyPoint.add((int) ((height/2)+(25)));
            }
            else{
                txtxPoint.add((int) ((width/4)));
                txtyPoint.add((int) ((height/2)+(height/4)));
            }
            Log.v("first_circle22","are_drawing"+txtxPoint.get(txtxPoint.size()-1));
        }
        else if(endangle<=270){
            if(endangle<=225){
                txtxPoint.add((int) ((width/4)));
                txtyPoint.add((int) ((height/2)-(25)));

            }else{
                txtxPoint.add((int) ((width/4)));
                txtyPoint.add((int) ((height/2)-(height/4)));
            }
            Log.v("first_circle22","are_drawing"+txtxPoint.get(txtxPoint.size()-1));
        }
        else{
            if(endangle<=315){
                txtxPoint.add((int) ((width/4)+25));
                txtyPoint.add((int) ((height/4)));
            }
            else{
                txtxPoint.add((int) ((width/2)+(width/4)));
                txtyPoint.add((int) ((height/4)));
            }
            Log.v("first_circle22","are_drawing"+txtxPoint.get(txtxPoint.size()-1));
        }
    }

    public void refreshCanva(){
        drawtextCanva=true;
        invalidate();
    }
}
