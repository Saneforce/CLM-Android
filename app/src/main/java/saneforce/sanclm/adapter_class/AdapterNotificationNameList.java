package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;

public class AdapterNotificationNameList extends BaseAdapter {
    ArrayList<String> array=new ArrayList<>();
    Context context;

    public AdapterNotificationNameList(Context context, ArrayList<String> array){
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
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_notify_name_list,parent,false);
        TextView textView=(TextView)convertView.findViewById(R.id.txt_name);
        textView.setText(array.get(position));
        return convertView;
    }
}
