package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.Pojo_Class.DCRapplist;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.DCRViewActivity;
import saneforce.sanclm.activities.Model.ModelTpApproval;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.util.SpecialityListener;

public class AdapterForDCRApproval extends BaseAdapter {
    Context context;
    ArrayList<DCRapplist> array=new ArrayList<>();
    CommonSharedPreference mCommonSharedPreference;
    static SpecialityListener specialityListener;

    public AdapterForDCRApproval(Context context, ArrayList<DCRapplist> array) {
        this.context = context;
        this.array = array;
        mCommonSharedPreference=new CommonSharedPreference(this.context);
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.row_item_tp_approval,parent,false);

        TextView txt_name=(TextView)convertView.findViewById(R.id.txt_name);
        TextView txt_mnth=(TextView)convertView.findViewById(R.id.txt_mnth);
        TextView txt_yr=(TextView)convertView.findViewById(R.id.txt_yr);
        Button btn_view=(Button)convertView.findViewById(R.id.btn_view);


        txt_name.setText(array.get(position).getSf_name());
        txt_mnth.setText(array.get(position).getActivity_date());
        txt_yr.setVisibility(View.GONE);

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DCRViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("transno",array.get(position).getTrans_slNo());
                intent.putExtra("sfname",array.get(position).getSf_name());
                intent.putExtra("activitydate",array.get(position).getActivity_date());
                intent.putExtra("worktype",array.get(position).getWorkType_name());
                intent.putExtra("sfcode",array.get(position).getReporting_to_sf());
                context.startActivity(intent);
            }
        });


        return convertView;
    }
}
