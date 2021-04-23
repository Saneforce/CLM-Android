package saneforce.sanclm.adapter_class;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ViewTagModel;
import saneforce.sanclm.adapter_interfaces.FindRouteListener;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.DirectionsJSONParser;
import saneforce.sanclm.util.ProductChangeListener;
import saneforce.sanclm.util.UpdateUi;

public class nearMeAdapter extends RecyclerView.Adapter<nearMeAdapter.MyViewHolder>{


    Context context;
    ArrayList<ViewTagModel> array = new ArrayList<>();
    private LayoutInflater mInflater;
    String laty,lngy;
    CommonSharedPreference commonSharedPreference;
    static FindRouteListener findRouteListener;
    public static String MY_API_KEY="AIzaSyD4E3jMHLmeOW9HFfyIXkk-2aFElUhKzUw";
    static ProductChangeListener productChangeListener;
    static UpdateUi updateUi;


    public nearMeAdapter(Activity context, ArrayList<ViewTagModel> array,String laty,String lngy) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.array=array;
        this.laty=laty;
        this.lngy=lngy;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.row_item_nearme, parent, false);
        commonSharedPreference=new CommonSharedPreference(context);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txt_dr.setText(array.get(position).getName());
        holder.txt_add.setText(array.get(position).getAddr());
        holder.btn_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i=new Intent(context, NearWebActivity.class);
                i.putExtra("slat",laty);
                i.putExtra("slng",lngy);
                i.putExtra("dlat",array.get(position).getLat());
                i.putExtra("dlng",array.get(position).getLng());
                context.startActivity(i);*/
               Log.v("start_uri_place",laty+" lngy "+lngy+" end_url "+array.get(position).getLat()+" lngy "+array.get(position).getLng());
               LatLng origin=new LatLng(Double.parseDouble(laty),Double.parseDouble(lngy));
               LatLng dest=new LatLng(Double.parseDouble(array.get(position).getLat()),Double.parseDouble(array.get(position).getLng()));

                String url = getDirectionsUrl(origin, dest);
                Log.v("btn_route_click",url);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }
        });

        holder.btn_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonSharedPreference.setValueToPreference("visit_dr","true");
                commonSharedPreference.setValueToPreference("drnames",array.get(position).getName());
                commonSharedPreference.setValueToPreference("drcodes",array.get(position).getCode());
                updateUi.updatingui();
                /*Intent i=new Intent(context, DCRCallSelectionActivity.class);
                context.startActivity(i);*/
            }
        });
        holder.rl_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productChangeListener.checkPosition(position);
            }
        });
        //http://maps.google.com/maps?saddr=43.0054446,-87.9678884&daddr=42.9257104,-88.0508355

        /*WebView webview = (WebView) findViewById(R.id.webView1);
webview.setWebViewClient(new WebViewClient());
webview.getSettings().setJavaScriptEnabled(true);
webview.loadUrl("http://maps.google.com/maps?" + "saddr=43.0054446,-87.9678884" + "&daddr=42.9257104,-88.0508355");*/
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img,img_selection;
        TextView txt_dr,txt_add;
        Button btn_route,btn_visit;
        RelativeLayout rl_popup;
        public MyViewHolder(View itemView) {
            super(itemView);

            txt_dr=(TextView)itemView.findViewById(R.id.txt_dr);
            txt_add=(TextView)itemView.findViewById(R.id.txt_add);
            btn_route=(Button)itemView.findViewById(R.id.btn_route);
            btn_visit=(Button)itemView.findViewById(R.id.btn_visit);
            rl_popup=(RelativeLayout)itemView.findViewById(R.id.rl_popup);
        }
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+"&key=" + MY_API_KEY;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String,String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String,String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String,String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String,String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                Log.v("btn_route_click1234",points.size()+"");
                findRouteListener.findRoute(points);
               /* lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);*/

            }

// Drawing polyline in the Google Map for the i-th route
            //mMap.addPolyline(lineOptions);
        }
    }

    private class DownloadTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("btn_route_click12",result);
            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    public static void bindFindListener(FindRouteListener findRouteListener1){
        findRouteListener=findRouteListener1;
    }

    public static void bindListenerForPosition(ProductChangeListener prodcut){
        productChangeListener=prodcut;
    }
    public static void bindListenerForSwift(UpdateUi up){
        updateUi=up;
    }
}
