package saneforce.sanclm.SFE_report.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.Common_Class.Shared_Common_Pref;
import saneforce.sanclm.Common_Class.sqlLite;
import saneforce.sanclm.R;
import saneforce.sanclm.SFE_report.Adapter.ALL_DivisionAdapter;
import saneforce.sanclm.SFE_report.Adapter.Division_Adapter;
import saneforce.sanclm.SFE_report.Adapter.Multicheckadapter;
import saneforce.sanclm.SFE_report.ModelClass.Categoryclass;
import saneforce.sanclm.SFE_report.ModelClass.Multicheckclass;
import saneforce.sanclm.SFE_report.ModelClass.OnCampaignClicklistener;
import saneforce.sanclm.SFE_report.ModelClass.divisionclass;
import saneforce.sanclm.SFE_report.ModelClass.hqclass;
import saneforce.sanclm.SFE_report.ModelClass.specialityclass;
import saneforce.sanclm.User_login.CustomerMe;
import saneforce.sanclm.api_Interface.ApiClient;
import saneforce.sanclm.api_Interface.ApiInterface;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.AppConfig;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import java.util.Locale;
import java.util.Map;

public class SFE_Activtity extends AppCompatActivity {

    private BarChart chart;
    float groupSpace = 0f;
    float barSpace = 0.1f;
    float barWidth = 0.16f;
    ImageView ic_back,imp_back_filter,filter_btn;
    DrawerLayout slidemenu;
    TextView monthselection,yearselection,catogoryselection,specalityselection;
    CardView Catgory_card,specality_card,speclcard;
    TextView cattxt,spectxt,txtspin,backcat,catog,brandtxt;
    LinearLayout special_selection;
    String selected_data = "";
    Dialog monthdialog;
    NumberPicker monthpicker;
    NumberPicker Yearpicker;
    Button clear, apply;
    TextView cur_month;
    String div_code = "";
    RecyclerView recyclerView;
    ArrayList<String> div_list1 = new ArrayList<>();
    ArrayList<String> div_list1id = new ArrayList<>();
    ArrayList<String> spcl_list = new ArrayList<>();
    ArrayList<String> spcl_listid = new ArrayList<>();
    ArrayList<Multicheckclass> spcl_list1 = new ArrayList<>();
    ArrayList<Multicheckclass> spcl_list1id = new ArrayList<>();
    ArrayList<Multicheckclass> spcl_list_temp = new ArrayList<>();
    ArrayList<String> spcl_div = new ArrayList<String>();
    public ArrayList<Multicheckclass> listSelected = new ArrayList<>();
    ArrayList<divisionclass> divnclass=new ArrayList<>();
    ArrayList<Categoryclass> catyclass=new ArrayList<>();
    ArrayList<specialityclass> spclclass=new ArrayList<>();
    ArrayList<hqclass> hqclas=new ArrayList<>();
    ALL_DivisionAdapter divisionAdapter;
    Division_Adapter divisnAdapter;
    Multicheckadapter muladapter;
    ArrayList<String> toback = new ArrayList<>();
    List<CustomerMe> CustomerMeList;
    LinearLayout linear_bottom;
    String strspcl="",strspclid="";
    String selectedUsers = "", selctectedId = "";
    boolean isSelectAll = false;
    int checkin = 1;
    int k=0;

    CommonSharedPreference mCommonSharedPreference;
    String sf_code,div_Code,db_connPath,sf_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_f_e_);

        mCommonSharedPreference=new CommonSharedPreference(this);
        sf_name=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);


        ic_back=findViewById(R.id.back_btn);
        monthselection=findViewById(R.id.select_month);
        yearselection=findViewById(R.id.select_year);
        specality_card = ( CardView ) findViewById(R.id.ytd);
        Catgory_card = ( CardView ) findViewById(R.id.qtd);
        speclcard=(CardView ) findViewById(R.id.specl_class);
        cattxt = (TextView)findViewById(R.id.cat_txt);
        spectxt = (TextView)findViewById(R.id.spec_text);
        chart = findViewById(R.id.barchart);
        clear = ( Button ) findViewById(R.id.filter_clear_1);
        apply = ( Button ) findViewById(R.id.filter_submit_1);
        slidemenu = ( DrawerLayout ) findViewById(R.id.drawer_layout);
        cur_month=findViewById(R.id.current_date);
        filter_btn = findViewById(R.id.camp_filter);
        recyclerView = findViewById(R.id.catogory_details);
        special_selection=findViewById(R.id.LV_spinner);
        txtspin=findViewById(R.id.divisionselection_ed);
        backcat=findViewById(R.id.back_cat);
        catog=findViewById(R.id.catog);
        backcat.setVisibility(View.GONE);
        linear_bottom=findViewById(R.id.linear_doc_bottomdcr);
        brandtxt=findViewById(R.id.brand_txt);
        //linear_bottom.setVisibility(View.GONE);
