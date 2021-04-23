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

public class LeaveAdapter extends BaseAdapter {

    Context context;
    ArrayList<LoadBitmap> list = new ArrayList<>();

    public LeaveAdapter(Context context, ArrayList<LoadBitmap> list) {
        this.context = context;
        this.list = list;
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

        view = LayoutInflater.from(context).inflate(R.layout.row_item_leave_type, viewGroup, false);
        TextView value = (TextView) view.findViewById(R.id.value);
        TextView label = (TextView) view.findViewById(R.id.label);
        LoadBitmap mm = list.get(i);

        System.out.println("Adapter_values" + list.get(i).getTiming());
        value.setText(mm.getTiming());
        label.setText(mm.getBrdName());


        return view;
    }
}
