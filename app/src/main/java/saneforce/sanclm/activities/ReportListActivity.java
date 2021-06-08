package saneforce.sanclm.activities;

import android.app.Dialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelForReportList;
import saneforce.sanclm.activities.Model.ModelReportList;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.adapter_class.AdapterForMisssedReportList;
import saneforce.sanclm.adapter_class.AdapterForReportList;
import saneforce.sanclm.adapter_class.AdapterForSelectionList;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.fragments.LocaleHelper;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;

public class ReportListActivity extends AppCompatActivity {

    String acd,typ;
    RecyclerView recycle_view;
    ArrayList<ModelReportList> array=new ArrayList<>();
    ArrayList<ModelForReportList> mis_array=new ArrayList<>();
    ArrayList<ModelForReportList> filter_mis_array=new ArrayList<>();
    AdapterForReportList adpt;
    AdapterForMisssedReportList missed_adpt;
    CommonSharedPreference commonSharedPreference;
    Api_Interface apiService;
    String db_connPath;
    ImageView back_icon;
    String val,sf_code,dt;
    public DrawerLayout drawer;
    ArrayList<PopFeed> cluster_list=new ArrayList<>();
    ArrayList<PopFeed> category_list=new ArrayList<>();
    ArrayList<PopFeed> speciality_list=new ArrayList<>();
    ArrayList<PopFeed> qualification_list=new ArrayList<>();
    ArrayList<PopFeed> class_list=new ArrayList<>();
    ImageView close_img,search_icon;
    CardView card_cluster,card_cat,card_spe,card_qua,card_cls;
    String clu="",cat="",spec="",qua="",cls="";
    TextView txt_clu,txt_cat,txt_spec,txt_cls,txt_qua,txt_head_report;
    Button btn_apply,btn_clr;
    String language;
    Context context;
    Resources resources;

    public void commonFun(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedPreferences.getString(language_string, "");
        if (!language.equals("")){
            Log.d("homelang",language);
            selected(language);
            context = LocaleHelper.setLocale(ReportListActivity.this, language);
            resources = context.getResources();
        }else {
            selected("en");
            context = LocaleHelper.setLocale(ReportListActivity.this, "en");
            resources = context.getResources();
        }

        Bundle value=getIntent().getExtras();
        val=value.getString("val");
        if(val.equalsIgnoreCase("1")) {
            acd = value.getString("acd");
            typ = value.getString("typ");
        }
        else{
            sf_code=value.getString("sf");
            dt=value.getString("dt");
        }
        recycle_view=(RecyclerView)findViewById(R.id.recycle_view);
        back_icon=(ImageView)findViewById(R.id.back_icon);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        close_img=(ImageView)findViewById(R.id.close_img);
        search_icon=(ImageView)findViewById(R.id.search_icon);
        card_cluster=(CardView)findViewById(R.id.card_cluster);
        card_cat=(CardView)findViewById(R.id.card_cat);
        card_spe=(CardView)findViewById(R.id.card_spe);
        card_qua=(CardView)findViewById(R.id.card_qua);
        card_cls=(CardView)findViewById(R.id.card_cls);
        txt_clu=(TextView)findViewById(R.id.txt_clu);
        txt_head_report=(TextView)findViewById(R.id.txt_head_report);
        txt_cat=(TextView)findViewById(R.id.txt_cat);
        txt_spec=(TextView)findViewById(R.id.txt_spec);
        txt_cls=(TextView)findViewById(R.id.txt_cls);
        txt_qua=(TextView)findViewById(R.id.txt_qua);
        btn_apply=(Button)findViewById(R.id.btn_apply);
        btn_clr=(Button)findViewById(R.id.btn_clr);

        commonSharedPreference=new CommonSharedPreference(ReportListActivity.this);
        db_connPath =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);

        if(val.equalsIgnoreCase("1")) {
            RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext());
            recycle_view.setLayoutManager(layout);
            recycle_view.setItemAnimator(new DefaultItemAnimator());
            adpt = new AdapterForReportList(ReportListActivity.this, array,typ);
            recycle_view.setAdapter(adpt);
            search_icon.setVisibility(View.INVISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            txt_head_report.setText(resources.getString(R.string.dayreport));

            callApi();
        }
        else{
            RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext());
            recycle_view.setLayoutManager(layout);
            recycle_view.setItemAnimator(new DefaultItemAnimator());
            missed_adpt = new AdapterForMisssedReportList(ReportListActivity.this, mis_array);
            recycle_view.setAdapter(missed_adpt);
            txt_head_report.setText(resources.getString(R.string.missedreport));
            callApiMissed();
        }

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        card_cluster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomShowDialog(1);
            }
        });
        card_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomShowDialog(2);
            }
        });
        card_spe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomShowDialog(3);
            }
        });
        card_qua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomShowDialog(4);
            }
        });
        card_cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomShowDialog(5);
            }
        });
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.END);
                commonFun();
            }
        });

        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    //super.onBackPressed();
                }
                commonFun();
            }
        });

        btn_clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTheArraylist(cluster_list);
                clearTheArraylist(category_list);
                clearTheArraylist(speciality_list);
                clearTheArraylist(qualification_list);
                clearTheArraylist(class_list);
                txt_cls.setText("Select Class");
                txt_qua.setText("Select Qualification");
                txt_spec.setText("Select Speciality");
                txt_cat.setText("Select Category");
                txt_clu.setText("Select Cluster");
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filter_mis_array.clear();
                filter_mis_array=filterThosField(mis_array,cluster_list,1);
                filter_mis_array=filterThosField(filter_mis_array,category_list,2);
                filter_mis_array=filterThosField(filter_mis_array,speciality_list,3);
                filter_mis_array=filterThosField(filter_mis_array,qualification_list,4);
                filter_mis_array=filterThosField(filter_mis_array,class_list,5);



