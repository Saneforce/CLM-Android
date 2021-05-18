package saneforce.sanclm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;
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
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.DataInterface;
import saneforce.sanclm.util.UpdateUi;

public class NewRCBentryActivity extends AppCompatActivity implements DataInterface {
    private static String compURL = "http://sanclm.info/";
    public static int PICK_IMAGE_MULTIPLE = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;

    ListView list_comp, listView_feed_call, listview_audit_list;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<CompNameProductNew> listComp = new ArrayList<CompNameProductNew>();
    ArrayList<CompNameProduct> full_list_prd = new ArrayList<>();
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
    public GalleryAdapter galleryAdapter;
    public static ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    public static ArrayList<String> imagelist = new ArrayList<>();

    static String Fcode;
    public static JSONObject competitorjson;
    public static JSONArray competiorarray;
    public static int GALLERY = 1, CAMERA = 2;
    public static ArrayList<ModelDynamicList> array = new ArrayList<>();
    public static ArrayList<ModelDynamicView> array_view = new ArrayList<>();
    int pos_upload_file = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_r_c_bentry);
        dbh = new DataBaseHandler(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mCommonSharedPreference = new CommonSharedPreference(this);
        SF_Code = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        db_connPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);

        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        try {
            obj.put("SF", SF_Code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NewcopList();

        list_comp = (ListView) findViewById(R.id.list_comp);
        listView_feed_call = (ListView) findViewById(R.id.listView_feed_call);
        listview_audit_list = (ListView) findViewById(R.id.listview_audit_list);
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
//
//        AdapterBrandAuditComp2.bindListenerBrand(new UpdateUi() {
//            @Override
//            public void updatingui() {
//                commonFun();
//            }
//        });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String yy = extra.getString("json_val");
            dr_name.setText(extra.getString("name"));
            jsonExtraction(yy);
            Log.e("Doc_Name", extra.getString("name"));
            Log.v("extract_it_print", "ing_inside" + yy);

        }


//        dr_name.setText(getIntent().getSerializableExtra("name").toString());
//        jsonExtraction(getIntent().getSerializableExtra("json_val").toString());
//        Log.e("Doc_Name_edt", getIntent().getSerializableExtra("name").toString());


        else {
            dr_name.setText(mCommonSharedPreference.getValueFromPreference("drName"));
        }

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

                if (chem_select_list.size() < 1) {
                    Toast.makeText(getApplicationContext(), "Select Chemist Name", Toast.LENGTH_LONG).show();

                } else if (edt_search_brd.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Select Product Name ", Toast.LENGTH_LONG).show();

                } else if (edt_qty.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Select Product Qty ", Toast.LENGTH_LONG).show();

                }
//                else if(TextUtils.isEmpty(competitorName) || competitorName.equals("")||competitorName.equalsIgnoreCase("null"))
//                {
//                    Toast.makeText(getApplicationContext(),"Select Competitor Company Name ",Toast.LENGTH_LONG).show();
//                } else if(competitorBrand.isEmpty())
//                {
//                    Toast.makeText(getApplicationContext(),"Select Competitor Brand Name ",Toast.LENGTH_LONG).show();
//                }else if(competitorQnty.isEmpty())
//                {
//                    Toast.makeText(getApplicationContext(),"Enter Competitor Qty Details ",Toast.LENGTH_LONG).show();
//                }
                else {
                    addBrandValue();
//                     jsonSave();
//
//                    Intent i = new Intent(BrandAuditActivity.this, FeedbackActivity.class);
//                    setResult(6, i);
//
//                    finish();
                }

            }

        });

        btn_add_brd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addBrandValues();


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
        final PopupAdapter popupAdapter = new PopupAdapter(NewRCBentryActivity.this, xx);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                popupAdapter.getFilter().filter(s);
                return false;
            }
        });


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
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));


        adapterBrandAuditComp = new AdapterBrandAuditComp2(NewRCBentryActivity.this, listComp);
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
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        adapterBrandAuditComp = new AdapterBrandAuditComp2(NewRCBentryActivity.this, listComp);
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
                chemObj.put("OPCode", ll.getCompPcode());
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
                        jsonObject.put("feedback",mm.getFeedback());
                        Log.v("choosenBrandprd", String.valueOf(jsonObject));
                    } else {
                        continue;

                    }
                    MainchemArr.put(jsonObject);

                }

                chemObj.put("Competitors", MainchemArr);

