package saneforce.sanclm.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.DetailingOfReport;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.adapter_class.AdapterDetailingReport;
import saneforce.sanclm.adapter_class.AdapterForSelectionList;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;

public class BrandDetailing extends Fragment {
    PieChart pie,pie1;
    ListView list;
    TextView tt_terri,txt_clu,tt_cat,txt_cat,tt_spec,txt_spec,tt_qua,txt_qua,tt_cls,txt_cls;
    RelativeLayout lay_filter;
    String[] country = { "This Month", "Previous Month", "Customise"};
    CardView card_cluster,card_cat,card_spe,card_qua,card_cls;
    String clu="",cat="",spec="",qua="",cls="";
    ArrayList<PopFeed> cluster_list=new ArrayList<>();
    ArrayList<PopFeed> category_list=new ArrayList<>();
    ArrayList<PopFeed> speciality_list=new ArrayList<>();
    ArrayList<PopFeed> qualification_list=new ArrayList<>();
    ArrayList<PopFeed> class_list=new ArrayList<>();
    TextView txt_from,txt_to;
    DatePickerDialog fromDatePickerDialog;
    Api_Interface apiService;
    CommonSharedPreference commonSharedPreference;
    String baseurl="",SFCode;
    ArrayList<DetailingOfReport> mainArray=new ArrayList<>();
    ArrayList<DetailingOfReport> filter_mis_array=new ArrayList<>();
    DrawerLayout drawer;
    Button btn_apply,btn_clr;
    ImageView close_img;
    AdapterDetailingReport adpt;
    String fdt,tdt;
    LinearLayout lay1,lay2,lay3,lay4;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vv=inflater.inflate(R.layout.activity_brand_detailing,container,false);
        pie=vv.findViewById(R.id.pie);
        pie1=vv.findViewById(R.id.pie1);
        list=vv.findViewById(R.id.list);
        tt_terri=vv.findViewById(R.id.tt_terri);
        txt_clu=vv.findViewById(R.id.txt_clu);
        tt_cat=vv.findViewById(R.id.tt_cat);
        txt_cat=vv.findViewById(R.id.txt_cat);
        tt_spec=vv.findViewById(R.id.tt_spec);
        txt_spec=vv.findViewById(R.id.txt_spec);
        tt_qua=vv.findViewById(R.id.tt_qua);
        txt_qua=vv.findViewById(R.id.txt_qua);
        tt_cls=vv.findViewById(R.id.tt_cls);
        txt_cls=vv.findViewById(R.id.txt_cls);
        txt_from=vv.findViewById(R.id.txt_from);
        txt_to=vv.findViewById(R.id.txt_to);
        lay1=vv.findViewById(R.id.lay1);
        lay2=vv.findViewById(R.id.lay2);
        lay3=vv.findViewById(R.id.lay3);
        lay4=vv.findViewById(R.id.lay4);

        lay_filter=vv.findViewById(R.id.lay_filter);
        card_cluster=(CardView)vv.findViewById(R.id.card_cluster);
        card_cat=(CardView)vv.findViewById(R.id.card_cat);
        card_spe=(CardView)vv.findViewById(R.id.card_spe);
        card_spe.setVisibility(View.GONE);
        card_qua=(CardView)vv.findViewById(R.id.card_qua);
        card_qua.setVisibility(View.GONE);
        card_cls=(CardView)vv.findViewById(R.id.card_cls);
        card_cls.setVisibility(View.GONE);
        btn_apply=(Button)vv.findViewById(R.id.btn_apply);

        btn_clr=(Button)vv.findViewById(R.id.btn_clr);
        close_img=(ImageView)vv.findViewById(R.id.close_img);
        drawer = (DrawerLayout) vv.findViewById(R.id.drawer_layout);
        Spinner spin = (Spinner) vv.findViewById(R.id.spinner);

