package saneforce.sanclm.adapter_class;

import android.content.Context;
import android.net.Uri;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import saneforce.sanclm.R;

public class RecyclerFullScreenImage extends RecyclerView.Adapter<RecyclerFullScreenImage.ViewHolder> {

    private String mData;
    private LayoutInflater mInflater;
    ArrayList<String> url=new ArrayList<>();
    int len;
    int doubleTouchCount=0;
    static touchGesture touchgesture;
    // Data is passed into the constructor
    public RecyclerFullScreenImage(Context context, String data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

        try {
            JSONArray json_array = new JSONArray(data);
            this.len=json_array.length();
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject jsonChildNode = json_array.getJSONObject(i);
                String imgname = jsonChildNode.optString("SlideName");
                String slideUrl = jsonChildNode.optString("SlideLocUrl");
                Log.e("slide ", "POS " + imgname);
                url.add(slideUrl);
                Uri imageUri = Uri.fromFile(new File(slideUrl));
                //simpleDraweeView.setImageURI(imageUri);
            }
        }catch (Exception e){}
    }

    public static void bindlistner(touchGesture touchgesture1){
        touchgesture=touchgesture1;
    }


    // Inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.detailingimageview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //  String animal = mData[position];
        String imgurl=url.get(position);
        Uri imageUri = Uri.fromFile(new File(imgurl));
        holder.imgview.setImageURI(imageUri);
        holder.imgview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                        Log.v("double_finget", "called");
                        int count = motionEvent.getPointerCount(); // Number of 'fingers' in this time
                        if (count == 2) {
                            Log.v("double_fingetssss", "called");
                            if (doubleTouchCount == 0) {
                                doubleTouchCount = 1;
                                touchgesture.touches();
                                // rll_overlay.setVisibility(View.VISIBLE);

                            } else {
                                doubleTouchCount = 0;
                                touchgesture.unTouches();
                                //rll_overlay.setVisibility(View.GONE);
                            }
                        }
                }
                return false;
            }
        });



    }

    // Total number of cells
    @Override
    public int getItemCount() {
        return url.size();
    }

    // Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgview;

        public ViewHolder(View itemView) {
            super(itemView);
            imgview = (SimpleDraweeView) itemView.findViewById(R.id.iv_detimage);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(view, getAdapterPosition());
        }
    }

    // Convenience method for getting data at click position
    public String getItem(int id) {
        return url.get(id);
    }

    // Method that executes your code for the action received
    public void onItemClick(View view, int position) {
        Log.i("recyclerview", "You clicked number " + getItem(position).toString() + ", which is at cell position " + position);



    }

    public interface touchGesture{
        void touches();
        void unTouches();
    }
}

