package saneforce.sanclm.adapter_class;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.StoreImageTypeUrl;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Adapter_feedproduct_feedicon extends BaseAdapter {

    Context context;
    ArrayList<StoreImageTypeUrl> list=new ArrayList<>();
    CommonUtilsMethods mCommonUtilsMethods;
    ImageView imgview;
     File[] files;
    String imgurl;
    String types;

    public Adapter_feedproduct_feedicon(Context context, ArrayList<StoreImageTypeUrl> list) {
        this.context = context;
        this.list = list;
        mCommonUtilsMethods=new CommonUtilsMethods(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.row_item_pop_feedback_icon,viewGroup,false);
        final LinearLayout ll_edt_bg=(LinearLayout)view.findViewById(R.id.ll_edt_bg);
        final EditText edt_feed=(EditText)view.findViewById(R.id.edt_feed);

        final StoreImageTypeUrl store=list.get(i);
        TextView slide_nam=(TextView)view.findViewById(R.id.slide_nam);
        TextView duraTime=(TextView)view.findViewById(R.id.dura_time);
         imgview=(ImageView)view.findViewById(R.id.imgview);
        RatingBar rating=(RatingBar)view.findViewById(R.id.rating);

        String seTime="";
        try {
            JSONArray jsonArray=new JSONArray(store.getRemTime());
            JSONObject js=jsonArray.getJSONObject(0);
            String sTT=js.getString("sT");
            String eTT=js.getString("eT");
            seTime=sTT+" "+eTT;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        edt_feed.setText(store.getSlideFeed());
        slide_nam.setText(store.getSlideNam());
        duraTime.setText(seTime);
        rating.setRating(Float.parseFloat(store.getRating()));

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                //popupRating(v,mm.getPrdNAme(),i);
                store.setRating(String.valueOf(v));
            }
        });

         imgurl=store.getSlideUrl();
         types=store.getSlideTyp();
        Uri imageUri = Uri.fromFile(new File(imgurl));
        Log.v("img_uri_feed", String.valueOf(imageUri) + " type " + types + " url  " + imgurl);
        if (types.equalsIgnoreCase("h")) {
            String HTMLPath = imgurl.replaceAll(".zip", "");
            File f = new File(HTMLPath);
            files = f.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.contains(".png") || filename.contains(".jpg");
                }
            });
            String url = "";
            try {

                if (files.length > 0) url = files[0].getAbsolutePath();
                File compressedImageFile = new Compressor(context).compressToFile(new File(url));
                Log.v("printted_uri", compressedImageFile.toString());
                Uri imageUri1 = Uri.fromFile(new File(url));
                imgview.setImageURI(Uri.fromFile(compressedImageFile));

            } catch (Exception e) {
            }
        } else if (types.equalsIgnoreCase("i")) {
            String imguri = String.valueOf(imageUri);
            String len = imguri.substring(7, imguri.length());
            Log.v("decode_bit", len);
            /*try {
                File compressedImageFile = new Compressor(context).compressToFile(new File(imgurl));
                imgview.setImageURI(Uri.fromFile(compressedImageFile));
            } catch (IOException e) {
                e.printStackTrace();
                Log.v("Exception_are_",e.getMessage());
            }
           *//* File ff=new File(imgurl);
            Bitmap myBitmap = BitmapFactory.decodeFile(ff.getAbsolutePath());

           // ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

            imgview.setImageBitmap(myBitmap);
            Log.v("image_gng_to_","datas");*//*
            //imgview.setImageURI(Uri.parse(len));*/
            try {
                imgview.setImageBitmap(decodeBitmapPath(len, 230, 150));
            } catch (Exception e) {
                Log.v("Exception_are_", e.getMessage());
            }
        } else if (types.equalsIgnoreCase("v")) {
            //String url="/storage/emulated/0/Products/Demo_Html/video/Horlicks1.mp4";
            String url = imgurl;
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(url,
                    MediaStore.Images.Thumbnails.MINI_KIND);
            imgview.setImageBitmap(thumb);
               /* BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
                holder.imgview.setBackgroundDrawable(bitmapDrawable);*/
        } else if (types.equalsIgnoreCase("p")) {
            imgview.setImageBitmap(getBitmap(new File(imgurl)));
        }
