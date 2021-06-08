package saneforce.sanclm.adapter_class;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.Model.ModelDynamicView;
import saneforce.sanclm.util.TwoTypeparameter;
import saneforce.sanclm.util.UpdateUi;


public class AdapterDynamicView extends BaseAdapter implements LocationListener  {

    ArrayList<ModelDynamicView> array_lst = new ArrayList<>();
    Context context;
    static TwoTypeparameter twoTypeparameter;
    public int DynamicCount = 0;
    static UpdateUi updateUi;
    String latitude,longitude;
    private int previousLength;
    private boolean backSpace;
    int length = 10;


    public AdapterDynamicView(ArrayList<ModelDynamicView> array_lst, Context context) {
        //super(context,R.layout.row_item_dynamic_view,array_lst);
        this.array_lst = array_lst;
        this.context = context;
    }

    public AdapterDynamicView(ArrayList<ModelDynamicView> array_lst,Context context, String latitude, String longitude) {
        //super(context,R.layout.row_item_dynamic_view,array_lst);
        this.context = context;
        this.array_lst = array_lst;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int getCount() {
        return array_lst.size();
    }

    @Override
    public Object getItem(int position) {
        return array_lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        return inflatView(position,convertView,parent);
    }

    private View inflatView(int position, View convertView, ViewGroup parent) {

        Log.e("position",String.valueOf(position));
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_dynamic_view, parent, false);
        TextView txt_label = (TextView) view.findViewById(R.id.txt_label);
        RelativeLayout rlay_spin = (RelativeLayout) view.findViewById(R.id.rlay_spin);
        RelativeLayout rlay_date = (RelativeLayout) view.findViewById(R.id.rlay_date);
        RelativeLayout rlay_upload = (RelativeLayout) view.findViewById(R.id.rlay_upload);
        RelativeLayout rlay_currency = (RelativeLayout) view.findViewById(R.id.rlay_currency);
        RelativeLayout rlay_spin_fdt = (RelativeLayout) view.findViewById(R.id.rlay_spin_fdt);
        RelativeLayout rlay_spin_tdt = (RelativeLayout) view.findViewById(R.id.rlay_spin_tdt);
        RelativeLayout rlay_head = (RelativeLayout) view.findViewById(R.id.rlay_head);
        TextView spin_txt = (TextView) view.findViewById(R.id.spin_txt);
        TextView spin_txt_fdt = (TextView) view.findViewById(R.id.spin_txt_fdt);
        TextView spin_txt_tdt = (TextView) view.findViewById(R.id.spin_txt_tdt);
        TextView txt_currency = (TextView) view.findViewById(R.id.txt_currency);
        TextView txt_upload = (TextView) view.findViewById(R.id.txt_upload);
        TextView txt_label_header = (TextView) view.findViewById(R.id.txt_label_header);
        EditText edt_field = (EditText) view.findViewById(R.id.edt_field);
        final EditText edt_field_numeric = (EditText) view.findViewById(R.id.edt_field_numeric);
        //  edt_field_numeric.setInputType(InputType.TYPE_CLASS_NUMBER);
        EditText edt_feed = (EditText) view.findViewById(R.id.edt_feed);
        EditText edt_field_currency = (EditText) view.findViewById(R.id.edt_field_currency);
        Button btn_upload = (Button) view.findViewById(R.id.btn_upload);

        RelativeLayout rlay_geo = (RelativeLayout) view.findViewById(R.id.rlay_geo);
        EditText edt_lat = (EditText) view.findViewById(R.id.edt_lat);
        EditText edt_lng = (EditText) view.findViewById(R.id.edt_lng);




        String lat, lng;


        final ModelDynamicView mm = array_lst.get(position);
        rlay_spin.setVisibility(View.GONE);
        edt_field.setVisibility(View.GONE);
        edt_field_numeric.setVisibility(View.GONE);
        edt_feed.setVisibility(View.GONE);
        rlay_date.setVisibility(View.GONE);
        rlay_upload.setVisibility(View.GONE);
        rlay_currency.setVisibility(View.GONE);
        rlay_head.setVisibility(View.GONE);
        rlay_geo.setVisibility(View.GONE);


        if (mm.getViewid().equalsIgnoreCase("0")) {
            rlay_head.setVisibility(View.VISIBLE);
            txt_label_header.setText(mm.getFieldname());
            txt_label.setText("");
        } else {
            if (mm.getMandatory().equalsIgnoreCase("1"))
                txt_label.setText(mm.getFieldname() + "*");
            else
                txt_label.setText(mm.getFieldname());
        }

        if (mm.getViewid().equalsIgnoreCase("1")) {
            edt_field.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mm.getCtrl_para())) {
                int maxLength = Integer.parseInt(mm.getCtrl_para());
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                edt_field.setFilters(FilterArray);
            }
            if (!TextUtils.isEmpty(mm.getValue()))
                edt_field.setText(mm.getValue());
        } else if (mm.getViewid().equalsIgnoreCase("2")) {
            edt_field_numeric.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mm.getCtrl_para())) {
                int maxLength = Integer.parseInt(mm.getCtrl_para());
                InputFilter[] FilterArray = new InputFilter[1];
                Log.d("MaxLength", String.valueOf(maxLength));
                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                edt_field_numeric.setFilters(FilterArray);
            }
            if (!TextUtils.isEmpty(mm.getValue()))
                edt_field_numeric.setText(mm.getValue());
        } else if (mm.getViewid().equalsIgnoreCase("3")) {
            edt_feed.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mm.getCtrl_para())) {
                int maxLength = Integer.parseInt(mm.getCtrl_para());
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                edt_feed.setFilters(FilterArray);

            }
            if (!TextUtils.isEmpty(mm.getValue()))
                edt_feed.setText(mm.getValue());

        } else if (mm.getViewid().equalsIgnoreCase("4") || mm.getViewid().equalsIgnoreCase("8")
                || mm.getViewid().equalsIgnoreCase("6") || mm.getViewid().equalsIgnoreCase("9")
                || mm.getViewid().equalsIgnoreCase("12") || mm.getViewid().equalsIgnoreCase("13")) {
            rlay_spin.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mm.getValue()))
                spin_txt.setText(mm.getValue());
        } else if (mm.getViewid().equalsIgnoreCase("5") || mm.getViewid().equalsIgnoreCase("7")) {
            rlay_date.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mm.getValue()))
                spin_txt_fdt.setText(mm.getValue());
            if (!TextUtils.isEmpty(mm.getTvalue()))
                spin_txt_tdt.setText(mm.getTvalue());
        } else if (mm.getViewid().equalsIgnoreCase("10")) {
            rlay_upload.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(mm.getValue())) {
                txt_upload.setText(mm.getValue());
                DynamicCount = position;
            }
        } else if (mm.getViewid().equalsIgnoreCase("11")) {
            rlay_currency.setVisibility(View.VISIBLE);
            txt_currency.setText(mm.getCtrl_para());
            if (!TextUtils.isEmpty(mm.getValue()))
                edt_field_currency.setText(mm.getValue());
        }else if(mm.getViewid().equalsIgnoreCase("17"))
        {
            rlay_geo.setVisibility(View.VISIBLE);


            mm.setLatValue(latitude);

            mm.setLngValue(longitude);

            edt_lat.setText(mm.getLatValue());
            edt_lng.setText(mm.getLngValue());

        }





      /*  else{
            rlay_spin.setVisibility(View.VISIBLE);
        }*/


        /*else    if(mm.getViewid().equalsIgnoreCase("5") ||  mm.getViewid().equalsIgnoreCase("7")){
            rlay_date.setVisibility(View.VISIBLE);
            Log.v("print_dynamic_count",count+"");
            if(count==1){
                if(!TextUtils.isEmpty(mm.getValue()))
                    spin_txt_fdt.setText(mm.getValue());
                count=0;
            }
        }
        else
            btn_upload.setVisibility(View.VISIBLE);*/


        edt_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = String.valueOf(s);
                mm.setValue(ss);
            }
        });

        edt_field_numeric.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if (after < count) {
//                    backSpace= true;
//                } else {
//                    backSpace= false;
//                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(count==0)
//                {
//edt_field_numeric.setText("");
//                }else {
//
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

//                String ss = "";
                // if (s != null && s.length() > 0) {
//                    if (s.length() > length) {
//                        s.clear();
//                        s.insert(0, ss);
//                    }

                // s.delete(s.length()-1,s.length());


                //  }
                // if (s.length() >= length) s.delete(length, s.length());
                String ss = String.valueOf(s);
                mm.setValue(ss);


//                if(!backSpace)
//                {
//                    String  ss= String.valueOf(s);
//                    mm.setValue(ss);
//                }else
//                {
//                    edt_field_numeric.setText(edt_field_numeric.getText().delete(edt_field_numeric.getText().length() - 1, edt_field_numeric.getText().length()));
//                    edt_field_numeric.setSelection(edt_field_numeric.getText().toString().length());
//                }
            }

        });
        edt_feed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = String.valueOf(s);
                mm.setValue(ss);
                // mm.setValue(latitude + "  " + longitude );
            }
        });
        edt_field_currency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = String.valueOf(s);
                mm.setValue(ss);
            }
        });




