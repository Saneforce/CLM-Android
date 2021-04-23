package saneforce.sanclm.adapter_class;

import android.content.Context;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelCalendarRow;


public class AdapterRecyclerSchedulerWeek extends RecyclerView.Adapter<AdapterRecyclerSchedulerWeek.MyViewHolder> {
        Context mContext;
        ArrayList<ModelCalendarRow> array=new ArrayList<>();
        public AdapterRecyclerSchedulerWeek(Context mContext, ArrayList<ModelCalendarRow> array) {
            this.mContext = mContext;
            this.array = array;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View vv= LayoutInflater.from(mContext).inflate(R.layout.row_item_week,parent,false);
            return new MyViewHolder(vv);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public  class   MyViewHolder    extends RecyclerView.ViewHolder{
            TextView txt_num;
            TextView    txt_event;
            TextView    txt_mem;
            public MyViewHolder(View vv) {
                super(vv);
               /* txt_num=(TextView)vv.findViewById(R.id.txt_num);
                txt_event=(TextView)vv.findViewById(R.id.txt_event);
                txt_mem=(TextView)vv.findViewById(R.id.txt_mem);*/
            }
        }
    }

