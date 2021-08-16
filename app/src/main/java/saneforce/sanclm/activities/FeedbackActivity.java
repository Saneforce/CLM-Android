    package saneforce.sanclm.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.CompNameProduct;
import saneforce.sanclm.activities.Model.FeedbackProductDetail;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.activities.Model.SlideDetail;
import saneforce.sanclm.activities.Model.StoreImageTypeUrl;
import saneforce.sanclm.adapter_class.FeedCallJoinAdapter;
import saneforce.sanclm.adapter_class.FeedInputAdapter;
import saneforce.sanclm.adapter_class.FeedProductAdapter;
import saneforce.sanclm.adapter_class.PopupAdapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.ImageFilePath;
import saneforce.sanclm.applicationCommonFiles.LocationTrack;
import saneforce.sanclm.fragments.LocaleHelper;
import saneforce.sanclm.sqlite.DataBaseHandler;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;
import static saneforce.sanclm.fragments.AppConfiguration.licenceKey;

public class
FeedbackActivity extends AppCompatActivity {
    TextView availcheckbutton;
    FeedProductAdapter feedProductAdapter;
    FeedInputAdapter feedInputAdapter;
    FeedCallJoinAdapter feedCallJoinAdapter;
    ArrayList<String> prd_value = new ArrayList<>();
    ArrayList<SlideDetail> input_array = new ArrayList<>();
    ArrayList<String> joint_array = new ArrayList<>();
    ArrayList<String> call_array = new ArrayList<>();
    ArrayList<StoreImageTypeUrl> arr = new ArrayList<>();
    ArrayList<CompNameProduct> arrListing = new ArrayList<>();
    ProgressDialog progressDialog = null;
    ListView listView_feed_product, listView_feed_input, listView_feed_call, listView_feed_join;
    ArrayList<PopFeed> arr_pfb = new ArrayList<>();
    Button join_plus, call_plus, ip_plus;
    EditText edt_report;
    LinearLayout ll_report;
    String filePath = "";
    Button bt_cancel, prd_plus, prd_plus1;
    ArrayList<PopFeed> xx = new ArrayList<>();
    DataBaseHandler dbh;
    Cursor mCursor;
    int detectCount = 0;
    CommonSharedPreference mCommonSharedPreference;
    CommonUtilsMethods mCommonUtilsMethod;
    String prdNameDiff, referPrdName;
    ArrayList<FeedbackProductDetail> listFeedPrd = new ArrayList<>();
    Button btn_query;
    ArrayList<StoreImageTypeUrl> storeList;
    Button btn_brand_audit, submit;
    TextView txt_name, txt_pob;
    String drname;
    EditText edt_sample;
    int queryCount = 0;
    Api_Interface apiService;
    String baseurl;
    String jsonValue = null;
    int colId = 0;
    String feedOption;
    ArrayList<FeedbackProductDetail> sampleValue = new ArrayList<>();
    int repitationCount = 0;
    String jsonBrandValue = "";
    String peopleType, peopleCode, commonSFCode, SFCode,SF_Type;
    public static String TypePeople = "D";
    LocationTrack locationTrack;
    static RelativeLayout sign_lay;
    SignatureCanva sign_canva;
    String signPath = "";
    String defaulttime = "00:00:00";
    int val;
    String startT, endT;
    String finalPrdNam;
    JSONArray jsonFeed;
    String subDate = "";
    LinearLayout ll_feed_prd2, ll_feed_prd;
    String val_pob;
    TextView txt_sign;
    ImageView img_capture;
    Uri outputFileUri;
    String currentPhotoPath,AvailableAduitNeeded="",RcpaNeeded="";
    String nn = null;
    String language;
    Context context;
    Resources resources;

    SharedPreferences sharedPreferences, sharedPreferences2;

    ArrayList<StoreImageTypeUrl> arrayStore = new ArrayList<>();
    LinearLayout addcalllayout,availLayout;
   String availability=null,custype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        SharedPreferences sharedPreferences1 = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedPreferences1.getString(language_string, "");
        if (!language.equals("")){
            Log.d("homelang",language);
            selected(language);
            context = LocaleHelper.setLocale(FeedbackActivity.this, language);
            resources = context.getResources();
        }else {
            selected("en");
            context = LocaleHelper.setLocale(FeedbackActivity.this, "en");
            resources = context.getResources();
        }

        dbh = new DataBaseHandler(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle extra = getIntent().getExtras();
        feedOption = extra.getString("feedpage", null);
        sharedPreferences2 = getSharedPreferences("feed_list", 0);
        custype=sharedPreferences2.getString("type","");
        //        Log.v("options>>>>", custype);



        listView_feed_product = (ListView) findViewById(R.id.listView_feed_product);
        listView_feed_input = (ListView) findViewById(R.id.listView_feed_input);
        listView_feed_call = (ListView) findViewById(R.id.listView_feed_call);
        listView_feed_join = (ListView) findViewById(R.id.listView_feed_join);
        prd_plus = (Button) findViewById(R.id.prd_plus);
        prd_plus1 = (Button) findViewById(R.id.prd_plus1);
        join_plus = (Button) findViewById(R.id.join_plus);
        call_plus = (Button) findViewById(R.id.call_plus);
        ip_plus = (Button) findViewById(R.id.ip_plus);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        edt_report = (EditText) findViewById(R.id.edt_report);
        ll_report = (LinearLayout) findViewById(R.id.ll_report);
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_brand_audit = (Button) findViewById(R.id.btn_brand_audit);
        submit = (Button) findViewById(R.id.submit);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_pob = (TextView) findViewById(R.id.txt_pob);
        sign_lay = (RelativeLayout) findViewById(R.id.sign_lay);
        sign_canva = (SignatureCanva) findViewById(R.id.sign_canva);
        ll_feed_prd2 = (LinearLayout) findViewById(R.id.ll_feed_prd2);
        ll_feed_prd = (LinearLayout) findViewById(R.id.ll_feed_prd);
        txt_sign = (TextView) findViewById(R.id.txt_sign);
        img_capture = findViewById(R.id.img_capture);
        addcalllayout=findViewById(R.id.addcallLayout);
        availLayout=findViewById(R.id.availLayout);
        availcheckbutton=findViewById(R.id.availcheckbtn);


        mCommonSharedPreference = new CommonSharedPreference(FeedbackActivity.this);
        mCommonUtilsMethod = new CommonUtilsMethods(FeedbackActivity.this);
        baseurl = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SFCode = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        val_pob = mCommonSharedPreference.getValueFromPreference("feed_pob");
        SF_Type = mCommonSharedPreference.getValueFromPreference("sf_type");
        AvailableAduitNeeded = mCommonSharedPreference.getValueFromPreference("AvailableAduitNeeded");
        RcpaNeeded= mCommonSharedPreference.getValueFromPreference("RcpaNd");
        availability=mCommonSharedPreference.getValueFromPreference("availjson");
            Log.v("avail>>>1",availability);


        if(AvailableAduitNeeded.equals("1")&&feedOption.equals("chemist")){
            availLayout.setVisibility(View.VISIBLE);
            addcalllayout.setVisibility(View.GONE);
            btn_brand_audit.setVisibility(View.VISIBLE);

        }else if(AvailableAduitNeeded.equals("1")&&custype.equalsIgnoreCase("C")&&feedOption.equals("edit")) {
            availLayout.setVisibility(View.VISIBLE);
            addcalllayout.setVisibility(View.GONE);
            btn_brand_audit.setVisibility(View.VISIBLE);
        }

       else  if(RcpaNeeded.equals("1")&&feedOption.equals("dr")){
            btn_brand_audit.setVisibility(View.VISIBLE);

        }else if(RcpaNeeded.equals("1")&&custype.equalsIgnoreCase("D")&&feedOption.equals("edit")) {
            btn_brand_audit.setVisibility(View.VISIBLE);
        }

        else{
            availLayout.setVisibility(View.GONE);
            addcalllayout.setVisibility(View.VISIBLE);
            btn_brand_audit.setVisibility(View.GONE);


        }

        availcheckbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FeedbackActivity.this,AvailablityCheckActivity.class);
                intent.putExtra("availjson",availability);
                startActivity(intent);
            }
        });
        Log.v("toshow_sharepref", val_pob);

        if (mCommonSharedPreference.getValueFromPreference("addAct").equalsIgnoreCase("0"))
            txt_pob.setText("POB");

        defaulttime = mCommonSharedPreference.getValueFromPreference("slide_endtime");
        Log.v("print_base_url", baseurl + " time " + defaulttime + " feedPage " + feedOption);
        apiService = RetroClient.getClient(baseurl).create(Api_Interface.class);
        //if(!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("productFB")) && mCommonSharedPreference.getValueFromPreference("productFB").equalsIgnoreCase("0"))


        if (feedOption.equalsIgnoreCase("chemist")) {
            if (mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0")) {
                txt_pob.setText("POB");
            }
            if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("cpob")))
                txt_pob.setText(mCommonSharedPreference.getValueFromPreference("cpob"));

            /*dbh.open();
            mCommonSharedPreference.clearFeedShare();
            dbh.deleteFeed();
            dbh.close();*/

            call_plus.setEnabled(false);
            call_plus.getBackground().setAlpha(128);
//            btn_brand_audit.setVisibility(View.INVISIBLE);
            // txt_name.setText(extra.getString("customer"));
            txt_name.setText(CommonUtils.TAG_CHEM_NAME);
            peopleType = "C";
            peopleCode = CommonUtils.TAG_CHEM_CODE;
            commonSFCode = CommonUtils.TAG_FEED_SF_CODE;
            TypePeople = "C";

        } else if (feedOption.equalsIgnoreCase("edit")) {
            SharedPreferences share = getSharedPreferences("feed_list", 0);
            jsonValue = share.getString("val", null);
            colId = share.getInt("columnid", 0);

            if(share.contains("name")){
                nn = share.getString("name", null);
                if (nn != null) {
                    if (nn.contains("-")) {
                        nn = nn.substring(0, nn.indexOf("-"));
                        txt_name.setText(nn);
                    } else
                        txt_name.setText(nn);
                }
                Log.e("Doc_Name_feed",nn);
            }


            peopleType = share.getString("type", "");
            peopleCode = share.getString("code", "");
            Log.v("peoplecode", peopleCode + " " + peopleType);
            commonSFCode = share.getString("common", "");
            if (!peopleType.equalsIgnoreCase("D")) {
                if (peopleType.equalsIgnoreCase("C")) {
                    if (mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0"))
                        txt_pob.setText("POB");
                    if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("cpob")))
                        txt_pob.setText(mCommonSharedPreference.getValueFromPreference("cpob"));

                } else if (peopleType.equalsIgnoreCase("S")) {
                    if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("spob")))
                        txt_pob.setText(mCommonSharedPreference.getValueFromPreference("spob"));

                }
                else if (peopleType.equalsIgnoreCase("I")) {
                    if (mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0"))
                        txt_pob.setText("POB");
                    if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("cpob")))
                        txt_pob.setText(mCommonSharedPreference.getValueFromPreference("cpob"));

                }
                else {
                    if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("dpob")))
                        txt_pob.setText(mCommonSharedPreference.getValueFromPreference("dpob"));
                }
                call_plus.setEnabled(false);
                call_plus.getBackground().setAlpha(128);
