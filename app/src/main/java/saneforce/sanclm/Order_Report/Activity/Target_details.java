package saneforce.sanclm.Order_Report.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.Common_Class.Shared_Common_Pref;
import saneforce.sanclm.Common_Class.sqlLite;
import saneforce.sanclm.Order_Report.Adapter.TargetAdapter_new;
import saneforce.sanclm.Order_Report.Adapter.Targetadapter_newsecnd;
import saneforce.sanclm.Order_Report.modelclass.Primarysales_class;
import saneforce.sanclm.Order_Report.modelclass.TargetPrimary_Class;
import saneforce.sanclm.Order_Report.modelclass.TargetSecondary_class;
import saneforce.sanclm.Order_Report.modelclass.secondarysales_class;
import saneforce.sanclm.R;
import saneforce.sanclm.User_login.CustomerMe;
import saneforce.sanclm.api_Interface.ApiClient;
import saneforce.sanclm.api_Interface.ApiInterface;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.AppConfig;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;

public class Target_details extends AppCompatActivity {
    ImageView cancel;
    PieChart pieChart;
    Spinner spinner;
    String div_code = "";
    Date formattedDate1;
    Date formattedDate2;
    RecyclerView recyclerView,secondarycycle;
    TargetAdapter_new targetadt;
    Targetadapter_newsecnd targetsadt;
    TextView fromdate,todate;
    CheckBox primebox,scondbox;
    public ArrayList<TargetPrimary_Class> targetdetails;
    public ArrayList<TargetSecondary_class> targetSdetails;
    public ArrayList<Primarysales_class> SalesPrimaryDetails;
    public ArrayList<secondarysales_class> SalesSecondaryDetails;
    ArrayList<PieEntry> values;
    String prod,prod_code,division_name,achie,growth;
    double cnt,Target_Val,Prev_Sale,pc;
    String Target_vals,pcp;
    String DataSF = "";
    sqlLite sqlLite1;
    String curval = null;
    Gson gson;
    List<CustomerMe> CustomerMeList;
    ArrayList<String> hq_list1 = new ArrayList<>();
    ArrayList<String> hq_list1id = new ArrayList<>();
    PieData pieData;
    PieDataSet pieDataSet;
    String fromstrdate = "";
    String tostrdate="";
    TextView name,Primary,Target,Growth,Acheview,secondary;
    CheckBox primarychk,secondarychk;
    TextView Prr,Pcr,arrow,sec_date,sec_from,prisale,charttext;
    ArrayList<String> piedata = new ArrayList<String>();
    String sfdata = "";
    String fromdata = "";
    String todata = "";

    CommonSharedPreference mCommonSharedPreference;
    String sf_code,div_Code,sf_name,db_connPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.target_new1);

        mCommonSharedPreference=new CommonSharedPreference(this);

        cancel = findViewById(R.id.back_btn_chepro);
        pieChart=findViewById(R.id.piechart);
        spinner=findViewById(R.id.report_spin1);
        recyclerView=findViewById(R.id.primaryDetails);
        fromdate=findViewById(R.id.strtdate);
        todate=findViewById(R.id.cur_date);
        sec_date=findViewById(R.id.sec_to);
        sec_from=findViewById(R.id.sec_from);
        pieChart=findViewById(R.id.piechart);
        primarychk=findViewById(R.id.primarycheck);
        secondarychk=findViewById(R.id.Secondarycheck);
        prisale=findViewById(R.id.prisale);
        charttext=findViewById(R.id.charttext);
        sqlLite1 = new sqlLite(Target_details.this);


        targetdetails=new ArrayList<>();
        targetSdetails=new ArrayList<>();
        SalesPrimaryDetails=new ArrayList<>();
        SalesSecondaryDetails=new ArrayList<>();
        values = new ArrayList<PieEntry>();

        name = findViewById(R.id.tv_name);
        Primary = findViewById(R.id.tv_sales);
        Target = findViewById(R.id.tv_target);
        Growth = findViewById(R.id.tv_growth);
        Acheview = findViewById(R.id.tv_archieve);
        secondary = findViewById(R.id.tv_sec);
        Pcr=findViewById(R.id.tv_pcr);
        Prr=findViewById(R.id.tv_prr);
        arrow=findViewById(R.id.Arrow);

        sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);
        sf_name=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);

        Log.d("DataSF", sf_code + " " + div_Code+ " " + sf_name);

        String title = getIntent().getStringExtra("title");
        String primary = getIntent().getStringExtra("primary");
        String target = getIntent().getStringExtra("Target");
        String growth = getIntent().getStringExtra("growth");
        String acheview = getIntent().getStringExtra("acheive");
