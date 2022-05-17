package saneforce.sanclm.applicationCommonFiles;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.BuildConfig;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.FeedbackActivity;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.LoginActivity;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class LocationTrack extends Service  {

    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates=false;
    Context context;
   // FeedbackActivity fed;
    //DCRCallSelectionActivity fed;
    HomeDashBoard fed;
    FeedbackActivity feedback;
    boolean getFirstLoc=true;

    double laty = 0.0, lngy = 0.0, limitKm = 0.2;
    Api_Interface apiService;
    String sfcode;
    CommonSharedPreference commonSharedPreference;
    String db_connPath;
    CommonUtilsMethods mCommonUtilsMethod;
    DataBaseHandler dbh;
    boolean isRunning=false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public LocationTrack()
    {

    }

    public LocationTrack(HomeDashBoard context,String sfcode){

        //this.context=context;

        fed=context;
        this.sfcode=sfcode;
        commonSharedPreference=new CommonSharedPreference(context);

        init();
        db_connPath =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        mCommonUtilsMethod=new CommonUtilsMethods(fed);
        dbh = new DataBaseHandler(context);
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        if(!isMockSettingsON(fed)){
            Log.v("Mock_location_not","detected");
            startLocationButtonClick();
/*
            if(!areThereMockPermissionApps(fed)){

            }
            else
                Toast.makeText(fed,"Mock location detected, cannot track location ",Toast.LENGTH_SHORT).show();
*/
        }
        else{
            Toast.makeText(fed,getResources().getString(R.string.mock_loc),Toast.LENGTH_SHORT).show();
           /* if(mock!=null)
            mock.mockDetect();*/
            commonSharedPreference.setValueToPreference("track_loc","0");
            stopLocationButtonClick();
           mCommonUtilsMethod.CommonIntentwithNEwTaskMock(LoginActivity.class);
            /*Intent dialogIntent = new Intent(this, LoginActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);*/
        }

        // restore the values from saved instance state
        //restoreValuesFromBundle(savedInstanceState);
    }

    public LocationTrack(FeedbackActivity context){

        //this.context=context;
        feedback=context;
        init();
        //startLocationButtonClick();

        // restore the values from saved instance state
        //restoreValuesFromBundle(savedInstanceState);
    }

    private void init() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(fed);
        mSettingsClient = LocationServices.getSettingsClient(fed);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            Log.v("CurrentLocation_FInd",mCurrentLocation.getLatitude()+"  "+mCurrentLocation.getLongitude()+" Acurate "+mCurrentLocation.getAccuracy());
           // Toast.makeText(fed,"Track_loc "+mCurrentLocation.getLatitude(),Toast.LENGTH_SHORT).show();
            SharedPreferences shares=fed.getSharedPreferences("location",0);
            SharedPreferences.Editor editor=shares.edit();
            editor.putString("lat", String.valueOf(mCurrentLocation.getLatitude()));
            editor.putString("lng", String.valueOf(mCurrentLocation.getLongitude()));
            editor.commit();

            if(!isMockSettingsON(fed)) {
                if (getFirstLoc) {
                    commonSharedPreference.setValueToPreference("track_loc","1");
                    laty = mCurrentLocation.getLatitude();
                    lngy = mCurrentLocation.getLongitude();
                    getFirstLoc = false;
                }

                //Toast.makeText(fed,"Total_distance"+distance(laty, lngy, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())+" old_lat "+laty+" lngyy "+lngy,Toast.LENGTH_SHORT).show();
                if (distance(laty, lngy, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()) > limitKm) {
                    Log.v("CurrentLocation_km_cro", mCurrentLocation.getLatitude() + "  " + mCurrentLocation.getLongitude());
                    if(mCommonUtilsMethod.isOnline(fed)){
                        svTrack(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),mCurrentLocation.getAccuracy());
                    }
                    else{
                        Log.v("CurrentLocation_km_cro", "are_offline_here");
                        dbh.open();
                        dbh.insertTracking(String.valueOf(mCurrentLocation.getLatitude()),String.valueOf(mCurrentLocation.getLongitude()),String.valueOf(mCurrentLocation.getAccuracy()),mCommonUtilsMethod.getCurrentInstance()+" "+mCommonUtilsMethod.getCurrentTime());
                    }

                    laty = mCurrentLocation.getLatitude();
                    lngy = mCurrentLocation.getLongitude();
                }

            }
            else{
                Toast.makeText(fed,getResources().getString(R.string.mock_loc),Toast.LENGTH_SHORT).show();
                commonSharedPreference.setValueToPreference("track_loc","0");
                stopLocationButtonClick();
                mCommonUtilsMethod.CommonIntentwithNEwTaskMock(LoginActivity.class);
            }

           // stopLocationButtonClick();
           /* txtLocationResult.setText(
                    "Lat: " + mCurrentLocation.getLatitude() + ", " +
                            "Lng: " + mCurrentLocation.getLongitude()
            );

            // giving a blink animation on TextView
            txtLocationResult.setAlpha(0);
            txtLocationResult.animate().alpha(1).setDuration(300);

            // location last updated time
            txtUpdatedOn.setText("Last updated on: " + mLastUpdateTime);*/
        }
        //stopLocationButtonClick();

        // toggleButtons();
    }

    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(fed)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
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
                }).check();
    }
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    private void startLocationUpdates() {
        Log.i("printingggx", "All location settings are satisfied.");
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(fed, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i("printingggg", "All location settings are satisfied.");

                        //Toast.makeText(fed, "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(fed, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i("printinggggg", "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(fed, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i("printinggggg", "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e("printinggggg", errorMessage);

                                //Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    public void stopLocationButtonClick() {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(fed, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                       // Toast.makeText(fed, "Location updates stopped!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static boolean isMockSettingsON(Context context) {
        // returns true if mock location enabled, false if not enabled.
        if (Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
            return false;
        else
            return true;
    }

    public static boolean areThereMockPermissionApps(Context context) {
        int count = 0;

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i]
                                .equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName())) {
                            count++;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("Got exception " , e.getMessage());
            }
        }

        if (count > 0)
            return true;
        return false;
    }
    public void svTrack(double lat,double lng,float acc){

        JSONObject json=new JSONObject();
        JSONArray jsonA=new JSONArray();
        try {
            json.put("SF",sfcode);
            json.put("latitude",String.valueOf(lat));
            json.put("longitude",String.valueOf(lng));
            json.put("theAccuracy",String.valueOf(acc));
            json.put("date",mCommonUtilsMethod.getCurrentInstance()+" "+mCommonUtilsMethod.getCurrentTime());
            Log.v("print_json_bb",json.toString()+" db_path "+db_connPath);
            jsonA.put(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<ResponseBody> taggedDr=apiService.saveTrack(jsonA.toString());

        taggedDr.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.v("printing_res_track",response.body().byteStream()+"");
                    JSONObject jsonObject = null;
                    String jsonData = null;

                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                    } catch (Exception e) {
                    }

                    Log.v("printing_trackeeed_22",is.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }



}

