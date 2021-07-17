package saneforce.sanclm.applicationCommonFiles;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import saneforce.sanclm.R;
import saneforce.sanclm.sqlite.DataBaseHandler;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class CommonUtilsMethods {
    Intent intent;
    Activity activity;
    Context context;
    private ArrayList permissionsToRequest;
    DataBaseHandler dbh;

    public CommonUtilsMethods(Activity activity) {
        this.activity = activity;
        dbh=new DataBaseHandler(this.activity);
    }

    public CommonUtilsMethods(Context context) {
        this.context = context;
        dbh=new DataBaseHandler(this.context);
    }
    public static String getWebURLFromSharedPreference(Context mContext) {
        CommonSharedPreference mCommonSharedPreference = new CommonSharedPreference(mContext);
        return mCommonSharedPreference.getValueFromPreference(CommonUtils.TAG_DB_URL);
    }
    public static void showToast(Context mContext,String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }
    public void CommonIntentwithNEwTask(Class classname) {
        intent = new Intent(activity, classname);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public void CommonIntentwithNEwTaskMock(Class classname) {
        intent = new Intent(activity, classname);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public void CommonIntentwithNEwTask_FM(Class classname) {
        intent = new Intent(context, classname);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        context.startActivity(intent);
    }

    public boolean checkForSFcodeBasedValue(String sfcode,int x){
        boolean val=false;
        dbh.open();
        Cursor mcur;
        if(x==0)
         mcur=dbh.select_ClusterList(sfcode);
        else if(x==1)
            mcur=dbh.select_dr_sfcode(sfcode);
        else if(x==2)
            mcur=dbh.select_chem_sfcode(sfcode);
        else if(x==3)
            mcur=dbh.select_joint_sfcode(sfcode);
        else if(x==5)
            mcur=dbh.select_hospitalist(sfcode);
        else
            mcur=dbh.select_TPworktypeList(sfcode);


        if(mcur.getCount()>0)
            val= true;

        return val;
    }

    public static void FullScreencall(Activity activity) {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_FULLSCREEN;;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    public static String getCurrentInstance() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTimeInMillis());
    }
    public static String getCurrentDayInstance() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        return sdf.format(c.getTimeInMillis());
    }
    public static String getCurrentTime(){
        Date currentTime = Calendar.getInstance().getTime();

        Log.v("Printing_current_time", String.valueOf(currentTime.getTime()));
        Log.v("Printing_current_time", String.valueOf(currentTime));
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        String val=sdf.format(currentTime);
        return val;
    }
    public static String getCurrentDate(){
        Date currentTime = Calendar.getInstance().getTime();

        Log.v("Printing_current_time", String.valueOf(currentTime.getTime()));
        Log.v("Printing_current_time", String.valueOf(currentTime));
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String val=sdf.format(currentTime);
        Log.v("Printing_current_date", String.valueOf(val));
        return val;
    }
    public void hideSoftKeyboard(View view) {
        if(this.context==null){
            Log.v("context_hidesoft", String.valueOf(context));
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
    public void showSoftKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view ,
                InputMethodManager.SHOW_IMPLICIT);

    }

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_progress);
        // dialog.setMessage(Message);
        return dialog;
    }
    public static ProgressDialog createProgressDialogDetailing(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
           // dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_progress);
        // dialog.setMessage(Message);
        return dialog;
    }

    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }


    public static String getCurrentDataTime ()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }


    public static void avoidSpinnerDropdownFocus(Spinner spinner) {
        try {
            Field listPopupField = Spinner.class.getDeclaredField("mPopup");
            listPopupField.setAccessible(true);
            Object listPopup = listPopupField.get(spinner);
            if (listPopup instanceof ListPopupWindow) {
                Field popupField = ListPopupWindow.class.getDeclaredField("mPopup");
                popupField.setAccessible(true);
                Object popup = popupField.get((ListPopupWindow) listPopup);
                if (popup instanceof PopupWindow) {
                    ((PopupWindow) popup).setFocusable(false);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }




}
