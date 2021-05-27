package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.percenttext.setTextColor(Color.parseColor(lists.get(position).getLblClr()));
        holder.nametxt.setText(lists.get(position).getBrand());
        holder.percenttext.setText(lists.get(position).getPercnt()+"%");
        holder.view.setBackgroundColor(Color.parseColor(lists.get(position).getBarclr()));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView nametxt,percenttext;
        View view;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nametxt=itemView.findViewById(R.id.nametext);
            percenttext=itemView.findViewById(R.id.percentagetext);
            view=itemView.findViewById(R.id.viewtext);

        }
    }
}
