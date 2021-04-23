package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ChatModel;

public class AdapterForChat extends BaseAdapter {

    ArrayList<ChatModel> array=new ArrayList<>();
    Context context;

    public AdapterForChat(Context context, ArrayList<ChatModel> array){
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
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_notification,parent,false);
        TextView txtFrom=(TextView)convertView.findViewById(R.id.txtFrom);
        TextView txtUs=(TextView)convertView.findViewById(R.id.txtUs);
        TextView txt_fromdate=(TextView)convertView.findViewById(R.id.txt_fromdate);
        TextView txt_usdate=(TextView)convertView.findViewById(R.id.txt_usdate);
        TextView txt_date=(TextView)convertView.findViewById(R.id.txt_date);
        ChatModel mm=array.get(position);

        if(!mm.getDate().equalsIgnoreCase("")){
            txt_date.setVisibility(View.VISIBLE);
            txt_date.setText(mm.getDate());
        }
        else
            txt_date.setVisibility(View.GONE);

        if(mm.isFromWhere()){
            txtUs.setVisibility(View.VISIBLE);
            txt_usdate.setVisibility(View.VISIBLE);
            txtFrom.setVisibility(View.GONE);
            txt_fromdate.setVisibility(View.GONE);
            txtUs.setText(mm.getMsg());
        }
        else{
            txtUs.setVisibility(View.GONE);
            txtFrom.setVisibility(View.VISIBLE);
            txtFrom.setText(mm.getMsg());
            txt_usdate.setVisibility(View.GONE);
            txt_fromdate.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
