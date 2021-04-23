package saneforce.sanclm.Order_Report.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import saneforce.sanclm.api_Interface.AppConfig;

public class Target_Vs_Sales extends Fragment {

    ImageView imp_back, filter_btn,imp_back_filter,secondary_sel,custom_datecheck;
    CardView secondary_card,mtd_card,qtd_card,ytd_card;
    EditText fromdate_ed,todate_ed;
    Button clear,apply;
    PieChart pieChart;
    Spinner spinner;
    String div_code = "";
    Date formattedDate1;
    Date formattedDate2;
    RecyclerView recyclerView,secondarycycle;
    LinearLayout checklayout_date;
    Spinner division_splitting;
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
    String DataSF = "";
    sqlLite sqlLite1;
    String curval = null;
    Gson gson;
    List<CustomerMe> CustomerMeList;
    ArrayList<String> hq_list1 = new ArrayList<>();
    ArrayList<String> hq_list1id = new ArrayList<>();
    ArrayList<String> div_list1 = new ArrayList<>();
    ArrayList<String> div_list1id = new ArrayList<>();
    PieData pieData;
    PieDataSet pieDataSet;
    String fromstrdate = "";
    String tostrdate="";
    ArrayList<String> toback;
    Spinner divspinner;
    String divison_code="";
    DrawerLayout slidemenu;
    TextView mtdtext,qtdtext,ytdtext;
    String selected_data="";

    public Target_Vs_Sales() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_target__vs__sales, container, false);
//        toolbar111.setVisibility(GONE);
        imp_back = v.findViewById(R.id.back_btn_drpt);
        spinner=v.findViewById(R.id.report_spin1);
        recyclerView=v.findViewById(R.id.primaryDetails);
        fromdate=v.findViewById(R.id.current_date);
        todate=v.findViewById(R.id.to_date);
        pieChart=v.findViewById(R.id.piechart);
        sqlLite1 = new sqlLite(getActivity());
        filter_btn = v.findViewById(R.id.camp_filter);
//        divspinner = v.findViewById(R.id.divisionselection);
        slidemenu = (DrawerLayout) v.findViewById(R.id.drawer_layout);
        secondary_card = (CardView)v.findViewById(R.id.sec);
        mtd_card = (CardView)v.findViewById(R.id.mtd);
        qtd_card = (CardView)v.findViewById(R.id.qtd);
        ytd_card = (CardView)v.findViewById(R.id.ytd);
        mtdtext = (TextView)v.findViewById(R.id.mtd_txt);
        qtdtext = (TextView)v.findViewById(R.id.qtd_txt);
        ytdtext = (TextView)v.findViewById(R.id.ytd_text);
        fromdate_ed = (EditText)v.findViewById(R.id.fromdate_ed);
        todate_ed = (EditText)v.findViewById(R.id.todate_ed);
        division_splitting = (Spinner) v.findViewById(R.id.divisionselection_ed);
        clear = (Button)v.findViewById(R.id.filter_clear_1);
        apply = (Button)v.findViewById(R.id.filter_submit_1);
        //secondary_sel = (ImageView)v.findViewById(R.id.second_check);
        checklayout_date = (LinearLayout)v.findViewById(R.id.customcheck_layout);
        custom_datecheck = (ImageView)v.findViewById(R.id.custom_check);
        // primartext = (TextView)v.findViewById(R.id.primar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), slidemenu, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        slidemenu.setDrawerListener(toggle);
        toggle.syncState();
        targetdetails=new ArrayList<>();
        targetSdetails=new ArrayList<>();
        SalesPrimaryDetails=new ArrayList<>();
        SalesSecondaryDetails=new ArrayList<>();
        toback = new ArrayList<String>();
        values = new ArrayList<PieEntry>();

//        if (!Dcrdatas.from_date.equals("")) {
//            fromdate.setText(Dcrdatas.from_date);
//
//        }else {
        String date_n = new SimpleDateFormat("MMM, yyyy", Locale.getDefault()).format(new Date());
        fromdate.setText(date_n);
        Dcrdatas.date_fromdetails=fromdate.getText().toString();
//        }
//        if (!Dcrdatas.to_date.equals("")) {
//            fromdate.setText(Dcrdatas.to_date);
//        }else {
        todate.setText("Till Date");
        todate.setVisibility(View.VISIBLE);
        Dcrdatas.till_date=todate.getText().toString();
        Dcrdatas.date_todetails=todate.getText().toString();
