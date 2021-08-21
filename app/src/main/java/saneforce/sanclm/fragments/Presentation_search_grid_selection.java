package saneforce.sanclm.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.PresentationCreationMainActivity;
import saneforce.sanclm.adapter_class.Custom_Products_GridView_Contents;
import saneforce.sanclm.adapter_class.PresentationRecycleAdapter;
import saneforce.sanclm.adapter_class.PresentationSearchRecycleAdapter;
import saneforce.sanclm.adapter_interfaces.OnSelectGridViewListener;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.CustomiseSearchOption;
import saneforce.sanclm.util.GridSelectionList;

public class Presentation_search_grid_selection extends Fragment implements AdapterView.OnItemClickListener,OnSelectGridViewListener,View.OnTouchListener {
    View v;
    private Communicator mappingActivityCommunicator;
    RecyclerView mGridview;
    private ArrayList<Custom_Products_GridView_Contents> mProducts_GridView_Contents=new ArrayList<Custom_Products_GridView_Contents>();
   static public ArrayList<Custom_Products_GridView_Contents> _product_list_arrange=new ArrayList<Custom_Products_GridView_Contents>();
    //presntation_gridview_adapter _presntation_search_gridview_adapter;
    PresentationSearchRecycleAdapter _presntation_search_gridview_adapter;
    ArrayList<String> al = new ArrayList<String>();
    CommonUtilsMethods commonUtilsMethods;
    DetailingTrackerPOJO mDetailingTrackerPOJO;
    DataBaseHandler dbh;
    Cursor mCursor;
    Boolean selectionstatus;
    ArrayList<String> listViewItems = new ArrayList<String>();
    static ArrayList<String> list_order = new ArrayList<String>();
    ListView lv_brdmatrix_list;
    LinearLayout ll_txt,ll_img,ll_det_serach_brdmtrx_sel;
    static CustomiseSearchOption customiseOption;
    public static int lastItemPresentation=0;
    CommonSharedPreference mCommonSharedPreference;
    int dragFrom = -1;
    int dragTo = -1;
    ArrayList<String> prdctNaming=new ArrayList<>();
    String skipSpeciality;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if (activity instanceof Communicator){
            mappingActivityCommunicator = (Communicator) activity;
        }else{
            throw new ClassCastException(activity.toString()+ " must implemenet MyListFragment.Communicator");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.presentation_search_left, container);

      /*  getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);*/

        dbh = new DataBaseHandler(getActivity());
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
        mGridview = (RecyclerView) v.findViewById(R.id.gv_presentation_creation_search);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        mGridview.setLayoutManager(gridLayoutManager);
        /*mGridview.setOnItemClickListener(this);*/
        mDetailingTrackerPOJO.setmPrsn_svName("");
        Log.v("customise_option","insided_here");
        mCommonSharedPreference=new CommonSharedPreference(getActivity());
        skipSpeciality=mCommonSharedPreference.getValueFromPreference("specFilter");
        mdDisplayProductInGridView("");

        lv_brdmatrix_list = (ListView) v.findViewById(R.id.detsearch_listview);

        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            //and in your imlpementaion of
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // get the viewHolder's and target's positions in your adapter data, swap them
                // Collections.swap(/*RecyclerView.Adapter's data collection*/, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Collections.swap(mProducts_GridView_Contents, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Log.v("holder_pos_index ",viewHolder.getAdapterPosition()+" target_position "+target.getAdapterPosition());
                if(dragFrom == -1) {
                    dragFrom =  viewHolder.getAdapterPosition();
                }
                dragTo = target.getAdapterPosition();
                _presntation_search_gridview_adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

