package saneforce.sanclm.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.api_Interface.Api_Interface;
import saneforce.sanclm.api_Interface.RetroClient;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;
import saneforce.sanclm.applicationCommonFiles.CommonUtils;
import saneforce.sanclm.sqlite.DataBaseHandler;

public class NetworkReceiver extends BroadcastReceiver {

    CommonSharedPreference commonSharedPreference;
    String baseurl;
    Api_Interface apiService;
    DataBaseHandler dbh;
    HomeDashBoard homeDashBoard;
    static UpdateUi mUpdateUi;

    @Override
    public void onReceive(Context context, Intent intent) {

        commonSharedPreference=new CommonSharedPreference(context);
        Log.v("isOnlineee", String.valueOf(isOnline(context)));
        String status = NetworkUtil.getConnectivityStatusString(context);

        if (status.contains(Constants.NOT_CONNECT)) {
            Log.e("Receiver ", "not connection");// your code when internet lost

            //Toast.makeText(context, "Internet is disconnected...Now in offine mode", Toast.LENGTH_SHORT).show();

        } else {
            dbh=new DataBaseHandler(context);

            baseurl =  commonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
            try {
                apiService = RetroClient.getClient(baseurl).create(Api_Interface.class);
                dbh.open();
                Cursor cur2=dbh.select_MDP();

                Cursor cur1 = dbh.select_json_list();
                if(isOnline(context)) {
                    Log.v("isOnlineee_print", String.valueOf(isOnline(context)));
                    if(cur2.getCount()>0){
                        do{
                            cur2.moveToFirst();
                            sendMDP(cur2.getString(1),cur2.getString(0));
                        }while(false);

                    }
                    else{
                        if (cur1.getCount() > 0) {
                            while (cur1.moveToNext()) {
                                if (cur1.getString(2).indexOf("_") != -1) {
                                    Log.v("printing_totla_val", cur1.getString(1) + " id_here " + cur1.getInt(0));
                                    finalSubmission(cur1.getString(1), cur1.getInt(0));
                                }
                            }
                        }

                    }
                    commonSharedPreference.setValueToPreference("check_online","true");
                } else{
                    commonSharedPreference.setValueToPreference("check_online","false");
                }
            }catch (Exception e){}
        }



        }



    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (null != netInfo) {
                Log.v("inside_checkk","netinfo_null");
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI)
                    return true;

                if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                    return true;
            }
        }
        //should check null because in airplane mode it will be null
        //return (netInfo != null && netInfo.isConnected());
        return false;
    }

    public String sendMDP(String jso, final String id){


        Call<ResponseBody> tdayTP = apiService.SVtodayTP(jso);
        tdayTP.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStreamReader ip=null;
                StringBuilder is=new StringBuilder();
                String line=null;
                try {
                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    Log.v("final_mdp_work", is.toString());
                    dbh.open();
                    dbh.delete_MDP(id);
                    Cursor cur2=dbh.select_MDP();
                    if(cur2.getCount()>0){
                        cur2.moveToFirst();
                        sendMDP(cur2.getString(1),cur2.getString(0));
                    }
                    else{
                        Cursor cur1 = dbh.select_json_list();
                        if (cur1.getCount() > 0) {
                            cur1.moveToFirst();
                            if (cur1.getString(2).indexOf("_") != -1) {
                                Log.v("printing_totla_val", cur1.getString(1) + " id_here " + cur1.getInt(0));
                                finalSubmission(cur1.getString(1), cur1.getInt(0));
                            }

                        }
                    }
                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        return "";
    }

    public void finalSubmission(String val, final int id){
        Call<ResponseBody> query=apiService.finalSubmit(val);
        query.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStreamReader ip=null;
                StringBuilder is=new StringBuilder();
                String line=null;
                try {
                    ip = new InputStreamReader(response.body().byteStream());
                    BufferedReader bf = new BufferedReader(ip);

                    while ((line = bf.readLine()) != null) {
                        is.append(line);
                    }

                    Log.v("final_submit_working",is.toString());
                    JSONObject js=new JSONObject(is.toString());
                    if(js.getString("success").equals("true")){
                        Log.v("final_submit_working","success");
                        dbh.delete_json(id);
                        if(mUpdateUi!=null)
                        mUpdateUi.updatingui();
                        Cursor cur1 = dbh.select_json_list();
                        if (cur1.getCount() > 0) {
                            cur1.moveToFirst();
                                if (cur1.getString(2).indexOf("_") != -1) {
                                    Log.v("printing_totla_val", cur1.getString(1) + " id_here " + cur1.getInt(0));
                                    finalSubmission(cur1.getString(1), cur1.getInt(0));
                                }

                        }
                       // Toast.makeText(FeedbackActivity.this, "Data Submitted Successfully", Toast.LENGTH_LONG).show();
                    }
                    else{
                       // Toast.makeText(FeedbackActivity.this, js.getString("Msg"), Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void bindupdateui(UpdateUi updateUi){
        mUpdateUi=updateUi;
    }

    public String recurFunction(String x,int y){
        if(TextUtils.isEmpty(x))
            return "";
        else
            recurFunction("Hello",9);
        return "";
    }

}