//        }
        imp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dcrdatas.custlist = 3;
                getActivity().finish();
            }
        });

        imp_back_filter = v.findViewById(R.id.back_btn_filter);
        imp_back_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidemenu.closeDrawer(Gravity.RIGHT);

            }
        });
        fromdate_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                fromdate_ed.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                Dcrdatas.from_date=fromdate_ed.getText().toString();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        todate_ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromdate_ed.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Select From Date", Toast.LENGTH_LONG).show();
                } else {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    todate_ed.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    Dcrdatas.to_date=todate_ed.getText().toString();
                                    custom_datecheck.setImageResource(R.drawable.checkbox_marked26);
                                    custom_datecheck.setTag("check");
                                    mtd_card.setCardBackgroundColor(Color.WHITE);
                                    qtd_card.setCardBackgroundColor(Color.WHITE);
                                    ytd_card.setCardBackgroundColor(Color.WHITE);
                                    mtdtext.setTextColor(Color.RED);
                                    qtdtext.setTextColor(Color.RED);
                                    ytdtext.setTextColor(Color.RED);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

//        secondary_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String checktag = secondary_sel.getTag().toString();
//                if(checktag.equalsIgnoreCase("uncheck")){
//                    secondary_sel.setImageResource(R.drawable.checkbox_marked26);
//                    secondary_sel.setTag("check");
//                }else{
//                    secondary_sel.setImageResource(R.drawable.checkbox);
//                    secondary_sel.setTag("uncheck");
//                }
//            }
//        });

        checklayout_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checktag = custom_datecheck.getTag().toString();
                if(checktag.equalsIgnoreCase("uncheck")){
                    custom_datecheck.setImageResource(R.drawable.checkbox_marked26);
                    custom_datecheck.setTag("check");
                    mtd_card.setCardBackgroundColor(Color.WHITE);
                    qtd_card.setCardBackgroundColor(Color.WHITE);
                    ytd_card.setCardBackgroundColor(Color.WHITE);
                    mtdtext.setTextColor(Color.RED);
                    qtdtext.setTextColor(Color.RED);
                    ytdtext.setTextColor(Color.RED);
                }else{
                    custom_datecheck.setImageResource(R.drawable.checkbox);
                    custom_datecheck.setTag("uncheck");
                }
            }
        });

        mtd_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtd_card.setCardBackgroundColor(Color.RED);
                qtd_card.setCardBackgroundColor(Color.WHITE);
                ytd_card.setCardBackgroundColor(Color.WHITE);
                mtdtext.setTextColor(Color.WHITE);
                qtdtext.setTextColor(Color.RED);
                ytdtext.setTextColor(Color.RED);
                custom_datecheck.setImageResource(R.drawable.checkbox);
                custom_datecheck.setTag("uncheck");
                fromdate_ed.setText("");
                todate_ed.setText("");
                selected_data ="MTD";
            }
        });
        qtd_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtd_card.setCardBackgroundColor(Color.WHITE);
                qtd_card.setCardBackgroundColor(Color.RED);
                ytd_card.setCardBackgroundColor(Color.WHITE);
                mtdtext.setTextColor(Color.RED);
                qtdtext.setTextColor(Color.WHITE);
                ytdtext.setTextColor(Color.RED);
                custom_datecheck.setImageResource(R.drawable.checkbox);
                custom_datecheck.setTag("uncheck");
                fromdate_ed.setText("");
                todate_ed.setText("");
                selected_data ="QTD";
            }
        });
        ytd_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtd_card.setCardBackgroundColor(Color.WHITE);
                qtd_card.setCardBackgroundColor(Color.WHITE);
                ytd_card.setCardBackgroundColor(Color.RED);
                mtdtext.setTextColor(Color.RED);
                qtdtext.setTextColor(Color.RED);
                ytdtext.setTextColor(Color.WHITE);
                custom_datecheck.setImageResource(R.drawable.checkbox);
                custom_datecheck.setTag("uncheck");
                fromdate_ed.setText("");
                todate_ed.setText("");
                selected_data ="YTD";
            }
        });

        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopup(v);
                slidemenu.openDrawer(Gravity.RIGHT);
            }
        });
        String divname = getvalues11("division");
        if((divname.equals(""))||(divname.isEmpty())){
            division_splitting.setVisibility(View.INVISIBLE);
            MTDprimeproduct();
            Orderproduct_PiechartMTD();
        }else{
            division_splitting.setVisibility(View.VISIBLE);
            parseJsonData_div(divname);
            if(div_list1id.size()>1) {
                division_splitting.setSelection(1);
                divison_code = div_list1id.get(1);
            }else{
                division_splitting.setSelection(0);
                divison_code = div_list1id.get(0);
            }
        }
        Log.d("gang",divname);


        division_splitting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(div_list1.get(position).equalsIgnoreCase("All")){
                    divison_code="";
                    for (int i=0;i<div_list1id.size();i++){
                        if(i==0){
                            // divison_code = div_list1id.get(position);
                        }else{
                            divison_code = divison_code+div_list1id.get(i);
                        }

                    }
                }else{
                    divison_code = div_list1id.get(position);
                }


                Log.d("11111", "one");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        MTDprimeproduct();
        Orderproduct_PiechartMTD();
        mtd_card.setCardBackgroundColor(Color.RED);
        mtdtext.setTextColor(Color.WHITE);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(secondary_sel.getTag().equals("check")){
//                    getFragmentManager().beginTransaction().replace(R.id.DCRMain_Frame, new Target_Vs_Secondary()).commit();
                }
                else if(selected_data.equalsIgnoreCase("MTD")) {
                    MTDprimeproduct();
                    Orderproduct_PiechartMTD();
                    //  primartext.setText("Primary");
                }
                else if(selected_data.equalsIgnoreCase("QTD")) {
                    Orderproduct_PiechartMTD();
                    Orderproduct_QTDprimary();
                }
                else if(selected_data.equalsIgnoreCase("YTD")) {
                    Orderproduct_YTDprimary();
                    Orderproduct_PiechartMTD();
                }
                else if(custom_datecheck.getTag().toString().equalsIgnoreCase("check")){
                    if(fromdate_ed.getText().toString().equalsIgnoreCase("")||todate_ed.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(getActivity(),"Please Fill From date and To date",Toast.LENGTH_SHORT).show();
                    }else {
                        allproduct();
                        Orderproduct_Piechart();
                    }
                    selected_data="";
                }
                else{
                    Toast.makeText(getActivity(),"Please Select the options to filter",Toast.LENGTH_SHORT).show();
                }
                slidemenu.closeDrawer(Gravity.RIGHT);

            }
        });

        String mger_subordinate = getvalues("subordinate");
        if (mger_subordinate != null && !mger_subordinate.isEmpty() && !mger_subordinate.equals("null")) {
            Log.d("headquaters_master", mger_subordinate);
            parseJsonData_hq(mger_subordinate);
        } else {
            hq_list1.add("");
            hq_list1id.add("");
            DataSF = CustomerMeList.get(0).getSFCode();
        }

        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(LayoutManagerpoc);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        targetadt=new TargetAdapter_new(getActivity(),targetdetails,targetSdetails,todate,fromdate,Target_Vs_Sales.this);
//        targetsadt=new Targetadapter_newsecnd(getActivity(),targetdetails,targetSdetails,todate,fromdate);
        recyclerView.setAdapter(targetadt);
        // MTDprimeproduct();
        //secondaryproduct_detail();

//        LinearLayoutManager LayoutManagerpocs = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(LayoutManagerpocs);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        targetsadt=new Targetadapter_newsecnd(getActivity(),targetSdetails);
//        recyclerView.setAdapter(targetsadt);
//        secondaryproduct_detail();

