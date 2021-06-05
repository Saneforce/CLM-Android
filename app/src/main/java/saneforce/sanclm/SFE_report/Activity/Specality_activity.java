package saneforce.sanclm.SFE_report.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Common_Class.Shared_Common_Pref;
import saneforce.sanclm.Common_Class.sqlLite;
import saneforce.sanclm.R;
import saneforce.sanclm.User_login.CustomerMe;
import saneforce.sanclm.api_Interface.ApiClient;
import saneforce.sanclm.api_Interface.ApiInterface;
import saneforce.sanclm.api_Interface.AppConfig;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

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

public class Specality_activity extends AppCompatActivity {

    private BarChart chart;
    float barWidth;
    float barSpace;
    float groupSpace;
    ImageView ic_back, imp_back_filter,filter_btn;
    DrawerLayout slidemenu;
    TextView monthselection, yearselection, catogoryselection, specalityselection;
    CardView Catgory_card, specality_card, speclcard,month_card,year_card;
    TextView cattxt, spectxt;
    String selected_data = "";
    Dialog monthdialog;
    NumberPicker monthpicker;
    NumberPicker Yearpicker;
    Button clear, apply;
    TextView cur_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specality_activity);

        ic_back = findViewById(R.id.back_btn);
        monthselection = findViewById(R.id.select_months);
        yearselection = findViewById(R.id.select_years);
        month_card=findViewById(R.id.prm);
        year_card=findViewById(R.id.sec);
        specality_card = ( CardView ) findViewById(R.id.ytd);
        Catgory_card = ( CardView ) findViewById(R.id.qtd);
        speclcard = ( CardView ) findViewById(R.id.specl_class);
        cattxt = ( TextView ) findViewById(R.id.cat_txt);
        spectxt = ( TextView ) findViewById(R.id.spec_text);
        chart = findViewById(R.id.barchart);
        clear = ( Button ) findViewById(R.id.filter_clear_1);
        apply = ( Button ) findViewById(R.id.filter_submit_1);
        slidemenu = ( DrawerLayout ) findViewById(R.id.drawer_layout);
        cur_month = findViewById(R.id.current_date);
        filter_btn = findViewById(R.id.camp_filter);
        barWidth = 0.3f;
        barSpace = 0f;
        groupSpace = 0.4f;
        chart.setDescription(null);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        ActionBarDrawerToggle toggle1 = new ActionBarDrawerToggle(
                Specality_activity.this, slidemenu, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        slidemenu.setDrawerListener(toggle1);
        toggle1.syncState();

        final Calendar c = Calendar.getInstance();
        final int y = c.get(Calendar.YEAR);
        final int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);

        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        Date date = new Date();
        Log.d("Month", dateFormat.format(date));
        cur_month.setText(dateFormat.format(date));

        specality_card.setCardBackgroundColor(Color.RED);
        spectxt.setTextColor(Color.WHITE);
        speclcard.setVisibility(View.VISIBLE);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imp_back_filter = findViewById(R.id.back_btn_filter);
        imp_back_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidemenu.closeDrawer(Gravity.RIGHT);

            }
        });
        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopup(v);
                slidemenu.openDrawer(Gravity.RIGHT);
            }
        });

        month_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Specality_activity.this);
                LayoutInflater factory = LayoutInflater.from(Specality_activity.this);
                final View vieww = factory.inflate(R.layout.customdialoglayout, null);
                alertDialog.setView(vieww);
                monthpicker = vieww.findViewById(R.id.picker_month);
                Yearpicker = vieww.findViewById(R.id.picker_year);
                Yearpicker.setVisibility(View.GONE);
                monthpicker.setMaxValue(12);
                monthpicker.setMinValue(1);
                monthpicker.setDisplayedValues(new String[]{"Jan", "Feb", "Mar", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"});
                Yearpicker.setMinValue(2000);
                Yearpicker.setMaxValue(2500);
                monthpicker.setValue(m);
                Yearpicker.setValue(y);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String months = String.valueOf(monthpicker.getValue());
                        String years = String.valueOf(Yearpicker.getValue());
                        formatDate(months);
                        dialog.dismiss();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        year_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Specality_activity.this);
                LayoutInflater factory = LayoutInflater.from(Specality_activity.this);
                final View vieww = factory.inflate(R.layout.customdialoglayout, null);
                alertDialog.setView(vieww);
                monthpicker = vieww.findViewById(R.id.picker_month);
                Yearpicker = vieww.findViewById(R.id.picker_year);
                monthpicker.setVisibility(View.GONE);
                monthpicker.setMaxValue(12);
                monthpicker.setMinValue(1);
                Yearpicker.setMinValue(2000);
                Yearpicker.setMaxValue(2500);
                monthpicker.setValue(m);
                Yearpicker.setValue(y);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String months = String.valueOf(monthpicker.getValue());
                        String years = String.valueOf(Yearpicker.getValue());
                        yearselection.setText("Year :" + years);
                        dialog.dismiss();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        Catgory_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specality_card.setCardBackgroundColor(Color.WHITE);
                spectxt.setTextColor(Color.RED);
                Catgory_card.setCardBackgroundColor(Color.RED);
                cattxt.setTextColor(Color.WHITE);
                selected_data = "Category";
                speclcard.setVisibility(View.GONE);
            }
        });

        specality_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Catgory_card.setCardBackgroundColor(Color.WHITE);
                cattxt.setTextColor(Color.RED);
                specality_card.setCardBackgroundColor(Color.RED);
                spectxt.setTextColor(Color.WHITE);
                selected_data = "Speciality";
                speclcard.setVisibility(View.VISIBLE);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("rateess",selected_data);
                if (selected_data.equalsIgnoreCase("Category")) {
                    Intent oo = new Intent(Specality_activity.this, SFE_Activtity.class);
                    startActivity(oo);
                } else if (selected_data.equalsIgnoreCase("Speciality")) {

                } else {
                    Toast.makeText(Specality_activity.this, "Please Select the options to filter", Toast.LENGTH_SHORT).show();
                }
                slidemenu.closeDrawer(Gravity.RIGHT);
            }
        });

        dummygragh();
    }

    public void dummygragh() {

        int groupCount = 6;

        ArrayList xVals = new ArrayList();

        xVals.add("Jan");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Apr");
        xVals.add("May");
        xVals.add("Jun");

        ArrayList yVals1 = new ArrayList();
        ArrayList yVals2 = new ArrayList();


        yVals1.add(new BarEntry(1, ( float ) 1));
        yVals2.add(new BarEntry(1, ( float ) 2));
        yVals1.add(new BarEntry(2, ( float ) 3));
        yVals2.add(new BarEntry(2, ( float ) 4));
        yVals1.add(new BarEntry(3, ( float ) 5));
        yVals2.add(new BarEntry(3, ( float ) 6));
        yVals1.add(new BarEntry(4, ( float ) 7));
        yVals2.add(new BarEntry(4, ( float ) 8));
        yVals1.add(new BarEntry(5, ( float ) 9));
        yVals2.add(new BarEntry(5, ( float ) 10));
        yVals1.add(new BarEntry(6, ( float ) 11));
        yVals2.add(new BarEntry(6, ( float ) 12));
        BarDataSet set1, set2;
        set1 = new BarDataSet(yVals1, "A");
        set1.setColor(Color.RED);
        set2 = new BarDataSet(yVals2, "B");
        set2.setColor(Color.BLUE);
        BarData data = new BarData(set1, set2);
        data.setValueFormatter(new LargeValueFormatter());
        chart.setData(data);
        chart.getBarData().setBarWidth(barWidth);
        chart.getXAxis().setAxisMinimum(0);
        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(0, groupSpace, barSpace);
        chart.getData().setHighlightEnabled(false);
        chart.invalidate();

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        //X-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(6);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
//Y-axis
        chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

    }

    public String formatDate(String dateString) {
        SimpleDateFormat fmt = new SimpleDateFormat("M");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("MMM");
        Log.d("outdate", fmtOut.format(date));
        monthselection.setText("Month :" + fmtOut.format(date));
        return fmtOut.format(date);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
    public void CategoryHq_filterdate(String div_code) {
        try {
            String division=div_code;
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(Specality_activity.this);
            String username = Shared_Common_Pref.getusernameFromSP(Specality_activity.this);
            Cursor cursor = sqlLite.getAllLoginData();

            if (cursor.moveToFirst()) {
                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
            }
            cursor.close();
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomerMe>>() {
            }.getType();
            CustomerMeList = gson.fromJson(curval, type);
            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());


            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
            } else {
                div_code = CustomerMeList.get(0).getDivisionCode();
            }

            DateFormat dateFormat1 = new SimpleDateFormat("M");
            Date date1 = new Date();
            Log.d("Month1",dateFormat1.format(date1));

            DateFormat dateFormat2 = new SimpleDateFormat("yyyy");
            Date date2 = new Date();
            Log.d("Month1",dateFormat2.format(date2));

            String fromstrdate = dateFormat1.format(date1);
            String tostrdate=dateFormat2.format(date2);

            ApiInterface apiService = ApiClient.getClient(Specality_activity.this).create(ApiInterface.class);
            Log.e(" caty request","cat Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/speciality_sfe");
            QryParam.put("divisionCode", division+",");
            QryParam.put("sfCode", "admin");
            QryParam.put("fmonth", fromstrdate);
            QryParam.put("fyear", tostrdate);
            QryParam.put("spec_code", "");
            QryParam.put("mode", "");
            Log.e("catreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(Specality_activity.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(Specality_activity.this).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());

                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(Specality_activity.this, "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Specality_activity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}