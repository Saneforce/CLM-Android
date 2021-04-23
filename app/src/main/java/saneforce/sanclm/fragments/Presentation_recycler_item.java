package saneforce.sanclm.fragments;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.animoto.android.views.DraggableGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.DetailingCreationActivity;
import saneforce.sanclm.activities.Model.SavePresentation;
import saneforce.sanclm.adapter_class.Custom_Products_GridView_Contents;
import saneforce.sanclm.adapter_class.PresentationRecycleAdapter;
import saneforce.sanclm.adapter_class.PresentationSearchRecycleAdapter;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.GridSelectionList;
import saneforce.sanclm.util.PresentationRightUnselect;

public class Presentation_recycler_item extends Fragment {
    RecyclerView recycler;
    DataBaseHandler dbh;
    DetailingTrackerPOJO mDetailingTrackerPOJO;
    private ArrayList<Custom_Products_GridView_Contents> mProducts_GridView_Contents=new ArrayList<Custom_Products_GridView_Contents>();
    private ArrayList<Custom_Products_GridView_Contents> _product_list=new ArrayList<Custom_Products_GridView_Contents>();
    private ArrayList<Custom_Products_GridView_Contents> _product_list_arrange=new ArrayList<Custom_Products_GridView_Contents>();
    List list = new ArrayList();
    DraggableGridView dgv;
    ArrayList<Bitmap> arrBit=new ArrayList<>();
    ArrayList<Bitmap> OrgarrBit=new ArrayList<>();
    static GridSelectionList listener;
    Bitmap bitmap=null;
    int count=0;
    int j=0;
    LottieAnimationView animation_view;
    ProgressDialog dialog;
    PresentationRecycleAdapter adap;
    int i=0,i1=5;
    int dragFrom = -1;
    int dragTo = -1;
    CommonSharedPreference mCommonSharedPreference;
    String skipSpeciality;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drag_recycler_view, container);
        recycler=(RecyclerView)v.findViewById(R.id.rclist);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        recycler.setLayoutManager(gridLayoutManager);
        dbh=new DataBaseHandler(getActivity());
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
        mCommonSharedPreference=new CommonSharedPreference(getActivity());
        skipSpeciality=mCommonSharedPreference.getValueFromPreference("specFilter");
        mdDisplayProductInGridView("","",false);

        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            //and in your imlpementaion of
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // get the viewHolder's and target's positions in your adapter data, swap them
                // Collections.swap(/*RecyclerView.Adapter's data collection*/, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Collections.swap(_product_list, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Log.v("holder_pos_index ",viewHolder.getAdapterPosition()+" target_position "+target.getAdapterPosition());
                if(dragFrom == -1) {
                    dragFrom =  viewHolder.getAdapterPosition();
                }
                dragTo = target.getAdapterPosition();
                adap.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

                return true;
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                Log.v("clearView_calling","inside"+dragFrom+" dragto "+dragTo);
                if(dragFrom!=-1) {
                    _product_list_arrange.add(dragTo, _product_list_arrange.get(dragFrom));
                    int postt;
                    if (dragFrom > dragTo)
                        postt = dragFrom + 1;
                    else
                        postt = dragFrom;
                    _product_list_arrange.remove(postt);
                    dragFrom = -1;
                }
                for(int i=0;i<_product_list_arrange.size();i++)
                    Log.v("recycler_pridct",_product_list_arrange.get(i).getmLogoURL());
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }
        };
        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(recycler);


        Presentation_bottom_grid_selection.bindPresentation(new SavePresentation() {
            @Override
            public void saveDetail() {
                arrangeTheProduct();
                mProducts_GridView_Contents.addAll(_product_list_arrange);
                dbh.open();
               /* Collections.sort(mProducts_GridView_Contents,new SortListBrdId());
                Collections.reverse(mProducts_GridView_Contents);*/
                arrangeTheProductBasedOnSearch();

                /*for(int k=0;k<mProducts_GridView_Contents.size();k++){
                    Log.v("prd_grid_content ",mProducts_GridView_Contents.get(k).getmProductName());
                }*/
                //for(int i=mProducts_GridView_Contents.size()-1;i>=0;i--){
                for(int i=0;i<mProducts_GridView_Contents.size();i++){
                    Custom_Products_GridView_Contents prdd=mProducts_GridView_Contents.get(i);
                    Log.v("saveDetail_valuess_1", String.valueOf(prdd.getmProductName())+" selrc "+prdd.getmSelectionState()+" getmLocalUrl "+prdd.getmLogoURL());
                    long id = dbh.insert_into_group_mapping(prdd.getmPresentName() ,prdd.getmProductCode(),prdd.getmProductName(),prdd.getmFileName(),prdd.getmFiletype(),prdd.getmLogoURL(),String.valueOf(prdd.getmSelectionState()),"1","1","1","A");
                }
                mProducts_GridView_Contents.clear();
                dbh.close();

            }
        });

        PresentationSearchRecycleAdapter.bindListenerUnselect(new PresentationRightUnselect() {
            @Override
            public void unSelecting(String prdname) {
                Log.v("unselection_prd",prdname);
                for(int i=0;i<_product_list.size();i++){
                    if(_product_list.get(i).getmProductName().equalsIgnoreCase(prdname)){
                        _product_list.get(i).setmSelectionState(false);
                        _product_list_arrange.get(i).setmSelectionState(false);

                    }
                }
                adap.notifyDataSetChanged();
            }
        });