//        ArrayList<PieEntry> values=new ArrayList<PieEntry>();
//        values.add(new PieEntry(35.34f,"Citmax Gold"));
//        values.add(new PieEntry(11.9f,"Thiolac"));
//        values.add(new PieEntry(9.78f,"Citmax D"));
//        values.add(new PieEntry(9.22f,"Win RD"));
//        values.add(new PieEntry(8.88f,"Tryclo D"));
//        values.add(new PieEntry(8.62f,"Phofol D"));
//        values.add(new PieEntry(7.45f,"Win HB"));
//        values.add(new PieEntry(4.64f,"Gawin Nt"));
//        values.add(new PieEntry(4.16f,"Protwin"));
//
//        ArrayList<String> xvals=new ArrayList<String>();
//        xvals.add("Citmax Gold");
//        xvals.add("Thiolac");
//        xvals.add("Citmax D");
//        xvals.add("Win RD");
//        xvals.add("Tryclo D");
//        xvals.add("Phofol D");
//        xvals.add("Win HB");
//        xvals.add("Gawin Nt");
//        xvals.add("Protwin");
//
//        pieDataSet=new PieDataSet(values,"");
//        pieData=new PieData(pieDataSet);
//        pieData.setValueFormatter(new PercentFormatter());
//        pieChart.setData(pieData);
//        pieChart.invalidate();
//        pieChart.setDrawHoleEnabled(false);
//        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        pieData.setValueTextSize(13f);
//        pieData.setValueTextColor(Color.WHITE);
//        pieChart.setDescription(null);
//        pieChart.animateXY(1400,1400);
//        pieChart.setUsePercentValues(false);
//
//        pieDataSet.setSliceSpace(1f);
//        pieDataSet.setValueTextSize(10f);
//        pieDataSet.setSelectionShift(10f);
//        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
//        pieDataSet.setValueLinePart1Length(0.5f);
//        pieDataSet.setValueLinePart2Length(0.5f);
//        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        pieDataSet.setValueTextColor(getResources().getColor(R.color.White));
//        pieChart.spin(500,0,-360f, Easing.EaseInOutQuad);
//        pieChart.setEntryLabelColor(Color.RED);
//        pieDataSet.setUsingSliceColorAsValueLineColor(true);
//        Legend legend = pieChart.getLegend();
//        legend.setTextColor(Color.GREEN);


//        Orderproduct_PiechartMTD();
//        secondaryproduct_detail();
        return v;
    }

//    public void showdivisionlist(){
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.showdivisionlistfilter, null);
//        dialogBuilder.setView(dialogView);
//        final RecyclerView divlists = dialogView.findViewById(R.id.divisionlist);
//
//        dialogBuilder.show();
//
//
//    }



    public String getvalues(String tempvalues) {
        Cursor cursor = sqlLite1.getAllMasterSyncData(tempvalues);
        String typevalue = null;
        if (cursor.moveToFirst()) {
            typevalue = cursor.getString(cursor.getColumnIndex("Master_Sync_Values"));
            Log.d("mas_worktype", tempvalues);
        }
        cursor.close();
//        if (typevalue != null && !typevalue.isEmpty() && !typevalue.equals("null"))
//            return null;
//         else
        return typevalue;
    }

    private void parseJsonData_hq(String jsonResponse) {
        Log.d("jsonResponse_hq", jsonResponse);
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            hq_list1.clear();
            hq_list1id.clear();
//            hq_list1.add("Select HeadQuater");
//            hq_list1id.add(Shared_Common_Pref.getsfcodeFromSP(getActivity()));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = jsonObject1.optString("id");
                String name = jsonObject1.optString("name");

                hq_list1.add(name);
                hq_list1id.add(id);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, hq_list1);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void allproduct(){
        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(LayoutManagerpoc);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        targetadt=new TargetAdapter_new(getActivity(),targetdetails,targetSdetails,todate,fromdate,Target_Vs_Sales.this);
        recyclerView.setAdapter(targetadt);
        Orderproduct_detail();
    }

    private void MTDprimeproduct(){
        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(LayoutManagerpoc);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        targetadt=new TargetAdapter_new(getActivity(),targetdetails,targetSdetails,todate,fromdate,Target_Vs_Sales.this);
        recyclerView.setAdapter(targetadt);
        Orderproduct_MTDprimary();
    }

    private void MTDsecondaryproduct(){
        LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(LayoutManagerpoc);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        targetadt=new TargetAdapter_new(getActivity(),targetdetails,targetSdetails,todate,fromdate,Target_Vs_Sales.this);
        recyclerView.setAdapter(targetadt);
        secondaryproduct_detail();
    }


    private void Orderproduct_detail() {
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(getActivity());
            String username = Shared_Common_Pref.getusernameFromSP(getActivity());
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

            SimpleDateFormat input = new SimpleDateFormat("dd-MM-yy");
            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = input.parse(fromstrdate);
                formattedDate2 = input.parse(tostrdate);// parse input
                fromdate.setText(output.format(formattedDate1));
                todate.setText(output.format(formattedDate2)); // format output
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ApiInterface apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
            Log.e(" order request","order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/target_sales_primary");
            if(division_splitting.getVisibility() == View.VISIBLE){
                QryParam.put("divisionCode", divison_code);
            }else{
                QryParam.put("divisionCode", div_code);
            }
            QryParam.put("sfCode", CustomerMeList.get(0).getSFCode());
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(getActivity()));
            QryParam.put("from_date", fromstrdate);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(getActivity()).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderDetail(response.body().toString());
                            secondaryproduct_detail11(fromstrdate,tostrdate);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Orderproduct_MTDprimary() {
        try {

            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(getActivity());
            String username = Shared_Common_Pref.getusernameFromSP(getActivity());
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
            String fromstrdate = "";
            String tostrdate="";

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date monthFirstDay = calendar.getTime();
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = calendar.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            final String startDateStr = dfs.format(monthFirstDay);
            final String endDateStr = dfs.format(monthLastDay);

            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                fromdate.setText(output.format(formattedDate1));// format output
                todate.setText("Till Date");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.e("DateFirstLast",startDateStr+" "+endDateStr);

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);

            ApiInterface apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
            Log.e(" order request","order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/target_sales_primary");
            if(division_splitting.getVisibility() == View.VISIBLE){
                QryParam.put("divisionCode", divison_code);
            }else{
                QryParam.put("divisionCode", div_code);
            }
            QryParam.put("sfCode", CustomerMeList.get(0).getSFCode());
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(getActivity()));
            QryParam.put("from_date", fromdate.getText().toString());
            if(todate.getText().toString().equalsIgnoreCase("Till Date")){
                QryParam.put("to_date", tostrdate);
            }else{
                QryParam.put("to_date", todate.getText().toString());
            }
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(getActivity()).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderDetail(response.body().toString());
                            secondaryproduct_detail11(startDateStr,endDateStr);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Orderproduct_YTDprimary() {
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(getActivity());
            String username = Shared_Common_Pref.getusernameFromSP(getActivity());
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
            String fromstrdate = "";
            String tostrdate="";
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MONTH, 0);
//            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//            Date monthFirstDay = calendar.getTime();
//            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//            Date monthLastDay = calendar.getTime();
//
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//            String startDateStr = dfs.format(monthFirstDay);
//            String endDateStr = dfs.format(monthLastDay);
//
//            Log.e("DateFirstLast",startDateStr+" "+endDateStr);

            Calendar cal = Calendar.getInstance();
            // cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, 03 );
            int monthee = Calendar.getInstance().get(Calendar.MONTH)+1;
            Log.d("check_month",""+monthee);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            if((monthee ==1)||(monthee==2)||(monthee==3)||(monthee==4)){
                cal.set(Calendar.YEAR, year-1);
            }else{
                cal.set(Calendar.YEAR, year);
            }
            Date monthFirstDay=cal.getTime();
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            final String startDateStr = dfs.format(monthFirstDay);

            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                fromdate.setText(output.format(formattedDate1));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);

            SimpleDateFormat output1 = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate2 = df.parse(tostrdate);                 // parse input
                todate.setText(output1.format(formattedDate2));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            ApiInterface apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
            Log.e(" order request","order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/target_sales_primary");
            if(division_splitting.getVisibility() == View.VISIBLE){
                QryParam.put("divisionCode", divison_code);
            }else{
                QryParam.put("divisionCode", div_code);
            }
            QryParam.put("sfCode", CustomerMeList.get(0).getSFCode());
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(getActivity()));
            QryParam.put("from_date", startDateStr);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(getActivity()).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderDetail(response.body().toString());
                            secondaryproduct_detail11(startDateStr,todate.getText().toString());
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Orderproduct_QTDprimary() {
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(getActivity());
            String username = Shared_Common_Pref.getusernameFromSP(getActivity());
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
            String fromstrdate = "";
            String tostrdate="";
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MONTH, 0);
//            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//            Date monthFirstDay = calendar.getTime();
//            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//            Date monthLastDay = calendar.getTime();
//
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//            String startDateStr = dfs.format(monthFirstDay);
//            String endDateStr = dfs.format(monthLastDay);
//
//            Log.e("DateFirstLast",startDateStr+" "+endDateStr);

            Calendar cal = Calendar.getInstance();
            // cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)/3 * 3);
            Date monthFirstDay=cal.getTime();
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//            String startDateStr = dfs.format(monthFirstDay);

            Calendar cals = Calendar.getInstance();
            //cal.setTime(date);
            cals.set(Calendar.DAY_OF_MONTH, 1);
            cals.set(Calendar.MONTH, cal.get(Calendar.MONTH)/3 * 3 + 2);
            cals.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay=cals.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            final String startDateStr = dfs.format(monthFirstDay);
            final String endDateStr = dfs.format(monthLastDay);


            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                formattedDate2=dfs.parse(endDateStr);
                fromdate.setText(output.format(formattedDate1));    // format output
                todate.setText(output.format(formattedDate2));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }
