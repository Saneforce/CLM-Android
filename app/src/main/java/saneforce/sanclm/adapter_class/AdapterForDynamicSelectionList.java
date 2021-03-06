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
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.DemoActivity;
import saneforce.sanclm.activities.Model.PopFeed;

public class AdapterForDynamicSelectionList extends BaseAdapter {
    Context context;
    ArrayList<PopFeed>  array=new ArrayList<>();
    ArrayList<PopFeed> dummyList=new ArrayList<>();
    int type=0;

    public AdapterForDynamicSelectionList(Context context, ArrayList<PopFeed> array,int type) {
        this.context = context;
        this.array = array;
        this.type=type;
        this.dummyList.addAll(array);
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
    public View getView(final int i, View convertView, ViewGroup parent) {
        View    view= LayoutInflater.from(context).inflate(R.layout.row_item_selection_list_tp,parent,false);
        RelativeLayout lay_row=(RelativeLayout)view.findViewById(R.id.lay_row);
        TextView txt_name=(TextView)view.findViewById(R.id.txt_name);
        final CheckBox check=(CheckBox)view.findViewById(R.id.check);
        check.setChecked(array.get(i).isClick());
        txt_name.setText(array.get(i).getTxt());

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(type==0){
                    for(int k=0;k<array.size();k++){
                        if(i!=k){
                            array.get(k).setClick(false);
                        }
                        else {

                           /* AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                            animation1.setDuration(500);
                            animation1.setStartOffset(200);
                            //animation1.setFillAfter(true);
                            check.startAnimation(animation1);*/
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
                            /*AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                            animation1.setDuration(200);
                            animation1.setStartOffset(200);
                            //animation1.setFillAfter(true);
                            check.startAnimation(animation1);*/
                            array.get(i).setClick(true);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });

        lay_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==0){
                    for(int k=0;k<array.size();k++){
                        if(i!=k){
                            array.get(k).setClick(false);
                        }
                        else {

                           /* AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                            animation1.setDuration(500);
                            animation1.setStartOffset(200);
                            //animation1.setFillAfter(true);
                            check.startAnimation(animation1);*/
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
                            /*AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                            animation1.setDuration(200);
                            animation1.setStartOffset(200);
                            //animation1.setFillAfter(true);
                            check.startAnimation(animation1);*/
                            array.get(i).setClick(true);
                        }
                    }
                    notifyDataSetChanged();
                }

            }
        });
        return view;
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {


                FilterResults filterResults = new FilterResults();
                if(charSequence != null && charSequence.length()>0) {
                    List<PopFeed> filteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        dummyList = array;
                    } else {


                        filteredList.clear();
                        for (PopFeed row : dummyList) {
                            if (row.getTxt().toLowerCase().contains(charString.toLowerCase()) || row.getTxt().contains(charSequence)) {
                                Log.v("lowercase_filter", row.getTxt());
                                filteredList.add(row);
                            }

                        }
                      /*  if (filteredList.size() == 0) {
                            filteredList.addAll(DrListFiltered);
                        }*/

                        //DrListFiltered = filteredList;
                    }


                    filterResults.values = filteredList;
                }else
                {
                    // results.count=filterList.size();
                    filterResults.values=dummyList;

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                // Log.e("frr",DrListFiltered.get(0).getmDoctorName());
                array = (ArrayList<PopFeed>) filterResults.values;
                //row=DrListFiltered;
                notifyDataSetChanged();
            }
        };
    }

}
