package saneforce.sanclm.applicationCommonFiles;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {

    private int NOT_CONNECTED = -1;
    private int MOBILE = 0;
    private int WIFI = 1;
    private Context context;

    public NetworkStatus(Context context) {
        this.context = context;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] ni = cm.getAllNetworkInfo();
            if (ni != null) {
                for (int i = 0; i < ni.length; i++) {
                    if (ni[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getConnectionType() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (null != ni) {
                if (ni.getType() == ConnectivityManager.TYPE_MOBILE)
                    return MOBILE;

                if (ni.getType() == ConnectivityManager.TYPE_WIFI)
                    return WIFI;
            }
        }
        return NOT_CONNECTED;
    }

    public String getConnectionString() {
        int type = getConnectionType();
        String status = null;
        if (type == MOBILE) {
            status = "Mobile data enabled";
        } else if (type == WIFI) {
            status = "Wifi enabled";
        } else if (type == NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }
}