//        AsyncTaskExample asyntask = new AsyncTaskExample();
//        asyntask.execute();



        /*else {
            *//*decodeBitmapPath(String.valueOf(imageUri),230,150);*//*
            String imguri= String.valueOf(imageUri);
            String len=imguri.substring(7,imguri.length());
            Log.v("decode_bit",len);
            imgview.setImageBitmap(decodeBitmapPath(len,230,150));
        }
*/

        ll_edt_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edt_feed.requestFocus();
                    showKeypad(edt_feed);

            }
        });
        edt_feed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                store.setSlideFeed(String.valueOf(editable));
            }
        });
        return view;
    }
//    private class AsyncTaskExample extends AsyncTask<String, String, Void> {
//
//        final ProgressDialog mProgressDialog = new ProgressDialog(context);
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mProgressDialog.setMessage("Loading");
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.show();
//        }
//        @Override
//        protected Void doInBackground(String... strings) {
//            Uri imageUri = Uri.fromFile(new File(imgurl));
//            Log.v("img_uri_feed", String.valueOf(imageUri) + " type " + types + " url  " + imgurl);
//            if (types.equalsIgnoreCase("h")) {
//                String HTMLPath = imgurl.replaceAll(".zip", "");
//                File f = new File(HTMLPath);
//                files = f.listFiles(new FilenameFilter() {
//                    @Override
//                    public boolean accept(File dir, String filename) {
//                        return filename.contains(".png") || filename.contains(".jpg");
//                    }
//                });
//                String url = "";
//                try {
//
//                    if (files.length > 0) url = files[0].getAbsolutePath();
//                    File compressedImageFile = new Compressor(context).compressToFile(new File(url));
//                    Log.v("printted_uri", compressedImageFile.toString());
//                    Uri imageUri1 = Uri.fromFile(new File(url));
//                    imgview.setImageURI(Uri.fromFile(compressedImageFile));
//
//                } catch (Exception e) {
//                }
//            } else if (types.equalsIgnoreCase("i")) {
//                String imguri = String.valueOf(imageUri);
//                String len = imguri.substring(7, imguri.length());
//                Log.v("decode_bit", len);
//            /*try {
//                File compressedImageFile = new Compressor(context).compressToFile(new File(imgurl));
//                imgview.setImageURI(Uri.fromFile(compressedImageFile));
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.v("Exception_are_",e.getMessage());
//            }
//           *//* File ff=new File(imgurl);
//            Bitmap myBitmap = BitmapFactory.decodeFile(ff.getAbsolutePath());
//
//           // ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
//
//            imgview.setImageBitmap(myBitmap);
//            Log.v("image_gng_to_","datas");*//*
//            //imgview.setImageURI(Uri.parse(len));*/
//                try {
//                    imgview.setImageBitmap(decodeBitmapPath(len, 230, 150));
//                } catch (Exception e) {
//                    Log.v("Exception_are_", e.getMessage());
//                }
//            } else if (types.equalsIgnoreCase("v")) {
//                //String url="/storage/emulated/0/Products/Demo_Html/video/Horlicks1.mp4";
//                String url = imgurl;
//                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(url,
//                        MediaStore.Images.Thumbnails.MINI_KIND);
//                imgview.setImageBitmap(thumb);
//               /* BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
//                holder.imgview.setBackgroundDrawable(bitmapDrawable);*/
//            } else if (types.equalsIgnoreCase("p")) {
//                imgview.setImageBitmap(getBitmap(new File(imgurl)));
//            }
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Void result) {
//            mProgressDialog.dismiss();
//          //  imgview.setImageURI(Uri.fromFile(compressedImageFile));
//        }
//
//
//    }

   public  void showKeypad(View view) {
       InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
       inputMethodManager.showSoftInput(view,
               InputMethodManager.SHOW_IMPLICIT);

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

    public static Bitmap decodeBitmapPath(String path, int width,
                                          int height) {
        final BitmapFactory.Options ourOption = new BitmapFactory.Options();
        ourOption.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, ourOption);
        ourOption.inSampleSize = calculateInSampleSize(ourOption, width,
                height);
        // Decode bitmap with inSampleSize set
        ourOption.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(path, ourOption);
        return bmp;
    }
    public static int calculateInSampleSize(BitmapFactory.Options ourOption,
                                            int imageWidth, int imageHeight) {
        final int height = ourOption.outHeight;
        final int width = ourOption.outWidth;
        int inSampleSize = 1;
        if (height > imageHeight || width > imageWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) imageHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) imageWidth);
            }
        }
        return inSampleSize;
    }

}
