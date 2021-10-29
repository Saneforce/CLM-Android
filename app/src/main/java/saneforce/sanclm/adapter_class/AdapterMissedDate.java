package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.DCRCallSelectionActivity;
import saneforce.sanclm.activities.LeaveActivity;
import saneforce.sanclm.activities.Model.MissedDate;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.util.UpdateUi;

public class AdapterMissedDate  extends BaseAdapter {
    Context context;
    ArrayList<MissedDate> list=new ArrayList<>();
    LayoutInflater layoutInflater;
    CommonSharedPreference mCommonSharedPreference;
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;
    static UpdateUi updateUi;

    public AdapterMissedDate(Context context, ArrayList<MissedDate> list) {
        this.context = context;
        this.list = list;
        mCommonSharedPreference=new CommonSharedPreference(this.context);
        sharedpreferences=context.getSharedPreferences(context.getResources().getString(R.string.preference_name), Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView=LayoutInflater.from(context).inflate(R.layout.row_item_missed_date,parent,false);
        LinearLayout lay_fw=(LinearLayout)convertView.findViewById(R.id.lay_fw);
        LinearLayout lay_other=(LinearLayout)convertView.findViewById(R.id.lay_others);
        LinearLayout lay_leave=(LinearLayout)convertView.findViewById(R.id.lay_leave);
        RelativeLayout mis_fw=(RelativeLayout)convertView.findViewById(R.id.mis_fw);
        RelativeLayout mis_other=(RelativeLayout)convertView.findViewById(R.id.mis_other);
        RelativeLayout mis_leave=(RelativeLayout)convertView.findViewById(R.id.mis_leave);
        TextView txt_mnyr=(TextView)convertView.findViewById(R.id.txt_mnyr);
        TextView txt_date=(TextView)convertView.findViewById(R.id.txt_date);
        TextView txt_day=(TextView)convertView.findViewById(R.id.txt_day);
        Button btn_leave=(Button) convertView.findViewById(R.id.submit);
        MissedDate mm=list.get(position);
        txt_mnyr.setText(mm.getMnthYr());
        txt_date.setText(mm.getDate());
        txt_day.setText(mm.getDay());

        if(mCommonSharedPreference.getValueFromPreference("Missed_leave").equalsIgnoreCase("0"))
            lay_leave.setVisibility(View.VISIBLE);

        if(list.get(position).getStatus().equalsIgnoreCase("6"))
        {
            lay_fw.setBackgroundResource(R.color.color_grey);
            lay_other.setBackgroundResource(R.color.color_grey);
        }else
        {
            lay_fw.setBackgroundResource(R.color.white);
            lay_other.setBackgroundResource(R.color.white);
        }

        mis_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommonSharedPreference.setValueToPreference("mis_date",list.get(position).getTd_date());
                updateUi.updatingui();
            }
        });

        btn_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommonSharedPreference.setValueToPreference("mis_date",list.get(position).getTd_date());
                Intent intent=new Intent(context, LeaveActivity.class);
                intent.putExtra("Missed","2");
                context.startActivity(intent);
            }
        });


        mis_fw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mCommonSharedPreference.setValueToPreference("mis_date",list.get(position).getTd_date());

               /* sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE,"");
                sharedpreferences.getString(CommonUtils.TAG_SF_CODE,"");
                sharedpreferences.getString(CommonUtils.TAG_SF_HQ,"");
                sharedpreferences.getString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME,"");*/

                editor.putString(CommonUtils.TAG_TMP_WORKTYPE_NAME, sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_NAME,""));
                editor.putString(CommonUtils.TAG_TMP_WORKTYPE_CODE, sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_CODE,""));
                editor.putString(CommonUtils.TAG_TMP_WORKTYPE_CLUSTER_CODE,sharedpreferences.getString(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE,""));
                editor.putString(CommonUtils.TAG_TMP_SF_CODE, sharedpreferences.getString(CommonUtils.TAG_SF_CODE,""));
                editor.putString(CommonUtils.TAG_TMP_SF_HQ, sharedpreferences.getString(CommonUtils.TAG_SF_HQ,""));
                editor.putString(CommonUtils.TAG_TMP_MYDAY_WORKTYPE_CLUSTER_NAME, sharedpreferences.getString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME,""));


                editor.putString(CommonUtils.TAG_WORKTYPE_NAME, "Field Work");
                editor.putString(CommonUtils.TAG_WORKTYPE_CODE, CommonUtils.wrktype_code);
                editor.commit();

                Log.v("showing_missed_date",sharedpreferences.getString(CommonUtils.TAG_TMP_WORKTYPE_CODE,"")+" wrkfield "+sharedpreferences.getString(CommonUtils.TAG_TMP_SF_CODE,""));
             /*   editor.putString(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE,mydayclustrCd);
                editor.putString(CommonUtils.TAG_SF_CODE, SF_Code);
                editor.putString(CommonUtils.TAG_SF_HQ, tv_headquater.getText().toString());
                editor.putString(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, tv_cluster.getText().toString());*/

                mCommonSharedPreference.setValueToPreference("missed","true");
                Intent i=new Intent(context, DCRCallSelectionActivity.class);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    public static void bindWorkListener(UpdateUi up){
        updateUi=up;
    }


    }
