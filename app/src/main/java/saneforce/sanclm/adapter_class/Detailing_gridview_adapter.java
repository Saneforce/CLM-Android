package saneforce.sanclm.adapter_class;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.adapter_interfaces.OnSelectGridViewListener;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.fragments.Detailing_Selection_search_grid_selection;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class Detailing_gridview_adapter extends ArrayAdapter<Custom_Products_GridView_Contents> {
    Activity activity;
    Context mContext;
    private Filter dataFilter;
    private List<Custom_Products_GridView_Contents> origDataList;
    List<Custom_Products_GridView_Contents> mProducts_GridView_Contents;
    private OnSelectGridViewListener<String> listener;
    DataBaseHandler dbh;
    Uri imageUri;
    DetailingTrackerPOJO mDetailingTrackerPOJO;
    CommonSharedPreference commonSharedPreference;


    public Detailing_gridview_adapter(Activity activity, int custom_presentation_search_gridview, ArrayList<Custom_Products_GridView_Contents> mProducts_gridView_contents) {
        super(activity,custom_presentation_search_gridview, mProducts_gridView_contents);
        this.activity=activity;
        this.mProducts_GridView_Contents=mProducts_gridView_contents;
        this.origDataList=new ArrayList<Custom_Products_GridView_Contents>();
        this.origDataList.addAll(mProducts_GridView_Contents);
        dbh=new DataBaseHandler(activity);
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
    }

    public Detailing_gridview_adapter(Activity activity, int mGridViewLayoutDesign, List<Custom_Products_GridView_Contents> mProducts_GridView_Contents,
                                        OnSelectGridViewListener<String> listener)
    {
        super(activity,mGridViewLayoutDesign, mProducts_GridView_Contents);
        Fresco.initialize(activity);
        this.activity=activity;
        this.mProducts_GridView_Contents=mProducts_GridView_Contents;
        this.origDataList=new ArrayList<Custom_Products_GridView_Contents>();
        this.origDataList.addAll(mProducts_GridView_Contents);
        this.listener = listener;
        dbh=new DataBaseHandler(mContext);
        commonSharedPreference=new CommonSharedPreference(this.activity);
    }

    private class ViewHolder {
        TextView mPdt_brand;
        SimpleDraweeView iv_pdtbrand_search,iv_pdtbrand_view;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Custom_Products_GridView_Contents _product_contents = mProducts_GridView_Contents.get(position);

        try
        {
            final Detailing_gridview_adapter.ViewHolder mViewHolder;
            if (convertView == null) {
                LayoutInflater mViewLayoutInflatar = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mViewLayoutInflatar.inflate(R.layout.custom_presentation_search_gridview, null);
                mViewHolder = new Detailing_gridview_adapter.ViewHolder();
                mViewHolder.mPdt_brand=(TextView) convertView.findViewById(R.id.tv_presentation_brd_name);
                mViewHolder.iv_pdtbrand_search=(SimpleDraweeView) convertView.findViewById(R.id.iv_presntation_search);
                mViewHolder.iv_pdtbrand_view=(SimpleDraweeView) convertView.findViewById(R.id.iv_presntation_view);
                mViewHolder.iv_pdtbrand_view.setVisibility(View.GONE);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (Detailing_gridview_adapter.ViewHolder) convertView.getTag();
            }

            mViewHolder.iv_pdtbrand_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Custom_Products_GridView_Contents _products = mProducts_GridView_Contents.get(position);

                    switch (_products.getmAdapterMode()) {
                        case CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_DETAILINGSEARCH:
                            if(Detailing_Selection_search_grid_selection.ProductCodeSplit1.size()!=0){
                                commonSharedPreference.setValueToPreference("prdCodee",Detailing_Selection_search_grid_selection.ProductCodeSplit1.get(position));
                            }
                            else
                                commonSharedPreference.setValueToPreference("prdCodee","-1");

                            listener.onTaskComplete(_products.mProductBrdName,_products.mProductBrdCode);
                            break;
                    }
                }
            });

            Bitmap bm = null ;

            if(_product_contents.getmAdapterMode()==CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS) {
                Log.v("inside_gridview","gettingAdapter");
                mViewHolder.mPdt_brand.setVisibility(View.GONE);
                if(_product_contents.getmFiletype().equalsIgnoreCase("i")){
                    Log.v("inside_gridview","gettingAdapter333");
                    imageUri= Uri.fromFile(new File(_product_contents.getmLogoURL()));
                    mViewHolder.iv_pdtbrand_search.setImageURI(imageUri);

                   /* BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(_product_contents.getmLogoURL(),bmOptions);
                    try{
                        bitmap = Bitmap.createScaledBitmap(bitmap,450,450,true);
                        mViewHolder.iv_pdtbrand_search.setImageBitmap(bitmap);
                    }catch (Exception e){
                        Log.v("image_url_getting",_product_contents.getmLogoURL());
                        mViewHolder.iv_pdtbrand_search.setImageURI(Uri.parse(_product_contents.getmLogoURL()));
                    }*/
                }
                if(_product_contents.getmFiletype().equalsIgnoreCase("h"))
                {
                    Log.v("inside_gridview","gettingAdapter4444");
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
                    if(!TextUtils.isEmpty(url)){
                        url=url.substring(0,url.lastIndexOf("/")+1)+"preview.png";
                    }
                    imageUri= Uri.fromFile(new File(url));
                    mViewHolder.iv_pdtbrand_search.setImageURI(imageUri);
                }
                if(_product_contents.getmFiletype().equalsIgnoreCase("v"))
                {
                   /* MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(_product_contents.getmLogoURL());
                    bm = mediaMetadataRetriever.getFrameAtTime(5000000); //unit in microsecond*/
                    bm=null;
                    String url=_product_contents.getmLogoURL();
                    Log.v("video_url_pr",url);
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(url,
                            MediaStore.Images.Thumbnails.MINI_KIND);

                    mViewHolder.iv_pdtbrand_search.setImageBitmap(thumb);
                }
                if(_product_contents.getmFiletype().equalsIgnoreCase("p"))
                {
                    Drawable myDrawable = activity.getResources().getDrawable(R.mipmap.pdf_logo);
                    bm = ((BitmapDrawable) myDrawable).getBitmap();
                    //bm=getBitmap(new File(_product_contents.getmLogoURL()));
                    bm = scaleDown(bm, 200, true);
                }
            }else {
                Log.v("inside_gridview","gettingAdapter22222"+_product_contents.getmFiletype());
                if (_product_contents.getmFiletype().equalsIgnoreCase("i")) {
                    Log.v("inside_gridview","gettingAdapter5555"+_product_contents.getmLogoURL());
                    imageUri = Uri.fromFile(new File(_product_contents.getmLogoURL()));
                    mViewHolder.iv_pdtbrand_search.setImageURI(imageUri);
                    mViewHolder.mPdt_brand.setText(_product_contents.getmProductName());
                    //bm = decodeSampledBitmapFromUri(_product_contents.getmLogoURL(), 150, 150);

                   /* imageUri= Uri.fromFile(new File(_product_contents.getmLogoURL()));
                    mViewHolder.iv_pdtbrand_search.setImageURI(imageUri);

                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(_product_contents.getmLogoURL(),bmOptions);
                    try{
                        bitmap = Bitmap.createScaledBitmap(bitmap,450,450,true);
                        mViewHolder.iv_pdtbrand_search.setImageBitmap(bitmap);
                    }catch (Exception e){
                        Log.v("image_url_getting",_product_contents.getmLogoURL());
                        mViewHolder.iv_pdtbrand_search.setImageURI(Uri.parse(_product_contents.getmLogoURL()));
                    }
                    mViewHolder.mPdt_brand.setText(_product_contents.getmProductName());*/
                }
                if (_product_contents.getmFiletype().equalsIgnoreCase("h")) {
                    Log.v("inside_gridview","gettingAdapter6666");
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
                    //Log.v("url_of_adapter",url.substring(0,url.lastIndexOf("/")+1));
                    if(!TextUtils.isEmpty(url)){
                        url=url.substring(0,url.lastIndexOf("/")+1)+"preview.png";
                    }
                    imageUri = Uri.fromFile(new File(url));
                    mViewHolder.iv_pdtbrand_search.setImageURI(imageUri);
                    mViewHolder.mPdt_brand.setText(_product_contents.getmProductName());
                }
                if (_product_contents.getmFiletype().equalsIgnoreCase("v")) {
                   /* MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(_product_contents.getmLogoURL());
                    bm = mediaMetadataRetriever.getFrameAtTime(5000000); //unit in microsecond*/
                   bm=null;
                    String url=_product_contents.getmLogoURL();
                    Log.v("video_url_pr22",url);
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(url,
                            MediaStore.Images.Thumbnails.MINI_KIND);

                    mViewHolder.iv_pdtbrand_search.setImageBitmap(thumb);
                    //holder.imgview.setImageBitmap(thumb);
                    mViewHolder.mPdt_brand.setText(_product_contents.getmProductName());
                }


                if (_product_contents.getmFiletype().equalsIgnoreCase("p")) {
                    Drawable myDrawable = activity.getResources().getDrawable(R.mipmap.pdf_logo);
                    bm = ((BitmapDrawable) myDrawable).getBitmap();
                    //bm=getBitmap(new File(_product_contents.getmLogoURL()));
                    bm = scaleDown(bm, 200, true);
                    mViewHolder.mPdt_brand.setText(_product_contents.getmProductName());
                    //mViewHolder.mProductLogo.setImageBitmap(bm.createBitmap(width, height, config));
                }
            }
            if(bm!=null) {
                //  mViewHolder.iv_pdtbrand_search.setImageBitmap(bm);
          /*  Glide
                    .with(mContext)
                     .as()
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.loading_spinner)
                    .into(myImageView);*/
          if(_product_contents.getmFiletype().equalsIgnoreCase("p")){
              mViewHolder.iv_pdtbrand_search.setImageBitmap(_product_contents.getBitmap());
              //mViewHolder.iv_pdtbrand_search.setImageURI(getImageUri(mContext,bm));
              //mViewHolder.iv_pdtbrand_search.setImageBitmap(getBitmap(new File(_product_contents.getmLogoURL())));
          }
          else if(_product_contents.getmFiletype().equalsIgnoreCase("v")){
              mViewHolder.iv_pdtbrand_search.setImageBitmap(bm);
          }
          else {
              Glide.with(mContext)
                      .load(bm)
                      .asBitmap()
                      .error(R.drawable.ic_menu_camera)

                      .into(mViewHolder.iv_pdtbrand_search);
          }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return convertView;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    /**
     *Image conversion in bitmap
     */
    public Bitmap getBitmap(File file){
        int pageNum = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(activity);
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

    @Override
    public Filter getFilter() {
        if (dataFilter == null)
            dataFilter = new Detailing_gridview_adapter.DataFilter();
        return dataFilter;
    }

    private class DataFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // Log.d("Constraint string: lenght", String.valueOf(constraint)+":"+String.valueOf(constraint.length()));
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origDataList;
                results.count = origDataList.size();
                //resetData();
            }else {
                // We perform filtering operation
                List<Custom_Products_GridView_Contents> nDataList = new ArrayList<Custom_Products_GridView_Contents>();
                for (Custom_Products_GridView_Contents p : origDataList)
                {
                    if (p.getmProductName().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                        nDataList.add(p);
                    }
                }
                results.values = nDataList;
                results.count = nDataList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,FilterResults results) {
            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
            {
                notifyDataSetInvalidated();
                clear();
            }else
            {
                mProducts_GridView_Contents = (List<Custom_Products_GridView_Contents>) results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = mProducts_GridView_Contents.size(); i < l; i++)
                    add(mProducts_GridView_Contents.get(i));
                notifyDataSetInvalidated();
            }
        }
    }

}
