package saneforce.sanclm.activities;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.CompNameProduct;
import saneforce.sanclm.activities.Model.ModelBrandAuditList;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.activities.Model.SlideDetail;
import saneforce.sanclm.adapter_class.AdapterBrandAuditComp;
import saneforce.sanclm.adapter_class.AdapterBrandAuditList;
import saneforce.sanclm.adapter_class.AdapterPopupSpinnerSelection;
import saneforce.sanclm.adapter_class.FeedCallJoinAdapter;
import saneforce.sanclm.adapter_class.PopupAdapter;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.Utility;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.DataInterface;
import saneforce.sanclm.util.UpdateUi;

public class BrandAuditActivity extends AppCompatActivity implements DataInterface {

    ListView list_comp, listView_feed_call, listview_audit_list1;
    RecyclerView listview_audit_list;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<CompNameProduct> listComp = new ArrayList<>();
    ArrayList<CompNameProduct> full_list_prd = new ArrayList<>();
    ArrayList<PopFeed> chem_select_list = new ArrayList<>();
    ArrayList<SlideDetail> prd_list = new ArrayList<>();
    ArrayList<String> prd_list_rate = new ArrayList<>();
    ArrayList<PopFeed> chem_list = new ArrayList<>();
    ArrayList<ModelBrandAuditList> brandList = new ArrayList<>();
    ArrayList<ModelBrandAuditList> choosenBrandList = new ArrayList<>();

