package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.SlideDetail;

public class AdapterPopupSpinnerSelection extends BaseAdapter {

    Context context;
    ArrayList<SlideDetail> list=new ArrayList<>();
    ArrayList<String> arrayList=new ArrayList<>();
    boolean single=false;

    public AdapterPopupSpinnerSelection(Context context, ArrayList<SlideDetail> list) {
        this.context = context;
        this.list = list;
        single=false;
    }
    public AdapterPopupSpinnerSelection(Context context, ArrayList<String> arrayList,boolean x) {
        this.context = context;
        this.arrayList = arrayList;
        this.single=x;
    }

    @Override
    public int getCount() {
        if(single)
            return arrayList.size();
        else
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        if(single)
            return arrayList.get(i);
        else
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.row_item_textview,viewGroup,false);
        TextView textView=(TextView)view.findViewById(R.id.txt_name);
        if(single){
            textView.setText(arrayList.get(i));
        }
        else {
            textView.setText(list.get(i).getInputName());
        }
        return view;
    }
}