//                btn_brand_audit.setVisibility(View.INVISIBLE);
                TypePeople = peopleType;
            } else {
                TypePeople = "D";
                if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("dpob")))
                    txt_pob.setText(mCommonSharedPreference.getValueFromPreference("dpob"));

            }
            SharedPreferences.Editor edit = share.edit();
            edit.clear();
            edit.commit();
            jsonExtract();
        } else if (feedOption.equalsIgnoreCase("undr")) {
            /*dbh.open();
            mCommonSharedPreference.clearFeedShare();
            dbh.deleteFeed();
            dbh.close();*/
            call_plus.setEnabled(false);
            call_plus.getBackground().setAlpha(128);
//            btn_brand_audit.setVisibility(View.INVISIBLE);
            /*txt_name.setText(extra.getString("customer"));*/
            txt_name.setText(CommonUtils.TAG_UNDR_NAME);
            peopleType = "U";
            peopleCode = CommonUtils.TAG_UNDR_CODE;
            commonSFCode = CommonUtils.TAG_FEED_SF_CODE;
            TypePeople = "U";
            if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("dpob")))
                txt_pob.setText(mCommonSharedPreference.getValueFromPreference("dpob"));

        } else if (feedOption.equalsIgnoreCase("stock")) {
            /*dbh.open();
            mCommonSharedPreference.clearFeedShare();
            dbh.deleteFeed();
            dbh.close();*/
            call_plus.setEnabled(false);
//            btn_brand_audit.setVisibility(View.INVISIBLE);
            call_plus.getBackground().setAlpha(128);
            txt_name.setText(CommonUtils.TAG_STOCK_NAME);
            peopleType = "S";
            peopleCode = CommonUtils.TAG_STOCK_CODE;
            commonSFCode = CommonUtils.TAG_FEED_SF_CODE;
            TypePeople = "S";
            if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("spob")))
                txt_pob.setText(mCommonSharedPreference.getValueFromPreference("spob"));

        }
        else if (feedOption.equalsIgnoreCase("cip")) {
            if (mCommonSharedPreference.getValueFromPreference("hosp_filter").equalsIgnoreCase("0")) {
                txt_pob.setText("POB");
            }
            if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("cpob")))
                txt_pob.setText(mCommonSharedPreference.getValueFromPreference("cpob"));

            call_plus.setEnabled(false);
            call_plus.getBackground().setAlpha(128);
//            btn_brand_audit.setVisibility(View.INVISIBLE);
            // txt_name.setText(extra.getString("customer"));
            txt_name.setText(CommonUtils.TAG_CHEM_NAME);
            peopleType = "I";
            peopleCode = CommonUtils.TAG_CHEM_CODE;
            commonSFCode = CommonUtils.TAG_FEED_SF_CODE;
            TypePeople = "I";

        }
        else {
            txt_name.setText(CommonUtils.TAG_DOCTOR_NAME);
            peopleType = "D";
            peopleCode = CommonUtils.TAG_DOCTOR_CODE;
            commonSFCode = CommonUtils.TAG_FEED_SF_CODE;
            TypePeople = "D";
            if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("dpob")))
                txt_pob.setText(mCommonSharedPreference.getValueFromPreference("dpob"));


        }
        Log.v("printing_final_pob", peopleType + " val_pob" + val_pob);
        if (val_pob.contains(peopleType)) {
            ll_feed_prd2.setVisibility(View.VISIBLE);
            ll_feed_prd.setVisibility(View.GONE);
        } else {
            ll_feed_prd2.setVisibility(View.GONE);
            ll_feed_prd.setVisibility(View.VISIBLE);
        }

       /* Log.v("printing_people_code",peopleCode);
        if(mCommonSharedPreference.getValueFromPreference("missed").equals("true")){
            *//*subDate=mCommonUtilsMethod.getCurrentInstance();*//*
            subDate=mCommonSharedPreference.getValueFromPreference("mis_date");
        }
        else*/
        subDate = CommonUtilsMethods.getCurrentInstance();

        if (mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")) {
            bt_cancel.setText("Back To List");
            submit.setVisibility(View.INVISIBLE);
        }

        val = mCommonSharedPreference.getValueFromPreferenceFeed("timeCount", 0);
        Log.v("val>>",String.valueOf(val));
        for (int i = 0; i < val; i++) {
            String timevalue = mCommonSharedPreference.getValueFromPreferenceFeed("timeVal" + i);
            String prdName = mCommonSharedPreference.getValueFromPreferenceFeed("slide_nam" + i);
            String prdddName = mCommonSharedPreference.getValueFromPreferenceFeed("brd_nam" + i);
            String slidetyp = mCommonSharedPreference.getValueFromPreferenceFeed("slide_typ" + i);
            String slideur = mCommonSharedPreference.getValueFromPreferenceFeed("slide_url" + i);
            Log.v("prdName_show", prdName + " time_val " + prdddName);
            //Toast.makeText(FeedbackActivity.this,prdddName,Toast.LENGTH_SHORT).show();
            String eTime;
            if (arrayStore.contains(new StoreImageTypeUrl(prdName))) {

                eTime = findingEndTime(i);
                int index = checkForProduct(prdName);
                StoreImageTypeUrl mmm = arrayStore.get(index);
                // Log.v("jsonarray_putting",mmm.getTiming());
                try {
                    JSONArray jj = new JSONArray(mmm.getRemTime());
                    JSONArray jk = new JSONArray();
                    JSONObject js = null;
                    for (int k = 0; k < jj.length(); k++) {
                        js = jj.getJSONObject(k);
                        jk.put(js);
                        //Log.v("jsonarray_putting34",jk.toString());
                    }
                    js = new JSONObject();
                    if(eTime.equalsIgnoreCase(timevalue)){
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                        df.setTimeZone(TimeZone.getTimeZone("UTC"));

                        Date   d1 = df.parse(eTime);
                       Date d2 = df.parse("00:00:02");

                        Long sumtime= d1.getTime()+d2.getTime();
                        eTime= df.format(new Date(sumtime));
                        Log.v("timenew>>",eTime);

                    }
                    js.put("sT", timevalue);
                    js.put("eT", eTime);
                    jk.put(js);
                    Log.v("jsonarray_putting", jj.toString() + " jsack_prnt " + jk.toString() + "etim" + eTime);
                    mmm.setRemTime(jk.toString());
                } catch (Exception e) {
                }

            } else if (!prdName.isEmpty()) {


                eTime = findingEndTime(i);
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                try {
                    if(eTime.equalsIgnoreCase(timevalue)){
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                        df.setTimeZone(TimeZone.getTimeZone("UTC"));

                        Date   d1 = df.parse(eTime);
                        Date d2 = df.parse("00:00:02");

                        Long sumtime= d1.getTime()+d2.getTime();
                        eTime= df.format(new Date(sumtime));
                        Log.v("timenew>>",eTime);
                    }
                    jsonObject.put("sT", timevalue);
                    jsonObject.put("eT", eTime);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
                Log.v("json_str_arra", jsonArray.toString());
                arrayStore.add(new StoreImageTypeUrl(prdName, slidetyp, slideur, "0", "", jsonArray.toString(), prdddName, false));

                //arrayStore.add(new StoreImageTypeUrl(prdName,jsonArray.toString(),prdddName));
                //}
            }
            // dateVal=mCommonSharedPreference.getValueFromPreferenceFeed("dateVal"+i);

        }
        String slideFeedback = mCommonSharedPreference.getValueFromPreference("slide_feed");
        try {
            jsonFeed = new JSONArray(slideFeedback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < arrayStore.size(); j++) {
            Log.v("Slidename_dummy ", arrayStore.get(j).getSlideNam() + " timing " + arrayStore.get(j).getRemTime());
            StoreImageTypeUrl model = arrayStore.get(j);
            String slideRemark = "";
            for (int k = 0; k < jsonFeed.length(); k++) {
                try {
                    JSONObject jsonObject = jsonFeed.getJSONObject(k);

                    if (jsonObject.getString("id").equalsIgnoreCase(model.getSlideUrl())) {
                        slideRemark = jsonObject.getString("feedBack");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            dbh.open();
            dbh.insertFeed(model.getBrdName(), model.getSlideNam(), model.getSlideTyp(), model.getSlideUrl(), model.getTiming(), model.getRemTime(), slideRemark);





            if (j == 0) {
                gettingProductStartEndTime(arrayStore.get(j).getRemTime(), j);
                finalPrdNam = arrayStore.get(j).getBrdName();
            } else if (finalPrdNam.equalsIgnoreCase(arrayStore.get(j).getBrdName())) {

            } else {
                //  Log.v("getting_prd_dif","inside");
                String time = gettingProductStartEndTime(arrayStore.get(j).getRemTime(), j);
              //  Log.v("printing_all_time", time);


             //   listFeedPrd.add(new FeedbackProductDetail(arrayStore.get(j - 1).getBrdName(), time, "", "", mCommonUtilsMethod.getCurrentDate(), "", "", "", gettingProductFB("")));
                finalPrdNam = arrayStore.get(j).getBrdName();

            }
        }
        if (arrayStore.size() > 0) {
            String time = gettingProductStartEndTime(arrayStore.get(arrayStore.size() - 1).getRemTime(), arrayStore.size() - 1);
            ArrayList<PopFeed> ff = new ArrayList<>();
            ff = arr_pfb;
         //   listFeedPrd.add(new FeedbackProductDetail(arrayStore.get(arrayStore.size() - 1).getBrdName(), time, "", "", mCommonUtilsMethod.getCurrentDate(), "", "", "", gettingProductFB("")));
           // Log.v("edet_feedback33", time + " print" + listFeedPrd.get(0).getSt_end_time());
        }
if(!feedOption.equalsIgnoreCase("edit")) {
    dbh.open();
    Cursor cursor = dbh.select_feedback_listfull();
    if (cursor.getCount() > 0) {
        String prdname = "ee";
        cursor.moveToFirst();
        do {
            Log.v("Printing_prd_name", cursor.getString(2) + " url " + cursor.getString(4));
            String seTime = "";
            try {
                JSONArray jsonArray = new JSONArray(cursor.getString(5));
                JSONObject js = jsonArray.getJSONObject(0);
                String sTT = js.getString("sT");
                String eTT = js.getString("eT");
                seTime = sTT + " " + eTT;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (prdname.contains(cursor.getString(1))) {

            } else {
                listFeedPrd.add(new FeedbackProductDetail(cursor.getString(1), seTime, "", "", mCommonUtilsMethod.getCurrentDate(), "", cursor.getString(5), "", gettingProductFB("")));
                prdname += cursor.getString(1);

            }
        } while (cursor.moveToNext());

    }
    dbh.close();

}

        feedProductAdapter = new FeedProductAdapter(FeedbackActivity.this, listFeedPrd);
        listView_feed_product.setAdapter(feedProductAdapter);
        feedProductAdapter.notifyDataSetChanged();
        if (mCommonSharedPreference.getValueFromPreference("GpsFilter").equalsIgnoreCase("0")) {
            CheckPermission();
        }
        ll_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_report.setEnabled(true);
                showSoftKeyboard(edt_report);
            }
        });
        img_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    captureFile();
                } else
                    captureFileLower();

            }
        });
        btn_brand_audit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jsonBrandValue = mCommonSharedPreference.getValueFromPreference("jsonarray");


                if(feedOption.equalsIgnoreCase("dr")){
                    Intent i = new Intent(FeedbackActivity.this,BrandAuditActivity.class);
                    i.putExtra("json_val", jsonBrandValue);
                    i.putExtra("name", txt_name.getText().toString());
                    startActivityForResult(i, 6);
                }else {
                    Log.v("total_json_val", jsonBrandValue);
                    Intent i = new Intent(FeedbackActivity.this,NewRCBentryActivity.class);
                    i.putExtra("json_val", jsonBrandValue);
                    i.putExtra("name", txt_name.getText().toString());
                    startActivityForResult(i, 6);

                }
