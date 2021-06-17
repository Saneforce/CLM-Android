package saneforce.sanclm.activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.LoadBitmap;
import saneforce.sanclm.activities.Model.StoreImageTypeUrl;
import saneforce.sanclm.adapter_class.MyCustomPagerAdapter;
import saneforce.sanclm.adapter_class.RecyclerFullScreenImage;
import saneforce.sanclm.adapter_class.SpecialityAdapter;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.fragments.Detailing_Selection_search_grid_selection;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.ProductChangeListener;
import saneforce.sanclm.util.SpecialityListener;
import saneforce.sanclm.util.UpdateUi;

public class DetailingFullScreenImageViewActivity extends FragmentActivity implements Detailing_Selection_search_grid_selection.Communicator{


    saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods CommonUtilsMethods;
    ViewPager viewPager;
    CommonSharedPreference mCommonSharedPreference;
    static CommonSharedPreference mCommonSharedPreference1;
    MyCustomPagerAdapter myCustomPagerAdapter;
    LinearLayout ll_lay;
    int doubleTouchCount=0;
    RelativeLayout rll_overlay;
    FrameLayout frame;
    ImageView btn_stopdetailing,img_zoom;
    RecyclerFullScreenImage recyclerFullScreenImage;
    MyRecyclerViewAdapter adp;
    DetailingTrackerPOJO mDetailingTrackerPOJO;
    static DataBaseHandler dbh;
    Cursor mCursor;
    public static int videoTap=0;
    public static String brandName,slideName;
    public static ArrayList<Integer> videoPositionCount=new ArrayList<>();
    ArrayList<StoreImageTypeUrl>slidess=new ArrayList<>();
    ArrayList<LoadBitmap>bitmapLoader=new ArrayList<>();
    String prdName;
    int startint=0;
    ArrayList<String> prdNAme=new ArrayList<>();
    ArrayList<String> prdCodeing=new ArrayList<>();
    ArrayList<Bitmap> bitMapList=new ArrayList<>();
    boolean checking=true;
    static Context context;
    static ArrayList<String> arr=new ArrayList<>();
    static ArrayList<String> code=new ArrayList<>();
    static SpecialityListener specialityListener;
    static Dialog dialog;
    static Intent iii;
    boolean returnValue=true;
    ProgressDialog progressDialog=null;
    RecyclerView recyclerView;
    static UpdateUi updateUi;

    String digital="";

