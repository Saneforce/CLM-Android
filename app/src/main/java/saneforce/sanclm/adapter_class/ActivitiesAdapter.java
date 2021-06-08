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

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.AcivityModel;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder>{
        Context activity;
        ArrayList<AcivityModel> lists;

public ActivitiesAdapter(Context activity, ArrayList<AcivityModel> lists) {
        this.activity = activity;
        this.lists = lists;
        }


@NonNull
@Override
public ActivitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_activity, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
        }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.name.setText(lists.get(position).getName());
       holder.plan.setText(lists.get(position).getPlan());
       holder.actual.setText(lists.get(position).getActual());
       if(lists.get(position).getPlan().equals(lists.get(position).getActual())){
           holder.actual.setTextColor(Color.parseColor("#4C5CB5"));

       }else{
           holder.actual.setTextColor(Color.parseColor("#cc2311"));

       }
    }



@Override
public int getItemCount() {
        return lists.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView name,plan,actual;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.actname);
        plan=itemView.findViewById(R.id.plan);
        actual=itemView.findViewById(R.id.actual);

    }
}
}
