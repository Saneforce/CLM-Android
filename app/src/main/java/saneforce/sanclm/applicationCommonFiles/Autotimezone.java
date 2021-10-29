package saneforce.sanclm.applicationCommonFiles;


import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import saneforce.sanclm.activities.BlockActivity;
import saneforce.sanclm.activities.HomeDashBoard;

public class Autotimezone extends Service {
    boolean isRunning=false;
    static boolean result=false,mReceiversRegistered=false;
    Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        checktimezone();
        return START_STICKY;
    }

    public Autotimezone() {

    }


    public Autotimezone(Context context)
    {
        this.context = context;
        checktimezone();
    }
    public void checktimezone() {

        try {
            if (Settings.Global.getInt(getApplicationContext().getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                result = false;

            } else {
                Intent intent=new Intent(this, BlockActivity.class);
                intent.putExtra("dcrblock","1");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                result = true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }



}
