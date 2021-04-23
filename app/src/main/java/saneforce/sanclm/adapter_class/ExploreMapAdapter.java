package saneforce.sanclm.adapter_class;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.SlideDetail;
import saneforce.sanclm.activities.Model.ViewTagModel;
import saneforce.sanclm.adapter_interfaces.FindRouteListener;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.DownloadMasters;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.ProductChangeListener;

public class ExploreMapAdapter extends RecyclerView.Adapter<ExploreMapAdapter.MyViewHolder>{


    Context context;
    ArrayList<ViewTagModel> array = new ArrayList<>();
    private LayoutInflater mInflater;
    String laty,lngy;
    CommonSharedPreference mCommonSharedPreference;
    static FindRouteListener findRouteListener;
    public static String MY_API_KEY="AIzaSyD4E3jMHLmeOW9HFfyIXkk-2aFElUhKzUw";
    TextView txt_select_qua, txt_select_category, txt_select_class, txt_select_spec, txt_select_terr;

    String SF_Code, subSfCode, SF_Type, db_connPath, div_codee;

    String txt_qua,txt_cat,txt_class,txt_spec,txt_terr,div_codes;
    ArrayList<SlideDetail> list_dr=new ArrayList<>();
    DataBaseHandler dbh;
    static ProductChangeListener productChangeListener;
    String placeDetail,full_add,name_dr,db_slidedwnloadPath;
    StringBuilder place;
    DownloadMasters dwnloadMasterData;

