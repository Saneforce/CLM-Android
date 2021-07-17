package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.text.TextUtils;
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
import saneforce.sanclm.util.UpdateUi;

public class AdapterBrandAuditList2 extends RecyclerView.Adapter<AdapterBrandAuditList2.Viewholder> {

    Context context;
    ArrayList<ModelBrandAuditList> list=new ArrayList<>();
    String prName;
    static UpdateUi updateUi;

    public AdapterBrandAuditList2(Context context, ArrayList<ModelBrandAuditList> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public Viewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.new_row_item_add_brand_audit_list,parent,false);
        return new AdapterBrandAuditList2.Viewholder(view);
    }

    @Override
    public void onBindViewHolder( AdapterBrandAuditList2.Viewholder holder, int position) {
        ModelBrandAuditList mm=list.get(position);
        if(TextUtils.isEmpty(prName) || !(prName.equals(mm.getPrName()))) {
            holder.prd_title.setVisibility(View.VISIBLE);
            holder.prd_title.setText(mm.getPrName());
            holder.txt_comp_name.setText(mm.getComName());
            holder.txt_comp_brd_name.setText(mm.getComPrdName());
            holder. txt_qty.setText("Inv.Qty : " + mm.getQty());
            holder. txt_rate.setText("PTP : " + mm.getRate());
            holder. txt_value.setText("PTR : " + mm.getVal());
            holder.txt_sw.setText("Avg.S/w : " + mm.getSw());
            holder. txt_rx.setText("Rx : " + mm.getRx());
            prName=mm.getPrName();
        }
        else{
            holder.prd_title.setVisibility(View.GONE);
            holder.prd_title.setText(mm.getPrName());
            holder.txt_comp_name.setText(mm.getComName());
            holder.txt_comp_brd_name.setText(mm.getComPrdName());
            holder.txt_qty.setText("Inv.Qty : " + mm.getQty());
            holder. txt_rate.setText("PTP : " + mm.getRate());
            holder. txt_value.setText("PTR : " + mm.getVal());
            holder. txt_sw.setText("Avg.S/w : " + mm.getSw());
            holder. txt_rx.setText("Rx : " + mm.getRx());

        }

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public static void bindListenerBrand(UpdateUi up){
        updateUi=up;
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        TextView prd_title,txt_comp_name,txt_comp_brd_name,txt_qty,txt_rate,txt_value,txt_sw,txt_rx;
        public Viewholder( View view) {
            super(view);
            prd_title=(TextView)view.findViewById(R.id.prd_title);
             txt_comp_name=(TextView)view.findViewById(R.id.txt_comp_name);
             txt_comp_brd_name=(TextView)view.findViewById(R.id.txt_comp_brd_name);
             txt_qty=(TextView)view.findViewById(R.id.txt_qty);
            txt_rate=(TextView)view.findViewById(R.id.txt_rate);
           txt_value=(TextView)view.findViewById(R.id.txt_value);
           txt_sw=(TextView)view.findViewById(R.id.txt_sw);
             txt_rx=(TextView)view.findViewById(R.id.txt_rx);
        }
    }
}
