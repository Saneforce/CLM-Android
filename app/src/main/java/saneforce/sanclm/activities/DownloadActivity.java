package saneforce.sanclm.activities;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import saneforce.sanclm.R;

public class DownloadActivity extends AppCompatActivity {
    private long enqueue;
    private DownloadManager dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

                            ImageView view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            view.setImageURI(Uri.parse(uriString));
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    public void onClick(View view) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse("http://sanffa.info/Edetailing_files/KS/download/Disolv1.png"));
        enqueue = dm.enqueue(request);

    }

    public void showDownload(View view) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }

    public class  RecycleDownloader extends RecyclerView.Adapter<RecycleDownloader.MyViewholder>{

        Context context;
        ArrayList<MainActivity.File1> array=new ArrayList<>();

        public RecycleDownloader(Context context, ArrayList<MainActivity.File1> array) {
            this.context = context;
            this.array = array;
        }

        @NonNull
        @Override
        public RecycleDownloader.MyViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View vv= LayoutInflater.from(context).inflate(R.layout.custom_download_row,viewGroup,false);
            return new RecycleDownloader.MyViewholder(vv);
        }

        @Override
        public void onBindViewHolder(@NonNull RecycleDownloader.MyViewholder myViewholder, int i) {

            MainActivity.File1 ff=array.get(i);

            myViewholder.tv_progress.setText(" "+array.get(i).getRecsize()+ "%");
            //myViewholder.tv_filesize.setText(array.get(i).getTotsize());
            String fileName = ff.toString().substring(ff.toString().lastIndexOf('/')+1, ff.toString().length() );
            Log.v("printing_filename",fileName+" holder "+array.get(i).getRecsize());
            myViewholder.tv.setText(fileName);
        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        class MyViewholder extends RecyclerView.ViewHolder{
            TextView tv_progress;
            TextView tv_filesize;
            TextView tv;
            public MyViewholder(View v) {
                super(v);
                tv_progress = (TextView) v.findViewById(R.id.tv_progress);
                tv_filesize = (TextView) v.findViewById(R.id.tv_filesize);
                tv = (TextView) v.findViewById(R.id.tv_setfilename);
            }
        }
    }
}