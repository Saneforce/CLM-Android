package saneforce.sanclm.adapter_class;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.BuildConfig;
import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.DetailingFullScreenImageViewActivity;
import saneforce.sanclm.activities.Model.LoadBitmap;
import saneforce.sanclm.activities.Model.StoreDetailingFeedback;
import saneforce.sanclm.activities.Model.StoreImageTypeUrl;
import saneforce.sanclm.activities.PdfViewActivity;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.HelpWebView;
import saneforce.sanclm.util.OnSwipeTouchListener;
import saneforce.sanclm.util.PaintView;
import saneforce.sanclm.util.ProductChangeListener;
import saneforce.sanclm.util.UpdateUi;
import saneforce.sanclm.util.WebviewTopSwipe;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class MyCustomPagerAdapter extends PagerAdapter implements Runnable{
    Context context;
    LayoutInflater layoutInflater;
    CommonSharedPreference mCommonSharedPreference;
    //SimpleDraweeView simpleDraweeView;
    ImageView simpleDraweeView,img_zoom;
    DetailingTrackerPOJO mDetailingTrackerPOJO;
    Uri imageUri;
    int length,currentBrdPos;
    private String TAG = "ON_TOUCH";
    String slideData,BrdCode,Next_prev_SlideData;
    saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods CommonUtilsMethods;
    float initialX, initialY;
    MyCustomPagerAdapter myCustomPagerAdapter;
    public static int doubleTouchCount=0;
    static touchGesture touchgesture;
    int pageSlide=-1,startPos,lastPos;
    int lastpos=-1;

    HelpWebView webView;
    VideoView video_view;
    MediaController media_Controller;
    LinearLayout ll_videoview,ll_media_controller;
    FrameLayout fl_videoview;
    RelativeLayout rlay;

    PDFView pdfView;
    ImageView imageView;
    DisplayMetrics dm;
    int videoCount=0;
    int pdfCount=0;
    String slideUrl1 = null;
    int videPlayingId=0;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    Object  objsd;
    PhotoViewAttacher mAttacher;
    DetailingFullScreenImageViewActivity detailingFullScreenImageViewActivity;
    static UpdateUi updateUi;
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
    String videoUrl="";
    long swipeStartTime,swipeEndTime;
    Button play_btn,visible_controller,zoom_btn;
    SeekBar seekBar;
    boolean isPlaying=false,isPause=false;
    public static ArrayList<StoreImageTypeUrl> slidess=new ArrayList<>();
    public static ArrayList<StoreImageTypeUrl> slidessStatic=new ArrayList<>();
    public static ArrayList<StoreImageTypeUrl> slidessDet=new ArrayList<>();
    public static boolean preVal=false;
    private Handler mHandler = new Handler();
    ScheduledExecutorService mScheduledExecutorService;
    static int  current_pos=2;
    ArrayList<String> slideNaming=new ArrayList<>();
    Runnable rr;
    JSONArray jsonArrayFeed = new JSONArray();;
    ArrayList<StoreImageTypeUrl> slideDescribe=new ArrayList<>();
    ArrayList<StoreDetailingFeedback> slideFeedbacks=new ArrayList<>();
    public static ArrayList<StoreImageTypeUrl> finalArray=new ArrayList<>();
    public static ArrayList<LoadBitmap> storingSlide=new ArrayList<>();
    ArrayList<Integer> storeVideoPos=new ArrayList<>();
   public static ProductChangeListener productChangeListener;
  public static int presentSlidePos;
   DetailingFullScreenImageViewActivity act;
    Dialog dialog1;
    DataBaseHandler dbh;
    String slideFeedback="",likes="false",dislikes="false";
    Api_Interface apiService;
    String db_connPath;
    int curent_pos=0;
    static ArrayList<String> allpath=new ArrayList<>();
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    public MyCustomPagerAdapter(DetailingFullScreenImageViewActivity context, int length, ArrayList<StoreImageTypeUrl> slidess) {
        this.context = context;
        this.length = length;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCommonSharedPreference = new CommonSharedPreference(context);
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
        CommonUtilsMethods = new CommonUtilsMethods(context);
        detailingFullScreenImageViewActivity=new DetailingFullScreenImageViewActivity();
        //scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        act= context;
        slidessStatic.addAll(slidess);
        dbh=new DataBaseHandler(this.context);
        rr=this;
        slideData = mCommonSharedPreference.getValueFromPreference("ProductBrdWiseSlides_jsonArray");
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        slideDescribe.clear();
        allpath.clear();
        //storingSlide.clear();
       apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        try {
            JSONArray Orig_jsonSLides_data = new JSONArray(slideData);
            Log.v("length_json_arr",Orig_jsonSLides_data.length()+" total_val "+ Orig_jsonSLides_data.toString());
            for(int i=0;i<Orig_jsonSLides_data.length();i++){
                JSONObject jsonObject=Orig_jsonSLides_data.getJSONObject(i);
                String brdNAme=jsonObject.getString("BrdName");
                String brdCOde=jsonObject.getString("BrdCode");
                Log.v("printing_brand_ing",brdNAme);
                JSONArray jsonSlide=jsonObject.getJSONArray("Slides");
                for(int j=0;j<jsonSlide.length();j++){
                    JSONObject json=jsonSlide.getJSONObject(j);
                    Log.v("slide_printing_nam",json.getString("SlideName")+"slide_url"+json.getString("SlideLocUrl"));
                    slideDescribe.add(new StoreImageTypeUrl(json.getString("SlideName"),json.getString("SlideType"),json.getString("SlideLocUrl"),""
                            ,json.getString("SlideId"),brdNAme,brdCOde));
                    allpath.add(json.getString("SlideLocUrl"));
                }
            }
            slidessDet.addAll(slideDescribe);

            HelpWebView.swipeTopListener(new WebviewTopSwipe() {
                @Override
                public void swipeTop() {
                    //popupScribbling(4,slideDescribe.get(presentSlidePos).getSlideUrl());
                }
            });
        }catch (Exception e){
            Log.v("pos_array_list_ex", String.valueOf(e));
        }

        }

    public MyCustomPagerAdapter(Context context, int length,int pageSlide) {
        this.context = context;
        this.length = length;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCommonSharedPreference = new CommonSharedPreference(context);
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
        CommonUtilsMethods = new CommonUtilsMethods(context);
        this.pageSlide=pageSlide;

       }

    public static void bindlistner(touchGesture touchgesture1){
        touchgesture=touchgesture1;
        HelpWebView.tapListener(touchgesture1);
        OnSwipeTouchListener.bindTouchGesture(touchgesture1);

    }


    @Override
    public int getCount() {
        return length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }



    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {


        final View itemView = layoutInflater.inflate(R.layout.detailingimageview, container, false);


                final String data =mCommonSharedPreference.getValueFromPreference("CurrentBrdSlides_Json_array");

        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        rlay = (RelativeLayout) itemView.findViewById(R.id.rlay);
        /*media_Controller = new MediaController(context);*/
        webView = (HelpWebView) itemView.findViewById(R.id.webview);
        //simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.iv_detimage);
        simpleDraweeView = (ImageView) itemView.findViewById(R.id.iv_detimage);
        //img_zoom = (ImageView) itemView.findViewById(R.id.img_zoom);

        ll_videoview=(LinearLayout)itemView.findViewById(R.id.ll_videoview);
        ll_media_controller=(LinearLayout)itemView.findViewById(R.id.ll_media_controller);
        fl_videoview=(FrameLayout) itemView.findViewById(R.id.fl_videoview);
        visible_controller=(Button)itemView.findViewById(R.id.visible_controller);
        visible_controller.setVisibility(View.INVISIBLE);

        //zoom_btn=(Button)itemView.findViewById(R.id.zoom_btn);

        ll_videoview.setEnabled(true);
        ll_videoview.setFocusable(true);
        Log.v("video_View_paly11","invisibleee");

        if(videoCount>0){
            if(video_view.isPlaying()){
                Log.v("video_View_paly","invisibleee");
                video_view.setVisibility(View.INVISIBLE);
                play_btn.setText("Play");
                //visible_controller.setVisibility(View.VISIBLE);
                ll_media_controller.setVisibility(View.INVISIBLE);
                seekBar.setProgress(0);

                }

        }

        mCommonSharedPreference.setValueToPreference("slide_feed","[]");

        imageView.setOnTouchListener(new OnSwipeTouchListener(context,true,slideUrl1){
            public void onSwipeTop() {
                Log.v("swiping_top","are_clciek");
                popupScribbling(2,slideDescribe.get(presentSlidePos).getSlideUrl());
            }
        });



/*
        simpleDraweeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.v("simple_draweeview","setOnclick_l");

                int action = event.getActionMasked();

                switch (action) {

                    case MotionEvent.ACTION_POINTER_DOWN:

                        int count = event.getPointerCount(); // Number of 'fingers' in this time
                        if (count == 2) {
                            if (doubleTouchCount == 0) {
                                doubleTouchCount = 1;
                                touchgesture.touches();

                            } else {
                                doubleTouchCount = 0;
                                touchgesture.unTouches();
                            }
                        }
                        break;



                                */
/*    oldDist = spacing(event);
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
                                            float xc = (view.getWidth() / 2) * sx;
                                            float yc = (view.getHeight() / 2) * sx;
                                            matrix.postRotate(r, tx + xc, ty + yc);
                                        }
                                    }


                }


 simpleDraweeView.setImageMatrix(matrix);
                           // view.setMa(matrix);
                            //view.getMatrix().set(matrix);

                           Bitmap bmap= Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
                            Canvas canvas = new Canvas(bmap);
                            view.draw(canvas);*//*

                }

                return false;
            }
        });
*/





        ll_videoview.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeTop() {
                Log.v("swiping_top","are_clciek");
                popupScribbling(3,slideDescribe.get(presentSlidePos).getSlideUrl());
            }
           /*     Log.v("ll_lay","are_Called_here");
                int action = event.getActionMasked();
                Log.v("frame_lay", String.valueOf(action));

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        break;

                        case MotionEvent.ACTION_MOVE:

                                    *//*media_Controller.hide();*//*
                            break;
                    case MotionEvent.ACTION_UP:
                        Log.v("layout_are_clicked","here"+String.valueOf(ll_media_controller.getVisibility()));
                      *//*  if(!media_Controller.isShowing())
                            media_Controller.show();
                        else
                            media_Controller.hide();*//*
                      if(ll_media_controller.getVisibility()==View.VISIBLE){
                          ll_media_controller.setVisibility(View.INVISIBLE);
                      }
                      else{
                      ll_media_controller.setVisibility(View.VISIBLE);}

                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        Log.v("layout_are_clicked","here");

                        int count = event.getPointerCount(); // Number of 'fingers' in this time
                        if (count == 2) {
                            if (doubleTouchCount == 0) {
                                doubleTouchCount = 1;
                                touchgesture.touches();
                                } else {
                                doubleTouchCount = 0;
                                touchgesture.unTouches();
                            }
                        }

*//*

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
*//*


                                *//*case MotionEvent.ACTION_MOVE:


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
                                            float xc = (view.getWidth() / 2) * sx;
                                            float yc = (view.getHeight() / 2) * sx;
                                            matrix.postRotate(r, tx + xc, ty + yc);
                                        }
                                    }*//*

                }
                           *//* simpleDraweeView.setImageMatrix(matrix);
                           // view.setMa(matrix);
                            //view.getMatrix().set(matrix);

                           Bitmap bmap= Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
                            Canvas canvas = new Canvas(bmap);
                            view.draw(canvas);*//*

                return false;
            }*/
        });

