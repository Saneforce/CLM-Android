package saneforce.sanclm.activities;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.zelory.compressor.Compressor;
import saneforce.sanclm.R;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.Decompress;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class MainActivity extends AppCompatActivity {
    static DataBaseHandler dbh;
    public static final String ID = "id";
    RecyclerView recycle;
    ArrayList<File1> files = new ArrayList<File1>();
    String db_slidedwnloadPath;
    private boolean mReceiversRegistered;
    RecycleDownloader adapt;
    static ProgressBar bar;
    double dtaSize, RDSize;
    DecimalFormat df;
    int tszflg = 0;
    int rszflg = 0;
    String Size;
    CommonSharedPreference mCommonSharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycle=findViewById(R.id.recycle);
        dbh = new DataBaseHandler(this);
        mCommonSharedPreference=new CommonSharedPreference(this);
        db_slidedwnloadPath =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        SlidesDownloader(0);

    }

    public static class File1 implements Parcelable {
        public static Object separator;
        private final String url;

        private int progress;
        private int recsize;
        private int totsize;
        private String strSize="0";

        public File1(String url) {
            this.url = url;
        }
        public File1(String url,int progress,int recsize,int totsize,String strSize) {
            this.url = url;
            this.progress = progress;
            this.recsize = recsize;
            this.totsize = totsize;
            this.strSize = strSize;
        }

        public int getProgress() {
            return progress;
        }

        public String getStrSize() {
            return strSize;
        }

        public void setStrSize(String strSize) {
            this.strSize = strSize;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public int getRecsize() {
            return recsize;
        }

        public void setRecsize(int recsize) {
            this.recsize = recsize;
        }

        public int getTotsize() {
            return totsize;
        }

        public void setTotsize(int totsize) {
            this.totsize = totsize;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return url;// + "   " + progress + " %";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.url);
            dest.writeInt(this.progress);
            dest.writeInt(this.recsize);
            dest.writeInt(this.totsize);
        }

        private File1(Parcel in) {
            this.url = in.readString();
            this.progress = in.readInt();
            this.recsize = in.readInt();
            this.totsize = in.readInt();
        }

        public static final Parcelable.Creator<File1> CREATOR = new Parcelable.Creator<File1>(){
            public File1 createFromParcel(Parcel source) {
                return new File1(source);
            }

            public File1[] newArray(int size) {
                return new File1[size];
            }
        };
    }

    public static class DownloadingService extends IntentService {
        public static String PROGRESS_UPDATE_ACTION = HomeDashBoard.DownloadingService.class.getName() + ".progress_update";

        private static final String ACTION_CANCEL_DOWNLOAD = HomeDashBoard.DownloadingService.class.getName() + "action_cancel_download";

        private boolean mIsAlreadyRunning;
        private boolean mReceiversRegistered;

        private ExecutorService mExec;
        private CompletionService<NoResultType> mEcs;
        private LocalBroadcastManager mBroadcastManager;
        private List<DownloadTask> mTasks;

        private static final long INTERVAL_BROADCAST = 300;
        private long mLastUpdate = 0;

        public DownloadingService() {
            super("DownloadingService");
            mExec = Executors.newFixedThreadPool( /* only 5 at a time */3);
            mEcs = new ExecutorCompletionService<NoResultType>(mExec);
            mBroadcastManager = LocalBroadcastManager.getInstance(this);
            dbh.open();
            mTasks = new ArrayList<DownloadTask>();
        }

        @Override
        public void onCreate() {
            super.onCreate();
            registerReceiver();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            //unregisterReceiver();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (mIsAlreadyRunning) {
                publishCurrentProgressOneShot(true);
            }
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            Log.v("onhandle","intent_are");
            if (mIsAlreadyRunning) {
                return;
            }
            mIsAlreadyRunning = true;
            Log.v("onhandle22","intent_are");
            ArrayList<File1> files = intent.getParcelableArrayListExtra("files");
            final Collection<DownloadTask> tasks = mTasks;
            int index = 0;
            Log.v("onhandle223","intent_are");
            for (File1 file : files) {

                int totsize = file.totsize;
                Log.v("onhandle2233","intent_are"+file+totsize);
                DownloadingService.DownloadTask yt1 = new DownloadTask(index++, file,totsize);
                tasks.add(yt1);
            }
            Log.v("onhandle44","intent_are");
            for (DownloadTask t : tasks) {
                mEcs.submit(t);
            }

            // wait for finish
            int n = tasks.size();
            int dwnloadsize =0;

            for (int i = 0; i < n; ++i) {
                NoResultType r;
                try {
                    r = mEcs.take().get();
                    if (r != null) {
                        dwnloadsize =dwnloadsize+1;
                        if(dwnloadsize==n){
                            closeactivity();
                        }
                        Log.d("TASK_SIZE",""+dwnloadsize +"TOT SIZE "+n);
                    }
                } catch (InterruptedException e) {
                    Log.d("TASK_SIZE_interrupt",""+e);
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Log.d("TASK_SIZE_excep",""+e);
                    e.printStackTrace();
                }
            }
            // send a last broadcast
            //publishCurrentProgressOneShot(true);
            mExec.shutdown();
           /* new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    tb_dwnloadSlides.setVisibility(View.INVISIBLE);
                }
            });
*/
        }

        private void closeactivity() {
            Intent home = new Intent(this,HomeDashBoard.class);
            home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);

        }

        private void publishCurrentProgressOneShot(boolean forced) {
            // if (forced || System.currentTimeMillis() - mLastUpdate > INTERVAL_BROADCAST) {
            mLastUpdate = System.currentTimeMillis();
            final List<DownloadTask> tasks = mTasks;
            int[] positions = new int[tasks.size()];
            int[] progresses = new int[tasks.size()];
            int[] recsize = new int[tasks.size()];
            int[] totsize = new int[tasks.size()];

            for (int i = 0; i < tasks.size(); i++) {
                DownloadTask t = tasks.get(i);
                // Log.d("POSITION", "POSITION"+t.mPosition +"<>>"+t.mProgress);
                positions[i] = t.mPosition;
                progresses[i] = t.mProgress;
                recsize[i] = t.mrecsize;
                totsize[i] = t.mtotsize;
            }
            publishProgress(positions, progresses,recsize,totsize);
            //}
        }

        private void publishCurrentProgressOneShot() {
            publishCurrentProgressOneShot(false);
        }

        private synchronized void publishProgress(int[] positions, int[] progresses,int[] recsize, int[] totsize) {
            Intent i = new Intent();
            i.setAction(PROGRESS_UPDATE_ACTION);
            i.putExtra("position", positions);
            i.putExtra("progress", progresses);
            i.putExtra("recsize", recsize);
            i.putExtra("totsize", totsize);
            i.putExtra("oneshot", true);

            mBroadcastManager.sendBroadcast(i);
        }

        // following methods can also be used but will cause lots of broadcasts
        private void publishCurrentProgress() {
            final Collection<DownloadTask> tasks = mTasks;
            for (DownloadTask t : tasks) {
                publishProgress(t.mPosition, t.mProgress,t.mrecsize,t.mtotsize);
            }
        }

        private synchronized void publishProgress(int position, int progress,int recsize,int totsize) {
            Intent i = new Intent();
            i.setAction(PROGRESS_UPDATE_ACTION);
            i.putExtra("progress", progress);
            i.putExtra("position", position);
            i.putExtra("recsize", recsize);
            i.putExtra("totsize", totsize);
            mBroadcastManager.sendBroadcast(i);
        }

        class DownloadTask implements Callable<NoResultType> {
            private int mPosition;
            private int mProgress;
            private int mrecsize;
            private int mtotsize;
            private boolean mCancelled;
            private final File1 mFile;
            private Random mRand = new Random();
            File compressedImageFile;
            public DownloadTask(int position, File1 file, int totsize) {
                mPosition = position;
                mFile = file;
                mtotsize = totsize;
                Log.v("file_in_post",file+"");
            }

            @SuppressLint("LongLogTag")
            @Override
            public NoResultType call() throws Exception {

                URL url = new URL(mFile.getUrl());
                // Log.d("FILE ANBME ",mFile.getUrl());
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int count = 0;
                File mydir = getApplicationContext().getDir("private_dir", Context.MODE_PRIVATE);
                //File file = new File(mydir.getAbsoluteFile().toString()+"/Products");
                File file = new File(Environment.getExternalStorageDirectory()+"/Products"); /*Internal Storage*/
                File file11 = new File(Environment.getExternalStorageDirectory()+"/Productsss"); /*Internal Storage*/

                if (!file.exists()){
                    if (!file.mkdirs()){
                        Log.d("IMAGE_DIRECTORY_NAME", "Oops! Failed create IMAGE_DIRECTORY_NAME directory");
                    }
                }
                if (!file11.exists()){
                    if (!file11.mkdirs()){
                        Log.d("IMAGE_DIRECTORY_NAME", "Oops! Failed create IMAGE_DIRECTORY_NAME directory");
                    }
                }
                String fileName = mFile.getUrl().substring(mFile.getUrl().lastIndexOf('/')+1, mFile.getUrl().length() );
                File targetLocation = new File(file.getPath() + File.separator+ fileName);
                File targetLocationnn = new File(file11.getPath() + File.separator+ fileName);
                InputStream input = new BufferedInputStream(url.openStream());

                int lenghtOfFile = conexion.getContentLength();

                String fileType =fileName.toString();
                String ZipFile = fileType.substring(fileType.lastIndexOf(".")+1);
                byte data[] = new byte[1024];
                int total = 0;
                int Status;

                Log.v("internal_storage_targetLoc: ",targetLocation+"");
                if(ZipFile.equalsIgnoreCase("zip"))
                {

                    String zipFile = targetLocation.toString();
                    String unzipLocation = file.getPath() + File.separator;
                    OutputStream output = new FileOutputStream(targetLocation);
                    OutputStream output1 = new FileOutputStream(targetLocationnn);
                    try {
                        while ((count = input.read(data)) != -1) {
                            total += count;
                            Status = (int)((total*100)/lenghtOfFile);
                            mProgress += Status;//mRand.nextInt(5);
                            mrecsize += total;
                            mtotsize += lenghtOfFile;

                            // System.out.println("STATUS : "+Status +"LENGTH OF FILE >"+lenghtOfFile +"Total >>"+total);
                            publishProgress(mPosition, Status,total,lenghtOfFile);
                            output.write(data, 0, count);
                            output1.write(data, 0, count);

                        }
                        output.flush();
                        output.close();
                        output1.flush();
                        output1.close();

                        Log.v("unzip_location_are",unzipLocation);
                        Decompress d = new Decompress(zipFile, unzipLocation);
                        d.unzip();
                        File file1 = new File(file.getPath() + File.separator+ fileName.toString());
                        Log.v("file_one_print",file1+"");
                        //boolean deleted = file1.delete();

                        String HTMLPath = targetLocation.toString().replaceAll(".zip", "");
                        Log.v("Presentation_dragss","adapter_called"+HTMLPath);
                        File f = new File(HTMLPath);
                        File[] files = f.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                return filename.contains(".png") || filename.contains(".jpg");
                            }
                        });
                        String urll = "";

                        if (files.length > 0) urll = files[0].getAbsolutePath();
                        Uri imageUri = Uri.fromFile(new File(urll));

                        Log.v("Presentation_drag_ht","adapter_called"+imageUri+"url"+urll);
                        //imageUri= Uri.parse("/storage/emulated/0/Products/IndianImmunologicals/preview.png");
                        Log.v("Presentation_drag_ht","adapter_called"+imageUri);
                        //Log.v("Presentation_drag_ht","adapter_called"+imageUri);


                        String bit=BitMapToString(getResizedBitmap(String.valueOf(imageUri).substring(7),150,150));
                        File compressedImageFile = new Compressor(getApplicationContext()).compressToFile(new File(urll));
                        // dbh.update_product_Content_Url(targetLocation.toString(),fileName,bit);
                        dbh.update_product_Content_Url(targetLocation.toString(),fileName,bit,compressedImageFile.toString());
                        Log.v("targetLocation_1",targetLocation.toString()+"compress");
                    }catch (NullPointerException e) {
                        Log.d("TASK_SIZE_dwnd_exe",""+e+url);
                        e.printStackTrace();
                    }catch (Exception e) {
                        Log.d("TASK_SIZE_dwnd_exec",""+e);
                        e.printStackTrace();
                    }


                }else{
                    //System.out.println("intetnal storege targetLocation: "+targetLocation.toString());
                    OutputStream output = new FileOutputStream(targetLocation);
                    try {
                        while ((count = input.read(data)) != -1) {
                            total += count;
                            Status = (int)((total*100)/lenghtOfFile);
                            // System.out.println("lenghtOfFile>>>/"+lenghtOfFile +"POSITION "+mPosition);
                            mProgress += Status;//mRand.nextInt(5);
                            mrecsize += total;
                            mtotsize += lenghtOfFile;
                            publishProgress(mPosition, Status,total,lenghtOfFile);
                            output.write(data, 0, count);
                        }

                        output.flush();
                        output.close();
                        System.out.println("lenghtOfFile>>>/"+fileName);
                        String bit="";
                        if(ZipFile.equalsIgnoreCase("pdf")){
                            Drawable myDrawable = getApplicationContext().getResources().getDrawable(R.mipmap.pdf_logo);
                            Bitmap bm = ((BitmapDrawable) myDrawable).getBitmap();
                            bit = BitMapToString(scaleDown(bm, 200, true));

                        }
                        else if(ZipFile.equalsIgnoreCase("mp4")){
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(targetLocation.toString());
                            Bitmap bm = mediaMetadataRetriever.getFrameAtTime(5000000);
                            bit=BitMapToString(bm);
                            /*dbh.update_product_Content_Url(targetLocation.toString(),fileName,bit,"empty");*/
                        }
                        else if(!targetLocation.toString().contains("avi")) {
                            bit = BitMapToString(getResizedBitmap(targetLocation.toString(), 150, 150));
                        }

                        if(targetLocation.toString().contains("png")|| targetLocation.toString().contains("jpg")){
                            File compressedImageFile = new Compressor(getApplicationContext()).compressToFile(targetLocation);
                            dbh.update_product_Content_Url(compressedImageFile.toString(),fileName,bit,compressedImageFile.toString());
                            Log.v("compressed_Filesss",compressedImageFile.toString());
                        }
                        else
                            dbh.update_product_Content_Url(targetLocation.toString(),fileName,bit,"empty");

                        Log.v("targetLocation_2",targetLocation.toString());
                    }catch (NullPointerException e) {e.printStackTrace();} catch (Exception e) {e.printStackTrace();
                    }finally{
                        dbh.close();
                    }}


