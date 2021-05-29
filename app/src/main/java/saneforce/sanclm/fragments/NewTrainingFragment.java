package saneforce.sanclm.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Pojo_Class.DetailingList;
import saneforce.sanclm.Pojo_Class.DoctorcoverageList;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.Model.Brandexpolist;
import saneforce.sanclm.adapter_class.DetailingAdapter;
import saneforce.sanclm.adapter_class.DoctorcoverageAdapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.sqlite.DataBaseHandler;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;

//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

public class NewTrainingFragment extends Fragment implements OnChartValueSelectedListener {
    private BarChart barChart;
    PieChartView pieChartView;
    String language;
    Context context;
    Resources resources;
    TextView mtd_call,mtd_reload,detailing,time_spent,brand_exposure,total_dr,total_pharma,totaldr,detail_reload;

   Api_Interface apiInterface;
    JSONObject obj = new JSONObject();
    String SF_Code = "", db_connPath;
    CommonSharedPreference mCommonSharedPreference;
    ArrayList<DoctorcoverageList>arrayList;
    RecyclerView recyclerViewdoctor,recyclerViewpharm,recyclerViewtotal,detailingrecyclerview;
    DoctorcoverageAdapter adapter,adapter1,adapter2;
    ArrayList<DoctorcoverageList>pharmlists;
    ArrayList<DoctorcoverageList>totaldetlist;
    DataBaseHandler db;
    Cursor mCursor;
    TextView drcallstext,drcallcount,pharmcalltxt,pharmcallcount,totaldrtext,totaldrcount;
   ArrayList<SliceValue>pieData;
   ArrayList<DetailingList>detailingLists;
   DetailingAdapter detailingAdapter;
    List<BarEntry> entries;
    ArrayList xVals ;
    ArrayList<String> colorlist;
    ArrayList<Brandexpolist>branlist;
    TextView reload1,reload2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vv=inflater.inflate(R.layout.new_training_graph,container,false);
        mCommonSharedPreference = new CommonSharedPreference(getActivity());
        SF_Code = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        Log.v("sfcode",SF_Code);
        db = new DataBaseHandler(getActivity());

       apiInterface= RetroClient.getClient(db_connPath).create(Api_Interface.class);

