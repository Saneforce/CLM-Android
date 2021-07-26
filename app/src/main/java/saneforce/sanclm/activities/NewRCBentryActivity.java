package saneforce.sanclm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.CompNameProduct;
import saneforce.sanclm.activities.Model.Feedbacklist;
import saneforce.sanclm.activities.Model.ModelBrandAuditList;
import saneforce.sanclm.activities.Model.ModelDynamicList;
import saneforce.sanclm.activities.Model.ModelDynamicView;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.activities.Model.SlideDetail;
import saneforce.sanclm.adapter_class.AdapterBrandAuditList2;
import saneforce.sanclm.adapter_class.AdapterPopupSpinnerSelection;
import saneforce.sanclm.adapter_class.FeedCallJoinAdapter;
import saneforce.sanclm.adapter_class.GalleryAdapter;
import saneforce.sanclm.adapter_class.PopupAdapter;
import saneforce.sanclm.adapter_class.TemplateAdapter;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.fragments.LocaleHelper;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.DataInterface;
import saneforce.sanclm.util.UpdateUi;

import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;

public class NewRCBentryActivity extends AppCompatActivity implements DataInterface {
    private static String compURL = "http://sanclm.info/";
    public static int PICK_IMAGE_MULTIPLE = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;
    Resources resources;

    ListView list_comp, listView_feed_call;
    RecyclerView listview_audit_list;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<CompNameProductNew> listComp = new ArrayList<CompNameProductNew>();
    ArrayList<CompNameProductNew> full_list_prd = new ArrayList<>();
    ArrayList<PopFeed> chem_select_list = new ArrayList<>();
    ArrayList<SlideDetail> prd_list = new ArrayList<>();
    ArrayList<String> prd_list_rate = new ArrayList<>();
    ArrayList<PopFeed> chem_list = new ArrayList<>();
    ArrayList<ModelBrandAuditList> brandList = new ArrayList<>();
    ArrayList<ModelBrandAuditList> choosenBrandList = new ArrayList<>();

    AdapterBrandAuditComp2 adapterBrandAuditComp;
    Button chem_plus;
    ImageView iv_dwnldmaster_back;
    DataBaseHandler dbh;
    Cursor mCursor;
    FeedCallJoinAdapter feedCallJoinAdapter;
    TextView edt_search_brd;
    RelativeLayout rl_search_brd;
    public static float prd_rate = 0;
    EditText edt_qty;
    EditText edt_rate, edt_val, edt_sw, edt_rx;
    TextView dr_name;
    Button btn_add_brd, btn_add_comp, save_btn;
    CommonSharedPreference mCommonSharedPreference;
    String prdEnterCode = null;
    String finalProduct = "ee";
    String competitorName, competitorBrand, competitorQnty;
    JSONObject obj = new JSONObject();
    String SF_Code = "", db_connPath;
    Api_Interface apiService;
    String imageEncoded;
    List<String> imagesEncodedList;
    public static RecyclerView gvGallery;
    public static  ImageView imageView;
    public static GalleryAdapter galleryAdapter;
    public static ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    public static ArrayList<String> imagelist ;

    static String Fcode;
    public static JSONObject competitorjson;
    public static JSONArray competiorarray;
    public static int GALLERY = 1, CAMERA = 2;
    public static ArrayList<ModelDynamicList> array = new ArrayList<>();
    public static ArrayList<ModelDynamicView> array_view = new ArrayList<>();
    int pos_upload_file = 0;
    String language="";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_r_c_bentry);
        dbh = new DataBaseHandler(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mCommonSharedPreference = new CommonSharedPreference(this);
        SF_Code = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SharedPreferences sharedPreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        language = sharedPreferences.getString(language_string, "");
        if (!language.equals("")){
            Log.d("homelang",language);
            selected(language);
            context = LocaleHelper.setLocale(this, language);
            resources = context.getResources();
        }else {
            selected("en");
            context = LocaleHelper.setLocale(this, "en");
            resources = context.getResources();
        }
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        try {
            obj.put("SF", SF_Code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
      //  NewcopList();
        list_comp = (ListView) findViewById(R.id.list_comp);
        listView_feed_call = (ListView) findViewById(R.id.listView_feed_call);
        listview_audit_list = (RecyclerView) findViewById(R.id.listview_audit_list);
        listview_audit_list.setHasFixedSize(true);
        listview_audit_list.setLayoutManager(new LinearLayoutManager(this));
        chem_plus = (Button) findViewById(R.id.chem_plus);
        edt_search_brd = (TextView) findViewById(R.id.edt_search_brd);
        rl_search_brd = (RelativeLayout) findViewById(R.id.rl_search_brd);
        edt_qty = (EditText) findViewById(R.id.edt_qty);
        edt_rate = findViewById(R.id.edt_rate);
        edt_val = findViewById(R.id.edt_val);
        edt_sw = findViewById(R.id.edt_sw);
        edt_rx = findViewById(R.id.edt_rx);
        dr_name = (TextView) findViewById(R.id.dr_name);
        btn_add_brd = (Button) findViewById(R.id.btn_add_brd);
        btn_add_comp = (Button) findViewById(R.id.btn_add_comp);
        save_btn = (Button) findViewById(R.id.save_btn);
        iv_dwnldmaster_back = (ImageView) findViewById(R.id.iv_dwnldmaster_back);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
//
        AdapterBrandAuditComp2.bindListenerBrand(new UpdateUi() {
            @Override
            public void updatingui() {
                commonFun();
            }
        });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String yy = extra.getString("json_val");
            dr_name.setText(extra.getString("name"));
        // jsonExtractionnew(yy);
           jsonExtraction(yy);
            Log.e("Doc_Name", extra.getString("name"));
            Log.v("extract_it_print", "ing_inside" + yy);
            Log.e("json_val", extra.getString("json_val"));
            addBrandValues();

        }else{


            dr_name.setText(mCommonSharedPreference.getValueFromPreference("drName"));

        }


//        dr_name.setText(getIntent().getSerializableExtra("name").toString());
//        jsonExtraction(getIntent().getSerializableExtra("json_val").toString());
//        Log.e("Doc_Name_edt", getIntent().getSerializableExtra("name").toString());


//        else {
//        }

/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideSoftKeyboard();
            }
        },150);
