package saneforce.sanclm.adapter_class;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.adapter_interfaces.OnSelectGridViewListener;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.fragments.Presentation_search_grid_selection;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.PresentationRightUnselect;

public class PresentationSearchRecycleAdapter extends RecyclerView.Adapter<PresentationSearchRecycleAdapter.MyViewHolder>{

    Activity activity;
    Context mContext;
    private Filter dataFilter;
    private List<Custom_Products_GridView_Contents> origDataList;
    List<Custom_Products_GridView_Contents> mProducts_GridView_Contents;
    public static OnSelectGridViewListener<String> listener;
    DataBaseHandler dbh;
    Uri imageUri;
    DetailingTrackerPOJO mDetailingTrackerPOJO;
    GestureDetector gestureDetector;
    int number_of_clicks = 0;
    boolean thread_started = false;
    final int DELAY_BETWEEN_CLICKS_IN_MILLISECONDS = 250;
    static PresentationRightUnselect presentationRightUnselect;
    String checkName;
    private LayoutInflater mInflater;



    public PresentationSearchRecycleAdapter(Activity activity, int mGridViewLayoutDesign, List<Custom_Products_GridView_Contents> mProducts_GridView_Contents,
                                            OnSelectGridViewListener<String> listener)
    {
        this.mInflater = LayoutInflater.from(activity);
        Fresco.initialize(activity);
        this.activity=activity;
        this.mProducts_GridView_Contents=mProducts_GridView_Contents;
        this.origDataList=new ArrayList<Custom_Products_GridView_Contents>();
        this.origDataList.addAll(mProducts_GridView_Contents);
        this.listener = listener;
        dbh=new DataBaseHandler(mContext);
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
        gestureDetector = new GestureDetector(this.activity,new GestureListener());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mPdt_brand;
        SimpleDraweeView iv_pdtbrand_search,iv_pdtbrand_view;
        public MyViewHolder(View convertView) {
            super(convertView);
            mPdt_brand=(TextView) convertView.findViewById(R.id.tv_presentation_brd_name);
           iv_pdtbrand_search=(SimpleDraweeView) convertView.findViewById(R.id.iv_presntation_search);
            iv_pdtbrand_view=(SimpleDraweeView) convertView.findViewById(R.id.iv_presntation_view);
            iv_pdtbrand_view.setVisibility(View.GONE);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =  mInflater.inflate(R.layout.custom_presentation_search_gridview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder mViewHolder, final int position) {

        final Custom_Products_GridView_Contents _product_contents = mProducts_GridView_Contents.get(position);


        if(CommonUtils.TAG_VIEW_PRESENTATION.equalsIgnoreCase("1")) {

            Log.v("tag_view_presentat", Presentation_search_grid_selection.lastItemPresentation+" "+_product_contents.mProductBrdName);
            if (_product_contents.mSelectionState == true) {

                listener.onTaskComplete(_product_contents.mProductBrdName, _product_contents.mProductBrdCode);

                // CommonUtils.TAG_VIEW_PRESENTATION="0";
            }

            if(position==(Presentation_search_grid_selection.lastItemPresentation)) {
                CommonUtils.TAG_VIEW_PRESENTATION = "0";
                Presentation_search_grid_selection.lastItemPresentation=0;
            }
            Log.v("view_presenttt",CommonUtils.TAG_VIEW_PRESENTATION+" pos "+Presentation_search_grid_selection.lastItemPresentation);

        }

        mViewHolder.iv_pdtbrand_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Custom_Products_GridView_Contents _products =_product_contents;
                Log.v("positon_printed",_product_contents.getmProductName()+" pos "+position+" bane "+_products.getmProductName());
                ++number_of_clicks;
                if(!thread_started){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            thread_started = true;
                            try {
                                Thread.sleep(DELAY_BETWEEN_CLICKS_IN_MILLISECONDS);
                                if(number_of_clicks == 1){
                                    Log.v("singie_click","pressed");

                                    /* new Handler(Looper.getMainLooper()).post(new Runnable() {
                                         @Override
                                         public void run() {
                                             mViewHolder.iv_pdtbrand_search.setAlpha(255);
                                         }
                                     });*/

                                    //_products.setmSelectionState(true);

                                    switch (_products.getmAdapterMode())
                                    {
                                        case CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING:
                                            Log.v("gridview_mode_mapping", String.valueOf(_products.mProductBrdCode));
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    listener.onTaskComplete(_products.mProductBrdName,_products.mProductBrdCode);
                                                }
                                            });

                                            break;

                                        case CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS:
                                            // Toast.makeText(activity, ""+_products.mFileName, Toast.LENGTH_SHORT).show();
                                            //Log.d("PRODUCT_GRIDVIEW_ADAPTER_MODE", "PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS");
                                            //need doctor id here
                                            Log.e("products_selectionbox", _products.mSelectionState + " - " + _products.mDoctorCode + " - " + _products.mProductBrdCode + " - " + _products.mFileName + " - " + _products.mFiletype + " - " + _products.mLogoURL);
                                            update_mappingTable(_products.mSelectionState,_products.mProductBrdCode,_products.mProductBrdName,_products.mFileName,_products.mFiletype,_products.mLogoURL);
                                            break;
                                    }

                                } else if(number_of_clicks == 2){
                                    Log.v("singie_click_double","pressed");
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mViewHolder.iv_pdtbrand_search.setAlpha(40);
                                            presentationRightUnselect.unSelecting(_products.mProductBrdName);
                                        }
                                    });

