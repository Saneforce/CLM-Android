package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ReportData;

public class AdapterForReport extends BaseAdapter {
    Context context;
    ArrayList<ReportData> array=new ArrayList<>();

    public AdapterForReport(Context context, ArrayList<ReportData> array){
        this.context=context;
        this.array=array;

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
        convertView=inflater.inflate(R.layout.row_item_dashboard,parent,false);
        TextView txt_name=(TextView)convertView.findViewById(R.id.txt_name);
        TextView txt_total=(TextView)convertView.findViewById(R.id.txt_total);
        TextView txt_seen=(TextView)convertView.findViewById(R.id.txt_seen);
        TextView txt_meet=(TextView)convertView.findViewById(R.id.txt_meet);
        TextView txt_miss=(TextView)convertView.findViewById(R.id.txt_miss);
        ReportData dd=array.get(position);
        txt_name.setText(dd.getName());
        txt_total.setText("Total Visit : "+dd.getTotal_dr());
        txt_seen.setText(dd.getSeen());
        txt_meet.setText(dd.getMeet());
        txt_miss.setText(dd.getMissed());
        return convertView;
    }
}