    public ExploreMapAdapter(Activity context, ArrayList<ViewTagModel> array, String laty, String lngy) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.array=array;
        this.laty=laty;
        this.lngy=lngy;
        Log.v("explore_lay",array.size()+"");

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.row_item_explore, parent, false);
        mCommonSharedPreference=new CommonSharedPreference(context);

        SF_Code= mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        subSfCode=mCommonSharedPreference.getValueFromPreference("sub_sf_code");
        SF_Type =  mCommonSharedPreference.getValueFromPreference("sf_type");
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        div_codee =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);
        db_slidedwnloadPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        dbh = new DataBaseHandler(context);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txt_dr.setText(array.get(position).getName());
        holder.txt_add.setText(array.get(position).getAddr());
        if(!TextUtils.isEmpty(array.get(position).getCode())){
            Log.v("adapter_photo_val",array.get(position).getCode());
            StringBuilder bu=new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?photoreference=");
            bu.append(array.get(position).getReference());
            bu.append("&sensor=false");
            bu.append("&maxheight="+array.get(position).getHeight());
            bu.append("&maxwidth="+array.get(position).getWidth());
            bu.append("&key=AIzaSyD4E3jMHLmeOW9HFfyIXkk-2aFElUhKzUw");
            //https://maps.googleapis.com/maps/api/place/photo?photoreference=CmRaAAAAZGVKYoIApQ0FY9qrZcoSquyHJTmRzKG6cwIUAeDA7ARoddKmfSG9mb1KUYzBMkxj7IWR9efFGWhKLyyivm2gkWvdhWQZtqBc0GszOetFku_7MfjGyrhCJ5vgs2Il4U8vEhCwnDmXrtkrQWQYxUxKH_heGhSTiUC8g9jAdoZ0BJTMN5dEXXLJDA&sensor=false&maxheight=MAX_HEIGHT&maxwidth=MAX_WIDTH&key=YOUR_API_KEY

            Glide.with(context)
                    .load(bu.toString()) // image url
                    .placeholder(R.drawable.iv_profile_icon) // any placeholder to load at start
                    .error(R.drawable.iv_profile_icon)  // any image in case of error
                    .override(200, 200) // resizing
        .centerCrop()
        .into(holder.img_profile);
        }

        holder.rl_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productChangeListener.checkPosition(position);
            }
        });
        holder.btn_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_dr=array.get(position).getName();
                full_add=array.get(position).getAddr();
                getPlaceID(array.get(position).getPhno(),position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img_profile,img_selection;
        TextView txt_dr,txt_add;
        Button btn_visit;
        RelativeLayout rl_popup;
        public MyViewHolder(View itemView) {
            super(itemView);

            txt_dr=(TextView)itemView.findViewById(R.id.txt_dr);
            txt_add=(TextView)itemView.findViewById(R.id.txt_add);
           // btn_route=(Button)itemView.findViewById(R.id.btn_route);
            btn_visit=(Button)itemView.findViewById(R.id.btn_visit);
            img_profile=(ImageView)itemView.findViewById(R.id.img_profile);
            rl_popup=(RelativeLayout)itemView.findViewById(R.id.rl_popup);


        }
    }


    public void popupAddDoctr(String drname, String phno, String addr, final String uid){

        final Dialog dialog=new Dialog(context,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_add_dcr);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        txt_select_qua=(TextView)dialog.findViewById(R.id.txt_select_qua);
        txt_select_category=(TextView)dialog.findViewById(R.id.txt_select_category);
        txt_select_class=(TextView)dialog.findViewById(R.id.txt_select_class);
        txt_select_spec=(TextView)dialog.findViewById(R.id.txt_select_spec);
        txt_select_terr=(TextView)dialog.findViewById(R.id.txt_select_terr);
        ImageView img_close=(ImageView)dialog.findViewById(R.id.img_close);
        Button save_btn=(Button)dialog.findViewById(R.id.btn_save);
        final EditText edt_dr=(EditText)dialog.findViewById(R.id.edt_dr);
        final EditText edt_addr=(EditText)dialog.findViewById(R.id.edt_addr);
        final EditText edt_mob=(EditText)dialog.findViewById(R.id.edt_mob);
        final EditText edt_code=(EditText)dialog.findViewById(R.id.edt_code);
        final EditText edt_phone=(EditText)dialog.findViewById(R.id.edt_phone);

        edt_dr.setText(drname);
        edt_addr.setText(addr);
        edt_phone.setText(phno);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txt_select_qua.getText().toString().isEmpty() && !txt_select_category.getText().toString().isEmpty()
                        && !txt_select_class.getText().toString().isEmpty() && !txt_select_spec.getText().toString().isEmpty()
                        && !txt_select_terr.getText().toString().isEmpty() && !edt_dr.getText().toString().isEmpty() ){
                    Log.v("qualification_txt","arent_empty");



                    JSONObject json=new JSONObject();
                    try{
                       /* if(SF_Type.equalsIgnoreCase("2"))
                            json.put("SF", SF_coding.get(spinnerpostion));
                        else*/
                            json.put("SF", SF_Code);
                        json.put("DivCode", div_codee);
                        json.put("DrName", edt_dr.getText().toString());
                        json.put("DrQCd", txt_qua.substring(txt_qua.indexOf(",")+1));
                        json.put("DrQNm", txt_select_qua.getText().toString());
                        json.put("DrClsCd", txt_class.substring(txt_class.indexOf(",")+1));
                        json.put("DrClsNm", txt_select_class.getText().toString());
                        json.put("DrCatCd", txt_cat.substring(txt_cat.indexOf(",")+1));
                        json.put("DrCatNm", txt_select_category.getText().toString());
                        json.put("DrSpcCd", txt_spec.substring(txt_spec.indexOf(",")+1));
                        json.put("DrSpcNm", txt_select_spec.getText().toString());
                        json.put("DrAddr", edt_addr.getText().toString());
                        json.put("DrTerCd", txt_terr.substring(txt_terr.indexOf(",")+1));
                        json.put("DrTerNm", txt_select_terr.getText().toString());
                        json.put("DrPincd", edt_code.getText().toString());
                        json.put("DrPhone", edt_phone.getText().toString());
                        json.put("DrMob", edt_mob.getText().toString());
                        json.put("Uid", uid);
                        Log.v("printing_add_dr",json.toString());

                        addDoctor(json.toString(),dialog);

                    }catch (Exception e){

                    }

                }
                else{
                    Toast.makeText(context,"Please fill all fields ",Toast.LENGTH_SHORT).show();
                }

            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        txt_select_qua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingTableValue(1);
            }
        });
        txt_select_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingTableValue(2);
            }
        });
        txt_select_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingTableValue(3);
            }
        });
        txt_select_spec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingTableValue(4);
            }
        });
        txt_select_terr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingTableValue(5);
            }
        });
    }

    public void gettingTableValue(int x){
        list_dr.clear();
        dbh.open();
        Cursor cur=null;
        if(x==1)
            cur=dbh.select_quality_list();
        else if(x==2)
            cur=dbh.select_category_list();
        else if(x==3)
            cur=dbh.select_class_list();
        else if(x==4)
            cur=dbh.select_speciality_list();
        else
            cur=dbh.select_ClusterList();

        if(cur.getCount()>0){
            while(cur.moveToNext()){
                list_dr.add(new SlideDetail(cur.getString(2),cur.getString(1)));

            }
        }

        popupSpinner(x);
    }

    public void popupSpinner(final int x){
        final Dialog dialog=new Dialog(context,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_spinner_selection);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ListView popup_list=(ListView)dialog.findViewById(R.id.popup_list);

        AdapterPopupSpinnerSelection popupAdapter=new AdapterPopupSpinnerSelection(context,list_dr);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("qualification_select",list_dr.get(position).getInputName());
                if(x==1) {
                    txt_select_qua.setText(list_dr.get(position).getInputName());
                    txt_qua=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();
                }
                else if(x==2){
                    txt_select_category.setText(list_dr.get(position).getInputName());
                    txt_cat=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();
                }
                else if(x==3) {
                    txt_select_class.setText(list_dr.get(position).getInputName());
                    txt_class=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();
                }
                else if(x==4) {
                    txt_select_spec.setText(list_dr.get(position).getInputName());
                    txt_spec=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();
                }
                else {
                    txt_select_terr.setText(list_dr.get(position).getInputName());
                    txt_terr=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();
                }

                dialog.dismiss();
            }
        });


    }

    public static void bindListenerForPosition(ProductChangeListener prodcut){
        productChangeListener=prodcut;
    }

