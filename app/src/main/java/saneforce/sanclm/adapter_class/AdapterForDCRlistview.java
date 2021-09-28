package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import saneforce.sanclm.Pojo_Class.DCRapplist;
import saneforce.sanclm.Pojo_Class.DoctorcoverageList;
import saneforce.sanclm.R;

public class AdapterForDCRlistview extends RecyclerView.Adapter<AdapterForDCRlistview.ViewHolder>{
    Context activity;
    ArrayList<DCRapplist> lists;

    public AdapterForDCRlistview (Context activity, ArrayList<DCRapplist> lists) {
        this.activity = activity;
        this.lists = lists;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_dcrview, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.sfname_txt.setText(lists.get(position).getActivity_date());
        holder.cluster_txt.setText(lists.get(position).getPlan_name());
        holder.products_txt.setText(lists.get(position).getWorkType_name());
        holder.gift_txt.setText(lists.get(position).getReporting_to_sf());


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sfname_txt,cluster_txt,products_txt,gift_txt;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

         sfname_txt=itemView.findViewById(R.id.sfname_txt);
         cluster_txt=itemView.findViewById(R.id.cluster_txt);
         products_txt=itemView.findViewById(R.id.product_txt);
         gift_txt=itemView.findViewById(R.id.gift_xt);



        }
    }
}
