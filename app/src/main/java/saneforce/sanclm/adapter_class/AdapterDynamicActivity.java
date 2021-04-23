package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.graphics.Color;
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
import saneforce.sanclm.util.ProductChangeListener;
import saneforce.sanclm.util.SpecialityListener;

public class AdapterDynamicActivity extends BaseAdapter {
    Context context;
    ArrayList<ModelDynamicList>   array=new ArrayList<>();
    ArrayList<ModelDynamicList>   dummyList=new ArrayList<>();
    static SpecialityListener specialityListener;
    String latitude,longitude;


    public AdapterDynamicActivity(Context context,ArrayList<ModelDynamicList>   array){
        this.context=context;
        this.array=array;
        this.latitude=latitude;
        this.longitude=longitude;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View  ll=LayoutInflater.from(context).inflate(R.layout.row_item_textview,parent,false);
        TextView    txt_name=ll.findViewById(R.id.txt_name);
        final LinearLayout llay_head=ll.findViewById(R.id.llay_head);
        txt_name.setText(array.get(position).getName());
        if(array.get(position).isClick()){
            llay_head.setBackgroundColor(Color.parseColor("#EDECEC"));
        }
        else
        llay_head.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llay_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("printing_here_dynmaci",array.get(position).getName()+"pos"+position+"id"+array.get(position).getId());
                for(int i=0;i<array.size();i++){
                    array.get(i).setClick(false);
                }
                array.get(position).setClick(true);

                notifyDataSetChanged();
                specialityListener.specialityCode(array.get(position).getId());
            }
        });
        return ll;
    }
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {


                FilterResults filterResults = new FilterResults();
                if(charSequence != null && charSequence.length()>0) {
                    List<ModelDynamicList> filteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        dummyList = array;
                    } else {


                        filteredList.clear();
                        for (ModelDynamicList row : dummyList) {
                            if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                                Log.v("lowercase_filter", row.getName());
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
                array = (ArrayList<ModelDynamicList>) filterResults.values;
                //row=DrListFiltered;
                notifyDataSetChanged();
            }
        };
    }

    public static void  bindDynamicListListner(SpecialityListener   sp ){
        specialityListener=sp;
    }
}
