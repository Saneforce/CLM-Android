package saneforce.sanclm.adapter_class;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.GridSelectionList;

public class PresentationRecycleAdapter extends RecyclerView.Adapter<PresentationRecycleAdapter.MyViewHolder>{

        Context context;
        ArrayList<String> array=new ArrayList<>();
    private LayoutInflater mInflater;
    public List<Custom_Products_GridView_Contents> mProducts_GridView_Contents;
    DetailingTrackerPOJO mDetailingTrackerPOJO;
    DataBaseHandler dbh;
    static GridSelectionList listener;
    public boolean img_selection_result=false;
    int startPos;
    static ArrayList<Boolean> selectionState=new ArrayList<>();
    CommonSharedPreference mCommonSharedPreference;
    JSONArray jsonArray=new JSONArray();

        public PresentationRecycleAdapter(Activity context,int mGridViewLayoutDesign, ArrayList<Custom_Products_GridView_Contents> array) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mProducts_GridView_Contents=array;
            dbh=new DataBaseHandler(this.context);
            mDetailingTrackerPOJO = new DetailingTrackerPOJO();
            mCommonSharedPreference=new CommonSharedPreference(this.context);

        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView =  mInflater.inflate(R.layout.row_item_recycler, parent, false);
        return new MyViewHolder(itemView);
        }

    public void addItem(Custom_Products_GridView_Contents row) {
        mProducts_GridView_Contents.add(row);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Custom_Products_GridView_Contents _product_contents = mProducts_GridView_Contents.get(position);


        Log.v("product_grid_list",_product_contents.getmLogoURL());

        if(_product_contents.getmSelectionState()){
            holder.img.setAlpha(255);
            holder.img_selection.setImageResource(R.mipmap.selected);
        }
        else{
            holder.img.setAlpha(40);
            holder.img_selection.setImageResource(R.mipmap.unselect);
        }
        holder.img.setImageBitmap(_product_contents.getBitmap());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("img_click","onclicj"+_product_contents.getmSelectionState()+" nam "+_product_contents.getmProductName());
                if(_product_contents.getmSelectionState()==true){
                    holder.img_selection.setImageResource(R.mipmap.unselect);
                    _product_contents.setmSelectionState(false);
                    holder.img.setAlpha(40);
                    removeJsonArray(_product_contents.mLocalID);
                    Log.v("img_click_after","onclicj"+_product_contents.getmSelectionState()+" nam "+_product_contents.getmProductName());

                }
                else{
                    holder.img_selection.setImageResource(R.mipmap.selected);
                    _product_contents.setmSelectionState(true);
                    holder.img.setAlpha(255);
                    addJsonArray(position);
                }
                searchSelectview();

            }
            });

       /* Bitmap bm = null;

        if (_product_contents.getmFiletype().equalsIgnoreCase("i")) {
            Uri imageUri = Uri.fromFile(new File(_product_contents.getmLogoURL()));
            holder.img.setImageURI(imageUri);
            Log.v("Presentation_drag_im","adapter_called");

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
            Uri imageUri = Uri.fromFile(new File(url));
            holder.img.setImageURI(imageUri);
            Log.v("Presentation_drag_ht","adapter_called");

        }
        if (_product_contents.getmFiletype().equalsIgnoreCase("v")) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(_product_contents.getmLogoURL());
            bm = mediaMetadataRetriever.getFrameAtTime(5000000); //unit in microsecond
            holder.img.setImageBitmap(bm);
            Log.v("Presentation_drag_vv","adapter_called");

        }


        if (_product_contents.getmFiletype().equalsIgnoreCase("p")) {
            Drawable myDrawable = context.getResources().getDrawable(R.mipmap.btn_go);
            bm = ((BitmapDrawable) myDrawable).getBitmap();
            bm = scaleDown(bm, 200, true);
            holder.img.setImageBitmap(bm);
            Log.v("Presentation_drag_pp","adapter_called");
            //mViewHolder.mProductLogo.setImageBitmap(bm.createBitmap(width, height, config));
        }*/

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

    public void onItemMove(int fPos,int tPos){

    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,boolean filter)
    {
        float ratio = Math.min((float) maxImageSize / realImage.getWidth(),(float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }


    @Override
    public int getItemCount() {
        return mProducts_GridView_Contents.size();
        }

    public class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView img,img_selection;
        public MyViewHolder(View itemView) {
        super(itemView);
        img=(ImageView)itemView.findViewById(R.id.img1);
        img_selection=(ImageView)itemView.findViewById(R.id.img_selection);
        }
    }
    public static void bindListener(GridSelectionList listeners){
        listener=listeners;
    }

    public void removeJsonArray(String code){
        try {
            Log.v("code_print_67",code);
            JSONArray newJson=new JSONArray();
            JSONArray jsonn=new JSONArray(mCommonSharedPreference.getValueFromPreference("selection_product").toString());
            for(int i=0;i<jsonn.length();i++){
                JSONObject js=jsonn.getJSONObject(i);
                JSONArray   jj=js.getJSONArray("Slides");
                JSONObject  jobj=jj.getJSONObject(0);
                if(!code.equalsIgnoreCase(jobj.getString("SlideId"))){
                    newJson.put(js);
                }
                Log.v("valid_prd_code",newJson.toString());
            }
            mCommonSharedPreference.setValueToPreference("selection_product",newJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void addJsonArray(int pos){
        Custom_Products_GridView_Contents   mm=mProducts_GridView_Contents.get(pos);
        try {
            JSONArray jsonn=new JSONArray(mCommonSharedPreference.getValueFromPreference("selection_product").toString());
            if(jsonn.length()==0){
                JSONObject ProductBrdSLidesJson = new JSONObject();
                JSONArray   jsonA=new JSONArray();
                ProductBrdSLidesJson.put("BrdCode", mm.getmProductCode());
                ProductBrdSLidesJson.put("BrdName", mm.getmProductName());
                JSONObject SLidesJson = new JSONObject();
                SLidesJson.put("SlideId", mm.getmLocalID());
                SLidesJson.put("SlideName", mm.getmFileName());
                SLidesJson.put("SlideType", mm.getmFiletype());
                SLidesJson.put("SlideLocUrl", mm.getmLogoURL());
                SLidesJson.put("PrdCode", "-1");

                jsonA.put(SLidesJson);
                ProductBrdSLidesJson.put("Slides",jsonA);
                jsonArray.put(ProductBrdSLidesJson);

                Log.v("json_print_b4sele","local_id "+mm.getmLocalID()+" array"+jsonArray.toString());
                mCommonSharedPreference.setValueToPreference("selection_product",jsonArray.toString());
                jsonArray=new JSONArray();
            }
            else{
                JSONObject ProductBrdSLidesJson = new JSONObject();
                JSONArray   jsonA=new JSONArray();
                ProductBrdSLidesJson.put("BrdCode", mm.getmProductCode());
                ProductBrdSLidesJson.put("BrdName", mm.getmProductName());
                JSONObject SLidesJson = new JSONObject();
                SLidesJson.put("SlideId", mm.getmLocalID());
                SLidesJson.put("SlideName", mm.getmFileName());
                SLidesJson.put("SlideType", mm.getmFiletype());
                SLidesJson.put("SlideLocUrl", mm.getmLogoURL());
                SLidesJson.put("PrdCode", "-1");

                jsonA.put(SLidesJson);
                ProductBrdSLidesJson.put("Slides",jsonA);
                //jsonArray.put(ProductBrdSLidesJson);
                jsonn.put(ProductBrdSLidesJson);
                Log.v("json_print_sele",jsonn.toString());
                mCommonSharedPreference.setValueToPreference("selection_product",jsonn.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    }
