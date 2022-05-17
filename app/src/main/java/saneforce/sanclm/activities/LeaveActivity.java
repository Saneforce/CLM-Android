package saneforce.sanclm.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.LoadBitmap;
import saneforce.sanclm.adapter_class.LeaveAdapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.DirectionsJSONParser;
import saneforce.sanclm.util.UpdateUi;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.licenceKey;

public class LeaveActivity extends AppCompatActivity {

    ListView list_elig, list_avail, list_taken;
    LeaveAdapter leaveAdapterE;
    LeaveAdapter leaveAdapterT;
    LeaveAdapter leaveAdapterA;
    ArrayList<LoadBitmap> arrElig = new ArrayList<>();
    ArrayList<LoadBitmap> arrTkn = new ArrayList<>();
    ArrayList<LoadBitmap> arrAva = new ArrayList<>();
    LinearLayout lay_cl, lay_pl, lay_sl, lay_lop, lay_el, txt_add, txt_reason;
    TextView txt_cl, txt_pl, txt_sl, txt_lop, txt_el;
    static TextView edt_from, edt_to, txt_day;
    static boolean from = true;
    static String leavestatus = "0                                                                                                                                                                                                                                                                                                 ";
    EditText edt_add, edt_reason;
    ImageView iv_dwnldmaster_back;
    static String leaveType = "";
    static CommonSharedPreference mCommonSharedPreference;
    static String SF_Code, Emp_Id,div_Code;
    String db_connPath;
    static Api_Interface apiService;
    Button submit;
    static UpdateUi updateUi;

    SharedPreferences sharedPreferences;
    String licence,leaveCode,leavetype,leaveName,aw_flag;
    String req = "0", empId,mislvdate="",lvdate="",status="";
    JSONObject svjson;
    AppCompatSpinner spinner;
    ArrayList<String> selectedLeave;
    ArrayAdapter<String> arrayAdapter;




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("keypad_down", "are_called");

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LeaveActivity.this, HomeDashBoard.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.v("keypad_down12", "are_called");
        commonFun();
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        list_elig = (ListView) findViewById(R.id.list_elig);
        list_taken = (ListView) findViewById(R.id.list_taken);
        list_avail = (ListView) findViewById(R.id.list_avail);
        lay_cl = (LinearLayout) findViewById(R.id.lay_cl);
        lay_pl = (LinearLayout) findViewById(R.id.lay_pl);
        lay_sl = (LinearLayout) findViewById(R.id.lay_sl);
        lay_lop = (LinearLayout) findViewById(R.id.lay_lop);
        txt_reason = (LinearLayout) findViewById(R.id.lay_reason);
        txt_add = (LinearLayout) findViewById(R.id.lay_add);
        txt_cl = (TextView) findViewById(R.id.type_cl);
        txt_pl = (TextView) findViewById(R.id.type_pl);
        txt_sl = (TextView) findViewById(R.id.type_sl);
        txt_lop = (TextView) findViewById(R.id.type_lop);
        edt_from = (TextView) findViewById(R.id.edt_from);
        edt_to = (TextView) findViewById(R.id.edt_to);
        txt_day = (TextView) findViewById(R.id.txt_day);
        edt_add = (EditText) findViewById(R.id.edt_add);
        edt_reason = (EditText) findViewById(R.id.edt_reason);
        edt_reason.hasFocusable();
        edt_add.hasFocusable();
        edt_add.setFocusable(true);
        edt_add.isClickable();
        edt_add.isCursorVisible();
        edt_reason.isCursorVisible();
        edt_reason.isClickable();
        edt_reason.setFocusable(true);
        iv_dwnldmaster_back = (ImageView) findViewById(R.id.iv_dwnldmaster_back);
        mCommonSharedPreference = new CommonSharedPreference(this);
        submit = (Button) findViewById(R.id.submit);
        spinner=(AppCompatSpinner) findViewById(R.id.lv_spinner);
        selectedLeave=new ArrayList<>();

        status=getIntent().getStringExtra("Missed");



