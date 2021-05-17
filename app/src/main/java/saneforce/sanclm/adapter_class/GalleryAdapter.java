package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import saneforce.sanclm.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context ctx;
    ArrayList<String>imagelist=new ArrayList<>();

    public GalleryAdapter(Context ctx, ArrayList<String>imagelist)
 {

        this.ctx = ctx;
        this.imagelist = imagelist;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.gv_item, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(ctx)
                .load(imagelist.get(position))
                .into(holder.image);


    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ivGallery);

        }
    }
}