package saneforce.sanclm.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.activities.Model.ViewTagModel;
import saneforce.sanclm.adapter_class.ExploreMapAdapter;
import saneforce.sanclm.applicationCommonFiles.GPSTrack;
import saneforce.sanclm.util.ProductChangeListener;

public class ExploreMap extends Fragment {
    StringBuilder sb, place;
    static String googlePlacesData, placeDetail;
    double laty = 0.0, lngy = 0.0, limitKm = 0.5;
    Marker marker;
    GoogleMap mMap;
    GPSTrack mGPSTrack;
    private SupportMapFragment mapFragment;
    ArrayList<ViewTagModel> arr = new ArrayList<ViewTagModel>();
    RecyclerView recyclerView;
    CarouselLayoutManager layoutManager;
    ArrayList<Marker> mark = new ArrayList<>();
    boolean withinKmTouch = false;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    ImageView marker_loc;
    FloatingActionMenu floatingActionMenu;
    ImageView switch_view,back_img;
    TextView txt_tag,txt_cat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vv = inflater.inflate(R.layout.view_tag_map, container, false);
        recyclerView = (RecyclerView) vv.findViewById(R.id.recycle);
        layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true);
        marker_loc = (ImageView) vv.findViewById(R.id.marker_loc);
        floatingActionMenu=(FloatingActionMenu)vv.findViewById(R.id.fb_menu);
        switch_view=(ImageView)vv.findViewById(R.id.switch_view);
        back_img=(ImageView)vv.findViewById(R.id.back_img);
        txt_tag=(TextView)vv.findViewById(R.id.txt_tag);
        txt_cat=(TextView)vv.findViewById(R.id.txt_cat);
        txt_tag.setVisibility(View.INVISIBLE);
        txt_cat.setVisibility(View.INVISIBLE);
        floatingActionMenu.setVisibility(View.INVISIBLE);
        switch_view.setVisibility(View.INVISIBLE);

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

                    Log.v("latitude_near123", laty + " lngy " + lngy);
                   /* marker = mMap.addMarker(new MarkerOptions().position(new LatLng(laty, lngy))
                            .title("Current").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));*/
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(laty, lngy), 15.0f));
                    //setMarkerBounce(marker);

                    /*if (markers[index].getAnimation() != google.maps.Animation.BOUNCE) {
                        markers[index].setAnimation(google.maps.Animation.BOUNCE);
                    } else {
                        markers[index].setAnimation(null);
                    }*/
                    getExploreDr();

                    /* getDrDetail(googlePlacesData);*/
                   /* getAllLatLng();
                    addCircle();
                    drDetail();*/
                   /* LatLng latLng = new LatLng(1.289545, 103.849972);
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .title("Singapore"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));*/

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Log.v("marker_cliking", marker.getId().substring(1) + " getting " + mHashMap.get(marker));
                            marker_loc.setVisibility(View.INVISIBLE);
                            withinKmTouch = true;
                            int yy = arr.size() - 1;
                            /*if(marker.getId().substring(1).equalsIgnoreCase("0")){

                            }
                            else{*/
                            // int x= Integer.parseInt(marker.getId().substring(1));
                            int x = mHashMap.get(marker);
                            Log.v("marker_cliking_scroll", "to_pos" + (x - 1));
                            recyclerView.scrollToPosition(x);
                            // }
                            return false;
                        }
                    });

                    mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                        @Override
                        public void onCameraMove() {

                            if (!withinKmTouch) {
                                Log.v("screen_layyy123456", " observer ");
                                marker_loc.setVisibility(View.VISIBLE);
                                Log.v("centerLat_move", mMap.getCameraPosition().target.latitude + "");
                                laty = mMap.getCameraPosition().target.latitude;
                                lngy = mMap.getCameraPosition().target.longitude;
                            }

                            //imgLocationPinUp.setVisibility(View.VISIBLE);

                        }
                    });

                    mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {
                            if (withinKmTouch) {
                                withinKmTouch = false;
                            } else {
                                mMap.clear();
                                mark.clear();
                                mHashMap.clear();
                                withinKmTouch = true;
                                getExploreDr();
                                withinKmTouch = true;

                            }
               /* imgLocationPinUp.setVisibility(View.GONE);
                mMap.clear();
                Log.v("centerLat_idle",mMap.getCameraPosition().target+"");
                MarkerOptions markerOptions =new MarkerOptions().position(mMap.getCameraPosition().target)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                mMap.addMarker(markerOptions);*/

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

//                    LatLng latLng = new LatLng(laty, lngy);
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));


                }
            });

            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

            ExploreMapAdapter.bindListenerForPosition(new ProductChangeListener() {
                @Override
                public void checkPosition(int i) {
                    marker_loc.setVisibility(View.INVISIBLE);
                    double latt= Double.parseDouble(arr.get(i).getLat());
                    double lng= Double.parseDouble(arr.get(i).getLng());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lng), 15.0f));
                    mark.get(i).showInfoWindow();
                    withinKmTouch=true;
                   // setMarkerBounce(mark.get(i));

                }
            });

            back_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getActivity(), HomeDashBoard.class);
                    startActivity(i);
                }
            });

        }


        return vv;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.v("get_dr_detttt","isvisible");
        if(isVisibleToUser){
            getExploreDr();
            withinKmTouch=true;
        }

    }

    public void getExploreDr(){
        sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location="+laty+","+lngy);
        sb.append("&radius=5000");
        sb.append("&types=doctor");
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyD4E3jMHLmeOW9HFfyIXkk-2aFElUhKzUw");
        Log.v("Doctor_detail_print",sb.toString());

        new findDrDetail().execute();
    }

    class findDrDetail extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            DownloadUrl downloadUrl = new DownloadUrl();
            try {
                googlePlacesData = downloadUrl.readUrl(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.v("get_dr_detttt",googlePlacesData);
            getDrDetail(googlePlacesData);
        }
    }

    public class DownloadUrl {

        public String readUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer("");

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                Log.d("downloadUrl", data.toString());
                br.close();

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }
    }

    public void getDrDetail(String placesdata){
        arr.clear();
        try {
            JSONObject jsonObject=new JSONObject(placesdata);
            JSONArray jsonArray=jsonObject.getJSONArray("results");

            for(int i=0;i<jsonArray.length();i++){
                JSONObject json=jsonArray.getJSONObject(i);
                String lat=json.getJSONObject("geometry").getJSONObject("location").getString("lat");
                String lng=json.getJSONObject("geometry").getJSONObject("location").getString("lng");
                String name=json.getString("name");
                String place_id=json.getString("place_id");
                String vicinity=json.getString("vicinity");
                String photoo="",reference="",height="",width="";
                try{
                    JSONArray jsonA=json.getJSONArray("photos");
                    JSONObject jo=jsonA.getJSONObject(0);
                    JSONArray ja=jo.getJSONArray("html_attributions");
                    //JSONObject jo1=ja.getJSONObject(0);
                     photoo=ja.getString(0);
                     reference=jo.getString("photo_reference");
                     height=jo.getString("height");
                     width=jo.getString("width");
                    Log.v("direction_latt",name+"phototss"+photoo);

                }catch (JSONException e){}


                Log.v("direction_latt",name+"phototss");
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                marker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                mark.add(marker);
                // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng),15.0f);
               // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
//https://maps.googleapis.com/maps/api/place/photo?photoreference=CmRaAAAAZGVKYoIApQ0FY9qrZcoSquyHJTmRzKG6cwIUAeDA7ARoddKmfSG9mb1KUYzBMkxj7IWR9efFGWhKLyyivm2gkWvdhWQZtqBc0GszOetFku_7MfjGyrhCJ5vgs2Il4U8vEhCwnDmXrtkrQWQYxUxKH_heGhSTiUC8g9jAdoZ0BJTMN5dEXXLJDA&sensor=false&maxheight=MAX_HEIGHT&maxwidth=MAX_WIDTH&key=YOUR_API_KEY
                arr.add(new ViewTagModel(photoo,name,lat,lng,vicinity,reference,height,width,place_id));
                mHashMap.put(marker, i);

            }

            drDetail();


        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                alertDialog.setTitle("Enable Location");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
                alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
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
            Toast toast=Toast.makeText(getActivity(), " Location cannot detected ", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

    }

    public void drDetail(){
        Log.v("explore_array",arr.size()+"");
        if(getActivity()!=null) {
            ExploreMapAdapter explore = new ExploreMapAdapter(getActivity(), arr, String.valueOf(laty), String.valueOf(lngy));
            layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
            Log.v("explore_array11", arr.size() + "");
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            Log.v("explore_array22", arr.size() + "");
            recyclerView.setAdapter(explore);
            recyclerView.addOnScrollListener(new CenterScrollListener());
            Log.v("explore_array33", arr.size() + "");
            explore.notifyDataSetChanged();
            Log.v("explore_array44", arr.size() + "");
        }

    }
    private void setMarkerBounce(final Marker marker) {
        final Handler handler = new Handler();
        final long startTime = SystemClock.uptimeMillis();
        final long duration = 500;
        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - startTime;
                float t = Math.max(1 - interpolator.getInterpolation((float) elapsed/duration), 0);
                marker.setAnchor(0.5f, 1.0f +  t);

                if (t > 0.0) {
                    handler.postDelayed(this, 16);
                } else {
                    setMarkerBounce(marker);
                }
            }
        });
    }

    public void getPlaceID(String placesdata,int pos){
       // https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJYe6-4klmUjoRUVZMrQ6ThEI&key=AIzaSyD4E3jMHLmeOW9HFfyIXkk-2aFElUhKzUw

                place = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        place.append("placeid="+placesdata);
        place.append("&key=AIzaSyD4E3jMHLmeOW9HFfyIXkk-2aFElUhKzUw");
        Log.v("Doctor_detail_print",place.toString());
    }

}