                                    _products.setmSelectionState(false);

                                }
                                number_of_clicks = 0;
                                thread_started = false;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });



        /////////////////////


        Bitmap bm = null;

        if(_product_contents.mSelectionState==false){
            mViewHolder.iv_pdtbrand_search.setAlpha(40);
        }else{
            mViewHolder.iv_pdtbrand_search.setAlpha(255);
        }
        if(_product_contents.getmAdapterMode()==CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS) {
            mViewHolder.mPdt_brand.setVisibility(View.GONE);
            if(_product_contents.getmFiletype().equalsIgnoreCase("i")){
                imageUri= Uri.fromFile(new File(_product_contents.getmLogoURL()));
                Log.v("selection_img_url", String.valueOf(imageUri));
                mViewHolder.iv_pdtbrand_search.setImageURI(imageUri);
            }
            if(_product_contents.getmFiletype().equalsIgnoreCase("h"))
            {
                String HTMLPath=_product_contents.getmLogoURL().replaceAll(".zip","");
                File f = new File(HTMLPath);
                File[] files=f.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.contains(".png")||filename.contains(".jpg");
                    }
                });
                String url="";
                if (files.length>0) url=files[0].getAbsolutePath();
                imageUri= Uri.fromFile(new File(url));
                mViewHolder.iv_pdtbrand_search.setImageURI(imageUri);
            }
            if(_product_contents.getmFiletype().equalsIgnoreCase("v"))
            {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(_product_contents.getmLogoURL());
                bm = mediaMetadataRetriever.getFrameAtTime(5000000); //unit in microsecond
            }
            if(_product_contents.getmFiletype().equalsIgnoreCase("p"))
            {
                /*Drawable myDrawable = activity.getResources().getDrawable(R.mipmap.btn_go);
                bm = ((BitmapDrawable) myDrawable).getBitmap();
                bm = scaleDown(bm, 200, true);*/
                bm=_product_contents.getBitmap();
            }
        }else {
            if (_product_contents.getmFiletype().equalsIgnoreCase("i")) {
                imageUri = Uri.fromFile(new File(_product_contents.getmLogoURL()));
                mViewHolder.iv_pdtbrand_search.setImageURI(imageUri);
                mViewHolder.mPdt_brand.setText(_product_contents.getmProductName());
                //bm = decodeSampledBitmapFromUri(_product_contents.getmLogoURL(), 150, 150);
            }
            if (_product_contents.getmFiletype().equalsIgnoreCase("h")) {
                String HTMLPath = _product_contents.getmLogoURL().replaceAll(".zip", "");
                File f = new File(HTMLPath);
                File[] files = f.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.contains(".png") || filename.contains(".jpg");
                    }
                });
                String url = "";
                if (files.length > 0) url = files[0].getAbsolutePath();
                imageUri = Uri.fromFile(new File(url));
                mViewHolder.iv_pdtbrand_search.setImageURI(imageUri);
                mViewHolder.mPdt_brand.setText(_product_contents.getmProductName());
            }
            if (_product_contents.getmFiletype().equalsIgnoreCase("v")) {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(_product_contents.getmLogoURL());
                bm = mediaMetadataRetriever.getFrameAtTime(5000000); //unit in microsecond
                mViewHolder.mPdt_brand.setText(_product_contents.getmProductName());
            }


            if (_product_contents.getmFiletype().equalsIgnoreCase("p")) {
               /* Drawable myDrawable = activity.getResources().getDrawable(R.mipmap.btn_go);
                bm = ((BitmapDrawable) myDrawable).getBitmap();
                bm = scaleDown(bm, 200, true);*/
                bm=_product_contents.getBitmap();
                mViewHolder.mPdt_brand.setText(_product_contents.getmProductName());
                //mViewHolder.mProductLogo.setImageBitmap(bm.createBitmap(width, height, config));
            }
        }
        if(bm!=null)
            mViewHolder.iv_pdtbrand_search.setImageBitmap(bm);
    }

    @Override
    public int getItemCount() {
        return mProducts_GridView_Contents.size();
    }

    @SuppressLint("LongLogTag")
    protected void update_mappingTable(Boolean mSelectionState, String mProductBrdCode, String mProductBrdName, String mFileName, String mFiletype, String mLogoURL) {
        String Gpname ="1";
        try
        {
            dbh.open();

            dbh.update_presentation_selectionStatus(mDetailingTrackerPOJO.getmPrsn_svName(),mProductBrdCode,mFileName,String.valueOf(mSelectionState));
            /*if(mDetailingTrackerPOJO.getmPrsn_svName().equals("") && mSelectionState.toString().equals("false")){


                long id = dbh.insert_into_group_mapping(Gpname ,mProductBrdCode,mProductBrdName,mFileName,mFiletype,mLogoURL,String.valueOf(mSelectionState),"1","1","1","A");
                Log.e("mapping insert listed doctor1", mFileName+" URL "+mLogoURL);
            }else if(mDetailingTrackerPOJO.getmPrsn_svName().equals("") && mSelectionState.toString().equals("true")){
                Log.e("mapping insert listed doctor12 ", mFileName);
                dbh.delete_group_mapping_table(Gpname,mProductBrdCode,mFileName,mFiletype,mLogoURL);
            }else if(mSelectionState.toString().equals("false")){
                Log.e("mapping insert listed doctor123 ",mFileName);
                dbh.delete_group_mapping_table(mDetailingTrackerPOJO.getmPrsn_svName(),mProductBrdCode,mFileName,mFiletype,mLogoURL);
            }else {
                Log.e("mapping insert listed doctor1234 ", mFileName);
                long id = dbh.insert_into_group_mapping(mDetailingTrackerPOJO.getmPrsn_svName(),mProductBrdCode,mProductBrdName,mFileName,mFiletype,mLogoURL,String.valueOf(mSelectionState),"1","1","1","A");
            }*/
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            dbh.close();
        }
    }

    /**
     *Image conversion in bitmap
     */
    public static void bindListenerUnselect(PresentationRightUnselect presentunsele){
        presentationRightUnselect=presentunsele;
    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,boolean filter)
    {
        float ratio = Math.min((float) maxImageSize / realImage.getWidth(),(float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }
    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
        Bitmap bm = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);
        return bm;
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            }else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }




    public class GestureListener extends
            GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            Log.v("whichis_in_e_msg","true_Statementtts");
            return false;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.v("whichis_in_e_msg_motion","true_Statementtts");
           /* tapped = !tapped;

            if (tapped) {



            } else {



            }*/

            return true;
        }
    }

}
