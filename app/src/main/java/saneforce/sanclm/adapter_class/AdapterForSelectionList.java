package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.DemoActivity;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;

public class AdapterForSelectionList extends BaseAdapter {
    Context context;
    ArrayList<PopFeed> array=new ArrayList<>();
    String selectcategory;
    CommonSharedPreference commonSharedPreference;

    public AdapterForSelectionList(Context context, ArrayList<PopFeed> array,String selector){
        this.context=context;
        this.array=array;
        selectcategory=selector;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.row_item_selection_list_tp,viewGroup,false);
        TextView txt_name=(TextView)view.findViewById(R.id.txt_name);
        RelativeLayout lay_row=(RelativeLayout)view.findViewById(R.id.lay_row);
        final CheckBox check=(CheckBox)view.findViewById(R.id.check);
        Log.v("selector_check",selectcategory+" txt"+array.get(i).getTxt()+array.get(i).isClick());
        commonSharedPreference=new CommonSharedPreference(context);
        txt_name.setText(array.get(i).getTxt());
       /* if(array.get(i).isClick()){
            AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
            animation1.setDuration(200);
            animation1.setStartOffset(200);
            //animation1.setFillAfter(true);
            check.startAnimation(animation1);
        }*/
        check.setChecked(array.get(i).isClick());


        lay_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectcategory.equalsIgnoreCase("w") || selectcategory.equalsIgnoreCase("h") || (DemoActivity.cltyp.equalsIgnoreCase("1") && selectcategory.equalsIgnoreCase("c"))){
                    for(int k=0;k<array.size();k++){
                        if(i!=k){
                            array.get(k).setClick(false);
                        }
                        else {

                            AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                            animation1.setDuration(500);
                            animation1.setStartOffset(200);
                            //animation1.setFillAfter(true);
                            check.startAnimation(animation1);
                            array.get(k).setClick(true);
                            check.setChecked(true);
                        }
                    }
                    notifyDataSetChanged();
                }
                else {
                    if(array.size()!=0) {
                        if (array.get(i).isClick()) {
                            check.setChecked(false);
                            array.get(i).setClick(false);
                        } else {

                            check.setChecked(true);
                            AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                            animation1.setDuration(200);
                            animation1.setStartOffset(200);
                            //animation1.setFillAfter(true);
                            check.startAnimation(animation1);
                            array.get(i).setClick(true);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(selectcategory.equalsIgnoreCase("w") || selectcategory.equalsIgnoreCase("h") ||  (DemoActivity.cltyp.equalsIgnoreCase("1") && selectcategory.equalsIgnoreCase("c"))){
                    Log.v("selectcategory","cluster");
                    for(int k=0;k<array.size();k++){
                        if(i!=k){
                            array.get(k).setClick(false);
                        }
                        else {

                            AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                            animation1.setDuration(200);
                            animation1.setStartOffset(200);
                            //animation1.setFillAfter(true);
                            check.startAnimation(animation1);
                            array.get(k).setClick(true);
                            check.setChecked(true);
                        }
                    }
                    notifyDataSetChanged();
                }else {
                    if (b) {
                        Log.v("selectcategory","hospital");
                        check.setChecked(true);
                        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                        animation1.setDuration(200);
                        animation1.setStartOffset(200);
                        //animation1.setFillAfter(true);
                        check.startAnimation(animation1);
                        array.get(i).setClick(true);
                    } else {
                        Log.v("selectcategory1","hospital");
                        check.setChecked(false);
                        array.get(i).setClick(false);
                    }
                    notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