*/

        iv_dwnldmaster_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewRCBentryActivity.this, FeedbackActivity.class);
                i.putExtra("custype","0");

                setResult(6, i);
                finish();
            }
        });
        btn_add_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSingleCompAdapter();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(brandList.size()>0){
//                    addBrandValue();
//
//                }

              if (chem_select_list.size() < 1&&brandList.size()==0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sclt_doc), Toast.LENGTH_LONG).show();

                } else if (edt_search_brd.getText().toString().isEmpty()&&brandList.size()==0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sclt_prdnm), Toast.LENGTH_LONG).show();

                } else if (edt_qty.getText().toString().isEmpty()&&brandList.size()==0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.scltprdqty), Toast.LENGTH_LONG).show();

                }

                else {
                    addBrandValue();

                }

            }

        });

        btn_add_brd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addBrandValues();
                listComp.clear();



            }
        });

        edt_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                commonFun();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                String val = String.valueOf(editable);
//                int qtyVal = 0;
//                if (!TextUtils.isEmpty(val))
//                    qtyVal = Integer.parseInt(val);
//                if (!TextUtils.isEmpty(edt_qty.getText().toString())) {
//                    edt_rate.setText(String.valueOf(prd_rate));
//                    float cal = qtyVal;
//                    cal = cal * prd_rate;
//                    edt_val.setText(String.valueOf(cal));

                // }
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
/*
        edt_qty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch(result) {
                    case EditorInfo.IME_ACTION_DONE:
                        // done stuff
                        Log.v("action_done","are_clicked");
                        if(!TextUtils.isEmpty(edt_qty.getText().toString())){
                            edt_rate.setText(String.valueOf(prd_rate));
                            float cal= Float.parseFloat(edt_qty.getText().toString());
                            cal=cal*prd_rate;
                            edt_val.setText(String.valueOf(cal));

                        }
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        // next stuff
                        break;
                }
                return false;
            }
        });
*/


        prd_list.clear();
        dbh.open();
        mCursor = dbh.select_product_content_master();

        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("product_name_feed", mCursor.getString(0));
                prd_list.add(new SlideDetail(mCursor.getString(0), mCursor.getString(2)));
                prd_list_rate.add(mCursor.getString(1));

            } while (mCursor.moveToNext());

        }
        mCursor.close();
        dbh.close();


        rl_search_brd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popUpAlert(prd_list);
            }
        });


        chem_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chem_list.clear();
                dbh.open();
                mCursor = dbh.select_doctor_listnew();

                if (mCursor.getCount() != 0) {
                    mCursor.moveToFirst();
                    do {
                        Log.v("plus_name_feed", mCursor.getString(0));
                        chem_list.add(new PopFeed(mCursor.getString(0), false, mCursor.getString(1)));
                    } while (mCursor.moveToNext());
                    popUpAlertFeed(chem_list);
                }
                mCursor.close();
                dbh.close();
            }
        });


    }

    private void jsonExtractionnew(String yy) {
        try {
            JSONArray jsonArray=new JSONArray(yy);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                JSONArray array=jsonObject.getJSONArray("Competitors");
                JSONArray jchem = jsonObject.getJSONArray("Chemists");

                for(int j=0;j<array.length();j++){
                    JSONObject object=array.getJSONObject(j);
                    ModelBrandAuditList modelBrandAuditList=new ModelBrandAuditList(jsonObject.getString("OPName"),object.getString("CompName"),object.getString("CompPName"),
                            object.getString("CPQty"),object.getString("CPptp"),object.getString("CPptr"),object.getString("CSw"),object.getString("CRx"),
                            object.getString("CompCode"),object.getString("CompPCode"),object.getJSONArray("feedback"),jsonObject.getString("OPCode"));

                                        if (finalProduct.contains(object.getString("CompPName"))) {
                                        }else {
                                            brandList.add(modelBrandAuditList);
                                     finalProduct += object.getString("CompPName");

                                        }



                }


                AdapterBrandAuditList2 adapter=new   AdapterBrandAuditList2(NewRCBentryActivity.this,brandList);
                listview_audit_list.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                chem_select_list.clear();
                for (int k = 0; k < jchem.length(); k++) {
                    Log.v("model_brand_audi",jsonObject.getString("OPName"));

                    JSONObject jsonchem = jchem.getJSONObject(k);
                    chem_select_list.add(new PopFeed(jsonchem.getString("Name"), false, jsonchem.getString("Code")));
                }
                JSONObject jsonnn = new JSONObject();
                jsonnn.put("Chemists", jchem);
                choosenBrandList.add(new ModelBrandAuditList( "",jsonObject.getString("OPName"), jsonObject.getString("OPQty"), jsonObject.getString("OPptp"), jsonObject.getString("OPptr"), jsonObject.getString("CSw"), jsonObject.getString("CRx"), "1", jsonnn.toString(),jsonObject.getString("OPCode")));




            }



            feedCallJoinAdapter = new FeedCallJoinAdapter(NewRCBentryActivity.this, chem_select_list, true);
            listView_feed_call.setAdapter(feedCallJoinAdapter);
            feedCallJoinAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void popUpAlertFeed(final ArrayList<PopFeed> xx) {

        final Dialog dialog = new Dialog(NewRCBentryActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_feedback);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button ok = (Button) dialog.findViewById(R.id.ok);
        final ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);
        ImageView iv_close_popup = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
        TextView textView=dialog.findViewById(R.id.tv_todayplan_popup_head);
        textView.setText(resources.getString(R.string.docnm));
        EditText search_edit = (EditText) dialog.findViewById(R.id.et_search);

        final PopupAdapter popupAdapter = new PopupAdapter(NewRCBentryActivity.this, xx);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        search_edit.addTextChangedListener(new TextWatcher() {
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
                        chem_select_list.add(new PopFeed(popFeed.getTxt(), false, popFeed.getCode()));
                    }
                }
                feedCallJoinAdapter = new FeedCallJoinAdapter(NewRCBentryActivity.this, chem_select_list, true);
                listView_feed_call.setAdapter(feedCallJoinAdapter);
                //listView_feed_call.setEmptyView(findViewById(android.R.id.empty));
                feedCallJoinAdapter.notifyDataSetChanged();
                dialog.dismiss();

                commonFun();
            }
        });

        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonFun();
                dialog.dismiss();
            }
        });

    }

    public void popUpAlert(final ArrayList<SlideDetail> list) {


        final Dialog dialog = new Dialog(NewRCBentryActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_spinner_selection);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ImageView close_img = (ImageView) dialog.findViewById(R.id.close_img);
        ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);

        AdapterPopupSpinnerSelection popupAdapter = new AdapterPopupSpinnerSelection(NewRCBentryActivity.this, list);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edt_search_brd.setText(list.get(i).getInputName());
                prd_rate = Float.parseFloat(prd_list_rate.get(i));
                prdEnterCode = list.get(i).getIqty();
                edt_qty.setText("");
                edt_rate.setText("");
                edt_rx.setText("");
                edt_sw.setText("");
                edt_val.setText("");
                dialog.dismiss();
                commonFun();
                listComp.clear();
                newmethod(prdEnterCode);


            }
        });

        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                commonFun();
            }
        });

    }

    private void newmethod(String prdEnterCode) {
        dbh.open();
        mCursor = dbh.select_comp_list_new(prdEnterCode);

        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("comp_name_feed", mCursor.getString(0) + " comp_prd" + mCursor.getString(1));

                CompNameProductNew compNameProductNew = new CompNameProductNew();
                compNameProductNew.setCcode(mCursor.getString(0));
                compNameProductNew.setCName(mCursor.getString(1));
                compNameProductNew.setPCode(mCursor.getString(2));
                compNameProductNew.setPName(mCursor.getString(3));
                compNameProductNew.setInvqty("");
                compNameProductNew.setPtp("");
                compNameProductNew.setPtr("");
                compNameProductNew.setSw("");
                compNameProductNew.setRx("");
                compNameProductNew.setFeedback(new JSONArray());
                compNameProductNew.setFmessage("");
                listComp.add(compNameProductNew);
//                listComp.add(new CompNameProduct(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3)));

            } while (mCursor.moveToNext());

        }
        mCursor.close();
        dbh.close();
        callCompAdapter();


    }

    public void callCompAdapter() {
        full_list_prd.clear();
        CompNameProductNew compNameProductNew = new CompNameProductNew();

        compNameProductNew.setCcode("");
        compNameProductNew.setCName("");
        compNameProductNew.setPCode("");
        compNameProductNew.setPName("");
        compNameProductNew.setInvqty("");
        compNameProductNew.setPtp("");
        compNameProductNew.setPtr("");
        compNameProductNew.setSw("");
        compNameProductNew.setRx("");
        compNameProductNew.setFeedback(new JSONArray());
        compNameProductNew.setFmessage("");
        full_list_prd.add(compNameProductNew);


        adapterBrandAuditComp = new AdapterBrandAuditComp2(full_list_prd,NewRCBentryActivity.this, listComp,this);
        list_comp.setAdapter(adapterBrandAuditComp);
        // list_comp.setEmptyView(findViewById(R.id.list_comp));
        adapterBrandAuditComp.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void addSingleCompAdapter() {
        CompNameProductNew compNameProductNew = new CompNameProductNew();
        compNameProductNew.setCcode("");
        compNameProductNew.setCName("");
        compNameProductNew.setPCode("");
        compNameProductNew.setPName("");
        compNameProductNew.setInvqty("");
        compNameProductNew.setPtp("");
        compNameProductNew.setPtr("");
        compNameProductNew.setSw("");
        compNameProductNew.setRx("");
        compNameProductNew.setFeedback(new JSONArray());
        compNameProductNew.setFmessage("");
        full_list_prd.add(compNameProductNew);
        adapterBrandAuditComp = new AdapterBrandAuditComp2(full_list_prd,NewRCBentryActivity.this, listComp,this);
        list_comp.setAdapter(adapterBrandAuditComp);
        adapterBrandAuditComp.notifyDataSetChanged();
    }


    public void hideSoftKeyboard() {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(edt_qty.getWindowToken(), 0);

    }

    public void jsonSave() {
        try {

            JSONObject chemObj = null;
            JSONArray chemArr = new JSONArray();
            JSONArray MainchemArr = new JSONArray();
            JSONObject jsonObject = null;


            for (int k = 0; k < choosenBrandList.size(); k++) {


                Log.v("choosenBrandList", String.valueOf(choosenBrandList.size()));
                chemObj = new JSONObject();
                ModelBrandAuditList ll = choosenBrandList.get(k);
                chemObj = new JSONObject(ll.getJsonChem());


                Log.v("chem_obj_inside", String.valueOf(chemObj));
                if (!ll.getComName().isEmpty()) {
                    chemObj.put("OPCode", ll.getPrcode());
                    chemObj.put("OPName", ll.getComName());
                    chemObj.put("OPQty", ll.getQty());
                    chemObj.put("OPptp", ll.getRate());
                    chemObj.put("OPptr", ll.getVal());
                    chemObj.put("OPsw", ll.getSw());
                    chemObj.put("OPrx", ll.getRx());
                    Log.v("choosenBrandjson", String.valueOf(chemObj));
                    for (int i = 0; i < brandList.size(); i++) {

                        ModelBrandAuditList mm = brandList.get(i);
                        if (ll.getComName().equals(mm.getPrName())) {
                            jsonObject = new JSONObject();
                            jsonObject.put("CSw", mm.getSw());
                            jsonObject.put("CRx", mm.getRx());
                            jsonObject.put("CPQty", mm.getQty());
                            jsonObject.put("CPptp", mm.getRate());
                            jsonObject.put("CPptr", mm.getVal());
                            jsonObject.put("CompCode", mm.getCompCode());
                            jsonObject.put("CompName", mm.getComName());
                            jsonObject.put("CompPCode", mm.getCompPcode());
                            jsonObject.put("CompPName", mm.getComPrdName());
                            jsonObject.put("feedback", mm.getFeedback());
                            Log.v("choosenBrandprd", String.valueOf(jsonObject));

                        } else {
                            continue;

                        }
                        MainchemArr.put(jsonObject);

                    }

                    chemObj.put("Competitors", MainchemArr);
                    MainchemArr = new JSONArray();

//                chemObj.put("FeedbackData",adapterBrandAuditComp.feedbackdata());
                    chemArr.put(chemObj);

                }


                Log.v("Printing_comp_prd", String.valueOf(chemArr));
                mCommonSharedPreference.setValueToPreference("jsonarray", String.valueOf(chemArr));
            }

        } catch (Exception e) {
        }

    }


    public void jsonExtraction(String jsonVal) {

        try {
            JSONArray jsonArray=new JSONArray(jsonVal);
            Log.v("json_array_turn", String.valueOf(jsonArray.length()));

            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String opname=jsonObject.getString("OPName");
                JSONArray jchem = jsonObject.getJSONArray("Chemists");
                JSONArray carray=jsonObject.getJSONArray("Competitors");
                for(int j=0; j< carray.length(); j++) {

                    JSONObject object = carray.getJSONObject(j);
                    String opname1=jsonObject.getString("OPName");


                    if (finalProduct.contains(object.getString("CompPName"))) {

                    } else {

                        ModelBrandAuditList modelBrandAuditList=new ModelBrandAuditList(opname,object.getString("CompName"),object.getString("CompPName")
                                ,object.getString("CPQty"),object.getString("CPptp"),object.getString("CPptr"),object.getString("CRx"),object.getString("CSw"),
                                object.getString("CompCode"),object.getString("CompPCode"), object.getJSONArray("feedback"),jsonObject.getString("OPCode"));


                        brandList.add(modelBrandAuditList);
                        finalProduct += object.getString("CompPName");

                    }
                }

                AdapterBrandAuditList2 adp = new AdapterBrandAuditList2(NewRCBentryActivity.this, brandList);
                listview_audit_list.setAdapter(adp);
                adp.notifyDataSetChanged();

                chem_select_list.clear();
                for (int k = 0; k < jchem.length(); k++) {
                    Log.v("model_brand_audi",jsonObject.getString("OPName"));

                    JSONObject jsonchem = jchem.getJSONObject(k);
                    chem_select_list.add(new PopFeed(jsonchem.getString("Name"), false, jsonchem.getString("Code")));
                }
                JSONObject jsonnn = new JSONObject();
                jsonnn.put("Chemists", jchem);
                choosenBrandList.add(new ModelBrandAuditList( "",opname, jsonObject.getString("OPQty"), jsonObject.getString("OPptp"), jsonObject.getString("OPptr"), jsonObject.getString("OPsw"), jsonObject.getString("OPrx"),"1", jsonnn.toString(),jsonObject.getString("OPCode")));








            }




            feedCallJoinAdapter = new FeedCallJoinAdapter(NewRCBentryActivity.this, chem_select_list, true);
            listView_feed_call.setAdapter(feedCallJoinAdapter);
            feedCallJoinAdapter.notifyDataSetChanged();



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void addBrandValue() {
        for (int i = 0; i < AdapterBrandAuditComp2.list_prd1.size(); i++) {
            CompNameProductNew mm = AdapterBrandAuditComp2.list_prd1.get(i);


            if (!TextUtils.isEmpty(mm.getInvqty())) {


                    Log.v("CompanyName", mm.getCName() + " edit_val " + edt_search_brd.getText().toString() + " chosenprd " + mm.getPName() + " compcod " + mm.getCcode() + " pcode " + mm.getPCode());
                    brandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), mm.getCName(), mm.getPName(), mm.getInvqty(), mm.getPtp(), mm.getPtr(), mm.getSw(), mm.getRx(), mm.getCcode(), mm.getPCode(), mm.getFeedback(),prdEnterCode));

            }
        }
        Log.v("brandsize", String.valueOf(brandList.size()));


        Log.v("last_select_array", String.valueOf(chem_select_list.size()));
        JSONArray arr = new JSONArray();
        JSONObject chemlist = new JSONObject();
        for (int k = 0; k < chem_select_list.size(); k++) {
            try {
                JSONObject json = new JSONObject();
                json.put("Name", chem_select_list.get(k).getTxt());
                json.put("Code", chem_select_list.get(k).getCode());
                arr.put(json);
            } catch (Exception e) {
            }

        }
        try {
            chemlist.put("Chemists", arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        choosenBrandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), edt_search_brd.getText().toString(), edt_qty.getText().toString(), edt_rate.getText().toString(), edt_val.getText().toString(), edt_sw.getText().toString(), edt_rx.getText().toString(), prdEnterCode, chemlist.toString(),prdEnterCode));

        Log.v("brandsize", String.valueOf(choosenBrandList.size()));



        AdapterBrandAuditList2 adp = new AdapterBrandAuditList2(NewRCBentryActivity.this, brandList);
        listview_audit_list.setAdapter(adp);
        adp.notifyDataSetChanged();

        if (brandList.size() < 1) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.sclt_compdetl), Toast.LENGTH_LONG).show();
            return;
        } else {
            jsonSave();

            Intent i = new Intent(NewRCBentryActivity.this, FeedbackActivity.class);
            i.putExtra("custype", "0");
            setResult(6, i);

            finish();
        }




        edt_search_brd.setText("");
        edt_qty.setText("");
        edt_rate.setText("");
        edt_val.setText("");
        edt_sw.setText("");
        edt_rx.setText("");
        callCompAdapter();


    }

    public void addBrandValues() {

        for (int i = 0; i < AdapterBrandAuditComp2.list_prd1.size(); i++) {
            CompNameProductNew mm = AdapterBrandAuditComp2.list_prd1.get(i);

            if (!TextUtils.isEmpty(mm.getCName()) && !TextUtils.isEmpty(mm.getPName()) && !TextUtils.isEmpty(mm.getInvqty())) {
                if (finalProduct.contains(mm.getPName() )) {
                } else {
                    finalProduct += mm.getPName();


                    Log.v("CompanyName", mm.getCName() + " edit_val " + edt_search_brd.getText().toString() + " chosenprd " + mm.getPName() + " compcod " + mm.getCcode() + " pcode " + mm.getPCode());
                    brandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), mm.getCName(), mm.getPName(), mm.getInvqty(), mm.getPtp(), mm.getPtr(), mm.getSw(), mm.getRx(), mm.getCcode(), mm.getPCode(), mm.getFeedback(),prdEnterCode));
                }
            }
        }
        Log.v("brandsize", String.valueOf(brandList));

        Log.v("last_select_array", String.valueOf(chem_select_list.size()));
        JSONArray arr = new JSONArray();
        JSONObject chemlist = new JSONObject();
        for (int k = 0; k < chem_select_list.size(); k++) {
            try {
                JSONObject json = new JSONObject();
                json.put("Name", chem_select_list.get(k).getTxt());
                json.put("Code", chem_select_list.get(k).getCode());
                arr.put(json);
            } catch (Exception e) {
            }

        }
        try {
            chemlist.put("Chemists", arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        choosenBrandList.add(new ModelBrandAuditList( edt_search_brd.getText().toString(),edt_search_brd.getText().toString(), edt_qty.getText().toString(), edt_rate.getText().toString(), edt_val.getText().toString(), edt_sw.getText().toString(), edt_rx.getText().toString(), prdEnterCode, chemlist.toString(),prdEnterCode));

        Log.v("brandsize", String.valueOf(choosenBrandList.size()));


        edt_search_brd.setText("");
        edt_qty.setText("");
        edt_rate.setText("");
        edt_val.setText("");
        edt_rx.setText("");
        edt_sw.setText("");
        callCompAdapter();

        AdapterBrandAuditList2 adp = new AdapterBrandAuditList2(NewRCBentryActivity.this, brandList);
        listview_audit_list.setAdapter(adp);
        adp.notifyDataSetChanged();


    }


    public void commonFun() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public void commonPopFun(Dialog dialog) {
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    @Override
    public void competitordetails(String compName, String prodName, String qty) {
//
//        Log.v("compName",compName);
//        Log.v("prodName",prodName);
//        Log.v("compQty",qty);

        competitorName = compName;
        competitorBrand = prodName;
        competitorQnty = qty;


    }

    public void NewcopList() {
        Call<ResponseBody> chm = apiService.getNewcompetitors(String.valueOf(obj));
        chm.enqueue(NewComplist);
    }

    public Callback<ResponseBody> NewComplist = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
            if (response.isSuccessful()) {

                try {
                    dbh.open();
                    dbh.del_comp_new();
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;

                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }
                    //  dbh.delete_All_tableDatas();
                    // List<Doctors> doctors = response.body();

                    JSONArray ja = new JSONArray(is.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js1 = ja.getJSONObject(i);

                        JSONArray jsonArray = js1.getJSONArray("Cmpt");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject js = jsonArray.getJSONObject(j);
                            String OProdCd = js1.getString("OProdCd");
                            String compSlNo = js.getString("CCode");
                            String compName = js.getString("CName");
                            String compPrdSlNo = js.getString("PCode");
                            String compPrdName = js.getString("PName");
//                            listComp.add(new CompNameProductNew(compSlNo,compName,compPrdSlNo,compPrdName));

//                            dbh.NewinsertCompetitorTable(compSlNo, compName, compPrdSlNo, compPrdName, OProdCd);
//                            Log.v("printing_chompnew", compName);


                        }
                    }
//                    adapterBrandAuditComp = new NewAdapterBrandAuditComp(NewRCBentryActivity.this, listComp);
//                    list_comp.setAdapter(adapterBrandAuditComp);
//                    // list_comp.setEmptyView(findViewById(R.id.list_comp));
//                    adapterBrandAuditComp.notifyDataSetChanged();
                    dbh.close();

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


    public static class AdapterBrandAuditComp2 extends BaseAdapter {
        public static ArrayList<CompNameProductNew> list_prd1 = new ArrayList<>();
        Activity context;
        boolean detectPrdClick = false;
        TextView txt_comp_name;
        ArrayList<Feedbacklist> arraylist;
        CommonUtilsMethods commonUtilsMethods;
        UpdateUi updateUi;
        DataInterface dataInterface;
        Api_Interface apiService;
        JSONObject obj = new JSONObject();
        String Divcode = "", db_connPath;
        CommonSharedPreference mCommonSharedPreference;
        TemplateAdapter adapter;
        Spinner spinner;
        ArrayList<String> myList;
        EditText editTextfeedback;
        public static ArrayList<CompNameProductNew> full_list_prd;
        public AdapterBrandAuditComp2(ArrayList<CompNameProductNew> full_list_prd, Activity context, ArrayList<CompNameProductNew> list_prd1, DataInterface dataInterface) {
            this.context = context;
            this.list_prd1 = list_prd1;
            this.full_list_prd=full_list_prd;
            this.dataInterface=dataInterface;
            commonUtilsMethods = new CommonUtilsMethods(this.context);
            arraylist = new ArrayList<>();
            mCommonSharedPreference = new CommonSharedPreference(context);
            Divcode = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION_CODE);
            db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
            apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
            try {
                obj.put("DivCode", "2");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public static void bindListenerBrand(UpdateUi updateUi) {
            updateUi=updateUi;

        }

        private void NewcopList() {
            Call<ResponseBody> chm = apiService.getFeedback(String.valueOf(obj));
            chm.enqueue(Feedback);
        }

        public Callback<ResponseBody> Feedback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("checkUser is sucessfuld :" + response.isSuccessful());
                if (response.isSuccessful()) {
                    try {

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;

                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                        //  dbh.delete_All_tableDatas();
                        // List<Doctors> doctors = response.body();

                        JSONArray ja = new JSONArray(is.toString());
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject js1 = ja.getJSONObject(i);
                            String code = js1.getString("Code");
                            String message = js1.getString("Name");

                            Feedbacklist feedbacklist = new Feedbacklist(code, message);
                            arraylist.add(feedbacklist);


                            adapter = new TemplateAdapter(context, arraylist);
                            spinner.setAdapter(adapter);


                        }
//                    adapterBrandAuditComp = new NewAdapterBrandAuditComp(NewRCBentryActivity.this, listComp);
//                    list_comp.setAdapter(adapterBrandAuditComp);
//                    // list_comp.setEmptyView(findViewById(R.id.list_comp));
//                    adapterBrandAuditComp.notifyDataSetChanged();

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

        @Override
        public int getCount() {
            return list_prd1.size();
        }

        @Override
        public Object getItem(int i) {
            return list_prd1.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.new_row_item_brand_audit_competor, viewGroup, false);
            txt_comp_name = (TextView) view.findViewById(R.id.txt_comp_name);
            final TextView txt_comp_brd_name = (TextView) view.findViewById(R.id.txt_comp_brd_name);

            RelativeLayout comp_name_list = (RelativeLayout) view.findViewById(R.id.comp_name_list);
            RelativeLayout comp_prd_list = (RelativeLayout) view.findViewById(R.id.comp_prd_list);
            final LinearLayout ll_qty = (LinearLayout) view.findViewById(R.id.ll_qty);
            EditText etqty = view.findViewById(R.id.edt_inqty);
            EditText etptp = view.findViewById(R.id.etptp);
            EditText etptr = view.findViewById(R.id.etptr);
            EditText etsw = view.findViewById(R.id.etsw);
            EditText etrx = view.findViewById(R.id.etrx);
            etqty.setText(list_prd1.get(i).getInvqty());
            etptp.setText(list_prd1.get(i).getPtp());
            etptr.setText(list_prd1.get(i).getPtr());
            etsw.setText(list_prd1.get(i).getSw());
            etrx.setText(list_prd1.get(i).getRx());

            etqty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list_prd1.get(i).setInvqty(etqty.getText().toString());

                }
            });
            etptp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list_prd1.get(i).setPtp(etptp.getText().toString());

                }
            });
            etptr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list_prd1.get(i).setPtr(etptr.getText().toString());

                }
            });
            etsw.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list_prd1.get(i).setSw(etsw.getText().toString());

                }
            });
            etrx.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list_prd1.get(i).setRx(etrx.getText().toString());

                }
            });

            Button feedbackbtn = view.findViewById(R.id.feedbackbtn);


            feedbackbtn.setOnClickListener(new View.OnClickListener() {
                ArrayList<String>imagelist1=new ArrayList<>();

                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.alert_feedback_layout);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    Button savebtn = dialog.findViewById(R.id.savebtn);
                    TextView feedbacktext = dialog.findViewById(R.id.templatetext);
                    spinner = dialog.findViewById(R.id.template);
                    editTextfeedback = dialog.findViewById(R.id.etfeedback);
                    ImageView camerabtn = dialog.findViewById(R.id.camerabtn);
                   imageView = dialog.findViewById(R.id.imageview);

                    gvGallery = dialog.findViewById(R.id.imagerecycle);
                    if(list_prd1.get(i).getImage()==null) {


                    }else{
                        imagelist1 = list_prd1.get(i).getImage();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                        gvGallery.setLayoutManager(layoutManager);
                        galleryAdapter = new GalleryAdapter(context, imagelist1);
                        gvGallery.setAdapter(galleryAdapter);
                    }

                    editTextfeedback.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            list_prd1.get(i).setFmessage(editTextfeedback.getText().toString());
                        }
                    });
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Fcode = arraylist.get(position).getCode();
                            list_prd1.get(i).setFmessage(arraylist.get(position).getMessage());
                            editTextfeedback.setText(list_prd1.get(i).getFmessage());

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    editTextfeedback.setText(list_prd1.get(i).getFmessage());

                    savebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveEntry();

                            // feedbackdata();
                            JSONArray wordList = new JSONArray();
                            JSONObject map = new JSONObject();
                            for (int i = 0; i < list_prd1.size(); i++) {
                                try {
                                    map.put("Templatecode", Fcode);
                                    map.put("feedback", editTextfeedback.getText().toString());
                                    map.put("image", imagelist);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            wordList.put(map);
                            list_prd1.get(i).setFeedback(wordList);
                            list_prd1.get(i).setImage(imagelist);

                            dialog.dismiss();
                        }
                    });
                    camerabtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showPictureDialog();