        try {
            obj.put("SF", SF_Code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        arrayList=new ArrayList<>();
        pharmlists=new ArrayList<>();
        totaldetlist=new ArrayList<>();
         pieData = new ArrayList<>();
         detailingLists=new ArrayList<>();
         branlist=new ArrayList<>();
         xVals=new ArrayList();
         entries=new ArrayList<>();

        recyclerViewdoctor=vv.findViewById(R.id.recycledoctorcoverage);
        recyclerViewdoctor.setHasFixedSize(true);
        recyclerViewdoctor.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewpharm=vv.findViewById(R.id.recyclepharmcoverage);
        recyclerViewpharm.setHasFixedSize(true);
        recyclerViewpharm.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewtotal=vv.findViewById(R.id.recycle3total);
        recyclerViewtotal.setHasFixedSize(true);
        recyclerViewtotal.setLayoutManager(new LinearLayoutManager(getActivity()));
        detailingrecyclerview=vv.findViewById(R.id.detailingrecycle);
        detailingrecyclerview.setHasFixedSize(true);
        detailingrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        drcallstext=vv.findViewById(R.id.drcallstotal);
        drcallcount=vv.findViewById(R.id.drtotal);
        pharmcalltxt=vv.findViewById(R.id.pharmacycalls);
        pharmcallcount=vv.findViewById(R.id.totalpharcalls);
        totaldrcount=vv.findViewById(R.id.totaldrcount);
        totaldrtext=vv.findViewById(R.id.totaldrtext);
        barChart= vv.findViewById(R.id.bar_chart);
        barChart.setOnChartValueSelectedListener(this);
        //UpperGraph();
        pieChartView = vv.findViewById(R.id.chart);
        reload1=vv.findViewById(R.id.reload);
        reload2=vv.findViewById(R.id.reload1);

         if(arrayList.size()==0){
             copList();

         }else {
         }
        if(pharmlists.size()==0){
            pharmList();

        }else {
        }
        if(totaldetlist.size()==0){
            totaldaylist();

        }else {
        }

        if(detailingLists.size()==0){
           detailingtmspent();

        }else {
        }
        if(branlist.size()==0){
            brandexposure();

        }else {
        }

        totalcalls();

        //sqlite
        getdrcoverage();
        getpharmcoverage();
        gettotalcoverage();
        gettotalcalls();
        getbrandexpo();
        getdetailing();

        reload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open();
                db.del_drcoverage();
                db.del_pharmcoverage();
                db.del_totalcoverage();
                db.del_totalcalls();
                arrayList.clear();
                pharmlists.clear();
                totaldetlist.clear();
                copList();
                pharmList();
                totaldaylist();
                totalcalls();
                getdrcoverage();
                getpharmcoverage();
                gettotalcoverage();
                gettotalcalls();
                db.close();

                Intent intent=new Intent(getActivity(), HomeDashBoard.class);
                startActivity(intent);


            }
        });

        reload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.open();
                db.del_brandexpose();
                db.del_detailing();
                branlist.clear();
                detailingLists.clear();
                detailingtmspent();
                brandexposure();
                getbrandexpo();
                getdetailing();
                db.close();
                Intent intent=new Intent(getActivity(), HomeDashBoard.class);
                startActivity(intent);
            }
        });


        int[] colors = new int[ branlist.size()];
        for(int i=0;i<branlist.size();i++){

            xVals.add(branlist.get(i).getBrand());
            entries.add(new BarEntry(i,Float.parseFloat(branlist.get(i).getTcount())));
          String col=branlist.get(i).getBarclr();
            colors[i]=Color.parseColor(col);

        }

        BarDataSet set = new BarDataSet(entries, "Visit Data");
        set.setColors(colors);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f);// set custom bar width
        barChart.setData(data);
        barChart.setDescription(null);
        barChart.getLegend().setEnabled(false);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        XAxis xAxis = barChart.getXAxis();

        /*xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);*/

        //xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        //xAxis.setAxisMinimum(0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(xVals.size());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        xAxis.setTextSize(5f);
        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);
        YAxis rightYAxis1 = barChart.getAxisLeft();
        rightYAxis1.setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();


