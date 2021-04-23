package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelDynamicList;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.util.DCRCallSelectionFilter;

public class AdapterForCluster  extends BaseAdapter {
    Context context;
    ArrayList<PopFeed>  arrayList=new ArrayList<>();
    ArrayList<PopFeed>   dummyList=new ArrayList<>();
    static  DCRCallSelectionFilter  dcrCallSelectionFilter;

    public AdapterForCluster(Context context, ArrayList<PopFeed> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.dummyList.addAll(arrayList);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.row_item_textview,parent,false);
        TextView    txt_name=(TextView)convertView.findViewById(R.id.txt_name);
        LinearLayout    llay_head=(LinearLayout)convertView.findViewById(R.id.llay_head);
        final PopFeed mm=arrayList.get(position);
        llay_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dcrCallSelectionFilter.itemClick(mm.getTxt(),mm.getCode());
            }
        });

        txt_name.setText(mm.getTxt());
        return convertView;
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

    public static   void    bindCluster(DCRCallSelectionFilter dcr){
        dcrCallSelectionFilter=dcr;
    }

}
