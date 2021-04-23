package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelDayReport;
import saneforce.sanclm.activities.ReportListActivity;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;

public class AdapterForReportClass extends RecyclerView.Adapter<AdapterForReportClass.MyViewHolder> {
    ArrayList<ModelDayReport> array=new ArrayList<>();
    Context context;
    int val;
    CommonSharedPreference commonSharedPreference;

    public AdapterForReportClass(Context context,ArrayList<ModelDayReport> array,int val){
        this.context=context;
        this.array=array;
        this.val=val;
        commonSharedPreference=new CommonSharedPreference(this.context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_item_day_report,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ModelDayReport mm=array.get(position);
        holder.txt_date.setText(mm.getDate());
        holder.txt_name.setText(mm.getName());
        holder.txt_wt.setText(mm.getWrktyp());
        holder.txt_cluster.setText(mm.getCluster());
        holder.txt_hw_value.setText(mm.getHoliday());
        holder.txt_remark.setText(mm.getRem());
        holder.txt_count_dr.setText(mm.getDr());
        holder.txt_count_chm.setText(mm.getCh());
        holder.txt_count_stk.setText(mm.getSt());
        holder.txt_count_ul.setText(mm.getUl());
        holder.txt_chemist.setText(commonSharedPreference.getValueFromPreference("chmcap"));
        holder.txt_dr.setText(commonSharedPreference.getValueFromPreference("drcap"));
        holder.txt_stk.setText(commonSharedPreference.getValueFromPreference("stkcap"));
        holder.txt_udr.setText(commonSharedPreference.getValueFromPreference("ucap"));

        holder.lay_dr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mm.getDr().equalsIgnoreCase("0"))
                    CommonUtilsMethods.showToast(context,"No Report");
                else{
                    Log.v("printing_acd_value",mm.getCode()+"");
                    Intent i=new Intent(context,ReportListActivity.class);
                    i.putExtra("val","1");
                    i.putExtra("acd",mm.getCode());
                    i.putExtra("typ","1");
                    context.startActivity(i);
                }
            }
        });
        holder.lay_chm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mm.getCh().equalsIgnoreCase("0"))
                    CommonUtilsMethods.showToast(context,"No Report");
                else{
                    Intent i=new Intent(context,ReportListActivity.class);
                    i.putExtra("val","1");
                    i.putExtra("acd",mm.getCode());
                    i.putExtra("typ","2");
                    context.startActivity(i);
                }
            }
        });
        holder.lay_stk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mm.getSt().equalsIgnoreCase("0"))
                    CommonUtilsMethods.showToast(context,"No Report");
                else{
                    Intent i=new Intent(context,ReportListActivity.class);
                    i.putExtra("val","1");
                    i.putExtra("acd",mm.getCode());
                    i.putExtra("typ","3");
                    context.startActivity(i);
                }
            }
        });
        holder.lay_ul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mm.getUl().equalsIgnoreCase("0"))
                    CommonUtilsMethods.showToast(context,"No Report");
                else{
                    Intent i=new Intent(context,ReportListActivity.class);
                    i.putExtra("val","1");
                    i.putExtra("acd",mm.getCode());
                    i.putExtra("typ","4");
                    context.startActivity(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_cluster,txt_hw_value,txt_remark,txt_date,txt_name,txt_wt,txt_count_ul,txt_count_dr,txt_count_chm,txt_count_stk;
        LinearLayout lay_ul,lay_dr,lay_chm,lay_stk;
        TextView txt_udr,txt_dr,txt_stk,txt_chemist;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_cluster=(TextView)itemView.findViewById(R.id.txt_cluster);
            txt_hw_value=(TextView)itemView.findViewById(R.id.txt_hw_value);
            txt_remark=(TextView)itemView.findViewById(R.id.txt_remark);
            txt_date=(TextView)itemView.findViewById(R.id.txt_date);
            txt_name=(TextView)itemView.findViewById(R.id.txt_name);
            txt_wt=(TextView)itemView.findViewById(R.id.txt_wt);
            txt_udr=(TextView)itemView.findViewById(R.id.txt_udr);
            txt_dr=(TextView)itemView.findViewById(R.id.txt_dr);
            txt_stk=(TextView)itemView.findViewById(R.id.txt_stk);
            txt_chemist=(TextView)itemView.findViewById(R.id.txt_chemist);
            txt_count_dr=(TextView)itemView.findViewById(R.id.txt_count_dr);
            txt_count_chm=(TextView)itemView.findViewById(R.id.txt_count_chm);
            txt_count_stk=(TextView)itemView.findViewById(R.id.txt_count_stk);
            txt_count_ul=(TextView)itemView.findViewById(R.id.txt_count_ul);
            lay_ul=(LinearLayout)itemView.findViewById(R.id.lay_ul);
            lay_dr=(LinearLayout)itemView.findViewById(R.id.lay_dr);
            lay_chm=(LinearLayout)itemView.findViewById(R.id.lay_chm);
            lay_stk=(LinearLayout)itemView.findViewById(R.id.lay_stk);
            if(val!=1)
                txt_date.setVisibility(View.GONE);
        }
    }

}
