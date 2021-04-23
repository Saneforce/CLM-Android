package saneforce.sanclm.adapter_class;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelForReportList;
import saneforce.sanclm.activities.Model.ModelMissedReport;

public class AdapterForMisssedReportList extends RecyclerView.Adapter<AdapterForMisssedReportList.MyViewHolder> {
    Context context;
    ArrayList<ModelForReportList> array=new ArrayList<>();

    public AdapterForMisssedReportList(Context context, ArrayList<ModelForReportList> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_item_missed_report_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelForReportList mm=array.get(position);

        holder.txt_name_miss.setText(mm.getName());
        holder.txt_date_miss.setText(mm.getDate());
        holder.txt_cluster.setText(mm.getCluster());
        holder.txt_hw_value.setText(mm.getQualify());
        holder.txt_remark.setText(mm.getSpec());
        holder.txt_previous.setText(mm.getPrev());
        holder.txt_class.setText(mm.getCls());
        holder.txt_category.setText(mm.getCategory());



    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_name_miss,txt_date_miss,txt_cluster,txt_hw_value,txt_remark,txt_previous,txt_class,txt_category;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_name_miss=(TextView)itemView.findViewById(R.id.txt_name_miss);
            txt_date_miss=(TextView)itemView.findViewById(R.id.txt_date_miss);
            txt_cluster=(TextView)itemView.findViewById(R.id.txt_cluster);
            txt_hw_value=(TextView)itemView.findViewById(R.id.txt_hw_value);
            txt_remark=(TextView)itemView.findViewById(R.id.txt_remark);
            txt_previous=(TextView)itemView.findViewById(R.id.txt_previous);
            txt_class=(TextView) itemView.findViewById(R.id.txt_class);
            txt_category=(TextView) itemView.findViewById(R.id.txt_category);
        }
    }
}
