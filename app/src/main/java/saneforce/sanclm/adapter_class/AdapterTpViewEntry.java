package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.DemoActivity;
import saneforce.sanclm.activities.Model.ModelTpSave;

public class AdapterTpViewEntry extends BaseAdapter {
    Context context;
    ArrayList<ModelTpSave> array=new ArrayList<>();

    public AdapterTpViewEntry(Context context, ArrayList<ModelTpSave> array) {
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
        view= LayoutInflater.from(context).inflate(R.layout.row_item_view_tp,viewGroup,false);
        TextView txt_session=(TextView)view.findViewById(R.id.txt_session);
        TextView txt_wrk=(TextView)view.findViewById(R.id.txt_wrk);
        TextView txt_hq=(TextView)view.findViewById(R.id.txt_hq);
        TextView txt_cluster=(TextView)view.findViewById(R.id.txt_cluster);
        TextView txt_head_c=(TextView)view.findViewById(R.id.txt_head_c);
        ModelTpSave tp=array.get(i);
        txt_session.setText(/*"Session "*/ context.getResources().getString(R.string.session) +(i+1));
        txt_wrk.setText(extractValues(tp.getWrk()));
        txt_hq.setText(extractValues(tp.getHq()));
        if(DemoActivity.hospNeed.equalsIgnoreCase("0")) {
            txt_head_c.setText(/*"Hospital"*/ context.getResources().getString(R.string.hospital));
            txt_cluster.setText(extractValues(tp.getHosp()));
        }
        else
        txt_cluster.setText(extractValues(tp.getCluster()));
        return view;
    }

    public String extractValues(String s){
        if(TextUtils.isEmpty(s))
            return "";
        Log.v("printing_str_adpt",s+" value "+s.indexOf("$")+"");
        String ss="";
        ss=s.substring(0,s.indexOf("$"));
        return ss;
    }
}
