package saneforce.sanclm.Sales_Report.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.R;
import saneforce.sanclm.Sales_Report.Interface.OnMultiCheckListener;
import saneforce.sanclm.Sales_Report.Modelclass.MultiCheckDivModel;

public class MultiCheckDivAdaptor extends RecyclerView.Adapter<MultiCheckDivAdaptor.MyViewHolder> {
    public static ArrayList<MultiCheckDivModel> divchecklist;
    Context Context;
    OnMultiCheckListener onMultiCheckDivListener;
    RecyclerView recyclerView;
    CheckBox checkBox;

    public MultiCheckDivAdaptor(ArrayList<MultiCheckDivModel> divchecklist, CheckBox checkBox, RecyclerView recyclerView, Context Context, OnMultiCheckListener onMultiCheckDivListener) {
        this.divchecklist = divchecklist;
        this.checkBox = checkBox;
        this.recyclerView = recyclerView;
        this.Context = Context;
        this.onMultiCheckDivListener = onMultiCheckDivListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dailog_list, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        MultiCheckDivModel multiCheckDivModel = divchecklist.get(i);
        myViewHolder.tv_name.setText(multiCheckDivModel.getStringName());
        myViewHolder.ch_checkbox.setOnCheckedChangeListener(null);
        myViewHolder.ch_checkbox.setChecked(multiCheckDivModel.isChecked());

        if (Dcrdatas.productFrom.equals("1")) {
            if (Dcrdatas.isselected == true) {
                myViewHolder.ch_checkbox.setChecked(true);
                divchecklist.get(i).setChecked(true);
                recyclerView.smoothScrollToPosition(divchecklist.size() - 1);
                if (multiCheckDivModel.isChecked() == true) {
                    onMultiCheckDivListener.OnMultiCheckDivListener_Add(multiCheckDivModel);
                } else {
                    onMultiCheckDivListener.OnMultiCheckDivListener_Remove(multiCheckDivModel);
                }
            } else {
                myViewHolder.ch_checkbox.setChecked(false);
                multiCheckDivModel.setChecked(false);
                if (multiCheckDivModel.isChecked() == true) {
                    onMultiCheckDivListener.OnMultiCheckDivListener_Add(multiCheckDivModel);
                } else {
                    onMultiCheckDivListener.OnMultiCheckDivListener_Remove(multiCheckDivModel);
                }
            }
        }

        myViewHolder.ch_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBox.setChecked(false);
                Dcrdatas.checkin=1;
                Dcrdatas.productFrom="0";
//                Dcrdatas.IsSelectAll=0;
                multiCheckDivModel.setChecked(isChecked);
                if (isChecked) {
                    onMultiCheckDivListener.OnMultiCheckDivListener_Add(multiCheckDivModel);
                } else {
                    onMultiCheckDivListener.OnMultiCheckDivListener_Remove(multiCheckDivModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return divchecklist.size();
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