//                chemObj.put("FeedbackData",adapterBrandAuditComp.feedbackdata());
                chemArr.put(chemObj);


            }
            Log.v("Printing_comp_prd", String.valueOf(chemArr));
            mCommonSharedPreference.setValueToPreference("jsonarray", String.valueOf(chemArr));

        } catch (Exception e) {
        }

    }


    public void jsonExtraction(String jsonVal) {

        try {
            JSONArray jsonArray = new JSONArray(jsonVal);
            Log.v("json_array_turn", String.valueOf(jsonArray.length()));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jobj = jsonArray.getJSONObject(i);
                JSONArray jarr = jobj.getJSONArray("Competitors");

                for (int j = 0; j < jarr.length(); j++) {
                    JSONObject jobj1 = jarr.getJSONObject(j);
                    if (finalProduct.contains(jobj1.getString("CompName"))) {
                    } else
                        brandList.add(new ModelBrandAuditList(jobj.getString("OPName"), jobj1.getString("CompName"), jobj1.getString("CompPName"), jobj1.getString("CPQty"), jobj1.getString("CPRate"), jobj1.getString("CPValue"), jobj1.getString("CSw"), jobj1.getString("CRx"), jobj1.getString("CompCode"), jobj1.getString("CompPCode"),jobj.getString("feedback")));
                    finalProduct += jobj1.getString("CompName");

                }

                JSONArray jchem = jobj.getJSONArray("Chemists");
                chem_select_list.clear();
                for (int k = 0; k < jchem.length(); k++) {
                    JSONObject jsonchem = jchem.getJSONObject(k);
                    chem_select_list.add(new PopFeed(jsonchem.getString("Name"), false, jsonchem.getString("Code")));
                }
                JSONObject jsonnn = new JSONObject();
                jsonnn.put("Chemists", jchem);
                //Log.v("model_brand_audi",jobj.getString("OPCode"));
                choosenBrandList.add(new ModelBrandAuditList("", jobj.getString("OPName"), jobj.getString("OPQty"), jobj.getString("OPptp"), jobj.getString("OPptr"), jobj.getString("CSw"), jobj.getString("CRx"), "1", jsonnn.toString()));

            }

            AdapterBrandAuditList2 adp = new AdapterBrandAuditList2(NewRCBentryActivity.this, brandList);
            listview_audit_list.setAdapter(adp);
            adp.notifyDataSetChanged();

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

            if (!TextUtils.isEmpty(mm.getCName()) && !TextUtils.isEmpty(mm.getPName()) && !TextUtils.isEmpty(mm.getInvqty())) {
                Log.v("CompanyName", mm.getCName() + " edit_val " + edt_search_brd.getText().toString() + " chosenprd " + mm.getPName() + " compcod " + mm.getCcode() + " pcode " + mm.getPCode());
                brandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), mm.getCName(), mm.getPName(), mm.getInvqty(), mm.getPtp(), mm.getPtr(), mm.getSw(), mm.getRx(), mm.getCcode(), mm.getPCode(),String.valueOf(mm.getFeedback())));
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
        choosenBrandList.add(new ModelBrandAuditList("", edt_search_brd.getText().toString(), edt_qty.getText().toString(), edt_rate.getText().toString(), edt_val.getText().toString(), edt_sw.getText().toString(), edt_rx.getText().toString(), prdEnterCode, chemlist.toString()));

        Log.v("brandsize", String.valueOf(choosenBrandList.size()));

