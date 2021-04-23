package saneforce.sanclm.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import saneforce.sanclm.R;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.LocationTrack;
import saneforce.sanclm.fragments.DCRCallsSelectionTablayout;
import saneforce.sanclm.fragments.DCRDRCallsSelection;
import saneforce.sanclm.util.LocationUpdate;
import saneforce.sanclm.util.UpdateUi;

public class DCRCallSelectionActivity extends AppCompatActivity {
    CommonUtilsMethods commonUtilsMethods;
    LocationTrack locationTrack;
    CommonSharedPreference mCommonSharedPreference;
    String gpsNeed="0";

    @Override
    public void onBackPressed() {
        Log.v("on_back_press","dcrcallselectact");
        if(mCommonSharedPreference.getValueFromPreference("missed").equalsIgnoreCase("true")){
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_WORKTYPE_NAME, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_WORKTYPE_NAME));
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_WORKTYPE_CODE, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_WORKTYPE_CODE));
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_WORKTYPE_CLUSTER_CODE,mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_WORKTYPE_CLUSTER_CODE));
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_SF_CODE, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_SF_CODE));
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_SF_HQ, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_SF_HQ));
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_MYDAY_WORKTYPE_CLUSTER_NAME));
            mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_MYDAY_WORKTYPE_CLUSTER_NAME, mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_TMP_MYDAY_WORKTYPE_CLUSTER_NAME));
            mCommonSharedPreference.setValueToPreference("sub_sf_code",mCommonSharedPreference.getValueFromPreference("tmp_sub_sf_code"));

            Log.v("getting_sf_code",mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE)+" sf_sf "+"");

        }
        Intent i=new Intent(DCRCallSelectionActivity.this,HomeDashBoard.class);
        startActivity(i);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcrcallselectionmain);
        commonUtilsMethods = new CommonUtilsMethods(this);
        mCommonSharedPreference=new CommonSharedPreference(this);
        gpsNeed=mCommonSharedPreference.getValueFromPreference("GpsFilter");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frag = new DCRCallsSelectionTablayout();
        ft.replace(R.id.frame_container, frag);
        ft.commit();

        CommonUtilsMethods.FullScreencall(this);

        if(gpsNeed.equals("0")) {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {


            new AlertDialog.Builder(DCRCallSelectionActivity.this)
                    .setTitle("Alert")  // GPS not found
                    .setCancelable(false)
                    .setMessage("Activate the Gps to proceed futher") // Want to enable?
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .show();
        }
        }

        DCRDRCallsSelection.bindLocListener(new LocationUpdate() {
            @Override
            public void startUpdate() {
                Log.v("Location_value","are_called");
                if(gpsNeed.equals("1")) {
                    //locationTrack = new LocationTrack(DCRCallSelectionActivity.this);
                }
            }

            @Override
            public void stopUpdate() {

            }
        });
        DCRCallsSelectionTablayout.bindDcrBackPress(new UpdateUi() {
            @Override
            public void updatingui() {
                onBackPressed();
            }
        });


    }
    @Override
    public void onResume() {
        CommonUtilsMethods.FullScreencall(this);
        super.onResume();
    }
}
