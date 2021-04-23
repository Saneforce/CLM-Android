package saneforce.sanclm.activities;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.provider.Settings.*;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import saneforce.sanclm.R;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;

public class SplashScreenActivity extends AppCompatActivity {

    CommonSharedPreference mCommonSharedPreference;
    CommonUtilsMethods commonUtilsMethods;
    String digital="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mCommonSharedPreference=new CommonSharedPreference(getApplicationContext());
        commonUtilsMethods = new CommonUtilsMethods(this);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        new BackgroundSplashTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("here_printing_destroy","method_are_called");
    }

    private class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                Thread.sleep(CommonUtils.SPLASH_SHOW_TIME);

                CommonUtils.TAG_DEVICEID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

                mCommonSharedPreference.setValueToPreference(CommonUtils.TAG_DEVICEID, Secure.getString(getContentResolver(), Secure.ANDROID_ID));


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            String val=mCommonSharedPreference.getValueFromPreference("loginCheck");
            digital=mCommonSharedPreference.getValueFromPreference("Digital_offline");

            Log.v("printing_the_exit_val",val);
            if(val!=null && !val.equals("true")) {
                mCommonSharedPreference.setValueToPreference("track_loc","0");
                Intent openLoginActivity = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(openLoginActivity);
                finish();
            }
            else if(digital.equalsIgnoreCase("1"))
            {
//                if(val!=null && !val.equals("true")) {
//                    Intent openLoginActivity = new Intent(SplashScreenActivity.this, HomeDashBoard.class);
//                    startActivity(openLoginActivity);
//                    finish();
//                }else {
                    Intent openDetailingActivity = new Intent(SplashScreenActivity.this, DetailingCreationActivity.class);
                    openDetailingActivity.putExtra("detailing",digital);
                    startActivity(openDetailingActivity);
                    finish();
                //}
            }
            else{

                //mCommonSharedPreference.setValueToPreference("track_loc","0");

                Intent openLoginActivity = new Intent(SplashScreenActivity.this, HomeDashBoard.class);
                startActivity(openLoginActivity);
                finish();
            }

        }
    }




}
