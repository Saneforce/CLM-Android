package saneforce.sanclm.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.adapter_class.Custom_Products_GridView_Contents;
import saneforce.sanclm.adapter_class.PresentationRecyclerAdapter;
import saneforce.sanclm.adapter_class.presntation_gridview_adapter;
import saneforce.sanclm.adapter_interfaces.OnSelectGridViewListener;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class Presentation_right_grid_view extends Fragment implements AdapterView.OnItemClickListener,View.OnTouchListener,OnSelectGridViewListener {

    View v;
    RelativeLayout rl_present_view;
    GridView mGridview;
    RecyclerView recyclerView;
    presntation_gridview_adapter _presntation_search_gridview_adapter;
    ArrayList<String> al = new ArrayList<String>();
    DataBaseHandler dbh;
    DetailingTrackerPOJO mDetailingTrackerPOJO;
    static PresentationRecyclerAdapter mProducts_GridView_Adapter=null;
    private  ArrayList<Custom_Products_GridView_Contents> mProducts_GridView_Contents=new ArrayList<Custom_Products_GridView_Contents>();
    List list = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.presentation_right_view, container);
        dbh=new DataBaseHandler(getActivity());
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
        rl_present_view = (RelativeLayout) v.findViewById(R.id.presn_rl_view);
       // mGridview = (GridView) v.findViewById(R.id.gv_presentation_creation);
      /*  recyclerView = (RecyclerView) v.findViewById(R.id.gv_presentation_creation);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        recyclerView.setLayoutManager(gridLayoutManager);
*/
       // mGridview.setOnItemClickListener(this);
        //rl_present_view.setOnTouchListener(this);
        mdDisplayProductInGridView("","");
        return v;
    }
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        try{
            init();
        }catch(Exception e){
            // Toast.makeText(getActivity(), ""+e, Toast.LENGTH_LONG).show();
        }
    }

    private void init() {

    }

    public void mdDisplayProductInGridView(String mProductBrdName,String mProductBrdCode)
    {
        if(mDetailingTrackerPOJO.getmPrsn_svName().equalsIgnoreCase("")){mDetailingTrackerPOJO.setmPrsn_svName("1");}else{}

        try
        {
            dbh.open();
            mProducts_GridView_Contents.clear();
            list.clear();
            Boolean selectionstatus;
            Cursor mCursor=dbh.select_BrandwiseforPresentation(mProductBrdCode);
          //  mDemoDetailsTracker=new DemoDetailsTracker();
            if(mCursor.getCount()>0) {
                while (mCursor.moveToNext())
                {
                    Cursor mCursor1 = dbh.select_MappedFileName_GroupMapping(mProductBrdCode,mCursor.getString(5),mDetailingTrackerPOJO.getmPrsn_svName());

                    if(mCursor1.getCount()>0) selectionstatus=false;	else selectionstatus=true;

                    long id = dbh.insert_into_group_mapping(mDetailingTrackerPOJO.getmPrsn_svName() ,mProductBrdCode,mProductBrdName,mCursor.getString(5),mCursor.getString(6),mCursor.getString(12),String.valueOf(selectionstatus),"1","1","1","A");
                    Bitmap bb=null;
                    if(mCursor.getString(12).contains(".pdf")){
                        bb=getResizedBitmapForPdf(getBitmap(new File(mCursor.getString(2))),150,150);
                    }
                    Custom_Products_GridView_Contents products= new Custom_Products_GridView_Contents(mCursor.getString(5),mCursor.getString(2), mCursor.getString(3), mCursor.getString(12),
                            selectionstatus, CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS,mCursor.getString(6),bb);
                    list.add(products.getmFileName());
                    mProducts_GridView_Contents.add(products);
                }


            }else{
               // Toast.makeText(getActivity(), "No Products available", Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            dbh.close();
        }

        _presntation_search_gridview_adapter=new presntation_gridview_adapter(getActivity(), R.layout.custom_presentation_search_gridview,
                mProducts_GridView_Contents,Presentation_right_grid_view.this);
        mGridview.setAdapter(_presntation_search_gridview_adapter);
      //  mProducts_GridView_Adapter=new PresentationRecyclerAdapter(getActivity(), R.layout.custom_presentation_search_gridview,
              //  mProducts_GridView_Contents);
       /* ItemTouchHelper.Callback callback =
                new ItemMoveCallback(mProducts_GridView_Adapter,getActivity());
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);*/
       // recyclerView.setAdapter(mProducts_GridView_Adapter);
       // mProducts_GridView_Adapter.notifyDataSetChanged();
     /*   mProducts_GridView_Adapter.setHandleDragEnabled(true); // default true
        mProducts_GridView_Adapter.setLongPressDragEnabled(true); // default true
        mProducts_GridView_Adapter.setSwipeEnabled(true); // default true

        mProducts_GridView_Adapter.setOnItemClickListener(new SimpleClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                super.onItemClick(v, pos);
                Toast.makeText(getActivity(), "onItemClick\npos: " + pos + " text: "
                        + mProducts_GridView_Adapter.getData().get(pos), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View v, int pos) {
                super.onItemClick(v, pos);
                Toast.makeText(getActivity(), "onItemLongClick\npos: " + pos + " text: "
                        + mProducts_GridView_Adapter.getData().get(pos), Toast.LENGTH_SHORT).show();
            }
        });

        mProducts_GridView_Adapter.setOnItemDragListener(new SimpleDragListener() {

            @Override
            public void onDrop(int fromPosition, int toPosition) {  // single callback
                super.onDrop(fromPosition, toPosition);
                Log.i("drag", "onDrop " + fromPosition + " -> " + toPosition);
            }

            @Override
            public void onSwiped(int pos) {
                super.onSwiped(pos);
                Log.d("drag", "onSwiped " + pos);
                Toast.makeText(getActivity(), "onSwiped\npos: " + pos, Toast.LENGTH_SHORT).show();
            }
        });*/
        //mGridview.setAdapter( list);  /// loading all slides in right side fragment
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){

            case R.id.presn_rl_view:
                CommonUtilsMethods.FullScreencall(getActivity());
                break;
        }
        return false;
    }


    @Override
    public void onTaskComplete(String ProductBrdName, String ProductBrdCode) {

    }
    public static ParcelFileDescriptor openFile1(File file) {
        ParcelFileDescriptor descriptor;
        try {
            descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return descriptor;
    }
    public Bitmap getBitmap(File file){
        int pageNum = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(getActivity());
        try {
            PdfDocument pdfDocument = pdfiumCore.newDocument(openFile1(file));
            pdfiumCore.openPage(pdfDocument, pageNum);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);


            // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
            // RGB_565 - little worse quality, twice less memory usage
            Bitmap bitmap = Bitmap.createBitmap(width , height ,
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
    public  Bitmap getResizedBitmapForPdf(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

}