//        edt_lat.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String ss = String.valueOf(s);
//                mm.setValue(ss);
//            }
//        });
//
//
//        edt_lng.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String ss = String.valueOf(s);
//                mm.setValue(ss);
//            }
//        });





        rlay_spin_fdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mm.getViewid().equalsIgnoreCase("5"))
                    twoTypeparameter.update(8, position);
                else
                    twoTypeparameter.update(12, position);
            }
        });
        rlay_spin_tdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mm.getViewid().equalsIgnoreCase("5"))
                    twoTypeparameter.update(9, position);
                else
                    twoTypeparameter.update(13, position);
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoTypeparameter.update(5, position);
            }
        });

        return view;
    }

    public static void bindListernerForDateRange(TwoTypeparameter pp) {
        twoTypeparameter = pp;
    }

    public static void bindListernerForUpdate(UpdateUi pp) {
        updateUi = pp;
    }



        @Override
        public void onLocationChanged(Location location) {
           updateLocation(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

//    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        return inflatView(position,convertView,parent);
//    }

    private void getLocation() {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria fineAccuracyCriteria = new Criteria();
        fineAccuracyCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        String preferredProvider = locationManager.getBestProvider(fineAccuracyCriteria, false);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(preferredProvider, 0, 0,this );
        Location location = locationManager.getLastKnownLocation(preferredProvider);
        Log.v("location",location.toString());
        updateLocation(location);

//            if (location != null) {
//                double lat = location.getLatitude();
//                double longi = location.getLongitude();
//                String latitude = String.valueOf(lat);
//                String longitude = String.valueOf(longi);
//                Log.v("lat",latitude);
//                Log.v("lng",longitude);
//
//            } else {
//                Toast.makeText(context, "Unable to find location.", Toast.LENGTH_SHORT).show();
//            }
            //return location;
        }

    private void updateLocation(Location location) {
        if (location == null)
            return;

        // save location details
        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());

        Log.v("lat",lat);
              Log.v("lng",lng);
        //return lat;
    }
}