//            if (!Dcrdatas.to_date.equals("")){
//                tostrdate=Dcrdatas.to_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                tostrdate = df.format(c);
//            }

            ApiInterface apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
            Log.e(" order request","order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/target_sales_primary");
            if(division_splitting.getVisibility() == View.VISIBLE){
                QryParam.put("divisionCode", divison_code);
            }else{
                QryParam.put("divisionCode", div_code);
            }
            QryParam.put("sfCode", CustomerMeList.get(0).getSFCode());
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(getActivity()));
            QryParam.put("from_date", startDateStr);
            QryParam.put("to_date", endDateStr);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(getActivity()).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderDetail(response.body().toString());
                            secondaryproduct_detail11(startDateStr,endDateStr);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJsonDataorderDetail(String ordrdcr) {
        String date = "";
        String jsw="";
        try {
            try {
                JSONArray js = new JSONArray(ordrdcr);

                if (js.length() > 0) {
                    targetdetails.clear();
                    for (int i = 0; i < js.length(); i++) {

                        JSONObject jsonObject = js.getJSONObject(i);
                        // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        TargetPrimary_Class targetreport = new TargetPrimary_Class(
                                jsonObject.getString("sno"),
                                jsonObject.getString("sf_code"),
                                jsonObject.getString("sf_name"),
                                jsonObject.getString("hq"),
                                jsonObject.getString("sf_type"),
                                jsonObject.getString("SF_Cat_Code"),
                                jsonObject.getString("sf_TP_Active_Flag"),
                                jsonObject.getString("SF_VacantBlock"),
                                jsonObject.getString("Sl_Achieve"),
                                jsonObject.getString("Sl_growth"),
                                jsonObject.getString("Sl_Primary"),
                                jsonObject.getString("Sl_Target"),
                                jsonObject.getString("div_code"),
                                jsonObject.getString("Sl_PCPM"));
                        targetdetails.add(targetreport);
                        targetadt.notifyDataSetChanged();

                        for(int j = 0 ; j < targetdetails.size() ; j++){
                            Dcrdatas.primaryvalue= targetdetails.get(j).getSl_Primary();
                        }
                    }

                } else {
//                    bottom_sum.setVisibility(View.GONE);
                    targetdetails.clear();
                    targetadt.notifyDataSetChanged();
                    //targetsadt.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "No Records found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity() , "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(),v);
//        Toast.makeText(getActivity(), "showPopup = " + popup, Toast.LENGTH_SHORT).show();
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.reportfilter, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //   Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                if (item.getTitle().equals("ALL")) {
                    if (Dcrdatas.tagetsales_flag.equals("1")) {
                        MTDprimeproduct();
                        Orderproduct_Piechart();
                        // Toast.makeText(getActivity(), "aall", Toast.LENGTH_LONG).show();
                    }
                }else if (item.getTitle().equals("Secondary")){
//                    secondaryproduct();
//                    Orderproduct_PiechartSecondary();
//                    getFragmentManager().beginTransaction().replace(R.id.DCRMain_Frame, new Target_Vs_Secondary()).commit();
                }else if (item.getTitle().equals("MTD")){
                    MTDprimeproduct();
                    Orderproduct_PiechartMTD();
                    // MTDsecondproduct();
                    // Toast.makeText(getActivity(), "MTD", Toast.LENGTH_LONG).show();
                }else if (item.getTitle().equals("QTD")){
                    Orderproduct_PiechartMTD();
                    Orderproduct_QTDprimary();
                }
                else if (item.getTitle().equals("YTD")){
                    Orderproduct_YTDprimary();
                    Orderproduct_PiechartMTD();
                }
                else if (item.getTitle().equals("Custom Dates")){
                    datefilter();
                }
                return true;
            }
        });
        popup.show();
    }



    public void datefilter() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.targetfilter, null);
        dialogBuilder.setView(dialogView);
        final TextView textView=dialogView.findViewById(R.id.from);
        final TextView textView1=dialogView.findViewById(R.id.to);
        dialogBuilder.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Dcrdatas.from_date.equals("")) {
                    Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_LONG).show();
                } else if (Dcrdatas.to_date.equals("")) {
                    Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_LONG).show();
                } else {
//                    SimpleDateFormat input = new SimpleDateFormat("dd-MM-yy");
//                    SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
//                    try {
//                        formattedDate1 = input.parse(textView.getText().toString());
//                        formattedDate2 = input.parse(textView1.getText().toString());// parse input
//                        fromdate.setText(output.format(formattedDate1));
//                        todate.setText(output.format(formattedDate2)); // format output
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    fromdate.setText(Dcrdatas.from_date);
//                    todate.setText(Dcrdatas.to_date);
                    allproduct();
                    Orderproduct_Piechart();
                    // secondaryproduct();
                }
            }
        });
        dialogBuilder.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
        final AlertDialog alertDialog = dialogBuilder.create();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                textView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                Dcrdatas.from_date=textView.getText().toString();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textView.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Select From Date", Toast.LENGTH_LONG).show();
                } else {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    textView1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    Dcrdatas.to_date=textView1.getText().toString();

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });
        alertDialog.show();
    }

    private void Orderproduct_Piechart() {
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(getActivity());
            String username = Shared_Common_Pref.getusernameFromSP(getActivity());
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
            String fromstrdate = "";
            String tostrdate="";

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date monthFirstDay = calendar.getTime();
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = calendar.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);

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

            ApiInterface apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
            Log.e(" order request","order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/product_sales_primary");
            if(division_splitting.getVisibility() == View.VISIBLE){
                QryParam.put("divisionCode", divison_code);
            }else{
                QryParam.put("divisionCode", div_code);
            }
            QryParam.put("sfCode",CustomerMeList.get(0).getSFCode());
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(getActivity()));
//            QryParam.put("from_date", "2019-05-04");
//            QryParam.put("to_date", "2020-06-04");
            QryParam.put("from_date", fromstrdate);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(getActivity()).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataPieprimary(response.body().toString());
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Orderproduct_PiechartMTD() {
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(getActivity());
            String username = Shared_Common_Pref.getusernameFromSP(getActivity());
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
            String fromstrdate = "";
            String tostrdate="";

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date monthFirstDay = calendar.getTime();
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = calendar.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);

            if (!Dcrdatas.from_date.equals("")){
                //   fromstrdate= Dcrdatas.from_date;
                //fromdate.setText(fromstrdate);
            }else {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                fromstrdate = df.format(c);

            }
