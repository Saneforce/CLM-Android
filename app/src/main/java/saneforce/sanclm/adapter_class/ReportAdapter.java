package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.LoadBitmap;

public class ReportAdapter extends BaseAdapter {

    Context context;
    ArrayList<LoadBitmap> list=new ArrayList<>();
    public ReportAdapter(Context context, ArrayList<LoadBitmap> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view= LayoutInflater.from(context).inflate(R.layout.row_item_report,viewGroup,false);
        TextView text_menu=(TextView)view.findViewById(R.id.text_menu);
        LoadBitmap mm=list.get(i);
        if(mm.getBrdName().equalsIgnoreCase("Dashboard"))
        {
            text_menu.setText(context.getResources().getString(R.string.Dashboard));
        }
        else
        {
            text_menu.setText(mm.getBrdName());
        }

        return view;
    }
}