    AdapterBrandAuditComp adapterBrandAuditComp;
    Button chem_plus;
    ImageView iv_dwnldmaster_back;
    DataBaseHandler dbh;
    Cursor mCursor;
    FeedCallJoinAdapter feedCallJoinAdapter;
    TextView edt_search_brd;
    RelativeLayout rl_search_brd;
    public static float prd_rate = 0;
    EditText edt_qty;
    TextView edt_rate, edt_val, dr_name;
    Button btn_add_brd, btn_add_comp, save_btn;
    CommonSharedPreference mCommonSharedPreference;
    String prdEnterCode = null;
    String finalProduct = "ee";
    String ourProduct="kk";
    String competitorName,competitorBrand,competitorQnty;
    FeedbackActivity feedbackActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_audit);
        dbh = new DataBaseHandler(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mCommonSharedPreference = new CommonSharedPreference(this);
        feedbackActivity= new FeedbackActivity();
        list_comp = (ListView) findViewById(R.id.list_comp);
        listView_feed_call = (ListView) findViewById(R.id.listView_feed_call);
        //listview_audit_list = (ListView) findViewById(R.id.listview_audit_list);
        listview_audit_list = (RecyclerView) findViewById(R.id.listview_audit_list);
        listview_audit_list.setHasFixedSize(true);
        listview_audit_list.setLayoutManager(new LinearLayoutManager(BrandAuditActivity.this));
        chem_plus = (Button) findViewById(R.id.chem_plus);
        edt_search_brd = (TextView) findViewById(R.id.edt_search_brd);
        rl_search_brd = (RelativeLayout) findViewById(R.id.rl_search_brd);
        edt_qty = (EditText) findViewById(R.id.edt_qty);
        edt_rate = (TextView) findViewById(R.id.edt_rate);
        edt_val = (TextView) findViewById(R.id.edt_val);
        dr_name = (TextView) findViewById(R.id.dr_name);
        btn_add_brd = (Button) findViewById(R.id.btn_add_brd);
        btn_add_comp = (Button) findViewById(R.id.btn_add_comp);
        save_btn = (Button) findViewById(R.id.save_btn);
        iv_dwnldmaster_back = (ImageView) findViewById(R.id.iv_dwnldmaster_back);
        // chem_select_list.clear();
        //  chem_list.clear();
        AdapterBrandAuditComp.bindListenerBrand(new UpdateUi() {
            @Override
            public void updatingui() {
                commonFun();
            }
        });

        Bundle extra=getIntent().getExtras();
        if(extra!=null){
            String yy=extra.getString("json_val");
            dr_name.setText(" : "+extra.getString("name"));
            jsonExtraction(yy);

            Log.e("Doc_Name",extra.getString("name"));
            Log.v("jsonvalue","ing_inside"+yy);

        }


//        dr_name.setText(getIntent().getSerializableExtra("name").toString());
//        jsonExtraction(getIntent().getSerializableExtra("json_val").toString());
//        Log.e("Doc_Name_edt", getIntent().getSerializableExtra("name").toString());


        else{
            dr_name.setText(" : "+mCommonSharedPreference.getValueFromPreference("drName"));
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
                Intent i = new Intent(BrandAuditActivity.this, FeedbackActivity.class);

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

                if (chem_select_list.size() < 1&&(brandList.size()==0)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sclt_chmnm), Toast.LENGTH_LONG).show();

                } else if ((edt_search_brd.getText().toString().isEmpty())&&(brandList.size()==0)) {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sclt_prdnm), Toast.LENGTH_LONG).show();


                } else if ((edt_qty.getText().toString().isEmpty()&&(brandList.size()==0))) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.scltprdqty), Toast.LENGTH_LONG).show();

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
                String val = String.valueOf(editable);
                int qtyVal = 0;
                if (!TextUtils.isEmpty(val))
                    qtyVal = Integer.parseInt(val);
                if (!TextUtils.isEmpty(edt_qty.getText().toString())) {
                    edt_rate.setText(String.valueOf(prd_rate));
                    float cal = qtyVal;
                    cal = cal * prd_rate;
                    edt_val.setText(String.valueOf(cal));
                }
                else{
                    edt_rate.setText("");
                    edt_val.setText("");
                }
                callCompAdapter();
                adapterBrandAuditComp.notifyDataSetChanged();
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
        dbh.open();
        mCursor = dbh.select_comp_list();

        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("comp_name_feed", mCursor.getString(0) + " comp_prd" + mCursor.getString(1));
                listComp.add(new CompNameProduct(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3)));

            } while (mCursor.moveToNext());

        }
        mCursor.close();
        dbh.close();
        callCompAdapter();

        chem_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chem_list.clear();
                dbh.open();
                mCursor = dbh.select_chemist_list();

                if (mCursor.getCount() != 0) {
                    mCursor.moveToFirst();
                    do {
                        Log.v("plus_name_feed", mCursor.getString(0));
                        chem_list.add(new PopFeed(mCursor.getString(0), false, mCursor.getString(1)));
                    } while (mCursor.moveToNext());
                    chem_plus.setEnabled(false);
                    popUpAlertFeed(chem_list);
                }
                mCursor.close();
                dbh.close();
            }
        });


    }


    public void popUpAlertFeed(final ArrayList<PopFeed> xx) {

        final Dialog dialog = new Dialog(BrandAuditActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_feedback);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button ok = (Button) dialog.findViewById(R.id.ok);
        final ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);
        ImageView iv_close_popup = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);

        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view.setIconified(false);
                InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(search_view, 0);
            }
        });

        final PopupAdapter popupAdapter = new PopupAdapter(BrandAuditActivity.this, xx);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();
        EditText search_edit = (EditText) dialog.findViewById(R.id.et_search);

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(xx.get(position).isClick()){
                    xx.get(position).setClick(false);
                }else {
                    xx.get(position).setClick(true);

                }
                popupAdapter.notifyDataSetChanged();
            }
        });

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

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < xx.size(); i++) {

                    PopFeed popFeed = xx.get(i);
                    if (popFeed.isClick()) {
                        Log.v("clicked", "here" + popFeed.getTxt() +"  "+popFeed.getCode());
                        if(chem_select_list.size()==0) {
                            chem_select_list.add(new PopFeed(popFeed.getTxt(), false, popFeed.getCode()));
                        }
                        else {
                            int kk=0;
                            for (int nn = 0; nn < chem_select_list.size(); nn++) {
                                Log.v("tcode",chem_select_list.get(nn).getCode());
                                if ((chem_select_list.get(nn).getCode()).equalsIgnoreCase(popFeed.getCode())) {
                                    kk+=1;
                                }
                            }
                            if(kk==0){
                                chem_select_list.add(new PopFeed(popFeed.getTxt(), false, popFeed.getCode()));
                            }
                        }
                    }
                }
                feedCallJoinAdapter = new FeedCallJoinAdapter(BrandAuditActivity.this, chem_select_list, true);
                listView_feed_call.setAdapter(feedCallJoinAdapter);
                //listView_feed_call.setEmptyView(findViewById(android.R.id.empty));
                feedCallJoinAdapter.notifyDataSetChanged();
                chem_plus.setEnabled(true);
                dialog.dismiss();

                commonFun();
            }
        });

        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonFun();
                chem_plus.setEnabled(true);
                dialog.dismiss();
            }
        });

    }

    public void popUpAlert(final ArrayList<SlideDetail> list) {


        final Dialog dialog = new Dialog(BrandAuditActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_spinner_selection);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ImageView close_img = (ImageView) dialog.findViewById(R.id.close_img);
        ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);

        AdapterPopupSpinnerSelection popupAdapter = new AdapterPopupSpinnerSelection(BrandAuditActivity.this, list);
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
                edt_val.setText("");
                dialog.dismiss();
                callCompAdapter();
                commonFun();
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

    public void callCompAdapter() {
        full_list_prd.clear();
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));
        full_list_prd.add(new CompNameProduct("", "", false, "", "", "", "", "", ""));


        adapterBrandAuditComp = new AdapterBrandAuditComp(full_list_prd, BrandAuditActivity.this, listComp,this,edt_search_brd.getText().toString(),edt_qty.getText().toString());
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
        adapterBrandAuditComp = new AdapterBrandAuditComp(full_list_prd, BrandAuditActivity.this, listComp,this,edt_search_brd.getText().toString(),edt_qty.getText().toString());
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


            int as=0;
            for (int k = 0; k < choosenBrandList.size(); k++) {

                Log.v("choosenBrandList", String.valueOf(choosenBrandList.size()));
                chemObj = new JSONObject();
                ModelBrandAuditList ll = choosenBrandList.get(k);
                chemObj = new JSONObject(ll.getJsonChem());
                Log.v("chem_obj_inside", String.valueOf(chemObj));
                chemObj.put("OPCode", ll.getCompPcode());
                chemObj.put("OPName", ll.getComName());
                chemObj.put("OPQty", ll.getQty());
                chemObj.put("OPRate", ll.getRate());
                chemObj.put("OPValue", ll.getVal());
                Log.v("choosenBrandjson", String.valueOf(chemObj));


                JSONObject jobj = new JSONObject(String.valueOf(chemObj));
                JSONArray jchem = jobj.getJSONArray("Chemists");
                String chemist_nm="";
                for (int kl = 0; kl < jchem.length(); kl++) {
                    JSONObject jsonchem = jchem.getJSONObject(kl);
                    chemist_nm =chemist_nm+jsonchem.getString("Name")+",";

                    // chem_select_list.add(new PopFeed(jsonchem.getString("Name"), false, jsonchem.getString("Code")));
                }
                for (int i = 0; i < brandList.size(); i++) {

                    ModelBrandAuditList mm = brandList.get(i);
                    Log.d("chem_name",chemist_nm+"~"+ll.getComName()+"--"+mm.getChem()+"~"+mm.getPrName());
                    if ((chemist_nm+"~"+ll.getComName()).equals(mm.getChem()+"~"+mm.getPrName())) {
                        jsonObject = new JSONObject();
                        jsonObject.put("CPQty", mm.getQty());
                        jsonObject.put("CPRate", mm.getRate());
                        jsonObject.put("CPValue", mm.getVal());
                        jsonObject.put("CompCode", mm.getCompCode());
                        jsonObject.put("CompName", mm.getComName());
                        jsonObject.put("CompPCode", mm.getCompPcode());
                        jsonObject.put("CompPName", mm.getComPrdName());
                        jsonObject.put("Chemname", mm.getChem());
                        jsonObject.put("Chemcode", mm.getChemcode());
                        Log.v("choosenBrandprd", String.valueOf(jsonObject));
                    } else {
                        continue;
                    }
                    if(feedbackActivity.prd_rpt_chk.size()==0) {
                        MainchemArr.put(jsonObject);
                        feedbackActivity.prd_rpt_chk.add(chemist_nm + "~" + mm.getPrName() + "~" + mm.getComName() + "~" + mm.getComPrdName());
                    }
                    else if(feedbackActivity.prd_rpt_chk.size()>0){
                        int bnn=0;
                        for(int vv=0;vv<feedbackActivity.prd_rpt_chk.size();vv++){
                            String[] checkdata=feedbackActivity.prd_rpt_chk.get(vv).split("~");

                            String [] asn = (checkdata[0].replaceAll(",$", "")).split(",");
                            for(int sx=0; sx< asn.length;sx++) {

                                String [] asn1 = (chemist_nm.replaceAll(",$", "")).split(",");
                                Log.v("savechem",chemist_nm);
                                for(int va=0;va< asn1.length;va++) {
                                    Log.v("innerloop",asn[sx] + "," + "~" + checkdata[1] + "~" + checkdata[2] + "~" + checkdata[3]+"-------"+asn1[va] + "," + "~" + mm.getPrName() + "~" + mm.getComName() + "~" + mm.getComPrdName());
                                    if ((asn[sx] + "," + "~" + checkdata[1] + "~" + checkdata[2] + "~" + checkdata[3]).equals(asn1[va] + "," + "~" + mm.getPrName() + "~" + mm.getComName() + "~" + mm.getComPrdName())) {
                                        bnn = bnn + 1;
                                    }
                                }
                            }
                        }
                        if(bnn==0){
                            MainchemArr.put(jsonObject);
                            feedbackActivity.prd_rpt_chk.add(chemist_nm + "~" + mm.getPrName() + "~" + mm.getComName() + "~" + mm.getComPrdName());
                        }

                    }
                }
                if(MainchemArr.length()!=0) {
                    chemObj.put("Competitors", MainchemArr);

                    chemArr.put(chemObj);
                }
//                else{
//                    as=as+1;
//                }

                MainchemArr = new JSONArray();
            }
            Log.v("Printing_comp_prd", String.valueOf(chemArr));

            Log.v("compsize", brandList.size()+"----"+feedbackActivity.prd_rpt_chk.size());

            mCommonSharedPreference.setValueToPreference("jsonarray", String.valueOf(chemArr));
            if(brandList.size()==feedbackActivity.prd_rpt_chk.size()) {
                Intent i = new Intent(BrandAuditActivity.this, FeedbackActivity.class);
                setResult(6, i);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(),"Same Competitor product choosed, It won't reflect",Toast.LENGTH_LONG).show();
