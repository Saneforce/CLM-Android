package saneforce.sanclm.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import saneforce.sanclm.activities.HomeDashBoard;
import saneforce.sanclm.applicationCommonFiles.CommonSharedPreference;


/***********************************************
 * Created by anartzmugika on 22/6/16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    public static final String NOT_CONNECT = "NOT_CONNECT";
    Context mContext;
    CommonSharedPreference mCommonSharedPreference;

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
        }
        HomeDashBoard.addLogText(status);

    }
}
