package saneforce.sanclm.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.R;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.Model.ViewTagModel;
import saneforce.sanclm.activities.NearTagActivity;
import saneforce.sanclm.adapter_class.nearMeAdapter;
import saneforce.sanclm.adapter_interfaces.FindRouteListener;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.applicationCommonFiles.GPSTrack;
import saneforce.sanclm.util.ProductChangeListener;
import saneforce.sanclm.util.UpdateUi;

public class NearMe extends Fragment {
    private SupportMapFragment mapFragment;
    double laty = 0.0, lngy = 0.0, limitKm = 0.5;
    Marker marker;
    GoogleMap mMap;
    GPSTrack mGPSTrack;
    private final int transparentBlue = 0x300000FF;
    RelativeLayout rl_popup, rl_inner_lay;
    Button btn_route;
    ArrayList<ViewTagModel> arr = new ArrayList<ViewTagModel>();
    RecyclerView recyclerView;
    CarouselLayoutManager layoutManager;
    PolylineOptions lineOptions = null;
    Polyline polyline;
    StringBuilder sb;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    ArrayList<Marker> mark = new ArrayList<>();
    ImageView switch_view;
    FloatingActionButton fab1,fab2,fab4,fab5,fab6;
    FloatingActionMenu fb_menu;
    CommonSharedPreference commonSharedPreference;
    String db_connPath,SF_Code,chooseDep="D";
    Api_Interface apiService;
    ImageView back_img;
    TextView txt_tag,txt_cat;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vv = inflater.inflate(R.layout.view_tag_map, container, false);
        recyclerView = (RecyclerView) vv.findViewById(R.id.recycle);
        switch_view=(ImageView)vv.findViewById(R.id.switch_view);
        layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true);
        switch_view.setVisibility(View.VISIBLE);

        switch_view.setImageResource(R.drawable.icon_refresh);
        commonSharedPreference=new CommonSharedPreference(getActivity());
        back_img=(ImageView)vv.findViewById(R.id.back_img);

        fab1=(FloatingActionButton)vv.findViewById(R.id.menu_item);
        fab2=(FloatingActionButton)vv.findViewById(R.id.menu_item1);
        fab5=(FloatingActionButton)vv.findViewById(R.id.menu_item2);
        fab6=(FloatingActionButton)vv.findViewById(R.id.menu_item3);
        fb_menu=(FloatingActionMenu)vv.findViewById(R.id.fb_menu);
        txt_tag=(TextView)vv.findViewById(R.id.txt_tag);
        txt_cat=(TextView)vv.findViewById(R.id.txt_cat);
        txt_tag.setText(R.string.refresh);

        fab1.setLabelText(commonSharedPreference.getValueFromPreference("drcap"));
        fab2.setLabelText(commonSharedPreference.getValueFromPreference("chmcap"));
        fab5.setLabelText(commonSharedPreference.getValueFromPreference("stkcap"));
        fab6.setLabelText(commonSharedPreference.getValueFromPreference("ucap"));


        SF_Code =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_SF_CODE);
        db_connPath =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        apiService = RetroClient.getClient(db_connPath).create(Api_Interface.class);
        limitKm = Double.parseDouble(commonSharedPreference.getValueFromPreference("radius"));


        /*rl_popup=(RelativeLayout)vv.findViewById(R.id.rl_popup);
        rl_inner_lay=(RelativeLayout)vv.findViewById(R.id.rl_inner_lay);
        btn_route=(Button)vv.findViewById(R.id.btn_route);*/
        if (CurrentLoc()) {
            laty = mGPSTrack.getLatitude();
            lngy = mGPSTrack.getLongitude();

        }

        if (mapFragment == null) {
                        mapFragment = SupportMapFragment.newInstance();
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                mMap = googleMap;
                    laty = mGPSTrack.getLatitude();
                    lngy = mGPSTrack.getLongitude();
                   /* marker = mMap.addMarker(new MarkerOptions().position(new LatLng(laty, lngy))
                            .title("Current").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));*/

                                if (CurrentLoc()) {
                                    LatLng latLng = new LatLng(laty, lngy);
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                                }
                    Log.v("latitude_near", laty + " lngy " + lngy);
                    getAllLatLng();
                    addCircle();
                    drDetail();

                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), 15.0f));

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Log.v("marker_cliking", marker.getId().substring(1) + " getting " + marker.getTitle()+" pos "+mHashMap.get(marker));
                           /* if (marker.getId().substring(1).equalsIgnoreCase("0")) {

                            } else {*/
                              //  int x = Integer.parseInt(marker.getId().substring(1));
                                    int x = Integer.parseInt(marker.getId().substring(1));
                                Log.v("marker_cliking_scroll", "to_pos" + (x));
                                recyclerView.scrollToPosition(x);
                            //}
                            return false;
                        }
                    });
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



                             //   Log.d("thelist", String.valueOf(laty+lngy));

                                    // LatLng latLng = new LatLng(laty, lngy);
                   /* mMap.addMarker(new MarkerOptions().position(latLng)
                            .title("Singapore"));*/
                    //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            });
        }

        // R.id.map is a FrameLayout, not a Fragment
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Dorctor_fab_are","clicked_here");
                commonSharedPreference.setValueToPreference("geo_tag", "1");
                commonSharedPreference.setValueToPreference("cat","D");
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRDRCallsSelection()).commit();
                chooseDep="D";
                getTagDetail("D");
                fb_menu.close(true);

            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Dorctcc_fab_are","clicked_here");
                //refreshMap();
                commonSharedPreference.setValueToPreference("cat","C");
                commonSharedPreference.setValueToPreference("geo_tag", "1");
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRCHMCallsSelection()).commit();
                chooseDep="C";
                getTagDetail("C");
                fb_menu.close(true);

            }
        });
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Dorctor_fab_are","clicked_here");
                commonSharedPreference.setValueToPreference("geo_tag", "1");
                commonSharedPreference.setValueToPreference("cat","S");
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRCHMCallsSelection()).commit();
                chooseDep="S";
                getTagDetail("S");
                fb_menu.close(true);

            }
        });
        fab6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Dorctor_fab_are","clicked_here");
                commonSharedPreference.setValueToPreference("geo_tag", "1");
                commonSharedPreference.setValueToPreference("cat","U");
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRCHMCallsSelection()).commit();
                chooseDep="U";
                getTagDetail("U");
                fb_menu.close(true);

            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), HomeDashBoard.class);
                startActivity(i);
                /**/
            }
        });



        switch_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences shares=getActivity().getSharedPreferences("location",0);
                String lat=shares.getString("lat","");
                String lng=shares.getString("lng","");
                if(TextUtils.isEmpty(lat)){
                    laty=0.0;
                    lngy=0.0;
                }
                else {
                    laty = Double.parseDouble(lat);
                    lngy = Double.parseDouble(lng);
                }
                mMap.clear();
                getAllLatLng();
                addCircle();
                drDetail();

            }
        });

        nearMeAdapter.bindFindListener(new FindRouteListener() {
            @Override
            public void findRoute(ArrayList points) {
                Log.v("near_me_tag","points_came"+points);
                lineOptions = new PolylineOptions();
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);
                if(polyline!=null)
                    polyline.remove();
                polyline= mMap.addPolyline(lineOptions);
                //mMap.setTrafficEnabled(true);
            }
        });

        nearMeAdapter.bindListenerForPosition(new ProductChangeListener() {
            @Override
            public void checkPosition(int i) {
                double latt= Double.parseDouble(arr.get(i).getLat());
                double lng= Double.parseDouble(arr.get(i).getLng());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lng), 15.0f));
                mark.get(i).showInfoWindow();
            }
        });
        nearMeAdapter.bindListenerForSwift(new UpdateUi() {
            @Override
            public void updatingui() {
                switch_view.setVisibility(View.VISIBLE);
                if(commonSharedPreference.getValueFromPreference("cat").equalsIgnoreCase("D")){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRDRCallsSelection()).commit();
                }
                else if(commonSharedPreference.getValueFromPreference("cat").equalsIgnoreCase("C")){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRCHMCallsSelection()).commit();
                }
                else if(commonSharedPreference.getValueFromPreference("cat").equalsIgnoreCase("S")){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRSTKCallsSelection()).commit();
                }
                else
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new DCRUDRCallsSelection()).commit();

                fb_menu.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                switch_view.setVisibility(View.INVISIBLE);
                back_img.setVisibility(View.INVISIBLE);
                txt_tag.setVisibility(View.INVISIBLE);
                txt_cat.setVisibility(View.INVISIBLE);
            }
        });



      /*  rl_popup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v("tapping_here_lay","are_clciek");
                return false;
            }
        });*/

       /* rl_popup.setOnTouchListener(new OnSwipeTouchListener(getActivity(),true){
            public void onSwipeTop() {
                Log.v("swiping_top","are_clciek");
                Animation slide_down = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_down);

                Animation slide_up = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_up);
                rl_inner_lay.startAnimation(slide_up);
                //popupScribbling(2,slideDescribe.get(presentSlidePos).getSlideUrl());
            }
            public void onSwipeBottom(){
                Log.v("swiping_btm","are_clciek");
            }
        });*/

       /* btn_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("tapping_here","are_clciek");
            }
        });
*/
        commonFun();
        return vv;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            /*try{
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, new NearMe()).commit();
            }catch (Exception e){}*/
            getAllLatLng();
        }
    }

    public void getAllLatLng() {
        arr.clear();
        mark.clear();
        mHashMap.clear();
        mMap.clear();
        for (int i = 0; i < NearTagActivity.list.size(); i++) {
            ViewTagModel mm = NearTagActivity.list.get(i);

                if (distance(laty, lngy, Double.parseDouble(mm.getLat()), Double.parseDouble(mm.getLng())) < limitKm) {
                    LatLng latLng = new LatLng(Double.parseDouble(mm.getLat()), Double.parseDouble(mm.getLng()));

                    marker = mMap.addMarker(new MarkerOptions().position(latLng)
                            .title(mm.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng),15.0f);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                    Log.v("map_camera_tt_near", mm.getLat()+" name "+mm.getName());
                    arr.add(mm);
                    mHashMap.put(marker, i);
                    Log.v("hashmap_nearrr ",mHashMap.size()+" gett "+arr.size());
                    mark.add(marker);

                }

        }


        addCircle();
        drDetail();
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

    public boolean CurrentLoc(){

        mGPSTrack=new GPSTrack(getActivity());

            CheckLocation();
           // CurrentLoc();

        return true;
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

    private void addCircle() {
        LatLng latLng = new LatLng(laty,lngy);
        CircleOptions circle = new CircleOptions()
                .center(latLng)
                .radius(1000.0)
                .strokeColor(Color.RED)
                .fillColor(transparentBlue)
                .clickable(true);
        mMap.addCircle(circle);
    }

    public void drDetail(){

        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new nearMeAdapter(getActivity(),arr,String.valueOf(laty),String.valueOf(lngy)));
        recyclerView.addOnScrollListener(new CenterScrollListener());

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

            }
        });
    }


    public void commonFun(){
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

}
