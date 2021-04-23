package saneforce.sanclm.activities;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelStoringFileTime;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.util.PaintDraw;

public class DummyActivity extends AppCompatActivity {

    Api_Interface ret;
    PaintDraw paintDraw;
    Button btn;
    ImageView imgg;
    CommonSharedPreference mCommonSharedPreference;
    ArrayList<ModelStoringFileTime> arrayStore = new ArrayList<>();
    ArrayList<ModelStoringFileTime> prdDetail = new ArrayList<>();
    String defaulttime = "00:00:00";
    int val;
    String startT, endT;
    String finalPrdNam;
    StringBuilder sb;
    ListView missed_list;
    ArrayList<String> arr=new ArrayList<>();
    RelativeLayout tag,nearme,explore;
    View view_up;
    RelativeLayout rl_up;
    LinearLayout viewpage_lay;
    GestureDetector gestureDetector;

    BarChart chart;
    ArrayList<String> xAxisValues = new ArrayList<String>();
    ArrayList<BarEntry> yValueGroup1 = new ArrayList<BarEntry>();
    ArrayList<BarEntry> yValueGroup2 = new ArrayList<BarEntry>();
    BarDataSet barDataSet1,barDataSet2;

    float barWidth;
    float barSpace;
    float groupSpace;
    int[] colorsss=new int[]{Color.RED,Color.CYAN,Color.YELLOW};
    int groupCount = 6;
    LinearLayout bottom_sheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=13.03033788544628,80.24131196341442");
        sb.append("&radius=5000");
        sb.append("&types=doctor");
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyCO3YZEo8VhfQt1LtvDJIppMS8NXroMXwE");

        Log.v("Doctor_detail_print",sb.toString());

        arr.add("Apple");
        arr.add("Apple");
        arr.add("Apple");
        arr.add("Apple");
        arr.add("Apple");
        arr.add("Apple");
        bottom_sheet=(LinearLayout)findViewById(R.id.bottom_sheet);

        String myDate = "2019/11/25 15:45:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        startAlert(millis);

        final Dialog dialog=new Dialog(DummyActivity.this,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.row_item_reject_reason);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        //bottom_sheet.setVisibility(View.GONE);