//        if(brandList.size()<1)
//        {
//            Toast.makeText(getApplicationContext(),"Select Competitor Details ",Toast.LENGTH_LONG).show();
//            return;
//        }  else {
        jsonSave();

        Intent i = new Intent(NewRCBentryActivity.this, FeedbackActivity.class);
        setResult(6, i);

        finish();
        //  }

        edt_search_brd.setText("");
        edt_qty.setText("");
        edt_rate.setText("");
        edt_val.setText("");
        edt_sw.setText("");
       edt_rx.setText("");

        callCompAdapter();

        AdapterBrandAuditList2 adp = new AdapterBrandAuditList2(NewRCBentryActivity.this, brandList);
        listview_audit_list.setAdapter(adp);
        adp.notifyDataSetChanged();
    }

    public void addBrandValues() {
        for (int i = 0; i < AdapterBrandAuditComp2.list_prd1.size(); i++) {
            CompNameProductNew mm = AdapterBrandAuditComp2.list_prd1.get(i);

            if (!TextUtils.isEmpty(mm.getInvqty())) {
                Log.v("CompanyName", mm.getCName() + " edit_val " + edt_search_brd.getText().toString() + " chosenprd " + mm.getPName() + " compcod " + mm.getCcode() + " pcode " + mm.getPCode());
                brandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), mm.getCName(), mm.getPName(), mm.getInvqty(), mm.getPtp(), mm.getPtr(), mm.getSw(), mm.getRx(), mm.getCcode(), mm.getPCode(),String.valueOf(mm.getFeedback())));
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
        choosenBrandList.add(new ModelBrandAuditList("", edt_search_brd.getText().toString(), edt_qty.getText().toString(), edt_rate.getText().toString(), edt_val.getText().toString(), edt_sw.getText().toString(), edt_rx.getText().toString(), prdEnterCode, chemlist.toString()));

        Log.v("brandsize", String.valueOf(choosenBrandList.size()));

        edt_search_brd.setText("");
        edt_qty.setText("");
        edt_rate.setText("");
        edt_val.setText("");
        edt_rx.setText("");
        edt_sw.setText("");
        listComp.clear();
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

                            dbh.NewinsertCompetitorTable(compSlNo, compName, compPrdSlNo, compPrdName, OProdCd);
                            Log.v("printing_chompnew", compName);


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

        public AdapterBrandAuditComp2(Activity context, ArrayList<CompNameProductNew> list_prd1) {
            this.context = context;
            this.list_prd1 = list_prd1;
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
                @Override
                public void onClick(View v) {
                    imagelist.clear();
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
                    gvGallery = dialog.findViewById(R.id.imagerecycle);
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

                            dialog.dismiss();
                        }
                    });
                    camerabtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            showPictureDialog();

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
        public JSONArray composeJSON() throws JSONException {
            ArrayList<HashMap<String, String>> wordList;
            wordList = new ArrayList<HashMap<String, String>>();


//            HashMap<String, String> map = new HashMap<String, String>();
        JSONObject map = new JSONObject();
            JSONObject jsonObject = new JSONObject();

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < list_prd1.size(); i++) {
                CompNameProductNew productNew = list_prd1.get(i);

                map.put("Ccode", productNew.getCcode());
                map.put("CName", productNew.getCName());
                map.put("Pcode", productNew.getPCode());
                map.put("PName", productNew.getPName());
                map.put("qty", productNew.getInvqty());
                map.put("ptp", productNew.getPtp());
                map.put("ptr", productNew.getPtr());
                map.put("sw", productNew.getSw());
                map.put("rx", productNew.getRx());
                map.put("feedbackData", String.valueOf(productNew.getFeedback()));
                jsonArray.put(map);

            }

          return jsonArray;
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
            Log.v("full_profile", path);
            try {
                if (!TextUtils.isEmpty(path)) {

                    File file = new File(path);
                    if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                        file = new Compressor(context).compressToFile(new File(path));
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
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            context.startActivityForResult(intent, CAMERA);
        }

        private void choosePhotoFromGallary() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            context.startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
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
            if (requestCode == GALLERY && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {
                    String orderBy = android.provider.MediaStore.Video.Media.DATE_TAKEN;

                    Uri mImageUri = data.getData();
                    String ImageFileName = getImageFilePath(this, mImageUri);

                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, null, null, orderBy + " DESC");

                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();

                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    imagelist.add(ImageFileName);
                    mArrayUri.add(mImageUri);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    gvGallery.setLayoutManager(layoutManager);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(), imagelist);
                    gvGallery.setAdapter(galleryAdapter);
                    ModelDynamicView mm = array_view.get(pos_upload_file);
                    mm.setValue(ImageFileName);

                } else {

                    if (data.getClipData() != null) {

                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);

                            String ImageFileName = getImageFilePath(this, uri);

                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();
                            imagelist.add(ImageFileName);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                            gvGallery.setLayoutManager(layoutManager);
                            galleryAdapter = new GalleryAdapter(getApplicationContext(), imagelist);
                            gvGallery.setAdapter(galleryAdapter);
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            ModelDynamicView mm = array_view.get(pos_upload_file);
                            mm.setValue(ImageFileName);

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else if (requestCode == CAMERA) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
                String picturePath = getRealPathFromURI(tempUri);
                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                mArrayUri.add(Uri.parse(picturePath));
                String ImageFileName = getImageFilePath(this, tempUri);

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
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.v("cameraerror", e.toString());
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
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



}