/*
        presntation_gridview_adapter.bindListenerUnselect(new PresentationRightUnselect() {
            @Override
            public void unSelecting(String prdname) {
                Log.v("unselection_prd",prdname);
                for(int i=0;i<_product_list.size();i++){
                    if(_product_list.get(i).getmProductName().equalsIgnoreCase(prdname)){
                        _product_list.get(i).setmSelectionState(false);
                        _product_list_arrange.get(i).setmSelectionState(false);

                    }
                }
                adap.notifyDataSetChanged();
            }
        });
*/

        DetailingCreationActivity.bindPresentation(new SavePresentation() {
            @Override
            public void saveDetail() {
                arrangeTheProduct();
                mProducts_GridView_Contents.addAll(_product_list_arrange);
                arrangeTheProductBasedOnSearch();
                dbh.open();
                /*Collections.sort(mProducts_GridView_Contents,new SortListBrdId());
                Collections.reverse(mProducts_GridView_Contents);*/

               /* for(int k=0;k<mProducts_GridView_Contents.size();k++){
                    Log.v("prd_grid_content ",mProducts_GridView_Contents.get(k).getmProductName());
                }*/
                String gpname="customised";
                for(int i=0;i<mProducts_GridView_Contents.size();i++){
                    Custom_Products_GridView_Contents prdd=mProducts_GridView_Contents.get(i);
                    Log.v("saveDetail_valuess_1", String.valueOf(prdd.getmProductName())+" selrc "+prdd.getmSelectionState()+" getmLocalUrl "+prdd.getmLogoURL());
                    if(Detailing_Selection_search_grid_selection.CustomClick)
                        prdd.setmPresentName(gpname);
                    long id = dbh.insert_into_group_mapping(prdd.getmPresentName() ,prdd.getmProductCode(),prdd.getmProductName(),prdd.getmFileName(),prdd.getmFiletype(),prdd.getmLogoURL(),String.valueOf(prdd.getmSelectionState()),"1","1","1","A");
                }
                mProducts_GridView_Contents.clear();
                dbh.close();

            }
        });


        return v;
    }

