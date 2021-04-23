package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ExpenseModel;

public class AdapterForExpense extends BaseAdapter {
    Context context;
    ArrayList<ExpenseModel> array=new ArrayList<>();

    public AdapterForExpense(Context context, ArrayList<ExpenseModel> array){
        this.context=context;
        this.array=array;
        Log.v("convertview_inadpt","position"+array.size());

    }
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.row_item_expense,parent,false);
Log.v("convertview_inadpt","position");
        TextView txt_date=(TextView)convertView.findViewById(R.id.txt_date);
        TextView txt_day=(TextView)convertView.findViewById(R.id.txt_day);
        TextView txt_total_add=(TextView)convertView.findViewById(R.id.txt_total_add);
        TextView txt_fare_amt=(TextView)convertView.findViewById(R.id.txt_fare_amt);
        TextView txt_distance=(TextView)convertView.findViewById(R.id.txt_distance);
        TextView txt_tot_fare_amt=(TextView)convertView.findViewById(R.id.txt_tot_fare_amt);
        TextView txt_tot_allowance_amt=(TextView)convertView.findViewById(R.id.txt_tot_allowance_amt);
        TextView txt_final_amt=(TextView)convertView.findViewById(R.id.txt_final_amt);

        ExpenseModel mm=array.get(position);
        txt_date.setText(mm.getDate());
        txt_day.setText(mm.getDay());
        txt_total_add.setText(mm.getPlace());
        txt_fare_amt.setText(mm.getFare());
        txt_distance.setText(mm.getDistance());
        txt_tot_fare_amt.setText(mm.getFare());
        txt_tot_allowance_amt.setText(mm.getAllowance());
        txt_final_amt.setText(mm.getTotal());
       /*
        ReportData dd=array.get(position);
        txt_name.setText(dd.getName());
        txt_total.setText("Total Visit : "+dd.getTotal_dr());
        txt_seen.setText(dd.getSeen());
        txt_meet.setText(dd.getMeet());
        txt_miss.setText(dd.getMissed());*/
        return convertView;
    }
}
