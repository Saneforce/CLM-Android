package saneforce.sanclm.adapter_class;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;

import saneforce.sanclm.R;
import saneforce.sanclm.activities.FeedbackActivity;
import saneforce.sanclm.activities.Model.FeedbackProductDetail;
import saneforce.sanclm.activities.Model.PopFeed;
import saneforce.sanclm.activities.Model.StoreImageTypeUrl;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtilsMethods;
import saneforce.sanclm.sqlite.DataBaseHandler;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class FeedProductAdapter extends BaseAdapter {
    Context context;
    ArrayList<FeedbackProductDetail> product = new ArrayList<>();
    String choosen_item = "";
    CommonUtilsMethods mCommonUtilsMethods;
    CommonSharedPreference mCommonSharedPreference;
    public ArrayList<StoreImageTypeUrl> storeList = new ArrayList<>();
    EditText edit_feed;
    DataBaseHandler dbh;
    String prdNm = "";
    int prdCount = 0;
    boolean editOption = false;
    JSONArray jsonFeed;
    boolean isEmpty;
    ListView list_slide;
     Dialog dialog;

    public FeedProductAdapter(FeedbackActivity context, final ArrayList<FeedbackProductDetail> product) {
        this.context = context;
        this.product = product;
        mCommonSharedPreference = new CommonSharedPreference(context);
        mCommonUtilsMethods = new CommonUtilsMethods(this.context);
        dbh = new DataBaseHandler(this.context);
        File ff = new File("");
        int val = mCommonSharedPreference.getValueFromPreferenceFeed("timeCount", 0);
        Log.v("time_count_adapter", String.valueOf(val));


        String slideFeedback = mCommonSharedPreference.getValueFromPreference("slide_feed");
        try {
            jsonFeed = new JSONArray(slideFeedback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ArrayList<StoreImageTypeUrl> tempArray = new ArrayList<>();
/*
        for(int i=0;i<val;i++) {
            String timevalue = mCommonSharedPreference.getValueFromPreferenceFeed("timeVal" + i);
            String prdName = mCommonSharedPreference.getValueFromPreferenceFeed("brd_nam" + i);
            dbh.open();
            Cursor cur=dbh.select_feedback_list(prdName);
            if(cur.getCount()>0) {
                cur.moveToFirst();
                Log.v("cursor_move_frst",cur.getString(1));
                editOption = true;
                i=val;
            }
            else{
                editOption=false;
            }
            String slidenam = mCommonSharedPreference.getValueFromPreferenceFeed("slide_nam" + i);
            if(TextUtils.isEmpty(slidenam))
                editOption=true;
            String slidetyp = mCommonSharedPreference.getValueFromPreferenceFeed("slide_typ" + i);
            String slideur = mCommonSharedPreference.getValueFromPreferenceFeed("slide_url" + i);
            Log.v("time_ValuePadap",timevalue+"edutOption"+editOption);
            tempArray.add(new StoreImageTypeUrl(slidenam,slidetyp,slideur,prdName,timevalue));


            }
*/

        if (editOption == false) /*{
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String endTim = "";
                        for (int i = 0; i < tempArray.size(); i++) {
                            String sTime = "", eTime = "";
                            StoreImageTypeUrl mm = tempArray.get(i);
                            StoreImageTypeUrl mm2 = null;
                            boolean checkProduct=false;
                            for (int k = 0; k < storeList.size(); k++) {

                                if (storeList.get(k).getBrdName().equalsIgnoreCase(prdNm))
                                    checkProduct=true;
                            }
                            Log.v("checkProductDifference",checkProduct+"");
                            if (TextUtils.isEmpty(prdNm) || checkProduct==false) {

                                if (i != (tempArray.size() - 1)) {
                                    mm2 = tempArray.get(i + 1);
                                    sTime = mm.getTiming();
                                    eTime = mm2.getTiming();
                                    Log.v("startTime", sTime + " endTime " + eTime);

                                } else {
                                    sTime = mm.getTiming();
                                    FeedbackProductDetail pp = null;
                                    for (int g = 0; g < product.size(); g++) {
                                        if (product.get(g).getPrdNAme().equalsIgnoreCase(prdNm))
                                            pp = product.get(g);
                                    }
                                    if(i==0){
                                        pp = product.get(0);
                                    }

                                    eTime = pp.getSt_end_time().substring(9);
                                }

                                prdNm = mm.getBrdName();
                                prdCount++;

                                JSONArray jsonArray = new JSONArray();
                                JSONObject js = new JSONObject();

                                try {

                                    Log.v("Start_Time", sTime + " endTime " + eTime);
                                    js.put("ST", sTime);
                                    js.put("ET", eTime);
                                    jsonArray.put(js);
                                    mm.setRemTime(jsonArray.toString());
                                    storeList.add(new StoreImageTypeUrl(mm.getSlideNam(), mm.getSlideTyp(), mm.getSlideUrl(), "", "", jsonArray.toString(), prdNm, false));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                StoreImageTypeUrl sT = null;
                                if (storeList.contains(new StoreImageTypeUrl(mm.getSlideNam()))) {
                                    Log.v("storeList_sae", "patterns_are_here");

                                    for (int k = 0; k < storeList.size(); k++) {

                                        if (storeList.get(k).getSlideNam().equalsIgnoreCase(mm.getSlideNam()))
                                            sT = storeList.get(k);
                                    }
                                    if (i != (tempArray.size() - 1)) {
                                        mm2 = tempArray.get(i + 1);
                                        sTime = mm.getTiming();
                                        eTime = mm2.getTiming();

                                    } else {
                                        sTime = mm.getTiming();
                                        FeedbackProductDetail pp = null;
                                        for (int g = 0; g < product.size(); g++) {
                                            if (product.get(g).getPrdNAme().equalsIgnoreCase(prdNm))
                                                pp = product.get(g);
                                        }
                                        endTim = pp.getSt_end_time().substring(9);
                                        eTime = pp.getSt_end_time().substring(9);
                                    }
                                    Log.v("storeList_checking", "contain");
                                    JSONArray jja = new JSONArray();
                                    try {
                                        JSONArray jA = new JSONArray(sT.getRemTime().toString());
                                        JSONObject jss = null;
                                        for (int k = 0; k < jA.length(); k++) {
                                            jss = jA.getJSONObject(k);
                                            jja.put(jss);
                                            if (jA.length() - 1 == k) {
                                                jss.put("ST", sTime);
                                                jss.put("ET", eTime);
                                                jja.put(jss);
                                            }

                                        }
                                        sT.setRemTime(jja.toString());
                                    } catch (Exception e) {

                                    }

                                } else {
                                    Log.v("storeList_sae", "patterns_are_not_here");
                                    if (i != (tempArray.size() - 1)) {
                                        mm2 = tempArray.get(i + 1);
                                        sTime = mm.getTiming();
                                        eTime = mm2.getTiming();

                                    } else {
                                        sTime = mm.getTiming();
                                        FeedbackProductDetail pp = null;
                                        for (int g = 0; g < product.size(); g++) {
                                            if (product.get(g).getPrdNAme().equalsIgnoreCase(prdNm))
                                                pp = product.get(g);
                                        }
                                        endTim = pp.getSt_end_time().substring(9);
                                        eTime = pp.getSt_end_time().substring(9);
                                    }

                                    prdNm = mm.getBrdName();
                                    prdCount++;

                                    JSONArray jsonArray = new JSONArray();
                                    JSONObject js = new JSONObject();

                                    try {

                                        Log.v("Start_Time", sTime + " endTime " + eTime);
                                        js.put("ST", sTime);
                                        js.put("ET", eTime);
                                        jsonArray.put(js);
                                        mm.setRemTime(jsonArray.toString());
                                        storeList.add(new StoreImageTypeUrl(mm.getSlideNam(), mm.getSlideTyp(), mm.getSlideUrl(), "", "", jsonArray.toString(), prdNm, false));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                        }

                        for (int j = 0; j < storeList.size(); j++) {
                            StoreImageTypeUrl ss = storeList.get(j);
                            Log.v("store_list_pr", storeList.get(j).getSlideNam() + "rem_time " + storeList.get(j).getRemTime());
                            String slideRemark="";
                            for(int k=0;k<jsonFeed.length();k++){
                                try {
                                    JSONObject jsonObject=jsonFeed.getJSONObject(k);

                                    if(jsonObject.getString("id").equalsIgnoreCase(ss.getSlideUrl())){
                                        slideRemark=jsonObject.getString("feedBack");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            dbh.open();
                            dbh.insertFeed(ss.getBrdName(), ss.getSlideNam(), ss.getSlideTyp(), ss.getSlideUrl(), ss.getTiming(), ss.getRemTime(),slideRemark);
                        }
                    }
                }).start();
            }*/

            dbh.close();
    }

    @Override
    public int getCount() {
        return product.size();
    }

    @Override
    public Object getItem(int i) {
        return product.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        String val = mCommonSharedPreference.getValueFromPreference("feed_pob");
        if (val.contains(FeedbackActivity.TypePeople)) {
            view = LayoutInflater.from(context).
                    inflate(R.layout.row_item_feed_product2, viewGroup, false);
        } else {
            view = LayoutInflater.from(context).
                    inflate(R.layout.row_item_feed_product, viewGroup, false);
        }
        TextView prd_nam = (TextView) view.findViewById(R.id.prd_nam);
        TextView prd_time = (TextView) view.findViewById(R.id.prd_time);
        Button img_minus = (Button) view.findViewById(R.id.img_minus);
        ImageView feed_icon = (ImageView) view.findViewById(R.id.feed_icon);
        RatingBar rating = (RatingBar) view.findViewById(R.id.rating);
        EditText edt_sample = (EditText) view.findViewById(R.id.edt_sample);
        if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("sampleMax")) && !mCommonSharedPreference.getValueFromPreference("sampleMax").equalsIgnoreCase("null")) {
            int maxLength = Integer.parseInt(mCommonSharedPreference.getValueFromPreference("sampleMax"));
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            edt_sample.setFilters(FilterArray);
        }

        EditText edt_pob;
        final FeedbackProductDetail mm = product.get(i);
        if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("productFB")) && mCommonSharedPreference.getValueFromPreference("productFB").equalsIgnoreCase("0")) {
            prd_nam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupSpinner(0, mm.getProdFb(), i);
                }
            });
        }

        if (val.contains(FeedbackActivity.TypePeople)) {
            edt_pob = (EditText) view.findViewById(R.id.edt_pob);
         /*   if (!TextUtils.isEmpty(mCommonSharedPreference.getValueFromPreference("pobMax"))) {
                int maxLength = 0;
                maxLength = Integer.parseInt(mCommonSharedPreference.getValueFromPreference("pobMax"));
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                edt_pob.setFilters(FilterArray);
            }*/
            edt_pob.setText(mm.getRxQty());
            edt_pob.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    mm.setRxQty(String.valueOf(editable));

                }
            });

        }

        Log.v("getting_time_productt", mm.getSt_end_time().trim());
        if (!FeedbackActivity.TypePeople.equalsIgnoreCase("D")) {
            if (mCommonSharedPreference.getValueFromPreference("Detailing_chem").equalsIgnoreCase("0") &&
                    FeedbackActivity.TypePeople.equalsIgnoreCase("C")) {
            } else if (mCommonSharedPreference.getValueFromPreference("Detailing_stk").equalsIgnoreCase("0") &&
                    FeedbackActivity.TypePeople.equalsIgnoreCase("S")) {
            } else if (mCommonSharedPreference.getValueFromPreference("Detailing_undr").equalsIgnoreCase("0") &&
                    FeedbackActivity.TypePeople.equalsIgnoreCase("U")) {
            } else {
                prd_time.setVisibility(View.INVISIBLE);
                rating.setVisibility(View.INVISIBLE);
                feed_icon.setVisibility(View.INVISIBLE);
            }
        }
        if (mm.getSt_end_time().trim().contains("00:00:00 00:00:00")) {
            prd_time.setVisibility(View.INVISIBLE);
            rating.setVisibility(View.INVISIBLE);
            feed_icon.setVisibility(View.INVISIBLE);
        }
        prd_nam.setText(mm.getPrdNAme());

        String currentString = mm.getSt_end_time();
        String[] separated = currentString.split(" ");
        String starttm=  separated[0];
        String endtm= separated[1];

        java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm:ss");
        java.util.Date date1 = null;

        try {
            date1 = df.parse(endtm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.util.Date date2 = null;
        try {
            date2 = df.parse(starttm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = date1.getTime() - date2.getTime();
        if(diff>0) {
            // seconds, minutes, hours, years,
            // and days
            long difference_In_Seconds
                    = (diff
                    / 1000)
                    % 60;

            long difference_In_Minutes
                    = (diff
                    / (1000 * 60))
                    % 60;

            long difference_In_Hours
                    = (diff
                    / (1000 * 60 * 60))
                    % 24;


            prd_time.setText(mm.getSt_end_time() + " (" + difference_In_Minutes + ":" + difference_In_Seconds + ")");
            Log.v("duration>>", "" + difference_In_Hours + ":" + difference_In_Minutes + ":" + difference_In_Seconds);

        }else{
            long diff1 = date2.getTime() - date1.getTime();
            long difference_In_Seconds
                    = (diff1
                    / 1000)
                    % 60;

            long difference_In_Minutes
                    = (diff1
                    / (1000 * 60))
                    % 60;

            long difference_In_Hours
                    = (diff1
                    / (1000 * 60 * 60))
                    % 24;


            prd_time.setText(mm.getSt_end_time() + " (" + difference_In_Minutes + ":" + difference_In_Seconds + ")");
            Log.v("duration>>", "" + difference_In_Hours + ":" + difference_In_Minutes + ":" + difference_In_Seconds);
        }
        if (!TextUtils.isEmpty(mm.getRating()))
            rating.setRating(Float.parseFloat(mm.getRating()));
        edt_sample.setText(mm.getSample());


        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("total_feedback_", mm.getSt_end_time());
                if (mm.getSt_end_time().equalsIgnoreCase("00:00:00 00:00:00")) {
                    product.remove(i);
                    notifyDataSetChanged();
                }
            }
        });
        feed_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeList.clear();
               /* String endTime = null;
                if(i==product.size()-1){
                    endTime=mm.getSt_end_time().trim();
                    endTime=endTime.substring(endTime.indexOf(" "));
                    Log.v("passing_endTime11",endTime);
                }
                else{
                    final FeedbackProductDetail mm=product.get(i+1);
                     endTime=mm.getSt_end_time().trim();
                     endTime=endTime.substring(0,endTime.indexOf(" "));

                    Log.v("passing_endTime",endTime);
                }*/

                // Log.v("passing_endTime",endTime);
                popupFeedbackIcon(mm.getPrdNAme(), "", i);
            }
        });

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                //popupRating(v,mm.getPrdNAme(),i);
                mm.setRating(String.valueOf(v));

            }
        });

        edt_sample.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mm.setSample(String.valueOf(editable));

            }
        });

        return view;
    }

    public void popupRating(float v, String prName, int pos) {

        final Dialog dialog = new Dialog(context, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.rating_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final FeedbackProductDetail mm = product.get(pos);
        edit_feed = (EditText) dialog.findViewById(R.id.edit_feed);
        edit_feed.setText(mm.getFeedback());
        final LinearLayout ll_edt_bg = (LinearLayout) dialog.findViewById(R.id.ll_edt_bg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCommonUtilsMethods.hideSoftKeyboard(edit_feed);
            }
        }, 100);

        RatingBar rating = (RatingBar) dialog.findViewById(R.id.rating);
        RelativeLayout rl_feed = (RelativeLayout) dialog.findViewById(R.id.rl_feed);
        ImageView iv_close_popup = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        TextView txt_prd_name = (TextView) dialog.findViewById(R.id.txt_prd_name);
        txt_prd_name.setText(prName);
        rating.setRating(v);

        ll_edt_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommonUtilsMethods.showSoftKeyboard(edit_feed);
            }
        });

        rl_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_feed.setText("");
                String val = popUpAlert();
                edit_feed.setText(val);
            }
        });

        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        edit_feed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mm.setFeedback(String.valueOf(editable));
            }
        });

    }

    public String popUpAlert() {

        final Dialog dialog = new Dialog(context, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_spinner_selection);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);
        final ArrayList<String> list = new ArrayList<>();
        list.add("aaaaaaaaa");
        list.add("aaaaaaaaa");
        list.add("aaaaaaaaa");
        list.add("aaaaaaaaa");
        list.add("aaaaaaaaa");
        list.add("aaaaaaaaa");
        AdapterPopupSpinnerSelection popupAdapter = new AdapterPopupSpinnerSelection(context, list, true);
        popup_list.setAdapter(popupAdapter);
        popupAdapter.notifyDataSetChanged();

        popup_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosen_item = list.get(i);

                edit_feed.setText(list.get(i));

                dialog.dismiss();
            }
        });
        return choosen_item;
    }

    public void popupFeedbackIcon(String prd, String endtime, int pos) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_slide_feed);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final FeedbackProductDetail mm = product.get(pos);

         list_slide = (ListView) dialog.findViewById(R.id.list_slide);
        edit_feed = (EditText) dialog.findViewById(R.id.edit_feed);
        edit_feed.setText(mm.getFeedback());
        LinearLayout ll_edt_bg = (LinearLayout) dialog.findViewById(R.id.ll_edt_bg);
        RelativeLayout rl_feed = (RelativeLayout) dialog.findViewById(R.id.rl_feed);
        TextView txt_prd_name = (TextView) dialog.findViewById(R.id.txt_prd_name);
        txt_prd_name.setText(prd);

        edit_feed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mm.setFeedback(String.valueOf(editable));
            }
        });

        ImageView iv_close_popup = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbh.open();
                for (int k = 0; k < storeList.size(); k++) {
                    if (!TextUtils.isEmpty(storeList.get(k).getSlideFeed())) {
                        dbh.updateSlideFeed(storeList.get(k).getColumnid(), storeList.get(k).getSlideFeed());
                        Log.v("Slide_ffeding", storeList.get(k).getSlideFeed());
                    }
                    if (!TextUtils.isEmpty(storeList.get(k).getRating())) {
                        dbh.updateSlideFeedRate(storeList.get(k).getColumnid(), storeList.get(k).getRating());
                    }
                }

                dialog.dismiss();
            }
        });

        ll_edt_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_feed.requestFocus();
                mCommonUtilsMethods.showSoftKeyboard(edit_feed);
            }
        });
        rl_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpAlert();
            }
        });

        storeList.clear();
        dbh.open();
        Cursor mCursor = dbh.select_feedback_list(mm.getPrdNAme());
        Log.v("Cursor_counttt", String.valueOf(mCursor.getCount()));
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Log.v("Printing_prd_name", mCursor.getString(2) + " url " + mCursor.getString(4));
                storeList.add(new StoreImageTypeUrl(mCursor.getString(2), mCursor.getString(3), mCursor.getString(4), mCursor.getString(6), mCursor.getString(7), mCursor.getString(5), mCursor.getInt(0), mCursor.getString(6)));
            } while (mCursor.moveToNext());

        }
        dbh.close();
        mCursor.close();

        ArrayList<String> slideNm = new ArrayList<>();
