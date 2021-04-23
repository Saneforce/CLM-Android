package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.DetailingOfReport;
import saneforce.sanclm.activities.ReportOfDetailing;

public class AdapterDetailingReport extends BaseAdapter {
    Context context;
    ArrayList<DetailingOfReport> array=new ArrayList<>();

    public AdapterDetailingReport(Context context, ArrayList<DetailingOfReport> array) {
        this.context = context;
        this.array = array;
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
        view= LayoutInflater.from(context).inflate(R.layout.row_item_detailing_report,viewGroup,false);
        TextView txt_name=view.findViewById(R.id.txt_name);
        TextView txt_field=view.findViewById(R.id.txt_field);
        TextView txt_type=view.findViewById(R.id.txt_type);
        TextView txt_terri=view.findViewById(R.id.txt_terri);
        TextView txt_spec=view.findViewById(R.id.txt_spec);
        TextView txt_cls=view.findViewById(R.id.txt_cls);
        TextView txt_count=view.findViewById(R.id.txt_count);
        TextView txt_dura=view.findViewById(R.id.txt_dura);
        DetailingOfReport mm=array.get(i);
        txt_name.setText(mm.getName());
        txt_field.setText(mm.getFieldperson());
        txt_type.setText(mm.getType());
        txt_terri.setText(mm.getTerritory());
        txt_spec.setText(mm.getSpec());
        txt_cls.setText(mm.getCls());
        txt_count.setText(mm.getCount());
        txt_dura.setText(mm.getTime());
        return view;
    }
}
