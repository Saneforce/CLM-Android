package saneforce.sanclm.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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


/***********************************************
 * Created by anartzmugika on 22/6/16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String NOT_CONNECT = "NOT_CONNECT";
    Context mContext;
    CommonSharedPreference mCommonSharedPreference;
    String baseurl;
    DataBaseHandler dbh;
    Api_Interface apiService;



    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mCommonSharedPreference=new CommonSharedPreference(mContext);
        String status = NetworkUtil.getConnectivityStatusString(context);

        Log.e("Receiver ", "" + status);

        if (status.equals(NOT_CONNECT)) {
            Log.e("Receiver ", "not connection");// your code when internet lost

            mCommonSharedPreference.setValueToPreference("net_con","notconnect");


        } else {
            Log.e("Receiver ", "connected to internet");//your code when internet connection come back
            mCommonSharedPreference.setValueToPreference("net_con","connect");

            dbh=new DataBaseHandler(context);
            dbh.open();
            Cursor cur12 = dbh.select_tp_listnew();
            Log.d("kdkk","---"+cur12.getCount());
            if (cur12.getCount() > 0) {
                while (cur12.moveToNext()) {
                    // Log.v("printing_totla_val", cur3.getString(1) + " id_here " + cur3.getInt(0));
                    //if (cur3.getString(2).indexOf("_") != -1) {
                    Log.v("printing_totla_val11", cur12.getString(1) + " id_here " + cur12.getInt(0));
                    finalSubmission1(cur12.getString(1), cur12.getInt(0));
                    //  }
                }
            }

        }
        HomeDashBoard.addLogText(status);

    }

    public void finalSubmission1(String val, final int id){
        baseurl =  mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
        try {
            apiService = RetroClient.getClient(baseurl).create(Api_Interface.class);
            Call<ResponseBody> drDetail;
            drDetail = apiService.svDayTp(val);
            drDetail.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            Log.v("day_save", is.toString());
                            JSONObject js=new JSONObject(is.toString());
                            if(js.getString("success").equalsIgnoreCase("true")) {

                                dbh.delete_tpnew(id);
//                            Cursor cur3 = dbh.select_tp_listnew();
//                            if (cur3.getCount() > 0) {
//                                cur3.moveToFirst();
//                                if (cur3.getString(2).indexOf("_") != -1) {
//                                    Log.v("printing_totla_val", cur3.getString(1) + " id_here " + cur3.getInt(0));
//                                    finalSubmission1(cur3.getString(1), cur3.getInt(0));
//                                }
//                            }
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e){e.printStackTrace();}
    }

}
