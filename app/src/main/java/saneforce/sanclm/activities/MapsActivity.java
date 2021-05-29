package saneforce.sanclm.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.applicationCommonFiles.DownloadMasters;
import saneforce.sanclm.applicationCommonFiles.GPSTrack;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    GPSTrack gpsTrack;
    double lat, lng;
    DataBaseHandler dbh;
    ImageView imgLocationPinUp;
    ImageView tag;
    String laty="", lngy="", drcode, divcode, db_connPath, drname,db_slidedwnloadPath,SF_Code,subSfCode;
    CommonSharedPreference commonSharedPreference;
    Api_Interface api_interface;
    TextView txt_actual_addr, txt_dr;
    RelativeLayout tag_lay;
    ObjectAnimator objectanimator1;
    int heigt;
    TextView tag_txt;
    String changingText;
    String cust;
    String share_lat,share_lng;
    ImageView back_img;
    DownloadMasters dwnloadMasterData;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        imgLocationPinUp = (ImageView) findViewById(R.id.imgLocationPinUp);
        tag = (ImageView) findViewById(R.id.tag);
        dbh = new DataBaseHandler(this);
        commonSharedPreference = new CommonSharedPreference(this);
        divcode = commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DIVISION);
        db_connPath = commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        db_slidedwnloadPath =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SLIDES_DOWNLOAD_URL);
        SF_Code= commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        subSfCode=commonSharedPreference.getValueFromPreference("sub_sf_code");
        api_interface = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        txt_actual_addr = (TextView) findViewById(R.id.txt_actual_addr);
        txt_dr = (TextView) findViewById(R.id.txt_dr);
        tag_txt = (TextView) findViewById(R.id.tag_txt);
        tag_lay=(RelativeLayout)findViewById(R.id.tag_lay);
        heigt=this.getResources().getDisplayMetrics().heightPixels;
        heigt=(heigt/2)-20;
        Log.v("dummy_out_lay",imgLocationPinUp.getHeight()+"");
        back_img=(ImageView)findViewById(R.id.back_img);

        CommonUtilsMethods.FullScreencall(this);

         changingText="Tag";
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<40;i=i+2){
                    changingText+="  ";
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            tag_txt.setText(changingText);
                            Log.v("thread_tag_wrk",tag_txt.getText().toString());
                        }
                    });
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        tag_txt.setText("Tag");
                    }
                });

            }
        }).start();

        imgLocationPinUp.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout() {
                int height = imgLocationPinUp.getHeight();
                int width = imgLocationPinUp.getWidth();
                int x = imgLocationPinUp.getLeft();
                int y = imgLocationPinUp.getTop();
                Log.v("screen_layyy",height+" observer "+y);
                heigt=y;


                // don't forget to remove the listener to prevent being called again
                // by future layout events:
                imgLocationPinUp.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.super.onBackPressed();
            }
        });

                Log.v("screen_layyy123",heigt+" observer ");
                objectanimator1 = ObjectAnimator.ofFloat(imgLocationPinUp,"y",heigt);
                objectanimator1.setDuration(1000);
                objectanimator1.start();

        objectanimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                txt_dr.setVisibility(View.VISIBLE);
                Log.v("heifgt_stup",heigt+"");
                objectanimator1 = ObjectAnimator.ofFloat(imgLocationPinUp,"y",heigt);
                objectanimator1.setDuration(100);
                objectanimator1.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.v("animation_rotate","animmm");
            }
        });



        Bundle extra = getIntent().getExtras();
        drcode = extra.getString("drcode", "");
        drname = extra.getString("drname", "");
        cust = extra.getString("cust", "");
        share_lat=extra.getString("lats", "");
        share_lng=extra.getString("lngs", "");
        txt_dr.setText(drname);

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.3, 30);
        myAnim.setInterpolator(interpolator);
        tag.startAnimation(myAnim);


        //expand(tag_lay,1500,45);





        tag_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mp = new ProgressDialog(MapsActivity.this);
                mp.setIndeterminate(true);
                mp.setMessage("Processing...");
                mp.setCancelable(false);
                mp.show();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("lat", laty);
                    jsonObject.put("long", lngy);
                    jsonObject.put("cuscode", drcode);
                    jsonObject.put("divcode", divcode);
                    jsonObject.put("cust", cust);
                    //{"divcode":"19","cuscode":"45437","long":"80.2652184292674","lat":"13.034855187423728","cust":"D"}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("geo_tagging_result", jsonObject.toString());

                Call<ResponseBody> geoTag = api_interface.saveGeo(jsonObject.toString());
                geoTag.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {
                            mp.dismiss();
                            InputStreamReader ip = null;
                            StringBuilder is = new StringBuilder();
                            String line = null;
                            try {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);

                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                }
                                Log.v("printing_the_geo_res", is.toString());
                                JSONObject json = new JSONObject(is.toString());
                                if (json.getString("success").equalsIgnoreCase("true")) {
                                    dwnloadMasterData = new DownloadMasters(MapsActivity.this, db_connPath, db_slidedwnloadPath, subSfCode,8);
                                    Log.v("printing_Custter",cust);
                                    if(cust.equalsIgnoreCase("D")) {
                                        dwnloadMasterData.drList();

                                        commonSharedPreference.setValueToPreference("map_return_count","one");
                                    }
                                    else if(cust.equalsIgnoreCase("C")) {
                                        dwnloadMasterData.chemsList();
                                        commonSharedPreference.setValueToPreference("map_return_count","one");
                                    }
                                    else if(cust.equalsIgnoreCase("S")) {
                                        dwnloadMasterData.stckList();
                                        commonSharedPreference.setValueToPreference("map_return_count","one");
                                    }
                                    else {
                                        dwnloadMasterData.unDrList();
                                        commonSharedPreference.setValueToPreference("map_return_count","one");
                                    }
                                    Toast.makeText(MapsActivity.this, getResources().getString(R.string.tag_succs), Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            }
                            catch (Exception e) {
                            }
                        }
                        else{
                            mp.dismiss();
                            Toast.makeText(MapsActivity.this,getResources().getString(R.string.something_wrong),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        mp.dismiss();
                        Log.e("failure",t.getMessage());
                    }
                });
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        CommonUtilsMethods.FullScreencall(this);
        Log.v("hello_called","here");
    }


    /**https://baraabytes.com/how-to-move-a-map-under-a-marker-like-uber/
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if(!share_lat.equalsIgnoreCase("0.0")){
            LatLng sydney = new LatLng(Double.parseDouble(share_lat), Double.parseDouble(share_lng));
            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            Log.v("Add_markerrss",share_lat);
           // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        gpsTrack = new GPSTrack(this);
        // Add a marker in Sydney and move the camera
        Log.v("markerrrrr_ss", gpsTrack.getLatitude() + "" + gpsTrack.getLongitude());

        LatLng sydney = new LatLng(gpsTrack.getLatitude(), gpsTrack.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTrack.getLatitude(), gpsTrack.getLongitude()), 15.0f));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
      //  imgLocationPinUp.setVisibility(View.VISIBLE);

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
               // mMap.clear();
                Log.v("screen_layyy123456",heigt+" observer ");
                Log.v("centerLat_move",mMap.getCameraPosition().target.latitude+"");
                laty= String.valueOf(mMap.getCameraPosition().target.latitude);
                lngy=String.valueOf(mMap.getCameraPosition().target.longitude);
                imgLocationPinUp.setVisibility(View.VISIBLE);

                //imgLocationPinUp.setVisibility(View.VISIBLE);

            }
        });


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
               /* imgLocationPinUp.setVisibility(View.GONE);
                mMap.clear();
                Log.v("centerLat_idle",mMap.getCameraPosition().target+"");
                MarkerOptions markerOptions =new MarkerOptions().position(mMap.getCameraPosition().target)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                mMap.addMarker(markerOptions);*/
               if(!laty.trim().isEmpty()){
                gettingAddress(Double.parseDouble(laty),Double.parseDouble(lngy));}
            }
        });

    }

    public void gettingAddress(double la,double ln){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(la, ln, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Log.v("totally_printing_add",address);
            txt_actual_addr.setText(" : "+address);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void expand(final View v, int duration, int targetHeight) {

        int prevHeight  = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }



}