        commonSharedPreference=new CommonSharedPreference(getActivity());
        baseurl =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SFCode =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        apiService = RetroClient.getClient(baseurl).create(Api_Interface.class);
        //callApi(CommonUtilsMethods.getCurrentDate(),CommonUtilsMethods.getCurrentDate());
        tt_terri.setText("Field Force");
        tt_cat.setText("Type");
        tt_spec.setText("Territory");
        tt_qua.setText("Speciality");
        tt_cls.setText("Class");

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

        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(mainArray,DetailingOfReport.customerNameComparator);
                adpt.notifyDataSetChanged();
            }
        });
        lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(mainArray,DetailingOfReport.fieldForceComparator);
                adpt.notifyDataSetChanged();
            }
        });
        lay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(mainArray,DetailingOfReport.typeComparator);
                adpt.notifyDataSetChanged();
            }
        });
        lay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(mainArray,DetailingOfReport.territoryComparator);
                adpt.notifyDataSetChanged();
            }
        });
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("printing_spinner_pos",i+" value ");
                if(i==2){
                    txt_from.setVisibility(View.VISIBLE);
                    txt_to.setVisibility(View.VISIBLE);
                }
                else{
                    txt_from.setVisibility(View.GONE);
                    txt_to.setVisibility(View.GONE);
                }
                if(i==0){
                    Calendar c = Calendar.getInstance();
                    int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    String last= CommonUtilsMethods.getCurrentDate();
                    Log.v("maximum_days",monthMaxDays+" hello"+last.substring(0,last.lastIndexOf("-")+1));
                    callApi(last.substring(0,last.lastIndexOf("-")+1)+"01",last.substring(0,last.lastIndexOf("-")+1)+monthMaxDays);
                }
                else if(i==1){
                    Calendar cal = Calendar.getInstance();
                    Date date=new Date();
                    cal.setTime(date);
                    cal.add(Calendar.MONTH, 0);
                    cal.set(Calendar.DAY_OF_MONTH,0);
                    cal.add(Calendar.DATE, 0);

                    Calendar c = Calendar.getInstance();
                    int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    String last=CommonUtilsMethods.getCurrentDate();
                    String tt= String.valueOf(cal.getTime());
                    int month = c.get(Calendar.MONTH);
                    Log.v("previous_mnth",tt.substring(8,10)+" jkj "+Calendar.MONTH);
                    callApi(last.substring(0,last.indexOf("-")+1)+month+"-01",last.substring(0,last.indexOf("-")+1)+month+"-"+tt.substring(8,10));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                txt_clu.setText("Select FieldForce");
                txt_cat.setText("Select Type");
                txt_spec.setText("Select Territory");
                txt_qua.setText("Select Speciality");
                txt_cls.setText("Select Class");
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_mis_array.clear();
                filter_mis_array=filterThosField(mainArray,cluster_list,1);
                filter_mis_array=filterThosField(filter_mis_array,category_list,2);
                filter_mis_array=filterThosField(filter_mis_array,speciality_list,3);
                filter_mis_array=filterThosField(filter_mis_array,qualification_list,4);
                filter_mis_array=filterThosField(filter_mis_array,class_list,5);
                adpt=new AdapterDetailingReport(getActivity(),filter_mis_array);
                list.setAdapter(adpt);
                adpt.notifyDataSetChanged();
                drawer.closeDrawer(GravityCompat.END);
            }
        });
        lay_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                else {
                    drawer.openDrawer(GravityCompat.END);
                    //super.onBackPressed();
                }
            }
        });
        txt_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick(1);
            }
        });
        txt_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick(0);
            }
        });
        txt_clu.setText("Select FieldForce");
        txt_cat.setText("Select Type");
        txt_spec.setText("Select Territory");
        txt_qua.setText("Select Speciality");
        txt_cls.setText("Select Class");
        return vv;
    }

    private static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, +1);
        return calendar;
    }
    public void clearTheArraylist(ArrayList<PopFeed> sub_list){
        if (sub_list.contains(new PopFeed(true))) {
            for (int j = 0; j < sub_list.size(); j++) {
                PopFeed mm=sub_list.get(j);
                mm.setClick(false);
            }
        }
    }

    public ArrayList<DetailingOfReport> filterThosField(ArrayList<DetailingOfReport> original, ArrayList<PopFeed> sub_list, int x){
        ArrayList<DetailingOfReport> arr=new ArrayList<>();
        if (sub_list.contains(new PopFeed(true))) {
            for (int j = 0; j < sub_list.size(); j++) {
                PopFeed pp = sub_list.get(j);
                if (pp.isClick()) {
                    for (int i = 0; i < original.size(); i++) {
                        String values;
                        if(x==1)
                            values=original.get(i).getFieldperson();
                        else if(x==2)
                            values=original.get(i).getType();
                        else if(x==3)
                            values=original.get(i).getTerritory();
                        else if(x==4)
                            values=original.get(i).getSpec();
                        else
                            values=original.get(i).getCls();

                        if (values.equalsIgnoreCase(pp.getTxt()))
                            arr.add(new DetailingOfReport(original.get(i).getName(), original.get(i).getFieldperson(), original.get(i).getType(),
                                    original.get(i).getTerritory(), original.get(i).getSpec(), original.get(i).getCls(), original.get(i).getCount(),
                                    original.get(i).getTime(),original.get(i).getTotsec()));
                    }

                }
            }
            return arr;
        }
        else
            return original;

    }

    public void datePick(final int x){
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                int mnth=monthOfYear+1;
                if(x==0) {
                    txt_from.setText("From : " + dayOfMonth + "-" + mnth + "-" + year);
                    String dd=dayOfMonth + "-" + mnth + "-" + year;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        Date date = dateFormat.parse(dd);
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        fdt=sdf.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    txt_to.setText("To : " + dayOfMonth + "-" + mnth + "-" + year);
                    String dd=dayOfMonth + "-" + mnth + "-" + year;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        Date date = dateFormat.parse(dd);
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        tdt=sdf.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    callApi(fdt,tdt);
                }


           /* Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);*/
                // txt_fdate.setText(dateFormatter.format(newDate.getTime()));
                //fdate=txt_fdate.getText().toString();

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }

    public void loadingValueForGraph(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<PieEntry> entries1 = new ArrayList<>();
        for(int k=0;k<5;k++){
            DetailingOfReport mm=mainArray.get(k);
            entries.add(new PieEntry(Float.parseFloat(mm.getCount()),mm.getFieldperson(),
                    k));
            entries1.add(new PieEntry(Float.parseFloat(mm.getTotsec()),mm.getFieldperson(),
                    k));
        }
        /*entries.add(new PieEntry(50,"Ezhil",
                0));
        entries.add(new PieEntry(50,"Arasi",
                1));*/

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

        PieDataSet dataSet1 = new PieDataSet(entries1, "Election Results");
        Log.v("printing_entries_val12",entries.size()+" checking");
        dataSet1.setDrawIcons(false);

        dataSet1.setSliceSpace(3f);
        dataSet1.setIconsOffset(new MPPointF(0, 40));
        dataSet1.setSelectionShift(5f);
        Log.v("printing_entries_val",entries.size()+" checking");
        // add a lot of colors



        dataSet1.setColors(colors);
        //dataSet.setSelectionShift(0f);
        Log.v("printing_entries_val12",entries.size()+" checking");
        pie1.getDescription().setEnabled(false);
        pie1.getLegend().setEnabled(false);
        pie1.getDescription().setEnabled(false);
        pie1.getLegend().setEnabled(false);
        PieData data1 = new PieData(dataSet1);
        // data.setValueFormatter(new PercentFormatter(pieChart));
        data1.setValueTextSize(11f);
        data1.setValueTextColor(Color.WHITE);

        pie1.setData(data1);
        pie1.animateXY(1400, 1400);
        Log.v("printing_entries_val1",entries.size()+" checking");
        // undo all highlights
        pie1.highlightValues(null);
        pie1.notifyDataSetChanged();
        pie1.invalidate();
    }
    public void CustomShowDialog(final int x){
        final Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.popup_missed_selection);
        dialog.show();

        final ListView list_selection=(ListView)dialog.findViewById(R.id.list_selection);

        Button btn_done=(Button)dialog.findViewById(R.id.btn_done);
        Button dr_close=(Button)dialog.findViewById(R.id.dr_close);
        dr_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //commonFun();
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
                //commonFun();
            }
        });
        AdapterForSelectionList adpt;
        switch (x){
            case 1:
                adpt = new AdapterForSelectionList(getActivity(), cluster_list, "a");
                list_selection.setAdapter(adpt);
                adpt.notifyDataSetChanged();
                break;
            case 2:
                adpt = new AdapterForSelectionList(getActivity(), category_list, "a");
                list_selection.setAdapter(adpt);
                adpt.notifyDataSetChanged();
                break;

            case 3:
                adpt = new AdapterForSelectionList(getActivity(), speciality_list, "a");
                list_selection.setAdapter(adpt);
                adpt.notifyDataSetChanged();
                break;

            case 4:
                adpt = new AdapterForSelectionList(getActivity(), qualification_list, "a");
                list_selection.setAdapter(adpt);
                adpt.notifyDataSetChanged();
                break;

            case 5:
                adpt = new AdapterForSelectionList(getActivity(), class_list, "a");
                list_selection.setAdapter(adpt);
                adpt.notifyDataSetChanged();
                break;

        }


    }
    public void callApi(String fdt,String tdt){
        mainArray.clear();
        cluster_list.clear();
        category_list.clear();
        speciality_list.clear();
        qualification_list.clear();
        class_list.clear();
        clu="";
        cat="";
        spec="";
        qua="";
        cls="";
        txt_cls.setText("Select Class");
        txt_qua.setText("Select Qualification");
        txt_spec.setText("Select Speciality");
        txt_cat.setText("Select Category");
        txt_clu.setText("Select Cluster");
        JSONObject jj=new JSONObject();
        try{

            jj.put("SF",SFCode);
            jj.put("fdt", fdt);
            jj.put("tdt", tdt);
            Log.v("printing_report",jj.toString());
        }catch (Exception e){}

        Call<ResponseBody> upload=apiService.getDetailRep(jj.toString());
        upload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("uploading_scr", String.valueOf(response.isSuccessful()));
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
                        Log.v("printing_scribble",is.toString());
                        JSONArray json=new JSONArray(is.toString());
                        for(int i=0;i<json.length();i++){
                            JSONObject jjj=json.getJSONObject(i);
                            mainArray.add(new DetailingOfReport(jjj.getString("Trans_Detail_Name"),jjj.getString("SF_Name"),jjj.getString("Type"),jjj.getString("Territory"),
                                    jjj.getString("SpecName"),jjj.getString("CatName"),jjj.getString("DetCnt"),jjj.getString("DurMin")+" : "+jjj.getString("DurSec"),jjj.getString("TotSec")));
                            if(!clu.contains(jjj.getString("SF_Name")) && !TextUtils.isEmpty(jjj.getString("Trans_Detail_Name"))) {
                                cluster_list.add(new PopFeed(jjj.getString("SF_Name"), false, ""));
                                clu+=","+jjj.getString("SF_Name");
                            }
                            if(!cat.contains(jjj.getString("Type")) && !TextUtils.isEmpty(jjj.getString("Type"))) {
                                category_list.add(new PopFeed(jjj.getString("Type"), false, ""));
                                cat+=","+jjj.getString("Type");
                            }
                            if(!spec.contains(jjj.getString("Territory")) && !TextUtils.isEmpty(jjj.getString("Territory"))) {
                                speciality_list.add(new PopFeed(jjj.getString("Territory"), false, ""));
                                spec+=","+jjj.getString("Territory");
                            }
                            if(!qua.contains(jjj.getString("SpecName")) && !TextUtils.isEmpty(jjj.getString("SpecName"))) {
                                qualification_list.add(new PopFeed(jjj.getString("SpecName"), false, ""));
                                qua+=","+jjj.getString("SpecName");
                            }
                            Log.v("class_value_are",cls +" bool "+cls.contains(jjj.getString("CatName"))+" final "+jjj.getString("CatName"));
                            if((!cls.contains(jjj.getString("CatName"))) && (!TextUtils.isEmpty(jjj.getString("CatName")))) {
                                class_list.add(new PopFeed(jjj.getString("CatName"), false, ""));
                                cls+=","+jjj.getString("CatName");
                            }
                        }
                        adpt=new AdapterDetailingReport(getActivity(),mainArray);
                        list.setAdapter(adpt);
                        loadingValueForGraph();
                    }catch (Exception e){
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("printing_scribble","Exception"+t);
            }
        });
    }
}