/*
    public void mdDisplayProductInGridView(final String mProductBrdName, final String mProductBrdCode, final boolean presentationlist) {


        Handler handler1 = new Handler();
        int ncountt=0;

        if(mDetailingTrackerPOJO.getmPrsn_svName().equalsIgnoreCase("")){mDetailingTrackerPOJO.setmPrsn_svName("1");}else{}

        try
        {
            dbh.open();
            list.clear();
            Boolean selectionstatus = true;
            int asc_count=0;
            count=0;
            arrangeTheProduct();
            boolean checkCount=false;
            mProducts_GridView_Contents.addAll(_product_list_arrange);
            Log.v("product_list_print", String.valueOf(mProducts_GridView_Contents.size())+" detail "+mDetailingTrackerPOJO.getmPrsn_svName()+" prd_nm "+mProductBrdName);
            _product_list.clear();
            _product_list_arrange.clear();
            adap=new PresentationRecycleAdapter(getActivity(),R.layout.row_item_recycler,_product_list);
            recycler.setAdapter(adap);
            Cursor mCursor=dbh.select_BrandwiseforPresentation(mProductBrdCode);
            if(mCursor.getCount()>0) {

                while (mCursor.moveToNext())
                {

                    Cursor mCursor1 = dbh.select_MappedFileName_GroupMapping(mProductBrdCode,mCursor.getString(5),mDetailingTrackerPOJO.getmPrsn_svName());
                    Log.v("Cursor_count_present_11", String.valueOf(mCursor1.getCount()));
                    if(mCursor1.getCount()>0){
                        mCursor1.moveToFirst();
                        Log.v("Cursor_count_present", String.valueOf(mCursor1.getString(1))+"selection_Status"+mCursor1.getString(0));
                        selectionstatus= Boolean.valueOf(mCursor1.getString(0));
                        asc_count=mCursor1.getInt(1);
                        if(mProducts_GridView_Contents.size()!=0 && CommonUtils.TAG_VIEW_PRESENTATION.equalsIgnoreCase("0"))
                            selectionstatus=findProductSelection(mCursor.getString(12));
                        checkCount=true;
                    }
                    else{
                        selectionstatus=true;
                        asc_count=0;
                        if(mProducts_GridView_Contents.size()!=0)
                            selectionstatus=findProductSelection(mCursor.getString(12));
                        checkCount=false;
                    }

                    Bitmap bb=StringToBitMap(mCursor.getString(14));

                    Log.v("selection_list_choosen", selectionstatus+" "+mCursor.getString(12));
                    // long id = dbh.insert_into_group_mapping(mDetailingTrackerPOJO.getmPrsn_svName() ,mProductBrdCode,mProductBrdName,mCursor.getString(5),mCursor.getString(6),mCursor.getString(12),String.valueOf(selectionstatus),"1","1","1","A");
                    Custom_Products_GridView_Contents products= new Custom_Products_GridView_Contents(mCursor.getString(5),mCursor.getString(2), mCursor.getString(3), mCursor.getString(12),
                            selectionstatus, CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS,mCursor.getString(6),mDetailingTrackerPOJO.getmPrsn_svName(),asc_count,bb);
                   // mProducts_GridView_Contents.add(products);
                    Log.v("saved_presentation_view",mCursor.getString(12)+" asc_count "+asc_count);
                    _product_list.add(products);
                    _product_list_arrange.add(products);

                    if(presentationlist){
                       */
/* Log.v("presentation_sort_s",_product_list.size()+" "+_product_list.size());
                        Collections.sort(_product_list,new SortListColumn());
                        Collections.sort(_product_list_arrange,new SortListColumn());
                        for(int i=0;i<_product_list_arrange.size();i++){
                            Log.v("presentation_sort_sa",_product_list.size()+" "+_product_list.size());
                            Log.v("after_ordering_list",_product_list_arrange.get(i).getmLogoURL());
                        }*//*


                        //Collections.reverse(_product_list);
                    }
                    else {

                        adap.notifyItemInserted(_product_list.size());
                    }


                       */
