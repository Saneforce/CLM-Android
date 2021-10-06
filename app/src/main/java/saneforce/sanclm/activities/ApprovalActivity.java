package saneforce.sanclm.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import saneforce.sanclm.activities.Model.ModelTpApproval;
import saneforce.sanclm.adapter_class.AdapterForDCRApproval;
import saneforce.sanclm.adapter_class.AdapterForLeaveApproval;
import saneforce.sanclm.adapter_class.AdapterTpApproval;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.DownloadMasters;

public class ApprovalActivity extends AppCompatActivity {
    ListView list_view; //test
    CommonSharedPreference mCommonSharedPreference;
    String db_connPath;
    Api_Interface apiService;
    String SF_Code;
    ArrayList<ModelForLeaveApproval> leaveArray=new ArrayList<>();
    ArrayList<ModelTpApproval> tpArray=new ArrayList<>();
    //ArrayList<ModelDcrApproval> tdcrArray=new ArrayList<>();
    RelativeLayout lay_leave,lay_tp,lay_dcr,lay_dcrappr;
    ImageView iv_dwnldmaster_back;
    ArrayList<String> aa=new ArrayList<>();
    LinearLayout lay_tp_header;
    DownloadMasters dwnloadMasterData;
    String db_slidedwnloadPath;
    LinearLayout ll_anim;
    CommonUtilsMethods mCommonUtilsMethods;
    TextView monthtxt,yeartxt;
    ArrayList<DCRapplist> dcrarraylist=new ArrayList<>();

    @Override
    public void onBackPressed() {
        Intent i=new Intent(ApprovalActivity.this,HomeDashBoard.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FullScreencall();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        list_view=(ListView)findViewById(R.id.list_view);
        mCommonSharedPreference=new CommonSharedPreference(this);
        db_connPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SF_Code =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        lay_leave=(RelativeLayout)findViewById(R.id.lay_leave);
        lay_tp=(RelativeLayout)findViewById(R.id.lay_tp);
        lay_dcr=(RelativeLayout)findViewById(R.id.lay_dcr);
        iv_dwnldmaster_back=(ImageView)findViewById(R.id.iv_dwnldmaster_back);
        lay_tp_header=(LinearLayout)findViewById(R.id.lay_tp_header);
        db_slidedwnloadPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        ll_anim=(LinearLayout)findViewById(R.id.ll_anim);
        mCommonUtilsMethods=new CommonUtilsMethods(this);
        lay_dcrappr=(RelativeLayout)findViewById(R.id.lay_dcrappr);
        monthtxt=findViewById(R.id.txt_mnth);
        yeartxt=findViewById(R.id.year_txt);
        FullScreencall();

        aa.add("hi");
        aa.add("hi");
        aa.add("hi");
        aa.add("hi");

        Log.v("Dcrflag",mCommonSharedPreference.getValueFromPreference("DcrapprvNd"));

        if(mCommonSharedPreference.getValueFromPreference("DcrapprvNd").equalsIgnoreCase("0"))
        {
            lay_dcrappr.setVisibility(View.VISIBLE);
            getDcrList();
        }

        getLvApproval();
        getTpApproval();

        lay_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lay_tp_header.setVisibility(View.GONE);
                AdapterForLeaveApproval leaveadpt=new AdapterForLeaveApproval(ApprovalActivity.this,leaveArray);
                list_view.setAdapter(leaveadpt);
                leaveadpt.notifyDataSetChanged();
            }
        });
        lay_tp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lay_tp_header.setVisibility(View.VISIBLE);
                monthtxt.setText(getResources().getString(R.string.mnth));
                yeartxt.setText(getResources().getString(R.string.year));
                yeartxt.setVisibility(View.VISIBLE);
                AdapterTpApproval tpadpt=new AdapterTpApproval(ApprovalActivity.this,tpArray);
                list_view.setAdapter(tpadpt);
                tpadpt.notifyDataSetChanged();
            }
        });
        lay_dcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lay_tp_header.setVisibility(View.VISIBLE);
                monthtxt.setText(getResources().getString(R.string.DcrDate));
                yeartxt.setVisibility(View.GONE);
                AdapterForDCRApproval adapterForDCRApproval=new AdapterForDCRApproval(ApprovalActivity.this,dcrarraylist);
                list_view.setAdapter(adapterForDCRApproval);
                adapterForDCRApproval.notifyDataSetChanged();
            }
        });
        iv_dwnldmaster_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /*DownloadMasters.bindManagerListLoading(new ManagerListLoading() {
            @Override
            public void ListLoading() {
                ll_anim.setVisibility(View.INVISIBLE);

            }
        });

        AdapterTpApproval.bindlistener(new SpecialityListener() {
            @Override
            public void specialityCode(String code) {
                //Updatecluster(code);
            }
        });
*/

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
    public void getLvApproval(){
        JSONObject json=new JSONObject();
        try {
            json.put("SF", SF_Code);
            Log.v("printing_sf_code",json.toString());
            Call<ResponseBody> approval=apiService.getLeaveApproval(json.toString());

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
                            Log.v("printing_here_tpapprove",is.toString());
                            leaveArray.clear();
                            JSONArray jsonArray=new JSONArray(is.toString());
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json=jsonArray.getJSONObject(i);
                                leaveArray.add(new ModelForLeaveApproval(json.getString("LvID"),json.getString("SFName"),json.getString("FDate"),
                                        json.getString("TDate"),json.getString("Reason"),json.getString("Address"),json.getString("LType"),json.getString("LAvail"),json.getString("No_of_Days")));
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
    public void getTpApproval(){
        JSONObject json=new JSONObject();
        try {
            json.put("SF", SF_Code);
            Log.v("printing_sf_code",json.toString());
            Call<ResponseBody> approval=apiService.getTpApproval(json.toString());

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
                            Log.v("printing_here_approval",is.toString());
                            tpArray.clear();
                            JSONArray jsonArray=new JSONArray(is.toString());
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json=jsonArray.getJSONObject(i);
                                tpArray.add(new ModelTpApproval(json.getString("Sf_Code"),json.getString("SFName"),json.getString("Mnth"),
                                        json.getString("Yr"),json.getString("Mn")));
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

    private void getDcrList() {
        JSONObject json=new JSONObject();
        try {
            json.put("sfCode", SF_Code);
            Log.v("printing_sf_code",json.toString());
            Call<ResponseBody> approval=apiService.getDCRlist(json.toString());

            approval.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        dcrarraylist.clear();
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
                            Log.v("printing_here_tpapprove",is.toString());

                            JSONArray jsonArray=new JSONArray(is.toString());
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json=jsonArray.getJSONObject(i);
                                dcrarraylist.add(new DCRapplist(json.getString("Trans_SlNo"),json.getString("Sf_Name"),json.getString("Activity_Date"),
                                        json.getString("Plan_Name"),json.getString("WorkType_Name"),json.getString("Sf_Code"),json.getString("FieldWork_Indicator")));
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

  /*  public void Updatecluster(String subsfcode){
        dwnloadMasterData = new DownloadMasters(getApplicationContext(), db_connPath, db_slidedwnloadPath, subsfcode,SF_Code);
        ll_anim.setVisibility(View.VISIBLE);
        //MasterList=true;

        dwnloadMasterData.drList();
        dwnloadMasterData.chemsList();
        dwnloadMasterData.terrList();
        dwnloadMasterData.jointwrkCall();

    }*/


}
