package saneforce.sanclm.adapter_class;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.FeedbackActivity;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.fragments.LocaleHelper;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.UpdateUi;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;

public class TodayCalls_recyclerviewAdapter extends RecyclerView.Adapter<TodayCalls_recyclerviewAdapter.MyViewHolder> {

    List<Custom_Todaycalls_contents> custom_todaycalls_contents;
    Context context;
    DataBaseHandler dbh;
    int pos=-1;
    String dr_name,visitTime;
    CommonSharedPreference mCommonSharedPreference;
    String db_connPath,SF_Code;
    ProgressDialog progressDialog=null;
    Api_Interface apiService;
    String language;
//    Context context;
    Resources resources;
    static UpdateUi mUpdateUi;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView DrName, DrVst;
        ImageView DrCallSync,iv_del,iv_edit,iv_sync;
        RelativeLayout rl_view;

        public MyViewHolder(View view) {
            super(view);
            DrName = (TextView) view.findViewById(R.id.tv_tdaycall_drNm);
            DrVst = (TextView) view.findViewById(R.id.tv_tdaycall_vstDt);
            DrCallSync = (ImageView) view.findViewById(R.id.iv_tdaycall_pf);
            iv_del = (ImageView) view.findViewById(R.id.iv_del);
            iv_sync = (ImageView) view.findViewById(R.id.iv_sync);
            iv_edit = (ImageView) view.findViewById(R.id.iv_edit);
            rl_view=(RelativeLayout)view.findViewById(R.id.rl_view);
        }
        }

    public TodayCalls_recyclerviewAdapter(Context context, List<Custom_Todaycalls_contents> custom_todaycalls_contents) {
        this.custom_todaycalls_contents = custom_todaycalls_contents;
        this.context = context;
        dbh = new DataBaseHandler(this.context);
        mCommonSharedPreference=new CommonSharedPreference(this.context);
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        Log.v("todaycall_recycle",this.custom_todaycalls_contents.size()+" rr "+db_connPath);
         apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
    }

    @Override
        public TodayCalls_recyclerviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_today_calls, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final TodayCalls_recyclerviewAdapter.MyViewHolder holder, final int position) {
            final Custom_Todaycalls_contents tdaycall = custom_todaycalls_contents.get(position);

            SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            language = sharedPreferences.getString(language_string, "");
            if (!language.equals("")){
                Log.d("homelang",language);
                selected(language);
                context = LocaleHelper.setLocale(context, language);
                resources = context.getResources();
            }else {
                selected("en");
                context = LocaleHelper.setLocale(context, "en");
                resources = context.getResources();
            }


             dr_name=tdaycall.getDrName();
            visitTime=tdaycall.getVstDt();
            Log.v("dr_names_today",dr_name);
                if(!tdaycall.isLocal()){
                    holder.iv_edit.setVisibility(View.VISIBLE);

                    String value =mCommonSharedPreference.getValueFromPreference("showDelete");
                    Log.v("delete_val",value);

                    if(value.equals("null")){
                        value ="0";
                    }


                    if(value.equalsIgnoreCase("0"))
                    holder.iv_del.setVisibility(View.VISIBLE);
                    else
                        holder.iv_del.setVisibility(View.INVISIBLE);
                    holder.iv_sync.setVisibility(View.INVISIBLE);
                }
                if(tdaycall.getDrName().indexOf("_")!=-1 && tdaycall.isLocal()){
                    dr_name=tdaycall.getDrName().substring(0,tdaycall.getDrName().indexOf("_"));
                    Log.v("printing_dr_name",dr_name);
                    holder.iv_edit.setVisibility(View.INVISIBLE);
                    holder.iv_del.setVisibility(View.VISIBLE);
                    holder.iv_sync.setVisibility(View.VISIBLE);
                    visitTime+="  [Offline]";
                }else if(tdaycall.isLocal()){
                    holder.DrName.setTextColor(Color.RED);
                    visitTime+="  [Draft]";
                }else
                {
                    holder.DrName.setTextColor(Color.BLACK);

                }


            holder.DrName.setText(dr_name);
            holder.DrVst.setText(visitTime);

            holder.iv_sync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dbh.open();
                    Cursor cur1 = dbh.select_json_list1(tdaycall.getDrCode());

                    if (cur1.getCount() > 0) {
                        while (cur1.moveToNext()) {
                            if (cur1.getString(2).indexOf("_") != -1) {
                                Log.v("printing_totla_val", cur1.getString(1) + " id_here " + cur1.getInt(0));
                                finalSubmission(cur1.getString(1), cur1.getInt(0),cur1.getString(4));
                            }
                        }
                    }
                    dbh.close();
                }
            });


