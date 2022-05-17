package saneforce.sanclm.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelDynamicList;
import saneforce.sanclm.activities.Model.ModelDynamicView;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.adapter_class.AdapterDynamicActivity;
import saneforce.sanclm.adapter_class.AdapterDynamicView;
import saneforce.sanclm.adapter_class.AdapterForDynamicSelectionList;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.GPSTrack;
import saneforce.sanclm.applicationCommonFiles.ImageFilePath;
import saneforce.sanclm.util.SpecialityListener;
import saneforce.sanclm.util.TwoTypeparameter;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DynamicActivity extends AppCompatActivity {
    ListView list, list_view;
    CommonSharedPreference mCommonSharedPreference;
    String db_connPath;
    Api_Interface apiService;
    ArrayList<ModelDynamicList> array = new ArrayList<>();
    ArrayList<ModelDynamicView> array_view = new ArrayList<>();
    CommonUtilsMethods commonUtilsMethods;
    ProgressDialog progressDialog = null;
    LinearLayout lin_view;
    RelativeLayout rlay_view, rlay_tp, main_lays;
    TimePicker tp;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat dateFormatter;
    TimePickerDialog timePickerDialog;
    AdapterDynamicView adp_view;
    int pos_upload_file = 0;
    boolean isEmpty = false;
    SearchView search_view;
    AdapterDynamicActivity adp;
    ImageView iv_dwnldmaster_back;
    Button btn_save;
    boolean keypadStatus = false;
    private FusedLocationProviderClient mFusedLocationClient;
    GPSTrack gpsTrack;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
     String latitude, longitude;
    protected boolean gps_enabled, network_enabled;

    @Override
    public void onBackPressed() {
        Log.v("on_back_press", "are_clicked");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        list_view = (ListView) findViewById(R.id.list_view);
        list = (ListView) findViewById(R.id.list);
        search_view = (SearchView) findViewById(R.id.search_view);
        btn_save = (Button) findViewById(R.id.btn_save);
        mCommonSharedPreference = new CommonSharedPreference(this);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        commonUtilsMethods = new CommonUtilsMethods(DynamicActivity.this);
        progressDialog = commonUtilsMethods.createProgressDialog(DynamicActivity.this);
        lin_view = (LinearLayout) findViewById(R.id.lin_view);
        rlay_view = (RelativeLayout) findViewById(R.id.rlay_view);
        main_lays = (RelativeLayout) findViewById(R.id.main_lays);
        iv_dwnldmaster_back = (ImageView) findViewById(R.id.iv_dwnldmaster_back);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        progressDialog.show();

        callDynamicList();
//        requestPermission();

//        getLocation();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        gpsTrack = new GPSTrack(this);
        latitude=String.valueOf(gpsTrack.getLatitude());
        longitude=String.valueOf(gpsTrack.getLongitude());
        Log.v("markerrrrr_ss", latitude + " " + longitude);

        AdapterDynamicView.bindListernerForDateRange(new TwoTypeparameter() {
            @Override
            public void update(int value, int pos) {
                if (value == 5) {
                    pos_upload_file = pos;
                    uploadFile();

                } else if (value > 5 && value < 10) {
                    datePick(pos, value);
                } else
                    timePicker(pos, value);
            }
        });
        AdapterDynamicActivity.bindDynamicListListner(new SpecialityListener() {
            @Override
            public void specialityCode(String code) {
                progressDialog.show();
                callDynamicViewList(code);
            }
        });
        main_lays.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                main_lays.getWindowVisibleDisplayFrame(r);
                int heightDiff = main_lays.getRootView().getHeight() - main_lays.getHeight();
                //Log.e("MyActivity", "keyboard opened");
                int screenHeight = main_lays.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                Log.d("keypad_jk", "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) {
                    Log.e("MyActivity", "keyboard opened");
                    keypadStatus = true;
                } else {
                    Log.e("MyActivity", "keyboard closed");
                    if (keypadStatus) commonFun();
                }
                  /*  if (heightDiff > 100) {
                    Log.e("MyActivity", "keyboard opened");
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    Log.e("MyActivity", "keyboard closed"+imm.isActive());
                    //commonFun();
                }*/
            }
        });
