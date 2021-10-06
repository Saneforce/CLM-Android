package saneforce.sanclm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Pojo_Class.DCRapplist;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelForLeaveApproval;
import saneforce.sanclm.adapter_class.AdapterForDCRlistview;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;

public class DCRViewActivity extends AppCompatActivity {
    String Aslno,Adate,Sfname,worktype,sfcode;
    TextView text_sfname,txt_date,txt_worktype;
    CommonSharedPreference mCommonSharedPreference;
    String db_connPath;
    Api_Interface apiService;
    String SF_Code,division_code;
    ImageView backbtn;
    ArrayList<DCRapplist>dcRapplists=new ArrayList<>();
    RecyclerView recyclerView;
    Button approve_btn,reject_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcrview);



        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        mCommonSharedPreference=new CommonSharedPreference(this);
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        division_code = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        FullScreencall();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           Aslno = extras.getString("transno");
            Adate= extras.getString("activitydate");
            Sfname = extras.getString("sfname");
            worktype= extras.getString("worktype");
            sfcode=extras.getString("sfcode");
            Log.v("trans>>",Aslno);
        }

        text_sfname=findViewById(R.id.sfname_txt);
        txt_date=findViewById(R.id.date_txt);
        txt_worktype=findViewById(R.id.worktype_txt);
        backbtn=findViewById(R.id.iv_dwnldmaster_back);
        approve_btn=findViewById(R.id.btn_approve);
        reject_btn=findViewById(R.id.btn_reject);
        recyclerView=findViewById(R.id.dcrlistRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DCRViewActivity.this));

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DCRViewActivity.this,ApprovalActivity.class);
                startActivity(intent);
            }
        });
        text_sfname.setText(Sfname);
        txt_worktype.setText(worktype);
        txt_date.setText(Adate);

        getDCRlist(Aslno);

        approve_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             sendApproval(sfcode);

            }
        });

        reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertForReject(0);
            }
        });

    }

    private void alertForReject(int i) {
        final Dialog dialog=new Dialog(DCRViewActivity.this,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.row_item_reject_reason);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
        TextView txt_title=(TextView)dialog.findViewById(R.id.title);
        Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);
        Button btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel);
        RelativeLayout lay_edt=(RelativeLayout)dialog.findViewById(R.id.lay_edt);
        txt_title.setText("Are you sure, Do You want to reject the DCR ?");

        final EditText edt_feed=(EditText)dialog.findViewById(R.id.edt_feed);
        lay_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoftKeyboard(edt_feed);
            }
        });
       /* if(x==1) {
            txt_title.setText("Rejection Reason");
            btn_ok.setVisibility(View.GONE);
            btn_cancel.setText("Ok");
            edt_feed.setEnabled(false);
            edt_feed.setText(reject);
            alertshow=true;
            commonFun();
        }*/

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(edt_feed.getText().toString())){
                    rejectApproval(edt_feed.getText().toString());
                }else{
                    Toast.makeText(DCRViewActivity.this, "Enter reason for reject", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    private void rejectApproval(String toString) {
        String date= CommonUtilsMethods.getCurrentDate();
        try{
            JSONObject jj=new JSONObject();
            jj.put("sfCode",sfcode);
            jj.put("date",Adate);
            jj.put("reason",toString);
            jj.put("div",division_code);
            Log.v("printing_reject",jj.toString());
            Call<ResponseBody> reject=apiService.sendDCRreject(jj.toString());
            reject.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.v("printing_reject11",response.isSuccessful()+"");
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
                            Log.v("day_reject", is.toString());
                            JSONObject js = new JSONObject(is.toString());
                            if(js.getString("success").equalsIgnoreCase("true")) {
                                Toast.makeText(DCRViewActivity.this, "DCR Rejected Successfully!", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(DCRViewActivity.this,ApprovalActivity.class);
                                startActivity(i);
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }catch (Exception e){}
    }

    private void sendApproval(String sfcode) {
        JSONObject json=new JSONObject();
        String date= CommonUtilsMethods.getCurrentDate();

        try{
            json.put("sfCode",sfcode);
            json.put("date",Adate);
            Log.v("printing_sf_code",json.toString());
            Call<ResponseBody> dcrapprove=apiService.sendDCRapproval(json.toString());
            dcrapprove.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track", response.body().byteStream() + "");

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);
                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            JSONObject jsonObject=new JSONObject(is.toString());
                            String result=jsonObject.getString("success");
                            if(result.equalsIgnoreCase("true")){
                                Toast.makeText(DCRViewActivity.this, "DCR Approved Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(DCRViewActivity.this,ApprovalActivity.class);
                                startActivity(intent);
                            }

                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch(Exception e){

        }

    }


    private void getDCRlist(String aslno) {
        JSONObject json=new JSONObject();
        try{
            json.put("Trans_SlNo",aslno);
            Log.v("printing_sf_code",json.toString());
            Call<ResponseBody> dcrlist=apiService.getDCRdetailedList(json.toString());
            dcrlist.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track", response.body().byteStream() + "");
                        dcRapplists.clear();

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("printing_here_tpapprove",is.toString());
                            JSONArray jsonArray=new JSONArray(is.toString());
                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject json=jsonArray.getJSONObject(i);
                                dcRapplists.add(new DCRapplist(json.getString("Type"),json.getString("Trans_Detail_Info_Type"),json.getString("Trans_Detail_Name"),
                                        json.getString("SDP_Name"),json.getString("products"),json.getString("gifts"),""));

                            }
                            AdapterForDCRlistview adapterForDCRlistview=new AdapterForDCRlistview(DCRViewActivity.this,dcRapplists);
                            recyclerView.setAdapter(adapterForDCRlistview);
                            adapterForDCRlistview.notifyDataSetChanged();



                        } catch (Exception e) {
                        }

                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }
        catch (Exception e){
        }

    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    public void showSoftKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view ,
                InputMethodManager.SHOW_IMPLICIT);

    }

}