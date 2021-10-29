package saneforce.sanclm.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.Model.ViewTagModel;
import saneforce.sanclm.activities.NearTagActivity;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.GPSTrack;

public class ViewTag extends Fragment {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    CommonSharedPreference commonSharedPreference;

    Marker marker;
    String db_connPath;
    String SF_Code;
    FloatingActionButton fab1, fab2, fab4, fab5, fab6;
    String chooseDep = "D";
    ImageView switch_view, back_img;
    FloatingActionMenu fb_menu;
    Api_Interface apiService;
    boolean isTap = false;
    TextView txt_tag, txt_cat;
    GPSTrack mGPSTrack;
    double laty = 0.0, lngy = 0.0;
    private int currentApiVersion;
    ProgressBar marker_progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View vv = inflater.inflate(R.layout.view_tag_map, container, false);
        commonSharedPreference = new CommonSharedPreference(getActivity());

        fab1 = (FloatingActionButton) vv.findViewById(R.id.menu_item);
        fab2 = (FloatingActionButton) vv.findViewById(R.id.menu_item1);
        fab5 = (FloatingActionButton) vv.findViewById(R.id.menu_item2);
        fab6 = (FloatingActionButton) vv.findViewById(R.id.menu_item3);
        switch_view = (ImageView) vv.findViewById(R.id.switch_view);
        fb_menu = (FloatingActionMenu) vv.findViewById(R.id.fb_menu);
        back_img = (ImageView) vv.findViewById(R.id.back_img);
        txt_cat = (TextView) vv.findViewById(R.id.txt_cat);
        txt_tag = (TextView) vv.findViewById(R.id.txt_tag);
        marker_progress=(ProgressBar)vv.findViewById(R.id.marker_progress);

        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);


        SF_Code = commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        db_connPath = commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);

        if(commonSharedPreference.getValueFromPreference("chem_need").equals("0"))
        fab2.setVisibility(View.VISIBLE);

        if(commonSharedPreference.getValueFromPreference("stk_need").equals("0"))
        fab5.setVisibility(View.VISIBLE);

        if(commonSharedPreference.getValueFromPreference("unl_need").equals("0"))
        fab6.setVisibility(View.VISIBLE);

        fab1.setLabelText(commonSharedPreference.getValueFromPreference("drcap"));
        fab2.setLabelText(commonSharedPreference.getValueFromPreference("chmcap"));
        fab5.setLabelText(commonSharedPreference.getValueFromPreference("stkcap"));
        fab6.setLabelText(commonSharedPreference.getValueFromPreference("ucap"));

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    if (commonSharedPreference.getValueFromPreference("switch_fragment").equalsIgnoreCase("false"))
                        marker_progress.setVisibility(View.VISIBLE);
                        getTagDetail(commonSharedPreference.getValueFromPreference("cat"));
                    Log.v("map_fragment_are123", "_equal_" + commonSharedPreference.getValueFromPreference("switch_fragment"));
                    commonSharedPreference.setValueToPreference("geo_tag", "1");

                   /* LatLng latLng = new LatLng(1.289545, 103.849972);
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .title("Singapore"));*/
                    //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    if (CurrentLoc()) {
                        laty = mGPSTrack.getLatitude();
                        lngy = mGPSTrack.getLongitude();
                        LatLng latLng = new LatLng(laty,lngy);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                    }

                   // Log.d("dataaakia",""+mMap.getMyLocation().getLatitude()+"---"+mMap.getMyLocation().getLongitude());
                   // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(,), 15.0f));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Log.v("marker_cliking",marker.getPosition()+" getting "+marker.getTitle());
                            return false;
                        }
                    });
                }
            });
        }
        else{
            Log.v("map_fragment_are123","null_equal_");
        }

        // R.id.map is a FrameLayout, not a Fragment

        if(commonSharedPreference.getValueFromPreference("switch_fragment").equalsIgnoreCase("true")){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRDRCallsSelection()).commit();
        }
        else{
            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Dorctor_fab_are","clicked_here");
                commonSharedPreference.setValueToPreference("geo_tag", "1");
               // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRDRCallsSelection()).commit();
                chooseDep="D";
                marker_progress.setVisibility(View.VISIBLE);
                getTagDetail("D");
                fb_menu.close(true);
                commonSharedPreference.setValueToPreference("cat","D");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new ViewTag()).commit();

            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Dorctcc_fab_are","clicked_here");
                //refreshMap();
                commonSharedPreference.setValueToPreference("geo_tag", "1");
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRCHMCallsSelection()).commit();
                chooseDep="C";
                marker_progress.setVisibility(View.VISIBLE);
                getTagDetail("C");
                fb_menu.close(true);
                commonSharedPreference.setValueToPreference("cat","C");
                Log.v("switch_dep_eq_ch",chooseDep);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new ViewTag()).commit();

            }
        });
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Dorctor_fab_are","clicked_here");
                commonSharedPreference.setValueToPreference("geo_tag", "1");
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRCHMCallsSelection()).commit();
                chooseDep="S";
                marker_progress.setVisibility(View.VISIBLE);
                getTagDetail("S");
                fb_menu.close(true);
                commonSharedPreference.setValueToPreference("cat","S");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new ViewTag()).commit();

            }
        });
        fab6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Dorctor_fab_are","clicked_here");
                commonSharedPreference.setValueToPreference("geo_tag", "1");
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRCHMCallsSelection()).commit();
                chooseDep="U";
                marker_progress.setVisibility(View.VISIBLE);
                getTagDetail("U");
                fb_menu.close(true);
                commonSharedPreference.setValueToPreference("cat","U");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new ViewTag()).addToBackStack(null).commit();

            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), HomeDashBoard.class);
                startActivity(i);
            }
        });

        switch_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRDRCallsSelection()).commit();
               /* Log.v("is_tap_print",isTap+" there ");
                if(isTap){
                    fb_menu.setVisibility(View.VISIBLE);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new ViewTag()).commit();
                    isTap=false;
                }
                else{*/
                    chooseDep=commonSharedPreference.getValueFromPreference("cat");
                    Log.v("switch_dep_eq",chooseDep+" shared "+commonSharedPreference.getValueFromPreference("cat")+" map_valide "+String.valueOf(R.id.map==0));
                    if (chooseDep.equalsIgnoreCase("D")) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRDRCallsSelection()).commit();
                    } else if (chooseDep.equalsIgnoreCase("C")) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRCHMCallsSelection()).commit();
                    }
                    else if(chooseDep.equalsIgnoreCase("S")){
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRSTKCallsSelection()).commit();
                    }
                    else{
                         getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRUDRCallsSelection()).commit();
                       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            getActivity().getSupportFragmentManager().beginTransaction().detach(new ViewTag()).commitNow();
                            getActivity().getSupportFragmentManager().beginTransaction().attach(new DCRUDRCallsSelection()).commitNow();
                        } else {
                            Log.v("here_new_app_call","created_hererfere");
                            getActivity().getSupportFragmentManager().beginTransaction().detach(new ViewTag()).attach(new DCRUDRCallsSelection()).commit();
                        }*/
                    }
                    isTap=true;
                    fb_menu.setVisibility(View.INVISIBLE);
                    switch_view.setVisibility(View.INVISIBLE);
                    back_img.setVisibility(View.INVISIBLE);
                    txt_tag.setVisibility(View.INVISIBLE);
                    txt_cat.setVisibility(View.INVISIBLE);
                //}
              /*  Log.v("Click_listener_swit",commonSharedPreference.getValueFromPreference("switch_fragment"));
                if (commonSharedPreference.getValueFromPreference("switch_fragment").equalsIgnoreCase("true")) {

                    //getTagDr();
                    //refreshMap();
                    commonSharedPreference.setValueToPreference("switch_fragment", "false");


                    isTap=false;
                    //refreshMap();
                   *//* getTagDetail(chooseDep);
                    *//*
                    fb_menu.setVisibility(View.VISIBLE);

                } else {
                    commonSharedPreference.setValueToPreference("switch_fragment", "true");
                   *//* chooseDep=commonSharedPreference.getValueFromPreference("cat");
                    Log.v("switch_dep_eq",chooseDep+" shared "+commonSharedPreference.getValueFromPreference("cat")+" map_valide "+String.valueOf(R.id.map==0));
                    if (chooseDep.equalsIgnoreCase("D")) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRDRCallsSelection()).commit();
                    } else if (chooseDep.equalsIgnoreCase("C")) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRCHMCallsSelection()).commit();
                    }
                    else if(chooseDep.equalsIgnoreCase("S")){
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRSTKCallsSelection()).commit();
                    }
                    else{
                       // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRUDRCallsSelection()).commit();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            getActivity().getSupportFragmentManager().beginTransaction().detach(new ViewTag()).commitNow();
                            getActivity().getSupportFragmentManager().beginTransaction().attach(new DCRUDRCallsSelection()).commitNow();
                        } else {
                            Log.v("here_new_app_call","created_hererfere");
                            getActivity().getSupportFragmentManager().beginTransaction().detach(new ViewTag()).attach(new DCRUDRCallsSelection()).commit();
                        }
                    }
                    isTap=true;
                    fb_menu.setVisibility(View.INVISIBLE);*//*
                }
                getActivity().getSupportFragmentManager().beginTransaction().
                        remove(new ViewTag()).commit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new ViewTag()).commit();*/

