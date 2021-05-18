package saneforce.sanclm.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.Availcheck;
import saneforce.sanclm.adapter_class.AvailcheckAdapter;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class AvailablityCheckActivity extends AppCompatActivity {
     EditText etSearchview;
     ToggleButton alloosText,allavailText;
     RecyclerView availabilityRecyclerview;
     ArrayList<Availcheck>availchecks;
     Button buttonsave;
     DataBaseHandler db;
     Cursor mCursor;
     ImageView backbtn;
     CommonSharedPreference mCommonSharedPreference;
     AvailcheckAdapter adapter;
     String availjson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availablity_check);

        mCommonSharedPreference = new CommonSharedPreference(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        availchecks=new ArrayList<>();
        availchecks.clear();
        etSearchview=findViewById(R.id.et_search);
        alloosText=findViewById(R.id.alloos);
        allavailText=findViewById(R.id.allavail);
        availabilityRecyclerview=findViewById(R.id.availabilty_recyclerview);
        buttonsave=findViewById(R.id.savebtn);
        backbtn=findViewById(R.id.iv_dwnldmaster_back);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String yy = extra.getString("availjson");

            jsonExtraction(yy);
            Log.v("availjson",yy);


        }

        db=new DataBaseHandler(this);

        db.open();
        mCursor = db.select_product_content_master();

        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("product_name_feed", mCursor.getString(0));