//        barWidth = 0.2f;
//        barSpace = 0.1f;
//        groupSpace = 0.1f;
//        toback.clear();
//        toback.add("Admin");
        catog.setText("Category");
        selected_data="Category";
        listSelected.clear();
        Dcrdatas.select_data=selected_data;
        Log.d("toback",toback.toString());

        ActionBarDrawerToggle toggle1 = new ActionBarDrawerToggle(
                SFE_Activtity.this, slidemenu, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        slidemenu.setDrawerListener(toggle1);
        toggle1.syncState();

        final Calendar c = Calendar.getInstance();
        final int y = c.get(Calendar.YEAR);
        final int m = c.get(Calendar.MONTH)+1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        DateFormat dateFormat3 = new SimpleDateFormat("MMM");
        Date date3 = new Date();
        Log.d("Month",dateFormat3.format(date3));
        monthselection.setText("Month: "+dateFormat3.format((date3)));

        DateFormat dateFormat1 = new SimpleDateFormat("M");
        Date date1 = new Date();
        Log.d("Month",dateFormat1.format(date1));
        Dcrdatas.select_month=dateFormat1.format((date1));

        DateFormat dateFormat2 = new SimpleDateFormat("yyyy");
        Date date2 = new Date();
        Log.d("Month",dateFormat2.format(date2));
        yearselection.setText("Year: "+dateFormat2.format(date2));
        Dcrdatas.select_year=dateFormat2.format((date2));

        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        Date date = new Date();
        Log.d("Month",dateFormat.format(date));
        if (Dcrdatas.select_month.equals("")){
            cur_month.setText(dateFormat.format(date));
        }else {
            formatDate1(Dcrdatas.select_month);
        }

        Catgory_card.setCardBackgroundColor(Color.RED);
        cattxt.setTextColor(Color.WHITE);


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

        monthselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SFE_Activtity.this);
                LayoutInflater factory = LayoutInflater.from(SFE_Activtity.this);
                final View vieww = factory.inflate(R.layout.customdialoglayout, null);
                alertDialog.setView(vieww);
                monthpicker=vieww.findViewById(R.id.picker_month);
                Yearpicker = vieww.findViewById(R.id.picker_year);
                Yearpicker.setVisibility(View.GONE);
                monthpicker.setMaxValue(12);
                monthpicker.setMinValue(1);
                monthpicker.setDisplayedValues( new String[] {"Jan", "Feb", "Mar", "April", "May","June","July","Aug","Sep","Oct","Nov","Dec"} );
                Yearpicker.setMinValue(2000);
                Yearpicker.setMaxValue(2500);
                monthpicker.setValue(m);
                Yearpicker.setValue(y);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String months= String.valueOf(monthpicker.getValue());
                        String years = String.valueOf(Yearpicker.getValue());
                        Dcrdatas.select_month = months;
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

        yearselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!monthselection.getText().toString().equals("")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SFE_Activtity.this);
                    LayoutInflater factory = LayoutInflater.from(SFE_Activtity.this);
                    final View vieww = factory.inflate(R.layout.customdialoglayout, null);
                    alertDialog.setView(vieww);
                    monthpicker=vieww.findViewById(R.id.picker_month);
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
                            String months= String.valueOf(monthpicker.getValue());
                            String years = String.valueOf(Yearpicker.getValue());
                            Dcrdatas.select_year=years;
                            yearselection.setText("Year: "+years);
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }else {
                    Toast.makeText(getApplicationContext(),"Select Month",Toast.LENGTH_LONG).show();
                }

            }
        });
        special_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specialitydata();

            }
        });

        Catgory_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specality_card.setCardBackgroundColor(Color.WHITE);
                spectxt.setTextColor(Color.RED);
                Catgory_card.setCardBackgroundColor(Color.RED);
                cattxt.setTextColor(Color.WHITE);
                selected_data="Category";
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
                selected_data="Speciality";
                speclcard.setVisibility(View.VISIBLE);
            }
        });


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chart.clear();
                Dcrdatas.select_data=selected_data;
                Log.d("rateess",selected_data+"----"+Dcrdatas.select_data);

                if (selected_data.equalsIgnoreCase("Category")) {
                    catog.setText("Category");
                    if (monthselection.getText().toString().equals("") && yearselection.getText().toString().equals("")){
                        Intent oo = new Intent(SFE_Activtity.this, SFE_Activtity.class);
                        startActivity(oo);
                    }else {
//                        if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
//                            if(toback.size()==1){
//                                CategoryHq_detail(Dcrdatas.selected_division);
//                                getsubdata(Dcrdatas.selected_division);
//                            }else if (toback.size()==0) {
//                                divisiondata();
//                            }else {

//                                getsubdataMR(Dcrdatas.select_divcode,Dcrdatas.select_sfcode);
//                                CategoryHq_details(Dcrdatas.select_divcode,Dcrdatas.select_sfcode);

                        getsubdataMR(div_Code,sf_code);
                        CategoryHq_details(div_Code,sf_code);
//                            }else{
//                            getsubdataMR(Dcrdatas.select_divcode,Dcrdatas.select_sfcode);
//                            CategoryHq_details(Dcrdatas.select_divcode,Dcrdatas.select_sfcode);
                       // }
                    }

                } else if (selected_data.equalsIgnoreCase("Speciality")) {
                    catog.setText("Speciality");
                    if (!strspclid.equals("")){
//                        if(toback.get(toback.size()-1).equalsIgnoreCase("Admin")){
//                            if(toback.size()==1){
//                                CategoryHq_detail(Dcrdatas.selected_division);
//                                getsubdataspecial(Dcrdatas.selected_division);
//                            }else if (toback.size()==0) {
//                                divisiondata();
//                            }else{

//                                getsubdataMRspcial(Dcrdatas.select_divcode,Dcrdatas.select_sfcode);
//                                CategoryHq_details(Dcrdatas.select_divcode,Dcrdatas.select_sfcode);

                        getsubdataMRspcial(div_Code,sf_code);
                        CategoryHq_details(div_Code,sf_code);
                           // }
//                        }else{
//                            getsubdataMRspcial(Dcrdatas.select_divcode,Dcrdatas.select_sfcode);
//                            CategoryHq_details(Dcrdatas.select_divcode,Dcrdatas.select_sfcode);
//                        }
                    }else {
                        Toast.makeText(SFE_Activtity.this, "Select Speciality", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SFE_Activtity.this, "Please Select the options to filter", Toast.LENGTH_SHORT).show();
                }
                slidemenu.closeDrawer(Gravity.RIGHT);
            }
        });

        backcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catback(Dcrdatas.selected_division);
            }
        });

        CategoryHq_data();
        Category_filterdetail();
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
        Log.d("outdate",fmtOut.format(date));
        monthselection.setText("Month: "+fmtOut.format(date));
        return fmtOut.format(date);
    }
    public String formatDate1(String dateString) {
        SimpleDateFormat fmt = new SimpleDateFormat("M");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("MMMM");
        Log.d("outdate",fmtOut.format(date));
        cur_month.setText(fmtOut.format(date));
        return fmtOut.format(date);
    }
    public void catback(String divdata){
        String selsfcode="";
        Log.d("", String.valueOf(toback.size()));
        if ((toback.size()) != 0) {
            selsfcode = toback.get(toback.size() - 1);
            // toback.remove(toback.size() - 1);
        } else {
//            selsfcode = CustomerMeList.get(0).getSFCode();
            divisiondata();
            chart.clear();
           // linear_bottom.setVisibility(View.GONE);
        }
        Log.d("toback",toback.toString());
        getbackdatas(selsfcode,divdata);

    }
    public void getbackdatas(String codeit,String divsion) {
        String sfcode=codeit;
        String divcode=divsion;
//        Toast.makeText(getApplicationContext(),sfcode+"-"+divcode+"-"+sfcode,Toast.LENGTH_LONG).show();
        if (sfcode.equals("admin")){
            if (Dcrdatas.select_data.equalsIgnoreCase("Category")){
                getsubdata(divcode);
                CategoryHq_detail(divcode);
            }else if (Dcrdatas.select_data.equalsIgnoreCase("Speciality")){
                getsubdataspecial(divcode);
                CategoryHq_detail(divcode);
            }
        }else if (sfcode.equals("")) {
//            divisiondata();
        }else {
            if (Dcrdatas.select_data.equalsIgnoreCase("Category")){
                CategoryHq_backdetails(divcode, sfcode);
                getsubdataMR(divcode, sfcode);
            }else if (Dcrdatas.select_data.equalsIgnoreCase("Speciality")){
                CategoryHq_backdetails(divcode, sfcode);
                getsubdataMRspcial(divcode, sfcode);
            }
        }
        if (!(toback.size() ==0)){
            toback.remove(toback.size() - 1);
        }

    }
    public void showalertspecial() {
        try{
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SFE_Activtity.this);
            LayoutInflater inflater = SFE_Activtity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.multiple_openmodal, null);
            final EditText editText = ( EditText ) dialogView.findViewById(R.id.search_multiple);
            TextView tv = ( TextView ) dialogView.findViewById(R.id.tv_searchheader_mul);
            CheckBox check=dialogView.findViewById(R.id.checkBox_mulcheck);
            TextView all=dialogView.findViewById(R.id.txtName_mulcheck);
            final RecyclerView rv_list = ( RecyclerView ) dialogView.findViewById(R.id.dailogrv_list_mul);
            dialogBuilder.setView(dialogView);
            editText.setHint("Search Speciality");
            tv.setText("Select the Speciality");
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dcrdatas.productFrom="1";
                    if(Dcrdatas.checkin == 1){
                        Dcrdatas.isselected=true;
                        Dcrdatas.checkin = 0;
                    }else{
                        Dcrdatas.isselected=false;
                        Dcrdatas.checkin=1;
                    }
                    muladapter.notifyDataSetChanged();
                }
            });
            muladapter = new Multicheckadapter(spcl_list1,check,rv_list, SFE_Activtity.this, new OnCampaignClicklistener() {

                @Override
                public void classCampaignItem_addClass(Multicheckclass classGroup) {
                    if (classGroup != null) {
                        if (!listSelected.contains(classGroup)){
                            listSelected.add(classGroup);
                        }
                    }
                }

                @Override
                public void classCampaignItem_removeClass(Multicheckclass classGroup) {
                    if (classGroup != null) {
                        listSelected.remove(classGroup);
                    }
                }
            });
            dialogBuilder.setNegativeButton("Done",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            // TODO Auto-generated method stub
                                    editText.getText().clear();
                                    selectedUsers = "";
                                    selctectedId = "";
                                    strspcl = "";
                                    strspclid = "";
                                    Log.d("listSelected_doctorsize", String.valueOf(listSelected.size()));
                                    if (listSelected.size() > 0) {
                                        for (int i = 0; i < listSelected.size(); i++) {
                                            if (listSelected.get(i).isChecked()) {
                                                selectedUsers = selectedUsers+listSelected.get(i).getStrname() + ",";
                                                Log.d("selectedUsersval", selectedUsers);
                                                selctectedId =selctectedId+listSelected.get(i).getStrid() + ",";
                                                Log.d("selectedUsersval", selctectedId);
                                                strspclid = selctectedId;
                                                strspcl = selectedUsers;
                                                txtspin.setText(listSelected.get(0).getStrname());
                                            }
                                        }
                                    }
                                    Dcrdatas.checkin=1;
                                    Dcrdatas.isselected=false;
                            dialog.dismiss();
                        }
                    });
            LinearLayoutManager LayoutManagerpocsale = new LinearLayoutManager(SFE_Activtity.this);
            rv_list.setLayoutManager(LayoutManagerpocsale);
            rv_list.setItemAnimator(new DefaultItemAnimator());
            rv_list.setAdapter(muladapter);
            muladapter.notifyDataSetChanged();
            final AlertDialog alertDialog = dialogBuilder.create();
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                adapter.getFilter().filter(s);
                    String text = editText.getText().toString().toLowerCase(Locale.getDefault());
                    filterjw2(text);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            alertDialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void filterjw2(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        spcl_list1.clear();
        if (charText.length() == 0) {
            spcl_list1.addAll(spcl_list_temp);
        } else {
            for (int i = 0; i < spcl_list_temp.size(); i++) {
                final String text = spcl_list_temp.get(i).getStrname().toLowerCase();
                if (text.contains(charText)) {

                    spcl_list1.add(spcl_list_temp.get(i));

                }
            }
        }

        muladapter.notifyDataSetChanged();
    }

    public void divisiondata() {
        try {
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(SFE_Activtity.this);
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
           Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e("Missed report request", "Missed report Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "getdivision_ho_sf");
            QryParam.put("divisionCode", div_code);
            QryParam.put("sfCode", "");
            QryParam.put("Ho_Id", CustomerMeList.get(0).getSFCode());
            Log.e("mreport_detail", QryParam.toString());
            try {
                final ProgressDialog mProgressDialog = new ProgressDialog(SFE_Activtity.this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("missreport:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("division:Res", response.body().toString());
                            parseJsonData_div(response.body().toString());
                            backcat.setVisibility(View.GONE);
                            Log.d("gang", response.body().toString());
                        } else {
                            Toast.makeText(SFE_Activtity.this, "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SFE_Activtity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void parseJsonData_div(String jsonResponse) {
        Log.d("jsonResponse_hq", jsonResponse);
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            div_list1.clear();
            div_list1id.clear();
            divnclass.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = jsonObject1.optString("division_code");
                String name = jsonObject1.optString("division_name");
                div_list1.add(name);
                div_list1id.add(id);

                divisionclass catagoryclass=new divisionclass();
                catagoryclass.setName(jsonObject1.optString("division_name"));
                catagoryclass.setId(jsonObject1.optString("division_code"));
                divnclass.add(catagoryclass);
            }
            LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(SFE_Activtity.this);
            recyclerView.setLayoutManager(LayoutManagerpoc);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            divisionAdapter = new ALL_DivisionAdapter(SFE_Activtity.this,divnclass,SFE_Activtity.this);
            recyclerView.setAdapter(divisionAdapter);
            divisionAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void specialitydata() {
        try {
//            sqlLite sqlLite;
//            String curval = null;
//            List<CustomerMe> CustomerMeList;
//            sqlLite = new sqlLite(SFE_Activtity.this);
//            Cursor cursor = sqlLite.getAllLoginData();
//            if (cursor.moveToFirst()) {
//                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//            }
//            cursor.close();
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<CustomerMe>>() {
//            }.getType();
//            CustomerMeList = gson.fromJson(curval, type);
//            listSelected.clear();
//            strspclid = "";
//            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());
//            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
//                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
//            } else {
//                div_code = CustomerMeList.get(0).getDivisionCode();
//            }

            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e("Missed report request", "Missed report Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "getdivision_speciality");
            QryParam.put("divisionCode", div_Code);
            QryParam.put("sfCode", "");
            QryParam.put("Ho_Id", sf_code);
            Log.e("mreport_detail", QryParam.toString());
            try {
                final ProgressDialog mProgressDialog = new ProgressDialog(SFE_Activtity.this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getSpecialityDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("missreport:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("division:Res", response.body().toString());
                            String data=response.body().toString();
                            parseJsonData_specialty(response.body().toString());
                            showalertspecial();
                            Log.d("gang", response.body().toString());
                        } else {
                            Toast.makeText(SFE_Activtity.this, "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SFE_Activtity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void parseJsonData_specialty(String jsonResponse) {
        Log.d("jsonResponse_hq", jsonResponse);
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            spcl_list1.clear();
            spcl_list1id.clear();
            spcl_div.clear();
            spcl_list.clear();
            spcl_listid.clear();
            String div_code1="";
//            spcl_list.add("All");
//            spcl_listid.add("0");
            Log.d("selec",Dcrdatas.selected_division1);

            String dibcode=Dcrdatas.selected_division1;
            Log.d("selec",Dcrdatas.selected_division1);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                div_code1=jsonObject1.optString("Division_Code");
                Log.d("selec", div_code1+"----"+dibcode);
                if(Dcrdatas.selected_division1.equals("")){
                    String id = jsonObject1.optString("id");
                    String name = jsonObject1.optString("name");
                    Multicheckclass mul = new Multicheckclass(id, name, false);
                    spcl_list1.add(mul);
                }else {
                    if (div_code1.equals(dibcode)) {
                        String id = jsonObject1.optString("id");
                        String name = jsonObject1.optString("name");

                        Multicheckclass mul = new Multicheckclass(id, name, false);
                        spcl_list1.add(mul);
                    }
                }
            }
            spcl_list_temp.clear();
            spcl_list_temp.addAll(spcl_list1);
            Log.d("selec1", String.valueOf(spcl_list1.size()));
            Log.d("selec11", String.valueOf(spcl_list_temp.size()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void CategoryHq_detail(String div_code) {
        try {
            String division=div_code;
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(SFE_Activtity.this);
            String username = Shared_Common_Pref.getusernameFromSP(SFE_Activtity.this);
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
            DateFormat dateFormat1 = new SimpleDateFormat("M");
            Date date1 = new Date();
            Log.d("Month1",dateFormat1.format(date1));

            DateFormat dateFormat2 = new SimpleDateFormat("yyyy");
            Date date2 = new Date();
            Log.d("Month1",dateFormat2.format(date2));

            if (Dcrdatas.select_month.equals("") && Dcrdatas.select_year.equals("")){
                fromstrdate = dateFormat1.format(date1);
                tostrdate=dateFormat2.format(date2);
            }else {
                fromstrdate = Dcrdatas.select_month;
                tostrdate=Dcrdatas.select_year;
            }



           Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" caty request","cat Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/hierarchy");
            QryParam.put("divisionCode", division);
            QryParam.put("sfCode", "admin");
            QryParam.put("fmonth", fromstrdate);
            QryParam.put("fyear", tostrdate);
            Log.e("catreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(SFE_Activtity.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.gethierarchyDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        String datas=response.body().toString();
                        if (!(datas.equals("") || datas.equals("[]"))) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataDetail(response.body().toString());
                            backcat.setVisibility(View.VISIBLE);
                            formatDate1(Dcrdatas.select_month);
                            if (selected_data.equalsIgnoreCase("Category")){
                                brandtxt.setText("Category Wise");
                            }else if (selected_data.equalsIgnoreCase("Speciality")) {
                                brandtxt.setText("Speciality Wise");
                            }
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(SFE_Activtity.this, "No Reporting found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SFE_Activtity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void CategoryHq_filterdate(String div_code) {
        try {
            String division=div_code;
            sqlLite sqlLite;
            String curval = null;
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(SFE_Activtity.this);
            String username = Shared_Common_Pref.getusernameFromSP(SFE_Activtity.this);
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

           Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" caty request","cat Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/hierarchy");
            QryParam.put("divisionCode", division+",");
            QryParam.put("sfCode", "admin");
            QryParam.put("fmonth", Dcrdatas.select_month);
            QryParam.put("fyear", Dcrdatas.select_year);
            Log.e("catreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(SFE_Activtity.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.gethierarchyDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataDetail(response.body().toString());
                            backcat.setVisibility(View.VISIBLE);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(SFE_Activtity.this, "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SFE_Activtity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void CategoryHq_details(String div_code,String sf_code) {
        try {

            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

//            String division=div_code;
//            String sfcode=sf_code;
//            if (toback.contains("Admin")) {
//                if (!toback.contains(sfcode)) {
//                    toback.add(sfcode);
//                }
//            } else {
//                toback.add("Admin");
//                toback.add(sfcode);
//            }
//            if (toback.contains(sfcode)){
//
//            }
//
//            Log.d("toback",toback.toString());
//            sqlLite sqlLite;
//            String curval = null;
//            List<CustomerMe> CustomerMeList;
//            sqlLite = new sqlLite(SFE_Activtity.this);
//            String username = Shared_Common_Pref.getusernameFromSP(SFE_Activtity.this);
//            Cursor cursor = sqlLite.getAllLoginData();
//
//            if (cursor.moveToFirst()) {
//                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//            }
//            cursor.close();
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<CustomerMe>>() {
//            }.getType();
//            CustomerMeList = gson.fromJson(curval, type);
//            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());
//
//
//            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
//                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
//            } else {
//                div_code = CustomerMeList.get(0).getDivisionCode();
//            }

            String fromstrdate="";
            String tostrdate="";
            DateFormat dateFormat1 = new SimpleDateFormat("M");
            Date date1 = new Date();
            Log.d("Month1",dateFormat1.format(date1));

            DateFormat dateFormat2 = new SimpleDateFormat("yyyy");
            Date date2 = new Date();
            Log.d("Month1",dateFormat2.format(date2));

            if (Dcrdatas.select_month.equals("") && Dcrdatas.select_year.equals("")){
                fromstrdate = dateFormat1.format(date1);
                tostrdate=dateFormat2.format(date2);
            }else {
                fromstrdate = Dcrdatas.select_month;
                tostrdate=Dcrdatas.select_year;
            }

           Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" caty request","cat Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/hierarchy");
            QryParam.put("divisionCode", div_Code);
            QryParam.put("sfCode", sf_code);
            QryParam.put("fmonth", fromstrdate);
            QryParam.put("fyear", tostrdate);
            Log.e("catreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(SFE_Activtity.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.gethierarchyDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataDetail(response.body().toString());
                            formatDate1(Dcrdatas.select_month);
                            if (selected_data.equalsIgnoreCase("Category")){
                                brandtxt.setText("Category Wise");
                            }else if (selected_data.equalsIgnoreCase("Speciality")) {
                                brandtxt.setText("Speciality Wise");
                            }
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(SFE_Activtity.this, "Reporting", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SFE_Activtity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void CategoryHq_backdetails(String div_code,String sf_code) {
        try {
//

            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

            DateFormat dateFormat1 = new SimpleDateFormat("M");
            Date date1 = new Date();
            Log.d("Month1",dateFormat1.format(date1));

            DateFormat dateFormat2 = new SimpleDateFormat("yyyy");
            Date date2 = new Date();
            Log.d("Month1",dateFormat2.format(date2));

            String fromstrdate = dateFormat1.format(date1);
            String tostrdate=dateFormat2.format(date2);

           Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" caty request","cat Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/hierarchy");
            QryParam.put("divisionCode", div_code);
            QryParam.put("sfCode", sf_code);
            QryParam.put("fmonth", fromstrdate);
            QryParam.put("fyear", tostrdate);
            Log.e("catreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(SFE_Activtity.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.gethierarchyDataAsJArray( QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataDetail(response.body().toString());
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(SFE_Activtity.this, "Reporting", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SFE_Activtity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Category_filterdetail() {
        try {
//            sqlLite sqlLite;
//            String curval = null;
//            List<CustomerMe> CustomerMeList;
//            sqlLite = new sqlLite(SFE_Activtity.this);
//            String username = Shared_Common_Pref.getusernameFromSP(SFE_Activtity.this);
//            Cursor cursor = sqlLite.getAllLoginData();
//
//            if (cursor.moveToFirst()) {
//                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//            }
//            cursor.close();
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<CustomerMe>>() {
//            }.getType();
//            CustomerMeList = gson.fromJson(curval, type);
//            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());
//
//
//            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
//                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
//            } else {
//                div_code = CustomerMeList.get(0).getDivisionCode();
//            }

            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

            DateFormat dateFormat = new SimpleDateFormat("MM");
            Date date = new Date();
            Log.d("Month",dateFormat.format(date));

            DateFormat dateFormat1 = new SimpleDateFormat("MM");
            Date date1 = new Date();
            Log.d("Month",dateFormat.format(date1));
            String frommonth = dateFormat.format(date);
            String tostrdate="";

           Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" caty request","cat Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/Category_sfe");
            QryParam.put("divisionCode", div_code);
            QryParam.put("sfCode", sf_code);
            QryParam.put("fmonth", Dcrdatas.select_month);
            QryParam.put("fyear", Dcrdatas.select_year);
            Log.e("catreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(SFE_Activtity.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getCategoryDataAsJArray(QryParam);
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
                            Toast.makeText(SFE_Activtity.this, "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SFE_Activtity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJsonDataDetail(String ordrdcr) {
        String date = "";
        String jsw = "";
        try {
            try {
                JSONArray js = new JSONArray(ordrdcr);
                Log.e("jsonarray", js.toString());
                if (js.length() > 0) {
                    hqclas.clear();
                    for (int i = 0; i < js.length(); i++) {
                        JSONObject jsonObject = js.getJSONObject(i);
                        hqclass hqclass = new hqclass();
                        hqclass.setDivision_code(jsonObject.optString("division_code"));
                        hqclass.setSf_Code(jsonObject.optString("Sf_Code"));
                        hqclass.setSf_Name(jsonObject.optString("Sf_Name"));
                        hqclass.setSf_type(jsonObject.optString("sf_type"));
                        hqclas.add(hqclass);
                    }
                    LinearLayoutManager LayoutManagerpoc = new LinearLayoutManager(SFE_Activtity.this);
                    recyclerView.setLayoutManager(LayoutManagerpoc);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    divisnAdapter = new Division_Adapter(SFE_Activtity.this,hqclas,SFE_Activtity.this);
                    recyclerView.setAdapter(divisnAdapter);
                    divisnAdapter.notifyDataSetChanged();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void parseJsonDataorderDetail(String ordrdcr) {
        String date = "";
        String jsw="";
        try {
            try {
                JSONArray js = new JSONArray(ordrdcr);
                Log.e("jsonarray",js.toString());
                if (js.length() > 0) {
                    catyclass.clear();
                    chart.clear();
                    for (int i = 0; i < js.length(); i++) {
                        JSONObject jsonObject = js.getJSONObject(i);
                        Categoryclass categoryclass = new Categoryclass();
                        categoryclass.setDivision_code(jsonObject.optString("Division_code"));
                        categoryclass.setNo_of_visit(jsonObject.optString("No_of_visit"));
                        categoryclass.setCat_Name(jsonObject.optString("Cat_Name"));
                        categoryclass.setTot_drs(jsonObject.optString("Tot_drs"));
                        categoryclass.setVst_1(jsonObject.getDouble("1_Vst"));
                        categoryclass.setVst_2(jsonObject.getDouble("2_Vst"));
                        categoryclass.setVst_3(jsonObject.getDouble("3_Vst"));
                        catyclass.add(categoryclass);
                    }


                    Log.d("sum12", String.valueOf(catyclass.size()));

                    int groupCount = catyclass.size();
                    Log.d("groupCount", String.valueOf(groupCount));
                    ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                    ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
                    ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
                    ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();

//                    for (int k=0;k<catyclass.size();k++) {
                    yVals1.add(new BarEntry(1, Float.parseFloat(String.valueOf(catyclass.get(0).getVst_1()))));
                    yVals2.add(new BarEntry(2, 0));
                    yVals3.add(new BarEntry(3,  0));

                    yVals1.add(new BarEntry(1, Float.parseFloat(String.valueOf(catyclass.get(1).getVst_1()))));
                    yVals2.add(new BarEntry(2, Float.parseFloat(String.valueOf(catyclass.get(1).getVst_2()))));
                    yVals3.add(new BarEntry(3, 0));

                    yVals1.add(new BarEntry(1, Float.parseFloat(String.valueOf(catyclass.get(2).getVst_1()))));
                    yVals2.add(new BarEntry(2, Float.parseFloat(String.valueOf(catyclass.get(2).getVst_2()))));
                    yVals3.add(new BarEntry(3, Float.parseFloat(String.valueOf(catyclass.get(2).getVst_3()))));


//                    }

                    ArrayList xVals = new ArrayList();
                    ArrayList<String> bardata = new ArrayList<>();
                    bardata.clear();
                    for (int n = 0; n < catyclass.size(); n++){
                        xVals.add(catyclass.get(n).getCat_Name());
                        bardata.add(catyclass.get(n).getCat_Name()+"~"+catyclass.get(n).getTot_drs()+"~"+catyclass.get(n).getVst_4());
                    }
                    Log.d("bardara",bardata.toString());

                    BarDataSet set1, set2,set3,set4;
                    set1 = new BarDataSet(yVals1, "1-visit");
                    set1.setColor(Color.BLUE);
                    set2 = new BarDataSet(yVals2, "2-visit");
                    set2.setColor(Color.GREEN);
                    set3 = new BarDataSet(yVals3, "3-visit");
                    set3.setColor(Color.CYAN);
                    set4 = new BarDataSet(yVals4, "");
                    set4.setColor(Color.CYAN);

                    BarData data = new BarData(set1, set2,set3,set4);
                    data.setValueFormatter(new LargeValueFormatter());
                    chart.setData(data);
                    chart.getBarData().setBarWidth(barWidth);
                    chart.getXAxis().setAxisMinimum(0);
                    chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
                    chart.groupBars(0, groupSpace, barSpace);
                    chart.getData().setHighlightEnabled(true);
                    chart.animateY(2000);
                    chart.setDescription(null);
                    chart.setPinchZoom(true);
                    chart.setScaleEnabled(false);
                    chart.setDrawBarShadow(false);
                    chart.setDrawGridBackground(false);
                    chart.setTouchEnabled(true);
//                    chart.moveViewToX(10);
                    chart.setFitBars(true);
//                    chart.setVisibleXRangeMinimum(yVals1.size());
//                    chart.setVisibleXRangeMinimum(yVals2.size());
//                    chart.setVisibleXRangeMinimum(yVals3.size());
                    chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, Highlight h) {
                            if (e == null) {
                                return;
                            }
                            Log.d("barchart","selected");
                            for (int i=0;i < yVals1.size(); i++) {
                                if (yVals1.get(i).getY() == e.getY() || yVals2.get(i).getY() == e.getY() || yVals3.get(i).getY() == e.getY()) {
                                    showchartdetails(bardata, e.getX(), e.getY(), i);
                                }
                            }
                            Log.d("bardd",e.getX()+"--"+e.getY());
                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });
                    chart.invalidate();

                    Legend l = chart.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setDrawInside(true);
                    l.setYOffset(0f);
                    l.setXOffset(0f);
                    l.setYEntrySpace(0f);
                    l.setTextSize(8f);
                    //X-axis
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setDrawGridLines(false);
                    xAxis.setLabelCount(xVals.size());
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
//                    xAxis.setSpaceMin(data.getBarWidth() / 2f);
//                    xAxis.setSpaceMax(data.getBarWidth() / 2f);
                    xAxis.setTextColor(Color.BLACK);
//                    xAxis.setTextSize(12);
                    xAxis.setAxisLineColor(Color.WHITE);
                    //Y-axis
                    chart.getAxisRight().setEnabled(false);
                    YAxis leftAxis = chart.getAxisLeft();
                    leftAxis.setValueFormatter(new LargeValueFormatter());
                    leftAxis.setDrawGridLines(true);
                    leftAxis.setSpaceTop(25f);
                    leftAxis.setAxisMinimum(0f);


                } else {
                    Toast.makeText(SFE_Activtity.this, "No Records found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SFE_Activtity.this, "No records found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(SFE_Activtity.this , "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJsonDataspecialDetail(String ordrdcr) {
        String date = "";
        String jsw="";
        try {
            try {
                JSONArray js = new JSONArray(ordrdcr);
                Log.e("jsonarray",js.toString());
                if (js.length() > 0) {
                    spclclass.clear();
                    chart.clear();
                    for (int i = 0; i < js.length(); i++) {
                        JSONObject jsonObject = js.getJSONObject(i);
                        specialityclass specialityclass = new specialityclass();
                        specialityclass.setDoc_Special_Code(jsonObject.optString("Doc_Special_Code"));
                        specialityclass.setSpec_Name(jsonObject.optString("Spec_Name"));
                        specialityclass.setCnt(jsonObject.optInt("cnt"));
                        specialityclass.setAvrg(jsonObject.optString("Avrg"));
                        spclclass.add(specialityclass);
                    }

                    Log.d("sum12", String.valueOf(spclclass.size()));
                    int groupCount = spclclass.size()+2;
                    Log.d("groupCount", String.valueOf(groupCount));

                    ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                    for (int k=0;k<spclclass.size();k++) {
                        yVals1.add(new BarEntry(k,spclclass.get(k).getCnt()+1));
                    }

                    ArrayList xVals = new ArrayList();
                    xVals.clear();
                    for (int n = 0; n < spclclass.size(); n++) {
                        String spp=spclclass.get(n).getSpec_Name();
                        String formattedString = spclclass.get(n).getSpec_Name().replaceAll("\\(.*?\\)","").trim();
                        Log.d("form",formattedString);
                        xVals.add(formattedString);
                    }
                    Log.d("xval", String.valueOf(xVals));

//                    https://www.semicolonworld.com/question/1329/how-to-show-labels-on-right-and-values-to-left-side-in-horizontalbarchart

                    BarDataSet bardataset = new BarDataSet(yVals1, "");
                    BarData data = new BarData();
                    data.addDataSet(bardataset);
                    data.setBarWidth(1);
                    bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

                    chart.setData(data);
                    chart.setDescription(null);
                    chart.setFitBars(true);
                    chart.setDragXEnabled(true);
                    chart.animateY(2000);
                    chart.setDrawValueAboveBar(true);
                    chart.setDragEnabled(true);

                    XAxis xAxis = chart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                    xAxis.setLabelRotationAngle(270);
                    xAxis.setTextSize(6f);
                    xAxis.setAxisLineWidth(1f);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setGranularity(1f);
                    xAxis.setDrawLabels(true);
                    xAxis.setLabelCount(yVals1.size());
                    xAxis.setAxisMinimum(0f);
                    xAxis.setAxisMaximum(yVals1.size());
                    xAxis.setSpaceMin(0.5f);
                    xAxis.setSpaceMax(0.5f);


                    Legend l = chart.getLegend();
                    l.setEnabled(false);

                    YAxis leftAxis = chart.getAxisLeft();
                    leftAxis.setSpaceTop(1f);
                    leftAxis.setGranularity(1f);
                    chart.invalidate();

                } else {
                    Toast.makeText(SFE_Activtity.this, "No Records found", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SFE_Activtity.this, "No records found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(SFE_Activtity.this , "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
    public void getsubdata(String div_code) {
        try {
            String division=div_code;
            sqlLite sqlLite;
            String curval = null;
            chart.clear();
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(SFE_Activtity.this);
            String username = Shared_Common_Pref.getusernameFromSP(SFE_Activtity.this);
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
            DateFormat dateFormat1 = new SimpleDateFormat("M");
            Date date1 = new Date();
            Log.d("Month1",dateFormat1.format(date1));

            DateFormat dateFormat2 = new SimpleDateFormat("yyyy");
            Date date2 = new Date();
            Log.d("Month1",dateFormat2.format(date2));

            if (Dcrdatas.select_month.equals("") && Dcrdatas.select_year.equals("")){
                fromstrdate = dateFormat1.format(date1);
                tostrdate=dateFormat2.format(date2);
            }else {
                fromstrdate = Dcrdatas.select_month;
                tostrdate=Dcrdatas.select_year;
            }

           Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" caty request","cat Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/Category_sfe");
            QryParam.put("divisionCode", division);
            QryParam.put("sfCode", "admin");
            QryParam.put("fmonth", fromstrdate);
            QryParam.put("fyear", tostrdate);
            Log.e("catreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(SFE_Activtity.this);
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
                            parseJsonDataorderDetail(response.body().toString());
                            linear_bottom.setVisibility(View.VISIBLE);
                            formatDate1(Dcrdatas.select_month);
                            if (selected_data.equalsIgnoreCase("Category")){
                                brandtxt.setText("Category Wise");
                            }else if (selected_data.equalsIgnoreCase("Speciality")) {
                                brandtxt.setText("Speciality Wise");
                            }
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(SFE_Activtity.this, "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SFE_Activtity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getsubdataspecial(String div_code) {
        try {
            String division=div_code;
            sqlLite sqlLite;
            String curval = null;
            chart.clear();
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(SFE_Activtity.this);
            String username = Shared_Common_Pref.getusernameFromSP(SFE_Activtity.this);
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
            DateFormat dateFormat1 = new SimpleDateFormat("M");
            Date date1 = new Date();
            Log.d("Month1",dateFormat1.format(date1));

            DateFormat dateFormat2 = new SimpleDateFormat("yyyy");
            Date date2 = new Date();
            Log.d("Month1",dateFormat2.format(date2));

            if (Dcrdatas.select_month.equals("") && Dcrdatas.select_year.equals("")){
                fromstrdate = dateFormat1.format(date1);
                tostrdate=dateFormat2.format(date2);
            }else {
                fromstrdate = Dcrdatas.select_month;
                tostrdate=Dcrdatas.select_year;
            }

           Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" caty request","cat Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/speciality_sfe");
            QryParam.put("divisionCode", division);
            QryParam.put("sfCode", "admin");
            QryParam.put("fmonth", fromstrdate);
            QryParam.put("fyear", tostrdate);
            QryParam.put("spec_code", strspclid);
            QryParam.put("mode", "1");
            Log.e("catreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(SFE_Activtity.this);
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
                            parseJsonDataspecialDetail(response.body().toString());
                            linear_bottom.setVisibility(View.VISIBLE);
                            formatDate1(Dcrdatas.select_month);
                            if (selected_data.equalsIgnoreCase("Category")){
                                brandtxt.setText("Category Wise");
                            }else if (selected_data.equalsIgnoreCase("Speciality")) {
                                brandtxt.setText("Speciality Wise");
                            }
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(SFE_Activtity.this, "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SFE_Activtity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getsubdataMR(String div_code,String sf_code) {
        try {
            String division=div_code;
            String sfcode=sf_code;

            chart.clear();
           
//            sqlLite sqlLite;
//            String curval = null;
//            chart.clear();
//            List<CustomerMe> CustomerMeList;
//            sqlLite = new sqlLite(SFE_Activtity.this);
//            String username = Shared_Common_Pref.getusernameFromSP(SFE_Activtity.this);
//            Cursor cursor = sqlLite.getAllLoginData();
//
//            if (cursor.moveToFirst()) {
//                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//            }
//            cursor.close();
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<CustomerMe>>() {
//            }.getType();
//            CustomerMeList = gson.fromJson(curval, type);
//            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());
//
//
//            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
//                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
//            } else {
//                div_code = CustomerMeList.get(0).getDivisionCode();
//            }
            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);
           
            String fromstrdate="";
            String tostrdate="";
            DateFormat dateFormat1 = new SimpleDateFormat("M");
            Date date1 = new Date();
            Log.d("Month1",dateFormat1.format(date1));

            DateFormat dateFormat2 = new SimpleDateFormat("yyyy");
            Date date2 = new Date();
            Log.d("Month1",dateFormat2.format(date2));

            if (Dcrdatas.select_month.equals("") && Dcrdatas.select_year.equals("")){
                fromstrdate = dateFormat1.format(date1);
                tostrdate=dateFormat2.format(date2);
            }else {
                fromstrdate = Dcrdatas.select_month;
                tostrdate=Dcrdatas.select_year;
            }

            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" caty request","cat Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/Category_sfe");
            QryParam.put("divisionCode", div_code);
            QryParam.put("sfCode", sf_code);
            QryParam.put("fmonth", fromstrdate);
            QryParam.put("fyear", tostrdate);
            Log.e("catreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(SFE_Activtity.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getCategoryDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataorderDetail(response.body().toString());
                            formatDate1(Dcrdatas.select_month);
                            if (selected_data.equalsIgnoreCase("Category")){
                                brandtxt.setText("Category Wise");
                            }else if (selected_data.equalsIgnoreCase("Speciality")) {
                                brandtxt.setText("Speciality Wise");
                            }
                        } else {
                            Toast.makeText(SFE_Activtity.this, "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SFE_Activtity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getsubdataMRspcial(String div_code,String sf_code) {
        try {
            String division=div_code;
            String sfcode=sf_code;
            sqlLite sqlLite;
            String curval = null;
            chart.clear();
            List<CustomerMe> CustomerMeList;
            sqlLite = new sqlLite(SFE_Activtity.this);
            String username = Shared_Common_Pref.getusernameFromSP(SFE_Activtity.this);
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
            String fromstrdate="";
            String tostrdate="";
            DateFormat dateFormat1 = new SimpleDateFormat("M");
            Date date1 = new Date();
            Log.d("Month1",dateFormat1.format(date1));

            DateFormat dateFormat2 = new SimpleDateFormat("yyyy");
            Date date2 = new Date();
            Log.d("Month1",dateFormat2.format(date2));

            if (Dcrdatas.select_month.equals("") && Dcrdatas.select_year.equals("")){
                fromstrdate = dateFormat1.format(date1);
                tostrdate=dateFormat2.format(date2);
            }else {
                fromstrdate = Dcrdatas.select_month;
                tostrdate=Dcrdatas.select_year;
            }

           Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" caty request","cat Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/speciality_sfe");
            QryParam.put("divisionCode", div_Code);
            QryParam.put("sfCode", sf_code);
            QryParam.put("fmonth", fromstrdate);
            QryParam.put("fyear", tostrdate);
            QryParam.put("spec_code", strspclid);
            QryParam.put("mode", "1");
            Log.e("catreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(SFE_Activtity.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.getCategoryDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataspecialDetail(response.body().toString());
                            formatDate1(Dcrdatas.select_month);
                            if (selected_data.equalsIgnoreCase("Category")){
                                brandtxt.setText("Category Wise");
                            }else if (selected_data.equalsIgnoreCase("Speciality")) {
                                brandtxt.setText("Speciality Wise");
                            }
                        } else {
                            Toast.makeText(SFE_Activtity.this, "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SFE_Activtity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showchartdetails(ArrayList<String> bardata, float x, float y, int n){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SFE_Activtity.this);
        LayoutInflater inflater = SFE_Activtity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_category, null);
        dialogBuilder.setView(dialogView);
        TextView prdname = dialogView.findViewById(R.id.cat_name);
        TextView prdtarget = dialogView.findViewById(R.id.missdr);
        TextView prdcnt = dialogView.findViewById(R.id.totdr);
        TextView prdarcheive = dialogView.findViewById(R.id.tv_avg);
//        TextView prdgrowth = dialogView.findViewById(R.id.prd_growth);
//        TextView prdpcm = dialogView.findViewById(R.id.prd_pcpm);
//        TextView prddname = dialogView.findViewById(R.id.prd_dname);
        TextView infobut = dialogView.findViewById(R.id.info_but);
        TextView ok =dialogView.findViewById(R.id.okbut);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
//        Log.d("position", String.valueOf(position));
        String[] separateddoc =bardata.get(n).split("~");
        prdname.setText(separateddoc[0]);
        prdtarget.setText(separateddoc[1]);
        prdcnt.setText(separateddoc[2]);
//        prdarcheive.setText(separateddoc[3]);
//        prdgrowth.setText(separateddoc[6]);
//        prdpcm.setText(separateddoc[3]);
//        prddname.setText(separateddoc[4]);

//        Log.d("webdata",separateddoc[7]+"/*/"+separateddoc[8]+"**"+separateddoc[9]+"**"+separateddoc[10]);

//        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
//        DateFormat outputFormat = new SimpleDateFormat("MM");
//        DateFormat outputyear = new SimpleDateFormat("yyyy");
////        String inputDateStr=separateddoc[9];
////        String inputDateStr1=separateddoc[10];
//
//        Date date = null;
//        Date date1 = null;
//        try {
//            date = inputFormat.parse(inputDateStr);
//            date1 = inputFormat.parse(inputDateStr1);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        String outputDateStr = outputFormat.format(date);
//        String outputDateyear = outputyear.format(date);
//
//        String outputDateStr1 = outputFormat.format(date1);
//        String outputDateyear1 = outputyear.format(date1);
//
//        Log.d("webdata",outputDateStr+"/*/"+outputDateyear+"**"+outputDateStr1+"**"+outputDateyear1);

        sqlLite sqlLite;
        String curval = null;
        List<CustomerMe> CustomerMeList;
        sqlLite = new sqlLite(this);
        String username = Shared_Common_Pref.getusernameFromSP(this);
        Cursor cursor = sqlLite.getAllLoginData();

        if (cursor.moveToFirst()) {
            curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
        }
        cursor.close();
        Gson gson = new Gson();
        Type type = new TypeToken<List<CustomerMe>>() {
        }.getType();
        CustomerMeList = gson.fromJson(curval, type);
        Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " +username);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        infobut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent oo = new Intent(SFE_Activtity.this, Web_Activity.class);
//                oo.putExtra("sfcode",separateddoc[8]);
//                oo.putExtra("fmon",outputDateStr);
//                oo.putExtra("fyear",outputDateyear);
//                oo.putExtra("tmon",outputDateStr1);
//                oo.putExtra("tyear",outputDateyear1);
//                oo.putExtra("Dcode",separateddoc[7]);
//                oo.putExtra("Brandcd",separateddoc[0]);
//                oo.putExtra("Brandname",label);
//                oo.putExtra("sfname",CustomerMeList.get(0).getSFName());
//                startActivity(oo);
            }
        });
        alertDialog.show();
    }

    public void CategoryHq_data() {
        try {

//            sqlLite sqlLite;
//            String curval = null;
//            List<CustomerMe> CustomerMeList;
//            sqlLite = new sqlLite(SFE_Activtity.this);
//            String username = Shared_Common_Pref.getusernameFromSP(SFE_Activtity.this);
//            Cursor cursor = sqlLite.getAllLoginData();
//
//            if (cursor.moveToFirst()) {
//                curval = cursor.getString(cursor.getColumnIndex("Log_Values"));
//            }
//            cursor.close();
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<CustomerMe>>() {
//            }.getType();
//            CustomerMeList = gson.fromJson(curval, type);
//            Log.d("CustomerMeListDataSF11", CustomerMeList.get(0).getSFCode() + " " + CustomerMeList.get(0).getDivisionCode());
//
//
//            if (CustomerMeList.get(0).getDivisionCode().contains(",")) {
//                div_code = CustomerMeList.get(0).getDivisionCode().substring(0, CustomerMeList.get(0).getDivisionCode().length() - 1);
//            } else {
//                div_code = CustomerMeList.get(0).getDivisionCode();
//            }

            sf_code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
            div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);

            String fromstrdate = "";
            String tostrdate="";
            DateFormat dateFormat1 = new SimpleDateFormat("M");
            Date date1 = new Date();
            Log.d("Month1",dateFormat1.format(date1));

            DateFormat dateFormat2 = new SimpleDateFormat("yyyy");
            Date date2 = new Date();
            Log.d("Month1",dateFormat2.format(date2));

            if (Dcrdatas.select_month.equals("") && Dcrdatas.select_year.equals("")){
                fromstrdate = dateFormat1.format(date1);
                tostrdate=dateFormat2.format(date2);
            }else {
                fromstrdate = Dcrdatas.select_month;
                tostrdate=Dcrdatas.select_year;
            }


            Api_Interface apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            Log.e(" caty request","cat Detail request");
            Map<String, String> QryParam = new HashMap<>();
            QryParam.put("axn", "get/hierarchy");
            QryParam.put("divisionCode",div_Code);
            QryParam.put("sfCode",sf_code);
            QryParam.put("fmonth", fromstrdate);
            QryParam.put("fyear", tostrdate);
            Log.e("catreport_detail", QryParam.toString());
            try {

                final ProgressDialog mProgressDialog = new ProgressDialog(SFE_Activtity.this);
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                Call<JsonArray> call = apiService.gethierarchyDataAsJArray(QryParam);
                call.enqueue(new Callback<JsonArray>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        Log.d("orderrepor:Code", response.code() + " - " + response.toString());
                        if (response.code() == 200 || response.code() == 201) {
                            Log.d("orderrepor:Res", response.body().toString());
                            parseJsonDataDetail(response.body().toString());
                            backcat.setVisibility(View.INVISIBLE);
                        } else {
//                            Log.d("expense:Res", "1112222233333444");
                            Toast.makeText(SFE_Activtity.this, "No records found", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SFE_Activtity.this, "Something went wrong  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}