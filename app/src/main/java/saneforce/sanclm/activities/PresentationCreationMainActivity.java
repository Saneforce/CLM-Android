package saneforce.sanclm.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;

import saneforce.sanclm.Pojo_Class.DetailingTrackerPOJO;
import saneforce.sanclm.R;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.fragments.Presentation_bottom_grid_selection;
import saneforce.sanclm.fragments.Presentation_recycler_item;
import saneforce.sanclm.fragments.Presentation_search_grid_selection;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class PresentationCreationMainActivity extends FragmentActivity implements View.OnClickListener,Presentation_search_grid_selection.Communicator,
        Presentation_bottom_grid_selection.Communicator1{
    ImageView iv_back;
    CommonUtilsMethods CommonUtilsMethods;
    DataBaseHandler dbh;
    DetailingTrackerPOJO mDetailingTrackerPOJO;
    ProgressDialog progressDialog=null;
   public static boolean presentationList=false;
   LinearLayout linearLayout;
    static boolean CustomDetail=false;
    CommonSharedPreference commonSharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtilsMethods = new CommonUtilsMethods(this);
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
        commonSharedPreference=new CommonSharedPreference(this);
        commonSharedPreference.setValueToPreference("present","yes");
        setContentView(R.layout.activity_presentation_creation_main);
        mDetailingTrackerPOJO.setmPrsn_svName("");
        iv_back = (ImageView)  findViewById(R.id.iv_back);
        linearLayout=(LinearLayout)findViewById(R.id.ll_anim);
        iv_back.setOnClickListener(this);

        dbh = new DataBaseHandler(this);
        dbh.open();
        CommonUtils.TAG_VIEW_PRESENTATION="0";
        Log.v("printing_spec_prese7",CommonUtils.TAG_DR_SPEC+" present "+commonSharedPreference.getValueFromPreference("present"));
        Cursor mCursor = dbh.select_presentationMapping();
        while (mCursor.moveToNext()) {
       // Log.e("PDT MAPP ",mCursor.getString(4) +"---"+mCursor.getString(7)  );
        }
        commonFun();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                CommonUtilsMethods.CommonIntentwithNEwTask(HomeDashBoard.class);
                break;
        }
    }

    @Override
    public void onResume() { CommonUtilsMethods.FullScreencall(this);super.onResume(); }
    @Override
    public void onPause() { super.onPause(); }

    @Override
    public void onBackPressed() {
        commonFun();
        Intent i=new Intent(PresentationCreationMainActivity.this,HomeDashBoard.class);
        startActivity(i);
    }

    @Override
    public void Message(String PdtBrdName, String productcode){
        CommonUtilsMethods.FullScreencall(this);
        Presentation_recycler_item _presentation_right_grid_view = (Presentation_recycler_item) getFragmentManager().findFragmentById(R.id.presentation_right_fm);
        if (_presentation_right_grid_view != null && _presentation_right_grid_view.isInLayout()) {
            Log.v("Presentation_wrk_call","herere");
            linearLayout.setVisibility(View.VISIBLE);
            _presentation_right_grid_view.mdDisplayProductInGridView(PdtBrdName, productcode,presentationList);
            Log.v("Presentation_wrk_called","herere");
            linearLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("printing_spec_prese12","herere");
    }

    @Override
    public void PresentationSelection(String PresentationName) {
        Log.v("printing_spec_prese123","herere");
        CommonUtilsMethods.FullScreencall(this);
        SharedPreferences sharedd=getSharedPreferences("presentation",0);
        SharedPreferences.Editor editt=sharedd.edit();
        editt.putBoolean("present",true);
        editt.commit();
        presentationList=true;
        Presentation_recycler_item _presentation_right_grid_view = (Presentation_recycler_item) getFragmentManager().findFragmentById(R.id.presentation_right_fm);
        if (_presentation_right_grid_view != null && _presentation_right_grid_view.isInLayout()) {
            _presentation_right_grid_view.mdDisplayProductInGridView("","",false);
        }
        Presentation_search_grid_selection _pPresentation_search_grid_selection = (Presentation_search_grid_selection) getFragmentManager().findFragmentById(R.id.frag1_search);
        if (_pPresentation_search_grid_selection != null && _pPresentation_search_grid_selection.isInLayout()) {
            _pPresentation_search_grid_selection.mdDisplayProductInGridView(PresentationName);
        }

    }

    public void commonFun(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }


}