//                availchecks.add(new Availcheck(mCursor.getString(0), mCursor.getString(2),false,false));
                Availcheck availcheck=new Availcheck();
                availcheck.setName(mCursor.getString(0));
                availcheck.setCode(mCursor.getString(2));
                availcheck.setIsoos(false);
                availcheck.setAvailis(false);
                availcheck.setQuantity("0");

                availchecks.add(availcheck);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        db.close();




        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonObject=new JSONObject();
                JSONArray jsonArray=new JSONArray();
                JSONObject jsonObject1=new JSONObject();

                try {
                    for(int i=0;i<availchecks.size();i++) {
                        Availcheck availcheck=availchecks.get(i);
                        jsonObject.put("code",availcheck.getCode());
                        jsonObject.put("name", availcheck.getName());
                        jsonObject.put("oos", availcheck.isIsoos());
                        jsonObject.put("avail",availcheck.isAvailis());
                        jsonObject.put("quantity",availcheck.getQuantity());

                        jsonArray.put(jsonObject);
                    }
                    jsonObject1.put("availability",jsonArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mCommonSharedPreference.setValueToPreference("availjson", String.valueOf(jsonObject1));
                Log.v("avail>>>",String.valueOf(jsonArray));
//                Toast.makeText(AvailablityCheckActivity.this, ""+availjson, Toast.LENGTH_SHORT).show();

                Intent i=new Intent(AvailablityCheckActivity.this, FeedbackActivity.class);

                setResult(6, i);
                finish();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AvailablityCheckActivity.this, FeedbackActivity.class);

                setResult(6, i);
                finish();
            }
        });


         adapter=new AvailcheckAdapter(this,availchecks,false,false);
        LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        availabilityRecyclerview.setLayoutManager(manager);
        availabilityRecyclerview.setAdapter(adapter);
        availabilityRecyclerview.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

        allavailText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){

                    availchecks.clear();
                    db.open();
                    mCursor = db.select_product_content_master();

                    if (mCursor.getCount() != 0) {
                        mCursor.moveToFirst();
                        do {
                            Log.v("product_name_feed", mCursor.getString(0));

//                            availchecks.add(new Availcheck(mCursor.getString(0), mCursor.getString(2),false,true));
                            Availcheck availcheck=new Availcheck();
                            availcheck.setName(mCursor.getString(0));
                            availcheck.setCode(mCursor.getString(2));
                            availcheck.setIsoos(false);
                            availcheck.setAvailis(true);
                            availcheck.setQuantity("0");

                            availchecks.add(availcheck);
                        } while (mCursor.moveToNext());
                    }
                    mCursor.close();
                    db.close();

                    AvailcheckAdapter adapter=new AvailcheckAdapter(AvailablityCheckActivity.this,availchecks,true,false);
                    LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                    availabilityRecyclerview.setLayoutManager(manager);
                    availabilityRecyclerview.setAdapter(adapter);
                    availabilityRecyclerview.setHasFixedSize(true);
                    adapter.notifyDataSetChanged();
                }else {
                    availchecks.clear();
                    db.open();
                    mCursor = db.select_product_content_master();

                    if (mCursor.getCount() != 0) {
                        mCursor.moveToFirst();
                        do {
                            Log.v("product_name_feed", mCursor.getString(0));


//                            availchecks.add(new Availcheck(mCursor.getString(0), mCursor.getString(2),false,false));
                            Availcheck availcheck=new Availcheck();
                            availcheck.setName(mCursor.getString(0));
                            availcheck.setCode(mCursor.getString(2));
                            availcheck.setIsoos(false);
                            availcheck.setAvailis(false);
                            availcheck.setQuantity("0");

                            availchecks.add(availcheck);

                        } while (mCursor.moveToNext());
                    }
                    mCursor.close();
                    db.close();

                    AvailcheckAdapter adapter=new AvailcheckAdapter(AvailablityCheckActivity.this,availchecks,false,false);
                    LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                    availabilityRecyclerview.setLayoutManager(manager);
                    availabilityRecyclerview.setAdapter(adapter);
                    availabilityRecyclerview.setHasFixedSize(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        alloosText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    availchecks.clear();
                    db.open();
                    mCursor = db.select_product_content_master();

                    if (mCursor.getCount() != 0) {
                        mCursor.moveToFirst();
                        do {
                            Log.v("product_name_feed", mCursor.getString(0));

//                            availchecks.add(new Availcheck(mCursor.getString(0), mCursor.getString(2),true,false));
                            Availcheck availcheck=new Availcheck();
                            availcheck.setName(mCursor.getString(0));
                            availcheck.setCode(mCursor.getString(2));
                            availcheck.setIsoos(true);
                            availcheck.setAvailis(false);
                            availcheck.setQuantity("0");

                            availchecks.add(availcheck);
                        } while (mCursor.moveToNext());
                    }
                    mCursor.close();
                    db.close();


                    AvailcheckAdapter adapter=new AvailcheckAdapter(AvailablityCheckActivity.this,availchecks,false,true);
                    LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                    availabilityRecyclerview.setLayoutManager(manager);
                    availabilityRecyclerview.setAdapter(adapter);
                    availabilityRecyclerview.setHasFixedSize(true);
                    adapter.notifyDataSetChanged();
                }else {
                    availchecks.clear();
                    db.open();
                    mCursor = db.select_product_content_master();

                    if (mCursor.getCount() != 0) {
                        mCursor.moveToFirst();
                        do {
                            Log.v("product_name_feed", mCursor.getString(0));

//                            availchecks.add(new Availcheck(mCursor.getString(0), mCursor.getString(2),false,false));
                            Availcheck availcheck=new Availcheck();
                            availcheck.setName(mCursor.getString(0));
                            availcheck.setCode(mCursor.getString(2));
                            availcheck.setIsoos(false);
                            availcheck.setAvailis(false);
                            availcheck.setQuantity("0");

                            availchecks.add(availcheck);
                        } while (mCursor.moveToNext());
                    }
                    mCursor.close();
                    db.close();

                    AvailcheckAdapter adapter=new AvailcheckAdapter(AvailablityCheckActivity.this,availchecks,false,false);
                    LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                    availabilityRecyclerview.setLayoutManager(manager);
                    availabilityRecyclerview.setAdapter(adapter);
                    availabilityRecyclerview.setHasFixedSize(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        etSearchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               adapter.getFilter().filter(s);
               adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void jsonExtraction(String yy) {
        String quantity,name,oos = null,avail = null,code;
        try {
            JSONArray jsonArray = new JSONArray(yy);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                JSONArray jsonArray1=jsonObject1.getJSONArray("availability");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject=jsonArray.getJSONObject(j);

                    quantity = jsonObject.getString("quantity");
                    name = jsonObject.getString("name");
                    oos = jsonObject.getString("oos");
                    avail = jsonObject.getString("avail");
                    code = jsonObject.getString("code");
                    Availcheck availcheck = new Availcheck();
                    availcheck.setName(name);
                    availcheck.setCode(code);
                    availcheck.setIsoos(Boolean.parseBoolean(oos));
                    availcheck.setAvailis(Boolean.parseBoolean(avail));
                    availcheck.setQuantity(quantity);

                    availchecks.add(availcheck);
                }
            }
            AvailcheckAdapter adapter=new AvailcheckAdapter(AvailablityCheckActivity.this,availchecks,Boolean.parseBoolean(avail),Boolean.parseBoolean(oos));
            LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
            availabilityRecyclerview.setLayoutManager(manager);
            availabilityRecyclerview.setAdapter(adapter);
            availabilityRecyclerview.setHasFixedSize(true);
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(AvailablityCheckActivity.this, FeedbackActivity.class);
        if (mCommonSharedPreference.getValueFromPreference("detail_").equalsIgnoreCase("chm"))
            i.putExtra("feedpage", "chemist");
        else if (mCommonSharedPreference.getValueFromPreference("detail_").equalsIgnoreCase("stk")) {
            i.putExtra("feedpage", "stock");
        } else if (mCommonSharedPreference.getValueFromPreference("detail_").equalsIgnoreCase("undr")) {
            i.putExtra("feedpage", "undr");
        } else
            i.putExtra("feedpage", "dr");
        startActivity(i);
        finish();

    }

}