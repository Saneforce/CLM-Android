package saneforce.sanclm.adapter_class;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import saneforce.sanclm.Pojo_Class.DoctorcoverageList;
import saneforce.sanclm.R;

public class DoctorcoverageAdapter extends RecyclerView.Adapter<DoctorcoverageAdapter.ViewHolder>{
    Context activity;
    ArrayList<DoctorcoverageList>lists;

    public DoctorcoverageAdapter(Context activity, ArrayList<DoctorcoverageList> lists) {
        this.activity = activity;
        this.lists = lists;
    }


    @NonNull
    @Override
    public DoctorcoverageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorcoverageAdapter.ViewHolder holder, int position) {
        holder.textView.setText(lists.get(position).getName());
        holder.textView2.setText(lists.get(position).getvCount()+"/"+lists.get(position).gettCnt());

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView,textView2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);
            textView2=itemView.findViewById(R.id.textView2);
        }
    }
}
