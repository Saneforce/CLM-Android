package saneforce.sanclm.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import saneforce.sanclm.BuildConfig;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.R;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.NetworkStatus;
import saneforce.sanclm.fragments.AppConfiguration;
import saneforce.sanclm.fragments.LocaleHelper;
import saneforce.sanclm.sqlite.DataBaseHandler;
import saneforce.sanclm.util.UpdateUi;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static saneforce.sanclm.fragments.AppConfiguration.MyPREFERENCES;
import static saneforce.sanclm.fragments.AppConfiguration.language_string;
import static saneforce.sanclm.fragments.AppConfiguration.licenceKey;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    CommonSharedPreference mCommonSharedPreference;
    CommonUtilsMethods CommonUtilsMethods;
    DataBaseHandler dbh;
    Button mloginButton;
    EditText et_login_password, et_login_username;
    ImageView iv_company_image;
    Cursor mCursor;
    PackageManager PkgMgr;
    String PkgNm;
    PackageInfo pkgInfo;
    String DataDir ,userName,passWord,divisionCode,deviceId;
    ProgressDialog progressDialog ;
    public static  String BASE_URL ="";
    private static Retrofit retrofit = null;
    NetworkStatus net;
    String db_pathUrl="",Slides_dwnload_url="";
    boolean accessStorageAllowed = false;
    boolean doubleBackToExitPressedOnce = false;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    static UpdateUi updateUi;
    String token_val="";

    String logincheck="";
    int x=0;


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            mCommonSharedPreference.setValueToPreference("loginCheck","false");
           /* finish();
            System.exit(0);*/
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.click_again), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCommonSharedPreference=new CommonSharedPreference(getApplicationContext());
        dbh = new DataBaseHandler(this);
        net = new NetworkStatus(this);
        progressDialog = new ProgressDialog(LoginActivity.this);
        CommonUtilsMethods = new CommonUtilsMethods(this);
        PkgMgr = getPackageManager();
        PkgNm = getPackageName();
        //startLocationButtonClick();

       /* permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);*/
       // permissions.add(CAMERA);
        permissions.add(CAMERA);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.R)
        permissions.add(MANAGE_EXTERNAL_STORAGE);
        FirebaseApp.initializeApp(this);
        //permissions.add(READ_CONTACTS);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.

        if(permissionsToRequest.size()>0){}
        else accessStorageAllowed=true;

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.R){
            if(Environment.isExternalStorageManager()) {
//todo when permission is granted
            } else {
//request for the permission
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0) {
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }
        else{
            //startLocationButtonClick();
        }


/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
*/

        /* location track  Ends */




        try {
            pkgInfo = PkgMgr.getPackageInfo(PkgNm, 0);
        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String DataDir = pkgInfo.applicationInfo.dataDir;

        mloginButton=(Button) findViewById(R.id.bt_login);
        et_login_username=(EditText)findViewById(R.id.et_login_username);
        et_login_password=(EditText)findViewById(R.id.et_login_password);

       SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String language = sharedPreferences.getString(language_string, "");
        if (!language.equals("")){
            Log.d("stringlang",language);
            selected(language);
        }else {
            selected("en");
        }

        if(TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_USERNAME)) ||
                mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_USERNAME).equalsIgnoreCase("null")){}else{
            et_login_username.setText(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_USERNAME));
        }

        mloginButton.setOnClickListener(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                |View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                );


        dbh.open();
         mCursor = dbh.select_login_username();
        while(mCursor.moveToNext()){
            if(mCursor.getString(0) != null){
                et_login_username.setVisibility(View.GONE);
               // tv_login_username.setVisibility(View.VISIBLE);
               // tv_login_username.setText(mCursor.getString(0));
               // demoDetailsTracker.setmUsername(mCursor.getString(0));
            }else{
                et_login_username.setVisibility(View.VISIBLE);
              //  tv_login_username.setVisibility(View.GONE);
            }
        }
        mCursor = dbh.select_urlconfiguration();
        Log.v("value_cursor", String.valueOf(mCursor.getCount()));

        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                db_pathUrl=mCursor.getString(0)+mCursor.getString(1);
                db_pathUrl=db_pathUrl.substring(0,db_pathUrl.lastIndexOf("/")+1);
                Slides_dwnload_url =mCursor.getString(0)+mCursor.getString(4);
                SharedPreferences sharedpreferences=getSharedPreferences(getResources().getString(R.string.preference_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(CommonUtils.TAG_DB_URL,  db_pathUrl);
                editor.putString(CommonUtils.TAG_DIVISION, mCursor.getString(2));
                editor.putString(CommonUtils.TAG_SLIDES_DOWNLOAD_URL, Slides_dwnload_url);
                editor.putString("report_url", mCursor.getString(3));
                editor.commit();

                //Toast.makeText(this,db_pathUrl, Toast.LENGTH_SHORT).show();

                String fileNm="Logo.PNG";
                final ImageView iv = (ImageView) findViewById(R.id.iv_login_user_image);
                File logofile = new File(DataDir,fileNm);
                Log.v("final_printing_detail",logofile.getAbsolutePath()+"logo."+DataDir);
                Drawable d = new BitmapDrawable(logofile.getAbsolutePath());
                iv.setBackground(d);

                //CheckForstoragePermission();
                checkingPermissions();

            }
        }else{
            UrlConfig();
        }
       /* SplashScreenActivity.bindLocationCallListener(new UpdateUi() {
            @Override
            public void updatingui() {
                LocationTrack tt = new LocationTrack(LoginActivity.this,mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE));

            }
        });*/

        SharedPreferences slide=getSharedPreferences("slide",0);
        SharedPreferences.Editor edit=slide.edit();
        edit.putString("slide_download","0");
        edit.commit();
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( this,  new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String updatedToken = instanceIdResult.getToken();
//                Log.e("Updated_Token",updatedToken);
//                token_val=updatedToken;
//
//            }
//        });
    }

    public boolean startLocationButtonClick() {
        boolean val=false;
        // Requesting ACCESS_FINE_LOCATION using Dexter library
       /* Dexter.withActivity(LoginActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();*/


       LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            new AlertDialog.Builder(LoginActivity.this)
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
        else
            val=true;
        return val;
    }

    private void UrlConfig() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AppConfiguration()).commit();
    }

    @Override
    protected void onResume() { super.onResume(); CommonUtilsMethods.FullScreencall(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_login:
            // CommonUtilsMethods.CommonIntentwithNEwTask(HomeDashBoard.class);

                try {
                if (net.isInternetAvailable()) {
                    progressDialog.setCancelable(false); // set cancelable to false
                    progressDialog.setMessage("Logging In"); // set message

                    userName = et_login_username.getText().toString().trim();
                    passWord = et_login_password.getText().toString().trim();
                    divisionCode = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);
                    deviceId = mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DEVICEID);
                    if(accessStorageAllowed){

                        if(validation()){
                         progressDialog.show();
                        /* HashMap<String, String> map = new HashMap<String, String>();

                         map.put("name", userName);
                         map.put("password", passWord);*/
                        JSONObject map=new JSONObject();
                        map.put("name", userName);
                        map.put("password", passWord);
                        map.put("Appver", "V1.9.8");
                        map.put("Mod", "Edet");
                        map.put("dev_id", token_val);
                         Log.v("database_url",db_pathUrl.substring(0,db_pathUrl.lastIndexOf("/")+1));
                         Log.v("response_login",map.toString());

                         //if(TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_USERNAME))|| mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_USERNAME).equalsIgnoreCase("null")) {
                         if(CommonUtilsMethods.isOnline(LoginActivity.this)) {
                             if(!(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_USERNAME).equalsIgnoreCase("null")))
                             {
                                 if (!(mCommonSharedPreference.getValueFromPreference("pass").equalsIgnoreCase(et_login_password.getText().toString())) ||
                                         !(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_USERNAME).equalsIgnoreCase(et_login_username.getText().toString()))) {
                                     Toast.makeText(LoginActivity.this, getResources().getString(R.string.check_us_ps), Toast.LENGTH_SHORT).show();
                                     progressDialog.dismiss();
                                 } else {
                                     Api_Interface apiService = RetroClient.getClient(db_pathUrl).create(Api_Interface.class);
                                     Call<ResponseBody> Callto = apiService.Login(String.valueOf(map));
                                     //  Call<ResponseBody> Callto = apiService.Todaycalls(json);
                                     Callto.enqueue(CheckUser);
                                 }
                             }
                             else{
                                 Api_Interface apiService = RetroClient.getClient(db_pathUrl).create(Api_Interface.class);
                                 Call<ResponseBody> Callto = apiService.Login(String.valueOf(map));
                                 //  Call<ResponseBody> Callto = apiService.Todaycalls(json);
                                 Callto.enqueue(CheckUser);
                             }
                         }
                         else{
                             Log.v("printing_passwrd",mCommonSharedPreference.getValueFromPreference("pass"));
                             if(!(mCommonSharedPreference.getValueFromPreference("pass").equalsIgnoreCase(et_login_password.getText().toString())) ||
                                     !(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_USERNAME).equalsIgnoreCase(et_login_username.getText().toString()))){
                                 Toast.makeText(LoginActivity.this,getResources().getString(R.string.check_us_ps),Toast.LENGTH_SHORT).show();
                                 progressDialog.dismiss();
                             }
                             else{
                                 CommonUtilsMethods.CommonIntentwithNEwTask(HomeDashBoard.class);
                                 progressDialog.dismiss();
                                 mCommonSharedPreference.setValueToPreference("loginCheck","true");
                             }
                         }
                           break;
                       }
                           }else {
                           Toast.makeText(this,getResources().getString(R.string.strge_permis), Toast.LENGTH_SHORT).show();
                           //CheckForstoragePermission();
                        checkingPermissions();
                           }

                }else{
                    if(TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_USERNAME))|| mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_USERNAME).equalsIgnoreCase("null")) {
                        Toast.makeText(this,getResources().getString(R.string.mob_inter), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(!(mCommonSharedPreference.getValueFromPreference("pass").equalsIgnoreCase(et_login_password.getText().toString())) ||
                                !(mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_USERNAME).equalsIgnoreCase(et_login_username.getText().toString()))){
                            Toast.makeText(LoginActivity.this,getResources().getString(R.string.check_us_ps),Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else {
                            CommonUtilsMethods.CommonIntentwithNEwTask(HomeDashBoard.class);
                            progressDialog.dismiss();
                            mCommonSharedPreference.setValueToPreference("loginCheck","true");
                        }

                    }

            }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }


    public Callback<ResponseBody> CheckUser = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            System.out.println("checkUser_is_sucessful_login :" + response.isSuccessful());
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    String jsonData = null;
                    try {
                        jsonData = response.body().string();
                        jsonObject = new JSONObject(jsonData);
                        Log.v("login_json", String.valueOf(jsonObject));

                        if (jsonObject.getString("success").equals("true")) {
                           // Log.v("reference_tokennnn", FirebaseInstanceId.getInstance().getToken());
                            SharedPreferences sharedpreferences=getSharedPreferences(getResources().getString(R.string.preference_name), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(CommonUtils.TAG_LOGIN_RESPONSE, jsonData);
                            editor.putString(CommonUtils.TAG_USERNAME, userName);
                            editor.putString("pass", passWord);
                            editor.putString(CommonUtils.TAG_NAME, jsonObject.getString("SF_Name"));
                            editor.putString(CommonUtils.TAG_LAST_LOGIN_TIME, CommonUtilsMethods.getCurrentInstance());
                            editor.putString(CommonUtils.TAG_SF_CODE, jsonObject.getString("SF_Code"));
                            Log.v("value_of_sfcode",jsonObject.getString("sf_type"));

                            editor.commit();
                            CommonUtils.TAG_USERNME=et_login_username.getText().toString();
                            CommonUtils.TAG_USERPWD=et_login_password.getText().toString();

                            mCommonSharedPreference.setValueToPreference("loginCheck","true");
                           // mCommonSharedPreference.setValueToPreference("workType","false");
                            mCommonSharedPreference.setValueToPreference("sf_type",jsonObject.getString("sf_type"));
                            mCommonSharedPreference.setValueToPreference("geoneed",jsonObject.getString("GEOTagNeed"));

                            if(jsonObject.has("Digital_offline"))
                                mCommonSharedPreference.setValueToPreference("Digital_offline",jsonObject.getString("Digital_offline"));
                            else
                                mCommonSharedPreference.setValueToPreference("Digital_offline","0");


                            logincheck= mCommonSharedPreference.getValueFromPreference("dashboard");

                            if(jsonObject.has("sf_emp_id"))
                                mCommonSharedPreference.setValueToPreference("sf_emp_id",jsonObject.getString("sf_emp_id"));
                            else
                                mCommonSharedPreference.setValueToPreference("sf_emp_id","");

                            mCommonSharedPreference.setValueToPreference("radius",jsonObject.getString("DisRad"));

                            if(jsonObject.has("GEOTagNeedche"))
                            mCommonSharedPreference.setValueToPreference("chmgeoneed",jsonObject.getString("GEOTagNeedche"));
                            else
                                mCommonSharedPreference.setValueToPreference("chmgeoneed" , "");

                            if(jsonObject.has("GEOTagNeedstock"))
                                mCommonSharedPreference.setValueToPreference("stkgeoneed",jsonObject.getString("GEOTagNeedstock"));
                            else
                                mCommonSharedPreference.setValueToPreference("stkgeoneed" , "");

                            if(jsonObject.has("GeoTagNeedcip"))
                                mCommonSharedPreference.setValueToPreference("cipgeoneed",jsonObject.getString("GeoTagNeedcip"));
                            else
                                mCommonSharedPreference.setValueToPreference("cipgeoneed" , "");



//                            if (jsonObject.has("tp_need"))
//                                mCommonSharedPreference.setValueToPreference("tp_need", jsonObject.getString("tp_need"));
//                            else
//                                mCommonSharedPreference.setValueToPreference("tp_need", "");

                            progressDialog.dismiss();

                            boolean checkPer=false;
                            Log.v("value_of_sfcode",sharedpreferences.getString(CommonUtils.TAG_SF_CODE,null)+" usernameee "+CommonUtils.TAG_USERNME);
                            mCommonSharedPreference.setValueToPreference("GpsFilter",jsonObject.getString("GeoChk"));
                            if(jsonObject.getString("GeoChk").equalsIgnoreCase("0")) {

                                if(startLocationButtonClick()) {
                                    checkPer=true;
                                }
                                else
                                    checkPer=false;
                            }
                            else
                                checkPer=true;

                             if(checkPer) {
                                //if(jsonObject.getString("Digital_offline").equalsIgnoreCase("1"))
                                if(mCommonSharedPreference.getValueFromPreference("Digital_offline").equalsIgnoreCase("1"))
                                {

                                    if (logincheck != null && !logincheck.equals("true")) {
                                        CommonUtilsMethods.CommonIntentwithNEwTask(HomeDashBoard.class);
                                        finish();
                                    }else {
                                        CommonUtilsMethods.CommonIntentwithNEwTask(DetailingCreationActivity.class);
                                        finish();
                                    }
                                }else {
                                    CommonUtilsMethods.CommonIntentwithNEwTask(HomeDashBoard.class);
                                    finish();
                                }


                            }
                    }else {
                        String err = jsonObject.getString("msg");
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("onresponse error catch" + e);
                }catch (Exception e) {
                        //Log.v("Exception_login_ac",e+"");
                        Intent i=new Intent(LoginActivity.this,LoginActivity.class);
                        startActivity(i);
            }
            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.toString());
                    Toast.makeText(LoginActivity.this, jObjError.toString(), Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "catchbody error " + e.toString(), Toast.LENGTH_LONG).show();
                    System.out.println("catchbody error " + e.toString());
                }
                progressDialog.dismiss();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.onfail)+t.getMessage() , Toast.LENGTH_LONG).show();
        }
    };

    public  boolean validation(){
            if (userName.equals("")) {
                et_login_username.setError("Enter username");
                et_login_username.requestFocus();
                return false;
            } else if (passWord.equals("")) {
                et_login_password.setError("Enter Password");
                et_login_password.requestFocus();
                return false;
            }

        return true;
    }

    private void hideSystemUI() {
       getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void CheckForstoragePermission()  {
        try  {
            java.lang.reflect.Method methodCheckPermission = Activity.class.getMethod("checkSelfPermission", java.lang.String.class);
            Object storagechkpm = methodCheckPermission.invoke(this, WRITE_EXTERNAL_STORAGE);
            int result = Integer.parseInt(storagechkpm.toString());
            if (result == PackageManager.PERMISSION_GRANTED){
                accessStorageAllowed = true;

            }
            else{

            }
                }catch(Exception ex){

        }
             if(accessStorageAllowed){
                return;
                }try{
                java.lang.reflect.Method methodRequestPermission = Activity.class.getMethod("requestPermissions", java.lang.String[].class, int.class);
                methodRequestPermission.invoke(this, new String[] {
                    WRITE_EXTERNAL_STORAGE }, 0x12345);
        }catch (Exception ex){
        }
    }
    public void checkingPermissions(){
        if (ContextCompat.checkSelfPermission(LoginActivity.this, WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.v("permission_check","are_not_granted");
            // Permission is not granted
            if (ContextCompat.checkSelfPermission(LoginActivity.this,
                    WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                        WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    Log.v("accessPermission","went_request");
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{WRITE_EXTERNAL_STORAGE},
                            11);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                accessStorageAllowed=true;

            }
        }else {
            // Permission has already been granted
            accessStorageAllowed=true;

        }
    }


    public void checkingPermissions2(){
        if (ContextCompat.checkSelfPermission(LoginActivity.this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) {
            Log.v("permission_check","are_not_granted");
            // Permission is not granted
            if (ContextCompat.checkSelfPermission(LoginActivity.this,
                    WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                        WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    Log.v("accessPermission","went_request");
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{WRITE_EXTERNAL_STORAGE},
                            11);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                accessStorageAllowed=true;

            }
        }else {
            // Permission has already been granted
            accessStorageAllowed=true;

        }
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 11: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.v("permisssion_are_granted","sucess");
                } else {
                    Log.v("permisssion_are_granted","denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
*/
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


   // @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public void selected(String lang){
        Context context = LocaleHelper.setLocale(LoginActivity.this, lang);
        Resources resources = context.getResources();
        mloginButton.setText(resources.getString(R.string.Login));
        et_login_username.setHint(resources.getString(R.string.username));
        et_login_password.setHint(resources.getString(R.string.password));
    }
}