/* new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        },1000);*//*



*/
/*
                    if(presentationlist){
                        Collections.sort(_product_list,new Presentation_drag_grid_view.SortListColumn());
                        Collections.reverse(_product_list);
                    }
*//*


                }

                if(presentationlist){
                    Log.v("true_presentation","are_printed_here");
                        adap.notifyItemInserted(_product_list.size());

                }

                adap.notifyDataSetChanged();



*/
/*
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        while(i<i1){
                            Custom_Products_GridView_Contents products=mProducts_GridView_Contents.get(i);
                            _product_list.add(products);
                        }
                        i1=i1+5;
                        adap=new PresentationRecycleAdapter(getActivity(),mProducts_GridView_Contents);
                        recycler.setAdapter(adap);
                    }
                },300);
*//*




            }else{
                // Toast.makeText(getActivity(), "No Products available", Toast.LENGTH_LONG).show();
            }
            boolean presentationClicked=mCommonSharedPreference.getBooleanValueFromPreference("presentationList");
            if(presentationClicked && checkCount){
                Log.v("presentation_sort_s",_product_list.size()+" "+_product_list.size());
                Collections.sort(_product_list,new SortListColumn());
                Collections.sort(_product_list_arrange,new SortListColumn());
                for(int i=0;i<_product_list_arrange.size();i++){
                    Log.v("presentation_sort_sa",_product_list.size()+" "+_product_list.size());
                    Log.v("after_ordering_list",_product_list_arrange.get(i).getmLogoURL());
                }
                adap.notifyDataSetChanged();
                //Collections.reverse(_product_list);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            dbh.close();
        }
        */