        if(status.equalsIgnoreCase("2")) {

            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            mislvdate=mCommonSharedPreference.getValueFromPreference("mis_date");
            Date date= null;
            try {
                date = inputFormat.parse(mislvdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert date != null;
            lvdate=outputFormat.format(date);
            edt_from.setText(lvdate);
            edt_to.setText(lvdate);


//            if (edt_from != null && edt_to!=null) {
//                edt_from.setEnabled(false);
//                edt_to.setEnabled(false);
//            }

            if (!TextUtils.isEmpty(edt_from.getText().toString()) && !TextUtils.isEmpty(edt_to.getText().toString())) {
                edt_from.setEnabled(false);
                edt_to.setEnabled(false);
                int count = getCountOfDays(edt_from.getText().toString(), edt_to.getText().toString());
                if (count > 0) {
                    txt_day.setText(String.valueOf(getCountOfDays(edt_from.getText().toString(), edt_to.getText().toString())));

                } else {
                    Toast.makeText(this, getResources().getString(R.string.togreater), Toast.LENGTH_SHORT).show();
                    edt_to.setText("");
                }
            }

        }else if(status.equalsIgnoreCase("3")) {

            mislvdate=mCommonSharedPreference.getValueFromPreference("todayDate");
            edt_from.setText(mislvdate);
            edt_to.setText(mislvdate);

            if (!TextUtils.isEmpty(edt_from.getText().toString()) && !TextUtils.isEmpty(edt_to.getText().toString())) {
                edt_from.setEnabled(false);
                edt_to.setEnabled(false);
                int count = getCountOfDays(edt_from.getText().toString(), edt_to.getText().toString());
                if (count > 0) {
                    txt_day.setText(String.valueOf(getCountOfDays(edt_from.getText().toString(), edt_to.getText().toString())));

                } else {
                    Toast.makeText(this, getResources().getString(R.string.togreater), Toast.LENGTH_SHORT).show();
                    edt_to.setText("");
                }
            }

        }


        //        lay_el=(LinearLayout) findViewById(R.id.lay_el);
//        txt_el=(TextView)findViewById(R.id.type_el);

//        txt_cl.setBackgroundColor(Color.BLACK);
//        txt_cl.setTextColor(Color.WHITE);
//
//        leaveType = "CL";


        leavestatus = mCommonSharedPreference.getValueFromPreference("LeaveStatus");
        SF_Code = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        div_Code=mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);


        Emp_Id = mCommonSharedPreference.getValueFromPreference("sf_emp_id");
        Log.v("empId:", Emp_Id);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        licence = sharedPreferences.getString(licenceKey, "");
        Log.v("licenceKey:", licence);

        //leavestatus="0";


        /*edt_reason.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.v("keypad_down13", "are_called");
                return true;
            }
        });*/
            if(CommonUtilsMethods.isOnline(LeaveActivity.this))
           {

           }else {
                   Toast.makeText(LeaveActivity.this, getResources().getString(R.string.offline), Toast.LENGTH_SHORT).show();
                 }

        edt_reason.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    //check if the right key was pressed
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {

                        return true;
                    }
                }
                return false;
            }
        });


        edt_add.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    //check if the right key was pressed
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                    {

                        return true;
                    }
                }
                return false;
            }
        });

        /*edt_add.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.v("keypad_down13", "are_called");
                return true;
            }
        });*/
        if (Emp_Id != null) {
            if (Emp_Id.length() == 6) {
                empId = Emp_Id;
            } else if (Emp_Id.length() == 5) {
                empId = ("0" + Emp_Id);
            } else if (Emp_Id.length() == 4) {
                empId = ("00" + Emp_Id);
            } else if (Emp_Id.length() == 3) {
                empId = ("000" + Emp_Id);
            } else if (Emp_Id.length() == 2) {
                empId = ("0000" + Emp_Id);
            } else if (Emp_Id.length() == 1) {
                empId = ("00000" + Emp_Id);
            }
        } else {
            Toast.makeText(this, "EMpid Null", Toast.LENGTH_LONG).show();
        }


        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);

        leaveTypes();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  // ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView)view).setTextColor(getResources().getColor(R.color.black));
                 leaveType = adapterView.getItemAtPosition(i).toString();
                 Log.v("leavetype",leaveType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        try {
            JSONObject js = new JSONObject();
            js.put("SF", SF_Code);
            Call<ResponseBody> sstatus = apiService.leaveStatus(js.toString());
            sstatus.enqueue(new Callback<ResponseBody>() {
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
                        Log.v("printing_date_status", is.toString());
                        JSONArray json = new JSONArray(is.toString());
                        arrElig.clear();
                        arrTkn.clear();
                        arrAva.clear();
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject jsonObject = json.getJSONObject(i);

                            JSONArray jsonArray = jsonObject.getJSONArray("values");
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jss = jsonArray.getJSONObject(j);
                                if(jsonObject.getString("Name").equalsIgnoreCase("Eligibility"))
                                    arrElig.add(new LoadBitmap(jss.getString("Name"), jss.getString("Value")));
                                if(jsonObject.getString("Name").equalsIgnoreCase("Taken"))
                                    arrTkn.add(new LoadBitmap(jss.getString("Name"), jss.getString("Value")));
                                if(jsonObject.getString("Name").equalsIgnoreCase("Available"))
                                    arrAva.add(new LoadBitmap(jss.getString("Name"), jss.getString("Value")));

                            }
                        }

                            leaveAdapterE = new LeaveAdapter(LeaveActivity.this, arrElig);
                            list_elig.setAdapter(leaveAdapterE);
                            leaveAdapterT = new LeaveAdapter(LeaveActivity.this, arrTkn);
                            list_taken.setAdapter(leaveAdapterT);
                            leaveAdapterA = new LeaveAdapter(LeaveActivity.this, arrAva);
                            list_avail.setAdapter(leaveAdapterA);
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonUtilsMethods.isOnline(LeaveActivity.this)) {
//               for (int i = 0; i < arrAva.size(); i++) {
//                    if (!txt_day.getText().toString().isEmpty()) {
//                        if(arrAva.get(i).getBrdName().equalsIgnoreCase(leaveType)) {
//                            if (Integer.parseInt(arrAva.get(i).getTiming()) >= Integer.parseInt(txt_day.getText().toString())) {
                    svjson = new JSONObject();

                    try {
                        JSONObject json = new JSONObject();
                        json.put("SF", SF_Code);
                        json.put("Fdt", edt_from.getText().toString());
                        json.put("Tdt", edt_to.getText().toString());
                        json.put("LTy", leaveType);
                        Log.v("dateValidation", json.toString());


                        svjson.put("SF", SF_Code);
                        svjson.put("FDate", edt_from.getText().toString());
                        svjson.put("TDate", edt_to.getText().toString());
                        svjson.put("LeaveType", leaveType);
                        svjson.put("NOD", txt_day.getText().toString());
                        svjson.put("LvOnAdd", edt_add.getText().toString());
                        svjson.put("LvRem", edt_reason.getText().toString());


                        Call<ResponseBody> dateVal = apiService.dateValidation(json.toString());
                        dateVal.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                JSONObject jsonObject = null;

                                InputStreamReader ip = null;
                                StringBuilder is = new StringBuilder();
                                String line = null;
                                try {
                                    ip = new InputStreamReader(response.body().byteStream());
                                    BufferedReader bf = new BufferedReader(ip);

                                    while ((line = bf.readLine()) != null) {
                                        is.append(line);
                                    }
                                    Log.v("printing_date_valid", is.toString());
                                    JSONArray js = new JSONArray(is.toString());
                                    for (int i = 0; i < js.length(); i++) {
                                        JSONObject jo = js.getJSONObject(i);
                                        if (TextUtils.isEmpty(jo.getString("Msg"))) {

                                            if (!TextUtils.isEmpty(txt_day.getText().toString())) {
                                                if (!TextUtils.isEmpty(edt_reason.getText().toString())) {
                                                    Call<ResponseBody> dateVal = apiService.saveLeave(svjson.toString());
                                                    dateVal.enqueue(new Callback<ResponseBody>() {
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
                                                                Log.v("printing_date_save", is.toString());
                                                                JSONObject jj = new JSONObject(is.toString());
                                                                if (jj.getString("success").equalsIgnoreCase("true")) {

                                                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.leave_success), Toast.LENGTH_LONG).show();
                                                                    txt_day.setText("");
                                                                    edt_from.setText("");
                                                                    edt_to.setText("");
                                                                    edt_add.setText("");
                                                                    edt_reason.setText("");
                                                                    Intent intent = new Intent(LeaveActivity.this, HomeDashBoard.class);
                                                                    startActivity(intent);
                                                                    // selectedLeave.clear();
                                                                    // arrayAdapter.notifyDataSetChanged();
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.leave_already), Toast.LENGTH_LONG).show();
                                                                }

                                    /* JSONArray js=new JSONArray(is.toString());
                                     for(int i=0;i<js.length();i++){
                                         JSONObject jo=js.getJSONObject(i);
                                         if(TextUtils.isEmpty(jo.getString("Msg"))){

                                         }
                                         else{
                                             // Toast.makeText(getContext(),jo.getString("Msg"),Toast.LENGTH_SHORT).show();
                                             edt_to.setText("");
                                             txt_day.setText("");
                                             //Toast.makeText(LeaveActivity.this,jo.getString("Msg"),Toast.LENGTH_SHORT).show();
                                         }
                                     }*/
                                                            } catch (Exception e) {

                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.choose_date), Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                                else
                                            {
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.ent_lvreason), Toast.LENGTH_SHORT).show();
                                            }


                                        } else {
                                            // Toast.makeText(getContext(),jo.getString("Msg"),Toast.LENGTH_SHORT).show();
                                            if(status.equalsIgnoreCase("2") || status.equalsIgnoreCase("3") )
                                            {
                                                Toast.makeText(getApplicationContext(), jo.getString("Msg"), Toast.LENGTH_SHORT).show();
                                            }else {
                                                edt_to.setText("");
                                                txt_day.setText("");
                                                Toast.makeText(getApplicationContext(), jo.getString("Msg"), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e("the vALID", "================" + e.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });


                    } catch (Exception e) {

                    }

                           }
                         else {
                              Toast.makeText(LeaveActivity.this, getResources().getString(R.string.offline), Toast.LENGTH_SHORT).show();
                         }
//                        }
//                    }
//                }

            }
        });

