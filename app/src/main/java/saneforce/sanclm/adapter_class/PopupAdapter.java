
package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.PopFeed;

public class PopupAdapter extends BaseAdapter {

    Context context;
    ArrayList<PopFeed> arrayList=new ArrayList<>();
    ArrayList<PopFeed> dummyList=new ArrayList<>();
    boolean feedlist=false;
    String value;
    String[] arr;
    ArrayList<Integer> posArr=new ArrayList<>();
    public PopupAdapter(Context context,ArrayList<PopFeed> arrayList){
        this.context=context;
        this.arrayList=arrayList;
        this.dummyList.addAll(arrayList);
    }
    public PopupAdapter(Context context,ArrayList<PopFeed> arrayList,String value){
        this.context=context;
        this.arrayList=arrayList;
        this.value=value;
        String[] arr1=this.value.split(",");
        for(int i=0;i<arr1.length;i++){
            posArr.add(Integer.valueOf(arr1[i]));
        }
        Log.v("total_size_array",this.arrayList.size()+"");
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }




    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view= LayoutInflater.from(context).inflate(R.layout.row_item_popup_feed,viewGroup,false);

        RelativeLayout rl_lay=(RelativeLayout)view.findViewById(R.id.rl_lay);
        final ImageView tick=(ImageView)view.findViewById(R.id.tick);
        TextView txt_content=(TextView)view.findViewById(R.id.txt_content);

        final PopFeed popFeed=arrayList.get(i);
        if(posArr.size()!=0 && posArr.contains(i))
            rl_lay.setBackgroundColor(Color.GRAY);
        txt_content.setText(popFeed.getTxt());

            if (popFeed.isClick()) {
                popFeed.setClick(true);
                tick.setVisibility(View.VISIBLE);
            } else {
                popFeed.setClick(false);
                tick.setVisibility(View.INVISIBLE);
            }

            rl_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("rl_lay_ja", "are_clicked");


                        if (popFeed.isClick()) {
                            popFeed.setClick(false);
                            tick.setVisibility(View.INVISIBLE);
                        } else {
                            popFeed.setClick(true);
                            tick.setVisibility(View.VISIBLE);
                        }
                    if(posArr.size()!=0 && posArr.contains(i)) {
                        popFeed.setClick(false);
                        tick.setVisibility(View.INVISIBLE);
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
                        dummyList = arrayList;
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
                arrayList = (ArrayList<PopFeed>) filterResults.values;
                //row=DrListFiltered;
                notifyDataSetChanged();
            }
        };
    }


}

