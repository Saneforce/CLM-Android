package saneforce.sanclm.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;


import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.SavePresentation;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.fragments.Detailing_Selection_search_grid_selection;
import saneforce.sanclm.fragments.Detailing_right_grid_view;
import saneforce.sanclm.fragments.DownloadMasterData;
import saneforce.sanclm.fragments.Presentation_recycler_item;
import saneforce.sanclm.fragments.Presentation_search_grid_selection;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.CustomiseOption;
import saneforce.sanclm.util.CustomiseSearchOption;
import saneforce.sanclm.util.PresentationRightUnselect;
import saneforce.sanclm.util.SpecialityListener;



public class DetailingCreationActivity extends FragmentActivity implements View.OnClickListener, Detailing_Selection_search_grid_selection.Communicator, Presentation_search_grid_selection.Communicator {

    ImageView iv_back, iv_startdet, iv_reload;
    CommonUtilsMethods CommonUtilsMethods;
    static DataBaseHandler dbh;
    DetailingTrackerPOJO mDetailingTrackerPOJO;
    Cursor mCursor;
    CommonSharedPreference mCommonSharedPreference;
    String prdName = "";
    TextView tv_detsel_drNm;
    static Context context;
    static SavePresentation savePresentation;
    Presentation_recycler_item _pPresentation_recycler_item;
    Presentation_search_grid_selection _pPresentation_search_grid_selection;
    Detailing_Selection_search_grid_selection det_search_sel_fragment1;
    Detailing_right_grid_view det_rightview_fragment;
    RelativeLayout rlay_spin;
    String skipSpeciality;
    TextView txt_spinner;
    static SpecialityListener specialityListener;

    DownloadMasterData downloadMasterData;

    Intent intent;
    String digital, db_slidedwnloadPath;
//    ArrayList<HomeDashBoard.File1> files = new ArrayList<HomeDashBoard.File1>();
//    ListView listView;
//    private ArrayAdapter<HomeDashBoard.File1> mAdapter;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailing_main);
        SharedPreferences sharedPreferences = getSharedPreferences("left_fragment", 0);
        SharedPreferences.Editor editt = sharedPreferences.edit();
        editt.putString("left", "1");
        editt.commit();
        CommonUtilsMethods = new CommonUtilsMethods(this);
        mCommonSharedPreference = new CommonSharedPreference(this);
        dbh = new DataBaseHandler(this);
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_startdet = (ImageView) findViewById(R.id.iv_startdet);
        iv_reload = (ImageView) findViewById(R.id.iv_reload);
        iv_startdet.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_detsel_drNm = (TextView) findViewById(R.id.tv_detsel_drNm);
        rlay_spin = findViewById(R.id.rlay_spin);
        mCommonSharedPreference.setValueToPreference("presentationList", false);
        mCommonSharedPreference.setValueToPreference("selection_product", "[]");
        tv_detsel_drNm.setText(mCommonSharedPreference.getValueFromPreference("drName"));
        skipSpeciality = mCommonSharedPreference.getValueFromPreference("specFilter");
        txt_spinner = (TextView) findViewById(R.id.txt_spinner);
        Detailing_Selection_search_grid_selection.checkActivity = false;
        det_search_sel_fragment1 = (Detailing_Selection_search_grid_selection) getFragmentManager().findFragmentById(R.id.det_search_sel_fragment1);
        det_rightview_fragment = (Detailing_right_grid_view) getFragmentManager().findFragmentById(R.id.det_rightview_fragment);
        _pPresentation_recycler_item = (Presentation_recycler_item) getFragmentManager().findFragmentById(R.id.presentation_right_fm);
        _pPresentation_search_grid_selection = (Presentation_search_grid_selection) getFragmentManager().findFragmentById(R.id.frag1_search);
        _pPresentation_recycler_item.getView().setVisibility(View.GONE);
        _pPresentation_search_grid_selection.getView().setVisibility(View.GONE);

        Log.v("DETAILING_DATA", "DETAILING_DATA");

        db_slidedwnloadPath = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        digital = mCommonSharedPreference.getValueFromPreference("Digital_offline");
        //downloadMasterData=new DownloadMasterData();
        iv_reload.setOnClickListener(this);
        iv_reload.setVisibility(View.GONE);
        if (digital.equalsIgnoreCase("1")) {
            iv_reload.setVisibility(View.VISIBLE);
        }
//        intent=getIntent();
//        digital=intent.getStringExtra("detailing");

        if (digital.equalsIgnoreCase("1")) {
            tv_detsel_drNm.setText("");
            iv_back.setClickable(false);
        }


        if (skipSpeciality.equalsIgnoreCase("1"))
            rlay_spin.setVisibility(View.VISIBLE);


