package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelNavDrawer;

public class NavigationItemAdapter extends BaseAdapter {
    ArrayList<ModelNavDrawer> array=new ArrayList<>();
    Context context;

    public NavigationItemAdapter(ArrayList<ModelNavDrawer> array, Context context) {
        this.array = array;
        this.context = context;
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
        View vv= LayoutInflater.from(context).inflate(R.layout.row_item_nav_drawer,viewGroup,false);
        TextView item=vv.findViewById(R.id.item);
        ImageView img=vv.findViewById(R.id.img);
        img.setImageResource(array.get(i).getDrawable());
        Log.v("entering_txt",array.get(i).getText());
        item.setText(array.get(i).getText());
        return vv;
    }
}