//            if (!Dcrdatas.to_date.equals("")){
//             //   tostrdate= Dcrdatas.to_date;
//                //todate.setText(tostrdate);
//            }else {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);

//            }

            ApiInterface apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
            Log.e(" order request","order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/product_sales_primary");
            if(division_splitting.getVisibility() == View.VISIBLE){
                QryParam.put("divisionCode", divison_code);
            }else{
                QryParam.put("divisionCode", div_code);
            }
            QryParam.put("sfCode",CustomerMeList.get(0).getSFCode());
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(getActivity()));
//            QryParam.put("from_date", "2019-05-04");
//            QryParam.put("to_date", "2020-06-04");
            QryParam.put("from_date", fromdate.getText().toString());
            if(todate.getText().toString().equalsIgnoreCase("Till Date")){
                QryParam.put("to_date", tostrdate);
            }else{
                QryParam.put("to_date", todate.getText().toString());
            }
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(getActivity()).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataPieprimary(response.body().toString());
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void parseJsonDataPieprimary(String monitor) {
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
                        targetadt.notifyDataSetChanged();

                        double sum = 0.0; //if you use version earlier than java-8
//double sum = IntStream.of(keyList).sum(); //if you are using java-8
                        for(int j = 0 ; j < SalesPrimaryDetails.size() ; j++){
                            sum += SalesPrimaryDetails.get(j).getCnt();
                        }
                        for(int k = 0 ; k < SalesPrimaryDetails.size() ; k++){
                            System.out.println((SalesPrimaryDetails.get(k).getCnt()/sum )*100 + "%");

                        }

                        ArrayList<PieEntry> values=new ArrayList<PieEntry>();
//                        values.add(new PieEntry(35.34f,"Citmax Gold"));
//                        values.add(new PieEntry(11.9f,"Thiolac"));
//                        values.add(new PieEntry(9.78f,"Citmax D"));
//                        values.add(new PieEntry(9.22f,"Win RD"));
//                        values.add(new PieEntry(8.88f,"Tryclo D"));
//                        values.add(new PieEntry(8.62f,"Phofol D"));
//                        values.add(new PieEntry(7.45f,"Win HB"));
//                        values.add(new PieEntry(4.64f,"Gawin Nt"));
//                        values.add(new PieEntry(4.16f,"Protwin"));

                        for(int n = 0;n<SalesPrimaryDetails.size();n++) {
                            double vuu=SalesPrimaryDetails.get(n).getCnt();
                            String name=SalesPrimaryDetails.get(n).getProd_name();
                            float soldPercentage = ( float ) (vuu * 100 / sum);
                            PieEntry pieEntry = new PieEntry(soldPercentage, name);
                            values.add(pieEntry);
                        }

                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.rgb(213, 0, 0));
                        colors.add(Color.rgb(24, 255, 255));
                        colors.add(Color.rgb(255, 195, 0 ));
                        colors.add(Color.rgb(0, 200, 83));
                        colors.add(Color.rgb(62, 39, 35));
                        colors.add(Color.rgb(255, 87, 51));
                        colors.add(Color.rgb(66, 165, 245));
                        colors.add(Color.rgb(124, 179, 66));
                        colors.add(Color.rgb(13, 71, 161));


                        pieDataSet=new PieDataSet(values,"");
                        pieData=new PieData(pieDataSet);
                        pieData.setValueFormatter(new PercentFormatter());
                        pieChart.setData(pieData);
                        pieChart.invalidate();
                        pieChart.setDrawHoleEnabled(false);
                        pieDataSet.setColors(colors);
                        pieData.setValueTextSize(13f);
                        pieData.setValueTextColor(Color.WHITE);
                        pieChart.setDescription(null);
                        pieChart.animateXY(1400,1400);
                        pieChart.setUsePercentValues(true);

                        pieDataSet.setSliceSpace(1.5f);
                        pieDataSet.setValueTextSize(10f);
                        pieDataSet.setSelectionShift(10f);
                        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
                        pieDataSet.setValueLinePart1Length(0.5f);
                        pieDataSet.setValueLinePart2Length(0.5f);
                        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
                        pieChart.spin(500,0,-360f, Easing.EaseInOutQuad);
                        pieChart.setEntryLabelColor(Color.RED);
                        pieChart.setEntryLabelTextSize(10f);
                        pieDataSet.setUsingSliceColorAsValueLineColor(true);
                        Legend legend = pieChart.getLegend();
                        legend.setEnabled(false);
