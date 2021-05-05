package saneforce.sanclm.adapter_class;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import saneforce.sanclm.activities.Model.CompNameProduct;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.util.DataInterface;
import saneforce.sanclm.util.UpdateUi;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class NewAdapterBrandAuditComp extends BaseAdapter {
    ArrayList<String> list=new ArrayList<>();
    ArrayList<CompNameProduct> list_prd=new ArrayList<>();
    public static ArrayList<CompNameProduct> full_list_prd=new ArrayList<>();
    Context context;
    boolean detectPrdClick=false;
    TextView txt_comp_name;
    ArrayList<String> arraylist=new ArrayList<>();
    CommonUtilsMethods commonUtilsMethods;
    static UpdateUi updateUi;
    DataInterface dataInterface;


    public NewAdapterBrandAuditComp(ArrayList<CompNameProduct> list, Activity context, ArrayList<CompNameProduct> list_prd, DataInterface dataInterface) {
        this.full_list_prd = list;
        this.context = context;
        this.list_prd=list_prd;
        this.dataInterface=dataInterface;
        commonUtilsMethods=new CommonUtilsMethods(this.context);
    }

    @Override
    public int getCount() {
        return full_list_prd.size();
    }

    @Override
    public Object getItem(int i) {
        return full_list_prd.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.new_row_item_brand_audit_competor,viewGroup,false);
        txt_comp_name=(TextView)view.findViewById(R.id.txt_comp_name);
        final TextView txt_comp_brd_name=(TextView)view.findViewById(R.id.txt_comp_brd_name);
        final TextView txt_rate=(TextView)view.findViewById(R.id.txt_rate);
        final TextView txt_value=(TextView)view.findViewById(R.id.txt_value);
        RelativeLayout comp_name_list=(RelativeLayout)view.findViewById(R.id.comp_name_list);
        RelativeLayout comp_prd_list=(RelativeLayout)view.findViewById(R.id.comp_prd_list);
        final EditText edt_qty=(EditText)view.findViewById(R.id.edt_qty);
        final LinearLayout ll_qty=(LinearLayout)view.findViewById(R.id.ll_qty);
        Button feedbackbtn=view.findViewById(R.id.feedbackbtn);
        feedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(context);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.alert_feedback_layout);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                Button savebtn=dialog.findViewById(R.id.savebtn);
                ImageView closebtn=dialog.findViewById(R.id.iv_close_popup);
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        final CompNameProduct mm=full_list_prd.get(i);
        if(!TextUtils.isEmpty(mm.getCompName())){
            txt_comp_name.setVisibility(View.VISIBLE);
            txt_comp_name.setText(mm.getCompName());


        }else{
            txt_comp_name.setVisibility(View.INVISIBLE);

        }
        if(!TextUtils.isEmpty(mm.getChoosenPrdName())){
            txt_comp_brd_name.setVisibility(View.VISIBLE);
            txt_comp_brd_name.setText(mm.getChoosenPrdName());

        }else{
            txt_comp_brd_name.setVisibility(View.INVISIBLE);
        }
        if(!TextUtils.isEmpty(mm.getQty())){
            edt_qty.setText(mm.getQty());
            txt_rate.setText(mm.getRate());
            txt_value.setText(mm.getValue());

        }
//        if(!TextUtils.isEmpty(mm.getCompName()))
//        {
//            if(!TextUtils.isEmpty(mm.getChoosenPrdName()))
//            {
//                if(!TextUtils.isEmpty(mm.getQty()))
//                {
//                    dataInterface.competitordetails(mm.getCompName(),mm.getChoosenPrdName(), mm.getQty());
//                }
//            }
//        }

       dataInterface.competitordetails(mm.getCompName(),mm.getChoosenPrdName(), mm.getQty());
        comp_name_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mm.getCompName())){
                    mm.setChoosenPrdName("");
                }
                detectPrdClick=false;
                Log.v("arrayss_as_list","clicked_here");
                popUpAlert(list_prd,"n",i);
                notifyDataSetChanged();
            }
        });
        comp_prd_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectPrdClick=true;
                popUpAlert(list_prd,"y",i);
            }
        });


        edt_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              //  updateUi.updatingui();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String val= String.valueOf(editable);
                int qtyVal= Integer.parseInt(val);

                Log.v("qtyValPring", String.valueOf(qtyVal));
                if(!TextUtils.isEmpty(edt_qty.getText().toString())){

                    float cal= qtyVal;
                    cal=cal*BrandAuditActivity.prd_rate;
                    mm.setQty(String.valueOf(qtyVal));
                    mm.setRate(String.valueOf(BrandAuditActivity.prd_rate));
                    mm.setValue(String.valueOf(cal));
                    txt_rate.setText(String.valueOf(BrandAuditActivity.prd_rate));
                    txt_value.setText(String.valueOf(cal));

                }
            }
        });

/*
        edt_qty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch(result) {
                    case EditorInfo.IME_ACTION_DONE:
                        // done stuff
                        Log.v("action_done","are_clicked");
                        if(!TextUtils.isEmpty(edt_qty.getText().toString())){

                            float cal= Float.parseFloat(edt_qty.getText().toString());
                            cal=cal*BrandAuditActivity.prd_rate;
                            mm.setQty(edt_qty.getText().toString());
                            mm.setRate(String.valueOf(BrandAuditActivity.prd_rate));
                            mm.setValue(String.valueOf(cal));
                            notifyDataSetChanged();

                        }
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        // next stuff
                        break;
                }
                return false;
            }
        });
*/

        ll_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(edt_qty ,
                        InputMethodManager.SHOW_IMPLICIT);
            }
        });


        return view;
    }

    public void popUpAlert(final ArrayList<CompNameProduct> list, final String c, final int pos){

        final Dialog dialog=new Dialog(context,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_spinner_selection);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ListView popup_list=(ListView)dialog.findViewById(R.id.popup_list);
        ImageView close_img=(ImageView)dialog.findViewById(R.id.close_img);
        arraylist.clear();
        if(c.equals("n")){
            for(int i=0;i<list.size();i++){
                arraylist.add(list.get(i).getCompName());

            }
        }

        else{
            CompNameProduct mm=full_list_prd.get(pos);
            String s1=mm.getCompPrd();
            String[] words=s1.split("/");
            ArrayList<String> stringArrayList=new ArrayList<>(Arrays.asList(words));
           // arraylist=new ArrayList<>(Arrays.asList(words));

            for(int i=0;i<stringArrayList.size();i++){

                System.out.println("stringArrayList"+stringArrayList.get(i));
                arraylist.add(stringArrayList.get(i));

            }
        }
        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //updateUi.updatingui();
            }
        });
        AdapterPopupSpinnerSelection popupAdapter=new AdapterPopupSpinnerSelection(context,arraylist,true);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!detectPrdClick) {
                    CompNameProduct mm = list_prd.get(i);

                    CompNameProduct mm1 = full_list_prd.get(pos);
                    mm1.setCompPrd(mm.getCompPrd());
                    mm1.setCompName(mm.getCompName());
                    mm1.setCompCode(mm.getCompCode());
                    mm1.setCompPCode(mm.getCompPCode());
                    notifyDataSetChanged();
                }
                else{
                    CompNameProduct mm1 = full_list_prd.get(pos);
                    mm1.setChoosenPrdName(arraylist.get(i));
                    notifyDataSetChanged();
                }
                dialog.dismiss();
               // updateUi.updatingui();
            }
        });

    }

    public static void bindListenerBrand(UpdateUi up){
        updateUi=up;
    }

}