        //chart=(BarChart)findViewById(R.id.chartConsumptionGraph);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashBoardMenu()).commit();
        //UpperGraph();
       /* barWidth = 0.15f;
        barSpace = 0.07f;
        groupSpace = 0.56f;

        BarDataSet bds=new BarDataSet(allVal(),"data set");
        BarDataSet bds1=new BarDataSet(allyval(),"data set1");
        bds.setColors(colorsss);

        BarData bar=new BarData(bds,bds1);

        mChart.setData(bar);

        mChart.getBarData().setBarWidth(barWidth);
        mChart.getXAxis().setAxisMinimum(0);
        mChart.getXAxis().setAxisMaximum(0 + mChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        mChart.groupBars(0, groupSpace, barSpace);
        mChart.getData().setHighlightEnabled(false);
        mChart.invalidate();
*/
        /*xAxisValues.add("Jan");
        xAxisValues.add("Feb");
        xAxisValues.add("Mar");
        xAxisValues.add("Apr");
        xAxisValues.add("May");
        xAxisValues.add("June");
        xAxisValues.add("Jul");
        xAxisValues.add("Aug");
        xAxisValues.add("Sep");
        xAxisValues.add("Oct");
        xAxisValues.add("Nov");
        xAxisValues.add("Dec");

        setAllData();

        barDataSet1 = new BarDataSet(yValueGroup1, "YR");
        barDataSet1.setColors(Color.BLUE, Color.RED);
        barDataSet1.setDrawIcons(false);
        barDataSet1.setDrawValues(true);

        barDataSet2 = new BarDataSet(yValueGroup2, "dfdfa");
        barDataSet2.setColors(Color.YELLOW, Color.RED);
        barDataSet2.setDrawIcons(false);
        barDataSet2.setDrawValues(true);

        barDataSet2.setDrawIcons(false);
        barDataSet2.setDrawValues(true);

        BarData data = new BarData(barDataSet1, barDataSet2);

       *//* barChart.description.isEnabled = false
        barChart.description.textSize = 0f
        data.setValueFormatter(LargeValueFormatter())*//*
        data.setValueFormatter(new LargeValueFormatter());
        barChart.setData(data);
        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinimum(0f);
        barChart.getXAxis().setAxisMaximum(12f);
        barChart.groupBars(0f, groupSpace, barSpace);
        barChart.getData().setHighlightEnabled(false);
        barChart.invalidate();

        Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        ArrayList<LegendEntry> legenedEntries = new ArrayList<>();

        legenedEntries.add(new LegendEntry("2016", Legend.LegendForm.SQUARE, 8f, 8f, null, Color.RED));
        legenedEntries.add(new LegendEntry("2017", Legend.LegendForm.SQUARE, 8f, 8f, null, Color.YELLOW));

        legend.setCustom(legenedEntries);

        legend.setYOffset(2f);
        legend.setXOffset(2f);
        legend.setYEntrySpace(0f);
        legend.setTextSize(5f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        //xAxis.textSize = 9f;

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));

        xAxis.setLabelCount(12);
        xAxis.mAxisMaximum = 12f;
        xAxis.setCenterAxisLabels(true);
        xAxis.setAvoidFirstLastClipping(true);
        *//*xAxis.spaceMin = 4f
        xAxis.spaceMax = 4f
*//*
        barChart.setVisibleXRangeMaximum(12f);
        barChart.setVisibleXRangeMinimum(12f);
        barChart.setDragEnabled(true);

        //Y-axis
        barChart.getAxisRight().setEnabled(false);
        barChart.setScaleEnabled(true);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(1f);
        leftAxis.setAxisMinimum(0f);


        barChart.setVisibleXRange(1f, 12f);
*/
      /*  view_up=(View)findViewById(R.id.view_up);
        rl_up=(RelativeLayout)findViewById(R.id.rl_up);
        viewpage_lay=(LinearLayout) findViewById(R.id.viewpage_lay);*/
/*
        view_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v("View_in_demo_","are_touching_here");
                return false;
            }
        });
*/




      /*  GestureDetector.SimpleOnGestureListener simpleOnGestureListener
                = new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                Log.v("onfling_methodsss","velocity_are");
                float sensitvity = 50;
                if((e1.getX() - e2.getX()) > sensitvity){
                  //  vf.showPrevious();
                }else if((e2.getX() - e1.getX()) > sensitvity){
                   // vf.showNext();
                }

                return true;
            }

        };

         gestureDetector
                = new GestureDetector(simpleOnGestureListener);*/

      /*  rl_up.setOnTouchListener(new OnSwipeTouchListener(DummyActivity.this){
            public void onSwipeTop() {
                Log.v("swiping_top","are_clciek");
                //popupScribbling(2,slideDescribe.get(presentSlidePos).getSlideUrl());
            }
        });*/
      /*  tag=(RelativeLayout)findViewById(R.id.tag);
        nearme=(RelativeLayout)findViewById(R.id.nearme);
        explore=(RelativeLayout)findViewById(R.id.explore);

        getSupportFragmentManager().beginTransaction().replace(R.id.maps, new ViewTag()).commit();

        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.maps, new ViewTag()).commit();
                nearme.setBackground(null);
                explore.setBackground(null);
                tag.setBackgroundColor(Color.RED);
            }
        });
        nearme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.maps, new NearMe()).commit();
                tag.setBackground(null);
                explore.setBackground(null);
                nearme.setBackgroundColor(Color.RED);
            }
        });
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.maps, new ExploreMap()).commit();
                tag.setBackground(null);
                nearme.setBackground(null);
                explore.setBackgroundColor(Color.RED);
            }
        });*/

