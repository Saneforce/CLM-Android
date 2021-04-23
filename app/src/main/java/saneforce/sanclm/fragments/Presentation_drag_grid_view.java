package saneforce.sanclm.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.animoto.android.views.*;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.SavePresentation;
import saneforce.sanclm.adapter_class.Custom_Products_GridView_Contents;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.CustomImageView;
import saneforce.sanclm.util.GridSelectionList;

public class Presentation_drag_grid_view extends Fragment {
    DataBaseHandler dbh;
    DetailingTrackerPOJO mDetailingTrackerPOJO;
    private ArrayList<Custom_Products_GridView_Contents> mProducts_GridView_Contents=new ArrayList<Custom_Products_GridView_Contents>();
    private ArrayList<Custom_Products_GridView_Contents> _product_list=new ArrayList<Custom_Products_GridView_Contents>();
    List list = new ArrayList();
    DraggableGridView dgv;
    ArrayList<Bitmap> arrBit=new ArrayList<>();
    ArrayList<Bitmap> OrgarrBit=new ArrayList<>();
    static GridSelectionList listener;
    Bitmap bitmap=null;
    int count=0;
    int j=0;
    LottieAnimationView animation_view;
    ProgressDialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.presentation_right_view, container);
        dgv = (DraggableGridView)v.findViewById(R.id.vgv);

        dbh=new DataBaseHandler(getActivity());
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
        mdDisplayProductInGridView("","",false);
        dgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.v("adapter_view_arg ",arg1+" second_view "+arg2);

                    CustomImageView img=(CustomImageView)dgv.findViewById(arg1.getId());
                if(mProducts_GridView_Contents.get(arg2).getmSelectionState()==true) {
                    img.setAlpha(60);
                   /* Drawable myDrawable = getActivity().getResources().getDrawable(R.mipmap.btn_go);
                    Bitmap btMap2 = ((BitmapDrawable) myDrawable).getBitmap();*/
                    img.setImageBitmap(OrgarrBit.get(arg2));
                    mProducts_GridView_Contents.get(arg2).setmSelectionState(false);
                }
                else{
                    img.setAlpha(255);
                    img.setImageBitmap(arrBit.get(arg2));
                    mProducts_GridView_Contents.get(arg2).setmSelectionState(true);
                }

                searchSelectview();

            }
        });

        dgv.setOnRearrangeListener(new OnRearrangeListener() {
            @Override
            public void onRearrange(int i, int i1) {
                Log.v("rearrrange_old",i+" newindex "+i1);
                Bitmap bm=arrBit.remove(i);
                Bitmap bm1=OrgarrBit.remove(i);
                Custom_Products_GridView_Contents prd=mProducts_GridView_Contents.remove(i);
                arrBit.add(i1,bm);
                OrgarrBit.add(i1,bm1);
                mProducts_GridView_Contents.add(i1,prd);
            }
        });

