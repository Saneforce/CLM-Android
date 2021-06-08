package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelMissedReport;
import saneforce.sanclm.activities.ReportListActivity;

public class AdapterForMissedReport extends RecyclerView.Adapter<AdapterForMissedReport.MyViewHolder> {
    Context context;
    ArrayList<ModelMissedReport> array=new ArrayList<>();

    public AdapterForMissedReport(Context context, ArrayList<ModelMissedReport> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_item_missed_report,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ModelMissedReport mm=array.get(position);

        holder.txt_name_miss.setText(mm.getName());
        holder.txt_date_miss.setText(mm.getDisplay_date());
        holder.txt_hq_miss_val.setText(" "+mm.getCluster());
        holder.txt_count_dr_miss.setText(mm.getDr());
        holder.txt_count_chm_miss.setText(mm.getMet());
        holder.txt_count_stk_miss.setText(mm.getMissed());

        holder.lay_stk_miss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, ReportListActivity.class);
                i.putExtra("val","2");
                i.putExtra("sf",mm.getCode());
                i.putExtra("dt",mm.getReq_date());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_name_miss,txt_date_miss,txt_hq_miss_val,txt_count_dr_miss,txt_count_chm_miss,txt_count_stk_miss;
        LinearLayout lay_stk_miss;
        public MyViewHolder(View itemView) {
            super(itemView);
            txt_name_miss=(TextView)itemView.findViewById(R.id.txt_name_miss);
            txt_date_miss=(TextView)itemView.findViewById(R.id.txt_date_miss);
            txt_hq_miss_val=(TextView)itemView.findViewById(R.id.txt_hq_miss_val);
            txt_count_dr_miss=(TextView)itemView.findViewById(R.id.txt_count_dr_miss);
            txt_count_chm_miss=(TextView)itemView.findViewById(R.id.txt_count_chm_miss);
            txt_count_stk_miss=(TextView)itemView.findViewById(R.id.txt_count_stk_miss);
            lay_stk_miss=(LinearLayout)itemView.findViewById(R.id.lay_stk_miss);
        }
    }
}