       // new DummyTask().execute();
        //imgg = (ImageView) findViewById(R.id.imgg);
        /*BetterVideoPlayer player = findViewById(R.id.player);

        // Set the source to the HTTP URL held in the TEST_URL variable.
        // To play files, you can use Uri.fromFile(new File("..."))
        player.setSource(Uri.parse("/storage/emulated/0/Products/ReninAngiotensinAldosteroneSystem.avi"));*/
        /*mCommonSharedPreference = new CommonSharedPreference(DummyActivity.this);
        final Dialog dialog=new Dialog(DummyActivity.this,R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_add_dcr);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();*/
        //  SerialModel mm=new SerialModel("Ezhil","arasi");
        /*Intent i=new Intent(DummyActivity.this,DumTry.class);
        i.putExtra("obj",mm);
        startActivity(i);*/

       /* paintDraw=(PaintDraw)findViewById(R.id.paint_draw);

        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintDraw.changeRect();
            }
        });*/


       /* ret=Retro.getClient().create(Api_Interface.class);

        Call<ResponseBody> user=ret.createUser("hello","hello");

        user.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    String jsonData = null;

                    InputStreamReader ip=null;
                    StringBuilder is=new StringBuilder();
                    String line=null;
                    try {
                        ip = new InputStreamReader(response.body().byteStream());
                        BufferedReader bf = new BufferedReader(ip);

                        while ((line = bf.readLine()) != null) {
                            is.append(line);
                        }
                        Log.v("user_response",is.toString());
                    }catch (Exception e){
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
*/
      /*  try {
            File compressedImageFile = new Compressor(this).compressToFile(new File("/storage/emulated/0/Products/Brand_Priority_Visit.png"));
            Log.v("printing_compressed_",compressedImageFile.getPath());
            imgg.setImageURI(Uri.parse(compressedImageFile.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

       /*  val=mCommonSharedPreference.getValueFromPreferenceFeed("timeCount",0);
        for(int i=0;i<val;i++){
            String timevalue=mCommonSharedPreference.getValueFromPreferenceFeed("timeVal"+i);
            String prdName=mCommonSharedPreference.getValueFromPreferenceFeed("slide_nam"+i);
            String prdddName=mCommonSharedPreference.getValueFromPreferenceFeed("brd_nam"+i);
            Log.v("prdName_show",prdName);
            String eTime;
            if (arrayStore.contains(new ModelStoringFileTime(prdName))) {

                eTime=findingEndTime(i);
                int index=checkForProduct(prdName);
                ModelStoringFileTime mmm=arrayStore.get(index);
               // Log.v("jsonarray_putting",mmm.getTiming());
                try {
                    JSONArray jj = new JSONArray(mmm.getTiming());
                    JSONArray jk = new JSONArray();
                    JSONObject js = null;
                    for (int k = 0; k < jj.length(); k++) {
                        js = jj.getJSONObject(k);
                        jk.put(js);
                        //Log.v("jsonarray_putting34",jk.toString());
                    }
                    js=new JSONObject();
                    js.put("sT", timevalue);
                    js.put("eT", eTime);
                    jk.put(js);
                   // Log.v("jsonarray_putting",jj.toString()+" jsack_prnt "+jk.toString());
                    mmm.setTiming(jk.toString());
                }catch (Exception e){}

            }
            else{


                     eTime=findingEndTime(i);
                    JSONObject jsonObject=new JSONObject();
                    JSONArray jsonArray=new JSONArray();
                    try {
                        jsonObject.put("sT",timevalue);
                        jsonObject.put("eT",eTime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);

                    arrayStore.add(new ModelStoringFileTime(prdName,jsonArray.toString(),prdddName));
                //}
            }
           // dateVal=mCommonSharedPreference.getValueFromPreferenceFeed("dateVal"+i);

        }

        for(int j=0;j<arrayStore.size();j++){
           // Log.v("Slidename_dummy ",arrayStore.get(j).getFilename()+" timing "+arrayStore.get(j).getTiming());
            if(j==0){
                gettingProductStartEndTime(arrayStore.get(j).getTiming(),j);
                finalPrdNam=arrayStore.get(j).getPrdName();
            }
            else if(finalPrdNam.equalsIgnoreCase(arrayStore.get(j).getPrdName())){

            }
            else{
              //  Log.v("getting_prd_dif","inside");
                String time=gettingProductStartEndTime(arrayStore.get(j).getTiming(),j);
                prdDetail.add(new ModelStoringFileTime(arrayStore.get(j-1).getPrdName(),time,""));
                finalPrdNam=arrayStore.get(j).getPrdName();

            }
        }

        String time=gettingProductStartEndTime(arrayStore.get(arrayStore.size()-1).getTiming(),arrayStore.size()-1);
        prdDetail.add(new ModelStoringFileTime(arrayStore.get(arrayStore.size()-1).getPrdName(),time,""));

      *//*  for(int k=0;k<prdDetail.size();k++){
            Log.v("printing_prd_time",prdDetail.get(k).getFilename()+" timees "+prdDetail.get(k).getTiming());
        }*//*
    }

    public String findingEndTime(int k){
        if(checkForLastSlide(k)){
            return defaulttime;
        }
        else{
            int j=k+1;

            String eTime=mCommonSharedPreference.getValueFromPreferenceFeed("timeVal"+j);
            return eTime;
        }
    }
    public boolean checkForLastSlide(int k){
        if(k==val-1)
            return true;
        else
            return false;

    }

    public int checkForProduct(String slidename){
        for(int i=0;i<arrayStore.size();i++){
            if(arrayStore.get(i).getFilename().equals(slidename)){
                return i;
            }
        }
        return -1;
    }

    public String gettingProductStartEndTime(String jsonvalue,int i){
        String finalTime = null;
        ModelStoringFileTime mm,mm1;
        try {
            JSONArray json=new JSONArray(jsonvalue);
        if(i!=0){
            if(i==arrayStore.size()-1){
                mm1=arrayStore.get(i);
            }
            else
            mm1=arrayStore.get(i-1);
            json=new JSONArray(mm1.getTiming());
            JSONObject jj=json.getJSONObject(0);
                Log.v("first_value_time",jj.getString("eT"));
                endT=jj.getString("eT");
        }

        finalTime=startT+" "+endT;

        if(i!=arrayStore.size()-1) {
            mm=arrayStore.get(i);
            json=new JSONArray(mm.getTiming());
            JSONObject jj = json.getJSONObject(0);
            Log.v("last_value_time", jj.getString("sT"));
            startT = jj.getString("sT");
        }
            return finalTime;

        } catch (JSONException e) {
            e.printStackTrace();
        }
       return finalTime;
    }
*/
    }

    public void startAlert(long val){
       // EditText text = findViewById(R.id.time);
        //int i = Integer.parseInt(text.getText().toString());
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, val, pendingIntent);
        Toast.makeText(this, "Alarm set in " + val + " seconds",Toast.LENGTH_LONG).show();
    }
    //new DummyTask().execute();

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

                StringBuffer sb = new StringBuffer();

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

    public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

        String googlePlacesData;
        GoogleMap mMap;
        String url;

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d("GetNearbyPlacesData", "doInBackground entered");
               // mMap = (GoogleMap) params[0];
                url = (String) params[1];
                DownloadUrl downloadUrl = new DownloadUrl();
                googlePlacesData = downloadUrl.readUrl(url);
                Log.d("GooglePlacesReadTask", "doInBackground Exit");
            } catch (Exception e) {
                Log.d("GooglePlacesReadTask", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("GooglePlacesReadTask", "onPostExecute Entered");
            List<HashMap<String, String>> nearbyPlacesList = null;
           /* DataParser dataParser = new DataParser();
            nearbyPlacesList =  dataParser.parse(result);
            ShowNearbyPlaces(nearbyPlacesList);*/
            Log.d("GooglePlacesReadTask", "onPostExecute Exit"+nearbyPlacesList);
        }

        private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
            for (int i = 0; i < nearbyPlacesList.size(); i++) {
                Log.d("onPostExecute","Entered into showing locations");
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName + " : " + vicinity);
                mMap.addMarker(markerOptions);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            }
        }
    }

    class DummyTask extends AsyncTask<Void,Void,Void>{
        String googlePlacesData;
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
            Log.v("Print_the_statement","last "+"times"+googlePlacesData);

            getDrDetail(googlePlacesData);
        }
    }

    public void getDrDetail(String placesdata){
        try {
            JSONObject jsonObject=new JSONObject(placesdata);
            JSONArray jsonArray=jsonObject.getJSONArray("results");

            for(int i=0;i<jsonArray.length();i++){
                JSONObject json=jsonArray.getJSONObject(i);
                String lat=json.getJSONObject("geometry").getJSONObject("location").getString("lat");
                String lng=json.getJSONObject("geometry").getJSONObject("location").getString("lng");
                String name=json.getString("name");
                String vicinity=json.getString("vicinity");
                try{
                    JSONArray jsonA=json.getJSONArray("photos");
                    JSONObject jo=jsonA.getJSONObject(0);
                    JSONArray ja=jo.getJSONArray("html_attributions");
                    //JSONObject jo1=ja.getJSONObject(0);
                    String photoo=ja.getString(0);
                    Log.v("direction_latt",name+"phototss"+photoo);
                }catch (JSONException e){}


                Log.v("direction_latt",name+"phototss");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
   /* @Override
    public boolean dispatchTouchEvent(MotionEvent e)
    {
        super.dispatchTouchEvent(e);
        return gestureDetector.onTouchEvent(e);
    }
*/
   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
// TODO Auto-generated method stub
        return gestureDetector.onTouchEvent(event);
    }*/

   public void setAllData(){
       yValueGroup1.add(new BarEntry(1, (float) 2));
       yValueGroup2.add(new BarEntry(1, (float) 2));

       yValueGroup1.add(new BarEntry(2, (float) 4));
       yValueGroup2.add(new BarEntry(2, (float) 4));

       yValueGroup1.add(new BarEntry(3, (float) 6));
       yValueGroup2.add(new BarEntry(3, (float) 6));

       yValueGroup1.add(new BarEntry(4, (float) 8));
       yValueGroup2.add(new BarEntry(4, (float) 8));

       yValueGroup1.add(new BarEntry(5, (float) 3));
       yValueGroup2.add(new BarEntry(5, (float) 3));

       yValueGroup1.add(new BarEntry(6, (float) 7));
       yValueGroup2.add(new BarEntry(6, (float) 7));

       yValueGroup1.add(new BarEntry(7, (float) 2));
       yValueGroup2.add(new BarEntry(7, (float) 2));

       yValueGroup1.add(new BarEntry(8, (float) 4));
       yValueGroup2.add(new BarEntry(8, (float) 4));

       yValueGroup1.add(new BarEntry(9, (float) 5));
       yValueGroup2.add(new BarEntry(9, (float) 5));

       yValueGroup1.add(new BarEntry(10, (float) 9));
       yValueGroup2.add(new BarEntry(10, (float) 9));

       yValueGroup1.add(new BarEntry(11, (float) 2));
       yValueGroup2.add(new BarEntry(11, (float) 2));

       yValueGroup1.add(new BarEntry(12, (float) 7));
       yValueGroup2.add(new BarEntry(12, (float) 7));

   }

   public ArrayList<BarEntry> allVal(){
       ArrayList<BarEntry> xx=new ArrayList<>();
       xx.add(new BarEntry(0,new float[]{2,4.5f,3}));
       xx.add(new BarEntry(1,new float[]{2,9f,6.3f}));
       xx.add(new BarEntry(3,new float[]{2,6f,7}));
       return xx;
   }

   public ArrayList<BarEntry> allyval(){
       ArrayList<BarEntry> xx=new ArrayList<>();
       xx.add(new BarEntry(0,(float)7));
       xx.add(new BarEntry(1,(float)3));
       xx.add(new BarEntry(3,(float)5));
       return xx;
   }

    public void UpperGraph(){
        barWidth = 0.3f;
        barSpace = 0f;
        groupSpace = 0.4f;


        chart.setDescription(null);
        chart.setPinchZoom(true);
        chart.setScaleEnabled(true);
        /*chart.setScaleEnabled(false);*/
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(true);

        int groupCount = 3;

        ArrayList xVals = new ArrayList();

        xVals.add("");
        xVals.add("Jan");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Apr");
        xVals.add("May");
        xVals.add("Jun");
        xVals.add("");

        ArrayList yVals1 = new ArrayList();
        ArrayList yVals2 = new ArrayList();
        ArrayList yVals3 = new ArrayList();

        yVals1.add(new BarEntry(1, (float) 1));
        yVals2.add(new BarEntry(1, (float) 2));
        yVals1.add(new BarEntry(2, (float) 3));
        yVals2.add(new BarEntry(2, (float) 4));
        yVals1.add(new BarEntry(3, (float) 5));
        yVals2.add(new BarEntry(3, (float) 6));
        yVals1.add(new BarEntry(4, (float) 7));
        yVals2.add(new BarEntry(4, (float) 8));
        yVals1.add(new BarEntry(5, (float) 9));
        yVals2.add(new BarEntry(5, (float) 10));
        yVals1.add(new BarEntry(6, (float) 11));
        yVals2.add(new BarEntry(6, (float) 12));
        yVals3.add(new BarEntry(1, (float) 2));
        yVals3.add(new BarEntry(2, (float) 2));
        yVals3.add(new BarEntry(3, (float) 5));
        yVals3.add(new BarEntry(4, (float) 2));
        yVals3.add(new BarEntry(5, (float) 7));
        yVals3.add(new BarEntry(6, (float) 8));

        BarDataSet set1, set2,set3;
        set1 = new BarDataSet(yVals1, "YR");
        set1.setColor(Color.RED);
        set2 = new BarDataSet(yVals2, "MNTH");
        set2.setColor(Color.BLUE);
        set3 = new BarDataSet(yVals3, "extra");
        set3.setColor(Color.YELLOW);
        BarData data = new BarData(set1, set2,set3);
        data.setValueFormatter(new LargeValueFormatter());
        chart.setData(data);
        chart.getBarData().setBarWidth(barWidth);
        chart.getXAxis().setAxisMinimum(0);
        float defaultBarWidth = -1;
      // groupCount = xVals.size();

       /* defaultBarWidth = (1 - groupSpace)/yVals1.size()  - barSpace;
        if(defaultBarWidth >=0) {
            data.setBarWidth(defaultBarWidth);
        }*/

        Log.v("printing_chart_axis",chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount+"");
        chart.getXAxis().setAxisMaximum(0+chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(0, groupSpace, barSpace);
        chart.getData().setHighlightEnabled(false);
        chart.invalidate();
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        //X-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(8);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
//Y-axis
        chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public static class MyBroadcastReceiver extends BroadcastReceiver {
        // MediaPlayer mp;
        @Override
        public void onReceive(Context context, Intent intent) {
            /*mp=MediaPlayer.create(context, R.raw.alarm);
            mp.start();*/
            Log.v("alarm_are_started","here");
            Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
        }
    }
    }