//        String call = getIntent().getStringExtra("call");
        String prr=getIntent().getStringExtra("prr");
        String pcr=getIntent().getStringExtra("pcr");
        String secd=getIntent().getStringExtra("Sec");
        String frmdate=getIntent().getStringExtra("Fromdate");
        String tdate=getIntent().getStringExtra("Todate");


        Double targ = Double.parseDouble(secd);
        Double pri = Double.parseDouble(primary);
        Log.d("primary",""+pri);

        if (pri.equals(0.0)){
            prisale.setText("0.00");
        }else {
            Double prisal=(targ * 100)/pri;
            prisale.setText(String.format("%.2f",prisal)+"%");
        }

        name.setText(title);
        Primary.setText(primary);
        Target.setText(target);
        Growth.setText(growth);
        Acheview.setText(acheview);
        Prr.setText(prr);
        Pcr.setText(pcr);
        fromdate.setText(frmdate);
        todate.setText(tdate);
        sec_from.setText(frmdate);
        sec_date.setText(tdate);
        secondary.setText(secd);
//        if ( !Dcrdatas.online_primary1.equals("")){
//            secondary.setText(secd);
//        }else {
//        }


        if (Growth.getText().toString().matches("^-\\d*\\.?\\d+$")){
            arrow.setBackgroundResource(R.drawable.roundeddown);
        }else {
            arrow.setBackgroundResource(R.drawable.roundbackground);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Target_details.this.finish();
            }
        });