/* dialog.dismiss();*//*


        //mGridview.setAdapter( list);  /// loading all slides in right side fragment
    }
*/
    public void mdDisplayProductInGridView(final String mProductBrdName, final String mProductBrdCode, final boolean presentationlist) {


        Handler handler1 = new Handler();
        int ncountt=0;

        if(mDetailingTrackerPOJO.getmPrsn_svName().equalsIgnoreCase("")){mDetailingTrackerPOJO.setmPrsn_svName("1");}else{}

        try {
            dbh.open();
            list.clear();
            Boolean selectionstatus = true;
            int asc_count = 0;
            count = 0;
            //arrangeTheProduct();
            boolean checkCount = false;
            if (_product_list_arrange.contains(new Custom_Products_GridView_Contents(true))){
                Log.v("not_avail_in345","listtts");
                mProducts_GridView_Contents.addAll(_product_list_arrange);
            }
            else{
                Log.v("not_avail_in","listtts");
            }

            Log.v("product_list_print", String.valueOf(_product_list_arrange.size()) + " detail " + mDetailingTrackerPOJO.getmPrsn_svName() + " prd_nm " + mProductBrdName);
            _product_list.clear();
            _product_list_arrange.clear();
            adap = new PresentationRecycleAdapter(getActivity(), R.layout.row_item_recycler, _product_list);
            recycler.setAdapter(adap);
            if (mProducts_GridView_Contents.contains(new Custom_Products_GridView_Contents(mProductBrdName))) {
                for(int k=0;k<mProducts_GridView_Contents.size();k++){
                    Custom_Products_GridView_Contents mmm=mProducts_GridView_Contents.get(k);
                    Log.v("prd_name_present",mmm.getmLogoURL()+" select "+mmm.getmSelectionState());
                    if(mmm.getmProductName().equalsIgnoreCase(mProductBrdName)){

                        _product_list.add(mProducts_GridView_Contents.get(k));
                        _product_list_arrange.add(mProducts_GridView_Contents.get(k));
                        mProducts_GridView_Contents.remove(k);
                        --k;
                    }

                }
            }
            else{ Cursor mCursor;
            Log.v("presentation_count_are",mCommonSharedPreference.getValueFromPreference("present"));
                if(mCommonSharedPreference.getValueFromPreference("present").equalsIgnoreCase("yes") || skipSpeciality.equalsIgnoreCase("0"))
                    mCursor = dbh.select_BrandwiseforPresentationWithoutSpec(mProductBrdCode);
                else
                    mCursor = dbh.select_BrandwiseforPresentation(mProductBrdCode,CommonUtils.TAG_DR_SPEC);

            if (mCursor.getCount() > 0) {

                while (mCursor.moveToNext()) {

                    Cursor mCursor1 = dbh.select_MappedFileName_GroupMapping(mProductBrdCode, mCursor.getString(5), mDetailingTrackerPOJO.getmPrsn_svName());
                    Log.v("Cursor_count_present_11", String.valueOf(mCursor1.getCount()));
                    if (mCursor1.getCount() > 0) {
                        mCursor1.moveToFirst();
                        Log.v("Cursor_count_present", String.valueOf(mCursor1.getString(1)) + "selection_Status" + mCursor1.getString(0));
                        selectionstatus = Boolean.valueOf(mCursor1.getString(0));
                        asc_count = mCursor1.getInt(1);
                        /*if (mProducts_GridView_Contents.size() != 0 && CommonUtils.TAG_VIEW_PRESENTATION.equalsIgnoreCase("0"))
                            selectionstatus = findProductSelection(mCursor.getString(12));*/
                        checkCount = true;
                    } else {
                        selectionstatus = false;
                        asc_count = 0;
                       /* if (mProducts_GridView_Contents.size() != 0)
                            selectionstatus = findProductSelection(mCursor.getString(12));*/
                        checkCount = false;
                    }

                    Bitmap bb = StringToBitMap(mCursor.getString(14));


                    int val=Presentation_search_grid_selection.list_order.indexOf(mCursor.getString(3));
                    Log.v("selection_list_choosen", selectionstatus + " " + mCursor.getString(12)+"listt "+val);
                    // long id = dbh.insert_into_group_mapping(mDetailingTrackerPOJO.getmPrsn_svName() ,mProductBrdCode,mProductBrdName,mCursor.getString(5),mCursor.getString(6),mCursor.getString(12),String.valueOf(selectionstatus),"1","1","1","A");
                    Custom_Products_GridView_Contents products = new Custom_Products_GridView_Contents(mCursor.getString(5), mCursor.getString(2), mCursor.getString(3), mCursor.getString(12),
                            selectionstatus, CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS, mCursor.getString(6), mDetailingTrackerPOJO.getmPrsn_svName(), asc_count, bb,val,mCursor.getString(1));
                    // mProducts_GridView_Contents.add(products);
                    Log.v("saved_presentation_view", mCursor.getString(12) + " asc_count " + asc_count);
                    _product_list.add(products);
                    _product_list_arrange.add(products);

                    if (presentationlist) {
                       /* Log.v("presentation_sort_s",_product_list.size()+" "+_product_list.size());
                        Collections.sort(_product_list,new SortListColumn());
                        Collections.sort(_product_list_arrange,new SortListColumn());
                        for(int i=0;i<_product_list_arrange.size();i++){
                            Log.v("presentation_sort_sa",_product_list.size()+" "+_product_list.size());
                            Log.v("after_ordering_list",_product_list_arrange.get(i).getmLogoURL());
                        }*/

                        //Collections.reverse(_product_list);
                    } else {

                        adap.notifyItemInserted(_product_list.size());
                    }


                       /* new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        },1000);*/


/*
                    if(presentationlist){
                        Collections.sort(_product_list,new Presentation_drag_grid_view.SortListColumn());
                        Collections.reverse(_product_list);
                    }
*/

                }

                if (presentationlist) {
                    Log.v("true_presentation", "are_printed_here");
                    adap.notifyItemInserted(_product_list.size());
                }
                adap.notifyDataSetChanged();



/*
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        while(i<i1){
                            Custom_Products_GridView_Contents products=mProducts_GridView_Contents.get(i);
                            _product_list.add(products);
                        }
                        i1=i1+5;
                        adap=new PresentationRecycleAdapter(getActivity(),mProducts_GridView_Contents);
                        recycler.setAdapter(adap);
                    }
                },300);
*/


            } else {
                // Toast.makeText(getActivity(), "No Products available", Toast.LENGTH_LONG).show();
            }
        }
            adap.notifyItemInserted(_product_list.size());
            adap.notifyDataSetChanged();
            boolean presentationClicked=mCommonSharedPreference.getBooleanValueFromPreference("presentationList");
            if(presentationClicked && checkCount){
                Log.v("presentation_sort_s",_product_list.size()+" "+_product_list.size());
                Collections.sort(_product_list,new SortListColumn());
                Collections.sort(_product_list_arrange,new SortListColumn());
                Collections.reverse(_product_list);
                Collections.reverse(_product_list_arrange);
                for(int i=0;i<_product_list_arrange.size();i++){
                    Log.v("presentation_sort_sa",_product_list.size()+" "+_product_list.size());
                    Log.v("after_ordering_list",_product_list_arrange.get(i).getmLogoURL());
                }
                adap.notifyDataSetChanged();
                //Collections.reverse(_product_list);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            dbh.close();
        }
        /* dialog.dismiss();*/

        //mGridview.setAdapter( list);  /// loading all slides in right side fragment
    }
    public Bitmap StringToBitMap(String encodedString){
        Bitmap bitmap=null;
        if(bitmap!=null){
            bitmap.recycle();
            bitmap=null;
        }
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
             bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            Log.v("how_to_sort_out",e.getMessage());
            e.getMessage();
            return null;
        }
    }

    class SortListColumn implements Comparator<Custom_Products_GridView_Contents> {

        @Override
        public int compare(Custom_Products_GridView_Contents e1, Custom_Products_GridView_Contents e2) {
            if(e1.getColumnid() < e2.getColumnid()){
                return 1;
            } else {
                return -1;
            }
        }
    }
    class SortListBrdId implements Comparator<Custom_Products_GridView_Contents> {

        @Override
        public int compare(Custom_Products_GridView_Contents e1, Custom_Products_GridView_Contents e2) {
            if(e1.getBrdid() < e2.getBrdid()){
                return 1;
            } else {
                return -1;
            }
        }
    }

    public void arrangeTheProduct(){
        for(int k=0;k<_product_list.size();k++){
            for(int j=0;j<_product_list_arrange.size();j++){
                if(_product_list.get(k).getmLogoURL().equalsIgnoreCase(_product_list_arrange.get(j).getmLogoURL())){
                    _product_list_arrange.get(j).setmSelectionState(_product_list.get(k).getmSelectionState());
                    j=_product_list_arrange.size();
                }
            }
        }
    }

    public boolean findProductSelection(String path){
        boolean val=true;
        for(int i=mProducts_GridView_Contents.size()-1;i>=0;i--){
            Custom_Products_GridView_Contents prdd=mProducts_GridView_Contents.get(i);
            if(path.equalsIgnoreCase(prdd.getmLogoURL()))
                val=false;
        }
        for(int i=mProducts_GridView_Contents.size()-1;i>=0;i--){
            Custom_Products_GridView_Contents prdd=mProducts_GridView_Contents.get(i);
            Log.v("saveDetail_valuess_1", String.valueOf(prdd.getmProductName())+" selrc "+prdd.getmSelectionState()+" getmLocalUrl "+prdd.getmLogoURL());
            if(path.equalsIgnoreCase(prdd.getmLogoURL()))
                return prdd.getmSelectionState();

        }
        return val;
    }

    public void arrangeTheProductBasedOnSearch(){
        for(int i=0;i<Presentation_search_grid_selection._product_list_arrange.size();i++)
            Log.v("recycler_pridct",Presentation_search_grid_selection._product_list_arrange.get(i).getmProductName()+" i_val "+i+" from_prd ");
      ArrayList<Custom_Products_GridView_Contents> dummy=new ArrayList<Custom_Products_GridView_Contents>();
        if(Presentation_search_grid_selection._product_list_arrange.size()!=0){
            for(int i=0;i<Presentation_search_grid_selection._product_list_arrange.size();i++){
                if (mProducts_GridView_Contents.contains(new Custom_Products_GridView_Contents
                        (Presentation_search_grid_selection._product_list_arrange.get(i).getmProductName()))) {

                    for(int k=0;k<mProducts_GridView_Contents.size();k++){
                        if(mProducts_GridView_Contents.get(k).getmProductName().equalsIgnoreCase
                                (Presentation_search_grid_selection._product_list_arrange.get(i).getmProductName())){
                            dummy.add(mProducts_GridView_Contents.get(k));
                        }
                    }
                }
            }
            mProducts_GridView_Contents.clear();
            Presentation_search_grid_selection._product_list_arrange.clear();
            mProducts_GridView_Contents.addAll(dummy);
        }
    }



}
