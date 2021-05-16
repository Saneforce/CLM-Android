package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ImageModel;

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.ViewHolder>{

    Context context;
    ArrayList<String> stringArrayList;
    ArrayList<ImageModel>imageModels;

    public SelectedImageAdapter(Context context, ArrayList<String> stringArrayList,ArrayList<ImageModel>imageModels) {
        this.context = context;
        this.stringArrayList = stringArrayList;
        this.imageModels=imageModels;
    }

    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.selected_image_list, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public  void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context)
                .load(stringArrayList.get(position))
                .centerCrop()
                .into(holder.image);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringArrayList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image,delete;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
           delete = (ImageView) itemView.findViewById(R.id.delete);

        }
    }
}