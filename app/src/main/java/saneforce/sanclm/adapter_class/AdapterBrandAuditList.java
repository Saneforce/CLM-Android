package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelBrandAuditList;

public class AdapterBrandAuditList extends BaseAdapter {

    Context context;
    ArrayList<ModelBrandAuditList> list=new ArrayList<>();
    String prName;

    public AdapterBrandAuditList(Context context, ArrayList<ModelBrandAuditList> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.row_item_add_brand_audit_list,viewGroup,false);
        TextView prd_title=(TextView)view.findViewById(R.id.prd_title);
        TextView txt_comp_name=(TextView)view.findViewById(R.id.txt_comp_name);
        TextView txt_comp_brd_name=(TextView)view.findViewById(R.id.txt_comp_brd_name);
        TextView txt_qty=(TextView)view.findViewById(R.id.txt_qty);
        TextView txt_rate=(TextView)view.findViewById(R.id.txt_rate);
        TextView txt_value=(TextView)view.findViewById(R.id.txt_value);

        ModelBrandAuditList mm=list.get(i);
        if(TextUtils.isEmpty(prName) || !(prName.equals(mm.getPrName()))) {
            prd_title.setVisibility(View.VISIBLE);
            prd_title.setText(mm.getPrName());
            txt_comp_name.setText(mm.getComName());
            txt_comp_brd_name.setText(mm.getComPrdName());
            txt_qty.setText("Qty : " + mm.getQty());
            txt_rate.setText("Rate : " + mm.getRate());
            txt_value.setText("Value : " + mm.getVal());
            prName=mm.getPrName();
        }
        else{
            prd_title.setVisibility(View.GONE);
            prd_title.setText(mm.getPrName());
            txt_comp_name.setText(mm.getComName());
            txt_comp_brd_name.setText(mm.getComPrdName());
            txt_qty.setText("Qty : " + mm.getQty());
            txt_rate.setText("Rate : " + mm.getRate());
            txt_value.setText("Value : " + mm.getVal());
        }


        return view;
    }
}
