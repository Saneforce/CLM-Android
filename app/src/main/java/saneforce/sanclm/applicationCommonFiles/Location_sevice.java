package saneforce.sanclm.applicationCommonFiles;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import saneforce.sanclm.Common_Class.Dcrdatas;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.LoginActivity;

public class Location_sevice extends Service {

    private static Location_sevice mInstance = null;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "location";
    FusedLocationProviderClient mFusedLocationClient;
    Context context;
    double latitude ;
    double longitude ;
    Location location;

    private LocationCallback callback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLocations() != null) {
                List<Location> locationList = locationResult.getLocations();
                if (locationList.size() > 0) {
                    location = locationList.get(locationList.size() - 1);
                    Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());

                    Dcrdatas.dcr_latitude = String.valueOf(location.getLatitude());
                    Dcrdatas.dcr_longitute = String.valueOf(location.getLongitude());

                    Log.d("Location_update", location.getLatitude() + " ( " + Dcrdatas.dcr_latitude + " ) " + "---" + location.getLongitude() + " ( " + Dcrdatas.dcr_longitute + " ) ");

                    sharedpreferences = getSharedPreferences("location", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edittor = sharedpreferences.edit();
                    edittor.putString("lat", String.valueOf(location.getLatitude()));
                    edittor.putString("lat", String.valueOf(location.getLongitude()));
                    edittor.commit();
                }
                boolean isMock = false;
                if (Build.VERSION.SDK_INT >= 18) {
                   isMock = location.isFromMockProvider();
                } else {
                    isMock = !Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
                }
                if (isMock == true) {
                    if (Dcrdatas.IsFakeLocation.equals("0")) {
                        Dcrdatas.IsFakeLocation = "1";
                        Intent hh = new Intent(getApplicationContext(), LoginActivity.class);
                        hh.putExtra("Warning", "Your Using Fake Location please Turn Off");
                        hh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(hh);
                    }
                }
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet Implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startLocationService() {
        String channelid = "Location_notification_channel";
        NotificationManager notificationManager = ( NotificationManager ) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultintent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelid);
        builder.setSmallIcon(R.drawable.san_clm_logo);
        builder.setContentTitle("SANCLM");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setChannelId(channelid);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelid) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(channelid, "SANCLM", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("SANCLM");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, callback, Looper.getMainLooper());
        startForeground(175, builder.build());
    }


    private void stopLocationServices() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(callback);
        stopForeground(true);
        stopSelf();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Dcrdatas.dcr_latitude = "";
        Dcrdatas.dcr_longitute = "";
        if (intent != null){
            String action = intent.getAction();
            if (action != null){
                if (action.equals("startLocationService")){
                    startLocationService();
                }else if (action.equals("stopLocationService")){
                    stopLocationServices();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }


    public static boolean isServiceCreated() {
        try {
            return mInstance != null && mInstance.ping();
        } catch (NullPointerException e) {
            return false;
        }
    }

    private boolean ping() {
        return true;
    }

    @Override
    public void onCreate() {
        mInstance = this;
    }

    @Override
    public void onDestroy() {
        latitude=0.0;
        longitude=0.0;
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(callback);
        stopForeground(true);
        stopSelf();
        mInstance = null;
    }
}
