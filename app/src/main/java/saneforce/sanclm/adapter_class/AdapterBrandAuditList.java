package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelBrandAuditList;

public class AdapterBrandAuditList extends RecyclerView.Adapter<AdapterBrandAuditList.ViewHolder> {

    Context context;
    ArrayList<ModelBrandAuditList> list=new ArrayList<>();
    String prName,chmname;

    public AdapterBrandAuditList(Context context, ArrayList<ModelBrandAuditList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.row_item_add_brand_audit_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelBrandAuditList mm = list.get(position);

        //  if (TextUtils.isEmpty(prName) || !((chmname+"~"+prName).equals(mm.getChem()+"~"+mm.getPrName()))) {
        holder.chm_title.setVisibility(View.VISIBLE);
        holder.prd_title.setVisibility(View.VISIBLE);
        holder.chm_title.setText(mm.getChem());
        holder.prd_title.setText(mm.getPrName());
        holder.txt_comp_name.setText(mm.getComName());
        holder.txt_comp_brd_name.setText(mm.getComPrdName());
        holder.txt_qty.setText("Qty : " + mm.getQty());
        holder.txt_rate.setText("Rate : " + mm.getRate());
        holder.txt_value.setText("Value : " + mm.getVal());
        prName = mm.getPrName();
        chmname = mm.getChem();
//        } else {
//            holder.chm_title.setVisibility(View.GONE);
//            holder.prd_title.setVisibility(View.GONE);
//            holder.prd_title.setText(mm.getPrName());
//            holder.txt_comp_name.setText(mm.getComName());
//            holder.txt_comp_brd_name.setText(mm.getComPrdName());
//            holder.txt_qty.setText("Qty : " + mm.getQty());
//            holder.txt_rate.setText("Rate : " + mm.getRate());
//            holder.txt_value.setText("Value : " + mm.getVal());
//        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }

    @Override
    public int getItemCount() { return list.size(); }

//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        final ViewHolder viewHolder;
//        if(view==null) {
//            viewHolder=new ViewHolder();
//            view = LayoutInflater.from(context).inflate(R.layout.row_item_add_brand_audit_list, viewGroup, false);
////        TextView prd_title=(TextView)view.findViewById(R.id.prd_title);
////        TextView txt_comp_name=(TextView)view.findViewById(R.id.txt_comp_name);
////        TextView txt_comp_brd_name=(TextView)view.findViewById(R.id.txt_comp_brd_name);
////        TextView txt_qty=(TextView)view.findViewById(R.id.txt_qty);
////        TextView txt_rate=(TextView)view.findViewById(R.id.txt_rate);
////        TextView txt_value=(TextView)view.findViewById(R.id.txt_value);
//
//            viewHolder.prd_title=(TextView)view.findViewById(R.id.prd_title);
//            viewHolder.txt_comp_name=(TextView)view.findViewById(R.id.txt_comp_name);
//            viewHolder.txt_comp_brd_name=(TextView)view.findViewById(R.id.txt_comp_brd_name);
//            viewHolder.txt_qty=(TextView)view.findViewById(R.id.txt_qty);
//            viewHolder.txt_rate=(TextView)view.findViewById(R.id.txt_rate);
//            viewHolder.txt_value=(TextView)view.findViewById(R.id.txt_value);
//
//
//
//            ModelBrandAuditList mm = list.get(i);
//            if (TextUtils.isEmpty(prName) || !(prName.equals(mm.getPrName()))) {
//                    viewHolder.prd_title.setVisibility(View.VISIBLE);
//                    viewHolder.prd_title.setText(mm.getPrName());
//                    viewHolder.txt_comp_name.setText(mm.getComName());
//                    viewHolder.txt_comp_brd_name.setText(mm.getComPrdName());
//                    viewHolder.txt_qty.setText("Qty : " + mm.getQty());
//                    viewHolder.txt_rate.setText("Rate : " + mm.getRate());
//                    viewHolder.txt_value.setText("Value : " + mm.getVal());
//                    prName = mm.getPrName();
//                } else {
//                    viewHolder.prd_title.setVisibility(View.GONE);
//                    viewHolder.prd_title.setText(mm.getPrName());
//                    viewHolder.txt_comp_name.setText(mm.getComName());
//                    viewHolder.txt_comp_brd_name.setText(mm.getComPrdName());
//                    viewHolder.txt_qty.setText("Qty : " + mm.getQty());
//                    viewHolder.txt_rate.setText("Rate : " + mm.getRate());
//                    viewHolder.txt_value.setText("Value : " + mm.getVal());
//                }
//            view.setTag(viewHolder);
//        }else
//        {
//            viewHolder = (ViewHolder) view.getTag();
//        }
//
//        return view;
//    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView prd_title,txt_comp_name,txt_comp_brd_name,txt_qty,txt_rate,txt_value,chm_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chm_title=(TextView)itemView.findViewById(R.id.chm_title);
            prd_title=(TextView)itemView.findViewById(R.id.prd_title);
            txt_comp_name=(TextView)itemView.findViewById(R.id.txt_comp_name);
            txt_comp_brd_name=(TextView)itemView.findViewById(R.id.txt_comp_brd_name);
            txt_qty=(TextView)itemView.findViewById(R.id.txt_qty);
            txt_rate=(TextView)itemView.findViewById(R.id.txt_rate);
            txt_value=(TextView)itemView.findViewById(R.id.txt_value);
        }
    }
}

