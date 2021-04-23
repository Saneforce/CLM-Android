package saneforce.sanclm.adapter_class;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ExpenseModel;

public class ExpenseEntryAdapter extends BaseAdapter {
    Context context;
    ArrayList<ExpenseModel> array=new ArrayList<>();
    ArrayList<ExpenseModel> arra;
    String expres;
    TextView txt_resource_val;

    public ExpenseEntryAdapter(Context context, ArrayList<ExpenseModel> array,String expres){
        this.context=context;
        this.array=array;
        this.expres=expres;
        Log.v("printing_expensed",expres+"kkkk");
    }
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.row_item_expense_entry,viewGroup,false);
        final EditText edt_amt=(EditText)view.findViewById(R.id.edt_amt);
        txt_resource_val=(TextView)view.findViewById(R.id.txt_resource_val);
        final ExpenseModel mm=array.get(i);
        edt_amt.setText(mm.getTotal());
        if(!TextUtils.isEmpty(mm.getPlace()))
        txt_resource_val.setText(mm.getPlace());
        edt_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int val= Integer.parseInt(editable.toString());
                if(mm.getDistance().equalsIgnoreCase("L")) {
                    if (val <= Integer.parseInt(mm.getFare()))
                        mm.setTotal(editable.toString());
                    else{
                        edt_amt.setText(mm.getTotal());
                        edt_amt.setSelection(mm.getTotal().length());
                        Log.v("error_msg_in", String.valueOf(editable));
                    }

                }
                else
                    mm.setTotal(editable.toString());
            }
        });
        txt_resource_val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(expres))
                    Toast.makeText(context,"No Resource Available",Toast.LENGTH_SHORT).show();
                    else
                popup(mm,view);
            }
        });
        return view;
    }

    public void popup(final ExpenseModel exps, final View v){
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.row_item_exp_res);
        dialog.show();
        arra=new ArrayList<>();

        try {
            JSONArray jsonArray=new JSONArray(expres);
            for(int j=0;j<jsonArray.length();j++){
                JSONObject jjss=jsonArray.getJSONObject(j);
                Log.v("expense_wrld",jjss.getString("Expense_Parameter_Name")+" type "+jjss.getString("Param_type")+" price "+jjss.getString("Fixed_Amount"));
                arra.add(new ExpenseModel(jjss.getString("Expense_Parameter_Name"),jjss.getString("Expense_Parameter_Code"),jjss.getString("Param_type"),jjss.getString("Fixed_Amount")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListView list_item=(ListView)dialog.findViewById(R.id.list_item);
        list_item.setDividerHeight(1);
        ExpenseResourceAdapter exp=new ExpenseResourceAdapter(context,arra);
        list_item.setAdapter(exp);
        exp.notifyDataSetChanged();

        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                txt_resource_val=(TextView)v.findViewById(R.id.txt_resource_val);
                txt_resource_val.setText(arra.get(i).getPlace());
                exps.setPlace(arra.get(i).getPlace());
                exps.setDay(arra.get(i).getDay());
                exps.setDistance(arra.get(i).getDistance());
                exps.setFare(arra.get(i).getFare());
                dialog.cancel();
            }

        });


    }

    public class ExpenseResourceAdapter extends BaseAdapter {
        Context context;
        ArrayList<ExpenseModel> array_res = new ArrayList<>();
        String expres;

        public ExpenseResourceAdapter(Context context,ArrayList<ExpenseModel> array_res) {
            this.context = context;
            this.array_res = array_res;

        }

        @Override
        public int getCount() {
            return array_res.size();
        }

        @Override
        public Object getItem(int i) {
            return array_res.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_item_selection_list_tp, viewGroup, false);
            CheckBox check=(CheckBox)view.findViewById(R.id.check);
            check.setVisibility(View.GONE);
            ExpenseModel pp=array_res.get(i);
            TextView txt_name=(TextView)view.findViewById(R.id.txt_name);
            txt_name.setTextSize(16);
            txt_name.setText(pp.getPlace());

            return view;
        }
    }
}