//                            choosePhotoFromGallary();


                        }
                    });
                    feedbacktext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            arraylist.clear();
                            NewcopList();
                            feedbacktext.setVisibility(View.GONE);
                            spinner.setVisibility(View.VISIBLE);
                            spinner.performClick();


                        }
                    });


                    ImageView closebtn = dialog.findViewById(R.id.iv_close_popup);
                    closebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });
                }
            });

            final CompNameProductNew mm = list_prd1.get(i);

            txt_comp_name.setVisibility(View.VISIBLE);
            txt_comp_name.setText(mm.getCName());


            txt_comp_brd_name.setVisibility(View.VISIBLE);
            txt_comp_brd_name.setText(mm.getPName());


            return view;
        }



        private void saveEntry() {
            boolean isEmpty = false;
            int count = 0;
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
            } else {
            }

        }

        public void getMulipart(String path, int x) {
            MultipartBody.Part imgg = convertimg("file", path);
            HashMap<String, RequestBody> values = field("MR0417");
            CallApiImage(values, imgg, x);
        }
        public MultipartBody.Part convertimg(String tag, String path) {
            MultipartBody.Part yy = null;
            MultipartBody.Part body = null;

            Log.v("full_profile", path);
            try {
                if (!TextUtils.isEmpty(path)) {
                    File file1 = new File(path);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                    body = MultipartBody.Part.createFormData("photo", file1.getName(), requestFile);

//                    File file = new File(path);
//                    if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
//                        file = new Compressor(context).compressToFile(new File(path));
//                    else
//                        file = new File(path);
//                    RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
//                    yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
                }
            } catch (Exception e) {
            }
            Log.v("full_profile", yy + "");
            return body;
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
                    Toast.makeText(context, ""+response.toString(), Toast.LENGTH_SHORT).show();
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
                                mm.setUpload_sv(js.getString("url"));


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

        private void showPictureDialog() {
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
            pictureDialog.setTitle("Select Action");
            String[] pictureDialogItems = {
                    "Select photo from gallery",
                    "Capture photo from camera"};
            pictureDialog.setItems(pictureDialogItems,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    choosePhotoFromGallary();
                                    break;
                                case 1:
                                    takePhotoFromCamera();
                                    break;
                            }
                        }
                    });
            pictureDialog.show();
        }

        private void takePhotoFromCamera() {
//            int count = 0;
//
//            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
//            File newdir = new File(dir);
//            if (!newdir.exists()) {
//                newdir.mkdir();
//            }
//            String file = dir+".jpg";
//            File newfile = new File(file);
//            try {
//                newfile.createNewFile();
//            }
//            catch (IOException e)
//            {
//            }
//
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//
//             Uri mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//            Uri outputFileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, newfile);
//
//            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outputFileUri);
//
//            try {
//                intent.putExtra("return-data", true);
//                context.startActivityForResult(intent,CAMERA);
//            }
//            catch (ActivityNotFoundException e)
//            {
//                e.printStackTrace();
//            }
          Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            context.startActivityForResult(intent, CAMERA);
        }

        private void choosePhotoFromGallary() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            context.startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
        }