/*
                    for(int j=0;j<cluster_list.size();j++){
                        PopFeed pp=cluster_list.get(j);
                        if(pp.isClick()) {
                            for (int i = 0; i < mis_array.size(); i++) {
                                if(mis_array.get(i).getCluster().equalsIgnoreCase(pp.getTxt()))
                                    filter_mis_array.add(new ModelForReportList(mis_array.get(i).getName(),mis_array.get(i).getDate(),mis_array.get(i).getCluster(),
                                            mis_array.get(i).getQualify(),mis_array.get(i).getSpec(),mis_array.get(i).getPrev(),mis_array.get(i).getCategory(),
                                            mis_array.get(i).getCls()));
                            }

                        }
                     }
*/
                missed_adpt = new AdapterForMisssedReportList(ReportListActivity.this, filter_mis_array);
                recycle_view.setAdapter(missed_adpt);
                missed_adpt.notifyDataSetChanged();

                drawer.closeDrawer(GravityCompat.END);            }
        });
        commonFun();
    }

    private void selected(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public void callApi(){
        try{
            JSONObject json=new JSONObject();
            json.put("ACd",acd);
            json.put("typ",typ);

            Log.v("visitDet",json.toString());

            Call<ResponseBody> report=apiService.getVisitDet(json.toString());
            report.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("day_report", is.toString());

                            JSONArray jsonA=new JSONArray(is.toString());
                            for(int i=0;i<jsonA.length();i++){
                                JSONObject json=jsonA.getJSONObject(i);
                                Log.v("teswt_name",json.getString("name"));

                                    array.add(new ModelReportList(json.getString("name"), json.getString("Territory"), json.getString("WWith"),
                                            json.getString("dcr_dt"), json.getString("products"), json.getString("gifts"), json.getString("remarks"),
                                            json.getString("visitTime"), json.getString("ModTime")));


                            }
                            adpt.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch (Exception e){}
    }
    public void callApiMissed(){

        try{
            JSONObject json=new JSONObject();
            json.put("sfCode",sf_code);
            json.put("divisionCode",commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            json.put("report_date",dt);

            Call<ResponseBody> report=apiService.getMissedRptview(json.toString());
            report.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("misview_report", is.toString());
                            cluster_list.clear();
                            category_list.clear();
                            speciality_list.clear();
                            qualification_list.clear();
                            class_list.clear();
                            JSONArray jsonA=new JSONArray(is.toString());
                            for(int i=0;i<jsonA.length();i++){
                                JSONObject json=jsonA.getJSONObject(i);
                                mis_array.add(new ModelForReportList(json.getString("ListedDr_Name"),json.getString("Msdt"),json.getString("territory_Name"),
                                        json.getString("Doc_QuaName"),json.getString("Doc_Special_SName"),json.getString("ListedDrCode"),json.getString("Doc_Cat_SName"),
                                        json.getString("Doc_ClsSName")));
                                if(!clu.contains(json.getString("territory_Name"))) {
                                    cluster_list.add(new PopFeed(json.getString("territory_Name"), false, ""));
                                    clu+=","+json.getString("territory_Name");
                                }
                                if(!cat.contains(json.getString("Doc_Cat_SName"))) {
                                    category_list.add(new PopFeed(json.getString("Doc_Cat_SName"), false, ""));
                                    cat+=","+json.getString("Doc_Cat_SName");
                                }
                                if(!spec.contains(json.getString("Doc_Special_SName"))) {
                                    speciality_list.add(new PopFeed(json.getString("Doc_Special_SName"), false, ""));
                                    spec+=","+json.getString("Doc_Special_SName");
                                }
                                if(!qua.contains(json.getString("Doc_QuaName"))) {
                                    qualification_list.add(new PopFeed(json.getString("Doc_QuaName"), false, ""));
                                    qua+=","+json.getString("Doc_QuaName");
                                }
                                if(!cls.contains(json.getString("Doc_ClsSName"))) {
                                    class_list.add(new PopFeed(json.getString("Doc_ClsSName"), false, ""));
                                    cls+=","+json.getString("Doc_ClsSName");
                                }
                            }
                            missed_adpt.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch (Exception e){}
    }

    public void CustomShowDialog(final int x){
        final Dialog dialog=new Dialog(ReportListActivity.this);
        dialog.setContentView(R.layout.popup_missed_selection);
        dialog.show();

        final ListView list_selection=(ListView)dialog.findViewById(R.id.list_selection);

        Button btn_done=(Button)dialog.findViewById(R.id.btn_done);
        Button dr_close=(Button)dialog.findViewById(R.id.dr_close);
        dr_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                commonFun();
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(x==1){
                if (cluster_list.contains(new PopFeed(true))) {
                    String ss = "";
                    for (int i = 0; i < cluster_list.size(); i++) {
                        PopFeed pp = cluster_list.get(i);
                        if (pp.isClick()) {
                            ss = ss + cluster_list.get(i).getTxt() + ",";
                        }

                    }
                    txt_clu.setText(ss);
                }
                else
                    txt_clu.setText("Select Cluster");

                }
                else if(x==2){
                    if (category_list.contains(new PopFeed(true))) {
                        String ss = "";
                        for (int i = 0; i < category_list.size(); i++) {
                            PopFeed pp = category_list.get(i);
                            if (pp.isClick()) {
                                ss = ss + category_list.get(i).getTxt() + ",";
                            }

                        }
                        txt_cat.setText(ss);
                    }
                    else
                        txt_cat.setText("Select Category");

                }
                else if(x==3){
                    if (speciality_list.contains(new PopFeed(true))) {
                        String ss = "";
                        for (int i = 0; i < speciality_list.size(); i++) {
                            PopFeed pp = speciality_list.get(i);
                            if (pp.isClick()) {
                                ss = ss + speciality_list.get(i).getTxt() + ",";
                            }

                        }
                        txt_spec.setText(ss);
                    }
                    else
                        txt_spec.setText("Select Speciality");

                }
                else if(x==4){
                    if (qualification_list.contains(new PopFeed(true))) {
                        String ss = "";
                        for (int i = 0; i < qualification_list.size(); i++) {
                            PopFeed pp = qualification_list.get(i);
                            if (pp.isClick()) {
                                ss = ss + qualification_list.get(i).getTxt() + ",";
                            }

                        }
                        txt_qua.setText(ss);
                    }
                    else
                        txt_qua.setText("Select Qualification");

                }
                else{
                    if (class_list.contains(new PopFeed(true))) {
                        String ss = "";
                        for (int i = 0; i < class_list.size(); i++) {
                            PopFeed pp = class_list.get(i);
                            if (pp.isClick()) {
                                ss = ss + class_list.get(i).getTxt() + ",";
                            }

                        }
                        txt_cls.setText(ss);
                    }
                    else
                        txt_cls.setText("Select Class");

                }
                dialog.dismiss();
                commonFun();
            }
        });
        AdapterForSelectionList adpt;
        switch (x){
            case 1:
                 adpt = new AdapterForSelectionList(ReportListActivity.this, cluster_list, "a");
                list_selection.setAdapter(adpt);
                adpt.notifyDataSetChanged();
                break;
            case 2:
                 adpt = new AdapterForSelectionList(ReportListActivity.this, category_list, "a");
                list_selection.setAdapter(adpt);
                adpt.notifyDataSetChanged();
                break;

                case 3:
                 adpt = new AdapterForSelectionList(ReportListActivity.this, speciality_list, "a");
                list_selection.setAdapter(adpt);
                adpt.notifyDataSetChanged();
                break;

            case 4:
                adpt = new AdapterForSelectionList(ReportListActivity.this, qualification_list, "a");
                list_selection.setAdapter(adpt);
                adpt.notifyDataSetChanged();
                break;

            case 5:
                adpt = new AdapterForSelectionList(ReportListActivity.this, class_list, "a");
                list_selection.setAdapter(adpt);
                adpt.notifyDataSetChanged();
                break;

        }


    }

    public ArrayList<ModelForReportList> filterThosField(ArrayList<ModelForReportList> original,ArrayList<PopFeed> sub_list,int x){
        ArrayList<ModelForReportList> arr=new ArrayList<>();
        if (sub_list.contains(new PopFeed(true))) {
            for (int j = 0; j < sub_list.size(); j++) {
                PopFeed pp = sub_list.get(j);
                if (pp.isClick()) {
                    for (int i = 0; i < original.size(); i++) {
                        String values;
                        if(x==1)
                         values=original.get(i).getCluster();
                        else if(x==2)
                            values=original.get(i).getCategory();
                        else if(x==3)
                            values=original.get(i).getSpec();
                        else if(x==4)
                            values=original.get(i).getQualify();
                        else
                            values=original.get(i).getCls();

                        if (values.equalsIgnoreCase(pp.getTxt()))
                            arr.add(new ModelForReportList(original.get(i).getName(), original.get(i).getDate(), original.get(i).getCluster(),
                                    original.get(i).getQualify(), original.get(i).getSpec(), original.get(i).getPrev(), original.get(i).getCategory(),
                                    original.get(i).getCls()));
                    }

                }
            }
            return arr;
        }
        else
            return original;

    }

    public void clearTheArraylist(ArrayList<PopFeed> sub_list){
        if (sub_list.contains(new PopFeed(true))) {
            for (int j = 0; j < sub_list.size(); j++) {

                PopFeed mm=sub_list.get(j);
                mm.setClick(false);
            }
        }
    }
}