//                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//                        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//                        legend.setDrawInside(false);
//                        legend.setXEntrySpace(4f);
//                        legend.setYEntrySpace(0f);
//                        legend.setWordWrapEnabled(true);
                        pieChart.setElevation(10);


                    }
                } else {
//                    bottom_sum.setVisibility(View.GONE);
                    SalesPrimaryDetails.clear();
                    targetadt.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "No Records found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Orderproduct_PiechartSecondary() {
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(getActivity());
            String username = Shared_Common_Pref.getusernameFromSP(getActivity());
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
            String fromstrdate = "";
            String tostrdate = "";

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

            ApiInterface apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
            Log.e(" order request", "order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/product_sales_secondary");
            QryParam.put("divisionCode", div_code);
            QryParam.put("sfCode", CustomerMeList.get(0).getSFCode());
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(getActivity()));
//            QryParam.put("from_date", "2019-05-04");
//            QryParam.put("to_date", "2020-06-04");
            QryParam.put("from_date", fromdate.getText().toString());
            if(todate.getText().toString().equalsIgnoreCase("Till Date")){
                QryParam.put("to_date", tostrdate);
            }else{
                QryParam.put("to_date", todate.getText().toString());
            }
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(getActivity()).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataPieSecondary(response.body().toString());
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void parseJsonDataPieSecondary(String monitor) {
        String date = "";
        try {
            try {
                JSONArray js = new JSONArray(monitor);
                Log.e("PieArray", js.toString());
                if (js.length() > 0) {
                    SalesSecondaryDetails.clear();
                    for (int i = 0; i < js.length(); i++) {

                        JSONObject jsonObject = js.getJSONObject(i);
                        cnt = jsonObject.getDouble("cnt");
                        prod = jsonObject.getString("prod_name");
//                        secondarysales_class secondarysales_class = new secondarysales_class(cnt, prod);
//                        SalesSecondaryDetails.add(secondarysales_class);
                        // targetadt.notifyDataSetChanged();

                        double sum = 0.0; //if you use version earlier than java-8
//double sum = IntStream.of(keyList).sum(); //if you are using java-8
                        for (int j = 0; j < SalesSecondaryDetails.size(); j++) {
                            sum += SalesSecondaryDetails.get(j).getCnt();
                        }
                        for (int k = 0; k < SalesSecondaryDetails.size(); k++) {
                            System.out.println((SalesSecondaryDetails.get(k).getCnt() / sum) * 100 + "%");

                        }


                        ArrayList<PieEntry> values = new ArrayList<PieEntry>();
//                        values.add(new PieEntry(35.34f,"Citmax Gold"));
//                        values.add(new PieEntry(11.9f,"Thiolac"));
//                        values.add(new PieEntry(9.78f,"Citmax D"));
//                        values.add(new PieEntry(9.22f,"Win RD"));
//                        values.add(new PieEntry(8.88f,"Tryclo D"));
//                        values.add(new PieEntry(8.62f,"Phofol D"));
//                        values.add(new PieEntry(7.45f,"Win HB"));
//                        values.add(new PieEntry(4.64f,"Gawin Nt"));
//                        values.add(new PieEntry(4.16f,"Protwin"));

                        for (int n = 0; n < SalesSecondaryDetails.size(); n++) {
                            double vuu = SalesSecondaryDetails.get(n).getCnt();
                            String name1 = SalesSecondaryDetails.get(n).getProd_name();
                            float soldPercentage = ( float ) (vuu * 100 / sum);
                            PieEntry pieEntry = new PieEntry(soldPercentage, name1);
                            values.add(pieEntry);
                        }

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
                        colors.add(Color.rgb(0, 0, 128));
                        colors.add(Color.rgb(213, 0, 0));
                        colors.add(Color.rgb(184, 134, 11));
                        colors.add(Color.rgb(255, 69, 0));
                        colors.add(Color.rgb(30, 144, 255));
                        colors.add(Color.rgb(0, 100, 0));
                        colors.add(Color.rgb(255, 140, 0));
                        colors.add(Color.rgb(183, 176, 253));
                        colors.add(Color.rgb(139, 69, 19));

                        pieDataSet = new PieDataSet(values, "");
                        pieData = new PieData(pieDataSet);
                        pieData.setValueFormatter(new PercentFormatter());
                        pieChart.setData(pieData);
                        pieChart.setDrawHoleEnabled(false);
                        pieDataSet.setColors(colors);
                        pieData.setValueTextSize(13f);
                        pieData.setValueTextColor(Color.WHITE);
                        pieChart.setDescription(null);
                        pieChart.animateXY(1400, 1400);
                        pieChart.setUsePercentValues(true);

                        pieDataSet.setSliceSpace(1.5f);
                        pieDataSet.setValueTextSize(10f);
                        pieDataSet.setSelectionShift(10f);
                        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
                        pieDataSet.setValueLinePart1Length(0.5f);
                        pieDataSet.setValueLinePart2Length(0.5f);
                        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
                        pieChart.spin(500, 0, -360f, Easing.EaseInOutQuad);
                        pieChart.setEntryLabelColor(Color.RED);
                        pieChart.setEntryLabelTextSize(10f);
                        pieDataSet.setUsingSliceColorAsValueLineColor(true);
                        Legend legend = pieChart.getLegend();
                        legend.setEnabled(false);
//                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//                        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//                        legend.setDrawInside(false);
//                        legend.setXEntrySpace(4f);
//                        legend.setYEntrySpace(0f);
//                        legend.setWordWrapEnabled(true);
                        pieChart.setElevation(10);
                        pieChart.invalidate();


                    }
                } else {
//                    bottom_sum.setVisibility(View.GONE);
                    SalesSecondaryDetails.clear();
                    //targetadt.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "No Records found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void secondaryproduct_detail() {
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(getActivity());
            String username = Shared_Common_Pref.getusernameFromSP(getActivity());
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
            String fromstrdate = "";
            String tostrdate="";

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate= Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }
//            if (!Dcrdatas.to_date.equals("")){
//                tostrdate= Dcrdatas.to_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                tostrdate = df.format(c);
//            }


            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date monthFirstDay = calendar.getTime();
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthLastDay = calendar.getTime();

            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = dfs.format(monthFirstDay);
            String endDateStr = dfs.format(monthLastDay);

            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
            try {
                formattedDate1 = dfs.parse(startDateStr);                 // parse input
                fromdate.setText(output.format(formattedDate1));// format output
                todate.setText("Till Date");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.e("DateFirstLast",startDateStr+" "+endDateStr);
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);
//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }

            Date c1 = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df1.format(c);

            ApiInterface apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
            Log.e(" order request","order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/target_sales_secondary");
            if(division_splitting.getVisibility() == View.VISIBLE){
                QryParam.put("divisionCode", divison_code);
            }else{
                QryParam.put("divisionCode", div_code);
            }
            QryParam.put("sfCode", CustomerMeList.get(0).getSFCode());
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(getActivity()));
            QryParam.put("from_date", fromdate.getText().toString());
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(getActivity()).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDatasecondDetail11(response.body().toString());
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void secondaryproduct_detail11(String fromdate1,String todate1) {
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(getActivity());
            String username = Shared_Common_Pref.getusernameFromSP(getActivity());
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
            String fromstrdate = fromdate1;
            String tostrdate=todate1;

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate= Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }
//            if (!Dcrdatas.to_date.equals("")){
//                tostrdate= Dcrdatas.to_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                tostrdate = df.format(c);
//            }

            ApiInterface apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
            Log.e(" order request","order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/target_sales_secondary");
            if(division_splitting.getVisibility() == View.VISIBLE){
                QryParam.put("divisionCode", divison_code);
            }else{
                QryParam.put("divisionCode", div_code);
            }
            QryParam.put("sfCode", CustomerMeList.get(0).getSFCode());
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(getActivity()));
            QryParam.put("from_date", fromstrdate);
            QryParam.put("to_date", tostrdate);
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(getActivity()).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDatasecondDetail11(response.body().toString());
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJsonDatasecondDetail(String ordrdcr) {
        String date = "";
        String jsw="";
        try {
            try {
                JSONArray js = new JSONArray(ordrdcr);

                if (js.length() > 0) {
                    targetSdetails.clear();
                    for (int i = 0; i < js.length(); i++) {

                        JSONObject jsonObject = js.getJSONObject(i);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        TargetSecondary_class targetreport1 = new TargetSecondary_class(
                                jsonObject.getString("sno"),
                                jsonObject.getString("sf_code"),
                                jsonObject.getString("sf_name"),
                                jsonObject.getString("hq"),
                                jsonObject.getString("sf_type"),
                                jsonObject.getString("SF_Cat_Code"),
                                jsonObject.getString("sf_TP_Active_Flag"),
                                jsonObject.getString("SF_VacantBlock"),
                                jsonObject.getString("Sl_Achieve"),
                                jsonObject.getString("Sl_growth"),
                                jsonObject.getString("Sl_Primary"),
                                jsonObject.getString("Sl_Target"));
                        targetSdetails.add(targetreport1);
                        targetsadt.notifyDataSetChanged();

                        for(int j = 0 ; j < targetSdetails.size() ; j++){
                            Dcrdatas.secondaryvalue= targetSdetails.get(j).getSl_Secondary();
                        }

                    }

                } else {
//                    bottom_sum.setVisibility(View.GONE);
                    targetSdetails.clear();
                    targetsadt.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "No Records found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity() , "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJsonDatasecondDetail11(String ordrdcr) {
        String date = "";
        String jsw="";
        try {
            try {
                JSONArray js = new JSONArray(ordrdcr);

                if (js.length() > 0) {
                    targetSdetails.clear();
                    for (int i = 0; i < js.length(); i++) {

                        JSONObject jsonObject = js.getJSONObject(i);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        TargetSecondary_class targetreport1 = new TargetSecondary_class(
                                jsonObject.getString("sno"),
                                jsonObject.getString("sf_code"),
                                jsonObject.getString("sf_name"),
                                jsonObject.getString("hq"),
                                jsonObject.getString("sf_type"),
                                jsonObject.getString("SF_Cat_Code"),
                                jsonObject.getString("sf_TP_Active_Flag"),
                                jsonObject.getString("SF_VacantBlock"),
                                jsonObject.getString("Sl_Achieve"),
                                jsonObject.getString("Sl_growth"),
                                jsonObject.getString("Sl_Primary"),
                                jsonObject.getString("Sl_Target"));
                        targetSdetails.add(targetreport1);
                        // targetsadt.notifyDataSetChanged();

                        for(int j = 0 ; j < targetSdetails.size() ; j++){
                            Dcrdatas.secondaryvalue= targetSdetails.get(j).getSl_Secondary();
                        }

                    }
                    Log.d("dsize",""+targetSdetails.size());
                    //  targetSdetails.remove(0);
                    Log.d("dsize",""+targetSdetails.size());

                } else {
//                    bottom_sum.setVisibility(View.GONE);
                    targetSdetails.clear();
                    // targetsadt.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "No Records found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity() , "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public  void getsubdata(String sf_name){

        String selsfcode = sf_name;
        sqlLite sqlLite;
        String curval = null;
        List<CustomerMe> CustomerMeList;
        sqlLite = new sqlLite(getActivity());
        String username = Shared_Common_Pref.getusernameFromSP(getActivity());
        Cursor cursor = sqlLite.getAllLoginData();

        if (cursor.moveToFirst()) {
            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
        }
        cursor.close();
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomerMe>>() {
        }.getType();
        CustomerMeList = gson.fromJson(curval, type);
        if(CustomerMeList.get(0).getSFCode().equalsIgnoreCase(selsfcode)){

        }else{
            LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(LayoutManagerpoc);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            targetadt=new TargetAdapter_new(getActivity(),targetdetails,targetSdetails,todate,fromdate,Target_Vs_Sales.this);
            recyclerView.setAdapter(targetadt);
            Orderproduct_MTDprimary1(selsfcode);
        }

    }
    private void Orderproduct_MTDprimary1(String selsf_code) {
        try {
            String selsfcode1 = selsf_code;
            toback.add(selsf_code);
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(getActivity());
            String username = Shared_Common_Pref.getusernameFromSP(getActivity());
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
//            String fromstrdate = "";
//            String tostrdate="";
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MONTH, 0);
//            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//            Date monthFirstDay = calendar.getTime();
//            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//            Date monthLastDay = calendar.getTime();
//
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//            String startDateStr = dfs.format(monthFirstDay);
//            String endDateStr = dfs.format(monthLastDay);
//
//            SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
//            try {
//                formattedDate1 = dfs.parse(startDateStr);                 // parse input
//                fromdate.setText(output.format(formattedDate1));// format output
//                todate.setText("Till Date");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

            //   Log.e("DateFirstLast",startDateStr+" "+endDateStr);

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);

            ApiInterface apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
            Log.e(" order request","order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/target_sales_primary");
            if(division_splitting.getVisibility() == View.VISIBLE){
                QryParam.put("divisionCode", divison_code);
            }else{
                QryParam.put("divisionCode", div_code);
            }
            QryParam.put("sfCode", selsfcode1);
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(getActivity()));
            QryParam.put("from_date", fromdate.getText().toString());
            if(todate.getText().toString().equalsIgnoreCase("Till Date")){
                QryParam.put("to_date", tostrdate);
            }else{
                QryParam.put("to_date", todate.getText().toString());
            }
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(getActivity()).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderDetail(response.body().toString());
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public  void getbackdata(String codeit){
        String selsfcode = codeit;
        sqlLite sqlLite;
        String curval = null;
        List<CustomerMe> CustomerMeList;
        sqlLite = new sqlLite(getActivity());
        String username = Shared_Common_Pref.getusernameFromSP(getActivity());
        Cursor cursor = sqlLite.getAllLoginData();

        if (cursor.moveToFirst()) {
            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
        }
        cursor.close();
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomerMe>>() {
        }.getType();
        CustomerMeList = gson.fromJson(curval, type);
        if(CustomerMeList.get(0).getSFCode().equalsIgnoreCase(selsfcode)){

        }else {
            LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(LayoutManagerpoc);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            targetadt = new TargetAdapter_new(getActivity(), targetdetails, targetSdetails, todate, fromdate, Target_Vs_Sales.this);
            recyclerView.setAdapter(targetadt);
            Orderproduct_MTDprimary2();
        }
    }

    private void Orderproduct_MTDprimary2() {
        try {

//            if(toback.size() ==0){
//
//            }else{
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(getActivity());
            String username = Shared_Common_Pref.getusernameFromSP(getActivity());
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
            String selsfcode1 = "";
            if((toback.size())!=0){

                selsfcode1 = toback.get(toback.size()-1);
                toback.remove(toback.size()-1);



            }else{
                selsfcode1 = CustomerMeList.get(0).getSFCode();
            }


            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
            } else {
                div_code = CustomerMeList.get(0).getDivisionCode();
            }
//                String fromstrdate = "";
//                String tostrdate="";
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.add(Calendar.MONTH, 0);
//                calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//                Date monthFirstDay = calendar.getTime();
//                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//                Date monthLastDay = calendar.getTime();
//
//                SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
//                String startDateStr = dfs.format(monthFirstDay);
//                String endDateStr = dfs.format(monthLastDay);
//
//                SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
//                try {
//                    formattedDate1 = dfs.parse(startDateStr);                 // parse input
//                    fromdate.setText(output.format(formattedDate1));// format output
//                    todate.setText("Till Date");
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                Log.e("DateFirstLast",startDateStr+" "+endDateStr);

//            if (!Dcrdatas.from_date.equals("")){
//                fromstrdate=Dcrdatas.from_date;
//            }else {
//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                fromstrdate = df.format(c);
//            }

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tostrdate = df.format(c);

            ApiInterface apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
            Log.e(" order request","order Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/target_sales_primary");
            if(division_splitting.getVisibility() == View.VISIBLE){
                QryParam.put("divisionCode", divison_code);
            }else{
                QryParam.put("divisionCode", div_code);
            }
            QryParam.put("sfCode", selsfcode1);
            QryParam.put("rSF", Shared_Common_Pref.getsfcodeFromSP(getActivity()));
            QryParam.put("from_date", fromdate.getText().toString());
            if(todate.getText().toString().equalsIgnoreCase("Till Date")){
                QryParam.put("to_date", tostrdate);
            }else{
                QryParam.put("to_date", todate.getText().toString());
            }
            // QryParam.put("to_date", todate.getText().toString());
            Log.e("orderreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(AppConfig.getInstance(getActivity()).getBaseurl(), QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderDetail(response.body().toString());
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
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
            //  }

            // String selsfcode1 = selsf_code;
            // toback.add(selsf_code);

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public String getvalues11(String tempvalues) {
        Cursor cursor = sqlLite1.getAllMasterSyncData(tempvalues);
        String typevalue = null;
        if (cursor.moveToFirst()) {
            typevalue = cursor.getString(cursor.getColumnIndex("Master_Sync_Values"));
            Log.d("Values12345", tempvalues + "  " + typevalue);
        }
        cursor.close();
        return typevalue;
    }
    private void parseJsonData_div(String jsonResponse) {
        Log.d("jsonResponse_hq", jsonResponse);
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            div_list1.clear();
            div_list1id.clear();
//            hq_list1.add("Select HeadQuater");
//            hq_list1id.add(Shared_Common_Pref.getsfcodeFromSP(getActivity()));
            div_list1.add("All");
            div_list1id.add("0");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = jsonObject1.optString("division_code");
                String name = jsonObject1.optString("division_name");

                div_list1.add(name);
                div_list1id.add(id);
            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, div_list1);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            division_splitting.setAdapter(adapter1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