//       public String  feedbackdata() {
//           ArrayList<HashMap<String, String>> wordList;
//           wordList = new ArrayList<HashMap<String, String>>();
//
//
//               HashMap<String, String> map = new HashMap<String, String>();
//           for (int i = 0; i < list_prd1.size(); i++) {
//               map.put("Templatecode", Fcode);
//               map.put("feedback", editTextfeedback.getText().toString());
//               map.put("image", String.valueOf(mArrayUri));
//
//           }
//
//               wordList.add(map);
//
//
//
//           Gson gson = new GsonBuilder().create();
//           //Use GSON to serialize Array List to JSON
//           return gson.toJson(wordList);
//
//       }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    imagelist = new ArrayList<>();

                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                        String ImageFileName = getImageFilePath(this, uri);
                        imagelist.clear();
                        imagelist.add(ImageFileName);
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded  = cursor.getString(columnIndex);
                        imagesEncodedList.add(imageEncoded);
                        cursor.close();


                        ModelDynamicView mm = array_view.get(pos_upload_file);
                        mm.setValue(ImageFileName);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                        gvGallery.setLayoutManager(layoutManager);
                        galleryAdapter = new GalleryAdapter(getApplicationContext(), imagelist);
                        gvGallery.setAdapter(galleryAdapter);
                    }


                }

                    else if (data.getData() != null) {
                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                            String ImageFileName = getImageFilePath(this, mImageUri);
                            imagelist=new ArrayList<>();
                            imagelist.clear();
                            imagelist.add(ImageFileName);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                            gvGallery.setLayoutManager(layoutManager);
                            galleryAdapter = new GalleryAdapter(getApplicationContext(), imagelist);
                            gvGallery.setAdapter(galleryAdapter);
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            ModelDynamicView mm = array_view.get(pos_upload_file);
                            mm.setValue(ImageFileName);



                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());

                }
            } else if (requestCode == CAMERA) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);

                String picturePath = getRealPathFromURI(tempUri);
                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                mArrayUri.add(Uri.parse(picturePath));
                String ImageFileName=null;
              //  imageView.setImageURI(tempUri);

                ImageFileName  = getRealPathFromURI( tempUri);
                    imagelist = new ArrayList<>();
                    imagelist.clear();
                    imagelist.add(ImageFileName);


                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                gvGallery.setLayoutManager(layoutManager);
                galleryAdapter = new GalleryAdapter(getApplicationContext(), imagelist);
                gvGallery.setAdapter(galleryAdapter);
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                        .getLayoutParams();
                ModelDynamicView mm = array_view.get(pos_upload_file);
                mm.setValue(ImageFileName);
            } else {
                Toast.makeText(this, getResources().getString(R.string.hnt_pickimg),
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.v("cameraerror", e.toString());
//            Toast.makeText(this, getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG)
//                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, null, null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public String getImageFilePath(Context context, Uri uri) {

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String image_id = cursor.getString(0);
        image_id = image_id.substring(image_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = context.getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path.toString();
    }


    private void selected(String language) {
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
