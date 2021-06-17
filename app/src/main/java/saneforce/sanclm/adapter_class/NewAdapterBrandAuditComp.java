package saneforce.sanclm.adapter_class;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.BrandAuditActivity;
import saneforce.sanclm.activities.CompNameProductNew;
import saneforce.sanclm.activities.Model.CompNameProduct;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.util.DataInterface;
import saneforce.sanclm.util.UpdateUi;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class NewAdapterBrandAuditComp extends BaseAdapter {
    ArrayList<CompNameProductNew> list_prd = new ArrayList<>();
    Context context;


    public NewAdapterBrandAuditComp(Activity context, ArrayList<CompNameProductNew> list_prd) {
        this.context = context;
        this.list_prd = list_prd;
    }

    @Override
    public int getCount() {
        return list_prd.size();
    }

    @Override
    public Object getItem(int i) {
        return list_prd.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.new_row_item_brand_audit_competor, viewGroup, false);
        TextView txt_comp_name = (TextView) view.findViewById(R.id.txt_comp_name);
        final TextView txt_comp_brd_name = (TextView) view.findViewById(R.id.txt_comp_brd_name);
        final TextView txt_rate = (TextView) view.findViewById(R.id.txt_rate);
        final TextView txt_value = (TextView) view.findViewById(R.id.txt_value);
        RelativeLayout comp_name_list = (RelativeLayout) view.findViewById(R.id.comp_name_list);
        RelativeLayout comp_prd_list = (RelativeLayout) view.findViewById(R.id.comp_prd_list);
        final EditText edt_qty = (EditText) view.findViewById(R.id.edt_qty);
        final LinearLayout ll_qty = (LinearLayout) view.findViewById(R.id.ll_qty);
        Button feedbackbtn = view.findViewById(R.id.feedbackbtn);

        txt_comp_name.setText(list_prd.get(i).getCName());
        txt_comp_brd_name.setText(list_prd.get(i).getPName());

        feedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.alert_feedback_layout);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                Button savebtn = dialog.findViewById(R.id.savebtn);
                ImageView closebtn = dialog.findViewById(R.id.iv_close_popup);
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        return view;
    }
}