//        lay_cl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.v("txt_get_color", String.valueOf(txt_cl.getCurrentTextColor()));
//
//
//                if (txt_cl.getCurrentTextColor() == -16777216) {
//                    txt_cl.setBackgroundColor(Color.BLACK);
//                    txt_cl.setTextColor(Color.WHITE);
//                    txt_pl.setBackgroundColor(Color.TRANSPARENT);
//                    txt_sl.setBackgroundColor(Color.TRANSPARENT);
//                    txt_lop.setBackgroundColor(Color.TRANSPARENT);
//                    txt_pl.setTextColor(Color.BLACK);
//                    txt_sl.setTextColor(Color.BLACK);
//                    txt_lop.setTextColor(Color.BLACK);
//
////                    txt_el.setBackgroundColor(Color.TRANSPARENT);
////                    txt_el.setTextColor(Color.BLACK);
//
//
//                    leaveType = "CL";
//
//
//                } else {
//                    /*txt_cl.setBackgroundColor(Color.TRANSPARENT);
//                    txt_cl.setTextColor(Color.BLACK);*/
//                }
//            }
//        });
//
//        lay_pl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.v("txt_get_color", String.valueOf(txt_pl.getCurrentTextColor()));
//
//
//                if (txt_pl.getCurrentTextColor() == -16777216) {
//                    txt_pl.setBackgroundColor(Color.BLACK);
//                    txt_pl.setTextColor(Color.WHITE);
//                    txt_cl.setBackgroundColor(Color.TRANSPARENT);
//                    txt_sl.setBackgroundColor(Color.TRANSPARENT);
//                    txt_lop.setBackgroundColor(Color.TRANSPARENT);
//                    txt_cl.setTextColor(Color.BLACK);
//                    txt_sl.setTextColor(Color.BLACK);
//                    txt_lop.setTextColor(Color.BLACK);
//
////                    txt_el.setBackgroundColor(Color.TRANSPARENT);
////                    txt_el.setTextColor(Color.BLACK);
//
//
//                    leaveType = "PL";
//
//
//                } else {
//                    /*txt_pl.setBackgroundColor(Color.TRANSPARENT);
//                    txt_pl.setTextColor(Color.BLACK);*/
//                }
//            }
//        });
//
//        lay_sl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.v("txt_get_color", String.valueOf(txt_sl.getCurrentTextColor()));
//
//
//                if (txt_sl.getCurrentTextColor() == -16777216) {
//                    txt_sl.setBackgroundColor(Color.BLACK);
//                    txt_sl.setTextColor(Color.WHITE);
//                    txt_pl.setBackgroundColor(Color.TRANSPARENT);
//                    txt_pl.setTextColor(Color.BLACK);
//                    txt_cl.setBackgroundColor(Color.TRANSPARENT);
//                    txt_cl.setTextColor(Color.BLACK);
//                    txt_lop.setBackgroundColor(Color.TRANSPARENT);
//                    txt_lop.setTextColor(Color.BLACK);
//
////                    txt_el.setBackgroundColor(Color.TRANSPARENT);
////                    txt_el.setTextColor(Color.BLACK);
//
//
//                    leaveType = "SL";
//
//
//                } else {
//                    /*txt_sl.setBackgroundColor(Color.TRANSPARENT);
//                    txt_sl.setTextColor(Color.BLACK);*/
//                }
//            }
//        });
//
//        lay_lop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.v("txt_get_color", String.valueOf(txt_lop.getCurrentTextColor()));
//
//
//                if (txt_lop.getCurrentTextColor() == -16777216) {
//                    txt_lop.setBackgroundColor(Color.BLACK);
//                    txt_lop.setTextColor(Color.WHITE);
//                    txt_pl.setBackgroundColor(Color.TRANSPARENT);
//                    txt_sl.setBackgroundColor(Color.TRANSPARENT);
//                    txt_cl.setBackgroundColor(Color.TRANSPARENT);
//                    txt_cl.setTextColor(Color.BLACK);
//                    txt_sl.setTextColor(Color.BLACK);
//                    txt_pl.setTextColor(Color.BLACK);
//
////                    txt_el.setBackgroundColor(Color.TRANSPARENT);
////                    txt_el.setTextColor(Color.BLACK);
//
//
//                    leaveType = "LOP";
//
//                } else {
//                   /* txt_lop.setBackgroundColor(Color.TRANSPARENT);
//                    txt_lop.setTextColor(Color.BLACK);*/
//                }
//            }
//        });


        edt_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from = true;
                DialogFragment dialogfragment = new DatePickerDialogTheme();
                dialogfragment.show(getFragmentManager(), "Theme");
                DatePickerDialogTheme.fun(getApplicationContext());
            }
        });
        edt_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from = false;
                DialogFragment dialogfragment = new DatePickerDialogTheme();
                dialogfragment.show(getFragmentManager(), "Theme");
                DatePickerDialogTheme.fun(getApplicationContext());
            }
        });

        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoftKeyboard(edt_add);
            }
        });
        txt_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoftKeyboard(edt_reason);
            }
        });
        iv_dwnldmaster_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LeaveActivity.this, HomeDashBoard.class);
                startActivity(i);
            }
        });
        commonFun();
        bindList(new UpdateUi() {
            @Override
            public void updatingui() {
                commonFun();
            }
        });
    }

    private void leaveTypes() {
        try {
            JSONObject jss = new JSONObject();
            jss.put("div", div_Code);
            Call<ResponseBody> leaveType = apiService.getLeaveType(jss.toString());
            leaveType.enqueue(new Callback<ResponseBody>() {
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
                        Log.v("printing_leave_types", is.toString());
                        JSONArray json = new JSONArray(is.toString());
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject js = json.getJSONObject(i);

                             leaveCode = js.getString("Leave_code");
                             leavetype= js.getString("Leave_SName");
                             leaveName = js.getString("Leave_Name");
                             aw_flag = js.getString("Active_Flag");

                             selectedLeave.add(leavetype);

                             Log.v("getResults",js.toString());
                        }
//                        arrayAdapter = new ArrayAdapter<String>(LeaveActivity.this, android.R.layout.simple_spinner_item, selectedLeave);
//                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinner.setAdapter(arrayAdapter);

                        arrayAdapter = new ArrayAdapter<String>(LeaveActivity.this,android.R.layout.simple_spinner_item, selectedLeave);
                        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinner.setAdapter(arrayAdapter);

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
        }
    }

    public void commonFun() {
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public static long getDaysBetweenDates(String start, String end) {
        String DATE_FORMAT = "d/M/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.v("printing_nu", String.valueOf(numberOfDays + 1));
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    public static class DatePickerDialogTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        static Context context;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = 0, month = 0, day = 0;
            if (from) {
                if(mCommonSharedPreference.getValueFromPreference("past_leave_post").equalsIgnoreCase("0"))
                {
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                            AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
                    datepickerdialog.getDatePicker().setCalendarViewShown(false);
                    long value = System.currentTimeMillis();
                    Log.v("printing_time", value + "");
                    return datepickerdialog;
                }else {
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    //day = calendar.get(Calendar.DAY_OF_MONTH);

                    if (!mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_NAME).isEmpty())

                        day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
                    else
                        day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                            AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
                    datepickerdialog.getDatePicker().setCalendarViewShown(false);
                    if (!mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_NAME).isEmpty())
                        datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() + (24 * 60 * 60 * 1000) - 1000);//where 24*60*60*1000 represents the total timestamp for one day
                    else
                        datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    long value = System.currentTimeMillis();
                    Log.v("printing_time", value + "");
                    return datepickerdialog;
                }
            } else {
                String data = edt_from.getText().toString();
                if (data.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.selctdate), Toast.LENGTH_SHORT).show();
                    return null;
                } else {
                    String[] arrSplit = data.split("-");
                    year = Integer.parseInt(arrSplit[0]);
                    month = Integer.parseInt(arrSplit[1]) - 1;
                    day = Integer.parseInt(arrSplit[2]);

                    Calendar calendarrr = Calendar.getInstance();
                    calendarrr.set(year, month, day);

                    DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                            AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
                    datepickerdialog.getDatePicker().setCalendarViewShown(false);
                    datepickerdialog.getDatePicker().setMinDate(calendarrr.getTimeInMillis());
                    long value = System.currentTimeMillis();
                    Log.v("printing_time", value + "");
                    return datepickerdialog;
                }

            }


