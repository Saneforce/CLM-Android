package saneforce.sanclm.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

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
import saneforce.sanclm.adapter_class.Detailing_gridview_adapter;
import saneforce.sanclm.adapter_class.SpecialityAdapter;
import saneforce.sanclm.adapter_interfaces.OnSelectGridViewListener;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.Customize_slide;
import saneforce.sanclm.util.PresentationRightUnselect;
import saneforce.sanclm.util.SpecialityListener;
import saneforce.sanclm.util.TherapticListener;

public class Detailing_right_grid_view extends Fragment implements OnSelectGridViewListener {

    View v;
    GridView gv_detright;
    static DataBaseHandler dbh;
    DetailingTrackerPOJO detailingTrackerPOJO;
    static Detailing_gridview_adapter Detailing_gridview_adapter = null;
    private ArrayList<Custom_Products_GridView_Contents> mProducts_GridView_Contents = new ArrayList<Custom_Products_GridView_Contents>();
    List list = new ArrayList();
    Cursor mCursor;
    static Context context;
    static String speccode, specName;
    static ArrayList<String> arr = new ArrayList<>();
    static ArrayList<String> code = new ArrayList<>();
    static SpecialityListener specialityListener;
    Presentation_recycler_item _pPresentation_recycler_item;
    Detailing_right_grid_view _pDetailing_right_grid_view;
    ArrayList<String> specDrProdCode = new ArrayList<>();
    CommonSharedPreference commonSharedPreference;
    String skipSpeciality;
    static PresentationRightUnselect presentationRightUnselect;