//                if (TextUtils.isEmpty(jsonBrandValue)&&feedOption.matches("chemist")) {
//
//
//                    Intent i = new Intent(FeedbackActivity.this, NewRCBentryActivity.class);
//                    startActivityForResult(i, 6);
//                } else {
//
//
//
//                    Log.v("total_json_val", jsonBrandValue);
//                    Intent i = new Intent(FeedbackActivity.this,NewRCBentryActivity.class);
//                    i.putExtra("json_val", jsonBrandValue);
//                    i.putExtra("name", txt_name.getText().toString());
//                    startActivityForResult(i, 6);
//                }
//                if (TextUtils.isEmpty(jsonBrandValue)&& !feedOption.equals("chemist")) {
//                    Intent i = new Intent(FeedbackActivity.this, BrandAuditActivity.class);
//                    startActivityForResult(i, 6);
//
//                }else {
//                    Log.v("total_json_val", jsonBrandValue);
//                    Intent i = new Intent(FeedbackActivity.this, BrandAuditActivity.class);
//                    i.putExtra("json_val", jsonBrandValue);
//                    i.putExtra("name", txt_name.getText().toString());
//                    startActivityForResult(i, 6);
//                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbFunctionToSave();
                if (mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")) {
                    mCommonSharedPreference.setValueToPreference("miss_select", "1");
                    if (peopleType.equalsIgnoreCase("D"))
                        mCommonSharedPreference.setValueToPreference("selection", 0);
                    else if (peopleType.equalsIgnoreCase("C"))
                        mCommonSharedPreference.setValueToPreference("selection", 1);
                    else if (peopleType.equalsIgnoreCase("I"))
                        mCommonSharedPreference.setValueToPreference("selection", 1);
                    else if (peopleType.equalsIgnoreCase("S"))
                        mCommonSharedPreference.setValueToPreference("selection", 2);
                    else
                        mCommonSharedPreference.setValueToPreference("selection", 3);

                    try {
                        JSONArray ja = new JSONArray();
                        JSONArray jsonn = new JSONArray(mCommonSharedPreference.getValueFromPreference("missed_array").toString());
                        for (int i = 0; i < jsonn.length(); i++) {
                            JSONObject js = jsonn.getJSONObject(i);
                            if (peopleCode.equalsIgnoreCase(js.getString("code"))) {

                                js.put("code", peopleCode);
                                js.put("type", peopleType);
                                js.put("jobj", mCommonSharedPreference.getValueFromPreference("jsonsave"));
                                ja.put(js);
                            } else
                                ja.put(js);

                            Log.v("missed_final", ja.toString());
                        }
                        mCommonSharedPreference.setValueToPreference("missed_array", ja.toString());
                        /*mCommonSharedPreference.setValueToPreference("hos_code","");
                        mCommonSharedPreference.setValueToPreference("hos_name","");*/
                    } catch (Exception e) {

                    }

                    Intent i = new Intent(FeedbackActivity.this, DCRCallSelectionActivity.class);
                    startActivity(i);

                } else {
                    popUpAlert();
                }
            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupQuery();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkPer = false;
                if (mCommonSharedPreference.getValueFromPreference("GpsFilter").equalsIgnoreCase("0")) {
                    if (CheckPermission()) {
                        checkPer = true;
                    } else
                        checkPer = false;
                } else {
                    checkPer = true;
                }
                if (checkPer) {
                    sign_canva.checkingForSign();
                    try {
                        signPath = captureCanvasScreen(sign_lay);
                    } catch (Exception e) {
                    }
//
                        dbFunctionToSave();


                    String finalValue = mCommonSharedPreference.getValueFromPreference("jsonsave");
                    Log.v("final_value_draft", finalValue);

                    if (isOnline(FeedbackActivity.this)) {
                        String prescriberValue = mCommonSharedPreference.getValueFromPreference("prescriber level");
                        Log.v("prescriber", prescriberValue);
                        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        String licence = sharedPreferences.getString(licenceKey, "");
                        Log.v("compKey", licence);
                        Log.v("counteeeee", "" + listFeedPrd.size());
                        int pl = 0;
                        for (int jj = 0; jj < listFeedPrd.size(); jj++) {
                            String check = getProdFb(listFeedPrd.get(jj).getProdFb());
                            if (check.equalsIgnoreCase("")) {
                                pl++;
                            }
                            Log.v("datacheck", String.valueOf(getProdFb(listFeedPrd.get(jj).getProdFb())));
                        }

                        if (pl != 0 && licence.equalsIgnoreCase("harl1420")) {

                            final Dialog dialog = new Dialog(FeedbackActivity.this, R.style.AlertDialogCustom);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.setContentView(R.layout.popup_prodfeedback);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();

                            LinearLayout ok = (LinearLayout) dialog.findViewById(R.id.ok);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            return;
                        }

                            if (mCommonSharedPreference.getValueFromPreference("DrSmpQMd").equals("1") && peopleType.equalsIgnoreCase("D")) {
                                for (int jj = 0; jj < listFeedPrd.size(); jj++) {
                                    String rxqty = listFeedPrd.get(jj).getSample();

                                    if (rxqty.equalsIgnoreCase("")) {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.entr_sample_val), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Log.v("rxqty", rxqty);
                                }

                            }
                            if (ll_feed_prd2.getVisibility() == View.VISIBLE) {
                                if (mCommonSharedPreference.getValueFromPreference("DrRxNd").equals("1") && mCommonSharedPreference.getValueFromPreference("DrRxQMd").equals("1") && peopleType.equalsIgnoreCase("D")) {
                                    for (int jj = 0; jj < listFeedPrd.size(); jj++) {
                                        String rxqty = listFeedPrd.get(jj).getRxQty();
                                        if (rxqty.equalsIgnoreCase("")) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.entr_rx_val), Toast.LENGTH_LONG).show();

                                            return;
                                        }
                                    }
                                }
                            }

                            if (mCommonSharedPreference.getValueFromPreference("DrInpMd").equals("1") && peopleType.equalsIgnoreCase("D")) {
                                if (input_array.size() < 1) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sclt_input), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                for (int jj = 0; jj < input_array.size(); jj++) {
                                    String inputName = input_array.get(jj).getInputName();
                                    String inputQty = input_array.get(jj).getIqty();

                                    if (inputName.equalsIgnoreCase("") && inputQty.equalsIgnoreCase("")) {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.entr_input_val), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }

                            }
                            if (mCommonSharedPreference.getValueFromPreference("RcpaNd").equals("1") && peopleType.equalsIgnoreCase("D")) {
                                String rcpa = mCommonSharedPreference.getValueFromPreference("jsonarray");
                                if (rcpa.equalsIgnoreCase("")) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.entr_rcpa_val), Toast.LENGTH_LONG).show();
                                    return;

                                }
                            }

                            finalSubmission(finalValue);

                    } else {
                        Calendar calander = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy hh:mm:ss");
                        String d = null;
                        try {
                            d = sdf.format(calander.getTime());
                            Log.v("date_value_conver", d + " ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dbh.open();
                        dbh.deleteFeed();
                        dbh.delete_groupName("customised");
                        dbh.delete_json(colId);
                        mCommonSharedPreference.setValueToPreference("slide_feed", "[]");

                        if (txt_name.getText().toString().equalsIgnoreCase("DocName")||txt_name.getText().toString().equalsIgnoreCase("")) {
                            Log.v("DocName", txt_name.toString());
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_cus), Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            dbh.insertJson(String.valueOf(finalValue), txt_name.getText().toString() + "_s_ync", String.valueOf(d), peopleCode, peopleType, commonSFCode);
                            Intent i = new Intent(FeedbackActivity.this, HomeDashBoard.class);
                            startActivity(i);
                        }

                    }
                    //if(feedOption.equalsIgnoreCase("edit"))
                    dbh.open();
                    dbh.deleteFeed();
                    dbh.delete_groupName("customised");
                    dbh.delete_json(colId);
                    dbh.close();
                    mCommonSharedPreference.setValueToPreference("slide_feed", "[]");
                }
            }
        });

        prd_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xx.clear();
                dbh.open();
                mCursor = dbh.select_product_content_master();

                if (mCursor.getCount() != 0) {
                    mCursor.moveToFirst();
                    do {
                        Log.v("product_name_feed", mCursor.getString(0));
                        xx.add(new PopFeed(mCursor.getString(0), false));

                    } while (mCursor.moveToNext());
                    popUpAlertFeed(xx, "p");
                }
                mCursor.close();
                dbh.close();


            }
        });
        prd_plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xx.clear();
                dbh.open();
                mCursor = dbh.select_product_content_master();

                if (mCursor.getCount() != 0) {
                    mCursor.moveToFirst();
                    do {
                        Log.v("product_name_feed", mCursor.getString(0));
                        xx.add(new PopFeed(mCursor.getString(0), false));

                    } while (mCursor.moveToNext());
                    popUpAlertFeed(xx, "p");
                }
                mCursor.close();
                dbh.close();


            }
        });
        ip_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xx.clear();
                dbh.open();
                mCursor = dbh.select_input_list();

                if (mCursor.getCount() != 0) {
                    mCursor.moveToFirst();
                    do {
                        Log.v("input_name_feed", mCursor.getString(0));
                        xx.add(new PopFeed(mCursor.getString(0), false));
                    } while (mCursor.moveToNext());
                    popUpAlertFeed(xx, "i");
                } else {
                    Toast.makeText(FeedbackActivity.this, getResources().getString(R.string.no_input), Toast.LENGTH_SHORT).show();
                }
                mCursor.close();
                dbh.close();


            }
        });
        call_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xx.clear();

                dbh.open();
                mCursor = dbh.select_doctor_list();

                if (mCursor.getCount() != 0) {
                    mCursor.moveToFirst();
                    do {
                        Log.v("plus_name_feed", mCursor.getString(0));
                        xx.add(new PopFeed(mCursor.getString(0), false));
                    } while (mCursor.moveToNext());
                    popUpAlertFeed(xx, "a");
                }
                mCursor.close();
                dbh.close();

            }
        });


        join_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xx.clear();
                dbh.open();
                mCursor = dbh.select_joint_list();

                if (mCursor.getCount() != 0) {
                    mCursor.moveToFirst();
                    do {
                        Log.v("plus_name_feed", mCursor.getString(0));
                        if (!SFCode.equalsIgnoreCase(mCursor.getString(1)))
                            xx.add(new PopFeed(mCursor.getString(0), false));
                    } while (mCursor.moveToNext());
                    popUpAlertFeed(xx, "j");
                }
                mCursor.close();
                dbh.close();


            }
        });


    }

    private void selected(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }



    public ArrayList<PopFeed> gettingProductFB(String splitvalue) {
        String[] svalue = null;
        if (!splitvalue.isEmpty()) {
            svalue = splitvalue.split("/");
        }
        boolean valuethere = false;
        ArrayList<PopFeed> arr = new ArrayList<>();
        if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("productFB")) && mCommonSharedPreference.getValueFromPreference("productFB").equalsIgnoreCase("0")) {

            dbh.open();
            Cursor mCursor = dbh.select_productfb();

            while (mCursor.moveToNext()) {
                if (!splitvalue.isEmpty()) {
                    for (int i = 0; i < svalue.length; i++) {
                        if (svalue[i].contains(mCursor.getString(1)))
                            valuethere = true;

                    }
                }
                if (valuethere)
                    arr.add(new PopFeed(mCursor.getString(0), true, mCursor.getString(1)));
                else
                    arr.add(new PopFeed(mCursor.getString(0), false, mCursor.getString(1)));
                valuethere = false;
            }
        } else
            return arr;
        return arr;

    }

    public void popUpAlert() {

        final Dialog dialog = new Dialog(FeedbackActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_product_detail);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        LinearLayout save = (LinearLayout) dialog.findViewById(R.id.save);
        LinearLayout ok = (LinearLayout) dialog.findViewById(R.id.ok);
        LinearLayout cancel = (LinearLayout) dialog.findViewById(R.id.cancel);
        TextView txt_save = (TextView) dialog.findViewById(R.id.txt_save);

        if (colId == -1) {
            txt_save.setTextColor(Color.argb(40, 255, 0, 0));
            save.setEnabled(false);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txt_name.getText().toString().equalsIgnoreCase("DocName") || txt_name.getText().toString().equalsIgnoreCase("")) {
                    Log.v("DocName", txt_name.toString());
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_cus), Toast.LENGTH_LONG).show();
                    return;
                } else {
                    try {
                        signPath = captureCanvasScreen(sign_lay);
                    } catch (Exception e) {
                    }
                    dbFunctionToSave();
                    String jsonValue = mCommonSharedPreference.getValueFromPreference("jsonsave");
                    dbh.open();

                    if (!feedOption.equalsIgnoreCase("edit")) {
                        Calendar calander = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy hh:mm:ss");
                        String d = null;
                        try {
                            d = sdf.format(calander.getTime());
                            Log.v("date_value_conver", d + " ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        dbh.insertJson(String.valueOf(jsonValue), txt_name.getText().toString(), String.valueOf(d), peopleCode, peopleType, commonSFCode);
                    } else {
                        dbh.updateJson(colId, jsonValue);
                    }
                    mCommonSharedPreference.clearFeedShare();
                    dbh.deleteFeed();
                    dbh.close();
                    mCommonSharedPreference.setValueToPreference("jsonarray", "");
                    Intent i = new Intent(FeedbackActivity.this, HomeDashBoard.class);
                    startActivity(i);
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCommonSharedPreference.clearFeedShare();
                dbh.open();
                dbh.deleteFeed();
                dbh.delete_groupName("customised");
                dbh.close();
                mCommonSharedPreference.setValueToPreference("jsonarray", "");
                mCommonSharedPreference.setValueToPreference("slide_feed", "[]");
                mCommonSharedPreference.setValueToPreferenceFeed("timeCount", 0);
                mCommonSharedPreference.setValueToPreference("availjson", "");

                Intent i = new Intent(FeedbackActivity.this, HomeDashBoard.class);
                startActivity(i);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
    }

    public void popUpAlertFeed(final ArrayList<PopFeed> xx, final String x) {

        final Dialog dialog = new Dialog(FeedbackActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_feedback);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Log.v("joint_wrk_are", "called");

        Button ok = (Button) dialog.findViewById(R.id.ok);
        final ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);
        ImageView iv_close_popup = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        TextView tv_todayplan_popup_head = (TextView) dialog.findViewById(R.id.tv_todayplan_popup_head);
        final SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
        EditText searchedit=dialog.findViewById(R.id.et_search);

        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view.setIconified(false);
                InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(search_view, 0);
            }
        });

        final PopupAdapter popupAdapter = new PopupAdapter(FeedbackActivity.this, xx);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        if (x.equals("i")) {
            tv_todayplan_popup_head.setText(resources.getString(R.string.inp_selct));
        } else if (x.equals("a")) {
            tv_todayplan_popup_head.setText(resources.getString(R.string.add_selct));
        } else if (x.equals("j")) {
            tv_todayplan_popup_head.setText(resources.getString(R.string.joint_selct));
        } else {
            tv_todayplan_popup_head.setText(resources.getString(R.string.prodct_selct));
        }
        searchedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                popupAdapter.getFilter().filter(s);
                popupAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                popupAdapter.getFilter().filter(s);