    @Override
    public void onBackPressed() {
        /*mCommonSharedPreference.clearFeedShare();
        Intent i=new Intent(DetailingFullScreenImageViewActivity.this,HomeDashBoard.class);
        startActivity(i);*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detailingfullscreen_imageview);
        mCommonSharedPreference = new CommonSharedPreference(this);
        mCommonSharedPreference1 = new CommonSharedPreference(this);

        digital=mCommonSharedPreference.getValueFromPreference("Digital_offline");

        Bundle extra=getIntent().getExtras();
        if(extra!=null) {
            prdName = extra.getString("pname");
            Log.v("detailing_pname",prdName);
        }
        dialog=new Dialog(DetailingFullScreenImageViewActivity.this,R.style.AlertDialogCustom);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
        rll_overlay=(RelativeLayout)findViewById(R.id.rll_overlay);
        ll_lay=(LinearLayout) findViewById(R.id.ll_lay);
        frame=(FrameLayout)findViewById(R.id.frame);
        btn_stopdetailing=(ImageView)findViewById(R.id.btn_stopdetailing);
        //img_zoom=(ImageView)findViewById(R.id.img_zoom);
        dbh = new DataBaseHandler(this);
       recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        videoPositionCount.clear();
        fun(getApplicationContext());
        Detailing_Selection_search_grid_selection.checkActivity=true;
         iii=new Intent(DetailingFullScreenImageViewActivity.this,DetailingFullScreenImageViewActivity.class);
        if (progressDialog == null) {
            CommonUtilsMethods commonUtilsMethods=new CommonUtilsMethods(DetailingFullScreenImageViewActivity.this);
            progressDialog = commonUtilsMethods.createProgressDialog(DetailingFullScreenImageViewActivity.this);
            progressDialog.dismiss();
        }
        else{
            progressDialog.dismiss();
        }
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                   String slideData = mCommonSharedPreference.getValueFromPreference("ProductBrdWiseSlides_jsonArray");

                    JSONArray Orig_jsonSLides_data = new JSONArray(slideData);
                    for (int i = 0; i < Orig_jsonSLides_data.length(); i++) {
                        JSONObject jsonObject = Orig_jsonSLides_data.getJSONObject(i);
                        String brdNAme = jsonObject.getString("BrdName");
                        String brdCOde = jsonObject.getString("BrdCode");
                        JSONArray jsonSlide = jsonObject.getJSONArray("Slides");
                        for (int j = 0; j < jsonSlide.length(); j++) {
                            JSONObject json = jsonSlide.getJSONObject(j);
                            Log.v("slide_printing_nam", json.getString("SlideName"));

                            if(json.getString("SlideType").equalsIgnoreCase("h"))
                            {
                                String HTMLPath=json.getString("SlideLocUrl").replaceAll(".zip","");
                                File f = new File(HTMLPath);
                                final File[] files=f.listFiles(new FilenameFilter() {
                                    @Override
                                    public boolean accept(File dir, String filename) {
                                        return filename.contains(".png")|| filename.contains(".jpg");
                                    }
                                });
                                String url="";
                                try {
                                    if (files.length > 0) url = files[0].getAbsolutePath();
                                    Uri imageUri1 = Uri.fromFile(new File(url));
                                    Bitmap bm = BitmapFactory.decodeStream(
                                            getContentResolver().openInputStream(imageUri1));
                                    bitMapList.add(bm);
                                    //holder.imgview.setImageURI(imageUri1);
                                }catch (Exception e){}
                            }
                            else if(json.getString("SlideType").equalsIgnoreCase("v")){
                                // String url="/storage/emulated/0/Products/Demo_Html/video/Horlicks1.mp4";
                                String url=json.getString("SlideLocUrl");
                                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(url,
                                        MediaStore.Images.Thumbnails.MINI_KIND);
                                bitMapList.add(thumb);

                            }
                            else if(json.getString("SlideType").equalsIgnoreCase("p")){
                               // getBitmap(new File(json.getString("SlideLocUrl")));
                                bitMapList.add(getBitmap(new File(json.getString("SlideLocUrl"))));
                               // holder.imgview.setImageBitmap(getBitmap(new File(imgurl)));
                            }
                            else {
                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                Bitmap bitmap = BitmapFactory.decodeFile(json.getString("SlideLocUrl"),bmOptions);
                                try{
                                    bitmap = Bitmap.createScaledBitmap(bitmap,250,250,true);
                                    bitMapList.add(bitmap);

                                }catch (Exception e){

                                }

                            }

                        }
                    }
                    Log.v("size_of_nbitmap_inserc", String.valueOf(bitMapList.size()));

                }catch (Exception e){}
            }
        }).start();
*/

/*
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            */
/*Interpolator sInterpolator = new AccelerateInterpolator();*//*

            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());

            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
*/
        SharedPreferences sharedPreferences=getSharedPreferences("left_fragment",0);
        SharedPreferences.Editor editt=sharedPreferences.edit();
        editt.putString("left","2");
        editt.commit();


    try {
       /* final String data = mCommonSharedPreference.getValueFromPreference("CurrentBrdSlides_Json_array");
        Log.e("DadaD",data);
        JSONArray json_array = new JSONArray(data);*/
/*

            Field mScroller = viewPager.getClass().getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            Scroller scroll = new Scroller(DetailingFullScreenImageViewActivity.this);
            Field scrollDuration = scroll.getClass().getDeclaredField("mDuration");
            scrollDuration.setAccessible(true);
            scrollDuration.set(scroll, 1);
            mScroller.set(viewPager, scroll);
*/


        slidess.clear();
/*
        for (int i = 0; i < json_array.length(); i++) {
            JSONObject jsonChildNode = json_array.getJSONObject(i);
            String imgname = jsonChildNode.optString("SlideName");
            String slideUrl = jsonChildNode.optString("SlideLocUrl");
            String slideType = jsonChildNode.optString("SlideType");
            slidess.add(new StoreImageTypeUrl(imgname,slideType,slideUrl));
            brandName = jsonChildNode.optString("brandName");
            if(slideType.equalsIgnoreCase("v")){
                videoPositionCount.add(i);
            }
            else if(slideType.equalsIgnoreCase("p")){
                Bitmap bb=getBitmap(new File(slideUrl));
                String val=BitMapToString(bb);
                Log.v("url_img_slide",""+" possss "+i);

                mCommonSharedPreference.setValueToPreference("pdfBitmap"+i,val);

            }
        }
*/
        Log.v("total_slide_lenth", String.valueOf(Detailing_Selection_search_grid_selection.len_slide));
        myCustomPagerAdapter = new MyCustomPagerAdapter(DetailingFullScreenImageViewActivity.this, Detailing_Selection_search_grid_selection.len_slide,slidess);
        viewPager.setAdapter(myCustomPagerAdapter);
        myCustomPagerAdapter.notifyDataSetChanged();

         int posSlide = -1;
        if(!TextUtils.isEmpty(prdName)) {
            String prdCodes=mCommonSharedPreference.getValueFromPreference("prdCodee");
            String slideData = mCommonSharedPreference.getValueFromPreference("ProductBrdWiseSlides_jsonArray");
            try {

                JSONArray Orig_jsonSLides_data = new JSONArray(slideData);
                for (int i = 0; i < Orig_jsonSLides_data.length(); i++) {
                    JSONObject jsonObject = Orig_jsonSLides_data.getJSONObject(i);
                    String brdNAme = jsonObject.getString("BrdName");
                    String brdCOde = jsonObject.getString("BrdCode");
                    JSONArray jsonSlide=jsonObject.getJSONArray("Slides");
                    for(int j=0;j<jsonSlide.length();j++) {

                        JSONObject jsObj=jsonSlide.getJSONObject(j);
                        Log.v("brdName_val",brdNAme+" prdNAme "+prdName+" prd_code"+jsObj.getString("PrdCode"));
                        if (brdNAme.equalsIgnoreCase(prdName) && prdCodes.equalsIgnoreCase(jsObj.getString("PrdCode"))) {
                            Log.v("brdName_val_finder",brdNAme+" prdNAme "+prdName+" prd_code"+jsObj.getString("SlideLocUrl"));
                            ++posSlide;
                            j=jsonSlide.length();
                            i=Orig_jsonSLides_data.length();
                            Log.v("j_value_are",j+"");


                        } else {
                            ++posSlide;
                            Log.v("posSlide_printing", "are_nt_match");
                        }
                    }

                }
                startint=posSlide;
                Log.v("posSlide_printing", String.valueOf(posSlide));
                viewPager.setCurrentItem(posSlide,true);


            } catch (Exception e) {
                Log.v("pos_array_list_ex", String.valueOf(e));
            }
        }
        else{
            Log.v("posSlide_printing","are_empty");
        }
        final String time_val=saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods.getCurrentTime();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v("handler_delaye","are_Created_here");
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
                gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
                recyclerView.setLayoutManager(gridLayoutManager);

                adp=new MyRecyclerViewAdapter(DetailingFullScreenImageViewActivity.this,"dfasdfa");
                recyclerView.setAdapter(adp);
                adp.notifyDataSetChanged();


              /*  int timecount=mCommonSharedPreference.getValueFromPreferenceFeed("timeCount",0);
                if(timecount==0){
                    Log.v("time_val_count", String.valueOf(timecount));

                    mCommonSharedPreference.setValueToPreferenceFeed("timeVal"+timecount,time_val);
                    mCommonSharedPreference.setValueToPreferenceFeed("dateVal"+timecount,saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentDate());
                    mCommonSharedPreference.setValueToPreferenceFeed("brd_nam"+timecount,brandName);
                    mCommonSharedPreference.setValueToPreferenceFeed("slide_nam"+timecount,slidess.get(0).getSlideNam());
                    mCommonSharedPreference.setValueToPreferenceFeed("slide_typ"+timecount,slidess.get(0).getSlideTyp());
                    mCommonSharedPreference.setValueToPreferenceFeed("slide_url"+timecount,slidess.get(0).getSlideUrl());
                    mCommonSharedPreference.setValueToPreferenceFeed("timeCount",++timecount);
                }
                else{
                    Log.v("time_val_count22", String.valueOf(slidess.get(0).getSlideUrl()));
                    String time_val=saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentTime();
                    mCommonSharedPreference.setValueToPreferenceFeed("timeVal"+timecount,time_val);
                    mCommonSharedPreference.setValueToPreferenceFeed("dateVal"+timecount,saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentDate());
                    mCommonSharedPreference.setValueToPreferenceFeed("brd_nam"+timecount,brandName);
                    mCommonSharedPreference.setValueToPreferenceFeed("slide_nam"+timecount,slidess.get(0).getSlideNam());
                    mCommonSharedPreference.setValueToPreferenceFeed("slide_typ"+timecount,slidess.get(0).getSlideTyp());
                    mCommonSharedPreference.setValueToPreferenceFeed("slide_url"+timecount,slidess.get(0).getSlideUrl());
                    mCommonSharedPreference.setValueToPreferenceFeed("timeCount",++timecount);
                }*/

            }
        },200);




       /* adp.bindlistner(new smoothScroll() {
            @Override
            public void scrolling(int id) {
                //recyclerView_displayview.getLayoutManager().scrollToPosition(4);

            }
        });
*/
        }catch(Exception e){}

        MyCustomPagerAdapter.bindlistner(new MyCustomPagerAdapter.touchGesture() {
            @Override
            public void touches() {
                Log.v("recyclerview_touches","are_clicked_here");
                // adp.notifyDataSetChanged();
                rll_overlay.setVisibility(View.VISIBLE);
                adp.notifyDataSetChanged();

            }

            @Override
            public void unTouches() {
                rll_overlay.setVisibility(View.INVISIBLE);
            }
        });