/*
        for(int i=0;i<storeList.size();i++){
            StoreImageTypeUrl mm1=storeList.get(i);
            String sT="",eT="";
            try {
                JSONArray jss=new JSONArray(mm1.getRemTime());
                for(int j=0;j<jss.length();j++){

                    JSONObject json=jss.getJSONObject(j);
                    if(j==0)
                        sT=json.getString("ST");
                    if(j==jss.length()-1)
                        eT=json.getString("ET");
                }
                mm1.setTiming(sT+" "+eT);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
*/
        dataselection();

    }
//    private class AsyncTaskExample extends AsyncTask<String, String, String> {
//        final ProgressDialog mProgressDialog = new ProgressDialog(context);
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog.show();
//            mProgressDialog.setMessage("Loading");
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.show();
//        }
//        @Override
//        protected String doInBackground(String... strings) {
//
//            return null;
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            mProgressDialog.dismiss();
//        }
//
//    }

    public void dataselection(){
        Adapter_feedproduct_feedicon adp = new Adapter_feedproduct_feedicon(context, storeList);
        list_slide.setAdapter(adp);
        adp.notifyDataSetChanged();
    }


    private int getCategoryPos(ArrayList<String> arr, String category) {
        return arr.indexOf(category);
    }

    public void popupSpinner(int type, final ArrayList<PopFeed> array_selection, final int pos) {

        final Dialog dialog = new Dialog(context, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_dynamic_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);
        TextView tv_todayplan_popup_head = (TextView) dialog.findViewById(R.id.tv_todayplan_popup_head);
        tv_todayplan_popup_head.setText("Feedback");
        ImageView iv_close_popup = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        Button ok = (Button) dialog.findViewById(R.id.ok);

        if (array_selection.contains(new PopFeed(true))) {
            isEmpty = false;
        } else
            isEmpty = true;

        final AdapterForDynamicSelectionList adapt = new AdapterForDynamicSelectionList(context, array_selection, type);
        popup_list.setAdapter(adapt);
        final SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view.setIconified(false);
                InputMethodManager im = ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(search_view, 0);
            }
        });
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.v("search_view_str", s);
                adapt.getFilter().filter(s);
                return false;
            }
        });

        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty) {
                    if (array_selection.contains(new PopFeed(true))) {
                        for (int i = 0; i < array_selection.size(); i++) {
                            PopFeed m = array_selection.get(i);
                            m.setClick(false);
                        }
                    }
                }
                dialog.dismiss();

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array_selection.contains(new PopFeed(true))) {
                    for (int i = 0; i < array_selection.size(); i++) {
                        PopFeed m = array_selection.get(i);
                        if (m.isClick()) {

                            /*i=array_selection.size();
                            adp_view.notifyDataSetChanged();*/
                            break;
                        }
                    }

                } else {
                   /* array_view.get(pos).setValue("");
                    adp_view.notifyDataSetChanged();*/
                }
                dialog.dismiss();

            }
        });

    }
}