               /* for(int i=0;i<mProducts_GridView_Contents.size();i++){
                    Log.v("mProduct_gridevie",mProducts_GridView_Contents.get(i).getmProductName());
                }*/
                return true;
            }
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                Log.v("clearView_calling","inside"+dragFrom+" dragto "+dragTo);
                if(dragFrom!=-1) {
                    if(dragTo>dragFrom){
                        dragTo=dragTo+1;
                    }
                    _product_list_arrange.add(dragTo, _product_list_arrange.get(dragFrom));
                    Log.v("recycler_pridcting",_product_list_arrange.get(2).getmProductName());
                    int postt;
                    if (dragFrom > dragTo)
                        postt = dragFrom + 1;
                    else
                        postt = dragFrom;
                    _product_list_arrange.remove(postt);
                    dragFrom = -1;
                }
               /* for(int i=0;i<_product_list_arrange.size();i++)
                    Log.v("recycler_pridct",_product_list_arrange.get(i).getmProductName()+" i_val "+i+" from_prd "+mProducts_GridView_Contents.get(i).getmProductName());*/
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP);
            }
        };

        ll_txt = (LinearLayout) v.findViewById(R.id.ll_txt);
        ll_img = (LinearLayout) v.findViewById(R.id.ll_img);
        ll_det_serach_brdmtrx_sel = (LinearLayout) v.findViewById(R.id.ll_det_serach_brdmtrx_sel);
        ll_txt.setOnTouchListener(this);
        ll_img.setOnTouchListener(this);
        ll_det_serach_brdmtrx_sel.setVisibility(View.GONE);
        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(mGridview);

       /* if(!Detailing_Selection_search_grid_selection.CustomClick)
            ll_det_serach_brdmtrx_sel.setVisibility(View.GONE);
        else
            ll_det_serach_brdmtrx_sel.setVisibility(View.VISIBLE);*/
        PresentationRecycleAdapter.bindListener(new GridSelectionList() {
            @Override
            public void selectionList(String prdName) {

                for(int i=0;i<mProducts_GridView_Contents.size();i++){
                    if(mProducts_GridView_Contents.get(i).getmProductName().equalsIgnoreCase(prdName)){
                        mProducts_GridView_Contents.get(i).setmSelectionState(false);
                        _presntation_search_gridview_adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void unselectionList(String prdNAme) {
                for(int i=0;i<mProducts_GridView_Contents.size();i++){
                    if(mProducts_GridView_Contents.get(i).getmProductName().equalsIgnoreCase(prdNAme)){
                        mProducts_GridView_Contents.get(i).setmSelectionState(true);
                        _presntation_search_gridview_adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        lv_brdmatrix_list.setVisibility(View.GONE);
        return v;
    }
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        try{
          // init();
        }catch(Exception e){
            // Toast.makeText(getActivity(), ""+e, Toast.LENGTH_LONG).show();
        }
    }

    public void visibilityHeader(){

            ll_det_serach_brdmtrx_sel.setVisibility(View.VISIBLE);
    }

/*
     void init() {
         dbh = new DataBaseHandler(getActivity());
         mGridview = (GridView) v.findViewById(R.id.gv_presentation_creation_search);
         mGridview.setOnItemClickListener(this);

         mdDisplayProductInGridView("");
    }
*/

    public void mdDisplayProductInGridView(String PresentationName){
        try
        {
            mProducts_GridView_Contents.clear();
            _product_list_arrange.clear();
            dbh.open();
            Cursor mCursor = null;
            Cursor mCursor1 = null;
            Custom_Products_GridView_Contents _products = null;
            Log.v("printing_spec_prese",CommonUtils.TAG_DR_SPEC+" present "+mCommonSharedPreference.getValueFromPreference("present"));
            Boolean selectionstatus = false;
            if(mCommonSharedPreference.getValueFromPreference("present").equalsIgnoreCase("yes") )
                mCursor=dbh.select_ProductBrdWiseSlide();
//            else
//                mCursor=dbh.select_ProductBrdWiseSlideSpec(CommonUtils.TAG_DR_SPEC);
            Log.v("in_presentation",mCursor.getCount()+" printing");
                if(mCursor!=null)
                    if(mCursor.getCount()>0) {
                        while (mCursor.moveToNext())
                        {
                            mCursor1 = dbh.select_MappedBrd_GroupMapping(mCursor.getString(0),PresentationName);
                            Log.v("selection_State_gridd",String.valueOf(selectionstatus)+"code "+mCursor.getString(0)+" name "+mCursor.getString(5));
                            if(mCursor1.getCount()>0) selectionstatus=true;	else selectionstatus=false;
                            if(selectionstatus) lastItemPresentation=mProducts_GridView_Contents.size();
                            Bitmap bb=null;
                            if(mCursor.getString(2).contains(".pdf"))
                                bb=StringToBitMap(mCursor.getString(8));
                            _products = new Custom_Products_GridView_Contents(mCursor.getString(1), mCursor.getString(0), mCursor.getString(5), mCursor.getString(2),
                                    selectionstatus, CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING, mCursor.getString(6),bb);
                            mProducts_GridView_Contents.add(_products);
                            _product_list_arrange.add(_products);
                            list_order.add(mCursor.getString(5));
                        }
                    }else{
                        Toast.makeText(getActivity(), getResources().getString(R.string.noprdavailable), Toast.LENGTH_LONG).show();
                    }

            prdctNaming.clear();
                    if(!TextUtils.isEmpty(PresentationName)) {
                        mCursor = dbh.select_products_Slides_PresntationWise(PresentationName);
                        if (mCursor.getCount() > 0) {
                            while (mCursor.moveToNext()) {
                                prdctNaming.add(mCursor.getString(5));
                                Log.v("product_full_name",mCursor.getString(5));
                            }
                            ArrayList<Custom_Products_GridView_Contents> dummy = new ArrayList<Custom_Products_GridView_Contents>();
                            for(int j=0;j<prdctNaming.size();j++) {
                                for (int i = 0; i < mProducts_GridView_Contents.size(); i++) {
                                    if (prdctNaming.get(j).equalsIgnoreCase(mProducts_GridView_Contents.get(i).getmProductName())) {
                                        dummy.add(mProducts_GridView_Contents.get(i));

                                    }
                                }
                            }
                            for (int k = 0; k < mProducts_GridView_Contents.size(); k++) {
                                if (dummy.contains(new Custom_Products_GridView_Contents(mProducts_GridView_Contents.get(k).getmProductName()))) {
                                } else {
                                    dummy.add(mProducts_GridView_Contents.get(k));
                                }
                            }

                            mProducts_GridView_Contents.clear();
                            mProducts_GridView_Contents.addAll(dummy);
                        }
                    }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            dbh.close();
        }
        _presntation_search_gridview_adapter=new PresentationSearchRecycleAdapter(getActivity(), R.layout.custom_presentation_search_gridview,
                mProducts_GridView_Contents,Presentation_search_grid_selection.this);
        mGridview.setAdapter(_presntation_search_gridview_adapter);

    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void LoadGroupName() {
        //populate_spinner_items();
    }

    private ArrayList<String> populate_spinner_items() {
        ArrayList<String> GPNAME = new ArrayList<String>();
        Cursor mCursor = null;
        dbh = new DataBaseHandler(getActivity());
       /* try{
            dbh.open();
            mCursor = dbh.select_GroupName_Presentation_Mapping();
            mGroupName.clear();

            if (mCursor.getCount() > 0) {
                GPNAME.add(gpName);  //add select group name in first position
                while (mCursor.moveToNext()) {
                    GPNAME.add(mCursor.getString(0));
                }
            }else{
                GPNAME.add(gpName);
            }
            mGroupName.addAll(GPNAME);
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            dbh.close();
        }

        ArrayAdapter groupName = null;
        groupName = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,GPNAME);
        groupName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_groupname.setAdapter(groupName);
        return GPNAME;*/
       return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onTaskComplete(String productBrdName, String ProductBrdCode) {
        updateFragment(productBrdName,ProductBrdCode);
        Log.d("grid selected", "yes"+ String.valueOf(ProductBrdCode)+"////"+productBrdName);
    }
    private void updateFragment(String Selectedname, String productcode) {
        mappingActivityCommunicator.Message(Selectedname,productcode);
        PresentationCreationMainActivity.presentationList=false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.ll_txt:
                Log.v("first_time_ppt_touch","here");
                if (lv_brdmatrix_list.getVisibility() == View.VISIBLE) {
                    lv_brdmatrix_list.setVisibility(View.GONE);
                    mGridview.setVisibility(View.VISIBLE);
                } else {
                    lv_brdmatrix_list.setVisibility(View.VISIBLE);
                    mGridview.setVisibility(View.GONE);
                    displayItems();
                }
                break;
        }
        return false;
    }

    public interface Communicator {
        public void Message(String PdtBrdName, String productcode);
    }

    public void displayItems(){
        listViewItems.clear();
        listViewItems.add(getResources().getString(R.string.brandmatrix));
        listViewItems.add(getResources().getString(R.string.speciality));
        listViewItems.add(getResources().getString(R.string.allbrand));
        lv_brdmatrix_list.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.listview_items, listViewItems));
        lv_brdmatrix_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lv_brdmatrix_list.setVisibility(View.GONE);
                mGridview.setVisibility(View.VISIBLE);
                mCommonSharedPreference.setValueToPreference("display_brand",adapterView.getItemAtPosition(i).toString());
                customiseOption.viewSearchOption();
            }
        });

    }

    public static void bindListenerCustomiseList(CustomiseSearchOption customiseOption1){
        customiseOption=customiseOption1;
    }

}