//        pieData.add(new SliceValue(15, Color.parseColor("#FA1628")));
//        pieData.add(new SliceValue(25, Color.parseColor("#1CAD53")));
//        pieData.add(new SliceValue(10, Color.parseColor("#F8C131")));
//        pieData.add(new SliceValue(30, Color.parseColor("#A8E055")));
//        pieData.add(new SliceValue(20, Color.parseColor("#28ABE3")));
//        pieData.add(new SliceValue(35, Color.parseColor("#BC0811")));






        return vv;
    }

    private void brandexposure() {
        Call<ResponseBody> chm6 = apiInterface.getBrndexpo(String.valueOf(obj));
        chm6.enqueue(brndexpose);
    }

    private void detailingtmspent() {
        Call<ResponseBody> chm5 = apiInterface.getDetailingtmspent(String.valueOf(obj));
        chm5.enqueue(dettmspent);
    }

    private void totaldaylist() {
        Call<ResponseBody> chm4 = apiInterface.getTotaldetails(String.valueOf(obj));
        chm4.enqueue(totdetails);
    }

    private void totalcalls() {
        Call<ResponseBody> chm2 = apiInterface.getTotalcallsCoverage(String.valueOf(obj));
        chm2.enqueue(totallist);
    }

    private void pharmList() {
        Call<ResponseBody> chm1 = apiInterface.getPharmCoverage(String.valueOf(obj));
        chm1.enqueue(pharmlist);
    }

    public void copList() {
        Call<ResponseBody> chm = apiInterface.getDoctorCoverage(String.valueOf(obj));
        chm.enqueue(NewComplist);
    }

    public Callback<ResponseBody> NewComplist = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {

                try {
                    db.open();
                    db.del_drcoverage();
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;

                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    JSONArray ja = new JSONArray(is.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js1 = ja.getJSONObject(i);

//                        DoctorcoverageList doctorcoverageList=new DoctorcoverageList(js1.getString("Name"),js1.getString("vCount"),js1.getString("TCnt"));
//                        arrayList.add(doctorcoverageList);
                        db.insert_drcoverage(js1.getString("Name"),js1.getString("vCount"),js1.getString("TCnt"));


                    }

                    db.close();


                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
        }
    };

    public Callback<ResponseBody>pharmlist = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {

                try {
                    db.open();
                    db.del_pharmcoverage();
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;

                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    JSONArray ja = new JSONArray(is.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js1 = ja.getJSONObject(i);
//                        DoctorcoverageList doctorcoverageList=new DoctorcoverageList(js1.getString("Name"),js1.getString("vCount"),js1.getString("TCnt"));
//                        pharmlists.add(doctorcoverageList);
                        db.insert_pharmcoverage(js1.getString("Name"),js1.getString("vCount"),js1.getString("TCnt"));

                    }
                    db.close();


                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
        }
    };
    public Callback<ResponseBody> totallist = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {

                try {
                    db.open();
                    db.del_totalcalls();

                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;

                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    JSONArray ja = new JSONArray(is.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js1 = ja.getJSONObject(0);
//                        drcallstext.setText(js1.getString("Name"));
//                        drcallcount.setText(js1.getString("vCount")+"/"+js1.getString("TCnt"));
                        JSONObject js2 = ja.getJSONObject(1);
//                        pharmcalltxt.setText(js2.getString("Name"));
//                        pharmcallcount.setText(js2.getString("vCount")+"/"+js2.getString("TCnt"));
                        JSONObject js3 = ja.getJSONObject(2);
//                        totaldrtext.setText(js3.getString("Name"));
//                        totaldrcount.setText(js3.getString("vCount")+"/"+js3.getString("TCnt"));
//
                         db.insertTotalcalls(js1.getString("Name"),js1.getString("vCount"),js1.getString("TCnt"),js2.getString("Name"),js2.getString("vCount"),js2.getString("TCnt"),
                                 js3.getString("Name"),js3.getString("vCount"),js3.getString("TCnt"));

                    }



                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
        }
    };

    public Callback<ResponseBody> totdetails = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {

                try {
                    db.open();
                    db.del_totalcoverage();
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;

                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    JSONArray ja = new JSONArray(is.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js1 = ja.getJSONObject(i);
//                        DoctorcoverageList doctorcoverageList=new DoctorcoverageList(js1.getString("Name"),js1.getString("vCount"),js1.getString("TCnt"));
//                       totaldetlist.add(doctorcoverageList);
                        db.insert_totalcoverage(js1.getString("Name"),js1.getString("vCount"),js1.getString("TCnt"));



                    }
                    db.close();




                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
        }
    };
    public Callback<ResponseBody> dettmspent = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {

                try {
                    db.open();
                    db.del_detailing();

                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;

                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    JSONArray ja = new JSONArray(is.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js1 = ja.getJSONObject(i);

//                        SliceValue sliceValue=new SliceValue(Float.parseFloat(js1.getString("Percnt")),Color.parseColor(js1.getString("barcolr")));
//                        pieData.add(sliceValue);

                        DetailingList list=new DetailingList(js1.getString("Brand"),js1.getString("Percnt"),js1.getString("lblClr"),js1.getString("barcolr"));
                         detailingLists.add(list);

                         db.insertdetailingtime(js1.getString("Brand"),js1.getString("Percnt"),js1.getString("barcolr"),js1.getString("lblClr"));


                    }



                   db.close();
                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
        }
    };

    public Callback<ResponseBody> brndexpose = new Callback<ResponseBody>() {
        @SuppressLint("Range")
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {
                try {
                    db.open();
                    db.del_brandexpose();

                    String col="";

                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;

                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    JSONArray ja = new JSONArray(is.toString());
                    int[] colors = new int[ ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js1 = ja.getJSONObject(i);

//                        xVals.add(js1.getString("Brand"));
//                        entries.add( new BarEntry(i, Float.parseFloat(js1.getString("TotCnt"))));
//                        col=js1.getString("barcolr");
//                        colors[i]=Color.parseColor(col);

                        db.insertBrandexpo(js1.getString("Brand"),js1.getString("TotCnt"),js1.getString("barcolr"), String.valueOf(i));


                    }



                    db.close();
                } catch (Exception e) {
                }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
        }
    };
    public void UpperGraph(){

//        List<BarEntry> entries = new ArrayList<>();
//        ArrayList xVals = new ArrayList<>();
//
//        xVals.add("Hemoforce");
//        xVals.add("Polygel");
//        xVals.add("Omeshal");
//        xVals.add("Shaltoux");
//        xVals.add("zanitin DUO");
//        xVals.add("Shalbactam-TZ");
//
//        entries.add(new BarEntry(0f, 170));
//        entries.add(new BarEntry(1f, 118));
//        entries.add(new BarEntry(2f, 56));
//        entries.add(new BarEntry(3f, 118));
//        entries.add(new BarEntry(4f, 56));
//        entries.add(new BarEntry(5f, 56));



    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("VAL_SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {

    }



    public void getdrcoverage(){
        arrayList.clear();
        db.open();
        mCursor=db.select_drcoverager();
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("product_name_feed", mCursor.getString(0));
                arrayList.add(new DoctorcoverageList(mCursor.getString(0), mCursor.getString(1),mCursor.getString(2)));

            } while (mCursor.moveToNext());
            adapter=new DoctorcoverageAdapter(getActivity(),arrayList);
            recyclerViewdoctor.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        mCursor.close();
        db.close();
    }

    public void getpharmcoverage(){
        pharmlists.clear();
        db.open();
        mCursor = db.select_pharmcoverager();
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("product_name_feed", mCursor.getString(0));
                pharmlists.add(new DoctorcoverageList(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2)));

            } while (mCursor.moveToNext());
            adapter1 = new DoctorcoverageAdapter(getActivity(), pharmlists);
            recyclerViewpharm.setAdapter(adapter1);
            adapter1.notifyDataSetChanged();
        }
        mCursor.close();
        db.close();
    }

    public void gettotalcoverage() {
        totaldetlist.clear();
        db.open();
        mCursor = db.select_totalcoverager();
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("product_name_feed", mCursor.getString(0));
                totaldetlist.add(new DoctorcoverageList(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2)));

            } while (mCursor.moveToNext());
            adapter2 = new DoctorcoverageAdapter(getActivity(), totaldetlist);
            recyclerViewtotal.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();
        }
        mCursor.close();
        db.close();
    }

    public void getdetailing() {
        detailingLists.clear();
        db.open();
        mCursor = db.select_Detailingtime();
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("product_name_feed", mCursor.getString(0));
                detailingLists.add(new DetailingList(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3)));
                SliceValue sliceValue = new SliceValue(Float.parseFloat(mCursor.getString(1)), Color.parseColor(mCursor.getString(3)));
                pieData.add(sliceValue);

            } while (mCursor.moveToNext());
            PieChartData pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(false);
            pieChartData.setHasCenterCircle(true).setCenterText1("Detailing Time Spent").setCenterText1FontSize(10).setCenterText1Color(Color.parseColor("#000000"));
            pieChartView.setPieChartData(pieChartData);

            detailingAdapter = new DetailingAdapter(getActivity(), detailingLists);
            detailingrecyclerview.setAdapter(detailingAdapter);
            detailingAdapter.notifyDataSetChanged();
        }
        mCursor.close();
        db.close();
    }

    public void getbrandexpo(){
        branlist.clear();
        db.open();
        mCursor = db.select_brandexpo();
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("product_name_feed", mCursor.getString(0));
                Brandexpolist list = new Brandexpolist(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3));
                branlist.add(list);
            } while (mCursor.moveToNext());


        }
        mCursor.close();
        db.close();
    }
    public void gettotalcalls(){
        db.open();
        mCursor = db.select_totalcalls();
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("product_name_feed", mCursor.getString(0));
                drcallstext.setText(mCursor.getString(0));
                drcallcount.setText(mCursor.getString(1) + "/" + mCursor.getString(2));
                pharmcalltxt.setText(mCursor.getString(3));
                pharmcallcount.setText(mCursor.getString(4) + "/" + mCursor.getString(5));
                totaldrtext.setText(mCursor.getString(6));
                totaldrcount.setText(mCursor.getString(7) + "/" + mCursor.getString(8));

            } while (mCursor.moveToNext());


        }
        mCursor.close();
        db.close();
    }

}