/*
        img_zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUi.updatingui();
            }
        });
*/

        simpleDraweeView.setOnTouchListener(new OnSwipeTouchListener(context,"img") {

            public void onSwipeTop() {
                Log.v("swiping_top","are_clciek");
               // img_zoom.setVisibility(View.VISIBLE);
                popupScribbling(1,slideDescribe.get(presentSlidePos).getSlideUrl());
            }


            @Override
            public void drag(Matrix matrix) {
                super.drag(matrix);
                if(objsd!=null){
                    rlay= (RelativeLayout) objsd;
                    Log.v("drag_are_cc","calledd"+matrix);
                    simpleDraweeView = (ImageView) rlay.findViewById(R.id.iv_detimage);
                    simpleDraweeView.setScaleType(ImageView.ScaleType.MATRIX);
                    simpleDraweeView.setImageMatrix(matrix);

                }
            }

            @Override
            public void onZoom(float scale) {
                super.onZoom(scale);

                if(objsd!=null){
                    rlay= (RelativeLayout) objsd;
                    Log.v("zooming_are_cc","calledd"+scale);
                    simpleDraweeView = (ImageView) rlay.findViewById(R.id.iv_detimage);
                    simpleDraweeView.setScaleX(scale);
                    simpleDraweeView.setScaleY(scale);
                }


                /*
                simpleDraweeView.setScaleType(ImageView.ScaleType.MATRIX);
                //https://stackoverflow.com/questions/2521959/how-to-scale-an-image-in-imageview-to-keep-the-aspect-ratio
                Log.v("zooming_are_cc","calledd"+scale);
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                simpleDraweeView.setImageMatrix(matrix);*/
                // simpleDraweeView.setScaleY(scale);
                /*simpleDraweeView.setScaleX(scale);
                simpleDraweeView.setScaleY(scale);*/
               /* Drawable drawing = simpleDraweeView.getDrawable();
                Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();
                // Get current dimensions
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);

                Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                BitmapDrawable result = new BitmapDrawable(scaledBitmap);
                width = scaledBitmap.getWidth();
                height = scaledBitmap.getHeight();

                simpleDraweeView.setImageDrawable(result);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) simpleDraweeView.getLayoutParams();
                params.width = width;
                params.height = height;
                simpleDraweeView.setLayoutParams(params);*/
            }
            /* public void onSwipeBottom() {
                Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
                dialog1.cancel();
            }*//*

             */
/* public void onSwipeRight() {
                Toast.makeText(context, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(context, "left", Toast.LENGTH_SHORT).show();
            }
           *//*


        });



        imageView.setOnTouchListener(new OnSwipeTouchListener(context,true,slideUrl1) {
            public void onSwipeTop() {
                Log.v("swiping_top","are_clciek");
                popupScribbling(2,slideDescribe.get(presentSlidePos).getSlideUrl());
            }

           /* private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d("TEST_er", "onDoubleTap");
                    Intent i=new Intent(context,PdfViewActivity.class);
                    i.putExtra("PdfUrl",slideUrl1);
                    context.startActivity(i);
                    return false;
                }

            });*/

/*
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.v("touching_s", "are_called");
                gestureDetector.onTouchEvent(event);
                int action = event.getActionMasked();

                switch (action) {

                              */
/* case MotionEvent.ACTION_DOWN:
                                    Intent i=new Intent(context,PdfViewActivity.class);
                                    i.putExtra("PdfUrl",slideUrl1);
                                    context.startActivity(i);*//*


                                    case MotionEvent.ACTION_POINTER_DOWN:
                        Log.v("double_finget", "called");
                        int count = event.getPointerCount(); // Number of 'fingers' in this time
                        if (count == 2) {
                            Log.v("double_fingetssss", "called");
                            if (doubleTouchCount == 0) {
                                doubleTouchCount = 1;
                                touchgesture.touches();
                                // rll_overlay.setVisibility(View.VISIBLE);
                            } else {
                                doubleTouchCount = 0;
                                touchgesture.unTouches();
                                //rll_overlay.setVisibility(View.GONE);
                            }
                        }
                        break;


                }
                return false;
            }
*/
        });


        visible_controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_media_controller.setVisibility(View.VISIBLE);
                visible_controller.setVisibility(View.INVISIBLE);
            }
        });





                final int page = pageSlide;
                Log.v("position_list", String.valueOf(position));

                try {
                   // JSONObject jsonObj = new JSONObject(slideData);
                    JSONArray Orig_jsonSLides_data =  new JSONArray(slideData);
                   // Log.v("pos_array_list", String.valueOf(Orig_jsonSLides_data));
                   /*
                    for (int i = 0; i < Orig_jsonSLides_data.length(); i++) {
                        JSONObject jsonChildNode = Orig_jsonSLides_data.getJSONObject(i);
                        String BrdCode =jsonChildNode.optString("BrdCode");
                        if(BrdCode.equalsIgnoreCase(mDetailingTrackerPOJO.getmDetstrtpdtBrdCode())){
                            currentBrdPos = i;
                        }
                    }*/


                   /* JSONArray json_array = new JSONArray(data);
                  //  for (int i = 0; i < json_array.length(); i++) {
                        JSONObject jsonChildNode = json_array.getJSONObject(position);
                        String imgname = jsonChildNode.optString("SlideName");
                        String slideUrl = jsonChildNode.optString("SlideLocUrl");
                        String slideType = jsonChildNode.optString("SlideType");*/

                        StoreImageTypeUrl mm=slideDescribe.get(position);
                        Log.v("slide_name_printing",mm.getSlideNam()+" type "+mm.getSlideTyp()+simpleDraweeView.getVisibility());
                        if(mm.getSlideTyp().equalsIgnoreCase("h"))
                        {

                            webView.setVisibility(View.VISIBLE);
                            simpleDraweeView.setVisibility(View.INVISIBLE);
                          //  img_zoom.setVisibility(View.INVISIBLE);
                            if(videoCount>0)
                            video_view.setVisibility(View.INVISIBLE);
                            imageView.setVisibility(View.INVISIBLE);
                            ll_videoview.setVisibility(View.INVISIBLE);
                            fl_videoview.setVisibility(View.INVISIBLE);
                            container.addView(itemView);
                           /* webView.getSettings().setBuiltInZoomControls(true);
                            webView.getSettings().setDisplayZoomControls(true);
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.getSettings().setLoadWithOverviewMode(false);
                            webView.getSettings().setUseWideViewPort(false);
                            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                            webView.getSettings().setLoadsImagesAutomatically(true);
                            webView.getSettings().setAppCacheEnabled(false);
                            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                            webView.getSettings().setMediaPlaybackRequiresUserGesture(true);
                            webView.getSettings().setAllowFileAccess(true);
                            webView.setHorizontalScrollBarEnabled(false);
                            webView.setVerticalScrollBarEnabled(false);*/




                            webView.getSettings().setBuiltInZoomControls(true);
                            webView.getSettings().setDisplayZoomControls(true);
                            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
                            webView.getSettings().setJavaScriptEnabled(true);

                            webView.getSettings().setLoadWithOverviewMode(true);
                            webView.getSettings().setUseWideViewPort(true);
                            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                            webView.getSettings().setLoadsImagesAutomatically(true);
                            webView.getSettings().setAppCacheEnabled(false);
                            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

                            webView.getSettings().setAllowFileAccess(true);
                            webView.setHorizontalScrollBarEnabled(false);
                            webView.setVerticalScrollBarEnabled(false);
                            webView.getSettings().setDomStorageEnabled(true);
                            webView.getSettings().setAppCacheEnabled(true);
                            webView.getSettings().setDatabaseEnabled(true);
                           /* webView.setWebChromeClient(new WebChromeClient(){

                            });*/
                            webView.setWebViewClient(new WebViewClient(){

                                    @Override
                                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                    super.onPageStarted(view, url, favicon);

                                    Log.d("WebView", "your current url when webpage loading.." + url);
                                }

                                    @Override
                                    public void onPageFinished(WebView view, String url) {
                                    Log.d("WebView", "your current url when webpage loading.. finish" + url);
                                    super.onPageFinished(view, url);
                                }

                                    @Override
                                    public void onLoadResource(WebView view, String url) {
                                    super.onLoadResource(view, url);
                                }
                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    System.out.println("when you click on any interlink on webview that time you got url :-" + url.substring(6));
                                    if(url.contains(".pdf")){
                                        Intent i = new Intent(context, PdfViewActivity.class);
                                        i.putExtra("PdfUrl", url.substring(7));
                                        context.startActivity(i);
                                        return false;
                                    }
                                    return super.shouldOverrideUrlLoading(view, url);
                                }

                            });
                    /*webView.getSettings().setAppCachePath(context.getFilesDir().getAbsolutePath() + "/cache");

                    webView.getSettings().setDatabasePath(context.getFilesDir().getAbsolutePath() + "/databases");*/
                            //webView.setWebViewClient(new WebViewPdfOpener(activity,result));
                	/*
                	webView.getSettings().setUserAgentString(getDefaultUserAgent());*/
                            //
                            webView.setInitialScale(1);
                            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                            //  webView.setWebViewClient(new WebViewPdfOpener(activity,result));
                            //webView.setInitialScale(1);
                            // webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                            //webView.setInitialScale(101);

                            detailingFullScreenImageViewActivity.videoTap=0;
                            String HTMLPath=mm.getSlideUrl().replaceAll(".zip","");
                            File f = new File(HTMLPath);
                            final File[] files=f.listFiles(new FilenameFilter() {
                                @Override
                                public boolean accept(File dir, String filename) {
                                    return filename.contains(".html");
                                }
                            });
                            String url="";
                            if (files.length>0) url=files[0].getAbsolutePath();
                            imageUri= Uri.fromFile(new File(url));
                           // simpleDraweeView.setVisibility(View.GONE);
                            //video_view.setVisibility(View.GONE);
                            // media_Controller.setVisibility(View.GONE);
                           // pdfView.setVisibility(View.GONE);

                            webView.loadUrl("file://"+files[0].getAbsolutePath());
                            //imageView.setVisibility(View.GONE);
                            return itemView;

                        }
                        else if(mm.getSlideTyp().equalsIgnoreCase("i")){
                            detailingFullScreenImageViewActivity.videoTap=0;

                            simpleDraweeView.setVisibility(View.VISIBLE);
                           // img_zoom.setVisibility(View.VISIBLE);
                            webView.setVisibility(View.INVISIBLE);
                            if(videoCount>0)
                            video_view.setVisibility(View.INVISIBLE);
                            imageView.setVisibility(View.INVISIBLE);
                            ll_videoview.setVisibility(View.INVISIBLE);
                            fl_videoview.setVisibility(View.INVISIBLE);
                            container.addView(itemView);
                            Log.v("printing_file_detailing","Exceptionnnnn"+mm.getSlideUrl());
                           /* Bitmap bm = BitmapFactory.decodeFile(mm.getSlideUrl());
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);*/
                            try{
                                simpleDraweeView.setImageURI(null);
                                //simpleDraweeView.setImageURI(Uri.parse(mm.getSlideUrl()));
                                Glide.with(context)
                                        .load(mm.getSlideUrl())
                                        .error(R.drawable.ic_menu_camera)

                                        .into(simpleDraweeView);
                            }catch (OutOfMemoryError e){
                                Log.v("img_loading_url","Exceptionnnnn");
                               /* Glide.with(context)
                                        .load(new File(mm.getSlideUrl()))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .dontAnimate()
                                        .skipMemoryCache(true)
                                        // Uri of the picture
                                        .into(simpleDraweeView);*/
                            }

                           // pdfView.setVisibility(View.GONE);
                           /* Drawable d = Drawable.createFromPath(mm.getSlideUrl());
                            simpleDraweeView.setBackground(d);*/
                           /* imageUri = Uri.fromFile(new File(mm.getSlideUrl()));
                            simpleDraweeView.setImageURI(imageUri);*/
                           /* BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            bmOptions.inSampleSize = 8;
                            Bitmap bm = BitmapFactory.decodeFile(mm.getSlideUrl(),bmOptions);
                            Bitmap resizedBitmap = Bitmap.createBitmap(
                                    bm, 0, 0, bm.getWidth(), bm.getHeight());*/
                            /*simpleDraweeView.setImageBitmap(null);
                            simpleDraweeView.setImageBitmap(getResizedBitmap(mm.getSlideUrl()));*/
                           /* if (bm != null && !bm.isRecycled()) {
                                bm.recycle();
                                bm = null;
                            }*/
                            Log.v("image_loading_url", String.valueOf(mm.getSlideUrl()));
                            //imageView.setVisibility(View.GONE);

                            return itemView;
                        }
                        else if(mm.getSlideTyp().equalsIgnoreCase("v")){
                            Log.v("webview_are","video_called"+mm.getSlideUrl());
                           // video_view=(VideoView) itemView.findViewById(R.id.video_view);
                            detailingFullScreenImageViewActivity.videoTap=1;
                            video_view=(VideoView) itemView.findViewById(R.id.video_view);
                           /* Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mm.getSlideUrl(),
                                    MediaStore.Images.Thumbnails.MINI_KIND);
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
                            video_view.setBackgroundDrawable(bitmapDrawable);*/
                            ll_media_controller.setVisibility(View.VISIBLE);
                            play_btn=(Button)itemView.findViewById(R.id.play_btn);
                            storeVideoPos.add(position);
                            play_btn.setText("Play");

                            play_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (video_view.isPlaying()) {
                                        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
                                        DisplayMetrics metrics = new DisplayMetrics();
                                        wm.getDefaultDisplay().getMetrics(metrics);
                                        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) video_view.getLayoutParams();
                                        params.width = metrics.widthPixels;
                                        params.height = metrics.heightPixels;
                                        params.leftMargin = 0;
                                        video_view.setLayoutParams(params);


                                        video_view.setVisibility(View.VISIBLE);
                                        play_btn.setText("Play");
                                        video_view.pause();
                                        Log.v("play_btn_ccc", "Clicked");
                   /* if(!isPause) {

                    }else{
                        video_view.setVisibility(View.VISIBLE);
                        video_view.resume();
                        isPlaying = true;
                    }*/
                                    } else {
                                        Log.v("play_btn_ccc11", "Clicked");
                                        video_view.setVisibility(View.VISIBLE);
                                        play_btn.setText("Pause");
                                        video_view.start();
                                        isPlaying = true;
                                        seekBar.setMax(video_view.getDuration());
                                        current_pos = video_view.getDuration();
                                        Log.v("CurrentPostttt11", String.valueOf(video_view.getDuration()));
                                        new Thread(rr).start();
                   /* if(!isPause) {

                    }
                    else{
                        boolean value=video_view==null;
                        Log.v("video_view_value",String.valueOf(value));
                        video_view.setVisibility(View.VISIBLE);
                        video_view.resume();
                        isPlaying = true;
                    }*/
                                    }

                                }
                            });

                            seekBar=(SeekBar)itemView.findViewById(R.id.seek_bar);

                            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    if(video_view != null && fromUser){
                                        Log.v("seek_progress", String.valueOf(progress));
                                        video_view.seekTo(progress);
                                    }
                                }
                            });


                            webView.setVisibility(View.INVISIBLE);
                            imageView.setVisibility(View.INVISIBLE);
                            simpleDraweeView.setVisibility(View.INVISIBLE);
                            fl_videoview.setVisibility(View.VISIBLE);
                            video_view.setVisibility(View.VISIBLE);
                            ll_videoview.setVisibility(View.VISIBLE);
                           // pdfView.setVisibility(View.GONE);
                           /* media_Controller = new MediaController(context);
                            media_Controller.show();*/
                            dm = new DisplayMetrics();
                            container.addView(itemView);
                            container.removeView(video_view);
                           // video_view.setVideoPath("/storage/emulated/0/Download/fp.avi");
                           // video_view.setMediaController(media_Controller);

                            video_view.setVideoPath(mm.getSlideUrl());

                           /* video_view.setVideoPath("/storage/emulated/0/Products/Demo_Html/video/Horlicks1.mp4");
                            videoUrl="/storage/emulated/0/Products/Demo_Html/video/Horlicks1.mp4";*/
                            video_view.seekTo(1);

                            videoCount++;
                            new Thread(this).start();
                           // seekBar.setMax(video_view.getDuration());
                            Log.v("video_currentPost", String.valueOf(video_view.getDuration()));
                            return itemView;
                        }
                        else if(mm.getSlideTyp().equalsIgnoreCase("p")){
                            Log.v("printing_total_pos",position+" present"+presentSlidePos);
                            slideUrl1=mm.getSlideUrl();
                            detailingFullScreenImageViewActivity.videoTap=1;
                            ++pdfCount;
                            if(pdfCount>1)
                                //webView.setVisibility(View.GONE);
                            simpleDraweeView.setVisibility(View.INVISIBLE);
                            if(videoCount>0)
                                video_view.setVisibility(View.INVISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                            //pdfView.setVisibility(View.GONE);
                            fl_videoview.setVisibility(View.INVISIBLE);
                            ll_videoview.setVisibility(View.INVISIBLE);
                            simpleDraweeView.setVisibility(View.INVISIBLE);
                            container.addView(itemView);
                            Log.v("url_img_slide", String.valueOf(position)+" pdf_path "+mm.getSlideUrl());
                           // imageUri = Uri.fromFile(new File(mm.getSlideUrl()));
                            imageView.setImageBitmap(getBitmap(new File(mm.getSlideUrl())));
                            /*Glide.with(context)
                                    .load(getBitmap(new File(mm.getSlideUrl())))
                                    .asBitmap()
                                    .error(R.drawable.ic_menu_camera)

                                    .into(imageView);*/
                           // imageView.setImageBitmap(StringToBitMap(mCommonSharedPreference.getValueFromPreference("pdfBitmap"+position)));
                            return itemView;
                        }

                    videPlayingId=position;




                }catch (Exception e) {
                    e.printStackTrace();
                }
           /* }
        }).start();*/


            return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //Log.v("postion_pager", String.valueOf(position)+"object_view"+object);
        container.removeView((RelativeLayout) object);
    }
    public static void productCheckBindListener(ProductChangeListener listt){
        productChangeListener=listt;
    }
    public void yyy(){
        Log.v("touching_s","are_called");
    }

    @Override
    public void run() {
        try {
           int currentPos=video_view.getDuration();
           if(currentPos==-1){
               currentPos=1;
               Log.v("logCat_currentPOs", String.valueOf(video_view.getCurrentPosition())+" Posss"+currentPos);
           }

            boolean findVal=video_view.getCurrentPosition()<current_pos;


            while(video_view.getCurrentPosition()<current_pos) {
                Thread.sleep(10);
                if(video_view.isPlaying()){
                    Log.v("logCat_currentPOs", String.valueOf(video_view.getCurrentPosition()));
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(video_view.getCurrentPosition());
                        }
                    });
                }


            }
        } catch (Exception e) {
            //Log.v("videoPlayingHeee", e.getMessage());
            //return;
        }
    }

    public interface touchGesture{
        void touches();
        void unTouches();
    }

    public static ParcelFileDescriptor openFile(File file) {
        ParcelFileDescriptor descriptor;
        try {
            descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return descriptor;
    }

    public Bitmap getBitmap(File file){
        int pageNum = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(context);
        try {
            PdfDocument pdfDocument = pdfiumCore.newDocument(openFile(file));
            pdfiumCore.openPage(pdfDocument, pageNum);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);


            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(width , height ,
                    Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0,
                    width, height);
            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param

            pdfiumCore.closeDocument(pdfDocument); // important!
            return bitmap;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
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

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static String getPAth(){
        return allpath.get(presentSlidePos);
    }
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {

        StoreImageTypeUrl mm=slideDescribe.get(position);
        presentSlidePos=position;
        objsd=object;
        Log.v("printing_postionand",DetailingFullScreenImageViewActivity.brandName+" position "+mm.getSlideUrl()+" name "+mm.getSlideNam()+presentSlidePos);
        //Toast.makeText(context,String.valueOf(storingSlide.size()),Toast.LENGTH_SHORT).show();

        //finalArray.add(new StoreImageTypeUrl(saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentTime(),saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentDate(),mm.getBrdName(),mm.getSlideNam(),mm.getSlideTyp(),mm.getSlideUrl()));
        //Log.v("printing_postionand",DetailingFullScreenImageViewActivity.brandName+" position "+position);

        storingSlide.add(new LoadBitmap(saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods.getCurrentTime(),position,saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods.getCurrentDate(),mm.getSlideNam()));
           // slidessDet.add(new SlideDetail(DetailingFullScreenImageViewActivity.brandName,String.valueOf(position),saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentTime(),slidess.get(position).getSlideNam()));
            preVal=true;
            if(storeVideoPos.contains(position)){

            }
            else {
                if (videoCount > 0) {
                    if (video_view.isPlaying()) {
                        video_view.setVisibility(View.INVISIBLE);
                        //visible_controller.setVisibility(View.VISIBLE);
                        ll_media_controller.setVisibility(View.INVISIBLE);
                    }
                }
            }

            if(productChangeListener!=null)
            productChangeListener.checkPosition(position);
/*
            if(videoCount>0){
                if(video_view.isPlaying()){
                    video_view.setVisibility(View.INVISIBLE);
                    //visible_controller.setVisibility(View.VISIBLE);
                    ll_media_controller.setVisibility(View.INVISIBLE);
                    seekBar.setProgress(0);
                }


            }
*/


        //super.setPrimaryItem(container, position, object);
      /*  int timecount=mCommonSharedPreference.getValueFromPreferenceFeed("timeCount",0);
        Log.v("time_countt", String.valueOf(timecount));
        String time_val=saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentTime();
        mCommonSharedPreference.setValueToPreferenceFeed("timeVal"+timecount,time_val);
        mCommonSharedPreference.setValueToPreferenceFeed("dateVal"+timecount,saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentDate());
        mCommonSharedPreference.setValueToPreferenceFeed("brd_nam"+timecount,DetailingFullScreenImageViewActivity.brandName);
        mCommonSharedPreference.setValueToPreferenceFeed("slide_nam"+timecount,slidess.get(position).getSlideNam());
        mCommonSharedPreference.setValueToPreferenceFeed("slide_typ"+timecount,slidess.get(position).getSlideTyp());
        mCommonSharedPreference.setValueToPreferenceFeed("slide_url"+timecount,slidess.get(position).getSlideUrl());
        mCommonSharedPreference.setValueToPreferenceFeed("timeCount",++timecount);
*/
    }

    public void popupScribbling(final int x, final String path){
         dialog1=new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setContentView(R.layout.detailing_bottom_popup);
        dialog1.setCanceledOnTouchOutside(true);
        Window window = dialog1.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        RelativeLayout main_lay=(RelativeLayout)dialog1.findViewById(R.id.main_lay);
        final ImageView like=(ImageView)dialog1.findViewById(R.id.like);
        final ImageView dislike=(ImageView)dialog1.findViewById(R.id.dislike);
        ImageView share=(ImageView)dialog1.findViewById(R.id.share);
        ImageView paint=(ImageView)dialog1.findViewById(R.id.paint);
        ImageView feedback=(ImageView)dialog1.findViewById(R.id.feedback);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        dislike.setImageResource(R.drawable.dislike_off);
        like.setImageResource(R.drawable.like_off);

        dbh.open();
        Cursor mCursor=dbh.select_scribbleSearchByPath(path);
        if(mCursor!=null && mCursor.getCount()>0){
            while (mCursor.moveToNext()){
                if(mCursor.getString(2).equalsIgnoreCase("true")) {
                    like.setImageResource(R.drawable.like_on);
                    likes="true";
                }
                if(mCursor.getString(3).equalsIgnoreCase("true")) {
                    dislike.setImageResource(R.drawable.dislike_on);
                    dislikes="true";
                }

            }
        }
        else{
            dbh.insertScrible(path,"false","false","","");
            likes="false";
            dislikes="false";
        }

        dbh.close();

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(likes.equalsIgnoreCase("true")){
                    like.setImageResource(R.drawable.like_off);
                    likes="false";

                }
                else{
                    like.setImageResource(R.drawable.like_on);
                    dislike.setImageResource(R.drawable.dislike_off);
                    likes="true";
                    dislikes="false";
                }
                dbh.open();
                dbh.updatePathScribBoth(path,likes,dislikes);
                dbh.close();
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dislikes.equalsIgnoreCase("true")){
                    dislike.setImageResource(R.drawable.dislike_off);
                    dislikes="false";
                }
                else{
                    dislike.setImageResource(R.drawable.dislike_on);
                    like.setImageResource(R.drawable.like_off);
                    dislikes="true";
                    likes="false";
                }
                dbh.open();
                dbh.updatePathScribBoth(path,likes,dislikes);
                dbh.close();

            }
        });
        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupPaint(path);
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedBackPopup();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                final File photoFile = new File(path);
                if(x==1) {
                    try {
                        shareIntent.setType("image/jpg");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
                        context.startActivity(Intent.createChooser(shareIntent, "Share image using"));
                    }catch (Exception e){
                        shareImage(photoFile,1);
                    }
                }
                else if(x==2){
                    try {
                        shareIntent.setType("application/pdf");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
                        // share.setPackage("com.whatsapp");
                        context.startActivity(Intent.createChooser(shareIntent, "Share pdf using"));
                    }catch (Exception e){
                        shareImage(photoFile,2);
                    }
                }
                else if(x==3){
                    try {
                        File videoFile = new File(path);
                        Uri videoURI = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                                ? FileProvider.getUriForFile(context, context.getPackageName(), videoFile)
                                : Uri.fromFile(videoFile);
                        ShareCompat.IntentBuilder.from(act)
                                .setStream(videoURI)
                                .setType("video/mp4")
                                .setChooserTitle("Share video...")
                                .startChooser();
                    }catch (Exception e){
                        shareImage(photoFile,3);
                    }
                }
                else{
                    Log.v("wehview_sharess",path);
                        File webfile = new File(path);
                        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                            intentShareFile.setType("application/zip");
                       // intentShareFile.setType("text/*");
                        //intentShareFile.setType(URLConnection.guessContentTypeFromName(webfile.getName()));
                    Log.v("webview_path_arre",webfile.getAbsolutePath());
/*
                        intentShareFile.putExtra(Intent.EXTRA_STREAM,
                                Uri.parse("file://"+webfile.getAbsolutePath()));
*/
                        /*intentShareFile.putExtra(Intent.EXTRA_STREAM,
                                Uri.parse("content://"+webfile.getAbsolutePath()));*/
                        intentShareFile.putExtra(Intent.EXTRA_STREAM,
                                Uri.fromFile(webfile));

                        //if you need
                        //intentShareFile.putExtra(Intent.EXTRA_SUBJECT,"Sharing File Subject);
                        //intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File Description");

                    context.startActivity(Intent.createChooser(intentShareFile, "Share File"));


                }

            }
        });
        params.setMargins(0, 0, 0, 30);
        //main_lay.setLayoutParams(params);
        wlp.gravity = Gravity.BOTTOM|Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog1.show();
    }

    private void shareImage(File path,int x){
        // save bitmap to cache directory
       /* try {
            File cachePath = new File(this.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
       /* File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(path, "image.png");*/
        Log.v("full_point_set",path+"");
        Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", path);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            if(x==1)
                shareIntent.setType("image/png");
            else if(x==2)
                shareIntent.setType("application/pdf");
            else if(x==3)
                shareIntent.setType("video/mp4");

            context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

    public void popupPaint(final String path){
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_scribble);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Display display = act.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.e("Widthsss", "" + width);
        Log.e("heightsss", "" + height);

        RelativeLayout rlay=(RelativeLayout)dialog.findViewById(R.id.r_lay);
        final RelativeLayout canvas_lay=(RelativeLayout)dialog.findViewById(R.id.canvas_lay);
        final ImageView erase=(ImageView)dialog.findViewById(R.id.erase);
        final ImageView pen_black=(ImageView)dialog.findViewById(R.id.pen_black);
        final ImageView pen_red=(ImageView)dialog.findViewById(R.id.pen_red);
        final ImageView pen_green=(ImageView)dialog.findViewById(R.id.pen_green);
        final ImageView sq=(ImageView)dialog.findViewById(R.id.sq);
        final ImageView cir=(ImageView)dialog.findViewById(R.id.cir);
        final ImageView canva_img=(ImageView)dialog.findViewById(R.id.canva_img);
        final ImageView img_close=(ImageView)dialog.findViewById(R.id.img_close);
        final PaintView paintviews=(PaintView)dialog.findViewById(R.id.paintviews);
        Button submit=(Button)dialog.findViewById(R.id.submit);
        ViewGroup.LayoutParams layoutParams = rlay.getLayoutParams();
        layoutParams.width = width-80;
        rlay.setLayoutParams(layoutParams);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(45, 35);
                //lp.setMargins(left, 10, right, bottom);
                lp.setMargins(0, 10, 0, 0);
                pen_black.setLayoutParams(lp);*/
                paintviews.erase();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureCanvasScreen(canvas_lay,dialog,path);
            }
        });
        sq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintviews.addRectangle();
            }
        });
        cir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintviews.addCircle();
            }
        });
        pen_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(45, 35);
                //lp.setMargins(left, 10, right, bottom);
                lp.setMargins(0, 10, 0, 0);
                pen_black.setLayoutParams(lp);*/
                paintviews.changePaintColor(1);
            }
        });
        pen_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(45, 35);
                //lp.setMargins(left, 10, right, bottom);
                lp.setMargins(0, 10, 0, 0);
                pen_black.setLayoutParams(lp);*/
                paintviews.changePaintColor(2);
            }
        });
        pen_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(45, 35);
                //lp.setMargins(left, 10, right, bottom);
                lp.setMargins(0, 10, 0, 0);
                pen_black.setLayoutParams(lp);*/
                paintviews.changePaintColor(3);
            }
        });
        //canva_img.setImageURI(Uri.parse("/storage/emulated/0/Newproduct/2LIPIMOD_PHY.jpg"));
        String pathName = path;
        Drawable d = Drawable.createFromPath(pathName);
        canva_img.setBackground(d);
    }


    public void feedBackPopup(){
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.detailing_pop_feed);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final EditText edt_feed=(EditText)dialog.findViewById(R.id.edt_feed);
        RelativeLayout edt_lay=(RelativeLayout)dialog.findViewById(R.id.edt_lay);
        ImageView img_close=(ImageView)dialog.findViewById(R.id.img_close);
        final StoreImageTypeUrl mm=slideDescribe.get(presentSlidePos);


        if (jsonArrayFeed.length() != 0) {
            try {
            JSONArray jsonArray=jsonArrayFeed;
            Log.v("json_array_print", String.valueOf(jsonArray));
            jsonArrayFeed=new JSONArray();
            for(int k=0;k<jsonArray.length();k++){

                    JSONObject jss=jsonArray.getJSONObject(k);
                    if(mm.getSlideUrl().equalsIgnoreCase(jss.getString("id"))){
                        edt_feed.setText(jss.getString("feedBack"));
                    }
                    else{
                        jsonArrayFeed.put(jss);
                    }

            }
                Log.v("json_array_printeeedd", String.valueOf(jsonArrayFeed));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        edt_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoftKeyboard(edt_feed);
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                slideFeedbacks.add(new StoreDetailingFeedback(edt_feed.getText().toString(),mm.getSlideid(),presentSlidePos));

                if(!TextUtils.isEmpty(edt_feed.getText().toString())) {
                    if (jsonArrayFeed.length() == 0) {

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("feedBack", edt_feed.getText().toString());
                            jsonObject.put("id", mm.getSlideUrl());
                            jsonObject.put("pos", presentSlidePos);
                            jsonArrayFeed.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            JSONObject js = new JSONObject();
                    /*for(int i=0;i<jsonArrayFeed.length();i++){

                            js=jsonArrayFeed.getJSONObject(i);
                    }*/
                            js.put("feedBack", edt_feed.getText().toString());
                            js.put("id", mm.getSlideUrl());
                            js.put("pos", presentSlidePos);
                            jsonArrayFeed.put(js);
                            Log.v("jsonArray_feed", jsonArrayFeed.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mCommonSharedPreference.setValueToPreference("slide_feed",jsonArrayFeed.toString());
                dialog.dismiss();
            }
        });

    }
    public void showSoftKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view ,
                InputMethodManager.SHOW_IMPLICIT);

    }
    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }
    private void getBitmapFromURI(String  additionalImage, ByteArrayOutputStream byteArrayOutputStream){
        Log.d(TAG, "getBitmapFromURI: " + additionalImage);
        Log.d(TAG, "getBitmapFromURI: " + additionalImage);
        Bitmap btm = null;
        try {
            /*if(additionalImage.isCamera) {
                btm = BitmapFactory.decodeFile(additionalImage.url);
                if(btm != null) {
                    btm.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                    btm.recycle();
                }
            } else*/ {
                btm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(additionalImage));
                if(btm != null) {
                    if(btm.getByteCount() <  Runtime.getRuntime().freeMemory()) {
                        btm.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        btm.recycle();
                    }else{
                        Bitmap resized = Bitmap.createScaledBitmap(btm, btm.getWidth(), btm.getHeight(), true);
                        btm = resized;
                        btm.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        btm.recycle();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (OutOfMemoryError e){
            e.printStackTrace();
        }finally {
            if(btm != null)
                Log.d(TAG, "getBitmapFromURI: btm" + btm);
        }
    }
    public Bitmap getResizedBitmap(String path) {
        int width=0;
        int height=0;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(path, bmOptions);
        try {
            width= bm.getWidth();
            height = bm.getHeight();
        }catch (Exception e){
            Log.v("TASK_SIZE_are_ptint",e.getMessage());
        }

       /* float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);*/

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height);
        if (bm != null && !bm.isRecycled())
            bm.recycle();

        return resizedBitmap;
    }

    public  String captureCanvasScreen(View layBg,Dialog dialog,String imgpath)
    {
        View content = layBg;
        content.setDrawingCacheEnabled(true);
        content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = content.getDrawingCache();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path,"EDetails/Pictures");
        if(!file.exists()){
            file.mkdirs();
        }
        String file_path=file+"/"+"paint_"+System.currentTimeMillis()+".png";
        FileOutputStream ostream;
        try {
            ostream = new FileOutputStream(file_path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.flush();
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* dbh.open();
        dbh.updatePathScrib(imgpath,file_path);
        dbh.close();*/
       Log.v("scribble_path_print",file_path);
        MultipartBody.Part imgg= convertimg("ScribbleImg",file_path);
        Log.v("scibble_multipart", String.valueOf(imgg));
        sendScribbleImg(imgg,dialog);

        return file_path;
    }

    public MultipartBody.Part createMultipart(String path){
        MultipartBody.Part body;
       // File file = new File("/storage/emulated/0/Download/Corrections 6.jpg");
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
        body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        return body;
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String path)
    {

        MultipartBody.Part filebody=null;
        if(path!=null)
        {
            File file=new File(path);
            RequestBody tempbody=RequestBody.create(MediaType.parse("multipart/form-data"),file);
            filebody=MultipartBody.Part.createFormData("ScribbleImg",file.getName(),tempbody);
        }
        // create RequestBody instance from file


        // MultipartBody.Part is used to send also the actual file name
        return filebody;}

    public MultipartBody.Part convertimg(String tag,String path){
        MultipartBody.Part yy=null;
        //Log.v("full_profile",path);

        if(!TextUtils.isEmpty(path))
        {
            File file = new File(path);
            RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
            yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
        }

        return yy;
    }



    public void sendScribbleImg(MultipartBody.Part img, final Dialog dialog){
        HashMap<String,RequestBody> values=field("MR0417");
        Toast.makeText(context,"Processing ",Toast.LENGTH_LONG).show();
        Call<ResponseBody> upload=apiService.uploadScrib(values,img);
        upload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("uploading_scr", String.valueOf(response.isSuccessful()));
                if (response.isSuccessful()) {

                    InputStreamReader ip=null;
                    StringBuilder is=new StringBuilder();
                    String line=null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                        Log.v("printing_scribble",is.toString());
                        JSONObject json=new JSONObject(is.toString());
                        if(json.getString("success").equalsIgnoreCase("true")){
                            Toast.makeText(context,"Scribble Uploaded Successfully ",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }catch (Exception e){
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("printing_scribble","Exception"+t);
            }
        });
    }

    public HashMap<String, RequestBody> field(String val){
        HashMap<String,RequestBody> xx=new HashMap<String,RequestBody>();
        xx.put("data",createFromString(val));

        return xx;

    }
    private RequestBody createFromString(String txt)
    {
        return RequestBody.create(MultipartBody.FORM,txt);
    }

/*
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            Log.v("mScaleFactor",mScaleFactor+"");
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            Log.v("scalor_here",mScaleFactor+"");
            if(objectsd!=null) {
                rlay= (RelativeLayout) objectsd;
                rlay.setScaleX(mScaleFactor);
                rlay.setScaleY(mScaleFactor);
            }
            return true;
        }
    }
*/
public static   void    bindZoomListener(UpdateUi uu){
    updateUi=uu;
}
}