/*                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Fragment frg = null;
                    frg = getActivity().getSupportFragmentManager().findFragmentByTag("Tagging");
                    getActivity().getSupportFragmentManager().beginTransaction().detach(frg).commitNow();
                    getActivity().getSupportFragmentManager().beginTransaction().attach(frg).commitNow();
                } else {
                    Log.v("here_new_app_call","created_hererfere");
                    Fragment frg = null;
                    frg = getActivity().getSupportFragmentManager().findFragmentByTag("Tagging");
                    if(frg==null){
                        Log.v("here_new_app_call","created_hererferenull");
                    }
                    else{
                        Log.v("here_new_app_call","created_hererferenotnull");
                    }
                   // getActivity().getSupportFragmentManager().beginTransaction().detach(frg).attach(frg).commit();

                }*/
            }
        });
/*
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new ViewTag()).commit();

            }
        });
*/
 /* getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .detach(new ViewTag())
                                .commitNowAllowingStateLoss();

                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .attach(new DCRCHMCallsSelection())
                                .commitAllowingStateLoss();*/
        commonFun();
       //  view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        return vv;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            Log.v("onAttach_method_are","are_called_here");
        }
    }
    public void CheckLocation(){
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle(getResources().getString(R.string.enable_location));
                alertDialog.setCancelable(false);
                alertDialog.setMessage(getResources().getString(R.string.alert_location));
                alertDialog.setPositiveButton(getResources().getString(R.string.location_setting), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                    }
                });
           /* alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });*/
                AlertDialog alert = alertDialog.create();
                alert.show();


            }
        }catch (Exception e){
            Toast toast=Toast.makeText(getActivity(), getResources().getString(R.string.loction_detcted), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

    }
    public boolean CurrentLoc(){

        mGPSTrack=new GPSTrack(getActivity());

        CheckLocation();
        // CurrentLoc();

        return true;
    }
    public void getAllLatLng() {
       Log.v("map_camera_tt_12", "are_calling"+(mMap!=null)+"nm"+(mapFragment!=null));
       mMap.clear();
        for(int i=0;i< NearTagActivity.list.size();i++){
            ViewTagModel mm=NearTagActivity.list.get(i);
            LatLng latLng = new LatLng(Double.parseDouble(mm.getLat()), Double.parseDouble(mm.getLng()));

            marker= mMap.addMarker(new MarkerOptions().position(latLng)
                    .title(mm.getName()));
            // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng),15.0f);
           // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            Log.v("map_camera_tt", mm.getLat());
        }
//        if(NearTagActivity.list.size() == 0){
//            LatLng latLng = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
//          //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( latLng, 15.0f));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
//            Log.d("dataaakia",""+mMap.getMyLocation().getLatitude()+"---"+mMap.getMyLocation().getLongitude());
//        }

   }

    public void getTagDr(){
        NearTagActivity.list.clear();
        JSONObject json=new JSONObject();
        try {
            json.put("SF",SF_Code);
            json.put("cust","D");
            Log.v("print_json_bb",json.toString()+" db_path "+db_connPath);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<ResponseBody> taggedDr=apiService.getTagDr(json.toString());

        taggedDr.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.v("printing_responsee",response.body().byteStream()+"");
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

                    Log.v("printing_tagged_dr22",is.toString());
                    if(is.toString().trim().isEmpty()){

                    }
                    else{
                        try{
                            JSONArray json=new JSONArray(is.toString());
                            for(int i=0;i<json.length();i++){
                                JSONObject jsony=json.getJSONObject(i);
                                if(jsony.getString("lat").trim().isEmpty() || jsony.getString("long").trim().isEmpty()){

                                }
                                else {
                                    NearTagActivity.list.add(new ViewTagModel(jsony.getString("Cust_Code"), jsony.getString("Doctor_Name"),
                                            jsony.getString("lat"), jsony.getString("long"),jsony.getString("ListedDr_Address1")));

                                   /* LatLng latLng = new LatLng(Double.parseDouble(jsonObject.getString("lat")) , Double.parseDouble(jsonObject.getString("long")));
                                    mMap.addMarker(new MarkerOptions().position(latLng)
                                            .title("Singapore"));
                                    // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng),15.0f);
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                                    Log.v("map_camera_tt",jsonObject.getString("Cust_Code"));*/
                                }
                            }
                            getAllLatLng();
                        }catch (JSONException e){}
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void getTagDetail(String cust){
        NearTagActivity.list.clear();
        JSONObject json=new JSONObject();
        try {
            json.put("SF",SF_Code);
            json.put("cust",cust);
            Log.v("print_json_bb",json.toString()+" db_path "+db_connPath);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<ResponseBody> taggedDr=apiService.getTagDr(json.toString());

        taggedDr.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.v("printing_responsee",response.body().byteStream()+"");
                    JSONObject jsonObject = null;
                    String jsonData = null;
                    marker_progress.setVisibility(View.GONE);

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

                    Log.v("printing_tagged_che22",is.toString());
                    if(is.toString().trim().isEmpty()){

                    }
                    else{
                        try{
                            JSONArray json=new JSONArray(is.toString());
                            for(int i=0;i<json.length();i++){
                                JSONObject jsony=json.getJSONObject(i);
                                if(jsony.getString("lat").trim().isEmpty() || jsony.getString("long").trim().isEmpty()){

                                }
                                else {
                                    Log.v("printing_tagged_che2233",jsony.getString("lat"));
                                    NearTagActivity.list.add(new ViewTagModel(jsony.getString("Cust_Code"), jsony.getString("name"),
                                            jsony.getString("lat"), jsony.getString("long"),jsony.getString("addr")));

                                   /* LatLng latLng = new LatLng(Double.parseDouble(jsonObject.getString("lat")) , Double.parseDouble(jsonObject.getString("long")));
                                    mMap.addMarker(new MarkerOptions().position(latLng)
                                            .title("Singapore"));
                                    // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng),15.0f);
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                                    Log.v("map_camera_tt",jsonObject.getString("Cust_Code"));*/
                                }
                            }
                            Log.v("total_excep_12","calling"+"print");
                            getAllLatLng();
                            Log.v("total_excep_1221","calling"+"print");
                        }catch (JSONException e){
                            Log.v("total_excep",e.getMessage()+"print");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                marker_progress.setVisibility(View.GONE);
                Log.e("on failure",t.getMessage());
            }
        });
    }
    public void getTagStock(){}
    public void getTagUDr(){}

    public void refreshMap(){
        if (mapFragment == null) {
            Log.v("map_fragment_are34","_equal_");
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Log.v("map_fragment_are","_equal_");
                    mMap=googleMap;
                   // getTagDr();
                    commonSharedPreference.setValueToPreference("geo_tag", "1");

                   /* LatLng latLng = new LatLng(1.289545, 103.849972);
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .title("Singapore"));*/
                    //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Log.v("marker_cliking",marker.getPosition()+" getting "+marker.getTitle());
                            return false;
                        }
                    });
                }
            });
        }
        else{
            Log.v("map_fragment_are","not_equal_");
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v("onAttach_method_are","are_called_here123");
/*
        try{
            if (commonSharedPreference.getValueFromPreference("switch_fragment").equalsIgnoreCase("true")) {
                getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(new DCRUDRCallsSelection()).commit();
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    getActivity().getSupportFragmentManager().beginTransaction().detach(this).commitNow();
                    getActivity().getSupportFragmentManager().beginTransaction().attach(this).commitNow();
                } else {
                    Log.v("here_new_app_call", "created_hererfere");
                    getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
            }
            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new ViewTag()).commit();
        }catch (Exception e){}
*/
    }
    public void commonFun(){
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }
//    @SuppressLint("NewApi")
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus)
//    {
//        super.onWindowFocusChanged(hasFocus);
//        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
//        {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }
}
