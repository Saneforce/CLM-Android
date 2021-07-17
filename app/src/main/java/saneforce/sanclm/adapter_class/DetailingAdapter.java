package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import saneforce.sanclm.Pojo_Class.DetailingList;
import saneforce.sanclm.R;

public class DetailingAdapter extends RecyclerView.Adapter<DetailingAdapter.Viewholder> {
    Context context;
    ArrayList<DetailingList>lists;

    public DetailingAdapter(Context context, ArrayList<DetailingList> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.detailing_time_row, parent, false);
        DetailingAdapter.Viewholder viewHolder = new DetailingAdapter.Viewholder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        if(lists.get(position).getTag().matches("detail")) {
            holder.percenttext.setTextColor(Color.parseColor(lists.get(position).getLblClr()));
            holder.nametxt.setText(lists.get(position).getBrand());
            holder.percenttext.setText(lists.get(position).getPercnt() + "%");
            holder.view.setBackgroundColor(Color.parseColor(lists.get(position).getBarclr()));
            if(lists.get(position).getPercnt().equalsIgnoreCase("0")){
                holder.nametxt.setVisibility(View.GONE);
                holder.percenttext.setVisibility(View.GONE);
                holder.view.setVisibility(View.GONE);
            }
        }else{
            holder.nametxt.setText(lists.get(position).getBrand());
            holder.percenttext.setText(lists.get(position).getPercnt() + "%");
           holder.view.setBackgroundColor(Color.parseColor(lists.get(position).getBarclr()));

        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView nametxt,percenttext;
        View view;
        LinearLayout ln1;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nametxt=itemView.findViewById(R.id.nametext);
            percenttext=itemView.findViewById(R.id.percentagetext);
            view=itemView.findViewById(R.id.viewtext);
            ln1=itemView.findViewById(R.id.ln1);

        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



}