/*
    public void addDoctor(String val, final Dialog dialog){
        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        Call<ResponseBody> drDetail = apiService.addDr(val);
        drDetail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                        Log.v("today_added_lis",is.toString());
                        JSONObject json=new JSONObject(is.toString());
                        if(json.getString("success").equalsIgnoreCase("true")){
                            dialog.dismiss();
                            Toast.makeText(context," Saved successfully!! ",Toast.LENGTH_SHORT).show();
                            if (progressDialog == null) {
                                CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(getActivity());
                                progressDialog = commonUtilsMethods.createProgressDialog(getActivity());
                                progressDialog.show();
                            } else {
                                progressDialog.show();
                            }
                            dwnloadMasterData = new DownloadMasters(getActivity(), db_connPath, db_slidedwnloadPath, SF_Code,8);
                            dwnloadMasterData.unDrList();

                            DownloadMasters.bindManagerListLoading(new ManagerListLoading() {
                                @Override
                                public void ListLoading() {
                                    dbh.open();
                                    drList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                                    drList.clear();//spinnerpostion
                                    if(SF_Type.equalsIgnoreCase("2"))
                                        mCursor = dbh.select_unListeddoctors_bySf(SF_coding.get(spinnerpostion),mMydayWtypeCd);
                                    else
                                        mCursor = dbh.select_unListeddoctors_bySf(SF_Code,mMydayWtypeCd);

                                    while (mCursor.moveToNext()) {
                                        _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(6),mCursor.getString(5));
                                        drList.add(_custom_DCR_GV_Dr_Contents);
                                    }
                                    gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
                                    _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),drList);
                                    gridView.setAdapter(_DCR_GV_Selection_adapter);
                                    progressDialog.dismiss();
                                    dbh.close();

                        */