//                Intent i = new Intent(BrandAuditActivity.this, FeedbackActivity.class);
//                setResult(6, i);
//                finish();
            }
        } catch (Exception e) {
        }
    }

    public void jsonExtraction(String jsonVal) {

        try {
            JSONArray jsonArray = new JSONArray(jsonVal);
            Log.v("json_array_turn", String.valueOf(jsonArray.length())+" json "+jsonVal);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jobj = jsonArray.getJSONObject(i);
                JSONArray jarr = jobj.getJSONArray("Competitors");
                Log.v("arrcheck",jarr.toString());

                for (int j = 0; j < jarr.length(); j++) {

                    JSONObject jobj1 = jarr.getJSONObject(j);

//                    if (finalProduct.contains(jobj1.getString("CompPName"))) {
//                    } else
                    Log.v("check_me",jobj.getString("OPName")+"---"+jobj1.getString("Chemname")+"----"+ jobj1.getString("Chemcode"));
                    brandList.add(new ModelBrandAuditList(jobj.getString("OPName"), jobj1.getString("CompName"), jobj1.getString("CompPName"), jobj1.getString("CPQty"), jobj1.getString("CPRate"), jobj1.getString("CPValue"), jobj1.getString("CompCode"), jobj1.getString("CompPCode"), jobj1.getString("Chemname"), jobj1.getString("Chemcode")));
                    // AdapterBrandAuditComp.full_list_prd.add(new CompNameProduct(jobj1.getString("CompName"), jobj1.getString("CompPName"), false, jobj1.getString("CPQty"), jobj1.getString("CPRate"), jobj1.getString("CPValue"), jobj.getString("OPName"), jobj1.getString("CompCode"), jobj1.getString("CompPCode")));
                    //  finalProduct += jobj1.getString("CompPName");
                    //ourProduct+=jobj1.getString("OPName");

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
                choosenBrandList.add(new ModelBrandAuditList("", jobj.getString("OPName"), jobj.getString("OPQty"), jobj.getString("OPRate"), jobj.getString("OPValue"), "1", jsonnn.toString()));

            }

            AdapterBrandAuditList adp = new AdapterBrandAuditList(BrandAuditActivity.this, brandList);
            listview_audit_list.setAdapter(adp);
            adp.notifyDataSetChanged();
            chem_select_list.clear();
//            feedCallJoinAdapter = new FeedCallJoinAdapter(BrandAuditActivity.this, chem_select_list, true);
//            listView_feed_call.setAdapter(feedCallJoinAdapter);
//            feedCallJoinAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void addBrandValue() {

        Log.v("brandsize",String.valueOf(brandList.size()));


        Log.v("last_select_array", String.valueOf(chem_select_list.size()));
        JSONArray arr = new JSONArray();
        JSONObject chemlist = new JSONObject();
        String chemname="",chemcode="";
        for (int k = 0; k < chem_select_list.size(); k++) {
            try {
                JSONObject json = new JSONObject();
                json.put("Name", chem_select_list.get(k).getTxt());
                json.put("Code", chem_select_list.get(k).getCode());
                arr.put(json);
                chemname= chemname+chem_select_list.get(k).getTxt()+",";
                chemcode= chemcode+chem_select_list.get(k).getCode()+",";
            } catch (Exception e) {

            }

        }
        try {
            chemlist.put("Chemists", arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("Adapter_brandAudit", String.valueOf(AdapterBrandAuditComp.full_list_prd.size()));
        int bn=0;
        int cvv=0;
        for (int i = 0; i < AdapterBrandAuditComp.full_list_prd.size(); i++) {
            CompNameProduct mm = AdapterBrandAuditComp.full_list_prd.get(i);

            if (!TextUtils.isEmpty(mm.getCompName()) && !TextUtils.isEmpty(mm.getChoosenPrdName()) && !TextUtils.isEmpty(mm.getQty())) {
                Log.v("CompanyName", mm.getCompName() + " edit_val " + edt_search_brd.getText().toString() + " chosenprd " + mm.getChoosenPrdName() + " compcod " + mm.getCompCode() + " pcode " + mm.getCompPCode()+"chem"+chemname+"chemcode"+chemcode);

                if(brandList.size()==0) {

                    brandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), mm.getCompName(), mm.getChoosenPrdName(), mm.getQty(), mm.getRate(), mm.getValue(), mm.getCompCode(), mm.getCompPCode(),chemname,chemcode));
                }
                else if(brandList.size()>0){
                    int bnns=0;
                    for(int vv1=0;vv1<brandList.size();vv1++){


                        String [] asnn = (brandList.get(vv1).getChem().replaceAll(",$", "")).split(",");
                        for(int sxx=0; sxx< asnn.length;sxx++) {

                            String [] asn1 = (chemname.replaceAll(",$", "")).split(",");
                            Log.v("savechem",chemname);
                            for(int va=0;va< asn1.length;va++) {
                                Log.v("innerloop",asnn[sxx] + "," + "~" + brandList.get(vv1).getPrName() + "~" + brandList.get(vv1).getComName() + "~" + brandList.get(vv1).getComPrdName()+"-------"+asn1[va] + "," + "~" + edt_search_brd.getText().toString() + "~" + mm.getCompName() + "~" + mm.getChoosenPrdName());
                                if ((asnn[sxx] + "," + "~" + brandList.get(vv1).getPrName() + "~" + brandList.get(vv1).getComName() + "~" + brandList.get(vv1).getComPrdName()).equals(asn1[va] + "," + "~" + edt_search_brd.getText().toString() + "~" + mm.getCompName() + "~" + mm.getChoosenPrdName())) {
                                    bnns = bnns + 1;
                                }
                            }
                        }
                    }
                    if(bnns==0){
                        brandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), mm.getCompName(), mm.getChoosenPrdName(), mm.getQty(), mm.getRate(), mm.getValue(), mm.getCompCode(), mm.getCompPCode(),chemname,chemcode));
                    }
                    else{
                        cvv=cvv+1;
                    }

                }

                // brandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), mm.getCompName(), mm.getChoosenPrdName(), mm.getQty(), mm.getRate(), mm.getValue(), mm.getCompCode(), mm.getCompPCode(),chemname,chemcode));
            }
            else if(TextUtils.isEmpty(mm.getCompName()) && TextUtils.isEmpty(mm.getChoosenPrdName()) && TextUtils.isEmpty(mm.getQty())){
                bn=bn+1;
            }

        }

        choosenBrandList.add(new ModelBrandAuditList("", edt_search_brd.getText().toString(), edt_qty.getText().toString(), edt_rate.getText().toString(), edt_val.getText().toString(), prdEnterCode, chemlist.toString()));

        Log.v("brandsize",String.valueOf(choosenBrandList.size()));

        if(brandList.size()<1&&(bn==AdapterBrandAuditComp.full_list_prd.size()))
        {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.sclt_compdetl),Toast.LENGTH_LONG).show();

        }  else {
//            if (bn==AdapterBrandAuditComp.full_list_prd.size()) {
//                Toast.makeText(getApplicationContext(),getResources().getString(R.string.sclt_compdetl),Toast.LENGTH_LONG).show();
//
//            }else {
            if(cvv==0) {

                jsonSave();


                edt_search_brd.setText("");
                edt_qty.setText("");
                edt_rate.setText("");
                edt_val.setText("");
                callCompAdapter();
            }else{
                Toast.makeText(getApplicationContext(),"Same Competitor product choosed, It won't reflect",Toast.LENGTH_LONG).show();
                edt_search_brd.setText("");
                edt_qty.setText("");
                edt_rate.setText("");
                edt_val.setText("");
                callCompAdapter();
            }


            AdapterBrandAuditList adp = new AdapterBrandAuditList(BrandAuditActivity.this, brandList);
            listview_audit_list.setAdapter(adp);
            adp.notifyDataSetChanged();
            //  }
        }


    }

    public void addBrandValues() {
//        Log.v("brandsize",String.valueOf(brandList.size()));
//
//        Log.v("last_select_array", String.valueOf(chem_select_list.size()));
//        String chemname="",chemcode="";
//        JSONArray arr = new JSONArray();
//        JSONObject chemlist = new JSONObject();
//        for (int k = 0; k < chem_select_list.size(); k++) {
//            try {
//                JSONObject json = new JSONObject();
//                json.put("Name", chem_select_list.get(k).getTxt());
//                json.put("Code", chem_select_list.get(k).getCode());
//                arr.put(json);
//                chemname= chemname+chem_select_list.get(k).getTxt()+",";
//                chemcode= chemcode+chem_select_list.get(k).getCode()+",";
//            } catch (Exception e) {
//            }
//
//        }
//        try {
//            chemlist.put("Chemists", arr);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.v("Adapter_brandAudit", String.valueOf(AdapterBrandAuditComp.full_list_prd.size()));
////        brandList.clear();
////        choosenBrandList.clear();
//        for (int i = 0; i < AdapterBrandAuditComp.full_list_prd.size(); i++) {
//            CompNameProduct mm = AdapterBrandAuditComp.full_list_prd.get(i);
//
//            if (!TextUtils.isEmpty(mm.getCompName()) && !TextUtils.isEmpty(mm.getChoosenPrdName()) && !TextUtils.isEmpty(mm.getQty())) {
//                Log.v("CompanyName", mm.getCompName() + " edit_val " + edt_search_brd.getText().toString() + " chosenprd " + mm.getChoosenPrdName() + " compcod " + mm.getCompCode() + " pcode " + mm.getCompPCode());
//                brandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), mm.getCompName(), mm.getChoosenPrdName(), mm.getQty(), mm.getRate(), mm.getValue(), mm.getCompCode(), mm.getCompPCode(),chemname,chemcode));
//            }
//        }
//
//        choosenBrandList.add(new ModelBrandAuditList("", edt_search_brd.getText().toString(), edt_qty.getText().toString(), edt_rate.getText().toString(), edt_val.getText().toString(), prdEnterCode, chemlist.toString()));
//
//        Log.v("brandsize",String.valueOf(choosenBrandList.size()));
//
//        edt_search_brd.setText("");
//        edt_qty.setText("");
//        edt_rate.setText("");
//        edt_val.setText("");
//        callCompAdapter();
//
//        AdapterBrandAuditList adp = new AdapterBrandAuditList(BrandAuditActivity.this, brandList);
//        listview_audit_list.setAdapter(adp);
//        adp.notifyDataSetChanged();

        Log.v("brandsize",String.valueOf(brandList.size()));


        Log.v("last_select_array", String.valueOf(chem_select_list.size()));
        JSONArray arr = new JSONArray();
        JSONObject chemlist = new JSONObject();
        String chemname="",chemcode="";
        for (int k = 0; k < chem_select_list.size(); k++) {
            try {
                JSONObject json = new JSONObject();
                json.put("Name", chem_select_list.get(k).getTxt());
                json.put("Code", chem_select_list.get(k).getCode());
                arr.put(json);
                chemname= chemname+chem_select_list.get(k).getTxt()+",";
                chemcode= chemcode+chem_select_list.get(k).getCode()+",";
            } catch (Exception e) {

            }

        }
        try {
            chemlist.put("Chemists", arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("Adapter_brandAudit", String.valueOf(AdapterBrandAuditComp.full_list_prd.size()));
        int bn=0;
        int cvvn=0;
        for (int i = 0; i < AdapterBrandAuditComp.full_list_prd.size(); i++) {
            CompNameProduct mm = AdapterBrandAuditComp.full_list_prd.get(i);
//
//            if (!TextUtils.isEmpty(mm.getCompName()) && !TextUtils.isEmpty(mm.getChoosenPrdName()) && !TextUtils.isEmpty(mm.getQty())) {
//                Log.v("CompanyName", mm.getCompName() + " edit_val " + edt_search_brd.getText().toString() + " chosenprd " + mm.getChoosenPrdName() + " compcod " + mm.getCompCode() + " pcode " + mm.getCompPCode()+"chem"+chemname+"chemcode"+chemcode);
//                brandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), mm.getCompName(), mm.getChoosenPrdName(), mm.getQty(), mm.getRate(), mm.getValue(), mm.getCompCode(), mm.getCompPCode(),chemname,chemcode));
//            }


            if (!TextUtils.isEmpty(mm.getCompName()) && !TextUtils.isEmpty(mm.getChoosenPrdName()) && !TextUtils.isEmpty(mm.getQty())) {
                Log.v("CompanyName", mm.getCompName() + " edit_val " + edt_search_brd.getText().toString() + " chosenprd " + mm.getChoosenPrdName() + " compcod " + mm.getCompCode() + " pcode " + mm.getCompPCode()+"chem"+chemname+"chemcode"+chemcode);

                if(brandList.size()==0) {

                    brandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), mm.getCompName(), mm.getChoosenPrdName(), mm.getQty(), mm.getRate(), mm.getValue(), mm.getCompCode(), mm.getCompPCode(),chemname,chemcode));
                }
                else if(brandList.size()>0){
                    int bnns=0;
                    for(int vv1=0;vv1<brandList.size();vv1++){


                        String [] asnn = (brandList.get(vv1).getChem().replaceAll(",$", "")).split(",");
                        for(int sxx=0; sxx< asnn.length;sxx++) {

                            String [] asn1 = (chemname.replaceAll(",$", "")).split(",");
                            Log.v("savechem",chemname);
                            for(int va=0;va< asn1.length;va++) {
                                Log.v("innerloop",asnn[sxx] + "," + "~" + brandList.get(vv1).getPrName() + "~" + brandList.get(vv1).getComName() + "~" + brandList.get(vv1).getComPrdName()+"-------"+asn1[va] + "," + "~" + edt_search_brd.getText().toString() + "~" + mm.getCompName() + "~" + mm.getChoosenPrdName());
                                if ((asnn[sxx] + "," + "~" + brandList.get(vv1).getPrName() + "~" + brandList.get(vv1).getComName() + "~" + brandList.get(vv1).getComPrdName()).equals(asn1[va] + "," + "~" + edt_search_brd.getText().toString() + "~" + mm.getCompName() + "~" + mm.getChoosenPrdName())) {
                                    bnns = bnns + 1;

                                }
                            }
                        }
                    }
                    if(bnns==0){
                        brandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), mm.getCompName(), mm.getChoosenPrdName(), mm.getQty(), mm.getRate(), mm.getValue(), mm.getCompCode(), mm.getCompPCode(),chemname,chemcode));
                    }
                    else{
                        cvvn=cvvn+1;
                    }

                }

                // brandList.add(new ModelBrandAuditList(edt_search_brd.getText().toString(), mm.getCompName(), mm.getChoosenPrdName(), mm.getQty(), mm.getRate(), mm.getValue(), mm.getCompCode(), mm.getCompPCode(),chemname,chemcode));
            }
            else if(TextUtils.isEmpty(mm.getCompName()) && TextUtils.isEmpty(mm.getChoosenPrdName()) && TextUtils.isEmpty(mm.getQty())){
                bn=bn+1;
            }

        }

        choosenBrandList.add(new ModelBrandAuditList("", edt_search_brd.getText().toString(), edt_qty.getText().toString(), edt_rate.getText().toString(), edt_val.getText().toString(), prdEnterCode, chemlist.toString()));

        Log.v("brandsize",String.valueOf(choosenBrandList.size()));
        if (bn==AdapterBrandAuditComp.full_list_prd.size()) {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.sclt_compdetl),Toast.LENGTH_LONG).show();

        }else {
            if(cvvn==0) {
                edt_search_brd.setText("");
                edt_qty.setText("");
                edt_rate.setText("");
                edt_val.setText("");
                callCompAdapter();

                AdapterBrandAuditList adp = new AdapterBrandAuditList(BrandAuditActivity.this, brandList);
                listview_audit_list.setAdapter(adp);
                adp.notifyDataSetChanged();
            }
            else{
                Toast.makeText(getApplicationContext(),"Same Competitor product choosed, It won't reflect",Toast.LENGTH_LONG).show();
                edt_search_brd.setText("");
                edt_qty.setText("");
                edt_rate.setText("");
                edt_val.setText("");
                callCompAdapter();

                AdapterBrandAuditList adp = new AdapterBrandAuditList(BrandAuditActivity.this, brandList);
                listview_audit_list.setAdapter(adp);
                adp.notifyDataSetChanged();
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

        Log.v("compName",compName);
        Log.v("prodName",prodName);
        Log.v("compQty",qty);

        competitorName=compName;
        competitorBrand=prodName;
        competitorQnty=qty;



    }
}