//            if(dr_name.equals("DocName"))
//            {
//                holder.rl_view.setVisibility(View.GONE);
//            }




                /*holder.DrName.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return false;
                        int
                    }
                });
                holder.DrName.setOnTouchListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.DrName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        holder.DrName.setSelected(true);
                    }
                });*/



            holder.iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String clustername = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_FLAG);
                    if (!clustername.equalsIgnoreCase("F")) {
                        Toast.makeText(context, resources.getString(R.string.NonFieldedit), Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences share = context.getSharedPreferences("feed_list", 0);
                        SharedPreferences.Editor edit = share.edit();
                        edit.putString("name", tdaycall.getDrName());
                        edit.putString("time", "00:00:00");
                        edit.putString("val", tdaycall.getARCd());
                        edit.putInt("columnid", Integer.parseInt(tdaycall.getVstTyp()));
                        edit.putString("type", tdaycall.getType());
                        edit.putString("common", tdaycall.getCommon());
                        edit.putString("code", tdaycall.getDrCode());
                        CommonUtils.TAG_DOCTOR_CODE = tdaycall.getDrCode();
                        Log.v("Doc_Name_Hme", tdaycall.getVstTyp());

                        edit.commit();


                        if (!tdaycall.isLocal()) {
                            mCommonSharedPreference.setValueToPreference("Draft", "false");
                            JSONObject json = new JSONObject();
                            try {
                                Toast toast = Toast.makeText(context, "Processing Data", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                json.put("detno", tdaycall.getARCd());
                                json.put("sf_code", SF_Code);
                                json.put("cusname", tdaycall.getDrName());
                                json.put("custype", tdaycall.getVstTyp());
                                String typ = "";
                                if (tdaycall.getVstTyp().equalsIgnoreCase("1"))
                                    typ = "D";
                                else if (tdaycall.getVstTyp().equalsIgnoreCase("2"))
                                    typ = "C";
                                else if (tdaycall.getVstTyp().equalsIgnoreCase("6"))
                                    typ = "I";
                                else
                                    typ = "";
                                if (mCommonSharedPreference.getValueFromPreference("feed_pob").contains(typ))
                                    json.put("pob", "1");
                                CommonUtils.TAG_DOCTOR_CODE = tdaycall.getDrCode();
                                Log.v("json_values_are_today", json.toString());
                                mCommonSharedPreference.setValueToPreference("detno", tdaycall.getARCd());
                                mCommonSharedPreference.setValueToPreference("visit", tdaycall.getVstDt());
                                apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
                                Call<ResponseBody> tdayTP = apiService.editCall(String.valueOf(json));
                                tdayTP.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        InputStreamReader ip = null;
                                        StringBuilder is = new StringBuilder();
                                        String line = null;
                                        try {
                                            ip = new InputStreamReader(response.body().byteStream());
                                            BufferedReader bf = new BufferedReader(ip);

                                            while ((line = bf.readLine()) != null) {
                                                is.append(line);
                                            }
                                            Log.v("printing_the_whole", is.toString() + " printing_type" + tdaycall.getVstTyp());

                                            SharedPreferences share = context.getSharedPreferences("feed_list", 0);
                                            SharedPreferences.Editor edit = share.edit();
                                            edit.putString("name", tdaycall.getDrName());
                                            edit.putString("time", "00:00:00");
                                            edit.putString("val", is.toString());
                                            edit.putInt("columnid", -1);
                                            if (tdaycall.getVstTyp().equalsIgnoreCase("1"))
                                                edit.putString("type", "D");
                                            else if (tdaycall.getVstTyp().equalsIgnoreCase("2"))
                                                edit.putString("type", "C");
                                            else if (tdaycall.getVstTyp().equalsIgnoreCase("4"))
                                                edit.putString("type", "U");
                                            else if (tdaycall.getVstTyp().equalsIgnoreCase("6"))
                                                edit.putString("type", "I");
                                            else
                                                edit.putString("type", "S");
                                            edit.putString("common", SF_Code);
                                            edit.putString("code", tdaycall.getDrCode());
                                            edit.commit();
                                            Intent i = new Intent(context, FeedbackActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            i.putExtra("feedpage", "edit");
                                            i.putExtra("custype", tdaycall.getVstTyp());
                                            context.startActivity(i);

                                        } catch (Exception e) {

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            mCommonSharedPreference.setValueToPreference("Draft", "true");
                            Intent i = new Intent(context, FeedbackActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("feedpage", "edit");
                            i.putExtra("custype", tdaycall.getVstTyp());

                            context.startActivity(i);
                        }


                    }
                }
            });
            holder.iv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!tdaycall.isLocal()){
                        Log.v("delete_the_call","here");
                        Toast toast=Toast.makeText(context, "Deleting Data", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        try {
                            JSONObject json=new JSONObject();
                            json.put("amc",tdaycall.getARCd());
                            Call<ResponseBody> tdayTP = apiService.delCall(String.valueOf(json));
                            tdayTP.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    InputStreamReader ip = null;
                                    StringBuilder is = new StringBuilder();
                                    String line = null;
                                    try {
                                        ip = new InputStreamReader(response.body().byteStream());
                                        BufferedReader bf = new BufferedReader(ip);

                                        while ((line = bf.readLine()) != null) {
                                            is.append(line);
                                        }
                                        Log.v("printing_the_whole", is.toString());

                                        JSONObject jj=new JSONObject(is.toString());
                                        if(jj.getString("success").equalsIgnoreCase("true")){
                                            dbh.open();
                                            dbh.delete_json1(tdaycall.getDrCode());
                                            dbh.close();
                                            custom_todaycalls_contents.remove(position);
                                            HomeDashBoard.tv_todaycall_count.setText(resources.getString(R.string.total)+" "+ custom_todaycalls_contents.size() + " "+resources.getString(R.string.calls));
                                            notifyDataSetChanged();
                                        }
                                        else
                                            Toast.makeText(context,"Cannot delete the call",Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                }
                            });
                        }catch (Exception e){



                        }
                    }
                    else {
                        dbh.open();
                        dbh.delete_json1(tdaycall.getDrCode());
                        dbh.close();
                        custom_todaycalls_contents.remove(position);
                        HomeDashBoard.tv_todaycall_count.setText(resources.getString(R.string.total)+" " + custom_todaycalls_contents.size() + " "+resources.getString(R.string.calls));

                    }
                    notifyDataSetChanged();
                }
            });
            //   holder.DrCallSync.setText(movie.getYear());
        }

    private void selected(String language) {
        Locale myLocale = new Locale(language);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


    @Override
        public int getItemCount() {
            return custom_todaycalls_contents.size();
        }

    public void finalSubmission(String val, final int id,String drcode){
        Call<ResponseBody> query=apiService.finalSubmit(val);
        query.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStreamReader ip=null;
                StringBuilder is=new StringBuilder();
                String line=null;
                try {
                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    Log.v("final_submit_working",is.toString());
                    JSONObject js=new JSONObject(is.toString());
                    if(js.getString("success").equals("true")){
                        Log.v("final_submit_working","success");
                        dbh.open();
                        dbh.delete_json1(drcode);
                        if(mUpdateUi!=null)
                            mUpdateUi.updatingui();

                        custom_todaycalls_contents.remove(pos);
                        notifyDataSetChanged();



                        // Toast.makeText(FeedbackActivity.this, "Data Submitted Successfully", Toast.LENGTH_LONG).show();
                    }
                    else{
                       Toast.makeText(context, js.getString("Msg"), Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e){
                    Log.v("error>>",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


}