/*
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("postion_view_id",array.get(position).getName()+"   printing_pos"+position);
                progressDialog.show();
                callDynamicViewList(array.get(position).getId());
            }
        });
*/
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (commonUtilsMethods.isOnline(DynamicActivity.this)) {
                    if (validationOfField())
                        saveEntry();
                    else
                        Toast.makeText(DynamicActivity.this, getResources().getString(R.string.fillmand), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(DynamicActivity.this, getResources().getString(R.string.nonetwrk), Toast.LENGTH_SHORT).show();


            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("postion_view_id", array_view.get(position).getViewid());
                if (array_view.get(position).getViewid().equalsIgnoreCase("8") ||
                        array_view.get(position).getViewid().equalsIgnoreCase("12") ||
                        array_view.get(position).getViewid().equalsIgnoreCase("13")) {
                    popupSpinner(0, array_view.get(position).getA_list(), position);
                } else if (array_view.get(position).getViewid().equalsIgnoreCase("9")) {
                    popupSpinner(1, array_view.get(position).getA_list(), position);
                } else if (array_view.get(position).getViewid().equalsIgnoreCase("6")) {
                    // tp.setVisibility(View.VISIBLE);
                    timePicker(position, 8);
                } else if (array_view.get(position).getViewid().equalsIgnoreCase("4")) {
                    // tp.setVisibility(View.VISIBLE);
                    datePick(position, 8);


                } else if (array_view.get(position).getViewid().equalsIgnoreCase("10"))
                    uploadFile();
               /* else    if(array_view.get(position).getViewid().equalsIgnoreCase("10")){
                   // tp.setVisibility(View.VISIBLE);
                    datePick(position);
                }*/

            }
        });
        iv_dwnldmaster_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DynamicActivity.this, HomeDashBoard.class);
                startActivity(i);
                finish();
            }
        });
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view.setIconified(false);
                InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(search_view, 0);
            }
        });
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.v("search_view_str", s);
                if(adp!=null)
                adp.getFilter().filter(s);
                return false;
            }
        });
        commonFun();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);

    }


    private void getLocation() {
        mFusedLocationClient  = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(DynamicActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(DynamicActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                latitude=String.valueOf(location.getLatitude());
                longitude=String.valueOf(location.getLongitude());

                Log.v("GETTING_LOCATION", latitude);
                Log.v("GETTING_LOCATION", longitude);
            }
        });

    }


    public void uploadFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 7);
    }

    public void datePick(final int pos, final int value) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(DynamicActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ModelDynamicView mm = array_view.get(pos);
                int mnth = monthOfYear + 1;
                if (mm.getViewid().equalsIgnoreCase("5")) {
                    if (value == 8) {
                        if (TextUtils.isEmpty(mm.getTvalue()))
                            mm.setValue(dayOfMonth + "-" + mnth + "-" + year);
                        else {
                            String val = dayOfMonth + "-" + mnth + "-" + year;
                            if (dateDifference(val, mm.getTvalue()) < 0)
                                Toast.makeText(DynamicActivity.this, getResources().getString(R.string.datelesser), Toast.LENGTH_SHORT).show();
                            else
                                mm.setValue(dayOfMonth + "-" + mnth + "-" + year);

                        }
                    } else {
                        if (TextUtils.isEmpty(mm.getValue()))
                            Toast.makeText(DynamicActivity.this, getResources().getString(R.string.filldate), Toast.LENGTH_SHORT).show();
                        else {
                            String val = dayOfMonth + "-" + mnth + "-" + year;
                            if (dateDifference(mm.getValue(), val) < 0)
                                Toast.makeText(DynamicActivity.this, getResources().getString(R.string.togreater), Toast.LENGTH_SHORT).show();
                            else
                                mm.setTvalue(dayOfMonth + "-" + mnth + "-" + year);
                        }


                    }
                } else
                    mm.setValue(dayOfMonth + "-" + mnth + "-" + year);

                adp_view.notifyDataSetChanged();
                commonFun();
           /* Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);*/
                // txt_fdate.setText(dateFormatter.format(newDate.getTime()));
                //fdate=txt_fdate.getText().toString();

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }

    public void timePicker(final int pos, final int value) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(DynamicActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                ModelDynamicView mm = array_view.get(pos);
                if (mm.getViewid().equalsIgnoreCase("7")) {
                    if (value == 12) {
                        if (TextUtils.isEmpty(mm.getTvalue()))
                            mm.setValue(selectedHour + ":" + selectedMinute);
                        else {
                            int thr = spiltTime(mm.getTvalue());
                            int tmin = spiltMin(mm.getTvalue());
                            if (thr == selectedHour) {
                                if (selectedMinute < tmin) {
                                    mm.setValue(selectedHour + ":" + selectedMinute);
                                } else
                                    Toast.makeText(DynamicActivity.this, getResources().getString(R.string.timelesser), Toast.LENGTH_SHORT).show();
                            } else if (thr > selectedHour) {
                                mm.setValue(selectedHour + ":" + selectedMinute);
                            } else
                                Toast.makeText(DynamicActivity.this, getResources().getString(R.string.timelesser), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        if (TextUtils.isEmpty(mm.getValue()))
                            Toast.makeText(DynamicActivity.this, getResources().getString(R.string.filltime), Toast.LENGTH_SHORT).show();
                        else {
                            int fhr = spiltTime(mm.getValue());
                            int fmin = spiltMin(mm.getValue());
                            if (fhr == selectedHour) {
                                if (selectedMinute > fmin) {
                                    mm.setTvalue(selectedHour + ":" + selectedMinute);
                                } else
                                    Toast.makeText(DynamicActivity.this, getResources().getString(R.string.totimegreater), Toast.LENGTH_SHORT).show();
                            } else if (fhr < selectedHour) {
                                mm.setTvalue(selectedHour + ":" + selectedMinute);
                            } else
                                Toast.makeText(DynamicActivity.this, getResources().getString(R.string.totimegreater), Toast.LENGTH_SHORT).show();

                        }
                    }

                } else
                    mm.setValue(selectedHour + ":" + selectedMinute);


                adp_view.notifyDataSetChanged();
                commonFun();

            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }

    public int spiltTime(String val) {
        String v = val.substring(0, val.indexOf(":"));
        return Integer.parseInt(v);
    }

    public int spiltMin(String val) {
        String v = val.substring(val.indexOf(":") + 1);
        return Integer.parseInt(v);
    }

    public long dateDifference(String d1, String d2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        try {
            Date date1 = simpleDateFormat.parse(d1 + " 00:00:00");
            Date date2 = simpleDateFormat.parse(d2 + " 00:00:00");

            long different = date2.getTime() - date1.getTime();
            Log.v("priting_date_diffence", different + "");
            return different;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void timePick() {
        TimePicker timePicker = new TimePicker(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);
        timePicker.setLayoutParams(layoutParams);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                String format = "";
                if (hourOfDay == 0) {
                    hourOfDay += 12;
                    format = "AM";
                } else if (hourOfDay == 12) {
                    format = "PM";
                } else if (hourOfDay > 12) {
                    hourOfDay -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }

                /*if (textView != null) {
                    String hour = String.valueOf(hourOfDay < 10 ? "0" + hourOfDay : hourOfDay);
                    String min = String.valueOf(minute < 10 ? "0" + minute : minute);

                    String text = getString(R.string.selected_time) + " " + hour + " : " + min + " " + format;
                    textView.setText(text);
                    textView.setVisibility(View.VISIBLE);
                }*/
            }
        });
        rlay_view.addView(timePicker);


    }

    public void callDynamicList() {
        JSONObject json = new JSONObject();
        try {
            json.put("div", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            Log.v("printing_sf_code", json.toString());
            Call<ResponseBody> approval = apiService.getActivity(json.toString());

            approval.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track", response.body().byteStream() + "");
                        Log.v("datawises", response.body().toString());
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
                            array.clear();
                            Log.v("printing_here_dynamic", is.toString());
                            JSONArray jsonArray = new JSONArray(is.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                array.add(new ModelDynamicList(json.getString("Activity_Name"), json.getString("Activity_SlNo"), false,"",""));
                            }
                            adp = new AdapterDynamicActivity(DynamicActivity.this, array,"activity");
                            list_view.setAdapter(adp);
                            adp.notifyDataSetChanged();
                            progressDialog.dismiss();
                            commonFun();


                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });

        } catch (Exception e) {
        }

    }

    public void callsave(String json) {

        try {

            Call<ResponseBody> approval = apiService.svdcrAct(json);

            approval.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track", response.body().byteStream() + "");
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
                            Log.v("printing_here_dynamic", is.toString());
                            JSONObject js = new JSONObject(is.toString());
                            if (js.getString("success").equalsIgnoreCase("true")) {
                                progressDialog.dismiss();
                                commonFun();
                                Toast.makeText(DynamicActivity.this, getResources().getString(R.string.datasave), Toast.LENGTH_SHORT).show();
                                array_view.clear();
                                adp_view.notifyDataSetChanged();
                            } else {
                                progressDialog.dismiss();
                                commonFun();
                                Toast.makeText(DynamicActivity.this, getResources().getString(R.string.cannotload), Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
        }

    }

    public void callDynamicViewList(String slno) {
        final JSONObject json = new JSONObject();
        try {
            json.put("slno", slno);
            json.put("SF", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
            json.put("div", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            Log.v("printing_sf_code", json.toString());
            Call<ResponseBody> approval = apiService.getDynamicViewTest(json.toString());

            approval.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track", response.body().byteStream() + "");
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
                            array_view.clear();
                            Log.v("printing_dynamic_view", is.toString());
                            JSONArray jsonArray = new JSONArray(is.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ArrayList<PopFeed> arr = new ArrayList<>();
                                JSONObject json = jsonArray.getJSONObject(i);
                                JSONArray jarray = json.getJSONArray("input");
                                if (jarray.length() != 0) {
                                    for (int m = 0; m < jarray.length(); m++) {
                                        JSONObject jjss = jarray.getJSONObject(m);
                                        Log.v("json_input_iss", jjss.getString(json.getString("Table_code")));
                                        arr.add(new PopFeed(jjss.getString(json.getString("Table_name")), false, jjss.getString(json.getString("Table_code"))));
                                    }

                                }
                                String para = "";
                                if (json.getString("Control_Id").equalsIgnoreCase("11"))
                                    para = json.getString("Table_code");
                                else
                                    para = json.getString("Control_Para");
                                array_view.add(new ModelDynamicView(json.getString("Control_Id"), "", json.getString("Field_Name"), "", arr, para, json.getString("Creation_Id"), json.getString("Activity_SlNo"), "", json.getString("Mandatory")));

//                                if(json.getString("Control_Id").equalsIgnoreCase("17"))
//                                {
//                                    para = json.getString("Control_Para");
//                                    array_view.add(new ModelDynamicView(json.getString("Control_Id"), "", json.getString("Field_Name"), "", arr, para, json.getString("Creation_Id"), json.getString("Activity_SlNo"), "", json.getString("Mandatory")));
//                                }
                            }

                            adp_view = new AdapterDynamicView(array_view, DynamicActivity.this,latitude,longitude);
                            list.setAdapter(adp_view);
                            adp_view.notifyDataSetChanged();
                            progressDialog.dismiss();
                            commonFun();

                        } catch (Exception e) {
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
        }

    }

    public void popupSpinner(int type, final ArrayList<PopFeed> array_selection, final int pos) {
        final Dialog dialog = new Dialog(DynamicActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_dynamic_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);
        TextView tv_todayplan_popup_head = (TextView) dialog.findViewById(R.id.tv_todayplan_popup_head);
        tv_todayplan_popup_head.setText(array_view.get(pos).getFieldname());
        ImageView iv_close_popup = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        EditText search_edit=(EditText)  dialog.findViewById(R.id.et_search);


        if (array_selection.contains(new PopFeed(true))) {
            isEmpty = false;
        } else
            isEmpty = true;

        final AdapterForDynamicSelectionList adapt = new AdapterForDynamicSelectionList(DynamicActivity.this, array_selection, type);
        popup_list.setAdapter(adapt);
        final SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view.setIconified(false);
                InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(search_view, 0);
            }
        });
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.v("search_view_str", s);
                adapt.getFilter().filter(s);
                return false;
            }
        });

        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapt.getFilter().filter(s);
                adapt.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty) {
                    if (array_selection.contains(new PopFeed(true))) {
                        for (int i = 0; i < array_selection.size(); i++) {
                            PopFeed m = array_selection.get(i);
                            m.setClick(false);
                        }
                    }
                }
                dialog.dismiss();
                commonFun();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array_selection.contains(new PopFeed(true))) {
                    for (int i = 0; i < array_selection.size(); i++) {
                        PopFeed m = array_selection.get(i);
                        if (m.isClick()) {
                            array_view.get(pos).setValue(m.getTxt());
                            i = array_selection.size();
                            adp_view.notifyDataSetChanged();
                            break;
                        }
                    }

                } else {
                    array_view.get(pos).setValue("");
                    adp_view.notifyDataSetChanged();
                }
                dialog.dismiss();
                commonFun();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK && data.getData()!=null) {

                    Uri uri = data.getData();
                    ImageFilePath filepath = new ImageFilePath();
                    String fullPath = filepath.getPath(DynamicActivity.this, uri);
                    Log.v("file_path_are", fullPath + "print");
                    String PathHolder = data.getData().getPath();
                    /*String filePath = getImageFilePath(data);
                    Log.v("file_path_are",filePath);*/
                    if (fullPath == null)
                        Toast.makeText(DynamicActivity.this, getResources().getString(R.string.filenot_support), Toast.LENGTH_LONG).show();
                    else {
                        ModelDynamicView mm = array_view.get(pos_upload_file);
                        mm.setValue(fullPath);
                        adp_view.notifyDataSetChanged();
                    }

                    commonFun();

                }
                break;

        }
    }

    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) {
            Log.v("Camera_data_are", "are_empty");
            return getCaptureImageOutputUri().getPath();
        } else {
            Log.v("Camera_data_are", data.getData() + "");
            return getPathFromURI(data.getData());
        }

    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.png"));
        }
        return outputFileUri;
    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void saveEntry() {
        boolean isEmpty = false;
        int count = 0;
        progressDialog.show();
        if (array_view.contains(new ModelDynamicView("10"))) {
            for (int i = 0; i < array_view.size(); i++) {
                ModelDynamicView mm = array_view.get(i);
                if (mm.getViewid().equalsIgnoreCase("10")) {
                    if (!TextUtils.isEmpty(mm.getValue())) {
                        isEmpty = true;
                        count = count + 1;
                        getMulipart(mm.getValue(), i);
                    }
                }
            }
            saveValueEntry();
        } else {
            saveValueEntry();
        }

    }

    public void getMulipart(String path, int x) {
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field("MR0417");
        CallApiImage(values, imgg, x);
    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        Log.v("full_profile", path);
        try {
            if (!TextUtils.isEmpty(path)) {

                File file = new File(path);
                if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(new File(path));
                else
                    file = new File(path);
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
                yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
            }
        } catch (Exception e) {
        }
        Log.v("full_profile", yy + "");
        return yy;
    }

    public HashMap<String, RequestBody> field(String val) {
        HashMap<String, RequestBody> xx = new HashMap<String, RequestBody>();
        xx.put("data", createFromString(val));

        return xx;

    }

    private RequestBody createFromString(String txt) {
        return RequestBody.create(MultipartBody.FORM, txt);
    }

    public void CallApiImage(HashMap<String, RequestBody> values, MultipartBody.Part imgg, final int x) {
        Call<ResponseBody> Callto;

        Callto = apiService.uploadimg(values, imgg);

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());
                //uploading.setText("Uploading "+String.valueOf(count)+"/"+String.valueOf(count_check));

                try {
                    if (response.isSuccessful()) {


                        Log.v("print_upload_file_true", "ggg" + response);
                        JSONObject jb = null;
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("request_data_upload", String.valueOf(jsonData));
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            ModelDynamicView mm = array_view.get(x);
                            Log.v("printing_dynamic_cou", adp_view.DynamicCount + "    xxx" + x);
                            mm.setUpload_sv(js.getString("url"));
                            adp_view.notifyDataSetChanged();

                            if (adp_view.DynamicCount == x) {
                                saveValueEntry();
                            }
                        }else if(js.getString("success").equalsIgnoreCase("false"))
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context,js.getString("message"),Toast.LENGTH_LONG).show();

                        }


                    }


                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("print_failure", "ggg" + t.getMessage());
            }
        });
    }


    public void saveValueEntry() {
        if (array_view.size() != 0) {
            JSONArray ja = new JSONArray();
            JSONObject js = null;
            JSONObject finaljs = new JSONObject();
            String typ = "0", cust = "0", wt = "0", pl = "0", datasf = "", lat = "", lng = "", cusname = "";

            if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("cust_act"))) {
                try {
                    JSONObject jj = new JSONObject(mCommonSharedPreference.getValueFromPreference("cust_act").toString());
                    typ = jj.getString("typ");
                    cust = jj.getString("cust");
                    wt = jj.getString("wt");
                    pl = jj.getString("pl");
                    datasf = jj.getString("DataSF");
                    lat = jj.getString("lat");
                    lng = jj.getString("lng");
                    cusname = jj.getString("custname");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < array_view.size(); i++) {
                ModelDynamicView mm = array_view.get(i);

                try {
                    js = new JSONObject();
                    js.put("SF", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
                    js.put("div", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
                    if(mCommonSharedPreference.getValueFromPreference("DlyCtrl").equalsIgnoreCase("0") &&( !mCommonSharedPreference.getValueFromPreference("choosedEditDate").contains("Today")&&!mCommonSharedPreference.getValueFromPreference("choosedEditDate").equalsIgnoreCase("")&&!mCommonSharedPreference.getValueFromPreference("choosedEditDate").equalsIgnoreCase("null"))) {
                        js.put("act_date", mCommonSharedPreference.getValueFromPreference("choosedEditDate")+ " " + "00:00:00");
                        js.put("dcr_date", mCommonSharedPreference.getValueFromPreference("choosedEditDate")+ " " + "00:00:00");
                        js.put("update_time", mCommonSharedPreference.getValueFromPreference("choosedEditDate")+ " " +  "00:00:00");
                    }else {
                        js.put("act_date", CommonUtilsMethods.getCurrentInstance() + " " + CommonUtilsMethods.getCurrentTime());
                        js.put("dcr_date", CommonUtilsMethods.getCurrentInstance() + " " + "00:00:00");
                        js.put("update_time", CommonUtilsMethods.getCurrentInstance() + " " + CommonUtilsMethods.getCurrentTime());
                    }
                    js.put("slno", mm.getSlno());
                    js.put("ctrl_id", mm.getViewid());
                    js.put("creat_id", mm.getCreation_id());
                    js.put("WT", wt);
                    js.put("Pl", pl);
                    js.put("cus_code", cust);
                    js.put("lat", lat);
                    js.put("lng", lng);
                    js.put("cusname", cusname);
                    js.put("DataSF", datasf);
                    js.put("type", typ);

                    if (mm.getViewid().equalsIgnoreCase("8") || mm.getViewid().equalsIgnoreCase("9")
                            || mm.getViewid().equalsIgnoreCase("12") || mm.getViewid().equalsIgnoreCase("13")) {
                        mm.getA_list();
                        if (mm.getA_list().contains(new PopFeed(true))) {
                            String name = "", code = "";
                            for (int k = 0; k < mm.getA_list().size(); k++) {
                                PopFeed pf = mm.getA_list().get(k);
                                if (pf.isClick()) {
                                    if (!mm.getViewid().equalsIgnoreCase("9")) {
                                        name = name + "," + pf.getTxt();
                                        code = code + "," + pf.getCode();
                                        k = mm.getA_list().size();
                                        break;
                                    } else {
                                        name = name + "," + pf.getTxt();
                                        code = code + "," + pf.getCode();
                                    }

                                }
                            }
                            js.put("values", name);
                            js.put("codes", code);
                        }
                    } else {
                        if (mm.getViewid().equalsIgnoreCase("10"))
                            js.put("values", mm.getUpload_sv());
                        else
                            js.put("values", mm.getValue());
                        js.put("codes", "");
                    }
                    if(mm.getViewid().equalsIgnoreCase("17"))
                    {
                        js.put("values",mm.getLatValue());
                        js.put("codes",mm.getLngValue());
                    }else if(mm.getViewid().equalsIgnoreCase("7"))
                    {
                        js.put("values",mm.getValue());
                        js.put("codes",mm.getTvalue());
                    }
                } catch (Exception e) {
                }
                ja.put(js);
            }

            try {
                finaljs.put("val", ja);
                Log.v("printing_json_dynamic", finaljs.toString());
                callsave(finaljs.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void commonFun() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public boolean validationOfField() {
        boolean val = true;
        for (int i = 0; i < array_view.size(); i++) {
            ModelDynamicView mm = array_view.get(i);
            if (mm.getMandatory().equalsIgnoreCase("1") && (!mm.getViewid().equalsIgnoreCase("22"))) {
                if (mm.getViewid().equalsIgnoreCase("5") || mm.getViewid().equalsIgnoreCase("7")) {
                    if (TextUtils.isEmpty(mm.getTvalue()) || TextUtils.isEmpty(mm.getValue()))
                        return false;
                }else if(mm.getViewid().equalsIgnoreCase("17"))
                {
                    if (TextUtils.isEmpty(mm.getLatValue()) && TextUtils.isEmpty(mm.getLngValue()))
                    return false;
                } else {
                    if (TextUtils.isEmpty(mm.getValue()))
                        return false;
                }
            }
        }
        return val;
    }





//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        Log.v("keypad_bak","clicked");
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//
//        if (imm.isActive() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            Log.v("keypad_bak","clicked");
//            commonFun();
//        }
//        return false;
//    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.v("keypad_bak","clicked");
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Log.v("keypad_bak","clicked");
//            commonFun();
//            return true;
//        }
//        return false;
//    }
}