/*dbh.open();
                        drList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                        drList.clear();
                        mCursor = dbh.select_unListeddoctors_bySf(SF_coding.get(i),mMydayWtypeCd);
                        while (mCursor.moveToNext()) {
                            _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(6),mCursor.getString(5));
                            drList.add(_custom_DCR_GV_Dr_Contents);
                        }

                        gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
                        _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),drList);
                        gridView.setAdapter(_DCR_GV_Selection_adapter);
                        progressDialog.dismiss();
                        dbh.close();*//*

                                }
                            });

                        }


                    }catch (Exception e){
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
*/

    class findPlaceDetail extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            DownloadUrl downloadUrl = new DownloadUrl();
            try {
                placeDetail = downloadUrl.readUrl(place.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
          //  Log.v("get_dr_detttt",googlePlacesData);
            //getDrDetail(googlePlacesData);
            extractPhno();
        }
    }

    public class DownloadUrl {

        public String readUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer("");

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                Log.d("downloadUrl", data.toString());
                br.close();

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }
    }

    public void getPlaceID(String placesdata,int pos){
        // https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJYe6-4klmUjoRUVZMrQ6ThEI&key=AIzaSyD4E3jMHLmeOW9HFfyIXkk-2aFElUhKzUw

        place = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        place.append("placeid="+placesdata);
        place.append("&key=AIzaSyD4E3jMHLmeOW9HFfyIXkk-2aFElUhKzUw");
        Log.v("Doctor_detail_print",place.toString());

        new findPlaceDetail().execute();
    }


    public void extractPhno() {
        JSONObject json = null;
        try {
            JSONObject jsonObject = new JSONObject(placeDetail);
             json = jsonObject.getJSONObject("result");
          /*  */
            Log.v("print_phnoo",json.getString("formatted_phone_number")+" addr "+json.getString("formatted_address")+json.getString("id"));
            popupAddDoctr(json.getString("name"),json.getString("formatted_phone_number"),json.getString("formatted_address"),json.getString("id"));

        } catch (Exception e) {
            Log.v("Missing_formated_iise",e.getMessage());
            try {
                popupAddDoctr(name_dr, " ", full_add, json.getString("id"));
            }catch (Exception e1){}
        }

    }

    public void addDoctor(String val, final Dialog dialog){
        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        Call<ResponseBody> drDetail = apiService.addDr(val);
        drDetail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                        Log.v("today_added_lis",is.toString());
                        JSONObject json=new JSONObject(is.toString());
                        if(json.getString("Qry").equalsIgnoreCase("false")){
                            dialog.dismiss();
                            Toast.makeText(context,"Already added into master",Toast.LENGTH_SHORT).show();
                           /* if (progressDialog == null) {
                                CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(getActivity());
                                progressDialog = commonUtilsMethods.createProgressDialog(getActivity());
                                progressDialog.show();
                            } else {
                                progressDialog.show();
                            }
                            dwnloadMasterData = new DownloadMasters(getActivity(), db_connPath, db_slidedwnloadPath, SF_Code,8);
                            dwnloadMasterData.unDrList();

                            DownloadMasters.bindManagerListLoading(new ManagerListLoading() {
                                @Override
                                public void ListLoading() {
                                    dbh.open();
                                    drList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                                    drList.clear();//spinnerpostion
                                    if(SF_Type.equalsIgnoreCase("2"))
                                        mCursor = dbh.select_unListeddoctors_bySf(SF_coding.get(spinnerpostion),mMydayWtypeCd);
                                    else
                                        mCursor = dbh.select_unListeddoctors_bySf(SF_Code,mMydayWtypeCd);

                                    while (mCursor.moveToNext()) {
                                        _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(6),mCursor.getString(5));
                                        drList.add(_custom_DCR_GV_Dr_Contents);
                                    }
                                    gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
                                    _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),drList);
                                    gridView.setAdapter(_DCR_GV_Selection_adapter);
                                    progressDialog.dismiss();
                                    dbh.close();*/

                        /*dbh.open();
                        drList = new ArrayList<Custom_DCR_GV_Dr_Contents>();
                        drList.clear();
                        mCursor = dbh.select_unListeddoctors_bySf(SF_coding.get(i),mMydayWtypeCd);
                        while (mCursor.moveToNext()) {
                            _custom_DCR_GV_Dr_Contents = new Custom_DCR_GV_Dr_Contents(mCursor.getString(2),mCursor.getString(1),mCursor.getString(10),mCursor.getString(9),mCursor.getString(6),mCursor.getString(5));
                            drList.add(_custom_DCR_GV_Dr_Contents);
                        }

                        gridView = (GridView) v.findViewById(R.id.gridview_dcrselect);
                        _DCR_GV_Selection_adapter = new DCR_GV_Selection_adapter(getContext(),drList);
                        gridView.setAdapter(_DCR_GV_Selection_adapter);
                        progressDialog.dismiss();
                        dbh.close();*/
                                //}
                           // });

                        }
                        else{
                            dialog.dismiss();
                            dwnloadMasterData = new DownloadMasters(context, db_connPath, db_slidedwnloadPath, SF_Code,8);
                            dwnloadMasterData.unDrList();
                            Toast.makeText(context," Saved successfully!! ",Toast.LENGTH_SHORT).show();

                        }


                    }catch (Exception e){
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }




}
