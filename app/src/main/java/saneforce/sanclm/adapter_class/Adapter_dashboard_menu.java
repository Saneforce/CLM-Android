package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.LoadBitmap;

public class Adapter_dashboard_menu extends BaseAdapter {
    ArrayList<LoadBitmap> array=new ArrayList<>();
    Context context;
    boolean chkNeed=true;

    public Adapter_dashboard_menu(Context context, ArrayList<LoadBitmap> array,boolean chkNeed){
        this.array=array;
        this.context=context;
        this.chkNeed=chkNeed;

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
        LayoutInflater mViewLayoutInflatar = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=mViewLayoutInflatar.inflate(R.layout.row_item_dash_menu_checkbox, null);
        TextView txt_row=(TextView)convertView.findViewById(R.id.txt_row);
        final CheckBox chck_box=(CheckBox)convertView.findViewById(R.id.chck_box);
        final LoadBitmap mm=array.get(position);
        txt_row.setText(mm.getBrdName());
        if(!chkNeed){
            chck_box.setVisibility(View.INVISIBLE);
        }
        chck_box.setChecked(mm.isCheck());

        chck_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mm.setCheck(isChecked);
                chck_box.setChecked(mm.isCheck());

            }
        });
        return convertView;
    }
}