    static TherapticListener therapticListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.detailing_right_gridview, container);
        mProducts_GridView_Contents.clear();
        fun(getActivity());
        commonSharedPreference = new CommonSharedPreference(getActivity());
        Detailing_Selection_search_grid_selection.bindPresentationListener(new Customize_slide() {
            @Override
            public void slideChange() {
             /*   _pPresentation_recycler_item.getView().setVisibility(View.VISIBLE);
                _pDetailing_right_grid_view.getView().setVisibility(View.GONE);*/
            }
        });
        skipSpeciality = commonSharedPreference.getValueFromPreference("specFilter");
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

    private void init() {
        gv_detright = (GridView) v.findViewById(R.id.gv_det_rightgrid_view);
        dbh = new DataBaseHandler(getActivity());
        detailingTrackerPOJO = new DetailingTrackerPOJO();
        /*_pPresentation_recycler_item = (Presentation_recycler_item) getFragmentManager().findFragmentById(R.id.presentation_right_fm);
        _pDetailing_right_grid_view = (Detailing_right_grid_view) getFragmentManager().findFragmentById(R.id.det_rightview_fragment);
        _pPresentation_recycler_item.getView().setVisibility(View.GONE);*/
        if (detailingTrackerPOJO.getmDetstrtpdtBrdCode() != null) {
            Log.v("detailingtrack", detailingTrackerPOJO.getmDetstrtpdtBrdCode());
            mdDisplayProductInGridView("");
            //mdDisplayProductInGridView(detailingTrackerPOJO.getmDetstrtpdtBrdCode());
        }
    }


    public void setText(String SelectedProduct) {
        mdDisplayProductInGridView(SelectedProduct);
    }

    private void mdDisplayProductInGridView(String ProductBrdCode) {

        try {
            dbh.open();
            mProducts_GridView_Contents.clear();
            Custom_Products_GridView_Contents _products = null;
            list.clear();
            Boolean selectionstatus = true;

            String speciality = detailingTrackerPOJO.getmDoctorSpeciality();

            String theraptic=detailingTrackerPOJO.getmDoctorTheraptic();

            switch (detailingTrackerPOJO.getmDetListview_Selection()) {
                case "Speciality Wise":
                    mCursor = dbh.select_AllSlides_specialitywise(ProductBrdCode, speciality);
                    break;
                case "Theraptic":
                    mCursor = dbh.select_AllSlides_therapticwise(ProductBrdCode, theraptic);
                    break;
                case "Brand Matrix":
                    Log.v("matric_prdcode", commonSharedPreference.getValueFromPreference("prdCodee") + " brd_codee " + ProductBrdCode);
                   /* if(TextUtils.isEmpty(CommonUtils.TAG_DR_SPEC))
                    mCursor = dbh.select_AllSlides_brandwise(ProductBrdCode);
                    else
                        mCursor = dbh.select_AllSlides_brandwiseSpec(ProductBrdCode,CommonUtils.TAG_DR_SPEC);*/
                    if (skipSpeciality.equalsIgnoreCase("0"))
                        mCursor = dbh.select_AllSlides_brandwiseWithoutSpecProd(ProductBrdCode, commonSharedPreference.getValueFromPreference("prdCodee"));
                    else
                        mCursor = dbh.select_AllSlides_brandwiseSpecProd(ProductBrdCode, CommonUtils.TAG_DR_SPEC, commonSharedPreference.getValueFromPreference("prdCodee"));

                    break;
                case "All Brands":
                    //if(TextUtils.isEmpty(CommonUtils.TAG_DR_SPEC))
                    if (skipSpeciality.equalsIgnoreCase("0")) {
                        mCursor = dbh.select_AllSlides_brandwise(ProductBrdCode);
                        Log.e("Tagme", mCursor.toString());
                    }
                    else { Log.e("Tagme1", mCursor.toString());
                        mCursor = dbh.select_AllSlides_brandwiseSpec(ProductBrdCode, CommonUtils.TAG_DR_SPEC);
                    }
                   /* else
                        mCursor = dbh.select_AllSlides_brandwiseSpec(ProductBrdCode,CommonUtils.TAG_DR_SPEC);*/

                    Log.e("Product_Categ", skipSpeciality);
                    break;
                default:
                    mCursor = dbh.selectAllProducts_GroupPresentationWise(ProductBrdCode, detailingTrackerPOJO.getmDetListview_Selection());

                    Log.e("Product_Categ1", skipSpeciality);
                   /* if(DetailingTrackerPOJO.getmDetListview_Selection().equalsIgnoreCase("customised"))
                     mCursor =  dbh.selectAllProducts_GroupPresentationWise(ProductBrdCode,detailingTrackerPOJO.getmDetListview_Selection());
                    else
                        mCursor =  dbh.selectAllProducts_GroupPresentationWiseByDesc(ProductBrdCode,detailingTrackerPOJO.getmDetListview_Selection());*/

                    break;
            }

            if (mCursor.getCount() > 0) {
                while (mCursor.moveToNext()) {
                    if (detailingTrackerPOJO.getmDetListview_Selection().equalsIgnoreCase("Brand Matrix")) {
                        String prdCode = mCursor.getString(8);
                        Log.v("cursor_mv_chech", "to_next" + " prd_code " + prdCode);

                        boolean FilterBrand = false;
                        for (String ret1 : prdCode.split(",")) {
                            if (specDrProdCode.contains(ret1))
                                FilterBrand = true;
                        }
                        if (FilterBrand) {
                            Bitmap bb = null;
                            if (mCursor.getString(4).contains(".pdf")) {
                                bb = StringToBitMap(mCursor.getString(7));
                                //bb=getResizedBitmapForPdf(getBitmap(new File(mCursor.getString(2))),150,150);
                            }
                            Log.d("testme"," CHECKIGN"+mCursor.toString());
                            Log.v("printing_full_path", mCursor.getString(4) + " brd_code " + mCursor.getString(0));
                            _products = new Custom_Products_GridView_Contents(mCursor.getString(2), mCursor.getString(0), mCursor.getString(1),
                                    mCursor.getString(4), selectionstatus, CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS, mCursor.getString(3), bb);
                            mProducts_GridView_Contents.add(_products);
                        }
                    } else {
                        Log.v("printing_full_path", mCursor.getString(4) + " brd_code " + mCursor.getString(0));
                        Bitmap bb = null;
                        if (mCursor.getString(4).contains(".pdf")) {
                            Log.v("printing_ful89", "choosing" + mCursor.getString(4));
                            if (detailingTrackerPOJO.getmDetListview_Selection().equalsIgnoreCase("All Brands")) {
                                Log.v("printing_ful898", "choosing");
                                bb = StringToBitMap(mCursor.getString(7));
                            } else if (detailingTrackerPOJO.getmDetListview_Selection().equalsIgnoreCase("Speciality Wise"))
                                bb = StringToBitMap(mCursor.getString(7));
                            else
                                bb = StringToBitMap(mCursor.getString(6));
                            Log.v("printing_ful899", "choosing");
                        }
                        _products = new Custom_Products_GridView_Contents(mCursor.getString(2), mCursor.getString(0), mCursor.getString(1),
                                mCursor.getString(4), selectionstatus, CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS, mCursor.getString(3), bb);
                        mProducts_GridView_Contents.add(_products);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbh.close();
        }

        Detailing_gridview_adapter = new Detailing_gridview_adapter(getActivity(), R.layout.custom_presentation_search_gridview,
                mProducts_GridView_Contents, Detailing_right_grid_view.this);
        gv_detright.setAdapter(Detailing_gridview_adapter);
    }

    public void ClearGridView() {
        Detailing_gridview_adapter.clear();
        mProducts_GridView_Contents.clear();
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

    public static String popupSpeciality() {
        arr.clear();
        code.clear();
        dbh.open();
        Cursor mCursor = dbh.select_speciality_list();
        while (mCursor.moveToNext()) {
            Log.v("Cursor_databasee", mCursor.getString(1) + mCursor.getString(2));
            arr.add(mCursor.getString(2));
            code.add(mCursor.getString(1));
        }

        final Dialog dialog = new Dialog(context, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_speciality);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        GridView popup_list = (GridView) dialog.findViewById(R.id.grid_list);
        ImageView popclose = (ImageView) dialog.findViewById(R.id.popclose);

        popclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        SpecialityAdapter popupAdapter = new SpecialityAdapter(arr, context);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.v("DATA_CHECKING","DATA_CHERCKING");
                speccode = code.get(i);
                specName = arr.get(i);
                specialityListener.specialityCode(speccode);
                if (presentationRightUnselect != null)
                    presentationRightUnselect.unSelecting(specName);
                dialog.dismiss();


                Log.v("DATA_CHECKING",speccode);
                Log.v("DATA_CHECKING",specName);
            }
        });
        return speccode;
    }


    public static String popupTheraptic() {
        arr.clear();
        code.clear();
        dbh.open();
        Cursor mCursor = dbh.select_theraptic_list();
        while (mCursor.moveToNext()) {
            Log.v("Cursor_databasee", mCursor.getString(1) + mCursor.getString(2));
            arr.add(mCursor.getString(2));
            code.add(mCursor.getString(1));
        }

        final Dialog dialog = new Dialog(context, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_speciality);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        GridView popup_list = (GridView) dialog.findViewById(R.id.grid_list);
        ImageView popclose = (ImageView) dialog.findViewById(R.id.popclose);

        popclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        SpecialityAdapter popupAdapter = new SpecialityAdapter(arr, context);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                speccode = code.get(i);
                specName = arr.get(i);
                //specialityListener.specialityCode(speccode);
                therapticListener.therapticCode(speccode);
                if (presentationRightUnselect != null)
                    presentationRightUnselect.unSelecting(specName);
                dialog.dismiss();
            }
        });
        return speccode;
    }


    public static void fun(Context context1) {
        context = context1;
    }

    public static void bindSpecListener(SpecialityListener speclist) {
        specialityListener = speclist;
    }

    public static void bindTherapticListener(TherapticListener therapticlist) {
        therapticListener = therapticlist;
    }

    public static void bindSpecName(PresentationRightUnselect speclist) {
        presentationRightUnselect = speclist;
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

}