//                return false;
//            }
//        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < xx.size(); i++) {
                    PopFeed popFeed = xx.get(i);
                    if (popFeed.isClick()) {
                        Log.v("clicked", "here" + popFeed.getTxt());
                        // prd_value.add(popFeed.getTxt());
                        if (x.equals("i")) {
                            if (!input_array.contains(new SlideDetail(popFeed.getTxt())))
                                input_array.add(new SlideDetail(popFeed.getTxt(), "1"));
                        } else if (x.equals("a")) {
                            call_array.add(popFeed.getTxt());
                        } else if (x.equals("j")) {
                            if (!joint_array.contains(popFeed.getTxt()))
                                joint_array.add(popFeed.getTxt());
                            Log.v("printing_total_joint", joint_array.toString());
                        } else {

                            if (!listFeedPrd.contains(new FeedbackProductDetail(popFeed.getTxt())))
                                listFeedPrd.add(new FeedbackProductDetail(popFeed.getTxt(), "00:00:00 00:00:00", "", "", CommonUtilsMethods.getCurrentDate(), "", "", "", gettingProductFB("")));
                        }
                    }
                }
                if (x.equals("i")) {
                    feedInputAdapter = new FeedInputAdapter(FeedbackActivity.this, input_array);
                    listView_feed_input.setAdapter(feedInputAdapter);
                    feedInputAdapter.notifyDataSetChanged();
                } else if (x.equals("a")) {
                    feedCallJoinAdapter = new FeedCallJoinAdapter(FeedbackActivity.this, call_array);
                    listView_feed_call.setAdapter(feedCallJoinAdapter);
                    feedCallJoinAdapter.notifyDataSetChanged();
                } else if (x.equals("j")) {
                    feedCallJoinAdapter = new FeedCallJoinAdapter(FeedbackActivity.this, joint_array);
                    listView_feed_join.setAdapter(feedCallJoinAdapter);
                    feedCallJoinAdapter.notifyDataSetChanged();

                } else {
                    feedProductAdapter = new FeedProductAdapter(FeedbackActivity.this, listFeedPrd);
                    listView_feed_product.setAdapter(feedProductAdapter);
                    feedProductAdapter.notifyDataSetChanged();

                }
                dialog.dismiss();
            }
        });

        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    public void showSoftKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        edt_report.requestFocus();
        inputMethodManager.showSoftInput(view,
                InputMethodManager.SHOW_IMPLICIT);

    }

    public void showSoftKeyboard(EditText view, int x) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view,
                InputMethodManager.SHOW_IMPLICIT);

    }

    public void hideSoftKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


    public void popupQuery() {

        final Dialog dialog = new Dialog(FeedbackActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.query_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ImageView iv_close_popup = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        LinearLayout lay_edit = (LinearLayout) dialog.findViewById(R.id.lay_edit);
        final EditText edt_report = (EditText) dialog.findViewById(R.id.edt_report);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        //hideSoftKeyboard(edt_report);
        lay_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoftKeyboard(edt_report, 9);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences sharedpreferences = getSharedPreferences(getResources().getString(R.string.preference_name), Context.MODE_PRIVATE);
                    String codeVal = mCommonSharedPreference.getValueFromPreference("drCode");
                    String divCode = sharedpreferences.getString(CommonUtils.TAG_DIVISION, null);
                    String SF_Code = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);

                    JSONObject json = new JSONObject();
                    json.put("QryDt", mCommonUtilsMethod.getCurrentInstance());
                    json.put("SF", SF_Code);
                    json.put("DivCode", divCode);
                    json.put("DrCode", codeVal);
                    json.put("DrName", txt_name.getText().toString());
                    json.put("DeptCode", arrListing.get(queryCount).getCompCode());
                    json.put("DeptName", arrListing.get(queryCount).getCompName());
                    json.put("QryMsg", edt_report.getText().toString());
                    json.put("QryID", "");

                    Call<ResponseBody> query = apiService.saveQuery(json.toString());
                    query.enqueue(new Callback<ResponseBody>() {
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
                                Log.v("printing_retro", String.valueOf(is));
                                JSONObject json = new JSONObject(is.toString());
                                Log.v("json_successs", json.getString("success"));
                                if (json.getString("success").equals("true")) {
                                    dialog.dismiss();
                                    Toast.makeText(FeedbackActivity.this, getResources().getString(R.string.data_success), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FeedbackActivity.this, getResources().getString(R.string.query_notsuccs), Toast.LENGTH_SHORT).show();

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
        });
        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                queryCount = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        arrListing.clear();
        List<String> categories = new ArrayList<String>();
        dbh.open();
        mCursor = dbh.select_departData();
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                String code = mCursor.getString(1);
                String name = mCursor.getString(2);
                String depname = mCursor.getString(3);
                String depCode = mCursor.getString(4);
                arrListing.add(new CompNameProduct(name, depname, code, depCode));
                categories.add(name);

            } while (mCursor.moveToNext());
        }
        mCursor.close();
        dbh.close();

        // Spinner Drop down elements


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.textview_bg_spinner, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.textview_bg_spinner);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    public void dbFunctionToSave() {
        dbh.open();
        JSONArray jsonArray = new JSONArray();
        JSONObject jointObj = new JSONObject();


        Log.v("printing_colid", colId + " jw " + mCommonSharedPreference.getValueFromPreference("visit"));
        try {
            for (int i = 0; i < joint_array.size(); i++) {
                JSONObject json_joint = new JSONObject();
                Log.v("json_select_joint", joint_array.get(i));
                mCursor = dbh.select_jointWork_gettingCode(joint_array.get(i));
                if (mCursor.getCount() != 0 && mCursor != null) {
                    mCursor.moveToFirst();
                    do {
                        json_joint.put("Code", mCursor.getString(0));
                        json_joint.put("Name", joint_array.get(i));

                    } while (mCursor.moveToNext());
                }

                jsonArray.put(json_joint);
            }

            jointObj.put("JWWrk", jsonArray);



            Log.v("joint_wrk_print", String.valueOf(jointObj));

            jsonArray = new JSONArray();
            for (int i = 0; i < input_array.size(); i++) {
                Log.v("input_array_str", String.valueOf(input_array.size()));
                JSONObject json_joint = new JSONObject();
                mCursor = dbh.select_input_gettingCode(input_array.get(i).getInputName());
                if (mCursor.getCount() != 0 && mCursor != null) {
                    mCursor.moveToFirst();
                    do {
                        json_joint.put("Code", mCursor.getString(0));
                        json_joint.put("Name", input_array.get(i).getInputName());
                        json_joint.put("IQty", input_array.get(i).getIqty());

                    } while (mCursor.moveToNext());
                }

                jsonArray.put(json_joint);
            }
            jointObj.put("Inputs", jsonArray);
            Log.v("joint_wrk_print11", String.valueOf(jointObj));
            jsonArray = new JSONArray();


            for (int i = 0; i < listFeedPrd.size(); i++) {
                boolean detail_prd = true;
                arr.clear();
                if (!TextUtils.isEmpty(listFeedPrd.get(i).getPrdNAme())) {
                    JSONObject json_joint = new JSONObject();
                    JSONArray jsonArray2 = new JSONArray();
                    String cap = listFeedPrd.get(i).getPrdNAme().substring(0, 1).toUpperCase() + listFeedPrd.get(i).getPrdNAme().substring(1).toLowerCase();
                    Log.v("captial_letter_changes ", cap + " cursor_Value " + listFeedPrd.get(i).getPrdNAme());
                    String prdCodde = "0";

                    Cursor cc = dbh.select_mappingPrdName(listFeedPrd.get(i).getPrdNAme());
                    if (cc.getCount() != 0) {
                        while (cc.moveToNext()) {
                            detail_prd = true;
                            Log.v("productnamess_yy ", cc.getString(4) + "codingg " + cc.getString(2) + listFeedPrd.get(i).getPrdNAme());
                            prdCodde = cc.getString(4)/*.substring(0, cc.getString(4).length() - 1)*/;
                            Log.v("product_codesssss_feed", prdCodde);
                        }

                    } else {
                        prdCodde = listFeedPrd.get(i).getPrdNAme();
                        Log.v("This_one_come", listFeedPrd.get(i).getPrdNAme());
                        Cursor c1 = dbh.select_product_content_master();
                        if (c1.getCount() != 0) {
                            while (c1.moveToNext()) {
                                if (listFeedPrd.get(i).getPrdNAme().equalsIgnoreCase(c1.getString(0))) {
                                    prdCodde = c1.getString(2);
                                    detail_prd = false;
                                    Log.v("product_code_feed", prdCodde);
                                }
                            }
                        }
                        c1.close();
                    }
                    cc.close();
                    boolean existProduct = false;
                    for (String retval : prdCodde.split(",")) {
                        if (!existProduct) {
                            mCursor = dbh.select_product_gettingCode(retval);
                            if (mCursor.getCount() != 0 && mCursor != null) {
                                existProduct = true;
                                mCursor.moveToFirst();
                                do {
                                    if (detail_prd)
                                        json_joint.put("Code", mCursor.getString(3));
                                    else
                                        json_joint.put("Code", mCursor.getString(0));
                                    json_joint.put("Name", listFeedPrd.get(i).getPrdNAme());
                                    if (listFeedPrd.get(i).getSt_end_time().equalsIgnoreCase("00:00:00 00:00:00"))
                                        json_joint.put("Group", "0");
                                    else
                                        json_joint.put("Group", "1");
                                    json_joint.put("ProdFeedbk", listFeedPrd.get(i).getFeedback());
                                    json_joint.put("Rating", listFeedPrd.get(i).getRating());
                                    JSONObject json_date = new JSONObject();
                                    String str = " hello     there   ";
                                    Log.v("for_each_prd", listFeedPrd.get(i).getSt_end_time() + " start_end " + listFeedPrd.get(i).getFeedback());
                                    System.out.println(listFeedPrd.get(i).getSt_end_time().replaceAll("( +)", " ").trim());
                                    listFeedPrd.get(i).setSt_end_time(listFeedPrd.get(i).getSt_end_time().replaceAll("( +)", " ").trim());
                                    Log.v("printing_start", listFeedPrd.get(i).getSt_end_time().trim() + " printtt " + listFeedPrd.get(i).getSt_end_time().substring(0, (listFeedPrd.get(i).getSt_end_time().indexOf(" "))));
                                    json_date.put("sTm", listFeedPrd.get(i).getDate() + " " + listFeedPrd.get(i).getSt_end_time().substring(0, (listFeedPrd.get(i).getSt_end_time().indexOf(" "))));
                                    json_date.put("eTm", listFeedPrd.get(i).getDate() + " " + listFeedPrd.get(i).getSt_end_time().substring((listFeedPrd.get(i).getSt_end_time().indexOf(" ")) + 1));
                                    json_joint.put("Timesline", json_date);
                                    json_joint.put("Appver", "V1.2");
                                    json_joint.put("Mod", "Edet");
                                    json_joint.put("SmpQty", listFeedPrd.get(i).getSample());
                                    if (val_pob.contains(peopleType))
                                        json_joint.put("RxQty", listFeedPrd.get(i).getRxQty());
                                    if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("productFB")) && mCommonSharedPreference.getValueFromPreference("productFB").equalsIgnoreCase("0"))
                                        json_joint.put("prdfeed", getProdFb(listFeedPrd.get(i).getProdFb()));
                                    json_joint.put("Type", peopleType);
                                    Log.v("getting_Slide_list_size", String.valueOf(json_joint));
                                    Cursor mCursor1 = dbh.select_feedback_list(listFeedPrd.get(i).getPrdNAme());
                                    Log.v("getting_Slide_list_size", String.valueOf(mCursor1.getCount()));

                                    if (mCursor1.getCount() != 0) {
                                        mCursor1.moveToFirst();
                                        do {
                                            Log.v("Printing_prd_name", mCursor1.getString(7) + "name" + mCursor1.getString(2) + "size" + arr.size());
                                            arr.add(new StoreImageTypeUrl(mCursor1.getString(2), mCursor1.getString(3), mCursor1.getString(4), mCursor1.getString(5), mCursor1.getString(7), mCursor1.getString(6)));
                                        } while (mCursor1.moveToNext());

                                    }
                                    mCursor1.close();
                                    JSONObject jsonSlide = null;
                                    for (int i1 = 0; i1 < arr.size(); i1++) {
                                        Log.v("total_arr_size", arr.size() + "");
                                        StoreImageTypeUrl mm1 = arr.get(i1);
                                        StoreImageTypeUrl mm2 = null;
                               /* if (i1 != (arr.size() - 1)) {
                                    mm2 = arr.get(i1 + 1);
                                    mm1.setTiming(mm1.getTiming() + " " + mm2.getTiming());
                                } else {
                                    mm1.setTiming(mm1.getTiming() + " " + listFeedPrd.get(i).getSt_end_time().substring(8));
                                }*/

                                        jsonSlide = new JSONObject();
                                        jsonSlide.put("Slide", mm1.getSlideNam());
                                        jsonSlide.put("SlidePath", mm1.getSlideUrl());
                                        jsonSlide.put("SlideRem", mm1.getSlideFeed());
                                        jsonSlide.put("SlideType", mm1.getSlideTyp());
                                        jsonSlide.put("SlideRating", mm1.getRemTime());
                                        JSONObject jsonTime = new JSONObject();
                                        JSONArray savejsonTime = new JSONArray();
                                        JSONArray jj = new JSONArray(mm1.getTiming());
                                        Log.v("jj_json_array", jj.toString());
                                        for (int t = 0; t < jj.length(); t++) {
                                            jsonTime = new JSONObject();
                                            JSONObject js = jj.getJSONObject(t);
                                            jsonTime.put("eTm", listFeedPrd.get(i).getDate() + " " + js.getString("eT"));
                                            jsonTime.put("sTm", listFeedPrd.get(i).getDate() + " " + js.getString("sT"));
                                            Log.v("jj_json_times_json", jsonTime.toString());
                                            // jsonArray1.put(jsonTime);
                                            savejsonTime.put(jsonTime);
                                            Log.v("jj_json_times", savejsonTime.toString());
                                        }
                                        Log.v("jj_json_out_time", savejsonTime.toString());
                               /* jsonTime.put("eTm", listFeedPrd.get(i).getDate() + " " + arr.get(i1).getTiming().trim().substring(9));
                                jsonTime.put("sTm", listFeedPrd.get(i).getDate() + " " + arr.get(i1).getTiming().trim().substring(0, 8));*/

                                        jsonSlide.put("Times", savejsonTime);
                                        jsonArray2.put(jsonSlide);
                                        Log.v("saving_timee_print", listFeedPrd.get(i).getDate() + " " + arr.get(i1).getTiming().trim());

                                    }

                                    json_joint.put("Slides", jsonArray2);


                                } while (mCursor.moveToNext());
                            }
                        }
                    }

                    jsonArray.put(json_joint);
                }
            }

            jointObj.put("Products", jsonArray);
            Log.v("joint_wrk_print22", String.valueOf(jointObj));

            jsonArray = new JSONArray();

            for (int i = 0; i < call_array.size(); i++) {
                JSONObject json_joint = new JSONObject();
                mCursor = dbh.select_DoctorDetailDa(call_array.get(i));
                if (mCursor.getCount() != 0 && mCursor != null) {
                    mCursor.moveToFirst();
                    do {
                        Log.v("json_joint", mCursor.getString(1));
                        json_joint.put("Code", mCursor.getString(1));
                        json_joint.put("Name", mCursor.getString(2));
                        json_joint.put("TCode", mCursor.getString(5));
                        json_joint.put("TName", mCursor.getString(6));

                    } while (mCursor.moveToNext());
                }

                jsonArray.put(json_joint);
            }
            jointObj.put("AdCuss", jsonArray);
            Log.v("joint_wrk_print44", String.valueOf(jointObj));
            SharedPreferences sharedpreferences = getSharedPreferences(getResources().getString(R.string.preference_name), Context.MODE_PRIVATE);
            String divCode = sharedpreferences.getString(CommonUtils.TAG_DIVISION, null);
            SharedPreferences shares = getSharedPreferences("location", 0);
            String lat = shares.getString("lat", "");
            String lng = shares.getString("lng", "");

            if (peopleType.equalsIgnoreCase("C") || peopleType.equalsIgnoreCase("S") || peopleType.equalsIgnoreCase("I")) {

                jointObj.put("CateCode", "");
                if (peopleType.equalsIgnoreCase("C"))
                    jointObj.put("CusType", "2");
              else  if (peopleType.equalsIgnoreCase("I"))
                    jointObj.put("CusType", "6");
              else
                    jointObj.put("CusType", "3");
                jointObj.put("CustCode", peopleCode);
                jointObj.put("CustName", txt_name.getText().toString());
                jointObj.put("DataSF", commonSFCode);
                jointObj.put("DivCode", divCode);
                jointObj.put("Entry_location", lat + ":" + lng);
                jointObj.put("Sf_Code", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
                jointObj.put("SF", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
                jointObj.put("Div", divCode);
                jointObj.put("AppUserSF", commonSFCode);
                jointObj.put("SFName", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME));
                jointObj.put("SpecCode", "");
                jointObj.put("mappedProds", "");
                jointObj.put("mode", "Edet");
                jointObj.put("WT", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CODE));
                jointObj.put("WTNm", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_NAME));
                jointObj.put("ModTime", subDate + " " + mCommonUtilsMethod.getCurrentTime());
                jointObj.put("ReqDt", subDate + " " + mCommonUtilsMethod.getCurrentTime());
                if (colId == -1)
                    jointObj.put("vstTime", mCommonSharedPreference.getValueFromPreference("visit"));
                else
                    jointObj.put("vstTime", subDate + " " + mCommonUtilsMethod.getCurrentTime());
                jointObj.put("Remks", edt_report.getText().toString());
                jointObj.put("Pl", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE));
                if (colId == -1)
                    jointObj.put("amc", mCommonSharedPreference.getValueFromPreference("detno"));
                else
                    jointObj.put("amc", "");
                if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("hos_code"))) {
                    jointObj.put("hos_code", mCommonSharedPreference.getValueFromPreference("hos_code"));
                    jointObj.put("hos_name", mCommonSharedPreference.getValueFromPreference("hos_name"));
                }


            } else {
                if (peopleType.equalsIgnoreCase("D"))
                    mCursor = dbh.select_DoctorDetailData(CommonUtils.TAG_DOCTOR_CODE);
                else if (peopleType.equalsIgnoreCase("U"))
                    mCursor = dbh.select_unDrDetailData(CommonUtils.TAG_UNDR_CODE);

                if (mCursor.getCount() != 0 && mCursor != null) {
                    mCursor.moveToFirst();
                    do {
                        Log.v("mCursor_count_is", mCursor.getCount() + " sfcode" + mCursor.getString(0));
                        jointObj.put("CateCode", mCursor.getString(7));
                        if (peopleType.equalsIgnoreCase("D"))
                            jointObj.put("CusType", "1");
                        else
                            jointObj.put("CusType", "4");
                        jointObj.put("CustCode", mCursor.getString(1));
                        jointObj.put("CustName", mCursor.getString(2));
                        jointObj.put("DataSF", mCursor.getString(0));
                        jointObj.put("DivCode", divCode);
                        jointObj.put("Entry_location", lat + ":" + lng);
                        jointObj.put("Sf_Code", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
                        jointObj.put("SF", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
                        jointObj.put("Div", divCode);
                        jointObj.put("AppUserSF", mCursor.getString(0));
                        jointObj.put("SFName", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME));
                        jointObj.put("SpecCode", mCursor.getString(8));
                        jointObj.put("mappedProds", "");
                        jointObj.put("mode", "0");
                        jointObj.put("WT", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CODE));
                        jointObj.put("WTNm", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_NAME));
                        jointObj.put("Pl", mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE));
                        jointObj.put("ModTime", subDate + " " + mCommonUtilsMethod.getCurrentTime());
                        jointObj.put("ReqDt", subDate + " " + mCommonUtilsMethod.getCurrentTime());
                        if (colId == -1)
                            jointObj.put("vstTime", mCommonSharedPreference.getValueFromPreference("visit"));
                        else
                            jointObj.put("vstTime", subDate + " " + mCommonUtilsMethod.getCurrentTime());
                        jointObj.put("Remks", edt_report.getText().toString());
                        if (colId == -1)
                            jointObj.put("amc", mCommonSharedPreference.getValueFromPreference("detno"));
                        else
                            jointObj.put("amc", "");

                        if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("hos_code"))) {
                            jointObj.put("hos_code", mCommonSharedPreference.getValueFromPreference("hos_code"));
                            jointObj.put("hos_name", mCommonSharedPreference.getValueFromPreference("hos_name"));
                        }

                    } while (mCursor.moveToNext());
                }
            }
            Log.v("joint_wrk_print55", String.valueOf(jointObj));
            /* jointObj.put("AdCuss", jsonArray);*/
            String jsonarrray = mCommonSharedPreference.getValueFromPreference("jsonarray");
            Log.v("json_array_rcpa", jsonarrray);
            JSONArray jsonArray1 = null;
            try {
                jsonArray1 = new JSONArray(jsonarrray);
                jointObj.put("RCPAEntry", jsonArray1);
            } catch (Exception e) {
                jointObj.put("RCPAEntry", jsonArray1);
            }

            String availjson = mCommonSharedPreference.getValueFromPreference("availjson");
            JSONObject jsonArrayavail = null;
            Log.v("json_avail", availjson);
            try {
                jsonArrayavail = new JSONObject(availjson);
                jointObj.put("AvailabilityAudit", jsonArrayavail);
            } catch (Exception e) {
                jointObj.put("AvailabilityAudit", jsonArrayavail);
            }


            Log.v("joint_wrk_print66", String.valueOf(jointObj));
            jointObj.put("sign_path", signPath);
            jointObj.put("filepath", filePath);

        } catch (JSONException e) {

        }

        Log.v("joint_wrk_print", String.valueOf(jointObj));

        mCommonSharedPreference.setValueToPreference("jsonsave", String.valueOf(jointObj));
        if (mCursor != null)
            mCursor.close();
        dbh.close();
    }


    public void jsonExtract() {
        listFeedPrd.clear();
        String namee = null;
        int count = 0;
        try {
            JSONObject json = new JSONObject(jsonValue.toString());
            if (funStringValidation(json.getString("Remks")))
                edt_report.setText(json.getString("Remks"));
            if (json.has("hos_code")) {
                mCommonSharedPreference.setValueToPreference("hos_code", json.getString("hos_code"));
                mCommonSharedPreference.setValueToPreference("hos_name", json.getString("hos_name"));
            }
            JSONArray jsonPrdArray = new JSONArray(json.getString("Products"));
            Log.v("Product_extract", String.valueOf(json));


            for (int i = 0; i < jsonPrdArray.length(); i++) {

                JSONObject js = jsonPrdArray.getJSONObject(i);

                if (!(listFeedPrd.contains(new FeedbackProductDetail(js.getString("Name"))))) {

                    if (js.length() != 0 && !js.getString("Name").isEmpty()) {
                        Log.v("json_name", js.getString("Name") + " val_pb " + val_pob + " people " + peopleType);
                        if (funStringValidation(js.getString("Name")))
                            namee = js.getString("Name");
                        String rat = "", feed = "", qty = "", STm = "", ETm = "", dt = "", pob = "";
                        if (funStringValidation(js.getString("Rating")))
                            rat = js.getString("Rating");
                        if (funStringValidation(js.getString("ProdFeedbk")))
                            feed = js.getString("ProdFeedbk");

                        if (funStringValidation(js.getString("SmpQty"))) {
                            if (js.getString("SmpQty").equalsIgnoreCase("$"))
                                qty = "";
                            else
                                qty = js.getString("SmpQty");
                        }
                        if (val_pob.contains(peopleType)) {
                            if (mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")) {
                                if (funStringValidation(js.getString("RxQty")))
                                    pob = js.getString("RxQty");
                            } else {
                                if (funStringValidation(js.getString("rx_pob")))
                                    pob = js.getString("rx_pob");
                            }
                        }

                        sampleValue.add(new FeedbackProductDetail(qty, feed, rat));
                        JSONObject jsonTim = js.getJSONObject("Timesline");
                        Log.v("json_time", jsonTim.getString("sTm"));
                        if (funStringValidation(jsonTim.getString("sTm")))
                            dt = jsonTim.getString("sTm").substring(0, 11);
                        if (funStringValidation(jsonTim.getString("sTm")))
                            STm = jsonTim.getString("sTm").substring((jsonTim.getString("sTm").indexOf(" ")) + 1);
                        if (funStringValidation(jsonTim.getString("eTm")))
                            ETm = jsonTim.getString("eTm").substring((jsonTim.getString("eTm").indexOf(" ")) + 1);
                        Log.v("name_js_js", namee + " smqty " + js.getString("SmpQty"));
                        if(js.has("prdfeed")) {
                            listFeedPrd.add(new FeedbackProductDetail(js.getString("Name"), STm.trim() + " " + ETm.trim(), rat, feed, dt, qty, "", pob, gettingProductFB(js.getString("prdfeed"))));
                        }else
                        {
                            listFeedPrd.add(new FeedbackProductDetail(js.getString("Name"), STm.trim() + " " + ETm.trim(), rat, feed, dt, qty, "", pob, gettingProductFB("")));
                        }

                        Log.v("list>>", String.valueOf(listFeedPrd.size()));
                        //Log.v("Product_extract22", namee);
                        JSONArray jsonArray = js.getJSONArray("Slides");

                        Log.v("slide_length_prinnt", jsonArray.length() + "");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            String sT = "", sR = "", ss = "", sP = "", sRate = "0";

                            JSONObject jsonSlide = jsonArray.getJSONObject(j);
                            Log.v("json_array_len", jsonArray.length() + "slidepath " + jsonSlide.getString("SlidePath"));
                            if (funStringValidation(jsonSlide.getString("SlideType")))
                                sT = jsonSlide.getString("SlideType");
                            if (funStringValidation(jsonSlide.getString("SlideRem")))
                                sR = jsonSlide.getString("SlideRem");
                            if (funStringValidation(jsonSlide.getString("Slide")))
                                ss = jsonSlide.getString("Slide");
                            if (funStringValidation(jsonSlide.getString("SlideRating")))
                                sRate = jsonSlide.getString("SlideRating");
                            if (funStringValidation(jsonSlide.getString("SlidePath")))
                                sP = jsonSlide.getString("SlidePath");
                            if (TextUtils.isEmpty(sP)) {
                                if (!TextUtils.isEmpty(sT)) {
                                    if (sT.equalsIgnoreCase("I")) {
                                        sP = "/data/user/0/saneforce.sanclm/cache/images/" + ss;
                                    } else
                                        sP = "/storage/emulated/0/Products/" + ss;
                                }
                            }
                            String date = "", sTm = "", eTm = "";
                            JSONArray remArray = new JSONArray();
                            JSONObject remObj = new JSONObject();
                            JSONArray jsonTime = jsonSlide.getJSONArray("Times");
                            dbh.open();
                            Cursor cur = dbh.select_tbl_feed(ss);
                            if (cur.getCount() != 0) {
                                while (cur.moveToNext()) {
                                    JSONArray jA = new JSONArray(cur.getString(5));
                                    for (int k = 0; k < jA.length(); k++) {
                                        remObj = new JSONObject();
                                        JSONObject jss = jA.getJSONObject(k);
                                        Log.v("jsonArrayTiming", jss.toString());
                                        remObj.put("sT", jss.getString("sT"));
                                        remObj.put("eT", jss.getString("eT"));
                                        remArray.put(remObj);
                                    }
                                }
                            }
                            cur.close();
                            for (int k = 0; k < jsonTime.length(); k++) {
                                remObj = new JSONObject();
                                JSONObject jsomtime = jsonTime.getJSONObject(k);
                                if (funStringValidation(jsomtime.getString("sTm")))
                                    date = jsomtime.getString("sTm").substring(0, 11);
                                if (funStringValidation(jsomtime.getString("sTm")))
                                    sTm = jsomtime.getString("sTm").substring(11);
                                if (funStringValidation(jsomtime.getString("eTm")))
                                    eTm = jsomtime.getString("eTm").substring(11);
                                Log.v("STart_date", String.valueOf(jsomtime));

                                remObj.put("sT", sTm);
                                remObj.put("eT", eTm);
                                Log.v("rem_obj_print", remObj.toString());
                                remArray.put(remObj);
                            }

                            Cursor cur1 = dbh.select_tbl_feed(ss);
                            if (cur1.getCount() == 0)
                                dbh.insertFeed(namee, ss, sT, sP, sRate, remArray.toString(), sR);
                            else {
                                while (cur1.moveToNext()) {
                                    Log.v("rem_array_print", remArray.toString() + " ");
                                    dbh.updateSlideTime(cur1.getInt(0), remArray.toString());
                                }

                            }
                            cur1.close();


                            mCommonSharedPreference.setValueToPreferenceFeed("timeCount", 0);



                  /*  Log.v("name_here_print",namee+" timeer "+sTm);
                    mCommonSharedPreference.setValueToPreferenceFeed("timeVal" + count, sTm);
                    mCommonSharedPreference.setValueToPreferenceFeed("brd_nam" + count, namee);
                    mCommonSharedPreference.setValueToPreferenceFeed("slide_nam" + count, ss);
                    mCommonSharedPreference.setValueToPreferenceFeed("slide_typ" + count, sT);
                    mCommonSharedPreference.setValueToPreferenceFeed("slide_url" + count, sP);
                    mCommonSharedPreference.setValueToPreferenceFeed("dateVal" + count, dt);
                    mCommonSharedPreference.setValueToPreferenceFeed("timeCount", ++count);

                    Log.v("count_total_val_date",count+" ");*/

                        }
                        if (jsonArray.length() == 0) {
                            mCommonSharedPreference.setValueToPreferenceFeed("timeVal" + count, "00:00:00");
                            mCommonSharedPreference.setValueToPreferenceFeed("brd_nam" + count, namee);
                            mCommonSharedPreference.setValueToPreferenceFeed("slide_nam" + count, "");
                            mCommonSharedPreference.setValueToPreferenceFeed("slide_typ" + count, "");
                            mCommonSharedPreference.setValueToPreferenceFeed("slide_url" + count, "");
                            mCommonSharedPreference.setValueToPreferenceFeed("dateVal" + count, dt);
                            mCommonSharedPreference.setValueToPreferenceFeed("timeCount", ++count);
                        }
                    }


                }
            }
            JSONArray jsonJoint = json.getJSONArray("JWWrk");
            Log.v("jwwrk_log", String.valueOf(jsonJoint));
            for (int w = 0; w < jsonJoint.length(); w++) {
                JSONObject jsJoint = jsonJoint.getJSONObject(w);
                String nam = null, code = "";
                if (funStringValidation(jsJoint.getString("Name")))
                    nam = jsJoint.getString("Name");
                if (funStringValidation(jsJoint.getString("Code")))
                    code = jsJoint.getString("Code");
                dbh.open();
                Cursor cc = dbh.select_jointWork_gettingName(code);
                while (cc.moveToNext()) {
                    joint_array.add(cc.getString(0));
                }


            }
            JSONArray jsonInput = json.getJSONArray("Inputs");
            Log.v("input_wrk", String.valueOf(jsonInput));
            String nam = "", code = "", iqty = "";
            for (int ip = 0; ip < jsonInput.length(); ip++) {
                JSONObject jsIp = jsonInput.getJSONObject(ip);
                if (funStringValidation(jsIp.getString("Name")))
                    nam = jsIp.getString("Name");
                if (funStringValidation(jsIp.getString("Code")))
                    code = jsIp.getString("Code");
                if (funStringValidation(jsIp.getString("IQty")))
                    iqty = jsIp.getString("IQty");
                input_array.add(new SlideDetail(nam, iqty));
            }
            JSONArray jsonAdditional = json.getJSONArray("AdCuss");
            Log.v("additional_wrk", String.valueOf(jsonAdditional));
            for (int aw = 0; aw < jsonAdditional.length(); aw++) {
                JSONObject jsAw = jsonAdditional.getJSONObject(aw);
                if (funStringValidation(jsAw.getString("Name")))
                    nam = jsAw.getString("Name");
                if (funStringValidation(jsAw.getString("Code")))
                    code = jsAw.getString("Code");
                call_array.add(nam);
            }
            mCommonSharedPreference.setValueToPreferenceFeed("timeCount", 0);


            feedCallJoinAdapter = new FeedCallJoinAdapter(FeedbackActivity.this, joint_array);
            listView_feed_join.setAdapter(feedCallJoinAdapter);
            feedCallJoinAdapter.notifyDataSetChanged();

            feedCallJoinAdapter = new FeedCallJoinAdapter(FeedbackActivity.this, call_array);
            listView_feed_call.setAdapter(feedCallJoinAdapter);
            feedCallJoinAdapter.notifyDataSetChanged();

            feedInputAdapter = new FeedInputAdapter(FeedbackActivity.this, input_array);
            listView_feed_input.setAdapter(feedInputAdapter);
            feedInputAdapter.notifyDataSetChanged();

            JSONArray jsonBrnd = json.getJSONArray("RCPAEntry");
            Log.v("jsonBrnd_val", String.valueOf(jsonBrnd));
            jsonBrandValue = String.valueOf(jsonBrnd);
            mCommonSharedPreference.setValueToPreference("jsonarray", jsonBrnd.toString());

            signPath = json.getString("sign_path");
            Log.v("sign_paths_fou", baseurl + signPath);
            if (!TextUtils.isEmpty(signPath)) {
                if (signPath.substring(0, 1).equalsIgnoreCase("s")) {
                    new DownloadingImage(baseurl + signPath).execute();
                } else {
                    Drawable d = Drawable.createFromPath(signPath);
                    sign_lay.setBackground(d);
                }
            }

        } catch (Exception e) {
            Log.v("printing_exception", e.getMessage());
        }
        dbh.close();

    }

    public boolean funStringValidation(String val) {
        if (TextUtils.isEmpty(val))
            return false;
        else
            return true;
    }


    public void finalSubmission(String val) {
        if (progressDialog == null) {
            CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(FeedbackActivity.this);
            progressDialog = commonUtilsMethods.createProgressDialog(FeedbackActivity.this);
            progressDialog.show();
        } else {
            progressDialog.show();
        }
        Call<ResponseBody> query;
        if (signPath.trim().isEmpty() && filePath.isEmpty()) {
            query = apiService.finalSubmit(val);
        } else {
            Log.v("signature_pic", signPath);
            Log.v("datasave",val);
            HashMap<String, RequestBody> values = field(val);
            MultipartBody.Part fileNeed = convertimg("SignImg", signPath);
            query = apiService.uploadData(values, fileNeed);
        }

        query.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStreamReader ip = null;
                StringBuilder is = new StringBuilder();
                String line = null;
                Log.d("datawise", String.valueOf(response.body()));
                try {
                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    Log.v("final_submit_working", is.toString());
                    JSONObject js = new JSONObject(is.toString());

                    if (js.getString("success").equals("true")) {
                        progressDialog.dismiss();
                        Toast toast = Toast.makeText(FeedbackActivity.this, getResources().getString(R.string.data_success), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        dbh.open();
                        mCommonSharedPreference.clearFeedShare();
                        dbh.deleteFeed();
                        dbh.close();
                        mCommonSharedPreference.setValueToPreference("jsonarray", "");

                        if (!TextUtils.isEmpty(filePath))
                            updateCaptureFile();
                        //Toast.makeText(FeedbackActivity.this, "Data Submitted Successfully", Toast.LENGTH_SHORT).show();
                        mCommonSharedPreference.setValueToPreference("detno", "");
                        Intent i = new Intent(FeedbackActivity.this, HomeDashBoard.class);
                        startActivity(i);
                        mCommonSharedPreference.setValueToPreference("availjson", "");

                    } else {
                        progressDialog.dismiss();

                        if (js.has("Msg")) {
                            Toast toast = Toast.makeText(FeedbackActivity.this, js.getString("Msg"), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        } else {
                            Toast toast = Toast.makeText(FeedbackActivity.this, getResources().getString(R.string.data_notsuccess), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        //Toast.makeText(FeedbackActivity.this, " OOPS!! data not submitted ", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Log.v("feedProduct",e.getMessage());
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    public boolean CheckPermission() {
        boolean val = false;
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new android.app.AlertDialog.Builder(FeedbackActivity.this)
                    .setTitle("Alert")  // GPS not found
                    .setCancelable(false)
                    .setMessage("Activate the Gps to proceed futher") // Want to enable?
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .show();

        } else {
            val = true;
            //  locationTrack=new LocationTrack(FeedbackActivity.this);
        }
        return val;
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    public String captureCanvasScreen(View layBg) {
        txt_sign.setText("");
        View content = layBg;
        content.setDrawingCacheEnabled(true);
        content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = content.getDrawingCache();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path, "EDetails/Pictures");
        if (!file.exists()) {
            file.mkdirs();
        }
        String file_path = file + "/" + "paint_" + System.currentTimeMillis() + ".png";
        FileOutputStream ostream;
        try {
            ostream = new FileOutputStream(file_path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.flush();
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("signature_print_file", file_path);
        return file_path;
    }

    public static void clearLay() {
        sign_lay.setBackgroundResource(0);
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String findingEndTime(int k) {
        if (checkForLastSlide(k)) {
            Log.v("calling_default1",defaulttime);

            return defaulttime;

        } else {
            int j = k + 1;

            String eTime = mCommonSharedPreference.getValueFromPreferenceFeed("timeVal"+j );
            Log.v("calling_default", eTime);

            return eTime;
       }
    }

    public boolean checkForLastSlide(int k) {
        Log.v("values>>",""+k+" "+val);
        if (k == val - 1)
            return true;
        else
            return false;

    }

    public int checkForProduct(String slidename) {
        for (int i = 0; i < arrayStore.size(); i++) {
            if (arrayStore.get(i).getSlideNam().equals(slidename)) {
                return i;
            }
        }
        return -1;
    }

    public String gettingProductStartEndTime(String jsonvalue, int i) {
        String finalTime = null;
        StoreImageTypeUrl mm, mm1;
        try {
            JSONArray json = new JSONArray(jsonvalue);
            if (i != 0) {

               /* if(i==arrayStore.size()-1){
                    mm1=arrayStore.get(i);
                }
                else*/
                mm1 = arrayStore.get(i - 1);
                json = new JSONArray(mm1.getRemTime());
                JSONObject jj = json.getJSONObject(0);
                Log.v("first_value_time", jj.getString("eT"));
                endT = jj.getString("eT");
            }

            finalTime = startT + " " + endT;

            // if(i!=arrayStore.size()-1) {
            mm = arrayStore.get(i);
            json = new JSONArray(mm.getRemTime());
            JSONObject jj = json.getJSONObject(0);
            Log.v("last_value_timemid", jj.getString("sT"));
            startT = jj.getString("sT");
           /* }
            else{

            }*/

            if (arrayStore.size() == 1) {
                mm = arrayStore.get(i);
                json = new JSONArray(mm.getRemTime());
                JSONObject jjj = json.getJSONObject(0);
                Log.v("last_value_time", jjj.getString("sT"));
                startT = jjj.getString("sT");
                finalTime = startT + " " + jjj.getString("eT");
            }
            Log.v("last_finall", finalTime);
            return finalTime;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return finalTime;
    }


    public HashMap<String, RequestBody> field(String val) {
        HashMap<String, RequestBody> xx = new HashMap<String, RequestBody>();
        xx.put("data", createFromString(val));

        return xx;

    }

    private RequestBody createFromString(String txt) {
        return RequestBody.create(MultipartBody.FORM, txt);
    }

    public MultipartBody.Part convertimg(String tag, String path) {


        MultipartBody.Part yy = null;
        //Log.v("full_profile",path);

        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
            yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
        }

        return yy;
    }


    private class DownloadingImage extends AsyncTask<Void, Void, Void> {

        String imgurl;

        public DownloadingImage(String url) {
            this.imgurl = url;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                saveImage(imgurl, "signature.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.v("singanga_path", signPath);
            Drawable d = Drawable.createFromPath(signPath);
            sign_lay.setBackground(d);
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";

        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            Drawable d = new BitmapDrawable(getResources(), result);
            sign_lay.setBackground(d);
            Log.v("finally_we_update", "imagesss");
            //saveImage(getApplicationContext(), result, "my_image.png");

        }
    }

    public String saveImage(String imageUrl, String image_name) throws IOException {

        String pathImaging = "";
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path, "EDetails/Pictures");
        if (!file.exists()) {
            file.mkdirs();
        }


        Log.v("imging", imageUrl + "names" + image_name);
        Log.v("stringPathimg", String.valueOf(file));
        String paint_path_noView = file + "/" + image_name;

        FileOutputStream ostream;
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            try {
                ostream = new FileOutputStream(paint_path_noView);
                Log.d("path_of_img", paint_path_noView);

                pathImaging = paint_path_noView;


                try {
                    byte[] b = new byte[2048];
                    int length;

                    while ((length = is.read(b, 0, b.length)) >= 0) {
                        ostream.write(b, 0, length);
                    }
                } finally {
                    ostream.close();
                }
            } finally {
                is.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("path_of_img", "error");
        }

        signPath = pathImaging;
        return pathImaging;
    }

    public String getProdFb(ArrayList<PopFeed> arr) {
        String value = "", code = "";

        if (arr.contains(new PopFeed(true))) {
            for (int i = 0; i < arr.size(); i++) {
                PopFeed m = arr.get(i);
                if (m.isClick()) {
                    code = code + m.getCode() + "/";
                    value = value + m.getTxt() + "/";
                            /*i=array_selection.size();
                            adp_view.notifyDataSetChanged();*/
                    break;
                }
            }
            return code + "," + value;
        } else
            return "";
    }

    public void captureFileLower() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Uri outputFileUri = Uri.fromFile(new File(getExternalCacheDir().getPath(), "pickImageResult.jpeg"));
        //outputFileUri = FileProvider.getUriForFile(FeedbackActivity.this, getApplicationContext().getPackageName() + ".fileprovider", new File(getExternalCacheDir().getPath(), "pickImageResult"+System.currentTimeMillis()+".jpeg"));
        //Log.v("priniting_uri",outputFileUri.toString()+" output "+outputFileUri.getPath()+" raw_msg "+getExternalCacheDir().getPath());
        //content://com.saneforce.sbiapplication.fileprovider/shared_video/Android/data/com.saneforce.sbiapplication/cache/pickImageResult.jpeg
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 19);
    }

    public void captureFile() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Uri outputFileUri = Uri.fromFile(new File(getExternalCacheDir().getPath(), "pickImageResult.jpeg"));
        outputFileUri = FileProvider.getUriForFile(FeedbackActivity.this, getApplicationContext().getPackageName() + ".fileprovider", new File(getExternalCacheDir().getPath(), "pickImageResult" + System.currentTimeMillis() + ".jpeg"));
        Log.v("priniting_uri", outputFileUri.toString() + " output " + outputFileUri.getPath() + " raw_msg " + getExternalCacheDir().getPath());
        //content://com.saneforce.sbiapplication.fileprovider/shared_video/Android/data/com.saneforce.sbiapplication/cache/pickImageResult.jpeg
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 15);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void updateCaptureFile() {
        MultipartBody.Part imgg = convertimg("ScribbleImg", filePath);
        HashMap<String, RequestBody> values = field("MR0417");
        Call<ResponseBody> upload = apiService.uploadphoto(values, imgg);
        upload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("uploading_scr", String.valueOf(response.isSuccessful()));
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
                        Log.v("printing_scribble", is.toString());
                        JSONObject json = new JSONObject(is.toString());
                        if (json.getString("success").equalsIgnoreCase("true")) {

                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("printing_scribble", "Exception" + t);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mCommonSharedPreference.clearFeedShare();

//        dbh.open();
//        dbh.deleteFeed();
//        dbh.close();

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            /*init();
            startLocationButtonClick();*/

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String finalPath = "/storage/emulated/0";
        if (requestCode == 6) {
            Log.v("brandactivtiy", "are_back");
        } else if (requestCode == 15) {

            filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));
            Log.v("printing_final_", filePath);
        } else if (requestCode == 19) {
            Log.v("printing_path", data.getData() + " request ");
            Uri uri = data.getData();
            filePath = ImageFilePath.getPath(FeedbackActivity.this, uri);

            /*filePath=uri.getPath();
            filePath=filePath.substring(1);
            filePath=finalPath+filePath.substring(filePath.indexOf("/"));*/
            Log.v("printing_final1_", filePath);
        }
    }


}
