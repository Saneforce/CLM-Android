package saneforce.sanclm.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Pojo_Class.MontlyVistDetail;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.UpdateUi;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;

public class CallFragment extends Fragment {
    PieChart pie;
    String db_connPath,SF_Code;
    CommonSharedPreference mCommonSharedPreference;
    CommonUtilsMethods mCommonUtilsMethods;
    TextView pbar_percentage;
    ProgressBar pBar;
    int pBarCount=1;
    ProgressBar pb_jan,pb_feb,pb_mar,pb_apr,pb_may,pb_jun,pb_jul,pb_aug,pb_sep,pb_oct,pb_nov,pb_dec;
    TextView txt_count_jan,txt_count_feb,txt_count_mar,txt_count_apr,txt_count_may,txt_count_jun,txt_count_jul,txt_count_aug,txt_count_sep,
            txt_count_oct,txt_count_nov,txt_count_dec,
            jan_txt,feb_txt,mar_txt,apr_txt,may_txt,jun_txt,jul_txt,aug_txt,sep_txt,oct_txt,nov_txt,dec_txt;
    DataBaseHandler dbh;
    static UpdateUi updateUi;
    ImageButton call_visit_detailsReload;
    String language;
    Context context;
    Resources resources;
    TextView tv_monthly_summary_head,tv_call_visit_details,categorycap,overallcap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vv=inflater.inflate(R.layout.home_call_graph, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedPreferences.getString(language_string, "");
        if (!language.equals("")){
            Log.d("homelang",language);
//            selected(language);
            context = LocaleHelper.setLocale(getActivity(), language);
            resources = context.getResources();
        }else {
            context = LocaleHelper.setLocale(getActivity(), "en");
            resources = context.getResources();
        }

        pie=(PieChart)vv.findViewById(R.id.pie);

        tv_monthly_summary_head=vv.findViewById(R.id.tv_monthly_summary_head);
        tv_call_visit_details=vv.findViewById(R.id.tv_call_visit_details);
        categorycap=vv.findViewById(R.id.categorycap);
        overallcap=vv.findViewById(R.id.overallcap);

        tv_monthly_summary_head.setText(resources.getString(R.string.monthly_average));
        tv_call_visit_details.setText(resources.getString(R.string.current_visit));
        categorycap.setText(resources.getString(R.string.category));
        overallcap.setText(resources.getString(R.string.overall));

        call_visit_detailsReload=(ImageButton) vv.findViewById(R.id.call_visit_detailsReload);

        HomeDashBoard.updateCallUI(new UpdateUi() {
            @Override
            public void updatingui() {
                Log.v("call_visit_fragment","are_called_her");
                catVisitDetail();
              //  call_visit_detailsReload.setImageResource(R.mipmap.sync);

            }
        });
        
        mCommonSharedPreference=new CommonSharedPreference(getActivity());
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        mCommonUtilsMethods=new CommonUtilsMethods(getActivity());
        dbh=new DataBaseHandler(getActivity());
        pbar_percentage  = (TextView) vv.findViewById(R.id.pbar_percentage);
        pBar=(ProgressBar)vv.findViewById(R.id.pBar);
        txt_count_jan = (TextView) vv.findViewById(R.id.txt_count_jan);
        txt_count_feb = (TextView) vv.findViewById(R.id.txt_count_feb);
        txt_count_mar = (TextView) vv.findViewById(R.id.txt_count_mar);
        txt_count_apr = (TextView) vv.findViewById(R.id.txt_count_apr);
        txt_count_may = (TextView) vv.findViewById(R.id.txt_count_may);
        txt_count_jun = (TextView) vv.findViewById(R.id.txt_count_jun);
        txt_count_jul = (TextView) vv.findViewById(R.id.txt_count_jul);
        txt_count_aug = (TextView) vv.findViewById(R.id.txt_count_aug);
        txt_count_sep = (TextView) vv.findViewById(R.id.txt_count_sep);
        txt_count_oct = (TextView) vv.findViewById(R.id.txt_count_oct);
        txt_count_nov = (TextView) vv.findViewById(R.id.txt_count_nov);
        txt_count_dec = (TextView) vv.findViewById(R.id.txt_count_dec);

        jan_txt = (TextView) vv.findViewById(R.id.jan_txt);
        feb_txt = (TextView) vv.findViewById(R.id.feb_txt);
        mar_txt = (TextView) vv.findViewById(R.id.mar_txt);
        apr_txt = (TextView) vv.findViewById(R.id.apr_txt);
        may_txt = (TextView) vv.findViewById(R.id.may_txt);
        jun_txt = (TextView) vv.findViewById(R.id.jun_txt);
        jul_txt = (TextView) vv.findViewById(R.id.jul_txt);
        oct_txt = (TextView) vv.findViewById(R.id.oct_txt);
        sep_txt = (TextView) vv.findViewById(R.id.sep_txt);
        aug_txt = (TextView) vv.findViewById(R.id.aug_txt);
        nov_txt = (TextView) vv.findViewById(R.id.nov_txt);
        dec_txt = (TextView) vv.findViewById(R.id.dec_txt);

        pb_jan=(ProgressBar)vv.findViewById(R.id.pb_jan);
        pb_feb=(ProgressBar)vv.findViewById(R.id.pb_feb);
        pb_mar=(ProgressBar)vv.findViewById(R.id.pb_mar);
        pb_apr=(ProgressBar)vv.findViewById(R.id.pb_apr);
        pb_may=(ProgressBar)vv.findViewById(R.id.pb_may);
        pb_jun=(ProgressBar)vv.findViewById(R.id.pb_jun);
        pb_jul=(ProgressBar)vv.findViewById(R.id.pb_jul);
        pb_aug=(ProgressBar)vv.findViewById(R.id.pb_aug);
        pb_sep=(ProgressBar)vv.findViewById(R.id.pb_sep);
        pb_oct=(ProgressBar)vv.findViewById(R.id.pb_oct);
        pb_nov=(ProgressBar)vv.findViewById(R.id.pb_nov);
        pb_dec=(ProgressBar)vv.findViewById(R.id.pb_dec);

//        call_visit_detailsReload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                call_visit_detailsReload.setImageResource(R.drawable.back_close);
//                catVisitDetail();
//            }
//        });
        
        return vv;
    }
    
   

