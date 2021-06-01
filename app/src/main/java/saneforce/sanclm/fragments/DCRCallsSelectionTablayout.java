package saneforce.sanclm.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.util.UpdateUi;

public class DCRCallsSelectionTablayout  extends Fragment implements TabHost.OnTabChangeListener{
    private FragmentTabHost mTabHost;
    int currentTab=0;
    DetailingTrackerPOJO detailingTrackerPOJO;
    CommonSharedPreference commonSharedPreference;
    Button sub_btn;
    ProgressDialog progressDialog=null;
    Api_Interface apiService;
    String baseurl,SF_Code,Div_code;
    CommonUtilsMethods commonUtilsMethods;
    public static UpdateUi updateUi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_callselection_main_tab_host_container, container, false);
        commonSharedPreference=new CommonSharedPreference(getActivity());
        baseurl =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        SF_Code =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        Div_code =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);
        Log.v("printing_base_rul",baseurl);
        apiService = RetroClient.getClient(baseurl).create(Api_Interface.class);
        commonUtilsMethods=new CommonUtilsMethods(getActivity());

        detailingTrackerPOJO = new DetailingTrackerPOJO();
        try
        {
            mTabHost = (FragmentTabHost) v.findViewById(R.id.tabhost);
            sub_btn=(Button)v.findViewById(R.id.sub_btn);

            mTabHost.setup(getActivity(), getFragmentManager(), R.id.tabFrameLayout);
            /*if(commonSharedPreference.getValueFromPreference("cat").equalsIgnoreCase())
            detailingTrackerPOJO.setTabSelection("0");  R.string.tab_option_listeddr,tab_option_chemist,tab_option_stockist,tab_option_unlisteddr*/

            mTabHost.addTab(mTabHost.newTabSpec("0").setIndicator(getTabIndicator(mTabHost.getContext(),commonSharedPreference.getValueFromPreference("drcap"), R.drawable.doctor_call)),DCRDRCallsSelection.class, null);
            if(commonSharedPreference.getValueFromPreference("chem_need").equals("0"))
                mTabHost.addTab(mTabHost.newTabSpec("1").setIndicator(getTabIndicator(mTabHost.getContext(), commonSharedPreference.getValueFromPreference("chmcap"), R.drawable.chem_tab)),DCRCHMCallsSelection.class, null);
            if(commonSharedPreference.getValueFromPreference("cip_need").equals("0"))
                mTabHost.addTab(mTabHost.newTabSpec("2").setIndicator(getTabIndicator(mTabHost.getContext(), commonSharedPreference.getValueFromPreference("cipcap"), R.drawable.chem_tab)),DCRCIPCallsSelection.class, null);
            if(commonSharedPreference.getValueFromPreference("stk_need").equals("0"))
                mTabHost.addTab(mTabHost.newTabSpec("3").setIndicator(getTabIndicator(mTabHost.getContext(),commonSharedPreference.getValueFromPreference("stkcap"), R.drawable.stockist_tab)),DCRSTKCallsSelection.class, null);
            if(commonSharedPreference.getValueFromPreference("unl_need").equals("0"))
                mTabHost.addTab(mTabHost.newTabSpec("4").setIndicator(getTabIndicator(mTabHost.getContext(), commonSharedPreference.getValueFromPreference("ucap"), R.drawable.ul_dr_tab)),DCRUDRCallsSelection.class, null);


          //  mTabHost.getTabWidget().getChildTabViewAt(mTabHost.getCurrentTab()).setBackgroundResource(bg_seltab);

            mTabHost.setOnTabChangedListener(this);

            setTabSelection(commonSharedPreference.getValueFromPreference("cat"));

            mTabHost.getTabWidget().setDividerDrawable(null);
            mTabHost.getTabWidget().setStripEnabled(false);

            if(!commonSharedPreference.getValueFromPreference("miss_select").equalsIgnoreCase("0")){
                mTabHost.setCurrentTab(commonSharedPreference.getValueFromPreference("selection",0));
            }

            if(commonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")){
                sub_btn.setVisibility(View.VISIBLE);
            }
            else{
                sub_btn.setVisibility(View.GONE);
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("printing_submit","here");
                checkForSubmitValue();
            }
        });

        return v;
    }


    private View getTabIndicator(Context context, String title, int icon)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab_host_layout_format, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }


    @Override
    public void onTabChanged(String tabId) {
        detailingTrackerPOJO.setTabSelection(tabId);

 /*       switch (tabId){

            case "0":
                detailingTrackerPOJO.setTabSelection(tabId);
                break;
            case "1":
                detailingTrackerPOJO.setTabSelection(tabId);
                break;
            case "2":
                detailingTrackerPOJO.setTabSelection(tabId);
                break;
            case "3":"UnListedDoctor
                detailingTrackerPOJO.setTabSelection("UnListedDoctor");
                break;
        }*/


       /* mTabHost.getTabWidget().getChildTabViewAt(currentTab).setBackgroundResource(R.color.ash);
        mTabHost.getTabWidget().getChildAt(Integer.parseInt(tabId)).setBackgroundColor(Color.RED);
      //  mTabHost.getTabWidget().getChildTabViewAt(Integer.parseInt(tabId)).setBackgroundResource(bg_seltab);

        currentTab = Integer.parseInt(tabId);*/
    }

    public void finalSubmission(String val){
        if (progressDialog == null) {
            CommonUtilsMethods commonUtilsMethods = new CommonUtilsMethods(getActivity());
            progressDialog = commonUtilsMethods.createProgressDialog(getActivity());
            progressDialog.show();
        } else {
            progressDialog.show();
        }

        Call<ResponseBody> query = apiService.savemisEntry(val);
        query.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStreamReader ip=null;
                StringBuilder is=new StringBuilder();
                String line=null;
                try {
                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    Log.v("missed_date_sv_det",is.toString());
                    JSONObject js=new JSONObject(is.toString());
                    if(js.getString("success").equalsIgnoreCase("true")){
                        progressDialog.dismiss();
                        updateUi.updatingui();
                        if(js.has("msg"))
                            Toast.makeText(getActivity(),js.getString("msg"),Toast.LENGTH_SHORT).show();
                        else
                        Toast.makeText(getActivity(),"Submit Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        progressDialog.dismiss();
                        if(js.has("msg"))
                            Toast.makeText(getActivity(),js.getString("msg"),Toast.LENGTH_SHORT).show();
//                        else
//                            Toast.makeText(getActivity(),"Please Update App",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void checkForSubmitValue(){
        Log.v("prrrintitnt11","here");
        try{
            Log.v("prrrintitnt112","here");
            JSONObject jsonCheck=new JSONObject();
            JSONArray jsArr=new JSONArray();

        JSONArray jsonn = new JSONArray(commonSharedPreference.getValueFromPreference("missed_array").toString());
        if(jsonn.length()!=0) {

            for (int i = 0; i < jsonn.length(); i++) {
                JSONObject js = jsonn.getJSONObject(i);
                Log.v("printing_final_miss",js.getString("jobj"));
               // finalSubmission(js.getString("jobj"));
                JSONObject jj;
                if(js.getString("jobj").equalsIgnoreCase("")) {
                    jj = new JSONObject();
                    if (js.getString("type").equalsIgnoreCase("D"))
                        jj.put("CateCode",js.getString("cat"));
                    if(js.getString("type").equalsIgnoreCase("D"))
                        jj.put("CusType", "1");
                    else    if(js.getString("type").equalsIgnoreCase("C"))
                        jj.put("CusType", "2");
                    else    if(js.getString("type").equalsIgnoreCase("S"))
                        jj.put("CusType", "3");
                    else
                        jj.put("CusType", "4");
                    jj.put("CustCode", js.getString("code"));
                    jj.put("CustName", js.getString("name"));
                    jj.put("DataSF", commonSharedPreference.getValueFromPreference("sub_sf_code"));
                    jj.put("DivCode", commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION_CODE));
                    jj.put("Sf_Code", commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
                    jj.put("SF", commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
                    jj.put("Div", commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION_CODE));
                    jj.put("AppUserSF", commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
                    jj.put("SFName", commonSharedPreference.getValueFromPreference(CommonUtils.TAG_NAME));
                    jj.put("SpecCode", js.getString("spec"));
                    jj.put("mappedProds", "");
                    jj.put("mode", "0");
                    jj.put("WT", commonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CODE));
                    jj.put("WTNm", commonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_NAME));
                    jj.put("ModTime", commonUtilsMethods.getCurrentInstance() + " " + commonUtilsMethods.getCurrentTime());
                    jj.put("ReqDt", commonUtilsMethods.getCurrentInstance() + " " + commonUtilsMethods.getCurrentTime());
                    jj.put("vstTime", commonUtilsMethods.getCurrentInstance() + " " + commonUtilsMethods.getCurrentTime());
                    jj.put("Remks", "");
                    jj.put("amc", "");
                    jj.put("Pl", commonSharedPreference.getValueFromPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE));

                    if(!TextUtils.isEmpty(commonSharedPreference.getValueFromPreference("hos_code"))){
                        jj.put("hos_code", commonSharedPreference.getValueFromPreference("hos_code"));
                        jj.put("hos_name", commonSharedPreference.getValueFromPreference("hos_name"));
                    }
                }
                    else
                 jj=new JSONObject(js.getString("jobj"));
                jsArr.put(jj);
            }
            Log.v("prrrintitnt113","here");
            jsonCheck.put("EData",jsArr);
            Log.v("prrrintitnt114","here");
            jsonCheck.put("SF",SF_Code);
            Log.v("prrrintitnt115","here");
            jsonCheck.put("SF_Code",SF_Code);
            Log.v("prrrintitnt116","here");
            jsonCheck.put("Div",Div_code+",");
            Log.v("prrrintitnt117","here");
            jsonCheck.put("ReqDt",commonUtilsMethods.getCurrentInstance());
            Log.v("prrrintitnt118","here");
            jsonCheck.put("EDt",commonSharedPreference.getValueFromPreference("mis_date"));
            Log.v("final_missed_sub",jsonCheck.toString());
            finalSubmission(jsonCheck.toString());
        }

            }catch(Exception e){
            Log.v("prrrintitnt112","here"+e.getMessage());
            }
        }


        public void setTabSelection(String val){

        switch (val){

                case "D":
                    detailingTrackerPOJO.setTabSelection("0");
                    mTabHost.setCurrentTab(0);
                break;
                case "C":
                    detailingTrackerPOJO.setTabSelection("1");
                    mTabHost.setCurrentTab(1);
                break;
              case "I":
                detailingTrackerPOJO.setTabSelection("2");
                mTabHost.setCurrentTab(2);
                break;
                case "S":
                    detailingTrackerPOJO.setTabSelection("3");
                    mTabHost.setCurrentTab(3);
                break;
                case "U":
                    detailingTrackerPOJO.setTabSelection("4");
                    mTabHost.setCurrentTab(4);
                break;
        }
            Log.v("selected_tab_are",detailingTrackerPOJO.getTabSelection());

        }

        public static void bindDcrBackPress(UpdateUi ui){
            updateUi=ui;
        }
}
