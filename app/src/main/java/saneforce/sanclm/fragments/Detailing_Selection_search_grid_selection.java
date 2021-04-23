package saneforce.sanclm.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.DetailingCreationActivity;
import saneforce.sanclm.activities.DetailingFullScreenImageViewActivity;
import saneforce.sanclm.adapter_class.Custom_Products_GridView_Contents;
import saneforce.sanclm.adapter_class.Detailing_gridview_adapter;
import saneforce.sanclm.adapter_interfaces.OnSelectGridViewListener;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.CustomiseOption;
import saneforce.sanclm.util.Customize_slide;
import saneforce.sanclm.util.SpecialityListener;
import saneforce.sanclm.util.TherapticListener;

public class Detailing_Selection_search_grid_selection extends Fragment implements View.OnTouchListener, OnSelectGridViewListener {
    TextView tv_brdmatrx_set;
    ImageView spec_img, down_arrow,cat_img;
    ListView lv_brdmatrix_list;
    GridView mGridview;
    private Communicator mappingActivityCommunicator;
    private ArrayList<Custom_Products_GridView_Contents> mProducts_GridView_Contents = new ArrayList<Custom_Products_GridView_Contents>();
    Detailing_gridview_adapter _deDetailing_gridview_adapter;
    ArrayList<String> listViewItems = new ArrayList<String>();
    View v;
    DataBaseHandler dbh;
    Cursor mCursor;
    DetailingTrackerPOJO DetailingTrackerPOJO;
    ArrayList<String> BrdCodeArray = new ArrayList<String>();
    ArrayList<String> BrdWiseSlidesDetails = new ArrayList<String>();
    ArrayList<String> currentSlidesArray = new ArrayList<String>();
    ArrayList<String> BrdNameArray = new ArrayList<String>();
    CommonSharedPreference mCommonSharedPreference;
    public static int len_slide = 0;
    //RelativeLayout ll_img;
    LinearLayout ll_img,ll_txt,ll_layoutCat;
    public static boolean checkActivity = false;
    Presentation_search_grid_selection _pPresentation_search_grid_selection;
    Detailing_Selection_search_grid_selection _pDetailing_Selection_search_grid_selection;
    public static Customize_slide customize_slide;
    public static boolean CustomClick = false;
    static CustomiseOption customiseOption;
    ArrayList<String> specDrProdCode = new ArrayList<>();
    String skipSpeciality, fileNames = "ee";
    public static ArrayList<String> ProductCodeSplit1 = new ArrayList<String>();



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Detailing_Selection_search_grid_selection.Communicator) {
            mappingActivityCommunicator = (Communicator) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implemenet MyListFragment.Communicator");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.detailing_select_search_left, container);
        Detailing_right_grid_view.bindSpecListener(new SpecialityListener() {
            @Override
            public void specialityCode(String codes) {
                if (!TextUtils.isEmpty(codes)) {
                    Log.v("Other_speciality", "are_calleddd" + codes);
                    Log.v("Other_speciality", "are_calleddd" + codes + "creation " + DetailingTrackerPOJO.getmDetListview_Selection());
                    CommonUtils.TAG_DR_SPEC = codes;
                    DetailingTrackerPOJO.setmDoctorSpeciality(codes);
                    DisplayProductGridView(DetailingTrackerPOJO.getmDetListview_Selection());
                    if (DetailingTrackerPOJO.getmDetListview_Selection().equalsIgnoreCase("Customise")) {
                        customiseOption.isClicked();
                        CustomClick = true;
                    }
                }
            }
        });


        Detailing_right_grid_view.bindTherapticListener(new TherapticListener() {
            @Override
            public void therapticCode(String codes) {
                if (!TextUtils.isEmpty(codes)) {
                    Log.v("Other_theraptic", "are_calleddd" + codes + "creation " + DetailingTrackerPOJO.getmDetListview_Selection());
                    CommonUtils.TAG_DR_SPEC = codes;
                    DetailingTrackerPOJO.setmDoctorTheraptic(codes);
                    DisplayProductGridView(DetailingTrackerPOJO.getmDetListview_Selection());
                    if (DetailingTrackerPOJO.getmDetListview_Selection().equalsIgnoreCase("Customise")) {
                        customiseOption.isClicked();
                        CustomClick = true;
                    }
                }
            }
        });


        DetailingFullScreenImageViewActivity.bindSpecListener(new SpecialityListener() {
            @Override
            public void specialityCode(String code) {
                Log.v("Other_speciality_det", "are_calleddd" + code);
                DetailingTrackerPOJO.setmDoctorSpeciality(code);
                DisplayProductGridView("Speciality Wise");
            }
        });
        DetailingCreationActivity.bindSpecListener(new SpecialityListener() {
            @Override
            public void specialityCode(String codes) {
                if (!TextUtils.isEmpty(codes)) {
                    Log.v("Other_speciality", "are_calleddd" + codes + "creation " + DetailingTrackerPOJO.getmDetListview_Selection());
                    CommonUtils.TAG_DR_SPEC = codes;
                    DetailingTrackerPOJO.setmDoctorSpeciality(codes);
                    DisplayProductGridView(DetailingTrackerPOJO.getmDetListview_Selection());
                    if (DetailingTrackerPOJO.getmDetListview_Selection().equalsIgnoreCase("Customise")) {
                        customiseOption.isClicked();
                        CustomClick = true;
                    }
                }
            }
        });
        if (!TextUtils.isEmpty(CommonUtils.TAG_DR_PRODCT_CODE)) {
            for (String retval : CommonUtils.TAG_DR_PRODCT_CODE.split(",")) {
                try {//i made change, get cursor count as 1 but cursor.getstring(0) is null

                    Log.v("retval_spec_com", retval);
                    specDrProdCode.add(retval);
                } catch (Exception e) {
                    mCursor = null;
                    // Log.v("cursor_move_exc","to_next"+ProductBrandCodeSplit.size());
                    break;
                }
            }

        }

        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            init();
        } catch (Exception e) {
            // Toast.makeText(getActivity(), ""+e, Toast.LENGTH_LONG).show();
        }
    }

    public void init() {
        tv_brdmatrx_set = (TextView) v.findViewById(R.id.tv_det_brdmatrix_set);
        spec_img = (ImageView) v.findViewById(R.id.spec_img);
        down_arrow = (ImageView) v.findViewById(R.id.down_arrow);
        lv_brdmatrix_list = (ListView) v.findViewById(R.id.detsearch_listview);
        mGridview = (GridView) v.findViewById(R.id.gv_detailing_creation_search);
        ll_txt = (LinearLayout) v.findViewById(R.id.ll_txt);
        ll_img = (LinearLayout) v.findViewById(R.id.ll_img);
        // tv_brdmatrx_set.setOnTouchListener(this);

        ll_layoutCat = (LinearLayout) v.findViewById(R.id.ll_layoutCat);
        cat_img = (ImageView) v.findViewById(R.id.cat_img);

        spec_img.setOnTouchListener(this);
        ll_txt.setOnTouchListener(this);
        ll_img.setOnTouchListener(this);
        ll_layoutCat.setOnTouchListener(this);
        cat_img.setOnTouchListener(this);

        dbh = new DataBaseHandler(getActivity());
        Log.v("customize_data", String.valueOf(dbh != null));

        DetailingTrackerPOJO = new DetailingTrackerPOJO();
        mCommonSharedPreference = new CommonSharedPreference(getActivity());
        String brand_type = mCommonSharedPreference.getValueFromPreference("display_brand");
        skipSpeciality = mCommonSharedPreference.getValueFromPreference("specFilter");
        if (brand_type.equals("null") || TextUtils.isEmpty(brand_type)) {

            DisplayProductGridView("Brand Matrix");
            mCommonSharedPreference.setValueToPreference("display_brand", "Brand Matrix");
        } else {
            if (brand_type.equalsIgnoreCase("Speciality Wise")) {
                if (skipSpeciality.equalsIgnoreCase("0")) {
                    ll_img.setVisibility(View.VISIBLE);
                    spec_img.setVisibility(View.VISIBLE);
                }
                down_arrow.setVisibility(View.INVISIBLE);
            } else {
                ll_img.setVisibility(View.GONE);
                spec_img.setVisibility(View.GONE);
            }


            if (brand_type.equalsIgnoreCase("Theraptic")) {
                if (skipSpeciality.equalsIgnoreCase("0")) {
                    ll_layoutCat.setVisibility(View.VISIBLE);
                    cat_img.setVisibility(View.VISIBLE);
                }
                down_arrow.setVisibility(View.INVISIBLE);
            } else {
                ll_layoutCat.setVisibility(View.GONE);
                cat_img.setVisibility(View.GONE);
            }


            DisplayProductGridView(brand_type);
            tv_brdmatrx_set.setText(brand_type);

        }

    }

    public static void bindPresentationListener(Customize_slide _pCustomize_slide) {
        customize_slide = _pCustomize_slide;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.ll_txt:

                if (lv_brdmatrix_list.getVisibility() == View.VISIBLE) {
                    lv_brdmatrix_list.setVisibility(View.GONE);
                    mGridview.setVisibility(View.VISIBLE);
                } else {
                    lv_brdmatrix_list.setVisibility(View.VISIBLE);
                    mGridview.setVisibility(View.GONE);
                    displayListItems();
                }
                break;
            case R.id.ll_img:
                lv_brdmatrix_list.setVisibility(View.GONE);
                if (!checkActivity) {
                    Detailing_right_grid_view.popupSpeciality();
                } else {
                    DetailingFullScreenImageViewActivity.popupSpeciality();
                }

                break;

            case R.id.ll_layoutCat:
                lv_brdmatrix_list.setVisibility(View.GONE);
                if (lv_brdmatrix_list.getVisibility() == View.VISIBLE) {
                    lv_brdmatrix_list.setVisibility(View.GONE);
                    mGridview.setVisibility(View.VISIBLE);
                } else {
                    lv_brdmatrix_list.setVisibility(View.VISIBLE);
                    mGridview.setVisibility(View.GONE);
                    displayListItems();
                }
                if (!checkActivity) {
                    Detailing_right_grid_view.popupTheraptic();
                } else {
                    DetailingFullScreenImageViewActivity.popupTheraptic();
                }
                break;
        }
        return false;
    }

    private void displayListItems() {
        Log.v("customize_datasss", String.valueOf(dbh != null));
        dbh.open();
        listViewItems.clear();
        BrdNameArray.clear();
        listViewItems.add("Brand Matrix");
        listViewItems.add("Speciality Wise");
        listViewItems.add("All Brands");
        if (!checkActivity)
            listViewItems.add("Customise");
        if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("theraptic"))) {
            Log.v("therapticValue",mCommonSharedPreference.getValueFromPreference("theraptic"));
            if (mCommonSharedPreference.getValueFromPreference("theraptic").equalsIgnoreCase("0"))
                listViewItems.add("Theraptic");
        }
        ArrayList<String> PresentationName = new ArrayList<String>();
        mCursor = dbh.select_Presentation_Names_List();
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                PresentationName.add(mCursor.getString(0));
            }
        }
        listViewItems.addAll(PresentationName);
        Log.v("list_view_printing_hh", listViewItems + "");

        lv_brdmatrix_list.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.listview_items, listViewItems));

        lv_brdmatrix_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
                tv_brdmatrx_set.setText(parent.getItemAtPosition(position).toString());
                lv_brdmatrix_list.setVisibility(View.INVISIBLE);
                mGridview.setVisibility(View.VISIBLE);
                Detailing_right_grid_view dtlng_right_gv = new Detailing_right_grid_view();
                dtlng_right_gv.ClearGridView();
                DetailingTrackerPOJO.setmDetstrtpdtBrdCode("");
                DetailingTrackerPOJO.setmDetListview_Selection(parent.getItemAtPosition(position).toString());

                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Speciality Wise")) {
                   /* _pPresentation_search_grid_selection.getView().setVisibility(View.GONE);
                    _pDetailing_Selection_search_grid_selection.getView().setVisibility(View.VISIBLE);*/
                    ll_img.setVisibility(View.VISIBLE);
                    spec_img.setVisibility(View.VISIBLE);

                    ll_layoutCat.setVisibility(View.GONE);
                    cat_img.setVisibility(View.GONE);

                    DisplayProductGridView(parent.getItemAtPosition(position).toString());
                    CustomClick = false;
                    /* */
                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Customise")) {
                    lv_brdmatrix_list.setVisibility(View.INVISIBLE);
                    customiseOption.isClicked();
                    CustomClick = true;
                }
               /* else if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Customise")){
                    _pPresentation_search_grid_selection.getView().setVisibility(View.VISIBLE);
                    _pDetailing_Selection_search_grid_selection.getView().setVisibility(View.GONE);
                    customize_slide.slideChange();
                    CustomClick=true;
                    DetailingTrackerPOJO.setmDetListview_Selection("customize");
                   // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.det_search_sel_fragment1, new Presentation_search_grid_selection()).commit();
                }*/

                else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Theraptic")) {
                    ll_layoutCat.setVisibility(View.VISIBLE);
                    cat_img.setVisibility(View.VISIBLE);

                    ll_img.setVisibility(View.GONE);
                    spec_img.setVisibility(View.GONE);

                    DisplayProductGridView(parent.getItemAtPosition(position).toString());
                    CustomClick = false;
                }
                else {
                   /* _pPresentation_search_grid_selection.getView().setVisibility(View.GONE);
                    _pDetailing_Selection_search_grid_selection.getView().setVisibility(View.VISIBLE);*/
                    ll_img.setVisibility(View.GONE);
                    spec_img.setVisibility(View.GONE);
                    ll_layoutCat.setVisibility(View.GONE);
                    cat_img.setVisibility(View.GONE);
                    CustomClick = false;
                    Log.v("first_prd_call_here222", "here_only");
                    DisplayProductGridView(parent.getItemAtPosition(position).toString());
                }
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Customise"))
                    mCommonSharedPreference.setValueToPreference("display_brand", "customised");
                else
                    mCommonSharedPreference.setValueToPreference("display_brand", parent.getItemAtPosition(position).toString());
            }
        });

    }

    public void DisplayProductGridView(String mSelectionList) {
        try {
            int check_display = 0;
            mProducts_GridView_Contents.clear();
            dbh.open();
            BrdCodeArray.clear();
            BrdNameArray.clear();
            fileNames = "ee";
            Cursor mCursor = null;
            Cursor mCursor1 = null;
            Custom_Products_GridView_Contents _products = null;
            Boolean selectionstatus = true;
            String ProductBrandCode = null;
            ArrayList<String> ProductBrandCodeSplit = new ArrayList<String>();
            ArrayList<String> FilterProductBrandCodeSplit = new ArrayList<String>();
            ArrayList<String> ProductCodeSplit = new ArrayList<String>();
            ProductCodeSplit1.clear();
            DetailingTrackerPOJO.setmDetListview_Selection(mSelectionList);
            String prdCode = null;
            Log.v("mSelectionList_detview", "calling" + skipSpeciality);
            switch (mSelectionList) {
                case "Brand Matrix":
                    /* *
                     * Getting Brand code for doctor wise mapping from web..
                     * */
                    mCursor1 = dbh.select_ProductCodeForDoctor(DetailingTrackerPOJO.getmDoctorCode());
                    Log.v("retval_str", String.valueOf(mCursor1.getCount()));
                    if (mCursor1.getCount() > 0) {

                        while (mCursor1.moveToNext()) {
                            Log.v("cursor_move", "to_next");
                            ProductBrandCode = mCursor1.getString(0);
                            Log.v("cursor_move", "to_next" + ProductBrandCode + " prd_code " + mCursor1.getString(1));
                            prdCode = mCursor1.getString(1);

                        }
                        if (prdCode != null) {
                            for (String retval : prdCode.split(",")) {
                                ProductCodeSplit.add(retval);
                            }
                            for (String retval : ProductBrandCode.split(",")) {
                                try {//i made change, get cursor count as 1 but cursor.getstring(0) is null

                                    String newStr = retval.substring(0, retval.indexOf("-"));
                                    ProductBrandCodeSplit.add(newStr);
                                } catch (Exception e) {
                                    mCursor = null;
                                    Log.v("cursor_move_exc", "to_next" + ProductBrandCodeSplit.size());
                                    break;

                                }
                                Log.v("productBrandSplitttt", ProductBrandCodeSplit + "");
                            }
                        }
                                      /*for (String retval : prdCode.split(",")) {
                                         try {//i made change, get cursor count as 1 but cursor.getstring(0) is null

                                            Log.v("new_retrival_Code",retval);
                                             ProductCodeSplit.add(retval);
                                         }catch (Exception e) {
                                             mCursor=null;
                                             Log.v("cursor_move_exc","to_next"+ProductBrandCodeSplit.size());
                                             break;

                                         }
                                       }*/
                    }

                    for (int i = 0; i < ProductBrandCodeSplit.size(); i++) {
                        String ProductBrdCode = ProductBrandCodeSplit.get(i);
                        Log.v("skipSpeciality_here", ProductBrdCode);
                        for (int k = 0; k < ProductCodeSplit.size(); k++) {
                            Log.v("skipSpeciality_here", skipSpeciality);
                            //if(skipSpeciality.equalsIgnoreCase("0"))
                            mCursor = dbh.select_ProductBrandCodeBrandWiseSearchWithoutSpec(ProductBrdCode, ProductCodeSplit.get(k));
                            // else
                            //mCursor = dbh.select_ProductBrandCodeBrandWiseSearchSpec(ProductBrdCode,CommonUtils.TAG_DR_SPEC,ProductCodeSplit.get(k));

                            Log.v("brand_matxixx_loop", mCursor.getCount() + " brd_code " + ProductBrdCode);
                            if (mCursor.getCount() > 0) {
                                while (mCursor.moveToNext()) {
                                    Log.v("Exce_topm_field", mCursor.getString(1) + " brdCode " + mCursor.getString(0) + " brdNamee " + mCursor.getString(5) + " cursor_count " + mCursor.getCount());
                                    if (fileNames.contains(mCursor.getString(1))) {
                                        Log.v("Exce_topm_field_11", mCursor.getString(1) + " brdCode " + mCursor.getString(0) + " filenamess " + mCursor.getCount() + fileNames);
                                        //Toast.makeText(getActivity(),"dva"+ mCursor.getString(1),Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.v("Exce_topm_field_22", mCursor.getString(1) + " brdCode " + mCursor.getString(0) + " filenamess " + mCursor.getCount() + fileNames + " curss" + mCursor.getString(7));
                                        Bitmap bb = null;
                                             /* if(mCursor.getString(2).contains(".pdf")){
                                                  bb=getResizedBitmapForPdf(getBitmap(new File(mCursor.getString(2))),150,150);
                                              }*/
                                        _products = new Custom_Products_GridView_Contents(mCursor.getString(1), mCursor.getString(0), mCursor.getString(5), mCursor.getString(2),
                                                selectionstatus, CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_DETAILINGSEARCH, mCursor.getString(6), bb);
                                        mProducts_GridView_Contents.add(_products);
                                        Log.e("BrandArray", BrdNameArray.toString());
                                        Log.e("BrandArray", String.valueOf(BrdNameArray.size()));
                                        Log.e("BrandArray", mCursor.getString(0));

                                        BrdCodeArray.add(mCursor.getString(0));
                                        BrdNameArray.add(mCursor.getString(5));
                                        ProductCodeSplit1.add(mCursor.getString(7));
                                        fileNames += mCursor.getString(1) + "," + fileNames;

                                        Log.e("BrandArray_add", BrdNameArray.toString());
                                        Log.e("BrandArray_add", String.valueOf(BrdNameArray.size()));


                                    }
                                }
                            }
                        }
                    }

/*
                            for (int i = 0; i < ProductBrandCodeSplit.size(); i++) {
                                    String ProductBrdCode = ProductBrandCodeSplit.get(i);
                                    Log.v("skipSpeciality_here",skipSpeciality);
                                    if(skipSpeciality.equalsIgnoreCase("0"))
                                    mCursor = dbh.select_ProductCodeBrandWiseSearch(ProductBrdCode);
                                    else
                                        mCursor = dbh.select_ProductCodeBrandWiseSearchSpec(ProductBrdCode,CommonUtils.TAG_DR_SPEC);

                                    if (mCursor.getCount() > 0) {
                                    while (mCursor.moveToNext()) {
                                        prdCode=mCursor.getString(7);
                                        Log.v("cursor_mv_chech","to_next"+" prd_code "+prdCode);

                                        boolean FilterBrand=false;
                                      */
                        /*  for(String ret1 : prdCode.split(",")){
                                            if(specDrProdCode.contains(ret1))
                                                FilterBrand=true;
                                        }
                                        if(FilterBrand) {*//*

                                            // Log.e("CURSOR ",mCursor.getString(1) +"---"+mCursor.getString(0));
                                            _products = new Custom_Products_GridView_Contents(mCursor.getString(1), mCursor.getString(0), mCursor.getString(5), mCursor.getString(2),
                                                    selectionstatus, CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_DETAILINGSEARCH, mCursor.getString(6));
                                            mProducts_GridView_Contents.add(_products);
                                            BrdCodeArray.add(mCursor.getString(0));
                                            BrdNameArray.add(mCursor.getString(5));
                                      //  }
                                    }
                                }
                             }
                            */
                    break;

                /**
                 * Getting Brand code for doctor speciality wise mapping
                 * */
                case "Speciality Wise":
                    mCursor = dbh.select_DrSpecialityWisePdts(DetailingTrackerPOJO.getmDoctorSpeciality());
                    Log.v("specialityWise_", DetailingTrackerPOJO.getmDoctorSpeciality());
                    /*while(mCursor.moveToNext()){
                        Log.v("prdCode_allbrand",mCursor.getString(3)+" SL "+mCursor.getString(8)+" brand_code "+mCursor.getString(0)+"prd_name "+mCursor.getString(5));
                    }
                    mCursor.moveToFirst();*/
                    break;
                case "Theraptic":
                    mCursor = dbh.select_DrTherapticWisePdts(DetailingTrackerPOJO.getmDoctorTheraptic());
                    Log.v("Theraptic", DetailingTrackerPOJO.getmDoctorTheraptic());
                    break;

                case "All Brands":
                    Log.v("all_brand_select", "all_brands_only_ss" + CommonUtils.TAG_DR_SPEC);
                    if (skipSpeciality.equalsIgnoreCase("0"))
                        mCursor = dbh.select_ProductBrdWiseSlide();
                    else
                        mCursor = dbh.select_ProductBrdWiseSlideSpec(CommonUtils.TAG_DR_SPEC);
                    Log.v("Log_total", mCursor.getCount() + "");

                           /*  while(mCursor.moveToNext()){
                                 Log.v("prdCode_allbrand",mCursor.getString(7)+" SL "+mCursor.getString(9)+" brand_code "+mCursor.getString(0)+"prd_name "+mCursor.getString(5));
                             }
                        mCursor.moveToFirst();*/
                            /* if(TextUtils.isEmpty(CommonUtils.TAG_DR_SPEC))
                            mCursor = dbh.select_ProductBrdWiseSlide();
                             else
                                 mCursor=dbh.select_ProductBrdWiseSlideSpec(CommonUtils.TAG_DR_SPEC);*/
                    break;

                default:
                    check_display = 6;
                    //if(mSelectionList.equalsIgnoreCase("customised"))
                    mCursor = dbh.select_products_Slides_PresntationWise(mSelectionList);
                            /*else
                                mCursor = dbh.select_products_Slides_PresntationWiseByDesc(mSelectionList);*/
                    break;
            }

            if (mCursor != null && mCursor.getCount() > 0) {
                while (mCursor.moveToNext()) {
                     Log.e("CURSOR ",mCursor.getString(1) +"---"+mCursor.getString(0));
                    Bitmap bb = null;
                    if (mCursor.getString(2).contains(".pdf")) {
                        if (mSelectionList.equalsIgnoreCase("Brand Matrix") || mSelectionList.equalsIgnoreCase("All Brands")) {
                            bb = StringToBitMap(mCursor.getString(8));
                        } else if (mSelectionList.equalsIgnoreCase("Speciality Wise")) {
                            bb = StringToBitMap(mCursor.getString(7));
                        } else {
                            bb = StringToBitMap(mCursor.getString(7));
                        }
                    }
                    if (mSelectionList.equalsIgnoreCase("Speciality Wise")) {
                       // Log.v("selection_request", mCursor.getString(8));
                        Log.v("selection_request", String.valueOf(mCursor.getCount()));
                    }

                    _products = new Custom_Products_GridView_Contents(mCursor.getString(1), mCursor.getString(0), mCursor.getString(5), mCursor.getString(2),
                            selectionstatus, CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_DETAILINGSEARCH, mCursor.getString(6), bb);
                    mProducts_GridView_Contents.add(_products);
                    Log.v("all_brand_cursor", mCursor.getString(3) + " brd_code " + mCursor.getString(0) + mCursor.getString(2) + "size_" + BrdCodeArray.size());
                    Log.v("product_Sizess", String.valueOf(mProducts_GridView_Contents.size()));
                    BrdCodeArray.add(mCursor.getString(0));
                    BrdNameArray.add(mCursor.getString(5));
                    mGridview.setVisibility(View.VISIBLE);
                }
            }
            DetailingTrackerPOJO.setBrdCodeArrayList(BrdCodeArray);
            DetailingTrackerPOJO.setBrdNameArrayList(BrdNameArray);


            Log.v("speciality_codeui", mProducts_GridView_Contents.size() + " visible " + mGridview.getVisibility());
            _deDetailing_gridview_adapter = new Detailing_gridview_adapter(getActivity(), R.layout.custom_presentation_search_gridview,
                    mProducts_GridView_Contents, Detailing_Selection_search_grid_selection.this);
            mGridview.setAdapter(_deDetailing_gridview_adapter);

            /*
             * Add Current slide list array
             * */
            String speciality = DetailingTrackerPOJO.getmDoctorSpeciality();
            Log.v("speciality_code_pir", speciality + " visible " + mGridview.getVisibility());

            String theraptic = DetailingTrackerPOJO.getmDoctorSpeciality();
            Log.v("theraptic_code_pir", theraptic + " visible " + mGridview.getVisibility());

            JSONArray ProductBrdWiseSlidesArray = new JSONArray();
            len_slide = 0;
            switch (DetailingTrackerPOJO.getmDetListview_Selection()) {

                case "Speciality Wise":
                    for (int j = 0; j < BrdCodeArray.size(); j++) {
                        JSONObject ProductBrdSLidesJson = new JSONObject();
                        ProductBrdSLidesJson.put("BrdCode", BrdCodeArray.get(j));
                        ProductBrdSLidesJson.put("BrdName", BrdNameArray.get(j));

                        Log.e("Speciality_wise", String.valueOf(BrdCodeArray.size()));
                        Log.e("Speciality_wise", BrdNameArray.get(j));

                        mCursor = dbh.select_AllSlides_specialitywise(BrdCodeArray.get(j), speciality);
                        JSONArray SlidesArray = new JSONArray();

try
{

                        if (mCursor.getCount() > 0) {
                            while (mCursor.moveToNext()) {
                                Log.v("slide_printing_SL", BrdCodeArray.get(j) + " :-" + mCursor.getString(2) + " printing_code " + mCursor.getString(9));
                                if (!mCursor.getString(4).equalsIgnoreCase("0")) {
                                    JSONObject SLidesJson = new JSONObject();
                                    SLidesJson.put("SlideId", mCursor.getString(6));
                                    SLidesJson.put("SlideName", mCursor.getString(2));
                                    SLidesJson.put("SlideType", mCursor.getString(3));
                                    SLidesJson.put("SlideLocUrl", mCursor.getString(4));
                                    SLidesJson.put("PrdCode", "-1");
                                    ++len_slide;
                                    SlidesArray.put(SLidesJson);
                                }
                            }
                        }
                    }catch (Exception e)
{
    Log.e("Error",e.getMessage());
}
                        Log.v("Selection_prd_json", String.valueOf(SlidesArray.length()));
                        ProductBrdSLidesJson.put("Slides", SlidesArray);

                        ProductBrdWiseSlidesArray.put(ProductBrdSLidesJson);

                    }
                    break;
//
                case "Theraptic":
                    for (int j = 0; j < BrdCodeArray.size(); j++) {
                        JSONObject ProductBrdSLidesJson = new JSONObject();
                        ProductBrdSLidesJson.put("BrdCode", BrdCodeArray.get(j));
                        ProductBrdSLidesJson.put("BrdName", BrdNameArray.get(j));


                        mCursor = dbh.select_AllSlides_therapticwise(BrdCodeArray.get(j), theraptic);
                        JSONArray SlidesArray = new JSONArray();
                        if (mCursor.getCount() > 0) {
                            while (mCursor.moveToNext()) {
                                if (!mCursor.getString(4).equalsIgnoreCase("0")) {
                                    JSONObject SLidesJson = new JSONObject();
                                    SLidesJson.put("SlideId", mCursor.getString(6));
                                    SLidesJson.put("SlideName", mCursor.getString(2));
                                    SLidesJson.put("SlideType", mCursor.getString(3));
                                    SLidesJson.put("SlideLocUrl", mCursor.getString(4));
                                    SLidesJson.put("PrdCode", "-1");
                                    ++len_slide;
                                    SlidesArray.put(SLidesJson);
                                }
                            }
                        }
                        Log.v("Selection_prd_json", String.valueOf(SlidesArray.length()));
                        ProductBrdSLidesJson.put("Slides", SlidesArray);

                        ProductBrdWiseSlidesArray.put(ProductBrdSLidesJson);

                    }

                    break;
                case "Brand Matrix":
                    for (int j = 0; j < BrdCodeArray.size(); j++) {
                        JSONObject ProductBrdSLidesJson = new JSONObject();
                        ProductBrdSLidesJson.put("BrdCode", BrdCodeArray.get(j));
                        ProductBrdSLidesJson.put("BrdName", BrdNameArray.get(j));
                        Log.v("skipSpeciality_pptt", skipSpeciality);
                        JSONArray SlidesArray = new JSONArray();
                        // for(int k=0;k<ProductCodeSplit1.size();k++){
                        Log.v("product_code_spl", ProductCodeSplit1.get(j) + " selection_grid " + BrdCodeArray.get(j));
                        //if(skipSpeciality.equalsIgnoreCase("0"))
                        mCursor = dbh.select_AllSlides_brandwiseWithoutSpecProd(BrdCodeArray.get(j), ProductCodeSplit1.get(j));
                        // else
                        //   mCursor = dbh.select_AllSlides_brandwiseSpecProd(BrdCodeArray.get(j),CommonUtils.TAG_DR_SPEC,ProductCodeSplit1.get(j));

                        if (mCursor.getCount() > 0) {
                            while (mCursor.moveToNext()) {
                                Log.v("slide_printing_prd", mCursor.getString(4) + " printing_code " + mCursor.getString(8));
                                if (!mCursor.getString(4).equalsIgnoreCase("0")) {
                                    JSONObject SLidesJson = new JSONObject();
                                    SLidesJson.put("SlideId", mCursor.getString(6));
                                    SLidesJson.put("SlideName", mCursor.getString(2));
                                    SLidesJson.put("SlideType", mCursor.getString(3));
                                    SLidesJson.put("SlideLocUrl", mCursor.getString(4));
                                    SLidesJson.put("PrdCode", ProductCodeSplit1.get(j));
                                    ++len_slide;
                                    SlidesArray.put(SLidesJson);
                                }
                            }
                        }

                        // }
                        Log.v("printinging_each_item", SlidesArray.toString());
                        ProductBrdSLidesJson.put("Slides", SlidesArray);
                        ProductBrdWiseSlidesArray.put(ProductBrdSLidesJson);

                       /* if(skipSpeciality.equalsIgnoreCase("0"))
                        mCursor = dbh.select_AllSlides_brandwise(BrdCodeArray.get(j));
                        else
                            mCursor = dbh.select_AllSlides_brandwiseSpec(BrdCodeArray.get(j),CommonUtils.TAG_DR_SPEC);*/

                      /*  JSONArray SlidesArray=new JSONArray();
                        if (mCursor.getCount() > 0) {
                            while (mCursor.moveToNext()) {
                                prdCode=mCursor.getString(8);
                                Log.v("cursor_mv_fulll","to_next"+" prd_code "+prdCode);

                                boolean FilterBrand=false;
                                for(String ret1 : prdCode.split(",")){
                                    if(specDrProdCode.contains(ret1))
                                        FilterBrand=true;
                                }
                                if(FilterBrand) {
                                    JSONObject SLidesJson = new JSONObject();
                                    SLidesJson.put("SlideId", mCursor.getString(6));
                                    SLidesJson.put("SlideName", mCursor.getString(2));
                                    SLidesJson.put("SlideType", mCursor.getString(3));
                                    SLidesJson.put("SlideLocUrl", mCursor.getString(4));
                                    ++len_slide;
                                    SlidesArray.put(SLidesJson);
                                }
                            }
                        }

                        ProductBrdSLidesJson.put("Slides",SlidesArray);
                        ProductBrdWiseSlidesArray.put(ProductBrdSLidesJson);*/
                    }
                    break;
                case "All Brands":
                    Log.v("all_brand_select11", "all_brands_only_ss" + CommonUtils.TAG_DR_SPEC);
                    Log.v("all_brandCount", "all_brands_only_ss" + BrdCodeArray.size());
                    for (int j = 0; j < BrdCodeArray.size(); j++) {
                        JSONObject ProductBrdSLidesJson = new JSONObject();
                        ProductBrdSLidesJson.put("BrdCode", BrdCodeArray.get(j));
                        ProductBrdSLidesJson.put("BrdName", BrdNameArray.get(j));

                        Log.e("Brand_Name", BrdNameArray.get(j));

                        if (skipSpeciality.equalsIgnoreCase("0"))
                            mCursor = dbh.select_AllSlides_brandwise(BrdCodeArray.get(j));
                        else
                            mCursor = dbh.select_AllSlides_brandwiseSpec(BrdCodeArray.get(j), CommonUtils.TAG_DR_SPEC);

                        /*if(TextUtils.isEmpty(CommonUtils.TAG_DR_SPEC))
                        mCursor = dbh.select_AllSlides_brandwise(BrdCodeArray.get(j));
                        else
                         mCursor = dbh.select_AllSlides_brandwiseSpec(BrdCodeArray.get(j),CommonUtils.TAG_DR_SPEC);*/

                        JSONArray SlidesArray = new JSONArray();
                        try
                        {

                        if (mCursor.getCount() > 0) {
                            while (mCursor.moveToNext()) {
                                Log.v("slide_printing_prd", "SL" + mCursor.getString(9) + "Brand" + BrdCodeArray.get(j) + " printing_code " + mCursor.getString(8));
                                if (!mCursor.getString(4).equalsIgnoreCase("0")) {
                                    JSONObject SLidesJson = new JSONObject();
                                    SLidesJson.put("SlideId", mCursor.getString(6));
                                    SLidesJson.put("SlideName", mCursor.getString(2));
                                    SLidesJson.put("SlideType", mCursor.getString(3));
                                    SLidesJson.put("SlideLocUrl", mCursor.getString(4));
                                    SLidesJson.put("PrdCode", "-1");
                                    ++len_slide;
                                    SlidesArray.put(SLidesJson);
                                }
                            }
                        }

                    }catch (Exception e)
                        {
                            Log.e("Error",e.getMessage());
                        }

                        ProductBrdSLidesJson.put("Slides", SlidesArray);
                        ProductBrdWiseSlidesArray.put(ProductBrdSLidesJson);
                    }
                    break;
                default:
                    check_display = 6;
                    Log.v("all_brand_select45", "all_brands_only_ss" + DetailingTrackerPOJO.getmDetListview_Selection());
                    for (int j = 0; j < BrdCodeArray.size(); j++) {
                        JSONObject ProductBrdSLidesJson = new JSONObject();
                        ProductBrdSLidesJson.put("BrdCode", BrdCodeArray.get(j));
                        ProductBrdSLidesJson.put("BrdName", BrdNameArray.get(j));
                        mCursor = dbh.selectAllProducts_GroupPresentationWise(BrdCodeArray.get(j), DetailingTrackerPOJO.getmDetListview_Selection());

//                        if(DetailingTrackerPOJO.getmDetListview_Selection().equalsIgnoreCase("customised"))
//                            mCursor = dbh.selectAllProducts_GroupPresentationWise(BrdCodeArray.get(j), DetailingTrackerPOJO.getmDetListview_Selection());
//                        else
//                        mCursor = dbh.selectAllProducts_GroupPresentationWiseByDesc(BrdCodeArray.get(j), DetailingTrackerPOJO.getmDetListview_Selection());
                        JSONArray SlidesArray = new JSONArray();
                        if (mCursor.getCount() > 0) {
                            while (mCursor.moveToNext()) {
                                if (!mCursor.getString(4).equalsIgnoreCase("0")) {
                                    JSONObject SLidesJson = new JSONObject();
                                    SLidesJson.put("SlideId", mCursor.getString(5));
                                    SLidesJson.put("SlideName", mCursor.getString(2));
                                    SLidesJson.put("SlideType", mCursor.getString(3));
                                    SLidesJson.put("PrdCode", "-1");
                               /* if(mCursor.getString(3).equalsIgnoreCase("i")) {
                                    File compressedImageFile = null;
                                    try {
                                        compressedImageFile = new Compressor(getActivity()).compressToFile(new File(mCursor.getString(4)));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Log.v("Exception_in_search_"," path "+ mCursor.getString(4));
                                    }
                                    SLidesJson.put("SlideLocUrl", compressedImageFile.toString());
                                }
                                else*/
                                    SLidesJson.put("SlideLocUrl", mCursor.getString(4));
                                    ++len_slide;
                                    SlidesArray.put(SLidesJson);
                                }
                            }
                        }

                        ProductBrdSLidesJson.put("Slides", SlidesArray);
                        ProductBrdWiseSlidesArray.put(ProductBrdSLidesJson);
                    }

                    break;
            }
            Log.v("ProductBrdWiseSlides_", ProductBrdWiseSlidesArray.toString());
            Log.v("len_slide_value", String.valueOf(len_slide));
            if (skipSpeciality.equalsIgnoreCase("0")) {
                mCommonSharedPreference.setValueToPreference("ProductBrdWiseSlides_jsonArray", ProductBrdWiseSlidesArray.toString());
            } else {
                if (check_display == 6 && CustomClick) {
                    JSONArray ja = new JSONArray(mCommonSharedPreference.getValueFromPreference("selection_product").toString());
                    len_slide = ja.length();
                    mCommonSharedPreference.setValueToPreference("ProductBrdWiseSlides_jsonArray", mCommonSharedPreference.getValueFromPreference("selection_product").toString());
                } else if (DetailingTrackerPOJO.getmDetListview_Selection().equalsIgnoreCase("customised")) {
                    JSONArray ja = new JSONArray(mCommonSharedPreference.getValueFromPreference("selection_product").toString());
                    len_slide = ja.length();
                    mCommonSharedPreference.setValueToPreference("ProductBrdWiseSlides_jsonArray", mCommonSharedPreference.getValueFromPreference("selection_product").toString());


               /* JSONArray   jj=new JSONArray(mCommonSharedPreference.getValueFromPreference("selection_product").toString());
                Log.v("printing_selection_rp",jj.toString());
                JSONObject  jjson=jj.getJSONObject(0);
                Log.v("printing_selection_rp",jjson.toString()+" brdnam "+jjson.getString("BrdCode"));
               // CustomClick=true;
                *//*mGridview.setVisibility(View.INVISIBLE);*//*
                onTaskComplete(jjson.getString("BrdName"),jjson.getString("BrdCode"));*/
                } else
                    mCommonSharedPreference.setValueToPreference("ProductBrdWiseSlides_jsonArray", ProductBrdWiseSlidesArray.toString());
                Log.v("ProductBrdWiseSlides_1", mCommonSharedPreference.getValueFromPreference("ProductBrdWiseSlides_jsonArray"));
                Log.v("len_slide_value21", String.valueOf(len_slide));
            }
        } catch (JSONException e) {
        } finally {
            dbh.close();
        }

        // DetailingTrackerPOJO.setmDetstrtpdtBrdCode(BrdCodeArray.get(0));

    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    @Override
    public void onTaskComplete(String productBrdName, String ProductBrdCode) {
        updateFragment(productBrdName, ProductBrdCode);
        DetailingTrackerPOJO.setmDetstrtpdtBrdCode(ProductBrdCode);
        Log.e("grid_selected", "yes" + String.valueOf(ProductBrdCode) + "////" + productBrdName);
    }

    private void updateFragment(String productBrdName, String productBrdCode) {
        mappingActivityCommunicator.Message(productBrdName, productBrdCode);
        if (mappingActivityCommunicator != null) {
        }
        //  Log.e("grid_selected", "yes111"+ String.valueOf(productBrdCode)+"////"+productBrdName);}
        else {
            /// Log.e("grid_selected", "yes222"+ String.valueOf(productBrdCode)+"////"+productBrdName);
        }
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

    public Bitmap getBitmap(File file) {
        int pageNum = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(getActivity());
        try {
            PdfDocument pdfDocument = pdfiumCore.newDocument(openFile1(file));
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

    public interface Communicator {
        public void Message(String PdtBrdName, String productcode);
    }

    public static void bindListenerCustomise(CustomiseOption customiseOption1) {
        customiseOption = customiseOption1;
    }

    public Bitmap getResizedBitmapForPdf(Bitmap bm, int newWidth, int newHeight) {
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