    public void catVisitDetail(){
        HashMap<String,String> xx=new HashMap<>();
        xx.put("SF", SF_Code);
        //xx.put("data","{\"SF\":\""+SF_Code+"\"}");
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("SF", SF_Code);
        }catch (Exception e){

        }
        Log.v("cat_visit_detail", String.valueOf(paramObject));
        Log.v("URL", db_connPath);               
        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        Call<ResponseBody> visitDetail = apiService.gettCatVst(paramObject.toString());
        visitDetail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    String jsonData = null;

                    InputStreamReader ip=null;
                    StringBuilder is=new StringBuilder();
                    String line=null;
                    try{
                        ip=new InputStreamReader(response.body().byteStream());
                        BufferedReader bf=new BufferedReader(ip);

                        while((line=bf.readLine())!=null){
                            is.append(line);
                        }
                        //jsonObject=new JSONObject(is.toString());
                        ArrayList<Entry> entries1 = new ArrayList<>();
                        ArrayList<String> labels1 = new ArrayList<>();
                        ArrayList<PieEntry> entries = new ArrayList<>();
                        float totalSum=100;
                        float sumVal = 0;
                        JSONArray jas=new JSONArray(is.toString());
                        Log.v("json_object_printing", String.valueOf(jas)+" leng"+jas.length());
                        for(int i=0;i<jas.length();i++){
                            JSONObject js=jas.getJSONObject(i);
                            Log.v("categoruNAme",js.getString("cnt"));

                            sumVal+= Float.parseFloat(js.getString("cnt"));

                          /*  entries1.add(new Entry(Float.parseFloat(js.getString("cnt")), i));
                            entries1.add(new Entry(Float.parseFloat(js.getString("cnt")), i));
                            labels1.add(js.getString("Category"));

                           */
                        }

                        for(int i=0;i<jas.length();i++){
                            JSONObject js=jas.getJSONObject(i);
                            float forOne=totalSum/sumVal;
                            float kk=forOne*Float.parseFloat(js.getString("cnt"));
                            int jj= Math.round(kk);
                            /*entries1.add(new Entry(jj, i));
                            labels1.add(js.getString("Category"));*/
/*
                            entries.add(new PieEntry(jj,"A",
                                    i));
*/
                            entries.add(new PieEntry(jj,js.getString("Category"),
                                    i));
                            /*pieChart.getLegend().setEnabled(false);
                            pieChart.setDescription("");
                            pieChart.animateX(1000);
                            PieDataSet dataset1 = new PieDataSet(entries1,"ofcalls");
                            PieData data1 = new PieData(labels1, dataset1);
                            // initialize Piedata<br />
                            data1.setValueFormatter(new PercentFormatter());
                            pieChart.setData(data1);
                            dataset1.setColors(ColorTemplate.COLORFUL_COLORS);
                            pieChart.notifyDataSetChanged();
                            pieChart.invalidate();*/
                        }
                        if(jas.length()==0){
                            entries1.add(new Entry(0, 0));
                            labels1.add(" ");
                            entries.add(new PieEntry(0," ",
                                    0));
                           /* pieChart.getLegend().setEnabled(false);
                            pieChart.setDescription("");
                            pieChart.animateX(1000);
                            PieDataSet dataset1 = new PieDataSet(entries1,"ofcalls");
                            PieData data1 = new PieData(labels1, dataset1);
                            // initialize Piedata<br />
                            data1.setValueFormatter(new PercentFormatter());
                            pieChart.setData(data1);
                            dataset1.setColors(ColorTemplate.COLORFUL_COLORS);*/
                        }
                        Log.v("printing_entries_val",entries.size()+" checking");
                        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
                        Log.v("printing_entries_val12",entries.size()+" checking");
                        dataSet.setDrawIcons(false);

                        dataSet.setSliceSpace(3f);
                        dataSet.setIconsOffset(new MPPointF(0, 40));
                        dataSet.setSelectionShift(5f);
                        Log.v("printing_entries_val",entries.size()+" checking");
                        // add a lot of colors

                        ArrayList<Integer> colors = new ArrayList<>();

                        for (int c : ColorTemplate.COLORFUL_COLORS)
                            colors.add(c);

                        for (int c : ColorTemplate.COLORFUL_COLORS)
                            colors.add(c);

                        for (int c : ColorTemplate.COLORFUL_COLORS)
                            colors.add(c);

                        for (int c : ColorTemplate.LIBERTY_COLORS)
                            colors.add(c);

                        for (int c : ColorTemplate.PASTEL_COLORS)
                            colors.add(c);

                        colors.add(ColorTemplate.getHoloBlue());

                        dataSet.setColors(colors);
                        //dataSet.setSelectionShift(0f);
                        Log.v("printing_entries_val12",entries.size()+" checking");
                        pie.getDescription().setEnabled(false);
                        pie.getLegend().setEnabled(false);
                        pie.getDescription().setEnabled(false);
                        pie.getLegend().setEnabled(false);
                        PieData data = new PieData(dataSet);
                        // data.setValueFormatter(new PercentFormatter(pieChart));
                        data.setValueTextSize(11f);
                        data.setValueTextColor(Color.WHITE);

                        pie.setData(data);
                        pie.animateXY(1400, 1400);
                        Log.v("printing_entries_val1",entries.size()+" checking");
                        // undo all highlights
                        pie.highlightValues(null);
                        pie.notifyDataSetChanged();
                        pie.invalidate();
                        Log.v("printing_entries_val1",entries.size()+" checking");
                       /* if (jsonObject.getString("success").equals("true")) {
                            mCommonSharedPreference.setValueToPreference("sub_sf_code",mydayhqCd);
                            Toast.makeText(getApplicationContext(),"TodayCAlls"+ jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                            mCommonSharedPreference.setValueToPreference("workType","true");
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"TodayCAlls22"+jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();

                        }*/
               /* try {
                    jsonData = response.body().string();
                    jsonObject = new JSONObject(jsonData);
                    Log.v("json_object_printing", String.valueOf(jsonObject));

                    if (jsonObject.getString("success").equals("true")) {
                        Toast.makeText(getApplicationContext(),"TodayCAlls"+ jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                        mCommonSharedPreference.setValueToPreference("workType","true");
                        dialog.dismiss();
                    }else{
                        Toast.makeText(getApplicationContext(),"TodayCAlls22"+jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();

                    }*/
                        //  Log.e("Tday ","dp "+todaytp.get(0).getPlNm());
                    /*tv_worktype.setText("Work Type : "+todaytp.get(0).getWTNm());
                    tv_cluster.setText("Cluster : "+todaytp.get(0).getPlNm());

                    editor.putString(CommonUtils.TAG_WORKTYPE_NAME, todaytp.get(0).getWTNm());
                    editor.putString(CommonUtils.TAG_WORKTYPE_CODE, todaytp.get(0).getWT());
                    editor.putString(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE,todaytp.get(0).getPl());
                    editor.putString(CommonUtils.TAG_SF_CODE, todaytp.get(0).getSFCode());
                    editor.commit();*/

                        // dbh.close();
                        setOverAllVisit();
                    }catch (Exception e) {
                    }
                }
                else
                    setOverAllVisit();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setOverAllVisit();
            }
        });
    }


    public void setOverAllVisit(){
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("SF", SF_Code);
        }catch (Exception e){

        }

        Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        Call<ResponseBody> visitDetail = apiService.getOverallVisit(paramObject.toString());
        visitDetail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    String jsonData = null;

                    InputStreamReader ip=null;
                    StringBuilder is=new StringBuilder();
                    String line=null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }

                        JSONArray jsonObject1=new JSONArray(is.toString());
                        Log.v("overall_visit", String.valueOf(jsonObject1));
                        for(int i=0;i<jsonObject1.length();i++){
                            JSONObject js=jsonObject1.getJSONObject(i);
                            Log.v("total_cnt_js",js.getString("totcnt"));
                            pBar.setMax(Integer.parseInt(js.getString("totcnt")));
                            pbar_percentage.setText(js.getString("cnt")+"%");
                            progressBarAnimation(Integer.parseInt(js.getString("cnt")));
                        }
                        monthlyVisitDetail();
                    }catch (Exception e){
                    }
                }
                else
                    monthlyVisitDetail();
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                monthlyVisitDetail();
            }
        });
    }

    public void progressBarAnimation(final int max){

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(pBarCount<max) {
                    ++pBarCount;
                    try {
                        Thread.sleep(30);
                        pBar.setProgress(pBarCount);
                    } catch (Exception e) {

                    }
                }
            }
        }).start();
    }


    public void monthlyVisitDetail(){
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("SF", SF_Code);

            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Call<List<MontlyVistDetail>> visitDetail = apiService.getMnthDetail(paramObject.toString());
            visitDetail.enqueue(new Callback<List<MontlyVistDetail>>() {
                @Override
                public void onResponse(Call<List<MontlyVistDetail>> call, Response<List<MontlyVistDetail>> response) {

                    if(response.isSuccessful()){
                        List<MontlyVistDetail> details=response.body();
                        Log.v("details_length", String.valueOf(details.size()));
                        int maxVal=0;
//                        for(int i=0;i<details.size();i++) {
//                            if(details.get(i).getCnt().equals("") || details.get(i).getCnt().equals(null))
//                                details.get(i).setCnt("0");
//                        }

                        for(int i=0;i<details.size();i++){
                            if(maxVal>=Integer.parseInt(details.get(i).getCnt())){

                            }
                            else
                                maxVal=Integer.parseInt(details.get(i).getCnt());
                        }

                        for(int i=0;i<details.size();i++){
                            switch (i){
                                case 0:
                                    pb_jan.setMax(maxVal);
                                    pb_jan.setProgress(Integer.parseInt(details.get(i).getCnt()));
                                    txt_count_jan.setText(details.get(i).getCnt());
                                    jan_txt.setText(details.get(i).getMon()+"-"+details.get(i).getYr());
                                    break;
                                case 1:
                                    pb_feb.setMax(maxVal);
                                    pb_feb.setProgress(Integer.parseInt(details.get(i).getCnt()));
                                    txt_count_feb.setText(details.get(i).getCnt());
                                    feb_txt.setText(details.get(i).getMon()+"-"+details.get(i).getYr());
                                    break;
                                case 2:
                                    pb_mar.setMax(maxVal);
                                    pb_mar.setProgress(Integer.parseInt(details.get(i).getCnt()));
                                    txt_count_mar.setText(details.get(i).getCnt());
                                    mar_txt.setText(details.get(i).getMon()+"-"+details.get(i).getYr());
                                    break;
                                case 3:
                                    pb_apr.setMax(maxVal);
                                    pb_apr.setProgress(Integer.parseInt(details.get(i).getCnt()));
                                    txt_count_apr.setText(details.get(i).getCnt());
                                    apr_txt.setText(details.get(i).getMon()+"-"+details.get(i).getYr());
                                    break;
                                case 4:
                                    pb_may.setMax(maxVal);
                                    pb_may.setProgress(Integer.parseInt(details.get(i).getCnt()));
                                    txt_count_may.setText(details.get(i).getCnt());
                                    may_txt.setText(details.get(i).getMon()+"-"+details.get(i).getYr());
                                    break;
                                case 5:
                                    pb_jun.setMax(maxVal);
                                    pb_jun.setProgress(Integer.parseInt(details.get(i).getCnt()));
                                    txt_count_jun.setText(details.get(i).getCnt());
                                    jun_txt.setText(details.get(i).getMon()+"-"+details.get(i).getYr());
                                    break;
                                case 6:
                                    pb_jul.setMax(maxVal);
                                    pb_jul.setProgress(Integer.parseInt(details.get(i).getCnt()));
                                    txt_count_jul.setText(details.get(i).getCnt());
                                    jul_txt.setText(details.get(i).getMon()+"-"+details.get(i).getYr());
                                    break;
                                case 7:
                                    pb_aug.setMax(maxVal);
                                    pb_aug.setProgress(Integer.parseInt(details.get(i).getCnt()));
                                    txt_count_aug.setText(details.get(i).getCnt());
                                    aug_txt.setText(details.get(i).getMon()+"-"+details.get(i).getYr());
                                    break;
                                case 8:
                                    pb_sep.setMax(maxVal);
                                    pb_sep.setProgress(Integer.parseInt(details.get(i).getCnt()));
                                    txt_count_sep.setText(details.get(i).getCnt());
                                    sep_txt.setText(details.get(i).getMon()+"-"+details.get(i).getYr());
                                    break;
                                case 9:
                                    pb_oct.setMax(maxVal);
                                    pb_oct.setProgress(Integer.parseInt(details.get(i).getCnt()));
                                    txt_count_oct.setText(details.get(i).getCnt());
                                    oct_txt.setText(details.get(i).getMon()+"-"+details.get(i).getYr());
                                    break;
                                case 10:
                                    pb_nov.setMax(maxVal);
                                    pb_nov.setProgress(Integer.parseInt(details.get(i).getCnt()));
                                    txt_count_nov.setText(details.get(i).getCnt());
                                    nov_txt.setText(details.get(i).getMon()+"-"+details.get(i).getYr());
                                    break;
                                case 11:
                                    pb_dec.setMax(maxVal);
                                    pb_dec.setProgress(Integer.parseInt(details.get(i).getCnt()));
                                    txt_count_dec.setText(details.get(i).getCnt());
                                    dec_txt.setText(details.get(i).getMon()+"-"+details.get(i).getYr());
                                    break;
                            }
                        }
                        getCustomSupport();
                    }
                    else
                        getCustomSupport();

                }
                @Override
                public void onFailure(Call<List<MontlyVistDetail>> call, Throwable t) {
                    getCustomSupport();
                }
            });
        }catch (Exception e){

        }

    }

    public void storeReports(){
        String yr=mCommonUtilsMethods.getCurrentInstance();
        yr="%"+yr.substring(0,4)+"%";
        Log.v("printingyer",yr);
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("SF", SF_Code);
            paramObject.put("yr", yr);

            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Call<ResponseBody> reports = apiService.wholeReport(paramObject.toString());
            reports.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        String jsonData = null;

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("json_object_report", is.toString());
                            JSONArray js=new JSONArray(is.toString());
                            for(int i=0;i<js.length();i++){
                                JSONObject jo=js.getJSONObject(i);
                                Log.v("printing_satt",jo.getString("sf_code")+"docr_code "+jo.getString("Trans_Detail_Info_Code")+" time "+jo.getString("Time")+" name "+jo.getString("Trans_Detail_Name")
                                +"activity_Date"+jo.getJSONObject("Activity_Date").getString("date")+" year "+jo.getJSONObject("Activity_Date").getString("date").substring(0,4)+" mnth "+jo.getJSONObject("Activity_Date").getString("date").substring(5,7));
                                String date=jo.getJSONObject("Activity_Date").getString("date");
                                dbh.open();
                                dbh.insert_store_report(jo.getString("sf_code"),date,jo.getString("Trans_Detail_Info_Code"),jo.getString("Trans_Detail_Name"),jo.getString("Time"),date.substring(0,4),date.substring(5,7));
                            }
                            dbh.close();
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
    public void getCustomSupport(){

        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("div",mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));

            Log.v("json_object_custom", paramObject.toString());
            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Call<ResponseBody> reports = apiService.gettingCustomSetup(paramObject.toString());
            reports.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        String jsonData = null;

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("json_object_custom", is.toString());
                            JSONArray   ja=new JSONArray(is.toString());
                            JSONObject  js=ja.getJSONObject(0);
                            if(js.has("addDr")){
                                mCommonSharedPreference.setValueToPreference("addDr",js.getString("addDr"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("addDr","1");
                            if(js.has("showDelete")){
                                mCommonSharedPreference.setValueToPreference("showDelete",js.getString("showDelete"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("showDelete","1");
                            if(js.has("Detailing_chem")){
                                mCommonSharedPreference.setValueToPreference("Detailing_chem",js.getString("Detailing_chem"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("Detailing_chem","1");
                            if(js.has("Detailing_stk")){
                                mCommonSharedPreference.setValueToPreference("Detailing_stk",js.getString("Detailing_stk"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("Detailing_stk","1");
                            if(js.has("Detailing_undr")){
                                mCommonSharedPreference.setValueToPreference("Detailing_undr",js.getString("Detailing_undr"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("Detailing_undr","1");
                            if(js.has("addChm")){
                                mCommonSharedPreference.setValueToPreference("addChm",js.getString("addChm"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("addChm","1");
                            if(js.has("addAct")) {
                                mCommonSharedPreference.setValueToPreference("addAct", js.getString("addAct"));
                                //updateUi.updatingui();
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("addAct","1");
                            if(js.has("hosp_filter"))
                            mCommonSharedPreference.setValueToPreference("hosp_filter",js.getString("hosp_filter"));

                            if(js.has("pobMax")){
                                mCommonSharedPreference.setValueToPreference("pobMax",js.getString("pobMax"));
                                Log.e("PROBMax",js.getString("pobMax"));

                                //mCommonSharedPreference.setValueToPreference("sampleMax",js.getString("sampleMax"));
                            }
                            else {
                                mCommonSharedPreference.setValueToPreference("pobMax", "");
                                mCommonSharedPreference.setValueToPreference("sampleMax", "");
                            }
                            if(js.has("productFB")){
                                mCommonSharedPreference.setValueToPreference("productFB", js.getString("productFB"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("productFB", "");
                            if(js.has("theraptic")){
                                mCommonSharedPreference.setValueToPreference("theraptic", js.getString("theraptic"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("theraptic", "");

                            if(js.has("Target_sales")){
                                mCommonSharedPreference.setValueToPreference("Target_sales", js.getString("Target_sales"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("Target_sales", "");

                            if(js.has("DrProfile")){
                                mCommonSharedPreference.setValueToPreference("DrProfile", js.getString("DrProfile"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("DrProfile", "");

                            if(js.has("Product_Stockist")){
                                mCommonSharedPreference.setValueToPreference("Product_Stockist", js.getString("Product_Stockist"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("Product_Stockist", "");

                            if(js.has("undr_hs_nd")){
                                mCommonSharedPreference.setValueToPreference("undr_hs_nd", js.getString("undr_hs_nd"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("undr_hs_nd", "");

                            if(js.has("PresentNd")){
                                mCommonSharedPreference.setValueToPreference("PresentNd", js.getString("PresentNd"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("PresentNd", "");

                            if(js.has("CustNd")){
                                mCommonSharedPreference.setValueToPreference("CustNd", js.getString("CustNd"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("CustNd", "");

                            if(js.has("yetrdy_call_del_Nd")){
                                mCommonSharedPreference.setValueToPreference("yetrdy_call_del_Nd", js.getString("yetrdy_call_del_Nd"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("yetrdy_call_del_Nd", "");

                            if(js.has("DcrapprvNd")){
                                mCommonSharedPreference.setValueToPreference("DcrapprvNd", js.getString("DcrapprvNd"));
                            }
                            else
                                mCommonSharedPreference.setValueToPreference("DcrapprvNd", "");

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

    public static void bindUpdateViewList(UpdateUi uu){
        updateUi=uu;
    }

}
