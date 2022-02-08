package saneforce.sanclm.Sales_Report.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.R;
import saneforce.sanclm.Sales_Report.Interface.onMultiFiledListener;
import saneforce.sanclm.Sales_Report.Modelclass.Designation_List;

public class MulticheckFieldAdapter extends RecyclerView.Adapter<MulticheckFieldAdapter.MyViewHolder> {
    public static ArrayList<Designation_List> designation_lists;
    Context Context;
    onMultiFiledListener onMultiCheckDivListener;

    public MulticheckFieldAdapter(ArrayList<Designation_List> designation_lists,Context context, onMultiFiledListener onMultiCheckDivListener) {
        Context = context;
        this.onMultiCheckDivListener = onMultiCheckDivListener;
        this.designation_lists = designation_lists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dailog_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Designation_List designation_list = designation_lists.get(position);

        myViewHolder.ch_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Dcrdatas.checkin=1;
                Dcrdatas.productFrom="0";
//                Dcrdatas.IsSelectAll=0;
                designation_list.setChecked(isChecked);
                if (isChecked) {
                    onMultiCheckDivListener.OnMultiCheckDivListener_Add(designation_list);
                } else {
                    onMultiCheckDivListener.OnMultiCheckDivListener_Remove(designation_list);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        CheckBox ch_checkbox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.txtName_mulcheck);
            ch_checkbox = (CheckBox) itemView.findViewById(R.id.checkBox_mulcheck);
        }
    }
}