//https://www.google.com/search?q=all+video+support+player+for+android+github&rlz=1C1CHBF_enIN853IN853&oq=all+video+support+player+for+android+github&aqs=chrome..69i57.33038j0j7&sourceid=chrome&ie=UTF-8
                //publishCurrentProgressOneShot();

                return new DownloadingService.NoResultType();
            }

            public int getProgress() {
                return mProgress;
            }

            public int getMrecsize() {
                return mrecsize;
            }

            public int getMtotsize() {
                return mtotsize;
            }

            public int getPosition() {
                return mPosition;
            }

            public void cancel() {
                mCancelled = true;
            }
        }

        public Bitmap getResizedBitmap(String path, int newWidth, int newHeight) {
            int width=0;
            int height=0;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bm = BitmapFactory.decodeFile(path, bmOptions);
            try {
                width= bm.getWidth();
                height = bm.getHeight();
            }catch (Exception e){
                Log.v("TASK_SIZE_are_ptint",e.getMessage());
            }

            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, width, height, matrix, false);
            if (bm != null && !bm.isRecycled())
                bm.recycle();

            return resizedBitmap;
        }

        private void registerReceiver() {
            unregisterReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(DownloadingService.ACTION_CANCEL_DOWNLOAD);
            LocalBroadcastManager.getInstance(this).registerReceiver(mCommunicationReceiver, filter);
            mReceiversRegistered = true;
        }

        private void unregisterReceiver() {
            if (mReceiversRegistered) {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(mCommunicationReceiver);
                mReceiversRegistered = false;
            }
        }

        private final BroadcastReceiver mCommunicationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(DownloadingService.ACTION_CANCEL_DOWNLOAD)) {
                    final long id = intent.getLongExtra(ID, -1);
                    if (id != -1) {
                        for (DownloadTask task : mTasks) {

                        }
                    }
                }
            }
        };

        class NoResultType {
        }
    }
    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,boolean filter)
    {
        float ratio = Math.min((float) maxImageSize / realImage.getWidth(),(float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }

    public class  RecycleDownloader extends RecyclerView.Adapter<RecycleDownloader.MyViewholder>{

        Context context;
        ArrayList<File1> array=new ArrayList<>();

        public RecycleDownloader(Context context, ArrayList<File1> array) {
            this.context = context;
            this.array = array;
        }

        @NonNull
        @Override
        public MyViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View vv= LayoutInflater.from(context).inflate(R.layout.custom_download_row,viewGroup,false);
            return new MyViewholder(vv);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewholder myViewholder, int i) {

            File1 ff=array.get(i);

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
    private final BroadcastReceiver mDownloadingProgressReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(HomeDashBoard.DownloadingService.PROGRESS_UPDATE_ACTION)) {
                final boolean oneShot = intent.getBooleanExtra("oneshot", false);
                if (oneShot) {
                    final int[] progresses = intent.getIntArrayExtra("progress");
                    final int[] positions = intent.getIntArrayExtra("position");
                    final int[] recsize = intent.getIntArrayExtra("progress");
                    final int[] totsize = intent.getIntArrayExtra("position");
                    onProgressUpdateOneShot(positions, progresses,recsize,totsize);
                } else {
                    final int progress = intent.getIntExtra("progress", -1);
                    final int position = intent.getIntExtra("position", -1);
                    final int recsize =  intent.getIntExtra("recsize", -1);
                    final int totsize =  intent.getIntExtra("totsize", -1);
                    if (position == -1) {
                        return;
                    }
                    onProgressUpdate(position, progress,recsize,totsize);
                }
            }
        }
    };
    protected void onProgressUpdateOneShot(int[] positions, int[] progresses,int[] recsize, int[] totsize) {
        for (int i = 0; i < positions.length; i++) {
            int position = positions[i];
            int progress = progresses[i];
            int recvsize = recsize[i];
            int totasize = totsize[i];
            onProgressUpdate(position, progress,recvsize,totasize);
        }
    }
    protected void onProgressUpdate(int position, int progress,int recsize, int totsize) {
        Log.v("progress_updation_pos",position+"");
       /* int first = recycle.getFirstVisiblePosition();
        int last = listView.getLastVisiblePosition();

        mAdapter.getItem(position).progress = progress > 100 ? 100 : progress;
        mAdapter.getItem(position).recsize = recsize;
        mAdapter.getItem(position).totsize  =totsize;
        if (position < first || position > last) {
        } else {
            View convertView = listView.getChildAt(position - first);
            updateRow(mAdapter.getItem(position), convertView);
        }*/
        updateRow(position);
    }

    public void SlidesDownloader(int x) {
        files.clear();
        dbh.open();
        try {
            Cursor cur = dbh.select_slidesUrlPathDummy();
            if (cur.getCount() == 0) {
                Log.v("slide_downloder_1", "are_activated");
            }
            Log.v("slide_downloder_1", "are_activated" + cur.getCount());
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    //if(downloadFilepath.contains(cur.getString(5))){}else{
                    files.add(new File1(db_slidedwnloadPath + cur.getString(5),0,0,0,"0"));
                    Log.v("slide_downloder_123", "are_activated_in" + db_slidedwnloadPath + cur.getString(5));
                    //downloadFilepath+=cur.getString(5)+",";
                    // }

                }
                SharedPreferences slide = getSharedPreferences("slide", 0);
                SharedPreferences.Editor edit = slide.edit();
                edit.putString("slide_download", "1");
                edit.commit();
                Log.v("slide_downloder", "are_activated");
                adapt=new RecycleDownloader(MainActivity.this,files);
                RecyclerView.LayoutManager lay=new LinearLayoutManager(this);
                recycle.setLayoutManager(lay);
                recycle.setAdapter(adapt);
               /* if (x == 0)
                    tb_dwnloadSlides.setVisibility(View.VISIBLE);
                listView.setAdapter(mAdapter = new ArrayAdapter<HomeDashBoard.File1>(this, R.layout.custom_download_row, R.id.tv_setfilename, files) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        updateRow(getItem(position), v);
                        return v;
                    }
                });*/
                //if (savedInstanceState == null) {
                Intent intent = new Intent(this, DownloadingService.class);
                intent.putParcelableArrayListExtra("files", new ArrayList<File1>(files));
                startService(intent);
                //}
                registerReceiver();
            } else {
                //tb_dwnloadSlides.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Log.v("Excetion_slipe",e.getMessage());
            dbh.open();
            SlidesDownloader(0);
        }
    }
    private void registerReceiver() {
        unregisterReceiver();
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction(HomeDashBoard.DownloadingService.PROGRESS_UPDATE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadingProgressReceiver, intentToReceiveFilter);
        mReceiversRegistered = true;
    }

    private void unregisterReceiver() {
        if (mReceiversRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadingProgressReceiver);
            mReceiversRegistered = false;
        }
    }
    private void updateRow(int pos) {

        File1 ff=files.get(pos);
        ff.progress = ff.progress > 100 ? 100 : ff.progress;
        df = new DecimalFormat("#.##");
        dtaSize= ff.getTotsize();
        if(dtaSize>1024) {dtaSize=dtaSize/1024;tszflg=1;}
        if(dtaSize>1024) {dtaSize=dtaSize/1024;tszflg=2;}
        if(dtaSize>1024) {dtaSize=dtaSize/1024;tszflg=3;}

        RDSize=ff.getRecsize();
        if(RDSize>1024) {RDSize=RDSize/1024;rszflg=1;}
        if(RDSize>1024) {RDSize=RDSize/1024;rszflg=2;}
        if(RDSize>1024) {RDSize=RDSize/1024;rszflg=3;}

        Size = df.format(RDSize) +" "+((rszflg==0)?"B":(rszflg==1)?"KB":(rszflg==2)?"MB":"GB") +" / "+df.format(dtaSize) +" "+((tszflg==0)?"B":(tszflg==1)?"KB":(tszflg==2)?"MB":"GB");


        ff.setProgress(ff.progress);
        ff.setTotsize(ff.totsize);

        ff.setStrSize(Size);

        adapt.notifyDataSetChanged();
/*
        v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(DownloadingService.ACTION_CANCEL_DOWNLOAD);
                // i.putExtra(ID, file.getId());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
            }
        });
*/
    }

}