/*
        OnSwipeTouchListener.bindImageVisible(new GridSelectionList() {
            @Override
            public void selectionList(String prdNAme) {
                Log.v("img_visibility","visible");
                img_zoom.setVisibility(View.VISIBLE);
            }

            @Override
            public void unselectionList(String prdNAme) {
                Log.v("img_visibility","gone");
                img_zoom.setVisibility(View.GONE);
            }
        });
*/

/*
        img_zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUi.updatingui();
            }
        });
*/

/*
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.v("postion_scroll", String.valueOf(position)+"offset "+positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                Log.v("postion_page_select", String.valueOf(position)+"video_pos_count"+videoPositionCount);


                if(videoPositionCount.contains(position)){
                    videoTap=1;
                }
                else{
                    videoTap=0;
                }
                int timecount=mCommonSharedPreference.getValueFromPreferenceFeed("timeCount",0);
                Log.v("time_countt", String.valueOf(timecount));
                String time_val=saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentTime();
                mCommonSharedPreference.setValueToPreferenceFeed("timeVal"+timecount,time_val);
                mCommonSharedPreference.setValueToPreferenceFeed("dateVal"+timecount,saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentDate());
                mCommonSharedPreference.setValueToPreferenceFeed("brd_nam"+timecount,brandName);
                mCommonSharedPreference.setValueToPreferenceFeed("slide_nam"+timecount,slidess.get(position).getSlideNam());
                mCommonSharedPreference.setValueToPreferenceFeed("slide_typ"+timecount,slidess.get(position).getSlideTyp());
                mCommonSharedPreference.setValueToPreferenceFeed("slide_url"+timecount,slidess.get(position).getSlideUrl());
                mCommonSharedPreference.setValueToPreferenceFeed("timeCount",++timecount);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
*/


/*
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.v("touching_video", "are_called"+event.getActionMasked()+"videCount"+videoTap);
                int action = event.getActionMasked();

                if(videoTap==1) {
                    switch (action) {

                        case MotionEvent.ACTION_POINTER_DOWN:
                            Log.v("double_finget", "called");
                            int count = event.getPointerCount(); // Number of 'fingers' in this time
                            if (count == 2) {
                                Log.v("double_fingetssss", "called");
                                if (doubleTouchCount == 0) {
                                    doubleTouchCount = 1;
                                    rll_overlay.setVisibility(View.VISIBLE);
                                    // rll_overlay.setVisibility(View.VISIBLE);

                                } else {
                                    doubleTouchCount = 0;
                                    rll_overlay.setVisibility(View.GONE);
                                    //rll_overlay.setVisibility(View.GONE);
                                }
                            }
                    }
                }
                return false;
            }
        });
*/


        btn_stopdetailing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCommonSharedPreference.setValueToPreference("slide_endtime",CommonUtilsMethods.getCurrentTime());

               // progressDialog.show();

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
              /*  Toast toast=Toast.makeText(DetailingFullScreenImageViewActivity.this, "Processing", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();*/
                //Toast.makeText(DetailingFullScreenImageViewActivity.this,"Processing",Toast.LENGTH_SHORT).show();
