package saneforce.sanclm.applicationCommonFiles;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;

import saneforce.sanclm.activities.BlockActivity;

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
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                result = true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }



}