        Detailing_Selection_search_grid_selection.bindListenerCustomise(new CustomiseOption() {
            @Override
            public void isClicked() {
                Log.v("present_grid_are", "called");
                _pPresentation_recycler_item.getView().setVisibility(View.VISIBLE);
                _pPresentation_search_grid_selection.getView().setVisibility(View.VISIBLE);
                det_search_sel_fragment1.getView().setVisibility(View.GONE);
                det_rightview_fragment.getView().setVisibility(View.GONE);
                _pPresentation_search_grid_selection.visibilityHeader();
                _pPresentation_search_grid_selection.mdDisplayProductInGridView("");

            }
        });

        Presentation_search_grid_selection.bindListenerCustomiseList(new CustomiseSearchOption() {
            @Override
            public void viewSearchOption() {
                Log.v("present_grid_are123", "called");
                _pPresentation_recycler_item.getView().setVisibility(View.GONE);
                _pPresentation_search_grid_selection.getView().setVisibility(View.GONE);
                det_search_sel_fragment1.getView().setVisibility(View.VISIBLE);
                det_rightview_fragment.getView().setVisibility(View.VISIBLE);
                Detailing_Selection_search_grid_selection.CustomClick = false;
                det_search_sel_fragment1.init();
            }
        });

        rlay_spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Detailing_right_grid_view.bindSpecName(new PresentationRightUnselect() {
                    @Override
                    public void unSelecting(String prdname) {
                        txt_spinner.setText(prdname);
                    //if(prdname=="null")
                        Log.v("DETAILG_DATA", txt_spinner.getText().toString());
                    }
                });
                Detailing_right_grid_view.popupSpeciality();
            }
        });

        if (TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("specCode")) || mCommonSharedPreference.getValueFromPreference("specCode") == "null"){
            txt_spinner.setText(getResources().getString(R.string.sclt_spcl));
        }

    }

    @Override
    public void onResume() {
        CommonUtilsMethods.FullScreencall(this);
        super.onResume();
        if (TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("specCode")) || mCommonSharedPreference.getValueFromPreference("specCode")=="null")
            txt_spinner.setText(getResources().getString(R.string.sclt_spcl));
        else {
            txt_spinner.setText(mCommonSharedPreference.getValueFromPreference("specName"));
            CommonUtils.TAG_DR_SPEC = mCommonSharedPreference.getValueFromPreference("specCode");
            Log.v("pritning_spec_code12", CommonUtils.TAG_DR_SPEC);
            specialityListener.specialityCode(mCommonSharedPreference.getValueFromPreference("specCode"));
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                CommonUtilsMethods.CommonIntentwithNEwTask(DCRCallSelectionActivity.class);
                mCommonSharedPreference.setValueToPreference("display_brand", getResources().getString(R.string.brandmatrix));
                break;

            case R.id.iv_startdet:
                if (Detailing_Selection_search_grid_selection.CustomClick) {

                    Detailing_Selection_search_grid_selection.CustomClick = true;
                    savePresentation.saveDetail();
                    prdName = "";
                    det_search_sel_fragment1.DisplayProductGridView(getResources().getString(R.string.customise));
                }
                String slideData = mCommonSharedPreference.getValueFromPreference("ProductBrdWiseSlides_jsonArray");
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(slideData.toString());
                    Log.e("Slide_list_details", slideData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonArray.length() == 0) {
                    Toast.makeText(DetailingCreationActivity.this, getResources().getString(R.string.noprdsclt), Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(DetailingCreationActivity.this, DetailingFullScreenImageViewActivity.class);
                    i.putExtra("pname", prdName);
                    startActivity(i);
                }
              /*  String speciality = mDetailingTrackerPOJO.getmDoctorSpeciality();
                String ProductBrdCode =mDetailingTrackerPOJO.getmDetstrtpdtBrdCode();
                JSONArray SlidesArray = new JSONArray();
                try {
                    dbh.open();
                    switch (mDetailingTrackerPOJO.getmDetListview_Selection()) {
                        case "Speciality Wise":
                            mCursor = dbh.select_AllSlides_specialitywise(ProductBrdCode, speciality);
                            break;
                        case "Brand Matrix":
                            mCursor = dbh.select_AllSlides_brandwise(ProductBrdCode);
                            break;
                        case "All Brands":
                            mCursor = dbh.select_AllSlides_brandwise(ProductBrdCode);
                            break;
                        default:
                            mCursor = dbh.selectAllProducts_GroupPresentationWise(ProductBrdCode, mDetailingTrackerPOJO.getmDetListview_Selection());
                            break;
                    }
                    if (mCursor.getCount() > 0) {
                        while (mCursor.moveToNext()) {
                            JSONObject SLidesJson = new JSONObject();
                            SLidesJson.put("SlideId", mCursor.getString(6));
                            SLidesJson.put("SlideName", mCursor.getString(2));
                            SLidesJson.put("SlideType", mCursor.getString(3));
                            SLidesJson.put("SlideLocUrl", mCursor.getString(4));
                            SLidesJson.put("brandName", mCursor.getString(1));

                            SlidesArray.put(SLidesJson);
                        }
                        mCommonSharedPreference.setValueToPreference("CurrentBrdSlides_Json_array",SlidesArray.toString());
                        Log.v("Values_are_store","in_preference");
                        Intent i=new Intent(DetailingCreationActivity.this,DetailingFullScreenImageViewActivity.class);
                        i.putExtra("pname",prdName);
                        startActivity(i);
                    }else{
                        Intent i=new Intent(DetailingCreationActivity.this,DetailingFullScreenImageViewActivity.class);
                        i.putExtra("pname","");
                        startActivity(i);
                    }*/
                // Log.e("Slides json",SlidesArray.toString());

               /* }catch (Exception e){}
                finally {
                    dbh.close();
                }*/


                //Log.e("brdcd",SlidesArray.toString());
                break;

            case R.id.iv_reload:
                iv_startdet.setVisibility(View.INVISIBLE);
                det_search_sel_fragment1.getView().setVisibility(View.INVISIBLE);
                det_rightview_fragment.getView().setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_download, new DownloadMasterData()).commit();
                break;
        }
    }

    public static void bindSpecListener(SpecialityListener speclist) {
        specialityListener = speclist;
    }

    @Override
    public void Message(String PdtBrdName, String pdtBrdCode) {
        Log.v("Message_pdfnam", pdtBrdCode);
        prdName = PdtBrdName;
        SharedPreferences sharedPreferences = getSharedPreferences("left_fragment", 0);

        String left = sharedPreferences.getString("left", null);

        Log.v("left_value", left + "dfjjefjek" + Detailing_Selection_search_grid_selection.CustomClick);
        if (Detailing_Selection_search_grid_selection.CustomClick) {
            Presentation_recycler_item _presentation_right_grid_view = (Presentation_recycler_item) getFragmentManager().findFragmentById(R.id.presentation_right_fm);
            if (_presentation_right_grid_view != null && _presentation_right_grid_view.isInLayout()) {
                Log.v("Presentation_wrk_call", "herere");

                _presentation_right_grid_view.mdDisplayProductInGridView(PdtBrdName, pdtBrdCode, false);
                Log.v("Presentation_wrk_called", "herere");

            }
        } else {
            Detailing_right_grid_view detailing_right_grid_view_productDetails = (Detailing_right_grid_view) getFragmentManager().
                    findFragmentById(R.id.det_rightview_fragment);
            if (detailing_right_grid_view_productDetails != null && detailing_right_grid_view_productDetails.isInLayout()) {
                detailing_right_grid_view_productDetails.setText(pdtBrdCode);
            }
        }

    }

    public Bitmap getBitmap(File file) {
        int pageNum = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(DetailingCreationActivity.this);
        try {
            PdfDocument pdfDocument = pdfiumCore.newDocument(openFile(file));
            pdfiumCore.openPage(pdfDocument, pageNum);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);


            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0,
                    width, height);
            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param

            pdfiumCore.closeDocument(pdfDocument); // important!
            return bitmap;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static ParcelFileDescriptor openFile(File file) {
        ParcelFileDescriptor descriptor;
        try {
            descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return descriptor;
    }

    public static void bindPresentation(SavePresentation savePresentations) {
        savePresentation = savePresentations;
    }


//    public void SlidesDownloader(int x) {
//        files.clear();
//
//        try {
//
//            Cursor cur = dbh.select_slidesUrlPath();
//            if (cur.getCount() == 0) {
//                Log.v("slide_downloder_1", "are_activated");
//            }
//            Log.v("slide_downloder_1", "are_activated" + cur.getCount());
//            if ((cur.getCount() > 0)) {
//                while (cur.moveToNext()) {
//                    //if(downloadFilepath.contains(cur.getString(5))){}else{
//                    files.add(new HomeDashBoard.File1(db_slidedwnloadPath + cur.getString(5)));
//                    Log.v("slide_downloder_123", "are_activated_in" + db_slidedwnloadPath + cur.getString(5));
//                    downloadFilepath += cur.getString(5) + ",";
//                    // }
//                }
//                SharedPreferences slide = getSharedPreferences("slide", 0);
//                SharedPreferences.Editor edit = slide.edit();
//                edit.putString("slide_download", "1");
//                edit.commit();
//                Log.v("slide_downloder", "are_activated");
//                if (x == 0)
//                    tb_dwnloadSlides.setVisibility(View.VISIBLE);
//                listView.setAdapter(mAdapter = new ArrayAdapter<HomeDashBoard.File1>(this, R.layout.custom_download_row, R.id.tv_setfilename, files) {
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//
//                        Log.e("GetFilesView", String.valueOf(files));
//                        View v = super.getView(position, convertView, parent);
//
//                        updateRow(getItem(position), v, files.get(position).size);
//                        return v;
//                    }
//                });
//
//
//                //if (savedInstanceState == null) {
//                Intent intent = new Intent(this, HomeDashBoard.DownloadingService.class);
//                intent.putParcelableArrayListExtra("files", new ArrayList<HomeDashBoard.File1>(files));
//                startService(intent);
//                //}
//                registerReceiver();
//
//            } else {
//                tb_dwnloadSlides.setVisibility(View.GONE);
//            }
//        } catch (Exception e) {
//            Log.v("Excetion_slipe", e.getMessage());
//            dbh.open();
//            SlidesDownloader(0);
//        }
//    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.v("here_printg_destroy", "method_are_called");
//        // unregisterReceiver();
//
//    }
//
//    private void registerReceiver() {
//        unregisterReceiver();
//        IntentFilter intentToReceiveFilter = new IntentFilter();
//        intentToReceiveFilter.addAction(HomeDashBoard.DownloadingService.PROGRESS_UPDATE_ACTION);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadingProgressReceiver, intentToReceiveFilter);
//        mReceiversRegistered = true;
//    }
//
//    private void unregisterReceiver() {
//        if (mReceiversRegistered) {
//            LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadingProgressReceiver);
//            mReceiversRegistered = false;
//        }
//    }
//
//    @SuppressLint("ResourceAsColor")
//    private void updateRow(final HomeDashBoard.File1 file, View v, String size) {
//        bar = (ProgressBar) v.findViewById(R.id.progressBar);
//        bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
//        bar.setProgress(file.progress);
//        String fileName = file.toString().substring(file.toString().lastIndexOf('/') + 1, file.toString().length());
//        TextView tv_progress = (TextView) v.findViewById(R.id.tv_progress);
//        TextView tv_filesize = (TextView) v.findViewById(R.id.tv_filesize);
//        TextView tv = (TextView) v.findViewById(R.id.tv_setfilename);
//
//
//        tv_progress.setText(" " + String.valueOf(file.progress) + "%");
//        tv_filesize.setText(size);
//        tv.setText(fileName);
//
//        v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent();
//                i.setAction(HomeDashBoard.DownloadingService.ACTION_CANCEL_DOWNLOAD);
//                // i.putExtra(ID, file.getId());
//                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
//            }
//        });
//    }
//
//
//    protected void onProgressUpdate(int position, int progress, int recsize, int totsize, String size) {
//        int first = listView.getFirstVisiblePosition();
//        int last = listView.getLastVisiblePosition();
//        mAdapter.getItem(position).progress = progress > 100 ? 100 : progress;
//        mAdapter.getItem(position).recsize = recsize;
//        mAdapter.getItem(position).totsize = totsize;
//        mAdapter.getItem(position).size = size;
//        if (position < first || position > last) {
//        } else {
//            View convertView = listView.getChildAt(position - first);
//            updateRow(mAdapter.getItem(position), convertView, size);
//        }
//    }
//
//    protected void onProgressUpdateOneShot(int[] positions, int[] progresses, int[] recsize, int[] totsize) {
//        for (int i = 0; i < positions.length; i++) {
//            int position = positions[i];
//            int progress = progresses[i];
//            int recvsize = recsize[i];
//            int totasize = totsize[i];
//            onProgressUpdate(position, progress, recvsize, totasize, "0B/0B");
//        }
//    }
//
//    private final BroadcastReceiver mDownloadingProgressReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(HomeDashBoard.DownloadingService.PROGRESS_UPDATE_ACTION)) {
//                final boolean oneShot = intent.getBooleanExtra("oneshot", false);
//                if (oneShot) {
//                    final int[] progresses = intent.getIntArrayExtra("progress");
//                    final int[] positions = intent.getIntArrayExtra("position");
//                    final int[] recsize = intent.getIntArrayExtra("progress");
//                    final int[] totsize = intent.getIntArrayExtra("position");
//                    onProgressUpdateOneShot(positions, progresses, recsize, totsize);
//                } else {
//                    final int progress = intent.getIntExtra("progress", -1);
//                    final int position = intent.getIntExtra("position", -1);
//                    final int recsize = intent.getIntExtra("recsize", -1);
//                    final int totsize = intent.getIntExtra("totsize", -1);
//                    String size = intent.getStringExtra("size");
//                    if (position == -1) {
//                        return;
//                    }
//                    onProgressUpdate(position, progress, recsize, totsize, size);
//                }
//            }
//        }
//    };

//    public static class DownloadingService extends IntentService {
//        public static String PROGRESS_UPDATE_ACTION = HomeDashBoard.DownloadingService.class.getName() + ".progress_update";
//
//        private static final String ACTION_CANCEL_DOWNLOAD = HomeDashBoard.DownloadingService.class.getName() + "action_cancel_download";
//
//        private boolean mIsAlreadyRunning;
//        private boolean mReceiversRegistered;
//
//        private ExecutorService mExec;
//        private CompletionService<HomeDashBoard.DownloadingService.NoResultType> mEcs;
//        private LocalBroadcastManager mBroadcastManager;
//        private List<HomeDashBoard.DownloadingService.DownloadTask> mTasks;
//
//        private static final long INTERVAL_BROADCAST = 300;
//        private long mLastUpdate = 0;
//
//        public DownloadingService() {
//            super("DownloadingService");
//            mExec = Executors.newFixedThreadPool( /* only 5 at a time */3);
//            mEcs = new ExecutorCompletionService<HomeDashBoard.DownloadingService.NoResultType>(mExec);
//            mBroadcastManager = LocalBroadcastManager.getInstance(this);
//            dbh.open();
//            mTasks = new ArrayList<HomeDashBoard.DownloadingService.DownloadTask>();
//        }
//
//        @Override
//        public void onCreate() {
//            super.onCreate();
//            registerReceiver();
//        }
//
//        @Override
//        public void onDestroy() {
//            super.onDestroy();
//            //unregisterReceiver();
//        }
//
//        @Override
//        public int onStartCommand(Intent intent, int flags, int startId) {
//            if (mIsAlreadyRunning) {
//                publishCurrentProgressOneShot(true);
//            }
//            return super.onStartCommand(intent, flags, startId);
//        }
//
//        @Override
//        protected void onHandleIntent(Intent intent) {
//            if (mIsAlreadyRunning) {
//                return;
//            }
//            mIsAlreadyRunning = true;
//
//            ArrayList<HomeDashBoard.File1> files = intent.getParcelableArrayListExtra("files");
//            final Collection<HomeDashBoard.DownloadingService.DownloadTask> tasks = mTasks;
//            int index = 0;
//            for (HomeDashBoard.File1 file : files) {
//                int totsize = file.totsize;
//                HomeDashBoard.DownloadingService.DownloadTask yt1 = new HomeDashBoard.DownloadingService.DownloadTask(index++, file, totsize);
//                tasks.add(yt1);
//            }
//
//            for (HomeDashBoard.DownloadingService.DownloadTask t : tasks) {
//                mEcs.submit(t);
//            }
//
//            // wait for finish
//            int n = tasks.size();
//            int dwnloadsize = 0;
//
//            for (int i = 0; i < n; ++i) {
//                HomeDashBoard.DownloadingService.NoResultType r;
//                try {
//                    r = mEcs.take().get();
//                    if (r != null) {
//                        dwnloadsize = dwnloadsize + 1;
//                        if (dwnloadsize == n) {
//                            closeactivity();
//                        }
//                        Log.d("TASK_SIZE", "" + dwnloadsize + "TOT SIZE " + n);
//                        //11
//                    }
//                } catch (InterruptedException e) {
//                    Log.d("TASK_SIZE_interrupt", "" + e);
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    Log.d("TASK_SIZE_excep", "" + e);
//                    e.printStackTrace();
//                }
//            }
//            // send a last broadcast
//            //publishCurrentProgressOneShot(true);
//            mExec.shutdown();
//
//
//           /* new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                    tb_dwnloadSlides.setVisibility(View.INVISIBLE);
//                }
//            });
//*/
//        }
//
//        private void closeactivity() {
//            Intent home = new Intent(this, HomeDashBoard.class);
//            home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(home);
//
//        }
//
//        private void publishCurrentProgressOneShot(boolean forced) {
//            // if (forced || System.currentTimeMillis() - mLastUpdate > INTERVAL_BROADCAST) {
//            mLastUpdate = System.currentTimeMillis();
//            final List<HomeDashBoard.DownloadingService.DownloadTask> tasks = mTasks;
//            int[] positions = new int[tasks.size()];
//            int[] progresses = new int[tasks.size()];
//            int[] recsize = new int[tasks.size()];
//            int[] totsize = new int[tasks.size()];
//            String[] sizee = new String[tasks.size()];
//
//            for (int i = 0; i < tasks.size(); i++) {
//                HomeDashBoard.DownloadingService.DownloadTask t = tasks.get(i);
//                // Log.d("POSITION", "POSITION"+t.mPosition +"<>>"+t.mProgress);
//                positions[i] = t.mPosition;
//                progresses[i] = t.mProgress;
//                recsize[i] = t.mrecsize;
//                totsize[i] = t.mtotsize;
//            }
//
//
//            Log.e("Download_Task", String.valueOf(tasks.size()));
//
//            publishProgress(positions, progresses, recsize, totsize);
//            //}
//        }
//
//        private void publishCurrentProgressOneShot() {
//            publishCurrentProgressOneShot(false);
//        }
//
//        private synchronized void publishProgress(int[] positions, int[] progresses, int[] recsize, int[] totsize) {
//            Intent i = new Intent();
//            i.setAction(PROGRESS_UPDATE_ACTION);
//            i.putExtra("position", positions);
//            i.putExtra("progress", progresses);
//            i.putExtra("recsize", recsize);
//            i.putExtra("totsize", totsize);
//            i.putExtra("oneshot", true);
//
//            mBroadcastManager.sendBroadcast(i);
//        }
//
//        // following methods can also be used but will cause lots of broadcasts
//        private void publishCurrentProgress() {
//            final Collection<HomeDashBoard.DownloadingService.DownloadTask> tasks = mTasks;
//            for (HomeDashBoard.DownloadingService.DownloadTask t : tasks) {
//                // publishProgress(t.mPosition, t.mProgress,t.mrecsize,t.mtotsize);
//            }
//        }
//
//        private synchronized void publishProgress(int position, int progress, int recsize, int totsize, String size) {
//            Intent i = new Intent();
//            i.setAction(PROGRESS_UPDATE_ACTION);
//            i.putExtra("progress", progress);
//            i.putExtra("position", position);
//            i.putExtra("recsize", recsize);
//            i.putExtra("totsize", totsize);
//            i.putExtra("size", size);
//            mBroadcastManager.sendBroadcast(i);
//        }
//
//        class DownloadTask implements Callable<HomeDashBoard.DownloadingService.NoResultType> {
//            private int mPosition;
//            private int mProgress;
//            private int mrecsize;
//            private int mtotsize;
//            private boolean mCancelled;
//            private final HomeDashBoard.File1 mFile;
//            private Random mRand = new Random();
//            File compressedImageFile;
//
//            public DownloadTask(int position, HomeDashBoard.File1 file, int totsize) {
//                mPosition = position;
//                mFile = file;
//                mtotsize = totsize;
//                Log.v("file_in_post", file + "");
//            }
//
//            @Override
//            public HomeDashBoard.DownloadingService.NoResultType call() throws Exception {
//
//                URL url = new URL(mFile.getUrl());
//                // Log.d("FILE ANBME ",mFile.getUrl());
//                URLConnection conexion = url.openConnection();
//                conexion.connect();
//                int count = 0;
//                File mydir = getApplicationContext().getDir("private_dir", Context.MODE_PRIVATE);
//                //File file = new File(mydir.getAbsoluteFile().toString()+"/Products");
//                File file = new File(Environment.getExternalStorageDirectory() + "/Products"); /*Internal Storage*/
//                File file11 = new File(Environment.getExternalStorageDirectory() + "/Productsss"); /*Internal Storage*/
//
//
//                if (!file.exists()) {
//                    if (!file.mkdirs()) {
//                        Log.d("IMAGE_DIRECTORY_NAME", "Oops! Failed create IMAGE_DIRECTORY_NAME directory");
//                    }
//                }
//                if (!file11.exists()) {
//                    if (!file11.mkdirs()) {
//                        Log.d("IMAGE_DIRECTORY_NAME", "Oops! Failed create IMAGE_DIRECTORY_NAME directory");
//                    }
//                }
//                String fileName = mFile.getUrl().substring(mFile.getUrl().lastIndexOf('/') + 1, mFile.getUrl().length());
//                File targetLocation = new File(file.getPath() + File.separator + fileName);
//                File targetLocationnn = new File(file11.getPath() + File.separator + fileName);
//                InputStream input = new BufferedInputStream(url.openStream());
//
//                int lenghtOfFile = conexion.getContentLength();
//
//                Log.e("length_of_file", String.valueOf(lenghtOfFile));
//
//                String fileType = fileName.toString();
//                String ZipFile = fileType.substring(fileType.lastIndexOf(".") + 1);
//                byte data[] = new byte[1024];
//                int total = 0;
//                int Status;
//
//                Log.v("intern_sto_target:", targetLocation + "");
//                if (ZipFile.equalsIgnoreCase("zip")) {
//
//                    String zipFile = targetLocation.toString();
//                    String unzipLocation = file.getPath() + File.separator;
//                    OutputStream output = new FileOutputStream(targetLocation);
//                    OutputStream output1 = new FileOutputStream(targetLocationnn);
//                    try {
//                        while ((count = input.read(data)) != -1) {
//                            total += count;
//                            Status = (int) ((total * 100) / lenghtOfFile);
//                            mProgress += Status;//mRand.nextInt(5);
//                            mrecsize += total;
//                            mtotsize += lenghtOfFile;
//                            df = new DecimalFormat("#.##");
//                            //File1 ff=files.get(mPosition);
//                            dtaSize = lenghtOfFile;
//                            if (dtaSize > 1024) {
//                                dtaSize = dtaSize / 1024;
//                                tszflg = 1;
//                            }
//                            if (dtaSize > 1024) {
//                                dtaSize = dtaSize / 1024;
//                                tszflg = 2;
//                            }
//                            if (dtaSize > 1024) {
//                                dtaSize = dtaSize / 1024;
//                                tszflg = 3;
//                            }
//
//                            RDSize = total;
//                            if (RDSize > 1024) {
//                                RDSize = RDSize / 1024;
//                                rszflg = 1;
//                            }
//                            if (RDSize > 1024) {
//                                RDSize = RDSize / 1024;
//                                rszflg = 2;
//                            }
//                            if (RDSize > 1024) {
//                                RDSize = RDSize / 1024;
//                                rszflg = 3;
//                            }
//
//                            Size = df.format(RDSize) + " " + ((rszflg == 0) ? "B" : (rszflg == 1) ? "KB" : (rszflg == 2) ? "MB" : "GB") + " / " + df.format(dtaSize) + " " + ((tszflg == 0) ? "B" : (tszflg == 1) ? "KB" : (tszflg == 2) ? "MB" : "GB");
//
//                            // System.out.println("STATUS : "+Status +"LENGTH OF FILE >"+lenghtOfFile +"Total >>"+total);
//                            publishProgress(mPosition, Status, total, lenghtOfFile, Size);
//                            output.write(data, 0, count);
//                            output1.write(data, 0, count);
//
//                        }
//                        output.flush();
//                        output.close();
//                        output1.flush();
//                        output1.close();
//
//                        Log.v("unzip_location_are", unzipLocation);
//                        Decompress d = new Decompress(zipFile, unzipLocation);
//                        d.unzip();
//                        File file1 = new File(file.getPath() + File.separator + fileName.toString());
//                        Log.v("file_one_print", file1 + "");
//                        //boolean deleted = file1.delete();
//
//                        String HTMLPath = targetLocation.toString().replaceAll(".zip", "");
//                        Log.v("Presentation_dragss", "adapter_called" + HTMLPath);
//                        File f = new File(HTMLPath);
//                        File[] files = f.listFiles(new FilenameFilter() {
//                            @Override
//                            public boolean accept(File dir, String filename) {
//                                return filename.contains(".png") || filename.contains(".jpg");
//                            }
//                        });
//                        String urll = "";
//                        Log.v("files_length_here", files.length + " are here");
//                        if (files.length > 0) urll = files[0].getAbsolutePath();
//                        Uri imageUri = Uri.fromFile(new File(urll));
//
//                        Log.v("Presentation_drag_ht", "adapter_called" + imageUri + "url" + urll);
//                        //imageUri= Uri.parse("/storage/emulated/0/Products/IndianImmunologicals/preview.png");
//                        Log.v("Presentation_drag_ht", "adapter_called" + imageUri);
//                        //Log.v("Presentation_drag_ht","adapter_called"+imageUri);
//
//
//                        String bit = BitMapToString(getResizedBitmap(String.valueOf(imageUri).substring(7), 150, 150));
//                        File compressedImageFile = new Compressor(getApplicationContext()).compressToFile(new File(urll));
//                        // dbh.update_product_Content_Url(targetLocation.toString(),fileName,bit);
//                        dbh.update_product_Content_Url(targetLocation.toString(), fileName, bit, compressedImageFile.toString());
//                        Log.v("targetLocation_1", targetLocation.toString() + "compress");
//                    } catch (NullPointerException e) {
//                        Log.d("TASK_SIZE_dwnd_exe", "" + e + url);
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        Log.d("TASK_SIZE_dwnd_exec", "" + e);
//                        e.printStackTrace();
//                    }
//
//
//                } else {
//                    //System.out.println("intetnal storege targetLocation: "+targetLocation.toString());
//                    OutputStream output = new FileOutputStream(targetLocation);
//                    try {
//                        while ((count = input.read(data)) != -1) {
//                            total += count;
//                            Status = (int) ((total * 100) / lenghtOfFile);
//                            // System.out.println("lenghtOfFile>>>/"+lenghtOfFile +"POSITION "+mPosition);
//                            mProgress += Status;//mRand.nextInt(5);
//                            mrecsize += total;
//                            mtotsize += lenghtOfFile;
//                            df = new DecimalFormat("#.##");
//                            dtaSize = lenghtOfFile;
//                            if (dtaSize > 1024) {
//                                dtaSize = dtaSize / 1024;
//                                tszflg = 1;
//                            }
//                            if (dtaSize > 1024) {
//                                dtaSize = dtaSize / 1024;
//                                tszflg = 2;
//                            }
//                            if (dtaSize > 1024) {
//                                dtaSize = dtaSize / 1024;
//                                tszflg = 3;
//                            }
//
//                            RDSize = total;
//                            if (RDSize > 1024) {
//                                RDSize = RDSize / 1024;
//                                rszflg = 1;
//                            }
//                            if (RDSize > 1024) {
//                                RDSize = RDSize / 1024;
//                                rszflg = 2;
//                            }
//                            if (RDSize > 1024) {
//                                RDSize = RDSize / 1024;
//                                rszflg = 3;
//                            }
//
//                            Size = df.format(RDSize) + " " + ((rszflg == 0) ? "B" : (rszflg == 1) ? "KB" : (rszflg == 2) ? "MB" : "GB") + " / " + df.format(dtaSize) + " " + ((tszflg == 0) ? "B" : (tszflg == 1) ? "KB" : (tszflg == 2) ? "MB" : "GB");
//                            publishProgress(mPosition, Status, total, lenghtOfFile, Size);
//                            output.write(data, 0, count);
//                        }
//
//                        output.flush();
//                        output.close();
//                        System.out.println("lenghtOfFile>>>/" + fileName);
//                        String bit = "";
//                        if (ZipFile.equalsIgnoreCase("pdf")) {
//                            /*Drawable myDrawable = getApplicationContext().getResources().getDrawable(R.mipmap.pdf_logo);
//                            Bitmap bm = ((BitmapDrawable) myDrawable).getBitmap();
//                            bit = BitMapToString(scaleDown(bm, 200, true));*/
//                            Log.v("printing_target_", targetLocation.toString() + " zip " + ZipFile);
//                            Bitmap bm = getResizedBitmapForPdf(getBitmap(new File(targetLocation.toString())), 150, 150);
//                            bit = BitMapToString(scaleDown(bm, 200, true));
//                        } else if (ZipFile.equalsIgnoreCase("mp4")) {
//                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//                            mediaMetadataRetriever.setDataSource(targetLocation.toString());
//                            Bitmap bm = mediaMetadataRetriever.getFrameAtTime(5000000);
//                            bit = BitMapToString(bm);
//                            /*dbh.update_product_Content_Url(targetLocation.toString(),fileName,bit,"empty");*/
//                        } else if (!targetLocation.toString().contains("avi")) {
//                            bit = BitMapToString(getResizedBitmap(targetLocation.toString(), 150, 150));
//                        }
//
//                        if (targetLocation.toString().contains("png") || targetLocation.toString().contains("jpg")) {
//                            File compressedImageFile = new Compressor(getApplicationContext()).compressToFile(targetLocation);
//                            dbh.update_product_Content_Url(compressedImageFile.toString(), fileName, bit, compressedImageFile.toString());
//                            Log.v("compressed_Filesss", compressedImageFile.toString());
//                        } else
//                            dbh.update_product_Content_Url(targetLocation.toString(), fileName, bit, "empty");
//
//                        Log.v("targetLocation_2", targetLocation.toString());
//                    } catch (NullPointerException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        dbh.close();
//                    }
//                }
//
//
////https://www.google.com/search?q=all+video+support+player+for+android+github&rlz=1C1CHBF_enIN853IN853&oq=all+video+support+player+for+android+github&aqs=chrome..69i57.33038j0j7&sourceid=chrome&ie=UTF-8
//                //publishCurrentProgressOneShot();
//
//                return new DetailingCreationActivity().DownloadingService.NoResultType();
//            }
//
//            public int getProgress() {
//                return mProgress;
//            }
//
//            public int getMrecsize() {
//                return mrecsize;
//            }
//
//            public int getMtotsize() {
//                return mtotsize;
//            }
//
//            public int getPosition() {
//                return mPosition;
//            }
//
//            public void cancel() {
//                mCancelled = true;
//            }
//        }
//
//        public Bitmap getResizedBitmap(String path, int newWidth, int newHeight) {
//            int width = 0;
//            int height = 0;
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            Bitmap bm = BitmapFactory.decodeFile(path, bmOptions);
//            try {
//                width = bm.getWidth();
//                height = bm.getHeight();
//            } catch (Exception e) {
//                Log.v("TASK_SIZE_are_ptint", e.getMessage());
//            }
//
//            float scaleWidth = ((float) newWidth) / width;
//            float scaleHeight = ((float) newHeight) / height;
//            // CREATE A MATRIX FOR THE MANIPULATION
//            Matrix matrix = new Matrix();
//            // RESIZE THE BIT MAP
//            matrix.postScale(scaleWidth, scaleHeight);
//
//            // "RECREATE" THE NEW BITMAP
//            Bitmap resizedBitmap = Bitmap.createBitmap(
//                    bm, 0, 0, width, height, matrix, false);
//            if (bm != null && !bm.isRecycled())
//                bm.recycle();
//
//            return resizedBitmap;
//        }
//
//        private void registerReceiver() {
//            unregisterReceiver();
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(HomeDashBoard.DownloadingService.ACTION_CANCEL_DOWNLOAD);
//            LocalBroadcastManager.getInstance(this).registerReceiver(mCommunicationReceiver, filter);
//            mReceiversRegistered = true;
//        }
//
//        private void unregisterReceiver() {
//            if (mReceiversRegistered) {
//                LocalBroadcastManager.getInstance(this).unregisterReceiver(mCommunicationReceiver);
//                mReceiversRegistered = false;
//            }
//        }
//
//        private final BroadcastReceiver mCommunicationReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (intent.getAction().equals(HomeDashBoard.DownloadingService.ACTION_CANCEL_DOWNLOAD)) {
//                    final long id = intent.getLongExtra(ID, -1);
//                    if (id != -1) {
//                        for (HomeDashBoard.DownloadingService.DownloadTask task : mTasks) {
//
//                        }
//                    }
//                }
//            }
//        };
//
//        class NoResultType {
//        }
//    }


//    public void progressBarAnimation(final int max) {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while (pBarCount < max) {
//                    ++pBarCount;
//                    try {
//                        Thread.sleep(30);
//                        pBar.setProgress(pBarCount);
//                        pbar_percentage.setText(max);
//                    } catch (Exception e) {
//
//                    }
//                }
//            }
//        }).start();
//    }


}