//to get back the touch event
                //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if(MyCustomPagerAdapter.preVal){


                           /* Gson gson = new GsonBuilder().create();
                            JsonArray myCustomArray = gson.toJsonTree(MyCustomPagerAdapter.storingSlide).getAsJsonArray();
*/


                            int timecount=mCommonSharedPreference.getValueFromPreferenceFeed("timeCount",0);

                            for(int i=0;i<MyCustomPagerAdapter.storingSlide.size();i++) {
                                int ll=0;
                                if(i==0){

                                }
                                else{
                                    ll=i-1;
                                    if(MyCustomPagerAdapter.storingSlide.get(ll).getIndexVal()==MyCustomPagerAdapter.storingSlide.get(i).getIndexVal()) {
                                        Log.v("printing_nextt22",MyCustomPagerAdapter.storingSlide.get(i).getBrdName()+" index_val "+MyCustomPagerAdapter.storingSlide.get(i).getIndexVal()+" previous "+MyCustomPagerAdapter.storingSlide.get(ll).getBrdName()+" index "+MyCustomPagerAdapter.storingSlide.get(ll).getIndexVal()+"timr"+MyCustomPagerAdapter.storingSlide.get(i).getTiming());
                                        MyCustomPagerAdapter.storingSlide.remove(i);
                                        Log.v("printing_nextt53_",MyCustomPagerAdapter.storingSlide.size()+"");
                                        //i=ll;
                                    }
                                    else {
                                        Log.v("CustompagerAdap", MyCustomPagerAdapter.storingSlide.get(i).getIndexVal() + " " + MyCustomPagerAdapter.storingSlide.get(ll).getIndexVal());
                                        Log.v("printing_nextt23",MyCustomPagerAdapter.storingSlide.get(i).getBrdName()+"timr"+MyCustomPagerAdapter.storingSlide.get(i).getTiming());
                                    }

                                }

                            }
                            Log.v("printing_nextt43",MyCustomPagerAdapter.storingSlide.size()+"");
                            for(int i=0;i<MyCustomPagerAdapter.storingSlide.size();i++) {
                                Log.v("total_printing",MyCustomPagerAdapter.storingSlide.get(i).getBrdName()+"size"+MyCustomPagerAdapter.storingSlide.size()+"slide"+MyCustomPagerAdapter.storingSlide.get(i).getIndexVal());
                            }
                            //Toast.makeText(DetailingFullScreenImageViewActivity.this,String.valueOf(MyCustomPagerAdapter.storingSlide.size()),Toast.LENGTH_SHORT).show();

                            for(int i=0;i<MyCustomPagerAdapter.storingSlide.size();i++){
                                int ll=0;
                                if(i!=0)
                                    ll=i-1;
                                if(i==0||MyCustomPagerAdapter.storingSlide.get(ll).getIndexVal()!=MyCustomPagerAdapter.storingSlide.get(i).getIndexVal()) {

                                    dbh.open();
                                    Cursor cur = dbh.select_searchSlideByName(MyCustomPagerAdapter.storingSlide.get(i).getBrdName());
                                    Log.v("cur_sor_count", String.valueOf(cur.getCount()));
                                    if (cur.getCount() > 0) {
                                        while (cur.moveToNext()) {
                                            Log.v("printing_nextt", MyCustomPagerAdapter.storingSlide.get(i).getBrdName() + " " + MyCustomPagerAdapter.storingSlide.get(i).getTiming());
                                            // Toast.makeText(DetailingFullScreenImageViewActivity.this,String.valueOf(MyCustomPagerAdapter.storingSlide.get(i).getBrdName()),Toast.LENGTH_SHORT).show();

                                            mCommonSharedPreference.setValueToPreferenceFeed("timeVal" + timecount, MyCustomPagerAdapter.storingSlide.get(i).getTiming());
                                            mCommonSharedPreference.setValueToPreferenceFeed("dateVal" + timecount, MyCustomPagerAdapter.storingSlide.get(i).getDateVal());
                                            mCommonSharedPreference.setValueToPreferenceFeed("brd_nam" + timecount, cur.getString(3));
                                            mCommonSharedPreference.setValueToPreferenceFeed("slide_nam" + timecount, MyCustomPagerAdapter.storingSlide.get(i).getBrdName());
                                            mCommonSharedPreference.setValueToPreferenceFeed("slide_typ" + timecount, cur.getString(6));
                                            mCommonSharedPreference.setValueToPreferenceFeed("slide_url" + timecount, cur.getString(12));
                                            mCommonSharedPreference.setValueToPreferenceFeed("timeCount", ++timecount);
                                        }
                                    }
                                }
                            }