/*
        dgv.setOnRearrangeListener(new DraggableGridView.OnRearrangeListener() {
            public void onRearrange(int oldIndex, int newIndex) {
                Log.v("rearrrange_old",oldIndex+" newindex "+newIndex);
                Bitmap bm=arrBit.remove(oldIndex);
                Custom_Products_GridView_Contents prd=mProducts_GridView_Contents.remove(oldIndex);
                arrBit.add(newIndex,bm);
                mProducts_GridView_Contents.add(newIndex,prd);


            }
        });
*/

        Presentation_bottom_grid_selection.bindPresentation(new SavePresentation() {
            @Override
            public void saveDetail() {
                dbh.open();
                for(int i=0;i<mProducts_GridView_Contents.size();i++){
                    Custom_Products_GridView_Contents prdd=mProducts_GridView_Contents.get(i);
                    Log.v("saveDetail_value", String.valueOf(prdd.getmPresentName()));
                    long id = dbh.insert_into_group_mapping(prdd.getmPresentName() ,prdd.getmProductCode(),prdd.getmProductName(),prdd.getmFileName(),prdd.getmFiletype(),prdd.getmLogoURL(),String.valueOf(prdd.getmSelectionState()),"1","1","1","A");
                }
                mProducts_GridView_Contents.clear();
                dbh.close();

            }
        });

        return v;
    }

    public static void bindListener(GridSelectionList listeners){
        listener=listeners;
    }

    public void mdDisplayProductInGridView(final String mProductBrdName, final String mProductBrdCode, final boolean presentationlist) {


        Handler handler1 = new Handler();
        int ncountt=0;
        dgv.removeAllViews();
        arrBit.clear();
        OrgarrBit.clear();
        if(mDetailingTrackerPOJO.getmPrsn_svName().equalsIgnoreCase("")){mDetailingTrackerPOJO.setmPrsn_svName("1");}else{}

        try
        {
            dbh.open();

            list.clear();
            Boolean selectionstatus = true;
            int asc_count=0;
            count=0;
            _product_list.clear();
            Cursor mCursor=dbh.select_BrandwiseforPresentation(mProductBrdCode);
            if(mCursor.getCount()>0) {

                while (mCursor.moveToNext())
                {

                    Cursor mCursor1 = dbh.select_MappedFileName_GroupMapping(mProductBrdCode,mCursor.getString(5),mDetailingTrackerPOJO.getmPrsn_svName());
                    Log.v("Cursor_count_present111", String.valueOf(mCursor1.getCount()));
                    if(mCursor1.getCount()>0){
                        mCursor1.moveToFirst();
                        Log.v("Cursor_count_present", String.valueOf(mCursor1.getString(1)));
                        selectionstatus= Boolean.valueOf(mCursor1.getString(0));
                        asc_count=mCursor1.getInt(1);
                    }
                    else{

                        selectionstatus=true;
                        asc_count=0;
                    }


                    Log.v("selection_list_choosen", String.valueOf(mCursor.getString(5)));
                   // long id = dbh.insert_into_group_mapping(mDetailingTrackerPOJO.getmPrsn_svName() ,mProductBrdCode,mProductBrdName,mCursor.getString(5),mCursor.getString(6),mCursor.getString(12),String.valueOf(selectionstatus),"1","1","1","A");
                    Custom_Products_GridView_Contents products= new Custom_Products_GridView_Contents(mCursor.getString(5),mCursor.getString(2), mCursor.getString(3), mCursor.getString(12),
                            selectionstatus, CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS,mCursor.getString(6),mDetailingTrackerPOJO.getmPrsn_svName(),asc_count,bitmap,8,mCursor.getString(1));
                    mProducts_GridView_Contents.add(products);
                    _product_list.add(products);

                    if(presentationlist){
                        Collections.sort(_product_list,new SortListColumn());
                        Collections.reverse(_product_list);
                    }

                }

                Handler handler=new Handler();
                for( int j=0;j<_product_list.size();j++){
                    final int k=j;
                  /*  handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {*/

                            Log.v("jvalue_printing_here", String.valueOf(k));
                            Custom_Products_GridView_Contents mm=_product_list.get(k);
                            int alpha_val=0;
                            if(mm.getmSelectionState()){
                                alpha_val=255;
                                listener.unselectionList(_product_list.get(0).getmProductName());
                            }
                            else{
                                alpha_val=40;
                            }
                            Bitmap btMap=null;

                            if(mm.getmFiletype().equalsIgnoreCase("i")){
                                Drawable myDrawable = getActivity().getResources().getDrawable(R.mipmap.btn_go);
                                Bitmap btMap2 = ((BitmapDrawable) myDrawable).getBitmap();
                                Drawable myDrawable1 = getActivity().getResources().getDrawable(R.drawable.green_tick);
                                Bitmap btMap3 = ((BitmapDrawable) myDrawable1).getBitmap();
                                btMap=getResizedBitmap(mm.getmLogoURL(),300,200,btMap2);
                                OrgarrBit.add(getResizedBitmap(mm.getmLogoURL(),300,200,btMap3));
                                CustomImageView view=new CustomImageView(getActivity());
                                view.setImageBitmap(btMap);
                                view.setId(count);
                                view.setAlpha(alpha_val);
                                count++;
                                arrBit.add(btMap);
                                dgv.addView(view);

                            }
                            if(mm.getmFiletype().equalsIgnoreCase("h"))
                            {
                                String HTMLPath=mm.getmLogoURL().replaceAll(".zip","");
                                File f = new File(HTMLPath);
                                File[] files=f.listFiles(new FilenameFilter() {
                                    @Override
                                    public boolean accept(File dir, String filename) {
                                        return filename.contains(".png")||filename.contains(".jpg");
                                    }
                                });
                                String url="";
                                if (files.length>0) url=files[0].getAbsolutePath();
                                Uri imageUri= Uri.fromFile(new File(url));
                                Log.v("Image_url", String.valueOf(imageUri).substring(7));
                                Drawable myDrawable = getActivity().getResources().getDrawable(R.mipmap.btn_go);
                                Bitmap btMap2 = ((BitmapDrawable) myDrawable).getBitmap();
                                OrgarrBit.add(getResizedBitmap(String.valueOf(imageUri).substring(7),300,200));
                                btMap=getResizedBitmap(String.valueOf(imageUri).substring(7),300,200);

                                Log.v("Bimap_user_finding", String.valueOf(btMap));
                               // btMap=getThumb(String.valueOf(imageUri).substring(7));
                                CustomImageView view=new CustomImageView(getActivity());
                                view.setImageBitmap(btMap);
                                view.setId(count);
                                view.setAlpha(alpha_val);
                                count++;
                                dgv.addView(view);
                                arrBit.add(btMap);

                            }
                            if(mm.getmFiletype().equalsIgnoreCase("v"))
                            {
                                Drawable myDrawable = getActivity().getResources().getDrawable(R.mipmap.btn_go);
                                Bitmap btMap2 = ((BitmapDrawable) myDrawable).getBitmap();
                                Drawable myDrawable1 = getActivity().getResources().getDrawable(R.drawable.green_tick);
                                Bitmap btMap3 = ((BitmapDrawable) myDrawable1).getBitmap();
                                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                mediaMetadataRetriever.setDataSource(mm.getmLogoURL());
                                btMap = mediaMetadataRetriever.getFrameAtTime(5000000);
                                OrgarrBit.add(getResizedBitmap(btMap,300,200,btMap3));
                                Bitmap btMap1=getResizedBitmap(btMap,300,200,btMap2);
                                CustomImageView view=new CustomImageView(getActivity());
                                view.setImageBitmap(btMap1);
                                view.setId(count);
                                view.setAlpha(alpha_val);
                                count++;
                                dgv.addView(view);
                                arrBit.add(btMap);
//unit in microsecond
                            }
                            if(mm.getmFiletype().equalsIgnoreCase("p"))
                            {
                                Drawable myDrawable2 = getActivity().getResources().getDrawable(R.drawable.green_tick);
                                Bitmap btMap3 = ((BitmapDrawable) myDrawable2).getBitmap();
                                Drawable myDrawable1 = getActivity().getResources().getDrawable(R.mipmap.btn_go);
                                Bitmap btMap2 = ((BitmapDrawable) myDrawable1).getBitmap();
                                Drawable myDrawable = getActivity().getResources().getDrawable(R.mipmap.btn_go);
                                btMap = ((BitmapDrawable) myDrawable).getBitmap();
                                OrgarrBit.add(getResizedBitmap(btMap,300,200,btMap3));
                                btMap=getResizedBitmap(btMap,300,200,btMap2);
                                CustomImageView view=new CustomImageView(getActivity());
                                view.setImageBitmap(btMap);
                                view.setId(count);
                                view.setAlpha(alpha_val);
                                count++;
                                dgv.addView(view);
                                arrBit.add(btMap);
                                // bm = scaleDown(bm, 200, true);
                            }

                       /* }
                    }, 500 * j);*/
                }





/*
                if(presentationlist){
                    Collections.sort(_product_list,new SortListColumn());
                    Collections.reverse(_product_list);
                    for(int j=0;j<_product_list.size();j++){
                        Custom_Products_GridView_Contents mm=_product_list.get(j);
                        int alpha_val=0;
                        if(mm.getmSelectionState()){
                            alpha_val=255;
                            listener.unselectionList(_product_list.get(0).getmProductName());
                        }
                        else{
                            alpha_val=40;
                        }
                        CustomImageView view=new CustomImageView(getActivity());
                        RelativeLayout.LayoutParams par=new RelativeLayout.LayoutParams(200,200);
                        view.setLayoutParams(par);
                        view.setImageBitmap(mm.getBitmap());
                        view.setId(count);
                        view.setAlpha(alpha_val);
                        count++;
                        arrBit.add(mm.getBitmap());
                        dgv.addView(view);
                  */
/*  Bitmap btMap=null;

                    if(mm.getmFiletype().equalsIgnoreCase("i")){
                        btMap=getThumb(mm.getmLogoURL());
                       // CustomImageView view=new CustomImageView(getActivity());
                        CustomImageView view=new CustomImageView(getActivity());
                        RelativeLayout.LayoutParams par=new RelativeLayout.LayoutParams(200,200);
                        view.setLayoutParams(par);
                        view.setImageBitmap(btMap);
                        view.setId(count);
                        view.setAlpha(alpha_val);
                        count++;
                        arrBit.add(btMap);
                        dgv.addView(view);


                    }
                    if(mm.getmFiletype().equalsIgnoreCase("h"))
                    {
                        String HTMLPath=mm.getmLogoURL().replaceAll(".zip","");
                        File f = new File(HTMLPath);
                        File[] files=f.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                return filename.contains(".png")||filename.contains(".jpg");
                            }
                        });
                        String url="";
                        if (files.length>0) url=files[0].getAbsolutePath();
                        Uri imageUri= Uri.fromFile(new File(url));
                        Log.v("Image_url", String.valueOf(imageUri).substring(7));
                        btMap=getThumb(String.valueOf(imageUri).substring(7));
                        CustomImageView view=new CustomImageView(getActivity());

                        view.setImageBitmap(btMap);
                        view.setId(count);
                        view.setAlpha(alpha_val);
                        count++;
                        dgv.addView(view);

                        arrBit.add(btMap);

                    }
                    if(mm.getmFiletype().equalsIgnoreCase("v"))
                    {
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(mm.getmLogoURL());
                        btMap = mediaMetadataRetriever.getFrameAtTime(5000000);
                        CustomImageView view=new CustomImageView(getActivity());
                        view.setImageBitmap(btMap);
                        view.setId(count);
                        view.setAlpha(alpha_val);
                        count++;
                        dgv.addView(view);
                        arrBit.add(btMap);
//unit in microsecond
                    }
                    if(mm.getmFiletype().equalsIgnoreCase("p"))
                    {
                        Drawable myDrawable = getActivity().getResources().getDrawable(R.mipmap.btn_go);
                        btMap = ((BitmapDrawable) myDrawable).getBitmap();
                        CustomImageView view=new CustomImageView(getActivity());
                        view.setImageBitmap(btMap);
                        view.setId(count);
                        view.setAlpha(alpha_val);
                        count++;
                        dgv.addView(view);
                        arrBit.add(btMap);
                        // bm = scaleDown(bm, 200, true);
                    }*//*

                    }
                }
*/


            }else{
                // Toast.makeText(getActivity(), "No Products available", Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            dbh.close();
        }
       /* dialog.dismiss();*/

        //mGridview.setAdapter( list);  /// loading all slides in right side fragment
    }

    private Bitmap getThumb(String s)
    {
        Bitmap bmp = null;
        if (bmp != null) {
            bmp.recycle();
            bmp = null;
        }
        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(s, bmOptions);
            bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(bmp);
            Paint paint = new Paint();
            paint.setTextSize(24);
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            canvas.drawRoundRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), 15, 15, paint);
            paint.setColor(Color.BLACK);
            //paint.setTextAlign(Paint.Align.CENTER);
            //  canvas.drawText(s, 75, 75, paint);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            if (bitmap != bmp) {
                bitmap.recycle();
                bitmap=null;
            }
        }catch (OutOfMemoryError e){
           // Log.v("out_of_memory_excep",e.getMessage());

        }
        return bmp;
    }


    public Bitmap getResizedBitmap(String path, int newWidth, int newHeight,Bitmap bit) {
        Bitmap bm = null;
        if (bm != null) {
            bm.recycle();
            bm = null;
        }
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
         bm = BitmapFactory.decodeFile(path, bmOptions);
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        Canvas canvas = new Canvas(resizedBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        if(!bm.isRecycled())
        canvas.drawBitmap(bit,260,155,paint);
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
            bm=null;
        }
        return resizedBitmap;
    }
    public Bitmap getResizedBitmap(String path, int newWidth, int newHeight) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(path, bmOptions);
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);

        bm.recycle();
        return resizedBitmap;
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight,Bitmap bit) {
       /* BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(path, bmOptions);*/
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);

        Canvas canvas = new Canvas(resizedBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bit,270,150,paint);
        bm.recycle();
        return resizedBitmap;
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    class SortListColumn implements Comparator<Custom_Products_GridView_Contents> {

        @Override
        public int compare(Custom_Products_GridView_Contents e1, Custom_Products_GridView_Contents e2) {
            if(e1.getColumnid() < e2.getColumnid()){
                return 1;
            } else {
                return -1;
            }
        }
    }
    public void searchSelectview(){
        ArrayList<Boolean> selectionList=new ArrayList<>();
        for(int i=0;i<mProducts_GridView_Contents.size();i++){
            selectionList.add(mProducts_GridView_Contents.get(i).getmSelectionState());
        }
        if(selectionList.contains(true)){
            listener.unselectionList(mProducts_GridView_Contents.get(0).getmProductName());
        }
        else{
            listener.selectionList(mProducts_GridView_Contents.get(0).getmProductName());
        }
    }


}
