package saneforce.sanclm.fragments;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.Model.SlideDetail;
import saneforce.sanclm.adapter_class.AdapterPopupSpinnerSelection;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.DownloadMasters;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class DrProfile extends Fragment {
    RelativeLayout rlay_qua,rlay_spec,rlay_cat;
    DataBaseHandler dbh;
    TextView spin_txt_qua,spin_txt_spec,spin_txt_cat,tv_dcrsel_drname;
    CheckBox chk_male,chk_fmale;
    Spinner spinner_day,spinner_mnth,spinnerw_day,spinnerw_mnth;
    ArrayList<String> list_day=new ArrayList<>();
    ArrayList<String>   list_mnth=new ArrayList<>();
    ArrayList<SlideDetail> list_dr=new ArrayList<>();
    RelativeLayout  main_lay;
    Button  btn_reselect,btn_submit;
    CommonSharedPreference  commonSharedPreference;
    ImageView   back_icon;
    String  spec,qua,category,day,mnth,wday,wmnth,gender="MALE",db_slidedwnloadPath;
    EditText    edtw_yr,edt_yr,edt_maddr,edt_addr2,edt_addr3,edt_addr4,edt_addr5,edt_mail,edt_phone,edt_mob;
    Api_Interface apiService;
    String db_connPath;
    DownloadMasters dwnloadMasterData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vv = inflater.inflate(R.layout.dr_profile_layout, container, false);
        rlay_qua=(RelativeLayout)vv.findViewById(R.id.rlay_qua);
        spinner_day = (Spinner) vv.findViewById(R.id.spinner_day);
        spinner_mnth = (Spinner) vv.findViewById(R.id.spinner_mnth);
        spinnerw_day = (Spinner) vv.findViewById(R.id.spinnerw_day);
        spinnerw_mnth = (Spinner) vv.findViewById(R.id.spinnerw_mnth);
        main_lay=(RelativeLayout)vv.findViewById(R.id.main_lay);
        rlay_cat=(RelativeLayout)vv.findViewById(R.id.rlay_cat);
        rlay_spec=(RelativeLayout)vv.findViewById(R.id.rlay_spec);
        tv_dcrsel_drname=(TextView) vv.findViewById(R.id.tv_dcrsel_drname);
        spin_txt_qua=(TextView) vv.findViewById(R.id.spin_txt_qua);
        spin_txt_spec=(TextView) vv.findViewById(R.id.spin_txt_spec);
        spin_txt_cat=(TextView) vv.findViewById(R.id.spin_txt_cat);
        edt_yr=(EditText) vv.findViewById(R.id.edt_yr);
        edtw_yr=(EditText) vv.findViewById(R.id.edtw_yr);
        edt_maddr=(EditText) vv.findViewById(R.id.edt_maddr);
        edt_addr2=(EditText) vv.findViewById(R.id.edt_addr2);
        edt_addr3=(EditText) vv.findViewById(R.id.edt_addr3);
        edt_addr4=(EditText) vv.findViewById(R.id.edt_addr4);
        edt_addr5=(EditText) vv.findViewById(R.id.edt_addr5);
        edt_mob=(EditText) vv.findViewById(R.id.edt_mob);
        edt_phone=(EditText) vv.findViewById(R.id.edt_phone);
        edt_mail=(EditText) vv.findViewById(R.id.edt_mail);
        chk_male=(CheckBox)vv.findViewById(R.id.chk_male);
        chk_fmale=(CheckBox)vv.findViewById(R.id.chk_fmale);
        btn_reselect=(Button)vv.findViewById(R.id.btn_reselect);
        btn_submit=(Button)vv.findViewById(R.id.btn_submit);
        back_icon=(ImageView)vv.findViewById(R.id.back_icon);
        dbh = new DataBaseHandler(getActivity());
        commonSharedPreference=new CommonSharedPreference(getActivity());
        db_connPath =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        db_slidedwnloadPath =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
         apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        for(int i=1;i<32;i++){
            list_day.add(String.valueOf(i));
        }
        for(int i=1;i<13;i++){
            list_mnth.add(String.valueOf(i));
        }
        tv_dcrsel_drname.setText(commonSharedPreference.getValueFromPreference("Pname"));
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i=new Intent(getActivity(), HomeDashBoard.class);
                startActivity(i);
            }
        });
        btn_reselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_lay, new DCRDRCallsSelection()).commit();

            }
        });
        chk_male.setChecked(true);
        chk_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    chk_fmale.setChecked(false);
                    gender="MALE";
                }
                else{
                    chk_fmale.setChecked(true);
                    gender="FEMALE";
                }
            }
        });
        chk_fmale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    chk_male.setChecked(false);
                    gender="FEMALE";
                }
                else{
                    chk_male.setChecked(true);
                    gender="MALE";
                }
            }
        });
        rlay_qua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingTableValue(1);
            }
        });
        rlay_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingTableValue(2);
            }
        });
        rlay_spec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingTableValue(4);
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),R.layout.textview_differ_size, list_day);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(),R.layout.textview_differ_size, list_mnth);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.textview_bg_spinner);
        dataAdapter1.setDropDownViewResource(R.layout.textview_bg_spinner);

        // attaching data adapter to spinner
        spinner_day.setAdapter(dataAdapter);
        spinner_mnth.setAdapter(dataAdapter1);
        spinnerw_day.setAdapter(dataAdapter);
        spinnerw_mnth.setAdapter(dataAdapter1);

        spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day=list_day.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerw_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wday=list_day.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerw_mnth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wmnth=list_mnth.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_mnth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mnth=list_mnth.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProfile();
            }
        });
        commonFun();
        return vv;
    }
    public void gettingTableValue(int x){
        list_dr.clear();
        dbh.open();
        Cursor cur=null;
        if(x==1)
            cur=dbh.select_quality_list();
        else if(x==2)
            cur=dbh.select_category_list();
        else if(x==3)
            cur=dbh.select_class_list();
        else if(x==4)
            cur=dbh.select_speciality_list();
        else
            cur=dbh.select_ClusterList();

        if(cur.getCount()>0){
            while(cur.moveToNext()){
                list_dr.add(new SlideDetail(cur.getString(2),cur.getString(1)));

            }
        }

        popupSpinner(x);
    }

    public void popupSpinner(final int x){
        final Dialog dialog=new Dialog(getActivity(),R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_spinner_selection);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ListView popup_list=(ListView)dialog.findViewById(R.id.popup_list);

        AdapterPopupSpinnerSelection popupAdapter=new AdapterPopupSpinnerSelection(getActivity(),list_dr);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("qualification_select",list_dr.get(position).getInputName());
                if(x==1) {
                    spin_txt_qua.setText(list_dr.get(position).getInputName());
                    qua=list_dr.get(position).getIqty();
                }
                else if(x==2){
                    spin_txt_cat.setText(list_dr.get(position).getInputName());
                    category=list_dr.get(position).getIqty();
                   /* txt_select_category.setText(list_dr.get(position).getInputName());
                    txt_cat=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();*/
                }
                else if(x==3) {
                    spin_txt_spec.setText(list_dr.get(position).getInputName());
                    /*txt_select_class.setText(list_dr.get(position).getInputName());
                    txt_class=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();*/
                }
                else if(x==4) {
                    spin_txt_spec.setText(list_dr.get(position).getInputName());
                    spec=list_dr.get(position).getIqty();
                    /*txt_select_spec.setText(list_dr.get(position).getInputName());
                    txt_spec=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();*/
                }
                else {
                    /*txt_select_terr.setText(list_dr.get(position).getInputName());
                    txt_terr=list_dr.get(position).getInputName()+","+list_dr.get(position).getIqty();*/
                }

                dialog.dismiss();
            }
        });


    }
    public void commonFun(){
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public void submitProfile(){
        try{
            JSONObject  json=new JSONObject();
            json.put("CustCode",commonSharedPreference.getValueFromPreference("Pcode"));
            json.put("SF",commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));
            json.put("vstTime", CommonUtilsMethods.getCurrentInstance()+" "+CommonUtilsMethods.getCurrentTime());
            json.put("DivCode", commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION));
            json.put("DrDOBY", edt_yr.getText().toString());
            json.put("DrDOBM", mnth);
            json.put("DrDOBD", day);
            json.put("DrDOWY", edtw_yr.getText().toString());
            json.put("DrDOWM", wmnth);
            json.put("DrDOWD", wday);
            json.put("Products", "[]");
            json.put("VisitDays", "[]");
            json.put("DrQual", qua);
            json.put("DrSpec", spec);
            json.put("DrCat", category);
            json.put("DrGender", gender);
            json.put("DrAdd1", edt_maddr.getText().toString());
            json.put("DrAdd2", edt_addr2.getText().toString());
            json.put("DrAdd3", edt_addr3.getText().toString());
            json.put("DrAdd4", edt_addr4.getText().toString());
            json.put("DrAdd5", edt_addr5.getText().toString());
            json.put("DrPhone", edt_phone.getText().toString());
            json.put("DrMob", edt_mob.getText().toString());
            json.put("DrEmail", edt_mail.getText().toString());
            json.put("DrType", "");
            json.put("DrTar", "");
            Log.v("printing_dr_profile",json.toString());

            Call<ResponseBody> taggedDr=apiService.svDrProfile(json.toString());

            taggedDr.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track",response.body().byteStream()+"");
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
                            Log.v("printing_res_profile",is.toString());
                            JSONObject  jj=new JSONObject(is.toString());
                            if(jj.getString("success").equalsIgnoreCase("true")){
                                dwnloadMasterData = new DownloadMasters(getActivity(), db_connPath, db_slidedwnloadPath, commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE),8);
                                dwnloadMasterData.drList();
                                Toast.makeText(getActivity(),"Successfully  Updated",Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_lay, new DCRDRCallsSelection()).commit();

                            }
                        } catch (Exception e) {
                        }






                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });


        }catch (Exception   e){}
    }
}