/*
                            for(int i=0;i<MyCustomPagerAdapter.storingSlide.size();i++) {

                                Log.v("time_countt_adapter", String.valueOf(MyCustomPagerAdapter.storingSlide.get(i).getBrdName()));
                                int ll=0;
                                if(i==0){

                                }
                                else{
                                    ll=i-1;
                                }

                                int pos=0;
                                for(int k=0;k<MyCustomPagerAdapter.slidessDet.size();k++){
                                    //  Log.v("slideStatic_Value",MyCustomPagerAdapter.slidess.get(k).getSlideNam());

                                    if(MyCustomPagerAdapter.slidessDet.get(k).getSlideNam().equalsIgnoreCase(MyCustomPagerAdapter.storingSlide.get(i).getBrdName())){
                                        pos=k;
                                        Log.v("postion_value",pos+" ");
                                        k=MyCustomPagerAdapter.slidessDet.size();
                                    }
                                }

                                if(MyCustomPagerAdapter.storingSlide.get(ll).getIndexVal()!=MyCustomPagerAdapter.storingSlide.get(i).getIndexVal()) {
                                    int valCount=MyCustomPagerAdapter.storingSlide.get(i).getIndexVal();
                                    String time_val = saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentTime();
                                    mCommonSharedPreference.setValueToPreferenceFeed("timeVal" + timecount, MyCustomPagerAdapter.storingSlide.get(i).getTiming());
                                    mCommonSharedPreference.setValueToPreferenceFeed("dateVal" + timecount, MyCustomPagerAdapter.storingSlide.get(i).getDateVal());
                                    mCommonSharedPreference.setValueToPreferenceFeed("brd_nam" + timecount, MyCustomPagerAdapter.slidessDet.get(pos).getBrdName());
                                    mCommonSharedPreference.setValueToPreferenceFeed("slide_nam" + timecount, MyCustomPagerAdapter.slidessDet.get(pos).getSlideNam());
                                    mCommonSharedPreference.setValueToPreferenceFeed("slide_typ" + timecount, MyCustomPagerAdapter.slidessDet.get(pos).getSlideTyp());
                                    mCommonSharedPreference.setValueToPreferenceFeed("slide_url" + timecount, MyCustomPagerAdapter.slidessDet.get(pos).getSlideUrl());
                                    mCommonSharedPreference.setValueToPreferenceFeed("timeCount", ++timecount);
                                }
                                else if(i==0){
                                    int valCount=MyCustomPagerAdapter.storingSlide.get(i).getIndexVal();
                                    String time_val = saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentTime();
                                    mCommonSharedPreference.setValueToPreferenceFeed("timeVal" + timecount, MyCustomPagerAdapter.storingSlide.get(i).getTiming());
                                    mCommonSharedPreference.setValueToPreferenceFeed("dateVal" + timecount, MyCustomPagerAdapter.storingSlide.get(i).getDateVal());
                                    mCommonSharedPreference.setValueToPreferenceFeed("brd_nam" + timecount, MyCustomPagerAdapter.slidessDet.get(pos).getBrdName());
                                    mCommonSharedPreference.setValueToPreferenceFeed("slide_nam" + timecount, MyCustomPagerAdapter.slidessDet.get(pos).getSlideNam());
                                    mCommonSharedPreference.setValueToPreferenceFeed("slide_typ" + timecount, MyCustomPagerAdapter.slidessDet.get(pos).getSlideTyp());
                                    mCommonSharedPreference.setValueToPreferenceFeed("slide_url" + timecount, MyCustomPagerAdapter.slidessDet.get(pos).getSlideUrl());
                                    mCommonSharedPreference.setValueToPreferenceFeed("timeCount", ++timecount);
                                }
                            }
*/


                        }

                        MyCustomPagerAdapter.storingSlide.clear();
                        MyCustomPagerAdapter.slidessDet.clear();
                        MyCustomPagerAdapter.preVal=false;

                        mCommonSharedPreference.setValueToPreference("display_brand","");

                     /*   ArrayList<LoadBitmap> ay=new ArrayList<>();
                        ay.add(new LoadBitmap("xxx","yyy"));
                        ay.add(new LoadBitmap("xxx","yyy"));
                        ay.add(new LoadBitmap("xxx","yyy"));
                        ay.add(new LoadBitmap("xxx","yyy"));
                        Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(ay).getAsJsonArray();
                Log.v("printing_final_arr",myCustomArray.toString());*/


                if (digital.equalsIgnoreCase("1")) {
                    Intent i = new Intent(DetailingFullScreenImageViewActivity.this, DetailingCreationActivity.class);
                    //i.putExtra("detailing", "1");
                    startActivity(i);
                }
                else {

                    Intent i = new Intent(DetailingFullScreenImageViewActivity.this, FeedbackActivity.class);
                    //Intent i=new Intent(DetailingFullScreenImageViewActivity.this,DummyActivity.class);
                    if (mCommonSharedPreference.getValueFromPreference("detail_").equalsIgnoreCase("chm"))
                        i.putExtra("feedpage", "chemist");
                    else if (mCommonSharedPreference.getValueFromPreference("detail_").equalsIgnoreCase("stk")) {
                        i.putExtra("feedpage", "stock");
                    } else if (mCommonSharedPreference.getValueFromPreference("detail_").equalsIgnoreCase("undr")) {
                        i.putExtra("feedpage", "undr");
                    }else if (mCommonSharedPreference.getValueFromPreference("detail_").equalsIgnoreCase("cip")) {
                        i.putExtra("feedpage", "cip");
                    } else
                        i.putExtra("feedpage", "dr");
                    startActivity(i);
                }
            }
        });

    }

    @Override
    public void onResume() { CommonUtilsMethods.FullScreencall(this);super.onResume(); }
    @Override
    public void onPause() { super.onPause(); }

    @Override
    public void Message(String PdtBrdName, String productcode) {

        Log.v("prd_name_display",PdtBrdName);
        String speciality = mDetailingTrackerPOJO.getmDoctorSpeciality();
        String ProductBrdCode =mDetailingTrackerPOJO.getmDetstrtpdtBrdCode();
         ProductBrdCode =productcode;
        Log.v("Doctor_speciality",speciality+"productCode"+ProductBrdCode);
        JSONArray SlidesArray = new JSONArray();
        prdName=PdtBrdName;
        Log.v("select_all_proof",mDetailingTrackerPOJO.getmDetListview_Selection());
        String selMode= mDetailingTrackerPOJO.getmDetListview_Selection();

        try {
            dbh.open();
            if(selMode==getResources().getString(R.string.spclwise)) { //"Speciality Wise":
                mCursor = dbh.select_AllSlides_specialitywise(ProductBrdCode, speciality);
            }else if(selMode==getResources().getString(R.string.theraptic)) {  //"Theraptic":
                mCursor = dbh.select_AllSlides_therapticwise(ProductBrdCode, speciality);
            }else if(selMode==getResources().getString(R.string.brandmatrix)) {  //"Brand Matrix":
                mCursor = dbh.select_AllSlides_brandwise(ProductBrdCode);
            }else if(selMode==getResources().getString(R.string.allbrand)) {  //"All Brands"
                mCursor = dbh.select_AllSlides_brandwise(ProductBrdCode);
                Cursor cursor1 = dbh.select_ProductBrdWiseSlide();
                if (cursor1.getCount() != 0) {
                    cursor1.moveToFirst();
                    do {
                        Log.v("all_brand_cursor", cursor1.getString(2));
                    } while (cursor1.moveToNext());
                }
            }else{
                Log.v("select_all_prrf",mDetailingTrackerPOJO.getmDetListview_Selection());
                mCursor = dbh.selectAllProducts_GroupPresentationWise(ProductBrdCode, mDetailingTrackerPOJO.getmDetListview_Selection());
                Log.v("select_all_prrf",mCursor.getCount()+"");
            }
            if (mCursor.getCount() > 0) {
               /* while (mCursor.moveToNext()) {
                    JSONObject SLidesJson = new JSONObject();
                    SLidesJson.put("SlideId", mCursor.getString(6));
                    SLidesJson.put("SlideName", mCursor.getString(2));
                    SLidesJson.put("SlideType", mCursor.getString(3));
                    SLidesJson.put("SlideLocUrl", mCursor.getString(4));
                    SLidesJson.put("brandName", mCursor.getString(1));
                    SlidesArray.put(SLidesJson);
                }
                mCommonSharedPreference.setValueToPreference("CurrentBrdSlides_Json_array",SlidesArray.toString());*/

               /* for(int i=0;i<prdNAme.size();i++){
                    if(prdNAme.get(i).equalsIgnoreCase(PdtBrdName)){

                        viewPager.setCurrentItem(i,true);
                        adp.updateRecyclerViews();
                        i=prdNAme.size();
                    }
                }*/

                Intent i=new Intent(DetailingFullScreenImageViewActivity.this,DetailingFullScreenImageViewActivity.class);
                i.putExtra("pname",PdtBrdName);
                startActivity(i);
               /* int timecount=mCommonSharedPreference.getValueFromPreference("timeCount",0);
                if(timecount==0){
                    String time_val=saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentTime();
                    mCommonSharedPreference.setValueToPreference("timeVal"+timecount,time_val);
                    mCommonSharedPreference.setValueToPreference("timeCount",++timecount);
                }
                else{
                    String time_val=saneforce.edetailing.applicationCommonFiles.CommonUtilsMethods.getCurrentTime();
                    mCommonSharedPreference.setValueToPreference("timeVal"+timecount,time_val);
                    mCommonSharedPreference.setValueToPreference("timeCount",++timecount);
                }*/
              /* Intent i=new Intent(DetailingFullScreenImageViewActivity.this,DetailingFullScreenImageViewActivity.class);
               i.putExtra("pname",PdtBrdName);
               startActivity(i);*/
                //CommonUtilsMethods.CommonIntentwithNEwTask(DetailingFullScreenImageViewActivity.class);
            }else{
                Toast.makeText(this, getResources().getString(R.string.noprdavailable), Toast.LENGTH_SHORT).show();
            }
            Log.e("Slides_json",SlidesArray.toString());

        }catch (Exception e){}
        finally {
            dbh.close();
        }
        Log.v("full_screen_called",PdtBrdName);
       /* Intent i=new Intent(DetailingFullScreenImageViewActivity.this,DetailingFullScreenImageViewActivity.class);
        i.putExtra("pname",PdtBrdName);
        startActivity(i);*/
    }


    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private String mData;
        private LayoutInflater mInflater;
        ArrayList<String> url=new ArrayList<>();
        ArrayList<String> fullurl=new ArrayList<>();
        ArrayList<String> type=new ArrayList<>();
        ArrayList<String> fulltype=new ArrayList<>();

        ArrayList<Integer> prdPos=new ArrayList<>();
        int len;
         smoothScroll smoothScrolls;
        String slideData;

        String startPrdName="em";
        boolean xx=true;

        void bindlistner(smoothScroll touchgesture1){
            smoothScrolls=touchgesture1;
        }

        public void updateRecyclerViews(){
            xx=true;
            url.clear();
            type.clear();
            for(int k=0;k<prdNAme.size();k++){
                if(prdNAme.get(k).equalsIgnoreCase(prdName)){
                    url.add(fullurl.get(k));
                    type.add(fulltype.get(k));
                    Log.v("url_type",fullurl.get(k));
                    if(xx){
                        Log.v("url_type_kkkk", String.valueOf(k));
                        startPrdName=prdNAme.get(k);
                        startint=k;
                        xx=false;
                    }
                }

            }

            notifyDataSetChanged();
        }

        // Data is passed into the constructor
        public MyRecyclerViewAdapter(final Context context, String data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
            prdNAme.clear();
            prdPos.clear();
            MyCustomPagerAdapter.productCheckBindListener(new ProductChangeListener() {
                @Override
                public void checkPosition(int i) {
                    if(prdNAme.size()<i)
                        return;

                    if(i==0 || returnValue){
                        Log.v("printing_position","here_are"+i+"prd_size"+prdNAme.size());
                        if(prdNAme.size()!=i)
                        startPrdName=prdNAme.get(i);
                    }
                    else {

                        if (prdNAme.size() != i){
                            if (startPrdName.equals("em")) {
                                Log.v("printing_the_product", "em not differ" + prdNAme.size());
                                startPrdName = prdNAme.get(i);
                            }
                        if (prdNAme.get(i).equalsIgnoreCase(startPrdName)) {
                            Log.v("printing_the_product", "not differ");
                        } else {
                            url.clear();
                            type.clear();
                            startPrdName = prdNAme.get(i);
                            Log.v("printing_the_product", "differ" + i + "full_url_size" + fullurl.size());
                            for (int k = 0; k < prdPos.size(); k++) {

                                if (prdPos.get(k) == i) {
                                    Log.v("printing_the_product", "differ_forward");
                                    if ((k + 1) > (prdPos.size() - 1)) {
                                        for (int j = prdPos.get(k); j < fullurl.size(); j++) {
                                            Log.v("printing_the_product", fullurl.get(j) + " size" + url.size());
                                            url.add(fullurl.get(j));
                                            type.add(fulltype.get(j));
                                        }

                                    } else {
                                        Log.v("product_printt", prdPos.get(k) + "plusval" + prdPos.get(k + 1));
                                        for (int j = prdPos.get(k); j < prdPos.get(k + 1); j++) {
                                            url.add(fullurl.get(j));
                                            Log.v("printing_the_product", fullurl.get(j) + " size" + url.size());
                                            type.add(fulltype.get(j));
                                        }
                                    }
                                    startint = prdPos.get(k);
                                    k = prdPos.size();
                                } else if ((prdPos.get(k) - 1) == i) {
                                    Log.v("printing_the_product", "differ_backward");
                                    for (int j = prdPos.get(k - 1); j < prdPos.get(k); j++) {
                                        Log.v("printing_the_product", fullurl.get(j) + " size" + url.size());
                                        url.add(fullurl.get(j));
                                        type.add(fulltype.get(j));
                                    }
                                    startint = prdPos.get(k - 1);
                                    k = prdPos.size();
                                }
                                //notifyItemRangeInserted(0,url.size());
                                //notifyDataSetChanged();
/*
                                if(prdPos.get(k)==i){
                                    if((k+1)>(prdPos.size()-1)){
                                        for(int j=prdPos.get(k);j<fullurl.size();j++){
                                            Log.v("printing_the_product", fullurl.get(j) + " size" + url.size());
                                            url.add(fullurl.get(j));
                                            type.add(fulltype.get(j));
                                        }
                                    }
                                    else {
                                        for (int j = prdPos.get(k); j < prdPos.get(k + 1); j++) {
                                            url.add(fullurl.get(j));
                                            Log.v("printing_the_product", fullurl.get(j) + " size" + url.size());
                                            type.add(fulltype.get(j));
                                        }
                                    }
                                    notifyDataSetChanged();
                                }
*/
                            }
                        }
                    }
                    }
                }
            });

            try {
                /*JSONArray json_array = new JSONArray(data);
                this.len=json_array.length();
                for (int i = 0; i < json_array.length(); i++) {
                    JSONObject jsonChildNode = json_array.getJSONObject(i);
                    String imgname = jsonChildNode.optString("SlideName");
                    String slideUrl = jsonChildNode.optString("SlideLocUrl");
                    String slideType = jsonChildNode.optString("SlideType");
                   *//**//*
                    Log.e("slide ", "POS " + slideUrl);
                    url.add(slideUrl);
                    type.add(slideType);
                    Uri imageUri = Uri.fromFile(new File(slideUrl));
                    //simpleDraweeView.setImageURI(imageUri);*/


                String prdCodes=mCommonSharedPreference.getValueFromPreference("prdCodee");
                slideData = mCommonSharedPreference.getValueFromPreference("ProductBrdWiseSlides_jsonArray");
                url.clear();
                type.clear();
                Log.v("product_name_size", String.valueOf(slideData));

                String checkBrdNm="";
                    JSONArray Orig_jsonSLides_data = new JSONArray(slideData);
                Log.v("product_name_size_len", String.valueOf(Orig_jsonSLides_data.length()));
                    for (int i = 0; i < Orig_jsonSLides_data.length(); i++) {
                        JSONObject jsonObject = Orig_jsonSLides_data.getJSONObject(i);
                        String brdNAme = jsonObject.getString("BrdName");
                        String brdCOde = jsonObject.getString("BrdCode");
                        JSONArray jsonSlide = jsonObject.getJSONArray("Slides");
                        for (int j = 0; j < jsonSlide.length(); j++) {
                            JSONObject json = jsonSlide.getJSONObject(j);
                            Log.v("slide_printing_nam", json.getString("SlideName"));
                            fullurl.add(json.getString("SlideLocUrl"));
                            fulltype.add(json.getString("SlideType"));
                            prdNAme.add(brdNAme);
                            prdCodeing.add(json.getString("PrdCode"));
                           /* if(i==0){
                                prdPos.add(i);
                            }
                            else{

                                JSONObject jsonObject1 = Orig_jsonSLides_data.getJSONObject(i-1);
                                if(brdNAme.equalsIgnoreCase(jsonObject1.getString("BrdName"))){

                                }
                                else{
                                    prdPos.add(i);
                                    Log.v("total_prdd_changes", String.valueOf(i));
                                }
                            }*/

                            if(i==0 && TextUtils.isEmpty(prdName)){
                                checkBrdNm=brdNAme;
                                url.add(json.getString("SlideLocUrl"));
                                type.add(json.getString("SlideType"));
                                startint=i;
                            }
                            else if(checkBrdNm.equalsIgnoreCase(brdNAme) && prdCodes.equalsIgnoreCase(json.getString("PrdCode"))){
                                url.add(json.getString("SlideLocUrl"));
                                type.add(json.getString("SlideType"));
                            }
                            else if(!TextUtils.isEmpty(prdName) && prdName.equalsIgnoreCase(brdNAme) && prdCodes.equalsIgnoreCase(json.getString("PrdCode"))){
                                url.add(json.getString("SlideLocUrl"));
                                type.add(json.getString("SlideType"));
                            }
                        }
                    }
                    Log.v("detailingFullscreen",prdNAme.size()+"");
                    for(int j=0;j<prdNAme.size();j++){
                        if(j==0){
                            prdPos.add(j);
                        }
                        else{
                            if(prdNAme.get(j).equalsIgnoreCase(prdNAme.get(j-1)) && prdCodeing.get(j).equalsIgnoreCase(prdCodeing.get(j-1))){

                            }
                            else{
                                prdPos.add(j);
                                Log.v("total_prdd_changes", String.valueOf(j));
                            }
                        }
                    }
                Log.v("product_name_size", String.valueOf(prdNAme.size())+prdNAme);
                returnValue=false;
            }catch (Exception e){
                Log.v("exception_findout",e.getMessage());
            }
        }

        // Inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.rowlayout_recycler, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        // Binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
          //  String animal = mData[position];
            String imgurl=url.get(position);
            String types=type.get(position);
            Log.v("url_types_are_print ",imgurl+" printt "+url.size());
            Uri imageUri = Uri.fromFile(new File(imgurl));
            if(types.equalsIgnoreCase("h"))
            {
                String HTMLPath=imgurl.replaceAll(".zip","");
                File f = new File(HTMLPath);
                final File[] files=f.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.contains(".png")|| filename.contains(".jpg");
                    }
                });
                String url="";
                try {
                    if (files.length > 0) url = files[0].getAbsolutePath();
                    Uri imageUri1 = Uri.fromFile(new File(url));
                    holder.imgview.setImageURI(imageUri1);
                }catch (Exception e){}
            }
            else if(types.equalsIgnoreCase("v")){
               // String url="/storage/emulated/0/Products/Demo_Html/video/Horlicks1.mp4";
                String url=imgurl;
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(url,
                        MediaStore.Images.Thumbnails.MINI_KIND);
                holder.imgview.setImageBitmap(thumb);
               /* BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
                holder.imgview.setBackgroundDrawable(bitmapDrawable);*/
            }
            else if(types.equalsIgnoreCase("p")){
                holder.imgview.setImageBitmap(getBitmap(new File(imgurl)));
            }
            else {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(imgurl,bmOptions);
                try{
                    bitmap = Bitmap.createScaledBitmap(bitmap,250,250,true);
                    holder.imgview.setImageBitmap(bitmap);
                }catch (Exception e){
                    Log.v("image_url_getting",imgurl);
                    holder.imgview.setImageURI(Uri.parse(imgurl));
                }

            }
        }

        // Total number of cells
        @Override
        public int getItemCount() {
            return url.size();
        }

        // Stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView imgview;

            public ViewHolder(View itemView) {
                super(itemView);
                imgview = (ImageView) itemView.findViewById(R.id.mproduct_gridView_logo_image1);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                onItemClick(view, getAdapterPosition());
            }
        }

        // Convenience method for getting data at click position
        public String getItem(int id) {
            return url.get(id);
        }

        // Method that executes your code for the action received
        public void onItemClick(View view, int position) {
            Log.i("recyclerview", "You clicked number " + getItem(position).toString() + ", which is at cell position " + position+"startint"+startint);

            int vv=position+startint;
            viewPager.setCurrentItem(vv,true);

           /* myCustomPagerAdapter = new MyCustomPagerAdapter(DetailingFullScreenImageViewActivity.this, len,position);
            viewPager.setAdapter(myCustomPagerAdapter);*/

          // smoothScrolls.scrolling(position);
        }
    }

    public interface smoothScroll{
        void scrolling(int id);
    }

    public Bitmap getBitmap(File file){
        int pageNum = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(DetailingFullScreenImageViewActivity.this);
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

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static void popupSpeciality(){
        arr.clear();
        code.clear();
        dbh.open();
        Cursor mCursor=dbh.select_speciality_list();
        while(mCursor.moveToNext()){
            Log.v("Cursor_databasee",mCursor.getString(1)+mCursor.getString(2));
            arr.add(mCursor.getString(2));
            code.add(mCursor.getString(1));
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_speciality);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        GridView popup_list=(GridView)dialog.findViewById(R.id.grid_list);

        SpecialityAdapter popupAdapter=new SpecialityAdapter(arr,context);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.v("speciality_code_",code.get(i));
                specialityListener.specialityCode(code.get(i));

                String slideData = mCommonSharedPreference1.getValueFromPreference("ProductBrdWiseSlides_jsonArray");
                JSONArray jsonArray = null;
                try {
                    jsonArray=new JSONArray(slideData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonArray.length()==0){
                    Toast.makeText(context,context.getResources().getString(R.string.noprdavailable),Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    iii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iii);
                }
            }
        });

    }

    public static void popupTheraptic(){
        arr.clear();
        code.clear();
        dbh.open();
        Cursor mCursor=dbh.select_theraptic_list();
        while(mCursor.moveToNext()){
            Log.v("Cursor_databasee",mCursor.getString(1)+mCursor.getString(2));
            arr.add(mCursor.getString(2));
            code.add(mCursor.getString(1));
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_speciality);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        GridView popup_list=(GridView)dialog.findViewById(R.id.grid_list);

        SpecialityAdapter popupAdapter=new SpecialityAdapter(arr,context);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.v("speciality_code_",code.get(i));
                specialityListener.specialityCode(code.get(i));

                String slideData = mCommonSharedPreference1.getValueFromPreference("ProductBrdWiseSlides_jsonArray");
                JSONArray jsonArray = null;
                try {
                    jsonArray=new JSONArray(slideData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonArray.length()==0){
                    Toast.makeText(context,context.getResources().getString(R.string.noprdavailable),Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    iii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iii);
                }
            }
        });

    }

    public static void fun(Context context1){
        context=context1;
    }

    public static void bindSpecListener(SpecialityListener speclist){
        specialityListener=speclist;
    }

    public static void callMainActivity(){
        iii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(iii);
    }

    public static void bindZoomListener(UpdateUi uu){
        updateUi=uu;
    }

    }



