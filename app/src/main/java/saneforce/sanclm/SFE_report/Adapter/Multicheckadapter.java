package saneforce.sanclm.SFE_report.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.R;
import saneforce.sanclm.SFE_report.ModelClass.Multicheckclass;
import saneforce.sanclm.SFE_report.ModelClass.OnCampaignClicklistener;

public class Multicheckadapter extends RecyclerView.Adapter<Multicheckadapter.MyViewHolder>{
    public static ArrayList<Multicheckclass> checklist;
    private Context context;
    private OnCampaignClicklistener onCampclicklistener;
    boolean[] checkBoxState;
    CheckBox ch1;
    RecyclerView rcy1;
    boolean isChecked1;

    public Multicheckadapter(ArrayList<Multicheckclass> checklist,CheckBox ch1,RecyclerView rcy1, Context context, OnCampaignClicklistener onCampclicklistener) {
        this.checklist = checklist;
        this.context = context;
        this.ch1=ch1;
        this.rcy1=rcy1;
        this.onCampclicklistener = onCampclicklistener;
        checkBoxState = new boolean[checklist.size()];
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dailog_list, viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final Multicheckclass mul=checklist.get(i);
        myViewHolder.tv_name.setText(mul.getStrname());

        myViewHolder.ch_checkbox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        myViewHolder.ch_checkbox.setChecked(mul.isChecked());

        Log.d("dataas", String.valueOf(Dcrdatas.isselected) + Dcrdatas.productFrom);

        if (Dcrdatas.productFrom.equals("1")){
            if (Dcrdatas.isselected==true){
                myViewHolder.ch_checkbox.setChecked(true);
                checklist.get(i).setChecked(true);
                rcy1.smoothScrollToPosition(checklist.size()-1);
                if (mul.isChecked()==true) {
                    onCampclicklistener.classCampaignItem_addClass(mul);
                } else {
                    onCampclicklistener.classCampaignItem_removeClass(mul);
                }
            }else {
                myViewHolder.ch_checkbox.setChecked(false);
                mul.setChecked(false);
                if (mul.isChecked()==true) {
                    onCampclicklistener.classCampaignItem_addClass(mul);
                } else {
                    onCampclicklistener.classCampaignItem_removeClass(mul);
                }
            }
        }

        myViewHolder.ch_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ch1.setChecked(false);
                Dcrdatas.checkin=1;
                Dcrdatas.productFrom="0";
                mul.setChecked(isChecked);
                if (isChecked) {
                    onCampclicklistener.classCampaignItem_addClass(mul);
                } else {
                    onCampclicklistener.classCampaignItem_removeClass(mul);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return checklist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        CheckBox ch_checkbox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = ( TextView ) itemView.findViewById(R.id.txtName_mulcheck);
            ch_checkbox= ( CheckBox ) itemView.findViewById(R.id.checkBox_mulcheck);

        }
    }

    public void selectAll() {
        for (int i = 0; i < checkBoxState.length; i++) {
            checkBoxState[i] = true;
        }
        notifyDataSetChanged();
    }

    public void unselectAll() {
        for (int i = 0; i < checkBoxState.length; i++) {
            checkBoxState[i] = false;
        }
        notifyDataSetChanged();
    }
}
