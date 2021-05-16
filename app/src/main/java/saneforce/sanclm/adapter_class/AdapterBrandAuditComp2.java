package saneforce.sanclm.adapter_class;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.CompNameProductNew;
import saneforce.sanclm.activities.ImageActivity;
import saneforce.sanclm.activities.Model.Feedbacklist;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.util.DataInterface;
import saneforce.sanclm.util.UpdateUi;

import static android.app.Activity.RESULT_OK;

public class AdapterBrandAuditComp2 extends BaseAdapter {
  public static ArrayList<CompNameProductNew> list_prd=new ArrayList<>();
   Activity context;
    boolean detectPrdClick=false;
    TextView txt_comp_name;
    ArrayList<Feedbacklist> arraylist;
    CommonUtilsMethods commonUtilsMethods;
    static UpdateUi updateUi;
    DataInterface dataInterface;
    Api_Interface apiService;
    JSONObject obj = new JSONObject();
    String Divcode="",db_connPath;
    CommonSharedPreference  mCommonSharedPreference;
    TemplateAdapter adapter;
   Spinner spinner;
  ArrayList<String>myList;

    public AdapterBrandAuditComp2( Activity context, ArrayList<CompNameProductNew> list_prd) {
        this.context = context;
        this.list_prd=list_prd;
        commonUtilsMethods=new CommonUtilsMethods(this.context);
        arraylist=new ArrayList<>();
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
                        String code=js1.getString("Code");
                        String message=js1.getString("Name");

                       Feedbacklist feedbacklist=new Feedbacklist(code,message);
                       arraylist.add(feedbacklist);


                        adapter=new TemplateAdapter(context,arraylist);
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
        return list_prd.size();
    }

    @Override
    public Object getItem(int i) {
        return list_prd.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.new_row_item_brand_audit_competor,viewGroup,false);
        txt_comp_name=(TextView)view.findViewById(R.id.txt_comp_name);
        final TextView txt_comp_brd_name=(TextView)view.findViewById(R.id.txt_comp_brd_name);

        RelativeLayout comp_name_list=(RelativeLayout)view.findViewById(R.id.comp_name_list);
        RelativeLayout comp_prd_list=(RelativeLayout)view.findViewById(R.id.comp_prd_list);
        final LinearLayout ll_qty=(LinearLayout)view.findViewById(R.id.ll_qty);
        EditText etqty=view.findViewById(R.id.edt_inqty);
        EditText etptp=view.findViewById(R.id.etptp);
        EditText etptr=view.findViewById(R.id.etptr);
        EditText etsw=view.findViewById(R.id.etsw);
        EditText etrx=view.findViewById(R.id.etrx);
        etqty.setText(list_prd.get(i).getInvqty());
        etptp.setText(list_prd.get(i).getPtp());
        etptr.setText(list_prd.get(i).getPtr());
        etsw.setText(list_prd.get(i).getSw());
        etrx.setText(list_prd.get(i).getRx());

        etqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list_prd.get(i).setInvqty(etqty.getText().toString());

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
                list_prd.get(i).setPtp(etptp.getText().toString());

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
                list_prd.get(i).setPtr(etptr.getText().toString());

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
                list_prd.get(i).setSw(etsw.getText().toString());

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
                list_prd.get(i).setRx(etrx.getText().toString());

            }
        });

        Button feedbackbtn = view.findViewById(R.id.feedbackbtn);



        feedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.alert_feedback_layout);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Button savebtn = dialog.findViewById(R.id.savebtn);
                TextView feedbacktext=dialog.findViewById(R.id.templatetext);
               spinner=dialog.findViewById(R.id.template);
               ImageView camerabtn=dialog.findViewById(R.id.camerabtn);
               camerabtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                     Intent intent=new Intent(context, ImageActivity.class);
                     context.startActivity(intent);
                   }
               });
                feedbacktext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewcopList();
                        feedbacktext.setVisibility(View.GONE);
                        spinner.setVisibility(View.VISIBLE);



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

        final CompNameProductNew mm=list_prd.get(i);

            txt_comp_name.setVisibility(View.VISIBLE);
            txt_comp_name.setText(mm.getCName());



            txt_comp_brd_name.setVisibility(View.VISIBLE);
            txt_comp_brd_name.setText(mm.getPName());



//        if(!TextUtils.isEmpty(mm.getCompName()))
//        {
//            if(!TextUtils.isEmpty(mm.getChoosenPrdName()))
//            {
//                if(!TextUtils.isEmpty(mm.getQty()))
//                {
//                    dataInterface.competitordetails(mm.getCompName(),mm.getChoosenPrdName(), mm.getQty());
//                }
//            }
//        }

//       dataInterface.competitordetails(mm.getCompName(),mm.getChoosenPrdName(), mm.getQty());
//        comp_name_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!TextUtils.isEmpty(mm.getCompName())){
//                    mm.setChoosenPrdName("");
//                }
//                detectPrdClick=false;
//                Log.v("arrayss_as_list","clicked_here");
//                popUpAlert(list_prd,"n",i);
//                notifyDataSetChanged();
//            }
//        });
//        comp_prd_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                detectPrdClick=true;
//                popUpAlert(list_prd,"y",i);
//            }
//        });



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

                            float cal= Float.parseFloat(edt_qty.getText().toString());
                            cal=cal*BrandAuditActivity.prd_rate;
                            mm.setQty(edt_qty.getText().toString());
                            mm.setRate(String.valueOf(BrandAuditActivity.prd_rate));
                            mm.setValue(String.valueOf(cal));
                            notifyDataSetChanged();

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




        return view;
    }




    public String composeJSON() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < list_prd.size(); i++) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Ccode", list_prd.get(i).getCcode());
            map.put("CName", list_prd.get(i).getCName());
            map.put("Pcode", list_prd.get(i).getPCode());
            map.put("PName", list_prd.get(i).getPName());
            map.put("qty",list_prd.get(i).getInvqty());
            map.put("ptp",list_prd.get(i).getPtp());
            map.put("ptr",list_prd.get(i).getPtr());
            map.put("sw",list_prd.get(i).getSw());
            map.put("rx",list_prd.get(i).getRx());


            wordList.add(map);

        }

        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);

    }


}
