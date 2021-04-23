package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.DemoActivity;
import saneforce.sanclm.activities.Model.ModelTpApproval;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.util.SpecialityListener;

public class AdapterTpApproval extends BaseAdapter {
    Context context;
    ArrayList<ModelTpApproval> array=new ArrayList<>();
    CommonSharedPreference mCommonSharedPreference;
    static SpecialityListener specialityListener;

    public AdapterTpApproval(Context context, ArrayList<ModelTpApproval> array) {
        this.context = context;
        this.array = array;
        mCommonSharedPreference=new CommonSharedPreference(this.context);
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.row_item_tp_approval,viewGroup,false);
        TextView txt_name=(TextView)view.findViewById(R.id.txt_name);
        TextView txt_mnth=(TextView)view.findViewById(R.id.txt_mnth);
        TextView txt_yr=(TextView)view.findViewById(R.id.txt_yr);
        Button btn_view=(Button)view.findViewById(R.id.btn_view);

        txt_name.setText(array.get(i).getName());
        txt_mnth.setText(array.get(i).getMnth());
        txt_yr.setText(array.get(i).getYr());

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json=new JSONObject();
                try{

                    json.put("SF",array.get(i).getCode());
                    json.put("Month",array.get(i).getMn());
                    json.put("Year",array.get(i).getYr());
                }catch (Exception e){}
                mCommonSharedPreference.setValueToPreference("approve",json.toString());
                mCommonSharedPreference.setValueToPreference("tpmonth",array.get(i).getMn());
                mCommonSharedPreference.setValueToPreference("tpyear",array.get(i).getYr());
                mCommonSharedPreference.setValueToPreference("subsf",array.get(i).getCode());
                mCommonSharedPreference.setValueToPreference("subsfname",array.get(i).getName());
              // specialityListener.specialityCode(array.get(i).getCode());
                Intent i=new Intent(context, DemoActivity.class);
                i.putExtra("viewStatus",true);
                context.startActivity(i);

            }
        });
        return view;
    }

    public static void bindlistener(SpecialityListener spec){
        specialityListener=spec;
    }
}