//for one


            /*DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);*/
//            datepickerdialog.getDatePicker().setCalendarViewShown(false);
//            long    value=System.currentTimeMillis();
//            Log.v("printing_time",value+"");
            //datepickerdialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

//for two

           /* DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);*/

//for three
           /*
DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this,year,month,day);
// for four

DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_DARK,this,year,month,day);


//for five
 DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);


//for six
DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_TRADITIONAL,this,year,month,day);

            */


            //  return datepickerdialog;
        }

        public static void fun(Context context1) {
            context = context1;
        }


        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

              Log.v("date_printt", i + " mnth" + i1 + " yr " + i2);
            updateUi.updatingui();
            if (from) {
                edt_from.setText(i + "-" + (i1 + 1) + "-" + i2);
            } else {
                edt_to.setText(i + "-" + (i1 + 1) + "-" + i2);
                if (leavestatus.equalsIgnoreCase("0")) {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("SF", SF_Code);
                        json.put("Fdt", edt_from.getText().toString());
                        json.put("Tdt", edt_to.getText().toString());
                        json.put("LTy", leaveType);
                        Log.v("jspon_dateeee", json.toString());
                        Call<ResponseBody> dateVal = apiService.dateValidation(json.toString());
                        dateVal.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                                    Log.v("printing_date_valid", is.toString());
                                    JSONArray js = new JSONArray(is.toString());
                                    for (int i = 0; i < js.length(); i++) {
                                        JSONObject jo = js.getJSONObject(i);
                                        if (TextUtils.isEmpty(jo.getString("Msg"))) {

                                        } else {
                                            // Toast.makeText(getContext(),jo.getString("Msg"),Toast.LENGTH_SHORT).show();
                                            edt_to.setText("");
                                            txt_day.setText("");
                                            Toast.makeText(context, jo.getString("Msg"), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    } catch (Exception e) {
                    }
                }
            }
            if (!TextUtils.isEmpty(edt_from.getText().toString()) && !TextUtils.isEmpty(edt_to.getText().toString())) {
                //getDaysBetweenDates(edt_from.getText().toString(),edt_to.getText().toString());
                // diffff(edt_from.getText().toString(),edt_to.getText().toString());
                int count = getCountOfDays(edt_from.getText().toString(), edt_to.getText().toString());
                if (count > 0) {
                    txt_day.setText(String.valueOf(getCountOfDays(edt_from.getText().toString(), edt_to.getText().toString())));

                } else {
                    Toast.makeText(context, getResources().getString(R.string.togreater), Toast.LENGTH_SHORT).show();
                    edt_to.setText("");
                }
            }
            //getDaysBetweenDates()

        }
    }

    public static void diffff(String st, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date stdate = sdf.parse(st);
            Date enddate = sdf.parse(end);

            long diff = stdate.getTime() - enddate.getTime();
            float days = (diff / (1000 * 60 * 60 * 24));
            System.out.println("Days_soonn: " + days);
            //System.out.println("Days_soonn: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
        }
    }

    public static int getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

//        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

//        } else {
//            Calendar cCal = Calendar.getInstance();
//            cCal.setTime(todayWithZeroTime);
//            cYear = cCal.get(Calendar.YEAR);
//            cMonth = cCal.get(Calendar.MONTH);
//            cDay = cCal.get(Calendar.DAY_OF_MONTH);
//        }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);
        Log.v("date_count", String.valueOf(dayCount));

        return ((int) (dayCount + 1));
    }

    public void showSoftKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        view.setFocusable(true);
        view.isFocusable();
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

    }

    public static void bindList(UpdateUi ui) {
        updateUi = ui;
    }



}