//        primarychk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "pre", Toast.LENGTH_SHORT).show();
//                Orderproduct_Piechart();
//                secondarychk.setChecked(false);
//                //primarychk.setChecked(true);
//            }
//        });
//        secondarychk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Orderproduct_PiechartSecondary();
//                primarychk.setChecked(false);
//               // secondarychk.setChecked(true);
//            }
//        });
        primarychk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked){
                    Orderproduct_Piechart();
                    secondarychk.setChecked(false);
                }
                else{
                }
            }
        });
        secondarychk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    Orderproduct_PiechartSecondary();
                    primarychk.setChecked(false);
                }
                else{
                    Orderproduct_Piechart();
                }
            }
        });

        Orderproduct_Piechart();
    }

    private void Orderproduct_Piechart() {
        try {

            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

            String fromstrdate = "";
            String tostrdate="";

            primarychk.setChecked(true);


            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" order request","order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/product_sales_primary");
            QryParam.put("divisionCode", div_Code);
            QryParam.put("sfCode",sf_code);
            QryParam.put("rSF", sf_code);
//            QryParam.put("from_date", "2019-05-04");
//            QryParam.put("to_date", "2020-06-04");
            QryParam.put("from_date", Dcrdatas.startdate);
            QryParam.put("to_date", Dcrdatas.enddate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_details.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataPieprimary(response.body().toString(),div_Code,sf_code,Dcrdatas.startdate,Dcrdatas.enddate);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void parseJsonDataPieprimary(String monitor,String div,String sfdata,String from,String to) {
        String date = "";
        try {
            try {
                JSONArray js = new JSONArray(monitor);
                Log.e("PieArray",js.toString());
                if (js.length() > 0) {
                    SalesPrimaryDetails.clear();
                    for (int i = 0; i < js.length(); i++) {

                        JSONObject jsonObject = js.getJSONObject(i);
                        cnt=jsonObject.getDouble("cnt");
                        prod=jsonObject.getString("prod_name");
                        prod_code = jsonObject.getString("prod_code");
                        division_name = jsonObject.getString("Division_Name");
                        Target_Val = jsonObject.getDouble("Target_Val");
                        Prev_Sale = jsonObject.getDouble("Prev_Sale");
                        achie = jsonObject.getString("achie");
                        growth = jsonObject.getString("Growth");
                        pc = jsonObject.getDouble("PC");

                        Primarysales_class primarysales_class = new Primarysales_class(cnt,Target_Val,Prev_Sale,pc,prod,prod_code,division_name,achie,growth);
                        SalesPrimaryDetails.add(primarysales_class);
                        // targetadt.notifyDataSetChanged();

                        double sum = 0.0; //if you use version earlier than java-8
//double sum = IntStream.of(keyList).sum(); //if you are using java-8
                        for(int j = 0 ; j < SalesPrimaryDetails.size() ; j++){
                            sum += SalesPrimaryDetails.get(j).getCnt();
                        }
                        for(int k = 0 ; k < SalesPrimaryDetails.size() ; k++){
                            System.out.println((SalesPrimaryDetails.get(k).getCnt()/sum )*100 + "%");

                        }


                        final ArrayList<PieEntry> values=new ArrayList<PieEntry>();
//                        values.add(new PieEntry(35.34f,"Citmax Gold"));
//                        values.add(new PieEntry(11.9f,"Thiolac"));
//                        values.add(new PieEntry(9.78f,"Citmax D"));
//                        values.add(new PieEntry(9.22f,"Win RD"));
//                        values.add(new PieEntry(8.88f,"Tryclo D"));
//                        values.add(new PieEntry(8.62f,"Phofol D"));
//                        values.add(new PieEntry(7.45f,"Win HB"));
//                        values.add(new PieEntry(4.64f,"Gawin Nt"));
//                        values.add(new PieEntry(4.16f,"Protwin"));
                        if (sum==0.0){
                            pieChart.setVisibility(View.GONE);
                            charttext.setVisibility(View.VISIBLE);
                            charttext.setText("No Data Found");
                        }
                        else {
                            pieChart.setVisibility(View.VISIBLE);
                            charttext.setVisibility(View.GONE);
                            piedata.clear();
                            for (int n = 0; n < SalesPrimaryDetails.size(); n++) {
                                double vuu = SalesPrimaryDetails.get(n).getCnt();
                                String name = SalesPrimaryDetails.get(n).getProd_name();
                                String prod_code = SalesPrimaryDetails.get(n).getProd_code();
                                double Target_Val = SalesPrimaryDetails.get(n).getTarget_Val();
                                double Prev_Sale = SalesPrimaryDetails.get(n).getPrev_Sale();
                                double pc = SalesPrimaryDetails.get(n).getPC();
                                String Division_Name = SalesPrimaryDetails.get(n).getDivision_Name();
                                String achie = SalesPrimaryDetails.get(n).getAchie();
                                String Growth = SalesPrimaryDetails.get(n).getGrowth();
                                float soldPercentage = (float) (vuu * 100 / sum);
                                PieEntry pieEntry = new PieEntry(soldPercentage, name);
                                values.add(pieEntry);
                                piedata.add(prod_code+"~"+Target_Val+"~"+Prev_Sale+"~"+pc+"~"+Division_Name+"~"+achie+"~"+Growth+"~"+div+"~"+sfdata+"~"+from+"~"+to);
                            }
                        }

                        PieDataSet pieDataSet=new PieDataSet(values,"");
                        PieData pieData=new PieData(pieDataSet);
                        pieData.setValueFormatter(new PercentFormatter());
                        pieChart.setData(pieData);
                        pieChart.invalidate();
                        //pieChart.setHoleRadius(15f);
                        pieChart.setDrawHoleEnabled(true);
                        //pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        pieData.setValueTextSize(10f);
                        pieData.setValueTextColor(Color.WHITE);
                        pieChart.setDescription(null);
                        pieChart.animateXY(1400,1400);
                        pieChart.setUsePercentValues(false);

                        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                        pieDataSet.setSliceSpace(4f);
                        pieDataSet.setValueTextSize(10f);
                        pieDataSet.setSelectionShift(10f);
                        pieDataSet.setValueLinePart1OffsetPercentage(60.f);
                        pieDataSet.setValueLinePart1Length(0.3f);
                        pieDataSet.setValueLinePart2Length(0.3f);
                        //pieDataSet.setValueLineColor(Color.MAGENTA);
                        //pieDataSet.setValueTextColor(Color.GREEN);


                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.rgb(255, 51, 153));
                        colors.add(Color.rgb(255,215,0));
                        colors.add(Color.rgb(102,205,170));
                        colors.add(Color.rgb(30,144,255));
                        colors.add(Color.rgb(3,63,73));
                        colors.add(Color.rgb(255,178,102));
                        colors.add(Color.rgb(255,51,51));
                        colors.add(Color.rgb(0,51,25));
                        colors.add(Color.rgb(0,102,51));

                        pieDataSet.setColors(colors);
                        pieChart.setEntryLabelColor(Color.RED);
                        pieChart.setEntryLabelTextSize(10f);
                        pieDataSet.setUsingSliceColorAsValueLineColor(true);

                        //  pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                        //   pieDataSet.setValueTextColor(getResources().getColor(R.color.Darkblueone));
                        Legend legend = pieChart.getLegend();
                        legend.setEnabled(false);
                        //legend.setTextColor(Color.GREEN);

                        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                            @Override
                            public void onValueSelected(Entry e, Highlight h) {
                                if (e == null) {
                                    return;
                                }
                                for (int i=0;i < values.size(); i++){
                                    if (values.get(i).getY() == e.getY()) {
                                        showchartdetails(values.get(i).getLabel(),values.get(i).getY(),piedata,i);
                                        Log.d("data_values",""+values.get(i).getLabel()+"--"+values.get(i).getY()+"**"+piedata+"//"+i);
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected() {

                            }
                        });


                    }
                } else {
//                    bottom_sum.setVisibility(View.GONE);
                    SalesPrimaryDetails.clear();
                    pieChart.setVisibility(View.GONE);
                    charttext.setText("No Records Found");
                    charttext.setVisibility(View.VISIBLE);
                    //targetadt.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "No Records found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Orderproduct_PiechartSecondary() {
        try {
            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

            if (!Dcrdatas.from_date.equals("")){
                fromstrdate= Dcrdatas.from_date;
                //fromdate.setText(fromstrdate);
            }else {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                fromstrdate = df.format(c);

            }
            if (!Dcrdatas.to_date.equals("")){
                tostrdate= Dcrdatas.to_date;
                //todate.setText(tostrdate);
            }else {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                tostrdate = df.format(c);

            }

            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" order request","order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/product_sales_secondary");
            QryParam.put("divisionCode", div_Code);
            QryParam.put("sfCode",sf_code);
            QryParam.put("rSF", sf_code);
//            QryParam.put("from_date", "2019-05-04");
//            QryParam.put("to_date", "2020-06-04");
            QryParam.put("from_date", Dcrdatas.startdate);
            QryParam.put("to_date", Dcrdatas.enddate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Target_details.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getsecDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataPieSecondary(response.body().toString(),div_Code,sf_code,Dcrdatas.startdate,Dcrdatas.enddate);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("expense:Failure", t.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void parseJsonDataPieSecondary(String monitor,String div,String sfdata,String from,String to){
        String date = "";
        try {
            try {
                JSONArray js = new JSONArray(monitor);
                Log.e("PieArray",js.toString());
                if (js.length() > 0) {
                    SalesSecondaryDetails.clear();
                    for (int i = 0; i < js.length(); i++) {

                        JSONObject jsonObject = js.getJSONObject(i);
                        cnt = jsonObject.getDouble("cnt");
                        prod = jsonObject.getString("prod_name");
                        prod_code = jsonObject.getString("prod_code");
                        division_name = jsonObject.getString("Division_Name");
                        Target_vals = jsonObject.getString("Target_Val");
                        achie = jsonObject.getString("achie");
                        growth = jsonObject.getString("Growth");
                        pcp = jsonObject.getString("PC");

                        secondarysales_class primarysales_class = new secondarysales_class(cnt,prod,prod_code,division_name,Target_vals,achie,growth,pcp);
                        SalesSecondaryDetails.add(primarysales_class);
                        // targetadt.notifyDataSetChanged();

                        double sum = 0.0; //if you use version earlier than java-8
//double sum = IntStream.of(keyList).sum(); //if you are using java-8
                        for(int j = 0 ; j < SalesSecondaryDetails.size() ; j++){
                            sum += SalesSecondaryDetails.get(j).getCnt();
                        }
                        for(int k = 0 ; k < SalesSecondaryDetails.size() ; k++){
                            System.out.println((SalesSecondaryDetails.get(k).getCnt()/sum )*100 + "%");

                        }


                        final ArrayList<PieEntry> values=new ArrayList<PieEntry>();
//                        values.add(new PieEntry(35.34f,"Citmax Gold"));
//                        values.add(new PieEntry(11.9f,"Thiolac"));
//                        values.add(new PieEntry(9.78f,"Citmax D"));
//                        values.add(new PieEntry(9.22f,"Win RD"));
//                        values.add(new PieEntry(8.88f,"Tryclo D"));
//                        values.add(new PieEntry(8.62f,"Phofol D"));
//                        values.add(new PieEntry(7.45f,"Win HB"));
//                        values.add(new PieEntry(4.64f,"Gawin Nt"));
//                        values.add(new PieEntry(4.16f,"Protwin"));
                        if (sum==0.0){
                            pieChart.setVisibility(View.GONE);
                            charttext.setVisibility(View.VISIBLE);
                            charttext.setText("No Data Found");
                        }
                        else{
                            pieChart.setVisibility(View.VISIBLE);
                            charttext.setVisibility(View.GONE);
                            piedata.clear();
                            for(int n = 0;n<SalesSecondaryDetails.size();n++) {
                                double vuu = SalesSecondaryDetails.get(n).getCnt();
                                String name = SalesSecondaryDetails.get(n).getProd_name();
                                String prod_code = SalesSecondaryDetails.get(n).getProd_code();
                                String Target_Val = SalesSecondaryDetails.get(n).getTarget_Val();
                                String pc = SalesSecondaryDetails.get(n).getPC();
                                String Division_Name = SalesSecondaryDetails.get(n).getDivision_Name();
                                String achie = SalesSecondaryDetails.get(n).getAchie();
                                String Growth = SalesSecondaryDetails.get(n).getGrowth();
                                float soldPercentage = ( float ) (vuu * 100 / sum);
                                PieEntry pieEntry = new PieEntry(soldPercentage, name);
                                values.add(pieEntry);
                                piedata.add(prod_code+"~"+Target_Val+"~"+vuu+"~"+pc+"~"+Division_Name+"~"+achie+"~"+Growth+"~"+div+"~"+sfdata+"~"+from+"~"+to);
                            }
                        }
                        PieDataSet pieDataSet=new PieDataSet(values,"");
                        PieData pieData=new PieData(pieDataSet);
                        pieData.setValueFormatter(new PercentFormatter());
                        pieChart.setData(pieData);
                        pieChart.invalidate();
                        //pieChart.setHoleRadius(15f);
                        pieChart.setDrawHoleEnabled(true);
                        //pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        pieData.setValueTextSize(10f);
                        pieData.setValueTextColor(Color.WHITE);
                        pieChart.setDescription(null);
                        pieChart.animateXY(1400,1400);
                        pieChart.setUsePercentValues(false);

                        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                        pieDataSet.setSliceSpace(2f);
                        pieDataSet.setValueTextSize(10f);
                        pieDataSet.setSelectionShift(10f);
                        pieDataSet.setValueLinePart1OffsetPercentage(60.f);
                        pieDataSet.setValueLinePart1Length(0.3f);
                        pieDataSet.setValueLinePart2Length(0.3f);
                        //pieDataSet.setValueLineColor(Color.MAGENTA);
                        //pieDataSet.setValueTextColor(Color.GREEN);


                        ArrayList<Integer> colors = new ArrayList<>();
//                        colors.add(Color.rgb(213, 0, 0));
//                        colors.add(Color.rgb(255,69,0));
//                        colors.add(Color.rgb(255,140,0));
//                        colors.add(Color.rgb(0,100,0));
//                        colors.add(Color.rgb(184,134,11));
//                        colors.add(Color.rgb(0,0,128));
//                        colors.add(Color.rgb(30,144,255));
//                        colors.add(Color.rgb(139,69,19));
//                        colors.add(Color.rgb(183,176,253));
                        colors.add(Color.rgb(0,0,128));
                        colors.add(Color.rgb(213, 0, 0));
                        colors.add(Color.rgb(184,134,11));
                        colors.add(Color.rgb(255,69,0));
                        colors.add(Color.rgb(30,144,255));
                        colors.add(Color.rgb(0,100,0));
                        colors.add(Color.rgb(255,140,0));
                        colors.add(Color.rgb(183,176,253));
                        colors.add(Color.rgb(139,69,19));

                        pieDataSet.setColors(colors);
                        pieChart.setEntryLabelColor(Color.RED);
                        pieChart.setEntryLabelTextSize(10f);
                        pieDataSet.setUsingSliceColorAsValueLineColor(true);

                        //  pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                        //   pieDataSet.setValueTextColor(getResources().getColor(R.color.Darkblueone));
                        Legend legend = pieChart.getLegend();
                        legend.setEnabled(false);
                        //legend.setTextColor(Color.GREEN);

                        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                            @Override
                            public void onValueSelected(Entry e, Highlight h) {
                                if (e == null) {
                                    return;
                                }
                                for (int i=0;i < values.size(); i++){
                                    if (values.get(i).getY() == e.getY()) {
                                        showchartdetailssec(values.get(i).getLabel(),values.get(i).getY(),piedata,i);
                                        Log.d("data_values",""+values.get(i).getLabel()+"--"+values.get(i).getY()+"**"+piedata+"//"+i);
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected() {

                            }
                        });


                    }
                } else {
//                    bottom_sum.setVisibility(View.GONE);
                    SalesSecondaryDetails.clear();
                    pieChart.setVisibility(View.GONE);
                    charttext.setText("No Records Found");
                    charttext.setVisibility(View.VISIBLE);
                    //targetadt.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "No Records found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void showchartdetails(final String label, float y, ArrayList<String> piedata, int position){

        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(Target_details.this);
        LayoutInflater inflater = Target_details.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialogue, null);
        dialogBuilder.setView(dialogView);
        TextView prdname = dialogView.findViewById(R.id.prd_name);
        TextView prdtarget = dialogView.findViewById(R.id.prd_Target);
        TextView prdcnt = dialogView.findViewById(R.id.prd_Sales);
        TextView prdarcheive = dialogView.findViewById(R.id.prd_Archeive);
        TextView prdgrowth = dialogView.findViewById(R.id.prd_growth);
        TextView prdpcm = dialogView.findViewById(R.id.prd_pcpm);
        TextView prddname = dialogView.findViewById(R.id.prd_dname);
        TextView infobut = dialogView.findViewById(R.id.info_but);
        TextView ok =dialogView.findViewById(R.id.okbut);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        prdname.setText(label);
        Log.d("position", String.valueOf(position));
        final String[] separateddoc =piedata.get(position).split("~");
        prdtarget.setText(separateddoc[1]);
        prdcnt.setText(separateddoc[2]);
        prdarcheive.setText(separateddoc[5]);
        prdgrowth.setText(separateddoc[6]);
        prdpcm.setText(separateddoc[3]);
        prddname.setText(separateddoc[4]);

        Log.d("webdata",separateddoc[7]+"/*/"+separateddoc[8]+"**"+separateddoc[9]+"**"+separateddoc[10]);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("MM");
        DateFormat outputyear = new SimpleDateFormat("yyyy");
        String inputDateStr=separateddoc[9];
        String inputDateStr1=separateddoc[10];

        Date date = null;
        Date date1 = null;
        try {
            date = inputFormat.parse(inputDateStr);
            date1 = inputFormat.parse(inputDateStr1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String outputDateStr = outputFormat.format(date);
        final String outputDateyear = outputyear.format(date);

        final String outputDateStr1 = outputFormat.format(date1);
        final String outputDateyear1 = outputyear.format(date1);

        Log.d("webdata",outputDateStr+"/*/"+outputDateyear+"**"+outputDateStr1+"**"+outputDateyear1);

//        sqlLite sqlLite;
//        String curval = null;
//        final List<CustomerMe> CustomerMeList;
//        sqlLite = new sqlLite(this);
//        String username = Shared_Common_Pref.getusernameFromSP(this);
//        Cursor cursor = sqlLite.getAllLoginData();
//
//        if (cursor.moveToFirst()) {
//            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//        }
//        cursor.close();
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<CustomerMe>>() {
//        }.getType();
//        CustomerMeList = gson.fromJson(curval, type);
//        Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " +username);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        infobut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oo = new Intent(Target_details.this, Web_Activity.class);
                oo.putExtra("sfcode",separateddoc[8]);
                oo.putExtra("fmon",outputDateStr);
                oo.putExtra("fyear",outputDateyear);
                oo.putExtra("tmon",outputDateStr1);
                oo.putExtra("tyear",outputDateyear1);
                oo.putExtra("Dcode",separateddoc[7]);
                oo.putExtra("Brandcd",separateddoc[0]);
                oo.putExtra("Brandname",label);
                oo.putExtra("sfname",CustomerMeList.get(0).getSFName());
                startActivity(oo);
            }
        });
        alertDialog.show();
    }

    public void showchartdetailssec(final String label, float y, ArrayList<String> piedata, int position){

        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(Target_details.this);
        LayoutInflater inflater = Target_details.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialogue, null);
        dialogBuilder.setView(dialogView);
        TextView prdname = dialogView.findViewById(R.id.prd_name);
        TextView prdtarget = dialogView.findViewById(R.id.prd_Target);
        TextView prdcnt = dialogView.findViewById(R.id.prd_Sales);
        TextView prdarcheive = dialogView.findViewById(R.id.prd_Archeive);
        TextView prdgrowth = dialogView.findViewById(R.id.prd_growth);
        TextView prdpcm = dialogView.findViewById(R.id.prd_pcpm);
        TextView prddname = dialogView.findViewById(R.id.prd_dname);
        TextView infobut = dialogView.findViewById(R.id.info_but);
        TextView ok =dialogView.findViewById(R.id.okbut);
        final android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        prdname.setText(label);
        Log.d("position", String.valueOf(position));
        final String[] separateddoc =piedata.get(position).split("~");
        prdtarget.setText(separateddoc[1]);
        prdcnt.setText(separateddoc[2]);
        prdarcheive.setText(separateddoc[5]);
        prdgrowth.setText(separateddoc[6]);
        prdpcm.setText(separateddoc[3]);
        prddname.setText(separateddoc[4]);

        Log.d("webdata",separateddoc[7]+"/*/"+separateddoc[8]+"**"+separateddoc[9]+"**"+separateddoc[10]);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("MM");
        DateFormat outputyear = new SimpleDateFormat("yyyy");
        String inputDateStr=separateddoc[9];
        String inputDateStr1=separateddoc[10];

        Date date = null;
        Date date1 = null;
        try {
            date = inputFormat.parse(inputDateStr);
            date1 = inputFormat.parse(inputDateStr1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String outputDateStr = outputFormat.format(date);
        final String outputDateyear = outputyear.format(date);

        final String outputDateStr1 = outputFormat.format(date1);
        final String outputDateyear1 = outputyear.format(date1);

        Log.d("webdata",outputDateStr+"/*/"+outputDateyear+"**"+outputDateStr1+"**"+outputDateyear1);

//        sqlLite sqlLite;
//        String curval = null;
//        final List<CustomerMe> CustomerMeList;
//        sqlLite = new sqlLite(this);
//        String username = Shared_Common_Pref.getusernameFromSP(this);
//        Cursor cursor = sqlLite.getAllLoginData();
//
//        if (cursor.moveToFirst()) {
//            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//        }
//        cursor.close();
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<CustomerMe>>() {
//        }.getType();
//        CustomerMeList = gson.fromJson(curval, type);
//        Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " +username);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        infobut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oo = new Intent(Target_details.this, Web_Activity.class);
                oo.putExtra("sfcode",separateddoc[8]);
                oo.putExtra("fmon",outputDateStr);
                oo.putExtra("fyear",outputDateyear);
                oo.putExtra("tmon",outputDateStr1);
                oo.putExtra("tyear",outputDateyear1);
                oo.putExtra("Dcode",separateddoc[7]);
                oo.putExtra("Brandcd",separateddoc[0]);
                oo.putExtra("Brandname",label);
                oo.putExtra("sfname",sf_name);
                startActivity(oo);
            }
        });
        alertDialog.show();
    }
}
